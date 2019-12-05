package com.esimtek.gemalto.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserBean {

    /**
     * code : 200
     * msg : Success
     * success : true
     * Data : {"Table":[{"UserTable_UserName":"admin","UserTable_SetupTime":"2019-05-31T11:01:18.747","UserTable_UserType":"admin"},{"UserTable_UserName":"staff1","UserTable_SetupTime":"2019-05-31T16:28:28.547","UserTable_UserType":"staff"}]}
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
        @SerializedName("Table")
        private List<UserInfoBean> userInfoBeans;

        public List<UserInfoBean> getUserInfoBeans() {
            return userInfoBeans;
        }

        public void setUserInfoBeans(List<UserInfoBean> userInfoBeans) {
            this.userInfoBeans = userInfoBeans;
        }

        public static class UserInfoBean {
            /**
             * UserTable_UserName : admin
             * UserTable_SetupTime : 2019-05-31T11:01:18.747
             * UserTable_UserType : admin
             */

            @SerializedName("UserTable_UserName")
            private String userName;
            @SerializedName("UserTable_SetupTime")
            private String setupTime;
            @SerializedName("UserTable_UserType")
            private String userType;

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getSetupTime() {
                return setupTime;
            }

            public void setSetupTime(String setupTime) {
                this.setupTime = setupTime;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }
        }
    }
}
