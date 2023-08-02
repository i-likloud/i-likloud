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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.internal.ViewUtils.dpToPx
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentGameBinding
import com.ssafy.likloud.ui.nftlist.NftListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ticker
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
    private val activityViewModel: MainActivityViewModel by activityViewModels()

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
        gameFragmentViewModel.frameWidth.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                val layoutParams = binding.layoutImageProfile.layoutParams
                layoutParams.width = gameFragmentViewModel.frameWidth.value!!
                binding.layoutImageProfile.layoutParams = layoutParams
            }
        }

        gameFragmentViewModel.remainTime.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                val progressbarTime = binding.progressbarTime
                val startValue = ((it/30.0) * 100.0).toInt()// 시작 퍼센트
                val endValue = (((it-1)/30.0) * 100.0).toInt() // 목표 퍼센트

                Log.d(TAG, "start -> end $startValue -> $endValue")
                val animator = ValueAnimator.ofInt(startValue, endValue)
                animator.duration = 1000 // 애니메이션 시간 (밀리초)
                animator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    progressbarTime.progress = animatedValue
                }
                animator.start()
            }
            binding.apply {
                textTime.text = it.toString()
            }
        }

        gameFragmentViewModel.currentQuestionIdx.observe(viewLifecycleOwner){
            binding.apply {
                textProblemQuiz.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[gameFragmentViewModel.currentQuestionIdx.value!!]].problem
                textAnswerLeft.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[gameFragmentViewModel.currentQuestionIdx.value!!]].answerLeft
                textAnswerRight.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[gameFragmentViewModel.currentQuestionIdx.value!!]].answerRight
            }
        }

        gameFragmentViewModel.nextFragment.observe(viewLifecycleOwner){
            Log.d(TAG, "nextFragment : ${it} ")
            //여기서 Fragment 변경 / 새로고침
            if(it == "home"){
                findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
            }else{
                findNavController().navigate(R.id.action_gameFragment_self)
            }
        }
    }

    private fun init(){

        view?.post{
            // Get the initial width and height of the image_member view
            gameFragmentViewModel.setFrameWidth(binding.layoutImageProfile.width)
            gameFragmentViewModel.setFrameHeight(binding.layoutImageProfile.height)

             CoroutineScope(Dispatchers.Main).launch {
                while(gameFragmentViewModel.frameWidth.value!! > 12){
                    delay(200)
                    gameFragmentViewModel.decreaseFrameWidth(4)
                    gameFragmentViewModel.decreaseFrameHeight(4)
                    if(gameFragmentViewModel.remainTime.value!!<=0) break
                }
                // 여긴 무조건 실패 종료
                if(gameFragmentViewModel.remainTime.value!!>0) gameFragmentViewModel.failGameDialog.show(childFragmentManager,"fail")
            }

            // Time PROGRESS BAR
            CoroutineScope(Dispatchers.Main).launch {
                while(gameFragmentViewModel.remainTime.value!! > 0){
                    delay(1000)
                    gameFragmentViewModel.decreaseRemainTime()
                    if(gameFragmentViewModel.frameWidth.value!!<=12) break
                }

                //이 밑에 게임 성공 종료 로직 필요
                if(gameFragmentViewModel.frameWidth.value!!>12) {
                    activityViewModel.plusSilver()
                    gameFragmentViewModel.successGameDialog.show(childFragmentManager,"success")
                }
            }

            //초기 문제
            binding.apply {
                Log.d(TAG, "init randomidx ${gameFragmentViewModel.randomQuestionIdxList}")
                textProblemQuiz.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[0]].problem
                textAnswerLeft.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[0]].answerLeft
                textAnswerRight.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[0]].answerRight

                imageMemberColor.setImageResource(activityViewModel.waterDropColorList[activityViewModel.memberInfo.value!!.profileColor].resourceId)
                imageMemberFace.setImageResource(activityViewModel.waterDropFaceList[activityViewModel.memberInfo.value!!.profileFace].resourceId)
                imageMemberAccessory.setImageResource(activityViewModel.waterDropAccessoryList[activityViewModel.memberInfo.value!!.profileAccessory].resourceId)
            }
        }

    }


    override fun initListener() {
        binding.apply {
            textAnswerLeft.setOnClickListener {
                gameFragmentViewModel.checkAnswer(textAnswerLeft.text.toString())
            }
            textAnswerRight.setOnClickListener {
                gameFragmentViewModel.checkAnswer(textAnswerRight.text.toString())
            }
        }
    }

    fun changeNextFragment(value: String){
        gameFragmentViewModel.changeNextFragment(value)
    }

}