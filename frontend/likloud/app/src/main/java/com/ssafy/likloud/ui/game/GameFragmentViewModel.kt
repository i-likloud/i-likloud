package com.ssafy.likloud.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    /////////////////// 이미지 크기 ///////////////////////
    private var _imageWidth = MutableLiveData<Int>()
    val imageWidth: LiveData<Int>
        get() = _imageWidth
    fun changeImageWidth(width: Int){
        _imageWidth.value = width
    }

    private var _imageHeight = MutableLiveData<Int>()
    val imageHeight: LiveData<Int>
        get() = _imageHeight
    fun changeImageHeight(height: Int){
        _imageHeight.value = height
    }

    ////////////////////////// 시간 PROGRESS BAR //////////////////////////////////
    private var _remainTime = MutableLiveData<Int>()
    val remainTime: LiveData<Int>
        get() = _remainTime
    fun changeRemainTime(time: Int){
        _remainTime.value = time
    }

    ///////////////////////////// 문제 랜덤 리스트 /////////////////////////////////
    private var _randomQuestionIdxList = MutableLiveData<List<Int>>()
    val randomQuestionIdxList: LiveData<List<Int>>
        get() = _randomQuestionIdxList
    fun initRandomIdxList(){
        _randomQuestionIdxList.value = List(30) { Random.nextInt(30) }
    }

    private var _currentQuestionIdx = MutableLiveData<Int>()
    val currentQuestionIdx: LiveData<Int>
        get() = _currentQuestionIdx
    fun changeCurrentQuestionIdx(idx: Int){
        _currentQuestionIdx.value = idx
    }

    ///////////////////////////// 유저 관련 ////////////////////////////////////////


}