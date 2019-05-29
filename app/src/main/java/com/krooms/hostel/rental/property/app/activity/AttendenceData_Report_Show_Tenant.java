package com.krooms.hostel.rental.property.app.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Attendence_Show_Adapter;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;

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

public class AttendenceData_Report_Show_Tenant extends Activity {
    Animation rotation;
    ImageView loader;
    Intent in;
    String datevaluemain;
    TextView emailid_tenant;
    String monthvalue;
    ArrayList<Attendence_Modal> show_arraylist;
    Attendence_Modal attendence_modal;
    ListView listview_show_attendence;
    TextView tenantnameview, roomnoview, attendce_heading;
    ImageView imageview_data;
    ImageLoader imageLoader;
    String attendecetype;
    String useridmain, tidvaluemain, propertyidvaluemain, owneridvaluemain;
    RelativeLayout back_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_report_layout);

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheSize(41943040)
                .discCacheSize(104857600)
                .threadPoolSize(20)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        in = getIntent();
        datevaluemain = in.getStringExtra("datevalue");
        String usernewvalue = in.getStringExtra("user_id_data");
        useridmain = usernewvalue;
        tidvaluemain = in.getStringExtra("tid");
        propertyidvaluemain = in.getStringExtra("property_id_data");
        owneridvaluemain = in.getStringExtra("ownerid");
        attendecetype = in.getStringExtra("attendecetype");
        emailid_tenant = (TextView) findViewById(R.id.emailid_tenant);
        attendce_heading = (TextView) findViewById(R.id.attendce_heading);
        imageview_data = (ImageView) findViewById(R.id.imageview_data);
        tenantnameview = (TextView) findViewById(R.id.tenantnameview);
        roomnoview = (TextView) findViewById(R.id.roomnoview);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        loader = (ImageView) findViewById(R.id.student_loader);
        listview_show_attendence = (ListView) findViewById(R.id.listview_show_attendence);

        if (attendecetype.equalsIgnoreCase("food")) {
            attendce_heading.setText("Food Attendance");
        } else if (attendecetype.equalsIgnoreCase("manual")) {
            attendce_heading.setText("Manual Attendance");
        } else {
            attendce_heading.setText("Biometric Attendance");
        }
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new AttendenceShowdate_Jsonarray().execute();
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
            try {
                show_arraylist = new ArrayList<Attendence_Modal>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "gettenantattendence"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyidvaluemain));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridvaluemain));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tidvaluemain));
                nameValuePairs.add(new BasicNameValuePair("user_id", useridmain));
                nameValuePairs.add(new BasicNameValuePair("date", datevaluemain));
                nameValuePairs.add(new BasicNameValuePair("attendecetype", attendecetype));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        attendence_modal = new Attendence_Modal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        attendence_modal.setDate(jsonobj.getString("date"));
                        attendence_modal.setTime(jsonobj.getString("time"));
                        attendence_modal.setAbsent_present(jsonobj.getString("attendence"));
                        attendence_modal.setAttendencetenantname(jsonobj.getString("tenant_name"));
                        attendence_modal.setTenant_photo(jsonobj.getString("tenant_photo"));
                        attendence_modal.setAttendenceroom_no(jsonobj.getString("room_no"));
                        attendence_modal.setAttendecstatus(jsonobj.getString("attendence_status"));
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
                Attendence_Show_Adapter adapter = new Attendence_Show_Adapter(AttendenceData_Report_Show_Tenant.this, show_arraylist, tenantnameview, roomnoview, imageview_data, emailid_tenant);
                listview_show_attendence.setAdapter(adapter);
            } else if (result.equals("N")) {
                roomnoview.setText("");
                Toast.makeText(AttendenceData_Report_Show_Tenant.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
