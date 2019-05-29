package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter;
import com.krooms.hostel.rental.property.app.adapter.SelectPropertymultiple_Adapter;
import com.krooms.hostel.rental.property.app.adapter.ViewemployeeAdapter;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.OtherownerdetailsModel;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.Propertynamemodel;
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
import java.util.List;

/**
 * Created by admin on 3/28/2017.
 */
public class ViewEmployeeActivity extends Activity {

    ListView otherownerList;
    ImageView loader;
    Animation rotation;
    String owneridvalue, property_idvalue;
    ImageButton main_header_back;
    ArrayList<OtherownerdetailsModel> ownerdetilsarraylist;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewemployeeactivity);
        main_header_back = (ImageButton) findViewById(R.id.main_header_back);
        otherownerList = (ListView) findViewById(R.id.otherownerList);
        loader = (ImageView) findViewById(R.id.loader);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        in = getIntent();
        owneridvalue = in.getStringExtra("owner_id");
        property_idvalue = in.getStringExtra("property_id");
        new MultiplePropertyByOwnerid().execute();
        main_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewEmployeeActivity.this, HostelListActivity.class);
                startActivity(i);
            }
        });

    }

    private class MultiplePropertyByOwnerid extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(ViewEmployeeActivity.this);

        int count;
        String name, result;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            ownerdetilsarraylist = new ArrayList<>();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "otherownergetdetails"));
                nameValuePairs.add(new BasicNameValuePair("property_id", property_idvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equals("Y")) {
                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        OtherownerdetailsModel propertyid = new OtherownerdetailsModel();
                        propertyid.setFullname(jsonobj.getString("fullname"));
                        propertyid.setEmail(jsonobj.getString("email"));
                        propertyid.setContactno(jsonobj.getString("contactno"));
                        propertyid.setOid(jsonobj.getString("oid"));
                        propertyid.setPropertyid(jsonobj.getString("property_id"));
                        propertyid.setRoletype(jsonobj.getString("role_id"));
                        ownerdetilsarraylist.add(propertyid);
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
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                ViewemployeeAdapter adapter_data_month = new ViewemployeeAdapter(ViewEmployeeActivity.this, ownerdetilsarraylist);
                otherownerList.setAdapter(adapter_data_month);
            } else {
                Toast.makeText(ViewEmployeeActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
