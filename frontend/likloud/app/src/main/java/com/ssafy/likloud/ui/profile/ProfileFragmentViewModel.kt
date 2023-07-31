package com.ssafy.likloud.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
import com.ssafy.likloud.data.model.response.LoginAdditionalResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileFragmentViewMode_싸피"
@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {
    /**
     * 회원가입 후 GUEST 단계의 사용자를 MEMBER 단계로 올리고, MEMBER용 JWT 토큰을 다시 받아옴
     */
    fun patchAdditionalInfo(loginAdditionalRequest: LoginAdditionalRequest) {
        viewModelScope.launch {
            baseRepository.patchAdditionalInfo(loginAdditionalRequest).apply {
                if (this.code() == 200) {
                    val token = this.headers().get("newtoken")
                    Log.d(TAG, "patchAdditionalInfo: 찐 토큰을 받아왔어용 ${token}")
                    ApplicationClass.sharedPreferences.putString(ApplicationClass.X_ACCESS_TOKEN, token.toString())
                }
            }
        }
    }
}