package com.ssafy.likloud.ui.photo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "차선호"
@HiltViewModel
class PhotoListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentPhotoListDtoList = MutableLiveData<MutableList<PhotoListDto>>()
    val currentPhotoListDtoList: LiveData<MutableList<PhotoListDto>>
        get() = _currentPhotoListDtoList

    /**
     * 최신순 조회9기본)
     */
    fun getRecentOrderPhotoListDtoList(){
        viewModelScope.launch {
            baseRepository.getPhotoList("new").onSuccess {
                _currentPhotoListDtoList.value = it
            }
        }
    }

    /**
     * 인기순(그림 그린 순) 조회
     */
    fun getRankingOrderPhotoListDtoList(){
        viewModelScope.launch {
            baseRepository.getPhotoList("pick").onSuccess {
                _currentPhotoListDtoList.value = it
            }
        }
    }

    /**
     * 즐찾순 조회
     */
    fun getBookmarkOrderPhotoListDtoList(){
        viewModelScope.launch {
            baseRepository.getPhotoList("bookmarkdesc").onSuccess {
                _currentPhotoListDtoList.value = it
            }
        }
    }


    /////////////////////////////// 현재 선택된 photo ///////////////////////////
    private var _currentPhotoListDto = MutableLiveData<PhotoListDto>()
    val currentPhotoListDto: LiveData<PhotoListDto>
        get() = _currentPhotoListDto
    fun setCurrentPhotoListDto(dto: PhotoListDto){
        Log.d(TAG, "setCurrentPhotoListDto: $dto")
        _currentPhotoListDto.value = dto
    }
    fun getCurrentPhotoListDto(photoId: Int){
        viewModelScope.launch {
            baseRepository.getCurrentPhotoListDto(photoId).onSuccess {
                _currentPhotoListDto.value = it
            }
        }
    }


    ////////////////////////////// 선택된 photo의 member //////////////////////////////
    private var _currentPhotoMember = MutableLiveData<MemberProfileDto>()
    val currentPhotoMember: LiveData<MemberProfileDto>
        get() = _currentPhotoMember
    fun getCurrentPhotoMember(memberId: Int){
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
        Log.d(TAG, "setIsBookmarked: ${_currentPhotoListDto.value}")
        _isBookmarked.value = _currentPhotoListDto.value!!.memberBookmarked
        initialIsBookmarked = _currentPhotoListDto.value!!.memberBookmarked
    }
    fun changeIsBookmarked(){
        // api 호출
        viewModelScope.launch {
            baseRepository.changePhotoBookmark(_currentPhotoListDto.value!!.photoId).onSuccess {
                _isBookmarked.value = !_isBookmarked.value!!
            }
        }
    }

    private val _bookmarkCount = MutableLiveData<Int>()
    val bookmarkCount: LiveData<Int>
        get() = _bookmarkCount
    fun setBookmarkCount(){
        _bookmarkCount.value = _currentPhotoListDto.value!!.bookmarkCount
    }
    fun changeBookmarkCount(){
        if(initialIsBookmarked){
            if (_isBookmarked.value!!) {
                _bookmarkCount.value = _currentPhotoListDto.value!!.bookmarkCount - 1
            } else {
                _bookmarkCount.value = _currentPhotoListDto.value!!.bookmarkCount
            }

        }else {
            if (_isBookmarked.value!!) {
                _bookmarkCount.value = _currentPhotoListDto.value!!.bookmarkCount
            } else {
                _bookmarkCount.value = _currentPhotoListDto.value!!.bookmarkCount + 1
            }
        }
    }

}