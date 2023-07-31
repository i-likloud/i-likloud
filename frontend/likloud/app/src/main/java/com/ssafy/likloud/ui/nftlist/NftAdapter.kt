package com.ssafy.likloud.ui.nftlist

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.likloud.R
import com.ssafy.likloud.data.model.DrawingListDto
import com.ssafy.likloud.databinding.ItemNftBinding
import com.ssafy.likloud.databinding.ItemPhotoBinding
import com.ssafy.likloud.ui.photolist.PhotoListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NftAdapter (var context: Context, var list : ArrayList<DrawingListDto>): RecyclerView.Adapter<NftAdapter.NftHolder>() {

//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image : ImageView = itemView.findViewById(R.id.image)
//    }

    inner class NftHolder(binding: ItemNftBinding) : RecyclerView.ViewHolder(binding.root){

        val drawingLayout = binding.layoutDrawing
        val frontLayout = binding.layoutFront
        val backLayout = binding.layoutBack

        fun bindInfo(drawing : DrawingListDto){

            drawingLayout.setOnClickListener{
                if (backLayout.visibility == View.INVISIBLE) {
                    flip(context, backLayout, frontLayout)
                } else {
                    flip(context, frontLayout, backLayout)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftHolder {
        val binding = ItemNftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecyclerView.ViewHolder(inflater)
        return NftHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NftHolder, position: Int) {
        holder.apply {
            bindInfo(list.get(position))
        }
    }

    fun updateData(list: ArrayList<DrawingListDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    //Use the method for item changed
    fun itemChanged() {
        // remove last item for test purposes
        this.list[0] = (DrawingListDto())
        notifyItemChanged(0)

    }

    //Use the method for checking the itemRemoved
    fun removeData() {
        // remove last item for test purposes
        val orgListSize = list.size
        this.list = this.list.subList(0, orgListSize - 1).toList() as ArrayList<DrawingListDto>
        notifyItemRemoved(orgListSize - 1)
    }

    private fun flip(context: Context, visibleView: View, inVisibleView: View) {
        try {
            // Create ObjectAnimator animations for flip_in.xml
            val scale = context.resources.displayMetrics.density
            val cameraDist = 8000 * scale
            visibleView.cameraDistance = cameraDist
            inVisibleView.cameraDistance = cameraDist
            val flipInAlphaAnimator1 = ObjectAnimator.ofFloat(visibleView, "alpha", 1.0f, 0.0f)
            val flipInRotationYAnimator = ObjectAnimator.ofFloat(visibleView, "rotationY", 0f, 180f)
            val flipInAlphaAnimator2 = ObjectAnimator.ofFloat(visibleView, "alpha", 0.0f, 1.0f)
            flipInRotationYAnimator.duration = 1500
            flipInAlphaAnimator2.startDelay = 750
            flipInAlphaAnimator2.duration = 750
            // Create ObjectAnimator animations for flip_out.xml
//            val flipOutAlphaAnimator1 = ObjectAnimator.ofFloat(inVisibleView, "alpha", 0.0f, 1.0f)
            val flipOutRotationYAnimator =
                ObjectAnimator.ofFloat(inVisibleView, "rotationY", 0f, 180f)
            val flipOutAlphaAnimator2 = ObjectAnimator.ofFloat(inVisibleView, "alpha", 1.0f, 0.0f)
            flipOutRotationYAnimator.duration = 1500
            flipOutAlphaAnimator2.duration = 750
            val flipAnimatorSet = AnimatorSet()
            flipAnimatorSet.playTogether(
                flipInAlphaAnimator1,
                flipInRotationYAnimator,
                flipInAlphaAnimator2,
                flipOutRotationYAnimator,
                flipOutAlphaAnimator2
            )
            inVisibleView.scaleX = 1f
            visibleView.scaleX = -1f
            flipAnimatorSet.start()
            CoroutineScope(Dispatchers.Main).launch {
                delay(750)
                visibleView.visibility = View.VISIBLE
            }


            // After the animation finishes, hide the inVisibleView
            flipAnimatorSet.doOnEnd {
                inVisibleView.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
//            logHandledException(e)
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
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