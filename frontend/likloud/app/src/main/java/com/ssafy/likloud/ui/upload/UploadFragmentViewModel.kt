package com.ssafy.likloud.ui.upload

import android.Manifest
import androidx.lifecycle.ViewModel
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {
    // photo의 multipart
    lateinit var photoMultipartBody: MultipartBody.Part

    // 요청하고자 하는 권한들
    val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

}