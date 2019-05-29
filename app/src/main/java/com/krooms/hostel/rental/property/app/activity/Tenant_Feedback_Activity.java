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
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.adapter.Tenant_Show_FeedbackList_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Feedbackdetailtenant;
import com.krooms.hostel.rental.property.app.modal.Feedbackdetailtenantnew;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Tenant_Feedbackcountmodel;
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


public class Tenant_Feedback_Activity extends AppCompatActivity implements View.OnClickListener {

    Dialog complaintype,tenantnamedialog;
    LinearLayout complainspinner,tenantnamespinner;
    ScrollView feedbacksendscroll;
    RelativeLayout backarrow,layshowfeedbackform;
    TextView txtcomplainspinner,txttenantname;
    EditText edittxtfeedbackdes;
    Button btnsendfeedback;
    ImageView student_loader;
    ListView student_list_dialog;
    ListView listViewfeedback;
    Animation rotation;
    Context context;
    String date,textcomptitle,Tenantname,editcompdescription,result="",useridvalue,settenant_name,studenttanentid,studenttanentpropertyid,studenttenantuserid,tenantownerid,tenantroomno,parentid;
    SharedStorage mShared;
    ArrayList<OwnerStudentNameModel> ownerstudentarraylist;
    Tenant_Show_FeedbackList_Adapter listviewshowfeedbacktadapter;
    String propertyidvalue_new,propertyownerid,value="true";
    public ArrayList<Feedbackdetailtenant> arraylistdetail;
    public ArrayList<Feedbackdetailtenantnew> arraylistdetailnew;
    public ArrayList<Tenant_Feedbackcountmodel> countarray;

    String warden_id;

