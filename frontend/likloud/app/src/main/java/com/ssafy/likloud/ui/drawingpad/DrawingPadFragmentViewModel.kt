package com.ssafy.likloud.ui.drawingpad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.response.LoginResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DrawingPadFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {
    private val _drawingMultipartBody = MutableLiveData<MultipartBody.Part>()
    val drawingMultipartBody: LiveData<MultipartBody.Part> get() = _drawingMultipartBody


    fun setDrawingMultipart(multipartBody: MultipartBody.Part) {
        _drawingMultipartBody.value = multipartBody
    }


}