package com.ssafy.likloud

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ssafy.likloud.base.BaseActivity
import com.ssafy.likloud.data.repository.BaseRepository
import com.ssafy.likloud.databinding.ActivityMainBinding
import com.ssafy.likloud.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var baseRepository: BaseRepository
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initNavController()
    }

    private fun initView() {
        binding.layoutProfile.translationX = 52f
        binding.layoutProfile.translationY = -52f
        binding.layoutProfile.visibility = View.INVISIBLE
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
        binding.profileColor.setImageResource(mainActivityViewModel.waterDropColorList[num])
    }

    fun changeProfileFace(num: Int) {
        binding.profileFace.setImageResource(mainActivityViewModel.waterDropFaceList[num])
    }

    fun changeProfileLayoutVisible() {
        binding.layoutProfile.visibility = View.VISIBLE
    }
}