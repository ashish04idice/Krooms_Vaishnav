package com.krooms.hostel.rental.property.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter;
import com.krooms.hostel.rental.property.app.asynctask.MyPropertyHoldUserAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPropertyHoldUsersListActivity extends FragmentActivity implements ServiceResponce, View.OnClickListener {
    static String tanent_user_id;
    private Common mCommon = null;
    private Boolean _isScroll = true;
    private int page_count = 0;
    private ImageButton main_header_back, deactive_user, holduser_list;
    private ListView listMyBookedUser;
    private MyBookedUserListAdapter mAdapterBookedUser;
    public ArrayList<PropertyUserModal> mArrayBookedUser;
    private FragmentActivity mActivity;
    // private View convertView;
    public static String owneridvaluemain = "";
    public static String holduserstatus = "";
    String usertype;
    private SharedStorage mSharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_property_list_booked);
        mActivity = this;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mSharedStorage = SharedStorage.getInstance(this);
        usertype = mSharedStorage.getUserType();
        holduserstatus = "1";
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
        holduser_list = (ImageButton) findViewById(R.id.holduser_list);
        holduser_list.setVisibility(View.GONE);
        main_header_back.setOnClickListener(this);
        deactive_user = (ImageButton) findViewById(R.id.deactive_user);
        deactive_user.setVisibility(View.GONE);
        deactive_user.setOnClickListener(this);

        listMyBookedUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

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
            MyPropertyHoldUserAsyncTask serviceAsyncTask = new MyPropertyHoldUserAsyncTask(mActivity);
            serviceAsyncTask.setCallBack(MyPropertyHoldUsersListActivity.this);
            serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserPropertyId());
        } else {
            mCommon.displayAlert(mActivity, getResources().getString(R.string.str_no_network), false);
        }

    }

    @Override
    public void requestResponce(String result) {

        Common.Config("response   " + result);
        try {

            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (page_count == 0) {
                mArrayBookedUser = new ArrayList<>();
            }
            if (status.equals("true")) {

                JSONArray jsoArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                for (int i = 0; i < jsoArray.length(); i++) {
                    String statusbooking = jsoArray.getJSONObject(i).getString("status");
                    String statuactivate = jsoArray.getJSONObject(i).getString("status_active");
                    // if (statusbooking.equals("1") && statuactivate.equals("")) {
                    PropertyUserModal modal = new PropertyUserModal(Parcel.obtain());
                    modal.setParent_id(jsoArray.getJSONObject(i).getString("parent_id"));
                    modal.setPropertyId(jsoArray.getJSONObject(i).getString("property_id"));
                    modal.setTenant_id(jsoArray.getJSONObject(i).getString("id"));
                    modal.setT_user_id((jsoArray.getJSONObject(i).getString("user_id")));
                    modal.setTenant_form_no(jsoArray.getJSONObject(i).getString("tenant_form_no"));
                    modal.setTenant_fname(jsoArray.getJSONObject(i).getString("tenant_fname"));
                    modal.setTenant_lname(jsoArray.getJSONObject(i).getString("tenant_lname"));
                    modal.setTenant_father_name(jsoArray.getJSONObject(i).getString("tenant_father_name"));
                    modal.setLandmark(jsoArray.getJSONObject(i).getString("landmark"));
                    modal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
                    modal.setFlat_number(jsoArray.getJSONObject(i).getString("flat_number"));
                    modal.setTenant_contact_number(jsoArray.getJSONObject(i).getString("tenant_contact_number"));
                    modal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                    modal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                    modal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                    modal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("flat_number"));
                    modal.setAadhar_card_no(jsoArray.getJSONObject(i).getString("aadhar_card_no"));
                    modal.setAadhar_card_photo(jsoArray.getJSONObject(i).getString("aadhar_card_photo"));
                    modal.setArm_licence_no(jsoArray.getJSONObject(i).getString("arm_licence_no"));
                    modal.setArm_licence_photo(jsoArray.getJSONObject(i).getString("arm_licence_photo"));
                    modal.setVehicle_registration_no(jsoArray.getJSONObject(i).getString("vehicle_registration_no"));
                    modal.setVehicle_registration_photo(jsoArray.getJSONObject(i).getString("vehicle_registration_photo"));
                    modal.setVoter_id_card_no(jsoArray.getJSONObject(i).getString("voter_id_card_no"));
                    modal.setVoter_id_card_photo(jsoArray.getJSONObject(i).getString("voter_id_card_photo"));
                    modal.setDriving_license_no(jsoArray.getJSONObject(i).getString("driving_license_no"));
                    modal.setDriving_license_photo(jsoArray.getJSONObject(i).getString("driving_license_photo"));
                    modal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                    modal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                    modal.setGuarantor_name(jsoArray.getJSONObject(i).getString("guarantor_name"));
                    modal.setGuarantor_address(jsoArray.getJSONObject(i).getString("guarantor_address"));
                    modal.setGuarantor_contact_number(jsoArray.getJSONObject(i).getString("guarantor_contact_number"));
                    modal.setState(jsoArray.getJSONObject(i).getString("state"));
                    modal.setCity(jsoArray.getJSONObject(i).getString("city"));
                    modal.setLocation(jsoArray.getJSONObject(i).getString("location"));
                    modal.setTelephone_number(jsoArray.getJSONObject(i).getString("telephone_number"));
                    modal.setMobile_number(jsoArray.getJSONObject(i).getString("mobile_number"));
                    modal.setPincode(jsoArray.getJSONObject(i).getString("pincode"));
                    modal.setContact_number(jsoArray.getJSONObject(i).getString("contact_number"));
                    modal.setEmail_id(jsoArray.getJSONObject(i).getString("email_id"));
                    modal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                    modal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                    modal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                    modal.setBookedRoom(jsoArray.getJSONObject(i).getString("room_no"));
                    modal.setBookedRoomId(jsoArray.getJSONObject(i).getString("room_id"));
                    modal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                    modal.setRoomAmount(jsoArray.getJSONObject(i).getString("amount"));
                    modal.setMainroomamount(jsoArray.getJSONObject(i).getString("mainroomamount"));
                    modal.setRemainingAmount(jsoArray.getJSONObject(i).getString("remaining_amount"));
                    modal.setPaymentStatus(jsoArray.getJSONObject(i).getString("transaction_status"));
                    modal.setUserAddress(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                    modal.setTransaction_id(jsoArray.getJSONObject(i).getString("transaction_id"));
                    owneridvaluemain = jsoArray.getJSONObject(i).getString("owner_id");
                    modal.setOwnerId(jsoArray.getJSONObject(i).getString("owner_id"));
                    Log.d("owner id", owneridvaluemain);
                    modal.setDriving_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("driving_license_issue_office"));
                    modal.setArm_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("arm_name_issue_office"));
                    modal.setPassport_no(jsoArray.getJSONObject(i).getString("passport_no"));
                    Common.Config("passport   " + modal.getPassport_no());
                    modal.setPassport_photo(jsoArray.getJSONObject(i).getString("passport_photo"));
                    modal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                    Common.Config("rashan   " + modal.getRashan_card_no());
                    modal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                    modal.setOtherid_no(jsoArray.getJSONObject(i).getString("other_identity_card"));
                    modal.setOtherid_photo(jsoArray.getJSONObject(i).getString("other_identity_photo"));
                    modal.setDetail_verification(jsoArray.getJSONObject(i).getString("other_verification"));
                    //attendance sms
                    modal.setAttendancesms(jsoArray.getJSONObject(i).getString("attendance_sms"));
                    //
                    if (jsoArray.getJSONObject(i).has("payment_type")) {
                        modal.setPaymentMode(jsoArray.getJSONObject(i).getString("payment_type"));
                    }
                    if (jsoArray.getJSONObject(i).has("order_id")) {
                        modal.setPaymentOrderId(jsoArray.getJSONObject(i).getString("order_id"));
                    }
                    if (jsoArray.getJSONObject(i).has("transition_number")) {
                        modal.setPaymentTransactionId(jsoArray.getJSONObject(i).getString("transition_number"));
                    }
                    mArrayBookedUser.add(modal);
                    //}
                }
                String key = "Owner";


                //Chnages on 16/10/2018 for hold user status
                mAdapterBookedUser = new MyBookedUserListAdapter(mActivity, mArrayBookedUser, key, "", "", holduserstatus, usertype);
                listMyBookedUser.setAdapter(mAdapterBookedUser);
                //
            } else {
                if (mArrayBookedUser.size() == 0) {
                    mCommon.displayAlert(mActivity, "" + jsonObject.getString(WebUrls.MESSAGE_JSON), true);

                } else {
                    mCommon.displayAlert(mActivity, "" + this.getResources()
                            .getString(R.string.str_no_more_data), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mCommon.displayAlert(mActivity, "Property user is not available.", false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_header_back:
//                finish();

                if (usertype.equals("5")) {
                    startActivity(new Intent(MyPropertyHoldUsersListActivity.this, Home_Accountantactivity.class));

                } else {
                    startActivity(new Intent(MyPropertyHoldUsersListActivity.this, MyPropertyUsersListActivity.class));
                }
                break;
            case R.id.deactive_user:
//                finish();
                Intent in = new Intent(MyPropertyHoldUsersListActivity.this, MyPropertyDeactiveUsersListActivity.class);
                in.putExtra("status", "deactive");
                startActivity(in);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (usertype.equals("5")) {
                    startActivity(new Intent(MyPropertyHoldUsersListActivity.this, Home_Accountantactivity.class));

                } else {
                    startActivity(new Intent(MyPropertyHoldUsersListActivity.this, MyPropertyUsersListActivity.class));
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}