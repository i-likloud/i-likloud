package com.ssafy.likloud.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {
    private val _user = MutableLiveData<UserDto>()
//    val user: LiveData<UserDto>
//        get() = _user
//
//    fun getUserInfo(userId: Int) {
//        viewModelScope.launch {
//            baseRepository.getUser(userId).onSuccess {
//                _user.value = it
//            }
//        }
//    }
}