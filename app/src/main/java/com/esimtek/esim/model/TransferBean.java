package com.esimtek.esim.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransferBean {

    /**
     * oldRFIDList : ["00A457","00A458"]
     * newRFIDList : ["00A457","00A458"]
     */

    @SerializedName("oldRFIDList")
    private List<String> beforeTransferList;
    @SerializedName("newRFIDList")
    private List<String> postTransferList;

    public TransferBean(List<String> beforeTransferList, List<String> postTransferList) {
        this.beforeTransferList = beforeTransferList;
        this.postTransferList = postTransferList;
    }

    public List<String> getBeforeTransferList() {
        return beforeTransferList;
    }

    public void setBeforeTransferList(List<String> beforeTransferList) {
        this.beforeTransferList = beforeTransferList;
    }

    public List<String> getPostTransferList() {
        return postTransferList;
    }

    public void setPostTransferList(List<String> postTransferList) {
        this.postTransferList = postTransferList;
    }
}
