package com.ssafy.likloud.ui.drawing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
import dagger.hilt.android.AndroidEntryPoint
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

        initObserver()
        init()
        initListener()
    }

    private fun initObserver(){
        drawingDetailFragmentViewModel.currentDrawingDetail.observe(viewLifecycleOwner){
            //현재 가운데 있는 그림 정보 조회 & 초기 좋아요 세팅
            drawingDetailFragmentViewModel.getCurrentDrawingMember(it.memberId)
            drawingDetailFragmentViewModel.setIsLiked()
            drawingDetailFragmentViewModel.setLikeCount()
        }

        drawingDetailFragmentViewModel.currentDrawingMember.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: observed currentDrawingMember")
            initInfoView(drawingDetailFragmentViewModel.currentDrawingDetail.value!!, it)
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
            }
            activityViewModel.getMemberInfo(ApplicationClass.sharedPreferences.getString(USER_EMAIL)!!)
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
            if(it){
                binding.buttonNft.setBackgroundResource(R.drawable.frame_button_rounded_border_grey_radius50)
            }else{
                binding.buttonNft.setBackgroundResource(R.drawable.frame_rounded_border_skyblue_radius50)
            }
        }

        activityViewModel.isWallet.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(mainActivity, "지급 발급 완료! 이제 맘껏 하시오", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "지갑 발급 완료!")
                if(activityViewModel.memberInfo.value!!.silverCoin>=5) {
                    drawingDetailFragmentViewModel.registNft(args.drawingId)
                }else{
                    //여기서 siverCoin 부족 메시지
                    Toast.makeText(mainActivity,"silverCoin 확인 바람", Toast.LENGTH_SHORT).show()
                }
            }
        }

        drawingDetailFragmentViewModel.isSuccess.observe(viewLifecycleOwner){
            Toast.makeText(mainActivity, "nft 발급 완료", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "nft 발급 완료")
            //seekbar를 통해 확인하러 가기 만들면 좋을듯
        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingDetailFragmentViewModel.isReported.collectLatest {
                Toast.makeText(mainActivity, "신고 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(){
        //여기서 args.drawingId로 DrawingDetailDto 불러와야 함
        Log.d(TAG, "init: args.drawingId is ${args.drawingId}")
        drawingDetailFragmentViewModel.getCurrentPhotoDrawingDetail(args.drawingId)

    }

    override fun initListener() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageHeart.setOnClickListener {
                drawingDetailFragmentViewModel.changeLikeCount()
                drawingDetailFragmentViewModel.changeIsLiked()
            }
            buttonDrawingComment.setOnClickListener {
                val content = edittextDrawingComment.text.toString()
                if(content == ""){
                    Toast.makeText(mainActivity,"댓글을 입력하세요", Toast.LENGTH_SHORT).show()
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
                Log.d(TAG, "현재 지갑 상태 : ${activityViewModel.memberInfo.value!!.wallet} 현재 그림 nftyYn ${drawingDetailFragmentViewModel.nftYn.value}")
                if(drawingDetailFragmentViewModel.nftYn.value == false) {
                    if (activityViewModel.memberInfo.value!!.wallet == null) {
                        // 지갑 발행해라
                        Log.d(TAG, "지갑이 없네요 발급할게요")
                        activityViewModel.getNftWallet()
                    } else {
                        Log.d(TAG, "이미 지갑이 있네요")
                        if (activityViewModel.memberInfo.value!!.silverCoin >= 5) {
                            drawingDetailFragmentViewModel.registNft(args.drawingId)
                        } else {
                            //여기서 siverCoin 부족 메시지
                            Toast.makeText(mainActivity, "silverCoin 확인 바람", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }else{
                    Toast.makeText(mainActivity, "이미 발급 받은 그림입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            buttonReport.setOnClickListener {
                drawingDetailFragmentViewModel.setDrawingReportDialog()
                drawingDetailFragmentViewModel.drawingReportDialog.show(childFragmentManager, "report")
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
                        drawingDetailFragmentViewModel.deleteDrawingComment(comment.commentId, position)
                    }
                }
            }
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun sendReport(content: String){
        drawingDetailFragmentViewModel.sendReport(content)
    }
}