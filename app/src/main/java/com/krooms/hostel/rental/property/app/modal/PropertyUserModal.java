package com.krooms.hostel.rental.property.app.modal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 2/20/2016.
 */
public class PropertyUserModal implements Serializable,Parcelable {

    public String propertyId;
    public String status_active;
    public String userId;
    public String ownerId;
    public String userName;
    public String userMobileno;
    public String userAddress;
    public String bookedRoom, bookedRoomId;
    public String bookedBad;
    public String bookedDate;
    public String leaveDate;
    public String paidAmount;
    public String remainingAmount;
    public String paymentStatus;
    public String roomAmount;
    public String userImage;
    public String transaction_id;
    private boolean isSelected;
    public String t_user_id;
    private String tenant_id;
    private String tenant_form_no;
    private String tenant_detail;
    private String tenant_fname;
    private String tenant_lname;
    private String tenant_father_name;
    private String tenant_father_contact_no;
    private String tenant_permanent_address;
    private String flat_number;
    private String landmark;
    private String location;
    private String state;
    private String city;
    private String pincode;
    private String contact_number;
    private String email_id;
    private String property_hire_date;
    private String property_leave_date;
    private String tenant_photo;
    private String tenant_office_institute;
    private String tenant_contact_number;
    private String tenant_work_detail;
    private String guarantor_name;
    private String guarantor_address;
    private String guarantor_contact_number;
    private String vehicle_registration_no;
    private String vehicle_registration_photo;
    private String voter_id_card_no;
    private String voter_id_card_photo;
    private String driving_license_no;
    private String driving_license_photo;
    private String driving_licence_issu_ofc_name;
    private String telephone_number;
    private String mobile_number;
    private String aadhar_card_no;
    private String aadhar_card_photo;
    private String arm_licence_no;
    private String arm_licence_photo;
    private String arm_licence_issu_ofc_name;
    private String passport_no;
    private String passport_photo;
    private String rashan_card_no;
    private String rashan_card_photo;
    private String otherid_no;
    private String otherid_photo;
    private String paymentMode;
    private String paymentOrderId;
    private String paymentTransactionId;
    private String detail_verification;
    private String keytenantid;
    private String Attendancesms;
    private String parent_id;
    private String mainroomamount;
    public String getAttendancesms() {
        return Attendancesms;
    }

    public void setAttendancesms(String attendancesms) {
        Attendancesms = attendancesms;
    }

    private  Bitmap imageBitmap;


    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getMainroomamount() {
        return mainroomamount;
    }

