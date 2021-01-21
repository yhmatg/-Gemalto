package com.esimtek.esim.uhf;

public class UhfTag {
    private String epc;
    private String tid;
    private String rssi;
    private String allData;
    private String barcode;

    public UhfTag(String epc, String tid, String rssi, String allData) {
        this.epc = epc;
        this.tid = tid;
        this.rssi = rssi;
        this.allData = allData;
    }

    public UhfTag() {
    }

    public UhfTag(String epc) {
        this.epc = epc;
    }

    public UhfTag(String epc, String tid) {
        this.epc = epc;
        this.tid = tid;
    }

    public UhfTag(String epc, String tid, String rssi) {
        this.epc = epc;
        this.tid = tid;
        this.rssi = rssi;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getAllData() {
        return allData;
    }

    public void setAllData(String allData) {
        this.allData = allData;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
