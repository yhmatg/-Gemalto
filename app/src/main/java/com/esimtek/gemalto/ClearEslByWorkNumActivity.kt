package com.esimtek.gemalto

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.Menu
import android.widget.Toast
import com.esimtek.gemalto.adapter.ESLListAdapter
import com.esimtek.gemalto.model.ResultBean
import com.esimtek.gemalto.model.workNumberBean
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
import kotlinx.android.synthetic.main.activity_clear_els.*
import kotlinx.android.synthetic.main.activity_clear_els.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ClearEslByWorkNumActivity : BaseActivity() {

    private val scanner: ODScannerHelper = ODScannerHelper.getDefaultHelper()
    private val rfid: RFIDReaderHelper = RFIDReaderHelper.getDefaultHelper()
    private var isFirstResume = false

    private var obRFID: RXObserver = object : RXObserver() {
        override fun onInventoryTag(tag: RXInventoryTag) {
            disposable.add(Observable.just(tag.strEPC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        // ESL条码长度为6，写入EPC后长度为8，每两位中间有空格，共11位
                        if (it.length == 11) {
                            BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
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
                        if (it.length == 8) {
                          getWrokNumByPlCodeOrESL(it.substring(2))
                        } else Toast.makeText(this, "ESL条码格式错误", Toast.LENGTH_SHORT).show()
                    })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clear_els)
        initScanner()
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
        clear.setOnClickListener {
            if (work_number.text.isNullOrEmpty()) {
                Toast.makeText(this@ClearEslByWorkNumActivity, "未获取到工单信息，请重新扫描后再试", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            clearESL()
        }

        clear_work_num.setOnClickListener{
            work_number.text = ""
            esl_num.text = ""
        }
        isFirstResume = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_switch, menu)
        //menu?.findItem(R.id.action_rfid)?.isVisible = false
        menu?.findItem(R.id.action_scan)?.isVisible = false
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

    private fun clearESL() {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).clearESL(work_number.text.toString()).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@ClearEslByWorkNumActivity, getString(R.string.clear_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ClearEslByWorkNumActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@ClearEslByWorkNumActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWrokNumByPlCodeOrESL(plCodeOrESL: String) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).getWrokNumByPlCodeOrESL(plCodeOrESL).enqueue(object : Callback<workNumberBean> {
            override fun onResponse(call: Call<workNumberBean>, response: Response<workNumberBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@ClearEslByWorkNumActivity, getString(R.string.get_work_num_success), Toast.LENGTH_SHORT).show()
                    work_number.setText(response.body()?.data?.workNumber)
                    esl_num.setText(response.body()?.data?.eslNum.toString())
                } else {
                    Toast.makeText(this@ClearEslByWorkNumActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<workNumberBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@ClearEslByWorkNumActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(!isFirstResume){
            scanner.registerObserver(obScanner)
        }else{
            isFirstResume = false
        }
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
}
