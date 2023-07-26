package com.ssafy.likloud.ui.upload

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingListBinding
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.ui.drawinglist.CommentListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

private const val PICK_IMAGE_REQUEST = 1
private const val TAG = "UploadFragment_싸피"

@AndroidEntryPoint
class UploadFragment :
    BaseFragment<FragmentUploadBinding>(FragmentUploadBinding::bind, R.layout.fragment_upload) {

    private val uploadFragmentViewModel: UploadFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

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
    }

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
                if (!it.value) showCustomToast("권한 허용 필요")
            }
        }

    override fun initListener() {
        binding.layoutAddPhoto.setOnClickListener {
            requestMultiplePermission.launch(uploadFragmentViewModel.permissionList)
            pushSettingDialog()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: destroyed")
        super.onDestroy()
    }


//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//
//        // 선택한 이미지의 Uri를 처리하는 코드를 작성합니다.
//        imageUri?.let { uri ->
//            Glide.with(this)
//                .load(uri)
//                .transform(CenterCrop(), RoundedCorners(20))
//                .into(binding.imageSelectedPhoto)
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.S)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            val imageUri = data.data!!
//
//            //uri를 multipart로 변환한다
//            if (imageUri != null) {
//                uploadFragmentViewModel.photoMultipartBody =
//                    createMultipartFromUri(requireContext(), imageUri)!!
//            }
//            Log.d(TAG, "onActivityResult: image binding")
//
//            // 선택한 이미지의 Uri를 처리하는 코드를 작성합니다.
//            Glide.with(this)
//                .load(imageUri)
//                .transform(CenterCrop(), RoundedCorners(20))
//                .into(binding.imageSelectedPhoto)
//        }
//    }



    private fun pushSettingDialog() {
        val dialog = CameraDialog(
            clickGallery ={
                openGallery()
            }
            ,
            clickCamera = {
                openCamera()
            },
        )
        dialog.show(childFragmentManager, TAG)
    }

    private fun openCamera() {

    }

//    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        // 결과를 처리하는 코드를 작성합니다.
//        // uri를 multipart로 변환한다
//        if (uri != null) {
//            uploadFragmentViewModel.photoMultipartBody =
//                createMultipartFromUri(requireContext(), uri)!!
//        }
//
//        Log.d(TAG, "onActivityResult: image binding")
//
//        // 선택한 이미지의 Uri를 처리하는 코드를 작성합니다.
//        Glide.with(this)
//            .load(uri)
//            .transform(CenterCrop(), RoundedCorners(20))
//            .into(binding.imageSelectedPhoto)
//    }

//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//
//    }


    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // 결과를 처리하는 코드를 작성합니다.
        // uri를 multipart로 변환한다
        if (uri != null) {
            uploadFragmentViewModel.photoMultipartBody =
                createMultipartFromUri(requireContext(), uri)!!
            Log.d(TAG, "onActivityResult: image binding")
        }



        // 선택한 이미지의 Uri를 처리하는 코드를 작성합니다.
        Glide.with(this)
            .load(uri)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(binding.imageSelectedPhoto)
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }


    fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        val file: File? = getFileFromUri(context, uri)
        if (file == null) {
            // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
            return null
        }

        val requestFile: RequestBody = createRequestBodyFromFile(file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val filePath = uriToFilePath(context, uri)
        return if (filePath != null) File(filePath) else null
    }

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

    private fun createRequestBodyFromFile(file: File): RequestBody {
        val MEDIA_TYPE_IMAGE = "image/*".toMediaTypeOrNull()
        val inputStream: InputStream = FileInputStream(file)
        val byteArray = inputStream.readBytes()
        return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
    }
}