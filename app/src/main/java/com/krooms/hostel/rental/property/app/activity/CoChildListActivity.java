package com.krooms.hostel.rental.property.app.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter;
import com.krooms.hostel.rental.property.app.asynctask.GetTenantInformationServiceAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoChildListActivity extends FragmentActivity implements ServiceResponce, View.OnClickListener {

    public static String owneridvaluemain_tenant;
    private Common mCommon = null;
    private Boolean _isScroll = true;
    private int page_count = 0;
    private ImageButton main_header_back;
    private ImageButton main_edit_tenant;
    private TextView main_title;
    private ListView listMyBookedUser;
    private MyBookedUserListAdapter mAdapterBookedUser;
    private ArrayList<PropertyUserModal> mArrayBookedUser = new ArrayList<>();
    private FragmentActivity mActivity;
    String owner_idnew = "", holduserstatus = "";
    SharedStorage mSharedStorage;
    private int noOfTenant = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_tenant_list);
        holduserstatus = "";
        mActivity = this;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mSharedStorage = SharedStorage.getInstance(this);
        createViews();
        callWebServiceBookedUser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callWebServiceBookedUser();
    }

    public void createViews() {

        mCommon = new Common();

        listMyBookedUser = (ListView) findViewById(R.id.propertyList);
        main_header_back = (ImageButton) findViewById(R.id.main_header_back);
        main_title = (TextView) findViewById(R.id.PropertyNameTitle);
        main_edit_tenant = (ImageButton) findViewById(R.id.editTenantInformationBtn);
        if (mSharedStorage.getUserType().equals("4")) {
            main_title.setText("Child List");

        } else {
            main_title.setText("Tenant List");
        }
        main_header_back.setOnClickListener(this);
        main_edit_tenant.setOnClickListener(this);
        listMyBookedUser.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public void callWebServiceBookedUser() {

        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
        Validation mValidation = new Validation(mActivity);
        if (mValidation.checkNetworkRechability()) {
            GetTenantInformationServiceAsyncTask service = new GetTenantInformationServiceAsyncTask(mActivity);
            service.setCallBack(this);
            service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mSharedStorage.getBookedPropertyId());
        } else {
            mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_header_back:
                finish();
                break;
            case R.id.editTenantInformationBtn:
                String rcuId = "";
                String roomId = "";
                if (Common.rcuDetails.size() != 0) {
                    for (int i = 0; i < Common.rcuDetails.size(); i++) {
                        rcuId = rcuId + Common.rcuDetails.get(i).getTenant_id() + ",";
                    }
                    for (int i = 0; i < mArrayBookedUser.size(); i++) {
                        roomId = roomId + mArrayBookedUser.get(i).getBookedRoomId() + ",";
                    }

                    Intent mIntent = new Intent(this, PropertyRCUTenantDetailActivity.class);
                    mIntent.putExtra("RCUId", "" + rcuId);
                    mIntent.putExtra("noOfTenant", noOfTenant);
                    mIntent.putExtra("isUpdate", true);
                    mIntent.putExtra("property_id", mSharedStorage.getBookedPropertyId());
                    mIntent.putExtra("owner_id", owner_idnew);
                    mIntent.putExtra("room_id", roomId);
                    startActivity(mIntent);
                } else {
                    mCommon.displayAlert(this, "Data not found", true);
                }
                break;
        }

    }

    @Override
    public void requestResponce(String result) {

        Common.Config("result     " + result);

        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(mActivity, Common.TimeOut_Message, false);
            } else {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    if (mArrayBookedUser.size() != 0) {
                        mArrayBookedUser.clear();
                    }
                    if (Common.rcuDetails.size() != 0) {
                        Common.rcuDetails.clear();
                    }
                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    noOfTenant = jsoArray.length();

                    for (int i = 0; i < jsoArray.length(); i++) {
                        String statusbooking = jsoArray.getJSONObject(i).getString("status");
                        String statuactivate = jsoArray.getJSONObject(i).getString("status_active");
                        if (statusbooking.equals("1") && statuactivate.equals("")) {
                            PropertyUserModal propertyUserModal = new PropertyUserModal(Parcel.obtain());
                            propertyUserModal.setTenant_id(jsoArray.getJSONObject(i).getString("id"));
                            propertyUserModal.setTenant_form_no(jsoArray.getJSONObject(i).getString("tenant_form_no"));
                            propertyUserModal.setT_user_id((jsoArray.getJSONObject(i).getString("user_id")));
                            propertyUserModal.setTenant_fname(jsoArray.getJSONObject(i).getString("tenant_fname"));
                            propertyUserModal.setTenant_lname(jsoArray.getJSONObject(i).getString("tenant_lname"));
                            propertyUserModal.setTenant_father_name(jsoArray.getJSONObject(i).getString("tenant_father_name"));
                            propertyUserModal.setLandmark(jsoArray.getJSONObject(i).getString("landmark"));
                            propertyUserModal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
                            propertyUserModal.setFlat_number(jsoArray.getJSONObject(i).getString("flat_number"));
                            propertyUserModal.setTenant_contact_number(jsoArray.getJSONObject(i).getString("tenant_contact_number"));
                            propertyUserModal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                            propertyUserModal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                            propertyUserModal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                            propertyUserModal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("flat_number"));
                            propertyUserModal.setAadhar_card_no(jsoArray.getJSONObject(i).getString("aadhar_card_no"));
                            propertyUserModal.setAadhar_card_photo(jsoArray.getJSONObject(i).getString("aadhar_card_photo"));
                            propertyUserModal.setArm_licence_no(jsoArray.getJSONObject(i).getString("arm_licence_no"));
                            propertyUserModal.setArm_licence_photo(jsoArray.getJSONObject(i).getString("arm_licence_photo"));
                            propertyUserModal.setVehicle_registration_no(jsoArray.getJSONObject(i).getString("vehicle_registration_no"));
                            propertyUserModal.setVehicle_registration_photo(jsoArray.getJSONObject(i).getString("vehicle_registration_photo"));
                            propertyUserModal.setVoter_id_card_no(jsoArray.getJSONObject(i).getString("voter_id_card_no"));
                            propertyUserModal.setVoter_id_card_photo(jsoArray.getJSONObject(i).getString("voter_id_card_photo"));
                            propertyUserModal.setDriving_license_no(jsoArray.getJSONObject(i).getString("driving_license_no"));
                            propertyUserModal.setDriving_license_photo(jsoArray.getJSONObject(i).getString("driving_license_photo"));
                            propertyUserModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                            propertyUserModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                            propertyUserModal.setGuarantor_name(jsoArray.getJSONObject(i).getString("guarantor_name"));
                            propertyUserModal.setGuarantor_address(jsoArray.getJSONObject(i).getString("guarantor_address"));
                            propertyUserModal.setGuarantor_contact_number(jsoArray.getJSONObject(i).getString("guarantor_contact_number"));
                            propertyUserModal.setState(jsoArray.getJSONObject(i).getString("state"));
                            propertyUserModal.setCity(jsoArray.getJSONObject(i).getString("city"));
                            propertyUserModal.setLocation(jsoArray.getJSONObject(i).getString("location"));
                            propertyUserModal.setTelephone_number(jsoArray.getJSONObject(i).getString("telephone_number"));
                            propertyUserModal.setMobile_number(jsoArray.getJSONObject(i).getString("mobile_number"));
                            propertyUserModal.setPincode(jsoArray.getJSONObject(i).getString("pincode"));
                            propertyUserModal.setContact_number(jsoArray.getJSONObject(i).getString("contact_number"));
                            propertyUserModal.setEmail_id(jsoArray.getJSONObject(i).getString("email_id"));
                            propertyUserModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                            propertyUserModal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                            propertyUserModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                            propertyUserModal.setBookedRoom(jsoArray.getJSONObject(i).getString("room_no"));
                            propertyUserModal.setBookedRoomId(jsoArray.getJSONObject(i).getString("room_id"));
                            propertyUserModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                            propertyUserModal.setRoomAmount(jsoArray.getJSONObject(i).getString("mainroomamount"));
                            propertyUserModal.setRemainingAmount(jsoArray.getJSONObject(i).getString("remaining_amount"));
                            propertyUserModal.setPaymentStatus(jsoArray.getJSONObject(i).getString("transaction_status"));
                            propertyUserModal.setUserAddress(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                            propertyUserModal.setPaymentStatus(jsoArray.getJSONObject(i).getString("transaction_status"));
                            propertyUserModal.setOwnerId(jsoArray.getJSONObject(i).getString("owner_id"));
                            propertyUserModal.setStatus_active(jsoArray.getJSONObject(i).getString("status_active"));
                            owner_idnew = propertyUserModal.getOwnerId();
                            owneridvaluemain_tenant = jsoArray.getJSONObject(i).getString("owner_id");
                            propertyUserModal.setDriving_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("driving_license_issue_office"));
                            propertyUserModal.setArm_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("arm_name_issue_office"));
                            propertyUserModal.setPassport_no(jsoArray.getJSONObject(i).getString("passport_no"));
                            propertyUserModal.setPassport_photo(jsoArray.getJSONObject(i).getString("passport_photo"));
                            propertyUserModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                            propertyUserModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                            propertyUserModal.setOtherid_no(jsoArray.getJSONObject(i).getString("other_identity_card"));
                            propertyUserModal.setOtherid_photo(jsoArray.getJSONObject(i).getString("other_identity_photo"));
                            propertyUserModal.setDetail_verification(jsoArray.getJSONObject(i).getString("other_verification"));
                            if (jsoArray.getJSONObject(i).has("payment_type")) {
                                propertyUserModal.setPaymentMode(jsoArray.getJSONObject(i).getString("payment_type"));
                            }
                            if (jsoArray.getJSONObject(i).has("order_id")) {
                                propertyUserModal.setPaymentOrderId(jsoArray.getJSONObject(i).getString("order_id"));
                            }
                            if (jsoArray.getJSONObject(i).has("transition_number")) {
                                propertyUserModal.setPaymentTransactionId(jsoArray.getJSONObject(i).getString("transition_number"));
                            }

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
                            rcuDetailModal.setTenant_room_no(jsoArray.getJSONObject(i).getString("room_no"));
                            rcuDetailModal.setTenant_room_id(jsoArray.getJSONObject(i).getString("room_id"));
                            rcuDetailModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                            rcuDetailModal.setDriving_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("driving_license_issue_office"));
                            rcuDetailModal.setArm_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("arm_name_issue_office"));
                            rcuDetailModal.setPassport_no(jsoArray.getJSONObject(i).getString("passport_no"));
                            rcuDetailModal.setPassport_photo(jsoArray.getJSONObject(i).getString("passport_photo"));
                            rcuDetailModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                            rcuDetailModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                            rcuDetailModal.setOtherid_no(jsoArray.getJSONObject(i).getString("other_identity_card"));
                            rcuDetailModal.setOtherid_photo(jsoArray.getJSONObject(i).getString("other_identity_photo"));
                            rcuDetailModal.setDetail_verification(jsoArray.getJSONObject(i).getString("other_verification"));

                            if (jsoArray.getJSONObject(i).has("payment_type")) {
                                rcuDetailModal.setPaymentMode(jsoArray.getJSONObject(i).getString("payment_type"));
                            }
                            if (jsoArray.getJSONObject(i).has("order_id")) {
                                rcuDetailModal.setPaymentOrderId(jsoArray.getJSONObject(i).getString("order_id"));
                            }
                            if (jsoArray.getJSONObject(i).has("transition_number")) {
                                rcuDetailModal.setPaymentTransactionId(jsoArray.getJSONObject(i).getString("transition_number"));
                            }
                            mArrayBookedUser.add(propertyUserModal);
                            Common.rcuDetails.add(rcuDetailModal);
                        }
                    }
                    setTenantList();
                } else if (status.equals("true_1")) {

                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    callPropertyRCUActivity(jsoArray.getJSONObject(0).getString("transaction_id"), jsoArray.getJSONObject(0).getString("room_id"));

                } else {
                    mCommon.displayAlert(mActivity, jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        } catch (Exception e) {

        }
    }

    public void setTenantList() {

        String usertype = mSharedStorage.getUserType();
        mAdapterBookedUser = new MyBookedUserListAdapter(mActivity, mArrayBookedUser, "", "", "", holduserstatus, usertype);
        listMyBookedUser.setAdapter(mAdapterBookedUser);
    }

    public void callPropertyRCUActivity(String transaction_id, String room_id) {

        Intent mIntent = new Intent(this, PropertyRCUActivity.class);
        mIntent.putExtra("property_id", mSharedStorage.getBookedPropertyId());
        mIntent.putExtra("transaction_id", transaction_id);
        mIntent.putExtra("room_id", room_id);
        mIntent.putExtra("setTenant", true);
        startActivity(mIntent);
    }
}