package com.ssafy.likloud.ui.drawinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.DrawingDto
import com.ssafy.likloud.databinding.ItemDrawingBinding

class DrawingListAdapter  (var list : ArrayList<DrawingDto>): RecyclerView.Adapter<DrawingListAdapter.DrawingHolder>() {

//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image : ImageView = itemView.findViewById(R.id.image)
//    }

    inner class DrawingHolder(binding: ItemDrawingBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDrawing = binding.imageDrawing
        fun bindInfo(drawing : DrawingDto){
            Glide.with(imageDrawing)
                .load(drawing.img)
                .into(imageDrawing)
            itemView.setOnClickListener{
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingHolder {
        val binding = ItemDrawingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return DrawingHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DrawingHolder, position: Int) {
        holder.apply {
            bindInfo(list.get(position))
        }
    }

    fun updateData(list: ArrayList<DrawingDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    //Use the method for item changed
    fun itemChanged() {
        // remove last item for test purposes
        this.list[0] = (DrawingDto(R.drawable.cloud1, "Thi is cool"))
        notifyItemChanged(0)

    }

    //Use the method for checking the itemRemoved
    fun removeData() {
        // remove last item for test purposes
        val orgListSize = list.size
        this.list = this.list.subList(0, orgListSize - 1).toList() as ArrayList<DrawingDto>
        notifyItemRemoved(orgListSize - 1)
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