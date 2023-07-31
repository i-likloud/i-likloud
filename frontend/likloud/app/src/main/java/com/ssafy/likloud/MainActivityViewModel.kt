package com.ssafy.likloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    val waterDropColorList: List<Int> = listOf(
        R.drawable.profile_water_drop_white,
        R.drawable.profile_water_drop_purple,
        R.drawable.profile_water_drop_mint,
        R.drawable.profile_water_drop_blue,
        R.drawable.profile_water_drop_orange,
        R.drawable.profile_water_drop_red,
        R.drawable.profile_water_drop_green,
        R.drawable.profile_water_drop_pink,
        R.drawable.profile_water_drop_lemon,
    )

    val waterDropFaceList: List<Int> = listOf(
        R.drawable.face_normal,
        R.drawable.face_smile,
        R.drawable.face_ggiu,
    )

    // 작은 프로필 창 이미지를 변경하는데 사용
    private val _profileColor = MutableLiveData<Int>()
    val profileColor: LiveData<Int>
        get() = _profileColor
    private val _profileFace = MutableLiveData<Int>()
    val profileFace: LiveData<Int>
        get() = _profileFace

    fun setProfileImage(color: Int, face: Int) {
        _profileColor.value = color
        _profileFace.value = face
    }
}