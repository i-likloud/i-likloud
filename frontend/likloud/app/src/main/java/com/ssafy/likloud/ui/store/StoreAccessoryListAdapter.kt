package com.ssafy.likloud.ui.store

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieListener
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.ImageTempDto
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.response.AccessoryResponse
import com.ssafy.likloud.data.model.response.StoreItemResponse
import com.ssafy.likloud.databinding.ItemStoreBinding

class StoreAccessoryListAdapter() :
    ListAdapter<StoreItemResponse, StoreAccessoryListAdapter.StoreAccessoryViewHolder>(StoreItemComparator) {

    companion object StoreItemComparator : DiffUtil.ItemCallback<StoreItemResponse>() {
        override fun areItemsTheSame(oldItem: StoreItemResponse, newItem: StoreItemResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StoreItemResponse, newItem: StoreItemResponse): Boolean {
            return oldItem.storeId == newItem.storeId
        }
    }

    class StoreAccessoryViewHolder(val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(data: StoreItemResponse) {
            binding.apply {
                imageFiltering(data.accessoryName)
                if (data.owned) {
                    binding.buttonBuy.apply {
                        setBackgroundResource(R.drawable.frame_button_grey_mild)
                        text = "구매됨"
                        isClickable = false
                        isEnabled = false
                    }
                } else {
                    binding.buttonBuy.setBackgroundResource(R.drawable.frame_button_green_mild)
                }
                binding.textviewMoney.text = data.accessoryPrice.toString()
            }
        }

        private fun imageFiltering(name: String) {
            when(name) {
                "duck_mouse" -> {
                    binding.image.setImageResource(R.drawable.water_drop_item_duckmouth_croped)
                }
                "shine" -> {
                    binding.image.setImageResource(R.drawable.water_drop_item_shine_croped)
                }
                "mustache" -> {
                    binding.image.setImageResource(R.drawable.water_drop_item_mustache_croped)
                }
                "sunglass" -> {
                    binding.image.setImageResource(R.drawable.water_drop_item_sunglass_croped)
                }
                "umbrella" -> {
                    binding.image.setImageResource(R.drawable.water_drop_item_umbrella_croped)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAccessoryViewHolder {
        val binding: ItemStoreBinding =
            ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoreAccessoryViewHolder(binding).apply {
            binding.image.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, getItem(layoutPosition))
            }
            binding.buttonBuy.setOnClickListener {
//                binding.lottieBuyAnimationOnStoreitem.visibility = View.VISIBLE
//                binding.lottieBuyAnimationOnStoreitem.playAnimation()
                itemBuyClickLitener.onClick(getItem(layoutPosition), binding.lottieBuyAnimationOnStoreitem)
            }
        }
    }

    override fun onBindViewHolder(holder: StoreAccessoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Log.d(TAG, "onBindViewHolder: ${getItem(position)}")
        holder.bindInfo(getItem(position))

        holder.binding.itemStore.animation = AnimationUtils.loadAnimation(holder.binding.itemStore.context, R.anim.list_item_anim_fade_in)
        holder.binding.lottieBuyAnimationOnStoreitem.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                holder.binding.lottieBuyAnimationOnStoreitem.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    lateinit var itemClickListener: ItemClickListener
    interface ItemClickListener {
        fun onClick(view: View, position: Int, data: StoreItemResponse)
    }

    lateinit var itemBuyClickLitener: ItemBuyClickLitener
    interface ItemBuyClickLitener {
        fun onClick(data: StoreItemResponse, lottieView: LottieAnimationView)
    }
}