package com.esimtek.esim

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_location_scan.*


class LocationScanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_scan)
        update_location_by_work_num.setOnClickListener {
            startActivity(Intent(this, UpdateLocationByWorkNumActivity::class.java))
        }
        scan.setOnClickListener{
            startActivity(Intent(this, LocationListActivity::class.java))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_F4 -> {
                //startActivity(Intent(this, LocationListActivity::class.java))
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
