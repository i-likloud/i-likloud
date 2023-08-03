package com.ssafy.likloud.ui.photo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.FragmentPhotoDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::bind, R.layout.fragment_photo_detail) {

    private val photoDetailFragmentViewModel: PhotoDetailFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    val args: PhotoDetailFragmentArgs by navArgs()


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

    private fun initObserver(){
        photoDetailFragmentViewModel.currentPhotoDetail.observe(viewLifecycleOwner){
            //여기서 현재 그림에 대한 사진 리스트, 사진 올린 멤버 조회 & 초기 bookmark 정보 세팅
            photoDetailFragmentViewModel.getCurrentPhotoDrawingList(it.photoId)
            photoDetailFragmentViewModel.getCurrentPhotoMember(it.memberId)
            photoDetailFragmentViewModel.setIsBookmarked()
            photoDetailFragmentViewModel.setBookmarkCount()
        }
        photoDetailFragmentViewModel.currentPhotoMember.observe(viewLifecycleOwner){
            //사진 정보, 유저 정보 뷰 세팅
            initInfoView()
        }
        photoDetailFragmentViewModel.currentPhotoDrawingList.observe(viewLifecycleOwner){
            //현재 사진에 대한 그림들 리사이클러뷰 세팅
            initPhotoDrawingListRecyclerView()
        }
        photoDetailFragmentViewModel.isBookmarked.observe(viewLifecycleOwner){
            if(it){
                binding.imageStar.setImageResource(R.drawable.icon_selected_star)
            }else{
                binding.imageStar.setImageResource(R.drawable.icon_unselected_star)
            }
        }
        photoDetailFragmentViewModel.bookmarkCount.observe(viewLifecycleOwner){
            binding.textBookmarkCount.text = it.toString()
        }
    }

    private fun init(){
        photoDetailFragmentViewModel.getCurrentPhotoDetail(args.photoId)
    }

    override fun initListener() {
        binding.apply {
            //뒤로가기 눌렀을 때
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            //즐겨찾기 눌렀을 때
            imageStar.setOnClickListener {
                photoDetailFragmentViewModel.changeBookmarkCount()
                photoDetailFragmentViewModel.changeIsBookmarked()
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

    private fun initInfoView(){
        binding.apply {
            Glide.with(imageCurrentPhoto)
                .load(photoDetailFragmentViewModel.currentPhotoDetail.value!!.photoUrl)
                .into(imageCurrentPhoto)
            Glide.with(imagePhotoProfileColor)
                .load(activityViewModel.waterDropColorList[photoDetailFragmentViewModel.currentPhotoMember.value!!.profileColor].resourceId)
                .into(imagePhotoProfileColor)
            Glide.with(imagePhotoProfileFace)
                .load(activityViewModel.waterDropFaceList[photoDetailFragmentViewModel.currentPhotoMember.value!!.profileFace].resourceId)
                .into(imagePhotoProfileFace)
            Glide.with(imagePhotoProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[photoDetailFragmentViewModel.currentPhotoMember.value!!.profileAccessory].resourceId)
                .into(imagePhotoProfileAccessory)
            textPhotoNickname.text = photoDetailFragmentViewModel.currentPhotoMember.value!!.nickname
            textBookmarkCount.text = photoDetailFragmentViewModel.currentPhotoDetail.value!!.bookmarkCount.toString()
            textDrawCount.text = photoDetailFragmentViewModel.currentPhotoDetail.value!!.pickCount.toString()
        }
    }
    private fun initPhotoDrawingListRecyclerView(){
        val photoDrawingListAdapter = PhotoDrawingListAdapter(photoDetailFragmentViewModel.currentPhotoDrawingList.value!!)
        binding.apply {
            recyclerviewPhotoDrawingList.apply {
                layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = photoDrawingListAdapter.apply {
                    this.itemClickListener = object: PhotoDrawingListAdapter.ItemClickListener{
                        override fun onClick(view: View, drawing: DrawingListDto) {
                            //여기서 drawing객체를 가진 상태로 PhotoDrawingDetailFragment로 전달
                            val action = PhotoDetailFragmentDirections.actionPhotoDetailFragmentToDrawingDetailFragment(drawing.drawingId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }


}