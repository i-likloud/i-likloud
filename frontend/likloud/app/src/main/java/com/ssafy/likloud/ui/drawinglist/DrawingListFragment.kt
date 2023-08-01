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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            drawingListFragmentViewModel.getRecentOrderDrawingListDtoList()
        }
    }

    override fun initListener(){

        binding.apply {
            //랭킹순 눌렀을 때
            buttonRankingOrder.setOnClickListener{
                drawingListFragmentViewModel.getRankingOrderDrawingListDtoList()
            }

            //최신순 눌렀을 때
            buttonRecentOrder.setOnClickListener {
                drawingListFragmentViewModel.getRecentOrderDrawingListDtoList()
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
    }

    private fun initRecyclerView(){
        val drawingListAdapter =
                DrawingListAdapter(drawingListFragmentViewModel.currentDrawingListDtoList.value!!)
        binding.apply {
            Log.d(TAG, "selectedDrawing : ${drawingListFragmentViewModel.selectedDrawingDetailDto.value} ")
            recyclerviewDrawaing.apply {
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

    private fun initObserver(){

        drawingListFragmentViewModel.currentDrawingListDtoList.observe(viewLifecycleOwner){
            Log.d(TAG, "currentDrawingListDtoList observe 발동... ")
            initRecyclerView()
            drawingListFragmentViewModel.getSelectedDrawingDetailDto(drawingListFragmentViewModel.currentDrawingListDtoList.value!![0])
        }

        drawingListFragmentViewModel.selectedDrawingDetailDto.observe(viewLifecycleOwner){
            Log.d(TAG, "selectedDrawing : ${drawingListFragmentViewModel.selectedDrawingDetailDto.value} ")
            binding.apply {
                Glide.with(binding.imageDrawingProfile)
                    .load(drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.imageUrl)
                    .into(binding.imageDrawingProfile)
                textDrawingArtist.text = drawingListFragmentViewModel.selectedDrawingDetailDto.value!!.artist
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
}