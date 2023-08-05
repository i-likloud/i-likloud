package com.ssafy.likloud.ui.photo

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
import com.ssafy.likloud.databinding.ItemPhotoBinding

class PhotoDrawingListAdapter (): ListAdapter<DrawingListDto, PhotoDrawingListAdapter.PhotoDrawingListHolder>(
    PhotoDrawingListComparator
) {
    private var pre: Int = 0
    companion object PhotoDrawingListComparator : DiffUtil.ItemCallback<DrawingListDto>() {
        override fun areItemsTheSame(oldItem: DrawingListDto, newItem: DrawingListDto): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: DrawingListDto, newItem: DrawingListDto): Boolean {
            return oldItem.drawingId  == newItem.drawingId
        }
    }
    inner class PhotoDrawingListHolder(binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDrawing = binding.imageDrawing
        val layoutPhotoDrawingItem = binding.layoutPhotoDrawingItem
        fun bindInfo(drawing : DrawingListDto){
            Glide.with(imageDrawing)
                .load(drawing.imageUrl)
                .into(imageDrawing)

            if (layoutPosition >= pre) {
                layoutPhotoDrawingItem.animation = AnimationUtils.loadAnimation(layoutPhotoDrawingItem.context, R.anim.list_item_anim_from_right)
            } else {
                layoutPhotoDrawingItem.animation = AnimationUtils.loadAnimation(layoutPhotoDrawingItem.context, R.anim.list_item_anim_from_left)
            }
            pre = layoutPosition

            itemView.setOnClickListener{
                itemClickListener.onClick(it, drawing)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDrawingListHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return PhotoDrawingListHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoDrawingListHolder, position: Int) {
        holder.apply {
            bindInfo(getItem(position))
        }

    }


    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, drawing: DrawingListDto)
    }
    //클릭리스너 선언
    lateinit var itemClickListener: ItemClickListener
}