package com.ssafy.likloud.ui.nftlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.response.MemberInfoResponse
import com.ssafy.likloud.databinding.ItemNftGiftBinding
import com.ssafy.likloud.databinding.ItemSearchUserBinding

class NftGiftSearchUserAdapter (val activityViewModel: MainActivityViewModel): ListAdapter<MemberInfoResponse, NftGiftSearchUserAdapter.NftGiftSearchUserHolder>(
    NftGiftSearchUserComparator
) {
    companion object NftGiftSearchUserComparator : DiffUtil.ItemCallback<MemberInfoResponse>() {
        override fun areItemsTheSame(oldItem: MemberInfoResponse, newItem: MemberInfoResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MemberInfoResponse, newItem: MemberInfoResponse): Boolean {
            return oldItem.memberId  == newItem.memberId
        }
    }
    inner class NftGiftSearchUserHolder(binding: ItemSearchUserBinding) : RecyclerView.ViewHolder(binding.root){

        val layoutSearchUser = binding.layoutSearchUser
        val imageProfileColor = binding.imageProfileColor
        val imageProfileFace = binding.imageProfileFace
        val imageProfileAccessory = binding.imageProfileAccessory
        val textNickname = binding.textNickname

        fun bindInfo(memberInfo : MemberInfoResponse){
            Glide.with(imageProfileColor)
                .load(activityViewModel.waterDropColorList[memberInfo.profileColor].resourceId)
                .into(imageProfileColor)

            Glide.with(imageProfileFace)
                .load(activityViewModel.waterDropFaceList[memberInfo.profileFace].resourceId)
                .into(imageProfileFace)

            Glide.with(imageProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[memberInfo.profileAccessory].resourceId)
                .into(imageProfileAccessory)
            textNickname.text = memberInfo.nickname

            layoutSearchUser.setOnClickListener {
                itemClickListner.onClick(memberInfo)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftGiftSearchUserAdapter.NftGiftSearchUserHolder {
        val binding = ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NftGiftSearchUserHolder(binding)
    }

    override fun onBindViewHolder(holder: NftGiftSearchUserAdapter.NftGiftSearchUserHolder, position: Int) {
        holder.apply {
            bindInfo(getItem(position))
        }
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(memberInfo: MemberInfoResponse)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}