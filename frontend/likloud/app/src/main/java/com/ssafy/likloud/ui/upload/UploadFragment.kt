package com.ssafy.likloud.ui.upload

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
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingListBinding
import com.ssafy.likloud.databinding.FragmentUploadBinding
import com.ssafy.likloud.ui.drawinglist.CommentListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListAdapter
import com.ssafy.likloud.ui.drawinglist.DrawingListFragmentViewModel
import kotlinx.coroutines.launch


class UploadFragment : BaseFragment<FragmentUploadBinding>(FragmentUploadBinding::bind, R.layout.fragment_upload) {

    private val uploadFragmentViewModel : UploadFragmentViewModel by viewModels()
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

    }
}