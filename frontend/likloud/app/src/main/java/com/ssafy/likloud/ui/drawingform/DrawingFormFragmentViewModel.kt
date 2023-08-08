package com.ssafy.likloud.ui.drawingform

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.MemberInfoDto
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.response.LoginResponse
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
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "DrawingFormFragmentView_싸피"
@HiltViewModel
class DrawingFormFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    // 변화가 있는 데이터를 감지해야 하므로 stateflow 사용
    private val _titleMessageText: MutableStateFlow<String> = MutableStateFlow("")
    val titleMessageText : StateFlow<String> get() = _titleMessageText.asStateFlow()

    private val _descMessageText: MutableStateFlow<String> = MutableStateFlow("")
    val descMessageText : StateFlow<String> get() = _descMessageText.asStateFlow()


    private val _isDrawingUploaded: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isDrawingUploaded : SharedFlow<Boolean> get() = _isDrawingUploaded.asSharedFlow()

    // emit으로 수정 필요
    fun settitleMessage(text : String){
        _titleMessageText.value = text
    }

    fun setDescMessage(text : String){
        _descMessageText.value = text
    }

    fun uploadDrawing(multipartBody: MultipartBody.Part, uploadPhotoId : Int, title: String, description : String){
        viewModelScope.launch {
            baseRepository.postDrawingMultipart(multipartBody, uploadPhotoId, title, description , MemberInfoDto("email", "role")).onSuccess {
                Log.d(TAG, "uploadDrawing: success")
                Log.d(TAG, "uploadDrawing: ${title}")
                _isDrawingUploaded.emit(true)
            }
                .onError {
                    Log.d(TAG, "uploadDrawing: fail")
                }
        }

    }

}