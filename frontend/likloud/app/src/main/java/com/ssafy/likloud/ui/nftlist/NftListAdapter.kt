package com.ssafy.likloud.ui.nftlist

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.navercorp.nid.NaverIdLoginSDK
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.databinding.ItemNftBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "차선호"
class NftListAdapter (var context: Context): ListAdapter<NftListDto, NftListAdapter.NftHolder>(
    NftListComparator
) {
    private val largerHeight by lazy { NaverIdLoginSDK.applicationContext.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp) }
    companion object NftListComparator : DiffUtil.ItemCallback<NftListDto>() {
        override fun areItemsTheSame(oldItem: NftListDto, newItem: NftListDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NftListDto, newItem: NftListDto): Boolean {
            return oldItem.nftId  == newItem.nftId
        }
    }
    inner class NftHolder(binding: ItemNftBinding) : RecyclerView.ViewHolder(binding.root){

        val layoutNft = binding.layoutNft
        val layoutFront = binding.layoutFront
        val layoutBack = binding.layoutBack
        val imageNft = binding.imageNft
        val textNickname = binding.textNftNickname
        val textTitle = binding.textNftTitle
        val textContent = binding.textNftContent
        val buttonGift = binding.buttonGift
        val layoutBackFrame = binding.layoutBackFrame

        fun bindInfo(nftDto : NftListDto){
            Glide.with(imageNft)
                .load(nftDto.imageUrl)
                .placeholder(R.drawable.button_game)
                .addListener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
//                            val width = resource.intrinsicWidth
                            val height = resource.intrinsicHeight
                            // 이미지의 크기를 이용하여 레이아웃을 조정
//                            imageView.layoutParams.width = width
                            if (textContent.length() < 46)
                                layoutBackFrame.layoutParams.height = height + largerHeight
                            else
                                layoutBackFrame.layoutParams.height = height + largerHeight
                            layoutBackFrame.requestLayout()
                        }
                        return false
                    }
                })
                .into(imageNft)

            textNickname.text = "이름 : ${nftDto.owner}"
            textTitle.text = "제목 : ${nftDto.title}"
            textContent.text = " 내용 : ${nftDto.content}"
            layoutNft.setOnClickListener{
                layoutNft.isClickable = false
                if (layoutBack.visibility == View.INVISIBLE) {
                    layoutBack.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                    layoutFront.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    flip(context, layoutBack, layoutFront, layoutNft)
                } else {
                    layoutBack.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                    layoutFront.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    flip(context, layoutFront, layoutBack, layoutNft)
                }
                Log.d(TAG, "here")
            }

            layoutNft.animation = AnimationUtils.loadAnimation(layoutNft.context, R.anim.list_item_anim_fade_in)

            Log.d(TAG, "bindInfo: 동적 높이 : ${imageNft.height}")
            layoutBackFrame.layoutParams.height = imageNft.height
//            layoutNft.layoutParams.height = imageNft.height
            buttonGift.setOnClickListener {
                itemClickListner.onClick(nftDto)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftHolder {
        val binding = ItemNftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return NftHolder(binding)
    }

    override fun onBindViewHolder(holder: NftHolder, position: Int) {
        holder.apply {
            bindInfo(getItem(position))
        }
    }

    private fun flip(context: Context, visibleView: View, inVisibleView: View, layoutNft: View) {
        try {
            // Create ObjectAnimator animations for flip_in.xml
            val scale = context.resources.displayMetrics.density
            val cameraDist = 8000 * scale
            visibleView.cameraDistance = cameraDist
            inVisibleView.cameraDistance = cameraDist
            val flipInAlphaAnimator1 = ObjectAnimator.ofFloat(visibleView, "alpha", 1.0f, 0.0f)
            val flipInRotationYAnimator = ObjectAnimator.ofFloat(visibleView, "rotationY", 0f, 180f)
            val flipInAlphaAnimator2 = ObjectAnimator.ofFloat(visibleView, "alpha", 0.0f, 1.0f)
            flipInRotationYAnimator.duration = 1500
            flipInAlphaAnimator2.startDelay = 750
            flipInAlphaAnimator2.duration = 750
            // Create ObjectAnimator animations for flip_out.xml
//            val flipOutAlphaAnimator1 = ObjectAnimator.ofFloat(inVisibleView, "alpha", 0.0f, 1.0f)
            val flipOutRotationYAnimator =
                ObjectAnimator.ofFloat(inVisibleView, "rotationY", 0f, 180f)
            val flipOutAlphaAnimator2 = ObjectAnimator.ofFloat(inVisibleView, "alpha", 1.0f, 0.0f)
            flipOutRotationYAnimator.duration = 1500
            flipOutAlphaAnimator2.duration = 750
            val flipAnimatorSet = AnimatorSet()
            flipAnimatorSet.playTogether(
//                flipInAlphaAnimator1,
                flipInRotationYAnimator,
                flipInAlphaAnimator2,
                flipOutRotationYAnimator,
                flipOutAlphaAnimator2
            )
            inVisibleView.scaleX = 1f
            visibleView.scaleX = -1f
            flipAnimatorSet.start()
            CoroutineScope(Dispatchers.Main).launch {
                delay(750)
                visibleView.visibility = View.VISIBLE
            }


            // After the animation finishes, hide the inVisibleView
            flipAnimatorSet.doOnEnd {
                inVisibleView.visibility = View.INVISIBLE
                layoutNft.isClickable = true
            }
        } catch (e: Exception) {
//            logHandledException(e)
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(nftDto: NftListDto)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}