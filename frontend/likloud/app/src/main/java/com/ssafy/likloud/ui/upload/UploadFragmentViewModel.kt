package com.ssafy.likloud.ui.upload

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _photoMultipartBody = MutableLiveData<MultipartBody.Part>()
    val photoMultipartBody: LiveData<MultipartBody.Part>
        get() = _photoMultipartBody

    private val _photoUrl = MutableLiveData<String>()
    val photoUrl: LiveData<String>
        get() = _photoUrl

    private val _isPhotoMultipartCreated = MutableLiveData<Boolean>()
    val isPhotoMultipartCreated: LiveData<Boolean> get() = _isPhotoMultipartCreated

    // 요청하고자 하는 권한들
    val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    fun getImageUrlFromMultipart(multipartBody: MultipartBody.Part) {

    }

    fun setMultipart(multipartBody: MultipartBody.Part) {
        _photoMultipartBody.value = multipartBody
        _isPhotoMultipartCreated.value = true
    }

    fun sendMultipart(multipartBody: MultipartBody.Part) {
        // multipart post
    }

}