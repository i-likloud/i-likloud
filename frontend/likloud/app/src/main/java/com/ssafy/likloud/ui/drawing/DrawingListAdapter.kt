package com.ssafy.likloud.ui.drawing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.ItemDrawingBinding

private const val TAG = "차선호"
class DrawingListAdapter  (var activityViewModel: MainActivityViewModel): ListAdapter<DrawingListDto, DrawingListAdapter.DrawingListHolder>(
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
        val buttonNft = binding.buttonNft
        fun bindInfo(drawing : DrawingListDto){
            Glide.with(imageDrawing)
                .load(drawing.imageUrl)
                .into(imageDrawing)
            Log.d(TAG, "artist : ${drawing.drawingId} // user : ${activityViewModel.memberInfo.value!!.nickname} ")
            if(drawing.memberId == activityViewModel.memberInfo.value!!.memberId){
                buttonNft.visibility = View.VISIBLE
            }else{
                buttonNft.visibility = View.GONE
            }
            buttonNft.setOnClickListener{
                itemClickListner.onClick(drawing)
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
        fun onClick(drawing: DrawingListDto)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}