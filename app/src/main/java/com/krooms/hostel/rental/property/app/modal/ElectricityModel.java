package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by ashishidice on 22/4/17.
 */

public class ElectricityModel {

    private String Room_no;
    private String Tenant_name;
    private String Tenant_id;
    private String Unit;
    private String Savedate;
    public String item_id;


    public String getTenant_id() {
        return Tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        Tenant_id = tenant_id;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSavedate() {
        return Savedate;
    }

    public void setSavedate(String savedate) {
        Savedate = savedate;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }


    public String getRoom_no() {
        return Room_no;
    }

    public void setRoom_no(String room_no) {
        Room_no = room_no;
    }

    public String getTenant_name() {
        return Tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        Tenant_name = tenant_name;
    }

}
