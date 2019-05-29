package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Payment_Paid_Unpaid_Adapter;
import com.krooms.hostel.rental.property.app.dialog.TermsAndCondition_ActivityDailog;
import com.krooms.hostel.rental.property.app.modal.PaymentPaidUnpaidModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by admin on 3/27/2018.
 */

public class Owner_TermsandCondition_Activity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    String propertyid = "", termsandcondition = "";
    EditText txtterms;
    Button submit;
    RelativeLayout back_arrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_termsandcondition_layout);
        intent = getIntent();
        propertyid = intent.getStringExtra("Propertyid");
        back_arrow = (RelativeLayout) findViewById(R.id.lback_button);
        txtterms = (EditText) findViewById(R.id.terms);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back_arrow.setOnClickListener(this);

        if (NetworkConnection.isConnected(Owner_TermsandCondition_Activity.this)) {
            getTerms();
        } else {
            Toast.makeText(Owner_TermsandCondition_Activity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lback_button:
                Intent ii = new Intent(Owner_TermsandCondition_Activity.this, HostelListActivity.class);
                startActivity(ii);
                break;

            case R.id.submit:

                termsandcondition = txtterms.getText().toString().trim();

                if (termsandcondition.length() == 0) {
                    Toast.makeText(this, "Please Enter Terms And Condition", Toast.LENGTH_SHORT).show();
                } else {
                    if (NetworkConnection.isConnected(Owner_TermsandCondition_Activity.this)) {
                        SendTerms();
                    } else {
                        Toast.makeText(Owner_TermsandCondition_Activity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
                    }
                    hideSoftKeyboard(Owner_TermsandCondition_Activity.this);
                }
                break;
        }

    }


    private void SendTerms() {

        final ProgressDialog dialog = new ProgressDialog(Owner_TermsandCondition_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "inserttermsandcondition");
            params.put("property_id", propertyid);
            params.put("terms", URLEncoder.encode(termsandcondition, "utf-8"));
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
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(Owner_TermsandCondition_Activity.this, message, Toast.LENGTH_SHORT).show();
                Intent ii = new Intent(Owner_TermsandCondition_Activity.this, HostelListActivity.class);
                startActivity(ii);

            } else {
                Toast.makeText(Owner_TermsandCondition_Activity.this, message, Toast.LENGTH_SHORT).show();
                Intent ii = new Intent(Owner_TermsandCondition_Activity.this, HostelListActivity.class);
                startActivity(ii);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getTerms() {

        final ProgressDialog dialog = new ProgressDialog(Owner_TermsandCondition_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "gettermsandcondition");
            params.put("property_id", propertyid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataTerms(result);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(Owner_TermsandCondition_Activity.this, "Please try again", Toast.LENGTH_LONG).show();
                }
            });

            //1 min time out
            request_json.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDataTerms(String result) {
        try {
            String status, message, terms = "";
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    terms = jsonobj.getString("terms");
                }

                txtterms.setText(terms);
                //Toast.makeText(TermsAndCondition_ActivityDailog.this,message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Owner_TermsandCondition_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
