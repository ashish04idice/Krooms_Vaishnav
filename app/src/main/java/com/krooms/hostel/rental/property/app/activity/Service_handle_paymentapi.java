package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by anuradhaidice on 23/12/16.
 */

public class Service_handle_paymentapi extends Service {
    String condtition = "false";
    int i;
    Context sContext;
    String property_id_new, room_id_new, ownerid_new, userid_main_json, tenantId;
    String resultmain = null;
    String adavnce;
    String tenantroomamount;
    String result1 = "", suserid;
    //public ProgressDialog dialog;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        property_id_new = intent.getStringExtra("Propertyid");
        room_id_new = intent.getStringExtra("Roomid");
        ownerid_new = intent.getStringExtra("Ownerid");
        userid_main_json = intent.getStringExtra("Userid");
        tenantId = intent.getStringExtra("Tenantid");
        suserid = intent.getStringExtra("Suserid");
        new AdvanceAmountGet().execute();
        return Service.START_NOT_STICKY;
    }


    public class AdvanceAmountGet extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "getadvanceamount"));
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_new));
                nameValuePairs.add(new BasicNameValuePair("room_id", room_id_new));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        adavnce = jsonarray.getJSONObject(i).getString("advance");
                        tenantroomamount = jsonarray.getJSONObject(i).getString("amount");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("Y")) {
                new GetTenantPaymentData().execute();

            } else if (result.equals("N")) {
            }

        }
    }


    private class GetTenantPaymentData extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            //connection timeout
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "ownerendmonthdetails"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", ownerid_new));
                nameValuePairs.add(new BasicNameValuePair("user_id", userid_main_json));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantId));
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_new));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                resultmain = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (resultmain.equalsIgnoreCase("Y")) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultmain;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("Y")) {
                stopSelf();
            } else if (result.equals("N")) {
                String hdatenew = HostelDetailActivity.hiredatevaluenew;
                if (hdatenew.equals("")) {
                    new SubmitpaymentEditForm().execute();
                } else {
                    new SubmitpaymentOnbooking().execute();
                }

            }

        }
    }

    //new api submit payment details
    private class SubmitpaymentOnbooking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            String yearv = "" + year;
            int monthv = cal.get(Calendar.MONTH);
            int datev = cal.get(Calendar.DAY_OF_MONTH);
            String advancemainvalue = adavnce;
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "ownerpaidamount"));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", ownerid_new));
            listNameValuePairs.add(new BasicNameValuePair("user_id", suserid));
            listNameValuePairs.add(new BasicNameValuePair("advance_amount", advancemainvalue));
            listNameValuePairs.add(new BasicNameValuePair("room_amount", tenantroomamount));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", tenantId));
            listNameValuePairs.add(new BasicNameValuePair("month_id", "0"));
            listNameValuePairs.add(new BasicNameValuePair("year", HostelDetailActivity.hiredatevaluenew));
            listNameValuePairs.add(new BasicNameValuePair("month_name", "Advance Payment"));
            listNameValuePairs.add(new BasicNameValuePair("amount", "0"));
            listNameValuePairs.add(new BasicNameValuePair("room_id", room_id_new));
            listNameValuePairs.add(new BasicNameValuePair("property_id", property_id_new));
            listNameValuePairs.add(new BasicNameValuePair("current_date", yearv + "/" + (monthv + 1) + "/" + datev));
            listNameValuePairs.add(new BasicNameValuePair("transaction_mode", "C"));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String msg = null;
            String status = null;

            if (result == null) {
                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    if (status.equalsIgnoreCase("Y")) {
                        stopSelf();
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end


    //new api submit payment on edit tenant form
    //this class used to send warden complaint
    private class SubmitpaymentEditForm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {


            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            String yearv = "" + year;
            int monthv = cal.get(Calendar.MONTH);
            int datev = cal.get(Calendar.DAY_OF_MONTH);
            String advancemainvalue = adavnce;
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "advancepaymentinsert"));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", ownerid_new));
            listNameValuePairs.add(new BasicNameValuePair("main_userid", userid_main_json));
            listNameValuePairs.add(new BasicNameValuePair("user_id", suserid));
            listNameValuePairs.add(new BasicNameValuePair("advance_amount", advancemainvalue));
            listNameValuePairs.add(new BasicNameValuePair("room_amount", tenantroomamount));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", tenantId));
            listNameValuePairs.add(new BasicNameValuePair("month_id", "0"));
            listNameValuePairs.add(new BasicNameValuePair("year", yearv + "/" + monthv + "/" + datev));
            listNameValuePairs.add(new BasicNameValuePair("month_name", "Advance Payment"));
            listNameValuePairs.add(new BasicNameValuePair("amount", "0"));
            listNameValuePairs.add(new BasicNameValuePair("room_id", room_id_new));
            listNameValuePairs.add(new BasicNameValuePair("property_id", property_id_new));
            listNameValuePairs.add(new BasicNameValuePair("current_date", yearv + "/" + (monthv + 1) + "/" + datev));
            listNameValuePairs.add(new BasicNameValuePair("transaction_mode", "C"));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String msg = null;
            String status = null;

            if (result == null) {
                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    if (status.equalsIgnoreCase("Y")) {
                        stopSelf();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end
    //end


}
