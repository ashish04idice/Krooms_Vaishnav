package com.krooms.hostel.rental.property.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Single_Tenant_RecordsAdapter;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.modal.TenantRecordmodel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by ashish on 25/4/17.
 */

public class LastOut extends Fragment {

    ListView listView;
    Single_Tenant_RecordsAdapter adapter;
    Context context;
    ArrayList<TenantRecordmodel> list = new ArrayList();
    RelativeLayout flback_button;

    TextView lastout, lastin;
    String formattedDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_out_fragment, container, false);

        context = getActivity();
        listView = (ListView) view.findViewById(R.id.list);
        // flback_button=(RelativeLayout)view.findViewById(R.id.flback_button);

        Calendar c = Calendar.getInstance();
        // System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        View header = getActivity().getLayoutInflater().inflate(R.layout.display_top, null);
        listView.addHeaderView(header);
        lastout = (TextView) header.findViewById(R.id.add1);
        lastin = (TextView) header.findViewById(R.id.add);
        lastout.setVisibility(View.VISIBLE);
        lastin.setVisibility(View.GONE);


        new UserRecord().execute();

        return view;
    }

    public class UserRecord extends AsyncTask<String, String, String> {

        String name, result, message;
        private boolean IsError = false;

        private ProgressDialog dialog = new ProgressDialog(getActivity());


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
                //tenantarray_list=new ArrayList<TenantRecordmodel>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getattendencetime_tablayout"));
                nameValuePairs.add(new BasicNameValuePair("property_id", "1"));
                nameValuePairs.add(new BasicNameValuePair("date", formattedDate));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        TenantRecordmodel tmodel = new TenantRecordmodel();
                        if (!jsonobj.getString("attendence_status").equalsIgnoreCase("sign in")) {
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
                adapter = new Single_Tenant_RecordsAdapter(context, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //   adapter.notifyDataSetChanged();

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(getActivity(), "No Tenant Found", Toast.LENGTH_LONG).show();
            }
        }

    }

}
