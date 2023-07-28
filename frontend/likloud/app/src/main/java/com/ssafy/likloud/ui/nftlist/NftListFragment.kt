package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingDto
import com.ssafy.likloud.databinding.FragmentNftListBinding
import com.ssafy.likloud.databinding.FragmentPhotoDrawingDetailBinding
import com.ssafy.likloud.ui.photolist.PhotoDrawingDetailFragmentViewModel
import kotlinx.coroutines.launch


class NftListFragment : BaseFragment<FragmentNftListBinding>(
    FragmentNftListBinding::bind, R.layout.fragment_nft_list) {

    private val nftListFragmentViewModel : NftListFragmentViewModel by viewModels()
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
            val nftList = initDrawingList()
            val nftAdapter = NftAdapter(mainActivity, nftList)
            recyclerviewNft.apply {
                this.adapter = nftAdapter
                layoutManager = GridLayoutManager(mainActivity, 3)
            }

        }
        viewLifecycleOwner.lifecycleScope.launch{
        }
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