package com.ssafy.likloud.ui.aftercloudvalid

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentAfterCloudValidBinding
import com.ssafy.likloud.ui.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        initView()
        initListener()
    }

    /**
     * 클릭 리스너를 init합니다.
     */
    override fun initListener() {

    }

    fun initView(){
        binding.buttonUploadOnly.setText(getString(R.string.upload_only))
        binding.buttonDrawInstantly.setText(getString(R.string.draw_instantly))
    }

}