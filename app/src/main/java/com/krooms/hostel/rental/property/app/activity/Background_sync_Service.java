package com.krooms.hostel.rental.property.app.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuradhaidice on 23/12/16.
 */

public class Background_sync_Service extends Service {
    String condtition = "false";
    int i;
    int servicecount = 0;
    SQLiteDatabase sq;
    int datalenght;
    String master_time_value;
    Context sContext;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        master_time_value = GetAttendenceActivity.master_time;
        HelperAttedence hdb = new HelperAttedence(Background_sync_Service.this);
        sq = hdb.getWritableDatabase();
        Cursor cs = sq.rawQuery("select * from Attendence", null);
        datalenght = cs.getCount();
        System.out.print(cs.getCount());
        while (cs.moveToNext()) {
            String propertyid_value = cs.getString(0);
            String ownerid_value = cs.getString(1);
            String tenantid_value = cs.getString(2);
            String userid_value = cs.getString(3);
            String roomid_value = cs.getString(4);
            String roomno = cs.getString(5);
            String tenant_name = cs.getString(6);
            String absent_present = cs.getString(7);
            String date_value = cs.getString(8);
            String time_value = cs.getString(9);
            new SubmitAttendenceJSONArray(propertyid_value, ownerid_value, tenantid_value, userid_value, roomid_value, roomno, tenant_name, absent_present, date_value, time_value).execute();
        }
        if (cs.moveToLast()) {
            String tenant_name = cs.getString(6);
            sq.execSQL("delete from Attendence");
        }
        if (datalenght == servicecount) {
            stopSelf();
        }
        return Service.START_NOT_STICKY;
    }

    //this class is used getting value by webservices
    private class SubmitAttendenceJSONArray extends AsyncTask<String, String, String> {
        String propertyid_value, ownerid_value, tenantid_value, userid_value, roomid_value, roomno, tenant_name, absent_present, date_value, time_value;
        int count;
        String name;
        String result = null;
        private boolean IsError = false;
        String message;
        String fullname = "";
        public SubmitAttendenceJSONArray(String propertyid_value, String ownerid_value, String tenantid_value, String userid_value, String roomid_value, String roomno, String tenant_name, String absent_present, String date_value, String time_value) {
            this.propertyid_value = propertyid_value;
            this.ownerid_value = ownerid_value;
            this.tenantid_value = tenantid_value;
            this.userid_value = userid_value;
            this.roomid_value = roomid_value;
            this.roomno = roomno;
            this.tenant_name = tenant_name;
            this.absent_present = absent_present;
            this.date_value = date_value;
            this.time_value = time_value;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_ATTENDACE_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
                nameValuePairs.add(new BasicNameValuePair("action", "insertattendence"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_value));
                nameValuePairs.add(new BasicNameValuePair("owner_id", ownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid_value));
                nameValuePairs.add(new BasicNameValuePair("user_id", userid_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", roomid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_name", tenant_name));
                nameValuePairs.add(new BasicNameValuePair("room_no", roomno));
                nameValuePairs.add(new BasicNameValuePair("absent_present", absent_present));
                nameValuePairs.add(new BasicNameValuePair("date", date_value));
                nameValuePairs.add(new BasicNameValuePair("time", time_value));
                nameValuePairs.add(new BasicNameValuePair("mtime", master_time_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
                    condtition = "true";
                    servicecount = servicecount + 1;
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
                if (result.equalsIgnoreCase("Y")) {
                } else if (result.equals("N")) {
                }
        }
    }
}