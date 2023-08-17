package com.ssafy.likloud.ui.profileedit

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.ImageTempDto
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.data.model.response.AccessoryResponse
import com.ssafy.likloud.databinding.ItemExampleBinding
import com.ssafy.likloud.databinding.ItemProfileTempBinding

class AccessoryListAdapter() :
    ListAdapter<AccessoryResponse, AccessoryListAdapter.AccessoryViewHolder>(AccessoryItemComparator) {
    private var pre: Int = 0

    companion object AccessoryItemComparator : DiffUtil.ItemCallback<AccessoryResponse>() {
        override fun areItemsTheSame(oldItem: AccessoryResponse, newItem: AccessoryResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AccessoryResponse, newItem: AccessoryResponse): Boolean {
            return oldItem.accessoryId == newItem.accessoryId
        }
    }

    class AccessoryViewHolder(val binding: ItemProfileTempBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(data: AccessoryResponse) {
            binding.apply {
                imageFiltering(data.accessoryName)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessoryViewHolder {
        val binding: ItemProfileTempBinding =
            ItemProfileTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AccessoryViewHolder(binding).apply {
            binding.root.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, getItem(layoutPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: AccessoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Log.d(TAG, "onBindViewHolder: ${getItem(position)}")
        holder.bindInfo(getItem(position))

        if (position >= pre) {
            holder.binding.profileItem.animation = AnimationUtils.loadAnimation(holder.binding.profileItem.context, R.anim.list_item_anim_from_right)
        } else {
            holder.binding.profileItem.animation = AnimationUtils.loadAnimation(holder.binding.profileItem.context, R.anim.list_item_anim_from_left)
        }
        pre = position
    }

    lateinit var itemClickListener: ItemClickListener
    interface ItemClickListener {
        fun onClick(view: View, position: Int, data: AccessoryResponse)
    }
}