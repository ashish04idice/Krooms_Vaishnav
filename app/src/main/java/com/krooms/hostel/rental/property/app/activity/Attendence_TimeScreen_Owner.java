package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.adapter.Attendence_Time_Show_Adapter;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;
import com.stacktips.view.CustomCalendarView;

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
 * Created by Raghveer on 29-12-2016.
 */
public class Attendence_TimeScreen_Owner extends Activity {
    CustomCalendarView calendarView;
    Intent in;
    Attendence_Modal time_attendence_modal;
    ArrayList<Attendence_Modal> time_show_arraylist;
    String datevalue, monthvalue;
    Button ok;
    String owneridmainvalue;
    RelativeLayout back_button;
    ListView timelistview;
    ImageView loader;
    Animation rotation;
    TextView dateview, dateheading;
    String datevaluemain, propertyid_data, propertyname_value;
    String timevalue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_timesreen_owner_layout);
        in = getIntent();
        owneridmainvalue = in.getStringExtra("ownerid");
        propertyid_data = in.getStringExtra("property_id_data");
        datevaluemain = in.getStringExtra("datevalue");
        monthvalue = in.getStringExtra("monthvalue");
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        timelistview = (ListView) findViewById(R.id.timelistview);
        loader = (ImageView) findViewById(R.id.loader);
        dateview = (TextView) findViewById(R.id.dateview);
        dateheading = (TextView) findViewById(R.id.dateheading);
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Arch_Rival_Bold.ttf");
        if (null != typeface) {
            dateview.setTypeface(typeface);
            dateheading.setTypeface(typeface);
        }
        dateview.setText(datevaluemain);
        new TimeGetJsonArray().execute();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        timelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                timevalue = time_show_arraylist.get(position).getTime();
                Intent i = new Intent(Attendence_TimeScreen_Owner.this, Attendence_Report_Show_owner.class);
                i.putExtra("ownerid", owneridmainvalue);
                i.putExtra("property_id_data", propertyid_data);
                i.putExtra("datevalue", datevaluemain);
                i.putExtra("timevalue", timevalue);
                startActivity(i);
            }
        });

    }

    //this class is used getting value by webservices
    private class TimeGetJsonArray extends AsyncTask<String, String, String> {

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
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                time_show_arraylist = new ArrayList<Attendence_Modal>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getattendencetime"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_data));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridmainvalue));
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
                        time_attendence_modal = new Attendence_Modal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        time_attendence_modal.setTime(jsonobj.getString("time"));
                        time_show_arraylist.add(time_attendence_modal);
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
                    Attendence_Time_Show_Adapter adapter = new Attendence_Time_Show_Adapter(Attendence_TimeScreen_Owner.this, time_show_arraylist);
                    timelistview.setAdapter(adapter);
                } else if (result.equals("N")) {
                    Toast.makeText(Attendence_TimeScreen_Owner.this, message, Toast.LENGTH_SHORT).show();
                }
        }
    }
}
