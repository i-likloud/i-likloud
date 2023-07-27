package com.ssafy.likloud.ui.profile

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::bind, R.layout.fragment_profile) {

    private val profileFragmentViewModel : ProfileFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAnimation()
        initListener()
    }

    override fun initListener() {
        binding.waterDropWhite.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[0])
        }
        binding.waterDropPurple.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[1])
        }
        binding.waterDropMint.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[2])
        }
        binding.waterDropBlue.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[3])
        }
        binding.waterDropOrange.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[4])
        }
        binding.waterDropRed.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[5])
        }
        binding.waterDropGreen.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[6])
        }
        binding.waterDropPink.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[7])
        }
        binding.waterDropLemon.setOnClickListener {
            changeWaterDrop(it, mainActivityViewModel.waterDropColorList[8])
        }
    }

    /**
     * 애니메이션을 init합니다.
     */
    private fun initAnimation() {
        binding.imageProfileNow.animation = AnimationUtils.loadAnimation(mActivity, R.anim.shake_up_down)
    }

    /**
     * 클릭했을 때 애니메이션을 구성합니다.
     */
    private fun clickedAnimation(view: View) {
        val nowProfileImage = binding.imageProfileNow

        nowProfileImage.scaleX = 0.1f
        nowProfileImage.scaleY = 0.1f

        makeAnimationScale(nowProfileImage, 1f)
        makeAnimationSpringY(view, -20f)
    }

    /**
     * 뷰에 위아래로 튀는듯한 애니메이션을 적용시킵니다.
     */
    private fun makeAnimationSpringY(view: View, values: Float) {
        CoroutineScope(Dispatchers.Main).launch {
            makeAnimationY(view, values)
            delay(250)
            makeAnimationY(view, 0f)
        }
    }

    /**
     * 뷰에 크기를 조절하는 애니메이션을 적용시킵니다.
     */
    private fun makeAnimationScale(view: View, values: Float) {
        ObjectAnimator.ofFloat(view, "scaleX", values).apply {
            duration = 500
            start()
        }
        ObjectAnimator.ofFloat(view, "scaleY", values).apply {
            duration = 500
            start()
        }
    }

    /**
     * 뷰에 Y축으로 움직이는 애니메이션을 적용시킵니다.
     */
    private fun makeAnimationY(view: View, values: Float) {
        ObjectAnimator.ofFloat(view, "translationY", values).apply {
            duration = 250
            start()
        }
    }

    /**
     * 현재 선택된 물방울을 바꾸고, 애니메이션을 넣습니다.
     */
    private fun changeWaterDrop(view: View, colorDrawable: Int) {
        binding.imageProfileNow.setImageResource(colorDrawable)
        clickedAnimation(view)
    }
}