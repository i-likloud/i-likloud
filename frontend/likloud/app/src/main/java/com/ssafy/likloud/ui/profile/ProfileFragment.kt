package com.ssafy.likloud.ui.profile

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
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
        initAdapter()
    }

    /**
     * 옵저버를 init합니다
     */
    private fun initObserver() {

    }

    override fun initListener() {
        binding.buttonChooseDone.setOnClickListener {
            val nickname = binding.edittextNickname.text.toString()

            if (nickname.isEmpty()) {
                showCustomToast("닉네임을 입력해주세요!")
            } else {
                // 추가 정보 선택 완료시 진짜 키 받아오는 로직
                viewLifecycleOwner.lifecycleScope.launch {
                    //중단 함수로 만들었음. patch 메소드가 통신이 끝나야 다음 메소드 실행
                    if (profileFragmentViewModel.patchAdditionalInfo(LoginAdditionalRequest(nickname, profileFragmentViewModel.selectedWaterDropColor, profileFragmentViewModel.selectedWaterDropFace, 0))) {
                        mainActivityViewModel.getMemberInfo(sharedPreferences.getString(USER_EMAIL).toString())
                        showSnackbar(binding.root, "info", "뭉게뭉게 도화지에 오신것을 환영합니다!")
                    } else {
                        showSnackbar(binding.root, "fail", "동일한 닉네임이 존재합니다. 다시 입력해주세요!")
                    }
                    //비동기로 메인액티비티에 멤버정보 불러온다.
                }
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            }
        }
    }

    /**
     * 리스트 어뎁터를 init합니다.
     */
    private fun initAdapter() {
        val colorListAdapter = ProfileListAdapter()
        colorListAdapter.submitList(mainActivityViewModel.waterDropColorList)
        binding.recyclerviewColor.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewColor.adapter = colorListAdapter
        colorListAdapter.itemClickListener = object: ProfileListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                changeWaterDropColor(view, mainActivityViewModel.waterDropColorList[position].resourceId)
                profileFragmentViewModel.selectedWaterDropColor = mainActivityViewModel.waterDropColorList[position].num
            }
        }
        val faceListAdapter = ProfileListAdapter()
        faceListAdapter.submitList(mainActivityViewModel.waterDropFaceList)
        binding.recyclerviewFace.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewFace.adapter = faceListAdapter
        faceListAdapter.itemClickListener = object: ProfileListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                changeWaterDropFace(view, mainActivityViewModel.waterDropFaceList[position].resourceId)
                profileFragmentViewModel.selectedWaterDropFace = mainActivityViewModel.waterDropFaceList[position].num
            }
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
        val nowProfileColor = binding.imageColorNow
        val nowProfileFace = binding.imageFaceNow

//        nowProfileImage.scaleX = 0.1f
//        nowProfileImage.scaleY = 0.1f

//        makeAnimationScale(nowProfileImage, 1f)
        makeAnimationSwingY(nowProfileFace, 20f)
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
        binding.imageColorNow.setImageResource(colorDrawable)
        clickedAnimation(view)
    }
    private fun changeWaterDropFace(view: View, colorDrawable: Int) {
        binding.imageFaceNow.setImageResource(colorDrawable)
        clickedAnimation(view)
    }

    override fun onPause() {
        super.onPause()

    }
}