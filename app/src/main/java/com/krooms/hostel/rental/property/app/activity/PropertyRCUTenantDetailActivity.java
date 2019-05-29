package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.App;
import com.krooms.hostel.rental.property.app.Utility.ScalingUtilities;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.asynctask.RCUTenantDetailAsynctask;
import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.MonthModalTenant;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.database.GetStatesAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.modal.MonthModal;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PropertyRCUTenantDetailActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceResponce, StateDataBaseResponce/*, CityDataBaseResponce, AreaDataBaseResponce*/ {

    String alreadyno,alreadyemail;
    String result1="";
    ProgressDialog dialog,dialogbooking;
    String resultmain=null;
    private EditText edtFirstName = null;
    private EditText edtLastName = null;
    private EditText edtFatherName = null;
    private EditText edtFatherContactno = null;
    private EditText edtFlatPlotno = null;
    private EditText edtLandMark = null;
    private EditText edtLocation = null;
    private EditText edtPinCode = null;
    String message="";
    String roomidnewvalue="";
    private EditText edtContactno = null;
    private EditText edtEmail = null;
    private TextView txtHireDate, txtLeaveDate;
    private TextView txtRoom_no = null;
    private ImageView tenantProfilePic;
    private EditText spinnerCity;
    private Spinner spinnerNoofCotenant, spinnerState;
    private String imgTenanto = null;
    private String imgTenantoDisplay = null;
    private ProgressBar mprogressBar;
    private Common mCommon;
    private Validation mValidation;
    private Button btnSubmit, btnBrowseImage;
    private TextView txtBack;
    private LinearLayout mLayoutTenant, mLayoutTenantView;
    private Button btnTenant1, btnTenant2, btnTenant3, btnTenant4;
    String room_new_value;
    String room_id = "";
    SharedStorage msharedobj;
    String suserid=null;
    String firstName,lastName,fatherName,fatherContactno,flatPlotno,landMark,pinCode,contactNo,email,hireDate,leaveDate,areaId,cityId,property_id,owner_id;
    String transaction_id;

    String monthnamemultiple,monthnamemultipleid;
    private ArrayList<StateModal> mArrayStates = null;
    /*private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = null;*/
    private ArrayList<StateModal> mArrayNoOfContent = null;
    private StateSpinnerAdapter mStateSpinnerAdapter/*, mCitySpinnerAdapter, mAreaSpinnerAdapter*/, mPropertyTypeAdapter;
    private String country = "India", countryId = "1", state, stateId = "0", /*city,
            cityId = "0", area = "", areaId = "0",*/
            noOfContent = "", tenantId = "1";
    private int year1, month, day;

    private boolean isUpdate = false;
    private String rcuId;
    private String[] rcuids;
    String property_id_main_jsop,room_id_main_json;
    String userid_main_json,ownerid_main_json;
    String  adavnce;
    String tenantroomamount;
    TextView textview_leave_date,textview_hire_date;
    String textview_hire_date_main_value="",textview_leave_date_main_value="";
    ArrayList<MonthModal> monthModalwhole_arraylist;
    MonthModal monthModalwhole;
    PropertyUserModal modal;
    ArrayList<MonthModalTenant> monthModalTenant;
    MonthModalTenant monthModalTenant1;
    String date1new="";
    String finalresultvalue="false";
    String mainuservaluemobile,useridemailvalue;
    String usertypevalue;
    Uri uri;
    String ImagePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_rcu_tenant_detail);
        imgTenanto = null;
        msharedobj=SharedStorage.getInstance(this);
        date1new = msharedobj.getHireDate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
        }

        rcuids = getIntent().getStringExtra("RCUId").split(",");
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        mainuservaluemobile=msharedobj.getUserMobileNumber();
        useridemailvalue=msharedobj.getUserEmail();
        if(msharedobj.getUserType().equals("4"))
        {
            usertypevalue =msharedobj.getParent_Id();
        }else
        {
            usertypevalue ="0";
        }

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edtFirstName = (EditText) findViewById(R.id.edittext_first_name);
        edtFirstName.setSelection(0);

        textview_leave_date=(TextView)findViewById(R.id.textview_leave_date);
        textview_hire_date=(TextView)findViewById(R.id.textview_hire_date);

        isUpdate = getIntent().getExtras().getBoolean("isUpdate", false);
        String roomId = getIntent().getExtras().getString("room_id");
        String[] roomIds = roomId.split(",");

        //value get by prpertyRcuActivity through intent
        Intent in=getIntent();
        property_id_main_jsop=in.getStringExtra("property_id");
        room_id_main_json=in.getStringExtra("room_id");
        ownerid_main_json=in.getStringExtra("owner_id");
        Log.d("adv ", "room id mainusab =" +ownerid_main_json+" ====room id new =   "+room_new_value);


        userid_main_json=msharedobj.getUserId();


        // data insert into month modal
        //..............

        createViews(savedInstanceState);

        if (HostelDetailActivity.tenantdatamodal!=null)
        {
            modal=HostelDetailActivity.tenantdatamodal;
            String statusvalue=modal.getStatus_active();
            String bookroomid=modal.getBookedRoomId();
            Log.d("new room id ",""+bookroomid);
            msharedobj.setAddCount("0");
            setData();
        }

        if (savedInstanceState == null) {
            setListeners();
            checkEditOrAdd();
        } else {
            setDataOnSavedState(savedInstanceState);

        }
    }

    private void createViews(Bundle savedBundle) {

        edtLastName = (EditText) findViewById(R.id.edittext_last_name);
        edtFatherName = (EditText) findViewById(R.id.edittext_father_name);
        edtFatherContactno = (EditText) findViewById(R.id.edittext_father_contact_no);
        edtFlatPlotno = (EditText) findViewById(R.id.edittext_flat_plot_no);
        edtLandMark = (EditText) findViewById(R.id.edittext_landmark);
        edtPinCode = (EditText) findViewById(R.id.edittext_pincode);
        edtContactno = (EditText) findViewById(R.id.edittext_contact_no);
        edtEmail = (EditText) findViewById(R.id.edittext_email);
        txtRoom_no = (TextView) findViewById(R.id.text_RoomNo);
        txtHireDate = (TextView) findViewById(R.id.textview_hire_date);
        txtLeaveDate = (TextView) findViewById(R.id.textview_leave_date);
        btnBrowseImage = (Button) findViewById(R.id.button_browse);
        spinnerNoofCotenant = (Spinner) findViewById(R.id.spinner_no_of_cotenant);
        spinnerState = (Spinner) findViewById(R.id.spinner_state);
        spinnerCity = (EditText) findViewById(R.id.spinner_city);
        edtLocation = (EditText) findViewById(R.id.edittext_location);
        tenantProfilePic = (ImageView) findViewById(R.id.tenantProfilePic);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);

        txtBack = (TextView) findViewById(R.id.textView_title);
        btnSubmit = (Button) findViewById(R.id.button_next);
        btnSubmit.setClickable(true);

        mLayoutTenant = (LinearLayout) findViewById(R.id.layout_tent_button);
        mLayoutTenant.setVisibility(View.GONE);
        mLayoutTenantView = (LinearLayout) findViewById(R.id.layout_tent_view);
        mLayoutTenantView.setVisibility(View.GONE);

        btnTenant1 = (Button) findViewById(R.id.button_tenant_1);
        btnTenant2 = (Button) findViewById(R.id.button_tenant_2);
        btnTenant3 = (Button) findViewById(R.id.button_tenant_3);
        btnTenant4 = (Button) findViewById(R.id.button_tenant_4);
        Date cal = (Date) Calendar.getInstance().getTime();
        year1 = cal.getYear() + 1900;
        month = cal.getMonth();
        day = cal.getDate();

        if (savedBundle == null) {
            int position = Integer.parseInt(tenantId) - 1;
            if (!isUpdate) {
                if (Common.selectedBedInfo.get(position) != null) {
                    txtRoom_no.setText("Room no. " + Common.selectedBedInfo.get(position).getRoomNo());
                }
            } else {

                txtRoom_no.setText("Room no. " + Common.rcuDetails.get(position).getTenant_room_no());

            }
        }
    }
    public void setData() {
        edtFirstName.setText(modal.getTenant_fname());
        edtFlatPlotno.setText(modal.getTenant_permanent_address());
        edtContactno.setText(modal.getContact_number());
        edtEmail.setText(modal.getEmail_id());
        edtFatherName.setText(modal.getTenant_father_name());
        edtFatherContactno.setText(modal.getTenant_father_contact_no());
        spinnerCity.setText(modal.getCity());
        edtLocation.setText(modal.getLocation());
    }

    public void resetAllData() {

        Common.Config("tenantId" + tenantId);
        try {
            if(isUpdate) {

                int position = Integer.parseInt(tenantId) - 1;
                edtFirstName.setText(Common.rcuDetails.get(position).getTenant_fname());
                edtLastName.setText(Common.rcuDetails.get(position).getTenant_lname());
                edtFatherName.setText(Common.rcuDetails.get(position).getTenant_father_name());
                edtFatherContactno.setText(Common.rcuDetails.get(position).getTenant_father_contact_no());
                edtFlatPlotno.setText(Common.rcuDetails.get(position).getFlat_number());
                edtLandMark.setText(Common.rcuDetails.get(position).getLandmark());
                edtPinCode.setText(Common.rcuDetails.get(position).getPincode());
                edtContactno.setText(Common.rcuDetails.get(position).getContact_number());
                edtEmail.setText(Common.rcuDetails.get(position).getEmail_id());
                alreadyno=Common.rcuDetails.get(position).getContact_number();
                alreadyemail=Common.rcuDetails.get(position).getEmail_id();

                if(alreadyemail.equals("") || alreadyemail ==null)
                {
                    edtEmail.setEnabled(true);
                }else
                {
                    edtEmail.setEnabled(false);
                }

                if(alreadyno.equals("") || alreadyno==null)
                {
                    edtContactno.setEnabled(true);
                }else
                {
                    edtContactno.setEnabled(false);
                }
                txtHireDate.setText(Common.rcuDetails.get(position).getProperty_hire_date());
                txtLeaveDate.setText(Common.rcuDetails.get(position).getProperty_leave_date());
                stateId = Common.rcuDetails.get(position).getState();
                spinnerCity.setText(Common.rcuDetails.get(position).getCity());
//            cityId = Common.rcuDetails.get(position).getCity();
                edtLocation.setText(Common.rcuDetails.get(position).getLocation());
//            areaId = Common.rcuDetails.get(position).getLocation();
                imgTenanto = Common.rcuDetails.get(position).getTenant_photo();
                Common.Config("image name   " + imgTenanto);
                if (imgTenanto.trim().equals("")) {
                    btnBrowseImage.setText("Browse");
                } else {
                    btnBrowseImage.setText("" + imgTenanto);
                }
                if (!imgTenanto.trim().equals("")) {
                    tenantProfilePic.setVisibility(View.VISIBLE);
                    imgTenantoDisplay = WebUrls.IMG_URL + Common.rcuDetails.get(position).getTenant_photo();
                    txtRoom_no.setText("Room no. " + Common.rcuDetails.get(position).getTenant_room_no());
                    Picasso.with(this)
                            .load(imgTenantoDisplay)
                            .placeholder(R.drawable.ic_default_background)
                            .error(R.drawable.ic_default_background)
                            .into(tenantProfilePic);
                } else {
                    tenantProfilePic.setVisibility(View.GONE);
                }
                changeStateCityArea();
            } else {

                Common.Config("tenantId" + tenantId + " rcuDetailSize   " + Common.rcuDetails.size());
                int position = Integer.parseInt(tenantId) - 1;
                if (Common.rcuDetails.size() > position) {
                    edtFirstName.setText(Common.rcuDetails.get(position).getTenant_fname());
                    edtLastName.setText(Common.rcuDetails.get(position).getTenant_lname());
                    edtFatherName.setText(Common.rcuDetails.get(position).getTenant_father_name());
                    edtFatherContactno.setText(Common.rcuDetails.get(position).getTenant_father_contact_no());
                    edtFlatPlotno.setText(Common.rcuDetails.get(position).getFlat_number());
                    edtLandMark.setText(Common.rcuDetails.get(position).getLandmark());
                    edtPinCode.setText(Common.rcuDetails.get(position).getPincode());
                    edtContactno.setText(Common.rcuDetails.get(position).getContact_number());
                    edtEmail.setText(Common.rcuDetails.get(position).getEmail_id());
                    alreadyno=Common.rcuDetails.get(position).getContact_number();
                    alreadyemail=Common.rcuDetails.get(position).getEmail_id();
                    if(alreadyemail.equals("") || alreadyemail ==null)
                    {
                        edtEmail.setEnabled(true);
                    }else
                    {
                        edtEmail.setEnabled(false);
                    }

                    if(alreadyno.equals("") || alreadyno==null)
                    {
                        edtContactno.setEnabled(true);
                    }else
                    {
                        edtContactno.setEnabled(false);
                    }
                    txtHireDate.setText(Common.rcuDetails.get(position).getProperty_hire_date());
                    txtLeaveDate.setText(Common.rcuDetails.get(position).getProperty_leave_date());
                    stateId = Common.rcuDetails.get(position).getState();
                    spinnerCity.setText(Common.rcuDetails.get(position).getCity());
                    //cityId = Common.rcuDetails.get(position).getCity();
                    edtLocation.setText(Common.rcuDetails.get(position).getLocation());
                    //areaId = Common.rcuDetails.get(position).getLocation();
                    imgTenanto = Common.rcuDetails.get(position).getTenant_photo();
                    Common.Config("image name   " + imgTenanto);
                    if (imgTenanto.trim().equals("")) {
                        btnBrowseImage.setText("Browse");
                    } else {
                        btnBrowseImage.setText("" + imgTenanto);
                    }
                    if (!imgTenanto.trim().equals("")) {
                        tenantProfilePic.setVisibility(View.VISIBLE);
                        imgTenantoDisplay = WebUrls.IMG_URL + Common.rcuDetails.get(position).getTenant_photo();
                        txtRoom_no.setText("Room no. " + Common.rcuDetails.get(position).getTenant_room_no());
                        Picasso.with(this)
                                .load(imgTenantoDisplay)
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)

                                .into(tenantProfilePic);
                    } else {
                        tenantProfilePic.setVisibility(View.GONE);
                    }

                    changeStateCityArea();
                } else {
                    //...............

                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtFatherName.setText("");
                    edtFatherContactno.setText("");
                    edtFlatPlotno.setText("");
                    edtLandMark.setText("");
                    edtPinCode.setText("");
                    edtContactno.setText("");
                    edtEmail.setText("");
                    stateId = "0";
                    spinnerCity.setText("");
                    edtLocation.setText("");
            /*cityId = "0";
            areaId = "0";*/

                    txtRoom_no.setText("Room no. " + Common.selectedBedInfo.get(position).getRoomNo());
                    txtHireDate.setText("");
                    txtLeaveDate.setText("");

                    edtFirstName.clearFocus();
                    //.............
                    edtLastName.clearFocus();
                    edtFatherName.clearFocus();
                    edtFatherContactno.clearFocus();
                    edtFlatPlotno.clearFocus();
                    edtLandMark.clearFocus();
                    edtPinCode.clearFocus();
                    edtContactno.clearFocus();
                    edtEmail.clearFocus();
                    spinnerCity.clearFocus();
                    edtLocation.clearFocus();
                    txtHireDate.clearFocus();
                    txtLeaveDate.clearFocus();


                    changeStateCityArea();
                    tenantProfilePic.setVisibility(View.GONE);
                    imgTenantoDisplay = "";
                    imgTenanto = "";
                    btnBrowseImage.setText("Browse");
                    Common.profileImagePath = "";
                }
            }
        } catch (Exception e) {

        }


    }


    public void changeStateCityArea() {

        if (mArrayStates != null && mArrayStates.size() > 0) {
            for (int i = 0; i < mArrayStates.size(); i++) {
                if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                    spinnerState.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();
    }

    private void setListeners() {

        mCommon = new Common();
        mValidation = new Validation(this);

        btnTenant1.setOnClickListener(this);

        btnTenant2.setOnClickListener(this);
        btnTenant3.setOnClickListener(this);
        btnTenant4.setOnClickListener(this);

        txtHireDate.setOnClickListener(this);
        txtLeaveDate.setOnClickListener(this);
        tenantProfilePic.setOnClickListener(this);

        btnBrowseImage.setOnClickListener(this);
        txtBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        // Search state data
        mArrayStates = new ArrayList<>();
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(PropertyRCUTenantDetailActivity.this, countryId);
        mGetStatesAsyncTask.setCallBack(PropertyRCUTenantDetailActivity.this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                "SELECT * FROM state where country_id = '" + countryId + "' ORDER BY state_name");
        mStateSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_state, mArrayStates);
        spinnerState.setAdapter(mStateSpinnerAdapter);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = mArrayStates.get(position).getStrStateName();
                if (position != 0) {
                    stateId = mArrayStates.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mArrayNoOfContent = new ArrayList<>();
        mArrayNoOfContent.addAll(getNoofCotenant());
        mPropertyTypeAdapter = new StateSpinnerAdapter(this, R.id.spinner_no_of_cotenant, mArrayNoOfContent);
        spinnerNoofCotenant.setAdapter(mPropertyTypeAdapter);
        spinnerNoofCotenant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfContent = mArrayNoOfContent.get(position).getStrStateName();
                setNoTenant();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        noOfContent = "" + Common.selectedBedInfo.size();
        setNoTenant();

        if (isUpdate) {
            rcuId = Common.rcuDetails.get(Integer.parseInt(tenantId) - 1).getTenant_id();
        } else {
//            rcuId  = getIntent().getStringExtra("RCUId");
            rcuId = rcuids[Integer.parseInt(tenantId) - 1];
        }
    }


    public void setNoTenant() {
        if (noOfContent.equals("1")) {

            mLayoutTenantView.setVisibility(View.VISIBLE);
            mLayoutTenant.setVisibility(View.VISIBLE);
            btnTenant1.setVisibility(View.VISIBLE);
            btnTenant2.setVisibility(View.GONE);
            btnTenant3.setVisibility(View.GONE);
            btnTenant4.setVisibility(View.GONE);
        } else if (noOfContent.equals("2")) {
            mLayoutTenantView.setVisibility(View.VISIBLE);
            mLayoutTenant.setVisibility(View.VISIBLE);
            btnTenant1.setVisibility(View.VISIBLE);
            btnTenant2.setVisibility(View.VISIBLE);
            btnTenant3.setVisibility(View.GONE);
            btnTenant4.setVisibility(View.GONE);
        } else if (noOfContent.equals("3")) {
            mLayoutTenantView.setVisibility(View.VISIBLE);
            mLayoutTenant.setVisibility(View.VISIBLE);
            btnTenant1.setVisibility(View.VISIBLE);
            btnTenant2.setVisibility(View.VISIBLE);
            btnTenant3.setVisibility(View.VISIBLE);
            btnTenant4.setVisibility(View.GONE);
        } else if (noOfContent.equals("4")) {
            mLayoutTenantView.setVisibility(View.VISIBLE);
            mLayoutTenant.setVisibility(View.VISIBLE);
            btnTenant1.setVisibility(View.VISIBLE);
            btnTenant2.setVisibility(View.VISIBLE);
            btnTenant3.setVisibility(View.VISIBLE);
            btnTenant4.setVisibility(View.VISIBLE);
        } else {
            mLayoutTenantView.setVisibility(View.GONE);
            mLayoutTenant.setVisibility(View.GONE);
            btnTenant1.setVisibility(View.GONE);
            btnTenant2.setVisibility(View.GONE);
            btnTenant3.setVisibility(View.GONE);
            btnTenant4.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this, v);
        switch (v.getId()) {
            case R.id.textView_title:
                mCommon.hideKeybord(this, txtBack);
                backpreedialog();
                break;
            case R.id.textview_hire_date:
                txtLeaveDate.setText("");
                leaveDateCal = null;
                onCreateDialogTest(txtHireDate, "hire_date").show();
                break;
            case R.id.textview_leave_date:

                if (hireDateCal != null) {
                    onCreateDialogTest(txtLeaveDate, "leave_date").show();
                } else {
                    mCommon.displayAlert(this, "Please select Hire date first.", false);
                }
//                onCreateDialogTest(txtLeaveDate, "leave_date" ).show();
                break;

            case R.id.tenantProfilePic:
               /* ImagePreviewDialog vehicleImagePreviewDialog1 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        int postion = getIntent().getExtras().getInt("tenant_no", 1) - 1;
                        String vehicleRcPhoto = "";

                        Picasso.with(PropertyRCUTenantDetailActivity.this)
                                .load(imgTenantoDisplay)
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);

                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                vehicleImagePreviewDialog1.getPerameter(this, "Profile Pic", true);
                vehicleImagePreviewDialog1.show(this.getSupportFragmentManager(), "image dialog");

*/

                CustomDialogClass VehicleRcId = new CustomDialogClass(PropertyRCUTenantDetailActivity.this);
                VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VehicleRcId.getPerameter(this, "Profile Pic", true);
                VehicleRcId.show();


                break;

            case R.id.button_tenant_1:

                if (edtFirstName.getText().toString().trim().length() != 0 || edtFatherName.getText().toString().trim().length() != 0) {
                  /*  AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {
                        @Override
                        public void callBack() {

                            submitBtnClickEvent();

                        }

                        @Override
                        public void cancelCallBack() {
                            tenantId = "1";
                            btnTenant1.setTextColor(Color.WHITE);
                            btnTenant2.setTextColor(Color.BLACK);
                            btnTenant3.setTextColor(Color.BLACK);
                            btnTenant4.setTextColor(Color.BLACK);
                            btnTenant1.setBackgroundResource(R.color.orange_color);
                            btnTenant2.setBackgroundResource(R.color.white_dark);
                            btnTenant3.setBackgroundResource(R.color.white_dark);
                            btnTenant4.setBackgroundResource(R.color.white_dark);
                            resetAllData();
                        }
                    };
                    dialog.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    dialog.show(getSupportFragmentManager(), "");*/


                    CustomDialogAlertDialogWithTwoCallback customDialogAlertDialogWithTwoCallback = new CustomDialogAlertDialogWithTwoCallback(PropertyRCUTenantDetailActivity.this, R.style.full_screen_dialog);
                    customDialogAlertDialogWithTwoCallback.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    customDialogAlertDialogWithTwoCallback.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    customDialogAlertDialogWithTwoCallback.show();

                } else {

                    tenantId = "1";
                    btnTenant1.setTextColor(Color.WHITE);
                    btnTenant2.setTextColor(Color.BLACK);
                    btnTenant3.setTextColor(Color.BLACK);
                    btnTenant4.setTextColor(Color.BLACK);
                    btnTenant1.setBackgroundResource(R.color.orange_color);
                    btnTenant2.setBackgroundResource(R.color.white_dark);
                    btnTenant3.setBackgroundResource(R.color.white_dark);
                    btnTenant4.setBackgroundResource(R.color.white_dark);
                    resetAllData();
                }

                break;
            case R.id.button_tenant_2:

                if (edtFirstName.getText().toString().trim().length() != 0 || edtFatherName.getText().toString().trim().length() != 0) {
                  /*  AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {
                        @Override
                        public void callBack() {

                            submitBtnClickEvent();
                        }

                        @Override
                        public void cancelCallBack() {
                            tenantId = "2";
                            btnTenant1.setTextColor(Color.BLACK);
                            btnTenant2.setTextColor(Color.WHITE);
                            btnTenant3.setTextColor(Color.BLACK);
                            btnTenant4.setTextColor(Color.BLACK);
                            btnTenant1.setBackgroundResource(R.color.white_dark);
                            btnTenant2.setBackgroundResource(R.color.orange_color);
                            btnTenant3.setBackgroundResource(R.color.white_dark);
                            btnTenant4.setBackgroundResource(R.color.white_dark);
                            resetAllData();
                        }
                    };
                    dialog.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    dialog.show(getSupportFragmentManager(), "");
*/

                    CustomDialogAlertDialogWithTwoCallback customDialogAlertDialogWithTwoCallback = new CustomDialogAlertDialogWithTwoCallback(PropertyRCUTenantDetailActivity.this, R.style.full_screen_dialog);
                    customDialogAlertDialogWithTwoCallback.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    customDialogAlertDialogWithTwoCallback.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    customDialogAlertDialogWithTwoCallback.show();


                } else {

                    tenantId = "2";
                    btnTenant1.setTextColor(Color.BLACK);
                    btnTenant2.setTextColor(Color.WHITE);
                    btnTenant3.setTextColor(Color.BLACK);
                    btnTenant4.setTextColor(Color.BLACK);
                    btnTenant1.setBackgroundResource(R.color.white_dark);
                    btnTenant2.setBackgroundResource(R.color.orange_color);
                    btnTenant3.setBackgroundResource(R.color.white_dark);
                    btnTenant4.setBackgroundResource(R.color.white_dark);
                    resetAllData();
                }

                break;
            case R.id.button_tenant_3:


                if (edtFirstName.getText().toString().trim().length() != 0 || edtFatherName.getText().toString().trim().length() != 0) {
                   /* AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {

                        @Override
                        public void callBack() {

                            submitBtnClickEvent();
                        }

                        @Override
                        public void cancelCallBack() {
                            tenantId = "3";
                            btnTenant1.setTextColor(Color.BLACK);
                            btnTenant2.setTextColor(Color.BLACK);
                            btnTenant3.setTextColor(Color.WHITE);
                            btnTenant4.setTextColor(Color.BLACK);
                            btnTenant1.setBackgroundResource(R.color.white_dark);
                            btnTenant2.setBackgroundResource(R.color.white_dark);
                            btnTenant3.setBackgroundResource(R.color.orange_color);
                            btnTenant4.setBackgroundResource(R.color.white_dark);
                            resetAllData();
                        }
                    };
                    dialog.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    dialog.show(getSupportFragmentManager(), "");
*/

                    CustomDialogAlertDialogWithTwoCallback customDialogAlertDialogWithTwoCallback = new CustomDialogAlertDialogWithTwoCallback(PropertyRCUTenantDetailActivity.this, R.style.full_screen_dialog);
                    customDialogAlertDialogWithTwoCallback.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    customDialogAlertDialogWithTwoCallback.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    customDialogAlertDialogWithTwoCallback.show();


                } else {

                    tenantId = "3";
                    btnTenant1.setTextColor(Color.BLACK);
                    btnTenant2.setTextColor(Color.BLACK);
                    btnTenant3.setTextColor(Color.WHITE);
                    btnTenant4.setTextColor(Color.BLACK);
                    btnTenant1.setBackgroundResource(R.color.white_dark);
                    btnTenant2.setBackgroundResource(R.color.white_dark);
                    btnTenant3.setBackgroundResource(R.color.orange_color);
                    btnTenant4.setBackgroundResource(R.color.white_dark);
                    resetAllData();
                }
                break;
            case R.id.button_tenant_4:

                if (edtFirstName.getText().toString().trim().length() != 0 || edtFatherName.getText().toString().trim().length() != 0) {
                  /*  AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {
                        @Override
                        public void callBack() {
                            submitBtnClickEvent();

                        }

                        @Override
                        public void cancelCallBack() {
                            tenantId = "4";
                            btnTenant1.setTextColor(Color.BLACK);
                            btnTenant2.setTextColor(Color.BLACK);
                            btnTenant3.setTextColor(Color.BLACK);
                            btnTenant4.setTextColor(Color.WHITE);
                            btnTenant1.setBackgroundResource(R.color.white_dark);
                            btnTenant2.setBackgroundResource(R.color.white_dark);
                            btnTenant3.setBackgroundResource(R.color.white_dark);
                            btnTenant4.setBackgroundResource(R.color.orange_color);
                            resetAllData();
                        }
                    };
                    dialog.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    dialog.show(getSupportFragmentManager(), "");
*/

                    CustomDialogAlertDialogWithTwoCallback customDialogAlertDialogWithTwoCallback = new CustomDialogAlertDialogWithTwoCallback(PropertyRCUTenantDetailActivity.this, R.style.full_screen_dialog);
                    customDialogAlertDialogWithTwoCallback.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    customDialogAlertDialogWithTwoCallback.getPerameter(this, "Alert", "Do you want to save Tenant " + tenantId + "'s information?");
                    customDialogAlertDialogWithTwoCallback.show();


                } else {

                    tenantId = "4";
                    btnTenant1.setTextColor(Color.BLACK);
                    btnTenant2.setTextColor(Color.BLACK);
                    btnTenant3.setTextColor(Color.BLACK);
                    btnTenant4.setTextColor(Color.WHITE);
                    btnTenant1.setBackgroundResource(R.color.white_dark);
                    btnTenant2.setBackgroundResource(R.color.white_dark);
                    btnTenant3.setBackgroundResource(R.color.white_dark);
                    btnTenant4.setBackgroundResource(R.color.orange_color);
                    resetAllData();
                }


                break;
            case R.id.button_browse:
               // takePhoto();
                //new code by ashish
                selectImage();
                break;
            case R.id.button_next:
                submitBtnClickEvent();
                break;
        }
    }


    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private ImageView imageView;
        private String titleStr;
        private TextView message;
        private Button downloadImageBtn;
        private Button alertNoBtn;
        private Boolean downloadBtnHide;


        public CustomDialogClass(PropertyRCUTenantDetailActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }

        /*public CustomDialogClass(FragmentManager supportFragmentManager) {
            super();
        }*/


        public void getPerameter(FragmentActivity activity, String titleStr, Boolean flag) {
            this.mFActivity = activity;
            this.titleStr = titleStr;
            this.downloadBtnHide = flag;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.list_group);
            title = (TextView) findViewById(R.id.dailogTitiel);
            imageView = (ImageView) findViewById(R.id.imageView);
            downloadImageBtn = (Button) findViewById(R.id.downloadImageBtn);

            try {
                //showImage(imageView);
                Picasso.with(PropertyRCUTenantDetailActivity.this)
                        .load(imgTenantoDisplay)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(imageView);
            } catch (Exception e) {

            }

            title.setText(titleStr);

            if (downloadBtnHide) {
                downloadImageBtn.setVisibility(View.GONE);
            } else {
                downloadImageBtn.setVisibility(View.VISIBLE);
            }
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

    //new code profile image capture
    /*image capture code start*/
    public void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyRCUTenantDetailActivity.this);
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PropertyRCUTenantDetailActivity.this, android.R.layout.simple_list_item_1);
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
                            Toast toast=Toast.makeText(PropertyRCUTenantDetailActivity.this,"Please give internal storage permission", Toast.LENGTH_SHORT);
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
            uri = UtilityImg.getExternalFilesDirForVersion24Above(PropertyRCUTenantDetailActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(PropertyRCUTenantDetailActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
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
                Log.e("Imageg-", "" + ImagePath);
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
                    if(  resultUri!=null   ){

                        Log.e("resultUri",""+resultUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , resultUri);
                        tenantProfilePic.setImageBitmap(bitmap);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        Bitmap bitmap1;
                        bitmap1 = ScalingUtilities.scaleDown(bitmap, 420, true);
                        FileOutputStream fOut;
                        try {
                            File f = new File(getRealPathFromURI(tempUri));
                            fOut = new FileOutputStream(f);
                            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            imgTenanto= f.getAbsolutePath();
                            Common.profileImagePath=imgTenanto;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // imgTenantoDisplay=Common.profileImagePath;
                        if (tenantProfilePic.getVisibility() == View.GONE) {
                            tenantProfilePic.setVisibility(View.VISIBLE);
                        }

                      /*  Picasso.with(this)
                                .load(imgTenanto)
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(tenantProfilePic);
                        String filename = imgTenanto.substring(imgTenanto.lastIndexOf("/") + 1);
                        btnBrowseImage.setText("" + filename);*/
                    }
                }
                catch (Exception e) {
                    //handle exception
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

    //end

    public void checkEditOrAdd() {

        if (isUpdate) {
            noOfContent = "" + getIntent().getExtras().getInt("noOfTenant", 0);

        } else {
            if (Common.selectedBedInfo.size() != 0) {
                noOfContent = "" + Common.selectedBedInfo.size();
            } else {
                noOfContent = "1";
            }
        }

        setNoTenant();
        if (isUpdate) {
            resetAllData();
        }

    }

    public void checkEditOrAdd1() {

        if (isUpdate) {
            noOfContent = "" + getIntent().getExtras().getInt("noOfTenant", 0);

        } else {
            if (Common.selectedBedInfo.size() != 0) {
                noOfContent = "" + Common.selectedBedInfo.size();
            } else {
                noOfContent = "1";
            }
        }

        setNoTenant();
        /*if (isUpdate) {
            resetAllData();
        }*/

    }

    public void updateTenantFrom(int tenanNo) {

        tenantId = "" + tenanNo;


        if (tenanNo == 1) {
            btnTenant1.setTextColor(Color.WHITE);
            btnTenant2.setTextColor(Color.BLACK);
            btnTenant3.setTextColor(Color.BLACK);
            btnTenant4.setTextColor(Color.BLACK);
            btnTenant1.setBackgroundResource(R.color.orange_color);
            btnTenant2.setBackgroundResource(R.color.white_dark);
            btnTenant3.setBackgroundResource(R.color.white_dark);
            btnTenant4.setBackgroundResource(R.color.white_dark);
        } else if (tenanNo == 2) {

            btnTenant1.setTextColor(Color.BLACK);
            btnTenant2.setTextColor(Color.WHITE);
            btnTenant3.setTextColor(Color.BLACK);
            btnTenant4.setTextColor(Color.BLACK);
            btnTenant1.setBackgroundResource(R.color.white_dark);
            btnTenant2.setBackgroundResource(R.color.orange_color);
            btnTenant3.setBackgroundResource(R.color.white_dark);
            btnTenant4.setBackgroundResource(R.color.white_dark);
        } else if (tenanNo == 3) {

            btnTenant1.setTextColor(Color.BLACK);
            btnTenant2.setTextColor(Color.BLACK);
            btnTenant3.setTextColor(Color.WHITE);
            btnTenant4.setTextColor(Color.BLACK);
            btnTenant1.setBackgroundResource(R.color.white_dark);
            btnTenant2.setBackgroundResource(R.color.white_dark);
            btnTenant3.setBackgroundResource(R.color.orange_color);
            btnTenant4.setBackgroundResource(R.color.white_dark);
        } else if (tenanNo == 4) {

            btnTenant1.setTextColor(Color.BLACK);
            btnTenant2.setTextColor(Color.BLACK);
            btnTenant3.setTextColor(Color.BLACK);
            btnTenant4.setTextColor(Color.WHITE);
            btnTenant1.setBackgroundResource(R.color.white_dark);
            btnTenant2.setBackgroundResource(R.color.white_dark);
            btnTenant3.setBackgroundResource(R.color.white_dark);
            btnTenant4.setBackgroundResource(R.color.orange_color);
        }
        tenantId = "" + tenanNo;

    }

    Calendar hireDateCal, leaveDateCal;

    protected Dialog onCreateDialogTest(final TextView txtDate, final String type) {

        DatePickerDialog datePicker = new DatePickerDialog(this,R.style.datepickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //showDate(year, monthOfYear + 1, dayOfMonth, v);
                        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        if (type.equals("hire_date")) {

                            hireDateCal = Calendar.getInstance();
                            hireDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            Calendar currentDateCal = Calendar.getInstance();
                            currentDateCal.set(year1, (month + 1), day - 1);
                           // if (getDateDiffStringBool(getDateFromCalender(currentDateCal), getDateFromCalender(hireDateCal))) {
                                txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                           /* } else {
                                mCommon.displayAlert(PropertyRCUTenantDetailActivity.this, "Hire date will not be less than Current date.", false);
                                txtDate.setText("");
                                hireDateCal = null;
                            }*/


                        } else {

                            leaveDateCal = Calendar.getInstance();
                            leaveDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            if (getDateDiffStringBool(getDateFromCalender(hireDateCal), getDateFromCalender(leaveDateCal))) {
                                txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                            } else {

                                mCommon.displayAlert(PropertyRCUTenantDetailActivity.this, "Leave date will not be equal or less than Hire date.", false);
                                txtDate.setText("");
                                leaveDateCal = null;
                            }

                        }

                        /*txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                .append(monthOfYear + 1).append("/").append(year));*/
                    }
                }, year1, month, day);

        return datePicker;

    }

    public Date getDateFromCalender(Calendar cal) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(cal.getTime());


        Date date1 = null;
        try {
            date1 = df.parse(formattedDate);
        } catch (ParseException e) {
//            e.printStackTrace();
        }

        return date1;

    }

    public boolean getDateDiffStringBool(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
//        getDay(delta);
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

    }

    public void takePhoto() {

        Intent mIntent = new Intent(this, PhotoCaptureActivity.class);
        mIntent.putExtra("photo_purpose", "profilePic");
        startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Common.CAMERA_CAPTURE_REQUEST) {
                imgTenanto = Common.profileImagePath;
                imgTenantoDisplay = "file://" + Common.profileImagePath;
                if (tenantProfilePic.getVisibility() == View.GONE) {
                    tenantProfilePic.setVisibility(View.VISIBLE);
                }

                Picasso.with(this)
                        .load(imgTenantoDisplay)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)

                        .into(tenantProfilePic);
                String filename = imgTenanto.substring(imgTenanto.lastIndexOf("/") + 1);
                btnBrowseImage.setText("" + filename);
            }
        } catch (NullPointerException e) {

            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }
    }
