package com.ssafy.likloud.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.util.LoadingDialog

// Fragment의 기본을 작성, 뷰 바인딩 활용
abstract class BaseFragment<B : ViewBinding>(
    private val bind: (View) -> B,
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId) {
    private var _binding: B? = null
    lateinit var mLoadingDialog: LoadingDialog

    protected val binding get() = _binding!!
    private lateinit var _mActivity: MainActivity
    protected val mActivity get() = _mActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bind(super.onCreateView(inflater, container, savedInstanceState)!!)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _mActivity = context as MainActivity
    }

    abstract fun initListener()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun showCustomToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
        mLoadingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

    /**
     * 스낵바를 띄웁니다. 커스텀 하려면 type 분기를 추가하고 사용하세요.
     */
    fun showSnackbar(view: View, type: String, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
        when (type) {
            "success" -> {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.green_mild
                    )
                )
                snackbar.setActionTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.sky_blue_deep
                    )
                )
                snackbar.setAction("확인하러 가기 ->") {
                    findNavController().navigate(R.id.action_storeFragment_to_profileEditFragment)
                }
            }

            "fail" -> {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.red_mile
                    )
                )
            }

            "failAndMoveToAppSettings" -> {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.red_mile
                    )
                )
                snackbar.setActionTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.sky_blue_deep
                    )
                )
                snackbar.setAction("권한 설정하기 ->") {
                    val intent: Intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + requireContext().packageName))
                    requireContext().startActivity(intent)
                }
            }

            "info" -> {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.green_mild
                    )
                )
            }

            "movetonft" -> {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.green_mild
                    )
                )
                snackbar.setActionTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.sky_blue_deep
                    )
                )
                snackbar.setAction("확인하러 가기 ->") {
                    findNavController().navigate(R.id.action_drawingDetailFragment_to_nftListFragment)
                }
            }

            "blue_bar" -> {
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.blue_mild
                    )
                )
            }
        }
        snackbar.show()
    }
}