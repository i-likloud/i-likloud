package com.ssafy.likloud.ui.profileedit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.response.AccessoryResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileEditFragmentView_싸피"
@HiltViewModel
class ProfileEditFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _selectedColor = 0
    val selectedColor: Int
        get() = _selectedColor
    fun setWaterDropColor(num: Int) {
        _selectedColor = num
    }

    private var _selectedFace = 0
    val selectedFace: Int
        get() = _selectedFace
    fun setWaterDropFace(num: Int) {
        _selectedFace = num
    }

    private var _selectedAccessory = 0
    val selectedAccessory: Int
        get() = _selectedAccessory
    fun setWaterDropAccessory(num: Int) {
        _selectedAccessory = num
    }

    private val _myAccessoryList = MutableLiveData<MutableList<AccessoryResponse>>(mutableListOf())
    val myAccessoryList: LiveData<MutableList<AccessoryResponse>>
        get() = _myAccessoryList

    suspend fun getMyAccessoryList() {
        baseRepository.getMyAccessoryList(MemberInfoRequest(
                ApplicationClass.sharedPreferences.getString(ApplicationClass.USER_EMAIL).toString()))
            .onSuccess {
                _myAccessoryList.value = it
            }
            .onError {
                Log.d(TAG, "getMyAccessoryList: ${it.message}")
            }
    }
}