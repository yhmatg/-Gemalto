package com.esimtek.gemalto

import android.os.Bundle
import android.widget.Toast
import com.esimtek.gemalto.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_set.*

class SetActivity : BaseActivity() {

    private var url by PreferenceUtil("url", "192.168.31.117:59790")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)
        urlEditText.setText(url)
        modify.setOnClickListener {
            val urlInput = urlEditText.text.toString().trim()
            if (urlInput.isEmpty()) {
                Toast.makeText(this, "请输入正确的url", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            url = urlInput
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
