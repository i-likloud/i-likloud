package com.ssafy.likloud.ui.nftlist

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.databinding.ItemNftBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NftGiftAdapter (var context: Context): ListAdapter<NftGiftDto, NftGiftAdapter.NftGiftHolder>(
    NftGiftComparator
) {
    companion object NftGiftComparator : DiffUtil.ItemCallback<NftGiftDto>() {
        override fun areItemsTheSame(oldItem: NftGiftDto, newItem: NftGiftDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NftGiftDto, newItem: NftGiftDto): Boolean {
            return oldItem.nftId  == newItem.nftId
        }
    }
    inner class NftGiftHolder(binding: ItemNftBinding) : RecyclerView.ViewHolder(binding.root){

        val layoutNft = binding.layoutNft
        val layoutFront = binding.layoutFront
        val layoutBack = binding.layoutBack
        val imageNft = binding.imageNft
        val textNickname = binding.textNftNickname
        val textTitle = binding.textNftTitle
        val textContent = binding.textNftContent

        fun bindInfo(giftDto : NftGiftDto){

//            Glide.with(imageNft)
//                .load(nftDto.imageUrl)
//                .into(imageNft)
//            textNickname.text = "이름 : ${nftDto.owner}"
//            textTitle.text = "제목 : ${nftDto.title}"
//            textContent.text = " 내용 : ${nftDto.content}"
            layoutNft.setOnClickListener{
                if (layoutBack.visibility == View.INVISIBLE) {
                    flip(context, layoutBack, layoutFront)
                } else {
                    flip(context, layoutFront, layoutBack)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftGiftAdapter.NftGiftHolder {
        val binding = ItemNftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NftGiftHolder(binding)
    }

    override fun onBindViewHolder(holder: NftGiftAdapter.NftGiftHolder, position: Int) {
        holder.apply {
            bindInfo(getItem(position))
        }
    }

    private fun flip(context: Context, visibleView: View, inVisibleView: View) {
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
                flipInAlphaAnimator1,
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
            }
        } catch (e: Exception) {
//            logHandledException(e)
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int, info:String)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}