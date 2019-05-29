package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.LoginServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.OTPVerificationAsyncTask;
import com.krooms.hostel.rental.property.app.callback.OTPServiceResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce, OTPServiceResponce {

    private EditText emailTextInput = null;
    private Button submitBtn = null;
    private Button creatAccountBtn = null;
    private Button forgotPasswordBtn = null;
    private Validation mValidation = null;
    private Common mCommon = null;
    public static Activity mActivity = null;
    private LinearLayout linearlayoutLoading = null;
    private TextView loadingMessage = null;
    private String mobileNo = "";
    String refreshedToken;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = LoginActivity.this;
        createView();
        setListners();

    }

    public void createView() {

        emailTextInput = (EditText) findViewById(R.id.edittext_email);
        submitBtn = (Button) findViewById(R.id.btn_confirm_account);
        creatAccountBtn = (Button) findViewById(R.id.button_new_account);
        forgotPasswordBtn = (Button) findViewById(R.id.button_forgot_password);

        TextView txtBackTitle = (TextView) findViewById(R.id.textView_title);
        txtBackTitle.setOnClickListener(this);

        linearlayoutLoading = (LinearLayout) findViewById(R.id.linearlayout_loading);
        linearlayoutLoading.setVisibility(View.GONE);
        loadingMessage = (TextView) findViewById(R.id.textView_loading_message);

    }

    private void setListners() {

        submitBtn.setOnClickListener(this);
        creatAccountBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);

        mCommon = new Common();
        mValidation = new Validation(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_account:
                submitBtnClickEvent();
                break;
            case R.id.button_new_account:
                Intent it = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(it);
                break;
        }
    }

    public void submitBtnClickEvent() {

        mobileNo = emailTextInput.getText().toString().trim();

        if (mobileNo.length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_your_mobile_no_alert), false);
        } else if (mobileNo.length() < 10) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_valid_mobile), false);
        } else {
            mCommon.hideKeybord(this, emailTextInput);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {
                submitBtn.setClickable(false);
                submitBtn.setEnabled(false);
                Common.reciveOTPCode = "";
                linearlayoutLoading.setVisibility(View.VISIBLE);

                loadingMessage.setText("" + getResources().getString(R.string.str_mobile_verify_sms));
                LoginServiceAsyncTask service = new LoginServiceAsyncTask(this);
                service.setCallBack(this);
                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mobileNo);
            }
        }
    }

    @Override
    public void requestResponce(String result) {

        Common.Config("response      " + result);

        try {

            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                linearlayoutLoading.setVisibility(View.GONE);
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
                    JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                    String userId = data.getJSONObject(0).getString("user_id");
                    String otp = data.getJSONObject(0).getString("otp");
                    mSharedStorage.setUserId(userId);
                    mSharedStorage.setUserMobileNumber(mobileNo);
                    callOTPReciver(userId);
                } else {
                    submitBtn.setClickable(true);
                    submitBtn.setEnabled(true);
                    linearlayoutLoading.setVisibility(View.GONE);
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            linearlayoutLoading.setVisibility(View.GONE);
            submitBtn.setClickable(true);
            submitBtn.setEnabled(true);

        } catch (NullPointerException e) {
            e.printStackTrace();
            linearlayoutLoading.setVisibility(View.GONE);
            submitBtn.setClickable(true);
            submitBtn.setEnabled(true);
        }

    }

    private void callOTPReciver(final String userid) {

        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (Common.reciveOTPCode.length() > 0) {
                        callOTPVerification(userid);
                    } else {
                        submitBtn.setEnabled(true);
                        submitBtn.setClickable(true);
                        linearlayoutLoading.setVisibility(View.GONE);
                        Intent mIntent = new Intent(LoginActivity.this, OTPVerificationActivity.class);
                        mIntent.putExtra("mobile_no", mobileNo);
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    }
                }
            }, 30000);

        } catch (Exception e) {
        }
