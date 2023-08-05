package com.ssafy.likloud

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.AsyncPlayer
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ssafy.likloud.base.BaseActivity
import com.ssafy.likloud.data.repository.BaseRepository
import com.ssafy.likloud.databinding.ActivityMainBinding
import com.ssafy.likloud.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var baseRepository: BaseRepository
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var mediaPlayer: MediaPlayer

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserver()
        initView()
        initNavController()
        initListener()

        mediaPlayer = MediaPlayer.create(this,R.raw.idokay_the_cycle_continues)
        mediaPlayer.start()
    }

    fun toggleMusic() {
        mediaPlayer.apply {
            if (isPlaying){
                pause()
                mainActivityViewModel.setToggleButtonText(getString(R.string.bgm_on))
            }
            else {
                start()
                mainActivityViewModel.setToggleButtonText(getString(R.string.bgm_off))
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
        lifecycleScope.launch{
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
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.mapFragment -> showBottomNav()
//                R.id.postListFragment -> showBottomNav()
//                R.id.myPageFragment -> showBottomNav()
//                else -> hideBottomNav()
//            }
//        }
//        binding.bottomNavi.setupWithNavController(navController)
//
//        // 중복터치 막기!!
//        binding.bottomNavi.setOnItemReselectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.mapFragment -> {}
//                R.id.postListFragment -> {}
//                R.id.myPageFragment -> {}
//            }
//        }
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
}