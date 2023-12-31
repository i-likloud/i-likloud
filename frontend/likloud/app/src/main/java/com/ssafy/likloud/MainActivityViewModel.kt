package com.ssafy.likloud

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.ImageTempDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.request.LoginRequest
import com.ssafy.likloud.data.model.request.MemberInfoRequest
import com.ssafy.likloud.data.model.request.ProfileEditRequest
import com.ssafy.likloud.data.model.response.LoginResponse
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.IOError
import java.io.IOException
import java.lang.NullPointerException
import java.net.ConnectException
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
    val drawingBitmap: LiveData<Bitmap> get() = _drawingBitmap

    private val _drawingMultipartBody = MutableLiveData<MultipartBody.Part>()
    val drawingMultipartBody: LiveData<MultipartBody.Part> get() = _drawingMultipartBody

    // 최종적으로 업로드 결정된 사진url
    private val _uploadingPhotoUrl =
        MutableLiveData<String>("https://www.freeiconspng.com/uploads/metal-black-red-transparent-background-for-the-symbol-error-5.png")
    val uploadingPhotoUrl: LiveData<String>
        get() = _uploadingPhotoUrl


    private val _uploadingPhotoId = MutableLiveData<Int>(1)
    val uploadingPhotoId: LiveData<Int>
        get() = _uploadingPhotoId


    private val _toggleBgmString = MutableLiveData<String>("BGM OFF")
    val toggleBgmString: LiveData<String>
        get() = _toggleBgmString


    private val _fcmReceivedDrawingId = MutableLiveData<Int>()
    val fcmReceivedDrawingId: LiveData<Int> get() = _fcmReceivedDrawingId

    /////////////// 로그인 프레그먼트에서 사용 ///////////////
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?>
        get() = _loginResponse


    /**
     * 멤버 정보를 가져옵니다.
     */
    @Throws(IOException::class)
    fun getMemberInfo(email: String) {
//        try {
            viewModelScope.launch {
    //            baseRepository.getMemberInfo(MemberInfoRequest(email)).apply {
    //                onSuccess {
    //                    Log.d(TAG, "getUserInfo: onSuccess ${it}")
    //                    _memberInfo.postValue(it)
    //                }
    //                onError {
    //                    Log.d(TAG, "getUserInfo: ${it.message}")
    //                }
    //            }
                baseRepository.getMemberInfo2().apply {
                    onSuccess {
                        Log.d(TAG, "getMemberInfo: 에러가 안떴어요.")
                        Log.d(TAG, "getUserInfo: onSuccess ${it}")
                        _memberInfo.postValue(it)
                    }
                    onError {
                        Log.d(TAG, "getUserInfo: 메인 액티비티에서 유저 정보 가져오는데 실패했습니다. ${it}")
                        when (it.message) {
                            "refresh_exception" -> {
                                Log.d(TAG, "getMemberInfo: 리프레시 오류! shared 비우고 다시 로그인 해야합니다.")
                                setRefreshInvaild()
                            }
                            "required_re_login" -> {
                                Log.d(TAG, "getMemberInfo: required_re_login")
                                setRefreshInvaild()
                            }
                        }
                        
                        if (it is NullPointerException) {
                            Log.d(TAG, "getMemberInfo: 로그인 버그 -> 다시 로그인 해야함")
                            setRefreshInvaild()
                        }
//                        throw IOException("refresh_exception")
                    }
                }
            }
//        }
//        catch (e: IOException) {
//            Log.d(TAG, "getMemberInfo: 익셉션 받았습니다. ${e.message}")
////            sharedPreferences.putString(X_ACCESS_TOKEN, "")
////            sharedPreferences.putString(X_REFRESH_TOKEN, "")
////            sharedPreferences.putString(USER_EMAIL, "")
//            throw IOException("refresh_exception")
//        } catch (e: ConnectException) {
//            Log.d(TAG, "getMemberInfo: 익셉션 받았습니다. ${e.message}")
//        }
    }

    fun editProflie(profileEditRequest: ProfileEditRequest) {
        viewModelScope.launch {
            baseRepository.editMyProfile(profileEditRequest)
                .onSuccess {
                    getMemberInfo(sharedPreferences.getString(USER_EMAIL).toString())
                }
                .onError {
                    Log.d(TAG, "editProflie: ${it.message}")
                }
        }
    }

    suspend fun editNickname(nickname: String): Boolean {
        var isPossible = false
        baseRepository.editNickname(nickname)
            .onSuccess {
                getMemberInfo(sharedPreferences.getString(USER_EMAIL).toString())
                isPossible = true
            }
            .onError {
                Log.d(TAG, "editNickname: ${it}")
                isPossible = false
            }
        return isPossible
    }

    fun logOutUser(){
        _loginResponse.value = null
    }

    fun setProfileImage(color: Int, face: Int) {
        _profileColor.value = color
        _profileFace.value = face
    }

    fun setUploadingPhotoUrl(url: String) {
        _uploadingPhotoUrl.value = url
    }

    fun setUploadingPhotoId(id: Int) {
        _uploadingPhotoId.value = id
    }

    fun setDrawingMultipart(multipartBody: MultipartBody.Part) {
        _drawingMultipartBody.value = multipartBody
    }

    fun setDrawingBitmap(bitmap: Bitmap) {
        _drawingBitmap.value = bitmap
    }


    ////////////////////////// 게임 성공 api 호출 ///////////////////////
    fun plusSilver() {
        viewModelScope.launch {
            baseRepository.plusSilver().onSuccess {
                getMemberInfo(ApplicationClass.sharedPreferences.getString("user_email").toString())
            }
        }
    }

    /////////////////////////// 선물 성공 /////////////////////////////////////////
    private var _isSended = MutableLiveData<Boolean>(false)
    val isSended: LiveData<Boolean>
        get() = _isSended
    fun setIsSended(value: Boolean){
        _isSended.value = value
    }
    fun sendGift(nftId: Int, toMemberId: Int, message: String){
        viewModelScope.launch {
            baseRepository.sendGift(nftId, toMemberId, message).onSuccess {
                _isSended.value = true
            }
        }
    }

    ///////////////////// 지갑 발급 여부 ///////////////////////////
