package com.ssafy.likloud.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MypageFragmentViewModel_싸피"
@HiltViewModel
class MypageFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _currentDrawingListDtoList = MutableLiveData<MutableList<DrawingListDto>>()
    val currentDrawingListDtoList: LiveData<MutableList<DrawingListDto>>
        get() = _currentDrawingListDtoList


    private var _isLoggedOut = MutableSharedFlow<Boolean>()
    val isLoggedout : SharedFlow<Boolean> = _isLoggedOut.asSharedFlow()


    /**
     * 내가 그린 그림 조회
     */
    fun getMyDrawingListDtoList(){
        viewModelScope.launch {
            baseRepository.getMyDrawingListDtoList().onSuccess {
                _currentDrawingListDtoList.value = it
            }
        }
    }
    /**
     * 내가 좋아요 한 그림 조회
     */
    fun getLikeDrawingListDtoList(){
        viewModelScope.launch {
            baseRepository.getLikeDrawingListDtoList().onSuccess {
                _currentDrawingListDtoList.value = it
            }
        }
    }

    ///////////////////////// 사진 관련 /////////////////////////////////////
    private var _currentPhotoListDtoList = MutableLiveData<MutableList<PhotoListDto>>()
    val currentPhotoListDtoList: LiveData<MutableList<PhotoListDto>>
        get() = _currentPhotoListDtoList
    /**
     * 내가 찍은 사진 조회
     */
    fun getMyPhotoListDtoList(){
        viewModelScope.launch {
            baseRepository.getMyPhotoListDtoList().onSuccess {
                _currentPhotoListDtoList.value = it
            }
        }
    }
    /**
     * 내가 즐찾한 사진 조회
     */
    fun getBookmarkPhotoListDtoList(){
        viewModelScope.launch {
            baseRepository.getBookmarkPhotoListDtoList().onSuccess {
                _currentPhotoListDtoList.value = it
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            val response = baseRepository.postLogout()
            if(response.isSuccessful){
                ApplicationClass.sharedPreferences.putString(ApplicationClass.X_ACCESS_TOKEN, "")
                ApplicationClass.sharedPreferences.putString(ApplicationClass.X_REFRESH_TOKEN, "")
                Log.d(TAG, "logout: FFF")
                _isLoggedOut.emit(true)
            }
        }
    }


    fun quitUser(){
        viewModelScope.launch {
            val response = baseRepository.postLogout()
            if(response.isSuccessful){
                ApplicationClass.sharedPreferences.putString(ApplicationClass.X_ACCESS_TOKEN, "")
                ApplicationClass.sharedPreferences.putString(ApplicationClass.X_REFRESH_TOKEN, "")
                Log.d(TAG, "logout: FFF")
                _isLoggedOut.emit(true)
            }
        }
    }
}