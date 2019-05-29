package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.krooms.hostel.rental.property.app.adapter.ListShowParentcomplaintAdapter;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Complaintdetailtenant;
import com.krooms.hostel.rental.property.app.modal.Complaintdetailtenantnew;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Tenant_Complaintcountmodel;
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


public class Parent_Complaint_Activity extends AppCompatActivity implements View.OnClickListener {

    Dialog complaintype, month_dialog;
    LinearLayout complainspinner, tenantnamespinner;
    ScrollView complaintsend;
    RelativeLayout backarrow, btnviewcomplaint;
    TextView txtcomplainspinner, txttenantname;
    EditText editcomplain;
    Button btnsendcomplain;
    ImageView student_loader;
    ListView student_list_dialog;
    ListView listViewcompint;
    Animation rotation;
    Context context;
    String date, textcomptitle, Tenantname, editcompdescription, result = "", useridvalue, settenant_name, studenttanentid, studenttanentpropertyid, studenttenantuserid, tenantownerid, tenantroomno, parentid;
    SharedStorage mShared;
    ArrayList<OwnerStudentNameModel> ownerstudentarraylist;
    ListShowParentcomplaintAdapter listviewshowcomplaintadapter;
    String propertyidvalue_new, propertyownerid, value = "true";
    public ArrayList<Complaintdetailtenant> arraylistdetail;
    public ArrayList<Complaintdetailtenantnew> arraylistdetailnew;
    public ArrayList<Tenant_Complaintcountmodel> countarray;

    Intent in;
    String useridmainvalue, parentvalueid;
    String warden_id;

    String[] complain = {
            "Room",
            "Water",
            "Electricity",
            "Food",
            "House Keeping",
            "Other"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_complaint_layout);
        mShared = SharedStorage.getInstance(this);
        useridvalue = mShared.getUserId();
        context = this;
        in = getIntent();
        parentvalueid = in.getStringExtra("parentvalueid");
        useridvalue = in.getStringExtra("useridvalue");
        String propertyid1 = mShared.getBookedPropertyId();
        arraylistdetail = new ArrayList<>();
        arraylistdetailnew = new ArrayList<>();
        countarray = new ArrayList<>();
        complainspinner = (LinearLayout) findViewById(R.id.complainnamelayout);
        tenantnamespinner = (LinearLayout) findViewById(R.id.complaintenantnamelay);
        complaintsend = (ScrollView) findViewById(R.id.layoutcomplaintsend);
        txtcomplainspinner = (TextView) findViewById(R.id.txtcomplaintype);
        listViewcompint = (ListView) findViewById(R.id.listshowcomplaint);
        txttenantname = (TextView) findViewById(R.id.txtcomptenantname);
        btnsendcomplain = (Button) findViewById(R.id.submit_complaintbtn);
        backarrow = (RelativeLayout) findViewById(R.id.back_button_lefttcomp);
        btnviewcomplaint = (RelativeLayout) findViewById(R.id.btn_headericoncomplaint);
        editcomplain = (EditText) findViewById(R.id.complaintmsg);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        txtcomplainspinner.setText("Select Complaint Type");
        txttenantname.setText("Tenant Name");

        new GetOwnerComplainttenatside().execute();

        complainspinner.setOnClickListener(this);
        btnsendcomplain.setOnClickListener(this);
        btnviewcomplaint.setOnClickListener(this);
        backarrow.setOnClickListener(this);
        tenantnamespinner.setOnClickListener(this);

        DateFormat df = new SimpleDateFormat("d/MM/yyyy,HH:mm:ss");
        date = df.format(Calendar.getInstance().getTime());

