package com.ssafy.likloud.ui.drawinglist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDto
import com.ssafy.likloud.databinding.FragmentDrawingListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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



        binding.apply {
            //그림 목록 리사이클러뷰
            val drawingList = initDrawingList()
            val drawingListAdapter = DrawingListAdapter(drawingList)
            drawingRecyclerview.apply {
                this.adapter = drawingListAdapter
                set3DItem(true)
                setAlpha(true)
                setOrientation(RecyclerView.VERTICAL)
                setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                    //본인한테서 멈췄을 때 이벤트
                    override fun onItemSelected(position: Int) {
                        //Cente item
                        binding.drawingExplainTv.text = drawingListAdapter.list[position].text
                        Toast.makeText(mainActivity, drawingListAdapter.list[position].text, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            //댓글 목록 리사이클러뷰
            val commentList = initCommentList()
            val commentListAdapter = CommentListAdapter(commentList)
            drawingCommentRecyclerview.apply {
                this.adapter = commentListAdapter
                this.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL,false)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{

        }
    }

    private fun initCommentList(): ArrayList<CommentDto>{
        val commentList = ArrayList<CommentDto>()
        commentList.add(CommentDto(R.drawable.profile_water_drop_blue, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_orange, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_blue, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_lemon, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_blue, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_lemon, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_blue, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_orange, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        commentList.add(CommentDto(R.drawable.profile_water_drop_blue, "영준 어린이","아놔 나두라고 나 댓글 달거라고~~~ 응애 응애 나 댓글 달게 놔둬줘!!! 제발아아아아", "10 : 19"))
        return commentList
    }

    private fun initDrawingList(): ArrayList<DrawingDto>{
        val list = ArrayList<DrawingDto>()

        list.add(DrawingDto(R.drawable.cloud1, "Thi is cloud1"))
        list.add(DrawingDto(R.drawable.cloud2, "Thi is cloud2"))
        list.add(DrawingDto(R.drawable.cloud3, "Thi is cloud3"))
        list.add(DrawingDto(R.drawable.cloud4, "Thi is cloud4"))
        list.add(DrawingDto(R.drawable.cloud5, "Thi is cloud5"))
        list.add(DrawingDto(R.drawable.cloud1, "Thi is cloud1"))
        list.add(DrawingDto(R.drawable.cloud2, "Thi is cloud2"))
        list.add(DrawingDto(R.drawable.cloud3, "Thi is cloud3"))
        list.add(DrawingDto(R.drawable.cloud4, "Thi is cloud4"))
        list.add(DrawingDto(R.drawable.cloud5, "Thi is cloud5"))
        list.add(DrawingDto(R.drawable.cloud1, "Thi is cloud1"))
        list.add(DrawingDto(R.drawable.cloud2, "Thi is cloud2"))
        list.add(DrawingDto(R.drawable.cloud3, "Thi is cloud3"))
        list.add(DrawingDto(R.drawable.cloud4, "Thi is cloud4"))
        list.add(DrawingDto(R.drawable.cloud5, "Thi is cloud5"))
        list.add(DrawingDto(R.drawable.cloud1, "Thi is cloud1"))
        list.add(DrawingDto(R.drawable.cloud2, "Thi is cloud2"))
        list.add(DrawingDto(R.drawable.cloud3, "Thi is cloud3"))
        list.add(DrawingDto(R.drawable.cloud4, "Thi is cloud4"))
        list.add(DrawingDto(R.drawable.cloud5, "Thi is cloud5"))
        list.add(DrawingDto(R.drawable.cloud1, "Thi is cloud1"))
        list.add(DrawingDto(R.drawable.cloud2, "Thi is cloud2"))
        list.add(DrawingDto(R.drawable.cloud3, "Thi is cloud3"))
        list.add(DrawingDto(R.drawable.cloud4, "Thi is cloud4"))
        list.add(DrawingDto(R.drawable.cloud5, "Thi is cloud5"))

        return list
    }

}