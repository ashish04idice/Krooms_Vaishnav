package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ashishdice on 22/4/17.
 */

public class Attandance_Device_Selection_Activity extends Activity {

    Button btn_secugen, btn_startak;
    RelativeLayout backarraow;
    Intent in;
    String propertyid, versionCode = "";
    Context context;
    DatabaseHandler handlerdb;
    RadioButton radioOffline, radioOnline;
    RadioGroup radioGroup;
    String currentDateandTime = "", urlname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_device_selection_layout);
        context = getApplicationContext();
        handlerdb = new DatabaseHandler(context);
        in = getIntent();
        propertyid = in.getStringExtra("propertyid");
        btn_secugen = (Button) findViewById(R.id.btn_secugen);
        btn_startak = (Button) findViewById(R.id.btn_startek);
        backarraow = (RelativeLayout) findViewById(R.id.flback_button);
        radioOffline = (RadioButton) findViewById(R.id.radioOffline);
        radioOnline = (RadioButton) findViewById(R.id.radioOnline);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateandTime = sdf.format(new Date());
        urlname = WebUrls.MAIN_ATTENDACE_URL;

        btn_secugen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonID != -1) {
                    if (NetworkConnection.isConnected(Attandance_Device_Selection_Activity.this)) {
                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                        String selectedRadioButtonText = selectedRadioButton.getText().toString();
                        getVersionDevice();
                        String device_name = "Secugen";
                        Utility.setSharedPreference(Attandance_Device_Selection_Activity.this, "deviceMode", selectedRadioButtonText);
                        sendDeviceInfoSecugen(propertyid, urlname, currentDateandTime, selectedRadioButtonText, device_name);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Please select Attendance mode", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_startak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonID != -1) {

                    if (NetworkConnection.isConnected(Attandance_Device_Selection_Activity.this)) {
                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                        String selectedRadioButtonText = selectedRadioButton.getText().toString();
                        getVersionDevice();
                        String device_name = "Startak";
                        sendDeviceInfoStartek(propertyid, urlname, currentDateandTime, selectedRadioButtonText, device_name);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Please select Attendance mode", Toast.LENGTH_SHORT).show();
                }
            }
        });


        backarraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Attandance_Device_Selection_Activity.this, HostelListActivity.class);
                startActivity(ii);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent ii = new Intent(Attandance_Device_Selection_Activity.this, HostelListActivity.class);
                startActivity(ii);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getVersionDevice() {

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(Attandance_Device_Selection_Activity.this.getPackageName(), 0);
            versionCode = info.versionName;
        } catch (Exception e) {
        }
    }


    private void sendDeviceInfoSecugen(String propertyid, String urlname, String currentDateandTime, String selectedRadioButtonText, String device_name) {

        final ProgressDialog dialog = new ProgressDialog(Attandance_Device_Selection_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "attendancedeviceInfo");
            params.put("property_id", propertyid);
            params.put("url_name", urlname);
            params.put("device_name", device_name);
            params.put("mode", selectedRadioButtonText);
            params.put("date_time", currentDateandTime);
            params.put("versionCode", versionCode);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataSecugen(result);
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

    public void getResponseDataSecugen(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                //int studentcount= Integer.parseInt(jsmain.getString("Count"));
               // Utility.setIntegerSharedPreference(context,"StudentCount",studentcount);
                Intent ii = new Intent(Attandance_Device_Selection_Activity.this, Attandance_Home_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", "Secugen");
                startActivity(ii);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendDeviceInfoStartek(String propertyid, String urlname, String currentDateandTime, String selectedRadioButtonText, String device_name) {

        final ProgressDialog dialog = new ProgressDialog(Attandance_Device_Selection_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "attendancedeviceInfo");
            params.put("property_id", propertyid);
            params.put("url_name", urlname);
            params.put("device_name", device_name);
            params.put("mode", selectedRadioButtonText);
            params.put("date_time", currentDateandTime);
            params.put("versionCode", versionCode);
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
            if (status.equalsIgnoreCase("Y")) {
                //int studentcount= Integer.parseInt(jsmain.getString("Count"));
               // Utility.setIntegerSharedPreference(context,"StudentCount",studentcount);
                Intent ii = new Intent(Attandance_Device_Selection_Activity.this, Attandance_Home_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", "Startak");
                startActivity(ii);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
