package com.esimtek.esim.uhf;


/**
 * Created by tiansen on 18-3-28.
 */

public interface IEsimUhfService {
    boolean initRFID();

    boolean startScanning();

    boolean stopScanning();

    boolean closeRFID();

    boolean startStopScanning();

    boolean isEnable();

    void setEnable(boolean invEnable);

    boolean isStart();

    int getDownKey();

    void writeEpcTag(String selectEpc, String epcData);

    int setFilterData(int area, int start, int length, String data, boolean isSave);

    void setPower(int data);

    int getPower();

    int getBatteryLevel();

    void setBeeper();
}
