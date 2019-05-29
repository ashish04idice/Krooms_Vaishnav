package com.krooms.hostel.rental.property.app.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedStorage {

    private static SharedPreferences sharedPreferences = null;
    private static SharedStorage instance;

    public SharedStorage(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedStorage getInstance(Context context) {
        if (instance == null) {
            instance = new SharedStorage(context);
        }
        return instance;
    }

    public void setLoginStatus(boolean status) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginStatus", status);
        editor.commit();
    }

    public boolean getLoginStatus() {
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    public String getPushRegistrationID() {
        return sharedPreferences.getString("push_id", "Not Available");
    }

    public void setParent_Id(String parent_id) {
        Editor editor = sharedPreferences.edit();
        editor.putString("Parent_id",parent_id);
        editor.commit();
    }

    public String getParent_Id() {
        return sharedPreferences.getString("Parent_id","");
    }



    public void setUsertype_Id(String User_type) {
        Editor editor = sharedPreferences.edit();
        editor.putString("User_type",User_type);
        editor.commit();
    }

    public String getUsertype_Id() {
        return sharedPreferences.getString("User_type","");
    }



    public void setPushRegistrationID(String regID) {
        Editor editor = sharedPreferences.edit();
        editor.putString("push_id", regID);
        editor.commit();
    }


    public int getApplicationVersion() {
        return sharedPreferences.getInt("appVersion", Integer.MIN_VALUE);
    }

    public void setApplicationVersion(int versionNo) {
        Editor editor = sharedPreferences.edit();
        editor.putInt("appVersion", versionNo);
        editor.commit();
    }

    public void setUserId(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userId);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", "");
    }

    public void setUserPropertyId(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("user_property_id", userId);
        editor.commit();
    }

    public String getUserPropertyId() {
        return sharedPreferences.getString("user_property_id", "");
    }

    public String getUserType() {
        return sharedPreferences.getString("user_type", "");
    }

    public void setUserType(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("user_type", userId);
        editor.commit();
    }

    public String getAddCount() {
        return sharedPreferences.getString("AddCount", "");
    }

    public void setAddCount(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("AddCount", userId);
        editor.commit();
    }


    public void setUserName(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("user_name", userId);
        editor.commit();
    }

    public String getUserName() {
        return sharedPreferences.getString("user_name", "");
    }

    public void setUserFirstName(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("first_name", userId);
        editor.commit();
    }

    public String getUserFirstName() {
        return sharedPreferences.getString("first_name", "");
    }
    public String getHireDate() {
        return sharedPreferences.getString("hire_date", "");
    }

    public void setHireDate(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("hire_date", userId);
        editor.commit();
    }


    public String getLeaveDate() {
        return sharedPreferences.getString("leave_date", "");
    }

    public void setLeaveDate(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("leave_date", userId);
        editor.commit();
    }

    public void setUserLastName(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("last_name", userId);
        editor.commit();
    }

    public String getUserLastName() {
        return sharedPreferences.getString("last_name", "");
    }


    public void setUserMobileNumber(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("mobile_number", userId);
        editor.commit();
    }

    public String getUserMobileNumber() {
        return sharedPreferences.getString("mobile_number", "");
    }

    public void setUserEmail(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("email", userId);
        editor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString("email", "");
    }

    public void setLoginAlert(String storageType) {
        Editor editor = sharedPreferences.edit();
        editor.putString("Login_Alert", storageType);
        editor.commit();
    }

    public String getLoginAlert() {
        return sharedPreferences.getString("Login_Alert", "");
    }

    public void setUserImage(String storageType) {
        Editor editor = sharedPreferences.edit();
        editor.putString("User_Image", storageType);
        editor.commit();
    }

    public String getUserImage() {
        return sharedPreferences.getString("User_Image", "");
    }

    public void setUserAddress(String str) {
        Editor editor = sharedPreferences.edit();
        editor.putString("User_Address", str);
        editor.commit();
    }

    public String getUserAddress() {
        return sharedPreferences.getString("User_Address", "");
    }

    public void setGender(String str) {
        Editor editor = sharedPreferences.edit();
        editor.putString("gender", str);
        editor.commit();
    }

    public String getGender() {
        return sharedPreferences.getString("gender", "");
    }


    public void setBookedPropertyId(String property_id) {
        Editor edit = sharedPreferences.edit();
        edit.putString("booked_property_id", property_id);
        edit.commit();
    }

    public String getBookedPropertyId() {
        return sharedPreferences.getString("booked_property_id", "");
    }

    public void setOTPCode(String otp_code) {
        Editor edit = sharedPreferences.edit();
        edit.putString("otpCode", otp_code);
        edit.commit();
    }

    public String getOTPCode() {
        return sharedPreferences.getString("otpCode", "");
    }

    public void setLandlineNo(String landlineNo) {
        Editor edit = sharedPreferences.edit();
        edit.putString("landlineNo", landlineNo);
        edit.commit();
    }

    //////////////////////////////// owner extra profile information //////////////////////////////////////////////

    public String getLandlineNo() {
        return sharedPreferences.getString("landlineNo", "");
    }

    public void setOrganizationName(String organizationName) {
        Editor edit = sharedPreferences.edit();
        edit.putString("organizationName", organizationName);
        edit.commit();
    }

    public String getOrganizationName() {
        return sharedPreferences.getString("organizationName", "");
    }

    public void setLandMark(String landMark) {
        Editor edit = sharedPreferences.edit();
        edit.putString("landMark", landMark);
        edit.commit();
    }

    public String getLandMark() {
        return sharedPreferences.getString("landMark", "");
    }

    public void setUserPincode(String userPincode) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userPincode", userPincode);
        edit.commit();
    }





    public void setWardenuserId(String userId) {
        Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userId);
        editor.commit();
    }


    public String getWardenuserId() {
        return sharedPreferences.getString("user_id", "");
    }



    public void setPropertyOwnerId(String ownerid) {
        Editor editor = sharedPreferences.edit();
        editor.putString("owner_id", ownerid);
        editor.commit();
    }


    public String getPropertyOwnerId() {
        return sharedPreferences.getString("owner_id", "");
    }

    public String getUserPincode() {
        return sharedPreferences.getString("userPincode", "");
    }


    public void setProfession(String profession) {
        Editor edit = sharedPreferences.edit();
        edit.putString("profession", profession);
        edit.commit();
    }

    public String getProfession() {
        return sharedPreferences.getString("profession", "");
    }

    public void setStateId(String stateId) {
        Editor edit = sharedPreferences.edit();
        edit.putString("stateId", stateId);
        edit.commit();
    }

    public String getStateId() {
        return sharedPreferences.getString("stateId", "0");
    }

    public void setCityId(String cityId) {
        Editor edit = sharedPreferences.edit();
        edit.putString("cityId", cityId);
        edit.commit();
    }

    public String getCityId() {
        return sharedPreferences.getString("cityId", "0");
    }

    public void setAreaId(String areaId) {
        Editor edit = sharedPreferences.edit();
        edit.putString("areaId", areaId);
        edit.commit();
    }

    public String getAreaId() {
        return sharedPreferences.getString("areaId", "0");
    }

    //////////////////////////////// owner extra profile information //////////////////////////////////////////////

    public void clearUserData() {

        setUserId("");
        setUserEmail("");
        setUserFirstName("");
        setUserLastName("");
        setUserMobileNumber("");
        setUserImage("");
        setUserName("");
        setLoginAlert("");
        setOTPCode("");
        setUserType("");
        setUserPropertyId("");
        setUserType("");
        setAddCount("");
        setLoginStatus(false);
        setLandlineNo("");
        setOrganizationName("");
        setUserPincode("");
        setLandMark("");
        setUserAddress("");
        setStateId("");
        setCityId("");
        setAreaId("");
        setProfession("");

        setIdType("");
        setIdNumber("");
        setUserFatherName("");
        setUserMothersName("");
        setUserParentEmailAddress("");
        setUserParentMobileNumber("");
        setUserParentFullAddress("");
        setUserGurdianName("");
        setUserGurdianRelation("");
        setUserGurdianEmail("");
        setUserGurdianContact("");
        setUserGurdianFullAddress("");
        setIdentityImage("");
        setGender("");
    }

    //////////////////////////////////////////// user extra profile info //////////////////////////////////////////////
    public void setIdType(String idType) {
        Editor edit = sharedPreferences.edit();
        edit.putString("idType", idType);
        edit.commit();
    }

    public String getIdType() {
        return sharedPreferences.getString("idType", "");
    }

    public void setIdNumber(String idNumber) {
        Editor edit = sharedPreferences.edit();
        edit.putString("idNumber", idNumber);
        edit.commit();
    }

    public String getIdentityImage() {
        return sharedPreferences.getString("identityImage", "");
    }

    public void setIdentityImage(String identityImage) {
        Editor edit = sharedPreferences.edit();
        edit.putString("identityImage", identityImage);
        edit.commit();
    }

    public String getIdNumber() {
        return sharedPreferences.getString("idNumber", "");
    }

    public void setUserFatherName(String userFatherName) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userFatherName", userFatherName);
        edit.commit();
    }

    public String getUserFatherName() {
        return sharedPreferences.getString("userFatherName", "");
    }

    public void setUserMothersName(String userMothersName) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userMothersName", userMothersName);
        edit.commit();
    }

    public String getUserMothersName() {
        return sharedPreferences.getString("userMothersName", "");
    }

    public void setUserParentEmailAddress(String userParentEmailAddress) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userParentEmailAddress", userParentEmailAddress);
        edit.commit();
    }

    public String getUserParentEmailAddress() {
        return sharedPreferences.getString("userParentEmailAddress", "");
    }

    public void setUserParentMobileNumber(String userParentMobileNumber) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userParentMobileNumber", userParentMobileNumber);
        edit.commit();
    }

    public String getUserParentMobileNumber() {
        return sharedPreferences.getString("userParentMobileNumber", "");
    }

    public void setUserParentFullAddress(String userParentFullAddress) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userParentFullAddress", userParentFullAddress);
        edit.commit();
    }

    public String getUserParentFullAddress() {
        return sharedPreferences.getString("userParentFullAddress", "");
    }

    public void setUserGurdianName(String userGurdianName) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userGurdianName", userGurdianName);
        edit.commit();
    }

    public String getUserGurdianName() {
        return sharedPreferences.getString("userGurdianName", "");
    }

    public void setUserGurdianRelation(String userGurdianRelation) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userGurdianRelation", userGurdianRelation);
        edit.commit();
    }

    public String getUserGurdianRelation() {
        return sharedPreferences.getString("userGurdianRelation", "");
    }

    public void setUserGurdianEmail(String userGurdianEmail) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userGurdianEmail", userGurdianEmail);
        edit.commit();
    }

    public String getUserUserGurdianEmail() {
        return sharedPreferences.getString("userGurdianEmail", "");
    }

    public void setUserGurdianContact(String userGurdianContact) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userGurdianContact", userGurdianContact);
        edit.commit();
    }

    public String getUserGurdianContact() {
        return sharedPreferences.getString("userGurdianContact", "");
    }

    public void setPropertyName(String userId) {
        Editor edit = sharedPreferences.edit();
        edit.putString("property_name", userId);
        edit.commit();
    }

    public String getPropertyname() {
        return sharedPreferences.getString("property_name", "");
    }
    public void setUserGurdianFullAddress(String userGurdianFullAddress) {
        Editor edit = sharedPreferences.edit();
        edit.putString("userGurdianFullAddress", userGurdianFullAddress);
        edit.commit();
    }

    public String getUserGurdianFullAddress() {
        return sharedPreferences.getString("userGurdianFullAddress", "");
    }


    public void setPropertyImageUploadInProccess(Boolean flag){

        Editor edit = sharedPreferences.edit();
        edit.putBoolean("PropertyImageUploadInProccess", flag);
        edit.commit();

    }

    public Boolean getPropertyImageUploadInProccess() {
        return sharedPreferences.getBoolean("PropertyImageUploadInProccess", false);
    }

    public void setRoomInfoUploadInProccess(Boolean flag){

        Editor edit = sharedPreferences.edit();
        edit.putBoolean("RoomInfoUploadInProccess", flag);
        edit.commit();

    }

    public Boolean getRoomInfoUploadInProccess() {
        return sharedPreferences.getBoolean("RoomInfoUploadInProccess", false);
    }

    public void setRCUImageUploadInProccess(Boolean flag){

        Editor edit = sharedPreferences.edit();
        edit.putBoolean("RCUImageUploadInProccess", flag);
        edit.commit();

    }

    public Boolean getRCUImageUploadInProccess() {
        return sharedPreferences.getBoolean("RCUImageUploadInProccess", false);
    }




    //////////////////////////////////////////// user extra profile info //////////////////////////////////////////////
}
