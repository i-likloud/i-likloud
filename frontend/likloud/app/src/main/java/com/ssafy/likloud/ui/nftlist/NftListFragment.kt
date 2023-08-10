package com.ssafy.likloud.ui.nftlist

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.databinding.FragmentNftListBinding
import com.ssafy.likloud.ui.photo.PhotoListFragmentDirections
import com.ssafy.likloud.ui.upload.UploadFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "차선호"
@AndroidEntryPoint
class NftListFragment : BaseFragment<FragmentNftListBinding>(
    FragmentNftListBinding::bind, R.layout.fragment_nft_list) {

    private val nftListFragmentViewModel : NftListFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var nftListAdapter: NftListAdapter
    private lateinit var giftListAdapter: NftGiftAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initListener()
        initObserver()

    }

    private fun init(){
        binding.layoutFrameFragmentSearchUser.translationX = 1600f
        nftListFragmentViewModel.getMyNftDtoList()
        toggleButton(binding.buttonMyNft)
        initNftListRecyclerView()
    }

    override fun initListener() {
        binding.apply {
            buttonMyNft.setOnClickListener {
                nftListFragmentViewModel.getMyNftDtoList()
                initNftListRecyclerView()
                toggleButton(binding.buttonMyNft)
            }
            buttonGiftNft.setOnClickListener {
                nftListFragmentViewModel.getNftGiftList()
                //선물함 불러온거 viewmodel에서 처리할 필요 있음
                initGiftListRecyclerView()
                toggleButton(binding.buttonGiftNft)
            }
            buttonDismissFragment.setOnClickListener {
                endSearchUserFragment()
            }
            //뒤로가기 눌렀을 때
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageNftInfo.setOnClickListener {
                nftListFragmentViewModel.nftInfoDialog.show(childFragmentManager, "nftInfo")
            }
            layoutFrameFragmentSearchUser.setOnClickListener {
            }
        }

        mActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(nftListFragmentViewModel.isShowSearchUserFragment) {
                    endSearchUserFragment()
                }else{
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun initObserver(){
        nftListFragmentViewModel.myNftDtoList.observe(viewLifecycleOwner){
            nftListAdapter.submitList(it)
            Log.d(TAG, "nftList : $it")
        }
        nftListFragmentViewModel.giftNftDtoList.observe(viewLifecycleOwner){
            giftListAdapter.submitList(it)
        }
        activityViewModel.isSended.observe(viewLifecycleOwner){
            if(it){
                endSearchUserFragment()
                nftListFragmentViewModel.getMyNftDtoList()
                activityViewModel.setIsSended(false)
                showSnackbar(binding.root, "info", "선물을 보냈습니다!")
                binding.lottieSendGift.visibility = View.VISIBLE
                binding.lottieSendGift.playAnimation()
                binding.lottieSendGift.addAnimatorListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator) {}
                    override fun onAnimationEnd(p0: Animator) {
                        binding.lottieSendGift.visibility = View.INVISIBLE
                    }
                    override fun onAnimationCancel(p0: Animator) {}
                    override fun onAnimationRepeat(p0: Animator) {}
                })
            }
        }
        nftListFragmentViewModel.isAccepted.observe(viewLifecycleOwner){
            if(it){ //수락했을 때
                showSnackbar(binding.root,"info","수락했어요!")
                nftListFragmentViewModel.getNftGiftList()
            }else{ //거부했을 때
                showSnackbar(binding.root, "fail", "거부했어요ㅠ")
                nftListFragmentViewModel.getNftGiftList()
            }
        }
    }

    private fun initNftListRecyclerView(){
        nftListAdapter = NftListAdapter(mActivity)
        binding.recyclerviewNft.apply {
            this.adapter = nftListAdapter.apply {
                itemClickListner = object: NftListAdapter.ItemClickListener{
                    override fun onClick(nftDto: NftListDto) {
                        //선물하기 로직
//                        Toast.makeText(mainActivity, "선물하기 클릭...", Toast.LENGTH_SHORT).show()
//                        val action = NftListFragmentDirections.actionNftListFragmentToNftGiftSearchUserFragment(nftDto.nftId)
//                        findNavController().navigate(action)
                        startSearchUserFragment(nftDto)
                    }

                }
            }
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun initGiftListRecyclerView(){
        giftListAdapter = NftGiftAdapter(mActivity)
        binding.recyclerviewNft.apply {
            this.adapter = giftListAdapter.apply {
                itemClickListner = object: NftGiftAdapter.ItemClickListener{
                    override fun onConfirmClick(nftGiftDto: NftGiftDto) {
                        //선물 확인 버튼 눌렀을 때 NftGiftConfirmDialog 띄워라
                        nftListFragmentViewModel.setNftGiftConfirmDialog(nftGiftDto)
                        nftListFragmentViewModel.nftGiftConfimDialog.show(childFragmentManager, "confirm gift")
                    }

                }
            }
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun toggleButton(view: View){
        binding.apply {
            buttonMyNft.background = ContextCompat.getDrawable(mActivity, R.drawable.frame_button_grey_mild)
            buttonGiftNft.background = ContextCompat.getDrawable(mActivity, R.drawable.frame_button_grey_mild)
        }
        view.background = ContextCompat.getDrawable(mActivity, R.drawable.frame_button_yellow_mild_200)
    }

    private fun makeButtonAnimationX(button: View, values: Float) {
        ObjectAnimator.ofFloat(button, "translationX", values).apply {
//            interpolator = DecelerateInterpolator()
            interpolator = OvershootInterpolator()
            duration = 500
            start()
        }
    }

    private fun startSearchUserFragment(nft: NftListDto) {
        makeButtonAnimationX(binding.layoutFrameFragmentSearchUser, 0f)
        nftListFragmentViewModel.isShowSearchUserFragment = true
        childFragmentManager.beginTransaction()
            .add(R.id.frame_fragment_search_user, NftGiftSearchUserFragment(nft.nftId))
            .commit()
    }

    fun endSearchUserFragment() {
        Log.d(TAG, "endSearchUserFragment....")
        makeButtonAnimationX(binding.layoutFrameFragmentSearchUser, 1600f)
        nftListFragmentViewModel.isShowSearchUserFragment = false
        childFragmentManager.findFragmentById(R.id.frame_fragment_search_user)?.let {
            childFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }

    fun acceptGift(nftGiftDto: NftGiftDto){
        nftListFragmentViewModel.acceptGift(nftGiftDto)
    }

    fun rejectGift(nftGiftDto: NftGiftDto){
        nftListFragmentViewModel.rejectGift(nftGiftDto)
    }




}