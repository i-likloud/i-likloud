package com.ssafy.likloud.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentDrawingListDtoList = MutableLiveData<MutableList<DrawingListDto>>()
    val currentDrawingListDtoList: LiveData<MutableList<DrawingListDto>>
        get() = _currentDrawingListDtoList
    /**
     * 내가 그린 그림 조회
     */
    fun getMyDrawingListDtoList(){
        viewModelScope.launch {
            baseRepository.getMyDrawingListDtoList().onSuccess {
                _currentDrawingListDtoList.value = it
            }
        }
    }

}