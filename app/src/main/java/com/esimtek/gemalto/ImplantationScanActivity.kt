package com.esimtek.gemalto

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.esimtek.gemalto.util.BeeperUtil
import com.nativec.tools.ModuleManager
import com.scanner1d.ODScannerHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_implantation_scan.*
import java.util.*

class ImplantationScanActivity : BaseActivity() {

    private val scanner: ODScannerHelper = ODScannerHelper.getDefaultHelper()

    private var obScanner: Observer = Observer { _, value ->
        if (value is String)
            disposable.add(Observable.just(value)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
                        if (it.length == 6) {
                            val intent = Intent(this@ImplantationScanActivity, PostBadCardActivity::class.java)
                            intent.putExtra("code", it)
                            startActivity(intent)
                        } else Toast.makeText(this, "ESL条码格式错误", Toast.LENGTH_SHORT).show()
                    })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implantation_scan)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_transfer -> {
                    //startActivity(Intent(this, TransferActivity::class.java))
                    startActivity(Intent(this, NewTransferActivity::class.java))
                }
                R.id.action_location -> {
                    //startActivity(Intent(this, LocationListActivity::class.java))
                    startActivity(Intent(this, LocationScanActivity::class.java))
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
        menuInflater.inflate(R.menu.toolbar_menu_implantation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        ModuleManager.newInstance().uhfStatus = false
        ModuleManager.newInstance().scanStatus = true
        scanner.setRunFlag(false)
        scanner.registerObserver(obScanner)
    }

    override fun onPause() {
        super.onPause()
        scanner.unRegisterObserver(obScanner)
    }

    override fun onDestroy() {
        super.onDestroy()
        ModuleManager.newInstance().uhfStatus = false
        ModuleManager.newInstance().scanStatus = false
        scanner.setRunFlag(false)
    }

    override fun onBackPressed() {
        exitDialog()
    }
}
