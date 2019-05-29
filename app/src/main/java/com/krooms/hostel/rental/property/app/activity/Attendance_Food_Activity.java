package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Food_Attendance_RecordsAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.modal.EnrollTenantModel;
import com.krooms.hostel.rental.property.app.modal.Foodmodel;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ashish on 4/29/2017.
 */
public class Attendance_Food_Activity extends Activity {

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
    public static ArrayList<UserModel> alist;
    private boolean[] matched = new boolean[1];
    Calendar calnder;
    String localTime, formattedDate;
    MediaPlayer mp;
    public static ArrayList<EnrollTenantModel> listtenantrecord;
    String datafinger;
    String matchfinger;
    DatabaseHandler handlerdb;
    EditText txtroomno;
    private Cursor attendance_cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_food_layout);
        context = getApplicationContext();
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
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(calnder.getTime());

        if (NetworkConnection.isConnected(Attendance_Food_Activity.this)) {
            getFoodRecord();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


        flback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Tenant_Details_Activity.class);
                intent.putExtra("propertyid", propertyid);
                startActivity(intent);
            }
        });

        layouthostelattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Tenant_Details_Activity.class);
                intent.putExtra("propertyid", propertyid);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(context, Tenant_Details_Activity.class);
                intent.putExtra("propertyid", propertyid);
                startActivity(intent);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getFoodRecord() {
        final ProgressDialog dialog = new ProgressDialog(Attendance_Food_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "showattendencefood");
            params.put("property_id", propertyid);
            params.put("date", formattedDate);
            String url = WebUrls.MAIN_URL;
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

    //volly for Registration
    public void getResponseData(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            list = new ArrayList();

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    recordmodel = new Foodmodel();
                    recordmodel.setAttendencestatus(jsonobj.getString("attendence_status"));
                    recordmodel.setRoomno(jsonobj.getString("room_no"));
                    recordmodel.setTname(jsonobj.getString("tenant_name"));
                    list.add(recordmodel);
                }
                adapter = new Food_Attendance_RecordsAdapter(Attendance_Food_Activity.this, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(Attendance_Food_Activity.this, "No Record Found", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