    String[] FeedbackType = {
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
        setContentView(R.layout.tenant_feedback_layout);
        mShared = SharedStorage.getInstance(this);
        useridvalue=mShared.getUserId();
        context=this;
        arraylistdetail=new ArrayList<>();
        arraylistdetailnew=new ArrayList<>();
        countarray=new ArrayList<>();
        complainspinner = (LinearLayout) findViewById(R.id.feedbacktypelayout);
        tenantnamespinner=(LinearLayout)findViewById(R.id.feedbacktenantnamelay);
        feedbacksendscroll=(ScrollView)findViewById(R.id.scrollviewfeedbacksend);
        txtcomplainspinner = (TextView) findViewById(R.id.txtfeedbacktype);
        listViewfeedback=(ListView)findViewById(R.id.listviewshowfeedback);
        txttenantname=(TextView)findViewById(R.id.txtfeedbacktenantname);
        btnsendfeedback = (Button) findViewById(R.id.submit_feedbackbtn);
        backarrow = (RelativeLayout) findViewById(R.id.ftback_button_left);
        layshowfeedbackform=(RelativeLayout)findViewById(R.id.btn_headericonfeedback);
        edittxtfeedbackdes = (EditText) findViewById(R.id.feedbackmsg);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        txtcomplainspinner.setText("Select Feedback Type");
        txttenantname.setText("Tenant Name");

        new GetOwnerFeedbackList().execute();

        complainspinner.setOnClickListener(this);
        btnsendfeedback.setOnClickListener(this);
        layshowfeedbackform.setOnClickListener(this);
        backarrow.setOnClickListener(this);
        tenantnamespinner.setOnClickListener(this);
        DateFormat df = new SimpleDateFormat("d/MM/yyyy,HH:mm:ss");
        date = df.format(Calendar.getInstance().getTime());

        listViewfeedback.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Intent intent=new Intent(Tenant_Feedback_Activity.this,Tenant_Feedback_Chat_Activity.class);
                intent.putExtra("OwnerId",arraylistdetail.get(position).getOwner_id());
                intent.putExtra("PropertyId",arraylistdetail.get(position).getOwner_Propertyid());
                intent.putExtra("Title",arraylistdetail.get(position).getOwner_feedbacktype());
                intent.putExtra("Description",arraylistdetail.get(position).getOwner_feedbackdescription());
                intent.putExtra("TenantName",arraylistdetail.get(position).getTenantname());
                intent.putExtra("UserId",arraylistdetail.get(position).getOwner_userid());
                intent.putExtra("TenantId",arraylistdetail.get(position).getTenant_id());
                intent.putExtra("Date", arraylistdetail.get(position).getDate());
                intent.putExtra("FeedbackId", arraylistdetail.get(position).getFeedback_id());
                intent.putExtra("ParentId",arraylistdetail.get(position).getParent_id());
                intent.putExtra("WardenId",arraylistdetail.get(position).getWarden_id());
                startActivity(intent);


            }
        });

    }


    // This function used to show Feedbacktype listdailog
    public void FeedbackTypedialogMethod() {
        complaintype = new Dialog(Tenant_Feedback_Activity.this);
        complaintype.requestWindowFeature(Window.FEATURE_NO_TITLE);
        complaintype.setContentView(R.layout.custom_dialog_country);
        ListView month_list = (ListView) complaintype.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Tenant_Feedback_Activity.this,FeedbackType);
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
                String comp_value = FeedbackType[position].toString();
                txtcomplainspinner.setText(comp_value);
                complaintype.dismiss();
            }
        });

        complaintype.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.feedbacktypelayout:

                FeedbackTypedialogMethod();

                break;

            case R.id.feedbacktenantnamelay:

                studentdatadialogMethod();

                break;

            case R.id.submit_feedbackbtn:

                sendtoserver();

                break;

            case R.id.ftback_button_left:

                Intent ii=new Intent(Tenant_Feedback_Activity.this,HostelListActivity.class);
                startActivity(ii);

                break;

            case R.id.btn_headericonfeedback:

                if(value.equals("true")){
                    feedbacksendscroll.setVisibility(View.VISIBLE);
                    value="false";
                }
                else
                {
                    feedbacksendscroll.setVisibility(View.GONE);
                    value="true";
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

    // This function used to get tenantname list
    private void studentdatadialogMethod() {
        tenantnamedialog=new Dialog(Tenant_Feedback_Activity.this);
        tenantnamedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tenantnamedialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading=(TextView)tenantnamedialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Tenant Name");
        student_list_dialog=(ListView)tenantnamedialog.findViewById(R.id.list_country);
        student_loader=(ImageView)tenantnamedialog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout=(RelativeLayout)tenantnamedialog.findViewById(R.id.country_cross_layout);

        if(NetworkConnection.isConnected(Tenant_Feedback_Activity.this))
        {
            new StudentNameJsonParse().execute();
        }else
        {
            Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenantnamedialog.dismiss();
            }
        });
        student_list_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                settenant_name=ownerstudentarraylist.get(position).getTanentusername().toString();
                studenttanentid=ownerstudentarraylist.get(position).getTanentid().toString();
                studenttanentpropertyid=ownerstudentarraylist.get(position).getTanentpropertyid().toString();
                studenttenantuserid=ownerstudentarraylist.get(position).getTanentuserid().toString();
                tenantownerid=ownerstudentarraylist.get(position).getTanentlistownerid().toString();
                tenantroomno=ownerstudentarraylist.get(position).getTenantroomno();
                parentid=ownerstudentarraylist.get(position).getParentid().toString();
                warden_id=ownerstudentarraylist.get(position).getWarden_id().toString();
                //System.out.println(studenttenantIdNew);
                txttenantname.setText(settenant_name);
                tenantnamedialog.dismiss();
            }
        });
        tenantnamedialog.show();
    }

    // This function used to validate tenant name feedback type feedback description
    private void sendtoserver() {

        textcomptitle = txtcomplainspinner.getText().toString();
        Tenantname   =  txttenantname.getText().toString();
        editcompdescription = edittxtfeedbackdes.getText().toString().trim();

        if (Tenantname.equals("Tenant Name")) {

            Toast.makeText(this,"Please Select Tenant", Toast.LENGTH_SHORT).show();

        }else if (textcomptitle.equals("Select Feedback Type")) {

            Toast.makeText(this, "Please Select Feedback Type", Toast.LENGTH_SHORT).show();

        } else if (editcompdescription.length() == 0) {

            Toast.makeText(this, "Please Enter Feedback", Toast.LENGTH_SHORT).show();

        } else {

           new SendFeedbacktoserver().execute();
        }

    }


