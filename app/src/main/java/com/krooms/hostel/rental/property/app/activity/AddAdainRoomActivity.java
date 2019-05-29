package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Roommodel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinod on 8/7/2017.
 */
public class AddAdainRoomActivity extends Activity implements
        View.OnClickListener {
    ArrayList<Roommodel> roomnoarraylist;
    String forwhom;
    Button createrooms;
    int slotsno;
    RelativeLayout topPanelBar;
    int totalroomnovalue = 20;
    GridView gridview;
    EditText no_of_rooms_selection_edit;
    Spinner for_whom_selection_spinner;
    String[] whoms = {"For Whom", "Boys", "Girls", "Family", "Businessman", "Employee"};
    int roomnovaluemainno = 0;
    LinearLayout mainlayout;
    String propertyidvalue;
    Button button_add_more;
    String countvalue, for_whom;
    String maincountval;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.againroomaddlayout);
        in = getIntent();
        propertyidvalue = in.getStringExtra("propertyid");
        countvalue = in.getStringExtra("countvalue");
        for_whom = in.getStringExtra("for_whom");
        roomnovaluemainno = Integer.parseInt(countvalue);
        maincountval = countvalue;
        no_of_rooms_selection_edit = (EditText) findViewById(R.id.no_of_rooms_selection_edit);
        createrooms = (Button) findViewById(R.id.addrooms);
        createrooms.setClickable(false);
        topPanelBar = (RelativeLayout) findViewById(R.id.topPanelBar);
        createrooms.setOnClickListener(AddAdainRoomActivity.this);

        topPanelBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddAdainRoomActivity.this, HostelListActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addrooms:

                String roomnovalue = no_of_rooms_selection_edit.getText().toString();
                if (roomnovalue.equalsIgnoreCase("")) {
                    createrooms.setClickable(true);
                    Toast.makeText(AddAdainRoomActivity.this, "Please Enter Room No", Toast.LENGTH_SHORT).show();
                } else {
                    int valuesetcount = Integer.parseInt(roomnovalue);
                    createRoomsApi(valuesetcount, propertyidvalue);
                }

                break;
        }
    }

    public void createRoomsApi(final int valuesetcount, final String propertyidvalue) {
        final ProgressDialog progressDialog = new ProgressDialog(AddAdainRoomActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url = WebUrls.MAIN_URL;

        createrooms.setClickable(false);

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        getResponsedata(response, valuesetcount);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error != null && error.getMessage() != null) {
                            Toast.makeText(getApplicationContext(), "error VOLLEY " + error.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "secondRoomsAddFirst");
                params.put("property_id", propertyidvalue);
                params.put("noofrooms", String.valueOf(valuesetcount));
                return params;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(AddAdainRoomActivity.this);
        queue.add(strRequest);
    }

    public void getResponsedata(String result, int valuesetcount) {
        try {
            JSONObject jo = new JSONObject(result);
            result = jo.getString("flag");
            String message = jo.getString("message");
            createrooms.setClickable(true);
            if (result.equalsIgnoreCase("Y")) {
                String totalcount = jo.getString("records");
                Toast.makeText(AddAdainRoomActivity.this, message, Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(this, PropertyRoomsCreationActivity.class);
                mIntent.putExtra("PropertyId", propertyidvalue);
                mIntent.putExtra("countvalue", totalcount);
                mIntent.putExtra("for_whom", for_whom);
                startActivity(mIntent);
                finish();
                overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
            } else {
                Toast.makeText(AddAdainRoomActivity.this, message, Toast.LENGTH_LONG).show();
            }
            Log.d("result..", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(AddAdainRoomActivity.this, HostelListActivity.class);
                startActivity(i);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
