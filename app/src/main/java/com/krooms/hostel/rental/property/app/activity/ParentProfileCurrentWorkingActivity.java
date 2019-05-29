package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.asynctask.UpdateParentProfileAsyncTask;
import com.krooms.hostel.rental.property.app.callback.AreaDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.CityDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.DataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.callback.SpinnerResponce;
import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.ccavenue_adapter.ObjectAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.database.GetAreaAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetCityAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetStatesAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetWorkDetailAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.util.UniversalImageLoaderNew;
import com.krooms.hostel.rental.property.app.util.WebUrls;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by admin on 2/27/2017.
 */
public class ParentProfileCurrentWorkingActivity extends AppCompatActivity implements ServiceResponce, StateDataBaseResponce, CityDataBaseResponce, AreaDataBaseResponce, DataBaseResponce {
    String emailid, mobilenovalue;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    int imagegallery = 1;
    int imageCamera = 2;
    String filepath = "";
    public Uri mImageCaptureUri;
    private static final String SINROM_TEMP_PATH_FROM_CAMERA = Environment.getExternalStorageDirectory() + File.separator + "/captured_temp_image.jpg";
    ImageView cameradataimgshow, applogoImgEditbutton;
    private SharedStorage mSharedStorage;
    private EditText parentFirstName, parentLastName, parentAddressInput, cityview;
    private EditText parentLandmarkInput, pinCodeInput, parentOrganizationInput, parentEmailId;
    private EditText Myareavalue, parentLandlineNumber, husband_wife_name, husband_wife_mobileno;
    private TextView parentMobileNumber = null;
    public String husband_wife_name_mainvalue = "", husband_wife_mobilenovalue = "";
    private Spinner spinnerStates = null, spinnerCity = null, spinnerProfession = null;
    private Button btnSubmit = null;
    private String professionStr = "";
    private AutoCompleteTextView myAutoComplete;
    private Common mCommon = null;
    ImageView loader;
    Animation rotation;
    private String country = "India", countryId = "1";
    private String state, stateId = "0";
    private String city, cityId = "0";
    private String area = "", areaId = "";
    private int falgCitySelect = 0;
    private ArrayList<StateModal> mArrayStates = null;
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = null;
    private ArrayList<StateModal> mArrayProfession = null;
    private String flagFirstTime = "", profileImage = "", profileImageUpload = "";
    private StateSpinnerAdapter mStateSpinnerAdapter, mCitySpinnerAdapter, mProfessionSpinnerAdapter;
    private ObjectAdapter mAdapter = null;
    String husband_wife_namevalue = "";
    private Validation mValidation = null;
    private String userName, lastName, parentfathername, parentAddress, parentLandmark, pinCode,
            parentOrganization, parentEmail, parentLandline, parentMobile;
    File sourceFile;
    ProgressBar images_loader;
    String citynamevalue;
    private String photo_purpose = "";
    private String cropedImageName = null;
    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent;
    String ImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camertadataview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        mSharedStorage = SharedStorage.getInstance(this);
        mValidation = new Validation(this);
        loader = (ImageView) findViewById(R.id.loader);
        images_loader = (ProgressBar) findViewById(R.id.images_loader);
        cameradataimgshow = (ImageView) findViewById(R.id.cameradataimgshow);
        parentFirstName = (EditText) findViewById(R.id.parentFirstNameInput);
        parentLastName = (EditText) findViewById(R.id.parentLastNameInput);
        cityview = (EditText) findViewById(R.id.cityview);
        parentAddressInput = (EditText) findViewById(R.id.parentAddressinput);
        parentLandmarkInput = (EditText) findViewById(R.id.parentLandmarkinput);
        pinCodeInput = (EditText) findViewById(R.id.pinCodeinput);
        parentOrganizationInput = (EditText) findViewById(R.id.parentOrganizationInput);
        parentEmailId = (EditText) findViewById(R.id.parentEmailInput);
        parentLandlineNumber = (EditText) findViewById(R.id.parent_landline_input);
        parentMobileNumber = (TextView) findViewById(R.id.parent_mobile_input);
        husband_wife_name = (EditText) findViewById(R.id.husband_wife_name);
        husband_wife_mobileno = (EditText) findViewById(R.id.husband_wife_mobileno);
        spinnerStates = (Spinner) findViewById(R.id.stateSelection);
        spinnerCity = (Spinner) findViewById(R.id.citySelection);
        Myareavalue = (EditText) findViewById(R.id.Myarea);
        spinnerProfession = (Spinner) findViewById(R.id.profession);

