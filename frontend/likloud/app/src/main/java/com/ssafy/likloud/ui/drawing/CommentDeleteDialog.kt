package com.ssafy.likloud.ui.drawing

import android.content.Context
import android.os.Bundle
import android.view.View
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalCommentDeleteBinding
import com.ssafy.likloud.databinding.ModalDrawingDeleteBinding

class CommentDeleteDialog (
    private val delete: (commentId: Int) -> Unit,
    private val commentId: Int
) : BaseDialog<ModalCommentDeleteBinding>(ModalCommentDeleteBinding::bind, R.layout.modal_comment_delete) {

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun initListener() {
        binding.apply {
            buttonDelete.setOnClickListener {
                delete.invoke(commentId)
                dismiss()
            }
            buttonCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        init()
    }

    private fun init(){

    }


}