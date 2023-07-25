package com.ssafy.likloud.ui.login

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.android.volley.ClientError
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.launcher
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.ssafy.likloud.MainActivity
//import com.kakao.sdk.auth.model.OAuthToken
//import com.kakao.sdk.common.model.ClientError
//import com.kakao.sdk.common.model.ClientErrorCause
//import com.kakao.sdk.user.UserApiClient
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentLoginBinding
import com.ssafy.likloud.example.ExampleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment_싸피"
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {

    private val loginFragmentViewModel : LoginFragmentViewModel by viewModels()
    private lateinit var mActivity: MainActivity

    private lateinit var OAUTH_CLIENT_ID: String
    private lateinit var OAUTH_CLIENT_SECRET: String
    private lateinit var OAUTH_CLIENT_NAME: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OAUTH_CLIENT_ID = "db3rVaNqaT6ViKz6geU3"
        OAUTH_CLIENT_SECRET = "Vc24r3C8eP"
        OAUTH_CLIENT_NAME = "운이 좋아"

        init()
        initListener()
    }

    /**
     * 필요한 정보를 init 합니다.
     */
    private fun init() {
//        NaverIdLoginSDK.initialize(mActivity, R.string.naver_oauth_client_id.toString(), R.string.naver_oauth_client_secret.toString(), R.string.naver_oauth_client_name.toString())
        NaverIdLoginSDK.initialize(mActivity, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)
        binding.buttonNaverLogin.setOAuthLogin(naverLoginLauncher)
    }

    /**
     * 클릭 리스너를 init합니다.
     */
    private fun initListener() {
        binding.kakaoLoginBtn.setOnClickListener {
//            startKakaoLogin()
        }
    }

//    /**
//     * 카카오 로그인을 시작합니다.
//     */
//    private fun startKakaoLogin(context: Context =requireContext()) {
//
//    // 카카오계정으로 로그인 공통 callback 구성
//    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
//            } else if (token != null) {
//                Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 accesstoken ${token.accessToken}")
//                Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 refreshtoken ${token.refreshToken}")
//                Log.i(ContentValues.TAG, "카카오톡: ${token.scopes?.get(2)}")
//                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//            }
//        }
//
//        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
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
//                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//                } else if (token != null) {
//                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 accesstoken ${token.accessToken}")
//                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 refreshtoken ${token.refreshToken}")
//                    Log.i(ContentValues.TAG, "카카오톡: ${token.scopes?.get(2)}")
//                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//                }
//            }
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//        }
//    }

    /**
     * 네이버 로그인 런처입니다.
     */
    private val naverLoginLauncher = registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result ->
        when(result.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d(TAG, ": AT = ${NaverIdLoginSDK.getAccessToken()}")
                Log.d(TAG, ": RT = ${NaverIdLoginSDK.getRefreshToken()}")
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//                binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
//                binding.tvType.text = NaverIdLoginSDK.getTokenType()
//                binding.tvState.text = NaverIdLoginSDK.getState().toString()
//                binding.tvLoginInfo.text = NidOAuthLogin().callProfileApi(object :
//                    NidProfileCallback<NidProfileResponse> {
//                    override fun onSuccess(response: NidProfileResponse) {
//                        binding.tvLoginInfo.text = response.toString()
//                    }
//
//                    override fun onFailure(httpStatus: Int, message: String) {
//                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                        binding.tvLoginInfo.text = ""
//                    }
//
//                    override fun onError(errorCode: Int, message: String) {
//                        onFailure(errorCode, message)
//                    }
//                }).toString()

//                Toast.makeText(this, "id : ${NidOAuthLogin()}", Toast.LENGTH_SHORT).show()
            }
            AppCompatActivity.RESULT_CANCELED -> {
                // 실패 or 에러
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                showCustomToast("errorCode:$errorCode, errorDesc:$errorDescription")
            }
        }
    }

}