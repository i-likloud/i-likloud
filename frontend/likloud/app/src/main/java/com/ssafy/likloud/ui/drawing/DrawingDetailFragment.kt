package com.ssafy.likloud.ui.drawing

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ssafy.likloud.ApplicationClass
import com.ssafy.likloud.ApplicationClass.Companion.USER_EMAIL
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingDetailDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.databinding.FragmentDrawingDetailBinding
import com.ssafy.likloud.ui.photo.PhotoDetailFragmentDirections
import com.ssafy.likloud.util.hideKeyboard
import com.ssafy.likloud.util.initEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "차선호"
@AndroidEntryPoint
class DrawingDetailFragment : BaseFragment<FragmentDrawingDetailBinding>(
    FragmentDrawingDetailBinding::bind, R.layout.fragment_drawing_detail
) {

    private val drawingDetailFragmentViewModel : DrawingDetailFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    val args: DrawingDetailFragmentArgs by navArgs()
    private lateinit var  memberProfileDto : MemberProfileDto
    private lateinit var commentListAdapter: CommentListAdapter
    private var isCurUserObserved : Boolean = false
    private var isCurCommentObserved : Boolean = false


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
        loadingAnimation()

        initObserver()
        init()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun initObserver(){
        drawingDetailFragmentViewModel.currentDrawingDetail.observe(viewLifecycleOwner) {
            //현재 가운데 있는 그림 정보 조회 & 초기 좋아요 세팅
            Log.d(TAG, "현재 drrawingdetaildto : $it")
            drawingDetailFragmentViewModel.getCurrentDrawingMember(it.memberId)
            drawingDetailFragmentViewModel.setIsLiked()
            drawingDetailFragmentViewModel.setLikeCount()
        }

        drawingDetailFragmentViewModel.currentDrawingMember.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: observed currentDrawingMember")
            if(activityViewModel.memberInfo.value!=null) initInfoView(drawingDetailFragmentViewModel.currentDrawingDetail.value!!, it)
            memberProfileDto = it
            if(isCurUserObserved) return@observe
            activityViewModel.memberInfo.observe(viewLifecycleOwner){
                isCurUserObserved = true
                Log.d(TAG, "initObserver: observed currentUser")
                // 현재 user가 받아져 오면 recyclerview 생성

                initCurUserView(memberProfileDto)

                // user가 받아져 온 다음에 comment ui 업데이트
                if(isCurCommentObserved) return@observe

                drawingDetailFragmentViewModel.currentDrawingCommentList.observe(viewLifecycleOwner){
                    initCommentRecyclerView()
                    Log.d(TAG, "initObserver: observed comment")
                    isCurCommentObserved = true
                    commentListAdapter.submitList(it.toMutableList())
                    if(it.size!=0) binding.recyclerviewDrawingComment.smoothScrollToPosition(it.size)
                }

                initInfoView(drawingDetailFragmentViewModel.currentDrawingDetail.value!!,drawingDetailFragmentViewModel.currentDrawingMember.value!!)
            }
            activityViewModel.getMemberInfo(ApplicationClass.sharedPreferences.getString(USER_EMAIL).toString())
        }



        drawingDetailFragmentViewModel.isLiked.observe(viewLifecycleOwner){
            Log.d(TAG, "current isLiked: $it")
            if(it){
                binding.imageHeart.setImageResource(R.drawable.icon_selected_heart)
            }else{
                binding.imageHeart.setImageResource(R.drawable.icon_unselected_heart)
            }
        }

        drawingDetailFragmentViewModel.likeCount.observe(viewLifecycleOwner){
            binding.textLikeCount.text = it.toString()
        }


        drawingDetailFragmentViewModel.nftYn.observe(viewLifecycleOwner){
            if (it){
                binding.buttonNft.setBackgroundResource(R.drawable.frame_button_grey_mild)
            } else {
                binding.buttonNft.setBackgroundResource(R.drawable.frame_button_purple_mild)
            }
        }

//        activityViewModel.isWallet.observe(viewLifecycleOwner){
//            if(it){
////                Toast.makeText(mainActivity, "지급 발급 완료!  맘껏이제 하시오", Toast.LENGTH_SHORT).show()
////                Log.d(TAG, "지갑 발급 완료!")
//                if(activityViewModel.memberInfo.value!!.silverCoin>=3) {
//                    drawingDetailFragmentViewModel.registNft(args.drawingId)
//                }else{
//                    //여기서 siverCoin 부족 메시지
//                    showSnackbar(binding.root, "fail",getString(R.string.nft_fail))
////                    Toast.makeText(mainActivity,"silverCoin 확인 바람", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            activityViewModel.isWallet.collectLatest {
                if(it){
    //                Toast.makeText(mainActivity, "지급 발급 완료!  맘껏이제 하시오", Toast.LENGTH_SHORT).show()
    //                Log.d(TAG, "지갑 발급 완료!")
                    if(activityViewModel.memberInfo.value!!.silverCoin>=3) {
                        drawingDetailFragmentViewModel.registNft(args.drawingId)
                    }else{
                        //여기서 siverCoin 부족 메시지
                        showSnackbar(binding.root, "fail",getString(R.string.nft_fail))
    //                    Toast.makeText(mainActivity,"silverCoin 확인 바람", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingDetailFragmentViewModel.isSuccess.collectLatest {
                dismissLoadingDialog()
                showSnackbar(binding.root, "movetonft",getString(R.string.nft_success))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingDetailFragmentViewModel.isDeleted.collectLatest {
                Log.d(TAG, "isDeleted : $it")
                if(it) showSnackbar(binding.root, "info", "삭제되었습니다.")
                findNavController().popBackStack()
            }
        }

//        drawingDetailFragmentViewModel.isSuccess.observe(viewLifecycleOwner){
////            Toast.makeText(mainActivity, "nft 발급 완료", Toast.LENGTH_SHORT).show()
//            Log.d(TAG, "isSuccess : $it")
//            showSnackbar(binding.root, "movetonft",getString(R.string.nft_success))
//            //seekbar를 통해 확인하러 가기 만들면 좋을듯
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingDetailFragmentViewModel.isReported.collectLatest {
                showSnackbar(binding.root, "info" , getString(R.string.report_message))
//                Toast.makeText(mainActivity, "신고 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(){
        //여기서 args.drawingId로 DrawingDetailDto 불러와야 함
        Log.d(TAG, "init: args.drawingId is ${args.drawingId}")
        drawingDetailFragmentViewModel.getCurrentPhotoDrawingDetail(args.drawingId)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {

        initEditText(
            binding.edittextDrawingComment,
            null,
            binding.layoutDrawingDetailFragment,
            mActivity,
            null
        )

        binding.apply {
            buttonBack.setOnClickListener {
                if(args.isFromFCM){
                    findNavController().navigate(R.id.action_drawingDetailFragment_to_homeFragment)
                }
                else{
                    findNavController().popBackStack()
                }

            }
            imageHeart.setOnClickListener {
                drawingDetailFragmentViewModel.changeLikeCount()
                drawingDetailFragmentViewModel.changeIsLiked()
            }
            buttonDrawingComment.setOnClickListener {
                val content = edittextDrawingComment.text.toString()
                if(content == ""){
                    showSnackbar(binding.root, "blue_bar", "댓글을 입력해주세요!")
//                    Toast.makeText(mainActivity,"댓글을 입력하세요", Toast.LENGTH_SHORT).show()
                }else{
                    //댓글 입력 함수
                    drawingDetailFragmentViewModel.registDrawingComment(drawingDetailFragmentViewModel.currentDrawingDetail.value!!.drawingId, content)
                    edittextDrawingComment.setText("")
                    edittextDrawingComment.clearFocus()
                    val keyboard = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.hideSoftInputFromWindow(edittextDrawingComment.windowToken,0)
                }
            }
            buttonNft.setOnClickListener {
                showLoadingDialog(mActivity)
                Log.d(TAG, "현재 지갑 상태 : ${activityViewModel.memberInfo.value!!.wallet} 현재 그림 nftyYn ${drawingDetailFragmentViewModel.nftYn.value}")
                if(drawingDetailFragmentViewModel.nftYn.value == false) {
                    if (activityViewModel.memberInfo.value!!.wallet == null) {
                        // 지갑 발행해라
                        Log.d(TAG, "지갑이 없네요 발급할게요")
                        activityViewModel.getNftWallet()
                        dismissLoadingDialog()
                    } else {
                        Log.d(TAG, "이미 지갑이 있네요")
                        if (activityViewModel.memberInfo.value!!.silverCoin >= 3) {
                            drawingDetailFragmentViewModel.registNft(args.drawingId)
                        } else {
                            dismissLoadingDialog()
                            //여기서 siverCoin 부족 메시지
                            showSnackbar(binding.root, "fail",getString(R.string.nft_fail))
//                            Toast.makeText(mainActivity, "silverCoin 확인 바람", Toast.LENGTH_SHORT)
//                                .show()
                        }
                    }
                }else{
                    dismissLoadingDialog()
                    showSnackbar(binding.root, "blue_bar", "이미 발급 받은 그림이에요.")
//                    Toast.makeText(mainActivity, "이미 발급 받은 그림입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            buttonReport.setOnClickListener {
                drawingDetailFragmentViewModel.setDrawingReportDialog()
                drawingDetailFragmentViewModel.drawingReportDialog.show(childFragmentManager, "report")
            }

            imageCurrentDrawing.setOnClickListener{
                drawingDetailFragmentViewModel.currentDrawingDetail.value?.let {
//                    val action = DrawingDetailFragmentDirections.actionDrawingDetailFragmentToDrawingOriginalFragment(it.imageUrl)
//                    findNavController().navigate(action)
                    Glide.with(binding.imageDrawingOrigin)
                        .load(it.imageUrl)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                        .into(binding.imageDrawingOrigin)
                    binding.constraintDrawingOriginal.visibility = View.VISIBLE
                    binding.imageDrawingOrigin.visibility = View.VISIBLE
                    binding.imageDrawingOrigin.scaleX = 0.0f
                    binding.imageDrawingOrigin.scaleY = 0.0f
                    val animationX = ObjectAnimator.ofFloat(binding.imageDrawingOrigin, "scaleX", 0.0f, 1.0f)
                    val animationY = ObjectAnimator.ofFloat(binding.imageDrawingOrigin, "scaleY", 0.0f, 1.0f)
                    animationX.duration = 400
                    animationY.duration = 400
                    val animation = AnimatorSet()
                    animation.playTogether(animationX, animationY)
                    animation.start()
                    binding.layoutAppbar.setBackgroundResource(R.color.background_half_transparent)
                }
            }


            constraintDrawingOriginal.setOnClickListener {
                val animationX = ObjectAnimator.ofFloat(binding.imageDrawingOrigin, "scaleX", 1.0f, 0.0f)
                val animationY = ObjectAnimator.ofFloat(binding.imageDrawingOrigin, "scaleY", 1.0f, 0.0f)
                animationX.duration = 400
                animationY.duration = 400
                val animation = AnimatorSet()
                animation.playTogether(animationX, animationY)
                animation.start()
                animation.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {}
                    override fun onAnimationEnd(p0: Animator) {
                        it.visibility = View.GONE
                        layoutAppbar.setBackgroundResource(R.color.transparent)
                    }
                    override fun onAnimationCancel(p0: Animator) {}
                    override fun onAnimationRepeat(p0: Animator) {}
                })
            }

            buttonDelete.setOnClickListener {
                drawingDetailFragmentViewModel.deleteDrawingDialog.show(childFragmentManager,"delete")
            }

            buttonModify.setOnClickListener {

            }
        }
        // 안드로이드 뒤로가기 버튼 눌렀을 때
        mainActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(args.isFromFCM){
                        findNavController().navigate(R.id.action_drawingDetailFragment_to_homeFragment)
                    }
                    else{
                        findNavController().popBackStack()
                    }

                }
            }
        )


    }

    private fun initInfoView(drawingDetail: DrawingDetailDto,member: MemberProfileDto){
        binding.apply {
            Glide.with(binding.imageCurrentDrawing)
                .load(drawingDetail.imageUrl)
                .into(binding.imageCurrentDrawing)
            Glide.with(binding.imageProfileColor)
                .load(activityViewModel.waterDropColorList[member.profileColor].resourceId)
                .into(binding.imageProfileColor)
            Glide.with(binding.imageProfileFace)
                .load(activityViewModel.waterDropFaceList[member.profileFace].resourceId)
                .into(binding.imageProfileFace)
            Glide.with(binding.imageProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[member.profileAccessory].resourceId)
                .into(binding.imageProfileAccessory)
            textDrawingNickname.text = member.nickname
            textDrawingTitle.text = drawingDetail.title
            textDrawingContent.text = drawingDetail.content
            textLikeCount.text = drawingDetail.likesCount.toString()
            textViewCount.text = drawingDetail.viewCount.toString()
            Log.d(TAG, "memberInfo .. ${activityViewModel.memberInfo.value}")
            if(drawingDetail.memberId == activityViewModel.memberInfo.value!!.memberId){
                buttonDelete.visibility = View.VISIBLE
                buttonModify.visibility = View.VISIBLE
            }else{
                buttonDelete.visibility = View.GONE
                buttonModify.visibility = View.GONE
            }
        }
    }

    private fun initCurUserView(member: MemberProfileDto){
//        Log.d(TAG, "member ${member.nickname} // user ${activityViewModel.memberInfo.value!!.nickname}")
        if(member.nickname == activityViewModel.memberInfo.value!!.nickname){
            binding.buttonNft.visibility = View.VISIBLE
        }else{
            binding.buttonNft.visibility = View.GONE
        }
    }

    private fun initCommentRecyclerView(){
        Log.d(TAG, "commentList : ${drawingDetailFragmentViewModel.currentDrawingCommentList.value} ")
        commentListAdapter = CommentListAdapter(activityViewModel)
        binding.recyclerviewDrawingComment.apply {
            this.adapter = commentListAdapter.apply {
                this.itemClickListner = object: CommentListAdapter.ItemClickListener{
                    override fun onClick(comment: CommentDto, position: Int) {
//                        drawingDetailFragmentViewModel.deleteDrawingComment(comment.commentId, position)
                        Log.d(TAG, "onClick...")
                        drawingDetailFragmentViewModel.createDeleteCommentDialog(comment.commentId, position)
                        drawingDetailFragmentViewModel.deleteCommentDialog.show(childFragmentManager, "deleteComment")
                    }
                }
            }
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun sendReport(content: String){
        drawingDetailFragmentViewModel.sendReport(content)
    }

    /**
     * 뷰에 X축으로 움직이는 애니메이션을 적용시킵니다.
     */
    private fun makeAnimationX(view: View, values: Float, speed: Long) {
        ObjectAnimator.ofFloat(view, "translationX", values).apply {
//            interpolator = DecelerateInterpolator()
            interpolator = OvershootInterpolator()
//            interpolator = AccelerateInterpolator()
//            interpolator = AccelerateDecelerateInterpolator()
            duration = speed
            start()
        }
    }

    private fun loadingAnimation() {
        binding.layoutInfo.translationX = 1300f
        binding.layoutComment.translationX = 1300f
        binding.layoutImage.translationX = -1300f
        initAnimation()
    }

    private fun initAnimation() {
        lifecycleScope.launch {
            makeAnimationX(binding.layoutInfo, 0f, 450)
            delay(100)
            makeAnimationX(binding.layoutComment, 0f, 500)
            delay(50)
            makeAnimationX(binding.layoutImage, 0f, 600)
        }
    }
}