package com.ssafy.likloud.ui.mypage

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.ItemMypageDrawingBinding
import com.ssafy.likloud.databinding.ItemMypagePhotoBinding

private const val TAG = "차선호"
class MypageDrawingAdapter  (var list : MutableList<DrawingListDto>): ListAdapter<DrawingListDto, MypageDrawingAdapter.DrawingListHolder>(
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

    inner class DrawingListHolder(binding: ItemMypageDrawingBinding) : RecyclerView.ViewHolder(binding.root){
        val imageDrawing = binding.imageDrawing
        val layoutMypageDrawing = binding.layoutItemMypageDrawing
        val imageNftMedal = binding.imageNftMedal
        fun bindInfo(drawing : DrawingListDto){
            Glide.with(imageDrawing)
                .load(drawing.imageUrl)
                .placeholder(R.drawable.button_camera)
                .into(imageDrawing)

//            imageDrawing.layoutParams = ViewGroup.LayoutParams(imageDrawing.width, imageDrawing.height)

            Log.d(TAG, "nft유무 : ${drawing.nftYn}")
            if(drawing.nftYn){
                imageNftMedal.visibility = View.VISIBLE
            }else{
                imageNftMedal.visibility = View.INVISIBLE
            }

            layoutMypageDrawing.animation = AnimationUtils.loadAnimation(layoutMypageDrawing.context, R.anim.list_item_anim_fade_in)

            itemView.setOnClickListener{
                itemClickListner.onClick(it,drawing)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingListHolder {
        val binding = ItemMypageDrawingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return DrawingListHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DrawingListHolder, position: Int) {
        holder.apply {
            bindInfo(list.get(position))
        }
    }

    fun updateData(list: ArrayList<DrawingListDto>) {
        this.list = list
    }
    //Use the method for checking the itemRemoved
    fun removeData() {
        // remove last item for test purposes
        val orgListSize = list.size
        this.list = this.list.subList(0, orgListSize - 1).toList() as ArrayList<DrawingListDto>
        notifyItemRemoved(orgListSize - 1)
    }


    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, drawing: DrawingListDto)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}