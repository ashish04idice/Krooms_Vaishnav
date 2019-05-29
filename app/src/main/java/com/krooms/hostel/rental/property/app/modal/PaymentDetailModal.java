package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by admin on 11/18/2016.
 */
public class PaymentDetailModal {

    String userid;
    String ownerid;
    String monthid;
    String monthName;
    String roomid;
    String t_Amount;
    String cur_date;
    String property_id;
    String mode_pay;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getMonthid() {
        return monthid;
    }

    public void setMonthid(String monthid) {
        this.monthid = monthid;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getT_Amount() {
        return t_Amount;
    }

    public void setT_Amount(String t_Amount) {
        this.t_Amount = t_Amount;
    }

    public String getCur_date() {
        return cur_date;
    }

    public void setCur_date(String cur_date) {
        this.cur_date = cur_date;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getMode_pay() {
        return mode_pay;
    }

    public void setMode_pay(String mode_pay) {
        this.mode_pay = mode_pay;
    }
}
