package com.esimtek.gemalto

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.Menu
import android.widget.Toast
import com.esimtek.gemalto.adapter.ESLListAdapter
import com.esimtek.gemalto.model.ResultBean
import com.esimtek.gemalto.network.Api
import com.esimtek.gemalto.network.HttpClient
import com.esimtek.gemalto.util.BeeperUtil
import com.nativec.tools.ModuleManager
import com.rfid.RFIDReaderHelper
import com.rfid.rxobserver.RXObserver
import com.rfid.rxobserver.bean.RXInventoryTag
import com.scanner1d.ODScannerHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_package_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PackageListActivity : BaseActivity() {

    private val adapter = ESLListAdapter()

    private val scanner: ODScannerHelper = ODScannerHelper.getDefaultHelper()
    private val rfid: RFIDReaderHelper = RFIDReaderHelper.getDefaultHelper()

    private var obRFID: RXObserver = object : RXObserver() {
        override fun onInventoryTag(tag: RXInventoryTag) {
            disposable.add(Observable.just(tag.strEPC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        // ESL条码长度为6，写入EPC后长度为8，每两位中间有空格，共11位
                        if (it.length == 11) {
                            BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
                            adapter.addItem(it.substring(0..8).replace(" ", ""))
                        }
                    })
        }
    }
    private var obScanner: Observer = Observer { _, value ->
        if (value is String)
            disposable.add(Observable.just(value)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
                        // ESL条码长度为6
                        if (it.length == 6) adapter.addItem(it)
                        else Toast.makeText(this, "ESL条码格式错误", Toast.LENGTH_SHORT).show()
                    })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_list)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_rfid -> {
                    initRFID()
                    it.isVisible = false
                    toolbar.menu.findItem(R.id.action_scan).isVisible = true
                }
                R.id.action_scan -> {
                    initScanner()
                    it.isVisible = false
                    toolbar.menu.findItem(R.id.action_rfid).isVisible = true
                }
            }
            return@setOnMenuItemClickListener false
        }
        initRFID()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        clear.setOnClickListener {
            if (adapter.list.isEmpty()) {
                Toast.makeText(this@PackageListActivity, "未扫描到标签，请重新扫描后再试", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (adapter.list.size > 21) {
                Toast.makeText(this@PackageListActivity, "标签数量超过21，请检查后长按删除", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            clearESL()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_switch, menu)
        menu?.findItem(R.id.action_rfid)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    private fun initRFID() {
        rfid.registerObserver(obRFID)
        ModuleManager.newInstance().uhfStatus = true
        ModuleManager.newInstance().scanStatus = false
        scanner.setRunFlag(true)
    }

    private fun initScanner() {
        scanner.registerObserver(obScanner)
        ModuleManager.newInstance().uhfStatus = false
        ModuleManager.newInstance().scanStatus = true
        scanner.setRunFlag(false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_F4 -> rfid.realTimeInventory(0xFF.toByte(), 0x01.toByte())
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun clearESL() {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).clearESL(adapter.list).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@PackageListActivity, getString(R.string.clear_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@PackageListActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@PackageListActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
