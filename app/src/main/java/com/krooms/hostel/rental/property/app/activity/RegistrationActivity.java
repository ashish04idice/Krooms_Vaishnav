package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.asynctask.RegisterServiceAsyncTask;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.asynctask.OTPVerificationAsyncTask;
import com.krooms.hostel.rental.property.app.callback.OTPServiceResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce, OTPServiceResponce {

    private EditText nameTextInput = null;
    private EditText emailTextInput = null;
    private EditText mobileTextInput = null;
    private Button submitBtn = null, alareadyAccount;
    private Validation mValidation = null;
    private Common mCommon = null;
    private RadioGroup radioGrp = null;
    private RadioButton userRadioBtn = null;
    private RadioButton propertyManagerRadioBtn = null;
    private RadioButton parentRadioBtn = null;
    public static Activity mActivity = null;
    private LinearLayout linearlayoutLoading = null;
    private TextView loadingMessage = null;
    private String mobileNo = "";
    private LinearLayout termsLayout = null;
    private CheckBox termCheckBox = null;
    private TextView termTectView = null;
    private String user_role_id = "";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mActivity = this;
        createView();
        setListners();
    }

    public void createView() {

        nameTextInput = (EditText) findViewById(R.id.edittext_name);
        emailTextInput = (EditText) findViewById(R.id.edittext_email);
        mobileTextInput = (EditText) findViewById(R.id.edittext_mobile);
        submitBtn = (Button) findViewById(R.id.button_confirm_account);
        alareadyAccount = (Button) findViewById(R.id.button_already_account);
        radioGrp = (RadioGroup) findViewById(R.id.radioGroup);
        userRadioBtn = (RadioButton) findViewById(R.id.person_user);
        parentRadioBtn= (RadioButton) findViewById(R.id.person_parent);

        propertyManagerRadioBtn = (RadioButton) findViewById(R.id.person_propertyManager);
        linearlayoutLoading = (LinearLayout) findViewById(R.id.linearlayout_loading);
        linearlayoutLoading.setVisibility(View.GONE);
        loadingMessage = (TextView) findViewById(R.id.textView_loading_message);
        termsLayout = (LinearLayout) findViewById(R.id.termsAndConditionLayout);
        termCheckBox = (CheckBox) findViewById(R.id.checbox_terms_condition);
        termTectView = (TextView) findViewById(R.id.termsAndConditionTextView);


    }

    private void setListners() {

        mValidation = new Validation(this);
        mCommon = new Common();

      /*  TextView txtBackTitle = (TextView) findViewById(R.id.textView_title);
        txtBackTitle.setOnClickListener(this);*/
        alareadyAccount.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        termTectView.setOnClickListener(this);

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (userRadioBtn.isChecked()) {
                    if (termsLayout.getVisibility() == View.VISIBLE) {
                        termCheckBox.setChecked(false);
                        termsLayout.setVisibility(View.GONE);
                    }

                } else if (propertyManagerRadioBtn.isChecked()) {
                    if (termsLayout.getVisibility() == View.GONE) {
                        termsLayout.setVisibility(View.VISIBLE);
                    }
                }else if(parentRadioBtn.isChecked()){

                    if (termsLayout.getVisibility() == View.GONE) {
                        termsLayout.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_already_account:
                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                break;
            case R.id.button_confirm_account:
                submitBtnClickEvent();
                break;
            case R.id.termsAndConditionTextView:
                TermAndConditionDialog dialog = new TermAndConditionDialog() {
                    @Override
                    public void agreeBtnClicked() {
                        termCheckBox.setChecked(true);
                    }
                };

                dialog.getPerameter((FragmentActivity) mActivity, true);
                dialog.show(this.getSupportFragmentManager(), "termDialog");
                break;
        }

    }


    public void submitBtnClickEvent() {

        if (nameTextInput.getText().toString().trim().length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_your_name_alert), false);
        } else if (emailTextInput.getText().toString().trim().length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_email_alert), false);
        } else if (!mValidation.checkEmail(emailTextInput.getText().toString().trim())) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_valid_email), false);
        } else if (mobileTextInput.getText().toString().trim().length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_your_mobile_no_alert), false);
        } else if (mobileTextInput.getText().toString().trim().length() < 10) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_valid_mobile), false);
        } else if (!userRadioBtn.isChecked() && !propertyManagerRadioBtn.isChecked() && !parentRadioBtn.isChecked()) {
            mCommon.displayAlert(this, "Select user type.", false);
        } else {

            user_role_id = userRadioBtn.isChecked() ? "2" : propertyManagerRadioBtn.isChecked() ? "3" :parentRadioBtn.isChecked() ? "4" : "";
            String name = nameTextInput.getText().toString().trim();
            String email = emailTextInput.getText().toString().trim();
            mobileNo = mobileTextInput.getText().toString().trim();
            mCommon.hideKeybord(this, emailTextInput);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {
                submitBtn.setClickable(false);
                submitBtn.setEnabled(false);
                if (user_role_id.equals("3")) {

                    if(termCheckBox.isChecked()){
                        Common.reciveOTPCode = "";
                        ScrollView sv = (ScrollView) findViewById(R.id.scrollview_layout);
                        sv.scrollTo(0, sv.getBottom());
                        linearlayoutLoading.setVisibility(View.VISIBLE);
                        loadingMessage.setText("" + getResources().getString(R.string.str_mobile_verify_sms) + " " + mobileNo);
                        RegisterServiceAsyncTask service = new RegisterServiceAsyncTask(this);
                        service.setCallBack(this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, name, email, mobileNo, user_role_id);
                    }else{
                        mCommon.displayAlert(mActivity,"In order to use our services, you must agree to Terms and Condition.",false);
                    }

                }
                else if(user_role_id.equals("4")){

                    if(termCheckBox.isChecked()){

                        Common.reciveOTPCode = "";
                        ScrollView sv = (ScrollView) findViewById(R.id.scrollview_layout);
                        sv.scrollTo(0, sv.getBottom());
                        linearlayoutLoading.setVisibility(View.VISIBLE);
                        loadingMessage.setText("" + getResources().getString(R.string.str_mobile_verify_sms) + " " + mobileNo);
                        RegisterServiceAsyncTask service = new RegisterServiceAsyncTask(this);
                        service.setCallBack(this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, name, email, mobileNo, user_role_id);


                    }else{
                        mCommon.displayAlert(mActivity,"In order to use our services, you must agree to Terms and Condition.",false);
                    }

                }
                else {
                    Common.reciveOTPCode = "";
                    ScrollView sv = (ScrollView) findViewById(R.id.scrollview_layout);
                    sv.scrollTo(0, sv.getBottom());
                    linearlayoutLoading.setVisibility(View.VISIBLE);
                    loadingMessage.setText("" + getResources().getString(R.string.str_mobile_verify_sms) + " " + mobileNo);
                    RegisterServiceAsyncTask service = new RegisterServiceAsyncTask(this);
                    service.setCallBack(this);
                    service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, name, email, mobileNo, user_role_id);
                }
            }
        }
    }

    @Override
    public void requestResponce(String result) {

        LogConfig.logd("Signup result", "" + result);
        try {
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
                linearlayoutLoading.setVisibility(View.GONE);
                submitBtn.setClickable(true);
                submitBtn.setEnabled(true);
                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }
        } catch (JSONException e) {
            submitBtn.setClickable(true);
            submitBtn.setEnabled(true);
            linearlayoutLoading.setVisibility(View.GONE);
        } catch (NullPointerException e) {

            submitBtn.setClickable(true);
            submitBtn.setEnabled(true);
            linearlayoutLoading.setVisibility(View.GONE);
        }

    }

    private void callOTPReciver(final String userid) {

        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if(Common.reciveOTPCode.length()>0) {
                        callOTPVerification(userid);
                    } else {
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        linearlayoutLoading.setVisibility(View.GONE);
                        Intent mIntent = new Intent(RegistrationActivity.this, OTPVerificationActivity.class);
                        mIntent.putExtra("mobile_no", mobileNo);
                        startActivity(mIntent);
                        finish();
                        if(mActivity!=null){
                            mActivity.finish();
                        }
                        overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    }
                }

            }, 30000);

        } catch (Exception e) {
        }
    }

    private void callOTPVerification(String userId) {

        if (!mValidation.checkNetworkRechability()) {
            linearlayoutLoading.setVisibility(View.GONE);
            mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
        } else {
            OTPVerificationAsyncTask mOTPVerificationAsyncTask = new OTPVerificationAsyncTask(mActivity);
            mOTPVerificationAsyncTask.setCallBack(this);
            mOTPVerificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userId, Common.reciveOTPCode);
        }
    }

    @Override
    public void requestOTPServiceResponce(String results) {

        linearlayoutLoading.setVisibility(View.GONE);

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
                        JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                        mSharedStorage.setUserId(data.getJSONObject(0).getString("user_id"));
                        mSharedStorage.setUserEmail(data.getJSONObject(0).getString("email"));
                        mSharedStorage.setUserMobileNumber(data.getJSONObject(0).getString("mobile"));
                        mSharedStorage.setUserFirstName(data.getJSONObject(0).getString("first_name"));
                        mSharedStorage.setUserLastName(data.getJSONObject(0).getString("last_name"));
                        mSharedStorage.setUserType("" + data.getJSONObject(0).getInt("user_type"));
                        mSharedStorage.setAddCount("" + data.getJSONObject(0).getInt("count"));

                        if (mSharedStorage.getUserType().equals("2")) {
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
                          } else {
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
                        }
                        mSharedStorage.setLoginStatus(true);

                        if (LandingActivityWithoutLogin.mActivity != null) {
                            LandingActivityWithoutLogin.mActivity.finish();
                            if (mSharedStorage.getUserType().equals("2")) {
                                Intent it = new Intent(this, HostelListActivity.class);
                                startActivity(it);
                            } else if(mSharedStorage.getUserType().equals("3")){
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
                            }
                            else if(mSharedStorage.getUserType().equals("4")){
                                if (!mSharedStorage.getAddCount().equals("0")) {
                                    Intent it = new Intent(this, HostelListActivity.class);
                                    startActivity(it);

                                } else {
                                    Intent mIntent = new Intent(this,ParentProfileActivity.class);
                                    mIntent.putExtra("isSkipOption", true);
                                    mIntent.putExtra("flag", "Owner_First_Time");
                                    mIntent.putExtra("property_id", "");
                                    startActivity(mIntent);
                                }


                            }

                        } else {
                            callSlider();
                        }
                        finish();
                    } else {
                        mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();

                } catch (NullPointerException e) {
                    //e.printStackTrace();

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
        // super.onBackPressed();
       /* AlertDialogWithTwoBtn callbackDialog = new AlertDialogWithTwoBtn() {
            @Override
            public void callBack() {
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
            }
        };
        callbackDialog.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        callbackDialog.show(getSupportFragmentManager(), "Exit ");
*/

        CustomDialogClass VehicleRcId = new CustomDialogClass(RegistrationActivity.this, R.style.full_screen_dialog);
        VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        VehicleRcId.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        VehicleRcId.show();
    }



    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {


        private FragmentActivity mFActivity = null;
        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;

        private String titleStr;
        private String messageStr;




        public CustomDialogClass(RegistrationActivity a,int full_screen_dialog) {
            super(a,full_screen_dialog);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, String messageStr){

            mFActivity = activity;
            this.titleStr = titleStr;
            this.messageStr = messageStr;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.alert_two_btn_dialog);
            title = (TextView) findViewById(R.id.alertTitle);
            message = (TextView) findViewById(R.id.categoryNameInput);
            alertYesBtn = (Button) findViewById(R.id.alertYesBtn);
            alertNoBtn = (Button) findViewById(R.id.alertNoBtn);

            title.setText(titleStr);
            message.setText(messageStr);

            alertYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    dismiss();
                }
            });

            alertNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });



        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
            }
            dismiss();
        }


    }

}