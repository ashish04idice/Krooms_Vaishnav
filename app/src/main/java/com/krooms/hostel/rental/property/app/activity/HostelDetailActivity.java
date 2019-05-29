package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.adapter.BedSelectionGridAdapter;
import com.krooms.hostel.rental.property.app.asynctask.HostelDetailServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.PaymentOptionThroughCase;
import com.krooms.hostel.rental.property.app.asynctask.UploadRoomImageServiceAsyncTask;
import com.krooms.hostel.rental.property.app.autorotateviewpager.AutoScrollViewPager;
import com.krooms.hostel.rental.property.app.callback.AminitesDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.FeaturesDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponseBed;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingActivity;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.database.GetAminitiesAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetFeaturesAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.modal.FeatureListModal;
import com.krooms.hostel.rental.property.app.modal.RoomModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.AminitiesAndFeaturesPagerAdapter;
import com.krooms.hostel.rental.property.app.adapter.PropertyDetatilViewpagerAdapter;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.ScrollViewChildScrollListener;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.indicator.CirclePageIndicator;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class HostelDetailActivity extends FragmentActivity implements View.OnClickListener,
        ServiceResponce, FeaturesDataBaseResponce, AminitesDataBaseResponce, ServiceResponseBed {

    private int selected_index;
    public static String Room_selected_id;
    public static String Room_rent;
    public static double room_rent = 0;
    public static double room_rent_perDay = 0;
    public static FragmentActivity mActivity = null;
    public static PropertyUserModal tenantdatamodal;
    private String listType;
    private TextView hostelName;
    private TextView hostelAddress;
    private TextView PropertyNameTitle;
    private ImageButton descriptionUpDownBtn, aminitiesUpDownBtn, featureUpDownBtn, mapUpDownBtn, bedSelectionUpDown;
    private ImageButton editPropertyBtn;
    private ArrayList<PropertyPhotoModal> list = new ArrayList<>();
    private SharedStorage mSharedStorage;
    private TextView hireDate;
    private TextView leaveDate;
    private ImageView propertyLocationMap;
    private Button shareBtn;
    private Button propertyVideoBtn;
    private RelativeLayout descriptionTitleLayout, aminitiesTitleLayout, freaturesTitleLayout,
            locationTitleLayout, bedSystemTitleLayout;
    public static String hiredatevaluenew = "";
    private AutoScrollViewPager propertyPhotoslist = null;
    private TextView bookHostelBtn = null;
    private PropertyDetatilViewpagerAdapter adapter = null;
    private RelativeLayout propertyDescriptionTextLayout;
    private TextView property_description_text;
    private TextView durationText;
    private AutoScrollViewPager propertyFeatureList, propertyAminitiesList;
    private RelativeLayout addPropertyBtnlayout;
    private ImageButton btnBack;
    private ListView bedSystemGrid = null;
    private CirclePageIndicator titleIndicatorTop = null;
    private int year1, month, day;
    private TextView priceOfSelectedBed;
    private Validation mValidation;
    private Common mCommon;
    private BedSelectionGridAdapter roomListAdapter;
    private String property_id;
    private ArrayList<StateModal> featureList = new ArrayList<>();
    private ArrayList<StateModal> amenitiesList = new ArrayList<>();
    private TextView totalArea;
    private TextView superBuiltupArea;
    private TextView propertyFace;
    private LinearLayout layoutKitchen, layoutKitchenType;
    private TextView kitchen;
    private TextView kitchenType;
    private TextView furnish;
    private TextView waterSupply;
    private TextView powerBackup;
    private TextView railwayStation;
    private TextView busStand;
    private TextView airport;
    private TextView nearSq;
    private TextView nearSqDistance;
    private TextView tenantType;
    private TextView curfewTime;
    private TextView noOfvehicle;
    private TextView smoking;
    private TextView drinking;
    private TextView noOfPeople;
    private TextView rentAmount;
    private TextView advanceAmount;
    private TextView maintenaceAmount;
    private TextView waterBillAmount;
    private TextView expensessAmount;
    private TextView electriCityBill;
    private Boolean _isAddLocalData = false;
    private ArrayList<FeatureListModal> featureListModalsComplete;
    public static String hireDateValue = "", leaveDateValue = "";
    String statusac = "";
    String googleurl;
    ProgressDialog pd;
    String area = "";
    RelativeLayout youtubevideo, googlevideo, image_cross_layout;
    Dialog successdialog;

    String fromroomchangevalue = "", tenantid = "", oldroomid = "";

    public static String roomchangestatus = "0";


    MapView mMapView;

    GoogleMap mMap;
    Bitmap bitmapMap;

    String mapcityname = "";

    String ImagePath = "";
    Uri uri = null;

    public static String finalbedid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hostel_detail);
        mSharedStorage = SharedStorage.getInstance(this);
        mActivity = this;
        selected_index = getIntent().getIntExtra("selected_index", 0);
        mCommon = new Common();
        room_rent_perDay = 0;
        room_rent = 0;

        if (getIntent().getParcelableExtra("tenantmodal") != null) {
            tenantdatamodal = getIntent().getParcelableExtra("tenantmodal");
            statusac = tenantdatamodal.getStatus_active();
            mSharedStorage.setAddCount("0");
        }

        if (getIntent().hasExtra("property_id")) {
            property_id = getIntent().getExtras().getString("property_id");
        } else {
            property_id = Common.CommonPropertList.get(selected_index).getProperty_id();
        }


        if (getIntent().hasExtra("roomchnage")) {
            tenantid = getIntent().getExtras().getString("tenantid");
            fromroomchangevalue = getIntent().getExtras().getString("roomchnage");
            oldroomid = getIntent().getExtras().getString("roomidnewvalue");

        }

        if (Common.selectedBedInfo != null) {
            Common.selectedBedInfo.clear();
        }

        Date cal = (Date) Calendar.getInstance().getTime();
        year1 = cal.getYear() + 1900;
        month = cal.getMonth();
        day = cal.getDate();
        getFeaturesAndAminities();
        PropertyModal.propertyModal = PropertyModal.getInstance();
        creteView();

        mMapView = (MapView) findViewById(R.id.mapView);
        /*map intizlize*/
        try {

            mMapView.setOnClickListener(HostelDetailActivity.this);
            MapsInitializer.initialize(HostelDetailActivity.this);
            mMapView.onCreate(this.onSaveInstanceState());
            mMapView.onResume();

        } catch (Exception e) {

            e.printStackTrace();

        }

        mValidation = new Validation(this);
        featureListModalsComplete = mCommon.feartueSetUpMethod();
        getReceiver();

        if (getIntent().getBooleanExtra("ShowRooms", false)) {
            if (bedSystemGrid.getVisibility() == View.GONE) {
                bedSystemGrid.setVisibility(View.VISIBLE);
                bedSelectionUpDown.setBackgroundResource(R.drawable.up_caret);
            } else {
                bedSelectionUpDown.setBackgroundResource(R.drawable.ic_down_caret);
                bedSystemGrid.setVisibility(View.GONE);
            }
        }
    }

    private void getFeaturesAndAminities() {
        GetFeaturesAsyncTask mGetFeaturesAsyncTask = new GetFeaturesAsyncTask(this);
        mGetFeaturesAsyncTask.setCallBack(this);
        mGetFeaturesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        GetAminitiesAsyncTask mGetAminitiesAsyncTask = new GetAminitiesAsyncTask(this);
        mGetAminitiesAsyncTask.setCallBack(this);
        mGetAminitiesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void requestAminitesDataBaseResponce(ArrayList<StateModal> mArray) {
        amenitiesList.addAll(mArray);
    }

    @Override
    public void requestFeaturesDataBaseResponce(ArrayList<StateModal> mArray) {
        featureList.addAll(mArray);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _isAddLocalData = true;
        getPropertyDetail();
    }

    public void getReceiver() {

        IntentFilter itFinish = new IntentFilter("com.krooms.hostel.rental.property.app.BED_SELECTION_CHANGE_ACTION");
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!PropertyModal.propertyModal.getProperty_rent_price().equals("")) {
                    room_rent_perDay = room_rent / 30;
                    calculateRent();
                }
            }
        }, itFinish);
    }

    public void creteView() {

        btnBack = (ImageButton) findViewById(R.id.main_header_back);
        hostelName = (TextView) findViewById(R.id.hostelDetailname_id);
        hostelAddress = (TextView) findViewById(R.id.hostelDetail_address_id);
        hireDate = (TextView) findViewById(R.id.hireDate);
        leaveDate = (TextView) findViewById(R.id.leaveDate);
        PropertyNameTitle = (TextView) findViewById(R.id.PropertyNameTitle);
        propertyPhotoslist = (AutoScrollViewPager) findViewById(R.id.property_photos);
        titleIndicatorTop = (CirclePageIndicator) findViewById(R.id.pagerIndicator);
        bookHostelBtn = (TextView) findViewById(R.id.bookPropertyBtn);
        durationText = (TextView) findViewById(R.id.durationText);
        propertyFeatureList = (AutoScrollViewPager) findViewById(R.id.featuresList);
        propertyAminitiesList = (AutoScrollViewPager) findViewById(R.id.aminitiesList);
        descriptionTitleLayout = (RelativeLayout) findViewById(R.id.propertyDescription_layout);
        aminitiesTitleLayout = (RelativeLayout) findViewById(R.id.aminitiesTitleLayout);
        freaturesTitleLayout = (RelativeLayout) findViewById(R.id.freaturesTitleLayout);
        locationTitleLayout = (RelativeLayout) findViewById(R.id.locationTitleLayout);
        bedSystemTitleLayout = (RelativeLayout) findViewById(R.id.bedSystemTitleLayout);
        propertyLocationMap = (ImageView) findViewById(R.id.propertyLocationMap);
        bedSystemGrid = (ListView) findViewById(R.id.bedSystemGrid);
        addPropertyBtnlayout = (RelativeLayout) findViewById(R.id.addPropertyBtnlayout);
        priceOfSelectedBed = (TextView) findViewById(R.id.priceOfSelectedBed);
        bedSystemGrid.setOnTouchListener(new ScrollViewChildScrollListener());
        propertyDescriptionTextLayout = (RelativeLayout) findViewById(R.id.propertyDescription_text_layout);
        propertyDescriptionTextLayout.setVisibility(View.GONE);
        property_description_text = (TextView) findViewById(R.id.property_description_text);
        descriptionUpDownBtn = (ImageButton) findViewById(R.id.propertyDescriptionUpDownBtn);
        aminitiesUpDownBtn = (ImageButton) findViewById(R.id.aminitiesUpDownBtn);
        featureUpDownBtn = (ImageButton) findViewById(R.id.freaturesUpDownBtn);
        mapUpDownBtn = (ImageButton) findViewById(R.id.propertyLocationMapUpDownBtn);
        bedSelectionUpDown = (ImageButton) findViewById(R.id.bedSystemGridUpDownBtn);
        editPropertyBtn = (ImageButton) findViewById(R.id.editPropertyBtn);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        propertyVideoBtn = (Button) findViewById(R.id.propertyVideoBtn);
        descriptionTitleLayout.setOnClickListener(this);
        aminitiesTitleLayout.setOnClickListener(this);
        freaturesTitleLayout.setOnClickListener(this);
        locationTitleLayout.setOnClickListener(this);
        bedSystemTitleLayout.setOnClickListener(this);
        addPropertyBtnlayout.setOnClickListener(this);
        propertyLocationMap.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        propertyVideoBtn.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        hireDate.setOnClickListener(this);
        leaveDate.setOnClickListener(this);

        addPropertyBtnlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSharedStorage.getLoginStatus()) {
                    CustomDialogClass VehicleRcId = new CustomDialogClass(HostelDetailActivity.this, R.style.full_screen_dialog);
                    VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    VehicleRcId.getPerameter(HostelDetailActivity.this,
                            "Login before booking", "Do you want to login?");
                    VehicleRcId.show();

                }

                // for accountant change
                else if (mSharedStorage.getUserType().equals("5")) {
                    mCommon.displayAlert(HostelDetailActivity.this, "Property Accountant can not book property.", false);

                } else {
                    if (mSharedStorage.getUserType().equals("2") &&
                            (!mSharedStorage.getAddCount().equals("") && !mSharedStorage.getAddCount().equals("0"))) {
                        mCommon.displayAlert(HostelDetailActivity.this, "You have already booked a property.", false);
                    } else {

                        if (Common.selectedBedInfo.size() != 0) {

                            BedBookingPayment cdd = new BedBookingPayment(HostelDetailActivity.this, R.style.full_screen_dialog);
                            //changes only room chanage on 7-9-18 by ashish
                            if (fromroomchangevalue.equals("1")) {


                                try {
                                    int noofroombook = (Common.selectedBedInfo.size());
                                    String advanceamountvaluenew = PropertyModal.propertyModal.getProperty_advance_rentAmount();
                                    int value = Integer.parseInt(advanceamountvaluenew);
                                    int advanceamountvalue = noofroombook * value;
                                    String advanceamount = String.valueOf(advanceamountvalue);
                                    cdd.BedBookingPayment(HostelDetailActivity.this,
                                            PropertyModal.propertyModal.propertyModal, hireDate.getText().toString(),
                                            leaveDate.getText().toString(), priceOfSelectedBed.getText().toString(), advanceamount, fromroomchangevalue);
                                    PropertyModal.propertyModal.getProperty_advance_rentAmount();
                                    mSharedStorage.setHireDate(hireDate.getText().toString());
                                    mSharedStorage.setLeaveDate(leaveDate.getText().toString());
                                    hireDateValue = mSharedStorage.getHireDate();
                                    leaveDateValue = mSharedStorage.getLeaveDate();
                                    cdd.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                if (hireDate.getText().toString().equals("")) {

                                    Toast.makeText(HostelDetailActivity.this, "Please Select Hire Date", Toast.LENGTH_SHORT).show();

                                } else {

                                    try {
                                        int noofroombook = (Common.selectedBedInfo.size());
                                        String advanceamountvaluenew = PropertyModal.propertyModal.getProperty_advance_rentAmount();
                                        int value = Integer.parseInt(advanceamountvaluenew);
                                        int advanceamountvalue = noofroombook * value;
                                        String advanceamount = String.valueOf(advanceamountvalue);
                                        cdd.BedBookingPayment(HostelDetailActivity.this,
                                                PropertyModal.propertyModal.propertyModal, hireDate.getText().toString(),
                                                leaveDate.getText().toString(), priceOfSelectedBed.getText().toString(), advanceamount, fromroomchangevalue);
                                        PropertyModal.propertyModal.getProperty_advance_rentAmount();
                                        mSharedStorage.setHireDate(hireDate.getText().toString());
                                        mSharedStorage.setLeaveDate(leaveDate.getText().toString());
                                        hireDateValue = mSharedStorage.getHireDate();
                                        leaveDateValue = mSharedStorage.getLeaveDate();
                                        cdd.show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        } else {
                            mCommon.displayAlert(HostelDetailActivity.this, "Please select bed before booking.", false);
                        }
                    }
                }
            }
        });

        adapter = new PropertyDetatilViewpagerAdapter(this, list);
        propertyPhotoslist.setAdapter(adapter);
        titleIndicatorTop.setViewPager(propertyPhotoslist);
        propertyPhotoslist.startAutoScroll(10000);

        if (property_id.equals(mSharedStorage.getUserPropertyId())) {

            if (mSharedStorage.getUserType().equals("5")) {
                editPropertyBtn.setVisibility(View.GONE);
            } else {
                editPropertyBtn.setVisibility(View.VISIBLE);
                editPropertyBtn.setOnClickListener(this);
            }
        } else {
            editPropertyBtn.setVisibility(View.GONE);
        }
        initDescriptionValue();
    }

    public void initDescriptionValue() {

        totalArea = (TextView) findViewById(R.id.total_area_Value);
        superBuiltupArea = (TextView) findViewById(R.id.super_builtup_value);
        propertyFace = (TextView) findViewById(R.id.property_face_Value);
        layoutKitchen = (LinearLayout) findViewById(R.id.kitchen_layout);
        layoutKitchen.setVisibility(View.GONE);
        layoutKitchenType = (LinearLayout) findViewById(R.id.kitchen_type_layout);
        layoutKitchenType.setVisibility(View.GONE);
        kitchen = (TextView) findViewById(R.id.kitchen_value);
        kitchenType = (TextView) findViewById(R.id.kitchen_type_Value);
        furnish = (TextView) findViewById(R.id.furnish_value);
        waterSupply = (TextView) findViewById(R.id.water_supply_Value);
        powerBackup = (TextView) findViewById(R.id.power_backup_value);
        railwayStation = (TextView) findViewById(R.id.distance_railwaystation_Value);
        busStand = (TextView) findViewById(R.id.distance_busstand_value);
        airport = (TextView) findViewById(R.id.distance_airport_Value);
        nearSq = (TextView) findViewById(R.id.nearestSqareName_value);
        nearSqDistance = (TextView) findViewById(R.id.nearestSqareDistance_Value);
        tenantType = (TextView) findViewById(R.id.allowedtenant_type_value);
        curfewTime = (TextView) findViewById(R.id.curfew_time_Value);
        noOfvehicle = (TextView) findViewById(R.id.no_of_vehicles_value);
        smoking = (TextView) findViewById(R.id.allowed_smoking_Value);
        drinking = (TextView) findViewById(R.id.allowed_drinking_value);
        noOfPeople = (TextView) findViewById(R.id.no_of_pepole_Value);
        rentAmount = (TextView) findViewById(R.id.textview_rent_amount_value);
        advanceAmount = (TextView) findViewById(R.id.textview_advance_amount_value);
        maintenaceAmount = (TextView) findViewById(R.id.textview_maintenance_value);
        waterBillAmount = (TextView) findViewById(R.id.textview_water_bill_value);
        expensessAmount = (TextView) findViewById(R.id.textview_other_expensess_value);
        electriCityBill = (TextView) findViewById(R.id.textview_electricity_bill_value);
    }

    public void setDescriptionValue() {

        if (PropertyModal.propertyModal.getProperty_totalarea().length() == 0
                || PropertyModal.propertyModal.getProperty_totalarea().equals("0")) {
            totalArea.setText("");
        } else {
            totalArea.setText(PropertyModal.propertyModal.getProperty_totalarea() + " sq.ft.");
        }

        if (PropertyModal.propertyModal.getProperty_Super_builtup_area().length() == 0
                || PropertyModal.propertyModal.getProperty_Super_builtup_area().equals("0")) {
            superBuiltupArea.setText("");
        } else {
            superBuiltupArea.setText(PropertyModal.propertyModal.getProperty_Super_builtup_area() + " sq.ft.");
        }
        if (PropertyModal.propertyModal.getProperty_face().equals("Property Face")
                || PropertyModal.propertyModal.getProperty_face().equals("0")) {
            propertyFace.setText("");
        } else {
            propertyFace.setText(PropertyModal.propertyModal.getProperty_face());
        }
        ////////////////////// start kitchen /////////////////////////
        if (PropertyModal.propertyModal.getProperty_type_id().equals("3")
                || PropertyModal.propertyModal.getProperty_type_id().equals("4")
                || PropertyModal.propertyModal.getProperty_type_id().equals("5")) {
            layoutKitchen.setVisibility(View.VISIBLE);
            layoutKitchenType.setVisibility(View.VISIBLE);
            if (PropertyModal.propertyModal.getProperty_kitchen().equals("Select Kitchen")
                    || PropertyModal.propertyModal.getProperty_kitchen().equals("0")) {
                kitchen.setText("");
                layoutKitchenType.setVisibility(View.GONE);
            } else {
                kitchen.setText(PropertyModal.propertyModal.getProperty_kitchen());
                layoutKitchenType.setVisibility(View.VISIBLE);
            }
            kitchenType.setText(PropertyModal.propertyModal.getProperty_kitchen_type());
        } else {
            layoutKitchen.setVisibility(View.GONE);
            layoutKitchenType.setVisibility(View.GONE);
        }

        ////////////////////////// end kitchen //////////////////

        furnish.setText(PropertyModal.propertyModal.getProperty_furnish());

        if (PropertyModal.propertyModal.getProperty_Water_Supplay().equals("Select Supply")
                || PropertyModal.propertyModal.getProperty_Water_Supplay().equals("0")) {
            waterSupply.setText("");
        } else {
            waterSupply.setText(PropertyModal.propertyModal.getProperty_Water_Supplay());
        }
        if (PropertyModal.propertyModal.getProperty_Power_Backup().equals("Select Backup")
                || PropertyModal.propertyModal.getProperty_Power_Backup().equals("0")) {
            powerBackup.setText("");
        } else {
            powerBackup.setText(PropertyModal.propertyModal.getProperty_Power_Backup());
        }
        if (PropertyModal.propertyModal.getProperty_distance_railway().length() == 0
                || PropertyModal.propertyModal.getProperty_distance_railway().equals("0")) {
            railwayStation.setText("");
        } else {
            railwayStation.setText(PropertyModal.propertyModal.getProperty_distance_railway() + " km.");
        }
        if (PropertyModal.propertyModal.getProperty_distance_busstand().length() == 0
                || PropertyModal.propertyModal.getProperty_distance_busstand().equals("0")) {
            busStand.setText("");
        } else {
            busStand.setText(PropertyModal.propertyModal.getProperty_distance_busstand() + " km.");
        }
        if (PropertyModal.propertyModal.getProperty_distance_airport().length() == 0
                || PropertyModal.propertyModal.getProperty_distance_airport().equals("0")) {
            airport.setText("");
        } else {
            airport.setText(PropertyModal.propertyModal.getProperty_distance_airport() + " km.");
        }
        if (PropertyModal.propertyModal.getProperty_nearest_square().length() == 0
                || PropertyModal.propertyModal.getProperty_nearest_square().equals("0")) {
            nearSq.setText("");
        } else {
            nearSq.setText(PropertyModal.propertyModal.getProperty_nearest_square());
        }
        if (PropertyModal.propertyModal.getProperty_nearest_square_distance().length() == 0
                || PropertyModal.propertyModal.getProperty_nearest_square_distance().equals("0")) {
            nearSqDistance.setText("");
        } else {
            nearSqDistance.setText(PropertyModal.propertyModal.getProperty_nearest_square_distance() + " km.");
        }
        if (PropertyModal.propertyModal.getProperty_tenant_type().equals("Select Tenant")
                || PropertyModal.propertyModal.getProperty_tenant_type().equals("0")) {
            tenantType.setText("");
        } else {
            ArrayList<StateModal> mArraynoTenantType = new ArrayList<>();
            mArraynoTenantType.addAll(getTenantType());
            for (int i = 0; i < mArraynoTenantType.size(); i++) {
                if (mArraynoTenantType.get(i).getStrSateId().equals(PropertyModal.propertyModal.getProperty_tenant_type())) {
                    tenantType.setText(mArraynoTenantType.get(i).getStrStateName());
                }
            }
        }
        curfewTime.setText(PropertyModal.propertyModal.getProperty_curfew_time());

        if (PropertyModal.propertyModal.getProperty_allow_no_vehicle().equals("No Of Vehicle")
                || PropertyModal.propertyModal.getProperty_allow_no_vehicle().equals("0")) {
            noOfvehicle.setText("");
        } else {
            noOfvehicle.setText(PropertyModal.propertyModal.getProperty_allow_no_vehicle());
        }

        if (PropertyModal.propertyModal.getProperty_is_allowed_smoking().equals("Smoking")
                || PropertyModal.propertyModal.getProperty_is_allowed_smoking().equals("0")) {
            smoking.setText("");
        } else {
            smoking.setText(PropertyModal.propertyModal.getProperty_is_allowed_smoking());
        }
        if (PropertyModal.propertyModal.getProperty_is_allowed_drinking().equals("Drinking")
                || PropertyModal.propertyModal.getProperty_is_allowed_drinking().equals("0")) {
            drinking.setText("");
        } else {
            drinking.setText(PropertyModal.propertyModal.getProperty_is_allowed_drinking());
        }
        if (PropertyModal.propertyModal.getProperty_no_of_tenant().equals("No Of People")
                || PropertyModal.propertyModal.getProperty_no_of_tenant().equals("0")) {
            noOfPeople.setText("");
        } else {
            noOfPeople.setText(PropertyModal.propertyModal.getProperty_no_of_tenant());
        }

        if (PropertyModal.propertyModal.getProperty_rent_price().length() == 0
                || PropertyModal.propertyModal.getProperty_rent_price().equals("0")) {
            rentAmount.setText("");
        } else {
            rentAmount.setText(PropertyModal.propertyModal.getProperty_rent_price() + " rs.");
        }

        if (PropertyModal.propertyModal.getProperty_advance_rentAmount().length() == 0
                || PropertyModal.propertyModal.getProperty_advance_rentAmount().equals("0")) {
            advanceAmount.setText("");
        } else {
            advanceAmount.setText(PropertyModal.propertyModal.getProperty_advance_rentAmount() + " rs.");
        }

        if (PropertyModal.propertyModal.getProperty_maintenance_rentAmount().length() == 0
                || PropertyModal.propertyModal.getProperty_maintenance_rentAmount().equals("0")) {
            maintenaceAmount.setText("");
        } else {
            maintenaceAmount.setText(PropertyModal.propertyModal.getProperty_maintenance_rentAmount() + " rs.");
        }

        if (PropertyModal.propertyModal.getProperty_water_bill_rentAmount().length() == 0
                || PropertyModal.propertyModal.getProperty_water_bill_rentAmount().equals("0")) {
            waterBillAmount.setText("");
        } else {
            waterBillAmount.setText(PropertyModal.propertyModal.getProperty_water_bill_rentAmount() + " rs.");
        }

        if (PropertyModal.propertyModal.getProperty_other_expance_rentAmount().length() == 0
                || PropertyModal.propertyModal.getProperty_other_expance_rentAmount().equals("0")) {
            expensessAmount.setText("");
        } else {
            expensessAmount.setText(PropertyModal.propertyModal.getProperty_other_expance_rentAmount() + " rs.");
        }

        if (PropertyModal.propertyModal.getProperty_Electricity_rentAmount().length() == 0
                || PropertyModal.propertyModal.getProperty_Electricity_rentAmount().equals("0")) {
            electriCityBill.setText("");
        } else {
            electriCityBill.setText(PropertyModal.propertyModal.getProperty_Electricity_rentAmount() + " rs.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_header_back:

                if (mSharedStorage.getUserType().equals("5")) {
                    Intent i = new Intent(HostelDetailActivity.this, Home_Accountantactivity.class);
                    startActivity(i);
                } else {

                    if (mSharedStorage.getUserType().equals("2")) {

                        String ppid = Utility.getSharedPreferences(HostelDetailActivity.this, "HideSearch");
                        if (!ppid.equalsIgnoreCase("")) {

                            HostelListActivity.searchlayout.setVisibility(View.GONE);
                            HostelListActivity.bottomLayout_panal.setVisibility(View.GONE);
                        }
                    }

                    Intent i = new Intent(HostelDetailActivity.this, HostelListActivity.class);
                    startActivity(i);
                }

                break;

            case R.id.propertyDescription_layout:
                if (propertyDescriptionTextLayout.getVisibility() == View.GONE) {
                    propertyDescriptionTextLayout.setVisibility(View.VISIBLE);
                    descriptionUpDownBtn.setBackgroundResource(R.drawable.up_caret);
                } else {
                    propertyDescriptionTextLayout.setVisibility(View.GONE);
                    descriptionUpDownBtn.setBackgroundResource(R.drawable.ic_down_caret);
                }
                break;
            case R.id.aminitiesTitleLayout:
                if (propertyAminitiesList.getVisibility() == View.GONE) {
                    propertyAminitiesList.setVisibility(View.VISIBLE);
                    aminitiesUpDownBtn.setBackgroundResource(R.drawable.up_caret);
                } else {
                    propertyAminitiesList.setVisibility(View.GONE);
                    aminitiesUpDownBtn.setBackgroundResource(R.drawable.ic_down_caret);
                }
                break;
            case R.id.freaturesTitleLayout:
                if (propertyFeatureList.getVisibility() == View.GONE) {
                    propertyFeatureList.setVisibility(View.VISIBLE);
                    featureUpDownBtn.setBackgroundResource(R.drawable.up_caret);
                } else {
                    propertyFeatureList.setVisibility(View.GONE);
                    featureUpDownBtn.setBackgroundResource(R.drawable.ic_down_caret);
                }
                break;
            case R.id.locationTitleLayout:
                if (propertyLocationMap.getVisibility() == View.GONE) {
                    propertyLocationMap.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.VISIBLE);
                    mapUpDownBtn.setBackgroundResource(R.drawable.up_caret);
                } else {
                    propertyLocationMap.setVisibility(View.GONE);
                    mMapView.setVisibility(View.GONE);
                    mapUpDownBtn.setBackgroundResource(R.drawable.ic_down_caret);
                }
                break;
            case R.id.bedSystemTitleLayout:
                if (bedSystemGrid.getVisibility() == View.GONE) {
                    bedSystemGrid.setVisibility(View.VISIBLE);
                    bedSelectionUpDown.setBackgroundResource(R.drawable.up_caret);
                } else {
                    bedSelectionUpDown.setBackgroundResource(R.drawable.ic_down_caret);
                    bedSystemGrid.setVisibility(View.GONE);
                }
                break;
            case R.id.propertyLocationMap:
                break;


            case R.id.hireDate:
                leaveDate.setText("");
                leaveDateCal = null;
                onCreateDialogTest(hireDate, "hire_date").show();

                break;
            case R.id.leaveDate:
                if (hireDateCal != null) {
                    onCreateDialogTest(leaveDate, "leave_date").show();
                } else {
                    mCommon.displayAlert(this, "Please select Hire date first.", false);
                }
                break;
            case R.id.shareBtn:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    }
                }

                propertyPhotoslist.stopAutoScroll();

                if (PropertyModal.propertyModal.getPropertyImage().size() != 0) {
                    ImageBitmapAsyncTask mImageBitmapAsyncTask = new ImageBitmapAsyncTask();
                    mImageBitmapAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            PropertyModal.propertyModal.getPropertyImage().get(0).getPhoto_Url());
                } else {
                    mCommon.displayAlert(HostelDetailActivity.this, "There is no any Image attached with this property.", false);
                }
                break;
            case R.id.propertyVideoBtn:

                VideoUrllink();
                break;
            case R.id.editPropertyBtn:
                Intent it = new Intent(this, PropertyInfoFirstActivity.class);
                it.putExtra("flag", "Edit");
                it.putExtra("property_id", mSharedStorage.getUserPropertyId());
                startActivity(it);
                break;
        }
    }

    public String getYoutubeVideoId(String youtubeUrl) {

        return youtubeUrl.substring((youtubeUrl.lastIndexOf("=") + 1), youtubeUrl.length());

    }

    public String getGoogleMapThumbnail(final double lati, final double longi) {

        String URL = "http://maps.google.com/maps/api/staticmap?"
                + "zoom=15&size=640x200&sensor=false&markers=size:large|color:red|" + lati + "," + longi;

        return URL;
    }

    public void setData() {

        hostelName.setText(PropertyModal.propertyModal.getProperty_name());
        property_description_text.setText(PropertyModal.propertyModal.getProperty_description());
        PropertyNameTitle.setText(PropertyModal.propertyModal.getProperty_name());
        DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        new GetAreaList(PropertyModal.propertyModal.getProperty_area()).execute();
        mDBAdapter.close();
        priceOfSelectedBed.setText("0");
        adapter.notifyDataSetChanged();
        titleIndicatorTop.invalidate();

        boolean flag = false;

        if (property_id.equals(mSharedStorage.getUserPropertyId())) {
            flag = true;
        } else {
            flag = false;
        }

        roomListAdapter = new BedSelectionGridAdapter(this, PropertyModal.propertyModal.getRoomList(), flag) {
            @Override
            public void uploadImageaAction(int index) {
                IntentCall(index);
            }
        };
        bedSystemGrid.setAdapter(roomListAdapter);


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                double lat = PropertyModal.propertyModal.getProperty_latlng().latitude;
                double log = PropertyModal.propertyModal.getProperty_latlng().longitude;
                mMap = googleMap;

                String nlat = String.valueOf(lat), nlong = String.valueOf(log);
                try {

                    DataBaseAdapter mDBAdapternew = new DataBaseAdapter(mActivity);
                    mDBAdapternew.createDatabase();
                    mDBAdapternew.open();

                    Cursor cursor = mDBAdapternew.getSqlqureies_new("Select *from city where city_id=" + PropertyModal.propertyModal.getProperty_city());

                    if (cursor.getCount() > 0) {

                        mapcityname = cursor.getString(cursor.getColumnIndex("city_name"));
                    } else {
                        mapcityname = "";
                    }

                    if (nlat.equals("") && nlong.equals("") || nlat.equals("0.0") && nlong.equals("0.0")) {

                    } else {
                        if (nlat.length() > 5) {
                            nlat.substring(0, 7);
                            nlong.substring(0, 7);

                            lat = Double.parseDouble(nlat);
                            log = Double.parseDouble(nlong);
                        }
                        LatLng sydney = new LatLng(lat, log);
                        mMap.addMarker(new MarkerOptions().position(sydney).title(mapcityname));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 12), 1000, null);
                        mMap.getUiSettings().setAllGesturesEnabled(false);
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                //  snapShot();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng arg0) {
                        Intent mIntentt = new Intent(HostelDetailActivity.this, PropertyMapActivity.class);
                        mIntentt.putExtra("lat", PropertyModal.propertyModal.getProperty_latlng().latitude);
                        mIntentt.putExtra("long", PropertyModal.propertyModal.getProperty_latlng().longitude);
                        mIntentt.putExtra("property_id_type", PropertyModal.propertyModal.getProperty_type_id());
                        mIntentt.putExtra("property_id", PropertyModal.propertyModal.getProperty_id());
                        mIntentt.putExtra("property_rent", PropertyModal.propertyModal.getProperty_rent_price());
                        startActivity(mIntentt);
                    }
                });
            }
        });


        try {

            ArrayList<FeatureListModal> featureListInner = new ArrayList<FeatureListModal>();

            if (!PropertyModal.propertyModal.getProperty_featuresStr().equals("") &&
                    !PropertyModal.propertyModal.getProperty_featuresStr().equals("Select Features")) {

                String data = PropertyModal.propertyModal.getProperty_featuresStr();
                String[] items = data.split(",");

                for (int j = 0; j < items.length; j++) {
                    for (int i = 0; i < featureList.size(); i++) {

                        if (featureList.get(i).getStrSateId().equals(items[j].trim())) {

                            FeatureListModal modal = new FeatureListModal();
                            for (int k = 0; k < featureListModalsComplete.size(); k++) {
                                if (featureList.get(i).getStrStateName().equals(featureListModalsComplete.get(k).getFeatureTitle())) {
                                    modal.setFeatureIcon(featureListModalsComplete.get(k).getFeatureIcon());
                                }
                            }
                            modal.setFeatureTitle(featureList.get(i).getStrStateName());
                            featureListInner.add(modal);
                        }
                    }
                }
                propertyFeatureList.setAdapter(new AminitiesAndFeaturesPagerAdapter(this, featureListInner));
                propertyFeatureList.startAutoScroll(10000);


            } else {
            }

            ArrayList<FeatureListModal> amenitiesListInner = new ArrayList<FeatureListModal>();
            if (!PropertyModal.propertyModal.getProperty_aminitiesStr().equals("") &&
                    !PropertyModal.propertyModal.getProperty_aminitiesStr().equals("Select Aminities")) {


                String data = PropertyModal.propertyModal.getProperty_aminitiesStr();
                String[] items = data.split(",");


                for (int i = 0; i < amenitiesList.size(); i++) {

                    for (int j = 0; j < items.length; j++) {

                        if (amenitiesList.get(i).getStrSateId().equals(items[j].trim())) {
                            FeatureListModal modal = new FeatureListModal();
                            for (int k = 0; k < featureListModalsComplete.size(); k++) {
                                if (amenitiesList.get(i).getStrStateName().equals(featureListModalsComplete.get(k).getFeatureTitle())) {
                                    modal.setFeatureIcon(featureListModalsComplete.get(k).getFeatureIcon());
                                }
                            }
                            modal.setFeatureTitle(amenitiesList.get(i).getStrStateName());
                            amenitiesListInner.add(modal);
                        }
                    }
                }
                propertyAminitiesList.setAdapter(new AminitiesAndFeaturesPagerAdapter(this, amenitiesListInner));
                propertyAminitiesList.startAutoScroll(10000);

            } else {

            }
        } catch (Exception e) {
        }
        setDescriptionValue();
    }

    ///------------------
    private void snapShot() {

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;


            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;

                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "map.png");
                    FileOutputStream fout = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
                    //   Toast.makeText (HostelDetailActivity.this, "Capture", Toast.LENGTH_SHORT).show ();
                    propertyLocationMap.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText (HostelDetailActivity.this, "Not Capture", Toast.LENGTH_SHORT).show ();
                }


            }
        };
        mMap.snapshot(callback);
    }

    //-------------

    public void IntentCall(int index) {
        finalbedid = PropertyModal.propertyModal.getRoomList().get(index).getRoomId();
        selectImage(finalbedid);

    }

    /*image capture code start*/
    public void selectImage(String index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HostelDetailActivity.this, R.style.MyDialogTheme);//R.style.MyDialogTheme
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(HostelDetailActivity.this, R.layout.bed_image_dialog_layout, R.id.txtview);//
        adapter.add("Take Picture");
        adapter.add("Choose from gallery");
        adapter.add("Cancel");


        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (UtilityImg.isExternalStorageAvailable()) {
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileURI());
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, 1);
                            }
                        } else {
                            Toast toast = Toast.makeText(HostelDetailActivity.this, "Please give internal storage permission", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        break;

                    case 1:
                        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent1, 2);
                        break;

                    case 2:
                        dialog.cancel();
                        // dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private Uri getPhotoFileURI() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssZ", Locale.ENGLISH);
        Date currentDate = new Date();
        String photoFileName = "photo.jpg";
        String fileName = simpleDateFormat.format(currentDate) + "_" + photoFileName;

        String APP_TAG = "ImageFolder";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uri = UtilityImg.getExternalFilesDirForVersion24Above(HostelDetailActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(HostelDetailActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setImage();
            }

            if (requestCode == 2) {
                Uri select = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(select, filepath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnindex = c.getColumnIndex(filepath[0]);
                String picturepath = c.getString(columnindex);
                ImagePath = picturepath;
                c.close();

                CropImage.activity(Uri.fromFile(new File(ImagePath)))
                        .start(this);

            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    if (resultUri != null) {


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        // ownerProfilePic.setImageBitmap(bitmap);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));

                        String profileImageUpload = String.valueOf(finalFile);
                        Common.profileImagePath = profileImageUpload;

                        if (Common.profileImagePath != null) {

                            uploadRoomImage(finalbedid);
                        } else {
                            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    //handle exception
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void setImage() {
        ImagePath = UtilityImg.getFile().getAbsolutePath();
        Log.d("string path of file", "" + ImagePath);
        CropImage.activity(Uri.fromFile(new File(ImagePath)))
                // .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    public void uploadRoomImage(String roomId) {
        UploadRoomImageServiceAsyncTask service = new UploadRoomImageServiceAsyncTask(this);
        service.setCallBackbed(this);
        service.execute(roomId, mSharedStorage.getUserPropertyId(), Common.profileImagePath);

    }

    @Override
    public void requestResponcebed(String result) {
        Common.Config("result    " + result);
        Common mCommon = new Common();
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    finish();

                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }


    //end


    public void getPropertyDetail() {

        if (!mValidation.checkNetworkRechability()) {
            mCommon.displayAlert(this, this.getResources().getString(R.string.str_no_network), true);
        } else {
            HostelDetailServiceAsyncTask propertyDetailService = new HostelDetailServiceAsyncTask(this);
            propertyDetailService.setCallBack(this);
            propertyDetailService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, property_id);
        }
    }


    @Override
    public void requestResponce(String result) {

        // pd.dismiss();
        if (result.trim().length() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);

                if (status.equals("true")) {
                    JSONArray jArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                    if (list != null) {
                        Room_rent = "0";
                        room_rent_perDay = 0;
                        Room_selected_id = "0";
                        list.clear();
                    }
                    PropertyModal.propertyModal.setProperty_id(jArray.getJSONObject(0).getString("property_id"));
                    PropertyModal.propertyModal.setProperty_name(jArray.getJSONObject(0).getString("property_name"));
                    PropertyModal.propertyModal.setProperty_address(jArray.getJSONObject(0).getString("owner_address"));
                    PropertyModal.propertyModal.setProperty_landmark(jArray.getJSONObject(0).getString("landmark"));
                    PropertyModal.propertyModal.setProperty_totalarea(jArray.getJSONObject(0).getString("total_area"));
                    PropertyModal.propertyModal.setProperty_face(jArray.getJSONObject(0).getString("property_face"));
                    PropertyModal.propertyModal.setProperty_Super_builtup_area(jArray.getJSONObject(0).getString("super_built_up_area"));
                    PropertyModal.propertyModal.setProperty_area(jArray.getJSONObject(0).getString("colony"));
                    PropertyModal.propertyModal.setProperty_city(jArray.getJSONObject(0).getString("city"));
                    PropertyModal.propertyModal.setProperty_state(jArray.getJSONObject(0).getString("state"));
                    String roomonovalue = jArray.getJSONObject(0).getString("no_of_room");
                    PropertyModal.propertyModal.setProperty_rent_price(jArray.getJSONObject(0).getString("rent_amount"));
                    PropertyModal.propertyModal.setNo_of_rooms(jArray.getJSONObject(0).getString("no_of_room"));
                    PropertyModal.propertyModal.setProperty_type_id(jArray.getJSONObject(0).getString("property_type"));
                    PropertyModal.propertyModal.setOwner_id(jArray.getJSONObject(0).getString("owner_id"));
                    PropertyModal.propertyModal.setUser_id(jArray.getJSONObject(0).getString("user_id"));
                    PropertyModal.propertyModal.setProperty_video_url(jArray.getJSONObject(0).getString("video_url"));
                    PropertyModal.propertyModal.setProperty_featuresStr(jArray.getJSONObject(0).getString("property_feature"));
                    PropertyModal.propertyModal.setProperty_aminitiesStr(jArray.getJSONObject(0).getString("other_aminities"));
                    PropertyModal.propertyModal.setProperty_pincode(jArray.getJSONObject(0).getString("pincode"));
                    PropertyModal.propertyModal.setProperty_for_whom(jArray.getJSONObject(0).getString("for_whom"));
                    PropertyModal.propertyModal.setProperty_distance_railway(jArray.getJSONObject(0).getString("railway_station"));
                    PropertyModal.propertyModal.setProperty_distance_busstand(jArray.getJSONObject(0).getString("bus_stand"));
                    PropertyModal.propertyModal.setProperty_distance_airport(jArray.getJSONObject(0).getString("airport"));
                    PropertyModal.propertyModal.setProperty_nearest_square(jArray.getJSONObject(0).getString("nearest"));
                    PropertyModal.propertyModal.setProperty_nearest_square_distance(jArray.getJSONObject(0).getString("square"));
                    PropertyModal.propertyModal.setProperty_tenant_type(jArray.getJSONObject(0).getString("tenant_type"));
                    PropertyModal.propertyModal.setProperty_curfew_time(jArray.getJSONObject(0).getString("curfew_time"));
                    PropertyModal.propertyModal.setProperty_is_allowed_smoking(jArray.getJSONObject(0).getString("smoking"));
                    PropertyModal.propertyModal.setProperty_is_allowed_drinking(jArray.getJSONObject(0).getString("drinking"));
                    PropertyModal.propertyModal.setProperty_no_of_tenant(jArray.getJSONObject(0).getString("no_of_people"));
                    PropertyModal.propertyModal.setProperty_advance_rentAmount(jArray.getJSONObject(0).getString("advance"));
                    PropertyModal.propertyModal.setProperty_maintenance_rentAmount(jArray.getJSONObject(0).getString("maintenance"));
                    PropertyModal.propertyModal.setProperty_water_bill_rentAmount(jArray.getJSONObject(0).getString("water_bill"));
                    PropertyModal.propertyModal.setProperty_other_expance_rentAmount(jArray.getJSONObject(0).getString("other_expenses"));
                    PropertyModal.propertyModal.setProperty_Electricity_rentAmount(jArray.getJSONObject(0).getString("electricity"));
                    PropertyModal.propertyModal.setProperty_Water_Supplay(jArray.getJSONObject(0).getString("water_supply"));
                    PropertyModal.propertyModal.setProperty_Power_Backup(jArray.getJSONObject(0).getString("power_backup"));
                    PropertyModal.propertyModal.setProperty_List(jArray.getJSONObject(0).getString("property_list"));
                    PropertyModal.propertyModal.setProperty_kitchen(jArray.getJSONObject(0).getString("kitchen"));
                    PropertyModal.propertyModal.setProperty_kitchen_type(jArray.getJSONObject(0).getString("kitchen_type"));
                    PropertyModal.propertyModal.setProperty_furnish(jArray.getJSONObject(0).getString("furnish_type"));
                    PropertyModal.propertyModal.setProperty_Description(jArray.getJSONObject(0).getString("description"));
                    PropertyModal.propertyModal.setProperty_allow_no_vehicle(jArray.getJSONObject(0).getString("vehicle"));
                    googleurl = jArray.getJSONObject(0).getString("google_url");

                    if (jArray.getJSONObject(0).has("property_images")) {
                        JSONArray propertyImageArray = jArray.getJSONObject(0).getJSONArray("property_images");

                        if (propertyImageArray.length() != 0) {

                            for (int i = 0; i < propertyImageArray.length(); i++) {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id(propertyImageArray.getJSONObject(i).getString("id"));
                                photoModal.setPhoto_Url(propertyImageArray.getJSONObject(i).getString("path"));
                                photoModal.setPhoto_from("server");
                                list.add(photoModal);
                            }

                        } else {
                            for (int i = 0; i < 3; i++) {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id("1");
                                photoModal.setPhoto_Url("Not Available");
                                list.add(photoModal);
                            }
                        }
                        PropertyModal.propertyModal.setPropertyImage(list);

                    } else {
                        PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                        photoModal.setPhoto_id("1");
                        photoModal.setPhoto_Url("Not Available");
                        list.add(photoModal);

                        PropertyModal.propertyModal.setPropertyImage(list);
                    }

                    double lat = 0.0;
                    double lng = 0.0;

                    if (jArray.getJSONObject(0).getString("lat").equals("") &&
                            jArray.getJSONObject(0).getString("long").equals("")) {
                        lat = 0.0;
                        lng = 0.0;
                    } else if (jArray.getJSONObject(0).getString("lat").equals("lat") &&
                            jArray.getJSONObject(0).getString("long").equals("long")) {
                        lat = 0.0;
                        lng = 0.0;
                    } else {
                        try {
                            lat = Double.parseDouble(jArray.getJSONObject(0).getString("lat"));
                            lng = Double.parseDouble(jArray.getJSONObject(0).getString("long"));
                        } catch (NumberFormatException e) {

                            lat = Double.parseDouble("");
                            lng = Double.parseDouble("");
                        }
                    }

                    PropertyModal.propertyModal.setProperty_latlng(new LatLng(lat, lng));

                    if (jArray.getJSONObject(0).has("room_info") && _isAddLocalData) {
                        JSONArray jArrayRooms = jArray.getJSONObject(0).getJSONArray("room_info");
                        if (jArrayRooms.length() > 0) {
                            _isAddLocalData = false;
                            DataBaseAdapter mDBAdapter = new DataBaseAdapter(this);
                            mDBAdapter.createDatabase();
                            mDBAdapter.open();
                            Cursor mCursor = mDBAdapter.getSqlqureies("Select *From add_rooms");
                            for (int i = 0; i < jArrayRooms.length(); i++) {
                                JSONObject jobjroom = jArrayRooms.getJSONObject(i);
                                ContentValues cv = new ContentValues();
                                cv.put("owner_id", mSharedStorage.getUserId());
                                cv.put("property_id", jobjroom.getString("property_id"));
                                cv.put("room_number", jobjroom.getString("room_no"));
                                cv.put("no_of_bads", jobjroom.getString("room_type"));
                                cv.put("vacant_bad", jobjroom.getString("vacant"));
                                cv.put("room_amunt", jobjroom.getString("amount"));
                                cv.put("room_lat_bath", jobjroom.getString("lat_bath"));
                                cv.put("room_ac", jobjroom.getString("ac"));
                                cv.put("room_balcony", jobjroom.getString("balcony"));
                                String imagePath = jobjroom.getString("image");
                                imagePath = imagePath.replaceAll(" ", "%20");
                                cv.put("room_image", imagePath);
                                Common.Config("room image   " + jobjroom.getString("image"));
                                cv.put("status", "1");

                                if (i == 0) {
                                    mDBAdapter.getSqlqureies("DELETE FROM add_rooms");
                                }
                                cv.put("room_id", jobjroom.getString("id"));
                                mDBAdapter.savedRoomsInfo("add_rooms", cv);
                                //}
                            }
                            mDBAdapter.close();
                        }
                        PropertyModal.propertyModal.setRoomList(getRoomFromLocal());
                    } else if (!jArray.getJSONObject(0).has("room_info")) {

                    }

                    setData();
                    room_rent_perDay = room_rent / 30;
                    calculateRent();
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                }
            } catch (JSONException e) {

            } catch (NullPointerException e) {

            }
        }
    }

    private ArrayList<RoomModal> getRoomFromLocal() {

        ArrayList<RoomModal> roomsArrays = new ArrayList();
        DataBaseAdapter mDBAdapter = new DataBaseAdapter(this);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        Cursor cursor = mDBAdapter.getSqlqureies("Select *from add_rooms");
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {
                    RoomModal modal = new RoomModal(Parcel.obtain());
                    modal.setRoomId(cursor.getString(cursor.getColumnIndex("room_id")));
                    modal.setRoomNo(cursor.getString(cursor.getColumnIndex("room_number")));
                    modal.setRoomType(cursor.getString(cursor.getColumnIndex("no_of_bads")));
                    modal.setVacantBed(cursor.getString(cursor.getColumnIndex("vacant_bad")));
                    modal.setBedPriceAmount(cursor.getString(cursor.getColumnIndex("room_amunt")));
                    modal.setRoomImage(cursor.getString(cursor.getColumnIndex("room_image")));
                    modal.setLat_bath(cursor.getString(cursor.getColumnIndex("room_lat_bath")));
                    modal.setRoomac(cursor.getString(cursor.getColumnIndex("room_ac")));
                    modal.setRoombalcony(cursor.getString(cursor.getColumnIndex("room_balcony")));
                    roomsArrays.add(modal);
                } while (cursor.moveToNext());
            }
        }
        mDBAdapter.close();

        return roomsArrays;
    }

    Calendar hireDateCal, leaveDateCal;

    protected Dialog onCreateDialogTest(final TextView v, final String type) {

        DatePickerDialog datePicker = new DatePickerDialog(this, R.style.datepickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (type.equals("hire_date")) {

                            hireDateCal = Calendar.getInstance();
                            hireDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            Calendar currentDateCal = Calendar.getInstance();
                            currentDateCal.set(year1, (month + 1), day - 1);
                            hiredatevaluenew = year + "/" + monthOfYear + "/" + dayOfMonth;
                            v.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                    .append(monthOfYear + 1).append("/").append(year));

                            /*if (getDateDiffStringBool(getDateFromCalender(currentDateCal), getDateFromCalender(hireDateCal))) {
                                hiredatevaluenew = year + "/" + monthOfYear + "/" + dayOfMonth;
                                v.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                            } else {
                                mCommon.displayAlert(HostelDetailActivity.this, "Hire date will not be less than Current date.", false);
                                v.setText("");
                                hireDateCal = null;
                            }*/

                        } else {

                            leaveDateCal = Calendar.getInstance();
                            leaveDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            if (getDateDiffStringBool(getDateFromCalender(hireDateCal), getDateFromCalender(leaveDateCal))) {
                                v.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                            } else {

                                mCommon.displayAlert(HostelDetailActivity.this, "Leave date will not be equal or less than Hire date.", false);
                                v.setText("");
                                leaveDateCal = null;
                            }

                        }

                        if (hireDateCal != null && leaveDateCal != null) {

                            if (getDateDiffStringBool(getDateFromCalender(hireDateCal), getDateFromCalender(leaveDateCal))) {
                                calculateRent();
                                getDateDiffString(getDateFromCalender(hireDateCal), getDateFromCalender(leaveDateCal));
                            }

                        }

                    }
                }, year1, month, day);

        return datePicker;

    }

    public void calculateRent() {
        double rentNew = 0;
        if (hireDateCal != null && leaveDateCal != null) {
            rentNew = (Common.daysBetween(hireDateCal, leaveDateCal) * room_rent_perDay);
        } else {
            rentNew = (30 * room_rent_perDay);
        }
        priceOfSelectedBed.setText(new DecimalFormat("##.##").format(rentNew));
    }

    public Date getDateFromCalender(Calendar cal) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(cal.getTime());
        Date date1 = null;
        try {
            date1 = df.parse(formattedDate);
        } catch (ParseException e) {
        }
        return date1;

    }

    public String getDateDiffString(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
        getDay(delta);
        if (delta > 0) {
            return "dateTwo is " + delta + " days after dateOne";
        } else {
            delta *= -1;
            return "dateTwo is " + delta + " days before dateOne";
        }
    }

    public boolean getDateDiffStringBool(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
        getDay(delta);
        if (delta > 0) {
            return true;
        } else {
            delta *= -1;
            return false;
        }
    }

    public void getDay(long delta) {
        int year = (int) delta / 365;
        int rest = (int) delta % 365;
        int month = rest / 30;
        rest = rest % 30;
        int weeks = rest / 7;
        int days = rest % 7;

        if (!leaveDate.getText().toString().equals("")) {

            if (year == 0) {
                if (month == 0) {
                    durationText.setText(rest + " Days");
                } else {
                    durationText.setText(month + " Months " + rest + " Days");
                }
            } else {
                durationText.setText(year + " Years " + month + " Months " + rest + " Days");
            }
        }
    }

    public void paymentResponce(String response) {

        if (response != null && response.trim().length() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    JSONArray jArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                    mSharedStorage.setAddCount("1");
                    mSharedStorage.setBookedPropertyId(property_id);
                    String room_id = "";
                    String transaction_id = jArray.getJSONObject(0).getString("transaction_id");
                    String keyproperty_id = jArray.getJSONObject(0).getString("key_property_information");
                    String amount = jArray.getJSONObject(0).getString("amount");
                    String paid = jArray.getJSONObject(0).getString("paid");
                    String payment_type = jArray.getJSONObject(0).getString("payment_type");
                    String comment = jArray.getJSONObject(0).getString("comment");
                    String remaining_amount = jArray.getJSONObject(0).getString("remaining_amount");
                    String room_no = jArray.getJSONObject(0).getString("room_no");
                    String payment_mode = "";

                    for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                        if (i == 0) {
                            room_id = Common.selectedBedInfo.get(i).getRoomId();

                        } else {
                            room_id = room_id + "," + Common.selectedBedInfo.get(i).getRoomId();
                        }
                    }

                    if (payment_type.equalsIgnoreCase("C")) {

                        payment_mode = "Cash";

                    } else if (payment_type.equalsIgnoreCase("Cheque")) {
                        payment_mode = "Cheque";

                    } else {

                        payment_mode = "Online";
                    }


                    if (fromroomchangevalue.equals("1")) {

                        String pid = HostelDetailActivity.this.property_id;
                        String newroomid = room_id;
                        String newtenantid = tenantid;
                        //updateroomid
                        updateRoomId(newroomid);

                    } else {
                        Successdialogslip(property_id, transaction_id, room_id, keyproperty_id, amount, paid, payment_mode, comment, remaining_amount, room_no);
                    }


                    //old working
                   /* Intent mIntent = new Intent(this, PropertyRCUActivity.class);
                    mIntent.putExtra("property_id", property_id);
                    mIntent.putExtra("transaction_id", transaction_id);
                    mIntent.putExtra("room_id",room_id);
                    startActivity(mIntent);
                    callSlider();
                    finish();*/
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            } catch (JSONException e) {
            } catch (NullPointerException e) {
            }
        }
    }

    public void callSlider() {

        mSharedStorage.setAddCount("1");
        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }

    public class ImageBitmapAsyncTask extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HostelDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                multipleShare();
            } catch (FileNotFoundException e) {
                HostelDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HostelDetailActivity.this, "Sorry, file not found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                HostelDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HostelDetailActivity.this, "Sorry, file not found", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return "";
        }

        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            pDialog.dismiss();
        }
    }

    public void multipleShare() throws Exception {
        String propertyType = "";
        String tenantType = "";
        for (int i = 0; i < getPropertyType().size(); i++) {

            if (getPropertyType().get(i).getStrSateId().equals(PropertyModal.propertyModal.getProperty_type_id())) {
                propertyType = getPropertyType().get(i).getStrStateName();
            }
        }

        for (int i = 0; i < getTenantType().size(); i++) {
            if (getTenantType().get(i).getStrSateId().equals(PropertyModal.propertyModal.property_tenant_type)) {
                tenantType = getTenantType().get(i).getStrStateName();
            }
        }

        String areaName = "";
        String cityName = "";
        DataBaseAdapter mDBAdapter = new DataBaseAdapter(this);
        mDBAdapter.createDatabase();
        mDBAdapter.open();

        if (PropertyModal.propertyModal.getProperty_area().equals("")) {
            areaName = "";
        } else {
            Cursor cursor = mDBAdapter.getSqlqureies_new("Select *from area where area_id=" + PropertyModal.propertyModal.getProperty_area());

            if (cursor.getCount() > 0) {
                areaName = cursor.getString(cursor.getColumnIndex("area_name")) + ",";
            } else {
                areaName = "";
            }
        }

        if (PropertyModal.propertyModal.getProperty_area().equals("")) {
            cityName = "";
        } else {
            Cursor cursor = mDBAdapter.getSqlqureies_new("Select *from city where city_id=" + PropertyModal.propertyModal.getProperty_city());

            if (cursor.getCount() > 0) {
                cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                mapcityname = cursor.getString(cursor.getColumnIndex("city_name"));
            } else {
                cityName = "";
                mapcityname = "";

            }
        }

        String RentAmount = PropertyModal.propertyModal.getProperty_rent_price();
        String template = "for rent in";
        String decscription = PropertyModal.propertyModal.getProperty_description();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Krooms Exclusive!!!");
        intent.putExtra(Intent.EXTRA_TEXT, propertyType + " (" + tenantType + ") Rs." + RentAmount + "/- " + template + " " + areaName + "" + cityName + "\n" + decscription);
        intent.setType("image/jpeg"); /*This example is sharing jpeg images.*/
        //ArrayList<Uri> files = new ArrayList<Uri>();
        //Log.e("file-", "" + files);

        ArrayList<Uri> files = new ArrayList<Uri>();
        for (PropertyPhotoModal path : PropertyModal.propertyModal.getPropertyImage()) {
            if (getBitmapFromURL(path.getPhoto_Url()) != null) {

                // Bitmap aa= getBitmapFromURL(path.getPhoto_Url());

                // String filename=SaveImage(aa);

                // String root = Environment.getExternalStorageDirectory().toString();
                //File myDir = new File(root + "/media");

               /* String url = MediaStore.Images.Media.insertImage(HostelDetailActivity.this.
                                getContentResolver(), getBitmapFromURL(path.getPhoto_Url()),
                        "title", "description");
             */  // url="content://media/external/images/media/14147";
                // String path1 = MediaStore.Images.Media.insertImage(HostelDetailActivity.this.getContentResolver(),aa, "ShareImage", null);
                //Log.e("path1-", path1);
               /* if(url != null){
                    Log.e("A10","Image saved!!");
                }else{
                    Log.e("A12","Image could not save to image gallery");
                }*/

                // Log.e("url-",""+String.valueOf(myDir));

               /* Uri uri = Uri.parse(url);
                files = new ArrayList<Uri>();
                Log.e("uri-",""+uri);
                files.add(uri);
                Log.e("final file",""+files);
*/

                // old working code
                String url = MediaStore.Images.Media.insertImage(HostelDetailActivity.this.getContentResolver(), getBitmapFromURL(path.getPhoto_Url()), "title", "description");
                Uri uri = Uri.parse(url);
                files.add(uri);
                if (files.isEmpty()) {
                    continue;
                }
            }
        }
        // Log.e("final outside",""+files);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(intent);
    }


    //only for testing


    private String SaveImage(Bitmap finalBitmap) {
        String result;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/KroomsImg");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            result = "saved";

        } catch (Exception e) {
            e.printStackTrace();
            result = "not saved";
        }
        return String.valueOf(file);
    }

    //


    public static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(WebUrls.IMG_URL + "" + src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            return decoded;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (IOException e) {
            return null;
        }

    }

    public ArrayList<StateModal> getTenantType() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Select Tenant");
        mStateModal.setStrSateId("0");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Boys");
        mStateModal.setStrSateId("1");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Girls");
        mStateModal.setStrSateId("2");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Family");
        mStateModal.setStrSateId("3");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Businessman");
        mStateModal.setStrSateId("4");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Employee");
        mStateModal.setStrSateId("5");
        mArray.add(mStateModal);
        return mArray;
    }


    private ArrayList<StateModal> getPropertyType() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Property Type");
        mStateModal.setStrSateId("0");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Hostel");
        mStateModal.setStrSateId("1");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Payin Guest");
        mStateModal.setStrSateId("2");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Service Apartment");
        mStateModal.setStrSateId("3");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Rooms");
        mStateModal.setStrSateId("4");
        mArray.add(mStateModal);
        mStateModal = new StateModal();
        mStateModal.setStrStateName("Studio Apartment");
        mStateModal.setStrSateId("5");
        mArray.add(mStateModal);

        return mArray;
    }

    public class BookRoomAgain extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;

            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "exittenant"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", tenantdatamodal.getUserId()));
            listNameValuePairs.add(new BasicNameValuePair("property_id", tenantdatamodal.getPropertyId()));
            listNameValuePairs.add(new BasicNameValuePair("room_id", tenantdatamodal.getBookedRoomId()));
            listNameValuePairs.add(new BasicNameValuePair("status", "0"));
            listNameValuePairs.add(new BasicNameValuePair("status_active", ""));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            String objectmain = result.replace("\t", "").replace("\n", "");
            System.out.print("" + result);
            listNameValuePairs.clear();
            return objectmain;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            String msg = null;
            String status = null;
            String image = null;
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Data not found ", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equals("Y")) {

                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class ActiveTenant extends AsyncTask<String, String, String> {

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

            mSharedStorage = SharedStorage.getInstance(HostelDetailActivity.this);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "updatetenantstatus"));
                nameValuePairs.add(new BasicNameValuePair("user_id", tenantdatamodal.getT_user_id()));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getBookedPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantdatamodal.getTenant_id()));
                nameValuePairs.add(new BasicNameValuePair("status", "0"));
                nameValuePairs.add(new BasicNameValuePair("status_active", ""));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
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
                startActivity(new Intent(HostelDetailActivity.this, HostelListActivity.class));
            } else if (result.equals("N")) {
            }
        }
    }

    public void VideoUrllink() {
        final Dialog video_dialog = new Dialog(HostelDetailActivity.this);
        video_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        video_dialog.setContentView(R.layout.videoshow_urldialog);
        image_cross_layout = (RelativeLayout) video_dialog.findViewById(R.id.image_cross_layout);
        youtubevideo = (RelativeLayout) video_dialog.findViewById(R.id.youtubevideo);
        googlevideo = (RelativeLayout) video_dialog.findViewById(R.id.googlevideo);
        image_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_dialog.dismiss();
            }
        });

        googlevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String googleurlvalue = googleurl;
                if (googleurlvalue.equals("")) {
                    Toast.makeText(HostelDetailActivity.this, "Google View is Not ADD ", Toast.LENGTH_SHORT).show();
                } else {
                    video_dialog.dismiss();
                    Intent i_view = new Intent(HostelDetailActivity.this, Googleview360_Owner.class);
                    i_view.putExtra("googleurl", googleurlvalue);
                    startActivity(i_view);
                }
            }
        });

        youtubevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PropertyModal.propertyModal.getProperty_video_url().equals("")) {
                    mCommon.displayAlert(HostelDetailActivity.this, "There is no any video attached with this property.", false);
                } else {

                    String videoUrl = getYoutubeVideoId(PropertyModal.propertyModal.getProperty_video_url());
                    String subString = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
                    String videoKey = "";

                    if (subString.contains("watch?v=")) {
                        videoKey = subString.substring(subString.lastIndexOf("watch?v=") + 1);
                    } else {
                        videoKey = "" + subString;

                    }

                    video_dialog.dismiss();
                    YoutubeViewActivity.VIDEO_ID = getYoutubeVideoId("https://www.youtube.com/watch?v=" + videoKey);
                    Intent mIntent1 = new Intent(HostelDetailActivity.this, YoutubeViewActivity.class);
                    startActivity(mIntent1);

                }

            }
        });

        video_dialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mSharedStorage.getUserType().equals("5")) {
                    startActivity(new Intent(HostelDetailActivity.this, Home_Accountantactivity.class));

                } else {

                    if (mSharedStorage.getUserType().equals("2")) {

                        String ppid = Utility.getSharedPreferences(HostelDetailActivity.this, "HideSearch");
                        if (!ppid.equalsIgnoreCase("")) {

                            HostelListActivity.searchlayout.setVisibility(View.GONE);
                            HostelListActivity.bottomLayout_panal.setVisibility(View.GONE);
                        }
                    }

                    startActivity(new Intent(HostelDetailActivity.this, HostelListActivity.class));

                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class GetAreaList extends AsyncTask<String, String, String> {
        String areanamevalue;
        String name, result, message, areaId;
        ProgressDialog pd;
        private boolean IsError = false;

        public GetAreaList(String areaId) {
            this.areaId = areaId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   pd = new ProgressDialog(mContext);
            //  pd.setMessage("Please wait..........");
            //  pd.show();

        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getAreaListByAreaId"));
                nameValuePairs.add(new BasicNameValuePair("area_id", areaId));
                //  nameValuePairs.add(new BasicNameValuePair("areaId",areaId));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    areanamevalue = jsonobj.getString("area_name");
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
            //  pd.dismiss();
            if (result.equalsIgnoreCase("Y")) {

                if (PropertyModal.propertyModal.getProperty_area().equals("")) {
                    area = "Area";
                    hostelAddress.setText(area);
                } else {
                    area = areanamevalue;
                    hostelAddress.setText(area);
                }
                // area = "Area";
            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(HostelDetailActivity.this, message, Toast.LENGTH_LONG).show();

            }
        }
    }


    //dailog pdf file download

    public void Successdialogslip(final String property_id, final String transaction_id, final String room_id, final String keyproperty_id, final String amount, final String paid, final String payment_type, final String comment, final String remaining_amount, final String room_no) {
        successdialog = new Dialog(HostelDetailActivity.this, R.style.AppTheme);
        successdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successdialog.setContentView(R.layout.success_dialogpayment_bedbooking);
        successdialog.setCanceledOnTouchOutside(false);
        final android.support.v7.widget.CardView card_view = (CardView) successdialog.findViewById(R.id.card_view);
        TextView userdate = (TextView) successdialog.findViewById(R.id.userdate);
        TextView userpid = (TextView) successdialog.findViewById(R.id.userpropertyid);
        TextView userroomno = (TextView) successdialog.findViewById(R.id.userroomno);
        TextView paymenttotal = (TextView) successdialog.findViewById(R.id.paymenttotal);
        TextView userpaid = (TextView) successdialog.findViewById(R.id.userpaid);
        TextView userdueamount = (TextView) successdialog.findViewById(R.id.userdueamount);
        TextView usermode = (TextView) successdialog.findViewById(R.id.usermode);
        TextView propertyname = (TextView) successdialog.findViewById(R.id.propertyname);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");

        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        final String currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HostelDetailActivity.this, PDFFileNewPage_owner_bedbooking.class);
                i.putExtra("date", currentdate_value);
                i.putExtra("propertyid", keyproperty_id);
                i.putExtra("roomno", room_no);
                i.putExtra("totalamount", amount);
                i.putExtra("paid", paid);
                i.putExtra("due", remaining_amount);
                i.putExtra("mode", payment_type);
                i.putExtra("Chequeno", comment);
                i.putExtra("PropertyIdMain", property_id);
                i.putExtra("transaction_id", transaction_id);
                i.putExtra("room_id", room_id);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                i.putExtra("Value", fromroomchangevalue);
                i.putExtra("oldroomid", oldroomid);
                startActivity(i);
                successdialog.dismiss();

            }
        });
        userdate.setText(currentdate_value);
        userpid.setText(keyproperty_id);
        paymenttotal.setText(amount);
        userpaid.setText(paid);
        usermode.setText(payment_type);
        userroomno.setText(room_no);
        userdueamount.setText(remaining_amount);
        chequeno.setText(comment);
        propertyname.setText(mSharedStorage.getPropertyname());

        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fromroomchangevalue.equals("1")) {

                    String pid = HostelDetailActivity.this.property_id;
                    String newroomid = room_id;
                    String newtenantid = tenantid;
                    //updateroomid
                    updateRoomId(newroomid);

                } else {
                    Intent mIntent = new Intent(HostelDetailActivity.this, PropertyRCUActivity.class);
                    mIntent.putExtra("property_id", HostelDetailActivity.this.property_id);
                    mIntent.putExtra("transaction_id", transaction_id);
                    mIntent.putExtra("room_id", room_id);
                    startActivity(mIntent);
                    callSlider();
                    finish();

                }


                successdialog.dismiss();
            }
        });

        successdialog.show();
    }

    //vacant room

    private void updateRoomId(String newroomid) {

        final ProgressDialog dialog = new ProgressDialog(HostelDetailActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "updateroomid");
            params.put("property_id", property_id);
            params.put("tenant_id", tenantid);
            params.put("oldroom_id", oldroomid);
            params.put("newroomid", newroomid);
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
                //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                deleteOldRecord();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //

    //Delete old record


    private void deleteOldRecord() {

        final ProgressDialog dialog = new ProgressDialog(HostelDetailActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "deleteoldrecordofuser");
            params.put("property_id", property_id);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataoldrecord(result);
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

    public void getResponseDataoldrecord(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {

                Toast.makeText(this, "Room Change Successfully", Toast.LENGTH_SHORT).show();

                Intent mIntent = new Intent(HostelDetailActivity.this, HostelListActivity.class);
                //mIntent.putExtra("tenantid",tenantid);
                startActivity(mIntent);
                callSlider();
                finish();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //

    private Bundle onSaveInstanceState() {
        return null;
    }


    //new payment dailog on click book bed changes on 18/2/2019 by ashish

    public class BedBookingPayment extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;

        //private FragmentActivity mFActivity = null;

        private HostelDetailActivity mFActivity = null;
        private EditText totalAmount, totalAmount_paid;
        private TextView termAndCondition;
        private Button bookProperty;
        private RadioButton caseOption, onlineOption, chequeOption;
        private CheckBox checkeBox;
        TextView paidamounttext;
        SharedStorage mSharedStorage;
        String paymentmode = "";
        public String paidvalue, paymenttype, statusvalue;
        String paymentOption = "";
        PropertyModal mPropertyModal;
        LinearLayout layout_cheque_details;
        public String mHireDate = " ", mLeaveDate = " ", rentAmount = "";

        private ImageButton main_header_menu;
        public String propertyidmain = "";
        Integer randomNum;

        public String fromroomchnage = "";

        private Common mCommon;
        public String roomSelectedBed = "";
        public String roomIds = "";
        String advanceamount;
        EditText txtchequeno;
        String chequenovalue = "";
        RelativeLayout mainlayout;


        public BedBookingPayment(FragmentActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            this.c = a;
        }


        public void BedBookingPayment(HostelDetailActivity hostelDetailActivity, PropertyModal propertyModal, String hireDate, String leaveDate, String rent, String advanceamount, String fromroomchangevalue) {

            mFActivity = hostelDetailActivity;
            mPropertyModal = propertyModal;
            mSharedStorage = SharedStorage.getInstance(mFActivity);
            mHireDate = hireDate;
            mLeaveDate = leaveDate;
            rentAmount = advanceamount;
            advanceamount = rent;
            fromroomchnage = fromroomchangevalue;
            randomNum = ServiceUtility.randInt(0, 9999999);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.payment_selection_dialog);
            //c.setTheme(R.style.full_screen_dialog);
            //c.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            createView();
            // setListner();


        }

        public void createView() {

            mCommon = new Common();
            totalAmount = (EditText) findViewById(R.id.totalAmount_output);
            paidamounttext = (TextView) findViewById(R.id.paidamounttext);
            termAndCondition = (TextView) findViewById(R.id.termsAndConditionTextView);
            bookProperty = (Button) findViewById(R.id.submitBtn);
            bookProperty.setClickable(true);
            caseOption = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
            onlineOption = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
            chequeOption = (RadioButton) findViewById(R.id.chequePaymentRadioBtn);
            checkeBox = (CheckBox) findViewById(R.id.checkeBox);
            main_header_menu = (ImageButton) findViewById(R.id.main_header_menu);
            totalAmount_paid = (EditText) findViewById(R.id.totalAmount_paid);
            layout_cheque_details = (LinearLayout) findViewById(R.id.cheque_details);
            txtchequeno = (EditText) findViewById(R.id.txt_chequeno);
            mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
            totalAmount.setText(rentAmount);

            if (mSharedStorage.getUserType().equalsIgnoreCase("2")) {
                layout_cheque_details.setVisibility(View.GONE);
            } else if (mSharedStorage.getUserType().equalsIgnoreCase("4")) {
                layout_cheque_details.setVisibility(View.GONE);
            } else {
                layout_cheque_details.setVisibility(View.VISIBLE);
            }
            //layout_cheque_details.setVisibility(View.GONE);

            totalAmount.setBackgroundColor(mFActivity.getResources().getColor(R.color.white));
            RadioGroup group = (RadioGroup) findViewById(R.id.paymentRadioGroup);

            String usertype = mSharedStorage.getUserType();
            if (usertype.equalsIgnoreCase("2")) {
                caseOption.setVisibility(View.GONE);
                chequeOption.setVisibility(View.GONE);
            }


            if (usertype.equalsIgnoreCase("4")) {
                caseOption.setVisibility(View.GONE);
                chequeOption.setVisibility(View.GONE);
            }


            if (fromroomchnage.equals("1")) {

                mainlayout.setVisibility(View.GONE);
                txtchequeno.setText("Room Change");
                checkeBox.setChecked(true);
                caseOption.setChecked(true);
                totalAmount_paid.setText("1");

                String paidamount = "1", chequenovalue = "Room Change";
                paidvalue = paidamount;
                paymentmode = "C";

                propertyidmain = mPropertyModal.getProperty_id();


                PaymentOptionThroughCase service = new PaymentOptionThroughCase(mFActivity);
                service.setCallBack(new ServiceResponce() {
                    @Override
                    public void requestResponce(String result) {
                        // paymentSelectionBtn(result);
                        bookProperty.setClickable(true);
                        paymentResponce(result);
                        dismiss();
                    }
                });

                int noOfBed = 1;

                for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                    if (i == 0) {
                        roomIds = Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = "1";
                    } else {
                        roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = roomSelectedBed + "," + 1;
                    }
                }


                int rentamountvalue = Integer.parseInt(rentAmount);
                int sizemain = Common.selectedBedInfo.size();
                int rentamountvaluenew = rentamountvalue / sizemain;
                String renatmainvalue = String.valueOf(rentamountvaluenew);

                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mPropertyModal.getProperty_id(),
                        roomIds, renatmainvalue, mHireDate, mLeaveDate, roomSelectedBed, paidvalue, "C", "PENDING", chequenovalue);

            }


            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // TODO Auto-generated method stub
                    if (caseOption.isChecked()) {
                        // layout_cheque_details.setVisibility(View.GONE);
                        Common.Config("case option selected   ");
                        totalAmount.setFocusable(false);
                        totalAmount.setFocusableInTouchMode(false);
                        totalAmount.setClickable(false);
                        totalAmount.setBackgroundColor(mFActivity.getResources().getColor(R.color.white));
                        totalAmount.setText(rentAmount);
                        totalAmount_paid.setVisibility(View.VISIBLE);
                        paidamounttext.setVisibility(View.VISIBLE);

                    } else if (onlineOption.isChecked()) {
                        // layout_cheque_details.setVisibility(View.GONE);
                        Common.Config("online option selected     ");
                        totalAmount.setFocusable(true);
                        totalAmount.setFocusableInTouchMode(true);
                        totalAmount.setBackgroundResource(R.drawable.input_text_background);
                        totalAmount.setClickable(true);
                        totalAmount_paid.setVisibility(View.INVISIBLE);
                        paidamounttext.setVisibility(View.INVISIBLE);
                    } else if (chequeOption.isChecked()) {
                        //layout_cheque_details.setVisibility(View.VISIBLE);
                        totalAmount.setFocusable(false);
                        totalAmount.setFocusableInTouchMode(false);
                        totalAmount.setClickable(false);
                        totalAmount.setBackgroundColor(mFActivity.getResources().getColor(R.color.white));
                        totalAmount.setText(rentAmount);
                        totalAmount_paid.setVisibility(View.VISIBLE);
                        paidamounttext.setVisibility(View.VISIBLE);

                    }
                }
            });


            setViewListners();

        }


        public void setViewListners() {

            termAndCondition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TermAndConditionDialog dialog = new TermAndConditionDialog() {
                        @Override
                        public void agreeBtnClicked() {
                            checkeBox.setChecked(true);
                        }
                    };
                    dialog.getPerameter(mFActivity, false);
                    dialog.show(mFActivity.getSupportFragmentManager(), "termDialog");
                }
            });

            bookProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    paymentOption = caseOption.isChecked() ? "cash" : onlineOption.isChecked() ? "online" : chequeOption.isChecked() ? "cheque" : "";
                    paidvalue = totalAmount_paid.getText().toString();

                    if (paymentOption.equals("")) {
                        Toast.makeText(mFActivity, "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                    } else if (checkeBox.isChecked()) {

                        if (paymentOption.equalsIgnoreCase("cash")) {

                            chequenovalue = txtchequeno.getText().toString().trim();

                            if (paidvalue.equals("")) {
                                Toast.makeText(mFActivity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                            } else if (paidvalue.startsWith("0")) {

                                Toast.makeText(mFActivity, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                            } else {
                                bookProperty.setClickable(false);
                                paidvalue = totalAmount_paid.getText().toString();
                                paymentmode = "C";
                                paymentThroughCash(chequenovalue);
                            }

                        } else if (paymentOption.equalsIgnoreCase("online")) {
                            chequenovalue = txtchequeno.getText().toString().trim();
                            paidvalue = totalAmount.getText().toString();
                            int mainamount = Integer.parseInt(paidvalue);

                            //by me only testing
                            if (mainamount > 1500) {//1500
                                bookProperty.setClickable(false);
                                paymentmode = "O";
                                paymentThroughCash(chequenovalue);
                            } else {
                                Toast.makeText(mFActivity, "Please Enter Amount Above 1500", Toast.LENGTH_SHORT).show();
                            }
                        /*paymentmode="O";
                        paymentThroughCash(chequenovalue);*/

                        } else if (paymentOption.equalsIgnoreCase("cheque")) {

                            chequenovalue = txtchequeno.getText().toString().trim();

                            if (paidvalue.equals("")) {
                                Toast.makeText(mFActivity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                            } else if (paidvalue.startsWith("0")) {

                                Toast.makeText(mFActivity, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                            } else {

                                if (paidvalue.equals("")) {
                                    Toast.makeText(mFActivity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                                } else if (paidvalue.startsWith("0")) {

                                    Toast.makeText(mFActivity, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                                } else {
                                    bookProperty.setClickable(false);
                                    paidvalue = totalAmount_paid.getText().toString();
                                    paymentmode = "Cheque";
                                    paymentThroughCash(chequenovalue);
                                }
                            }
                        }
                    } else {
                        mCommon.displayAlert(mFActivity, "In order to use our services, you must agree to Terms and Condition.", false);
                    }

                }
            });

            main_header_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }


        public void paymentThroughCash(String chequenovalue) {

            propertyidmain = mPropertyModal.getProperty_id();
            if (paymentOption.equals("cash")) {

                PaymentOptionThroughCase service = new PaymentOptionThroughCase(mFActivity);
                service.setCallBack(new ServiceResponce() {
                    @Override
                    public void requestResponce(String result) {
                        // paymentSelectionBtn(result);
                        paymentResponce(result);
                        dismiss();
                    }
                });

                int noOfBed = 1;

                for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                    if (i == 0) {
                        roomIds = Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = "1";
                    } else {
                        roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = roomSelectedBed + "," + 1;
                    }
                }


                int rentamountvalue = Integer.parseInt(rentAmount);
                int sizemain = Common.selectedBedInfo.size();
                int rentamountvaluenew = rentamountvalue / sizemain;
                String renatmainvalue = String.valueOf(rentamountvaluenew);

                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mPropertyModal.getProperty_id(),
                    /*Common.selectedBedInfo.get(0).getRoomId()*/roomIds, renatmainvalue, mHireDate, mLeaveDate,/*""+noOfBed*/roomSelectedBed, paidvalue, "C", "PENDING", chequenovalue);

            } else if (paymentOption.equals("cheque")) {

                PaymentOptionThroughCase service = new PaymentOptionThroughCase(mFActivity);
                service.setCallBack(new ServiceResponce() {
                    @Override
                    public void requestResponce(String result) {
                        paymentResponce(result);
                        dismiss();
                    }
                });

                int noOfBed = 1;

                for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                    if (i == 0) {
                        roomIds = Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = "1";
                    } else {
                        roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = roomSelectedBed + "," + 1;
                    }
                }


                int rentamountvalue = Integer.parseInt(rentAmount);
                int sizemain = Common.selectedBedInfo.size();
                int rentamountvaluenew = rentamountvalue / sizemain;
                String renatmainvalue = String.valueOf(rentamountvaluenew);

                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mPropertyModal.getProperty_id(),
                        roomIds, renatmainvalue, mHireDate, mLeaveDate, roomSelectedBed, paidvalue, "Cheque", "PENDING", chequenovalue);

            } else {


                int noOfBed = 1;
                for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                    if (i == 0) {
                        roomIds = Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = "1";
                    } else {
                        roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                        roomSelectedBed = roomSelectedBed + "," + 1;
                    }
                }


                int rentamountvalue = Integer.parseInt(rentAmount);
                int sizemain = Common.selectedBedInfo.size();
                int rentamountvaluenew = rentamountvalue / sizemain;
                String renatmainvalue = String.valueOf(rentamountvaluenew);

                if (totalAmount.getText().toString().trim().length() > 0) {
                    double amount = Double.parseDouble(totalAmount.getText().toString());
                    if (amount <= Double.parseDouble(rentAmount)) {
                        if (amount >= 1500) {

                            if (mLeaveDate.length() == 0 || mLeaveDate.equals("")) {

                                mLeaveDate = "00/00/0000";
                            }

                            if (chequenovalue.equals("") || chequenovalue.length() == 0) {

                                chequenovalue = "test";
                            }
                            Intent intent = new Intent(mFActivity, BillingShippingActivity.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                            intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                            intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                            intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                            intent.putExtra(AvenuesParams.AMOUNT, amount + "");
                            intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                            intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                            intent.putExtra(AvenuesParams.USER_ID, mSharedStorage.getUserId());
                            intent.putExtra(AvenuesParams.PROPERTY_ID, mPropertyModal.getProperty_id());
                            intent.putExtra(AvenuesParams.ROOM_ID, roomIds);
                            intent.putExtra(AvenuesParams.ROOM_AMOUNT, renatmainvalue);
                            intent.putExtra(AvenuesParams.HIRING_DATE, mHireDate);
                            intent.putExtra(AvenuesParams.LEAVING_DATE, mLeaveDate);
                            intent.putExtra(AvenuesParams.BOOKED_BED, "" + roomSelectedBed);
                            intent.putExtra(AvenuesParams.comment, "" + chequenovalue);//for comment entry
                            startActivity(intent);
                        } else {
                            mCommon.displayAlert(mFActivity, "Amount should be greater or equal then Minimum Amount (1500). ", false);
                        }
                    } else {
                        mCommon.displayAlert(mFActivity, "Amount should be less or equal then Total Amount (" + rentAmount + ") ", false);
                    }
                } else {
                    mCommon.displayAlert(mFActivity, "Please enter correct amount", false);
                    totalAmount.setText(rentAmount);
                }

            }

        }


        public void setListner() {

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                default:
                    break;
            }
        }
    }


    //


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


        public CustomDialogClass(HostelDetailActivity a, int full_screen_dialog) {
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
                    Intent it = new Intent(HostelDetailActivity.this, LoginActivity.class);
                    startActivity(it);
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    break;
            }
            dismiss();
        }

    }

}