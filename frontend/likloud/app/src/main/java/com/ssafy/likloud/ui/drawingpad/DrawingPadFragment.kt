package com.ssafy.likloud.ui.drawingpad

import android.content.Context
import android.content.res.ColorStateList
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
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.slider.Slider
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
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedEraserStrokeWidth
import com.ssafy.likloud.ui.drawingpad.BitmapCanvasObject.selectedStrokeWidth
import com.ssafy.likloud.util.createMultipartFromUri
import com.ssafy.likloud.util.createMultipartFromUriNameFile
import com.ssafy.likloud.util.makeButtonAnimationX
import com.ssafy.likloud.util.makeButtonAnimationXWithDuration
import com.ssafy.likloud.util.saveImageToGallery
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

const val DRAWING_STYLE_PAD_LEFT = -1000f
const val CANVAS_RIGHT = 1000f
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
    private var isPenStylePadOpened = false
    private val largerWidth by lazy { applicationContext.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._120sdp) }
    private val originalWidth by lazy { applicationContext.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp) }
    private val largerWeight = 1.2f
    private val originalWeight = 1.0f
    private lateinit var layoutListener: OnGlobalLayoutListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun initListener() {

        penClickListener()
        penStylePadClickListener()
        undoRedoClickListener()

        binding.layoutPenEraserWidth.setOnClickListener {

        }

        binding.layoutDrawingPad.setOnClickListener {
            movePenWithLayoutToLeft()
        }

        mActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
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



        layoutListener = OnGlobalLayoutListener {
            if (imageViewHeight != binding.imageChosenPhoto.height) {
                binding.cardviewCanvas.visibility = View.VISIBLE
                makeButtonAnimationXWithDuration(binding.cardviewCanvas, 0f , 500)
                imageViewHeight = binding.imageChosenPhoto.height
                Log.d(TAG, "onViewCreated: ${imageViewHeight}")
                binding.canvasDrawingpad.layoutParams.height = imageViewHeight
                binding.canvasDrawingpad.requestLayout()

            }
        }
        binding.imageChosenPhoto.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        binding.buttonSaveDrawing.setOnClickListener {
            bmp = viewToBitmap(binding.canvasDrawingpad)
            mainActivityViewModel.setDrawingBitmap(bmp!!)
            mainActivityViewModel.setDrawingMultipart(
                createMultipartFromUriNameFile(
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
            findNavController().navigate(R.id.action_drawingPadFragment_to_drawingFormFragment)
        }

        binding.seekbarPen.apply {
            value = selectedStrokeWidth
            addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
                selectedStrokeWidth = value
                val layoutParams = binding.dotPensize.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = value.toInt()
                binding.dotPensize.layoutParams = layoutParams
                binding.dotPensize.requestLayout()
            })
        }

        binding.seekbarEraser.apply {
            value = selectedEraserStrokeWidth
            addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
                selectedEraserStrokeWidth = value
            })
        }
    }

    /**
     * 앞으로가기, 뒤로가기 클릭 리스너 입니다.
     */
    private fun undoRedoClickListener() {
        undoRedoMap.entries.forEach { entry ->
            entry.key.setOnClickListener {
                processUndoRedo(entry.value)
                redrawRemainingPoints()
                binding.canvasDrawingpad.invalidate()
            }
        }
    }

    private fun penStylePadClickListener() {
        binding.buttonPenWidth.setOnClickListener {
            if (isPenStylePadOpened) movePenWithLayoutToLeft()
            else movePenWithLayoutToRight()
        }
    }

    /**
     * 펜을 클릭된 것은 키우고, 아닌 것은 원래 사이즈로 되돌립니다.
     */
    private fun penClickListener() {
        colorMap.entries.forEach { entry ->
            entry.key.setOnClickListener {
                selectedColor = entry.value

                if (selectedColor != Color.TRANSPARENT) {
                    setDotAndButtonView()
                }

                colorMap.entries.forEach { otherEntry ->
                    val view = otherEntry.key

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
    }

    /**
     * 펜 스타일 패드를 숨깁니다.
     */
    private fun movePenWithLayoutToLeft() {
        if(!isPenStylePadOpened) return
        isPenStylePadOpened = false
        makeButtonAnimationX(binding.layoutPenEraserWidth, DRAWING_STYLE_PAD_LEFT)
    }

    /**
     * 펜스타일 패드를 보여줍니다.
     */
    private fun movePenWithLayoutToRight() {
        if(isPenStylePadOpened) return
        isPenStylePadOpened = true
        makeButtonAnimationX(binding.layoutPenEraserWidth, 0f)
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

        // 초기 펜 스타일 현재 글자 크기로 지정
        setDotAndButtonView()
        val newWidth = selectedStrokeWidth.toInt()
        val layoutParams = binding.dotPensize.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = newWidth
        binding.dotPensize.layoutParams = layoutParams
        binding.dotPensize.requestLayout()

        // 검정색 펜만 초기에 크기 키움
        colorMap.entries.forEach { otherEntry ->
            val view = otherEntry.key
            val layoutParams = view.layoutParams as LinearLayout.LayoutParams

            if (otherEntry.value == selectedColor) {
                layoutParams.width = largerWidth
                layoutParams.weight = largerWeight
            } else {
                layoutParams.width = originalWidth
                layoutParams.weight = originalWeight
            }
            view.layoutParams = layoutParams

            view.requestLayout()

        }

        // 지우개인 경우 투명색 지정 및 지워지는 기능 추가
        paint.xfermode = if (selectedColor == Color.TRANSPARENT) {
            PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else null

        // 펜 스타일 버튼 초기에 숨기기
        makeButtonAnimationXWithDuration(binding.layoutPenEraserWidth, DRAWING_STYLE_PAD_LEFT, 0)
        makeButtonAnimationXWithDuration(binding.cardviewCanvas, CANVAS_RIGHT, 0)
    }

    /**
     * 선택한 색으로 펜스타일 지정 패드의 뷰를 설정합니다.
     */
    private fun setDotAndButtonView() {
//        binding.buttonSaveDrawing.setText(getString(R.string.move_to_save))
        binding.seekbarPen.trackActiveTintList = ColorStateList.valueOf(selectedColor)
        binding.dotPensize.setCardBackgroundColor(selectedColor)
        binding.seekbarPen.trackActiveTintList = ColorStateList.valueOf(selectedColor)
        binding.seekbarPen.thumbTintList = ColorStateList.valueOf(selectedColor)
        binding.seekbarPen.trackActiveTintList = ColorStateList.valueOf(selectedColor)
    }

    /**
     * 뒤로가기, 앞으로 가기에 따라 지워진 큐, 그려진 큐에 add 합니다.
     */
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
                while (erasedPoints.isNotEmpty() && erasedPoints.last().isContinue) points.add(
                    erasedPoints.removeLast()
                )
            }
        }
        bitmap?.eraseColor(Color.TRANSPARENT)
    }


    /**
     * point 객체들을 paint 해줍니다.
     */
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

    /**
     * 각 point마다 색을 정해주고, 투명색, 즉 지우개인 경우 지우기 모드를 적용합니다.
     */
    private fun updatePaintForPoint(point: Point) {
        paint.color = point.color
        paint.strokeWidth = point.strokeWidth
        paint.xfermode = if (paint.color == Color.TRANSPARENT) {
            PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else null
    }

    /**
     * mainviewmodel에서 저장한 그림 사진을 imageview와 캔버스에 로드합니다.
     * 이미지뷰는 크기 측정만을 위함으로 실제로 그려지지는 않습니다.
     */
    private fun loadImage() {
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


    /**
     * canvas 뷰를 bitmap 객체로 변환합니다.
     */
    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    override fun onPause() {
        mActivity.changeProfileLayoutVisible()
        binding.imageChosenPhoto.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
        super.onPause()
    }

}