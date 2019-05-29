package com.krooms.hostel.rental.property.app.activity;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

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
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
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

public class ServiceSyncRecord_Night_Attendance extends Service {

    private SQLiteDatabase db;
    private Cursor c;
    DatabaseHandler handlerdb;
    boolean status = false, statusfood = false;
    public static ArrayList<UserModel> nightattendance_list;
    String propertyid = "", versionCode = "";
    private SharedStorage mSharedStorage;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerdb = new DatabaseHandler(ServiceSyncRecord_Night_Attendance.this);
        db = handlerdb.getWritableDatabase();
        versionCode = Utility.getSharedPreferences(ServiceSyncRecord_Night_Attendance.this, "GetVersion");


        mSharedStorage = SharedStorage.getInstance(this);
        propertyid = mSharedStorage.getUserPropertyId();

        mTimer = new Timer();   //recreate new


        if (NetworkConnection.isConnected(ServiceSyncRecord_Night_Attendance.this)) {

            Date mToday = new Date();
            Date userDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);//hh:mm:ss aa
            String curTime = sdf.format(mToday);

            Date start = null, end = null, start1 = null, end1 = null, start2 = null, end2 = null, start3 = null, end3 = null, start4 = null, end4 = null, start5 = null, end5 = null;

            try {
                start = sdf.parse("20:10:00");//8:30:00 PM
                end = sdf.parse("21:00:01");//9:00:01 PM
                userDate = sdf.parse(curTime);
                if (userDate.after(start) && userDate.before(end)) {
                    mTimer.scheduleAtFixedRate(new TimeDisplay(), 0l, 1000 * 60 * 3);  // 2 min

                }
                start1 = sdf.parse("21:30:00");//9:30:00 PM
                end1 = sdf.parse("23:00:01");//11:00:01 PM

                if (userDate.after(start1) && userDate.before(end1)) {
                    mTimer.scheduleAtFixedRate(new TimeDisplay(), 0l, 1000 * 60 * 3);  // 2 min

                }
            } catch (ParseException e) {
                e.printStackTrace();
                AppError.handleUncaughtException(e, "ServiceSyncRecord_Night_Attendance_Secugen - onStartCommand", propertyid, versionCode);

            }
        } else {
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
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (NetworkConnection.isConnected(ServiceSyncRecord_Night_Attendance.this)) {
                            sendOfflineattendance();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void sendOfflineattendance() {
        Cursor ccc=null;
        nightattendance_list = new ArrayList<>();
        String querynight = "select * from krooms_nightattendance where status='1' and propertyid='" + propertyid + "' ";
         ccc = db.rawQuery(querynight, null);
        nightattendance_list.clear();
        JSONArray jArraynight = null;

        if (ccc.getCount() != 0) {
            if (ccc.moveToFirst()) {
                do {
                    UserModel model = new UserModel();
                    model.setId(ccc.getString(0));
                    model.setUserid(ccc.getString(1));
                    model.setPropertyid(ccc.getString(2));
                    model.setUsername(ccc.getString(3));
                    model.setAttendance_status(ccc.getString(4));
                    model.setAttendance_reasons(ccc.getString(5));
                    model.setDate(ccc.getString(6));
                    model.setTime(ccc.getString(7));
                    model.setTimeinterval(ccc.getString(8));
                    model.setStatus(ccc.getString(9));
                    model.setRoomno(ccc.getString(10));
                    nightattendance_list.add(model);
                } while (ccc.moveToNext());
            }

            jArraynight = new JSONArray();// /ItemDetail jsonArray
            for (int i = 0; i < nightattendance_list.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("id", nightattendance_list.get(i).getId());
                    jGroup.put("tenant_id", nightattendance_list.get(i).getUserid());
                    jGroup.put("property_id", nightattendance_list.get(i).getPropertyid());
                    jGroup.put("name", nightattendance_list.get(i).getUsername());
                    jGroup.put("attendance", nightattendance_list.get(i).getAttendance_status());
                    jGroup.put("reasons", nightattendance_list.get(i).getAttendance_reasons());
                    jGroup.put("date", nightattendance_list.get(i).getDate());
                    jGroup.put("time", nightattendance_list.get(i).getTime());
                    jGroup.put("timeinterval", nightattendance_list.get(i).getTimeinterval());
                    jGroup.put("roomno", nightattendance_list.get(i).getRoomno());
                    jArraynight.put(jGroup);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else {
        }
        //end night attendance
        try {
            if (nightattendance_list.size() != 0) {
                Sendofflinehostelattendance(jArraynight);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, " ServiceSyncRecord_Night_Attendance_Secugen - nightattendance_list", propertyid, versionCode);
        }
    }


    public void Sendofflinehostelattendance(final JSONArray jArraynight) {
        try {
            StringRequest postRequest = new StringRequest(Request.Method.POST, WebUrls.MAIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String result = response.toString();
                            getResponsetenantlist(result);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            AppError.handleUncaughtException(error, " ServiceSyncRecord_Night_Attendance_Secugen - onErrorResponse", propertyid, versionCode);

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parames = new HashMap<String, String>();//
                    parames.put("action", "insertnightattendence");
                    parames.put("arraylistnight", String.valueOf(jArraynight));
                    parames.put("property_id", propertyid);
                    return parames;
                }
            };
            //30000
            DefaultRetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);//1000*60=60000//1min
            postRequest.setRetryPolicy(policy);
           /* RequestQueue requestQueue = Volley.newRequestQueue(ServiceSyncRecord_Night_Attendance.this);
            requestQueue.add(postRequest);
*/
            App.getInstance().addToRequestQueue(postRequest, "tag5");


        } catch (Exception e) {
            e.printStackTrace();

            AppError.handleUncaughtException(e, " ServiceSyncRecord_Night_Attendance_Secugen - Sendofflinehostelattendance apicall", propertyid, versionCode);

        }
    }

    public void getResponsetenantlist(String resultobj) {
        try {
            JSONObject jsmain = new JSONObject(resultobj);
            String result = jsmain.getString("flag");
            String message = jsmain.getString("message");
            if (result.equalsIgnoreCase("Y")) {
                String nightattendance = jsmain.getString("arraylistnight");

                if (nightattendance.equalsIgnoreCase("0") || nightattendance.equalsIgnoreCase(null) || nightattendance.equals("") || nightattendance.equals("null")) {

                } else {
                    JSONArray jsonArraynight = jsmain.getJSONArray("arraylistnight");
                    String idnight = jsonArraynight.getJSONObject(0).getString("local_id");
                    String query = "UPDATE krooms_nightattendance set status='0' where id <= '" + idnight + "'";
                    db.execSQL(query);
                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, " ServiceSyncRecord_Night_Attendance_Secugen - getResponsetenantlist", propertyid, versionCode);

        }
    }


}
