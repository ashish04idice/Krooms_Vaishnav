package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.asynctask.GetTenantInformationServiceAsyncTask;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.asynctask.GetOwnerDetailInfoAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.RCUInformationAsynctask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PropertyRCUActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce {

    private EditText edtTenantFormno = null;
    private EditText edtPropertyId = null;
    private EditText edtPropertyOwnerid = null;
    private EditText edtFirstName = null;
    private EditText edtLastName = null;
    private EditText edtFatherName = null;
    private EditText edtPermanentAddress = null;
    private EditText edtContactNo = null;
    private EditText edtEmail = null;
    private ProgressBar mprogressBar;
    private Common mCommon;
    private Validation mValidation;
    private SharedStorage mSharedStorage;

    private TextView txtBack;
    private Button btnSubmit = null;
    private String property_id;
    private String transaction_id;
    private String room_id;

    private boolean isUpdate = false;
    private boolean getTenantData = false;

    private int noOfTenant = 0;
    String usrtypevalue,parentidvalue="0",useridvalue;
     Intent in;
    String room_id_value,propertyid_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_rcu);
        mValidation = new Validation(this);
        mCommon = new Common();

        //permiss check......

        isUpdate = getIntent().getExtras().getBoolean("isUpDate", false);
        getTenantData = getIntent().getExtras().getBoolean("setTenant", false);
        if (Common.rcuDetails != null) {
            Common.rcuDetails.clear();
        }
        createViews();
        setListeners();
    }

    private void createViews() {

        edtTenantFormno = (EditText) findViewById(R.id.edittext_tenant_formno);
        edtPropertyId = (EditText) findViewById(R.id.edittext_property_id);
        edtPropertyOwnerid = (EditText) findViewById(R.id.edittext_property_ownerid);
        edtFirstName = (EditText) findViewById(R.id.edittext_first_name);
        edtLastName = (EditText) findViewById(R.id.edittext_last_name);
        edtFatherName = (EditText) findViewById(R.id.edittext_father_name);
        edtPermanentAddress = (EditText) findViewById(R.id.edittext_permanent_address);
        edtContactNo = (EditText) findViewById(R.id.edittext_contact_no);
        edtEmail = (EditText) findViewById(R.id.edittext_email);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);

        btnSubmit = (Button) findViewById(R.id.button_next);
        txtBack = (TextView) findViewById(R.id.textView_title);

        mSharedStorage = SharedStorage.getInstance(this);

        usrtypevalue=mSharedStorage.getUserType();

      //  Toast.makeText(PropertyRCUActivity.this,parentidvalue+"userid"+useridvalue, Toast.LENGTH_SHORT).show();

        if (getIntent().hasExtra("property_id")) {
            property_id = getIntent().getExtras().getString("property_id");
            getPropertyDetail();
        }
        if (getIntent().hasExtra("transaction_id")) {
            transaction_id = getIntent().getExtras().getString("transaction_id");
        }
        if (getIntent().hasExtra("room_id")) {
            room_id = getIntent().getExtras().getString("room_id");

        }
        setDataIfHaving();

    }

    public void setDataIfHaving() {
        if (isUpdate) {

        }
    }

    public void getPropertyDetail() {

        if (!mValidation.checkNetworkRechability()) {
            mCommon.displayAlert(this, this.getResources().getString(R.string.str_no_network), true);
        } else {
            GetOwnerDetailInfoAsyncTask propertyDetailService = new GetOwnerDetailInfoAsyncTask(this);
            propertyDetailService.setCallBack(new ServiceResponce() {
                @Override
                public void requestResponce(String result) {
                    setOwnerInformation(result);
                }
            });
            propertyDetailService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, property_id);
        }

    }

    public void setOwnerInformation(String result) {


        if (result.trim().length() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    JSONArray dataObject = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                    edtPropertyOwnerid.setText(dataObject.getJSONObject(0).getString("owner_id"));
                    edtPermanentAddress.setText(dataObject.getJSONObject(0).getString("owner_address"));
                    edtFatherName.setText(dataObject.getJSONObject(0).getString("father_name"));
                    edtEmail.setText(dataObject.getJSONObject(0).getString("email_id"));
                    edtContactNo.setText(dataObject.getJSONObject(0).getString("mobile"));
                    edtFirstName.setText(dataObject.getJSONObject(0).getString("fname"));
                    edtLastName.setText(dataObject.getJSONObject(0).getString("lname"));
                    edtTenantFormno.setText("1");
                    edtPropertyId.setText(property_id);

                }
            } catch (JSONException e) {
            }
        }
        if (isUpdate) {
            getTenantDetail();
        } else {

        }

    }

    public void getTenantDetail() {


        GetTenantInformationServiceAsyncTask service = new GetTenantInformationServiceAsyncTask(this);
        service.setCallBack(new ServiceResponce() {
            @Override
            public void requestResponce(String result) {
                tenantInfoParse(result);
            }
        });
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), property_id);
    }

    public void tenantInfoParse(String result) {

        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    noOfTenant = jsoArray.length();

                    for (int i = 0; i < jsoArray.length(); i++) {

                        RCUDetailModal rcuDetailModal = new RCUDetailModal(Parcel.obtain());
                        rcuDetailModal.setTenant_id(jsoArray.getJSONObject(i).getString("id"));
                        rcuDetailModal.setTenant_form_no(jsoArray.getJSONObject(i).getString("tenant_form_no"));
                        rcuDetailModal.setTenant_fname(jsoArray.getJSONObject(i).getString("tenant_fname"));
                        rcuDetailModal.setTenant_lname(jsoArray.getJSONObject(i).getString("tenant_lname"));
                        rcuDetailModal.setTenant_father_name(jsoArray.getJSONObject(i).getString("tenant_father_name"));
                        rcuDetailModal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
//                        rcuDetailModal.setTenant_contact_number(jsoArray.getJSONObject(i).getString(""));
                        rcuDetailModal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                        rcuDetailModal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                        rcuDetailModal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                        rcuDetailModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                        rcuDetailModal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                        rcuDetailModal.setAadhar_card_no(jsoArray.getJSONObject(i).getString("aadhar_card_no"));
                        rcuDetailModal.setAadhar_card_photo(jsoArray.getJSONObject(i).getString("aadhar_card_photo"));
                        rcuDetailModal.setArm_licence_no(jsoArray.getJSONObject(i).getString("arm_licence_no"));
                        rcuDetailModal.setArm_licence_photo(jsoArray.getJSONObject(i).getString("arm_licence_photo"));
                        rcuDetailModal.setVehicle_registration_no(jsoArray.getJSONObject(i).getString("vehicle_registration_no"));
                        rcuDetailModal.setVehicle_registration_photo(jsoArray.getJSONObject(i).getString("vehicle_registration_photo"));
                        rcuDetailModal.setVoter_id_card_no(jsoArray.getJSONObject(i).getString("voter_id_card_no"));
                        rcuDetailModal.setVoter_id_card_photo(jsoArray.getJSONObject(i).getString("voter_id_card_photo"));
                        rcuDetailModal.setDriving_license_no(jsoArray.getJSONObject(i).getString("driving_license_no"));
                        rcuDetailModal.setDriving_license_photo(jsoArray.getJSONObject(i).getString("driving_license_photo"));
                        rcuDetailModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                        rcuDetailModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                        rcuDetailModal.setGuarantor_name(jsoArray.getJSONObject(i).getString("guarantor_name"));
                        rcuDetailModal.setGuarantor_address(jsoArray.getJSONObject(i).getString("guarantor_address"));
                        rcuDetailModal.setGuarantor_contact_number(jsoArray.getJSONObject(i).getString("guarantor_contact_number"));
                        rcuDetailModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                        rcuDetailModal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                        Common.rcuDetails.add(rcuDetailModal);
                    }

                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }

    private void setListeners() {


        mValidation = new Validation(this);
        txtBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this, v);
        switch (v.getId()) {
            case R.id.textView_title:

               // mCommon.AlertDialogWithTwoBtn(PropertyRCUActivity.this, "If you back from these activity so your payment will be Loss", false);
                backpreedialog();
                break;
            case R.id.button_next:
                submitBtnClickEvent();
                break;
        }
    }

    public void submitBtnClickEvent() {

        SharedStorage mSharedStorage = SharedStorage.getInstance(this);

        String tenantNo = edtTenantFormno.getText().toString().trim();
        String propertyId = edtPropertyId.getText().toString().trim();
        String OwnerId = edtPropertyOwnerid.getText().toString().trim();
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String fatherName = edtFatherName.getText().toString().trim();
        String address = edtPermanentAddress.getText().toString().trim();
        String contactNo = edtContactNo.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (tenantNo.length() == 0) {
            mCommon.displayAlert(this, "Enter tenanto number.", false);
        } else if (propertyId.length() == 0) {
            mCommon.displayAlert(this, "Enter property id.", false);
        } else if (OwnerId.length() == 0) {
            mCommon.displayAlert(this, "Enter property owner id.", false);
        } else if (contactNo.length() > 0 && contactNo.length() < 10) {
            mCommon.displayAlert(this, "Enter valid contact number.", false);
        } else if (email.length() > 0 && !mValidation.checkEmail(email)) {
            mCommon.displayAlert(this, "Enter valid email id.", false);
        } else {
            mCommon.hideKeybord(this, edtTenantFormno);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {
                mprogressBar.setVisibility(View.VISIBLE);

                String usertypevalue=usrtypevalue;
                if(usertypevalue.equals("4"))
                {
                    useridvalue=mSharedStorage.getUserId();
                    parentidvalue=mSharedStorage.getParent_Id();
                }else
                {
                    parentidvalue="0";
                }

                RCUInformationAsynctask service = new RCUInformationAsynctask(this);
                service.setCallBack(this);
                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), OwnerId,
                        propertyId, room_id, transaction_id, tenantNo, firstName, lastName,
                        fatherName, address, contactNo, email,parentidvalue,""+Common.selectedBedInfo.size());
            }

        }
    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        LogConfig.logd("Rcu response =", "" + result);
        Common.Config("Rcu response =" + result);
        try {
            if(result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            }else{

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    if (getTenantData) {
                        finish();
                        overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    } else {
                        JSONArray jsoArray = jsonObject.getJSONArray("data");
                        JSONArray rcuIdArray = jsoArray.getJSONObject(0).getJSONArray("rcu_id");
                        String rcuId = "";
                        for (int i = 0; i < rcuIdArray.length(); i++) {
                            rcuId = rcuId + rcuIdArray.getString(i) + ",";
                        }
                        JSONArray tenentDetail = jsoArray.getJSONObject(0).getJSONArray("result_response_array");

                        for (int i = 0; i < tenentDetail.length(); i++) {
                            RCUDetailModal rcuDetailModal = new RCUDetailModal(Parcel.obtain());
                            rcuDetailModal.setTenant_id(tenentDetail.getJSONObject(i).getString("id"));
                            rcuDetailModal.setTenant_form_no(tenentDetail.getJSONObject(i).getString("tenant_form_no"));
                            rcuDetailModal.setTenant_fname(tenentDetail.getJSONObject(i).getString("tenant_fname"));
                            rcuDetailModal.setTenant_lname(tenentDetail.getJSONObject(i).getString("tenant_lname"));
                            rcuDetailModal.setTenant_father_name(tenentDetail.getJSONObject(i).getString("tenant_father_name"));
                            rcuDetailModal.setLandmark(tenentDetail.getJSONObject(i).getString("landmark"));
                            rcuDetailModal.setTenant_father_contact_no(tenentDetail.getJSONObject(i).getString("tenant_father_contact_no"));
                            rcuDetailModal.setFlat_number(tenentDetail.getJSONObject(i).getString("flat_number"));
                            rcuDetailModal.setTenant_contact_number(tenentDetail.getJSONObject(i).getString("tenant_contact_number"));
                            rcuDetailModal.setTenant_work_detail(tenentDetail.getJSONObject(i).getString("tenant_work_detail"));
                            rcuDetailModal.setTenant_detail(tenentDetail.getJSONObject(i).getString("tenant_detail"));
                            rcuDetailModal.setTenant_office_institute(tenentDetail.getJSONObject(i).getString("tenant_office_institute"));
                            rcuDetailModal.setTenant_permanent_address(tenentDetail.getJSONObject(i).getString("tenant_permanent_address"));
                            rcuDetailModal.setAadhar_card_no(tenentDetail.getJSONObject(i).getString("aadhar_card_no"));
                            rcuDetailModal.setAadhar_card_photo(tenentDetail.getJSONObject(i).getString("aadhar_card_photo"));
                            rcuDetailModal.setArm_licence_no(tenentDetail.getJSONObject(i).getString("arm_licence_no"));
                            rcuDetailModal.setArm_licence_photo(tenentDetail.getJSONObject(i).getString("arm_licence_photo"));
                            rcuDetailModal.setVehicle_registration_no(tenentDetail.getJSONObject(i).getString("vehicle_registration_no"));
                            rcuDetailModal.setVehicle_registration_photo(tenentDetail.getJSONObject(i).getString("vehicle_registration_photo"));
                            rcuDetailModal.setVoter_id_card_no(tenentDetail.getJSONObject(i).getString("voter_id_card_no"));
                            rcuDetailModal.setVoter_id_card_photo(tenentDetail.getJSONObject(i).getString("voter_id_card_photo"));
                            rcuDetailModal.setDriving_license_no(tenentDetail.getJSONObject(i).getString("driving_license_no"));
                            rcuDetailModal.setDriving_license_photo(tenentDetail.getJSONObject(i).getString("driving_license_photo"));

                            rcuDetailModal.setGuarantor_name(tenentDetail.getJSONObject(i).getString("guarantor_name"));
                            rcuDetailModal.setGuarantor_address(tenentDetail.getJSONObject(i).getString("guarantor_address"));
                            rcuDetailModal.setGuarantor_contact_number(tenentDetail.getJSONObject(i).getString("guarantor_contact_number"));
                            rcuDetailModal.setState(tenentDetail.getJSONObject(i).getString("state"));
                            rcuDetailModal.setCity(tenentDetail.getJSONObject(i).getString("city"));
                            rcuDetailModal.setLocation(tenentDetail.getJSONObject(i).getString("location"));
                            rcuDetailModal.setTelephone_number(tenentDetail.getJSONObject(i).getString("telephone_number"));
                            rcuDetailModal.setMobile_number(tenentDetail.getJSONObject(i).getString("mobile_number"));
                            rcuDetailModal.setPincode(tenentDetail.getJSONObject(i).getString("pincode"));
                            rcuDetailModal.setContact_number(tenentDetail.getJSONObject(i).getString("contact_number"));
                            rcuDetailModal.setEmail_id(tenentDetail.getJSONObject(i).getString("email_id"));
                            rcuDetailModal.setProperty_hire_date(tenentDetail.getJSONObject(i).getString("property_hire_date"));
                            rcuDetailModal.setProperty_leave_date(tenentDetail.getJSONObject(i).getString("property_leave_date"));
                            rcuDetailModal.setTenant_photo(tenentDetail.getJSONObject(i).getString("tenant_photo"));
                            rcuDetailModal.setDriving_licence_issu_ofc_name(tenentDetail.getJSONObject(i).getString("driving_license_issue_office"));
                            rcuDetailModal.setArm_licence_issu_ofc_name(tenentDetail.getJSONObject(i).getString("arm_name_issue_office"));
                            rcuDetailModal.setPassport_no(tenentDetail.getJSONObject(i).getString("passport_no"));
                            rcuDetailModal.setPassport_photo(tenentDetail.getJSONObject(i).getString("passport_photo"));
                            rcuDetailModal.setRashan_card_no(tenentDetail.getJSONObject(i).getString("rashan_card_no"));
                            rcuDetailModal.setRashan_card_photo(tenentDetail.getJSONObject(i).getString("rashan_card_photo"));
                            rcuDetailModal.setOtherid_no(tenentDetail.getJSONObject(i).getString("other_identity_card"));
                            rcuDetailModal.setOtherid_photo(tenentDetail.getJSONObject(i).getString("other_identity_photo"));
                            rcuDetailModal.setDetail_verification(tenentDetail.getJSONObject(i).getString("other_verification"));
                            /*rcuDetailModal.setTenant_room_no(tenentDetail.getJSONObject(i).getString("room_no"));
                            rcuDetailModal.setTenant_room_id(tenentDetail.getJSONObject(i).getString("room_id"));*/

                            /*if (jsoArray.getJSONObject(i).has("payment_type")) {
                                rcuDetailModal.setPaymentMode(tenentDetail.getJSONObject(i).getString("payment_type"));
                            }
                            if (jsoArray.getJSONObject(i).has("order_id")) {
                                rcuDetailModal.setPaymentOrderId(tenentDetail.getJSONObject(i).getString("order_id"));
                            }
                            if (jsoArray.getJSONObject(i).has("transition_number")) {
                                rcuDetailModal.setPaymentTransactionId(tenentDetail.getJSONObject(i).getString("transition_number"));
                            }*/

                            Common.rcuDetails.add(rcuDetailModal);
                        }

                        Intent mIntent = new Intent(this,PropertyRCUTenantDetailActivity.class);
                        mIntent.putExtra("RCUId", "" + rcuId);
                        mIntent.putExtra("noOfTenant", noOfTenant);
                        mIntent.putExtra("isUpdate", isUpdate);
                        mIntent.putExtra("property_id", property_id);
                        mIntent.putExtra("room_id", room_id);
                        mIntent.putExtra("transaction_id", transaction_id);
                        mIntent.putExtra("owner_id", edtPropertyOwnerid.getText().toString().trim());
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    }
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        mCommon.hideKeybord(this, txtBack);



       *//* AlertDialogWithTwoBtn callbackDialog = new AlertDialogWithTwoBtn() {
            @Override
            public void callBack() {
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
            }
        };
        callbackDialog.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        callbackDialog.show(getSupportFragmentManager(), "Exit ");*//*
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:  backpreedialog();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Propertyid_Json extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(PropertyRCUActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action","exittenant"));
                nameValuePairs.add(new BasicNameValuePair("room_id","4"));
                nameValuePairs.add(new BasicNameValuePair("transaction_id","14"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message=jsmain.getString("message");
                if (result.equalsIgnoreCase("y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                    }
                }
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!IsError) {
                if (result.equalsIgnoreCase("y")) {

                } else if (result.equalsIgnoreCase("n")) {
                }
            } else {
                Toast.makeText(PropertyRCUActivity.this, "please check network connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


   public void  backpreedialog()
    {
       final Dialog backpressdialog=new Dialog(PropertyRCUActivity.this);
        backpressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        backpressdialog.setContentView(R.layout.alert_two_btn_dialog);
        backpressdialog.setCancelable(false);
        TextView title = (TextView)backpressdialog.findViewById(R.id.alertTitle);
       TextView message = (TextView)backpressdialog.findViewById(R.id.categoryNameInput);
        Button alertYesBtn = (Button)backpressdialog.findViewById(R.id.alertYesBtn);
        Button alertNoBtn  = (Button)backpressdialog.findViewById(R.id.alertNoBtn);

        title.setText("User Conformation");
        message.setText("Caution : Please do not press back button.");

        alertYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backpressdialog.dismiss();
                new ViewEmaployeeownerid().execute();

            }
        });

        alertNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backpressdialog.dismiss();
                //Intent i=new Intent(PropertyRCUActivity.this,HostelListActivity.class);
                //startActivity(i);


            }
        });

        backpressdialog.show();
    }

    private class ViewEmaployeeownerid extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(PropertyRCUActivity.this);

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(PropertyRCUActivity.this);
            room_id_value=room_id;
            propertyid_value= property_id;

            HttpClient client = new DefaultHttpClient();
            HttpParams httpParameters =client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            httpParameters.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action","bedvacantremoveapi"));
                nameValuePairs.add(new BasicNameValuePair("property_id",propertyid_value));
                nameValuePairs.add(new BasicNameValuePair("room_id",room_id_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message=jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                }


            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equalsIgnoreCase("Y")) {

                if(mSharedStorage.getUserType().equals("2"))
                {
                    mSharedStorage.setAddCount("0");
                    mSharedStorage.setBookedPropertyId("0");
                }

                Intent  mIntent = new Intent(PropertyRCUActivity.this,HostelListActivity.class);
                startActivity(mIntent);

            } else if (result.equals("N")) {
                Toast.makeText(PropertyRCUActivity.this,"Please check network connection", Toast.LENGTH_SHORT).show();
            }
        }
    }



}