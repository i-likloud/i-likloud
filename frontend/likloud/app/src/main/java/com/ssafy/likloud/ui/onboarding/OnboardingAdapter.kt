package com.ssafy.likloud.ui.onboarding

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.likloud.data.model.SampleDto
import com.ssafy.likloud.databinding.ItemExampleBinding
import com.ssafy.likloud.databinding.ViewpagerOnboardingBinding

data class OnboardData(
    val id: Int,
    val description: String,
    val onboardImgPath : Int
)

class OnboardingAdapter :
    ListAdapter<OnboardData, OnboardingAdapter.CustomViewHolder>(ModelExampleComparator) {

    companion object ModelExampleComparator : DiffUtil.ItemCallback<OnboardData>() {
        override fun areItemsTheSame(oldItem: OnboardData, newItem: OnboardData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OnboardData, newItem: OnboardData): Boolean {
            return oldItem.id  == newItem.id
        }
    }

    class CustomViewHolder(val binding: ViewpagerOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(data: OnboardData) {
            binding.apply {
                descriptionText.text = data.description
                onboardImg.setImageResource(data.onboardImgPath)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding: ViewpagerOnboardingBinding =
            ViewpagerOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomViewHolder(binding).apply {
//            binding.root.setOnClickListener {
//
//            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }
}