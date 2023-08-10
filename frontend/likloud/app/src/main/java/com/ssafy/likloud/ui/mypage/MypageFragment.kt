package com.ssafy.likloud.ui.mypage

import android.animation.ObjectAnimator
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
import com.ssafy.likloud.ApplicationClass.Companion.sharedPreferences
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MypageFragment_싸피"
@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage) {

    private val mypageFragmentViewModel : MypageFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        viewSetting()
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
        initObserver()
    }

    private fun initView() {
        mActivity.changeProfileLayoutInvisible()
        mypageFragmentViewModel.getMyDrawingListDtoList()
    }

    override fun initListener() {
        binding.buttonBack.setOnClickListener {
            mActivity.changeProfileLayoutVisible()
            findNavController().popBackStack()
        }
        // 안드로이드 뒤로가기 버튼 눌렀을 때
        mActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mActivity.changeProfileLayoutVisible()
                findNavController().popBackStack()
            }
        })
        binding.buttonEdit.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_profileEditFragment)
        }
        binding.chipMyDrawing.setOnClickListener {
            mypageFragmentViewModel.getMyDrawingListDtoList()
        }
        binding.chipMyPhoto.setOnClickListener {
            mypageFragmentViewModel.getMyPhotoListDtoList()
        }
        binding.chipLikeDrawing.setOnClickListener {
            mypageFragmentViewModel.getLikeDrawingListDtoList()
        }
        binding.chipBookmarkPhoto.setOnClickListener {
            mypageFragmentViewModel.getBookmarkPhotoListDtoList()
        }
        binding.buttonGoStore.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_storeFragment)
        }
        binding.buttonNft.setOnClickListener {
            findNavController().navigate(R.id.action_mypageFragment_to_nftListFragment)
        }
        binding.buttonSetting.setOnClickListener {
            invokeSettingsDialog()
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

    private fun initObserver(){
        mypageFragmentViewModel.currentDrawingListDtoList.observe(viewLifecycleOwner){
            //그림에 대해서 recyclerview 변경
            initDrawingRecyclerView()
        }
        mypageFragmentViewModel.currentPhotoListDtoList.observe(viewLifecycleOwner){
            initPhotoRecyclerView()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mypageFragmentViewModel.isLoggedout.collectLatest {
                if(it == true){
                    findNavController().navigate(R.id.action_mypageFragment_to_loginFragment)
                }
            }
        }

    }

    private fun initDrawingRecyclerView(){
        val drawingListAdapter =
            MypageDrawingAdapter(mypageFragmentViewModel.currentDrawingListDtoList.value!!)
        binding.recyclerviewDrawingPhotoList.apply {
//            layoutManager = GridLayoutManager(mActivity, 3) // 한 줄에 3개씩 보이도록 설정
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            adapter = drawingListAdapter.apply {
                this.itemClickListner = object: MypageDrawingAdapter.ItemClickListener{
                    override fun onClick(view: View, drawing: DrawingListDto) {
                        val action = MypageFragmentDirections.actionMypageFragmentToDrawingDetailFragment(drawing.drawingId)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun initPhotoRecyclerView(){
        val photoListAdapter =
            MypagePhotoAdapter(mypageFragmentViewModel.currentPhotoListDtoList.value!!)
        binding.recyclerviewDrawingPhotoList.apply {
//            layoutManager = GridLayoutManager(mActivity, 3) // 한 줄에 3개씩 보이도록 설정
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            adapter = photoListAdapter.apply {
                this.itemClickListner = object: MypagePhotoAdapter.ItemClickListener{
                    override fun onClick(view: View, photo: PhotoListDto) {
                        val action = MypageFragmentDirections.actionMypageFragmentToPhotoDetailFragment(photo.photoId)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun invokeSettingsDialog() {
        val dialog = SettingsDialog(
            clickBgmToggle = { toggleMusic()},
            logout = {invokeLogoutDialog()},
            deleteUser = {deleteUser()},
            bgmText = musicStatus()
        )
        dialog.show(childFragmentManager, TAG)
    }

    private fun invokeLogoutDialog(){
        val dialog = LogoutDialog(
            logout = {logout()}
        )
        dialog.show(childFragmentManager, TAG)
    }

    private fun musicStatus(): String {
        if(ApplicationClass.sharedPreferences.getMusicStatus() == true){
            return getString(R.string.bgm_off)
        }
        return getString(R.string.bgm_on)
    }

    private fun toggleMusic() {
        mActivity.toggleMusic()
    }

    private fun deleteUser() {
    }

    private fun logout() {
        mypageFragmentViewModel.logout()
    }

    private fun makeAnimationX(view: View, values: Float) {
        ObjectAnimator.ofFloat(view, "translationX", values).apply {
//            interpolator = DecelerateInterpolator()
            interpolator = OvershootInterpolator()
            duration = 500
            start()
        }
    }

    private fun makeAnimationFade(view: View, values: Float) {
        ObjectAnimator.ofFloat(view, "alpha", values).apply {
            duration = 100
            start()
        }
        ObjectAnimator.ofFloat(view, "scaleX", values).apply {
            duration = 200
            start()
        }
        ObjectAnimator.ofFloat(view, "scaleY", values).apply {
            duration = 200
            start()
        }
    }

    private fun viewSetting() {
        lifecycleScope.launch {
            mainActivityViewModel.getMemberInfo(sharedPreferences.getString(USER_EMAIL).toString())
            binding.chipGroup.check(binding.chipMyDrawing.id)
            // 마이페이지 내 캐릭터
            val profileColor = mainActivityViewModel.waterDropColorList[mainActivityViewModel.memberInfo.value!!.profileColor].resourceId
            val profileFace = mainActivityViewModel.waterDropFaceList[mainActivityViewModel.memberInfo.value!!.profileFace].resourceId
            val profileAccessory = mainActivityViewModel.waterDropAccessoryList[mainActivityViewModel.memberInfo.value!!.profileAccessory].resourceId
            setProfileImages(profileColor, profileFace, profileAccessory)

            // 마이페이지 티켓 수, 도장 수
            binding.textviewNickname.text = mainActivityViewModel.memberInfo.value!!.nickname
            binding.textviewTicketCnt.text = mainActivityViewModel.memberInfo.value!!.goldCoin.toString()
            binding.textviewStampCnt.text = mainActivityViewModel.memberInfo.value!!.silverCoin.toString()

            binding.layoutMyCharacter.translationX = -500f
            makeAnimationX(binding.layoutMyCharacter, 0f)
            // 들어오자 마자 크기는 0.5f
            binding.textviewNickname.scaleX = 0.5f
            binding.textviewNickname.scaleY = 0.5f
            binding.textviewTicketCnt.scaleX = 0.5f
            binding.textviewTicketCnt.scaleY = 0.5f
            binding.textviewStampCnt.scaleX = 0.5f
            binding.textviewStampCnt.scaleY = 0.5f

            // 들어오자 마자 투명도
            binding.textviewNickname.alpha = 0f
            binding.textviewTicketCnt.alpha = 0f
            binding.textviewStampCnt.alpha = 0f

            // 나타나는 애니메이션
            makeAnimationFade(binding.textviewNickname, 1f)
            makeAnimationFade(binding.textviewTicketCnt, 1f)
            makeAnimationFade(binding.textviewStampCnt, 1f)
        }
    }

    fun goInfoFragment(){
        findNavController().navigate(R.id.action_mypageFragment_to_infoFragment)
    }
}