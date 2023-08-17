package com.ssafy.likloud.ui.onboarding

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.ApplicationClass.Companion.ONBOARD_DONE
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

    override fun initListener() {

    }


    // adapter에 onboarding 파일 연결
    private fun initAdapter() {
        onboardingAdapter = OnboardingAdapter()
         binding.viewpager.apply {
             onboardingAdapter.submitList(
                 listOf(
                     OnboardData(1, context.getString(R.string.onboarding_one), R.drawable.image_onboard_1),
                     OnboardData(1, context.getString(R.string.onboarding_two),R.drawable.image_onboard_2),
                     OnboardData(3, context.getString(R.string.onboarding_three), R.drawable.image_onboard_3),
                     OnboardData(4, context.getString(R.string.onboarding_four), R.drawable.image_onboard_4),
                     OnboardData(5, context.getString(R.string.onboarding_five), R.drawable.image_onboard_5)
                 )
             )
             adapter = onboardingAdapter

         }
        binding.springDotsIndicator.attachTo(binding.viewpager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            ApplicationClass.sharedPreferences.setBoolean(ONBOARD_DONE,true)
        }
    }

}