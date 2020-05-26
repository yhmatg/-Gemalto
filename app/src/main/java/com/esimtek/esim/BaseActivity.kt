package com.esimtek.esim

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.disposables.CompositeDisposable

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    var disposable = CompositeDisposable()
    lateinit var hudDialog: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hudDialog = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    fun exitDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.alert_dialog_title))
                .setMessage(getString(R.string.are_you_sure_to_exit))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(getString(R.string.sure)) { _, _ -> EsimApplication.instance.onTerminate() }
                .show()
    }
}
