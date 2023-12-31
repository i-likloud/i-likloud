package com.ssafy.likloud.ui.drawing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.ssafy.likloud.MainActivity
import com.ssafy.likloud.R
import com.ssafy.likloud.base.BaseDialog
import com.ssafy.likloud.databinding.ModalDrawingReportBinding
import com.ssafy.likloud.util.initEditText

private const val TAG = "차선호"
class DrawingReportDialog(
    var drawingId: Int
) : BaseDialog<ModalDrawingReportBinding>(ModalDrawingReportBinding::bind, R.layout.modal_drawing_report) {

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    // hideKeyboard 함수는 아래와 같이 구현되어 있다고 가정합니다.
    fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun initListener() {

        /**
         * edittext의 포커스를 없애줍니다. fragment내에서는 keyboard도 같이 내려주지만 modal에서는 hideKeyboard함수도 사용해줘야 합니다.
         */
        initEditText(
            binding.edittextReport,
            null,
            binding.layoutReportModal,
            requireContext() as MainActivity,
            null
        )


        binding.edittextReport.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }

        binding.apply {
            buttonReport.setOnClickListener {
                if(binding.edittextReport.text.toString() == "") {
                    Log.d(TAG, "신고 내용을 입력해주세요")
                }else{
                    val parentFragment = parentFragment
                    if (parentFragment is DrawingDetailFragment) {
                        parentFragment.sendReport(binding.edittextReport.text.toString())
                    }else if(parentFragment is DrawingListFragment){
                        parentFragment.sendReport(binding.edittextReport.text.toString())
                    }
                    dismiss()
                }
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

        val spinner = binding.spinnerReport
        val editTextReport = binding.edittextReport

        val items = arrayOf(getString(R.string.report_item1), getString(R.string.report_item2), getString(R.string.report_item3), getString(R.string.report_item4))
        val adapter = ArrayAdapter(mainActivity, R.layout.item_custom_spinner, items)
        adapter.setDropDownViewResource(R.layout.item_custom_spinner)
        spinner.adapter = adapter

        // 스피너 아이템 선택 이벤트 처리
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                var selectedItem = parentView.getItemAtPosition(position) as String
                // 선택된 아이템에 대한 처리를 여기에 추가
                if(selectedItem == getString(R.string.report_item1)){
                    editTextReport.isEnabled = true
                    editTextReport.text.clear()
                }else{
                    editTextReport.isEnabled = false
                    editTextReport.setText(selectedItem)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}