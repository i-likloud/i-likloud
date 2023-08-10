package com.ssafy.likloud.ui.info

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentInfoBinding
import com.ssafy.likloud.ui.drawing.DrawingDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>(
    FragmentInfoBinding::bind, R.layout.fragment_info
) {
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        init()
    }

    override fun initListener() {
        binding.buttonBack.setOnClickListener {
            Log.d("차선호", "backbutton clicked,,,,")
            findNavController().popBackStack()
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

    private fun init(){
        binding.webview.apply {
            webViewClient = WebViewClient()
            loadUrl(getString(R.string.info_url))
        }
    }
}