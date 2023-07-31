package com.ssafy.likloud.ui.drawinglist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.FragmentDrawingListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "차선호"
@AndroidEntryPoint
class DrawingListFragment : BaseFragment<FragmentDrawingListBinding>(FragmentDrawingListBinding::bind, R.layout.fragment_drawing_list) {

    private val drawingListFragmentViewModel : DrawingListFragmentViewModel by viewModels()
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
        // rangkingOrderDrawingList, recentOrderDrawingList 여기서 두 개 각자 받자
//        rankingOrderDrawingList =

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        init()
        initListener()
    }

    private fun init(){
        binding.apply {

            drawingListFragmentViewModel.getRankingOrderDrawingListDtoList()
            drawingListFragmentViewModel.getRecentOrderDrawingListDtoList()


        }
    }

    override fun initListener(){

        binding.apply {
            //랭킹순 눌렀을 때
            buttonRankingOrder.setOnClickListener{
//                drawingListFragmentViewModel.changeCurrentDrawingListDtoList(drawingListFragmentViewModel.rankingOrderDrawingListDtoList.value!!)
                drawingListFragmentViewModel.changeCurrentToRanking()
            }

            //최신순 눌렀을 때
            buttonRecentOrder.setOnClickListener {
//                drawingListFragmentViewModel.changeCurrentDrawingListDtoList(drawingListFragmentViewModel.recentOrderDrawingListDtoList.value!!)
                drawingListFragmentViewModel.changeCurrentToRecent()
            }

            imageHeart.setOnClickListener {
                drawingListFragmentViewModel.changeSelectedDrawingDetailDtoMemberLiked()
                if(drawingListFragmentViewModel.selectedDrawingDetailDto.memberLiked){
                    binding.imageHeart.setImageResource(R.drawable.icon_selected_heart)
                }else{
                    binding.imageHeart.setImageResource(R.drawable.icon_unselected_heart)
                }
            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initRecyclerView(){
        binding.apply {
            val drawingListAdapter =
                DrawingListAdapter(drawingListFragmentViewModel.currentDrawingListDtoList.value!!)
            recyclerviewDrawaing.apply {
                this.adapter = drawingListAdapter
                set3DItem(true)
                setAlpha(true)
                setOrientation(RecyclerView.VERTICAL)
                setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                    //본인한테서 멈췄을 때 이벤트
                    override fun onItemSelected(position: Int) {
                        drawingListFragmentViewModel.changeSelectedDrawingListDto(drawingListAdapter.list[position])

                        //여기서 selectedDrawing을 가지고 DrawingDetailDto 받아라
                        drawingListFragmentViewModel.getSelectedDrawingDetailDto(drawingListFragmentViewModel.selectedDrawingListDto)

                        //
                        Glide.with(binding.imageDrawingProfile)
                            .load(drawingListFragmentViewModel.selectedDrawingDetailDto.imageUrl)
                            .into(binding.imageDrawingProfile)
                        binding.textDrawingArtist.text = drawingListFragmentViewModel.selectedDrawingDetailDto.artist
                        binding.textDrawingTitle.text = drawingListFragmentViewModel.selectedDrawingDetailDto.title
                        binding.textDrawingContent.text = drawingListFragmentViewModel.selectedDrawingDetailDto.content
                        if(drawingListFragmentViewModel.selectedDrawingDetailDto.memberLiked){
                            binding.imageHeart.setImageResource(R.drawable.icon_selected_heart)
                        }else{
                            binding.imageHeart.setImageResource(R.drawable.icon_unselected_heart)
                        }
                        drawingListFragmentViewModel.changeSelectedDrawingCommentList(drawingListFragmentViewModel.selectedDrawingDetailDto.commentList)
                    }
                })
            }

            //댓글 목록 리사이클러뷰
            if(drawingListFragmentViewModel.selectedDrawingCommentList.value != null) {
                val commentListAdapter =
                    CommentListAdapter(drawingListFragmentViewModel.selectedDrawingCommentList.value!!)
                recyclerviewDrawingComment.apply {
                    this.adapter = commentListAdapter
                    this.layoutManager =
                        LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
                }
            }
        }
    }

    private fun initObserver(){
        drawingListFragmentViewModel.recentOrderDrawingListDtoList.observe(viewLifecycleOwner){
            drawingListFragmentViewModel.changeCurrentToRecent()
            Log.d(TAG, "init currentDrawingListDtoList :  ${drawingListFragmentViewModel.currentDrawingListDtoList}")

            //맨 처음에는 리스트 가장 첫 번째 그림
//            drawingListFragmentViewModel.changeSelectedDrawingListDto(drawingListFragmentViewModel.currentDrawingListDtoList[0])

            drawingListFragmentViewModel.getSelectedDrawingDetailDto(drawingListFragmentViewModel.selectedDrawingListDto)
            Glide.with(binding.imageDrawingProfile)
                .load(drawingListFragmentViewModel.selectedDrawingListDto.imageUrl)
                .into(binding.imageDrawingProfile)
            binding.textDrawingArtist.text = drawingListFragmentViewModel.selectedDrawingListDto.artist
            binding.textDrawingTitle.text = drawingListFragmentViewModel.selectedDrawingListDto.title
            initRecyclerView()
        }

        drawingListFragmentViewModel.rankingOrderDrawingListDtoList.observe(viewLifecycleOwner){
            drawingListFragmentViewModel.changeCurrentToRanking()
            Log.d(TAG, "init currentDrawingListDtoList :  ${drawingListFragmentViewModel.currentDrawingListDtoList}")

            //맨 처음에는 리스트 가장 첫 번째 그림
//            drawingListFragmentViewModel.changeSelectedDrawingListDto(drawingListFragmentViewModel.currentDrawingListDtoList[0])

            drawingListFragmentViewModel.getSelectedDrawingDetailDto(drawingListFragmentViewModel.selectedDrawingListDto)
            Glide.with(binding.imageDrawingProfile)
                .load(drawingListFragmentViewModel.selectedDrawingListDto.imageUrl)
                .into(binding.imageDrawingProfile)
            binding.textDrawingArtist.text = drawingListFragmentViewModel.selectedDrawingListDto.artist
            binding.textDrawingTitle.text = drawingListFragmentViewModel.selectedDrawingListDto.title
            initRecyclerView()
        }

        drawingListFragmentViewModel.selectedDrawingCommentList.observe(viewLifecycleOwner) {
        }
    }
}