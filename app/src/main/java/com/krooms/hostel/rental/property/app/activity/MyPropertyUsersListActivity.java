package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.MyPropertyBookedUserAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPropertyUsersListActivity extends FragmentActivity implements ServiceResponce, View.OnClickListener ,AbsListView.OnScrollListener{
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
    boolean flag_loading = false;
    public static int page_no = 0;
    private int firstVisibleItem1, visibleItemCount1, totalItemCount1;
    int mVisibleThreshold = 5;
    int threshold = 0;
    int mPreviousTotal = 0;
    boolean mLoading = true;
    boolean mLastPage = false;
    boolean userScrolled = false;
    RecyclerView propertyListRecycle;
    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    int currentScrollState = 0;
    boolean loadingMore = false;
    Long startIndex = 0L;
    Long offset = 10L;
    View footerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_property_list_booked);
        mActivity = this;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mSharedStorage = SharedStorage.getInstance(this);
        usertype = mSharedStorage.getUserType();
        holduserstatus = "";
        createViews();
        page_no = 0;

        callWebServiceBookedUser();
    }


    public void createViews() {

        mCommon = new Common();

        listMyBookedUser = (ListView) findViewById(R.id.propertyList);
        main_header_back = (ImageButton) findViewById(R.id.main_header_back);
        holduser_list = (ImageButton) findViewById(R.id.holduser_list);

        main_header_back.setOnClickListener(this);
        deactive_user = (ImageButton) findViewById(R.id.deactive_user);
        deactive_user.setVisibility(View.GONE);
        deactive_user.setOnClickListener(this);
        holduser_list.setOnClickListener(this);
        listMyBookedUser.setOnScrollListener(this);

        listMyBookedUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });

    }

    public void callWebServiceBookedUser() {

        String page = String.valueOf(page_no);
        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
        Validation mValidation = new Validation(mActivity);
        //footerView = MyPropertyUsersListActivity.this.getLayoutInflater().inflate(R.layout.item_list_loading_footer, null);
        String key = "Owner";
     //   if (page_no>0){
            //listMyBookedUser.addFooterView(footerView);
//            mAdapterBookedUser = new MyBookedUserListAdapter(mActivity, mArrayBookedUser, key, "", "", holduserstatus, usertype);
//            listMyBookedUser.setAdapter(mAdapterBookedUser);
       // }
        if (mValidation.checkNetworkRechability()) {
            MyPropertyBookedUserAsyncTask serviceAsyncTask = new MyPropertyBookedUserAsyncTask(mActivity);
            serviceAsyncTask.setCallBack(MyPropertyUsersListActivity.this);
            serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserPropertyId(), page);

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
            if (page_no == 0) {
                mArrayBookedUser = new ArrayList<>();
            }
            if (status.equals("true")){
              //  if (page_no>0){
                   // listMyBookedUser.removeFooterView(footerView);
               // }
                loadingMore = false;

                JSONArray jsoArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                for (int i = 0; i < jsoArray.length(); i++) {
                    String statusbooking = jsoArray.getJSONObject(i).getString("status");
                    String statuactivate = jsoArray.getJSONObject(i).getString("status_active");
                    if (statusbooking.equals("1") && statuactivate.equals("")) {
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
                        // modal.setAttendancesms(jsoArray.getJSONObject(i).getString("attendance_sms"));
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
                    }
                }
                String key = "Owner";
                mAdapterBookedUser = new MyBookedUserListAdapter(mActivity, mArrayBookedUser, key, "", "", holduserstatus, usertype);
                listMyBookedUser.setAdapter(mAdapterBookedUser);
                mAdapterBookedUser.notifyDataSetChanged();
                if (page_no>0){
                    listMyBookedUser.setSelection(mAdapterBookedUser.getCount() - 1);
                }

                page_no++;

            } else {
                if (mArrayBookedUser.size() == 0) {
                    //mCommon.displayAlert(mActivity, "" + jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                    Toast.makeText(mActivity, "" + jsonObject.getString(WebUrls.MESSAGE_JSON), Toast.LENGTH_SHORT).show();
                    page_no=0;
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
                if (usertype.equals("5")) {
                    startActivity(new Intent(MyPropertyUsersListActivity.this, Home_Accountantactivity.class));
                } else {
                    startActivity(new Intent(MyPropertyUsersListActivity.this, HostelListActivity.class));
                }
                break;
            case R.id.deactive_user:
                Intent in = new Intent(MyPropertyUsersListActivity.this, MyPropertyDeactiveUsersListActivity.class);
                in.putExtra("status", "deactive");
                startActivity(in);
                break;
            case R.id.holduser_list:
                Intent ii = new Intent(MyPropertyUsersListActivity.this, MyPropertyHoldUsersListActivity.class);
                startActivity(ii);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (usertype.equals("5")) {
                    startActivity(new Intent(MyPropertyUsersListActivity.this, Home_Accountantactivity.class));
                } else {
                    startActivity(new Intent(MyPropertyUsersListActivity.this, HostelListActivity.class));
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;


     /*   int lastInScreen = firstVisibleItem + visibleItemCount;
        if(lastInScreen == totalItemCount)
        {
            mArrayBookedUser.clear();
            //Add the values in you Array here.
            callWebServiceBookedUser();

        }
*/
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState){
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE && this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
            /*** In this way I detect if there's been a scroll which has completed ***/
            /*** do the work for load more date! ***/
            if (!loadingMore) {
                loadingMore = true;
                callWebServiceBookedUser();
            }
        }
    }

    /*copy adapter from main adapter class*/
    public class MyBookedUserListAdapter extends BaseAdapter {

        private Context context;
        private Activity activity;
        private ArrayList<PropertyUserModal> _listArray;
        String comefrom;
        String value;
        String useridmainvalue, parentvalueid;
        String holduserstatus;
        String usertype = "";
        boolean isLoading;
        int cou = 10;

        // boolean[] checkBoxState;
        public MyBookedUserListAdapter(Activity context, ArrayList<PropertyUserModal> objects, String key, String useridmainvalue, String parentvalueid, String holduserstatus, String usertype) {
            this.context = context;
            this.activity = context;
            this._listArray = objects;
            comefrom = key;
            this.useridmainvalue = useridmainvalue;
            this.parentvalueid = parentvalueid;
            this.holduserstatus = holduserstatus;
            this.usertype = usertype;
            // checkBoxState = new boolean[_listArray.size()];
        }

        @Override
        public int getCount() {

            return _listArray.size();
        }

        @Override
        public Object getItem(int position) {
            return (position);
        }

        @Override
        public long getItemId(int position) {
            return (position);
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public class ViewHolder {

            ImageView userImage;
            TextView userName, bookRoom, bookBad, bookDate, userAmount, userAddress;
            LinearLayout click, laysms;
            CheckBox smscheckBox;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            isLoading = true;
            final MyBookedUserListAdapter.ViewHolder _viewHolder;
            View v = convertView;
            if (null == convertView) {
                LayoutInflater inflater = activity.getLayoutInflater();
                v = inflater.inflate(R.layout.myproeprty_user_list_item, null);
                _viewHolder = new MyBookedUserListAdapter.ViewHolder();
                _viewHolder.userName = (TextView) v.findViewById(R.id.property_user_name);
                _viewHolder.bookRoom = (TextView) v.findViewById(R.id.property_user_book_room);
                _viewHolder.bookBad = (TextView) v.findViewById(R.id.property_user_book_room_bad);
                _viewHolder.bookDate = (TextView) v.findViewById(R.id.property_user_book_date);
                _viewHolder.userAddress = (TextView) v.findViewById(R.id.property_user_address);
                _viewHolder.userAmount = (TextView) v.findViewById(R.id.property_user_book_amount);
                _viewHolder.userImage = (ImageView) v.findViewById(R.id.imageview_itm_user);
                _viewHolder.click = (LinearLayout) v.findViewById(R.id.layout_top);
                _viewHolder.laysms = (LinearLayout) v.findViewById(R.id.laysms);
                _viewHolder.smscheckBox = (CheckBox) v.findViewById(R.id.smscheckBox);
                v.setTag(_viewHolder);

            } else {
                _viewHolder = (MyBookedUserListAdapter.ViewHolder) convertView.getTag();
            }

            _viewHolder.userName.setText(_listArray.get(position).getTenant_fname() + " " + _listArray.get(position).getTenant_lname());
            _viewHolder.bookRoom.setText("Room no: " + _listArray.get(position).getBookedRoom());
            _viewHolder.bookBad.setText("Rent Amount: " + _listArray.get(position).getMainroomamount() + "/m");
            _viewHolder.userAmount.setText("Status: " + _listArray.get(position).getPaymentStatus());
            _viewHolder.bookDate.setText("Join Date: " + _listArray.get(position).getProperty_hire_date());
            _viewHolder.userAddress.setText("Address: " + _listArray.get(position).getTenant_permanent_address());
            Picasso.with(context)
                    .load(WebUrls.IMG_URL + _listArray.get(position).getTenant_photo())
                    .placeholder(R.drawable.user_xl)
                    .error(R.drawable.user_xl)
                    .transform(new RoundedTransformation(activity))
                    .into(_viewHolder.userImage);

            //old working convertView
            _viewHolder.click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                Intent it = new Intent(activity, HostelDetailActivity.class);
                    //                it.putExtra("selected_index", position);
                    //                it.putExtra("property_id", list.get(position).getProperty_id());
                    //                it.putExtra("list_type", "list_type_searchResult");
                    //                activity.startActivity(it);

                    Common.Config("rashan    " + _listArray.get(position).getRashan_card_no());
                    Common.Config("passport    " + _listArray.get(position).getPassport_no());

                    Intent mIntent = new Intent(activity, TenantDetailActivity.class);
                    mIntent.putExtra("modal", (Parcelable) _listArray.get(position));
                    mIntent.putExtra("useridv", _listArray.get(position).getT_user_id());
                    String newt_id = _listArray.get(position).getT_user_id();
                    //for back button handel
                    mIntent.putExtra("KEY", comefrom);
                    if (comefrom.equalsIgnoreCase("Parent")) {
                        mIntent.putExtra("useridd", useridmainvalue);
                        mIntent.putExtra("parentidd", parentvalueid);
                    }
                    mIntent.putExtra("NEWTID", newt_id);
                    String tenantid = _listArray.get(position).getTenant_id();
                    mIntent.putExtra("tid", tenantid);
                    mIntent.putExtra("roomid", _listArray.get(position).getBookedRoomId());
                    mIntent.putExtra("tfname", _listArray.get(position).getTenant_fname());
                    mIntent.putExtra("tlname", _listArray.get(position).getTenant_lname());
                    mIntent.putExtra("tfathername", _listArray.get(position).getTenant_father_name());
                    mIntent.putExtra("tfather_no", _listArray.get(position).getTenant_father_contact_no());
                    mIntent.putExtra("temail", _listArray.get(position).getEmail_id());
                    mIntent.putExtra("tcontact", _listArray.get(position).getContact_number());
                    mIntent.putExtra("taddress", _listArray.get(position).getTenant_permanent_address());
                    mIntent.putExtra("tlandmark", _listArray.get(position).getLandmark());
                    String propertyidforseparatevalue = _listArray.get(position).getPropertyId();
                    mIntent.putExtra("propertyidforseparate", propertyidforseparatevalue);
                    mIntent.putExtra("thiredate", _listArray.get(position).getProperty_hire_date());
                    mIntent.putExtra("tleavedate", _listArray.get(position).getProperty_leave_date());
                    mIntent.putExtra("tofc_institute", _listArray.get(position).getTenant_office_institute());
                    mIntent.putExtra("tofc_contact", _listArray.get(position).getTenant_contact_number());
                    mIntent.putExtra("twork_detail", _listArray.get(position).getTenant_work_detail());
                    mIntent.putExtra("tguarantorname", _listArray.get(position).getGuarantor_name());
                    mIntent.putExtra("tguarantoraddress", _listArray.get(position).getGuarantor_address());
                    mIntent.putExtra("tguarantorcontact", _listArray.get(position).getGuarantor_contact_number());
                    mIntent.putExtra("tpmode", _listArray.get(position).getPaymentMode());
                    mIntent.putExtra("tporderid", _listArray.get(position).getPaymentOrderId());
                    mIntent.putExtra("tptransactionid", _listArray.get(position).getPaymentTransactionId());
                    mIntent.putExtra("troomamount", _listArray.get(position).getRoomAmount());//advance amount
                    mIntent.putExtra("tvehicalid", _listArray.get(position).getVehicle_registration_no());
                    mIntent.putExtra("tvoterid", _listArray.get(position).getVoter_id_card_no());
                    mIntent.putExtra("tdrivingid", _listArray.get(position).getDriving_license_no());
                    mIntent.putExtra("tadharid", _listArray.get(position).getAadhar_card_no());
                    mIntent.putExtra("tarmid", _listArray.get(position).getArm_licence_no());
                    mIntent.putExtra("tdriving_issue_ofc", _listArray.get(position).getDriving_licence_issu_ofc_name());
                    mIntent.putExtra("tarm_issue_ofc", _listArray.get(position).getArm_licence_issu_ofc_name());
                    mIntent.putExtra("trashanid", _listArray.get(position).getRashan_card_no());
                    mIntent.putExtra("tpassportid", _listArray.get(position).getPassport_no());
                    mIntent.putExtra("totherid", _listArray.get(position).getOtherid_no());
                    mIntent.putExtra("tdetailverif", _listArray.get(position).getDetail_verification());
                    mIntent.putExtra("tpstatus", _listArray.get(position).getPaymentStatus());
                    mIntent.putExtra("tvehicalphoto", _listArray.get(position).getVehicle_registration_photo());
                    mIntent.putExtra("tvoteridphoto", _listArray.get(position).getVoter_id_card_photo());
                    mIntent.putExtra("tdrivinglicphoto", _listArray.get(position).getDriving_license_photo());
                    mIntent.putExtra("taadharphoto", _listArray.get(position).getAadhar_card_photo());
                    mIntent.putExtra("tarmphoto", _listArray.get(position).getArm_licence_photo());
                    mIntent.putExtra("tpassportphoto", _listArray.get(position).getPassport_photo());
                    mIntent.putExtra("trashanphoto", _listArray.get(position).getRashan_card_photo());
                    mIntent.putExtra("totherphoto", _listArray.get(position).getOtherid_photo());
                    mIntent.putExtra("tenantphoto", _listArray.get(position).getTenant_photo());
                    // mIntent.putExtra("tflatno",_listArray.get(position).getFlat_number());
                    mIntent.putExtra("tpincode", _listArray.get(position).getPincode());
                    mIntent.putExtra("tstate", _listArray.get(position).getState());
                    mIntent.putExtra("tcity", _listArray.get(position).getCity());
                    mIntent.putExtra("tbookedroomno", _listArray.get(position).getBookedRoom());
                    mIntent.putExtra("tflatno", _listArray.get(position).getFlat_number());
                    mIntent.putExtra("tflatno", _listArray.get(position).getFlat_number());
                    mIntent.putExtra("tarea", _listArray.get(position).getLocation());
                    mIntent.putExtra("tRcu_id", _listArray.get(position).getTenant_id());
                    mIntent.putExtra("tTransaction_id", _listArray.get(position).getTransaction_id());
                    mIntent.putExtra("tParent_id", _listArray.get(position).getParent_id());
                    String parentidvaliue = _listArray.get(position).getParent_id();
                    mIntent.putExtra("tOwner_id", _listArray.get(position).getOwnerId());
                    String tOwner_idvalue = _listArray.get(position).getOwnerId();
                    mIntent.putExtra("ownerid", tOwner_idvalue);
                    mIntent.putExtra("tPhoto", _listArray.get(position).getTenant_photo());
                    mIntent.putExtra("tofficeAddress", _listArray.get(position).getTenant_office_institute());
                    mIntent.putExtra("totherVerifaction", _listArray.get(position).getDetail_verification());
                    mIntent.putExtra("tDrivinglicenseNo", _listArray.get(position).getDriving_license_no());
                    mIntent.putExtra("tDrivinglicensePhoto", _listArray.get(position).getDriving_license_photo());
                    mIntent.putExtra("tDrivinglicenseIssuename", _listArray.get(position).getDriving_licence_issu_ofc_name());
                    mIntent.putExtra("tAadharcardNo", _listArray.get(position).getAadhar_card_no());
                    mIntent.putExtra("tAadharcardPhoto", _listArray.get(position).getAadhar_card_photo());
                    mIntent.putExtra("tArmlicenceNo", _listArray.get(position).getArm_licence_no());
                    mIntent.putExtra("tArmlicencePhoto", _listArray.get(position).getArm_licence_photo());
                    mIntent.putExtra("tArmlicenceIssuename", _listArray.get(position).getArm_licence_issu_ofc_name());
                    mIntent.putExtra("tOtherIdno", _listArray.get(position).getOtherid_no());
                    mIntent.putExtra("tOtherIdPhoto", _listArray.get(position).getOtherid_photo());
                    mIntent.putExtra("tVoteridcardno", _listArray.get(position).getVoter_id_card_no());
                    mIntent.putExtra("tVoteridcardPhoto", _listArray.get(position).getVoter_id_card_photo());
                    mIntent.putExtra("tVehicleRegistrationNo", _listArray.get(position).getVehicle_registration_no());
                    mIntent.putExtra("tVehicleRegistrationPhoto", _listArray.get(position).getVehicle_registration_photo());
                    mIntent.putExtra("tPassportNo", _listArray.get(position).getPassport_no());
                    mIntent.putExtra("tPassportPhoto", _listArray.get(position).getPassport_photo());
                    mIntent.putExtra("tRashanCardNo", _listArray.get(position).getRashan_card_no());
                    mIntent.putExtra("tRashanCardPhoto", _listArray.get(position).getRashan_card_photo());
                    mIntent.putExtra("tsformTelephoneNo", _listArray.get(position).getTelephone_number());
                    mIntent.putExtra("tsformMobileNo", _listArray.get(position).getMobile_number());
                    mIntent.putExtra("tmonthlyRoomRant", _listArray.get(position).getMainroomamount());
                    Log.d("un 18", "in adapter" + _listArray.get(position).getBookedRoomId());
                    //changes on 16/10/2018 ashish
                    mIntent.putExtra("holduserstatus", holduserstatus);
                    //
                    activity.startActivity(mIntent);
                }
            });

            isLoading = false;
            return v;

        }

        private void Sendtoserver(String tenantid, String propertyidforseparatevalue, String value) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

            try {
                JSONObject params = new JSONObject();
                params.put("action", "updateAttendancesms");
                params.put("property_id", propertyidforseparatevalue);
                params.put("tenant_id", tenantid);
                params.put("value", value);

                String url = WebUrls.MAIN_URL;
                JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        String result = response.toString();
                        getResponseData(result);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                    }
                });
                request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request_json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void getResponseData(String result) {
            try {
                String status, message;
                JSONObject jsmain = new JSONObject(result);
                status = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (status.equalsIgnoreCase("Y")) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    callWebServiceBookedUser();

                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getAttendancestatus(String tenantid, String propertyidforseparatevalue, final int position, final CheckBox smscheckBox) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

            try {
                JSONObject params = new JSONObject();
                params.put("action", "getAttendancestatus");
                params.put("property_id", propertyidforseparatevalue);
                params.put("tenant_id", tenantid);
                String url = WebUrls.MAIN_URL;
                JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        String result = response.toString();
                        getResponseDatauncheck(result, position, smscheckBox);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                    }
                });
                request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request_json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void getResponseDatauncheck(String result, int position, CheckBox smscheckBox) {
            try {
                String status, message, statussms;
                JSONObject jsmain = new JSONObject(result);
                status = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (status.equalsIgnoreCase("Y")) {
                    //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    statussms = jsmain.getString("attendance_sms");

                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}