        listViewcompint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Parent_Complaint_Activity.this, Parent_Complaint_Chat_Activity.class);
                intent.putExtra("ComplaintId", arraylistdetail.get(position).getComplaint_id());
                intent.putExtra("OwnerId", arraylistdetail.get(position).getOwner_id());
                intent.putExtra("PropertyId", arraylistdetail.get(position).getOwner_Propertyid());
                intent.putExtra("Title", arraylistdetail.get(position).getOwner_complainttype());
                intent.putExtra("Description", arraylistdetail.get(position).getOwner_complaintdescription());
                intent.putExtra("TenantName", arraylistdetail.get(position).getTenant_name());

                Log.d("Tenantname", "" + arraylistdetail.get(position).getTenant_name());
                intent.putExtra("UserId", arraylistdetail.get(position).getOwner_userid());
                intent.putExtra("TenantId", arraylistdetail.get(position).getTenant_id());
                intent.putExtra("Date", arraylistdetail.get(position).getDate());
                intent.putExtra("ParentId", arraylistdetail.get(position).getParent_id());
                intent.putExtra("WardenId", arraylistdetail.get(position).getWarden_id());
                startActivity(intent);

            }
        });

    }


    // This function used to show complainttype listdailog
    public void yeardialogMethod() {
        complaintype = new Dialog(Parent_Complaint_Activity.this);
        complaintype.requestWindowFeature(Window.FEATURE_NO_TITLE);
        complaintype.setContentView(R.layout.custom_dialog_country);
        ListView month_list = (ListView) complaintype.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Parent_Complaint_Activity.this, complain);
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
                String comp_value = complain[position].toString();
                txtcomplainspinner.setText(comp_value);
                complaintype.dismiss();
            }
        });

        complaintype.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.complainnamelayout:

                yeardialogMethod();

                break;

            case R.id.complaintenantnamelay:
                studentdatadialogMethod();
                break;

            case R.id.submit_complaintbtn:

                sendtoserver();

                break;

            case R.id.back_button_lefttcomp:

                // finish();
                Intent ii = new Intent(Parent_Complaint_Activity.this, HostelListActivity.class);
                startActivity(ii);
                break;

            case R.id.btn_headericoncomplaint:


                if (value.equals("true")) {
                    complaintsend.setVisibility(View.VISIBLE);
                    value = "false";
                } else {
                    complaintsend.setVisibility(View.GONE);
                    value = "true";
                }

                break;

        }

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void studentdatadialogMethod() {


        month_dialog = new Dialog(Parent_Complaint_Activity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Tenant Name");
        student_list_dialog = (ListView) month_dialog.findViewById(R.id.list_country);
        student_loader = (ImageView) month_dialog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);

        if (NetworkConnection.isConnected(Parent_Complaint_Activity.this)) {
            new StudentNameJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
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
                tenantroomno = ownerstudentarraylist.get(position).getTenantroomno().toString();
                parentid = ownerstudentarraylist.get(position).getParentid().toString();
                warden_id = ownerstudentarraylist.get(position).getWarden_id().toString();
                txttenantname.setText(settenant_name);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }

    // This function used to validate tenant name complaint type complaint description
    private void sendtoserver() {

        textcomptitle = txtcomplainspinner.getText().toString();
        Tenantname = txttenantname.getText().toString();
        editcompdescription = editcomplain.getText().toString().trim();

        if (Tenantname.equals("Tenant Name")) {

            Toast.makeText(this, "Please Select Tenant", Toast.LENGTH_SHORT).show();

        } else if (textcomptitle.equals("Select Complaint Type")) {

            Toast.makeText(this, "Please Select Complaint Type", Toast.LENGTH_SHORT).show();

        } else if (editcompdescription.length() == 0) {

            Toast.makeText(this, "Please Enter Complaint", Toast.LENGTH_SHORT).show();

        } else {

            new SendComplaint().execute();

        }

    }

    // this class is used to save Parent Complaint to server
    public class SendComplaint extends AsyncTask<String, String, String> {

        String wid;

        private ProgressDialog dialog = new ProgressDialog(Parent_Complaint_Activity.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action", "parent_complain"));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", studenttanentid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", studenttanentpropertyid));
            listNameValuePairs.add(new BasicNameValuePair("user_id", studenttenantuserid));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", tenantownerid));
            listNameValuePairs.add(new BasicNameValuePair("title", textcomptitle));
            listNameValuePairs.add(new BasicNameValuePair("description", editcompdescription));
            listNameValuePairs.add(new BasicNameValuePair("date", date));
            listNameValuePairs.add(new BasicNameValuePair("room_no", tenantroomno));
            listNameValuePairs.add(new BasicNameValuePair("tenant_name", Tenantname));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", parentid));
            listNameValuePairs.add(new BasicNameValuePair("complain_id", ""));
            listNameValuePairs.add(new BasicNameValuePair("warden_id", warden_id));
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
                        Intent pintent = new Intent(Parent_Complaint_Activity.this, Parent_Complaint_Activity.class);
                        pintent.putExtra("useridvalue", useridvalue);
                        pintent.putExtra("parentvalueid", parentvalueid);
                        startActivity(pintent);
                        hideSoftKeyboard(Parent_Complaint_Activity.this);

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }


    // these is jsonparse class through these class we get student name
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
                nameValuePairs.add(new BasicNameValuePair("action", "getchildlist"));
                nameValuePairs.add(new BasicNameValuePair("parent_user_id", useridvalue));
                nameValuePairs.add(new BasicNameValuePair("parent_id", parentvalueid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                if (result.equalsIgnoreCase("Y")) {
                    JSONArray datarecordsArray = jsmain.getJSONArray("data");
                    for (int i = 0; i < datarecordsArray.length(); i++) {
                        JSONObject jsonobj = datarecordsArray.getJSONObject(i);
                        String statusvalue = jsonobj.getString("status");
                        String statusacvalue = jsonobj.getString("status_active");
                        if (statusvalue.equals("1") && statusacvalue.equals("")) {
                            OwnerStudentNameModel ownerstudentmodel = new OwnerStudentNameModel();
                            ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname") + " " + jsonobj.getString("tenant_lname"));
                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                            ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                            ownerstudentmodel.setTanentpropertyid(jsonobj.getString("property_id"));
                            ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setParentid(jsonobj.getString("parent_id"));
                            ownerstudentmodel.setWarden_id(jsonobj.getString("warden_id"));
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
            if (result.equalsIgnoreCase("Y")) {
                if (ownerstudentarraylist.size() > 0) {
                    OwnerStudentNameTanentAdapter adapter_student = new OwnerStudentNameTanentAdapter(Parent_Complaint_Activity.this, ownerstudentarraylist);
                    student_list_dialog.setAdapter(adapter_student);
                }
            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(Parent_Complaint_Activity.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetOwnerComplainttenatside extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Parent_Complaint_Activity.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action", "getparentcomplainlist"));
            listNameValuePairs.add(new BasicNameValuePair("property_id", mShared.getBookedPropertyId()));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", parentvalueid));
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
                            Complaintdetailtenant commodel = new Complaintdetailtenant();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            commodel.setComplaint_id(jsonObject.getString("complain_id"));
                            commodel.setOwner_id(jsonObject.getString("owner_id"));
                            commodel.setOwner_Propertyid(jsonObject.getString("property_id"));
                            commodel.setOwner_complainttype(jsonObject.getString("title"));
                            commodel.setOwner_complaintdescription(jsonObject.getString("description"));
                            commodel.setId(jsonObject.getString("id"));
                            commodel.setStatus(jsonObject.getString("status"));
                            commodel.setTenant_id(jsonObject.getString("tenant_id"));
                            commodel.setCheck_Reply(jsonObject.getString("check_reply"));
                            commodel.setTenant_name(jsonObject.getString("tenant_name"));
                            commodel.setTenant_roomno(jsonObject.getString("room_no"));
                            String ownerfname = jsonObject.getString("fname");
                            String ownerlname = jsonObject.getString("lname");
                            String name = ownerfname + " " + ownerlname;
                            commodel.setOwner_name(name);
                            commodel.setOwner_userid(jsonObject.getString("user_id"));
                            commodel.setDate(jsonObject.getString("date"));
                            commodel.setParent_id(jsonObject.getString("parent_id"));
                            commodel.setWarden_id(jsonObject.getString("warden_id"));
                            arraylistdetail.add(commodel);
                        }

                        for (int j = 0; j < jsonArraycount.length(); j++) {
                            Tenant_Complaintcountmodel tmodel = new Tenant_Complaintcountmodel();
                            JSONObject jsonObject1 = jsonArraycount.getJSONObject(j);
                            tmodel.setTcomplaint_id(jsonObject1.getString("complain_id"));
                            tmodel.setTtotalcount(jsonObject1.getString("total"));
                            countarray.add(tmodel);
                        }


                        //change
                        for (int n = 0; n < arraylistdetail.size(); n++) {
                            String CId = arraylistdetail.get(n).getComplaint_id();
                            Complaintdetailtenantnew detailmodel = new Complaintdetailtenantnew();

                            for (int count = 0; count < countarray.size(); count++) {

                                String nId = countarray.get(count).getTcomplaint_id();
                                if (CId.equals(nId)) {
                                    detailmodel.setOwnercounttotalnew(countarray.get(count).getTtotalcount());
                                }

                            }
                            detailmodel.setComplaint_idnew(arraylistdetail.get(n).getComplaint_id());
                            detailmodel.setOwner_idnew(arraylistdetail.get(n).getOwner_id());
                            detailmodel.setOwner_Propertyidnew(arraylistdetail.get(n).getOwner_Propertyid());
                            detailmodel.setOwner_complainttypenew(arraylistdetail.get(n).getOwner_complainttype());
                            detailmodel.setOwner_complaintdescriptionnew(arraylistdetail.get(n).getOwner_complaintdescription());
                            detailmodel.setIdnew(arraylistdetail.get(n).getId());
                            detailmodel.setStatusnew(arraylistdetail.get(n).getStatus());
                            detailmodel.setTenant_idnew(arraylistdetail.get(n).getTenant_id());
                            detailmodel.setCheck_Replynew(arraylistdetail.get(n).getCheck_Reply());
                            detailmodel.setTenant_namenew(arraylistdetail.get(n).getTenant_name());
                            detailmodel.setOwner_namenew(arraylistdetail.get(n).getOwner_name());
                            detailmodel.setOwner_useridnew(arraylistdetail.get(n).getOwner_userid());
                            detailmodel.setDatenew(arraylistdetail.get(n).getDate());
                            detailmodel.setTenant_roomnonew(arraylistdetail.get(n).getTenant_roomno());
                            detailmodel.setParent_idnew(arraylistdetail.get(n).getParent_id());
                            arraylistdetailnew.add(detailmodel);
                        }
                        listviewshowcomplaintadapter = new ListShowParentcomplaintAdapter(context, arraylistdetailnew, countarray);
                        listViewcompint.setAdapter(listviewshowcomplaintadapter);
                        listviewshowcomplaintadapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ii = new Intent(Parent_Complaint_Activity.this, HostelListActivity.class);
            startActivity(ii);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
