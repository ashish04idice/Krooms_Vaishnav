package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Tenant_Attendance_Notification_ListAdapter;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 7/19/2018.
 */

public class ShowAttendance_Notification extends AppCompatActivity {

    ListView listView;
    Tenant_Attendance_Notification_ListAdapter  attendance_notification_listAdapter;
    public static ArrayList<TenantModal> attendance_list;
    private String property_id="",parent_id="";
    RelativeLayout back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_attendance_notificationlayout);
        listView=(ListView)findViewById(R.id.listview);
        back=(RelativeLayout)findViewById(R.id.flback_button);
        Intent intent=getIntent();
        parent_id= intent.getStringExtra("parentid");
        property_id=intent.getStringExtra("propertyid");
        attendance_list=new ArrayList<>();

        if (NetworkConnection.isConnected(ShowAttendance_Notification.this)) {
            getNotificationSeen();


        }else{
            Toast.makeText(ShowAttendance_Notification.this, "Please check network connection", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ShowAttendance_Notification.this,HostelListActivity.class);
                startActivity(intent);

            }
        });

    }

    private void getNotificationSeen() {

        final ProgressDialog dialog = new ProgressDialog(ShowAttendance_Notification.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action","notification_seen");
            params.put("property_id",property_id);
            params.put("parent_id",parent_id);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataSeen(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDataSeen(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                //Toast.makeText(ShowAttendance_Notification.this,message, Toast.LENGTH_LONG).show();
                getNotification();
            }else{
               //Toast.makeText(ShowAttendance_Notification.this,message, Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //notification seen

    private void getNotification() {

        final ProgressDialog dialog = new ProgressDialog(ShowAttendance_Notification.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();


        try {
            JSONObject params = new JSONObject();
            params.put("action","attendance_record_send");
            params.put("property_id",property_id);
            params.put("parent_id",parent_id);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseData(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseData(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("attendance_record");
                for (int i = 0; i < jsonarray.length(); i++) {

                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    TenantModal recordmodel = new TenantModal();
                    recordmodel.setTenant_name(jsonobj.getString("tenant_name"));
                    recordmodel.setTenant_roomno(jsonobj.getString("room_no"));
                    recordmodel.setAtt_status(jsonobj.getString("attendance_status"));
                    recordmodel.setDate(jsonobj.getString("date"));
                    recordmodel.setTime(jsonobj.getString("time"));
                    attendance_list.add(recordmodel);
                }

                attendance_notification_listAdapter=new Tenant_Attendance_Notification_ListAdapter(ShowAttendance_Notification.this,attendance_list);
                listView.setAdapter(attendance_notification_listAdapter);
                attendance_notification_listAdapter.notifyDataSetChanged();

            }else{
                Toast.makeText(ShowAttendance_Notification.this,message, Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(ShowAttendance_Notification.this,HostelListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
