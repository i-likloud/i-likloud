package com.ssafy.likloud.ui.drawing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingDetailBinding
import dagger.hilt.android.AndroidEntryPoint

//PhotoListFragment에서 해당 그림 선택하면 그 그림 객체 얻어와야함!!!!
@AndroidEntryPoint
class DrawingDetailFragment : BaseFragment<FragmentDrawingDetailBinding>(
    FragmentDrawingDetailBinding::bind, R.layout.fragment_drawing_detail
) {

    private val photoDrawingDetailFragmentViewModel : DrawingDetailFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    val args: DrawingDetailFragmentArgs by navArgs()


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
        photoDrawingDetailFragmentViewModel.currentDrawingDetail.observe(viewLifecycleOwner){
            //member 정보 조회
            photoDrawingDetailFragmentViewModel.getCurrentDrawingMember(photoDrawingDetailFragmentViewModel.currentDrawingDetail.value!!.memberId)
        }

        photoDrawingDetailFragmentViewModel.currentDrawingMember.observe(viewLifecycleOwner){
            binding.apply {
                Glide.with(binding.imageCurrentDrawing)
                    .load(photoDrawingDetailFragmentViewModel.currentDrawingDetail.value!!.imageUrl)
                    .into(binding.imageCurrentDrawing)
                Glide.with(binding.imageProfileColor)
                    .load(activityViewModel.waterDropColorList[photoDrawingDetailFragmentViewModel.currentDrawingMember.value!!.profileColor].resourceId)
                    .into(binding.imageProfileColor)
                Glide.with(binding.imageProfileFace)
                    .load(activityViewModel.waterDropFaceList[photoDrawingDetailFragmentViewModel.currentDrawingMember.value!!.profileFace].resourceId)
                    .into(binding.imageProfileFace)
                Glide.with(binding.imageProfileAccessory)
                    .load(activityViewModel.waterDropAccessoryList[photoDrawingDetailFragmentViewModel.currentDrawingMember.value!!.profileAccessory].resourceId)
                    .into(binding.imageProfileAccessory)
                textDrawingNickname.text = photoDrawingDetailFragmentViewModel.currentDrawingMember.value!!.nickname
                textDrawingTitle.text = photoDrawingDetailFragmentViewModel.currentDrawingDetail.value!!.title
                textDrawingContent.text = photoDrawingDetailFragmentViewModel.currentDrawingDetail.value!!.content
                if (photoDrawingDetailFragmentViewModel.currentDrawingDetail.value!!.memberLiked) {
                    imageHeart.setImageResource(R.drawable.icon_selected_heart)
                } else {
                    imageHeart.setImageResource(R.drawable.icon_unselected_heart)
                }
            }
        }
    }

    private fun init(){
        //여기서 args.drawingId로 DrawingDetailDto 불러와야 함
        photoDrawingDetailFragmentViewModel.getCurrentPhotoDrawingDetail(args.drawingId)
    }

    override fun initListener() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }// 안드로이드 뒤로가기 버튼 눌렀을 때
        mainActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

}