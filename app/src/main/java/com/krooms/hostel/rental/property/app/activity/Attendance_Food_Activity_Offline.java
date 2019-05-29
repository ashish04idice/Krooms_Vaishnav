package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.Food_Attendance_RecordsAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.EnrollTenantModel;
import com.krooms.hostel.rental.property.app.modal.Foodmodel;
import com.krooms.hostel.rental.property.app.modal.UserModel;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import library.ashish.example.com.jarauthentication.Encodingformat;


/**
 * Created by ashish on 4/29/2017.
 */
public class Attendance_Food_Activity_Offline extends Activity {

    ListView listView;
    Food_Attendance_RecordsAdapter adapter;
    Context context;
    ArrayList<Foodmodel> list;
    RelativeLayout flback_button;
    Foodmodel recordmodel;
    RelativeLayout listiconfood;
    TextView textviewgetattedence;
    LinearLayout layouthostelattend, layoutfoodgetdata, layoutfoodsynch;
    Intent in;
    String propertyid;
    String mobileno;
    private Common mCommon = null;
    private SQLiteDatabase db, database;
    private Cursor c;
    public static ArrayList<UserModel> alist;
    Calendar calnder;
    String localTime, formattedDate = "", devicetype = "";
    MediaPlayer mp;
    public static ArrayList<EnrollTenantModel> listtenantrecord;
    String datafinger;
    String matchfinger;
    DatabaseHandler handlerdb;
    DatabaseHandlerStartak_Sdk handlerStartak_sdk;
    EditText txtroomno;
    private Cursor attendance_cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_food_layout);
        context = getApplicationContext();
        handlerdb = new DatabaseHandler(context);
        handlerStartak_sdk = new DatabaseHandlerStartak_Sdk(context);
        db = handlerdb.getWritableDatabase();
        database = handlerStartak_sdk.getWritableDatabase();
        alist = new ArrayList<>();
        listtenantrecord = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listfood);
        flback_button = (RelativeLayout) findViewById(R.id.flback_button1);
        textviewgetattedence = (TextView) findViewById(R.id.getattedence);
        layouthostelattend = (LinearLayout) findViewById(R.id.list_hostelicon);
        layoutfoodgetdata = (LinearLayout) findViewById(R.id.attendance_food_getrecord);
        layoutfoodsynch = (LinearLayout) findViewById(R.id.attendance_food_syn);
        listiconfood = (RelativeLayout) findViewById(R.id.list_iconfood);
        txtroomno = (EditText) findViewById(R.id.roomno);
        in = getIntent();
        propertyid = in.getStringExtra("propertyid");
        devicetype = in.getStringExtra("device");
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        formattedDate = df.format(calnder.getTime());

        if (devicetype.equalsIgnoreCase("Secugen")) {
            GetfoodRecord();
        } else {
            GetfoodRecordStartek();
        }

        flback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (devicetype.equalsIgnoreCase("Secugen")) {
                    Intent intent = new Intent(context, Tenant_Details_Activity.class);
                    intent.putExtra("propertyid", propertyid);
                    intent.putExtra("device", devicetype);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, Get_Attendance_Startek.class);
                    intent.putExtra("propertyid", propertyid);
                    intent.putExtra("device", devicetype);
                    startActivity(intent);
                }
            }
        });


        layouthostelattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Tenant_Details_Activity.class);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("device", devicetype);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (devicetype.equalsIgnoreCase("Secugen")) {
                    Intent intent = new Intent(context, Tenant_Details_Activity.class);
                    intent.putExtra("propertyid", propertyid);
                    intent.putExtra("device", devicetype);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, Get_Attendance_Startek.class);
                    intent.putExtra("propertyid", propertyid);
                    intent.putExtra("device", devicetype);
                    startActivity(intent);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //getofflinefoodrecord
    private void GetfoodRecord() {

        list = new ArrayList();
        Cursor cc = null;
        String query = "select * from krooms_foodattendance where date='" + formattedDate + "'";
        cc = db.rawQuery(query, null);
        list.clear();

        try {
            if (cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    do {
                        Foodmodel recordmodel = new Foodmodel();
                        recordmodel.setAttendencestatus(cc.getString(5));
                        recordmodel.setRoomno(cc.getString(10));
                        recordmodel.setTname(cc.getString(3));
                        list.add(recordmodel);
                    } while (cc.moveToNext());
                }

                adapter = new Food_Attendance_RecordsAdapter(Attendance_Food_Activity_Offline.this, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
            }
        } catch (Exception rr) {
            rr.printStackTrace();
        } finally {
            if (cc != null)
                cc.close();
        }

    }

    //getofflinefoodrecord
    private void GetfoodRecordStartek() {
        list = new ArrayList();
        Cursor cc = null;
        String query = "select * from krooms_foodattendance where date='" + formattedDate + "'";
        cc = database.rawQuery(query, null);
        list.clear();
        try {
            if (cc.getCount() != 0) {
                if (cc.moveToFirst()) {
                    do {
                        Foodmodel recordmodel = new Foodmodel();
                        recordmodel.setAttendencestatus(cc.getString(5));
                        recordmodel.setRoomno(cc.getString(10));
                        recordmodel.setTname(cc.getString(3));
                        list.add(recordmodel);

                    } while (cc.moveToNext());
                }
                adapter = new Food_Attendance_RecordsAdapter(Attendance_Food_Activity_Offline.this, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cc != null)
                cc.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

}
