package com.esimtek.esim.uhf;

import android.media.ToneGenerator;
import android.serialport.DeviceControlSpd;
import android.view.KeyEvent;
import com.esimtek.esim.util.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.Timer;
import cn.com.example.rfid.driver.Driver;
import cn.com.example.rfid.driver.RfidDriver;

import static android.serialport.DeviceControlSpd.PowerType.EXPAND;


/**
 * @author rylai
 * created at 2019/5/31 10:54
 */
public class NewSpeedataUhfServiceImpl extends EsimUhfAbstractService {
    private static final String TAG = "SpeedataUhfServiceImpl";
    private Driver driver;
    private DeviceControlSpd newUHFDeviceControl;
    //普通声音
    private static ToneGenerator toneGenerator;
    private boolean beepON = false;
    private Timer tbeep;
    //定位
    private final int BEEP_DELAY_TIME_MIN = 0;
    private final int BEEP_DELAY_TIME_MAX = 300;
    private boolean beepONLocate;
    private Timer locatebeep;
    private boolean locaitonStart = false;
    private short maxValue = -30, minValue = -80; //RSSI的最大值和最小值

    public NewSpeedataUhfServiceImpl() {
        driver = new RfidDriver();
        int[] gpios = {9, 14};
        try {
            newUHFDeviceControl = new DeviceControlSpd(EXPAND, gpios);
            newUHFDeviceControl.PowerOnDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean initRFID() {
        int status = driver.initRFID("/dev/ttyMT0");
        if (1000 == status) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT);
            EventBus.getDefault().post(uhfMsgEvent);
            return true;
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent1 = new UhfMsgEvent<>(UhfMsgType.UHF_CONNECT_FAIL);
            EventBus.getDefault().post(uhfMsgEvent1);
            return false;
        }

    }

    @Override
    public boolean closeRFID() {
        try {
            newUHFDeviceControl.PowerOffDevice();
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_DISCONNECT);
            EventBus.getDefault().post(uhfMsgEvent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean startStopScanning() {
        try {
            if (!isStart) {
                startScanning();
            } else {
                stopScanning();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean startScanning() {
        try {
            if (!isStart) {
                isStart = true;
                int status = driver.readMore();
                new TagThread().start();
                UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_START);
                EventBus.getDefault().post(uhfMsgEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean stopScanning() {
        if (isStart) {
            isStart = false;
            driver.stopRead();
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_STOP);
            EventBus.getDefault().post(uhfMsgEvent);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int setFilterData(int area, int start, int length, String data, boolean isSave) {
        if (StringUtils.isEmpty(data)) {
            locaitonStart = false;
        } else {
            locaitonStart = true;
        }
        return driver.Set_Filter_Data(area, start, length, data, isSave);
    }

    //设置功率
    @Override
    public void setPower(int data) {
        int status = driver.setTxPowerOnce(data);
        if (-1000 == status || (-1020) == status || 0 == status) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.SETTING_POWER_SUCCESS);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    //获取当前功率
    @Override
    public int getPower() {
        int power = driver.GetTxPower();
        return power;
    }

    @Override
    public int getDownKey() {
        return KeyEvent.KEYCODE_F1;
    }

    @Override
    public void writeEpcTag(String selectEpc, String epcData) {
        //密码
        String passWard = "00000000";
        //选中区域 epc:1
        int selectArea = 1;
        //选中标签起始地址
        int selectStartAdr = 32;
        //选中标签长度
        int selectLength = 96;
        //selectEpc 选中标签epc数据
        //写入区域 epc:1
        int writeArea = 1;
        //写入标签起始地址
        int writeStartAdr = 2;
        //写入标签长度
        int writeLength = 6;
        //epcData 要写入的epc数据
        int writeResult = driver.Write_Data_Tag(passWard, selectArea, selectStartAdr, selectLength, selectEpc, writeArea, writeStartAdr, writeLength, epcData);
        if (writeResult == 0) {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_WRITE_SUC);
            EventBus.getDefault().post(uhfMsgEvent);
        } else {
            UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.UHF_READ_FAIL);
            EventBus.getDefault().post(uhfMsgEvent);
        }
    }

    class TagThread extends Thread {

        private int mBetween = 0;

        public TagThread() {

        }

        public void run() {

            while (isStart) {

                String[] strEpc1 = {driver.GetBufData()};
                String strEpc = strEpc1[0];
                if (strEpc != null && strEpc.length() != 0) {
                    String text = strEpc.substring(4);
                    String len = strEpc.substring(0, 2);
                    int epclen = (Integer.parseInt(len, 16) / 8) * 4;
                    String finalEpc = text.substring(0, epclen);
                    //add rssi start
                    int rssi = 0;
                    int Hb = 0;
                    int Lb = 0;
                    String rssiString = text.substring(text.length() - 6, text.length() - 2);
                    if (4 != rssiString.length()) {
                        rssiString = "0000";
                    } else {
                        Hb = Integer.parseInt(rssiString.substring(0, 2), 16);
                        Lb = Integer.parseInt(rssiString.substring(2, 4), 16);
                        rssi = ((Hb - 256 + 1) * 256 + (Lb - 256)) / 10;
                    }
                    if (rssi >= maxValue) {
                        rssi = maxValue;
                    } else if (rssi <= minValue) {
                        rssi = minValue;
                    }
                    rssi -= minValue;//rssi+80转为正数
                    rssi = rssi * 2;
                    //add rssi end
                    UhfTag utag = new UhfTag(finalEpc, null, null, strEpc);
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG, utag);
                    EventBus.getDefault().post(uhfMsgEvent);
                } else {
                    UhfMsgEvent<UhfTag> uhfMsgEvent = new UhfMsgEvent<>(UhfMsgType.INV_TAG_NULL);
                    EventBus.getDefault().post(uhfMsgEvent);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean setWorkAndWaitTime(int workTime, int waitTime, boolean isSave) {
        return driver.ScanWaitTime_Set(workTime, waitTime, isSave);
    }

    //获取固件版本
    public String getFirmwareVersion() {
        String Fw_buffer;
        Fw_buffer = driver.readUM7fwOnce();
        if (Fw_buffer.equals("-1000")) {
            return "";
        }
        if (Fw_buffer.equals("-1020")) {
            return "";
        }
        return getDesignatedDecimalData(Fw_buffer);
    }

    private String getDesignatedDecimalData(String hexString) {
        StringBuilder version = new StringBuilder();
        int n = 0;
        for (int i = 0; i < 3; i++) {
            int vals = Integer.parseInt(hexString.substring(n, n + 2), 16);
            String val = i < 2 ? vals + "." : vals + "";
            version.append(val);
            n += 2;
        }
        return version.toString();
    }
}
