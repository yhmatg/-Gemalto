package com.esimtek.gemalto

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.Menu
import android.widget.Toast
import com.esimtek.gemalto.adapter.ESLListAdapter
import com.esimtek.gemalto.model.*
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
import kotlinx.android.synthetic.main.activity_new_transfer.*
import kotlinx.android.synthetic.main.activity_new_transfer.next_submit
import kotlinx.android.synthetic.main.activity_new_transfer.recyclerView
import kotlinx.android.synthetic.main.activity_new_transfer.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class NewTransferActivity : BaseActivity() {

    private val postTransferList: ArrayList<String> = ArrayList()

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
                        // ESL条码长度为6
                        BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
                        if (it.length == 6) {
                            if(workNumIsEmpty()){
                                getWrokNumByPlCodeOrESL(it)
                            }else{
                                adapter.addItem(it)
                                esl_num_scan.setText(adapter.list.size.toString())
                            }
                        } else {
                            Toast.makeText(this, "ESL条码格式错误", Toast.LENGTH_SHORT).show()
                        }
                    })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transfer)
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
        initScanner()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        next_submit.setOnClickListener {
            if(workNumIsEmpty() || eslkNumIsEmpty()){
                Toast.makeText(this@NewTransferActivity, "请先扫描标签获取工单信息", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (adapter.list.isEmpty()) {
                Toast.makeText(this@NewTransferActivity, "未扫描到标签，请重新扫描后再试", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (adapter.list.size != esl_num.text.toString().toInt()) {
                Toast.makeText(this@NewTransferActivity, "请扫描工单号要求的标签数再重试", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            postTransferList.clear()
            postTransferList.addAll(adapter.list)
            transferESL(NewTransferBean(postTransferList,work_num.text.toString()))
        }

        clear_work_num.setOnClickListener{
            work_num.text = ""
            esl_num.text = ""
            esl_num_scan.text = ""
            adapter.clear()
            postTransferList.clear()
        }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_F4 -> rfid.realTimeInventory(0xFF.toByte(), 0x01.toByte())
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun transferESL(bean: NewTransferBean) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).transferESL(bean).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                response.errorBody()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@NewTransferActivity, "转移成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@NewTransferActivity, "转移失败," + response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@NewTransferActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun workNumIsEmpty() :Boolean{

        return work_num.text.toString().isEmpty()
    }

    private fun eslkNumIsEmpty() :Boolean{

        return esl_num.text.toString().isEmpty()
    }
    private fun getWrokNumByPlCodeOrESL(plCodeOrESL: String) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).getWrokNumByPlCodeOrESL(plCodeOrESL).enqueue(object : Callback<workNumberBean> {
            override fun onResponse(call: Call<workNumberBean>, response: Response<workNumberBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@NewTransferActivity, getString(R.string.get_work_num_success), Toast.LENGTH_SHORT).show()
                    work_num.setText(response.body()?.data?.workNumber)
                    esl_num.setText(response.body()?.data?.eslNum.toString())
                } else {
                    Toast.makeText(this@NewTransferActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<workNumberBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@NewTransferActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
