package com.ssafy.likloud.ui.drawingpad

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentDrawingPadBinding
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.bitmap
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.bitmapCanvas
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.clearedPoints
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.erasedPoints
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.isCleared
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.paint
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.points
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedColor
import com.ssafy.likloud.util.createMultipartFromUri
import com.ssafy.likloud.util.saveImageToGallery
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date


private const val TAG = "DrawingPadFragment_싸피"

@AndroidEntryPoint
class DrawingPadFragment : BaseFragment<FragmentDrawingPadBinding>(
    FragmentDrawingPadBinding::bind,
    R.layout.fragment_drawing_pad
) {
    private lateinit var mActivity: MainActivity
    private lateinit var colorMap: Map<ImageView, Int>
    private lateinit var undoRedoMap: Map<ImageView, Boolean>
    private var imageViewHeight = 0
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val drawingPadFragmentViewModel: DrawingPadFragmentViewModel by viewModels()
    private var bmp: Bitmap? = null
    val largerWidth =
        applicationContext!!.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._120sdp)
    val originalWidth =
        applicationContext!!.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp)
    val largerWeight = 1.2f
    val originalWeight = 1.0f
    private lateinit var layoutListener : OnGlobalLayoutListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun initListener() {

        colorMap.entries.forEach { entry ->
            entry.key.setOnClickListener {
                selectedColor = entry.value

                // 크기 조절을 위해 모든 View를 순회합니다.
                colorMap.entries.forEach { otherEntry ->
                    val view = otherEntry.key

                    // 클릭된 View와 다른 View를 구분하여 처리합니다.
                    if (view == it) {
                        // 클릭된 View에 큰 크기를 설정합니다.
                        view.layoutParams.width = largerWidth
                        val layoutParams = view.layoutParams as LinearLayout.LayoutParams
                        layoutParams.weight = largerWeight
                        view.layoutParams = layoutParams
                    } else {
                        // 클릭되지 않은 View에 원래 크기를 유지합니다.
                        view.layoutParams.width = originalWidth
                        val layoutParams = view.layoutParams as LinearLayout.LayoutParams
                        layoutParams.weight = originalWeight
                        view.layoutParams = layoutParams
                    }

                    view.requestLayout()
                }

                paint.xfermode = if (selectedColor == Color.TRANSPARENT) {
                    PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                } else null
            }

        }

        mActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                mActivity.changeProfileLayoutVisible()
                findNavController().popBackStack()
            }
        })

        binding.buttonClear.setOnClickListener {
            if (points.isEmpty()) return@setOnClickListener
            clearedPoints = ArrayDeque(points.toList())
            points.clear()
            isCleared = true
            bitmap?.eraseColor(Color.TRANSPARENT)
            binding.canvasDrawingpad.invalidate()
        }

        undoRedoMap.entries.forEach { entry ->
            entry.key.setOnClickListener {
                processUndoRedo(entry.value)
                redrawRemainingPoints()
                binding.canvasDrawingpad.invalidate()
            }
        }

        /**
         * imageview height 계산해서 canvas height에 적용
         */
        layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (binding.imageChosenPhoto != null && imageViewHeight != binding.imageChosenPhoto.height) {
                imageViewHeight = binding.imageChosenPhoto.height
                Log.d(TAG, "onViewCreated: ${imageViewHeight}")
                binding.canvasDrawingpad.layoutParams.height = imageViewHeight
                binding.canvasDrawingpad.requestLayout()
            }
        }
        binding.imageChosenPhoto.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        binding.buttonSaveDrawing.clicked {
            bmp = viewToBitmap(binding.canvasDrawingpad)
            drawingPadFragmentViewModel.setDrawingMultipart(
                createMultipartFromUri(
                    requireContext(),
                    Uri.parse(
                        saveImageToGallery(
                            requireContext(),
                            bmp!!,
                            SimpleDateFormat("yyMMdd_HHmmss").format(Date())
                        )
                    )
                )!!
            )
            Log.d(TAG, "initListener: ${drawingPadFragmentViewModel.drawingMultipartBody}")
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity.changeProfileLayoutInvisible()

        colorMap = mapOf(
            binding.imageBlackPencil to Color.BLACK,
            binding.imageBluePencil to Color.parseColor("#009DFA"),
            binding.imageGreenPencil to Color.parseColor("#33BF00"),
            binding.imageYellowPencil to Color.parseColor("#FFE92B"),
            binding.imageNavyPencil to Color.parseColor("#004CBD"),
            binding.imageYellowPencil to Color.parseColor("#FFBC16"),
            binding.imagePurplePencil to Color.parseColor("#A023FF"),
            binding.imageOrangePencil to Color.parseColor("#FF8A12"),
            binding.imageWhitePencil to Color.WHITE,
            binding.imageRedPencil to Color.parseColor("#F54800"),
            binding.imageEraser to Color.TRANSPARENT
        )

        undoRedoMap = mapOf(
            binding.buttonUndo to true,
            binding.buttonRedo to false
        )

        initView()
        loadImage()
        initListener()

    }

    private fun initView() {
        binding.buttonSaveDrawing.setText(getString(R.string.save_drawing))

        colorMap.entries.forEach { otherEntry ->
            val view = otherEntry.key
            val layoutParams = view.layoutParams as LinearLayout.LayoutParams

            if(view == binding.imageBlackPencil) {
                layoutParams.width = largerWidth
                layoutParams.weight = largerWeight
            }
            else{
                layoutParams.width = originalWidth
                layoutParams.weight = originalWeight
            }
            view.layoutParams = layoutParams


            view.requestLayout()

        }

        paint.xfermode = if (selectedColor == Color.TRANSPARENT) {
            PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else null

    }


    private fun processUndoRedo(isUndo: Boolean) {
        if (isUndo) {
            if (isCleared && points.isEmpty()) {
                points.addAll(clearedPoints)
                clearedPoints.clear()
                isCleared = false
            } else if (points.isNotEmpty()) {
                while (points.last().isContinue) erasedPoints.add(points.removeLast())
                erasedPoints.add(points.removeLast())
            }
        } else {
            if (erasedPoints.isNotEmpty()) {
                points.add(erasedPoints.removeLast())
                while (erasedPoints.isNotEmpty() && erasedPoints.last().isContinue) points.add(erasedPoints.removeLast())
            }
        }
        bitmap?.eraseColor(Color.TRANSPARENT)
    }


    private fun redrawRemainingPoints() {
        points.forEachIndexed { index, point ->
            if (index >= 1 && point.isContinue) {
                updatePaintForPoint(points[index])
                bitmapCanvas?.drawLine(
                    points[index - 1].x,
                    points[index - 1].y,
                    point.x,
                    point.y,
                    paint
                )
            }
        }
    }

    fun updatePaintForPoint(point: Point) {
        paint.color = point.color
        paint.strokeWidth = point.strokeWidth
        paint.xfermode = if (paint.color == Color.TRANSPARENT) {
            PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else null
    }

    fun loadImage() {
        Glide.with(this)
            .load(mainActivityViewModel.uploadingPhotoUrl.value)
            .into(binding.imageChosenPhoto)

        Glide.with(this)
            .load(mainActivityViewModel.uploadingPhotoUrl.value)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    // Set the loaded image as the background of the view
                    binding.canvasDrawingpad.background = resource
                }
            })
    }


    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onPause() {
        binding.imageChosenPhoto.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
        super.onPause()
    }

}