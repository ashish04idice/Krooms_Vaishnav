package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by ashishidice on 22/4/17.
 */

public class PaymentPaidUnpaidModel {

    private String Room_no;
    private String Tenant_name;
    private String Totalamount;
    private String Paid;
    private String Paydate;
    private String Totalsum;
    private String Totalpaidsum;

    public String item_id;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getTotalsum() {
        return Totalsum;
    }

    public void setTotalsum(String totalsum) {
        Totalsum = totalsum;
    }

    public String getTotalpaidsum() {
        return Totalpaidsum;
    }

    public void setTotalpaidsum(String totalpaidsum) {
        Totalpaidsum = totalpaidsum;
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

    public String getTotalamount() {
        return Totalamount;
    }

    public void setTotalamount(String totalamount) {
        Totalamount = totalamount;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String getPaydate() {
        return Paydate;
    }

    public void setPaydate(String paydate) {
        Paydate = paydate;
    }
}
