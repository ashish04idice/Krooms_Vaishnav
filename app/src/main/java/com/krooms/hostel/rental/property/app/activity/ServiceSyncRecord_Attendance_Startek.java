package com.krooms.hostel.rental.property.app.activity;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.App;
import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2/2/2018.
 */

public class ServiceSyncRecord_Attendance_Startek extends Service {

    private SQLiteDatabase db;
    private Cursor c;
    DatabaseHandlerStartak_Sdk handlerdb;
    boolean status=false,statusfood=false;
    public static ArrayList<UserModel> attendance_list;
    public static ArrayList<UserModel> foodattendance_list;
    String propertyid="",versionCode="";
    private SharedStorage mSharedStorage;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handlerdb = new DatabaseHandlerStartak_Sdk(ServiceSyncRecord_Attendance_Startek.this);
        db = handlerdb.getWritableDatabase();
        versionCode = Utility.getSharedPreferences(ServiceSyncRecord_Attendance_Startek.this, "GetVersion");
        mSharedStorage = SharedStorage.getInstance(this);
        propertyid = mSharedStorage.getUserPropertyId();
        if (NetworkConnection.isConnected(ServiceSyncRecord_Attendance_Startek.this)) {
            mTimer = new Timer();   //recreate new
            mTimer.scheduleAtFixedRate(new TimeDisplay(), 0l, 1000*60*2);  // 2 min
        }else{
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }




    class TimeDisplay extends TimerTask {

        @Override
        public void run(){
            mHandler.post(new Runnable(){
                @Override
                public void run(){

                    try {
                        if (NetworkConnection.isConnected(ServiceSyncRecord_Attendance_Startek.this)) {
                            sendOfflineattendance();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void sendOfflineattendance() {
        attendance_list = new ArrayList<>();
        foodattendance_list=new ArrayList<>();
        Cursor cc=null;
        String query = "select * from krooms_attendance where status='1' and propertyid='"+propertyid+"'";
        cc = db.rawQuery(query, null);
        attendance_list.clear();
        JSONArray jArray=null ;
        JSONArray jArrayfood=null;
        if (cc.getCount() != 0) {
            if (cc.moveToFirst()) {
                do {
                    UserModel model = new UserModel();
                    model.setId(cc.getString(0));
                    model.setUserid(cc.getString(1));
                    model.setPropertyid(cc.getString(2));
                    model.setUsername(cc.getString(3));
                    model.setAttendance_status(cc.getString(4));
                    model.setAttendance_reasons(cc.getString(5));
                    model.setDate(cc.getString(6));
                    model.setTime(cc.getString(7));
                    model.setTimeinterval(cc.getString(8));
                    model.setStatus(cc.getString(9));
                    model.setRoomno(cc.getString(10));
                    attendance_list.add(model);
                } while (cc.moveToNext());
            }
            jArray = new JSONArray();


            for (int i = 0; i < attendance_list.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("id", attendance_list.get(i).getId());
                    jGroup.put("tenant_id", attendance_list.get(i).getUserid());
                    jGroup.put("property_id", attendance_list.get(i).getPropertyid());
                    jGroup.put("name", attendance_list.get(i).getUsername());
                    jGroup.put("attendance", attendance_list.get(i).getAttendance_status());
                    jGroup.put("reasons", attendance_list.get(i).getAttendance_reasons());
                    jGroup.put("date", attendance_list.get(i).getDate());
                    jGroup.put("time", attendance_list.get(i).getTime());
                    jGroup.put("timeinterval",attendance_list.get(i).getTimeinterval());
                    jGroup.put("roomno",attendance_list.get(i).getRoomno());
                    jArray.put(jGroup);

                } catch (Exception e) {

                    e.printStackTrace();
                    AppError.handleUncaughtException(e,"ServiceSyncRecord_Attendance_Startek - attendance_list.size()",propertyid, versionCode);

                }
                finally {
                    if (cc != null)
                        cc.close();
                }
            }

        } else {
        }
        //foodattendance
        Cursor cursor=null;
        String queryfood = "select * from krooms_foodattendance where status='1' and propertyid='"+propertyid+"'";
        cursor = db.rawQuery(queryfood, null);
        foodattendance_list.clear();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    UserModel model = new UserModel();
                    model.setId(cursor.getString(0));
                    model.setUserid(cursor.getString(1));
                    model.setPropertyid(cursor.getString(2));
                    model.setUsername(cursor.getString(3));
                    model.setAttendance_status(cursor.getString(4));
                    model.setAttendance_reasons(cursor.getString(5));
                    model.setDate(cursor.getString(6));
                    model.setTime(cursor.getString(7));
                    model.setStatus(cursor.getString(9));
                    model.setRoomno(cursor.getString(10));
                    foodattendance_list.add(model);
                } while (cursor.moveToNext());
            }

            jArrayfood=new JSONArray();
            for (int i = 0; i < foodattendance_list.size(); i++) {

                JSONObject jGroup = new JSONObject();
                try {
                    jGroup.put("id", foodattendance_list.get(i).getId());
                    jGroup.put("tenant_id",foodattendance_list.get(i).getUserid());
                    jGroup.put("property_id",foodattendance_list.get(i).getPropertyid());
                    jGroup.put("name", foodattendance_list.get(i).getUsername());
                    jGroup.put("attendance", foodattendance_list.get(i).getAttendance_status());
                    jGroup.put("reasons", foodattendance_list.get(i).getAttendance_reasons());
                    jGroup.put("date", foodattendance_list.get(i).getDate());
                    jGroup.put("time", foodattendance_list.get(i).getTime());
                    jGroup.put("timeinterval", foodattendance_list.get(i).getTimeinterval());
                    jGroup.put("roomno", foodattendance_list.get(i).getRoomno());
                    jArrayfood.put(jGroup);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if (cursor != null)
                        cursor.close();
                }
            }

        } else {
        }
        //
        try {
            if (attendance_list.size()!=0 && attendance_list.size()>0 ) {
                Sendofflinehostelattendance(jArray);
            }
            if (foodattendance_list.size()!=0 && foodattendance_list.size()>0) {
                SendofflinehostelattendanceFood(jArrayfood);
            }
        }catch (Exception e){
            e.printStackTrace();
            AppError.handleUncaughtException(e,"ServiceSyncRecord_Attendance_Startek - sendOfflineattendance()",propertyid, versionCode);

        }
    }

    private void SendofflinehostelattendanceFood(final JSONArray jArrayfood) {

        try
        {
            StringRequest postRequest = new StringRequest(Request.Method.POST, WebUrls.MAIN_ATTENDACE_URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            String result=response.toString();
                            getResponsetenantlistFood(result);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            AppError.handleUncaughtException(error,"ServiceSyncRecord_Attendance_Startek - onErrorResponseFood",propertyid, versionCode);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> parames = new HashMap<String, String>();
                    parames.put("action","insertattendencesystemofflineFood");
                    parames.put("arraylistfood",String.valueOf(jArrayfood));
                    parames.put("property_id",propertyid);
                    return parames;
                }
            };
            //30000
            DefaultRetryPolicy policy=new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);//1000*60=60000//1min
            postRequest.setRetryPolicy(policy);
            //old working gives memory error
          /*  RequestQueue   requestQueue= Volley.newRequestQueue(ServiceSyncRecord_Attendance_Startek.this);
            requestQueue.add(postRequest);*/
           //chnages on 23/04/19
            App.getInstance().addToRequestQueue(postRequest, "tag1");

        } catch (Exception e)
        {
            e.printStackTrace();
            AppError.handleUncaughtException(e,"ServiceSyncRecord_Attendance_Startek  -Sendofflinehostelattendancefood",propertyid, versionCode);
        }

    }

