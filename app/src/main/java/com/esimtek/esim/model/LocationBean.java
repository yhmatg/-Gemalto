package com.esimtek.esim.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationBean {

    /**
     * location : BeJson
     * RFIDEpc : ["00A457","00A458"]
     */

    private String location;
    @SerializedName("RFIDEpc")
    private List<String> epcList;

    public LocationBean(String location, List<String> epcList) {
        this.location = location;
        this.epcList = epcList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getEpcList() {
        return epcList;
    }

    public void setEpcList(List<String> epcList) {
        this.epcList = epcList;
    }
}
