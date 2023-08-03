package com.ssafy.likloud.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailFragmentViewModel  @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentPhotoDetail = MutableLiveData<PhotoListDto>()
    val currentPhotoDetail: LiveData<PhotoListDto>
        get() = _currentPhotoDetail
    /**
     * 넘겨받은 drawingId로 DrawingDetailDto 조회
     */
    fun getCurrentPhotoDetail(photoId: Int){
        viewModelScope.launch {
            baseRepository.getPhotoDetail(photoId).onSuccess {
                _currentPhotoDetail.value = it
            }
        }
    }

    /////////////////// 해당 멤버 정보 /////////////////////////////////
    private var _currentPhotoMember = MutableLiveData<MemberProfileDto>()
    val currentPhotoMember: LiveData<MemberProfileDto>
        get() = _currentPhotoMember
    fun getCurrentPhotoMember(memberId: Int){
        //여기서 api호출해서 받아라
        viewModelScope.launch {
            baseRepository.getMemberProfile(memberId).onSuccess {
                _currentPhotoMember.value = it
            }
        }
    }

    //////////////////////////// 선택된 photo의 DrawingListDto 목록 ///////////////////////////////
    private var _currentPhotoDrawingList = MutableLiveData<MutableList<DrawingListDto>>()
    val currentPhotoDrawingList: LiveData<MutableList<DrawingListDto>>
        get() = _currentPhotoDrawingList
    fun getCurrentPhotoDrawingList(photoId: Int){
        viewModelScope.launch {
            baseRepository.getPhotoDrawingList(photoId).onSuccess {
                _currentPhotoDrawingList.value = it
            }
        }
    }

    ///////////////////////////////////////////////////// 북마크 //////////////////////////////////
    private val _isBookmarked = MutableLiveData<Boolean>()
    var initialIsBookmarked: Boolean = false
    val isBookmarked: LiveData<Boolean>
        get() = _isBookmarked
    fun setIsBookmarked(){
        _isBookmarked.value = _currentPhotoDetail.value!!.memberBookmarked
        initialIsBookmarked = _currentPhotoDetail.value!!.memberBookmarked
    }
    fun changeIsBookmarked(){
        // api 호출
        viewModelScope.launch {
            baseRepository.changePhotoBookmark(_currentPhotoDetail.value!!.photoId)
            _isBookmarked.value = !_isBookmarked.value!!
        }
    }

    private val _bookmarkCount = MutableLiveData<Int>()
    val bookmarkCount: LiveData<Int>
        get() = _bookmarkCount
    fun setBookmarkCount(){
        _bookmarkCount.value = _currentPhotoDetail.value!!.bookmarkCount
    }
    fun changeBookmarkCount(){
        if(initialIsBookmarked){
            if (_isBookmarked.value!!) {
                _bookmarkCount.value = _currentPhotoDetail.value!!.bookmarkCount - 1
            } else {
                _bookmarkCount.value = _currentPhotoDetail.value!!.bookmarkCount
            }

        }else {
            if (_isBookmarked.value!!) {
                _bookmarkCount.value = _currentPhotoDetail.value!!.bookmarkCount
            } else {
                _bookmarkCount.value = _currentPhotoDetail.value!!.bookmarkCount + 1
            }
        }
    }

}