package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.krooms.hostel.rental.property.app.adapter.Single_Tenant_RecordsAdapter;
import com.krooms.hostel.rental.property.app.modal.Foodmodel;
import com.krooms.hostel.rental.property.app.modal.TenantRecordmodel;
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
 * Created by admin on 6/7/2017.
 */
public class Tenant_Records_show_in_tablayout extends Activity {

    String formattedDate;
    Single_Tenant_RecordsAdapter adapter;
    ArrayList<TenantRecordmodel> list,list_out_array;
    LinearLayout lastin_layout,lastout_layout,lastin_main_layout,lastout_main_layout;
    RelativeLayout back_button;
    ListView list_out,list_in;
    Intent in;
    String propertyid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_records_tab_layout);
        in=getIntent();
        propertyid=in.getStringExtra("propertyid");
        lastout_layout=(LinearLayout)findViewById(R.id.lastout_layout);
        lastin_layout=(LinearLayout)findViewById(R.id.lastin_layout);
        back_button=(RelativeLayout)findViewById(R.id.back_button);
        lastin_main_layout=(LinearLayout)findViewById(R.id.lastin_main_layout);
        lastout_main_layout=(LinearLayout)findViewById(R.id.lastout_main_layout);
        list_out=(ListView)findViewById(R.id.list_out);
        list_in=(ListView)findViewById(R.id.list_in);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Tenant_Records_show_in_tablayout.this,Tenant_Details_Activity.class);
                i.putExtra("propertyid",propertyid);
                startActivity(i);

            }
        });

        lastin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastin_layout.setBackgroundResource(R.color.grey);
                lastout_layout.setBackgroundResource(R.color.lightgray);
                lastin_main_layout.setVisibility(View.VISIBLE);
                lastout_main_layout.setVisibility(View.GONE);

                if(NetworkConnection.isConnected(Tenant_Records_show_in_tablayout.this))
                {
                    getTenantInRecord();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
                }



            }
            });

        lastout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastout_layout.setBackgroundResource(R.color.grey);
                lastin_layout.setBackgroundResource(R.color.lightgray);
                lastin_main_layout.setVisibility(View.GONE);
                lastout_main_layout.setVisibility(View.VISIBLE);

                if(NetworkConnection.isConnected(Tenant_Records_show_in_tablayout.this))
                {
                    //new UserRecord_out().execute();
                    getTenantOutRecord();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Calendar c = Calendar.getInstance();
        // System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());

        if(NetworkConnection.isConnected(Tenant_Records_show_in_tablayout.this))
        {
            //new UserRecord_in().execute();

            getTenantInRecord();

        }else
        {
            Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }


    public class UserRecord_in extends AsyncTask<String, String, String> {

        String name,result,message;
        private boolean IsError = false;

        private ProgressDialog dialog = new ProgressDialog(Tenant_Records_show_in_tablayout.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();

            String  url = "";

             url = WebUrls.MAIN_URL;

            HttpPost post = new HttpPost(url);
            Log.d("uu 1", "" + post);
            try {
                //tenantarray_list=new ArrayList<TenantRecordmodel>();
                list=new ArrayList();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getattendencetime_tablayout"));
                nameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
                nameValuePairs.add(new BasicNameValuePair("date",formattedDate));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain =new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i <jsonarray.length(); i++)
                    {
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        TenantRecordmodel tmodel=new TenantRecordmodel();
                        if(jsonobj.getString("attendence_status").equalsIgnoreCase("sign in")) {
                            tmodel.setTime(jsonobj.getString("time"));
                            tmodel.setTenant_name(jsonobj.getString("tenant_name"));
                            tmodel.setAttendancestatus(jsonobj.getString("attendence_status"));
                            tmodel.setRoom_no(jsonobj.getString("room_no"));
                            tmodel.setTime_interval(jsonobj.getString("time_interval"));
                            list.add(tmodel);
                        }
                        if (jsonobj.getString("attendence_status").equalsIgnoreCase("Night in")){

                            tmodel.setTime(jsonobj.getString("time"));
                            tmodel.setTenant_name(jsonobj.getString("tenant_name"));
                            tmodel.setAttendancestatus(jsonobj.getString("attendence_status"));
                            tmodel.setRoom_no(jsonobj.getString("room_no"));
                            tmodel.setTime_interval(jsonobj.getString("time_interval"));
                            list.add(tmodel);

                        }


                        //  tmodel.setTenant_photo(jsonobj.getString("tenant_photo"));

                    }

                }
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result.equalsIgnoreCase("Y")) {

                // int lenght=tenantarray_list.size();
                adapter=new Single_Tenant_RecordsAdapter(Tenant_Records_show_in_tablayout.this,list);
                list_in.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //adapter.notifyDataSetChanged();

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(Tenant_Records_show_in_tablayout.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }
        }

    }


    public class UserRecord_out extends AsyncTask<String, String, String> {

        String name,result,message;
        private boolean IsError = false;

        private ProgressDialog dialog = new ProgressDialog(Tenant_Records_show_in_tablayout.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();

           String  url = "";

            url = WebUrls.MAIN_URL;

            HttpPost post = new HttpPost(url);
            Log.d("uu 1", "" + post);
            try {
                //tenantarray_list=new ArrayList<TenantRecordmodel>();
                list_out_array=new ArrayList();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getattendencetime_tablayout"));
                nameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
                nameValuePairs.add(new BasicNameValuePair("date",formattedDate));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain =new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i <jsonarray.length(); i++)
                    {
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        TenantRecordmodel tmodel=new TenantRecordmodel();
                        if(!jsonobj.getString("attendence_status").equalsIgnoreCase("sign in") && !jsonobj.getString("attendence_status").equalsIgnoreCase("Night in") && !jsonobj.getString("attendence_status").equalsIgnoreCase("")) {
                            tmodel.setTime(jsonobj.getString("time"));
                            tmodel.setTenant_name(jsonobj.getString("tenant_name"));
                            tmodel.setAttendancestatus(jsonobj.getString("attendence_status"));
                            tmodel.setRoom_no(jsonobj.getString("room_no"));
                            tmodel.setTime_interval(jsonobj.getString("time_interval"));
                            list_out_array.add(tmodel);
                        }
                        //  tmodel.setTenant_photo(jsonobj.getString("tenant_photo"));

                    }

                }
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result.equalsIgnoreCase("Y")) {

                // int lenght=tenantarray_list.size();
                adapter=new Single_Tenant_RecordsAdapter(Tenant_Records_show_in_tablayout.this,list_out_array);
                list_out.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //   adapter.notifyDataSetChanged();

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(Tenant_Records_show_in_tablayout.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }
        }

    }




    private void getTenantInRecord() {

        final ProgressDialog dialog = new ProgressDialog(Tenant_Records_show_in_tablayout.this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();


        try {
            JSONObject params = new JSONObject();
            params.put("action","getattendencetime_tablayout");
            params.put("property_id",propertyid);
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


    public void getResponseData(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            list=new ArrayList();

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i <jsonarray.length(); i++)
                {
                    JSONObject jsonobj =jsonarray.getJSONObject(i);
                    TenantRecordmodel tmodel=new TenantRecordmodel();
                    if(jsonobj.getString("attendence_status").equalsIgnoreCase("sign in")) {
                        tmodel.setTime(jsonobj.getString("time"));
                        tmodel.setTenant_name(jsonobj.getString("tenant_name"));
                        tmodel.setAttendancestatus(jsonobj.getString("attendence_status"));
                        tmodel.setRoom_no(jsonobj.getString("room_no"));
                        tmodel.setTime_interval(jsonobj.getString("time_interval"));
                        list.add(tmodel);
                    }
                    if (jsonobj.getString("attendence_status").equalsIgnoreCase("Night in")){

                        tmodel.setTime(jsonobj.getString("time"));
                        tmodel.setTenant_name(jsonobj.getString("tenant_name"));
                        tmodel.setAttendancestatus(jsonobj.getString("attendence_status"));
                        tmodel.setRoom_no(jsonobj.getString("room_no"));
                        tmodel.setTime_interval(jsonobj.getString("time_interval"));
                        list.add(tmodel);

                    }
                    if (list.size() > 0) {
                        adapter = new Single_Tenant_RecordsAdapter(Tenant_Records_show_in_tablayout.this, list);
                        list_in.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }

            }else{
                Toast.makeText(Tenant_Records_show_in_tablayout.this, "No Record Found", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getTenantOutRecord() {

        final ProgressDialog dialog = new ProgressDialog(Tenant_Records_show_in_tablayout.this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();


        try {
            JSONObject params = new JSONObject();
            params.put("action","getattendencetime_tablayout");
            params.put("property_id",propertyid);
            params.put("date", formattedDate);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataOut(result);
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


    public void getResponseDataOut(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            list_out_array=new ArrayList();

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i <jsonarray.length(); i++)
                 {
                    JSONObject jsonobj =jsonarray.getJSONObject(i);
                    TenantRecordmodel tmodel=new TenantRecordmodel();
                    if(!jsonobj.getString("attendence_status").equalsIgnoreCase("sign in") && !jsonobj.getString("attendence_status").equalsIgnoreCase("Night in") && !jsonobj.getString("attendence_status").equalsIgnoreCase("")) {
                        tmodel.setTime(jsonobj.getString("time"));
                        tmodel.setTenant_name(jsonobj.getString("tenant_name"));
                        tmodel.setAttendancestatus(jsonobj.getString("attendence_status"));
                        tmodel.setRoom_no(jsonobj.getString("room_no"));
                        tmodel.setTime_interval(jsonobj.getString("time_interval"));
                        list_out_array.add(tmodel);
                    }

                }

                if (list_out_array.size() > 0) {
                    adapter = new Single_Tenant_RecordsAdapter(Tenant_Records_show_in_tablayout.this,list_out_array);
                    list_out.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }


            }else{
                Toast.makeText(Tenant_Records_show_in_tablayout.this, "No Record Found", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent i=new Intent(Tenant_Records_show_in_tablayout.this,Tenant_Details_Activity.class);
                i.putExtra("propertyid", propertyid);
                startActivity(i);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }



}
