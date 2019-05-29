package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.adapter.WardenFeedbackListAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Feedbackdetailowner;
import com.krooms.hostel.rental.property.app.modal.Feedbackdetailownernew;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Owner_Feedbackcountmodel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by ashish on 19/1/17.
 */

public class Warden_Feedback_Activity extends AppCompatActivity implements View.OnClickListener {

    String value = "true";
    RelativeLayout backarrow, btnsendComplaint;
    ListView listView;
    public WardenFeedbackListAdapter wardenFeedbackListAdapter;
    ScrollView laysendcompaint;
    Context lcontext;
    String result, useridvalue, propertyidvalue_new, propertyownerid;
    public ArrayList<Feedbackdetailowner> arraylistdetail;
    public ArrayList<Feedbackdetailownernew> arraylistdetailnew;
    public ArrayList<Owner_Feedbackcountmodel> countarray;
    ArrayList<String> OwnerIdlist;
    SharedStorage mShared;
    Button sendcompalintsubmit;
    TextView tenantname, txtcomplainthead;
    EditText editcomplaint;
    Dialog complaintype, Tenantnamedailog;
    ImageView student_loader;
    Animation rotation;
    ListView student_list_dialog;
    LinearLayout laytenantname, laycomplainthead;
    String message, datetime;
    String TenantName, Complaint, ComplaintType;
    String settenant_name, studenttanentid, studenttanentpropertyid, studenttenantuserid, tenantownerid, tenantroomno, parentid;
    ArrayList<OwnerStudentNameModel> ownerstudentarraylist;
    OwnerStudentNameTanentAdapter adapter_data_month;
    OwnerStudentNameModel ownerstudentmodel;
    String wardenid, wardenname;

