package com.esimtek.esim.model;

import com.google.gson.annotations.SerializedName;

public class LoggedBean {


    /**
     * code : 200
     * msg : Success
     * success : true
     * Data : {"userId":4,"UserTable_UserType":"admin","passwordExpire":false}
     */

    private int code;
    private String msg;
    private boolean success;
    private DataBean Data;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * userId : 4
         * UserTable_UserType : admin
         * passwordExpire : false
         */

        private int userId;
        @SerializedName("UserTable_UserType")
        private String userType;
        private boolean passwordExpire;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public boolean isPasswordExpire() {
            return passwordExpire;
        }

        public void setPasswordExpire(boolean passwordExpire) {
            this.passwordExpire = passwordExpire;
        }
    }
}
