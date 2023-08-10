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
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "차선호"
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
                _currentDrawingCommentList.value = it.commentList
                _nftYn.value = it.nftYn
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
                _currentDrawingCommentList.value = _currentDrawingCommentList.value!!
            }
        }
    }
    fun deleteDrawingComment(commentId: Int, position: Int){
        viewModelScope.launch {
            baseRepository.deleteDrawingComment(commentId)
            _currentDrawingCommentList.value!!.removeAt(position)
            _currentDrawingCommentList.value = _currentDrawingCommentList.value!!
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
    var initialIsLiked: Boolean = false
    val isLiked: LiveData<Boolean>
        get() = _isLiked
    fun setIsLiked(){
        _isLiked.value = _currentDrawingDetail.value!!.memberLiked
        initialIsLiked = _currentDrawingDetail.value!!.memberLiked
    }
    fun changeIsLiked(){
        // api 호출
        viewModelScope.launch {
            baseRepository.changeDrawingLike(_currentDrawingDetail.value!!.drawingId).onSuccess {
                _isLiked.value = !_isLiked.value!!
            }
        }
    }

    private val _likeCount = MutableLiveData<Int>()
    val likeCount: LiveData<Int>
        get() = _likeCount
    fun setLikeCount(){
        _likeCount.value = _currentDrawingDetail.value!!.likesCount
    }
    fun changeLikeCount(){
        if(initialIsLiked){
            if(_isLiked.value!!){
                _likeCount.value = _currentDrawingDetail.value!!.likesCount - 1
            }else{
                _likeCount.value = _currentDrawingDetail.value!!.likesCount
            }
        }else{
            if(_isLiked.value!!){
                _likeCount.value = _currentDrawingDetail.value!!.likesCount
            }else{
                _likeCount.value = _currentDrawingDetail.value!!.likesCount + 1
            }
        }
    }

    private var _isSuccess = MutableSharedFlow<Boolean>()
    val isSuccess: SharedFlow<Boolean>
        get() = _isSuccess.asSharedFlow()

    fun registNft(drawingId: Int){
        viewModelScope.launch {
            baseRepository.registNft(drawingId).onSuccess {
                // NFT 발급 성공
                Log.d(TAG, "registNft 성공")
                _isSuccess.emit(true)
                _nftYn.value = true
            }
        }
    }

    private var _nftYn = MutableLiveData<Boolean>()
    val nftYn: LiveData<Boolean>
        get() = _nftYn
    fun setNftYn(value: Boolean){
        _nftYn.value = value
    }

    //////리포트
    lateinit var drawingReportDialog: DrawingReportDialog
    fun setDrawingReportDialog(){
        drawingReportDialog = DrawingReportDialog(currentDrawingDetail.value!!.drawingId)
    }
    private var _isReported = MutableSharedFlow<Boolean>()
    val isReported: SharedFlow<Boolean>
        get() = _isReported.asSharedFlow()
    fun sendReport(content: String){
        viewModelScope.launch {
            baseRepository.sendReport(_currentDrawingDetail.value!!.drawingId, content).apply {
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

}