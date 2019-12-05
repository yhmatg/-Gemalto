package com.esimtek.gemalto.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForceBean {

    /**
     * location : BeJson
     * workNumber : BeJson
     * EpcNum : 1
     * EpcList : ["00A457","00A458"]
     */

    private String location;
    private String workNumber;
    @SerializedName("EpcNum")
    private int epcNumber;
    @SerializedName("EpcList")
    private List<String> epcList;

    public ForceBean(String location, String workNumber, int epcNumber, List<String> epcList) {
        this.location = location;
        this.workNumber = workNumber;
        this.epcNumber = epcNumber;
        this.epcList = epcList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public int getEpcNum() {
        return epcNumber;
    }

    public void setEpcNum(int epcNumber) {
        this.epcNumber = epcNumber;
    }

    public List<String> getEpcList() {
        return epcList;
    }

    public void setEpcList(List<String> epcList) {
        this.epcList = epcList;
    }
}
