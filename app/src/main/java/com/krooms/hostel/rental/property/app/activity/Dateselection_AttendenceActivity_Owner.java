package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.adapter.Attendence_Time_Show_Adapter;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;
import com.stacktips.view.CalendarListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by anuradhaidice on 24/12/16.
 */

public class Dateselection_AttendenceActivity_Owner extends Activity {
    Dialog time_dialog;
    ListView timelistview;
    Attendence_Modal time_attendence_modal;
    ArrayList<Attendence_Modal> time_show_arraylist;
    CustomCalendarView calendarView;
    Intent in;
    String datevalue = "", monthvalue = "";
    Button ok;
    ImageView time_loader;
    String owneridmainvalue;
    RelativeLayout back_button;
    String timevalue;
    LinearLayout food_layout, daily_layout, manual_layout;
    String attendencevalue = "daily";
    Animation rotation;
    String usernamevale, useriddata_value, tid_value, propertyid_data, propertyname_value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dateselection_attendence_layout);
        ok = (Button) findViewById(R.id.ok);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        in = getIntent();
        owneridmainvalue = in.getStringExtra("ownerid");
        propertyid_data = in.getStringExtra("property_id_data");
        Log.d("all values", propertyid_data + "" + owneridmainvalue + "");
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        food_layout = (LinearLayout) findViewById(R.id.food_layout);
        daily_layout = (LinearLayout) findViewById(R.id.daily_layout);
        manual_layout = (LinearLayout) findViewById(R.id.manual_layout);
        food_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_layout.setBackgroundResource(R.color.grey);
                daily_layout.setBackgroundResource(R.color.lightgray);
                manual_layout.setBackgroundResource(R.color.lightgray);
                attendencevalue = "food";
            }
        });

        daily_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily_layout.setBackgroundResource(R.color.grey);
                food_layout.setBackgroundResource(R.color.lightgray);
                manual_layout.setBackgroundResource(R.color.lightgray);
                attendencevalue = "daily";
            }
        });

        manual_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                daily_layout.setBackgroundResource(R.color.lightgray);
                food_layout.setBackgroundResource(R.color.lightgray);
                manual_layout.setBackgroundResource(R.color.grey);
                attendencevalue = "manual";
            }
        });

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance();
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                datevalue = df.format(date);
                monthvalue = "0";
                Toast.makeText(Dateselection_AttendenceActivity_Owner.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
                datevalue = "";
                monthvalue = df.format(date);
                //   Toast.makeText(Dateselection_AttendenceActivity_Owner.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        //Setting custom font
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Arch_Rival_Bold.ttf");
        if (null != typeface) {
            calendarView.setCustomTypeface(typeface);
            calendarView.refreshCalendar(currentCalendar);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (datevalue == null || datevalue.isEmpty() || datevalue.equals("null")) {
                    Toast.makeText(Dateselection_AttendenceActivity_Owner.this, "Please select Date", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Dateselection_AttendenceActivity_Owner.this, datevalue, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Dateselection_AttendenceActivity_Owner.this, Attendence_Report_Show_owner.class);
                    i.putExtra("property_id_data", propertyid_data);
                    i.putExtra("attendecetype", attendencevalue);
                    i.putExtra("datevalue", datevalue);
                    startActivity(i);
                }

            }
        });

    }

    //these is for student name list
    public void timedialogMethod() {
        time_dialog = new Dialog(Dateselection_AttendenceActivity_Owner.this);
        time_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        time_dialog.setCancelable(false);
        time_dialog.setCanceledOnTouchOutside(false);
        time_dialog.setContentView(R.layout.attendence_timesreen_owner_layout);
        TextView dateview = (TextView) time_dialog.findViewById(R.id.dateview);
        TextView dateheading = (TextView) time_dialog.findViewById(R.id.timeheading);
        //Setting custom font
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Arch_Rival_Bold.ttf");
        if (null != typeface) {
        }
        dateview.setText(datevalue);

        timelistview = (ListView) time_dialog.findViewById(R.id.timelistview);
        time_loader = (ImageView) time_dialog.findViewById(R.id.loader);
        RelativeLayout cross_button = (RelativeLayout) time_dialog.findViewById(R.id.cross_button);

        if (NetworkConnection.isConnected(Dateselection_AttendenceActivity_Owner.this)) {
            new TimeGetJsonArray().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_dialog.dismiss();
            }
        });

        timelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                timevalue = time_show_arraylist.get(position).getTime();
                time_dialog.dismiss();
                Intent i = new Intent(Dateselection_AttendenceActivity_Owner.this, Attendence_Report_Show_owner.class);
                i.putExtra("ownerid", owneridmainvalue);
                i.putExtra("property_id_data", propertyid_data);
                i.putExtra("datevalue", datevalue);
                i.putExtra("timevalue", timevalue);
                startActivity(i);


            }
        });

        time_dialog.show();
    }
    //student dialog end
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
            time_loader.startAnimation(rotation);
            time_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_ATTENDACE_URL);
            try {
                time_show_arraylist = new ArrayList<Attendence_Modal>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getattendencetime"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_data));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridmainvalue));
                nameValuePairs.add(new BasicNameValuePair("date", datevalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        time_attendence_modal = new Attendence_Modal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        time_attendence_modal.setTime(jsonobj.getString("m_time"));
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
            time_loader.clearAnimation();
            time_loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                Attendence_Time_Show_Adapter adapter = new Attendence_Time_Show_Adapter(Dateselection_AttendenceActivity_Owner.this, time_show_arraylist);
                timelistview.setAdapter(adapter);
            } else if (result.equals("N")) {
                time_dialog.dismiss();
                Toast.makeText(Dateselection_AttendenceActivity_Owner.this, "Please Select Validate Date", Toast.LENGTH_SHORT).show();
            }

        }
    }

}




