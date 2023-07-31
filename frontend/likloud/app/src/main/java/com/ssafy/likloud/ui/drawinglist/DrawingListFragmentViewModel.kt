package com.ssafy.likloud.ui.drawinglist

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

@HiltViewModel
class DrawingListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private val _rankingOrderDrawingDtoList =  mutableListOf<DrawingListDto>()
    val rankingOrderDrawingListDto: MutableList<DrawingListDto>
        get() = _rankingOrderDrawingDtoList
    fun getRankingOrderDrawingDtoList(){
        // api 호출해서 _rankingOrderDrawingDtoList에 넣어줘라
    }

    private val _recentOrderDrawingDtoList =  mutableListOf<DrawingListDto>()
    val recentOrderDrawingDtoList: MutableList<DrawingListDto>
        get() = _recentOrderDrawingDtoList
    fun getRecentOrderDrawingDtoList(){
        // api 호출해서 _recentOrderDrawingDtoList에 넣어줘라
    }

    private var _currentDrawingDtoList = mutableListOf<DrawingListDto>(DrawingListDto(), DrawingListDto())
    val currentDrawingDtoList: MutableList<DrawingListDto>
        get() = _currentDrawingDtoList
    fun changeCurrentDrawingDtoList(list: MutableList<DrawingListDto>){
        _currentDrawingDtoList = list
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