// this class is used to send TenantFeedback to server
    public class SendFeedbacktoserver extends AsyncTask<String, String, String> {

        String wid;
        private ProgressDialog dialog = new ProgressDialog(Tenant_Feedback_Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
            mShared = SharedStorage.getInstance(context);

        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action","tenant_feedback"));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id",studenttanentid));
            listNameValuePairs.add(new BasicNameValuePair("property_id",studenttanentpropertyid));
            listNameValuePairs.add(new BasicNameValuePair("user_id",studenttenantuserid));
            listNameValuePairs.add(new BasicNameValuePair("owner_id",tenantownerid));
            listNameValuePairs.add(new BasicNameValuePair("title",textcomptitle));
            listNameValuePairs.add(new BasicNameValuePair("description",editcompdescription));
            listNameValuePairs.add(new BasicNameValuePair("date",date));
            listNameValuePairs.add(new BasicNameValuePair("tenant_name",Tenantname));
            listNameValuePairs.add(new BasicNameValuePair("room_no",tenantroomno));
            listNameValuePairs.add(new BasicNameValuePair("parent_id",parentid));
            listNameValuePairs.add(new BasicNameValuePair("warden_id",warden_id));
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
                        Intent ii=new Intent(Tenant_Feedback_Activity.this,Tenant_Feedback_Activity.class);
                        startActivity(ii);
                        hideSoftKeyboard(Tenant_Feedback_Activity.this);


                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }


    // this is jsonparse class through these class we get student name
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
            try
            {
                ownerstudentarraylist=new ArrayList<>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action","getTanentInfoByUserId"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mShared.getBookedPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("user_id",useridvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain =new JSONObject(objectmain);
                result = jsmain.getString("flag");
                // message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarraydata = jsmain.getJSONArray("data");
                    for (int i = 0; i <jsonarraydata.length(); i++)
                    {
                        JSONObject jsonobj =jsonarraydata.getJSONObject(i);
                        String statusvalue=jsonobj.getString("status");
                        String statusacvalue=jsonobj.getString("status_active");
                        if(statusvalue.equals("1")&&statusacvalue.equals(""))
                        {
                            OwnerStudentNameModel ownerstudentmodel = new OwnerStudentNameModel();
                            ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname")+" "+jsonobj.getString("tenant_lname"));
                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                            ownerstudentmodel.setTanentpropertyid(jsonobj.getString("property_id"));
                            ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                            ownerstudentmodel.setParentid(jsonobj.getString("parent_id"));
                            ownerstudentmodel.setWarden_id(jsonobj.getString("warden_id"));
                            ownerstudentarraylist.add(ownerstudentmodel);
                        }
                    }


                    JSONArray jsonarray = jsmain.getJSONArray("user_records");
                    for (int i = 0; i <jsonarray.length(); i++)
                    {
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        String statusvalue=jsonobj.getString("status");
                        String statusacvalue=jsonobj.getString("status_active");
                        if(statusvalue.equals("1")&&statusacvalue.equals(""))
                        {
                            if (! useridvalue.equals(jsonobj.getString("user_id")))
                            {
                                OwnerStudentNameModel ownerstudentmodel = new OwnerStudentNameModel();
                                ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname") + " " + jsonobj.getString("tenant_lname"));
                                ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                                ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                                ownerstudentmodel.setTanentpropertyid(jsonobj.getString("property_id"));
                                ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                                ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                                ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                                ownerstudentmodel.setParentid(jsonobj.getString("parent_id"));
                                ownerstudentmodel.setWarden_id(jsonobj.getString("warden_id"));
                                ownerstudentarraylist.add(ownerstudentmodel);
                            }
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
            if (!IsError) {
                if (result.equalsIgnoreCase("Y"))
                {
                    if (ownerstudentarraylist.size() > 0)
                    {
                        OwnerStudentNameTanentAdapter adapter_student = new OwnerStudentNameTanentAdapter(Tenant_Feedback_Activity.this,ownerstudentarraylist);
                        student_list_dialog.setAdapter(adapter_student);
                    }
                } else
                if (result.equalsIgnoreCase("N"))
                {
                    Toast.makeText(Tenant_Feedback_Activity.this,"No Tenant Found", Toast.LENGTH_LONG).show();
                }
            }
            else {
            }
        }
    }


    //this class used to get feedbacklist
    public class GetOwnerFeedbackList extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Tenant_Feedback_Activity.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action","gettenantfeedbacklist"));
            listNameValuePairs.add(new BasicNameValuePair("user_id",useridvalue));
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
                            Feedbackdetailtenant commodel=new Feedbackdetailtenant();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            commodel.setFeedback_id(jsonObject.getString("feedback_id"));
                            commodel.setOwner_id(jsonObject.getString("owner_id"));
                            commodel.setOwner_Propertyid(jsonObject.getString("property_id"));
                            commodel.setOwner_feedbacktype(jsonObject.getString("title"));
                            commodel.setOwner_feedbackdescription(jsonObject.getString("description"));
                            commodel.setId(jsonObject.getString("id"));
                            commodel.setStatus(jsonObject.getString("status"));
                            commodel.setCheck_Reply(jsonObject.getString("check_reply"));
                            commodel.setTenant_roomno(jsonObject.getString("room_no"));
                            commodel.setTenantname(jsonObject.getString("tenant_name"));
                            commodel.setTenant_id(jsonObject.getString("tenant_id"));
                            String ownerfname=jsonObject.getString("fname");
                            String ownerlname=jsonObject.getString("lname");
                            String name=ownerfname+" "+ownerlname;
                            commodel.setOwner_name(name);
                            commodel.setOwner_userid(jsonObject.getString("user_id"));
                            commodel.setParent_id(jsonObject.getString("parent_id"));
                            commodel.setWarden_id(jsonObject.getString("warden_id"));
                           // commodel.setSendername(jsonObject.getString(""));
                            commodel.setDate(jsonObject.getString("date"));
                            arraylistdetail.add(commodel);

                        }

                        for(int j=0;j<jsonArraycount.length();j++){
                            Tenant_Feedbackcountmodel tmodel=new Tenant_Feedbackcountmodel();
                            JSONObject jsonObject1 = jsonArraycount.getJSONObject(j);
                            tmodel.setTfeedback_id(jsonObject1.getString("feedback_id"));
                            tmodel.setTtotalcount(jsonObject1.getString("total"));
                            countarray.add(tmodel);
                        }


                        //change
                        for(int n=0;n<arraylistdetail.size();n++){
                            String CId=arraylistdetail.get(n).getFeedback_id();
                            Feedbackdetailtenantnew detailmodel=new Feedbackdetailtenantnew();

                            for(int count=0;count<countarray.size();count++){

                                String nId=countarray.get(count).getTfeedback_id();
                                if(CId.equals(nId)){
                                    detailmodel.setOwnercounttotalnew(countarray.get(count).getTtotalcount());
                                }
                            }

                            detailmodel.setFeedback_idnew(arraylistdetail.get(n).getFeedback_id());
                            detailmodel.setOwner_idnew(arraylistdetail.get(n).getOwner_id());
                            detailmodel.setOwner_Propertyidnew(arraylistdetail.get(n).getOwner_Propertyid());
                            detailmodel.setOwner_feedbacktypenew(arraylistdetail.get(n).getOwner_feedbacktype());
                            detailmodel.setOwner_feedbackdescriptionnew(arraylistdetail.get(n).getOwner_feedbackdescription());
                            detailmodel.setIdnew(arraylistdetail.get(n).getId());
                            detailmodel.setStatusnew(arraylistdetail.get(n).getStatus());
                            detailmodel.setTenant_idnew(arraylistdetail.get(n).getTenant_id());
                            detailmodel.setCheck_Replynew(arraylistdetail.get(n).getCheck_Reply());
                            detailmodel.setTenantnamenew(arraylistdetail.get(n).getTenantname());
                            detailmodel.setOwner_namenew(arraylistdetail.get(n).getOwner_name());
                            detailmodel.setOwner_useridnew(arraylistdetail.get(n).getOwner_userid());
                            detailmodel.setDatenew(arraylistdetail.get(n).getDate());
                            detailmodel.setTenant_roomnonew(arraylistdetail.get(n).getTenant_roomno());
                            detailmodel.setParent_idnew(arraylistdetail.get(n).getParent_id());
                          //  detailmodel.setSender_name_main(arraylistdetail.get(n).getSendername());
                            arraylistdetailnew.add(detailmodel);
                        }

                        listviewshowfeedbacktadapter=new Tenant_Show_FeedbackList_Adapter(context,arraylistdetail,arraylistdetailnew);
                        listViewfeedback.setAdapter(listviewshowfeedbacktadapter);
                        listviewshowfeedbacktadapter.notifyDataSetChanged();

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

        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent ii=new Intent(Tenant_Feedback_Activity.this,HostelListActivity.class);
            startActivity(ii);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
