package com.ssafy.likloud.ui.login

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
class LoginFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {
    private val _isTokenReceived = MutableLiveData<Boolean>()
    val isTokenReceived: LiveData<Boolean>
        get() = _isTokenReceived

     fun getTokenValidation(accessToken : String){
        // token validation checking api 구현 필요
        _isTokenReceived.value = true
    }

}