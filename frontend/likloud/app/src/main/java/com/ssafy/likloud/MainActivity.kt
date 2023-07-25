package com.ssafy.likloud

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavController()
    }

    private fun initNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
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
}