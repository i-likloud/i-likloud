package com.ssafy.likloud.ui.profile

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
import com.ssafy.likloud.databinding.ItemExampleBinding
import com.ssafy.likloud.databinding.ItemProfileTempBinding

class ProfileListAdapter() :
    ListAdapter<ImageTempDto, ProfileListAdapter.ProfileViewHolder>(ProfileItemComparator) {
    private var pre: Int = 0

    companion object ProfileItemComparator : DiffUtil.ItemCallback<ImageTempDto>() {
        override fun areItemsTheSame(oldItem: ImageTempDto, newItem: ImageTempDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImageTempDto, newItem: ImageTempDto): Boolean {
            return oldItem.num == newItem.num
        }
    }

    class ProfileViewHolder(val binding: ItemProfileTempBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(data: ImageTempDto) {
            binding.apply {
                binding.image.setImageResource(data.resourceId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding: ItemProfileTempBinding =
            ItemProfileTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ProfileViewHolder(binding).apply {
            binding.root.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, @SuppressLint("RecyclerView") position: Int) {
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
        fun onClick(view: View, position: Int)
    }
}