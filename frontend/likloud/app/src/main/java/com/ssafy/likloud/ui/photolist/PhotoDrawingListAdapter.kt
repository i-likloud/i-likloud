package com.ssafy.likloud.ui.photolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.PhotoListDto
import com.ssafy.likloud.databinding.ItemPhotoBinding

class PhotoDrawingListAdapter (var list : MutableList<DrawingListDto>): ListAdapter<DrawingListDto, PhotoDrawingListAdapter.PhotoDrawingListHolder>(
    PhotoDrawingListComparator
) {
    companion object PhotoDrawingListComparator : DiffUtil.ItemCallback<DrawingListDto>() {
        override fun areItemsTheSame(oldItem: DrawingListDto, newItem: DrawingListDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DrawingListDto, newItem: DrawingListDto): Boolean {
            return oldItem._id  == newItem._id
        }
    }
    inner class PhotoDrawingListHolder(binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDrawing = binding.imageDrawing
        fun bindInfo(drawing : DrawingListDto){
            Glide.with(imageDrawing)
                .load(drawing.imageUrl)
                .into(imageDrawing)
            itemView.setOnClickListener{
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDrawingListHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return PhotoDrawingListHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PhotoDrawingListHolder, position: Int) {
        holder.apply {
            bindInfo(list.get(position))
        }
    }


    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int, info:String)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}