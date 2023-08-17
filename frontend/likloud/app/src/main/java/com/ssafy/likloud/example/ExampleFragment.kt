package com.ssafy.likloud.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentExampleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExampleFragment : BaseFragment<FragmentExampleBinding>(FragmentExampleBinding::bind, R.layout.fragment_example) {

    private val exampleFragmentViewModel : ExampleFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exampleFragmentViewModel.getUserInfo(1)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initListener() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch{
            exampleFragmentViewModel.user.observe(requireActivity()){
                binding.textview.text = it.title
            }
        }
    }

}