package com.ssafy.likloud.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.response.LoginResponse
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginFragmentViewModel_싸피"
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

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    /**
     * 회원가입 시 GUEST단계의 accessToken, refreshToken을 받아옵니다.
     * 추후 MEMBER로 올리기 위해 accessToken이 필요합니다.
     */
    fun postLogin(email: String, socialType: String) {
        viewModelScope.launch {
            baseRepository.postLogin(LoginRequest(email, socialType)).onSuccess {
                _loginResponse.value = it
                sharedPreferences.putString(X_ACCESS_TOKEN, it.accessToken)
                sharedPreferences.putString(X_REFRESH_TOKEN, it.refreshToken)
            }
        }
    }

    private val _memberInfo = MutableLiveData<MemberInfoResponse>()
    val memberInfo: LiveData<MemberInfoResponse>
        get() = _memberInfo

    /**
     * 멤버 정보를 불러 옵니다.
     */
    suspend fun getUserInfo(email: String) {
        viewModelScope.launch {
            baseRepository.getMemberInfo(MemberInfoRequest(email)).apply {
                onSuccess {
                    Log.d(TAG, "getUserInfo: onSuccess ${it}")
                    _memberInfo.postValue(it)
                }
                onError {
                    Log.d(TAG, "getUserInfo: ${it.message}")
                }
            }
        }
    }
}