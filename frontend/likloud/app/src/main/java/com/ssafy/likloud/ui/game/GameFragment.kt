package com.ssafy.likloud.ui.game

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.ViewUtils.dpToPx
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentGameBinding
import com.ssafy.likloud.ui.nftlist.NftListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "차선호"
@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding>(
    FragmentGameBinding::bind, R.layout.fragment_game) {

    private val gameFragmentViewModel : GameFragmentViewModel by viewModels()
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

        initObserver()
        init()
        initListener()

    }

    private fun initObserver(){
        gameFragmentViewModel.imageWidth.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                val layoutParams = binding.imageMember.layoutParams
                layoutParams.width = gameFragmentViewModel.imageWidth.value!!
                binding.imageMember.layoutParams = layoutParams
            }
        }
        gameFragmentViewModel.imageHeight.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                val layoutParams = binding.imageMember.layoutParams
                layoutParams.height = gameFragmentViewModel.imageHeight.value!!
                binding.imageMember.layoutParams = layoutParams
            }
        }

        gameFragmentViewModel.remainTime.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                val progressbarTime = binding.progressbarTime
                Log.d(TAG, "remainTime : ${gameFragmentViewModel.remainTime.value} ")
                progressbarTime.progress = ((gameFragmentViewModel.remainTime.value!!/60.0) * 100.0).toInt()
                Log.d(TAG, "progress : ${progressbarTime.progress} ")
            }
        }
    }

    private fun init(){

        view?.post{
            // Get the initial width and height of the image_member view
            gameFragmentViewModel.changeImageWidth(binding.imageMember.width)
            gameFragmentViewModel.changeImageHeight(binding.imageMember.height)

             CoroutineScope(Dispatchers.Main).launch {
                while(gameFragmentViewModel.imageWidth.value!! > 12){
                    delay(200)
                    gameFragmentViewModel.changeImageWidth(gameFragmentViewModel.imageWidth.value!! - 4)
                    gameFragmentViewModel.changeImageHeight(gameFragmentViewModel.imageHeight.value!! - 4)
//                    Log.d(TAG, "width / height : ${gameFragmentViewModel.imageWidth.value} / ${gameFragmentViewModel.imageHeight.value}")
                }
            }

            // Time PROGRESS BAR
            gameFragmentViewModel.changeRemainTime(60)

            CoroutineScope(Dispatchers.Main).launch {
                while(gameFragmentViewModel.remainTime.value!! >= 0){
                    delay(1000)
                    gameFragmentViewModel.changeRemainTime(gameFragmentViewModel.remainTime.value!!-1)
                }
            }
        }

    }


    override fun initListener() {

    }
}