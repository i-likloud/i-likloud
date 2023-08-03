package com.ssafy.likloud.ui.drawing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.databinding.ItemCommentBinding

class CommentListAdapter  (var list : MutableList<CommentDto>, var member: MemberProfileDto, var activityViewModel: MainActivityViewModel): ListAdapter<CommentDto, CommentListAdapter.CommentListHolder>(
    CommentListComparator
) {

    companion object CommentListComparator : DiffUtil.ItemCallback<CommentDto>() {
        override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            return oldItem.commentId  == newItem.commentId
        }
    }

    inner class CommentListHolder(binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){
        val imageProfileColor = binding.imageProfileColor
        val imageProfileFace = binding.imageProfileFace
        val imageProfileAccessory = binding.imageProfileAccessory
        val textNickname = binding.textNickname
        val textContent = binding.textContent
        val textTime = binding.textTime
        fun bindInfo(comment : CommentDto){
            Glide.with(imageProfileColor)
                .load(activityViewModel.waterDropColorList[member.profileColor].resourceId)
                .into(imageProfileColor)
            Glide.with(imageProfileFace)
                .load(activityViewModel.waterDropFaceList[member.profileFace].resourceId)
                .into(imageProfileFace)
            Glide.with(imageProfileAccessory)
                .load(activityViewModel.waterDropAccessoryList[member.profileAccessory].resourceId)
                .into(imageProfileAccessory)
            textNickname.text = member.nickname
            textContent.text = comment.content
            textTime.text = comment.createdAt
            itemView.setOnClickListener{
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return CommentListHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentListHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    fun updateData(list: ArrayList<CommentDto>) {
        this.list = list
    }
    //Use the method for checking the itemRemoved
    fun removeData() {
        // remove last item for test purposes
        val orgListSize = list.size
        this.list = this.list.subList(0, orgListSize - 1).toList() as ArrayList<CommentDto>
    }


    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int, info:String)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}