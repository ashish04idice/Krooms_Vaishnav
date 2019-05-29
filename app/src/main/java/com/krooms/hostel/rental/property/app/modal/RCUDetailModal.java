package com.krooms.hostel.rental.property.app.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 3/17/2016.
 */
public class RCUDetailModal implements Parcelable {

    private String tenant_id = "";
    private String tenant_room_no = "";
    private String tenant_room_id = "";
    private String tenant_form_no = "";
    private String tenant_detail = "";
    private String tenant_fname = "";
    private String tenant_lname = "";
    private String tenant_father_name = "";
    private String tenant_father_contact_no = "";
    private String tenant_permanent_address = "";
    private String flat_number = "";
    private String landmark = "";
    private String location = "";
    private String state = "";
    private String city = "";
    private String pincode = "";
    private String contact_number = "";
    private String email_id = "";
    private String property_hire_date = "";
    private String property_leave_date = "";
    private String tenant_photo = "";
    private String tenant_office_institute = "";
    private String tenant_contact_number = "";
    private String tenant_work_detail = "";
    private String guarantor_name = "";
    private String guarantor_address = "";
    private String guarantor_contact_number = "";
    private String vehicle_registration_no = "";
    private String vehicle_registration_photo = "";
    private String voter_id_card_no = "";
    private String voter_id_card_photo = "";
    private String driving_license_no = "";
    private String driving_license_photo = "";
    private String driving_licence_issu_ofc_name = "";
    private String telephone_number = "";
    private String mobile_number = "";
    private String aadhar_card_no = "";
    private String aadhar_card_photo = "";
    private String arm_licence_no = "";
    private String arm_licence_photo = "";
    private String arm_licence_issu_ofc_name = "";

    private String passport_no = "";
    private String passport_photo = "";
    private String rashan_card_no = "";
    private String rashan_card_photo = "";
    private String otherid_no = "";
    private String otherid_photo = "";



    private String paymentMode = "";
    private String paymentOrderId = "";
    private String paymentTransactionId = "";
    private String detail_verification = "";



    public RCUDetailModal(Parcel in) {
        tenant_id = in.readString();
        tenant_room_no = in.readString();
        tenant_room_id = in.readString();
        tenant_form_no = in.readString();
        tenant_detail = in.readString();
        tenant_fname = in.readString();
        tenant_lname = in.readString();
        tenant_father_name = in.readString();
        tenant_father_contact_no = in.readString();
        tenant_permanent_address = in.readString();
        flat_number = in.readString();
        landmark = in.readString();
        location = in.readString();
        state = in.readString();
        city = in.readString();
        pincode = in.readString();
        contact_number = in.readString();
        email_id = in.readString();
        property_hire_date = in.readString();
        property_leave_date = in.readString();
        tenant_photo = in.readString();
        tenant_office_institute = in.readString();
        tenant_contact_number = in.readString();
        tenant_work_detail = in.readString();
        guarantor_name = in.readString();
        guarantor_address = in.readString();
        guarantor_contact_number = in.readString();
        vehicle_registration_no = in.readString();
        vehicle_registration_photo = in.readString();
        voter_id_card_no = in.readString();
        voter_id_card_photo = in.readString();
        driving_license_no = in.readString();
        driving_license_photo = in.readString();
        driving_licence_issu_ofc_name = in.readString();
        telephone_number = in.readString();
        mobile_number = in.readString();
        rashan_card_no = in.readString();
        rashan_card_photo = in.readString();
        passport_no = in.readString();
        passport_photo = in.readString();
        otherid_no = in.readString();
        otherid_photo = in.readString();
        aadhar_card_no = in.readString();
        aadhar_card_photo = in.readString();
        arm_licence_no = in.readString();
        arm_licence_photo = in.readString();
        arm_licence_issu_ofc_name = in.readString();
        paymentMode = in.readString();
        paymentOrderId = in.readString();
        paymentTransactionId = in.readString();
        detail_verification = in.readString();
    }

    public static final Creator<RCUDetailModal> CREATOR = new Creator<RCUDetailModal>() {
        @Override
        public RCUDetailModal createFromParcel(Parcel in) {
            return new RCUDetailModal(in);
        }

        @Override
        public RCUDetailModal[] newArray(int size) {
            return new RCUDetailModal[size];
        }
    };

    public void setTenant_room_no(String tenant_room_no) {
        this.tenant_room_no = tenant_room_no;
    }

    public void setTenant_room_id(String tenant_room_id) {
        this.tenant_room_id = tenant_room_id;
    }

    public void setAadhar_card_no(String aadhar_card_no) {
        this.aadhar_card_no = aadhar_card_no;
    }

    public void setAadhar_card_photo(String aadhar_card_photo) {
        this.aadhar_card_photo = aadhar_card_photo;
    }

    public void setArm_licence_no(String arm_licence_no) {
        this.arm_licence_no = arm_licence_no;
    }

