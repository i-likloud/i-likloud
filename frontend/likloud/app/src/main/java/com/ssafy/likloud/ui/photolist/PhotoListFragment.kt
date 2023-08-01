package com.ssafy.likloud.ui.photolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentPhotoListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoListFragment : BaseFragment<FragmentPhotoListBinding>(FragmentPhotoListBinding::bind, R.layout.fragment_photo_list) {

    private val photoListFragmentViewModel: PhotoListFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity


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

        binding.apply {

            //맨 처음에는 리스트 가장 첫 번째 그림
//            selectedPhoto = photoList[0]
//            Glide.with(binding.imageDrawingProfile)
//                .load(selectedPhoto.imageUrl)
//                .into(binding.imageDrawingProfile)
//            binding.textDrawingNickname.text = selectedPhoto.artist
        }
    }


    private fun init(){
        photoListFragmentViewModel.getRecentOrderPhotoListDtoList()
    }

    override fun initListener() {
        binding.apply {
            //최신순 눌렀을 때
            buttonRecentOrder.setOnClickListener {
                photoListFragmentViewModel.getRecentOrderPhotoListDtoList()
            }
            //랭킹순 눌렀을 때
            buttonRankingOrder.setOnClickListener{
                photoListFragmentViewModel.getRankingOrderPhotoListDtoList()
            }
            //즐겨찾기(스타)를 눌렀을 때
            imageStar.setOnClickListener {

            }
            //뒤로가기 눌렀을 때
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObserver(){

        photoListFragmentViewModel.currentPhotoListDtoList.observe(viewLifecycleOwner){
            initRecyclerView()
            photoListFragmentViewModel.setSelectedPhotoListDto(photoListFragmentViewModel.currentPhotoListDtoList.value!![0])
        }

        photoListFragmentViewModel.selectedPhotoListDto.observe(viewLifecycleOwner){
            photoListFragmentViewModel.getSelectedPhotoMember(photoListFragmentViewModel.selectedPhotoListDto.value!!.memberId)
        }

        photoListFragmentViewModel.selectedPhotoMember.observe(viewLifecycleOwner){
            binding.apply {
                Glide.with(binding.imageDrawingProfileColor)
                    .load(photoListFragmentViewModel.selectedPhotoMember.value!!.profileColor)
                    .into(binding.imageDrawingProfileColor)
                Glide.with(binding.imageDrawingProfileFace)
                    .load(photoListFragmentViewModel.selectedPhotoMember.value!!.profileFace)
                    .into(binding.imageDrawingProfileFace)
                Glide.with(binding.imageDrawingProfileAccessory)
                    .load(photoListFragmentViewModel.selectedPhotoMember.value!!.profileAccessory)
                    .into(binding.imageDrawingProfileAccessory)
                textDrawingNickname.text = photoListFragmentViewModel.selectedPhotoMember.value!!.nickname
            }
        }
    }

    private fun initRecyclerView(){
        val photoListAdapter = PhotoListAdapter(photoListFragmentViewModel.currentPhotoListDtoList.value!!)
        binding.apply {
            recyclerviewDrawaing.apply {
                this.adapter = photoListAdapter
                set3DItem(true)
                setAlpha(true)
                setOrientation(RecyclerView.VERTICAL)
                setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                    //본인한테서 멈췄을 때 이벤트
                    override fun onItemSelected(position: Int) {
                        photoListFragmentViewModel.setSelectedPhotoListDto(photoListAdapter.list[position])
                    }
                })
            }
        }
    }



}