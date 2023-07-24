package com.ssafy.likloud.example

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.data.model.UserDto
import com.ssafy.likloud.databinding.ItemExampleBinding

class ExampleListAdapter :
    ListAdapter<SampleDto, ExampleListAdapter.CustomViewHolder>(ModelExampleComparator) {

    companion object ModelExampleComparator : DiffUtil.ItemCallback<SampleDto>() {
        override fun areItemsTheSame(oldItem: SampleDto, newItem: SampleDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SampleDto, newItem: SampleDto): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class CustomViewHolder(val binding: ItemExampleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(data: SampleDto) {
            binding.apply {
                title.text = data.id.toString()
                no.text = data.name
                regDate.text = data.profilePath

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding: ItemExampleBinding =
            ItemExampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomViewHolder(binding).apply {
            binding.root.setOnClickListener {
                Toast.makeText(
                    parent.context,
                    "onCreateViewHolder: adapterPosition:${adapterPosition}, layoutPosition: ${layoutPosition}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${getItem(position)}")
        holder.bindInfo(getItem(position))
    }
}