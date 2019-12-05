package com.esimtek.gemalto

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.esimtek.gemalto.model.BadNoBean
import com.esimtek.gemalto.model.OrderBean
import com.esimtek.gemalto.model.ResultBean
import com.esimtek.gemalto.network.Api
import com.esimtek.gemalto.network.HttpClient
import com.esimtek.gemalto.util.BeeperUtil
import com.esimtek.gemalto.util.PreferenceUtil
import com.nativec.tools.ModuleManager
import com.scanner1d.ODScannerHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_bad_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostBadCardActivity : BaseActivity() {

    private var scene by PreferenceUtil("scene", "main")
    private val sceneType = if ("hotStamping" == scene) 1 else 2

    private var eslCode: String? = null

    private val scanner: ODScannerHelper = ODScannerHelper.getDefaultHelper()
    private var obScanner: Observer = Observer { _, value ->
        if (value is String)
            disposable.add(Observable.just(value)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        BeeperUtil.beep(BeeperUtil.BEEPER_SHORT)
                        orderByESLCode(it)
                    })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_bad_card)
        ModuleManager.newInstance().uhfStatus = false
        ModuleManager.newInstance().scanStatus = true
        scanner.setRunFlag(false)
        modify.setOnClickListener {
            if (eslCode.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.bad_card_no_esl_tip), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val badCardCountText = badNoEditText.text.toString().trim()
            if (badCardCountText.isEmpty()) {
                Toast.makeText(this, getString(R.string.bad_card_no_empty_tip), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val badCardCount = badCardCountText.toInt()
            if (badCardCount < 1) {
                Toast.makeText(this, getString(R.string.bad_card_no_less_tip), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (badCardCount > 1000) {
                Toast.makeText(this, getString(R.string.bad_card_no_more_tip), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            badCardCount(badCardCount)
        }
        reset.setOnClickListener {
            if (eslCode.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.bad_card_no_esl_tip), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AlertDialog.Builder(this).setTitle(getString(R.string.alert_dialog_title_reset))
                    .setMessage(getString(R.string.are_you_sure_to_reset))
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    .setPositiveButton(getString(R.string.sure)) { _, _ -> badCardCount(0) }
                    .show()
        }
        orderByESLCode(intent.getStringExtra("code"))
    }

    override fun onResume() {
        super.onResume()
        scanner.registerObserver(obScanner)
    }

    override fun onPause() {
        super.onPause()
        scanner.unRegisterObserver(obScanner)
    }

    private fun initPLView(tableBean: OrderBean.DataBean.OrderInfoBean) {
        tv_pl_ord.text = tableBean.orderNumber
        tv_pl_ref.text = tableBean.referenceNo
        tv_pl_bat.text = tableBean.batch
        tv_pl_cus.text = tableBean.customerName
        tv_pl_pro.text = tableBean.productName
        tv_pl_aco.text = tableBean.acode
        tv_pl_mmyy.text = tableBean.mmyy.toString()
        tv_pl_qty.text = (tableBean.rfidQty - tableBean.hotStampingBadCardNo - tableBean.implantationBadCardNo).toString()
        tv_pl_inlay.text = tableBean.inlay
        plCardView.visibility = View.VISIBLE
        badNoCardView.visibility = View.VISIBLE
        modify.visibility = View.VISIBLE
    }

    private fun orderByESLCode(eslCode: String?) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).orderByESLCode(eslCode).enqueue(object : Callback<OrderBean> {
            override fun onResponse(call: Call<OrderBean>, response: Response<OrderBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    response.body()?.data?.orderInfoBeans?.get(0)?.let {
                        this@PostBadCardActivity.eslCode = eslCode
                        initPLView(it)
                    }
                } else {
                    Toast.makeText(this@PostBadCardActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@PostBadCardActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun badCardCount(badCardCount: Int) {
        hudDialog.show()
        HttpClient().provideRetrofit().create(Api::class.java).badCardNo(BadNoBean(eslCode, badCardCount, sceneType)).enqueue(object : Callback<ResultBean> {
            override fun onResponse(call: Call<ResultBean>, response: Response<ResultBean>) {
                hudDialog.dismiss()
                if (response.body()?.isSuccess == true) {
                    Toast.makeText(this@PostBadCardActivity, getString(R.string.modify_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@PostBadCardActivity, response.body()?.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultBean>, t: Throwable) {
                hudDialog.dismiss()
                t.printStackTrace()
                Toast.makeText(this@PostBadCardActivity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
