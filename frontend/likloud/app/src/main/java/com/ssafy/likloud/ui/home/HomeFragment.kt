package com.ssafy.likloud.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentHomeBinding
import com.ssafy.likloud.databinding.FragmentLoginBinding
import com.ssafy.likloud.example.ExampleFragmentViewModel
import com.ssafy.likloud.ui.login.LoginFragmentDirections
import com.ssafy.likloud.ui.login.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding ::bind, R.layout.fragment_home ) {

    private val homeFragmentViewModel : HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()

//        viewLifecycleOwner.lifecycleScope.launch{
//            loginFragmentViewModel.user.observe(requireActivity()){
//
//            }
//        }
    }

    private fun initListener() {
        binding.buttonBack.setOnClickListener {

        }
    }
}