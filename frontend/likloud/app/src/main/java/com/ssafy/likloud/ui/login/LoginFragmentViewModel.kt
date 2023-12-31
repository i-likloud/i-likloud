package com.ssafy.likloud.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass.Companion.FIREBASE_TOKEN
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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

    fun getTokenValidation(accessToken: String) {
        // token validation checking api 구현 필요
        _isTokenReceived.value = true
    }

//    private var _loginResponse = MutableSharedFlow<LoginResponse>()
//    val loginResponse: SharedFlow<LoginResponse>
//        get() = _loginResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    /**
     * 회원가입 시 GUEST단계의 accessToken, refreshToken을 받아옵니다.
     * 추후 MEMBER로 올리기 위해 accessToken이 필요합니다.
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun postLogin(email: String, socialType: String) {
        Log.d(TAG, "postLogin: 포스트 로그인 시도입니다.")
        GlobalScope.launch {
            Log.d(TAG, "postLogin: ddddd")
            baseRepository.postLogin(
                LoginRequest(
                    email, socialType, sharedPreferences.getString(
                        FIREBASE_TOKEN
                    )?: "firebase_token_not_registered"
                )
            ).onSuccess {
                _loginResponse.postValue(it)
                sharedPreferences.putString(X_ACCESS_TOKEN, it.accessToken)
                Log.d(TAG, "postLogin 찐 refresh: ${it.refreshToken}")
                sharedPreferences.putString(X_REFRESH_TOKEN, it.refreshToken)
            }.onError {
                Log.d(TAG, "postLogin: 에러가 났슴니다 에러메시지는? ${it.message}")
            }
        }
    }


//        @OptIn(DelicateCoroutinesApi::class)
//    fun postLogin(email: String, socialType: String) {
//        Log.d(TAG, "postLogin: 포스트 로그인 시도입니다.")
//        GlobalScope.launch {
//            Log.d(TAG, "postLogin: ddddd")
//            baseRepository.postLogin(
//                LoginRequest(
//                    email, socialType, sharedPreferences.getString(
//                        FIREBASE_TOKEN
//                    )?: "firebase_token_not_registered"
//                )
//            ).onSuccess {
//                _loginResponse.emit(it)
//                sharedPreferences.putString(X_ACCESS_TOKEN, it.accessToken)
//                Log.d(TAG, "postLogin 찐 refresh: ${it.refreshToken}")
//                sharedPreferences.putString(X_REFRESH_TOKEN, it.refreshToken)
//            }.onError {
//                Log.d(TAG, "postLogin: 에러가 났슴니다 에러메시지는? ${it.message}")
//            }
//        }
//    }


}