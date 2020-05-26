package com.esimtek.esim.model;

import com.google.gson.annotations.SerializedName;

public class UserManagerBean {

    /**
     * userName : admin
     * password : admin
     */

    private String userName;
    private String password;
    @SerializedName("Request")
    private int requestType;

    public UserManagerBean(String userName, String password, int requestType) {
        this.userName = userName;
        this.password = password;
        this.requestType = requestType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }
}
