package com.ssafy.likloud.ui.upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.navercorp.nid.NaverIdLoginSDK
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject
import com.ssafy.likloud.util.createMultipartFromUri
import com.ssafy.likloud.util.saveImageToGallery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "UploadFragment_싸피"

@AndroidEntryPoint
class UploadFragment :
    BaseFragment<FragmentUploadBinding>(FragmentUploadBinding::bind, R.layout.fragment_upload) {

    private val uploadFragmentViewModel: UploadFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var aiCheckingDialog: AICheckingDialog
    private lateinit var currentPhotoPath: String
    private lateinit var file: File


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        /**
         * 구름인지 아닌지를 observe하여 구름이면 화면이동, 아니면 dialog를 띄웁니다.
         */
        viewLifecycleOwner.lifecycleScope.launch {
            uploadFragmentViewModel.isPhotoMultipartValidated.collectLatest {
                Log.d(TAG, "onViewCreated: observed!")
                if (it == true) {
                    aiCheckingDialog.dismiss()
                    showSnackbar(binding.root, "info", "선택한 사진은 구름이에요!")
                    uploadFragmentViewModel.uploadPhotoUrl.value?.let { url ->
                        mainActivityViewModel.setUploadingPhotoUrl(url)
                    }
                    uploadFragmentViewModel.uploadPhotoId.value?.let { id ->
                        mainActivityViewModel.setUploadingPhotoId(id)
                    }
                    findNavController().navigate(R.id.action_homeFragment_to_afterCloudValidFragment)
                    this.cancel()

                } else {
                    aiCheckingDialog.dismiss()
                    invokeNotCloudDialog()
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
        binding.layoutUploadFragment.setOnClickListener {

        }

        binding.layoutAddPhoto.setOnClickListener {
            requestMultiplePermission.launch(uploadFragmentViewModel.permissionList)
            invokeCameraDialog()
        }

        binding.buttonChoose.setOnClickListener {
            if (uploadFragmentViewModel.photoMultipartBody.value != null) {
                invokeAICheckingDialog()
                BitmapCanvasObject.clearAllDrawingPoints()
                uploadFragmentViewModel.sendMultipart(uploadFragmentViewModel.photoMultipartBody.value!!)
            } else {
                showSnackbar(binding.root, "fail", getString(R.string.alert_add_photo))
            }
        }
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

    private fun invokeAICheckingDialog() {
        aiCheckingDialog = AICheckingDialog()
        aiCheckingDialog.show(childFragmentManager, TAG)
    }

    /**
     * AI 인식 실패시 모달창을 띄웁니다.
     */
    private fun invokeNotCloudDialog() {
        val dialog = NotCloudDialog(
            uploadFragmentViewModel.notCloudErrorMessage.value!!,
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
        val photoUri =
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
                .transform(CenterCrop() ,RoundedCorners(mActivity.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp)))
                .into(binding.imageSelectedPhoto)

            binding.textWarningContent.text = getString(R.string.terms_second_creation)
            binding.textWarningContent.setTextColor(Color.RED)
            binding.cardviewImageWarningDark.visibility = View.GONE
            binding.textWarningDark.visibility = View.GONE
            binding.cardviewImageWarningNotcloud.visibility = View.GONE
            binding.textWarningNotcloud.visibility = View.GONE
        }

    /**
     * gallery ActivityForResult를 실행합니다.
     */
    private fun openGallery() {
        galleryActivityResult.launch("image/*")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged: dd")
    }
}