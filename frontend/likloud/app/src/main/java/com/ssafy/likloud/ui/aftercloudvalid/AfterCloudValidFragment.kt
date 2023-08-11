package com.ssafy.likloud.ui.aftercloudvalid

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentAfterCloudValidBinding
import com.ssafy.likloud.ui.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

private const val TAG = "AfterCloudValidFragment_싸피"

@AndroidEntryPoint
class AfterCloudValidFragment : BaseFragment<FragmentAfterCloudValidBinding>(
    FragmentAfterCloudValidBinding::bind,
    R.layout.fragment_after_cloud_valid
) {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var navController: NavController

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
        binding.buttonUploadOnly.setOnClickListener {
            Log.d(TAG, "initListener: buttononly")
            navController.navigate(R.id.action_afterCloudValidFragment_to_photoListFragment)
        }

        binding.buttonDrawInstantly.setOnClickListener {
            // 그림판으로 이동
            Log.d(TAG, "initView: draw")
            navController.navigate(R.id.action_afterCloudValidFragment_to_drawingPadFragment)
        }

    }

    fun initView() {

        Log.d(TAG, "initView: ${mainActivityViewModel.uploadingPhotoUrl.value}")

        activity?.let {
            showLoadingDialog(mActivity)
            Glide.with(it)
                .load(mainActivityViewModel.uploadingPhotoUrl.value)
                .addListener(object : RequestListener<Drawable?>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        dismissLoadingDialog()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        dismissLoadingDialog()
                        return false
                    }

                })
                .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                .into(binding.imageChosenPhoto)
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
                if (layoutParams.width > maxWidth) {
                    layoutParams.width = maxWidth
                }
                this.layoutParams = layoutParams
            }

        }
    }
}