package com.ssafy.likloud.ui.photo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.FragmentPhotoListBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "차선호"
@AndroidEntryPoint
class PhotoListFragment : BaseFragment<FragmentPhotoListBinding>(FragmentPhotoListBinding::bind, R.layout.fragment_photo_list) {

    private val photoListFragmentViewModel: PhotoListFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var photoListAdapter: PhotoListAdapter
    private lateinit var photoDrawingListAdapter: PhotoDrawingListAdapter
    private var isScrolling = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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
        initObserver()
        init()
        initListener()
    }


    private fun init(){
        photoListFragmentViewModel.getRecentOrderPhotoListDtoList()
        toggleButton(binding.buttonRecentOrder)
        initRecyclerView()
        initPhotoDrawingListRecyclerView()
    }

    override fun initListener() {
        binding.apply {
            //최신순 눌렀을 때
            buttonRecentOrder.setOnClickListener {
                if(!isScrolling) {
                    photoListFragmentViewModel.getRecentOrderPhotoListDtoList()
                    initRecyclerView()
                    toggleButton(buttonRecentOrder)
                }
            }
            //랭킹순 눌렀을 때
            buttonRankingOrder.setOnClickListener{
                if(!isScrolling) {
                    photoListFragmentViewModel.getRankingOrderPhotoListDtoList()
                    initRecyclerView()
                    toggleButton(buttonRankingOrder)
                }
            }
            //즐찾순 눌렀을 때
            buttonBookmarkOrder.setOnClickListener{
                if(!isScrolling) {
                    photoListFragmentViewModel.getBookmarkOrderPhotoListDtoList()
                    initRecyclerView()
                    toggleButton(buttonBookmarkOrder)
                }
            }
            //즐겨찾기(스타)를 눌렀을 때
            imageStar.setOnClickListener {
                photoListFragmentViewModel.changeBookmarkCount()
                photoListFragmentViewModel.changeIsBookmarked()
            }
            //뒤로가기 눌렀을 때
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        // 안드로이드 뒤로가기 버튼 눌렀을 때
        mainActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun initObserver(){

        photoListFragmentViewModel.currentPhotoListDtoList.observe(viewLifecycleOwner){
            photoListAdapter.submitList(it)
            photoListFragmentViewModel.setCurrentPhotoListDto(it[0])
        }

        photoListFragmentViewModel.currentPhotoListDto.observe(viewLifecycleOwner){
            //여기서 현재 그림에 대한 사진 리스트, 사진 올린 멤버 조회 & 초기 bookmark 정보 세팅
            photoListFragmentViewModel.getCurrentPhotoMember(it.memberId)
            photoListFragmentViewModel.getCurrentPhotoDrawingList(it.photoId)
            photoListFragmentViewModel.setIsBookmarked()
            photoListFragmentViewModel.setBookmarkCount()
        }

        photoListFragmentViewModel.currentPhotoMember.observe(viewLifecycleOwner){
            //사진 정보, 유저 정보 뷰 세팅
            initInfoView(photoListFragmentViewModel.currentPhotoListDto.value!!, it)
            isScrolling = false
        }

        photoListFragmentViewModel.currentPhotoDrawingList.observe(viewLifecycleOwner){
            //현재 사진에 대한 그림들 리사이클러뷰 세팅
            photoDrawingListAdapter.submitList(it)
        }

        photoListFragmentViewModel.isBookmarked.observe(viewLifecycleOwner){
            if (it) {
                binding.imageStar.setImageResource(R.drawable.icon_selected_star)
            } else {
                binding.imageStar.setImageResource(R.drawable.icon_unselected_star)
            }
        }

        photoListFragmentViewModel.bookmarkCount.observe(viewLifecycleOwner){
            binding.textBookmarkCount.text = photoListFragmentViewModel.bookmarkCount.value.toString()
        }
    }

    private fun initRecyclerView(){
        Log.d(TAG, "initRecyclerView list : ${photoListFragmentViewModel.currentPhotoListDtoList.value} ")
        photoListAdapter = PhotoListAdapter()
        binding.apply {
            recyclerviewDrawaing.apply {
                this.adapter = photoListAdapter
                set3DItem(true)
                setAlpha(true)
                setOrientation(RecyclerView.VERTICAL)
                setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                    //본인한테서 멈췄을 때 이벤트
                    override fun onItemSelected(position: Int) {
                        photoListFragmentViewModel.setCurrentPhotoListDto(photoListFragmentViewModel.currentPhotoListDtoList.value!![position])
                    }
                })
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                            isScrolling = true
                        }
                    }
                })
            }
        }
    }

    private fun initInfoView(photo: PhotoListDto, member: MemberProfileDto){
        binding.apply {
            Glide.with(binding.imageDrawingProfileColor)
                .load(activityViewModel.waterDropColorList[member.profileColor].resourceId)
                .into(binding.imageDrawingProfileColor)
            Glide.with(binding.imageDrawingProfileFace)
                .load(activityViewModel.waterDropFaceList[member.profileFace].resourceId)
                .into(binding.imageDrawingProfileFace)
            Glide.with(binding.imageDrawingProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[member.profileAccessory].resourceId)
                .into(binding.imageDrawingProfileAccessory)
            textDrawingNickname.text = member.nickname
            textBookmarkCount.text = photo.bookmarkCount.toString()
            textDrawCount.text = photo.pickCount.toString()
        }
    }

    private fun toggleButton(view: View){
        binding.apply {
            buttonRecentOrder.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_black)
            buttonRankingOrder.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_black)
            buttonBookmarkOrder.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_black)
        }
        view.background = ContextCompat.getDrawable(mainActivity, R.drawable.button_frame_skyblue)
    }

    private fun initPhotoDrawingListRecyclerView(){
        photoDrawingListAdapter = PhotoDrawingListAdapter()
        binding.apply {
            recyclerviewPhotoDrawingList.apply {
                layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = photoDrawingListAdapter.apply {
                    this.itemClickListener = object: PhotoDrawingListAdapter.ItemClickListener{
                        override fun onClick(view: View, drawing: DrawingListDto) {
                            //여기서 drawing객체를 가진 상태로 PhotoDrawingDetailFragment로 전달
                            val action = PhotoListFragmentDirections.actionPhotoListFragmentToPhotoDrawingDetailFragment(drawing.drawingId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }
}