        myAutoComplete = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
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

        new ParentProfileDetails().execute();
        btnSubmit = (Button) findViewById(R.id.button_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPropertyDetail();

            }
        });


        TextView backTitle = (TextView) findViewById(R.id.textView_title);
        backTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // finish();

                Intent intent = new Intent(ParentProfileCurrentWorkingActivity.this, HostelListActivity.class);
                startActivity(intent);

            }
        });

        applogoImgEditbutton = (ImageView) findViewById(R.id.applogoImgEditbutton);
        applogoImgEditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gotoAddProfileImage(imagegallery, imageCamera);
                selectImage();
            }
        });

        cameradataimgshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogClass VehicleRcId = new CustomDialogClass(ParentProfileCurrentWorkingActivity.this);
                VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VehicleRcId.getPerameter(ParentProfileCurrentWorkingActivity.this, "Profile Pic", true);
                VehicleRcId.show();
            }
        });


        setListner();
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

        public CustomDialogClass(ParentProfileCurrentWorkingActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }

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
                Picasso.with(ParentProfileCurrentWorkingActivity.this)
                        .load(profileImage)
                        .placeholder(R.drawable.user_xl)
                        .error(R.drawable.user_xl)
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
                case R.id.btn1:
                    break;
                case R.id.btn2:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }


    }


    public void createPropertyDetail() {
        userName = parentFirstName.getText().toString().trim();
        lastName = parentLastName.getText().toString().trim();
        husband_wife_namevalue = husband_wife_name.getText().toString().trim();
        citynamevalue = cityview.getText().toString().trim();
        husband_wife_mobilenovalue = husband_wife_mobileno.getText().toString().trim();
        // parentfathername = parentFatherNameInput.getText().toString().trim();
        parentAddress = parentAddressInput.getText().toString().trim();
        parentLandmark = parentLandmarkInput.getText().toString().trim();
        pinCode = pinCodeInput.getText().toString().trim();
        parentOrganization = parentOrganizationInput.getText().toString().trim();
        parentEmail = parentEmailId.getText().toString().trim();
        parentLandline = parentLandlineNumber.getText().toString().trim();
        parentMobile = parentMobileNumber.getText().toString().trim();
        areaId = Myareavalue.getText().toString().trim();

        Log.d("area id", areaId);
        if (userName.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_please_enter_your_name_alert), Toast.LENGTH_SHORT).show();
        } else {
            mCommon.hideKeybord(this, parentFirstName);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {

                UpdateParentProfileAsyncTask serviceAsynctask = new UpdateParentProfileAsyncTask(ParentProfileCurrentWorkingActivity.this, "");
                serviceAsynctask.setCallBack(this);
                serviceAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(),
                        userName, lastName, husband_wife_namevalue, husband_wife_mobilenovalue, parentAddress, parentLandmark, stateId, citynamevalue, areaId, pinCode, professionStr,
                        parentOrganization, parentEmail, parentLandline, parentMobile, profileImageUpload);
            }
        }
    }


    @Override
    public void requestResponce(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                mSharedStorage.setUserImage(data.getJSONObject(0).getString("owner_pic"));
                mSharedStorage.setUserFirstName(data.getJSONObject(0).getString("fname"));
                mSharedStorage.setUserLastName(data.getJSONObject(0).getString("lname"));

                //  mSharedStorage.setUserFatherName(parentfathername);
                mSharedStorage.setUserEmail(parentEmail);
                mSharedStorage.setUserMobileNumber(parentMobile);
                mSharedStorage.setLandlineNo(parentLandline);
                mSharedStorage.setOrganizationName(parentOrganization);
                mSharedStorage.setUserAddress(parentAddress);
                mSharedStorage.setLandMark(parentLandmark);
                mSharedStorage.setUserPincode(pinCode);
                mSharedStorage.setProfession(professionStr);
                mSharedStorage.setStateId(stateId);
                mSharedStorage.setCityId(cityId);
                mSharedStorage.setAreaId(areaId);

                Intent mIntent = new Intent(this, HostelListActivity.class);
                mIntent.putExtra("isSkipOption", true);
                mIntent.putExtra("flag", "Parent_First_Time");
                mIntent.putExtra("property_id", "");
                startActivity(mIntent);
                finish();


            } else {
                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }

    }

    public void setListner() {

        // Search state data
        mArrayStates = new ArrayList<>();
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(ParentProfileCurrentWorkingActivity.this, countryId);
        mGetStatesAsyncTask.setCallBack(ParentProfileCurrentWorkingActivity.this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                "SELECT * FROM state where country_id = '" + countryId + "' ORDER BY state_name");
        mStateSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_state, mArrayStates);
        spinnerStates.setAdapter(mStateSpinnerAdapter);

        spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = mArrayStates.get(position).getStrStateName();
                if (position != 0) {
                    stateId = mArrayStates.get(position).getStrSateId();
                    if (falgCitySelect != 0) {
                        cityId = "0";
                        areaId = "0";
                    } else {
                        falgCitySelect++;
                    }
                    GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(ParentProfileCurrentWorkingActivity.this, stateId);
                    mGetCityAsyncTask.setCallBack(ParentProfileCurrentWorkingActivity.this);
                    mGetCityAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    stateId = "0";
                    cityId = "0";
                    areaId = "0";
                    mArrayCity.clear();
                    StateModal mCity = new StateModal();
                    mCity.setStrSateId("0");
                    mCity.setStrStateName("City");
                    mArrayCity.add(mCity);
                    spinnerCity.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Search city data
        mArrayCity = new ArrayList<>();
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(ParentProfileCurrentWorkingActivity.this, stateId);
        mGetCityAsyncTask.setCallBack(ParentProfileCurrentWorkingActivity.this);
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
                myAutoComplete.setText("");
                if (!cityId.equals(mArrayCity.get(position).getStrSateId())) {
                    areaId = "0";
                }
                if (position != 0) {

                    cityId = mArrayCity.get(position).getStrSateId();
                    GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(ParentProfileCurrentWorkingActivity.this, cityId);
                    mGetAreaAsyncTask.setCallBack(ParentProfileCurrentWorkingActivity.this);
                    mGetAreaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mArrayArea.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdapter = new ObjectAdapter(this, mArrayArea);
        mAdapter.setCallBack(new SpinnerResponce() {
            @Override
            public void requestOTPServiceResponce(String id, String value) {
                areaId = id;
                area = value;
                myAutoComplete.setText(area);
                myAutoComplete.dismissDropDown();
            }
        });
        myAutoComplete.setAdapter(mAdapter);
        myAutoComplete.setThreshold(0);

        mArrayProfession = new ArrayList<>();
        GetWorkDetailAsyncTask mGetProfessionTask = new GetWorkDetailAsyncTask(this);
        mGetProfessionTask.setCallBack(this);
        mGetProfessionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        StateModal mWorkDetail = new StateModal();
        mWorkDetail.setStrSateId("0");
        mWorkDetail.setStrStateName("Select Work Detail");
        mArrayProfession.add(mWorkDetail);
//      mArrayProfession.addAll(getProfession());
        mProfessionSpinnerAdapter = new StateSpinnerAdapter(this, R.id.profession, mArrayProfession);
        spinnerProfession.setAdapter(mProfessionSpinnerAdapter);
        spinnerProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                professionStr = mArrayProfession.get(position).getStrStateName();
                if (position != 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (professionStr != null && professionStr.length() > 0) {
            for (int i = 0; i < mArrayProfession.size(); i++) {
                if (mArrayProfession.get(i).getStrStateName().equals(professionStr)) {
                    spinnerProfession.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();

    }

    @Override
    public void requestStateDataBaseResponce(ArrayList<StateModal> mArray) {
        try {
            mArrayStates.addAll(mArray);
            if (mArrayStates != null && mArrayStates.size() > 0) {
                for (int i = 0; i < mArrayStates.size(); i++) {
                    if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                        spinnerStates.setSelection(i);
                    }
                }
            }
            mStateSpinnerAdapter.notifyDataSetChanged();
        } catch (IllegalStateException e) {
            LogConfig.logd("IllegalStateException ", "" + e.getMessage());
        }
    }

    @Override
    public void requestCityDataBaseResponce(ArrayList<StateModal> mArray) {
        try {
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
        } catch (IllegalStateException e) {
            LogConfig.logd("IllegalStateException ", "" + e.getMessage());
        }
    }

    @Override
    public void requestAreaDataBaseResponce(ArrayList<StateModal> mArray) {

        try {
            mArrayArea.clear();
            mArrayArea.addAll(mArray);
            if (mArrayArea != null && mArrayArea.size() > 0) {
                String reariddata = areaId;
                for (int i = 0; i < mArrayArea.size(); i++) {
                    if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                        //spinnerArea.setSelection(i);
                        myAutoComplete.setText("" + mArrayArea.get(i).getStrStateName());
                    }
                }
            } else {
                mArrayArea.clear();
            }

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (IllegalStateException e) {
            LogConfig.logd("IllegalStateException ", "" + e.getMessage());
            e.printStackTrace();
        }
    }


    public void changeStateCityArea() {

        if (mArrayStates != null && mArrayStates.size() > 0) {
            for (int i = 0; i < mArrayStates.size(); i++) {
                if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                    spinnerStates.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();


        if (mArrayCity != null && mArrayCity.size() > 0) {
            for (int i = 0; i < mArrayCity.size(); i++) {
                if (mArrayCity.get(i).getStrSateId().equals(cityId)) {
                    spinnerCity.setSelection(i);
                }
            }
        }
        mCitySpinnerAdapter.notifyDataSetChanged();

        if (mArrayArea != null && mArrayArea.size() > 0) {
            for (int i = 0; i < mArrayArea.size(); i++) {
                if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                    //spinnerArea.setSelection(i);
                    myAutoComplete.setText("" + mArrayArea.get(i).getStrSateId());
                }
            }
        }

        mAdapter.notifyDataSetChanged();

        if (mArrayProfession != null && mArrayProfession.size() > 0) {
            for (int i = 0; i < mArrayProfession.size(); i++) {
                if (mArrayProfession.get(i).getStrStateName().equals(professionStr)) {
                    spinnerProfession.setSelection(i);
                }
            }
        }

        mProfessionSpinnerAdapter.notifyDataSetChanged();

    }

    @Override
    public void requestDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayProfession.clear();
        mArrayProfession.addAll(mArray);
        if (mArrayProfession.size() > 0 && professionStr != null && professionStr.length() > 0) {
            for (int i = 0; i < mArrayProfession.size(); i++) {
                if (mArrayProfession.get(i).getStrStateName().equals(professionStr)) {
                    spinnerProfession.setSelection(i);
                }
            }
        } else if (mArrayProfession != null && mArrayProfession.size() > 0) {
            spinnerProfession.setSelection(0);
        }
        mProfessionSpinnerAdapter.notifyDataSetChanged();
    }


    //this class is used getting value by ownerendmonthdetails webservice for
    private class ParentProfileDetails extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
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
            //connection timeout
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            //.......

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "getparentdetails"));
                nameValuePairs.add(new BasicNameValuePair("user_id", mSharedStorage.getUserId()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
                    JSONArray data = jsmain.getJSONArray("records");
                    mSharedStorage.setUserImage(data.getJSONObject(0).getString("owner_pic"));
                    mSharedStorage.setUserFirstName(data.getJSONObject(0).getString("fname"));
                    mSharedStorage.setUserLastName(data.getJSONObject(0).getString("lname"));
                    mSharedStorage.setUserEmail(data.getJSONObject(0).getString("email_id"));
                    mSharedStorage.setUserMobileNumber(data.getJSONObject(0).getString("mobile"));
                    mSharedStorage.setLandlineNo(data.getJSONObject(0).getString("landline"));
                    mSharedStorage.setOrganizationName(data.getJSONObject(0).getString("organization"));
                    mSharedStorage.setUserAddress(data.getJSONObject(0).getString("address"));
                    mSharedStorage.setLandMark(data.getJSONObject(0).getString("landmark"));
                    mSharedStorage.setUserPincode(data.getJSONObject(0).getString("pincode"));
                    mSharedStorage.setProfession(data.getJSONObject(0).getString("profession"));
                    mSharedStorage.setStateId(data.getJSONObject(0).getString("state"));
                    mSharedStorage.setCityId(data.getJSONObject(0).getString("city"));
                    mSharedStorage.setAreaId(data.getJSONObject(0).getString("area"));
                    emailid = (data.getJSONObject(0).getString("email_id"));
                    mobilenovalue = (data.getJSONObject(0).getString("mobile"));
                    husband_wife_name_mainvalue = data.getJSONObject(0).getString("husband_wife_name");
                    husband_wife_mobilenovalue = data.getJSONObject(0).getString("husband_wife_mobileno");
                    mSharedStorage.setParent_Id(data.getJSONObject(0).getString("parent_id"));

                }
            } catch (Exception e) {
                IsError = true;
                // Log.v("Class Name Function Name Exception", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                setData();
            } else if (result.equals("N")) {
                Toast.makeText(ParentProfileCurrentWorkingActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }

    public void setData() {

        String mobileno = mSharedStorage.getUserMobileNumber();
        String emailid = mSharedStorage.getUserEmail();
        String parentnamevalid = mSharedStorage.getUserFirstName();


        if (parentnamevalid != null && !parentnamevalid.isEmpty() && !parentnamevalid.equals("null")) {
            parentFirstName.setText(mSharedStorage.getUserFirstName());
        }

        if (mSharedStorage.getUserLastName().toString() != null && !mSharedStorage.getUserLastName().toString().isEmpty() && !mSharedStorage.getUserLastName().toString().equals("null")) {
            parentLastName.setText(mSharedStorage.getUserLastName());
        }

        if (husband_wife_name_mainvalue != null && !husband_wife_name_mainvalue.isEmpty() && !husband_wife_name_mainvalue.equals("null")) {
            husband_wife_name.setText(husband_wife_name_mainvalue);
        }

        if (husband_wife_mobilenovalue != null && !husband_wife_mobilenovalue.isEmpty() && !husband_wife_mobilenovalue.equals("null")) {
            husband_wife_mobileno.setText(husband_wife_mobilenovalue);
        }

        parentEmailId.setText(emailid);
        parentMobileNumber.setText(mobileno);

        if (mSharedStorage.getUserAddress() != null && !mSharedStorage.getUserAddress().isEmpty() && !mSharedStorage.getUserAddress().equals("null")) {
            parentAddressInput.setText(mSharedStorage.getUserAddress());
        }

        if (mSharedStorage.getLandMark() != null && !mSharedStorage.getLandMark().isEmpty() && !mSharedStorage.getLandMark().equals("null")) {
            parentLandmarkInput.setText(mSharedStorage.getLandMark());
        }

        if (mSharedStorage.getCityId() != null && !mSharedStorage.getCityId().isEmpty() && !mSharedStorage.getCityId().equals("null")) {
            cityview.setText(mSharedStorage.getCityId());

        }
        if (mSharedStorage.getUserPincode() != null && !mSharedStorage.getUserPincode().isEmpty() && !mSharedStorage.getUserPincode().equals("null")) {
            pinCodeInput.setText(mSharedStorage.getUserPincode());
        }

        if (mSharedStorage.getOrganizationName() != null && !mSharedStorage.getOrganizationName().isEmpty() && !mSharedStorage.getOrganizationName().equals("null")) {
            parentOrganizationInput.setText(mSharedStorage.getOrganizationName());
        }

        if (mSharedStorage.getLandlineNo() != null && !mSharedStorage.getLandlineNo().isEmpty() && !mSharedStorage.getLandlineNo().equals("null")) {
            parentLandlineNumber.setText(mSharedStorage.getLandlineNo());
        }

        if (mSharedStorage.getAreaId() != null && !mSharedStorage.getAreaId().isEmpty() && !mSharedStorage.getAreaId().equals("null")) {
            Myareavalue.setText(mSharedStorage.getAreaId());
        }

        int a = mArrayStates.indexOf("Madhya Pradesh");
        //Log.e("e","mpgovt"+a);
        stateId = mSharedStorage.getStateId();
        cityId = mSharedStorage.getCityId();
        areaId = mSharedStorage.getAreaId();
        professionStr = mSharedStorage.getProfession();


        String imgvaluedata = mSharedStorage.getUserImage();
        if (imgvaluedata != null && !imgvaluedata.isEmpty() && !imgvaluedata.equals("null")) {
            profileImage = WebUrls.IMG_URL + mSharedStorage.getUserImage();
            profileImageUpload = "";
            //UniversalImageLoaderNew.initUniversalImageLoaderOptionsForRoundImage();//old working
            if (profileImage != null && !profileImage.equals("")) {
                try {
                    //   UniversalImageLoaderNew.loadImageFromURI(cameradataimgshow, profileImage, images_loader);//old working

                    Picasso.with(this)
                            .load(profileImage)
                            .placeholder(R.drawable.user_xl)
                            .error(R.drawable.user_xl)
                            .transform(new RoundedTransformation(this))
                            .into(cameradataimgshow);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Urlex=", "" + e);
                }
            }
        }

        changeStateCityArea();

    }

    public void gotoAddProfileImage(final int galleryvalue, final int Cameravalue) {
        final Dialog dialog = new Dialog(ParentProfileCurrentWorkingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_pic_alert_view);
        dialog.setCanceledOnTouchOutside(false);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_light.ttf");

        Button image_cross_layout = (Button) dialog.findViewById(R.id.image_cross_layout);
        image_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button gallerytext = (Button) dialog.findViewById(R.id.upload_gallery);
        Button camera = (Button) dialog.findViewById(R.id.take_camera);
        image_cross_layout.setTypeface(font);
        gallerytext.setTypeface(font);
        camera.setTypeface(font);
        gallerytext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    GetImageFromGallery();
                    dialog.dismiss();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    ClickImageFromCamera();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery() {

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ParentProfileCurrentWorkingActivity.this, HostelListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //Select profile image form camera and gallery

    /*image capture code start*/
    public void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ParentProfileCurrentWorkingActivity.this);
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ParentProfileCurrentWorkingActivity.this, android.R.layout.simple_list_item_1);
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
                            Toast toast = Toast.makeText(ParentProfileCurrentWorkingActivity.this, "Please give internal storage permission", Toast.LENGTH_SHORT);
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
            uri = UtilityImg.getExternalFilesDirForVersion24Above(ParentProfileCurrentWorkingActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(ParentProfileCurrentWorkingActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
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
                        cameradataimgshow.setImageBitmap(bitmap);


                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));

                        profileImageUpload = String.valueOf(finalFile);

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
    //end

}
