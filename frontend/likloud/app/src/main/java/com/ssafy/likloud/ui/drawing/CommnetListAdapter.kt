package com.ssafy.likloud.ui.drawing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.MainActivityViewModel
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.data.model.MemberProfileDto
import com.ssafy.likloud.databinding.ItemCommentBinding

private const val TAG = "차선호"
class CommentListAdapter  (var activityViewModel: MainActivityViewModel): ListAdapter<CommentDto, CommentListAdapter.CommentListHolder>(
    CommentListComparator
) {

    companion object CommentListComparator : DiffUtil.ItemCallback<CommentDto>() {
        override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            Log.d(TAG, "areItemsTheSame...")
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            Log.d(TAG, "areContentsTheSame...")
            return oldItem.commentId  == newItem.commentId
        }
    }

    inner class CommentListHolder(binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){
        val layoutCommentItem = binding.layoutCommentItem
        val imageProfileColor = binding.imageProfileColor
        val imageProfileFace = binding.imageProfileFace
        val imageProfileAccessory = binding.imageProfileAccessory
        val textNickname = binding.textNickname
        val textContent = binding.textContent
        val textTime = binding.textTime
        val imageDeleteComment = binding.imageDeleteComment
        fun bindInfo(comment : CommentDto){
//            Glide.with(imageProfileColor)
//                .load(activityViewModel.waterDropColorList[comment.profileColor].resourceId)
//                .into(imageProfileColor)
//            Glide.with(imageProfileFace)
//                .load(activityViewModel.waterDropFaceList[comment.profileFace].resourceId)
//                .into(imageProfileFace)
//            Glide.with(imageProfileAccessory)
//                .load(activityViewModel.waterDropAccessoryList[comment.profileAccessory].resourceId)
//                .into(imageProfileAccessory)
            imageProfileColor.setImageResource(activityViewModel.waterDropColorList[comment.profileColor].resourceId)
            imageProfileFace.setImageResource(activityViewModel.waterDropFaceList[comment.profileFace].resourceId)
            Log.d(TAG, "bindInfo: 댓글 정보중 악세서리 ${comment.profileAccessory}")
            imageProfileAccessory.setImageResource(activityViewModel.waterDropAccessoryList[comment.profileAccessory].resourceId)
            textNickname.text = comment.nickname
            textContent.text = comment.content
            textTime.text = comment.createdAt
            //여기 자기가 쓴 댓글인지 비교해서 쓰레기통 보여줘라
            if(comment.memberId == activityViewModel.memberInfo.value!!.memberId){
                imageDeleteComment.visibility = View.VISIBLE
            }else{
                imageDeleteComment.visibility = View.GONE
            }

            layoutCommentItem.animation = AnimationUtils.loadAnimation(layoutCommentItem.context, R.anim.list_item_anim_fade_in)

            imageDeleteComment.setOnClickListener{
                itemClickListner.onClick(comment, layoutPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListHolder {
        Log.d(TAG, "onCreateViewHolder...")
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return CommentListHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentListHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder....")
        holder.apply {
            bindInfo(getItem(position))
        }
    }

    //    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(comment: CommentDto, position: Int)
    }
    //클릭리스너 선언
    lateinit var itemClickListner: ItemClickListener
}