    public void setArm_licence_photo(String arm_licence_photo) {
        this.arm_licence_photo = arm_licence_photo;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setDriving_license_no(String driving_license_no) {
        this.driving_license_no = driving_license_no;
    }

    public void setDriving_license_photo(String driving_license_photo) {
        this.driving_license_photo = driving_license_photo;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setFlat_number(String flat_number) {
        this.flat_number = flat_number;
    }

    public void setGuarantor_address(String guarantor_address) {
        this.guarantor_address = guarantor_address;
    }

    public void setGuarantor_contact_number(String guarantor_contact_number) {
        this.guarantor_contact_number = guarantor_contact_number;
    }

    public void setGuarantor_name(String guarantor_name) {
        this.guarantor_name = guarantor_name;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setProperty_hire_date(String property_hire_date) {
        this.property_hire_date = property_hire_date;
    }

    public void setProperty_leave_date(String property_leave_date) {
        this.property_leave_date = property_leave_date;
    }

    public void setArm_licence_issu_ofc_name(String arm_licence_issu_ofc_name) {
        this.arm_licence_issu_ofc_name = arm_licence_issu_ofc_name;
    }

    public void setDetail_verification(String detail_verification) {
        this.detail_verification = detail_verification;
    }

    public void setDriving_licence_issu_ofc_name(String driving_licence_issu_ofc_name) {
        this.driving_licence_issu_ofc_name = driving_licence_issu_ofc_name;
    }


    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public void setPassport_photo(String passport_photo) {
        this.passport_photo = passport_photo;
    }

    public void setRashan_card_no(String rashan_card_no) {
        this.rashan_card_no = rashan_card_no;
    }

    public void setRashan_card_photo(String rashan_card_photo) {
        this.rashan_card_photo = rashan_card_photo;
    }

    public void setOtherid_no(String otherid_no) {
        this.otherid_no = otherid_no;
    }

    public void setOtherid_photo(String otherid_photo) {

        this.otherid_photo = otherid_photo;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public void setTenant_contact_number(String tenant_contact_number) {
        this.tenant_contact_number = tenant_contact_number;
    }

    public void setTenant_detail(String tenant_detail) {
        this.tenant_detail = tenant_detail;
    }

    public void setTenant_father_contact_no(String tenant_father_contact_no) {
        this.tenant_father_contact_no = tenant_father_contact_no;
    }

    public void setTenant_father_name(String tenant_father_name) {
        this.tenant_father_name = tenant_father_name;
    }

    public void setTenant_fname(String tenant_fname) {
        this.tenant_fname = tenant_fname;
    }

    public void setTenant_form_no(String tenant_form_no) {
        this.tenant_form_no = tenant_form_no;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public void setTenant_lname(String tenant_lname) {
        this.tenant_lname = tenant_lname;
    }

    public void setTenant_office_institute(String tenant_office_institute) {
        this.tenant_office_institute = tenant_office_institute;
    }

    public void setTenant_permanent_address(String tenant_permanent_address) {
        this.tenant_permanent_address = tenant_permanent_address;
    }

    public void setTenant_photo(String tenant_photo) {
        this.tenant_photo = tenant_photo;
    }

    public void setTenant_work_detail(String tenant_work_detail) {
        this.tenant_work_detail = tenant_work_detail;
    }

    public void setVehicle_registration_no(String vehicle_registration_no) {
        this.vehicle_registration_no = vehicle_registration_no;
    }

    public void setVehicle_registration_photo(String vehicle_registration_photo) {
        this.vehicle_registration_photo = vehicle_registration_photo;
    }

    public void setVoter_id_card_no(String voter_id_card_no) {
        this.voter_id_card_no = voter_id_card_no;
    }

    public void setVoter_id_card_photo(String voter_id_card_photo) {
        this.voter_id_card_photo = voter_id_card_photo;
    }


    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getTenant_room_no() {
        return tenant_room_no;
    }

    public String getTenant_room_id() {
        return tenant_room_id;
    }

    public String getAadhar_card_no() {
        return aadhar_card_no;
    }

    public String getAadhar_card_photo() {
        if(aadhar_card_photo!=null){
            aadhar_card_photo = aadhar_card_photo.replaceAll(" ", "%20");
        }else{
            aadhar_card_photo = "";
        }
        aadhar_card_photo = aadhar_card_photo.replaceAll(" ", "%20");
        return aadhar_card_photo;
    }

    public String getArm_licence_no() {
        return arm_licence_no;
    }

    public String getArm_licence_photo() {
        if(arm_licence_photo!=null){
            arm_licence_photo = arm_licence_photo.replaceAll(" ", "%20");
        }else{
            arm_licence_photo = "";
        }
        return arm_licence_photo;
    }

    public String getCity() {
        return city;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getDriving_license_no() {
        return driving_license_no;
    }

    public String getDriving_license_photo() {

        if(passport_photo!=null){
            driving_license_photo = driving_license_photo.replaceAll(" ", "%20");
        }else{
            driving_license_photo = "";
        }


        return driving_license_photo;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getFlat_number() {
        return flat_number;
    }

    public String getGuarantor_address() {
        return guarantor_address;
    }

    public String getGuarantor_contact_number() {
        return guarantor_contact_number;
    }

    public String getGuarantor_name() {
        return guarantor_name;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getLocation() {
        return location;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getPincode() {
        return pincode;
    }

    public String getProperty_hire_date() {
        return property_hire_date;
    }

    public String getProperty_leave_date() {
        return property_leave_date;
    }

    public String getArm_licence_issu_ofc_name() {
        return arm_licence_issu_ofc_name;
    }

    public String getDriving_licence_issu_ofc_name() {
        return driving_licence_issu_ofc_name;
    }

    public String getDetail_verification() {
        return detail_verification;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public String getPassport_photo() {
        if(passport_photo!=null){
            passport_photo = passport_photo.replaceAll(" ", "%20");
        }else{
            passport_photo = "";
        }

        return passport_photo;
    }

    public String getRashan_card_no() {
        return rashan_card_no;
    }

    public String getRashan_card_photo() {

        if(rashan_card_photo!=null){
            rashan_card_photo = rashan_card_photo.replaceAll(" ", "%20");
        }else{
            rashan_card_photo = "";
        }

        return rashan_card_photo;
    }

    public String getOtherid_no() {
        return otherid_no;
    }

    public String getOtherid_photo() {
        if(otherid_photo!=null){
            otherid_photo = otherid_photo.replaceAll(" ", "%20");
        }else{
            otherid_photo = "";
        }
        return otherid_photo;
    }

    public String getState() {
        return state;
    }

    public String getTelephone_number() {
        return telephone_number;
    }

    public String getTenant_contact_number() {
        return tenant_contact_number;
    }

    public String getTenant_detail() {
        return tenant_detail;
    }

    public String getTenant_father_contact_no() {
        return tenant_father_contact_no;
    }

    public String getTenant_father_name() {
        return tenant_father_name;
    }

    public String getTenant_fname() {
        return tenant_fname;
    }

    public String getTenant_form_no() {
        return tenant_form_no;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public String getTenant_lname() {
        return tenant_lname;
    }

    public String getTenant_office_institute() {
        return tenant_office_institute;
    }

    public String getTenant_permanent_address() {
        return tenant_permanent_address;
    }

    public String getTenant_photo() {
        if(tenant_photo!=null){
            tenant_photo = tenant_photo.replaceAll(" ", "%20");
        }else{
            tenant_photo = "";
        }
        return tenant_photo;
    }

    public String getTenant_work_detail() {
        return tenant_work_detail;
    }

    public String getVehicle_registration_no() {
        return vehicle_registration_no;
    }

    public String getVehicle_registration_photo() {
        if(vehicle_registration_photo!=null){
            vehicle_registration_photo = vehicle_registration_photo.replaceAll(" ", "%20");
        }else{
            vehicle_registration_photo = "";
        }
        return vehicle_registration_photo;
    }

    public String getVoter_id_card_no() {
        return voter_id_card_no;
    }

    public String getVoter_id_card_photo() {

        if(voter_id_card_photo!=null){
            voter_id_card_photo = voter_id_card_photo.replaceAll(" ", "%20");
        }else{
            voter_id_card_photo = "";
        }
        return voter_id_card_photo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tenant_id);
        dest.writeString(tenant_room_no);
        dest.writeString(tenant_room_id);
        dest.writeString(tenant_form_no);
        dest.writeString(tenant_detail);
        dest.writeString(tenant_fname);
        dest.writeString(tenant_lname);
        dest.writeString(tenant_father_name);
        dest.writeString(tenant_father_contact_no);
        dest.writeString(tenant_permanent_address);
        dest.writeString(flat_number);
        dest.writeString(landmark);
        dest.writeString(location);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(pincode);
        dest.writeString(contact_number);
        dest.writeString(email_id);
        dest.writeString(property_hire_date);
        dest.writeString(property_leave_date);
        dest.writeString(tenant_photo);
        dest.writeString(tenant_office_institute);
        dest.writeString(tenant_contact_number);
        dest.writeString(tenant_work_detail);
        dest.writeString(guarantor_name);
        dest.writeString(guarantor_address);
        dest.writeString(guarantor_contact_number);
        dest.writeString(vehicle_registration_no);
        dest.writeString(vehicle_registration_photo);
        dest.writeString(voter_id_card_no);
        dest.writeString(voter_id_card_photo);
        dest.writeString(driving_license_no);
        dest.writeString(driving_license_photo);
        dest.writeString(driving_licence_issu_ofc_name);
        dest.writeString(telephone_number);
        dest.writeString(mobile_number);
        dest.writeString(rashan_card_no);
        dest.writeString(rashan_card_photo);
        dest.writeString(passport_no);
        dest.writeString(passport_photo);
        dest.writeString(otherid_no);
        dest.writeString(otherid_photo);
        dest.writeString(aadhar_card_no);
        dest.writeString(aadhar_card_photo);
        dest.writeString(arm_licence_no);
        dest.writeString(arm_licence_photo);
        dest.writeString(arm_licence_issu_ofc_name);
        dest.writeString(paymentMode);
        dest.writeString(paymentOrderId);
        dest.writeString(paymentTransactionId);
        dest.writeString(detail_verification);
    }
}
