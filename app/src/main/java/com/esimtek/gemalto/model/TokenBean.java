package com.esimtek.gemalto.model;

import com.google.gson.annotations.SerializedName;

public class TokenBean {
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
        @SerializedName("ExpireTime")
        private String expireTime;
        @SerializedName("SignToken")
        private String signToken;
        @SerializedName("StaffId")
        private int staffId;

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            expireTime = expireTime;
        }

        public String getSignToken() {
            return signToken;
        }

        public void setSignToken(String signToken) {
            signToken = signToken;
        }

        public int getStaffId() {
            return staffId;
        }

        public void setStaffId(int staffId) {
            staffId = staffId;
        }
    }
}