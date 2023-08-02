package com.ssafy.likloud.ui.photolist

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
    private var _selectedPhotoListDto = MutableLiveData<PhotoListDto>()
    val selectedPhotoListDto: LiveData<PhotoListDto>
        get() = _selectedPhotoListDto
    fun setSelectedPhotoListDto(dto: PhotoListDto){
        _selectedPhotoListDto.value = dto
    }


    ////////////////////////////// 선택된 photo의 member //////////////////////////////
    private var _selectedPhotoMember = MutableLiveData<MemberProfileDto>()
    val selectedPhotoMember: LiveData<MemberProfileDto>
        get() = _selectedPhotoMember
    fun getSelectedPhotoMember(memberId: Int){
        viewModelScope.launch {
            baseRepository.getMemberProfile(memberId).onSuccess {
                _selectedPhotoMember.value = it
            }
        }
    }

    //////////////////////////// 선택된 photo의 DrawingListDto 목록 ///////////////////////////////
    private var _selectedPhotoDrawingList = MutableLiveData<MutableList<DrawingListDto>>()
    val selectedPhotoDrawingList: LiveData<MutableList<DrawingListDto>>
        get() = _selectedPhotoDrawingList
    fun getSelectedPhotoDrawingList(photoId: Int){
        viewModelScope.launch {
            baseRepository.getPhotoDrawingList(photoId).onSuccess {
                _selectedPhotoDrawingList.value = it
            }
        }
    }

}