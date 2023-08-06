package com.ssafy.likloud

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AsyncPlayer
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.likloud.base.BaseActivity
import com.ssafy.likloud.data.repository.BaseRepository
import com.ssafy.likloud.databinding.ActivityMainBinding
import com.ssafy.likloud.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivity_싸피"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var baseRepository: BaseRepository
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var mediaPlayer: MediaPlayer

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionList = arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        requestMultiplePermission.launch(permissionList)
        initFCMMessageAccept()

        initObserver()
        initView()
        initNavController()
        initListener()
        Log.d(TAG, "onCreate: oncreated!")

        mediaPlayer = MediaPlayer.create(this, R.raw.summer_shower_quincas_moreira)
        if(ApplicationClass.sharedPreferences.getMusicStatus()==true && !mediaPlayer.isPlaying){
            mediaPlayer.start()
        }

    }

    fun toggleMusic() {
        mediaPlayer.apply {
            if (isPlaying) {
                pause()
                ApplicationClass.sharedPreferences.setMusicOff()
            } else {
                start()
                ApplicationClass.sharedPreferences.setMusicOn()
            }
        }
    }


    private fun initListener() {
        binding.layoutProfile.setOnClickListener {
            when (navController.currentDestination!!.id) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.action_homeFragment_to_mypageFragment)
                }

                R.id.photoListFragment -> {
                    navController.navigate(R.id.action_photoListFragment_to_mypageFragment)
                }

                R.id.drawingListFragment -> {
                    navController.navigate(R.id.action_drawingListFragment_to_mypageFragment)
                }

                R.id.mypageFragment -> {

                }
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainActivityViewModel.memberInfo.observe(this@MainActivity) {
                changeProfileColor(it.profileColor)
                changeProfileFace(it.profileFace)
                changeProfileAccessory(it.profileAccessory)

                // 마이페이지에서는 오른쪽 상단 프로필이 보이면 안된다.
                when (navController.currentDestination!!.id) {
                    R.id.mypageFragment -> {

                    }

                    R.id.storeFragment -> {

                    }

                    else -> {
                        changeProfileLayoutVisible()
                    }
                }
            }
        }
    }

    private fun initView() {
        binding.layoutProfile.translationX = 52f
        binding.layoutProfile.translationY = -52f
        binding.layoutProfile.visibility = View.INVISIBLE
        binding.profileMy.animation = AnimationUtils.loadAnimation(this, R.anim.rotation)
    }

    private fun initNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFCMMessageAccept() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token 정보: ${task.result}")
            if (task.result != null) {
                uploadToken(task.result)
            }
        })
        createNotificationChannel(channel_id, "ssafy")
    }

    // notification 수신 시 foreground에서 notification 생성 위해 channel 생성
    @RequiresApi(Build.VERSION_CODES.O)
    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    companion object {
        private const val RC_SIGN_IN = 9001
        const val channel_id = "ssafy_channel"
        fun uploadToken(token: String) {

        }
    }

    /**
     * 작은 프로필 창 프로필 이미지를 변경합니다.
     */
    fun changeProfileColor(num: Int) {
        binding.profileColor.setImageResource(mainActivityViewModel.waterDropColorList[num].resourceId)
    }

    fun changeProfileFace(num: Int) {
        binding.profileFace.setImageResource(mainActivityViewModel.waterDropFaceList[num].resourceId)
    }

    fun changeProfileAccessory(num: Int) {
        binding.profileAccessory.setImageResource(mainActivityViewModel.waterDropAccessoryList[num].resourceId)
    }

    fun changeProfileLayoutVisible() {
        binding.layoutProfile.visibility = View.VISIBLE
    }

    fun changeProfileLayoutInvisible() {
        binding.layoutProfile.visibility = View.INVISIBLE
    }


    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
            }
        }


}