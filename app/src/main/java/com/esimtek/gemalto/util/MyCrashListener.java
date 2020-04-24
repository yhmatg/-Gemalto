package com.esimtek.gemalto.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import com.xuexiang.xlog.crash.ICrashHandler;
import com.xuexiang.xlog.crash.OnCrashListener;
import com.xuexiang.xlog.crash.XCrash;
import com.xuexiang.xlog.crash.ui.CrashActivity;
import com.xuexiang.xlog.crash.ui.CrashInfo;
import com.xuexiang.xlog.crash.ui.CrashUtils;

public class MyCrashListener implements OnCrashListener {
    @Override
    public void onCrash(Context context, ICrashHandler crashHandler, Throwable throwable) {
        Toast.makeText(context, "程序异常日志路径：/storage/emulated/0/Android/data/com.esimtek.gemalto/cache/crash_log", Toast.LENGTH_LONG).show();
        crashHandler.setIsHandledCrash(true);
    }
}
