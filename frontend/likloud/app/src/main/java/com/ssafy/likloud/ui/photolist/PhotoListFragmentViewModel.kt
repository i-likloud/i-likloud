package com.ssafy.likloud.ui.photolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.repository.BaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

}