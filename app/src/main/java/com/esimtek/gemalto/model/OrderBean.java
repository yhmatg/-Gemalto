package com.esimtek.gemalto.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class OrderBean {

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
        @SerializedName("Table")
        private List<OrderInfoBean> orderInfoBeans;

        public List<OrderInfoBean> getOrderInfoBeans() {
            return orderInfoBeans;
        }

        public void setOrderInfoBeans(List<OrderInfoBean> orderInfoBeans) {
            this.orderInfoBeans = orderInfoBeans;
        }

        public static class OrderInfoBean {
            @SerializedName("GemaltoLabel_aCode")
            private String acode;
            @SerializedName("GemaltoLabel_barcode")
            private String barCode;
            @SerializedName("GemaltoLabel_batch")
            private String batch;
            @SerializedName("GemaltoLabel_customerName")
            private String customerName;
            @SerializedName("GemaltoLabel_inlay")
            private String inlay;
            @SerializedName("GemaltoLabel_location")
            private String location;
            @SerializedName("GemaltoLabel_mmyy")
            private int mmyy;
            @SerializedName("GemaltoLabel_orderNumber")
            private String orderNumber;
            @SerializedName("GemaltoLabel_productName")
            private String productName;
            @SerializedName("GemaltoLabel_qty")
            private int gemaltoQty;
            @SerializedName("GemaltoLabel_referenceNo")
            private String referenceNo;
            @SerializedName("GemaltoLabel_supplierName")
            private String supplierName;
            @SerializedName("RFIDLabel_qty")
            private int rfidQty;
            @SerializedName("RFIDLabel_badCardNo1")
            private int hotStampingBadCardNo;
            @SerializedName("RFIDLabel_badCardNo2")
            private int implantationBadCardNo;
            @SerializedName("StatusTable_state")
            private String clearEslStatus;

            public String getAcode() {
                return acode;
            }

            public void setAcode(String acode) {
                this.acode = acode;
            }

            public String getBarCode() {
                return barCode;
            }

            public void setBarCode(String barCode) {
                this.barCode = barCode;
            }

            public String getBatch() {
                return batch;
            }

            public void setBatch(String batch) {
                this.batch = batch;
            }

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getInlay() {
                return inlay;
            }

            public void setInlay(String inlay) {
                this.inlay = inlay;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public int getMmyy() {
                return mmyy;
            }

            public void setMmyy(int mmyy) {
                this.mmyy = mmyy;
            }

            public String getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(String orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public int getGemaltoQty() {
                return gemaltoQty;
            }

            public void setGemaltoQty(int gemaltoQty) {
                this.gemaltoQty = gemaltoQty;
            }

            public String getReferenceNo() {
                return referenceNo;
            }

            public void setReferenceNo(String referenceNo) {
                this.referenceNo = referenceNo;
            }

            public String getSupplierName() {
                return supplierName;
            }

            public void setSupplierName(String supplierName) {
                this.supplierName = supplierName;
            }

            public int getRfidQty() {
                return rfidQty;
            }

            public void setRfidQty(int rfidQty) {
                this.rfidQty = rfidQty;
            }

            public int getHotStampingBadCardNo() {
                return hotStampingBadCardNo;
            }

            public void setHotStampingBadCardNo(int hotStampingBadCardNo) {
                this.hotStampingBadCardNo = hotStampingBadCardNo;
            }

            public int getImplantationBadCardNo() {
                return implantationBadCardNo;
            }

            public void setImplantationBadCardNo(int implantationBadCardNo) {
                this.implantationBadCardNo = implantationBadCardNo;
            }

            public String getClearEslStatus() {
                return clearEslStatus;
            }

            public void setClearEslStatus(String clearEslStatus) {
                this.clearEslStatus = clearEslStatus;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                OrderInfoBean orderInfoBean = (OrderInfoBean) o;
                return mmyy == orderInfoBean.mmyy &&
                        Objects.equals(acode, orderInfoBean.acode) &&
                        Objects.equals(batch, orderInfoBean.batch) &&
                        Objects.equals(customerName, orderInfoBean.customerName) &&
                        Objects.equals(inlay, orderInfoBean.inlay) &&
                        Objects.equals(orderNumber, orderInfoBean.orderNumber) &&
                        Objects.equals(productName, orderInfoBean.productName) &&
                        Objects.equals(referenceNo, orderInfoBean.referenceNo) &&
                        Objects.equals(supplierName, orderInfoBean.supplierName);
            }

            @Override
            public int hashCode() {
                return Objects.hash(acode, batch, customerName, inlay, mmyy, orderNumber, productName, referenceNo, supplierName);
            }
        }
    }
}