package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 05-Sep-18.
 */

public class RoomChange_And_ExitTenantSelection_Activity extends Activity implements View.OnClickListener {

    Button btn_roomchange,btn_exittenant;
    RelativeLayout back;
    ScrollView layroomchange,laycleardue;
    public static String uname,ufathername,ufathercontact,
            umobile,uaddress,uamount,uhiredate,currentdate_value1,
            uleavedate,uemail,UId="",UBroomid,OwnerId,Dues,userid,
            propertyid;

    EditText txtdueamout;
    Button btndueclear,btngeneratepdf,btnexituser,btnyes,btnno;

   // flback_button
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomchange_exittenant_selection);
        findViewbyId();
        getIntentValues();

        txtdueamout.setText(Dues);

        btn_exittenant.setOnClickListener(this);
        btn_roomchange.setOnClickListener(this);
        back.setOnClickListener(this);
        btndueclear.setOnClickListener(this);
        btngeneratepdf.setOnClickListener(this);
    }

    private void getIntentValues() {

        Intent intent=getIntent();
        uname=intent.getStringExtra("Tname");
        ufathername=intent.getStringExtra("Tfather");
        umobile=intent.getStringExtra("Tmobile");
        uemail=intent.getStringExtra("Temail");
        uaddress=intent.getStringExtra("Taddress");
        uhiredate=intent.getStringExtra("Thire");
        uleavedate=intent.getStringExtra("Tleave");
        uamount=intent.getStringExtra("Tamount");
        ufathercontact=intent.getStringExtra("TFatherCont");
        UId=intent.getStringExtra("NTenantid");
        UBroomid=intent.getStringExtra("TBookRoom");
        Dues=intent.getStringExtra("TDue");
        OwnerId=intent.getStringExtra("Towner");
        userid=intent.getStringExtra("Tuserid");
        propertyid=intent.getStringExtra("sepratepropertyid");

    }

    private void findViewbyId() {
        btn_roomchange=(Button)findViewById(R.id.btn_roomchange);
        btn_exittenant=(Button)findViewById(R.id.btn_exittenant);
        back=(RelativeLayout)findViewById(R.id.flback_button);
        layroomchange=(ScrollView)findViewById(R.id.Scrollroomchange);
        laycleardue=(ScrollView)findViewById(R.id.Scrollcleardue);

        txtdueamout=(EditText)findViewById(R.id.txtdueamout);
        txtdueamout.setEnabled(false);
        btndueclear=(Button)findViewById(R.id.due_clear);
        btngeneratepdf=(Button)findViewById(R.id.exitdownload);
    }


    @Override
    public void onClick(View view) {

       switch (view.getId()){

           case R.id.btn_exittenant:
               exitTenant();
               break;

           case R.id.btn_roomchange:

               changeRoom();

               break;

           case R.id.flback_button:
               Intent intenti=new Intent(RoomChange_And_ExitTenantSelection_Activity.this,MyPropertyUsersListActivity.class);
               startActivity(intenti);
               break;


           case R.id.due_clear:
               Intent ii=new Intent(RoomChange_And_ExitTenantSelection_Activity.this,UserOwner_RemoveActivity.class);
               ii.putExtra("DuePayment",txtdueamout.getText().toString());
               ii.putExtra("tenantid",UId);
               ii.putExtra("userid",userid);
               ii.putExtra("propertyid",propertyid);
               ii.putExtra("ownerid",OwnerId);
               ii.putExtra("roomid",UBroomid);

               //new changes for back from clear due amount

               ii.putExtra("Tname",uname);
               ii.putExtra("Tfather",ufathername);
               ii.putExtra("Tmobile",umobile);
               ii.putExtra("Temail",uemail);
               ii.putExtra("Taddress",uaddress);
               ii.putExtra("Thire",uhiredate);
               ii.putExtra("Tleave",uleavedate);
               ii.putExtra("Tamount",uamount);
               ii.putExtra("TFatherCont",ufathercontact);
               startActivity(ii);

               break;


           case R.id.exitdownload:

               break;


       }

    }

    private void changeRoom() {

        if(Dues.equals("0") || Dues.equals(""))
        {
            //Toast.makeText(this, "Change Rooms", Toast.LENGTH_SHORT).show();
            vacantRoom();
        }
        else
        {
            Toast.makeText(this, "Please Clear Due Amount", Toast.LENGTH_SHORT).show();

           laycleardue.setVisibility(View.VISIBLE);
           layroomchange.setVisibility(View.GONE);

        }

    }


    private void exitTenant() {

        Intent ii=new Intent(RoomChange_And_ExitTenantSelection_Activity.this,UserRemoveActivity.class);
        ii.putExtra("Tname",uname);
        ii.putExtra("Towner",OwnerId);
        ii.putExtra("Taddress",uaddress);
        ii.putExtra("Tamount",uamount);
        ii.putExtra("Tmobile",umobile);
        ii.putExtra("Tfather",ufathername);
        ii.putExtra("Thire",uhiredate);
        ii.putExtra("Tleave",uleavedate);
        ii.putExtra("Temail",uemail);
        ii.putExtra("NTenantid",UId);
        ii.putExtra("TBookRoom",UBroomid);
        ii.putExtra("TDue",Dues);
        ii.putExtra("sepratepropertyid",propertyid);
        ii.putExtra("TFatherCont", ufathercontact);
        ii.putExtra("Tuserid",userid);
        startActivity(ii);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(RoomChange_And_ExitTenantSelection_Activity.this,MyPropertyUsersListActivity.class));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void vacantRoom() {

        final ProgressDialog dialog = new ProgressDialog(RoomChange_And_ExitTenantSelection_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action","Vacantroom");
            params.put("property_id",propertyid);
            params.put("room_id",UBroomid);
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

    public void getResponseData(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                //vacant_btn.setEnabled(false);
                vacantbackupRoom();

            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //backup room change
    //vacant room

    private void vacantbackupRoom() {

        final ProgressDialog dialog = new ProgressDialog(RoomChange_And_ExitTenantSelection_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action","getroomchangebackup");
            params.put("property_id",propertyid);
            params.put("tenant_id",UId);
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

    public void getResponseDataRoomchange(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Intent ii=new Intent(RoomChange_And_ExitTenantSelection_Activity.this,HostelDetailActivity.class);
                ii.putExtra("property_id",propertyid);
                ii.putExtra("roomchnage","1");
                ii.putExtra("roomidnewvalue",UBroomid);
                ii.putExtra("tenantid",UId);
                //ii.putExtra("roomno",ii.getStringExtra("tbookedroomno"));
                startActivity(ii);

            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //

}
