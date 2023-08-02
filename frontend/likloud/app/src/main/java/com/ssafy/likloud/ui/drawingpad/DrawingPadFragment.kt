package com.ssafy.likloud.ui.drawingpad

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingPadBinding
import com.ssafy.likloud.databinding.FragmentExampleBinding
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.bitmap
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.clearedPoints
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.isCleared
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.points
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedStrokeWidth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrawingPadFragment : BaseFragment<FragmentDrawingPadBinding>(
    FragmentDrawingPadBinding::bind,
    R.layout.fragment_drawing_pad
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        colorMap.entries.forEach { entry ->
//            entry.key.setOnClickListener {
//                selectedColor = entry.value
//                paint.xfermode = if (selectedColor == Color.TRANSPARENT) {
//                    PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//                } else null
//            }
//        }
//
//        binding.buttonClear.setOnClickListener {
//            clearedPoints =  ArrayDeque(points.toList())
//            points.clear()
//            isCleared = true
//            bitmap?.eraseColor(Color.TRANSPARENT)
//            binding.canvasView.invalidate()
//        }
//
//        binding.seekBtn.apply {
//            progress = selectedStrokeWidth.toInt() * 2
//            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                    selectedStrokeWidth = progress.toFloat() * 0.5f
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//            })
//        }
//
//        setUndoRedoFunctionality()



        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun initListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorMap = mapOf(
            binding.imageBlackPencil  to Color.BLACK,
            binding.imageBluePencil  to Color.BLUE,
            binding.imageGreenPencil to Color.GREEN,
            binding.imageYellowPencil to Color.YELLOW,
            binding.imageNavyPencil to Color.parseColor("#00008B"),
            binding.imageYellowPencil to Color.YELLOW,
            binding.imagePurplePencil to Color.parseColor("#800080"),
            binding.imageOrangePencil to Color.parseColor("#FFA500"),
            binding.imageWhitePencil to Color.WHITE,
            binding.imageEraser to Color.TRANSPARENT
        )
        val imageViewHeight = binding.imageChosenPhoto.layoutParams.height
        binding.canvasDrawingpad.setCanvasHeight(imageViewHeight)

    }



}