package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by admin on 11/10/2016.
 */
public class MonthDetailModal
{
    String month_id;
    String room_id;
    String owner_id;
    String month;
    String date;
    String monthlypayment;
    String totalamount;
    String paid;
    String Due;
    String  mode;
    String  paymentcomment;


    public String getPaymentcomment() {
        return paymentcomment;
    }

    public void setPaymentcomment(String paymentcomment) {
        this.paymentcomment = paymentcomment;
    }

    public String getMonth_id() {
        return month_id;
    }

    public void setMonth_id(String month_id) {
        this.month_id = month_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getMonthlypayment() {
        return monthlypayment;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDue() {
        return Due;
    }

    public void setDue(String due) {
        Due = due;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public void setMonthlypayment(String monthlypayment) {
        this.monthlypayment = monthlypayment;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
