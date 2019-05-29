package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by ashishidice on 22/4/17.
 */

public class TenantRecordmodel {

    String Room_no;
    int Photos;
    String Tenant_name;
    String Last_out_time;
    String LAst_in_time;
    String Count;
    String Reason;
    String attendancestatus;
    String time;
    String Time_interval;


    public String getTime_interval() {
        return Time_interval;
    }

    public void setTime_interval(String time_interval) {
        Time_interval = time_interval;
    }

    public String getAttendancestatus() {
        return attendancestatus;
    }

    public void setAttendancestatus(String attendancestatus) {
        this.attendancestatus = attendancestatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom_no() {
        return Room_no;
    }

    public void setRoom_no(String room_no) {
        Room_no = room_no;
    }

    public int getPhotos() {
        return Photos;
    }

    public void setPhotos(int photos) {
        Photos = photos;
    }

    public String getTenant_name() {
        return Tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        Tenant_name = tenant_name;
    }

    public String getLast_out_time() {
        return Last_out_time;
    }

    public void setLast_out_time(String last_out_time) {
        Last_out_time = last_out_time;
    }

    public String getLAst_in_time() {
        return LAst_in_time;
    }

    public void setLAst_in_time(String LAst_in_time) {
        this.LAst_in_time = LAst_in_time;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
