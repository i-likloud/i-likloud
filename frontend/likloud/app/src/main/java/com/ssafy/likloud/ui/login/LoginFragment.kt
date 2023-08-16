package com.ssafy.likloud.ui.login

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import com.ssafy.likloud.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
import com.ssafy.likloud.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.repository.BaseRepository
import com.ssafy.likloud.data.repository.BaseRepositoryImpl
import com.ssafy.likloud.databinding.FragmentLoginBinding
import com.ssafy.likloud.di.RepositoryModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "LoginFragment_싸피"

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {

    private val loginFragmentViewModel: LoginFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
//    private lateinit var mActivity: MainActivity
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mActivity = context as MainActivity
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: dd")
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: dd")
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        init()
        initListener()

//        viewLifecycleOwner.lifecycleScope.launch {
//            mainActivityViewModel.isTokenReceived.observe(mActivity) {
//                if (it == true) findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: dd")
//        Log.d(TAG, "onResume: ${sharedPreferences.getBoolean("firstLogin") }")
//        if(sharedPreferences.getBoolean("firstLogin") == true){
//            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
//            sharedPreferences.setBoolean("firstLogin", false)
//        }
    }

    /**
     * 필요한 정보를 init 합니다.
     */
    private fun init() {
//        findNavController().clearBackStack(R.id.homeFragment)
//        findNavController().clearBackStack(R.id.loginFragment)
        Log.d(TAG, "init: ${findNavController().backQueue.size}")
//        findNavController().getBackStackEntry(R.id.loginFragment)
        NaverIdLoginSDK.initialize(
            mActivity,
            getString(R.string.OAUTH_CLIENT_ID),
            getString(R.string.OAUTH_CLIENT_SECRET),
            getString(R.string.OAUTH_CLIENT_NAME)
        )
        binding.buttonNaverLogin.setOAuthLogin(naverLoginLauncher)
    }

    /**
     * 클릭 리스너를 init합니다.
     */
    override fun initListener() {
        binding.kakaoLoginBtn.setOnClickListener {
            startKakaoLogin()
        }
        binding.textInfo.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_infoFragment)
        }
    }

    private fun initObserver() {
        mainActivityViewModel.loginResponse.observe(viewLifecycleOwner) {
            sharedPreferences.putString(X_ACCESS_TOKEN, it.accessToken)
            sharedPreferences.putString(X_REFRESH_TOKEN, it.refreshToken)
            Log.d(TAG, "initObserver: ${it.role}")
            when (it.role) {
                "MEMBER" -> {
                    Log.d(TAG, "initObserver: moving to homefragment")
                    viewLifecycleOwner.lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            mainActivityViewModel.getMemberInfo(
                                sharedPreferences.getString(
                                    USER_EMAIL
                                ).toString()
                            )
                            withContext(Dispatchers.Main) {
//                                showSnackbar(binding.root, "success", "안녕하세요 ${mainActivityViewModel.memberInfo.value!!.nickname}님!")
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                        }
                    }
                }

                "GUEST" -> {
//                    showSnackbar(binding.root, "success", "뭉게뭉게 도화지에 오신것을 환영합니다!")
                    Log.d(TAG, "initObserver: moving to profile")
                    findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                }
            }
        }
    }

    /**
     * 네이버 로그인 런처입니다.
     */
    private val naverLoginLauncher =
        registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                    val accessToken = NaverIdLoginSDK.getAccessToken()
                    Log.d(TAG, "naver 로그인 : AT = $accessToken")
                    var email = ""
                    var name = ""
                    NidOAuthLogin().callProfileApi(object :
                        NidProfileCallback<NidProfileResponse> {
                        override fun onSuccess(response: NidProfileResponse) {
                            email = response.profile?.email.toString()
                            name = response.profile?.name.toString()
                            Log.d(TAG, "onSuccess: 네이버 이메일: ${email}, ${name}")

                            // 네이버 accessToken을 넣고
                            sharedPreferences.putString(X_ACCESS_TOKEN, accessToken.toString())
                            // 이메일을 넣고
                            sharedPreferences.putString(USER_EMAIL, email)

                            loginFragmentViewModel.postLogin(email, "NAVER")
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                            email = ""
                        }

                        override fun onError(errorCode: Int, message: String) {
                            onFailure(errorCode, message)
                        }
                    }).toString()
                }

                AppCompatActivity.RESULT_CANCELED -> {
                    // 실패 or 에러
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    showCustomToast("errorCode:$errorCode, errorDesc:$errorDescription")
                }
            }
        }


    /**
     * 카카오 SDK 로그인 구현부입니다.
     */
    private fun startKakaoLogin(context: Context = requireContext()) {
        Log.d(TAG, "startKakaoLogin: clicked")

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 accesstoken ${token.accessToken}")
                Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 refreshtoken ${token.refreshToken}")
//                Log.i(ContentValues.TAG, "카카오톡: ${token.scopes?.get(2)}")
                val email = token.scopes!![0]

//                Log.d(TAG, "startKakaoLogin: 카카오 로그인 이메일은? -> ${email}")
                sharedPreferences.putString(X_ACCESS_TOKEN, token.accessToken)
                sharedPreferences.putString(USER_EMAIL, email)
                ////////// 수정 /////////
                mainActivityViewModel.postLogin("email", "KAKAO")

                sharedPreferences.setBoolean("firstLogin", true)

//                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
//                loginFragmentViewModel.getTokenValidation(token.accessToken)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
//            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
//                if (error != null) {
//                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
//
//                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    }
//
//                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                    UserApiClient.instance.loginWithKakaoAccount(
//                        context,
//                        callback = callback
//                    )
//                    getUserEmailFromKakao()
//                } else if (token != null) {
//                    Log.i(
//                        ContentValues.TAG,
//                        "카카오톡으로 로그인 성공 accesstoken ${token.accessToken}"
//                    )
//                    getUserEmailFromKakao()
//                    sharedPreferences.putString(X_ACCESS_TOKEN, token.accessToken)
//                    loginFragmentViewModel.postLogin("email", "KAKAO")
////                    findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
//                }
//            }
//        }
//        else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        getUserEmailFromKakao()
    }

    fun getUserEmailFromKakao() {
        UserApiClient.instance.me { user, error ->
            val userEmail = user?.kakaoAccount?.email
            userEmail?.let {
                Log.d(TAG, "getUserEmailFromKakao: 카카오 로그인 이메일 -> $it")
                sharedPreferences.putString(USER_EMAIL, userEmail)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: dd")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: dd")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: dd")
    }
}



