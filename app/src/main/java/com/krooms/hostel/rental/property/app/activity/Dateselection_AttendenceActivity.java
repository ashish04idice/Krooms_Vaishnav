package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anuradhaidice on 24/12/16.
 */

public class Dateselection_AttendenceActivity extends Activity {
    CustomCalendarView calendarView;
    Intent in;
    String datevalue, monthvalue;
    RelativeLayout back_button;
    LinearLayout food_layout, daily_layout, manual_layout;
    String attendencevalue = "daily";
    Button ok;
    public static String userid_atten_value, ownerid_atten_value, propertyid_atten_value, tid_atten_value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dateselection_attendence_layout);
        in = getIntent();
        userid_atten_value = in.getStringExtra("user_id_data");
        ownerid_atten_value = in.getStringExtra("ownerid");
        propertyid_atten_value = in.getStringExtra("property_id_data");
        tid_atten_value = in.getStringExtra("tid");
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

        ok = (Button) findViewById(R.id.ok);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
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
                Toast.makeText(Dateselection_AttendenceActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
                datevalue = " ";
                monthvalue = df.format(date);
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
                    Toast.makeText(Dateselection_AttendenceActivity.this, "Please select Date", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Dateselection_AttendenceActivity.this, datevalue, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Dateselection_AttendenceActivity.this, AttendenceData_Report_Show_Tenant.class);
                    i.putExtra("datevalue", datevalue);
                    i.putExtra("attendecetype", attendencevalue);
                    i.putExtra("user_id_data", userid_atten_value);
                    i.putExtra("tid", tid_atten_value);
                    i.putExtra("ownerid", ownerid_atten_value);
                    i.putExtra("property_id_data", propertyid_atten_value);
                    startActivity(i);
                }


            }
        });

    }
}
