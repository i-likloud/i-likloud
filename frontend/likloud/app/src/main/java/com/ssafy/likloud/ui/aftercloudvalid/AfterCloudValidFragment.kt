package com.ssafy.likloud.ui.aftercloudvalid

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentAfterCloudValidBinding
import com.ssafy.likloud.ui.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfterCloudValidFragment : BaseFragment<FragmentAfterCloudValidBinding>(FragmentAfterCloudValidBinding ::bind, R.layout.fragment_after_cloud_valid ) {

    private val mainActivityViewModel : MainActivityViewModel by activityViewModels()
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
        initImageMaxWidth()
        Glide.with(requireContext())
            .load(mainActivityViewModel.uploadingPhotoUrl)
            .transform(CenterCrop())
            .into(binding.imageChosenPhoto)
    }

    private fun initImageMaxWidth() {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val maxWidth = (screenWidth * 0.7).toInt()

       binding.layoutChosenPhoto.layoutParams.apply {
           if(width > maxWidth){
               width =maxWidth
           }
       }
    }

}