package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by Raghveer on 08-02-2017.
 */
public class Feedbackdetailtenant {


    String Id,Check_Reply,Status,ownercounttotal,Owner_name,Owner_Propertyid,Owner_id,Owner_userid,Owner_feedbacktype,Owner_feedbackdescription,Time,Date,feedback_id,Tenantname,Tenant_id,Tenant_roomno,Parent_id,Warden_id;
    String sendername;

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getParent_id() {
        return Parent_id;
    }

    public String getWarden_id() {
        return Warden_id;
    }

    public void setWarden_id(String warden_id) {
        Warden_id = warden_id;
    }

    public void setParent_id(String parent_id) {
        Parent_id = parent_id;
    }

    public String getTenant_roomno() {
        return Tenant_roomno;
    }

    public void setTenant_roomno(String tenant_roomno) {
        Tenant_roomno = tenant_roomno;
    }

    public String getTenant_id() {
        return Tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        Tenant_id = tenant_id;
    }

    public String getOwner_Propertyid() {
        return Owner_Propertyid;
    }

    public void setOwner_Propertyid(String owner_Propertyid) {
        Owner_Propertyid = owner_Propertyid;
    }

    public String getTenantname() {
        return Tenantname;
    }

    public void setTenantname(String tenantname) {
        Tenantname = tenantname;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCheck_Reply() {
        return Check_Reply;
    }

    public void setCheck_Reply(String check_Reply) {
        Check_Reply = check_Reply;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOwnercounttotal() {
        return ownercounttotal;
    }

    public void setOwnercounttotal(String ownercounttotal) {
        this.ownercounttotal = ownercounttotal;
    }

    public String getOwner_name() {
        return Owner_name;
    }

    public void setOwner_name(String owner_name) {
        Owner_name = owner_name;
    }


    public String getOwner_id() {
        return Owner_id;
    }

    public void setOwner_id(String owner_id) {
        Owner_id = owner_id;
    }

    public String getOwner_userid() {
        return Owner_userid;
    }

    public void setOwner_userid(String owner_userid) {
        Owner_userid = owner_userid;
    }

    public String getOwner_feedbacktype() {
        return Owner_feedbacktype;
    }

    public void setOwner_feedbacktype(String owner_feedbacktype) {
        Owner_feedbacktype = owner_feedbacktype;
    }

    public String getOwner_feedbackdescription() {
        return Owner_feedbackdescription;
    }

    public void setOwner_feedbackdescription(String owner_feedbackdescription) {
        Owner_feedbackdescription = owner_feedbackdescription;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }
}
