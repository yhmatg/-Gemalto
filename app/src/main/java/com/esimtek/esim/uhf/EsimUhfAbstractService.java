package com.esimtek.esim.uhf;

import android.view.KeyEvent;

/**
 * Created by tiansen on 18-3-28.
 */

public abstract class EsimUhfAbstractService implements IEsimUhfService {
    boolean isStart;
    private boolean mEnable = false;

    @Override
    public boolean initRFID() {
        return false;
    }


    @Override
    public boolean startScanning() {
        return false;
    }

    @Override
    public boolean closeRFID() {
        return false;
    }

    @Override
    public boolean startStopScanning() {
        return false;
    }

    @Override
    public int getDownKey() {
        return KeyEvent.KEYCODE_F1;
    }

    @Override
    public boolean stopScanning() {
        return false;
    }

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        return -1;
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
    }

    @Override
    public boolean isEnable() {
        return mEnable;
    }

    @Override
    public void setEnable(boolean invEnable) {
        mEnable = invEnable;
    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
    public void setPower(int data) {

    }

    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public int getBatteryLevel() {
        return 0;
    }

    @Override
    public void setBeeper() {

    }
}
