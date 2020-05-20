package com.esimtek.gemalto

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import com.esimtek.gemalto.model.OrderBean
import com.esimtek.gemalto.model.RelateBean
import com.esimtek.gemalto.model.ResultBean
import com.esimtek.gemalto.network.Api
import com.esimtek.gemalto.network.HttpClient
import com.esimtek.gemalto.util.BeeperUtil
import com.esimtek.gemalto.util.ZxingUtil
import com.nativec.tools.ModuleManager
import com.scanner1d.ODScannerHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_box.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChangeBoxActivity : BaseActivity() {

    private val scanner: ODScannerHelper = ODScannerHelper.getDefaultHelper()

    private var obScanner: Observer = Observer { _, value ->
        if (value is String)
            disposable.add(Observable.just(value)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
                        if (it.length > 8) {
                            if ((it == pl1Code) or (it == pl2Code)) {
                                Toast.makeText(this, getString(R.string.box_exist), Toast.LENGTH_SHORT).show()
                                return@subscribe
                            }
                            if ((pl1Code.isNullOrEmpty()) or (pl2Code.isNullOrEmpty())) {
                                orderByPLCode(it)
                                return@subscribe
                            }
                            Toast.makeText(this, getString(R.string.box_is_full), Toast.LENGTH_SHORT).show()
                        } else initESLView(it.substring(2))
                    })
    }

    private var pl1Code: String? = null
    private var pl2Code: String? = null
    private var eslCode: String? = null
    private var pl1Bean: OrderBean.DataBean.OrderInfoBean? = null
    private var isUnbind: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_box)
        ModuleManager.newInstance().uhfStatus = false
        ModuleManager.newInstance().scanStatus = true
        scanner.setRunFlag(false)

        isUnbind = intent.getBooleanExtra("isUnbind", false)
        if (isUnbind) {
            setTitle(R.string.delete_change_box)
            tvChangeBox.setText(R.string.delete_change_box)
        }else{
            orderByPLCode(intent.getStringExtra("code"))
        }
        changeBox.setOnClickListener {
            if (isUnbind) {
                unbindESLAndPL()
            } else {
                relateESLAndPL()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scanner.registerObserver(obScanner)
    }

    override fun onPause() {
        super.onPause()
        scanner.unRegisterObserver(obScanner)
    }

    //展示根据条码获取的订单信息展示
    private fun initPL1View(tableBean: OrderBean.DataBean.OrderInfoBean) {
        pl1Code = tableBean.barCode
        iv_pl1.setImageBitmap(ZxingUtil.createOneDCode(pl1Code))
        tv_pl1_code.text = pl1Code
        tv_pl1_ord.text = tableBean.orderNumber
        tv_pl1_ref.text = tableBean.referenceNo
        tv_pl1_bat.text = tableBean.batch
        tv_pl1_cus.text = tableBean.customerName
        tv_pl1_pro.text = tableBean.productName
        tv_pl1_aco.text = tableBean.acode
        tv_pl1_mmyy.text = tableBean.mmyy.toString()
        tv_pl1_qty.text = tableBean.gemaltoQty.toString()
        tv_pl1_inlay.text = tableBean.inlay
        pl1.visibility = View.VISIBLE
        pl1Bean = tableBean
    }

    private fun initPL2View(tableBean: OrderBean.DataBean.OrderInfoBean) {
        if (tableBean != pl1Bean) {
            Toast.makeText(this, getString(R.string.box_information_not_same), Toast.LENGTH_SHORT).show()
            return
        }
        pl2Code = tableBean.barCode
        iv_pl2.setImageBitmap(ZxingUtil.createOneDCode(pl2Code))
        tv_pl2_code.text = pl2Code
        tv_pl2_ord.text = tableBean.orderNumber
        tv_pl2_ref.text = tableBean.referenceNo
        tv_pl2_bat.text = tableBean.batch
        tv_pl2_cus.text = tableBean.customerName
        tv_pl2_pro.text = tableBean.productName
        tv_pl2_aco.text = tableBean.acode
        tv_pl2_mmyy.text = tableBean.mmyy.toString()
        tv_pl2_qty.text = tableBean.gemaltoQty.toString()
        tv_pl2_inlay.text = tableBean.inlay
        pl2.visibility = View.VISIBLE
    }

    //扫描到esl条码展示
    private fun initESLView(code: String) {
        eslCode = code
        iv_esl.setImageBitmap(ZxingUtil.createOneDCode(eslCode))
        tv_esl_code.text = eslCode
        esl.visibility = View.VISIBLE
        changeBox.visibility = View.VISIBLE
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    //根据纸质条码网络获取订单信息
    private fun orderByPLCode(plCode: String?) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).orderByPLCode(plCode).enqueue(object : Callback<OrderBean> {
            override fun onResponse(call: Call<OrderBean>, response: Response<OrderBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    response.body()?.data?.orderInfoBeans?.get(0)?.let {
                        if (pl1.visibility == View.GONE) initPL1View(it)
                        else initPL2View(it)
                    }
                } else {
                    Toast.makeText(this@ChangeBoxActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@ChangeBoxActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //换盒子操作，网络绑定ESL和纸质标签
    private fun relateESLAndPL() {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).relateESLAndPL(RelateBean(eslCode, pl1Code, pl2Code)).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@ChangeBoxActivity, getString(R.string.box_change_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ChangeBoxActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@ChangeBoxActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //解绑操作 网络解绑ESL和纸质标签
    private fun unbindESLAndPL() {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).deleteESL(RelateBean(eslCode, pl1Code, pl2Code)).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@ChangeBoxActivity, getString(R.string.unbind_box_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ChangeBoxActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@ChangeBoxActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
