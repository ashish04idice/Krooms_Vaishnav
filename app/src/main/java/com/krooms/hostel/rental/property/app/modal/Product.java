package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by Raghveer on 02-02-2017.
 */
public class Product {

    public   String name;
    public  String hostelname,parentid,tenantid,mobilenovalue;
    public boolean box;

    public String getMobilenovalue() {
        return mobilenovalue;
    }

    public void setMobilenovalue(String mobilenovalue) {
        this.mobilenovalue = mobilenovalue;
    }

    public Product(String _describe, String hostelnamevalue,String ParentId,String TenantId,String Mobilenovalue, boolean _box) {
        name = _describe;
        hostelname=hostelnamevalue;
        parentid=ParentId;
        tenantid=TenantId;
        mobilenovalue=Mobilenovalue;
        box = _box;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostelname() {
        return hostelname;
    }

    public void setHostelname(String hostelname) {
        this.hostelname = hostelname;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public Product() {

    }
}