    String[] complaint = {
            "Payment",
            "Fine",
            "Fees",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_feedback_layout);
        lcontext = this;
        mShared = SharedStorage.getInstance(this);
        useridvalue = mShared.getUserId();
        Intent ii = getIntent();
        propertyownerid = ii.getStringExtra("Ownerid");
        propertyidvalue_new = ii.getStringExtra("Propertyid");
        wardenid = ii.getStringExtra("Wardenid");
        wardenname = ii.getStringExtra("Wardenname");
        OwnerIdlist = new ArrayList<>();
        arraylistdetail = new ArrayList<>();
        countarray = new ArrayList<>();
        arraylistdetailnew = new ArrayList<>();
        backarrow = (RelativeLayout) findViewById(R.id.flback_button);
        btnsendComplaint = (RelativeLayout) findViewById(R.id.btn_headericonfeedbackowner);
        laysendcompaint = (ScrollView) findViewById(R.id.layoutfeedbacksendowner);
        laytenantname = (LinearLayout) findViewById(R.id.feedbacktenantnamelayowner);
        laycomplainthead = (LinearLayout) findViewById(R.id.feedbacktypelayoutowner);
        listView = (ListView) findViewById(R.id.flistview);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        tenantname = (TextView) findViewById(R.id.txtfeedtenantnameowner);
        txtcomplainthead = (TextView) findViewById(R.id.txtfeedbacktypeowner);
        editcomplaint = (EditText) findViewById(R.id.feedbackmsgowner);
        sendcompalintsubmit = (Button) findViewById(R.id.submit_feedbackowner);
        txtcomplainthead.setText("Select Feedback Type");
        tenantname.setText("Tenant Name");
        new GetTenantComplaint().execute();
        btnsendComplaint.setOnClickListener(this);
        laycomplainthead.setOnClickListener(this);
        laytenantname.setOnClickListener(this);
        sendcompalintsubmit.setOnClickListener(this);
        DateFormat df = new SimpleDateFormat("d/MM/yyyy,HH:mm:ss");
        datetime = df.format(Calendar.getInstance().getTime());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Warden_Feedback_Activity.this, Warden_Feedback_Chat_Activity.class);
                String Id = arraylistdetail.get(position).getFeedback_id();
                intent.putExtra("FeedbackId", arraylistdetail.get(position).getFeedback_id());
                intent.putExtra("OwnerId", arraylistdetail.get(position).getOwner_id());
                intent.putExtra("PropertyId", arraylistdetail.get(position).getProperty_id());
                intent.putExtra("Title", arraylistdetail.get(position).getFeedbacktitle());
                intent.putExtra("Description", arraylistdetail.get(position).getFeedbackdescription());
                intent.putExtra("TenantName", arraylistdetail.get(position).getTenant_name());
                intent.putExtra("UserId", arraylistdetail.get(position).getUser_id());
                intent.putExtra("TenantId", arraylistdetail.get(position).getTenant_id());
                intent.putExtra("ParentId", arraylistdetail.get(position).getParent_id());
                intent.putExtra("WardenId", arraylistdetail.get(position).getWarden_id());
                intent.putExtra("Wardenname", wardenname);
                startActivity(intent);

            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Warden_Feedback_Activity.this, HostelListActivity.class);
                startActivity(ii);

            }
        });

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btn_headericonfeedbackowner:
                if (value.equals("true")) {
                    laysendcompaint.setVisibility(View.VISIBLE);
                    value = "false";
                } else {
                    laysendcompaint.setVisibility(View.GONE);
                    value = "true";
                }
                break;

            case R.id.feedbacktenantnamelayowner:
                GetTenantName();
                break;

            case R.id.submit_feedbackowner:
                SendFeedbacktoserver();
                break;

            case R.id.feedbacktypelayoutowner:
                getFeedbackheadDialog();
                break;

        }
    }

    private void SendFeedbacktoserver() {

        TenantName = tenantname.getText().toString().trim();
        ComplaintType = txtcomplainthead.getText().toString().trim();
        Complaint = editcomplaint.getText().toString().trim();

        if (TenantName.equals("Tenant Name")) {

            Toast.makeText(this, "Please Select Tenant", Toast.LENGTH_SHORT).show();

        } else if (ComplaintType.equals("Select Feedback Type")) {

            Toast.makeText(this, "Please Select Feedback Type", Toast.LENGTH_SHORT).show();

        } else if (Complaint.length() == 0) {

            Toast.makeText(this, "Please Enter Feedback", Toast.LENGTH_SHORT).show();

        } else {

            new SendOwnerFeedback().execute();
        }

    }

    private void GetTenantName() {


        Tenantnamedailog = new Dialog(Warden_Feedback_Activity.this);
        Tenantnamedailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Tenantnamedailog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) Tenantnamedailog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Tenant Name");
        student_list_dialog = (ListView) Tenantnamedailog.findViewById(R.id.list_country);
        student_loader = (ImageView) Tenantnamedailog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) Tenantnamedailog.findViewById(R.id.country_cross_layout);
        if (NetworkConnection.isConnected(Warden_Feedback_Activity.this)) {
            new StudentNameJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tenantnamedailog.dismiss();
            }
        });
        student_list_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                settenant_name = ownerstudentarraylist.get(position).getTanentusername().toString();
                studenttanentid = ownerstudentarraylist.get(position).getTanentid().toString();
                studenttanentpropertyid = ownerstudentarraylist.get(position).getTanentpropertyid().toString();
                studenttenantuserid = ownerstudentarraylist.get(position).getTanentuserid().toString();
                tenantownerid = ownerstudentarraylist.get(position).getTanentlistownerid().toString();
                tenantroomno = ownerstudentarraylist.get(position).getTenantroomno();
                parentid = ownerstudentarraylist.get(position).getParentid().toString();
                tenantname.setText(settenant_name);
                Tenantnamedailog.dismiss();
            }
        });
        Tenantnamedailog.show();
    }

    public void getFeedbackheadDialog() {
        complaintype = new Dialog(Warden_Feedback_Activity.this);
        complaintype.requestWindowFeature(Window.FEATURE_NO_TITLE);
        complaintype.setContentView(R.layout.custom_dialog_country);
        ListView month_list = (ListView) complaintype.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Warden_Feedback_Activity.this, complaint);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout = (RelativeLayout) complaintype.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintype.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String feedback_value = complaint[position].toString();
                txtcomplainthead.setText(feedback_value);
                complaintype.dismiss();
            }
        });

        complaintype.show();
    }


    public class GetTenantComplaint extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Warden_Feedback_Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getwardenfeedbacklist"));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyidvalue_new));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", propertyownerid));
            listNameValuePairs.add(new BasicNameValuePair("warden_id", wardenid));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        JSONArray jsonArraycount = obj.getJSONArray("record");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Feedbackdetailowner commodel = new Feedbackdetailowner();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            commodel.setFeedback_id(jsonObject.getString("feedback_id"));
                            commodel.setOwner_id(jsonObject.getString("owner_id"));//6
                            commodel.setProperty_id(jsonObject.getString("property_id"));//4
                            commodel.setFeedbacktitle(jsonObject.getString("title"));
                            commodel.setFeedbackdescription(jsonObject.getString("description"));
                            commodel.setTenant_name(jsonObject.getString("tenant_name"));
                            commodel.setUser_id(jsonObject.getString("user_id"));
                            commodel.setStatus(jsonObject.getString("status"));
                            commodel.setTenant_roomno(jsonObject.getString("room_no"));
                            commodel.setDate(jsonObject.getString("date"));
                            commodel.setCheck_reply(jsonObject.getString("check_reply"));
                            commodel.setTenant_id(jsonObject.getString("tenant_id"));
                            commodel.setParent_id(jsonObject.getString("parent_id"));
                            String ownerfname = jsonObject.getString("fname");
                            String ownerlname = jsonObject.getString("lname");
                            String name = ownerfname + " " + ownerlname;
                            commodel.setOwner_name(name);
                            commodel.setWarden_id(jsonObject.getString("warden_id"));
                            arraylistdetail.add(commodel);
                        }

                        for (int j = 0; j < jsonArraycount.length(); j++) {
                            Owner_Feedbackcountmodel tmodel = new Owner_Feedbackcountmodel();
                            JSONObject jsonObject1 = jsonArraycount.getJSONObject(j);
                            tmodel.setOFeedback_id(jsonObject1.getString("feedback_id"));
                            tmodel.setOtotalcount(jsonObject1.getString("total"));
                            countarray.add(tmodel);
                        }
                        for (int n = 0; n < arraylistdetail.size(); n++) {
                            String CId = arraylistdetail.get(n).getFeedback_id();
                            Feedbackdetailownernew detailmodel = new Feedbackdetailownernew();
                            for (int count = 0; count < countarray.size(); count++) {
                                String nId = countarray.get(count).getOFeedback_id();
                                if (CId.equals(nId)) {
                                    detailmodel.setTotal_countnew(countarray.get(count).getOtotalcount());
                                }
                            }
                            detailmodel.setFeedback_idnew(arraylistdetail.get(n).getFeedback_id());
                            detailmodel.setOwner_idnew(arraylistdetail.get(n).getOwner_id());
                            detailmodel.setProperty_idnew(arraylistdetail.get(n).getProperty_id());
                            detailmodel.setFeedbacktitlenew(arraylistdetail.get(n).getFeedbacktitle());
                            detailmodel.setFeedbackdescriptionnew(arraylistdetail.get(n).getFeedbackdescription());
                            detailmodel.setUser_idnew(arraylistdetail.get(n).getUser_id());
                            detailmodel.setStatusnew(arraylistdetail.get(n).getStatus());
                            detailmodel.setTenant_idnew(arraylistdetail.get(n).getTenant_id());
                            detailmodel.setCheck_replynew(arraylistdetail.get(n).getCheck_reply());
                            detailmodel.setTenant_namenew(arraylistdetail.get(n).getTenant_name());
                            detailmodel.setOwner_namenew(arraylistdetail.get(n).getOwner_name());
                            detailmodel.setDatenew(arraylistdetail.get(n).getDate());
                            detailmodel.setTenant_roomnonew(arraylistdetail.get(n).getTenant_roomno());
                            detailmodel.setOwner_namenew(arraylistdetail.get(n).getOwner_name());
                            detailmodel.setParent_idnew(arraylistdetail.get(n).getParent_id());
                            arraylistdetailnew.add(detailmodel);
                        }
                        wardenFeedbackListAdapter = new WardenFeedbackListAdapter(lcontext, arraylistdetail, arraylistdetailnew);
                        listView.setAdapter(wardenFeedbackListAdapter);
                        wardenFeedbackListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class StudentNameJsonParse extends AsyncTask<String, String, String> {

        String name;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            student_loader.startAnimation(rotation);
            student_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);

            try {
                ownerstudentarraylist = new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "userListOfAPropertyNew"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mShared.getUserPropertyId()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        String statusvalue = jsonobj.getString("status");
                        String statusacvalue = jsonobj.getString("status_active");
                        if (statusvalue.equals("1") && statusacvalue.equals("")) {
                            ownerstudentmodel = new OwnerStudentNameModel();
                            ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname") + " " + jsonobj.getString("tenant_lname"));
                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setTanentroomid(jsonobj.getString("room_id"));
                            ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                            ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                            ownerstudentmodel.setTanentpropertyid(jsonobj.getString("property_id"));
                            ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                            ownerstudentmodel.setHiredate(jsonobj.getString("property_hire_date"));
                            ownerstudentmodel.setTenantkeyid(jsonobj.getString("key_rcu_booking"));
                            ownerstudentmodel.setParentid(jsonobj.getString("parent_id"));
                            ownerstudentarraylist.add(ownerstudentmodel);
                        }
                    }
                }
            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("true")) {
                if (ownerstudentarraylist.size() > 0) {
                    adapter_data_month = new OwnerStudentNameTanentAdapter(Warden_Feedback_Activity.this, ownerstudentarraylist);
                    student_list_dialog.setAdapter(adapter_data_month);
                }
            } else if (result.equalsIgnoreCase("false")) {
                Toast.makeText(Warden_Feedback_Activity.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    //this class used to send owner feedback to server
    private class SendOwnerFeedback extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Warden_Feedback_Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "warden_feedback"));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", studenttanentid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", studenttanentpropertyid));
            listNameValuePairs.add(new BasicNameValuePair("user_id", studenttenantuserid));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", tenantownerid));
            listNameValuePairs.add(new BasicNameValuePair("title", ComplaintType));
            listNameValuePairs.add(new BasicNameValuePair("description", Complaint));
            listNameValuePairs.add(new BasicNameValuePair("date", datetime));
            listNameValuePairs.add(new BasicNameValuePair("room_no", tenantroomno));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", parentid));
            listNameValuePairs.add(new BasicNameValuePair("tenant_name", TenantName));
            listNameValuePairs.add(new BasicNameValuePair("warden_id", wardenid));
            listNameValuePairs.add(new BasicNameValuePair("feedback_sender_name", wardenname));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent ii = new Intent(Warden_Feedback_Activity.this, Warden_Feedback_Activity.class);
                        ii.putExtra("Ownerid", propertyownerid);
                        ii.putExtra("Propertyid", propertyidvalue_new);
                        ii.putExtra("Wardenid", wardenid);
                        ii.putExtra("Wardenname", wardenname);
                        startActivity(ii);
                        hideSoftKeyboard(Warden_Feedback_Activity.this);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ii = new Intent(Warden_Feedback_Activity.this, HostelListActivity.class);
            startActivity(ii);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

