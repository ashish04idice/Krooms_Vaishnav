package com.krooms.hostel.rental.property.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.krooms.hostel.rental.property.app.asynctask.ResendOTPAsyncTask;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.asynctask.OTPVerificationAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.callback.OTPServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OTPVerificationActivity extends FragmentActivity implements OnClickListener, OTPServiceResponce, ServiceResponce {

    private EditText edtCode = null;
    private Button btnResendOtp = null, btnSubmit;
    private String mobileNo = "";
    private String userId = "";
    private Common mCommon = null;
    private FragmentActivity mActivity = null;
    private SharedStorage mSharedStorage = null;
    private LinearLayout linearlayoutLoading = null;
    private Validation mValidation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        mActivity = OTPVerificationActivity.this;
        createViews();
        setListeners();
    }

    private void createViews() {

        mValidation = new Validation(mActivity);
        mobileNo = getIntent().getStringExtra("mobile_no");
        edtCode = (EditText) findViewById(R.id.edittext_otp);
        btnResendOtp = (Button) findViewById(R.id.button_resend_otp);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        linearlayoutLoading = (LinearLayout) findViewById(R.id.linearlayout_loading);
        linearlayoutLoading.setVisibility(View.GONE);
    }

    private void setListeners() {

        mSharedStorage = SharedStorage.getInstance(mActivity);
        userId = mSharedStorage.getUserId();
        mCommon = new Common();
        btnResendOtp.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_title:
                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                break;
            case R.id.button_resend_otp:
                btnClick_ResendOTP();
                break;
            case R.id.btn_submit:
                btnClick_OTPVerification();
                break;
            default:
                break;
        }
    }

    private void btnClick_ResendOTP() {

        mCommon.hideKeybord(mActivity, edtCode);
        if (!mValidation.checkNetworkRechability()) {
            mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
        } else {
            ScrollView sv = (ScrollView) findViewById(R.id.scrollview_layout);
            sv.scrollTo(0, sv.getBottom());
            linearlayoutLoading.setVisibility(View.VISIBLE);
            ResendOTPAsyncTask mResendOTPAsyncTask = new ResendOTPAsyncTask(mActivity);
            mResendOTPAsyncTask.setCallBack(this);
            mResendOTPAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mobileNo);
        }
    }

    private void btnClick_OTPVerification() {

        if (edtCode.length() == 0) {
            mCommon.displayAlert(this, "Please enter one time password.", false);
        } else {
            mCommon.hideKeybord(mActivity, edtCode);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
            } else {
                linearlayoutLoading.setVisibility(View.VISIBLE);
                ScrollView sv = (ScrollView) findViewById(R.id.scrollview_layout);
                sv.scrollTo(0, sv.getBottom());
                OTPVerificationAsyncTask mOTPVerificationAsyncTask = new OTPVerificationAsyncTask(mActivity);
                mOTPVerificationAsyncTask.setCallBack(this);
                mOTPVerificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        userId, edtCode.getText().toString().trim());

            }
        }
    }

    @Override
    public void requestOTPServiceResponce(String results) {
        linearlayoutLoading.setVisibility(View.GONE);
        LogConfig.logd("OTP verification response =", "" + results);


        if (results != null && results.length() > 0) {
            if (results != null && results.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(mActivity, Common.TimeOut_Message, false);
            } else {
                try {

                    JSONObject jsonObject = new JSONObject(results);
                    String status = jsonObject.getString(WebUrls.STATUS_JSON);
                    if (status.equals("true")) {
                        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
                        mSharedStorage.clearUserData();
                        JSONArray data = jsonObject.getJSONArray(com.krooms.hostel.rental.property.app.util.WebUrls.DATA_JSON);
                        mSharedStorage.setUserId(data.getJSONObject(0).getString("user_id"));
                        mSharedStorage.setUserEmail(data.getJSONObject(0).getString("email"));
                        mSharedStorage.setUserMobileNumber(data.getJSONObject(0).getString("mobile"));
                        mSharedStorage.setUserFirstName(data.getJSONObject(0).getString("first_name"));
                        mSharedStorage.setUserLastName(data.getJSONObject(0).getString("last_name"));

                        int countvalue = data.getJSONObject(0).getInt("count");
                        int usertypevalue = data.getJSONObject(0).getInt("user_type");

                        mSharedStorage.setAddCount("" + data.getJSONObject(0).getInt("count"));
                        mSharedStorage.setUserType("" + data.getJSONObject(0).getInt("user_type"));

                        // Log.e("v=","10");


                        if (mSharedStorage.getUserType().equals("5")) {

                            JSONObject jsonobj = data.getJSONObject(0).getJSONObject("user_info");

                            String userOwnerid = jsonobj.getString("owner_userid");
                            mSharedStorage.setPropertyOwnerId(userOwnerid);
                            mSharedStorage.setUserPropertyId(data.getJSONObject(0).getString("property_id"));
                        } else if (mSharedStorage.getUserType().equals("6")) {


                            JSONObject jsonobj = data.getJSONObject(0).getJSONObject("user_info");

                            String wardenuserid = jsonobj.getString("oid");
                            String userOwnerid = jsonobj.getString("owner_userid");
                            String username = jsonobj.getString("fullname");
                            // String lastname=jsonobj.getString("last_name");
                            String main_name = username;
                            mSharedStorage.setUserName(main_name);
                            mSharedStorage.setWardenuserId(wardenuserid);
                            mSharedStorage.setPropertyOwnerId(userOwnerid);
                            mSharedStorage.setUserPropertyId(data.getJSONObject(0).getString("property_id"));

                        } else if (mSharedStorage.getUserType().equals("2")) {
                            mSharedStorage.setUserPropertyId("");
                            mSharedStorage.setBookedPropertyId(data.getJSONObject(0).getString("property_id"));

                            JSONObject jOjectInfo = data.getJSONObject(0).getJSONObject("user_info");
                            if (jOjectInfo.length() > 5) {
                                if (jOjectInfo.getString("gender") != null && !jOjectInfo.getString("gender").equals("null")) {
                                    mSharedStorage.setGender(jOjectInfo.getString("gender"));
                                }
                                if (jOjectInfo.getString("address") != null && !jOjectInfo.getString("address").equals("null")) {
                                    mSharedStorage.setUserAddress(jOjectInfo.getString("address"));
                                }
                                if (jOjectInfo.getString("identity") != null && !jOjectInfo.getString("identity").equals("null")) {
                                    mSharedStorage.setIdType(jOjectInfo.getString("identity"));
                                }
                                if (jOjectInfo.getString("identity_number") != null && !jOjectInfo.getString("identity_number").equals("null")) {
                                    mSharedStorage.setIdNumber(jOjectInfo.getString("identity_number"));
                                }
                                if (jOjectInfo.getString("identity_pic") != null && !jOjectInfo.getString("identity_pic").equals("null")) {
                                    mSharedStorage.setIdentityImage(jOjectInfo.getString("identity_pic"));
                                }
                                /*if(jOjectInfo.getString("father_name") != null && !jOjectInfo.getString("father_name").equals("null")) {
                                    mSharedStorage.setUserFatherName(jOjectInfo.getString("father_name"));
                                }*/

                                if (data.getJSONObject(0).has("father_name")) {
                                    mSharedStorage.setUserFatherName(jOjectInfo.getString("father_name"));
                                }
                                if (jOjectInfo.getString("mother_name") != null && !jOjectInfo.getString("mother_name").equals("null")) {
                                    mSharedStorage.setUserMothersName(jOjectInfo.getString("mother_name"));
                                }
                                if (jOjectInfo.getString("parent_email") != null && !jOjectInfo.getString("parent_email").equals("null")) {
                                    mSharedStorage.setUserParentEmailAddress(jOjectInfo.getString("parent_email"));
                                }

                                if (jOjectInfo.getString("parent_contact") != null && !jOjectInfo.getString("parent_contact").equals("null")) {
                                    mSharedStorage.setUserParentMobileNumber(jOjectInfo.getString("parent_contact"));
                                }

                                if (jOjectInfo.getString("parent_address") != null && !jOjectInfo.getString("parent_address").equals("null")) {
                                    mSharedStorage.setUserParentFullAddress(jOjectInfo.getString("parent_address"));
                                }

                                if (jOjectInfo.getString("guardian_name") != null && !jOjectInfo.getString("guardian_name").equals("null")) {
                                    mSharedStorage.setUserGurdianName(jOjectInfo.getString("guardian_name"));
                                }
                                if (jOjectInfo.getString("relation") != null && !jOjectInfo.getString("relation").equals("null")) {
                                    mSharedStorage.setUserGurdianRelation(jOjectInfo.getString("relation"));
                                }
                                if (jOjectInfo.getString("guardian_email") != null && !jOjectInfo.getString("guardian_email").equals("null")) {
                                    mSharedStorage.setUserGurdianEmail(jOjectInfo.getString("guardian_email"));
                                }
                                if (jOjectInfo.getString("guardian_contact") != null && !jOjectInfo.getString("guardian_contact").equals("null")) {
                                    mSharedStorage.setUserGurdianContact(jOjectInfo.getString("guardian_contact"));
                                }
                                if (jOjectInfo.getString("guardian_address") != null && !jOjectInfo.getString("guardian_address").equals("null")) {
                                    mSharedStorage.setUserGurdianFullAddress(jOjectInfo.getString("guardian_address"));
                                }
                                if (jOjectInfo.getString("photo") != null && !jOjectInfo.getString("photo").equals("null")) {
                                    mSharedStorage.setUserImage(jOjectInfo.getString("photo"));
                                }
                            }
                        } else if (mSharedStorage.getUserType().equals("3")) {
                            mSharedStorage.setBookedPropertyId("");
                            mSharedStorage.setUserPropertyId(data.getJSONObject(0).getString("property_id"));
                            JSONObject jOjectInfo = data.getJSONObject(0).getJSONObject("user_info");
                            if (jOjectInfo.length() > 5) {
                                if (jOjectInfo.getString("address") != null && !jOjectInfo.getString("address").equals("null")) {
                                    mSharedStorage.setUserAddress(jOjectInfo.getString("address"));
                                }
                                if (jOjectInfo.getString("landmark") != null && !jOjectInfo.getString("landmark").equals("null")) {
                                    mSharedStorage.setLandMark(jOjectInfo.getString("landmark"));
                                }
                                if (jOjectInfo.getString("state") != null && !jOjectInfo.getString("state").equals("null")) {
                                    mSharedStorage.setStateId(jOjectInfo.getString("state"));
                                }
                                if (jOjectInfo.getString("city") != null && !jOjectInfo.getString("city").equals("null")) {
                                    mSharedStorage.setCityId(jOjectInfo.getString("city"));
                                }
                                if (jOjectInfo.getString("area") != null && !jOjectInfo.getString("area").equals("null")) {
                                    mSharedStorage.setAreaId(jOjectInfo.getString("area"));
                                }
                                if (jOjectInfo.getString("pincode") != null && !jOjectInfo.getString("pincode").equals("null")) {
                                    mSharedStorage.setUserPincode(jOjectInfo.getString("pincode"));
                                }
                                if (jOjectInfo.getString("profession") != null && !jOjectInfo.getString("profession").equals("null")) {
                                    mSharedStorage.setProfession(jOjectInfo.getString("profession"));
                                }
                                if (jOjectInfo.getString("organization") != null && !jOjectInfo.getString("organization").equals("null")) {
                                    mSharedStorage.setOrganizationName(jOjectInfo.getString("organization"));
                                }
                                if (jOjectInfo.getString("landline") != null && !jOjectInfo.getString("landline").equals("null")) {
                                    mSharedStorage.setLandlineNo(jOjectInfo.getString("landline"));
                                }
                                if (jOjectInfo.getString("owner_pic") != null && !jOjectInfo.getString("owner_pic").equals("null")) {
                                    mSharedStorage.setUserImage(jOjectInfo.getString("owner_pic"));
                                }
                                if (jOjectInfo.getString("father_name") != null && !jOjectInfo.getString("father_name").equals("null")) {
                                    mSharedStorage.setUserFatherName(jOjectInfo.getString("father_name"));
                                }

                                if (jOjectInfo.getString("fname") != null && !jOjectInfo.getString("fname").equals("null")) {
                                    mSharedStorage.setUserFirstName(jOjectInfo.getString("fname"));
                                } else {
                                    mSharedStorage.setUserFirstName("");
                                }
                                if (jOjectInfo.getString("lname") != null && !jOjectInfo.getString("lname").equals("null")) {
                                    mSharedStorage.setUserLastName(jOjectInfo.getString("lname"));
                                } else {
                                    mSharedStorage.setUserLastName("");
                                }

                            }
                        } else if (mSharedStorage.getUserType().equals("4")) {

                            mSharedStorage.setBookedPropertyId(data.getJSONObject(0).getString("property_id"));
                            mSharedStorage.setUserPropertyId("");
                            JSONObject jOjectInfo = data.getJSONObject(0).getJSONObject("user_info");


                        }
                        //end


                        mSharedStorage.setLoginStatus(true);

                        if (LandingActivityWithoutLogin.mActivity != null) {
                            LandingActivityWithoutLogin.mActivity.finish();
                            if (mSharedStorage.getUserType().equals("2")) {
                                Intent it = new Intent(this, HostelListActivity.class);
                                startActivity(it);
                            } else if (mSharedStorage.getUserType().equals("3")) {

                                if (!mSharedStorage.getAddCount().equals("0")) {
                                    Intent it = new Intent(this, HostelListActivity.class);
                                    startActivity(it);

                                } else {
                                    Intent mIntent = new Intent(this, OwnerProfileActivity.class);
                                    mIntent.putExtra("isSkipOption", true);
                                    mIntent.putExtra("flag", "Owner_First_Time");
                                    mIntent.putExtra("property_id", "");
                                    startActivity(mIntent);
                                }
                            } else if (mSharedStorage.getUserType().equals("4")) {

                                String countvaluemain = mSharedStorage.getAddCount();

                                if (mSharedStorage.getAddCount().equals("1")) {
                                    Intent it = new Intent(this, HostelListActivity.class);
                                    startActivity(it);

                                } else {
                                    Intent mIntent = new Intent(this, ParentProfileCurrentWorkingActivity.class);
                                    mIntent.putExtra("isSkipOption", true);
                                    mIntent.putExtra("flag", "Owner_First_Time");
                                    mIntent.putExtra("property_id", "");
                                    startActivity(mIntent);
                                }
                            }
                            //change for user type accountant
                            else if (mSharedStorage.getUserType().equals("5")) {
                                // Intent it = new Intent(this, HostelListActivity.class);
                                Intent it = new Intent(this, Home_Accountantactivity.class);
                                startActivity(it);
                            }//


                            //change for user type Warden
                            else if (mSharedStorage.getUserType().equals("6")) {

                                Intent it = new Intent(this, HostelListActivity.class);
                                startActivity(it);

                            }

                        } else {
                            callSlider();
                        }
                        finish();
                    } else {
                        mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                    }
                } catch (JSONException e) {
                    LogConfig.logd("Mobile varification JSONException =", "" + e.getMessage());
                }
            }
        } else {
            //e.printStackTrace();
            LogConfig.logd("OTP Response =", "" + results);
        }

    }

    public void callSlider() {

        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }

    @Override
    public void requestResponce(String results) {

        linearlayoutLoading.setVisibility(View.GONE);
        LogConfig.logd("Resend response =", "" + results);
        if (results != null && results.length() > 0) {
            if (results != null && results.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(mActivity, Common.TimeOut_Message, false);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(results);
                    String status = jsonObject.getString(WebUrls.STATUS_JSON);
                    if (status.equals("true")) {
                        mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                    } else {
                        mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                    }
                } catch (JSONException e) {
                    LogConfig.logd("Mobile varification JSONException =", "" + e.getMessage());
                }
            }
        } else {
            //e.printStackTrace();
            LogConfig.logd("Resend response =", "" + results);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }
}
