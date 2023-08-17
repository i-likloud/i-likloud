package com.ssafy.likloud.ui.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.ItemPhotoBinding

class PhotoListAdapter (): ListAdapter<PhotoListDto, PhotoListAdapter.PhotoListHolder>(
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
    inner class PhotoListHolder(binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDrawing = binding.imageDrawing
        fun bindInfo(photo : PhotoListDto){
            Glide.with(imageDrawing)
                .load(photo.photoUrl)
                .into(imageDrawing)
            itemView.setOnClickListener{
                itemClickListner.onClick(it,photo.photoUrl)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoListHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoListHolder, position: Int) {
        holder.apply {
            bindInfo(getItem(position))
        }
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, imageUrl : String)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}