package com.ssafy.likloud.ui.photolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDrawingDetailFragmentViewModel@Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentPhotoDrawingDetail = MutableLiveData<DrawingDetailDto>()
    val currentPhotoDrawingDetail: LiveData<DrawingDetailDto>
        get() = _currentPhotoDrawingDetail
    /**
     * 넘겨받은 drawingId로 DrawingDetailDto 조회
     */
    fun getCurrentPhotoDrawingDetail(drawingId: Int){
        viewModelScope.launch {
            baseRepository.getDrawingDetail(drawingId).onSuccess {
                _currentPhotoDrawingDetail.value = it
            }
        }
    }

}