package com.ssafy.likloud.ui.drawingform

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingFormBinding
import com.ssafy.likloud.util.initEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

private const val TAG = "DrawingFormFragment_싸피"
@AndroidEntryPoint
class DrawingFormFragment : BaseFragment<FragmentDrawingFormBinding>(
    FragmentDrawingFormBinding::bind,
    R.layout.fragment_drawing_form
) {
    private val drawingFormFragmentViewModel: DrawingFormFragmentViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        initEditText(
            binding.edittextDescription,
            binding.layoutDescription,
            binding.layoutSaveDrawing,
            mActivity
        ) { message ->
            drawingFormFragmentViewModel.setDescMessage(message)
        }

        initEditText(
            binding.edittextTitle,
            binding.layoutTitle,
            binding.layoutSaveDrawing,
            mActivity
        ) { message ->
            drawingFormFragmentViewModel.settitleMessage(message)
        }

        binding.buttonSaveDrawing.setOnClickListener {

            drawingFormFragmentViewModel.uploadDrawing(
                mainActivityViewModel.drawingMultipartBody.value!!,
                mainActivityViewModel.uploadingPhotoId.value!!,
                binding.edittextTitle.text.toString(),
                binding.edittextDescription.text.toString()
            )
            showLoadingDialog(mActivity)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()

        viewLifecycleOwner.lifecycleScope.launch {
            drawingFormFragmentViewModel.titleMessageText.collectLatest {
                binding.textTitleCount.text =
                    "${it.length}${context?.getString(R.string.drawing_title_max)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingFormFragmentViewModel.descMessageText.collectLatest {
                binding.textDescriptionCount.text =
                    "${it.length}${context?.getString(R.string.drawing_desc_max)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingFormFragmentViewModel.isDrawingUploaded.collectLatest {
                dismissLoadingDialog()
                if (it == true) {
                    showSnackbar(binding.root, "info", "성공적으로 그림이 업로드 되었어요!")
                    findNavController().navigate(R.id.action_drawingFormFragment_to_drawingListFragment)
                }
                else{
                    showSnackbar(binding.root, "fail", "제목과 설명을 입력해주세요!")
                }
            }
        }
    }

    private fun initView() {
        Glide.with(this)
            .load(mainActivityViewModel.drawingBitmap.value) // Bitmap 객체를 로드합니다.
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시를 사용하지 않도록 설정 (선택사항)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
            .into(binding.imageSelectedDrawing) // ImageView에 설정합니다.
    }

}