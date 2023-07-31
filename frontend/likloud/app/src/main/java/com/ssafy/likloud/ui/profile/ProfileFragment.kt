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
import androidx.navigation.fragment.findNavController
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.request.LoginAdditionalRequest
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

    private var selectedWaterDropColor = 0
    private var selectedWaterDropFace = 0

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
        initObserver()
    }

    /**
     * 옵저버를 init합니다
     */
    private fun initObserver() {
        mainActivityViewModel.profileColor.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                mActivity.changeProfileColor(it)
            }
        }
        mainActivityViewModel.profileFace.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                mActivity.changeProfileFace(it)
            }
        }
    }

    override fun initListener() {
        binding.waterDropWhite.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[0])
            selectedWaterDropColor = 0
        }
        binding.waterDropPurple.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[1])
            selectedWaterDropColor = 1
        }
        binding.waterDropMint.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[2])
            selectedWaterDropColor = 2
        }
        binding.waterDropBlue.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[3])
            selectedWaterDropColor = 3
        }
        binding.waterDropOrange.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[4])
            selectedWaterDropColor = 4
        }
        binding.waterDropRed.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[5])
            selectedWaterDropColor = 5
        }
        binding.waterDropGreen.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[6])
            selectedWaterDropColor = 6
        }
        binding.waterDropPink.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[7])
            selectedWaterDropColor = 7
        }
        binding.waterDropLemon.setOnClickListener {
            changeWaterDropColor(it, mainActivityViewModel.waterDropColorList[8])
            selectedWaterDropColor = 8
        }
        binding.imageFaceNormal.setOnClickListener {
            changeWaterDropFace(it, mainActivityViewModel.waterDropFaceList[0])
            selectedWaterDropFace = 0
        }
        binding.imageFaceSmile.setOnClickListener {
            changeWaterDropFace(it, mainActivityViewModel.waterDropFaceList[1])
            selectedWaterDropFace = 1
        }
        binding.imageFaceQqiu.setOnClickListener {
            changeWaterDropFace(it, mainActivityViewModel.waterDropFaceList[2])
            selectedWaterDropFace = 2
        }
        binding.buttonChooseDone.setOnClickListener {
            mainActivityViewModel.setProfileImage(selectedWaterDropColor, selectedWaterDropFace)
            val nickname = binding.edittextNickname.text.toString()
            // 추가 정보 선택 완료시 진짜 키 받아오는 로직
            profileFragmentViewModel.patchAdditionalInfo(LoginAdditionalRequest(nickname, selectedWaterDrop, 0, 0))
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }

    /**
     * 애니메이션을 init합니다.
     */
    private fun initAnimation() {
        binding.layoutSelectedCharacter.animation = AnimationUtils.loadAnimation(mActivity, R.anim.shake_up_down)
    }

    /**
     * 클릭했을 때 애니메이션을 구성합니다.
     */
    private fun clickedAnimation(view: View) {
        val nowProfileImage = binding.imageProfileNow

//        nowProfileImage.scaleX = 0.1f
//        nowProfileImage.scaleY = 0.1f

//        makeAnimationScale(nowProfileImage, 1f)
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
    private fun changeWaterDropColor(view: View, colorDrawable: Int) {
        binding.imageProfileNow.setImageResource(colorDrawable)
        clickedAnimation(view)
    }

    private fun changeWaterDropFace(view: View, colorDrawable: Int) {
        binding.imageFaceNow.setImageResource(colorDrawable)
        clickedAnimation(view)
    }
}