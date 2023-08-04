package com.ssafy.likloud

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.ImageTempDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.request.ProfileEditRequest
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

private const val TAG = "MainActivityViewModel_싸피"
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    val waterDropColorList: List<ImageTempDto> = listOf(
        ImageTempDto(0, R.drawable.profile_water_drop_white),
        ImageTempDto(1, R.drawable.profile_water_drop_purple),
        ImageTempDto(2, R.drawable.profile_water_drop_mint),
        ImageTempDto(3, R.drawable.profile_water_drop_blue),
        ImageTempDto(4, R.drawable.profile_water_drop_orange),
        ImageTempDto(5, R.drawable.profile_water_drop_red),
        ImageTempDto(6, R.drawable.profile_water_drop_green),
        ImageTempDto(7, R.drawable.profile_water_drop_pink),
        ImageTempDto(8, R.drawable.profile_water_drop_lemon),
    )

    val waterDropFaceList: List<ImageTempDto> = listOf(
        ImageTempDto(0, R.drawable.face_normal),
        ImageTempDto(1, R.drawable.face_smile),
        ImageTempDto(2, R.drawable.face_ggiu),
    )

    val waterDropAccessoryList: List<ImageTempDto> = listOf(
        ImageTempDto(0, R.drawable.water_drop_item_empty),
        ImageTempDto(1, R.drawable.water_drop_item_duckmouth),
        ImageTempDto(2, R.drawable.water_drop_item_shine),
        ImageTempDto(3, R.drawable.water_drop_item_mustache),
        ImageTempDto(4, R.drawable.water_drop_item_sunglass),
        ImageTempDto(5, R.drawable.water_drop_item_umbrella)
    )

    // 작은 프로필 창 이미지를 변경하는데 사용
    private val _profileColor = MutableLiveData<Int>()
    val profileColor: LiveData<Int>
        get() = _profileColor
    private val _profileFace = MutableLiveData<Int>()
    val profileFace: LiveData<Int>
        get() = _profileFace

    private val _memberInfo = MutableLiveData<MemberInfoResponse>()
    val memberInfo: LiveData<MemberInfoResponse>
        get() = _memberInfo

    private val _drawingBitmap = MutableLiveData<Bitmap>()
    val drawingBitmap : LiveData<Bitmap> get() = _drawingBitmap

    private val _drawingMultipartBody = MutableLiveData<MultipartBody.Part>()
    val drawingMultipartBody: LiveData<MultipartBody.Part> get() = _drawingMultipartBody

    // 최종적으로 업로드 결정된 사진url
    private val _uploadingPhotoUrl = MutableLiveData<String>("https://www.freeiconspng.com/uploads/metal-black-red-transparent-background-for-the-symbol-error-5.png")
    val uploadingPhotoUrl : LiveData<String>
        get()  = _uploadingPhotoUrl


    private val _uploadingPhotoId = MutableLiveData<Int>(1)
    val uploadingPhotoId : LiveData<Int>
        get()  = _uploadingPhotoId


    /**
     * 멤버 정보를 가져옵니다.
     */
    suspend fun getMemberInfo(email: String) {
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

    suspend fun editProflie(profileEditRequest: ProfileEditRequest) {
        viewModelScope.launch {
            baseRepository.editMyProfile(profileEditRequest)
                .onSuccess {
                    _memberInfo.value = it
                }
                .onError {
                    Log.d(TAG, "editProflie: ${it.message}")
                }
        }
    }

    fun setProfileImage(color: Int, face: Int) {
        _profileColor.value = color
        _profileFace.value = face
    }

    fun setUploadingPhotoUrl(url : String){
        _uploadingPhotoUrl.value = url
    }

    fun setUploadingPhotoId(id : Int){
        _uploadingPhotoId.value = id
    }

    fun setDrawingMultipart(multipartBody: MultipartBody.Part) {
        _drawingMultipartBody.value = multipartBody
    }

    fun setDrawingBitmap(bitmap : Bitmap){
        _drawingBitmap.value = bitmap
    }


    ////////////////////////// 게임 성공 api 호출 ///////////////////////
    fun plusSilver(){
        viewModelScope.launch {
            baseRepository.plusSilver()
            getMemberInfo(ApplicationClass.sharedPreferences.getString("user_email").toString())
        }
    }

}