package com.esimtek.gemalto

import android.app.Application
import com.esimtek.gemalto.model.LoggedBean
import com.esimtek.gemalto.util.BeeperUtil
import com.esimtek.gemalto.util.BeeperUtil.BeepMode
import com.esimtek.gemalto.util.MyCrashListener
import com.esimtek.gemalto.util.PreferenceUtil
import com.nativec.tools.ModuleManager
import com.rfid.ReaderConnector
import com.scanner1d.ODScannerConnector
import com.xuexiang.xlog.XLog
import com.xuexiang.xlog.crash.CrashHandler
import java.util.*

class GemaltoApplication : Application() {

    private var readerConnector = ReaderConnector()
    private var scannerConnector = ODScannerConnector()

    var uuid by PreferenceUtil("uuid", "")
    var token: String? = null
    var logged: LoggedBean? = null

    companion object {
        lateinit var instance: GemaltoApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (uuid.isEmpty()) uuid = UUID.randomUUID().toString()
        BeeperUtil.init(applicationContext)
        BeeperUtil.setBeepMode(BeepMode.BEEP_PER_TAG)
        scannerConnector.connectCom("dev/ttyS1", 9600)
        readerConnector.connectCom("dev/ttyS4", 115200)
        XLog.init(this)
        CrashHandler.getInstance().setOnCrashListener(MyCrashListener())
    }

    override fun onTerminate() {
        super.onTerminate()
        BeeperUtil.release()
        scannerConnector.disConnect()
        readerConnector.disConnect()
        ModuleManager.newInstance().scanStatus = false
        ModuleManager.newInstance().uhfStatus = false
        ModuleManager.newInstance().release()
        System.exit(0)
    }

}