package com.ssafy.likloud.ui.photo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.FragmentPhotoDetailBinding
import com.ssafy.likloud.ui.drawing.DrawingDetailFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoDetailFragment : BaseFragment<FragmentPhotoDetailBinding>(FragmentPhotoDetailBinding::bind, R.layout.fragment_photo_detail) {

    private val photoDetailFragmentViewModel: PhotoDetailFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var photoDrawingListAdapter: PhotoDrawingListAdapter
    val args: PhotoDetailFragmentArgs by navArgs()


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
        photoDetailFragmentViewModel.currentPhotoDetail.observe(viewLifecycleOwner){
            //여기서 현재 그림에 대한 사진 리스트, 사진 올린 멤버 조회 & 초기 bookmark 정보 세팅
            photoDetailFragmentViewModel.getCurrentPhotoDrawingList(it.photoId)

            photoDetailFragmentViewModel.getCurrentPhotoMember(it.memberId)

            photoDetailFragmentViewModel.setIsBookmarked()
            photoDetailFragmentViewModel.setBookmarkCount()

            // drawingPad에서 보여지기 위해 photoId, photoUrl 업데이트
            activityViewModel.setUploadingPhotoId(it.photoId)
            activityViewModel.setUploadingPhotoUrl(it.photoUrl)
        }
        photoDetailFragmentViewModel.currentPhotoMember.observe(viewLifecycleOwner){
            //사진 정보, 유저 정보 뷰 세팅
            initInfoView(photoDetailFragmentViewModel.currentPhotoDetail.value!!, it)
        }
        photoDetailFragmentViewModel.currentPhotoDrawingList.observe(viewLifecycleOwner){
            //현재 사진에 대한 그림들 리사이클러뷰 세팅
            photoDrawingListAdapter.submitList(it)
            if(it.size==0){
                binding.apply {
                    imageNoDrawing.visibility = View.VISIBLE
                    textNoDrawing.visibility = View.VISIBLE
                }
            }else{
                binding.apply {
                    imageNoDrawing.visibility = View.INVISIBLE
                    textNoDrawing.visibility = View.INVISIBLE
                }
            }
        }
        photoDetailFragmentViewModel.isBookmarked.observe(viewLifecycleOwner){
            if(it){
                binding.imageStar.setImageResource(R.drawable.icon_selected_star)
            }else{
                binding.imageStar.setImageResource(R.drawable.icon_unselected_star)
            }
        }
        photoDetailFragmentViewModel.bookmarkCount.observe(viewLifecycleOwner){
            binding.textBookmarkCount.text = it.toString()
        }
    }

    private fun init(){
        photoDetailFragmentViewModel.getCurrentPhotoDetail(args.photoId)

        initPhotoDrawingListRecyclerView()
        loadingAnimation()
    }

    override fun initListener() {
        binding.apply {
            //뒤로가기 눌렀을 때
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            //즐겨찾기 눌렀을 때
            imageStar.setOnClickListener {
                photoDetailFragmentViewModel.changeBookmarkCount()
                photoDetailFragmentViewModel.changeIsBookmarked()
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

        binding.buttonPaint.setOnClickListener {
            findNavController().navigate(R.id.action_photoDetailFragment_to_drawingPadFragment)
        }

        binding.imageCurrentPhoto.setOnClickListener {
            photoDetailFragmentViewModel.currentPhotoDetail.value?.let {
                Glide.with(binding.imagePhotoOrigin)
                    .load(it.photoUrl)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                    .into(binding.imagePhotoOrigin)
                binding.constraintPhotoOriginal.visibility = View.VISIBLE
                binding.imagePhotoOrigin.visibility = View.VISIBLE
                binding.imagePhotoOrigin.scaleX = 0.0f
                binding.imagePhotoOrigin.scaleY = 0.0f
                val animationX = ObjectAnimator.ofFloat(binding.imagePhotoOrigin, "scaleX", 0.0f, 1.0f)
                val animationY = ObjectAnimator.ofFloat(binding.imagePhotoOrigin, "scaleY", 0.0f, 1.0f)
                animationX.duration = 400
                animationY.duration = 400
                val animation = AnimatorSet()
                animation.playTogether(animationX, animationY)
                animation.start()
                binding.layoutAppbar.setBackgroundResource(R.color.background_half_transparent)
            }
        }
        binding.constraintPhotoOriginal.setOnClickListener {
            val animationX = ObjectAnimator.ofFloat(binding.imagePhotoOrigin, "scaleX", 1.0f, 0.0f)
            val animationY = ObjectAnimator.ofFloat(binding.imagePhotoOrigin, "scaleY", 1.0f, 0.0f)
            animationX.duration = 400
            animationY.duration = 400
            val animation = AnimatorSet()
            animation.playTogether(animationX, animationY)
            animation.start()
            animation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    it.visibility = View.GONE
                    binding.layoutAppbar.setBackgroundResource(R.color.transparent)
                }
                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
            })
        }
    }

    private fun initInfoView(photoDetail: PhotoListDto, member: MemberProfileDto){
        binding.apply {
            Glide.with(imageCurrentPhoto)
                .load(photoDetail.photoUrl)
                .into(imageCurrentPhoto)
            Glide.with(imageDrawingProfileColor)
                .load(activityViewModel.waterDropColorList[member.profileColor].resourceId)
                .into(imageDrawingProfileColor)
            Glide.with(imageDrawingProfileFace)
                .load(activityViewModel.waterDropFaceList[member.profileFace].resourceId)
                .into(imageDrawingProfileFace)
            Glide.with(imageDrawingProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[member.profileAccessory].resourceId)
                .into(imageDrawingProfileAccessory)
            textDrawingNickname.text = member.nickname
            textBookmarkCount.text = photoDetail.bookmarkCount.toString()
            textDrawCount.text = photoDetail.pickCount.toString()
        }
    }
    private fun initPhotoDrawingListRecyclerView(){
        photoDrawingListAdapter = PhotoDrawingListAdapter()
        binding.apply {
            recyclerviewPhotoDrawingList.apply {
                layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = photoDrawingListAdapter.apply {
                    this.itemClickListener = object: PhotoDrawingListAdapter.ItemClickListener{
                        override fun onClick(view: View, drawing: DrawingListDto) {
                            //여기서 drawing객체를 가진 상태로 PhotoDrawingDetailFragment로 전달
                            val action = PhotoDetailFragmentDirections.actionPhotoDetailFragmentToDrawingDetailFragment(drawing.drawingId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun loadingAnimation() {
        binding.layoutInfo.translationX = 1300f
        binding.layoutPhotoDrawingList.translationX = 1300f
        binding.frameImageCurrent.translationX = -1300f
        binding.textviewDrawings.translationX = 1300f
        initAnimation()
    }

    private fun initAnimation() {
        lifecycleScope.launch {
            makeAnimationX(binding.layoutInfo, 0f, 450)
            delay(100)
            makeAnimationX(binding.layoutPhotoDrawingList, 0f, 500)
            makeAnimationX(binding.textviewDrawings, 0f, 500)
            delay(50)
            makeAnimationX(binding.frameImageCurrent, 0f, 600)
        }
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
}