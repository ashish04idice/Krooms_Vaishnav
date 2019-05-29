package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.OwnerComplaintChatAdapter;
import com.krooms.hostel.rental.property.app.adapter.TenantComplaintChatAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Complaindetailowner;
import com.krooms.hostel.rental.property.app.modal.OwnerChatmodel;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Raghveer on 09-02-2017.
 */
public class Tenant_Complaint_Chat_Activity extends AppCompatActivity {


    String descriptionvalue;
    private ListView listView;
    private LinkedList<OwnerChatmodel> friends;
    private OwnerComplaintChatAdapter adapter;
    ArrayList<OwnerChatmodel> tenantarraylist;
    ArrayList<OwnerChatmodel> ownerarraylist;
    ArrayList<OwnerChatmodel> mainarraylist;
    OwnerChatmodel evnentmodel;
    LinearLayout send;
    EditText description;
    RelativeLayout backarrow;
    TextView sendreply,for_tenant;
    String wid;
    Intent ii;
    SharedStorage mShared;
    String PropertyId,OwnerId,ComplaintId,TenantId,TenantName,UserId,Date,ParentId,WardenId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_complaint_chat_layout);
        mShared = SharedStorage.getInstance(this);
        sendreply=(TextView)findViewById(R.id.sendreply);
        listView = (ListView)findViewById(R.id.list);
        send=(LinearLayout)findViewById(R.id.send);
        description=(EditText)findViewById(R.id.description);
        backarrow=(RelativeLayout)findViewById(R.id.back_button_left);
        for_tenant=(TextView)findViewById(R.id.for_tenant);

        DateFormat df = new SimpleDateFormat("d/MM/yyyy,HH:mm:ss");
        Date = df.format(Calendar.getInstance().getTime());
        ii= getIntent();
        PropertyId=ii.getStringExtra("PropertyId");
        OwnerId=ii.getStringExtra("OwnerId");
        TenantId=ii.getStringExtra("TenantId");
        ComplaintId=ii.getStringExtra("ComplaintId");
        TenantName=ii.getStringExtra("TenantName");
        UserId=ii.getStringExtra("UserId");
        ParentId=ii.getStringExtra("ParentId");
        WardenId=ii.getStringExtra("WardenId");
        for_tenant.setText(TenantName);

        new GetTenantSeen().execute();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Tenant_Complaint_Chat_Activity.this,Tenant_Complaint_Activity.class);
                startActivity(i);

            }
        });

        sendreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                descriptionvalue = description.getText().toString();
                if (descriptionvalue.equals("")) {

                    Toast.makeText(Tenant_Complaint_Chat_Activity.this, "please enter description", Toast.LENGTH_SHORT).show();

                } else {
                    new SendComplaintJsonCalss().execute();
                }

            }
        });

        new getEventdataJsonCalss().execute();

    }

    public class getEventdataJsonCalss extends AsyncTask<String, String, String> {
        String result = null;
        String username1, password1;
        Boolean IsError=false;
        String message;
        ImageView loader;
        ProgressDialog pd;
        String usernamevalue,passwordvalue;
        String user_forgot_email;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Tenant_Complaint_Chat_Activity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                mainarraylist=new ArrayList<OwnerChatmodel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
                nameValuePairs.add(new BasicNameValuePair("action","getcomplainconversation"));
                nameValuePairs.add(new BasicNameValuePair("property_id",PropertyId));
                nameValuePairs.add(new BasicNameValuePair("owner_id",OwnerId));
                nameValuePairs.add(new BasicNameValuePair("complain_id",ComplaintId));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",TenantId));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message=jsmain.getString("message");
                if (result.equals("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj=jsonarray.getJSONObject(i);
                        evnentmodel=new OwnerChatmodel();
                        evnentmodel.setDescription(jsonobj.getString("description"));
                        evnentmodel.setMessage(jsonobj.getString("check_reply"));
                        String checkvalue=jsonobj.getString("check_reply");
                        String fnamevalue=jsonobj.getString("fname");
                        String lnamevalue=jsonobj.getString("lname");
                        evnentmodel.setSender_name(jsonobj.getString("sender_name_first"));
                        evnentmodel.setName(fnamevalue+" "+lnamevalue);
                        mainarraylist.add(evnentmodel);
                    }

                }

            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String th) {
            pd.dismiss();
            if (!IsError)
            {
                if(result.equals("Y")) {
                    TenantComplaintChatAdapter adapter=new TenantComplaintChatAdapter(Tenant_Complaint_Chat_Activity.this,mainarraylist);
                    listView.setAdapter(adapter);
                }else
                {
                    Toast.makeText(Tenant_Complaint_Chat_Activity.this,message, Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(Tenant_Complaint_Chat_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

//for Tenantside
    public class SendComplaintJsonCalss extends AsyncTask<String, String, String> {
        String result = null;
        String username1, password1;
        Boolean IsError=false;
        String message;
        ImageView loader;
        ProgressDialog pd;
        String usernamevalue,passwordvalue;
        String user_forgot_email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Tenant_Complaint_Chat_Activity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                tenantarraylist=new ArrayList<OwnerChatmodel>();
                ownerarraylist=new ArrayList<OwnerChatmodel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
                nameValuePairs.add(new BasicNameValuePair("action","tenantcomplain_reply"));
                nameValuePairs.add(new BasicNameValuePair("property_id",PropertyId));
                nameValuePairs.add(new BasicNameValuePair("owner_id",OwnerId));
                nameValuePairs.add(new BasicNameValuePair("complain_id",ComplaintId));
                nameValuePairs.add(new BasicNameValuePair("user_id",UserId));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",TenantId));
                nameValuePairs.add(new BasicNameValuePair("description",descriptionvalue));
                nameValuePairs.add(new BasicNameValuePair("date",Date));
                nameValuePairs.add(new BasicNameValuePair("parent_id",ParentId));
                nameValuePairs.add(new BasicNameValuePair("warden_id",WardenId));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message=jsmain.getString("message");
                if (result.equals("Y")) {

                }

            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String th) {
            pd.dismiss();
            if (!IsError)
            {
                    Toast.makeText(Tenant_Complaint_Chat_Activity.this,""+message, Toast.LENGTH_SHORT).show();
                    description.setText("");
                    Intent i=new Intent(Tenant_Complaint_Chat_Activity.this,Tenant_Complaint_Chat_Activity.class);
                    i.putExtra("ComplaintId",ComplaintId);
                    i.putExtra("OwnerId",OwnerId);
                    i.putExtra("PropertyId",PropertyId);
                    i.putExtra("Title","");
                    i.putExtra("Description", "");
                    i.putExtra("TenantName", TenantName);
                    i.putExtra("UserId",UserId);
                    i.putExtra("TenantId",TenantId);
                    i.putExtra("Date","");
                    i.putExtra("ParentId",ParentId);
                    i.putExtra("WardenId",WardenId);
                    startActivity(i);
            }
            else
            {
                Toast.makeText(Tenant_Complaint_Chat_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
//

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent i=new Intent(Tenant_Complaint_Chat_Activity.this,Tenant_Complaint_Activity.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    //this class used to update complaint seen status

    public class GetTenantSeen extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Tenant_Complaint_Chat_Activity.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action","tenant_read_msg"));
            listNameValuePairs.add(new BasicNameValuePair("complain_id",ComplaintId));
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Complaindetailowner commodel=new Complaindetailowner();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //


}
