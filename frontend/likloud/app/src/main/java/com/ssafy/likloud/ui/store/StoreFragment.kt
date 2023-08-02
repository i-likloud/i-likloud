package com.ssafy.likloud.ui.store

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentMypageBinding
import com.ssafy.likloud.databinding.FragmentStoreBinding
import com.ssafy.likloud.ui.mypage.MypageFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>(FragmentStoreBinding::bind, R.layout.fragment_store) {

    private val storeFragmentViewModel: StoreFragmentViewModel by viewModels()
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

        initView()
        initListener()
        initAnimation()
    }

    private fun initView() {
        val profileColor = mainActivityViewModel.waterDropColorList[mainActivityViewModel.memberInfo.value!!.profileColor].resourceId
        val profileFace = mainActivityViewModel.waterDropFaceList[mainActivityViewModel.memberInfo.value!!.profileFace].resourceId
        val profileAccessory = mainActivityViewModel.waterDropAccessoryList[mainActivityViewModel.memberInfo.value!!.profileAccessory].resourceId

        setProfileImages(profileColor, profileFace, profileAccessory)
    }

    override fun initListener() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        // 안드로이드 뒤로가기 버튼 눌렀을 때
        mActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mActivity.changeProfileLayoutVisible()
                    findNavController().popBackStack()
                }
            }
        )

        binding.accessoryDuckmouse.setOnClickListener {
            changeWaterDropAccessory(it, mainActivityViewModel.waterDropAccessoryList[1].resourceId)
        }
        binding.accessoryShine.setOnClickListener {
            changeWaterDropAccessory(it, mainActivityViewModel.waterDropAccessoryList[2].resourceId)
        }
        binding.accessoryMustache.setOnClickListener {
            changeWaterDropAccessory(it, mainActivityViewModel.waterDropAccessoryList[3].resourceId)
        }
        binding.accessorySunglass.setOnClickListener {
            changeWaterDropAccessory(it, mainActivityViewModel.waterDropAccessoryList[4].resourceId)
        }
        binding.accessoryUmbrella.setOnClickListener {
            changeWaterDropAccessory(it, mainActivityViewModel.waterDropAccessoryList[5].resourceId)
        }

    }

    private fun initAnimation() {
        binding.layoutMyCharacter.animation = AnimationUtils.loadAnimation(mActivity, R.anim.shake_up_down_in_store)
    }

    private fun setProfileImages(color: Int, face: Int, accessory: Int) {
        binding.imageColorNow.setImageResource(color)
        binding.imageFaceNow.setImageResource(face)
        binding.imageAccessoryNow.setImageResource(accessory)
    }

    private fun changeWaterDropAccessory(view: View, accessoryDrawable: Int) {
        binding.imageAccessoryNow.setImageResource(accessoryDrawable)
        clickedAnimation(view)
    }

    /**
     * 클릭했을 때 애니메이션을 구성합니다.
     */
    private fun clickedAnimation(view: View) {
        when (view.id) {
            R.id.accessory_duckmouse -> {
                makeAnimationSwingY(binding.imageFaceNow, 20f)
                makeAnimationSwingY(binding.imageAccessoryNow, 20f)
            }
            R.id.accessory_mustache -> {
                makeAnimationSwingY(binding.imageFaceNow, 20f)
                makeAnimationSwingY(binding.imageAccessoryNow, 20f)
            }
            R.id.accessory_sunglass -> {
                makeAnimationSwingY(binding.imageFaceNow, 20f)
                makeAnimationSwingY(binding.imageAccessoryNow, 20f)
            }
            else -> {
                makeAnimationSwingY(binding.imageFaceNow, 20f)
            }
        }
    }

    /**
     * 뷰에 위아래로 두번 흔드는듯한 애니메이션을 적용시킵니다.
     */
    private fun makeAnimationSwingY(view: View, values: Float) {
        CoroutineScope(Dispatchers.Main).launch {
            makeAnimationY(view, values)
            delay(200)
            makeAnimationY(view, 0f)
            delay(200)
            makeAnimationY(view, values)
            delay(200)
            makeAnimationY(view, 0f)
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
}