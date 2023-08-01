package com.ssafy.likloud.ui.mypage

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
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage) {

    private val mypageFragmentViewModel : MypageFragmentViewModel by viewModels()
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
        mActivity.changeProfileLayoutInvisible()
        val profileColor = mainActivityViewModel.waterDropColorList[mainActivityViewModel.memberInfo.value!!.profileColor].resourceId
        val profileFace = mainActivityViewModel.waterDropFaceList[mainActivityViewModel.memberInfo.value!!.profileFace].resourceId
        val profileAccessory = mainActivityViewModel.waterDropAccessoryList[mainActivityViewModel.memberInfo.value!!.profileAccessory].resourceId
        setProfileImages(profileColor, profileFace, profileAccessory)
        binding.textviewNickname.text = mainActivityViewModel.memberInfo.value!!.nickname
        binding.textviewTicketCnt.text = mainActivityViewModel.memberInfo.value!!.goldCoin.toString()
        binding.textviewStampCnt.text = mainActivityViewModel.memberInfo.value!!.silverCoin.toString()
    }

    override fun initListener() {
        binding.buttonBack.setOnClickListener {
            mActivity.changeProfileLayoutVisible()
            findNavController().popBackStack()
        }
    }

    private fun initAnimation() {
        binding.layoutMyCharacter.animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotation)
    }

    private fun setProfileImages(color: Int, face: Int, accessory: Int) {
        binding.imageColorNow.setImageResource(color)
        binding.imageFaceNow.setImageResource(face)
        binding.imageAccessoryNow.setImageResource(accessory)
    }
}