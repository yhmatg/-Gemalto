package com.esimtek.gemalto.model;

public class workNumberBean {
    private DataBean Data;
    private int code;
    private String msg;
    private boolean success;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean data) {
        Data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        private String workNumber;
        private int eslNum;
        private Boolean BarcodeIsUsing;
        private String Details;

        public String getWorkNumber() {
            return workNumber;
        }

        public void setWorkNumber(String workNumber) {
            this.workNumber = workNumber;
        }

        public int getEslNum() {
            return eslNum;
        }

        public void setEslNum(int eslNum) {
            this.eslNum = eslNum;
        }

        public Boolean getBarcodeIsUsing() {
            return BarcodeIsUsing;
        }

        public void setBarcodeIsUsing(Boolean barcodeIsUsing) {
            BarcodeIsUsing = barcodeIsUsing;
        }

        public String getDetails() {
            return Details;
        }

        public void setDetails(String details) {
            Details = details;
        }
    }
}