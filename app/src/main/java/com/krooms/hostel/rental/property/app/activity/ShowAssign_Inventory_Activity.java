package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.ShowAssignInventory_Adapter;
import com.krooms.hostel.rental.property.app.adapter.StudentName_Adapter;
import com.krooms.hostel.rental.property.app.adapter.Warden_Complaint_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Complaindetailowner;
import com.krooms.hostel.rental.property.app.modal.Complaindetailownernew;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Owner_Complaintcountmodel;
import com.krooms.hostel.rental.property.app.modal.ShowInventory_item_model;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 4/20/2017.
 */
public class ShowAssign_Inventory_Activity extends Activity {

    Intent in;
    ArrayList<ShowInventory_item_model> showinventory_arraylist;
    ShowInventory_item_model showinventory_model;
    String tenantidvalue,propertyid;
    ListView showinventoerylist;
    RelativeLayout lback_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinventory_activity_layout);
        in=getIntent();
        tenantidvalue=in.getStringExtra("tenantid");
        propertyid=in.getStringExtra("propertyidseprate");

        showinventory_arraylist=new ArrayList<>();
        lback_button=(RelativeLayout)findViewById(R.id.lback_button);
        lback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ShowAssign_Inventory_Activity.this,HostelListActivity.class);
                startActivity(i);
            }
        });

        showinventoerylist=(ListView)findViewById(R.id.showinventoerylist);
        new getAssignitemlist().execute();

    }

    public class getAssignitemlist extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(ShowAssign_Inventory_Activity.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action","showassignitemlist"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id",tenantidvalue));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            //listNameValuePairs.clear();
            //Log.e("result is ----", result);
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
                        JSONArray jsonArray = obj.getJSONArray("record");
                       // JSONArray jsonArraycount = obj.getJSONArray("record");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobj =jsonArray.getJSONObject(i);
                            String artical_name_value=jsonobj.getString("artical_name");
                            String mainarticalcode=jsonobj.getString("artical_code");
                            showinventory_model = new ShowInventory_item_model();
                            showinventory_model.setItem_name(artical_name_value);
                            showinventory_model.setItem_code(mainarticalcode);
                            showinventory_model.setItem_id(jsonobj.getString("artical_id"));
                            showinventory_model.setAssign_date(jsonobj.getString("assign_date"));
                            showinventory_arraylist.add(showinventory_model);

                        }

                        if (showinventory_arraylist.size() > 0) {

                            ShowAssignInventory_Adapter adapter_data_month = new ShowAssignInventory_Adapter(ShowAssign_Inventory_Activity.this,showinventory_arraylist,tenantidvalue,propertyid);
                            showinventoerylist.setAdapter(adapter_data_month);
                        }
                     else if (result.equalsIgnoreCase("N")) {
                        Toast.makeText(ShowAssign_Inventory_Activity.this, "Iteam Is Not Assign", Toast.LENGTH_LONG).show();
                        }

                        //   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    //Log.e("Exp=",""+e);
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                    startActivity(new Intent(ShowAssign_Inventory_Activity.this,HostelListActivity.class));

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //getAssign item

 /*   private class getAssignitemlist extends AsyncTask<String, String, String> {

        String name,result,message;
        private boolean IsError = false;
        private ProgressDialog dialog = new ProgressDialog(ShowAssign_Inventory_Activity.this);


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
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                showinventory_arraylist=new ArrayList<ShowInventory_item_model>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action","showassignitemlist"));
                nameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",tenantidvalue));
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

                        String artical_name_value=jsonobj.getString("artical_name");
                        String mainarticalcode=jsonobj.getString("artical_code");
                        showinventory_model = new ShowInventory_item_model();
                        showinventory_model.setItem_name(artical_name_value);
                        showinventory_model.setItem_code(mainarticalcode);
                        showinventory_model.setItem_id(jsonobj.getString("artical_id"));
                        showinventory_model.setAssign_date(jsonobj.getString("assign_date"));
                        showinventory_arraylist.add(showinventory_model);


                    }
                }
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            dialog.dismiss();
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                    if (showinventory_arraylist.size() > 0) {

                        ShowAssignInventory_Adapter adapter_data_month = new ShowAssignInventory_Adapter(ShowAssign_Inventory_Activity.this,showinventory_arraylist,tenantidvalue,propertyid);
                        showinventoerylist.setAdapter(adapter_data_month);
                    }
                } else if (result.equalsIgnoreCase("N")) {
                    Toast.makeText(ShowAssign_Inventory_Activity.this, "Iteam Is Not Assign", Toast.LENGTH_LONG).show();
                }
            }
            else {
            }
        }
    }
*/
    //........

}
