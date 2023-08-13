package com.ssafy.likloud.ui.drawingoriginal

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
import androidx.navigation.fragment.navArgs
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
import com.ssafy.likloud.databinding.FragmentDrawingOriginalBinding
import com.ssafy.likloud.ui.drawing.DrawingDetailFragmentArgs
import com.ssafy.likloud.ui.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

private const val TAG = "AfterCloudValidFragment_싸피"

@AndroidEntryPoint
class DrawingOriginalFragment : BaseFragment<FragmentDrawingOriginalBinding>(
    FragmentDrawingOriginalBinding::bind,
    R.layout.fragment_drawing_original
) {
    private val args: DrawingOriginalFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)
        initView()
        initListener()
    }

    override fun initListener() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun initView() {
        activity?.let {
            showLoadingDialog(mActivity)
            Glide.with(it)
                .load(args.photoUrl)
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
}