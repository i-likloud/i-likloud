package com.ssafy.likloud.ui.mypage

import android.animation.ObjectAnimator
import android.content.ClipData.Item
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MypageFragment_싸피"
@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage) {

    private val mypageFragmentViewModel : MypageFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        val profileColor = mainActivityViewModel.waterDropColorList[mainActivityViewModel.memberInfo.value!!.profileColor].resourceId
        val profileFace = mainActivityViewModel.waterDropFaceList[mainActivityViewModel.memberInfo.value!!.profileFace].resourceId
        val profileAccessory = mainActivityViewModel.waterDropAccessoryList[mainActivityViewModel.memberInfo.value!!.profileAccessory].resourceId
        setProfileImages(profileColor, profileFace, profileAccessory)

        // 프래그먼트 연결성을 부드럽게 하기 위한 애니메이션
        binding.layoutMyCharacter.translationX = -500f
        makeButtonAnimationX(binding.layoutMyCharacter, 0f)
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
        binding.textviewNickname.text = mainActivityViewModel.memberInfo.value!!.nickname
        binding.textviewTicketCnt.text = mainActivityViewModel.memberInfo.value!!.goldCoin.toString()
        binding.textviewStampCnt.text = mainActivityViewModel.memberInfo.value!!.silverCoin.toString()
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
        binding.buttonNft.setOnClickListener{
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
    }

    private fun initDrawingRecyclerView(){
        val drawingListAdapter =
            MypageDrawingAdapter(mypageFragmentViewModel.currentDrawingListDtoList.value!!)
        binding.recyclerviewDrawingPhotoList.apply {
            layoutManager = GridLayoutManager(mActivity, 3) // 한 줄에 3개씩 보이도록 설정
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
            layoutManager = GridLayoutManager(mActivity, 3) // 한 줄에 3개씩 보이도록 설정
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
            logout = {logout()},
            deleteUser = {deleteUser()},
            bgmText = mainActivityViewModel.toggleBgmString.value!!
        )
        dialog.show(childFragmentManager, TAG)
    }

    private fun toggleMusic() {
        mActivity.toggleMusic()
    }

    private fun deleteUser() {
    }

    private fun logout() {
    }

    private fun makeButtonAnimationX(view: View, values: Float) {
        ObjectAnimator.ofFloat(view, "translationX", values).apply {
//            interpolator = DecelerateInterpolator()
            interpolator = OvershootInterpolator()
            duration = 500
            start()
        }
    }
}