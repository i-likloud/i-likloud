package com.ssafy.likloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.likloud.data.model.ImageTempDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

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

    // 작은 프로필 창 이미지를 변경하는데 사용
    private val _profileColor = MutableLiveData<Int>()
    val profileColor: LiveData<Int>
        get() = _profileColor
    private val _profileFace = MutableLiveData<Int>()
    val profileFace: LiveData<Int>
        get() = _profileFace

    // 최종적으로 업로드 결정된 사진url
    private val _uploadingPhotoUrl = MutableLiveData<String>()
    val uploadingPhotoUrl : LiveData<String>
    get()  = _uploadingPhotoUrl

    fun setProfileImage(color: Int, face: Int) {
        _profileColor.value = color
        _profileFace.value = face
    }

    fun setUploadingPhotoUrl(url : String){
        _uploadingPhotoUrl.value = url
    }

}