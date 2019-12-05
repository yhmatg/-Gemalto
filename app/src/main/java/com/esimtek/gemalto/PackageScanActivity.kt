package com.esimtek.gemalto

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_package_scan.*


class PackageScanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_scan)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_location -> {
                    startActivity(Intent(this, LocationListActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_scene, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                exitDialog()
                true
            }
            KeyEvent.KEYCODE_F4 -> {
                startActivity(Intent(this, PackageListActivity::class.java))
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
