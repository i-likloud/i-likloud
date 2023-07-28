package com.ssafy.likloud.ui.photolist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDto
import com.ssafy.likloud.databinding.FragmentDrawingListBinding
import com.ssafy.likloud.databinding.FragmentPhotoDrawingDetailBinding
import com.ssafy.likloud.ui.drawinglist.CommentListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListFragmentViewModel
import kotlinx.coroutines.launch

//PhotoListFragment에서 해당 그림 선택하면 그 그림 객체 얻어와야함!!!!
class PhotoDrawingDetailFragment : BaseFragment<FragmentPhotoDrawingDetailBinding>(
    FragmentPhotoDrawingDetailBinding::bind, R.layout.fragment_photo_drawing_detail) {

    private val photoDrawingDetailFragmentViewModel : PhotoDrawingDetailFragmentViewModel by viewModels()
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

    override fun initListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


        }
        viewLifecycleOwner.lifecycleScope.launch{
        }
    }


}