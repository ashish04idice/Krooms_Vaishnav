package com.krooms.hostel.rental.property.app.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.krooms.hostel.rental.property.app.activity.Capture_FingerImage_Terms_Activity;
import com.krooms.hostel.rental.property.app.activity.HostelListActivity;
import com.krooms.hostel.rental.property.app.activity.Owner_TermsandCondition_Activity;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.PaymentPaidUnpaidModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by admin on 3/28/2018.
 */

public class TermsAndCondition_ActivityDailog extends Activity implements View.OnClickListener {


    EditText txtterms;
    Button btntenant, btnparent;
    String propertyid = "", tenant_id = "";
    private SharedStorage mSharedStorage;
    RelativeLayout crossimg;
    LinearLayout btnlayout;
    String terms = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition_dialog_activity);
        mSharedStorage = SharedStorage.getInstance(this);
        txtterms = (EditText) findViewById(R.id.termText);
        btntenant = (Button) findViewById(R.id.btntenant);
        btnparent = (Button) findViewById(R.id.btnparent);
        crossimg = (RelativeLayout) findViewById(R.id.back_button);
        btnlayout = (LinearLayout) findViewById(R.id.btnlayout);
        btntenant.setOnClickListener(this);
        btnparent.setOnClickListener(this);
        crossimg.setOnClickListener(this);
        Intent ii = getIntent();
        tenant_id = ii.getStringExtra("Tenantid");
        propertyid = ii.getStringExtra("propertyid");

        if (NetworkConnection.isConnected(TermsAndCondition_ActivityDailog.this)) {
            getTerms();
        } else {
            Toast.makeText(TermsAndCondition_ActivityDailog.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btntenant:
                //  Toast.makeText(this, "Agree", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TermsAndCondition_ActivityDailog.this, Capture_FingerImage_Terms_Activity.class);
                intent.putExtra("Tenantid", tenant_id);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("Terms", terms);
                intent.putExtra("Key", "1");
                startActivity(intent);
                // finish();
                break;


            case R.id.btnparent:

                Intent intent1 = new Intent(TermsAndCondition_ActivityDailog.this, Capture_FingerImage_Terms_Activity.class);
                intent1.putExtra("Tenantid", tenant_id);
                intent1.putExtra("propertyid", propertyid);
                intent1.putExtra("Terms", terms);
                intent1.putExtra("Key", "2");
                startActivity(intent1);
                break;


            case R.id.back_button:
                finish();
                break;

        }

    }


    private void getTerms() {

        final ProgressDialog dialog = new ProgressDialog(TermsAndCondition_ActivityDailog.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "gettermsandcondition");
            params.put("property_id", propertyid);
            //params.put("terms", URLEncoder.encode(termsandcondition, "utf-8"));
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
                    Toast.makeText(TermsAndCondition_ActivityDailog.this, "Please try again", Toast.LENGTH_LONG).show();
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

    public void getResponseData(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    terms = jsonobj.getString("terms");
                }

                btnlayout.setVisibility(View.VISIBLE);
                txtterms.setText(terms);
                //Toast.makeText(TermsAndCondition_ActivityDailog.this,message, Toast.LENGTH_SHORT).show();

            } else {
                btnlayout.setVisibility(View.GONE);
                Toast.makeText(TermsAndCondition_ActivityDailog.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
