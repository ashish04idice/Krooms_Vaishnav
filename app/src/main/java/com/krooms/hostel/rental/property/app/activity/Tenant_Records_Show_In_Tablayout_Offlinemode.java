package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Single_Tenant_RecordsAdapter;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.TenantRecordmodel;
import com.krooms.hostel.rental.property.app.modal.UserModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 6/7/2017.
 */
public class Tenant_Records_Show_In_Tablayout_Offlinemode extends Activity {

    String formattedDate,devicetype="";
    Single_Tenant_RecordsAdapter adapter;
    ArrayList<TenantRecordmodel> list,list_out_array;
    LinearLayout lastin_layout,lastout_layout,lastin_main_layout,lastout_main_layout;
    RelativeLayout back_button;
    ListView list_out,list_in;
    Intent in;
    String propertyid;
    private SQLiteDatabase db,database;
    Context context;
    DatabaseHandler handlerdb;
    DatabaseHandlerStartak_Sdk handlerStartak_sdk;
    public static ArrayList<UserModel> attendance_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_records_tab_layout);
        context = this;
        list_out_array=new ArrayList();
        list=new ArrayList();
        handlerdb = new DatabaseHandler(context);
        db = handlerdb.getWritableDatabase();

        handlerStartak_sdk=new DatabaseHandlerStartak_Sdk(context);
        database=handlerStartak_sdk.getWritableDatabase();

        attendance_list=new ArrayList<UserModel>();
        in=getIntent();
        propertyid=in.getStringExtra("propertyid");
        devicetype=in.getStringExtra("device");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
        formattedDate = df.format(c.getTime());

        lastout_layout=(LinearLayout)findViewById(R.id.lastout_layout);
        lastin_layout=(LinearLayout)findViewById(R.id.lastin_layout);
        back_button=(RelativeLayout)findViewById(R.id.back_button);
        lastin_main_layout=(LinearLayout)findViewById(R.id.lastin_main_layout);
        lastout_main_layout=(LinearLayout)findViewById(R.id.lastout_main_layout);
        list_out=(ListView)findViewById(R.id.list_out);
        list_in=(ListView)findViewById(R.id.list_in);

        if(devicetype.equalsIgnoreCase("Secugen")) {
            GetUserInRecord();
        }else{
            GetUserInRecordStartek();
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(devicetype.equalsIgnoreCase("Secugen")) {
                    Intent i=new Intent(Tenant_Records_Show_In_Tablayout_Offlinemode.this,Tenant_Details_Activity.class);
                    i.putExtra("propertyid", propertyid);
                    i.putExtra("device",devicetype);
                    startActivity(i);
                }else{
                    Intent i=new Intent(Tenant_Records_Show_In_Tablayout_Offlinemode.this,Get_Attendance_Startek.class);
                    i.putExtra("propertyid", propertyid);
                    i.putExtra("device",devicetype);
                    startActivity(i);
                }
            }
        });

        lastin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastin_layout.setBackgroundResource(R.color.grey);
                lastout_layout.setBackgroundResource(R.color.lightgray);
                lastin_main_layout.setVisibility(View.VISIBLE);
                lastout_main_layout.setVisibility(View.GONE);
                if(devicetype.equalsIgnoreCase("Secugen")) {
                    GetUserInRecord();
                }else{
                    GetUserInRecordStartek();
                }
            }
            });

        lastout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastout_layout.setBackgroundResource(R.color.grey);
                lastin_layout.setBackgroundResource(R.color.lightgray);
                lastin_main_layout.setVisibility(View.GONE);
                lastout_main_layout.setVisibility(View.VISIBLE);

                if(devicetype.equalsIgnoreCase("Secugen")) {
                    GetUserOutRecord();
                }else{
                    GetUserOutRecordStartek();
                }

            }


        });


    }

    private void GetUserInRecord() {
        Cursor cc=null;
        try {
            String query = "select * from krooms_attendance where (attendancestatus='Sign in') and propertyid='" + propertyid + "' and date='" + formattedDate + "' union select * from krooms_nightattendance where propertyid='" + propertyid + "' and date='" + formattedDate + "' order by timenew ";
            cc = db.rawQuery(query, null);
            list.clear();
            if (cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    do {
                        TenantRecordmodel tmodel = new TenantRecordmodel();
                        tmodel.setTime(cc.getString(7));
                        tmodel.setTenant_name(cc.getString(3));
                        tmodel.setAttendancestatus(cc.getString(5));
                        tmodel.setRoom_no(cc.getString(10));
                        tmodel.setTime_interval(cc.getString(8));
                        list.add(tmodel);
                    } while (cc.moveToNext());
                }

                adapter = new Single_Tenant_RecordsAdapter(Tenant_Records_Show_In_Tablayout_Offlinemode.this, list);
                list_in.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
            }
        }catch (Exception e){

            e.printStackTrace();
        }

        finally {
            // this gets called even if there is an exception somewhere above
            if (cc!=null)
                cc.close();
        }
    }


    private void GetUserInRecordStartek() {
        Cursor cc=null;
        try {
            String query = "select * from krooms_attendance where (attendancestatus='Sign in') and propertyid='" + propertyid + "' and date='" + formattedDate + "' union select * from krooms_nightattendance where propertyid='" + propertyid + "' and date='" + formattedDate + "' order by timenew ";
             cc = database.rawQuery(query, null);
            list.clear();
            if (cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    do {
                        TenantRecordmodel tmodel = new TenantRecordmodel();
                        tmodel.setTime(cc.getString(7));
                        tmodel.setTenant_name(cc.getString(3));
                        tmodel.setAttendancestatus(cc.getString(5));
                        tmodel.setRoom_no(cc.getString(10));
                        tmodel.setTime_interval(cc.getString(8));
                        list.add(tmodel);
                    } while (cc.moveToNext());
                }

                adapter = new Single_Tenant_RecordsAdapter(Tenant_Records_Show_In_Tablayout_Offlinemode.this, list);
                list_in.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
            }
        }catch (Exception e){

            e.printStackTrace();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if (cc!=null)
                cc.close();
        }
    }



    private void GetUserOutRecord() {
        Cursor cc=null;

        try {
            String query = "select * from krooms_attendance where (attendancestatus!='Sign in' and attendancestatus!='Night in') and propertyid='"+propertyid+"' and date='"+formattedDate+"' ";
            cc = db.rawQuery(query, null);
            list_out_array.clear();
            if (cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    do {
                        TenantRecordmodel tmodel=new TenantRecordmodel();
                        tmodel.setTime(cc.getString(7));
                        tmodel.setTenant_name(cc.getString(3));
                        tmodel.setAttendancestatus(cc.getString(5));
                        tmodel.setRoom_no(cc.getString(10));
                        tmodel.setTime_interval(cc.getString(8));
                        list_out_array.add(tmodel);

                    } while (cc.moveToNext());
                }
                try {
                    adapter=new Single_Tenant_RecordsAdapter(Tenant_Records_Show_In_Tablayout_Offlinemode.this,list_out_array);
                    list_out.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            } else {
            }
        }catch (Exception rr){

        }finally {
            if (cc!=null)
                cc.close();
        }

    }

    private void GetUserOutRecordStartek() {
        Cursor cc=null;
        try {
            String query = "select * from krooms_attendance where (attendancestatus!='Sign in' and attendancestatus!='Night in') and propertyid='"+propertyid+"' and date='"+formattedDate+"' ";
            cc = database.rawQuery(query, null);
            list_out_array.clear();
            if (cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    do {
                        TenantRecordmodel tmodel=new TenantRecordmodel();
                        tmodel.setTime(cc.getString(7));
                        tmodel.setTenant_name(cc.getString(3));
                        tmodel.setAttendancestatus(cc.getString(5));
                        tmodel.setRoom_no(cc.getString(10));
                        tmodel.setTime_interval(cc.getString(8));
                        list_out_array.add(tmodel);

                    } while (cc.moveToNext());
                }
                try {
                    adapter=new Single_Tenant_RecordsAdapter(Tenant_Records_Show_In_Tablayout_Offlinemode.this,list_out_array);
                    list_out.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            } else {
            }
        }catch (Exception rr){

        }finally {
            // this gets called even if there is an exception somewhere above
            if (cc!=null)
                cc.close();
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if(devicetype.equalsIgnoreCase("Secugen")) {
                    Intent i=new Intent(Tenant_Records_Show_In_Tablayout_Offlinemode.this,Tenant_Details_Activity.class);
                    i.putExtra("propertyid", propertyid);
                    i.putExtra("device",devicetype);
                    startActivity(i);
                }else{
                    Intent i=new Intent(Tenant_Records_Show_In_Tablayout_Offlinemode.this,Get_Attendance_Startek.class);
                    i.putExtra("propertyid", propertyid);
                    i.putExtra("device",devicetype);
                    startActivity(i);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

}
