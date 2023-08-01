package com.ssafy.likloud.ui.aftercloudvalid

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnLayout
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

private const val TAG = "AfterCloudValidFragment_싸피"
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
        binding.buttonUploadOnly.setOnClickListener{
            // 그림 목록으로 이동

        }

        binding.buttonDrawInstantly.setOnClickListener {
            // 그림판으로 이동
        }
    }

    fun initView(){
        binding.buttonUploadOnly.setText(getString(R.string.upload_only))
        binding.buttonDrawInstantly.setText(getString(R.string.draw_instantly))


        Log.d(TAG, "initView: ${mainActivityViewModel.uploadingPhotoUrl.value}")

        activity?.let {
            Glide.with(it)
                .load(mainActivityViewModel.uploadingPhotoUrl.value)
                .into(binding.imageChosenPhoto)


            val maxWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._11sdp)
            binding.imageChosenPhoto.doOnLayout {
                if (it.width > maxWidth) {
                    val layoutParams = it.layoutParams
                    layoutParams.width = maxWidth
                    it.layoutParams = layoutParams
                }
            }
        }
    }

    /**
     * maxWidth 설정 함수... 잘안됨
     */
    private fun initImageMaxWidth() {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val maxWidth = 1

       binding.imageChosenPhoto.apply {
           this.doOnLayout {
               val layoutParams = this.layoutParams
               if(layoutParams.width>maxWidth){
                   layoutParams.width = maxWidth
               }
               this.layoutParams = layoutParams
           }

       }
    }

}