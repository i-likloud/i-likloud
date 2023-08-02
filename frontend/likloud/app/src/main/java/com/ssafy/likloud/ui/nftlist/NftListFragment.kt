package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.FragmentNftListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
            val nftAdapter = NftListAdapter(mainActivity, nftList)
            recyclerviewNft.apply {
                this.adapter = nftAdapter
                layoutManager = GridLayoutManager(mainActivity, 3)
            }

        }
        viewLifecycleOwner.lifecycleScope.launch{
        }
    }

    private fun initDrawingList(): ArrayList<DrawingListDto>{
        val list = ArrayList<DrawingListDto>()

        list.add(DrawingListDto())
        list.add(DrawingListDto())
        list.add(DrawingListDto())
        list.add(DrawingListDto())
        list.add(DrawingListDto())

        return list
    }


}