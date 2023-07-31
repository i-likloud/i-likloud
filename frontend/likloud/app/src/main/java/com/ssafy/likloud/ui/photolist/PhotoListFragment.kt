package com.ssafy.likloud.ui.photolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.FragmentPhotoListBinding
import com.ssafy.likloud.ui.drawinglist.CommentListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListAdapter
import kotlinx.coroutines.launch

class PhotoListFragment : BaseFragment<FragmentPhotoListBinding>(FragmentPhotoListBinding::bind, R.layout.fragment_photo_list) {

    private val photoListFragmentViewModel : PhotoListFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

    private lateinit var rankifgOrderPhotoList: ArrayList<DrawingListDto>
    private lateinit var recentOrderPhotoList: ArrayList<DrawingListDto>
    private lateinit var photoList: ArrayList<DrawingListDto>

    private lateinit var selectedPhoto: DrawingListDto

    private lateinit var layoutPhotoDrawingList: ConstraintLayout

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

    override fun initListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            //랭킹순 눌렀을 때
            buttonRankingOrder.setOnClickListener{
                photoList = rankifgOrderPhotoList
            }

            //최신순 눌렀을 때
            buttonRecentOrder.setOnClickListener {
                photoList = recentOrderPhotoList
            }

            //그림 목록 리사이클러뷰(최초에는 인기순)
            val photoList = initDrawingList()
//            drawingList = rankingOrderDrawingList

            //맨 처음에는 리스트 가장 첫 번째 그림
            selectedPhoto = photoList[0]
            Glide.with(binding.imageDrawingProfile)
                .load(selectedPhoto.imageUrl)
                .into(binding.imageDrawingProfile)
            binding.textDrawingNickname.text = selectedPhoto.artist

            val photoListAdapter = PhotoListAdapter(photoList)
            recyclerviewDrawaing.apply {
                this.adapter = photoListAdapter
                set3DItem(true)
                setAlpha(true)
                setOrientation(RecyclerView.VERTICAL)
                setItemSelectListener(object : CarouselLayoutManager.OnSelected {
                    //본인한테서 멈췄을 때 이벤트
                    override fun onItemSelected(position: Int) {

                        selectedPhoto = photoListAdapter.list[position]
                        //Cente item
                        Glide.with(binding.imageDrawingProfile)
                            .load(selectedPhoto.imageUrl)
                            .into(binding.imageDrawingProfile)
                        binding.textDrawingNickname.text = selectedPhoto.artist + "NICKNAME"
                    }
                })
            }

            this@PhotoListFragment.layoutPhotoDrawingList = layoutPhotoDrawingList.apply {

            }


            buttonPaint.setOnClickListener {
                toggleLayoutPhotoDrawingListAlpha()
            }

            //즐겨찾기(스타)를 눌렀을 때
            imageStar.setOnClickListener {

            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

    private fun toggleLayoutPhotoDrawingListAlpha(){
        val targetAlpha = if (layoutPhotoDrawingList.alpha == 1.0f) 0.5f else 1.0f
        layoutPhotoDrawingList.alpha = targetAlpha
    }

    private fun initDrawingList(): ArrayList<DrawingListDto>{
        val list = ArrayList<DrawingListDto>()

        list.add(DrawingListDto())
        list.add(DrawingListDto())
        list.add(DrawingListDto())

        return list
    }



}