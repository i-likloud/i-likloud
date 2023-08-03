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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
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


            imageHeart.setOnClickListener {
                drawingListFragmentViewModel.changeSelectedDrawingDetailDtoMemberLiked()
                if(drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.memberLiked){
                    binding.imageHeart.setImageResource(R.drawable.icon_selected_heart)
                }else{
                    binding.imageHeart.setImageResource(R.drawable.icon_unselected_heart)
                }
            }

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
            Log.d(TAG, "currentDrawingListDtoList observe 발동... ")
            initRecyclerView()
            drawingListFragmentViewModel.getSelectedDrawingDetailDto(drawingListFragmentViewModel.currentDrawingListDtoList.value!![0])
        }

        drawingListFragmentViewModel.selectedDrawingDetailDto.observe(viewLifecycleOwner){
            Log.d(TAG, "selectedDrawing : ${drawingListFragmentViewModel.selectedDrawingDetailDto.value} ")
            drawingListFragmentViewModel.getSelectedDrawingMember(drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.memberId)
        }

        drawingListFragmentViewModel.selectedDrawingMember.observe(viewLifecycleOwner){
            Log.d(TAG, "selectedDrawingMember : ${drawingListFragmentViewModel.selectedDrawingMember.value}")
            binding.apply {
                Glide.with(binding.imageProfileColor)
                    .load(activityViewModel.waterDropColorList[drawingListFragmentViewModel.selectedDrawingMember.value!!.profileColor].resourceId)
                    .into(binding.imageProfileColor)
                Glide.with(binding.imageProfileFace)
                    .load(activityViewModel.waterDropFaceList[drawingListFragmentViewModel.selectedDrawingMember.value!!.profileFace].resourceId)
                    .into(binding.imageProfileFace)
                Glide.with(binding.imageProfileAccessory)
                    .load(activityViewModel.waterDropAccessoryList[drawingListFragmentViewModel.selectedDrawingMember.value!!.profileAccessory].resourceId)
                    .into(binding.imageProfileAccessory)
                textDrawingNickname.text = drawingListFragmentViewModel.selectedDrawingMember.value!!.nickname
                textDrawingTitle.text = drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.title
                textDrawingContent.text = drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.content
                if (drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.memberLiked) {
                    imageHeart.setImageResource(R.drawable.icon_selected_heart)
                } else {
                    imageHeart.setImageResource(R.drawable.icon_unselected_heart)
                }
            }
        }

        drawingListFragmentViewModel.selectedDrawingCommentList.observe(viewLifecycleOwner) {
        }
    }


    private fun initRecyclerView(){
        val drawingListAdapter =
            DrawingListAdapter(drawingListFragmentViewModel.currentDrawingListDtoList.value!!)
        binding.recyclerviewDrawaing.apply {
            this.adapter = drawingListAdapter
            set3DItem(true)
            setAlpha(true)
            setOrientation(RecyclerView.VERTICAL)
            setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                //본인한테서 멈췄을 때 이벤트
                override fun onItemSelected(position: Int) {
                    //여기서 selectedDrawing을 가지고 DrawingDetailDto 받아라
                    drawingListFragmentViewModel.getSelectedDrawingDetailDto(drawingListAdapter.list[position])
                    Log.d(TAG, "SelectedDrawingDetail : ${drawingListFragmentViewModel.selectedDrawingDetailDto.value} ")
                }
            })
        }
    }
}