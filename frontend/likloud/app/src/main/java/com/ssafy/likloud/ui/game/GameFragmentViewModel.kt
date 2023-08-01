package com.ssafy.likloud.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    /////////////////// 이미지 크기 ///////////////////////
    private var _imageWidth = MutableLiveData<Int>()
    val imageWidth: LiveData<Int>
        get() = _imageWidth
    fun changeImageWidth(width: Int){
        viewModelScope.launch {
            _imageWidth.value = width
        }
    }

    private var _imageHeight = MutableLiveData<Int>()
    val imageHeight: LiveData<Int>
        get() = _imageHeight
    fun changeImageHeight(height: Int){
        viewModelScope.launch {
            _imageHeight.value = height
        }
    }

    ////////////////////////// 시간 PROGRESS BAR //////////////////////////////////
    private var _remainTime = MutableLiveData<Int>()
    val remainTime: LiveData<Int>
        get() = _remainTime
    fun changeRemainTime(time: Int){
        viewModelScope.launch {
            _remainTime.value = time
        }
    }


}