*/
    public void submitBtnClickEvent() {

        if (imgTenanto == null) {
            imgTenanto = "";
        }
        int position = Integer.parseInt(tenantId) - 1;
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        fatherName = edtFatherName.getText().toString().trim();
        fatherContactno = edtFatherContactno.getText().toString().trim();
        flatPlotno = edtFlatPlotno.getText().toString().trim();
        landMark = edtLandMark.getText().toString().trim();
        /*String location = edtLocation.getText().toString().trim();*/
        pinCode = edtPinCode.getText().toString().trim();
        contactNo = edtContactno.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        hireDate = msharedobj.getHireDate();
        leaveDate = txtLeaveDate.getText().toString().trim();
        areaId = edtLocation.getText().toString().trim();
        cityId = spinnerCity.getText().toString().trim();
        property_id = getIntent().getExtras().getString("property_id");
        owner_id = getIntent().getExtras().getString("owner_id");
        if(!isUpdate) {
            room_id = Common.selectedBedInfo.get(position).getRoomId();
            Log.d("room new 123",room_id);

        } else {
            room_id = Common.rcuDetails.get(position).getTenant_room_id();
        }
        Log.d("room new valu123",room_id);



        transaction_id = getIntent().getExtras().getString("transaction_id");


        //Toast.makeText(PropertyRCUTenantDetailActivity.this, "tid"+transaction_id, Toast.LENGTH_SHORT).show();

        if (firstName.length() == 0) {
            mCommon.displayAlert(this, "Enter Tenant's full name.", false);
        } else if (fatherName.length() == 0) {
            mCommon.displayAlert(this, "Enter father name.", false);
        } else if (fatherContactno.length() > 0 && fatherContactno.length() < 10) {
            mCommon.displayAlert(this, "Enter father valid contact no.", false);
        } else if (fatherContactno.length() ==0) {
            mCommon.displayAlert(this, "Enter father contact no.", false);
        } else if (contactNo.length() > 0 && contactNo.length() < 10) {
            mCommon.displayAlert(this, "Enter valid contact no.", false);
        } else if (contactNo.length()==0) {
            mCommon.displayAlert(this, "Enter contact no.", false);
        } else if(email.length() > 0 && !mValidation.checkEmail(email) ) {
            mCommon.displayAlert(this, "Enter valid email id.", false);
        } else if (pinCode.length() > 0 && pinCode.length() < 6) {
            mCommon.displayAlert(this, "Enter valid pincode number.", false);
        } else if (flatPlotno.length() == 0) {
            mCommon.displayAlert(this, "Enter flat/plot/building no.", false);
        } else {
            mCommon.hideKeybord(this, edtFatherContactno);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {

                if (isUpdate) {
                    this.rcuId = Common.rcuDetails.get(Integer.parseInt(tenantId) - 1).getTenant_id();
                } else {
                    this.rcuId = rcuids[Integer.parseInt(tenantId) - 1];
                }

                String namemainvalue = firstName;
                String emailmainvalue = email;
                String mobileNomainvalue = contactNo;
                String datasimplenumber=mainuservaluemobile;
                String datasimpleemail= useridemailvalue;

                //new

                if(isUpdate){
                    btnSubmit.setClickable(false);
                    new UpdateUserDetail(namemainvalue, emailmainvalue, mobileNomainvalue).execute();
                }else{
                    btnSubmit.setClickable(false);
                    new UpdateUserDetail(namemainvalue, emailmainvalue, mobileNomainvalue).execute();
                }


            }

        }
    }

    @Override
    public void requestResponce(String result) {
        Common.Config("result      " + result);
        LogConfig.logd("Rcu tenant detail response =", "" + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this,Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {



                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    rcuId = jsoArray.getJSONObject(0).getString("rcu_id");
                    roomidnewvalue=RCUTenantDetailAsynctask.newroomid;
                    edtFirstName.requestFocus();

                    Intent intent=new Intent(PropertyRCUTenantDetailActivity.this,Service_handle_paymentapi.class);
                    intent.putExtra("Propertyid",property_id_main_jsop);
                    intent.putExtra("Roomid",room_id_main_json);
                    intent.putExtra("Ownerid",ownerid_main_json);
                    intent.putExtra("Userid", userid_main_json);
                    intent.putExtra("Tenantid",rcuId);
                    intent.putExtra("Suserid", suserid);
                    //intent.putExtra("Hiredate",hireDate);
                    startService(intent);
                    String nooftenanttenantid=tenantId;
                    if(isUpdate)
                    {
                    }else {

                        if (tenantId.equals("1")) {

                            btnTenant1.setVisibility(View.GONE);

                        } else if (tenantId.equals("2")) {
                            btnTenant2.setVisibility(View.GONE);
                        } else if (tenantId.equals("3")) {

                            btnTenant3.setVisibility(View.GONE);

                        } else {
                            btnTenant4.setVisibility(View.GONE);
                        }
                    }

                    mprogressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    dialogbooking.dismiss();
                   // Toast.makeText(PropertyRCUTenantDetailActivity.this, "tenant count"+tenantId+" rcuid "+rcuId, Toast.LENGTH_SHORT).show();
                  //  App.MethodLog(tenantId,rcuId,"PropertyRCUTDetailsActivity","requestResponce",result);
                    Intent mIntent = new Intent(this,PropertyRCUTenantProfessionalDetailActivity.class);
                    mIntent.putExtra("RCUId", "" + rcuId);
                    mIntent.putExtra("isUpdate", isUpdate);
                    mIntent.putExtra("modaltenant",(Parcelable)modal);
                    mIntent.putExtra("tenant_no", Integer.parseInt(tenantId));
                    Log.d("tanent id intent",Integer.parseInt(tenantId)+"");
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    String tenantPhotoUrl = jsoArray.getJSONObject(0).getString("tenant_photo");
                    setDataToLoacal(tenantPhotoUrl);
                    if (Integer.parseInt(tenantId) < Integer.parseInt(noOfContent)) {

                        if (isUpdate) {
                            this.rcuId = Common.rcuDetails.get(Integer.parseInt(tenantId) - 1).getTenant_id();
                            Log.d("tanent id updat method=", tenantId+"");

                        } else {
                            this.rcuId = rcuids[Integer.parseInt(tenantId) - 1];
                            Log.d("tanent id without upda=", tenantId+"");
                        }
                        int tenantNo = (Integer.parseInt(tenantId) + 1);
                        updateTenantFrom(tenantNo);

                        Log.d("tanent no=", tenantNo+"");

                        resetAllData();
                    } else {
                        finish();
                    }
                } else {
                    dialogbooking.dismiss();
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setDataToLoacal(String photoUrl) {
        int tenantNo = Integer.parseInt(tenantId);

        Common.Config("   tenantNo   " + tenantNo);
        int position = Integer.parseInt(tenantId) - 1;

        try {
            if (!isUpdate) {

                if (Common.rcuDetails.size() < (position + 1)) {
                    RCUDetailModal rcuDetailModal = new RCUDetailModal(Parcel.obtain());
                    rcuDetailModal.setTenant_id(rcuids[position]);
                    rcuDetailModal.setTenant_fname(edtFirstName.getText().toString().trim());
                    rcuDetailModal.setTenant_lname(edtLastName.getText().toString().trim());
                    rcuDetailModal.setTenant_father_name(edtFatherName.getText().toString().trim());
                    rcuDetailModal.setLandmark(edtLandMark.getText().toString().trim());
                    rcuDetailModal.setTenant_father_contact_no(edtFatherContactno.getText().toString().trim());
                    rcuDetailModal.setFlat_number(edtFlatPlotno.getText().toString().trim());
                    rcuDetailModal.setState(stateId);
                    rcuDetailModal.setCity(spinnerCity.getText().toString().trim());
                    rcuDetailModal.setLocation(edtLocation.getText().toString().trim());
                    rcuDetailModal.setPincode(edtPinCode.getText().toString().trim());
                    rcuDetailModal.setEmail_id(edtEmail.getText().toString().trim());
                    rcuDetailModal.setContact_number(edtContactno.getText().toString().trim());
                    rcuDetailModal.setProperty_hire_date(txtHireDate.getText().toString().trim());
                    rcuDetailModal.setProperty_leave_date(txtLeaveDate.getText().toString().trim());
                    rcuDetailModal.setTenant_room_no(txtRoom_no.getText().toString().trim());
                    rcuDetailModal.setTenant_room_id(Common.selectedBedInfo.get(position).getRoomId());
                    rcuDetailModal.setTenant_photo(photoUrl);
                    Common.rcuDetails.add(position, rcuDetailModal);
                } else {

                    Common.rcuDetails.get(position).setTenant_id(rcuids[position]);
                    Common.rcuDetails.get(position).setTenant_fname(edtFirstName.getText().toString().trim());
                    Common.rcuDetails.get(position).setTenant_lname(edtLastName.getText().toString().trim());
                    Common.rcuDetails.get(position).setTenant_father_name(edtFatherName.getText().toString().trim());
                    Common.rcuDetails.get(position).setLandmark(edtLandMark.getText().toString().trim());
                    Common.rcuDetails.get(position).setTenant_father_contact_no(edtFatherContactno.getText().toString().trim());
                    Common.rcuDetails.get(position).setFlat_number(edtFlatPlotno.getText().toString().trim());
                    Common.rcuDetails.get(position).setState(stateId);
                    Common.rcuDetails.get(position).setCity(spinnerCity.getText().toString().trim());
                    Common.rcuDetails.get(position).setLocation(edtLocation.getText().toString().trim());
                    Common.rcuDetails.get(position).setPincode(edtPinCode.getText().toString().trim());
                    Common.rcuDetails.get(position).setEmail_id(edtEmail.getText().toString().trim());
                    Common.rcuDetails.get(position).setContact_number(edtContactno.getText().toString().trim());
                    Common.rcuDetails.get(position).setProperty_hire_date(txtHireDate.getText().toString().trim());
                    Common.rcuDetails.get(position).setProperty_leave_date(txtLeaveDate.getText().toString().trim());
                    Common.rcuDetails.get(position).setTenant_room_no(txtRoom_no.getText().toString().trim());
                    Common.rcuDetails.get(position).setTenant_room_id(Common.selectedBedInfo.get(position).getRoomId());
                    Common.rcuDetails.get(position).setTenant_photo(photoUrl);
                }

            } else {

                Common.rcuDetails.get(position).setTenant_id(rcuids[position]);
                Common.rcuDetails.get(position).setTenant_fname(edtFirstName.getText().toString().trim());
                Common.rcuDetails.get(position).setTenant_lname(edtLastName.getText().toString().trim());
                Common.rcuDetails.get(position).setTenant_father_name(edtFatherName.getText().toString().trim());
                Common.rcuDetails.get(position).setLandmark(edtLandMark.getText().toString().trim());
                Common.rcuDetails.get(position).setTenant_father_contact_no(edtFatherContactno.getText().toString().trim());
                Common.rcuDetails.get(position).setFlat_number(edtFlatPlotno.getText().toString().trim());
                Common.rcuDetails.get(position).setState(stateId);
                Common.rcuDetails.get(position).setCity(spinnerCity.getText().toString().trim());
                Common.rcuDetails.get(position).setLocation(edtLocation.getText().toString().trim());
                Common.rcuDetails.get(position).setPincode(edtPinCode.getText().toString().trim());
                Common.rcuDetails.get(position).setEmail_id(edtEmail.getText().toString().trim());
                Common.rcuDetails.get(position).setContact_number(edtContactno.getText().toString().trim());
                Common.rcuDetails.get(position).setProperty_hire_date(txtHireDate.getText().toString().trim());
                Common.rcuDetails.get(position).setProperty_leave_date(txtLeaveDate.getText().toString().trim());
                Common.rcuDetails.get(position).setTenant_room_no(txtRoom_no.getText().toString().trim());
                Common.rcuDetails.get(position).setTenant_room_id(Common.rcuDetails.get(position).getTenant_room_id());
                Common.rcuDetails.get(position).setTenant_photo(photoUrl);
            }
        } catch (IndexOutOfBoundsException e) {
            LogConfig.logd("Tanant ", "IndexOutOfBoundsException " + e.getMessage());
        }


    }


    @Override
    public void requestStateDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayStates.addAll(mArray);
        if (mArrayStates != null && mArrayStates.size() > 0) {
            for (int i = 0; i < mArrayStates.size(); i++) {
                if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                    spinnerState.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();
    }

   /* @Override
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
    }*/

    @Override
    protected void onResume() {
        super.onResume();

    }

    private ArrayList<StateModal> getNoofCotenant() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("No of Cotenant");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("1");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("2");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("3");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("4");
        mArray.add(mStateModal);

        return mArray;
    }

   /* @Override
    public void requestAreaDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayArea.clear();
        mArrayArea.addAll(mArray);
        if (mArrayArea != null && mArrayArea.size() > 0) {
            for (int i = 0; i < mArrayArea.size(); i++) {
                if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                    edtLocation.setSelection(i);
                }
            }
        }

        mAreaSpinnerAdapter.notifyDataSetChanged();
    }*/


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString("noOfCotanant", noOfContent);
        outState.putStringArray("rcuids", rcuids);
        outState.putBoolean("isUpdate", isUpdate);

        outState.putString("tenantId", tenantId);

        outState.putString("tenant_firstName", edtFirstName.getText().toString().trim());
        outState.putString("tenant_lastName", edtLastName.getText().toString().trim());
        outState.putString("tenant_FatherName", edtFatherName.getText().toString().trim());
        outState.putString("tenant_FatherContactNo", edtFatherContactno.getText().toString().trim());
        outState.putString("tenant_FlatPlotno", edtFlatPlotno.getText().toString().trim());
        outState.putString("tenant_LandMark", edtLandMark.getText().toString().trim());

        outState.putString("tenant_PinCode", edtPinCode.getText().toString().trim());
        outState.putString("tenant_Contactno", edtContactno.getText().toString().trim());
        outState.putString("tenant_Email", edtEmail.getText().toString().trim());
        outState.putString("tenant_HireDate", txtHireDate.getText().toString().trim());
        outState.putString("tenant_LeaveDate", txtLeaveDate.getText().toString().trim());

        outState.putString("property_stateid", stateId);
        outState.putString("city_id", spinnerCity.getText().toString().trim());
        outState.putString("area_id", edtLocation.getText().toString().trim());
        /*outState.putString("property_cityid", cityId);
        outState.putString("property_areaid", areaId);*/
        outState.putString("txtRoom_no", txtRoom_no.getText().toString().trim());

        outState.putParcelableArrayList("selectedBedInfo", Common.selectedBedInfo);

        outState.putParcelableArrayList("rcuDetailList", Common.rcuDetails);

    }

    public void setDataOnSavedState(Bundle savedInstanceState) {


        noOfContent = savedInstanceState.getString("noOfCotanant");
        rcuids = savedInstanceState.getStringArray("rcuids");
        isUpdate = savedInstanceState.getBoolean("isUpdate");

        tenantId = savedInstanceState.getString("tenantId");


        edtFirstName.setText(savedInstanceState.getString("tenant_firstName"));
        edtLastName.setText(savedInstanceState.getString("tenant_lastName"));
        edtFatherName.setText(savedInstanceState.getString("tenant_FatherName"));
        edtFatherContactno.setText(savedInstanceState.getString("tenant_FatherContactNo"));
        edtFlatPlotno.setText(savedInstanceState.getString("tenant_FlatPlotno"));
        edtLandMark.setText(savedInstanceState.getString("tenant_LandMark"));

        txtRoom_no.setText(savedInstanceState.getString("txtRoom_no"));

        edtPinCode.setText(savedInstanceState.getString("tenant_PinCode"));
        edtContactno.setText(savedInstanceState.getString("tenant_Contactno"));
        edtEmail.setText(savedInstanceState.getString("tenant_Email"));
        txtHireDate.setText(savedInstanceState.getString("tenant_HireDate"));
        txtLeaveDate.setText(savedInstanceState.getString("tenant_LeaveDate"));

        stateId = savedInstanceState.getString("property_stateid");
        spinnerCity.setText(savedInstanceState.getString("city_id"));
        edtLocation.setText(savedInstanceState.getString("area_id"));
        /*cityId = savedInstanceState.getString("property_cityid");
        areaId = savedInstanceState.getString("property_areaid");*/

        if (Common.rcuDetails != null) {
            Common.rcuDetails.clear();
        }

        if (Common.selectedBedInfo.size() == 0) {
            Common.selectedBedInfo.clear();
            Common.selectedBedInfo = savedInstanceState.getParcelableArrayList("selectedBedInfo");

        }

        Common.rcuDetails = savedInstanceState.getParcelableArrayList("rcuDetailList");


        setListeners();
        updateTenantFrom(Integer.parseInt(tenantId));
        checkEditOrAdd1();

    }

    //data json claass
// seconf json class
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
            Log.d("uu 1", "" + post);
            try {

                Log.d("adv ", "room id json =" +room_id_main_json+" ====room id new =   "+room_new_value);
                Log.d("room id last value","room is is most="+room_id);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action","getadvanceamount"));
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_main_jsop));
                nameValuePairs.add(new BasicNameValuePair("room_id",room_id_main_json));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y"))
                {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i =0; i <jsonarray.length(); i++)
                    {
                        adavnce=jsonarray.getJSONObject(i).getString("advance");
                        tenantroomamount=jsonarray.getJSONObject(i).getString("amount");
                        //ownerid_main_json=jsonarray.getJSONObject(i).getString("owner_id");
                        Log.d("advance payment 1",""+adavnce+" amount value"+tenantroomamount+"   owner id "+ownerid_main_json+"   ");
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
            if (!IsError)
            {
                if (result.equalsIgnoreCase("Y"))
                {

                }else if (result.equals("N")) {
//                    Toast.makeText(TenantDetailActivity.this,"No data found", Toast.LENGTH_LONG).show();
                }
            }
            else {
//                Toast.makeText(TenantDetailActivity.this,"please check network connection", Toast.LENGTH_LONG).show();
            }
        }
    }



