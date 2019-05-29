package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by ashish on 27/1/17.
 */

public class Tenantviewfeedbackmodel {

    String Tenantname,Ownerfeedbackreply,time;

    public String getTenantname() {
        return Tenantname;
    }

    public void setTenantname(String tenantname) {
        Tenantname = tenantname;
    }

    public String getOwnerfeedbackreply() {
        return Ownerfeedbackreply;
    }

    public void setOwnerfeedbackreply(String ownerfeedbackreply) {
        Ownerfeedbackreply = ownerfeedbackreply;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
