package com.krooms.hostel.rental.property.app.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.NavDrawerListAdapter;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.asynctask.DownloadRCUFromServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.GetTenantInformationServiceAsyncTask;
import com.krooms.hostel.rental.property.app.callback.AreaDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.CityDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.callback.SpinnerResponce;
import com.krooms.hostel.rental.property.app.ccavenue_adapter.ObjectAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.FileDownloader;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.database.BackgroungSyncing;
import com.krooms.hostel.rental.property.app.database.GetCityAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.SearchFilterDialog;
import com.krooms.hostel.rental.property.app.dialog.SearchPropertyDialog;
import com.krooms.hostel.rental.property.app.fragments.PropertyListFragment;
import com.krooms.hostel.rental.property.app.fragments.PropertyListMapFragment;
import com.krooms.hostel.rental.property.app.fragments.SimpleTenantPropertyListFragment;
import com.krooms.hostel.rental.property.app.modal.NavDrawerItem;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HostelListActivity extends FragmentActivity implements View.OnClickListener,
        CityDataBaseResponce, AreaDataBaseResponce, ServiceResponce {

    private Common mCommon;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public static FragmentActivity fActivity;
    private ImageButton searchBtn = null, imgButtonMenu = null;
    private int noOfTenant = 0;
    public static LinearLayout bottomLayout_panal = null;
    public static int Count;
    public static int tenantcount;
    public static int feedbackownercount;
    public static int feedbacktenantcount;
    public static String Parentcomplaintcount;
    public static String Parentfeedbackcount;
    public static String Countss;
    public static String tenantcountss;
    public static String feedbackownercountss;
    public static String feedbacktenantcountss;
    public static String complaintwardencountss;
    public static String feedbackwardencountss;
    public static String nightattendancecount;
    private RelativeLayout filterBtnTab, sortBtnTab, mapBtnTab;
    private TextView filterText, sortText, mapText;
    private BackgroungSyncing mBackgroungSyncing;
    private Spinner spinnerCity;
    public static String statusvaluepaid = "false", statusvaluepaidvideo = "false";
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = new ArrayList<>();
    private StateSpinnerAdapter mCitySpinnerAdapter;
    private String city, cityId = "0", area, areaId = "0";
    public static NavDrawerListAdapter adapter;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private SharedStorage mSharedStorage;
    private AutoCompleteTextView myAutoComplete;
    private ObjectAdapter mAdapter = null;
    public String HTenatId = null, HPropertyId = null;
    public static String ownerid_new, propertyidvalue_new;
    public static int valuecount;
    public static int ownerreplycount;
    String OwnerId;
    Context mcContext;
    public static String paidarray[] = {};
    String propertyidmainvalue;
    String userId_value;
    String parent_id = null;
    String pid;
    String owneridforpaid;
    public static ArrayList<String> datapaidlist;
    Calendar calnder;
    String formattedDate = "";
    public static RelativeLayout searchlayout;
    boolean parentpropertyhide = false;
    RelativeLayout laynotification, searchArea, searchlayoutnotification;
    TextView txtnotification;
    LinearLayout layareaserch;
    ImageButton btnnotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_list);
        this.fActivity = this;
        mcContext = this;
        mSharedStorage = SharedStorage.getInstance(this);
        String usertype = mSharedStorage.getUserType();
        if (usertype.equals("4")) {
            new GetParentshareddataid().execute();
            createViews();
            if (savedInstanceState == null) {
                displayView(7);
            }
        } else {

            createViews();
            if (savedInstanceState == null) {
                displayView(7);
            }
        }
    }

    public void createViews() {

        bottomLayout_panal = (LinearLayout) findViewById(R.id.bottomLayout_panal);
        filterBtnTab = (RelativeLayout) findViewById(R.id.filterBtnTab);
        sortBtnTab = (RelativeLayout) findViewById(R.id.sortBtnTab);
        mapBtnTab = (RelativeLayout) findViewById(R.id.mapBtnTab);
        filterText = (TextView) findViewById(R.id.filterText);
        sortText = (TextView) findViewById(R.id.sortText);
        mapText = (TextView) findViewById(R.id.mapText);
        searchlayout = (RelativeLayout) findViewById(R.id.searchlayout);
        myAutoComplete = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
        //new chnage
        laynotification = (RelativeLayout) findViewById(R.id.relativenotification);
        searchlayoutnotification = (RelativeLayout) findViewById(R.id.searchlayoutnotification);
        txtnotification = (TextView) findViewById(R.id.txtnotification_count);
        btnnotification = (ImageButton) findViewById(R.id.notificationbtn);
        searchArea = (RelativeLayout) findViewById(R.id.searchArea);
        layareaserch = (LinearLayout) findViewById(R.id.layareaserch);
        //new chnages
        mCommon = new Common();
        fActivity = this;
        mSharedStorage = SharedStorage.getInstance(this);
        userId_value = mSharedStorage.getUserId();
        String usertype = mSharedStorage.getUserType();
        String propertymainvalue = mSharedStorage.getBookedPropertyId();
        String propertyidlastvalue = mSharedStorage.getUserPropertyId();
        String addcountvalue = mSharedStorage.getAddCount();
        mSharedStorage.setAddCount(addcountvalue);
        // on these place we call api
        if (usertype.equals("2")) {
            new PropertyidtenantJson().execute();
            getHostelnameTenant();
            try {
                String ppid="";
                ppid = Utility.getSharedPreferences(mcContext, "HideSearch");
                if (!ppid.equalsIgnoreCase("") && !ppid.equals(null) && !ppid.equals("null")) {
                    searchlayout.setVisibility(View.GONE);
                    bottomLayout_panal.setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (usertype.equals("4")) {
            getHostelnameTenant();
            try {
                String ppid="";
                ppid = Utility.getSharedPreferences(mcContext, "PARENTID");
                if (!ppid.equalsIgnoreCase("") && !ppid.equals(null) && !ppid.equals("null") ) {
                    searchlayout.setVisibility(View.GONE);
                    bottomLayout_panal.setVisibility(View.GONE);
                }
            }catch (Exception ee){
                ee.printStackTrace();
            }

        }
        //warden unread msg count
        else if (usertype.equals("6")) {
            new GetWardenComplaintCountNew().execute();
            new GetWardenFeedbackCountNew().execute();
            Date mToday = new Date();
            Date userDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);//hh:mm:ss aa
            String curTime = sdf.format(mToday);
            Date start = null, end = null, start1 = null, end1 = null, start2 = null, end2 = null, start3 = null, end3 = null, start4 = null, end4 = null, start5 = null, end5 = null;

            try {
                start = sdf.parse("20:15:00");//8:30:00 PM
                end = sdf.parse("23:59:00");//11:59:00 PM
                userDate = sdf.parse(curTime);
                if (userDate.after(start) && userDate.before(end)) {
                    getNightRecord();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //PropertyOwnerId
            getHostelname();
            String ownerid = mSharedStorage.getPropertyOwnerId();
            new PaidUnpaidLogicJson(ownerid).execute();

        } else {

            if (NetworkConnection.isConnected(HostelListActivity.this)) {
                new Propertyid_Jsonowner().execute();
                //getNightRecord();
                Date mToday = new Date();
                Date userDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);//hh:mm:ss aa
                String curTime = sdf.format(mToday);
                Date start = null, end = null, start1 = null, end1 = null, start2 = null, end2 = null, start3 = null, end3 = null, start4 = null, end4 = null, start5 = null, end5 = null;

                try {
                    start = sdf.parse("20:15:00");//8:30:00 PM
                    end = sdf.parse("23:59:00");//11:59:00 PM
                    userDate = sdf.parse(curTime);
                    if (userDate.after(start) && userDate.before(end)) {
                        getNightRecord();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getHostelname();

            } else {
                Toast.makeText(HostelListActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
            }
        }
        HTenatId = mSharedStorage.getUserId();
        //  new GetownerreplyCount().execute();
        new GetOwnerComplaintCountNew().execute();
        new GetOwnerFeedbackCountNew().execute();
        HPropertyId = mSharedStorage.getBookedPropertyId();
        myAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 666) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(myAutoComplete.getWindowToken(), 0);
                }
                return false;
            }
        });

        imgButtonMenu = (ImageButton) findViewById(R.id.main_header_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        if (!mSharedStorage.getLoginStatus()) {
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_geust);
            navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_geust);
        } else {

            if (mSharedStorage.getUserType().equals("2")) {
                navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_hosteller);
                navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_hosteller);


            } else if (mSharedStorage.getUserType().equals("4")) {
                Utility.setSharedPreference(mcContext, "Utype", "4");
                navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_parent);
                navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_property_parent);
            } else if (mSharedStorage.getUserType().equals("3")) {
                if (mSharedStorage.getAddCount().equals("0")) {
                    navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_owner);
                    navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_property_owner);
                } else {
                    navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_owner1);
                    navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_property_owner1);
                }
            }
            // 6 for warden
            else if (mSharedStorage.getUserType().equals("6")) {
                navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_warden);
                navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons1_property_warden);
            }
        }

        spinnerCity = (Spinner) findViewById(R.id.cityInput);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
        navMenuIcons.recycle();
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //chnages on 06/12/2018 for notification
        if (usertype.equalsIgnoreCase("4")) {
            searchlayout.setVisibility(View.GONE);
            searchlayoutnotification.setVisibility(View.VISIBLE);
            final String parentid = mSharedStorage.getParent_Id();
            final String propertyid = mSharedStorage.getBookedPropertyId();
            btnnotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HostelListActivity.this, ShowAttendance_Notification.class);
                    intent.putExtra("parentid", parentid);
                    intent.putExtra("propertyid", propertyid);
                    startActivity(intent);
                }
            });
        }

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(HostelListActivity.this, SearchedPropertyListActivity.class);
                it.putExtra("from", "normalSearch");
                it.putExtra("city", "" + cityId);
                it.putExtra("area", "" + areaId);
                startActivity(it);

            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (navMenuTitles[position].equals("Login")) {
                    mIntent = new Intent(HostelListActivity.this, LoginActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Home")) {

                    if (mSharedStorage.getUserType().equals("6") || mSharedStorage.getUserType().equals("3")) {
                        if (NetworkConnection.isConnected(HostelListActivity.this)) {
                            getNightRecord();
                            displayView(7);
                        } else {
                            displayView(7);
                            Toast.makeText(HostelListActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        displayView(7);
                    }
                } else if (navMenuTitles[position].equals("Add Property")) {
                    if (!mSharedStorage.getLoginStatus()) {
                        CustomDialogClass VehicleRcId = new CustomDialogClass(HostelListActivity.this, R.style.full_screen_dialog);
                        VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        VehicleRcId.getPerameter(HostelListActivity.this, "Please Login First",
                                "To use this feature you have to login first.Do you want to login ?");
                        VehicleRcId.show();

                    } else {
                        mIntent = new Intent(HostelListActivity.this, PropertyInfoFirstActivity.class);
                        mIntent.putExtra("flag", "Add");
                        mIntent.putExtra("property_id", "");
                        startActivity(mIntent);
                    }

                } else if (navMenuTitles[position].equals("Book Property")) {

                    if (!mSharedStorage.getLoginStatus()) {
                        CustomDialogClass VehicleRcId = new CustomDialogClass(HostelListActivity.this, R.style.full_screen_dialog);
                        VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        VehicleRcId.getPerameter(HostelListActivity.this, "Please Login First", "To use this feature you have to login first.Do you want to lognin ?");
                        VehicleRcId.show();

                    } else {
                        SearchPropertyDialog dialog = new SearchPropertyDialog() {
                            @Override
                            public void goBtnEvent(String cityId1, String areaId1) {
                                Intent it = new Intent(HostelListActivity.this, SearchedPropertyListActivity.class);
                                it.putExtra("from", "normalSearch");
                                it.putExtra("city", cityId1);
                                it.putExtra("area", areaId1);
                                startActivity(it);
                            }
                        };
                        dialog.getPerameter(HostelListActivity.this);
                        dialog.show(getSupportFragmentManager(), "");
                    }

                } else if (navMenuTitles[position].equals("Booked Property")) {
                    Intent it = new Intent(HostelListActivity.this, HostelDetailActivity.class);
                    it.putExtra("property_id", mSharedStorage.getBookedPropertyId());
                    startActivity(it);
                    // new  GetTenantBookedStatus().execute();
                } else if (navMenuTitles[position].equals("Owner Profile")) {
                    mIntent = new Intent(HostelListActivity.this, OwnerProfileActivity.class);
                    mIntent.putExtra("flag", "");
                    startActivity(mIntent);
                }//Night Attendance Status
                else if (navMenuTitles[position].equals("Night Attendance Status")) {
                    String propertyid = mSharedStorage.getUserPropertyId();
                    mIntent = new Intent(HostelListActivity.this, Night_Attendance_AbsentList_Activity.class);
                    mIntent.putExtra("flag", "");
                    mIntent.putExtra("propertyid", propertyid);
                    startActivity(mIntent);
                }
                //for parent module item
                else if (navMenuTitles[position].equals("Parent Profile")) {

                    mIntent = new Intent(HostelListActivity.this, ParentProfileCurrentWorkingActivity.class);
                    mIntent.putExtra("flag", "");
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Add Child")) {

                    new GetParentIdJson().execute();

                } else if (navMenuTitles[position].equals("View Child")) {

                    new GetParentIdJsonViewChild().execute();

                } else if (navMenuTitles[position].equals("Payment")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Payment")) {
                            statusvaluepaid = "true";
                            Utility.setSharedPreference(HostelListActivity.this, "Payment", "1");
                            break;
                        } else {
                            statusvaluepaid = "false";
                            Utility.setSharedPreference(HostelListActivity.this, "Payment", "0");
                        }
                    }

                    if (statusvaluepaid.equals("true")) {
                        mIntent = new Intent(HostelListActivity.this, OwnerFromActivity.class);
                        mIntent.putExtra("flag", "");
                        startActivity(mIntent);
                    } else {
                        statusvaluepaid = "false";
                        Utility.setSharedPreference(HostelListActivity.this, "Payment", "0");
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }
                    String sts = Utility.getSharedPreferences(HostelListActivity.this, "Payment");
                    //Attendance Report
                } else if (navMenuTitles[position].equals("Other Charges")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Other Charges")) {
                            statusvaluepaid = "true";
                            Utility.setSharedPreference(HostelListActivity.this, "Other", "1");
                            break;
                        } else {
                            statusvaluepaid = "false";
                            Utility.setSharedPreference(HostelListActivity.this, "Other", "0");
                        }
                    }

                    if (statusvaluepaid.equals("true")) {
                        if (mSharedStorage.getUserType().equals("6")) {
                            propertyidvalue_new = mSharedStorage.getUserPropertyId();
                            String wardenownerid = mSharedStorage.getPropertyOwnerId();
                            mIntent = new Intent(HostelListActivity.this, Owner_Fine_Activity.class);
                            mIntent.putExtra("Ownerid", wardenownerid);
                            mIntent.putExtra("Propertyid", propertyidvalue_new);
                            startActivity(mIntent);

                        } else {
                            new GetOwnerPropertyIdFinevalue().execute();
                        }
                    } else {
                        statusvaluepaid = "false";

                        Utility.setSharedPreference(HostelListActivity.this, "Other", "0");

                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }

                    //fine generate
                } else if (navMenuTitles[position].equals("Payment Status")) {

                    propertyidvalue_new = mSharedStorage.getUserPropertyId();
                    mIntent = new Intent(HostelListActivity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                    mIntent.putExtra("Propertyid", propertyidvalue_new);
                    startActivity(mIntent);

                } else if (navMenuTitles[position].equals("Electricity")) {

                    if (mSharedStorage.getUserType().equals("6")) {
                        propertyidvalue_new = mSharedStorage.getUserPropertyId();
                        mIntent = new Intent(HostelListActivity.this, Electricity_Activity.class);
                        mIntent.putExtra("Propertyid", propertyidvalue_new);
                        startActivity(mIntent);
                    } else {
                        propertyidvalue_new = mSharedStorage.getUserPropertyId();
                        mIntent = new Intent(HostelListActivity.this, Electricity_Activity.class);
                        mIntent.putExtra("Propertyid", propertyidvalue_new);
                        startActivity(mIntent);
                    }

                } else if (navMenuTitles[position].equals("Attendance Report")) {
                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("attendance")) {
                            statusvaluepaid = "true";
                            break;
                        } else {
                            statusvaluepaid = "false";
                        }
                    }
                    if (statusvaluepaid.equals("true")) {
                        if (mSharedStorage.getUserType().equals("6")) {
                            propertyidvalue_new = mSharedStorage.getUserPropertyId();
                            String wardenownerid = mSharedStorage.getPropertyOwnerId();
                            mIntent = new Intent(HostelListActivity.this, Dateselection_AttendenceActivity_Owner.class);
                            mIntent.putExtra("ownerid", wardenownerid);
                            mIntent.putExtra("property_id_data", propertyidvalue_new);
                            startActivity(mIntent);
                        } else {
                            new Propertyid_Json().execute();
                        }
                    } else {
                        statusvaluepaid = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }
                } else if (navMenuTitles[position].equals("Tenant Info")) {
                    String useridmainvalue = mSharedStorage.getUserId();
                    String propertyid = mSharedStorage.getBookedPropertyId();
                    mIntent = new Intent(HostelListActivity.this, CoTenantListActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Support")) {
                    mIntent = new Intent(HostelListActivity.this, SupportActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("About Us")) {
                    mIntent = new Intent(HostelListActivity.this, AboutUsActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Inventory")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("inventory")) {
                            statusvaluepaid = "true";
                            Utility.setSharedPreference(HostelListActivity.this, "Inventory", "1");
                            break;
                        } else {
                            statusvaluepaid = "false";
                            Utility.setSharedPreference(HostelListActivity.this, "Inventory", "0");
                        }
                    }

                    if (statusvaluepaid.equals("true")) {

                        if (mSharedStorage.getUserType().equals("6")) {
                            propertyidvalue_new = mSharedStorage.getUserPropertyId();
                            String wardenownerid = mSharedStorage.getPropertyOwnerId();
                            mIntent = new Intent(HostelListActivity.this, Enventory_Activity_Owner.class);
                            mIntent.putExtra("Ownerid", wardenownerid);
                            mIntent.putExtra("Propertyid", propertyidvalue_new);
                            startActivity(mIntent);

                        } else {
                            new GetownerincventoryPropertyId().execute();
                        }
                    } else {
                        statusvaluepaid = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);

                    }

                } else if (navMenuTitles[position].equals("Manual Attendance")) {

                    String propertyid = mSharedStorage.getUserPropertyId();
                    mIntent = new Intent(HostelListActivity.this, GetAttendenceActivity.class);
                    mIntent.putExtra("propertyid", propertyid);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Attendance")) {
                    //Live Video
                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("attendance")) {
                            statusvaluepaid = "true";
                            Utility.setSharedPreference(HostelListActivity.this, "Attendance", "1");
                            break;
                        } else {
                            statusvaluepaid = "false";
                            Utility.setSharedPreference(HostelListActivity.this, "Attendance", "0");
                        }
                    }
                    if (statusvaluepaid.equals("true")) {
                        String propertyid = mSharedStorage.getUserPropertyId();
                        mIntent = new Intent(HostelListActivity.this, Attandance_Device_Selection_Activity.class);
                        mIntent.putExtra("propertyid", propertyid);
                        startActivity(mIntent);
                    } else {
                        statusvaluepaid = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }

                }//get attendance
                else if (navMenuTitles[position].equals("Get Attendance")) {
                    String propertyid = mSharedStorage.getUserPropertyId();
                    mIntent = new Intent(HostelListActivity.this, Attandance_Scanner_Selection_Activity.class);
                    mIntent.putExtra("propertyid", propertyid);
                    startActivity(mIntent);
                }//Add Terms and Condition
                else if (navMenuTitles[position].equals("Terms and Condition")) {

                    String propertyid = mSharedStorage.getUserPropertyId();
                    mIntent = new Intent(HostelListActivity.this, Owner_TermsandCondition_Activity.class);
                    mIntent.putExtra("Propertyid", propertyid);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("Live Video")) {
                    mIntent = new Intent(HostelListActivity.this, LiveVideoShowActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("View Tenants")) {
                    /*displayView(5);*/
                    mIntent = new Intent(HostelListActivity.this, MyPropertyUsersListActivity.class);
                    startActivity(mIntent);
                } else if (navMenuTitles[position].equals("View Employee")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("add employee")) {
                            statusvaluepaid = "true";
                            Utility.setSharedPreference(HostelListActivity.this, "Employee", "1");
                            break;
                        } else {
                            statusvaluepaid = "false";
                            Utility.setSharedPreference(HostelListActivity.this, "Employee", "0");
                        }
                    }
                    if (statusvaluepaid.equals("true")) {
                        new ViewEmaployeeownerid().execute();
                    } else {
                        statusvaluepaid = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }
                } else if (navMenuTitles[position].equals("View Property")) {

                    Intent it = new Intent(HostelListActivity.this, HostelDetailActivity.class);
                    it.putExtra("property_id", mSharedStorage.getUserPropertyId());
                    startActivity(it);
                } else if (navMenuTitles[position].equals("Make Payment")) {
                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("monthly payment")) {
                            statusvaluepaid = "true";
                            break;
                        } else {
                            statusvaluepaid = "false";
                        }
                    }

                    if (statusvaluepaid.equals("true")) {
                        if (mSharedStorage.getUserType().equals("2")) {
                            Intent it = new Intent(HostelListActivity.this, TanentactivityFrom.class);
                            startActivity(it);
                        } else {
                            Intent it = new Intent(HostelListActivity.this, ParentActivityform.class);
                            startActivity(it);
                        }
                    } else {
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }

                } else if (navMenuTitles[position].equals("Payment Report")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("monthly payment")) {
                            statusvaluepaid = "true";
                            break;
                        } else {
                            statusvaluepaid = "false";
                        }
                    }

                    if (statusvaluepaid.equals("true")) {
                        Intent it = new Intent(HostelListActivity.this, ListViewActivityTanent.class);
                        it.putExtra("property_id", mSharedStorage.getUserPropertyId());
                        startActivity(it);

                    } else {
                        statusvaluepaid = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }

                } else if (navMenuTitles[position].equals("Change Property")) {

                    Validation mValidation = new Validation(fActivity);
                    if (mValidation.checkNetworkRechability()) {
                        new changepropety().execute();
                    } else {
                        mCommon.displayAlert(fActivity, getResources().getString(R.string.str_no_network), false);
                    }
                } else if (navMenuTitles[position].equals("Complaint")) {
                    if (mSharedStorage.getUserType().equals("3")) {

                        if (NetworkConnection.isConnected(HostelListActivity.this)) {
                            new GetOwnerPropertyId().execute();
                        } else {
                        }

                    } else if (mSharedStorage.getUserType().equals("2")) {

                        Intent itnew = new Intent(HostelListActivity.this, Tenant_Complaint_Activity.class);
                        startActivity(itnew);

                    } else if (mSharedStorage.getUserType().equals("6")) {
                        propertyidvalue_new = mSharedStorage.getUserPropertyId();
                        String wardenownerid = mSharedStorage.getPropertyOwnerId();
                        String wardenid = mSharedStorage.getWardenuserId();
                        String warden_name = mSharedStorage.getUserName();
                        Intent intent = new Intent(HostelListActivity.this, Warden_Complaint_Activity.class);
                        intent.putExtra("Ownerid", wardenownerid);
                        intent.putExtra("Propertyid", propertyidvalue_new);
                        intent.putExtra("Wardenid", wardenid);
                        intent.putExtra("WardenName", warden_name);
                        startActivity(intent);

                    } else {
                        new GetParentIdComplaint().execute();
                    }
                } else if (navMenuTitles[position].equals("Add Employee")) {

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];
                        if (paidvalue.equalsIgnoreCase("add employee")) {
                            statusvaluepaid = "true";
                            break;
                        } else {
                            statusvaluepaid = "false";
                        }
                    }
                    if (statusvaluepaid.equals("true")) {

                        if (NetworkConnection.isConnected(HostelListActivity.this)) {
                            new GetOwnerIdforuser().execute();
                        } else {
                        }
                    } else {
                        statusvaluepaid = "false";
                        mCommon.displayAlert(fActivity, "This Servies Is Paid Please Contact:- 9926914699 To Buy", false);
                    }

                } else if (navMenuTitles[position].equals("Feedback")) {
                    if (mSharedStorage.getUserType().equals("3")) {
                        new GetOwnerPropertyIdFeedback().execute();
                    } else if (mSharedStorage.getUserType().equals("2")) {
                        Intent intentfeedback = new Intent(HostelListActivity.this, Tenant_Feedback_Activity.class);
                        startActivity(intentfeedback);
                    } else if (mSharedStorage.getUserType().equals("6")) {
                        propertyidvalue_new = mSharedStorage.getUserPropertyId();
                        String wardenownerid = mSharedStorage.getPropertyOwnerId();
                        String wardenid = mSharedStorage.getWardenuserId();
                        String warden_name = mSharedStorage.getUserName();
                        Intent intent = new Intent(HostelListActivity.this, Warden_Feedback_Activity.class);
                        intent.putExtra("Ownerid", wardenownerid);
                        intent.putExtra("Propertyid", propertyidvalue_new);
                        intent.putExtra("Wardenid", wardenid);
                        intent.putExtra("Wardenname", warden_name);
                        startActivity(intent);
                    } else {
                        new GetParentIdFeedback().execute();
                    }
                } else if (navMenuTitles[position].equals("Download Tenant Info")) {
                    displayPdfAlert(HostelListActivity.this, "Tenant Information",
                            "Do you want to download tenant information.");
                } else if (navMenuTitles[position].equals("Expenses")) {
                    propertyidvalue_new = mSharedStorage.getUserPropertyId();
                    Intent intent = new Intent(HostelListActivity.this, Catering_Management_Owner.class);
                    intent.putExtra("Propertyid", propertyidvalue_new);
                    startActivity(intent);
                } else if (navMenuTitles[position].equals("Logout")) {
                    Utility.setSharedPreference(HostelListActivity.this, "Payment", "0");
                    Utility.setSharedPreference(HostelListActivity.this, "Other", "0");
                    Utility.setSharedPreference(HostelListActivity.this, "Attendance", "0");
                    Utility.setSharedPreference(HostelListActivity.this, "Employee", "0");
                    Utility.setSharedPreference(mcContext, "HideSearch", "");
                    Utility.setSharedPreference(HostelListActivity.this, "PARENTID", "");
                    mSharedStorage.clearUserData();
                    Intent mIntent = new Intent(HostelListActivity.this, LandingActivityWithoutLogin.class);
                    startActivity(mIntent);
                    HostelListActivity.this.finish();
                }
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                    mDrawerList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
        });
        filterBtnTab.setOnClickListener(this);
        sortBtnTab.setOnClickListener(this);
        mapBtnTab.setOnClickListener(this);
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

        Common.hideKeybord(this, myAutoComplete);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setListner() {
        // Search city data
        mArrayCity = new ArrayList<>();
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(HostelListActivity.this, "");
        mGetCityAsyncTask.setCallBack(HostelListActivity.this);
        mGetCityAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (mArrayCity.size() == 0) {
            StateModal mCity = new StateModal();
            mCity.setStrSateId("0");
            mCity.setStrStateName("City");
            mArrayCity.add(mCity);
        }
        mCitySpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_property_city, mArrayCity);
        spinnerCity.setAdapter(mCitySpinnerAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = mArrayCity.get(position).getStrStateName();
                if (position != 0) {
                    cityId = mArrayCity.get(position).getStrSateId();
                    myAutoComplete.setText("");
                    area = "";
                    areaId = "";
                    new GetAreaList(cityId, areaId).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Search area data
        mAdapter = new ObjectAdapter(this, mArrayArea);
        mAdapter.setCallBack(new SpinnerResponce() {
            @Override
            public void requestOTPServiceResponce(String id, String value) {
                area = value;
                areaId = id;
                myAutoComplete.setText(area);
                myAutoComplete.dismissDropDown();
            }
        });
        myAutoComplete.setAdapter(mAdapter);
        myAutoComplete.setThreshold(0);
    }

    public void getTenantDetail() {
        GetTenantInformationServiceAsyncTask service = new GetTenantInformationServiceAsyncTask(this);
        service.setCallBack(new ServiceResponce() {
            @Override
            public void requestResponce(String result) {
                tenantInfoParse(result);
            }
        });
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mSharedStorage.getBookedPropertyId());
    }

    public void tenantInfoParse(String result) {
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    noOfTenant = jsoArray.length();
                    for (int i = 0; i < jsoArray.length(); i++) {
                        RCUDetailModal rcuDetailModal = new RCUDetailModal(Parcel.obtain());
                        rcuDetailModal.setTenant_id(jsoArray.getJSONObject(i).getString("id"));
                        rcuDetailModal.setTenant_form_no(jsoArray.getJSONObject(i).getString("tenant_form_no"));
                        rcuDetailModal.setTenant_fname(jsoArray.getJSONObject(i).getString("tenant_fname"));
                        rcuDetailModal.setTenant_lname(jsoArray.getJSONObject(i).getString("tenant_lname"));
                        rcuDetailModal.setTenant_father_name(jsoArray.getJSONObject(i).getString("tenant_father_name"));
                        rcuDetailModal.setLandmark(jsoArray.getJSONObject(i).getString("landmark"));
                        rcuDetailModal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
                        rcuDetailModal.setFlat_number(jsoArray.getJSONObject(i).getString("flat_number"));
                        rcuDetailModal.setTenant_contact_number(jsoArray.getJSONObject(i).getString("tenant_contact_number"));
                        rcuDetailModal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                        rcuDetailModal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                        rcuDetailModal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                        rcuDetailModal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                        rcuDetailModal.setAadhar_card_no(jsoArray.getJSONObject(i).getString("aadhar_card_no"));
                        rcuDetailModal.setAadhar_card_photo(jsoArray.getJSONObject(i).getString("aadhar_card_photo"));
                        rcuDetailModal.setArm_licence_no(jsoArray.getJSONObject(i).getString("arm_licence_no"));
                        rcuDetailModal.setArm_licence_photo(jsoArray.getJSONObject(i).getString("arm_licence_photo"));
                        rcuDetailModal.setVehicle_registration_no(jsoArray.getJSONObject(i).getString("vehicle_registration_no"));
                        rcuDetailModal.setVehicle_registration_photo(jsoArray.getJSONObject(i).getString("vehicle_registration_photo"));
                        rcuDetailModal.setVoter_id_card_no(jsoArray.getJSONObject(i).getString("voter_id_card_no"));
                        rcuDetailModal.setVoter_id_card_photo(jsoArray.getJSONObject(i).getString("voter_id_card_photo"));
                        rcuDetailModal.setDriving_license_no(jsoArray.getJSONObject(i).getString("driving_license_no"));
                        rcuDetailModal.setDriving_license_photo(jsoArray.getJSONObject(i).getString("driving_license_photo"));
                        rcuDetailModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                        rcuDetailModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                        rcuDetailModal.setGuarantor_name(jsoArray.getJSONObject(i).getString("guarantor_name"));
                        rcuDetailModal.setGuarantor_address(jsoArray.getJSONObject(i).getString("guarantor_address"));
                        rcuDetailModal.setGuarantor_contact_number(jsoArray.getJSONObject(i).getString("guarantor_contact_number"));
                        rcuDetailModal.setState(jsoArray.getJSONObject(i).getString("state"));
                        rcuDetailModal.setCity(jsoArray.getJSONObject(i).getString("city"));
                        rcuDetailModal.setLocation(jsoArray.getJSONObject(i).getString("location"));
                        rcuDetailModal.setTelephone_number(jsoArray.getJSONObject(i).getString("telephone_number"));
                        rcuDetailModal.setMobile_number(jsoArray.getJSONObject(i).getString("mobile_number"));
                        rcuDetailModal.setPincode(jsoArray.getJSONObject(i).getString("pincode"));
                        rcuDetailModal.setContact_number(jsoArray.getJSONObject(i).getString("contact_number"));
                        rcuDetailModal.setEmail_id(jsoArray.getJSONObject(i).getString("email_id"));
                        rcuDetailModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                        rcuDetailModal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                        rcuDetailModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                        Common.rcuDetails.add(rcuDetailModal);
                        updateBtnPressed();
                    }
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {

        } catch (NullPointerException e) {
        }
    }

    public void updateBtnPressed() {
        Intent mIntent = new Intent(this, PropertyRCUTenantDetailActivity.class);
        mIntent.putExtra("RCUId", "" + Common.rcuDetails.get(0).getTenant_id());
        mIntent.putExtra("noOfTenant", noOfTenant);
        mIntent.putExtra("isUpdate", true);
        mIntent.putExtra("property_id", mSharedStorage.getBookedPropertyId());
        mIntent.putExtra("owner_id", ""/*owner_id*/);
        startActivity(mIntent);
    }

    //for update feedback count in background
    @Override
    protected void onRestart() {
        super.onRestart();
        if (NetworkConnection.isConnected(HostelListActivity.this)) {
            new Propertyid_Jsonowner().execute();
            new GetOwnerComplaintCountNew().execute();
            new GetOwnerFeedbackCountNew().execute();
        } else {
            Toast.makeText(HostelListActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListner();
        if (mSharedStorage.getLoginStatus() && !Common.isDatabaseSyncingInProgress) {
            mBackgroungSyncing = new BackgroungSyncing(this);
            Common.isDatabaseSyncingInProgress = true;
            Common._isDatabaseSyncingFromBackground = true;
            cityId = "0";
            areaId = "0";
            mBackgroungSyncing.startBGSyncing();
        }
        Common.hideKeybord(this, myAutoComplete);
    }

    @Override
    public void requestCityDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayCity.clear();
        mArrayCity.addAll(mArray);
        if (mArrayCity != null && mArrayCity.size() > 0) {
            for (int i = 0; i < mArrayCity.size(); i++) {
                if (mArrayCity.get(i).getStrSateId().equals(cityId)) {
                    spinnerCity.setSelection(i);
                }
            }
        }
        mCitySpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestAreaDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayArea.clear();
        mArrayArea.addAll(mArray);
        if (mArrayArea != null && mArrayArea.size() > 0) {
            for (int i = 0; i < mArrayArea.size(); i++) {
                if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                    myAutoComplete.setText("" + mArrayArea.get(i).getStrSateId());
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private Intent mIntent = null;

    private void displayView(int position) {

        Fragment mFragment = null;
        switch (position) {
            case 6:
                mFragment = new PropertyListMapFragment();

                break;
            case 7:
                String bookproeprtyvalue = mSharedStorage.getBookedPropertyId().toString();
                String usertype = mSharedStorage.getUserType();
                if (!bookproeprtyvalue.equalsIgnoreCase("0") && !bookproeprtyvalue.equalsIgnoreCase("") && usertype.equalsIgnoreCase("2")) {
                    mFragment = new SimpleTenantPropertyListFragment();
                } else if (!bookproeprtyvalue.equalsIgnoreCase("0") && !bookproeprtyvalue.equalsIgnoreCase("") && usertype.equalsIgnoreCase("4")) {
                    mFragment = new SimpleTenantPropertyListFragment();
                } else {
                    mFragment = new PropertyListFragment();
                }
                break;
        }

        if (mFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                clearBackStack(fragmentManager);
            }
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerLayout, mFragment).commit();
        }
        mDrawerLayout.closeDrawers();
    }

    public void clearBackStack(FragmentManager manager) {
        int rootFragment = manager.getBackStackEntryAt(0).getId();
        manager.popBackStack(rootFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filterBtnTab:
                SearchFilterDialog filterDialog = new SearchFilterDialog();
                filterDialog.getPerameter(this);
                filterDialog.show(getSupportFragmentManager(), "Search filter");
                break;
            case R.id.sortBtnTab:
                break;
            case R.id.mapBtnTab:
                if (mapText.getText().equals("Map")) {
                    mapText.setText("List");
                    displayView(6);
                } else {
                    mapText.setText("Map");
                    displayView(7);
                }
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private void displayPdfAlert(final Activity activity, String title, String message) {

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Panel);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_two_btn_dialog);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wmlp);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        txtTitle.setText(title);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.categoryNameInput);
        txtMessage.setText(message);

        if (!dialog.isShowing())
            dialog.show();

        Button btnYes = (Button) dialog.findViewById(R.id.alertYesBtn);
        btnYes.setText("No");
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btnNo = (Button) dialog.findViewById(R.id.alertNoBtn);
        btnNo.setText("Yes");
        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                downLoadRCU(mSharedStorage.getBookedPropertyId(), mSharedStorage.getUserId());
            }
        });

    }

    public void downLoadRCU(String property_id, String user_id) {

        Validation mValidation = new Validation(this);
        if (mValidation.checkNetworkRechability()) {
            DownloadRCUFromServiceAsyncTask serviceAsyncTask = new DownloadRCUFromServiceAsyncTask(this);
            serviceAsyncTask.setCallBack(this);
            serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    property_id, user_id, "Police Verification");
        } else {
            mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
        }
    }

    @Override
    public void requestResponce(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                String pdf = WebUrls.IMG_URL + "" + jsonObject.getString(WebUrls.DATA_JSON);
                new DownloadFile().execute(pdf, "maven.pdf");
            }
        } catch (JSONException e) {
        }

    }

    public void view() {
        File pdfFile = new File(Environment.getExternalStorageDirectory() +
                "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(HostelListActivity.this,
                    "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroungSyncing != null) {
            mBackgroungSyncing.stopBGSyncing();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBackgroungSyncing != null) {
            mBackgroungSyncing.stopBGSyncing();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            Common.profileImagePath = null;
            getFragmentManager().popBackStack();
        } else {

            if (mapText.getText().equals("List")) {
                mapText.setText("Map");
                displayView(7);
            } else {
                CustomDialogClassBack cdd = new CustomDialogClassBack(HostelListActivity.this, R.style.full_screen_dialog);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(this, getResources()
                                .getString(R.string.app_name),
                        getResources().getString(R.string.str_exitapp_message));
                cdd.show();
            }
        }

    }

    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClassBack extends Dialog {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;
        private String titleStr;
        private String messageStr;

        public CustomDialogClassBack(HostelListActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, String messageStr) {

            mFActivity = activity;
            this.titleStr = titleStr;
            this.messageStr = messageStr;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.alert_two_btn_dialog);
            title = (TextView) findViewById(R.id.alertTitle);
            message = (TextView) findViewById(R.id.categoryNameInput);
            alertYesBtn = (Button) findViewById(R.id.alertYesBtn);
            alertNoBtn = (Button) findViewById(R.id.alertNoBtn);

            title.setText(titleStr);
            message.setText(messageStr);

            alertYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginActivity.mActivity != null) {
                        LoginActivity.mActivity.finish();
                    }
                    if (RegistrationActivity.mActivity != null) {
                        RegistrationActivity.mActivity.finish();
                    }

                    ActivityCompat.finishAffinity(HostelListActivity.this);
                    System.exit(0);
                    dismiss();
                }
            });

            alertNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }
    //

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
        System.gc();
        Toast.makeText(this, "Low memory", Toast.LENGTH_SHORT).show();
    }

    //.....for getting fine property id and owner id
    private class GetOwnerPropertyIdFinevalue extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
                    }
                }

            } catch (Exception e) {
                IsError = true;
                Log.d("Exp=", "" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
                String newownerid = ownerid_new;
                mIntent = new Intent(HostelListActivity.this, Owner_Fine_Activity.class);
                mIntent.putExtra("Ownerid", newownerid);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);

            } else if (result.equals("false")) {
            }

        }

    }


    //this class is used getting value by webservices
    private class Propertyid_Json extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            Log.d("user id value", propertyidvalue_new);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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
            if (result.equalsIgnoreCase("true")) {

                mIntent = new Intent(HostelListActivity.this, Dateselection_AttendenceActivity_Owner.class);
                mIntent.putExtra("ownerid", ownerid_new);
                mIntent.putExtra("property_id_data", propertyidvalue_new);
                startActivity(mIntent);


            } else if (result.equals("false")) {
            }
        }

    }

    //This class is used to find owner id and send to Owner_Feedback_Activity.class

    private class GetOwnerPropertyIdFeedback extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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


            //    if (!IsError) {
            if (result.equalsIgnoreCase("true")) {

                String newownerid = ownerid_new;
                Intent intent = new Intent(HostelListActivity.this, Owner_Feedback_Activity.class);
                intent.putExtra("Ownerid", newownerid);
                intent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(intent);

            } else if (result.equals("false")) {
            }

        }


    }

    //end
    //change property for calling API
    //class is used to find owner id and send to Owner_Complaint_Activity.class
    private class changepropety extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            Log.d("user id value", propertyidvalue_new);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result.equalsIgnoreCase("true")) {

                String newownerid = ownerid_new;
                Intent it = new Intent(HostelListActivity.this, SelectPropertymultipleActivity.class);
                it.putExtra("Ownerid", newownerid);
                startActivity(it);

            } else if (result.equals("false")) {
            }
        }
    }

    //......Enventory data

    private class GetownerincventoryPropertyId extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result.equalsIgnoreCase("true")) {
                String newownerid = ownerid_new;
                Intent mIntent = new Intent(HostelListActivity.this, Enventory_Activity_Owner.class);
                mIntent.putExtra("Ownerid", newownerid);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);
            } else if (result.equals("false")) {
            }
        }
    }

    //other copy api only 200 testing
    public class GetOwnerPropertyId extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
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
                        JSONArray jsonarray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobj = jsonarray.getJSONObject(i);
                            ownerid_new = jsonobj.getString("owner_id");
                        }
                        String newownerid = ownerid_new;
                        Intent intent = new Intent(HostelListActivity.this, Owner_Complaint_Activity.class);
                        intent.putExtra("Ownerid", newownerid);
                        intent.putExtra("Propertyid", propertyidvalue_new);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    // class is used to show complaint count at owner side send by tenant
    public class GetTenantComplaintCountNew extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String propertyidvalue = mSharedStorage.getUserPropertyId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "ownergetunreadmsgcount"));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyidvalue));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", OwnerId));
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
            valuecount = 0;
            Count = 0;
            Countss = "";
            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Countss = object.getString("countmsg");
                        }
                        Count = jsonArray.length();
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    // class is used to show complaint count at Tenant side send by Owner
    public class GetOwnerComplaintCountNew extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String Tenantidvalue = mSharedStorage.getUserId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "tenantgetunreadmsgcount"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", Tenantidvalue));
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
            valuecount = 0;
            tenantcount = 0;
            tenantcountss = "";

            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            tenantcountss = jsonObject.getString("countmsg");
                        }
                        tenantcount = jsonArray.length();
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end

    // class is used to show Feedback count at owner side send by tenant
    public class GetTenantFeedbackCountNew extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String propertyidvalue = mSharedStorage.getUserPropertyId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "ownergetunreadfeedbackcount"));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyidvalue));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", OwnerId));
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
            valuecount = 0;
            feedbackownercountss = "";
            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            feedbackownercountss = jsonObject.getString("countmsg");
                        }
                        feedbackownercount = jsonArray.length();
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end

    // class is used to show Feedback count at Tenant side send by Owner
    public class GetOwnerFeedbackCountNew extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String Tenantidvalue = mSharedStorage.getUserId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "tenantgetunreadfeedbackcount"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", Tenantidvalue));
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
            valuecount = 0;
            feedbacktenantcount = 0;
            feedbacktenantcountss = "";
            if (result == null) {
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            feedbacktenantcountss = jsonObject.getString("countmsg");
                        }
                        feedbacktenantcount = jsonArray.length();
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    //new changes
    private class PropertyidtenantJson extends AsyncTask<String, String, String> {

        String pid = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getpropertyid"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
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
            String parent_id = null;
            if (result == null) {
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonarray = obj.getJSONArray("records");
                        Log.d("uu 0", "" + jsonarray);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobj = jsonarray.getJSONObject(i);
                            propertyidmainvalue = jsonobj.getString("property_id");
                            owneridforpaid = jsonobj.getString("owner_id");
                            mSharedStorage.setBookedPropertyId(propertyidmainvalue);
                        }
                        OwnerId = owneridforpaid;
                        pid = propertyidmainvalue;
                        if (!pid.equalsIgnoreCase("")) {
                            //hide for tenant and parent end
                            searchlayout.setVisibility(View.GONE);
                            bottomLayout_panal.setVisibility(View.GONE);
                            Utility.setSharedPreference(mcContext, "HideSearch", pid);
                        }
                        //end
                        new PaidUnpaidLogictenant(OwnerId, pid).execute();

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //...
    // this class is used to get complain count at owner end
    private class Propertyid_Jsonowner extends AsyncTask<String, String, String> {
        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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

            if (result.equalsIgnoreCase("true")) {
                OwnerId = ownerid_new;
                new PaidUnpaidLogicJson(OwnerId).execute();
            } else if (result.equals("false")) {
            }
        }
    }

    private class GetParentIdJson extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

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
            String useridvalue = mSharedStorage.getUserId();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getparentdetails"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", useridvalue));
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
            String parent_id = null;

            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            parent_id = jsonObject.getString("parent_id");
                        }
                        mIntent = new Intent(HostelListActivity.this, AddChild_activity.class);
                        mIntent.putExtra("useridvalue", parent_id);
                        startActivity(mIntent);
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //this class is used to get parent id
    private class GetParentshareddataid extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        String useridvalue;

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
            useridvalue = mSharedStorage.getUserId();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getparentdetails"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
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
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            parent_id = jsonObject.getString("parent_id");
                            mSharedStorage.setParent_Id(parent_id);
                        }
                        Utility.setSharedPreference(HostelListActivity.this, "PARENTID", parent_id);
                        new Getowneridforparent().execute();
                        new GetParentComplaintCountNew().execute();
                        new GetParentFeedbackCountNew().execute();
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    // class is used to show complaint count at Parentside side send by tenant
    public class GetParentComplaintCountNew extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String propertyidvalue = mSharedStorage.getUserPropertyId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "parentgetunreadmsgcount"));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", parent_id));
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
            Parentcomplaintcount = "";
            if (result == null) {
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Parentcomplaintcount = object.getString("countmsg");
                        }
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end

    // class is used to show Feedback count at Parentside side send by tenant
    public class GetParentFeedbackCountNew extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String propertyidvalue = mSharedStorage.getUserPropertyId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "parentgetunreadfeedbackcount"));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", parent_id));
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
            Parentfeedbackcount = "";
            if (result == null) {
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Parentfeedbackcount = object.getString("countmsg");
                        }
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end

    private class GetParentIdJsonViewChild extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        String useridvalue;

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
            useridvalue = mSharedStorage.getUserId();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getparentdetails"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", useridvalue));
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
            String parent_id = null;
            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            parent_id = jsonObject.getString("parent_id");
                        }
                        String mainuseriddata = useridvalue;
                        mIntent = new Intent(HostelListActivity.this, ViewChildWorkingActivity.class);
                        mIntent.putExtra("useridvalue", mainuseriddata);
                        mIntent.putExtra("parentvalueid", parent_id);
                        startActivity(mIntent);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class PaidUnpaidLogicJson extends AsyncTask<String, String, String> {
        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String ownerid;

        public PaidUnpaidLogicJson(String ownerid) {
            this.ownerid = ownerid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            String owneridvalue = ownerid;
            Log.d("user id value", propertyidvalue_new);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getpaidservice"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyidvalue_new));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                datapaidlist = new ArrayList<>();
                if (result.equalsIgnoreCase("y")) {
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String paid_service = jsonObject.getString("paid_service");
                        paidarray = paid_service.split(",");

                        for (int j = 0; j < paidarray.length; j++) {
                            String paidvalue = paidarray[j];
                            datapaidlist.add(paidvalue);
                        }
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
                String owneridvalue = ownerid;
                new GetTenantComplaintCountNew().execute();
                new GetTenantFeedbackCountNew().execute();
            } else if (result.equals("N")) {
            }
        }
    }

    //for tenant
    private class PaidUnpaidLogictenant extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String ownerid;
        String propertynew;

        public PaidUnpaidLogictenant(String ownerid, String pid) {
            this.ownerid = ownerid;
            this.propertynew = pid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getpaidservice"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertynew));
                nameValuePairs.add(new BasicNameValuePair("owner_id", ownerid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

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
                Log.v("22", "22" + e.getMessage());
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


    //ParentId for Complaint send
    private class GetParentIdComplaint extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        String useridvalue;

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
            useridvalue = mSharedStorage.getUserId();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getparentdetails"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", useridvalue));
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
            String parent_id = null;

            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            parent_id = jsonObject.getString("parent_id");
                        }
                        String mainuseriddata = useridvalue;
                        Intent pintent = new Intent(HostelListActivity.this, Parent_Complaint_Activity.class);
                        pintent.putExtra("useridvalue", mainuseriddata);
                        pintent.putExtra("parentvalueid", parent_id);
                        startActivity(pintent);

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    //ParentId for Feedback send
    private class GetParentIdFeedback extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        String useridvalue;

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
            useridvalue = mSharedStorage.getUserId();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getparentdetails"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", useridvalue));
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
            String parent_id = null;

            if (result == null) {
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            parent_id = jsonObject.getString("parent_id");
                        }
                        String mainuseriddata = useridvalue;
                        Intent pintent = new Intent(HostelListActivity.this, Parent_Feedback_Activity.class);
                        pintent.putExtra("useridvalue", mainuseriddata);
                        pintent.putExtra("parentvalueid", parent_id);
                        startActivity(pintent);

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    // this class is used to get owner id value for add other owner role
    private class GetOwnerIdforuser extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {
                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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

            if (result.equalsIgnoreCase("true")) {
                String newownerid = ownerid_new;
                Intent tent = new Intent(HostelListActivity.this, Add_Other_Owner_Activity.class);
                tent.putExtra("OuserId", mSharedStorage.getUserId());
                tent.putExtra("OpropertyId", mSharedStorage.getUserPropertyId());
                tent.putExtra("Ownerid", newownerid);
                startActivity(tent);
            } else if (result.equals("false")) {
            }
        }
    }

    //end
    // class is used to show complaint count at warden side send by others
    public class GetWardenComplaintCountNew extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String wardenid = mSharedStorage.getWardenuserId();
            pid = mSharedStorage.getUserPropertyId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "wardengetunreadmsgcount"));
            listNameValuePairs.add(new BasicNameValuePair("warden_id", wardenid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", pid));
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
            complaintwardencountss = "";
            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            complaintwardencountss = object.getString("countmsg");
                        }
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //end
    // class is used to show complaint count at warden side send by others
    public class GetWardenFeedbackCountNew extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String wardenid = mSharedStorage.getWardenuserId();
            pid = mSharedStorage.getUserPropertyId();
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "wardengetunreadfeedbackcount"));
            listNameValuePairs.add(new BasicNameValuePair("warden_id", wardenid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", pid));
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
            feedbackwardencountss = "";
            if (result == null) {

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            feedbackwardencountss = object.getString("countmsg");
                        }
                        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                        mDrawerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //end

    private class ViewEmaployeeownerid extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String userId_value = mSharedStorage.getUserId();
            propertyidvalue_new = mSharedStorage.getUserPropertyId();
            Log.d("user id value", propertyidvalue_new);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getownerdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userId_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        ownerid_new = jsonobj.getString("owner_id");
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
            dialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
                OwnerId = ownerid_new;
                Intent mIntent = new Intent(HostelListActivity.this, ViewEmployeeActivity.class);
                mIntent.putExtra("owner_id", OwnerId);
                mIntent.putExtra("property_id", propertyidvalue_new);
                startActivity(mIntent);
            } else if (result.equals("false")) {

            }
        }
    }

    private class Getowneridforparent extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        String propertyid="", ownerid="";
        SimpleTenantPropertyListFragment mFragment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            mSharedStorage = SharedStorage.getInstance(HostelListActivity.this);
            String result = null;
            String url = null;
            String parentid = mSharedStorage.getParent_Id();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getpropertyidforparent"));
            listNameValuePairs.add(new BasicNameValuePair("parent_id", parentid));
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

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equalsIgnoreCase("Y")) {
                        JSONArray jsonArray = obj.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            propertyid = jsonObject.getString("property_id");
                            ownerid = jsonObject.getString("owner_id");
                            mSharedStorage.setBookedPropertyId(propertyid);
                        }
                        if (!propertyid.equalsIgnoreCase("0")) {
                            mFragment = new SimpleTenantPropertyListFragment();
                            if (mFragment != null) {
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                if (fragmentManager.getBackStackEntryCount() > 0) {
                                    clearBackStack(fragmentManager);
                                }
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainerLayout, mFragment).commit();
                            }
                        }
                        parentpropertyhide = true;
                        if (!propertyid.equalsIgnoreCase("")) {
                            searchlayout.setVisibility(View.GONE);
                            bottomLayout_panal.setVisibility(View.GONE);
                            searchlayoutnotification.setVisibility(View.VISIBLE);
                            getNotificationCount(propertyid);
                        }
                        new PaidUnpaidLogictenant(ownerid, propertyid).execute();

                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class GetAreaList extends AsyncTask<String, String, String> {

        String name, result, message, cityId, areaId;
        ProgressDialog pd;
        private boolean IsError = false;

        public GetAreaList(String cityId, String areaId) {
            this.cityId = cityId;
            this.areaId = areaId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HostelListActivity.this);
            pd.setMessage("Please wait..........");
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getAreaList"));
                nameValuePairs.add(new BasicNameValuePair("city_id", cityId));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
                    mArrayArea.clear();
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        StateModal mCountry = new StateModal();
                        String countryId = jsonobj.getString("city_id");
                        String stateId = jsonobj.getString("id");
                        String stateName = jsonobj.getString(("area_name"));
                        mCountry.setCountryId(countryId);
                        mCountry.setStrSateId(stateId);
                        mCountry.setStrStateName(stateName);
                        mArrayArea.add(mCountry);
                    }
                }
                int sievakyew = mArrayArea.size();
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            if (result.equalsIgnoreCase("Y")) {
                if (mArrayArea != null && mArrayArea.size() > 0) {
                    String areaidvalue = areaId;
                    Log.d("areaid", areaidvalue);
                    for (int i = 0; i < mArrayArea.size(); i++) {
                        if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                            myAutoComplete.setText("" + mArrayArea.get(i).getStrStateName());
                        }
                    }
                } else {
                    mArrayArea.clear();
                }
                mAdapter.notifyDataSetChanged();
            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(HostelListActivity.this, message, Toast.LENGTH_LONG).show();
                mArrayArea.clear();
            }
        }
    }

    //night attendance count
    private void getNightRecord() {
        final ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        formattedDate = df.format(calnder.getTime());
        String propertyid = mSharedStorage.getUserPropertyId();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "nightattendenceabsentrecords");
            params.put("property_id", propertyid);
            params.put("date", formattedDate);
            String url = WebUrls.MAIN_ATTENDACE_URL;
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
            nightattendancecount = "";
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                nightattendancecount = String.valueOf(jsonarray.length());
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

                    String result = response.toString();
                    getResponseDatahostel(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

    private void getHostelnameTenant() {
        try {
            String propertyid = mSharedStorage.getBookedPropertyId();
            JSONObject params = new JSONObject();
            params.put("action", "gethostelname");
            params.put("property_id", propertyid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String result = response.toString();
                    getResponseDatahostelTenant(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDatahostelTenant(String result) {
        try {
            String status, hostelname = "";
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
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

    private void getNotificationCount(String propertyid) {

        final ProgressDialog dialog = new ProgressDialog(HostelListActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "notification_count");
            params.put("property_id", propertyid);
            params.put("parent_id", parent_id);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataNotification(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDataNotification(String result) {
        try {
            String status, message, notification_count = "";
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                notification_count = jsmain.getString("totalnotification");
                txtnotification.setText(notification_count);
            } else {
                Toast.makeText(HostelListActivity.this, message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;
        private String titleStr;
        private String messageStr;

        public CustomDialogClass(HostelListActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
        }

        public void getPerameter(FragmentActivity activity, String titleStr, String messageStr) {
            mFActivity = activity;
            this.titleStr = titleStr;
            this.messageStr = messageStr;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.alert_two_btn_dialog);
            title = (TextView) findViewById(R.id.alertTitle);
            message = (TextView) findViewById(R.id.categoryNameInput);
            alertYesBtn = (Button) findViewById(R.id.alertYesBtn);
            alertNoBtn = (Button) findViewById(R.id.alertNoBtn);
            alertYesBtn.setOnClickListener(this);
            alertNoBtn.setOnClickListener(this);
            title.setText(titleStr);
            message.setText(messageStr);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alertYesBtn:
                    mIntent = new Intent(HostelListActivity.this, LoginActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.alertNoBtn:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}