    public void setMainroomamount(String mainroomamount) {
        this.mainroomamount = mainroomamount;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public static PropertyUserModal propertyUserModal;

    public boolean isSelected() {
        return isSelected;
    }


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PropertyUserModal(Parcel in) {


        keytenantid=in.readString();
        propertyId = in.readString();
        status_active = in.readString();
        userId = in.readString();
        ownerId = in.readString();
        userName = in.readString();
        userMobileno = in.readString();
        userAddress = in.readString();
        bookedRoom = in.readString();
        bookedRoomId = in.readString();
        bookedBad = in.readString();
        bookedDate = in.readString();
        leaveDate = in.readString();
        paidAmount = in.readString();
        remainingAmount = in.readString();
        paymentStatus = in.readString();
        roomAmount = in.readString();
        userImage = in.readString();
        transaction_id = in.readString();
        tenant_id = in.readString();
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
        passport_no = in.readString();
        passport_photo = in.readString();
        rashan_card_no = in.readString();
        rashan_card_photo = in.readString();
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
        t_user_id=in.readString();
        mainroomamount=in.readString();
        Attendancesms=in.readString();
    }

    public static final Creator<PropertyUserModal> CREATOR = new Creator<PropertyUserModal>() {
        @Override
        public PropertyUserModal createFromParcel(Parcel in) {
            return new PropertyUserModal(in);
        }

        @Override
        public PropertyUserModal[] newArray(int size) {
            return new PropertyUserModal[size];
        }
    };

    public String getKeytenantid() {
        return keytenantid;
    }

    public void setKeytenantid(String keytenantid) {
        this.keytenantid = keytenantid;
    }

    public String getStatus_active() {
        return status_active;
    }

    public void setStatus_active(String status_active) {
        this.status_active = status_active;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getT_user_id() {
        return t_user_id;
    }

    public void setT_user_id(String t_user_id) {
        this.t_user_id = t_user_id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobileno() {
        return userMobileno;
    }

    public void setUserMobileno(String userMobileno) {
        this.userMobileno = userMobileno;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getBookedRoom() {
        return bookedRoom;
    }

    public void setBookedRoom(String bookedRoom) {
        this.bookedRoom = bookedRoom;
    }

    public String getBookedRoomId() {
        return bookedRoomId;
    }

    public void setBookedRoomId(String bookedRoomId) {
        this.bookedRoomId = bookedRoomId;
    }

    public String getBookedBad() {
        return bookedBad;
    }

    public void setBookedBad(String bookedBad) {
        this.bookedBad = bookedBad;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(String roomAmount) {
        this.roomAmount = roomAmount;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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

    public String getAadhar_card_no() {
        return aadhar_card_no;
    }

    public String getAadhar_card_photo() {
        aadhar_card_photo = aadhar_card_photo.replaceAll(" ", "%20");
        return aadhar_card_photo;
    }

    public String getArm_licence_no() {
        return arm_licence_no;
    }

    public String getArm_licence_photo() {
        arm_licence_photo = arm_licence_photo.replaceAll(" ", "%20");
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
        driving_license_photo = driving_license_photo.replaceAll(" ", "%20");
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
        passport_photo = passport_photo.replaceAll(" ", "%20");
        return passport_photo;
    }

    public String getRashan_card_no() {
        return rashan_card_no;
    }

    public String getRashan_card_photo() {
        rashan_card_photo = rashan_card_photo.replaceAll(" ", "%20");

        return rashan_card_photo;
    }

    public String getOtherid_no() {
        return otherid_no;
    }

    public String getOtherid_photo() {
        otherid_photo = otherid_photo.replaceAll(" ", "%20");
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

        tenant_photo = tenant_photo.replaceAll(" ", "%20");
        return tenant_photo;
    }

    public String getTenant_work_detail() {
        return tenant_work_detail;
    }

    public String getVehicle_registration_no() {
        return vehicle_registration_no;
    }

    public String getVehicle_registration_photo() {
        vehicle_registration_photo = vehicle_registration_photo.replaceAll(" ", "%20");
        return vehicle_registration_photo;
    }

    public String getVoter_id_card_no() {
        return voter_id_card_no;
    }

    public String getVoter_id_card_photo() {

        voter_id_card_photo = voter_id_card_photo.replaceAll(" ", "%20");
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
        dest.writeString(propertyId);
        dest.writeString(status_active);
        dest.writeString(userId);
        dest.writeString(ownerId);
        dest.writeString(userName);
        dest.writeString(userMobileno);
        dest.writeString(userAddress);
        dest.writeString(bookedRoom);
        dest.writeString(bookedRoomId);
        dest.writeString(bookedBad);
        dest.writeString(bookedDate);
        dest.writeString(leaveDate);
        dest.writeString(paidAmount);
        dest.writeString(remainingAmount);
        dest.writeString(paymentStatus);
        dest.writeString(roomAmount);
        dest.writeString(userImage);
        dest.writeString(transaction_id);
        dest.writeString(tenant_id);
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
        dest.writeString(passport_no);
        dest.writeString(passport_photo);
        dest.writeString(rashan_card_no);
        dest.writeString(rashan_card_photo);
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
        dest.writeString(t_user_id);
        dest.writeString(keytenantid);
        dest.writeString(mainroomamount);
        dest.writeString(Attendancesms);
    }
}