//        callOTPVerification(userid,Otp);
    }

    private void callOTPVerification(String userId) {
        if (!mValidation.checkNetworkRechability()) {
            mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
        } else {
            OTPVerificationAsyncTask mOTPVerificationAsyncTask = new OTPVerificationAsyncTask(mActivity);
            mOTPVerificationAsyncTask.setCallBack(LoginActivity.this);
            mOTPVerificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userId, Common.reciveOTPCode);
        }
    }

    @Override
    public void requestOTPServiceResponce(String results) {

        linearlayoutLoading.setVisibility(View.GONE);

        if (results != null && results.length() > 0) {
            if (results.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(mActivity, Common.TimeOut_Message, false);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(results);
                    String status = jsonObject.getString(WebUrls.STATUS_JSON);
                    if (status.equals("true")) { //"user_type":3,"count":1,"property_id":"17"
                        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
                        mSharedStorage.clearUserData();
                        JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                        mSharedStorage.setUserId(data.getJSONObject(0).getString("user_id"));
                        mSharedStorage.setUserEmail(data.getJSONObject(0).getString("email"));
                        mSharedStorage.setUserMobileNumber(data.getJSONObject(0).getString("mobile"));
                        mSharedStorage.setUserFirstName(data.getJSONObject(0).getString("first_name"));
                        mSharedStorage.setUserLastName(data.getJSONObject(0).getString("last_name"));
                        mSharedStorage.setUserType("" + data.getJSONObject(0).getInt("user_type"));
                        mSharedStorage.setAddCount("" + data.getJSONObject(0).getInt("count"));

                        if (mSharedStorage.getUserType().equals("5")) {

                            JSONObject jsonobj = data.getJSONObject(0).getJSONObject("user_info");

                            mSharedStorage.setUserPropertyId(data.getJSONObject(0).getString("property_id"));
                            String userOwnerid = jsonobj.getString("owner_userid");
                            mSharedStorage.setPropertyOwnerId(userOwnerid);
                            // mSharedStorage.setBookedPropertyId(data.getJSONObject(0).getString("property_id"));

                        } else if (mSharedStorage.getUserType().equals("6")) {

                            JSONObject jsonobj = data.getJSONObject(0).getJSONObject("user_info");

                            String wardenuserid = jsonobj.getString("oid");
                            String userOwnerid = jsonobj.getString("owner_userid");
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
                        } else if (mSharedStorage.getUserType().equals("4")) {

                            mSharedStorage.setBookedPropertyId(data.getJSONObject(0).getString("property_id"));
                            mSharedStorage.setUserPropertyId("");
                            JSONObject jOjectInfo = data.getJSONObject(0).getJSONObject("user_info");


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
                                } else {
                                    System.out.println("     1111    " + jOjectInfo.getString("area"));
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

                                /*if(jOjectInfo.getString("father_name") != null && !jOjectInfo.getString("father_name").equals("null")) {
                                    mSharedStorage.setUserFatherName(jOjectInfo.getString("father_name"));
                                }*/

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
                        }
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
                                if (!mSharedStorage.getAddCount().equals("1")) {
                                    Intent it = new Intent(this, HostelListActivity.class);
                                    startActivity(it);
                                } else {
                                    Intent mIntent = new Intent(this, ParentProfileCurrentWorkingActivity.class);
                                    mIntent.putExtra("isSkipOption", true);
                                    mIntent.putExtra("flag", "Parent_First_Time");
                                    mIntent.putExtra("property_id", "");
                                    startActivity(mIntent);
                                }
                            }

                            //change for user type accountant ashish
                            else if (mSharedStorage.getUserType().equals("5")) {
                                Intent it = new Intent(this, Home_Accountantactivity.class);
                                startActivity(it);
                            }//

                            //for property warden
                            else if (mSharedStorage.getUserType().equals("6")) {
                                Intent it = new Intent(this, HostelListActivity.class);
                                startActivity(it);
                            }//end

                        } else {
                            callSlider();
                        }
                        finish();

                    } else {
                        mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();

                } catch (NullPointerException e) {

                }
            }
        }
    }

    public void callSlider() {

        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(LoginActivity.this);
        // this.finish();
        this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }
}