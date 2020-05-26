package com.esimtek.esim.model;

public class NewLocationBean {
    private String location;
    private String workNumber;

    public NewLocationBean(String location, String workNumber) {
        this.location = location;
        this.workNumber = workNumber;
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
}

