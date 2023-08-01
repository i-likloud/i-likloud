package com.ssafy.likloud.ui.photolist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseFragment
import com.ssafy.likloud.databinding.FragmentPhotoDrawingDetailBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "차선호"
//PhotoListFragment에서 해당 그림 선택하면 그 그림 객체 얻어와야함!!!!
@AndroidEntryPoint
class PhotoDrawingDetailFragment : BaseFragment<FragmentPhotoDrawingDetailBinding>(
    FragmentPhotoDrawingDetailBinding::bind, R.layout.fragment_photo_drawing_detail) {

    private val photoDrawingDetailFragmentViewModel : PhotoDrawingDetailFragmentViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    val args: PhotoDrawingDetailFragmentArgs by navArgs()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        init()
        initListener()
    }

    private fun initObserver(){
        photoDrawingDetailFragmentViewModel.currentPhotoDrawingDetail.observe(viewLifecycleOwner){
            //여기서부터 뷰에 다 붙이면 됨
            Log.d(TAG, "currentPhotoDrawingDetail -> ${photoDrawingDetailFragmentViewModel.currentPhotoDrawingDetail.value}")
        }
    }

    private fun init(){
        //여기서 args.drawingId로 DrawingDetailDto 불러와야 함
        photoDrawingDetailFragmentViewModel.getCurrentPhotoDrawingDetail(args.drawingId)
    }

    override fun initListener() {

    }

}