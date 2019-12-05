package com.esimtek.gemalto

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent


class LocationScanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_scan)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_F4 -> {
                startActivity(Intent(this, LocationListActivity::class.java))
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