    public void Sendofflinehostelattendance(final JSONArray attendencelist)
    {
        try
        {
            StringRequest postRequest = new StringRequest(Request.Method.POST, WebUrls.MAIN_ATTENDACE_URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            String result=response.toString();
                            getResponsetenantlist(result);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            AppError.handleUncaughtException(error,"ServiceSyncRecord_Attendance_Startek - onErrorResponsehostel",propertyid, versionCode);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> parames = new HashMap<String, String>();
                    parames.put("action","insertattendencesystemofflineHostel");
                    parames.put("arraylisthostel",String.valueOf(attendencelist));
                    parames.put("property_id",propertyid);
                    return parames;
                }
            };
            DefaultRetryPolicy policy=new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);//1000*60=60000//1min
            postRequest.setRetryPolicy(policy);
            //old working
           /* RequestQueue requestQueue= Volley.newRequestQueue(ServiceSyncRecord_Attendance_Startek.this);
            requestQueue.add(postRequest);*/

            App.getInstance().addToRequestQueue(postRequest, "tag2");


        } catch (Exception e)
        {
            e.printStackTrace();
            AppError.handleUncaughtException(e,"ServiceSyncRecord_Attendance_Startek  -Sendofflinehostelattendancehostel",propertyid, versionCode);
        }
    }


    public void getResponsetenantlist(String resultobj)
    {
        try
        {
            JSONObject jsmain =new JSONObject(resultobj);
            String result = jsmain.getString("flag");
            String message = jsmain.getString("message");
            if(result.equalsIgnoreCase("Y")){
                String attendance=jsmain.getString("listhostel_localid");
                if(attendance.equalsIgnoreCase("0") || attendance.equalsIgnoreCase(null) || attendance.equals("") || attendance.equals("null")){

                }else{
                    JSONArray jsonArray = jsmain.getJSONArray("listhostel_localid");
                    String  id = jsonArray.getJSONObject(0).getString("local_id");
                    String query = "UPDATE krooms_attendance  set status='0' where id <= '"+id+"' and propertyid='"+propertyid+"'";
                    db.execSQL(query);
                }
            }else{
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            AppError.handleUncaughtException(e,"ServiceSyncRecord_Attendance_Startek - getResponsetenantlist",propertyid, versionCode);
        }
    }



    public void getResponsetenantlistFood(String resultobj)
    {
        try
        {
            JSONObject jsmain =new JSONObject(resultobj);
            String result = jsmain.getString("flag");
            String message = jsmain.getString("message");
            if(result.equalsIgnoreCase("Y")){
                String food=jsmain.getString("food_localid");

                if(food.equalsIgnoreCase("0") || food.equalsIgnoreCase(null) || food.equals("") || food.equals("null")){

                }else{
                    JSONArray jsonArrayfood = jsmain.getJSONArray("food_localid");
                    String  idfood = jsonArrayfood.getJSONObject(0).getString("local_id");
                    String query = "UPDATE krooms_foodattendance  set status='0' where id <= '"+idfood+"' and propertyid='"+propertyid+"'";
                    db.execSQL(query);
                }

            }else{

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            AppError.handleUncaughtException(e,"ServiceSyncRecord_Attendance_Startek - getResponsetenantlistFood",propertyid, versionCode);

        }
    }

}
