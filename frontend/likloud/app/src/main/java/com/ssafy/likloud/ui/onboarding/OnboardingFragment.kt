package com.ssafy.likloud.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.databinding.FragmentOnboardingBinding
import com.ssafy.likloud.example.ExampleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::bind, R.layout.fragment_onboarding) {

    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    private fun initAdapter() {
        onboardingAdapter = OnboardingAdapter()
         binding.introViewpager.apply {
             onboardingAdapter.submitList(
                 listOf(
                     OnboardData(1, context.getString(R.string.onboarding_one), R.drawable.onboard_img_1),
                     OnboardData(1, context.getString(R.string.onboarding_two),R.drawable.onboard_img_2),
                     OnboardData(3, context.getString(R.string.onboarding_three), R.drawable.onboard_img_3)
                 )
             )
             adapter = onboardingAdapter

         }
        binding.springDotsIndicator.attachTo(binding.introViewpager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        super.onViewCreated(view, savedInstanceState)
    }

}