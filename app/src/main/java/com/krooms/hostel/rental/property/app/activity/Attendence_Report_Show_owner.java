package com.krooms.hostel.rental.property.app.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.adapter.Attendence_ListAdapter;
import com.krooms.hostel.rental.property.app.adapter.Attendence_Show_Owner_End_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.WebUrls;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuradhaidice on 23/12/16.
 */

public class Attendence_Report_Show_owner extends Activity {
    ListView roomno_listview;
    ArrayList<Attendence_Modal> roomno_arraylist;
    Attendence_ListAdapter adapter;
    TextView date_textview, time_textview;
    Button downloadslip;
    SharedStorage mSharedStorage;
    Animation rotation;
    ImageView loader;
    String owneridmainvalue;
    Intent in;
    String datevaluemain;
    RelativeLayout Loginattendece;
    TextView emailid_tenant, attendce_heading;
    String monthvalue;
    ArrayList<Attendence_Modal> show_arraylist;
    Attendence_Modal attendence_modal;
    String usernamevale, attendecetype, useriddata_value, tid_value, propertyid_data_value, propertyname_value;
    RelativeLayout back_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_report_layout_owner);
        in = getIntent();
        datevaluemain = in.getStringExtra("datevalue");
        attendecetype = in.getStringExtra("attendecetype");
        propertyid_data_value = in.getStringExtra("property_id_data");
        attendce_heading = (TextView) findViewById(R.id.attendce_heading);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        loader = (ImageView) findViewById(R.id.student_loader);
        roomno_listview = (ListView) findViewById(R.id.listview_attendence);
        date_textview = (TextView) findViewById(R.id.date_textview);
        time_textview = (TextView) findViewById(R.id.time_textview);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        Loginattendece = (RelativeLayout) findViewById(R.id.Loginattendece);

        Loginattendece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Attendence_Report_Show_owner.this, Tenant_Records_show_in_tablayout_Report_value.class);
                i.putExtra("propertyid", propertyid_data_value);
                i.putExtra("attendecetype", attendecetype);
                i.putExtra("datevalue", datevaluemain);
                startActivity(i);
            }
        });

        if (attendecetype.equalsIgnoreCase("food")) {
            attendce_heading.setText("Food Attendance");
        }else if(attendecetype.equalsIgnoreCase("manual")){
            attendce_heading.setText("Manual Attendance");
        }
        else {
            attendce_heading.setText("Biometric Attendance");
        }
        date_textview.setText(datevaluemain);
        new AttendenceShowdate_Jsonarray().execute();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }
    //this class is used getting value by webservices
    private class AttendenceShowdate_Jsonarray extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_ATTENDACE_URL);
            Log.d("uu 1", "" + post);
            try {
                show_arraylist = new ArrayList<Attendence_Modal>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action","ownergettenantattendence"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_data_value));
                nameValuePairs.add(new BasicNameValuePair("attendecetype", attendecetype));
                nameValuePairs.add(new BasicNameValuePair("date", datevaluemain));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        attendence_modal = new Attendence_Modal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        attendence_modal.setAttendencetenantname(jsonobj.getString("tenant_name"));
                        attendence_modal.setAttendenceroom_no(jsonobj.getString("room_no"));
                        attendence_modal.setTime(jsonobj.getString("time"));
                        attendence_modal.setAttendecstatus(jsonobj.getString("attendence_status"));
                        attendence_modal.setAbsent_present(jsonobj.getString("attendence"));
                        show_arraylist.add(attendence_modal);
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
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                Attendence_Show_Owner_End_Adapter adapter = new Attendence_Show_Owner_End_Adapter(Attendence_Report_Show_owner.this, show_arraylist);
                roomno_listview.setAdapter(adapter);
            } else if (result.equals("N")) {
                Toast.makeText(Attendence_Report_Show_owner.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
