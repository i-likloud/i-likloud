package com.ssafy.likloud.ui.drawinglist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.CommentDto
import com.ssafy.likloud.databinding.ItemCommentBinding

class CommentListAdapter (var list : ArrayList<CommentDto>): RecyclerView.Adapter<CommentListAdapter.CommentHolder>() {

//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image : ImageView = itemView.findViewById(R.id.image)
//    }

    inner class CommentHolder(binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){
        var profileImg = binding.commentProfileImg
        var nickname = binding.commentNicknameTv
        var content = binding.commentContentTv
        var time = binding.commentTimeTv

        fun bindInfo(comment: CommentDto){

            Glide.with(profileImg)
                .load(comment.img)
                .into(profileImg)
            nickname.text = comment.nickname
            content.text = comment.content
            time.text = comment.time

            itemView.setOnClickListener{

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
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