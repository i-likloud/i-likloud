package com.ssafy.likloud.ui.upload

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentUploadBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "UploadFragment_싸피"

@AndroidEntryPoint
class UploadFragment :
    BaseFragment<FragmentUploadBinding>(FragmentUploadBinding::bind, R.layout.fragment_upload) {

    private val uploadFragmentViewModel: UploadFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    lateinit var currentPhotoPath: String
    lateinit var photoUri: Uri
    lateinit var file: File

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        viewLifecycleOwner.lifecycleScope.launch {
            uploadFragmentViewModel.isPhotoMultipartCreated.observe(requireActivity()) {
                if (it == true) uploadFragmentViewModel.photoMultipartBody.value?.let { multipart ->
                    uploadFragmentViewModel.sendMultipart(
                        multipart
                    )
                }
            }
        }
    }

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
            }
        }

    override fun initListener() {
        binding.layoutAddPhoto.setOnClickListener {
            requestMultiplePermission.launch(uploadFragmentViewModel.permissionList)
            invokeCameraDialog()
        }

        binding.buttonChoose.setOnClickListener {
            if(uploadFragmentViewModel.photoMultipartBody.value!=null){

            }
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: destroyed")
        super.onDestroy()
    }


    /**
     * 카메라, 갤러리 모달창을 띄웁니다.
     */
    private fun invokeCameraDialog() {
        val dialog = CameraDialog(
            clickGallery = {
                openGallery()
            },
            clickCamera = {
                openCamera()
            },
        )
        dialog.show(childFragmentManager, TAG)
    }

    private fun invokeAICheckingDialog(){
        val dialog = AICheckingDialog()
        dialog.show(childFragmentManager,TAG)

    }

    /**
     * AI 인식 실패시 모달창을 띄웁니다.
     */
    private fun invokeNotCloudDialog() {
        val dialog = NotCloudDialog(
            clickTakePhotoAgain = {
                invokeCameraDialog()
            }
        )
        dialog.show(childFragmentManager, TAG)
    }

    /**
     * 카메라 앱을 띄워 사진을 받아옵니다.
     */
    private fun openCamera() {
        file = createImageFile()
        //AndroidMenifest에 설정된 URI와 동일한 값으로 설정한다.
        photoUri =
            FileProvider.getUriForFile(requireContext(), "com.ssafy.likloud.fileprovider", file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraAcitityResult.launch(intent) //카메라 앱을 실행 한 후 결과를 받기 위해서 launch
    }


    val cameraAcitityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                var bitmap: Bitmap

                if (Build.VERSION.SDK_INT >= 29) {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(
                        requireContext().contentResolver,
                        Uri.fromFile(file)
                    )

                    bitmap = ImageDecoder.decodeBitmap(source)

                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        Uri.fromFile(file)
                    )
                }

                binding.imageSelectedPhoto.setImageBitmap(bitmap)
                binding.imageSelectedPhoto.setBackgroundResource(R.drawable.frame_rounded_border_transparent_radius20)
                uploadFragmentViewModel.setMultipart(
                    createMultipartFromUri(
                        requireContext(),
                        Uri.parse(
                            saveImageToGallery(
                                bitmap = bitmap,
                                context = requireContext(),
                                displayName = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
                            )
                        )
                    )!!
                )
            }
        }


    /**
     * 카메라에서 찍은 사진을 갤러리에 저장합니다.
     */
    fun saveImageToGallery(context: Context, bitmap: Bitmap, displayName: String): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }

        var outputStream: OutputStream? = null
        try {
            val collection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val imageUri = contentResolver.insert(collection, contentValues)
            if (imageUri != null) {
                outputStream = contentResolver.openOutputStream(imageUri)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                return imageUri.toString()
            }
        } catch (e: Exception) {
            // 저장 실패 시 예외 처리
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
        return null
    }

    /**
     * 카메라로 찍은 사진을 사진파일로 만듭니다.
     * openCamera()로 결과를 반환합니다.
     */
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path
            currentPhotoPath = absolutePath
        }
    }


    /**
     * createMultipartFromUri로 갤러리에서 받아온 사진 multipart를 저장하고 사진을 뷰 바인딩합니다.
     */
    private val galleryActivityResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // 결과를 처리하는 코드를 작성합니다.
            // uri를 multipart로 변환한다
            if (uri != null) {
                uploadFragmentViewModel.setMultipart(
                    createMultipartFromUri(
                        requireContext(),
                        uri
                    )!!
                )
            }


            // 선택한 이미지의 Uri를 처리하는 코드를 작성합니다.
            Glide.with(this)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(binding.imageSelectedPhoto)
        }

    /**
     * gallery ActivityForResult를 실행합니다.
     */
    private fun openGallery() {
        galleryActivityResult.launch("image/*")
    }

    /**
     * uri로 multipart 객체를 만듭니다.
     */
    fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        val file: File? = getFileFromUri(context, uri)
        if (file == null) {
            // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
            return null
        }

        val requestFile: RequestBody = createRequestBodyFromFile(file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    /**
     * uri로 사진 파일을 가져옵니다
     * createMultipartFromUri로 결과값을 반환합니다
     */
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val filePath = uriToFilePath(context, uri)
        return if (filePath != null) File(filePath) else null
    }

    /**
     * 만들어진 uri를 파일로 변환합니다
     */
    @SuppressLint("Range")
    private fun uriToFilePath(context: Context, uri: Uri): String? {
        lateinit var filePath: String
        context.contentResolver.query(uri, null, null, null, null).use { cursor ->
            cursor?.let {
                if (it.moveToFirst()) {
                    val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    val file = File(context.cacheDir, displayName)
                    try {
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val outputStream = FileOutputStream(file)
                        inputStream?.copyTo(outputStream)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    filePath = file.absolutePath
                }
            }
        }
        return filePath
    }

    /**
     * 저장된 사진 파일의 body를 가져옵니다
     */
    private fun createRequestBodyFromFile(file: File): RequestBody {
        val MEDIA_TYPE_IMAGE = "image/*".toMediaTypeOrNull()
        val inputStream: InputStream = FileInputStream(file)
        val byteArray = inputStream.readBytes()
        return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
    }
}