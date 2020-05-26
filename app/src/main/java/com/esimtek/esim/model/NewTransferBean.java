package com.esimtek.esim.model;

import java.util.List;

public class NewTransferBean {
    private List<String> eslCodeList;
    private String workNumber;

    public NewTransferBean(List<String> eslCodeList, String workNumber) {
        this.eslCodeList = eslCodeList;
        this.workNumber = workNumber;
    }

    public List<String> getEslCodeList() {
        return eslCodeList;
    }

    public void setEslCodeList(List<String> eslCodeList) {
        this.eslCodeList = eslCodeList;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }
}
