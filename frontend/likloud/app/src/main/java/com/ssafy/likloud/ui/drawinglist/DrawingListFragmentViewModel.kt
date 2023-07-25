package com.ssafy.likloud.ui.drawinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private val _commentList = MutableLiveData<MutableList<CommentDto>>()
    val commentList: LiveData<MutableList<CommentDto>> get() = _commentList


    fun addToCommentList(comment: CommentDto) {
        viewModelScope.launch {
            _commentList.value?.add(comment)
            _commentList.value = _commentList.value
        }
    }

    fun clearCommentList(){
        viewModelScope.launch {
            _commentList.value = arrayListOf()
        }
    }

    fun removeComment(posi:Int){
        viewModelScope.launch {
            _commentList.value!!.removeAt(posi)
            _commentList.value = _commentList.value
        }
    }
}