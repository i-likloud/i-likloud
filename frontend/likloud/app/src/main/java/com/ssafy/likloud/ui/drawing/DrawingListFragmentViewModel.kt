package com.ssafy.likloud.ui.drawing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Collections
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

    private var _currentDrawingDetailDto = MutableLiveData<DrawingDetailDto>()
    val currentDrawingDetailDto: LiveData<DrawingDetailDto>
        get() = _currentDrawingDetailDto
    fun getCurrentDrawingDetailDto(dto: DrawingListDto){
        //여기서 api호출해서 받아라
        viewModelScope.launch {
            baseRepository.getDrawingDetail(dto.drawingId).onSuccess {
                Log.d(TAG, "getCurrentDrawingDetailDto: 현재 불러온 그림에대한 정보들 : ${it}")
                _currentDrawingDetailDto.value = it
                _currentDrawingCommentList.value = it.commentList
            }
        }
    }
    //현재 그림에 대한 댓글 리스트
    private val _currentDrawingCommentList = MutableLiveData<MutableList<CommentDto>>()
    val currentDrawingCommentList: LiveData<MutableList<CommentDto>>
        get() = _currentDrawingCommentList
    fun registDrawingComment(drawingId: Int,content: String){
        viewModelScope.launch {
            baseRepository.registDrawingComment(drawingId, content).onSuccess {
                _currentDrawingCommentList.value!!.add(it)
                Log.d(TAG, "registDrawingComment... ${_currentDrawingCommentList.value}")
                _currentDrawingCommentList.value = _currentDrawingCommentList.value!!
            }
        }
    }
    fun deleteDrawingComment(commentId: Int, position: Int){
        viewModelScope.launch {
            baseRepository.deleteDrawingComment(commentId).onSuccess {
                _currentDrawingCommentList.value!!.removeAt(position)
                _currentDrawingCommentList.value = _currentDrawingCommentList.value!!
            }
        }
    }

    ///////////////////////////////////////////////////////// 선택된 그림의 member ////////////////////////////////////////////////////////////

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
    var initialIsLiked: Boolean = false
    val isLiked: LiveData<Boolean>
        get() = _isLiked
    fun setIsLiked(){
        _isLiked.value = _currentDrawingDetailDto.value!!.memberLiked
        initialIsLiked = _currentDrawingDetailDto.value!!.memberLiked
    }
    fun changeIsLiked(){
        // api 호출
        viewModelScope.launch {
            baseRepository.changeDrawingLike(_currentDrawingDetailDto.value!!.drawingId).onSuccess {
                _isLiked.value = !_isLiked.value!!
            }
        }
    }

    private val _likeCount = MutableLiveData<Int>()
    val likeCount: LiveData<Int>
        get() = _likeCount
    fun setLikeCount(){
        _likeCount.value = _currentDrawingDetailDto.value!!.likesCount
    }
    fun changeLikeCount(){
        if(initialIsLiked){
            if(_isLiked.value!!){
                _likeCount.value = _currentDrawingDetailDto.value!!.likesCount - 1
            }else{
                _likeCount.value = _currentDrawingDetailDto.value!!.likesCount
            }
        }else{
            if(_isLiked.value!!){
                _likeCount.value = _currentDrawingDetailDto.value!!.likesCount
            }else{
                _likeCount.value = _currentDrawingDetailDto.value!!.likesCount + 1
            }
        }
    }
    fun registNft(drawingId: Int){
        viewModelScope.launch {
            baseRepository.registNft(drawingId).onSuccess {
                // NFT 발급 성공
                Log.d(TAG, "registNft 성공")
            }
        }
    }

    //////리포트
    lateinit var drawingReportDialog: DrawingReportDialog
    fun setDrawingReportDialog(){
        drawingReportDialog = DrawingReportDialog(currentDrawingDetailDto.value!!.drawingId)
    }
    private var _isReported = MutableSharedFlow<Boolean>()
    val isReported: SharedFlow<Boolean>
        get() = _isReported.asSharedFlow()
    fun sendReport(content: String){
        viewModelScope.launch {
            baseRepository.sendReport(_currentDrawingDetailDto.value!!.drawingId, content).apply {
                onSuccess {
                    Log.d(TAG, "sendReport 성공")
                    _isReported.emit(true)
                }
                onError {
                    Log.d(TAG, "sendReport error : $it ")
                }
            }
        }
    }

    lateinit var deleteCommentDialog: CommentDeleteDialog
    fun createDeleteCommentDialog(commentId: Int, position: Int){
        deleteCommentDialog = CommentDeleteDialog(
            delete = {deleteDrawingComment(commentId,position)},
            commentId
        )
    }
}