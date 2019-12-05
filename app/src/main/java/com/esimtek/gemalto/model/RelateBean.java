package com.esimtek.gemalto.model;

public class RelateBean {
    private String eslCode;
    private String plCode1;
    private String plCode2;

    public RelateBean(String eslCode, String plCode1, String plCode2) {
        this.eslCode = eslCode;
        this.plCode1 = plCode1;
        this.plCode2 = plCode2;
    }

    public String getEslCode() {
        return eslCode;
    }

    public void setEslCode(String eslCode) {
        this.eslCode = eslCode;
    }

    public String getPlCode1() {
        return plCode1;
    }

    public void setPlCode1(String plCode1) {
        this.plCode1 = plCode1;
    }

    public String getPlCode2() {
        return plCode2;
    }

    public void setPlCode2(String plCode2) {
        this.plCode2 = plCode2;
    }
}