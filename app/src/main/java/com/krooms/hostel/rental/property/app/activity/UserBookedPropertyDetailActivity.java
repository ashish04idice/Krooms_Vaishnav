package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.asynctask.GetTenantInformationServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.UserBookedPropertyAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserBookedPropertyDetailActivity extends FragmentActivity implements ServiceResponce, View.OnClickListener {

    private Common mCommon = null;
    private TextView txtPropertyName, txtPropertyAddress, txtRoomno, txtDepositPayment,
            txtDuePayment, txtJoinDate, txtLeaveDate, txtIdentityProof, txtPropertyType;
    private FragmentActivity mActivity;
    private ImageView imgProperty;
    private ImageButton updateTenantInfoBtn;
    private int noOfTenant = 0;
    private String property_id, owner_id;
    private SharedStorage mSharedStorage;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_booked_property_detail);
        mActivity = this;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mSharedStorage = SharedStorage.getInstance(mActivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

        createViews();
        callWebServiceBookedUser();
    }

    public void createViews() {

        mCommon = new Common();
        txtPropertyName = (TextView) findViewById(R.id.textview_property_name);
        txtPropertyAddress = (TextView) findViewById(R.id.textview_property_address);
        txtRoomno = (TextView) findViewById(R.id.textview_property_roomno);
        //txtBadno = (TextView) convertView.findViewById(R.id.textview_property_badno);
        txtDepositPayment = (TextView) findViewById(R.id.textview_property_dep_payment);
        txtDuePayment = (TextView) findViewById(R.id.textview_property_due_payment);
        txtJoinDate = (TextView) findViewById(R.id.textview_property_join_date);
        txtLeaveDate = (TextView) findViewById(R.id.textview_property_leave_date);
        txtPropertyType = (TextView) findViewById(R.id.textview_property_type);
        txtIdentityProof = (TextView) findViewById(R.id.textview_property_identity_proof);
        imgProperty = (ImageView) findViewById(R.id.imageview_property);
        updateTenantInfoBtn = (ImageButton) findViewById(R.id.updateTenantInfoBtn);
        updateTenantInfoBtn.setOnClickListener(this);
        backButton = (ImageButton) findViewById(R.id.main_header_back);
        backButton.setOnClickListener(this);

    }

    public void callWebServiceBookedUser() {

        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
        Validation mValidation = new Validation(mActivity);
        if (mValidation.checkNetworkRechability()) {
            UserBookedPropertyAsyncTask serviceAsyncTask = new UserBookedPropertyAsyncTask(mActivity);
            serviceAsyncTask.setCallBack(UserBookedPropertyDetailActivity.this);
            serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId());
        } else {
            mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
        }

    }

    @Override
    public void requestResponce(String result) {


        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                JSONArray jArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                owner_id = jArray.getJSONObject(0).getString("owner_id");
                //String userId = jArray.getJSONObject(0).getString("user_id");
                property_id = jArray.getJSONObject(0).getString("property_id");
                String propertyName = jArray.getJSONObject(0).getString("property_name");
                String stateId = jArray.getJSONObject(0).getString("state");
                String cityId = jArray.getJSONObject(0).getString("city");
                String areaId = jArray.getJSONObject(0).getString("colony");
                String pincode = jArray.getJSONObject(0).getString("pincode");
                String propertyAddress = jArray.getJSONObject(0).getString("owner_address");
                String propertyRoomNo = jArray.getJSONObject(0).getString("no_of_room");
                String depositPayment = jArray.getJSONObject(0).getString("rent_amount");
                String duePayment = "0";//jArray.getJSONObject(0).getString("rent_amount");
                String joinDate = jArray.getJSONObject(0).getString("booking_from_date");
                String leaveDate = jArray.getJSONObject(0).getString("booking_end_date");
                String propertyImage = jArray.getJSONObject(0).getString("property_image");
                String identityProof = jArray.getJSONObject(0).getString("identity_number");
                String propertyType = jArray.getJSONObject(0).getString("for_whom");

                if (propertyName == null || propertyName.equals("null")) {
                    propertyName = "";
                }
                txtPropertyName.setText(propertyName);
                txtPropertyAddress.setText(propertyAddress + getLocalData(stateId, cityId, areaId) + " " + pincode);

                if (propertyType == null || propertyType.equals("null")) {
                    propertyType = "";
                }
                txtPropertyType.setText(propertyType);

                if (propertyRoomNo == null || propertyRoomNo.equals("null")) {
                    propertyRoomNo = "";
                }
                txtRoomno.setText(propertyRoomNo);

                if (depositPayment == null || depositPayment.equals("null")) {
                    depositPayment = "";
                }
                txtDepositPayment.setText(depositPayment);

                if (duePayment == null || duePayment.equals("null")) {
                    duePayment = "0";
                }
                txtDuePayment.setText(duePayment);

                if (joinDate == null || joinDate.equals("null")) {
                    joinDate = "";
                }
                txtJoinDate.setText(joinDate);

                if (leaveDate == null || leaveDate.equals("null")) {
                    leaveDate = "";
                }
                txtLeaveDate.setText(leaveDate);

                if (identityProof == null || identityProof.equals("null")) {
                    identityProof = "";
                }
                txtIdentityProof.setText(identityProof);

                Picasso.with(mActivity)
                        .load(propertyImage)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(imgProperty);

                getTenantDetail();

            } else {
                mCommon.displayAlert(mActivity, "" +
                        jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }
        } catch (JSONException e) {
        }
    }

    private String getLocalData(String stateId, String cityId, String areaId) {

        DataBaseAdapter mDBHealper = new DataBaseAdapter(mActivity);
        mDBHealper.createDatabase();
        mDBHealper.open();
        Cursor curState = mDBHealper.getSqlqureies("Select *from state where state_id = '" + stateId + "'");
        Cursor curCity = mDBHealper.getSqlqureies("Select *from city where city_id = '" + cityId + "'");
        Cursor curArea = mDBHealper.getSqlqureies("Select *from area where area_id = '" + areaId + "'");
        String stateName = "", cityName = "", areaName = "";
        if (curState.getCount() > 0) {
            stateName = curState.getString(curState.getColumnIndex("state_name"));
        }
        if (curCity.getCount() > 0) {
            cityName = curCity.getString(curCity.getColumnIndex("city_name"));
        }
        if (curArea.getCount() > 0) {
            areaName = curArea.getString(curArea.getColumnIndex("area_name"));
        }

        return " " + areaName + " " + cityName + " " + stateName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_header_back:
                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                break;
            case R.id.updateTenantInfoBtn:
                //updateBtnPressed();
                break;


        }
    }


    public void getTenantDetail() {
        GetTenantInformationServiceAsyncTask service = new GetTenantInformationServiceAsyncTask(mActivity);
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
                mCommon.displayAlert(mActivity, Common.TimeOut_Message, false);
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
                        rcuDetailModal.setLandmark(jsoArray.getJSONObject(i).getString("landmark"));
                        rcuDetailModal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
                        rcuDetailModal.setFlat_number(jsoArray.getJSONObject(i).getString("flat_number"));
                        rcuDetailModal.setTenant_contact_number(jsoArray.getJSONObject(i).getString("tenant_contact_number"));
                        rcuDetailModal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                        rcuDetailModal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                        rcuDetailModal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                        rcuDetailModal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
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
                        rcuDetailModal.setState(jsoArray.getJSONObject(i).getString("state"));
                        rcuDetailModal.setCity(jsoArray.getJSONObject(i).getString("city"));
                        rcuDetailModal.setLocation(jsoArray.getJSONObject(i).getString("location"));
                        rcuDetailModal.setTelephone_number(jsoArray.getJSONObject(i).getString("telephone_number"));
                        rcuDetailModal.setMobile_number(jsoArray.getJSONObject(i).getString("mobile_number"));
                        rcuDetailModal.setPincode(jsoArray.getJSONObject(i).getString("pincode"));
                        rcuDetailModal.setContact_number(jsoArray.getJSONObject(i).getString("contact_number"));
                        rcuDetailModal.setEmail_id(jsoArray.getJSONObject(i).getString("email_id"));
                        rcuDetailModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                        rcuDetailModal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                        rcuDetailModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                        Common.rcuDetails.add(rcuDetailModal);
                    }
                } else {
                    mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }
}