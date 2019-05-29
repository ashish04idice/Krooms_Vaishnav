package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Custom_GridAdapter;
import com.krooms.hostel.rental.property.app.adapter.NavDrawerListAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.NavDrawerItem;
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
 * Created by ashish on 3/20/2017.
 */
public class Home_Accountantactivity extends FragmentActivity {

    private ImageButton imgButtonMenu = null;
    private DrawerLayout mDrawerLayout;
    public static NavDrawerListAdapter adapter;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private SharedStorage mSharedStorage;
    String userId_value;
    private ListView mDrawerList;
    public static FragmentActivity fActivity;
    GridView gridView;
    String ownerid;
    Context mcontext;
    private Common mCommon = null;
    Validation mValidation;
    public static String statusvaluepaid = "false";
    public static String statusvaluepaidlogic = "false";
    public static String statusforpayment;
    public static String paidarray[] = {};

    String[] web = {
            "View Property",
            "View Tenant",
            "Payment",
            "Payment Status",
            "Other Charges",
            "Support",
            "About Us",
            "Logout"
    };
    int[] imageId = {
            R.drawable.property_icon,
            R.drawable.tenant_icon,
            R.drawable.payment_icon,
            R.drawable.payment_icon,
            R.drawable.payment_icon,
            R.drawable.support_icon,
            R.drawable.about_icon,
            R.drawable.logout_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_mainuser_layout);
        mcontext = this;
        this.fActivity = this;
        mCommon = new Common();
        mValidation = new Validation(mcontext);
        imgButtonMenu = (ImageButton) findViewById(R.id.main_header_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        gridView = (GridView) findViewById(R.id.homegrid);
        mSharedStorage = SharedStorage.getInstance(this);
        userId_value = mSharedStorage.getUserId();
        String usertype = mSharedStorage.getUserType();
        String propertyidlastvalue = mSharedStorage.getUserPropertyId();
        String addcountvalue = mSharedStorage.getAddCount();
        ownerid = mSharedStorage.getPropertyOwnerId();
        mSharedStorage.setAddCount(addcountvalue);
        getHostelname();
        new PaidUnpaidLogicJson(ownerid, propertyidlastvalue).execute();

        Custom_GridAdapter gridAdapter = new Custom_GridAdapter(mcontext, web, imageId);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        Intent it = new Intent(Home_Accountantactivity.this, HostelDetailActivity.class);
                        it.putExtra("property_id", mSharedStorage.getUserPropertyId());
                        startActivity(it);
                        break;

                    case 1:
                        Intent mIntent1 = new Intent(Home_Accountantactivity.this, MyPropertyUsersListActivity.class);
                        startActivity(mIntent1);
                        break;
                    case 2:


                        for (int j = 0; j < paidarray.length; j++) {
                            String paidvalue = paidarray[j];
                            if (paidvalue.equalsIgnoreCase("Payment")) {
                                statusvaluepaid = "true";
                                break;
                            } else {
                                statusvaluepaid = "false";
                            }
                        }

                        if (statusvaluepaid.equals("true")) {
                            Intent mIntent2 = new Intent(Home_Accountantactivity.this, OwnerFromActivity.class);
                            mIntent2.putExtra("flag", "");
                            startActivity(mIntent2);
                        } else {
                            statusvaluepaid = "false";

                            mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);

                        }
                        break;

