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
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "차선호"
@HiltViewModel
class DrawingListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _rankingOrderDrawingListDtoList = MutableLiveData<MutableList<DrawingListDto>>()
    val rankingOrderDrawingListDtoList: LiveData<MutableList<DrawingListDto>>
        get() = _rankingOrderDrawingListDtoList
    fun getRankingOrderDrawingListDtoList(){

        viewModelScope.launch {
            // api 호출해서 _rankingOrderDrawingDtoList에 넣어줘라
            baseRepository.getDrawingList("?orderBy=likesCount").onSuccess {
                _rankingOrderDrawingListDtoList = it
                Log.d(TAG, "getRankingOrderDrawingListDtoList 결과 : ${it.value} ")
            }
        }
    }

    private var _recentOrderDrawingListDtoList =  MutableLiveData<MutableList<DrawingListDto>>()
    val recentOrderDrawingListDtoList: LiveData<MutableList<DrawingListDto>>
        get() = _recentOrderDrawingListDtoList
    fun getRecentOrderDrawingListDtoList(){
        // api 호출해서 _recentOrderDrawingDtoList에 넣어줘라
        viewModelScope.launch {
            // api 호출해서 _rankingOrderDrawingDtoList에 넣어줘라
            baseRepository.getDrawingList("").onSuccess {
                _recentOrderDrawingListDtoList = it
                Log.d(TAG, "getRecentOrderDrawingListDtoList 결과: ${it.value}")
            }
        }
    }

    private var _currentDrawingListDtoList = MutableLiveData<MutableList<DrawingListDto>>()
    val currentDrawingListDtoList: LiveData<MutableList<DrawingListDto>>
        get() = _currentDrawingListDtoList
    fun changeCurrentDrawingListDtoList(list: MutableList<DrawingListDto>){
        _currentDrawingListDtoList.value = list
    }
    fun changeCurrentToRanking(){
        _currentDrawingListDtoList = _rankingOrderDrawingListDtoList
    }
    fun changeCurrentToRecent(){
        _currentDrawingListDtoList = _recentOrderDrawingListDtoList
    }

    private var _selectedDrawingListDto = DrawingListDto()
    val selectedDrawingListDto: DrawingListDto
        get() = _selectedDrawingListDto
    fun changeSelectedDrawingListDto(dto: DrawingListDto){
        _selectedDrawingListDto = dto
    }

    private var _selectedDrawingDetailDto = DrawingDetailDto()
    val selectedDrawingDetailDto: DrawingDetailDto
        get() = _selectedDrawingDetailDto
    fun getSelectedDrawingDetailDto(dto: DrawingListDto){
        //여기서 api호출해서 받아라
    }
    fun changeSelectedDrawingDetailDtoMemberLiked(){
        _selectedDrawingDetailDto.memberLiked = !_selectedDrawingDetailDto.memberLiked
        // api 호출
    }



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