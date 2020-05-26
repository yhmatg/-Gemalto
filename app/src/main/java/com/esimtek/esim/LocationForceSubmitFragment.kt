package com.esimtek.esim

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_location_force_submit_dialog.*


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    LocationForceSubmitFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [LocationForceSubmitFragment.Listener].
 */
class LocationForceSubmitFragment : BottomSheetDialogFragment() {

    private var mListener: Listener? = null
    private var locationItem = 0
    private lateinit var locationValue: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_force_submit_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationValue = resources.getStringArray(R.array.locationValue)
        locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //不做操作
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                locationItem = p2
            }

        }
        submit.setOnClickListener {
            mListener?.let {
                val workOrder = workOrderEditText.text.toString().trim()
                val epcCount = epcCountEditText.text.toString().trim()
                if (workOrder.isEmpty()) {
                    Toast.makeText(activity, "请输入工单号", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (epcCount.isEmpty()) {
                    Toast.makeText(activity, "请输入物料盒数量", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (epcCount.toInt() < 1) {
                    Toast.makeText(activity, "物料盒数量不能为0", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (epcCount.toInt() > 20) {
                    Toast.makeText(activity, "物料盒数量不能大于20", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                it.onForceSubmitClicked(locationValue[locationItem], workOrder, epcCount.toInt())
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (parentFragment != null) parentFragment as Listener else context as Listener
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onForceSubmitClicked(location: String, workOrder: String, epcCount: Int)
    }
}

