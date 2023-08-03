package com.ssafy.likloud.ui.drawing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingDetailFragmentViewModel@Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentDrawingDetail = MutableLiveData<DrawingDetailDto>()
    val currentDrawingDetail: LiveData<DrawingDetailDto>
        get() = _currentDrawingDetail
    /**
     * 넘겨받은 drawingId로 DrawingDetailDto 조회
     */
    fun getCurrentPhotoDrawingDetail(drawingId: Int){
        viewModelScope.launch {
            baseRepository.getDrawingDetail(drawingId).onSuccess {
                _currentDrawingDetail.value = it
            }
        }
    }

    /////////////////// 해당 멤버 정보 /////////////////////////////////
    private var _currentDrawingMember = MutableLiveData<MemberProfileDto>()
    val currentDrawingMember: LiveData<MemberProfileDto>
        get() = _currentDrawingMember
    fun getCurrentDrawingMember(memberId: Int){
        //여기서 api호출해서 받아라
        viewModelScope.launch {
            baseRepository.getMemberProfile(memberId).onSuccess {
                _currentDrawingMember.value = it
            }
        }
    }


    ///////////////////////////////////////////////////// 좋아요 //////////////////////////////////
    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked
    fun setIsLiked(){
        _isLiked.value = _currentDrawingDetail.value!!.memberLiked
    }
    fun changeIsLiked(){
        // api 호출
        viewModelScope.launch {
            baseRepository.changeDrawingLike(_currentDrawingDetail.value!!.drawingId)
            _isLiked.value = !_isLiked.value!!
        }
    }

    private val _likeCount = MutableLiveData<Int>()
    val likeCount: LiveData<Int>
        get() = _likeCount
    fun setLikeCount(){
        _likeCount.value = _currentDrawingDetail.value!!.likesCount
    }
    fun changeLikeCount(){
        if(_isLiked.value!!){
            _likeCount.value = _currentDrawingDetail.value!!.likesCount
        }else{
            _likeCount.value = _currentDrawingDetail.value!!.likesCount - 1
        }
    }
}