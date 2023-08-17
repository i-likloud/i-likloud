package com.ssafy.likloud.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.ItemMypagePhotoBinding

class MypagePhotoAdapter (var list : MutableList<PhotoListDto>): ListAdapter<PhotoListDto, MypagePhotoAdapter.PhotoListHolder>(
    PhotoListComparator
) {
    companion object PhotoListComparator : DiffUtil.ItemCallback<PhotoListDto>() {
        override fun areItemsTheSame(oldItem: PhotoListDto, newItem: PhotoListDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PhotoListDto, newItem: PhotoListDto): Boolean {
            return oldItem.photoId  == newItem.photoId
        }
    }
    inner class PhotoListHolder(binding: ItemMypagePhotoBinding) : RecyclerView.ViewHolder(binding.root){
        val layoutMypagePhoto = binding.layoutItemMypagePhoto
        val imageDrawing = binding.imageDrawing
        fun bindInfo(photo : PhotoListDto){
            Glide.with(imageDrawing)
                .load(photo.photoUrl)
                .placeholder(R.drawable.image_loading)
                .into(imageDrawing)
            itemView.setOnClickListener{
                itemClickListner.onClick(it,photo)
            }

            layoutMypagePhoto.animation = AnimationUtils.loadAnimation(layoutMypagePhoto.context, R.anim.list_item_anim_fade_in)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListHolder {
        val binding = ItemMypagePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return PhotoListHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PhotoListHolder, position: Int) {
        holder.apply {
            bindInfo(list.get(position))
        }
    }

    fun updateData(list: ArrayList<PhotoListDto>) {
        this.list = list
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, photo: PhotoListDto)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}