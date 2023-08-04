package com.ssafy.likloud.ui.drawingform

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingFormBinding
import com.ssafy.likloud.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrawingFormFragment : BaseFragment<FragmentDrawingFormBinding>(FragmentDrawingFormBinding::bind, R.layout.fragment_drawing_form) {

    private val drawingFormFragmentViewModel : DrawingFormFragmentViewModel by viewModels()
    private val mainActivityViewModel : MainActivityViewModel by activityViewModels()

    override fun initListener() {
        initEditText(binding.edittextDescription, binding.layoutDescription, drawingFormFragmentViewModel) { message ->
            drawingFormFragmentViewModel.setDescMessage(message)
        }

        initEditText(binding.edittextTitle, binding.layoutTitle, drawingFormFragmentViewModel) { message ->
            drawingFormFragmentViewModel.settitleMessage(message)
        }

        binding.buttonSaveDrawing.setOnClickListener {
            drawingFormFragmentViewModel.uploadDrawing(
                mainActivityViewModel.drawingMultipartBody.value!!,
                mainActivityViewModel.uploadingPhotoId.value!!,
                binding.edittextTitle.text.toString(),
                binding.edittextDescription.text.toString()
            )
        }
    }
    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
        private fun initEditText(
            editText: EditText,
            layout: View,
            viewModel: DrawingFormFragmentViewModel,
            setMessage: (String) -> Unit // 함수 타입 파라미터를 추가하여 메시지를 설정하는 함수를 받습니다.
        ) {
            editText.onFocusChangeListener = View.OnFocusChangeListener { view, gainFocus ->
                if (gainFocus)
                    layout.background = requireContext().getDrawable(R.drawable.frame_rounded_border_radius20_stroke3)
                else
                    layout.background = requireContext().getDrawable(R.drawable.frame_rounded_border_radius20)
            }

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setMessage(s.toString()) // setMessage 파라미터를 사용하여 적절한 메시지 설정 함수 호출
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.layoutSaveDrawing.setOnTouchListener { _, _ ->
                requireActivity().hideKeyboard()
                editText.clearFocus()
                false
            }
        }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()

        viewLifecycleOwner.lifecycleScope.launch {
            drawingFormFragmentViewModel.titleMessageText.collectLatest {
                binding.textTitleCount.text =  "${it.length}${context?.getString(R.string.drawing_title_max)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            drawingFormFragmentViewModel.descMessageText.collectLatest {
                binding.textDescriptionCount.text =  "${it.length}${context?.getString(R.string.drawing_desc_max)}"
            }
        }
    }

    private fun initView(){
        Glide.with(this)
            .load(mainActivityViewModel.drawingBitmap.value) // Bitmap 객체를 로드합니다.
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시를 사용하지 않도록 설정 (선택사항)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(50) ))
            .into(binding.imageSelectedDrawing) // ImageView에 설정합니다.
    }

}