package com.ssafy.likloud.ui.nftlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "차선호"
@HiltViewModel
class NftGiftSearchUserFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentSearchUserList = MutableLiveData<MutableList<MemberInfoResponse>>()
    val currentSearchUserList: LiveData<MutableList<MemberInfoResponse>>
        get() = _currentSearchUserList
    fun getCurrentSearchUserList(nickname: String){
        viewModelScope.launch {
            baseRepository.getCurrentSearchUserList(nickname).onSuccess {
                _currentSearchUserList.value = it
            }
        }
    }

    lateinit var toMemberInfo: MemberInfoResponse
    fun setMemberInfo(memberInfo: MemberInfoResponse){
        this.toMemberInfo = memberInfo
    }

    val nftGiftDialog = NftGiftDialog()



    private var _isWallet = MutableSharedFlow<Boolean>()
    val isWallet: SharedFlow<Boolean>
        get() = _isWallet.asSharedFlow()
    fun getMemberNftWallet(memberId: Int){
        Log.d(TAG, "getMemberNftWallet의 memberId: $memberId")
        viewModelScope.launch {
            baseRepository.getMemberNftWallet(memberId).apply {
                onSuccess {
                    _isWallet.emit(true)
                }
                onError {
                    _isWallet.emit(false)
                }
            }
        }
    }

}