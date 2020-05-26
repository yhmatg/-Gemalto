package com.esimtek.esim

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.esimtek.esim.util.RegexUtil
import kotlinx.android.synthetic.main.fragment_password_modify_dialog.*

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    PasswordModifyFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [PasswordModifyFragment.Listener].
 */
class PasswordModifyFragment : BottomSheetDialogFragment() {

    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_password_modify_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modify.setOnClickListener {
            mListener?.let {
                val password = passwordEditText.text.toString().trim()
                if (!RegexUtil.isPassword(password)) {
                    Toast.makeText(activity, "密码不符合规范", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                it.onModifyClicked(password)
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
        fun onModifyClicked(password: String)
    }
}