                    case 3:
                        //Payment_Paid_Unpaid_Activity.class
                        Intent mIntent = new Intent(Home_Accountantactivity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                        mIntent.putExtra("Propertyid", mSharedStorage.getUserPropertyId());
                        startActivity(mIntent);
                        break;


                    case 4:

                        for (int j = 0; j < paidarray.length; j++) {
                            String paidvalue = paidarray[j];
                            if (paidvalue.equalsIgnoreCase("Other Charges")) {
                                statusvaluepaid = "true";
                                break;
                            } else {
                                statusvaluepaid = "false";
                            }
                        }

                        if (statusvaluepaid.equals("true")) {
                            Intent mIntent33 = new Intent(Home_Accountantactivity.this, Owner_Fine_Activity.class);
                            mIntent33.putExtra("Ownerid", ownerid);
                            mIntent33.putExtra("Propertyid", mSharedStorage.getUserPropertyId());
                            startActivity(mIntent33);
                        } else {
                            statusvaluepaid = "false";

                            mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                        }
                        break;

                    case 5:
                        Intent mIntent3 = new Intent(Home_Accountantactivity.this, SupportActivity.class);
                        startActivity(mIntent3);
                        break;

                    case 6:
                        Intent mIntent5 = new Intent(Home_Accountantactivity.this, AboutUsActivity.class);
                        startActivity(mIntent5);
                        break;

                    case 7:
                        mSharedStorage.clearUserData();
                        Intent mIntent6 = new Intent(Home_Accountantactivity.this, LandingActivityWithoutLogin.class);
                        startActivity(mIntent6);
                        Home_Accountantactivity.this.finish();
                        break;
                }

            }
        });


        if (!mSharedStorage.getLoginStatus()) {
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_geust);
            navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_geust);
        } else {
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_accountant);
            navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons_accountant);

        }

        navDrawerItems = new ArrayList<NavDrawerItem>();
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));

        }

        navMenuIcons.recycle();
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        imgButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                    mDrawerList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                    mDrawerList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mDrawerLayout.openDrawer(mDrawerList);
                }
            }

        });

        setdraweritemclick();
    }

    private void setdraweritemclick() {


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (navMenuTitles[position].equals("Home")) {

                    if (mValidation.checkNetworkRechability()) {

                        Intent mIntent = new Intent(Home_Accountantactivity.this, Home_Accountantactivity.class);
                        startActivity(mIntent);

                    } else {
                        mCommon.displayAlert((Activity) mcontext, getResources().getString(R.string.str_no_network), false);
                    }


                } else if (navMenuTitles[position].equals("View Tenants")) {

                    if (mValidation.checkNetworkRechability()) {

                        Intent mIntent = new Intent(Home_Accountantactivity.this, MyPropertyUsersListActivity.class);
                        startActivity(mIntent);

                    } else {
                        mCommon.displayAlert((Activity) mcontext, getResources().getString(R.string.str_no_network), false);
                    }

                } else if (navMenuTitles[position].equals("View Property")) {


                    if (mValidation.checkNetworkRechability()) {

                        Intent it = new Intent(Home_Accountantactivity.this, HostelDetailActivity.class);
                        it.putExtra("property_id", mSharedStorage.getUserPropertyId());
                        startActivity(it);
                    } else {
                        mCommon.displayAlert((Activity) mcontext, getResources().getString(R.string.str_no_network), false);
                    }

                } else if (navMenuTitles[position].equals("Support")) {
                    Intent mIntent = new Intent(Home_Accountantactivity.this, SupportActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("About Us")) {
                    Intent mIntent = new Intent(Home_Accountantactivity.this, AboutUsActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Payment")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Payment")) {
                            statusvaluepaid = "true";
                            statusforpayment = "true";
                            break;
                        } else {
                            statusvaluepaid = "false";
                            statusforpayment = "false";
                        }
                    }

                    if (statusvaluepaid.equals("true")) {
                        Intent mIntent = new Intent(Home_Accountantactivity.this, OwnerFromActivity.class);
                        mIntent.putExtra("flag", "");
                        startActivity(mIntent);
                    } else {
                        statusvaluepaid = "false";
                        statusforpayment = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }

                } else if (navMenuTitles[position].equals("Other Charges")) {
                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Other Charges")) {
                            statusvaluepaid = "true";
                            break;
                        } else {
                            statusvaluepaid = "false";
                        }

                    }

                    if (statusvaluepaid.equals("true")) {
                        Intent mIntent33 = new Intent(Home_Accountantactivity.this, Owner_Fine_Activity.class);
                        mIntent33.putExtra("Ownerid", ownerid);
                        mIntent33.putExtra("Propertyid", mSharedStorage.getUserPropertyId());
                        startActivity(mIntent33);
                    } else {
                        statusvaluepaid = "false";

                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }
                } else if (navMenuTitles[position].equals("Payment Status")) {

                    Intent mIntent = new Intent(Home_Accountantactivity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                    mIntent.putExtra("Propertyid", mSharedStorage.getUserPropertyId());
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Logout")) {
                    mSharedStorage.clearUserData();
                    Intent mIntent = new Intent(Home_Accountantactivity.this, LandingActivityWithoutLogin.class);
                    startActivity(mIntent);
                    Home_Accountantactivity.this.finish();
                }


            }
        });

    }

    private class PaidUnpaidLogicJson extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String ownerid, propertyidlastvalue;

        public PaidUnpaidLogicJson(String ownerid, String propertyidlastvalue) {
            this.ownerid = ownerid;
            this.propertyidlastvalue = propertyidlastvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(Home_Accountantactivity.this);
            String userId_value = mSharedStorage.getUserId();
            String owneridvalue = ownerid;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getpaidservice"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyidlastvalue));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String paid_service = jsonObject.getString("paid_service");
                        paidarray = paid_service.split(",");
                        for (int j = 0; j < paidarray.length; j++) {
                            String paidvalue = paidarray[j];
                        }
                    }
                }


            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("Y")) {

            } else if (result.equals("N")) {
            }

        }

    }

    private void getHostelname() {

        try {

            String propertyid = mSharedStorage.getUserPropertyId();
            JSONObject params = new JSONObject();
            params.put("action", "gethostelname");
            params.put("property_id", propertyid);

            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // dialog.dismiss();
                    String result = response.toString();
                    getResponseDatahostel(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //dialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDatahostel(String result) {
        try {
            String status, hostelname = "";
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            //hostelname = jsmain.getString("records");

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    hostelname = jsonobj.getString("property_name");
                }

                mSharedStorage.setPropertyName(hostelname);

            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
