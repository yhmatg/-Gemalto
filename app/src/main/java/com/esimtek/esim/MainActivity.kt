package com.esimtek.esim

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.esimtek.esim.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var scene by PreferenceUtil("scene", "main")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.action_set -> {
                    startActivity(Intent(this, SetActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this, UserManageActivity::class.java))
                    finish()
                }
            }

            return@setOnMenuItemClickListener false
        }
        hotStamping.setOnClickListener {
            scene = "hotStamping"
            startActivity(Intent(this@MainActivity, HotStampingScanActivity::class.java))
            finish()
        }
        implantation.setOnClickListener {
            scene = "implantation"
            startActivity(Intent(this@MainActivity, ImplantationScanActivity::class.java))
            finish()
        }
        packageLine.setOnClickListener {
            scene = "packageLine"
            startActivity(Intent(this@MainActivity, PackageScanActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_set, menu)
        if(EsimApplication.instance.logged?.data?.userType == "admin"){
            menu?.getItem(1)?.setVisible(true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        exitDialog()
    }
}
