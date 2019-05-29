package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.krooms.hostel.rental.property.app.adapter.Food_Attendance_RecordsAdapter;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter_newAttendence;
import com.krooms.hostel.rental.property.app.adapter.Night_Attendance_Absent_Adapter;
import com.krooms.hostel.rental.property.app.adapter.TenantRecordListAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.Foodmodel;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 7/1/2017.
 */
public class Night_Attendance_AbsentList_Activity extends AppCompatActivity {

    ListView listView;
    Night_Attendance_Absent_Adapter adapter;
    ArrayList<TenantModal> userlist;
    Context context;
    RelativeLayout back;
    Intent in;
    String propertyid;
    Calendar calnder;
    String formattedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.night_attendance_layout);
        in = getIntent();
        propertyid = in.getStringExtra("propertyid");
        context = this;
        listView = (ListView) findViewById(R.id.tenantlist);
        back = (RelativeLayout) findViewById(R.id.flback_button);
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(calnder.getTime());


        if (NetworkConnection.isConnected(Night_Attendance_AbsentList_Activity.this)) {
            getNightRecord();
        } else {
            Toast.makeText(Night_Attendance_AbsentList_Activity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Night_Attendance_AbsentList_Activity.this, HostelListActivity.class);
                startActivity(intent);

            }
        });


    }

    private void getNightRecord() {

        final ProgressDialog dialog = new ProgressDialog(Night_Attendance_AbsentList_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();


        try {
            JSONObject params = new JSONObject();
            params.put("action", "nightattendenceabsentrecords");
            params.put("property_id", propertyid);
            params.put("date", formattedDate);
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
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            userlist = new ArrayList();

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    int j = i + 1;
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    TenantModal recordmodel = new TenantModal();
                    recordmodel.setTenant_name(jsonobj.getString("tenant_name"));
                    recordmodel.setTenant_roomno(jsonobj.getString("roomno"));
                    recordmodel.setNumber(String.valueOf(j));
                    userlist.add(recordmodel);
                }

                adapter = new Night_Attendance_Absent_Adapter(Night_Attendance_AbsentList_Activity.this, userlist);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(Night_Attendance_AbsentList_Activity.this, message, Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ii = new Intent(Night_Attendance_AbsentList_Activity.this, HostelListActivity.class);
            startActivity(ii);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
