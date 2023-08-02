package com.ssafy.likloud.ui.drawinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "차선호"
@HiltViewModel
class DrawingListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {


    private var _currentDrawingListDtoList = MutableLiveData<MutableList<DrawingListDto>>()
    val currentDrawingListDtoList: LiveData<MutableList<DrawingListDto>>
        get() = _currentDrawingListDtoList

    /**
     * 최신순(기본) 조회
     */
    fun getRecentOrderDrawingListDtoList(){
        // api 호출해서 _recentOrderDrawingDtoList에 넣어줘라
        viewModelScope.launch {
            baseRepository.getDrawingList("").onSuccess {
                Log.d(TAG, "getRecentOrderDrawingListDtoList 결과: $it")
                _currentDrawingListDtoList.value = it
            }
        }
    }
    /**
     * 인기순 조회
     */
    fun getRankingOrderDrawingListDtoList(){
        viewModelScope.launch {
            // api 호출해서 _rankingOrderDrawingDtoList에 넣어줘라
            baseRepository.getDrawingList("likesCount").onSuccess {
                Log.d(TAG, "getRankingOrderDrawingListDtoList 결과 : $it ")
                _currentDrawingListDtoList.value = it
            }
        }
    }

    /**
     * 조회순 조회
     */
    fun getViewOrderDrawingListDtoLit(){
        viewModelScope.launch {
            // api 호출해서 _rankingOrderDrawingDtoList에 넣어줘라
            baseRepository.getDrawingList("viewCount").onSuccess {
                _currentDrawingListDtoList.value = it
            }
        }
    }



    /////////////////////////////////////////////////////////   선택된 drawing  ////////////////////////////////////////////////////////////////////////

    private var _selectedDrawingDetailDto = MutableLiveData<DrawingDetailDto>()
    val selectedDrawingDetailDto: LiveData<DrawingDetailDto>
        get() = _selectedDrawingDetailDto
    fun getSelectedDrawingDetailDto(dto: DrawingListDto){
        //여기서 api호출해서 받아라
        viewModelScope.launch {
            baseRepository.getDrawingDetail(dto.drawingId).onSuccess {
                _selectedDrawingDetailDto.value = it
            }
        }
    }
    fun changeSelectedDrawingDetailDtoMemberLiked(){
        _selectedDrawingDetailDto.value!!.memberLiked = !_selectedDrawingDetailDto.value!!.memberLiked
        // api 호출
    }

    ///////////////////////////////////////////////////////// 선택된 그림의 member ////////////////////////////////////////////////////////////

    private var _selectedDrawingMember = MutableLiveData<MemberProfileDto>()
    val selectedDrawingMember: LiveData<MemberProfileDto>
        get() = _selectedDrawingMember
    fun getSelectedDrawingMember(memberId: Int){
        //여기서 api호출해서 받아라
        viewModelScope.launch {
            baseRepository.getMemberProfile(memberId).onSuccess {
                _selectedDrawingMember.value = it
            }
        }
    }



    /////////////////////////////////////////////////////////   댓글  ////////////////////////////////////////////////////////////////////////

    private val _selectedDrawingCommentList = MutableLiveData<MutableList<CommentDto>>()
    val selectedDrawingCommentList: LiveData<MutableList<CommentDto>>
        get() = _selectedDrawingCommentList
    fun changeSelectedDrawingCommentList(list: MutableList<CommentDto>){
        _selectedDrawingCommentList.value = list
    }
//    fun addToCommentList(comment: CommentDto) {
//        viewModelScope.launch {
//            selectedDrawingCommentList.value?.add(comment)
//        }
//    }
//
//    fun removeComment(posi:Int){
//        viewModelScope.launch {
//            selectedDrawingCommentList.value!!.removeAt(posi)
//        }
//    }
}