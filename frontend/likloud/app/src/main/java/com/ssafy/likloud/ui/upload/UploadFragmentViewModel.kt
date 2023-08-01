package com.ssafy.likloud.ui.upload

import android.Manifest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.getOrElse
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.MemberInfoDto
import com.ssafy.likloud.data.model.photo.PhotoErrorResponseDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.apache.commons.lang3.mutable.Mutable
import retrofit2.Retrofit
import javax.inject.Inject

private const val TAG = "UploadFragmentViewModel_싸피"

@HiltViewModel
class UploadFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository,
    private val retrofit: Retrofit
) : ViewModel() {
    // photo의 multipart
    private val _photoMultipartBody = MutableLiveData<MultipartBody.Part>()
    val photoMultipartBody: LiveData<MultipartBody.Part> get() = _photoMultipartBody

    private val _photoUrl = MutableLiveData<String>()
    val photoUrl: LiveData<String> get() = _photoUrl

    private val _isPhotoMultipartValidated: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isPhotoMultipartValidated: SharedFlow<Boolean> get() = _isPhotoMultipartValidated.asSharedFlow()

    private val _notCloudErrorMessage = MutableLiveData<String>()
    val notCloudErrorMessage: LiveData<String> get() = _notCloudErrorMessage


    private val _uploadPhotoUrl = MutableLiveData<String>()
    val uploadPhotoUrl: LiveData<String> get() = _uploadPhotoUrl

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
    }

    /**
     * 이미지가 구름인지 아닌지에 따라서 error 혹은 success에 다른 responseDto가 옵니다.
     * errorBody를 받기 위해 NetworkResult가 아닌 Response객체로 받아 PhotoErrorResponseDto로 errorBody를 매핑합니다.
     */
    fun sendMultipart(multipartBody: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = baseRepository.postPhotoMultipart(
                    listOf(multipartBody),
                    MemberInfoDto("email", "role")
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _uploadPhotoUrl.value = responseBody[0].photoUrl
                        Log.d(TAG, "sendMultipart is cloud: ${responseBody[0].photoUrl} ")
                        _isPhotoMultipartValidated.emit(true)
                    } else {
                        Log.e(TAG, "sendMultipart: Response body is null.")
                    }
                } else {
                    val errorBody = response.errorBody()?.let {
                        retrofit.responseBodyConverter<PhotoErrorResponseDto>(
                            PhotoErrorResponseDto::class.java,
                            PhotoErrorResponseDto::class.java.annotations
                        ).convert(it)
                    }

                    if (errorBody != null) {
                        Log.d(TAG, "sendMultipart: ${errorBody.message}")
                        _notCloudErrorMessage.value = errorBody.message
                        Log.d(TAG, "sendMultipart: ${errorBody.photoUrl}")
                        _isPhotoMultipartValidated.emit(false)
                    } else {
                        Log.e(TAG, "sendMultipart: Error body is null.")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "sendMultipart: Exception occurred: ${e.message}", e)
            }
        }
    }

    fun setValidatedTrue() {
        viewModelScope.launch {
            _isPhotoMultipartValidated.emit(true)
        }

    }


}