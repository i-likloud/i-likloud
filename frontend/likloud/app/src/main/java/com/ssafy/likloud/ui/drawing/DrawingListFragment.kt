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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.databinding.FragmentDrawingListBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "차선호"
@AndroidEntryPoint
class DrawingListFragment : BaseFragment<FragmentDrawingListBinding>(FragmentDrawingListBinding::bind, R.layout.fragment_drawing_list) {

    private val drawingListFragmentViewModel : DrawingListFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()


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

        init()
        initListener()
        initObserver()
    }

    private fun init(){
        drawingListFragmentViewModel.getRecentOrderDrawingListDtoList()
    }

    override fun initListener(){

        binding.apply {
            //최신순 눌렀을 때
            buttonRecentOrder.setOnClickListener {
                drawingListFragmentViewModel.getRecentOrderDrawingListDtoList()
            }
            //랭킹순 눌렀을 때
            buttonRankingOrder.setOnClickListener{
                drawingListFragmentViewModel.getRankingOrderDrawingListDtoList()
            }
            //조회순 눌렀을 때
            buttonViewOrder.setOnClickListener {
                drawingListFragmentViewModel.getViewOrderDrawingListDtoLit()
            }
            //좋아요 눌렀을 때
            imageHeart.setOnClickListener {
                drawingListFragmentViewModel.changeLikeCount()
                drawingListFragmentViewModel.changeIsLiked()
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

        drawingListFragmentViewModel.currentDrawingListDtoList.observe(viewLifecycleOwner){
            //그림 목록 리사이클러뷰 세팅
            initRecyclerView()
            drawingListFragmentViewModel.getCurrentDrawingDetailDto(it[0])
        }

        drawingListFragmentViewModel.currentDrawingDetailDto.observe(viewLifecycleOwner){
            //현재 가운데 있는 그림 정보 조회 & 초기 좋아요 세팅
            drawingListFragmentViewModel.getCurrentDrawingMember(it.memberId)
            drawingListFragmentViewModel.setIsLiked()
            drawingListFragmentViewModel.setLikeCount()
        }

        drawingListFragmentViewModel.currentDrawingMember.observe(viewLifecycleOwner){
            //현재 그림에 대한 정보, 그림 그린 멤버 정보 뷰 세팅
            initInfoView()
            initCommentRecyclerView()
        }

        drawingListFragmentViewModel.isLiked.observe(viewLifecycleOwner){
            if(it){
                binding.imageHeart.setImageResource(R.drawable.icon_selected_heart)
            }else{
                binding.imageHeart.setImageResource(R.drawable.icon_unselected_heart)
            }
        }

        drawingListFragmentViewModel.likeCount.observe(viewLifecycleOwner){
            binding.textLikeCount.text = it.toString()
        }
    }

    private fun initInfoView(){
        binding.apply {
            Glide.with(binding.imageProfileColor)
                .load(activityViewModel.waterDropColorList[drawingListFragmentViewModel.currentDrawingMember.value!!.profileColor].resourceId)
                .into(binding.imageProfileColor)
            Glide.with(binding.imageProfileFace)
                .load(activityViewModel.waterDropFaceList[drawingListFragmentViewModel.currentDrawingMember.value!!.profileFace].resourceId)
                .into(binding.imageProfileFace)
            Glide.with(binding.imageProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[drawingListFragmentViewModel.currentDrawingMember.value!!.profileAccessory].resourceId)
                .into(binding.imageProfileAccessory)
            textDrawingNickname.text = drawingListFragmentViewModel.currentDrawingMember.value!!.nickname
            textDrawingTitle.text = drawingListFragmentViewModel.currentDrawingDetailDto.value!!.title
            textDrawingContent.text = drawingListFragmentViewModel.currentDrawingDetailDto.value!!.content
            textLikeCount.text = drawingListFragmentViewModel.currentDrawingDetailDto.value!!.likesCount.toString()
            textViewCount.text = drawingListFragmentViewModel.currentDrawingDetailDto.value!!.viewCount.toString()
        }
    }

    private fun initRecyclerView(){
        val drawingListAdapter = DrawingListAdapter(drawingListFragmentViewModel.currentDrawingListDtoList.value!!)
        binding.recyclerviewDrawaing.apply {
            this.adapter = drawingListAdapter
            set3DItem(true)
            setAlpha(true)
            setOrientation(RecyclerView.VERTICAL)
            setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                //본인한테서 멈췄을 때 이벤트
                override fun onItemSelected(position: Int) {
                    //여기서 selectedDrawing을 가지고 DrawingDetailDto 받아라
                    drawingListFragmentViewModel.getCurrentDrawingDetailDto(drawingListAdapter.list[position])
                    Log.d(TAG, "SelectedDrawingDetail : ${drawingListFragmentViewModel.currentDrawingDetailDto.value} ")
                }
            })
        }
    }

    private fun initCommentRecyclerView(){
        Log.d(TAG, "commentList : ${drawingListFragmentViewModel.currentDrawingCommentList.value} ")
        val commentListAdapter = CommentListAdapter(drawingListFragmentViewModel.currentDrawingCommentList.value!!,
            drawingListFragmentViewModel.currentDrawingMember.value!!,
            activityViewModel)
        binding.recyclerviewDrawingComment.apply {
            this.adapter = commentListAdapter
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }
}