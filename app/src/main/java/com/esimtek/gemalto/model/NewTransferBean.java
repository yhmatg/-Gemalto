package com.esimtek.gemalto.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewTransferBean {
    @SerializedName("oldRFIDList")
    private List<String> beforeTransferList;
    private String workNumber;

    public NewTransferBean(List<String> beforeTransferList, String workNumber) {
        this.beforeTransferList = beforeTransferList;
        this.workNumber = workNumber;
    }

    public List<String> getBeforeTransferList() {
        return beforeTransferList;
    }

    public void setBeforeTransferList(List<String> beforeTransferList) {
        this.beforeTransferList = beforeTransferList;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }
}
