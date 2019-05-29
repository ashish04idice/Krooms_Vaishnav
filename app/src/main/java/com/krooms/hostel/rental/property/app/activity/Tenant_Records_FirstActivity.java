package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter_newAttendence;
import com.krooms.hostel.rental.property.app.adapter.TenantRecordListAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 5/24/2017.
 */
public class Tenant_Records_FirstActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    ListView listView;
    TenantRecordListAdapter adapter;
    ArrayList<TenantModal> userlist;
    Context context;
    int GET_FINGER_TEMPLATE = 1;
    int SAVE_FINGER_TEMPLATE = 2;
    public static String tid,tname,roomno;
    RelativeLayout back;
    Intent in;
    String propertyid,devicetype="";
    public ArrayList<PropertyUserModal> mArrayBookedUser;
    public static int page_no = 0;
    boolean loadingMore = false;
    MyBookedUserListAdapter_newAttendence mAdapterBookedUser;
    private Common mCommon = null;
    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    int currentScrollState = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_records_firstactivity_layout);
        context=this;
        in=getIntent();
        page_no=0;
        mCommon = new Common();
        propertyid=in.getStringExtra("propertyid");
        devicetype=in.getStringExtra("device");
        listView=(ListView)findViewById(R.id.tenantlist);
        listView.setOnScrollListener(this);
        back=(RelativeLayout)findViewById(R.id.flback_button);
        Validation mValidation = new Validation(context);
        if(mValidation.checkNetworkRechability()) {
            new StudentNameJsonParse().execute();
        } else {
            Toast.makeText(Tenant_Records_FirstActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Attandance_Home_Activity.class);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("device",devicetype);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tid = mArrayBookedUser.get(position).getTenant_id();
                tname = mArrayBookedUser.get(position).getTenant_fname() + " " + mArrayBookedUser.get(position).getTenant_lname();
                roomno = mArrayBookedUser.get(position).getBookedRoom();

                if(devicetype.equalsIgnoreCase("Secugen")) {
                    Intent intent = new Intent(context, Finger_Selction_Activity.class);
                    intent.putExtra("NAME", tname);
                    intent.putExtra("ID", tid);
                    intent.putExtra("propertyid", propertyid);
                    intent.putExtra("Roomno", roomno);
                    intent.putExtra("Akey", "Selection");
                    intent.putExtra("device",devicetype);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(context, Finger_Selction_Activity.class);
                    intent.putExtra("NAME", tname);
                    intent.putExtra("ID", tid);
                    intent.putExtra("propertyid", propertyid);
                    intent.putExtra("Roomno", roomno);
                    intent.putExtra("Akey", "Selection");
                    intent.putExtra("device",devicetype);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;

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
                new StudentNameJsonParse().execute();
            }
        }
    }


    private class StudentNameJsonParse extends AsyncTask<String, String, String> {

        String name,result,message;
        private boolean IsError = false;

        private ProgressDialog dialog = new ProgressDialog(Tenant_Records_FirstActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String url=null;
            String page_nosend= String.valueOf(page_no);
            HttpClient client = new DefaultHttpClient();
            url = WebUrls.MAIN_URL;
            HttpPost post = new HttpPost(url);
            Log.d("uu 1", "" + post);
            try {

                if (page_no == 0) {
                    mArrayBookedUser = new ArrayList<>();
                }
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action","userListOfAPropertyNewPagination"));
                nameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
                nameValuePairs.add(new BasicNameValuePair("page_no",page_nosend));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsonObject =new JSONObject(objectmain);
                result = jsonObject.getString("status");
                message = jsonObject.getString("message");

                if (result.equals("true")) {

                    loadingMore = false;

                    JSONArray jsoArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                    for(int i= 0;i<jsoArray.length();i++) {
                        String statusbooking=jsoArray.getJSONObject(i).getString("status");
                        String statuactivate=jsoArray.getJSONObject(i).getString("status_active");
                        if (statusbooking.equals("1")&& statuactivate.equals(""))
                        {
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
                            modal.setRemainingAmount(jsoArray.getJSONObject(i).getString("remaining_amount"));
                            modal.setPaymentStatus(jsoArray.getJSONObject(i).getString("transaction_status"));
                            modal.setUserAddress(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                            modal.setTransaction_id(jsoArray.getJSONObject(i).getString("transaction_id"));
                            String owneridvaluemain=jsoArray.getJSONObject(i).getString("owner_id");
                            modal.setOwnerId(jsoArray.getJSONObject(i).getString("owner_id"));
                            Log.d("owner id",owneridvaluemain);
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
                            if(jsoArray.getJSONObject(i).has("payment_type")){
                                modal.setPaymentMode(jsoArray.getJSONObject(i).getString("payment_type"));
                            }
                            if(jsoArray.getJSONObject(i).has("order_id")) {
                                modal.setPaymentOrderId(jsoArray.getJSONObject(i).getString("order_id"));
                            }
                            if(jsoArray.getJSONObject(i).has("transition_number")) {
                                modal.setPaymentTransactionId(jsoArray.getJSONObject(i).getString("transition_number"));
                            }
                            mArrayBookedUser.add(modal);
                        }
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

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            //student_loader.clearAnimation();
            // student_loader.setVisibility(View.GONE);

            if (result.equalsIgnoreCase("true")) {
                if (mArrayBookedUser.size() > 0) {

                    mAdapterBookedUser = new MyBookedUserListAdapter_newAttendence(Tenant_Records_FirstActivity.this,mArrayBookedUser);
                    listView.setAdapter(mAdapterBookedUser);
                   // mAdapterBookedUser.notifyDataSetChanged();
                    if (page_no>0){
                        listView.setSelection(mAdapterBookedUser.getCount() - 1);
                    }

                    page_no++;

                }
            } else if (result.equalsIgnoreCase("false")) {

                if (mArrayBookedUser.size() == 0) {
                    //mCommon.displayAlert(mActivity, "" + jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                    Toast.makeText(Tenant_Records_FirstActivity.this,message, Toast.LENGTH_LONG).show();

                    page_no=0;
                } else {
                    mCommon.displayAlert(Tenant_Records_FirstActivity.this, "" + context.getResources()
                            .getString(R.string.str_no_more_data), false);
                }

            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(context, Attandance_Home_Activity.class);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("device",devicetype);
                startActivity(intent);

                break;
        }
        return super.onKeyDown(keyCode, event);
    }


}
