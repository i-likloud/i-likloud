package com.ssafy.likloud.ui.game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import androidx.activity.OnBackPressedCallback
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Singleton

private const val TAG = "차선호"
@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding>(
    FragmentGameBinding::bind, R.layout.fragment_game) {

    private val gameFragmentViewModel : GameFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var coroutineProfile: CoroutineScope
    private lateinit var coroutineTime: CoroutineScope

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

        showGameStartDialog()
//        initObserver()
//        init()
//        initListener()

    }

    private fun initObserver(){
        gameFragmentViewModel.initialFrameHeight.observe(viewLifecycleOwner){
            initView()
        }

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

//                Log.d(TAG, "start -> end $startValue -> $endValue")
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

        gameFragmentViewModel.isCorrected.observe(viewLifecycleOwner){
            binding.textAnswerRight.isClickable = false
            binding.textAnswerLeft.isClickable  = false
            viewLifecycleOwner.lifecycleScope.launch{
                if(gameFragmentViewModel.direction==0){ //왼쪽이 정답
                    binding.apply {
                        lottieLeftCorrect.apply {
                            visibility = View.VISIBLE
                            playAnimation()
                        }
                        lottieRightIncorrect.apply {
                            visibility = View.VISIBLE
                            playAnimation()
                        }

                        lottieLeftIncorrect.visibility = View.INVISIBLE
                        lottieRightCorrect.visibility = View.INVISIBLE
                    }
                }else{
                    binding.apply {
                        lottieRightCorrect.apply {
                            visibility = View.VISIBLE
                            playAnimation()
                        }
                        lottieLeftIncorrect.apply {
                            visibility = View.VISIBLE
                            playAnimation()
                        }

                        lottieLeftCorrect.visibility = View.INVISIBLE
                        lottieRightIncorrect.visibility = View.INVISIBLE
                    }
                }
                delay(700)
                binding.apply {
                    textAnswerRight.isClickable = true
                    textAnswerLeft.isClickable  = true
                    lottieLeftCorrect.visibility = View.INVISIBLE
                    lottieRightIncorrect.visibility = View.INVISIBLE
                    lottieLeftIncorrect.visibility = View.INVISIBLE
                    lottieRightCorrect.visibility = View.INVISIBLE
                }
                gameFragmentViewModel.increaseCurrentQuestionIdx()
            }
        }

    }

    private fun init() {
        Log.d(TAG, "init...")
        view?.post {
            gameFragmentViewModel.setInitialFrameSize(binding.layoutImageProfile.width, binding.layoutImageProfile.height)
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
            //뒤로가기 눌렀을 때
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
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

    private fun initView(){
        // Get the initial width and height of the image_member view
        gameFragmentViewModel.setFrameWidth()
        gameFragmentViewModel.setFrameHeight()
        Log.d(TAG, "init: ${gameFragmentViewModel.frameWidth.value}")

//        coroutineProfile = CoroutineScope(Dispatchers.Main)
        coroutineProfile.launch {
            while(gameFragmentViewModel.frameWidth.value!! > 12){
                delay(200)
                gameFragmentViewModel.decreaseFrameWidth(6)
                gameFragmentViewModel.decreaseFrameHeight(6)
                if(gameFragmentViewModel.remainTime.value!!<=0) break
            }
            // 여긴 무조건 실패 종료
            if(gameFragmentViewModel.remainTime.value!!>0) {
                Log.d(TAG, "faildialog")
                gameFragmentViewModel.failGameDialog.show(childFragmentManager,"fail")
            }
        }

//        coroutineTime = CoroutineScope(Dispatchers.Main)
        coroutineTime.launch {
            Log.d(TAG, "coroutineTime launch....")
            while(gameFragmentViewModel.remainTime.value!! > 0){
                delay(1000)
                gameFragmentViewModel.decreaseRemainTime()
                if(gameFragmentViewModel.frameWidth.value!!<=12) break
            }

            //이 밑에 게임 성공 종료 로직 필요
            if(gameFragmentViewModel.frameWidth.value!!>12) {
                activityViewModel.plusSilver()
                Log.d(TAG, "현재 dialog ${childFragmentManager.findFragmentById(R.id.frame_fragment_upload) is SuccessGameDialog && (childFragmentManager.findFragmentById(R.id.frame_fragment_upload) as SuccessGameDialog).isAdded}")
                gameFragmentViewModel.successGameDialog.show(childFragmentManager,"success")
            }
        }

        //초기 문제
        binding.apply {
            Log.d(TAG, "init randomidx ${gameFragmentViewModel.randomQuestionIdxList}")
            textProblemQuiz.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[0]].problem
            textAnswerLeft.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[0]].answerLeft
            textAnswerRight.text = QuestionLIist.questionList[gameFragmentViewModel.randomQuestionIdxList[0]].answerRight
            Log.d(TAG, "initView activityViewModel.memberInfo : ${activityViewModel.memberInfo.value!!}")
            imageMemberColor.setImageResource(activityViewModel.waterDropColorList[activityViewModel.memberInfo.value!!.profileColor].resourceId)
            imageMemberFace.setImageResource(activityViewModel.waterDropFaceList[activityViewModel.memberInfo.value!!.profileFace].resourceId)
            imageMemberAccessory.setImageResource(activityViewModel.waterDropAccessoryList[activityViewModel.memberInfo.value!!.profileAccessory].resourceId)
        }
    }

    fun goHomefragment(){
        findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
    }

    override fun onPause() {
        Log.d(TAG, "onPause..")
        super.onPause()
        coroutineProfile.cancel()
        coroutineTime.cancel()
    }

    fun refreshFragment(){
        gameFragmentViewModel.initRemainTime()
        coroutineProfile.cancel()
        coroutineTime.cancel()
        coroutineTime = CoroutineScope(Dispatchers.Main)
        coroutineProfile = CoroutineScope(Dispatchers.Main)
//        initObserver()
        initView()
//        initListener()
    }

    fun showGameStartDialog(){
        viewLifecycleOwner.lifecycleScope.launch {
            delay(300)
            gameFragmentViewModel.gameStartDialog.show(childFragmentManager, "game start")
            coroutineTime = CoroutineScope(Dispatchers.Main)
            coroutineProfile = CoroutineScope(Dispatchers.Main)
        }
    }

    fun gameStart(){
        initObserver()
        init()
        initListener()
    }


}