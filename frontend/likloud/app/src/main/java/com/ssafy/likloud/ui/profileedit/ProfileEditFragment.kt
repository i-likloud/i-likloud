package com.ssafy.likloud.ui.profileedit

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
import com.ssafy.likloud.data.model.request.ProfileEditRequest
import com.ssafy.likloud.data.model.response.AccessoryResponse
import com.ssafy.likloud.databinding.FragmentProfileBinding
import com.ssafy.likloud.databinding.FragmentProfileEditBinding
import com.ssafy.likloud.ui.profile.ProfileListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>(FragmentProfileEditBinding::bind, R.layout.fragment_profile_edit) {

    private val profileEditFragmentViewModel : ProfileEditFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
//    private lateinit var mActivity: MainActivity

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        mActivity = context as MainActivity
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        init()
        initView()
        initAnimation()
        initListener()
        initAdapter()
    }

    private fun init() {
        viewLifecycleOwner.lifecycleScope.launch {
            profileEditFragmentViewModel.getMyAccessoryList()
        }
        // 현재 내 물방울 모습의 초기값을 불러옵니다.
        profileEditFragmentViewModel.setWaterDropColor(mainActivityViewModel.memberInfo.value!!.profileColor)
        profileEditFragmentViewModel.setWaterDropFace(mainActivityViewModel.memberInfo.value!!.profileFace)
        profileEditFragmentViewModel.setWaterDropAccessory(mainActivityViewModel.memberInfo.value!!.profileAccessory)
    }

    private fun initView() {
        val profileColor = mainActivityViewModel.waterDropColorList[mainActivityViewModel.memberInfo.value!!.profileColor].resourceId
        val profileFace = mainActivityViewModel.waterDropFaceList[mainActivityViewModel.memberInfo.value!!.profileFace].resourceId
        val profileAccessory = mainActivityViewModel.waterDropAccessoryList[mainActivityViewModel.memberInfo.value!!.profileAccessory].resourceId

        setProfileImages(profileColor, profileFace, profileAccessory)
        binding.edittextNickname.setText(mainActivityViewModel.memberInfo.value!!.nickname)
    }
    /**
     * 옵저버를 init합니다
     */
    private fun initObserver() {
        // 내가 가진 악세서리가 불러와지면 리사이클러 뷰로 나타냅니다.
        profileEditFragmentViewModel.myAccessoryList.observe(viewLifecycleOwner) {
            val accessoryListAdapter = AccessoryListAdapter()
            accessoryListAdapter.submitList(it)
            binding.recyclerviewAccessory.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerviewAccessory.adapter = accessoryListAdapter
            accessoryListAdapter.itemClickListener = object: AccessoryListAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, data: AccessoryResponse) {
                    // 선택된 악세서리 기록
                    profileEditFragmentViewModel.setWaterDropAccessory(changeAccessoryNameToInt(data.accessoryName))
                    // 뷰 리소스 갈아끼우기
                    changeWaterDropAccessory(view, mainActivityViewModel.waterDropAccessoryList[changeAccessoryNameToInt(data.accessoryName)].resourceId)
                }
            }
        }
    }

    override fun initListener() {
        binding.buttonChooseDone.setOnClickListener {
            // 프로필 수정
            viewLifecycleOwner.lifecycleScope.launch {
                val color = profileEditFragmentViewModel.selectedColor
                val face = profileEditFragmentViewModel.selectedFace
                val accessory = profileEditFragmentViewModel.selectedAccessory

                mainActivityViewModel.editProflie(ProfileEditRequest(color, face, accessory))
            }
            findNavController().popBackStack()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonTakeOff.setOnClickListener {
            profileEditFragmentViewModel.setWaterDropAccessory(changeAccessoryNameToInt("empty"))
            changeWaterDropAccessory(binding.imageAccessoryNow, R.drawable.water_drop_item_empty)
        }

        binding.buttonEditNickname.setOnClickListener {
            val nickname = binding.edittextNickname.text.toString()

            lifecycleScope.launch {
                if (mainActivityViewModel.editNickname(nickname)) {
                    showSnackbar(binding.root, "info","닉네임이 변경되었습니다.")
                } else {
                    showSnackbar(binding.root, "fail", "사용중인 닉네임입니다.")
                }
            }
        }
    }

    /**
     * 리스트 어뎁터를 init합니다.
     */
    private fun initAdapter() {
        // 물방울 색 리사이클러 뷰 init
        val colorListAdapter = ProfileListAdapter()
        colorListAdapter.submitList(mainActivityViewModel.waterDropColorList)
        binding.recyclerviewColor.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewColor.adapter = colorListAdapter
        colorListAdapter.itemClickListener = object: ProfileListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                profileEditFragmentViewModel.setWaterDropColor(position)
                changeWaterDropColor(view, mainActivityViewModel.waterDropColorList[position].resourceId)
            }
        }

        // 물방울 얼굴 리사이클러 뷰 init
        val faceListAdapter = ProfileListAdapter()
        faceListAdapter.submitList(mainActivityViewModel.waterDropFaceList)
        binding.recyclerviewFace.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewFace.adapter = faceListAdapter
        faceListAdapter.itemClickListener = object: ProfileListAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                profileEditFragmentViewModel.setWaterDropFace(position)
                changeWaterDropFace(view, mainActivityViewModel.waterDropFaceList[position].resourceId)
            }
        }
    }

    /**
     * 애니메이션을 init합니다.
     */
    private fun initAnimation() {
        binding.layoutMyCharacter.animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotation)
    }

    /**
     * 클릭했을 때 애니메이션을 구성합니다.
     */
    private fun clickedAnimation(view: View) {
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
        binding.imageColorNow.setImageResource(colorDrawable)
        clickedAnimation(view)
    }
    /**
     * 현재 선택된 물방울 얼굴을 바꾸고, 애니메이션을 넣습니다.
     */
    private fun changeWaterDropFace(view: View, faceDrawable: Int) {
        binding.imageFaceNow.setImageResource(faceDrawable)
        clickedAnimation(view)
    }
    /**
     * 현재 선택된 물방울 악세서리를 바꾸고, 애니메이션을 넣습니다.
     */
    private fun changeWaterDropAccessory(view: View, accessoryDrawable: Int) {
        binding.imageAccessoryNow.setImageResource(accessoryDrawable)
        clickedAnimation(view)
    }

    /**
     * 프로필 이미지를 set 합니다.
     */
    private fun setProfileImages(color: Int, face: Int, accessory: Int) {
        binding.imageColorNow.setImageResource(color)
        binding.imageFaceNow.setImageResource(face)
        binding.imageAccessoryNow.setImageResource(accessory)
    }

    private fun changeAccessoryNameToInt(name: String): Int {
        when(name) {
            "duck_mouse" -> {
                return 1
            }
            "shine" -> {
                return 2
            }
            "mustache" -> {
                return 3
            }
            "sunglass" -> {
                return 4
            }
            "umbrella" -> {
                return 5
            }
        }
        return 0
    }

    override fun onPause() {
        super.onPause()

    }
}