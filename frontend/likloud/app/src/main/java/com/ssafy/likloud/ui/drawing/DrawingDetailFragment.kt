package com.ssafy.likloud.ui.drawing

import android.content.Context
import android.os.Bundle
import android.util.Log
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

private const val TAG = "차선호"
@AndroidEntryPoint
class DrawingDetailFragment : BaseFragment<FragmentDrawingDetailBinding>(
    FragmentDrawingDetailBinding::bind, R.layout.fragment_drawing_detail
) {

    private val drawingDetailFragmentViewModel : DrawingDetailFragmentViewModel by viewModels()
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
        drawingDetailFragmentViewModel.currentDrawingDetail.observe(viewLifecycleOwner){
            //현재 가운데 있는 그림 정보 조회 & 초기 좋아요 세팅
            drawingDetailFragmentViewModel.getCurrentDrawingMember(it.memberId)
            drawingDetailFragmentViewModel.setIsLiked()
            drawingDetailFragmentViewModel.setLikeCount()
        }

        drawingDetailFragmentViewModel.currentDrawingMember.observe(viewLifecycleOwner) {
            initInfoView()
        }

        drawingDetailFragmentViewModel.isLiked.observe(viewLifecycleOwner){
            Log.d(TAG, "current isLiked: $it")
            if(it){
                binding.imageHeart.setImageResource(R.drawable.icon_selected_heart)
            }else{
                binding.imageHeart.setImageResource(R.drawable.icon_unselected_heart)
            }
        }

        drawingDetailFragmentViewModel.likeCount.observe(viewLifecycleOwner){
            binding.textLikeCount.text = it.toString()
        }
    }

    private fun init(){
        //여기서 args.drawingId로 DrawingDetailDto 불러와야 함
        drawingDetailFragmentViewModel.getCurrentPhotoDrawingDetail(args.drawingId)
    }

    override fun initListener() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageHeart.setOnClickListener {
                drawingDetailFragmentViewModel.changeLikeCount()
                drawingDetailFragmentViewModel.changeIsLiked()
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
            Glide.with(binding.imageCurrentDrawing)
                .load(drawingDetailFragmentViewModel.currentDrawingDetail.value!!.imageUrl)
                .into(binding.imageCurrentDrawing)
            Glide.with(binding.imageProfileColor)
                .load(activityViewModel.waterDropColorList[drawingDetailFragmentViewModel.currentDrawingMember.value!!.profileColor].resourceId)
                .into(binding.imageProfileColor)
            Glide.with(binding.imageProfileFace)
                .load(activityViewModel.waterDropFaceList[drawingDetailFragmentViewModel.currentDrawingMember.value!!.profileFace].resourceId)
                .into(binding.imageProfileFace)
            Glide.with(binding.imageProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[drawingDetailFragmentViewModel.currentDrawingMember.value!!.profileAccessory].resourceId)
                .into(binding.imageProfileAccessory)
            textDrawingNickname.text = drawingDetailFragmentViewModel.currentDrawingMember.value!!.nickname
            textDrawingTitle.text = drawingDetailFragmentViewModel.currentDrawingDetail.value!!.title
            textDrawingContent.text = drawingDetailFragmentViewModel.currentDrawingDetail.value!!.content
            textLikeCount.text = drawingDetailFragmentViewModel.currentDrawingDetail.value!!.likesCount.toString()
            textViewCount.text = drawingDetailFragmentViewModel.currentDrawingDetail.value!!.viewCount.toString()
        }
    }


}