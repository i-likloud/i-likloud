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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.response.AccessoryResponse
import com.ssafy.likloud.data.model.response.StoreItemResponse
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentMypageBinding
import com.ssafy.likloud.databinding.FragmentStoreBinding
import com.ssafy.likloud.ui.mypage.MypageFragmentViewModel
import com.ssafy.likloud.ui.profileedit.AccessoryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>(FragmentStoreBinding::bind, R.layout.fragment_store) {

    private val storeFragmentViewModel: StoreFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mActivity: MainActivity

    private lateinit var storeAccessoryListAdapter: StoreAccessoryListAdapter

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

        initAdapter()
        initObserver()
        init()
        initView()
        initListener()
        initAnimation()
    }

    private fun init() {
        viewLifecycleOwner.lifecycleScope.launch {
            storeFragmentViewModel.getStoreAccessoryList()
        }
    }

    private fun initView() {

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

        binding.buttonChooseDone.setOnClickListener {
            findNavController().popBackStack()
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

    private fun initObserver() {
        // 내가 가진 악세서리가 불러와지면 리사이클러 뷰로 나타냅니다.
        storeFragmentViewModel.storeAccessoryList.observe(viewLifecycleOwner) {
            storeAccessoryListAdapter.submitList(it)
        }

        // 내 정보
        storeFragmentViewModel.memberInfo.observe(viewLifecycleOwner) {
            // 내 프로필 이미지 셋
            val profileColor = mainActivityViewModel.waterDropColorList[it.profileColor].resourceId
            val profileFace = mainActivityViewModel.waterDropFaceList[it.profileFace].resourceId
            val profileAccessory = mainActivityViewModel.waterDropAccessoryList[it.profileAccessory].resourceId
            setProfileImages(profileColor, profileFace, profileAccessory)

            // 티켓 수, 스탬프 수 셋
            binding.textviewStampCnt.text = it.silverCoin.toString()
            binding.textviewTicketCnt.text = it.goldCoin.toString()
        }
    }
    private fun initAdapter() {
        storeAccessoryListAdapter = StoreAccessoryListAdapter()
        binding.recyclerviewStore.layoutManager = GridLayoutManager(mActivity, 3, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewStore.adapter = storeAccessoryListAdapter
        storeAccessoryListAdapter.apply {
            itemClickListener = object: StoreAccessoryListAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, data: StoreItemResponse) {
                    // 뷰 리소스 갈아끼우기
                    changeWaterDropAccessory(view, mainActivityViewModel.waterDropAccessoryList[changeAccessoryNameToInt(data.accessoryName)].resourceId)
                    clickedAnimation(data.accessoryName)
                    binding.lottieChoose.playAnimation()
                }
            }
            itemBuyClickLitener = object: StoreAccessoryListAdapter.ItemBuyClickLitener {
                override fun onClick(data: StoreItemResponse, lottieView: LottieAnimationView) {
                    if (storeFragmentViewModel.memberInfo.value!!.goldCoin >= data.accessoryPrice) {
                        storeFragmentViewModel.postBuyAccessory(data.storeId)
                        lottieView.visibility = View.VISIBLE
                        lottieView.playAnimation()
                        showSnackbar(binding.root, "success", "구매 완료!")
                    }
                    else {
                        showSnackbar(binding.root, "fail", "티켓이 ${data.accessoryPrice - storeFragmentViewModel.memberInfo.value!!.goldCoin} 만큼 부족해요.")
                    }
                }
            }
        }
    }

    private fun changeWaterDropAccessory(view: View, accessoryDrawable: Int) {
        binding.imageAccessoryNow.setImageResource(accessoryDrawable)
    }

    /**
     * 클릭했을 때 애니메이션을 구성합니다.
     */
    private fun clickedAnimation(name: String) {
        when (name) {
            "duck_mouse" -> {
                makeAnimationSwingY(binding.imageFaceNow, 20f)
                makeAnimationSwingY(binding.imageAccessoryNow, 20f)
            }
            "mustache" -> {
                makeAnimationSwingY(binding.imageFaceNow, 20f)
                makeAnimationSwingY(binding.imageAccessoryNow, 20f)
            }
            "sunglass" -> {
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
}