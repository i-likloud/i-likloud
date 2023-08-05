package com.ssafy.likloud.ui.drawing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.ItemDrawingBinding

class DrawingListAdapter  (): ListAdapter<DrawingListDto, DrawingListAdapter.DrawingListHolder>(
    DrawingListComparator
) {

    companion object DrawingListComparator : DiffUtil.ItemCallback<DrawingListDto>() {
        override fun areItemsTheSame(oldItem: DrawingListDto, newItem: DrawingListDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DrawingListDto, newItem: DrawingListDto): Boolean {
            return oldItem.drawingId  == newItem.drawingId
        }
    }

    inner class DrawingListHolder(binding: ItemDrawingBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDrawing = binding.imageDrawing
        fun bindInfo(drawing : DrawingListDto){
            Glide.with(imageDrawing)
                .load(drawing.imageUrl)
                .into(imageDrawing)
            itemView.setOnClickListener{
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingListHolder {
        val binding = ItemDrawingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return DrawingListHolder(binding)
    }

    override fun onBindViewHolder(holder: DrawingListHolder, position: Int) {
        holder.apply {
            bindInfo(getItem(position))
        }
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int, info:String)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}