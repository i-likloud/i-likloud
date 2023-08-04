package com.ssafy.likloud.ui.store

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.data.model.response.StoreItemResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "StoreFragmentViewModel_싸피"
@HiltViewModel
class StoreFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {
    private var _storeAccessoryList = MutableLiveData<MutableList<StoreItemResponse>>()
    val storeAccessoryList: LiveData<MutableList<StoreItemResponse>>
        get() = _storeAccessoryList

    private var _memberInfo = MutableLiveData<MemberInfoResponse>()
    val memberInfo: LiveData<MemberInfoResponse>
        get() = _memberInfo

    suspend fun getStoreAccessoryList() {
        viewModelScope.launch {
            baseRepository.getStoreInfo(MemberInfoRequest(sharedPreferences.getString(USER_EMAIL).toString()))
                .onSuccess {
                    _storeAccessoryList.postValue(it.storeItemList)
                    _memberInfo.postValue(it.memberInfoResponse)
                }
                .onError {
                    Log.d(TAG, "getStoreAccessoryList: getStoreInfo 에러입니다. ${it.message}")
                }
        }
    }

    fun postBuyAccessory(storeId: Int) {
        viewModelScope.launch {
            baseRepository.postBuyAccessory(storeId, MemberInfoRequest(sharedPreferences.getString(USER_EMAIL).toString()))
                .onSuccess {
                    getStoreAccessoryList()
                }
                .onError {
                    Log.d(TAG, "getStoreAccessoryList: getStoreInfo 에러입니다. ${it.message}")
                }
        }
    }
}