package com.esimtek.esim.model;

public class BadNoBean {
    private int badCardNo;
    private int numForSubmit;
    private String eslCode;

    public BadNoBean(String eslCode, int badCardNo, int numForSubmit) {
        this.eslCode = eslCode;
        this.badCardNo = badCardNo;
        this.numForSubmit = numForSubmit;

    }

    public String getEslCode() {
        return this.eslCode;
    }

    public void setEslCode(String eslCode) {
        this.eslCode = eslCode;
    }

    public int getBadCardNo() {
        return this.badCardNo;
    }

    public void setBadCardNo(int badCardNo) {
        this.badCardNo = badCardNo;
    }

    public int getNumForSubmit() {
        return numForSubmit;
    }

    public void setNumForSubmit(int numForSubmit) {
        this.numForSubmit = numForSubmit;
    }
}