//    private var _isWallet = MutableLiveData<Boolean>()
//    val isWallet: LiveData<Boolean>
//        get() = _isWallet
//    fun getNftWallet(){
//        viewModelScope.launch {
//            baseRepository.getNftWallet().onSuccess {
//                _isWallet.value = true
//            }
//        }
//    }

    private var _isWallet = MutableSharedFlow<Boolean>()
    val isWallet: SharedFlow<Boolean>
        get() = _isWallet
    fun getNftWallet(){
        viewModelScope.launch {
            baseRepository.getNftWallet().onSuccess {
                _isWallet.emit(true)
            }
        }
    }

    var isLogined = false

    private var _isRefreshTokenInvalid = MutableSharedFlow<Boolean>()
    val isRefreshTokenInvalid: SharedFlow<Boolean>
        get() = _isRefreshTokenInvalid

    fun setRefreshInvaild() {
        viewModelScope.launch {
            _isRefreshTokenInvalid.emit(true)
        }
    }


    fun postLogin(email: String, socialType: String) {
        Log.d(TAG, "postLogin: 포스트 로그인 시도입니다. (메인 액티비티 뷰 모델)")
        viewModelScope.launch {
            Log.d(TAG, "postLogin: ddddd")
            baseRepository.postLogin(
                LoginRequest(
                    email, socialType, sharedPreferences.getString(
                        ApplicationClass.FIREBASE_TOKEN
                    )?: "firebase_token_not_registered"
                )
            ).onSuccess {
                _loginResponse.value = it
                sharedPreferences.putString(X_ACCESS_TOKEN, it.accessToken)
                Log.d(TAG, "postLogin 찐 refresh: ${it.refreshToken}")
                sharedPreferences.putString(X_REFRESH_TOKEN, it.refreshToken)
            }.onError {
                Log.d(TAG, "postLogin: 에러가 났슴니다 에러메시지는? ${it.message}")
            }
        }
    }
}