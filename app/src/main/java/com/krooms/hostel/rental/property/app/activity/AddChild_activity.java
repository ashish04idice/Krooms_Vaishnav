package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.ListAdapterchild;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Childlistmodel;
import com.krooms.hostel.rental.property.app.modal.Product;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashish on 31/1/17.
 */

public class AddChild_activity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    EditText editText;
    RelativeLayout btnsearch, btnbackarrow;
    public ListAdapterchild listadapter;
    ArrayList<Product> products = new ArrayList<Product>();
    Context context;
    String tenantmobile = "";
    Button btnaddchild;
    String TenantId = "";
    String tenamtmobileno = "";
    Intent in;
    String useridmainvalue;
    LinearLayout otpverificationlayout, ownerlayout;
    Button btn_submit;
    EditText edittext_otp;
    String otpverificationvalue;
    private SharedStorage mSharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addchild_activity);
        context = this;
        mSharedStorage = SharedStorage.getInstance(this);
        in = getIntent();
        useridmainvalue = in.getStringExtra("useridvalue");
        editText = (EditText) findViewById(R.id.psearchmobile);
        listView = (ListView) findViewById(R.id.plistview);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        ownerlayout = (LinearLayout) findViewById(R.id.ownerlayout);
        edittext_otp = (EditText) findViewById(R.id.edittext_otp);
        otpverificationlayout = (LinearLayout) findViewById(R.id.otpverificationlayout);
        btnsearch = (RelativeLayout) findViewById(R.id.pserchbtn);
        btnbackarrow = (RelativeLayout) findViewById(R.id.pback_button);
        btnaddchild = (Button) findViewById(R.id.btnaddchild);
        btnsearch.setOnClickListener(this);
        btnbackarrow.setOnClickListener(this);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otpverificationvalue = edittext_otp.getText().toString();
                if (otpverificationvalue.equals("")) {
                    edittext_otp.setError("Please enter otp");
                } else {
                    new OTPVerificationJson().execute();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.pserchbtn:
                getchildlist();
                break;
            case R.id.pback_button:
                Intent i = new Intent(AddChild_activity.this, HostelListActivity.class);
                startActivity(i);
                break;
        }
    }


    public void showResult(View v) {
        try {

            for (Product p : listadapter.getBox()) {
                if (p.box) {

                    TenantId += "," + p.getTenantid();
                    tenamtmobileno += "," + p.getMobilenovalue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (products.size() == 0 || TenantId.equals("")) {
            Toast.makeText(AddChild_activity.this, "Please Select Child", Toast.LENGTH_SHORT).show();

        } else {
            new AddchildJson().execute();
        }

    }

    private void getchildlist() {
        tenantmobile = editText.getText().toString().trim();

        if (tenantmobile.length() == 0 || tenantmobile.length() < 10) {
            Toast.makeText(AddChild_activity.this, "Please Enter valid Mobileno ", Toast.LENGTH_SHORT).show();
        } else {
            new Getchilddetail().execute();
        }
    }

    public class Getchilddetail extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "searchChild"));
            listNameValuePairs.add(new BasicNameValuePair("mobile_no", tenantmobile));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            String msg = null;
            String status = null;
            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("true")) {
                        products.clear();
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Childlistmodel cmodel = new Childlistmodel();
                            Product p = new Product();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String fname = jsonObject.getString("tenant_fname");
                            String flast = jsonObject.getString("tenant_lname");
                            String parentid = jsonObject.getString("parent_id");
                            String mobilenovalue = jsonObject.getString("contact_number");
                            String tenantid = jsonObject.getString("id");
                            String propertyidmain = jsonObject.getString("property_id");
                            mSharedStorage.setBookedPropertyId(propertyidmain);
                            String tenanthostalname = jsonObject.getString("tenant_hostalname");
                            p.setName(fname + " " + flast);
                            p.setHostelname(tenanthostalname);
                            p.setParentid(parentid);
                            p.setMobilenovalue(mobilenovalue);
                            p.setTenantid(tenantid);
                            cmodel.setTenant_name(fname + " " + flast);
                            cmodel.setHostal_name(tenanthostalname);
                            products.add(p);
                        }
                        listadapter = new ListAdapterchild(context, products);
                        listView.setAdapter(listadapter);
                        listadapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class AddchildJson extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "addchild"));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", TenantId));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", useridmainvalue));
            listNameValuePairs.add(new BasicNameValuePair("mobile_no", tenamtmobileno));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        ownerlayout.setVisibility(View.GONE);
                        btnaddchild.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        otpverificationlayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Please verify OTP Then " + msg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class OTPVerificationJson extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "verifyChildOtp"));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", useridmainvalue));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", TenantId));
            listNameValuePairs.add(new BasicNameValuePair("otp", otpverificationvalue));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("true")) {
                        products.clear();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddChild_activity.this, HostelListActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