//

//................



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
        protected String doInBackground(String... args)
        {

            //connection timeout
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            //.......

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action","ownerendmonthdetails"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", ownerid_main_json));
                nameValuePairs.add(new BasicNameValuePair("user_id",userid_main_json));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",rcuId));
                nameValuePairs.add(new BasicNameValuePair("property_id",property_id_main_jsop));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain =new JSONObject(object);
                resultmain = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (resultmain.equalsIgnoreCase("Y"))
                {
                }
            } catch (Exception e) {
                IsError = true;
                // Log.v("Class Name Function Name Exception", "22" + e.getMessage());
                e.printStackTrace();
            }
            return resultmain;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!IsError) {
                if (result.equalsIgnoreCase("Y"))
                {
                } else if (result.equals("N")) {
                }
            }
            else {
            }
        }
    }

    // for separte tenant login
    public class UpdateUserDetail extends AsyncTask<String, String, String> {

        String namemainvalue,emailmainvalue,mobileNomainvalue;

        public UpdateUserDetail(String namemainvalue, String emailmainvalue, String mobileNomainvalue) {
            this.namemainvalue =namemainvalue;
            this.emailmainvalue=emailmainvalue;
            this.mobileNomainvalue=mobileNomainvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PropertyRCUTenantDetailActivity.this);
            dialog.setMessage("Please wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action","tenantlogin"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",property_id));
            listNameValuePairs.add(new BasicNameValuePair("room_id",room_id));
            listNameValuePairs.add(new BasicNameValuePair("name",namemainvalue));
            listNameValuePairs.add(new BasicNameValuePair("email",emailmainvalue));
            listNameValuePairs.add(new BasicNameValuePair("mobile",mobileNomainvalue));
            listNameValuePairs.add(new BasicNameValuePair("user_role_id","2"));
            listNameValuePairs.add(new BasicNameValuePair("tenantid",rcuId));
            listNameValuePairs.add(new BasicNameValuePair("device_id",mCommon.getDeviceId(PropertyRCUTenantDetailActivity.this)));
            listNameValuePairs.add(new BasicNameValuePair("gsm_registration_id",""));
            listNameValuePairs.add(new BasicNameValuePair("device_type",Common.DEVICE_TYPE));
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

            //dialog.dismiss();
            if (result == null) {

                Toast.makeText(getApplicationContext(),"Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");

                    btnSubmit.setClickable(true);
                    if (status.equalsIgnoreCase("true"))
                    {
                        JSONArray jsonarraydata = obj.getJSONArray("data");
                        for (int i = 0; i <jsonarraydata.length(); i++) {
                            JSONObject jsonobj = jsonarraydata.getJSONObject(i);
                            suserid = jsonobj.getString("user_id");
                        }

                        if(isUpdate){

                        }else{
                            Toast.makeText(getApplicationContext(),"Separate Login Provided and Primary details Save", Toast.LENGTH_SHORT).show();

                        }

                        dialogbooking = new ProgressDialog(PropertyRCUTenantDetailActivity.this);
                        dialogbooking.setMessage("Please wait");
                        dialogbooking.setCancelable(false);
                        dialogbooking.setCanceledOnTouchOutside(false);
                        dialogbooking.show();
                        RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(PropertyRCUTenantDetailActivity.this);
                        service.setCallBack(PropertyRCUTenantDetailActivity.this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, rcuId, "" + tenantId, firstName,
                                lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                                cityId, pinCode,contactNo, email, hireDate,leaveDate,imgTenanto,
                                suserid, property_id, owner_id, room_id, transaction_id, usertypevalue);

                    }
                    else if(status.equalsIgnoreCase("false"))
                    {
                        JSONArray jsonarraydata = obj.getJSONArray("data");
                        for (int i = 0; i <jsonarraydata.length(); i++) {
                            JSONObject jsonobj = jsonarraydata.getJSONObject(i);
                            suserid = jsonobj.getString("user_id");
                        }

                        if(isUpdate){

                        }else{

                            Toast.makeText(getApplicationContext(),"Only Booking", Toast.LENGTH_SHORT).show();

                        }

                        dialogbooking = new ProgressDialog(PropertyRCUTenantDetailActivity.this);
                        dialogbooking.setMessage("Please wait");
                        dialogbooking.setCancelable(false);
                        dialogbooking.setCanceledOnTouchOutside(false);
                        dialogbooking.show();

                        String mainuserid=SharedStorage.getInstance(PropertyRCUTenantDetailActivity.this).getUserId();
                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                        RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(PropertyRCUTenantDetailActivity.this);
                        service.setCallBack(PropertyRCUTenantDetailActivity.this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,rcuId, "" + tenantId, firstName,
                                lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                                cityId, pinCode, contactNo, email, hireDate, leaveDate, imgTenanto,
                                suserid, property_id, owner_id, room_id, transaction_id, usertypevalue);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Already booking with this mobile no,Please Change", Toast.LENGTH_SHORT).show();
                    }

                   //chnages on ashisb 25-09-2018
                    dialog.dismiss();

                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                mCommon.hideKeybord(this, txtBack);
                backpreedialog();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void  backpreedialog()
    {
        final Dialog backpressdialog=new Dialog(PropertyRCUTenantDetailActivity.this);
        backpressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        backpressdialog.setContentView(R.layout.alert_two_btn_dialog);
        backpressdialog.setCancelable(false);
        TextView title = (TextView)backpressdialog.findViewById(R.id.alertTitle);
        TextView message = (TextView)backpressdialog.findViewById(R.id.categoryNameInput);
        Button alertYesBtn = (Button)backpressdialog.findViewById(R.id.alertYesBtn);
        Button alertNoBtn  = (Button)backpressdialog.findViewById(R.id.alertNoBtn);

        title.setText("User Conformation");
        message.setText("Do you want to exit from this screen");

        alertYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpressdialog.dismiss();
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);

            }
        });

        alertNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backpressdialog.dismiss();
                //Intent i=new Intent(PropertyRCUActivity.this,HostelListActivity.class);
                //startActivity(i);


            }
        });

        backpressdialog.show();
    }




    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogAlertDialogWithTwoCallback extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;

        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;
        private String titleStr;
        private String messageStr;


        public CustomDialogAlertDialogWithTwoCallback(PropertyRCUTenantDetailActivity a, int full_screen_dialog) {
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
            alertYesBtn.setOnClickListener(this);
            alertNoBtn.setOnClickListener(this);
            title.setText(titleStr);
            message.setText(messageStr);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alertYesBtn:
                    submitBtnClickEvent();
                   dismiss();
                    break;
                case R.id.alertNoBtn:

                    if(titleStr.equals("1")) {
                        tenantId = "1";
                        btnTenant1.setTextColor(Color.WHITE);
                        btnTenant2.setTextColor(Color.BLACK);
                        btnTenant3.setTextColor(Color.BLACK);
                        btnTenant4.setTextColor(Color.BLACK);
                        btnTenant1.setBackgroundResource(R.color.orange_color);
                        btnTenant2.setBackgroundResource(R.color.white_dark);
                        btnTenant3.setBackgroundResource(R.color.white_dark);
                        btnTenant4.setBackgroundResource(R.color.white_dark);
                        resetAllData();
                    }else if(titleStr.equals("2")){
                        tenantId = "2";
                        btnTenant1.setTextColor(Color.BLACK);
                        btnTenant2.setTextColor(Color.WHITE);
                        btnTenant3.setTextColor(Color.BLACK);
                        btnTenant4.setTextColor(Color.BLACK);
                        btnTenant1.setBackgroundResource(R.color.white_dark);
                        btnTenant2.setBackgroundResource(R.color.orange_color);
                        btnTenant3.setBackgroundResource(R.color.white_dark);
                        btnTenant4.setBackgroundResource(R.color.white_dark);
                        resetAllData();
                    }
                    else if(titleStr.equals("3")){
                        tenantId = "3";
                        btnTenant1.setTextColor(Color.BLACK);
                        btnTenant2.setTextColor(Color.BLACK);
                        btnTenant3.setTextColor(Color.WHITE);
                        btnTenant4.setTextColor(Color.BLACK);
                        btnTenant1.setBackgroundResource(R.color.white_dark);
                        btnTenant2.setBackgroundResource(R.color.white_dark);
                        btnTenant3.setBackgroundResource(R.color.orange_color);
                        btnTenant4.setBackgroundResource(R.color.white_dark);
                        resetAllData();
                    }else if(titleStr.equals("4")){

                        tenantId = "4";
                        btnTenant1.setTextColor(Color.BLACK);
                        btnTenant2.setTextColor(Color.BLACK);
                        btnTenant3.setTextColor(Color.BLACK);
                        btnTenant4.setTextColor(Color.WHITE);
                        btnTenant1.setBackgroundResource(R.color.white_dark);
                        btnTenant2.setBackgroundResource(R.color.white_dark);
                        btnTenant3.setBackgroundResource(R.color.white_dark);
                        btnTenant4.setBackgroundResource(R.color.orange_color);
                        resetAllData();
                    }

                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }


    }




}
