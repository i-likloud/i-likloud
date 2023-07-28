package com.ssafy.likloud.ui.home

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Interpolator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentAfterCloudValidBinding
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentHomeBinding
import com.ssafy.likloud.databinding.FragmentLoginBinding
import com.ssafy.likloud.example.ExampleFragmentViewModel
import com.ssafy.likloud.ui.login.LoginFragmentDirections
import com.ssafy.likloud.ui.login.LoginFragmentViewModel
import com.ssafy.likloud.ui.upload.UploadFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AfterCloudValidFragment : BaseFragment<FragmentAfterCloudValidBinding>(FragmentAfterCloudValidBinding ::bind, R.layout.fragment_after_cloud_valid ) {

    private val homeFragmentViewModel : HomeFragmentViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var mActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)
        initListener()
    }

    /**
     * 클릭 리스너를 init합니다.
     */
    override fun initListener() {
    }

}