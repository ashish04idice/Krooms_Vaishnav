package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.ElectricityModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 6/5/2018.
 */

public class Change_Room extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout back_btn;
    private Button vacant_btn,change_btn;
    private String property_id="",roomid="",tenant_id="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomchange_layout);
        back_btn=(RelativeLayout)findViewById(R.id.flback_button);
        vacant_btn=(Button)findViewById(R.id.btnvacantroom);
        change_btn=(Button)findViewById(R.id.btnchangeroom);

        Intent ii=getIntent();
        property_id=ii.getStringExtra("property_id");
        roomid=ii.getStringExtra("roomidnewvalue");
        tenant_id=ii.getStringExtra("tenantid");

        vacant_btn.setOnClickListener(this);
        change_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnvacantroom:

              vacantRoom();

                break;

            case R.id.btnchangeroom:

               Intent ii=new Intent(Change_Room.this,HostelDetailActivity.class);
                ii.putExtra("property_id",property_id);
                ii.putExtra("roomchnage","1");
                ii.putExtra("roomidnewvalue",roomid);
                ii.putExtra("tenantid",tenant_id);
                startActivity(ii);
                break;

            case R.id.flback_button:
                Intent intent=new Intent(Change_Room.this,HostelListActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent ii=new Intent(Change_Room.this,HostelListActivity.class);
            startActivity(ii);
        }
        return super.onKeyDown(keyCode, event);
    }

    //vacant room

    private void vacantRoom() {

        final ProgressDialog dialog = new ProgressDialog(Change_Room.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action","Vacantroom");
            params.put("property_id",property_id);
            params.put("room_id",roomid);
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
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                vacant_btn.setEnabled(false);
                vacantbackupRoom();

            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void vacantbackupRoom() {

        final ProgressDialog dialog = new ProgressDialog(Change_Room.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action","getroomchangebackup");
            params.put("property_id",property_id);
            params.put("tenant_id",tenant_id);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataRoomchange(result);
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

    public void getResponseDataRoomchange(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //



}


