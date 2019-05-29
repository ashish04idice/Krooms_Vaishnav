package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.adapter.AddPropertyImagesAdapter;
import com.krooms.hostel.rental.property.app.adapter.MultiChoiceAdapter;
import com.krooms.hostel.rental.property.app.callback.AreaDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.asynctask.AddPropertyImageServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.AddPropertyInformationAsynctask;
import com.krooms.hostel.rental.property.app.callback.AminitesDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.CityDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.FeaturesDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.callback.SpinnerResponce;
import com.krooms.hostel.rental.property.app.ccavenue_adapter.ObjectAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.custom.HorizontalListView;
import com.krooms.hostel.rental.property.app.database.GetAminitiesAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetAreaAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetCityAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetStatesAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.database.GetFeaturesAsyncTask;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

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


public class PropertyInfoFirstActivity extends AppCompatActivity implements View.OnClickListener, FeaturesDataBaseResponce,
        AminitesDataBaseResponce, ServiceResponce, StateDataBaseResponce, CityDataBaseResponce, AreaDataBaseResponce {

    private EditText edtPropertyName = null;
    private EditText edtPropertyAddress = null, edtPropertyDescription;
    private EditText edtPropertyLandMark = null;
    private EditText edtPinCode = null;
    private EditText edtPropertyLat = null;
    private EditText edtPropertyLong = null;
    private EditText edtPropertyTotalArea = null;
    private EditText edtPropertySuperArea = null;
    private EditText videoUrlInput = null;
    private int setFeatures = 0;
    private int falgCitySelect = 0;
    private ScrollView scroll_view_property_info;
    private TextView txtOwnerAddress = null;
    private TextView txtGoogleMap = null;

    private Spinner spinnerState = null;
    private Spinner spinnerCity = null;
    //private Spinner spinnerArea = null;
    private Spinner spinnerPropertyType;
    private Spinner spinnerPropertyFace;

    private Spinner spinnerWaterSupply = null;
    private Spinner spinnerPowerBackup = null;
    private Spinner spinnerKitchen = null;
    private RelativeLayout spinnerKitchenRelativeLayout = null;
    private RelativeLayout relativelayout_feature = null;

    private RadioGroup radiogroupFurnish = null;
    private RadioGroup modularNonModulerlayout = null;
    private TextView txtBack, txtFeatures, txtAminities;
    private LinearLayout mprogressBar = null;
    private HorizontalListView addpropertyImageList = null;
    private Button btnSubmit = null;
    private Button btnSkip = null;
    private Validation mValidation = null;
    private Common mCommon = null;
    private ArrayList<StateModal> mArrayStates = null;
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = null;

    private ArrayList<StateModal> mArrayPropertyType = null;
    private ArrayList<StateModal> mArrayPropertyFace = null;
    private ArrayList<StateModal> mArrayFeaturesList = null;
    private ArrayList<StateModal> mArrayAminities = null;
    private ArrayList<StateModal> mArrayWaterSupply = null;
    private ArrayList<StateModal> mArrayPowerBackup = null;
    private ArrayList<StateModal> mArrayKitchen = null;

    private StateSpinnerAdapter mStateSpinnerAdapter, mCitySpinnerAdapter,
            mPropertyFaceAdapter, mPropertyTypeAdapter, mWaterSupplayAdapter, mPowerBackupAdapter, mKitchen;

    private String country = "India", countryId = "1", state, stateId = "0", city, cityId = "0", area = "", areaId = "0",
            propertyType, propertyType_id, propertyFace, waterSupplay, powerBackup, kitchen = "";
    private SharedStorage mSharedStorage = null;

    private String propertyFeatures, otherAminities;
    //private AlertDialogMultiCheckbox mFeaturesDialog, mAminitiesDialog;
    private ArrayList<PropertyPhotoModal> modalList = new ArrayList<>();
    private AddPropertyImagesAdapter mPropertyImageAdapter;
    private AutoCompleteTextView myAutoComplete;
    private ObjectAdapter mAdapter = null;
    public String flag;
    public String property_id;
    private String furnish;
    private String modularKitchen;
    private boolean isSkipOption = false;
    private int editImagePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info_first);

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

        createViews();
        if (savedInstanceState == null) {
            flag = getIntent().getExtras().getString("flag");
            property_id = getIntent().getExtras().getString("property_id");
            isSkipOption = getIntent().getBooleanExtra("isSkipOption", false);
            // Uopdate form data to set views
            setListeners();
        } else {
            setListeners();
            setDataOnSavedState(savedInstanceState);
        }

    }

    private void createViews() {

        scroll_view_property_info = (ScrollView) findViewById(R.id.scroll_view_property_info);

        edtPropertyName = (EditText) findViewById(R.id.edittext_property_name);
        edtPropertyAddress = (EditText) findViewById(R.id.edittext_property_address);
        edtPropertyDescription = (EditText) findViewById(R.id.edittext_property_description);
        edtPropertyLandMark = (EditText) findViewById(R.id.edittext_property_landmark);
        edtPinCode = (EditText) findViewById(R.id.edittext_property_pincode);
        edtPropertyLat = (EditText) findViewById(R.id.edittext_property_latitude);
        edtPropertyLong = (EditText) findViewById(R.id.edittext_property_longitude);
        edtPropertyTotalArea = (EditText) findViewById(R.id.edittext_property_owner_area);
        edtPropertySuperArea = (EditText) findViewById(R.id.edittext_property_super_area);
        videoUrlInput = (EditText) findViewById(R.id.videoUrlInput);
        txtOwnerAddress = (TextView) findViewById(R.id.textview_sameasowner_address);
        txtGoogleMap = (TextView) findViewById(R.id.textview_google_map);

        txtFeatures = (TextView) findViewById(R.id.spinner_feature_security);
        txtAminities = (TextView) findViewById(R.id.spinner_other_aminities);

        spinnerPropertyType = (Spinner) findViewById(R.id.spinner_property_type);
        spinnerPropertyFace = (Spinner) findViewById(R.id.spinner_property_face);
        spinnerState = (Spinner) findViewById(R.id.spinner_property_state);
        spinnerCity = (Spinner) findViewById(R.id.spinner_property_city);
        //spinnerArea = (Spinner) findViewById(R.id.spinner_property_area);
        spinnerWaterSupply = (Spinner) findViewById(R.id.spinner_water_supplay);
        spinnerPowerBackup = (Spinner) findViewById(R.id.spinner_power_backup);
        relativelayout_feature = (RelativeLayout) findViewById(R.id.relativelayout_feature);
        spinnerKitchenRelativeLayout = (RelativeLayout) findViewById(R.id.KitchenLayout);
        spinnerKitchen = (Spinner) findViewById(R.id.spinner_Kitchen);
        radiogroupFurnish = (RadioGroup) findViewById(R.id.radiogroup_furnish);
        modularNonModulerlayout = (RadioGroup) findViewById(R.id.modularNonModulerlayout);

        mprogressBar = (LinearLayout) findViewById(R.id.layout_progressBar);
        mprogressBar.setVisibility(View.GONE);

        addpropertyImageList = (HorizontalListView) findViewById(R.id.addpropertyImageList);


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

        mPropertyImageAdapter = new AddPropertyImagesAdapter(this, modalList) {
            @Override
            public void updatePropertyImage(int position) {
                editImagePosition = position;

                Intent it = new Intent(PropertyInfoFirstActivity.this, PhotoCaptureWithoutCropActivity.class);
                PropertyInfoFirstActivity.this.startActivityForResult(it, Common.CAMERA_CAPTURE_REQUEST);
            }

            @Override
            public void viewPropertyImage(final int position) {
                if (modalList.size() < 10) {
                    if (position == modalList.size()) {
                        editImagePosition = position;

                        Intent it = new Intent(PropertyInfoFirstActivity.this, PhotoCaptureWithoutCropActivity.class);
                        PropertyInfoFirstActivity.this.startActivityForResult(it, Common.CAMERA_CAPTURE_REQUEST);
                    } else {

                        ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                            @Override
                            public void showImage(ImageView img) {

                                String image_path;

                                if (modalList.get(position).getPhoto_from().equals("server")) {
                                    image_path = WebUrls.IMG_URL + "" + modalList.get(position).getPhoto_Url();
                                } else {
                                    image_path = "file://" + modalList.get(position).getPhoto_Url();
                                }

                                Picasso.with(PropertyInfoFirstActivity.this)
                                        .load(image_path)
                                        .placeholder(R.drawable.ic_default_background)
                                        .error(R.drawable.ic_default_background)
                                        .into(img);

                            }

                            @Override
                            public void downloadImage() {

                            }
                        };
                        vehicleImagePreviewDialog.getPerameter(PropertyInfoFirstActivity.this, "image preview dialog", true);
                        vehicleImagePreviewDialog.show(getSupportFragmentManager(), "image dialog");
                    }
                } else {
                    ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                        @Override
                        public void showImage(ImageView img) {

                            Picasso.with(PropertyInfoFirstActivity.this)
                                    .load(modalList.get(position).getPhoto_Url())
                                    .placeholder(R.drawable.ic_default_background)
                                    .error(R.drawable.ic_default_background)
                                    .into(img);
                        }

                        @Override
                        public void downloadImage() {

                        }
                    };
                    vehicleImagePreviewDialog.getPerameter(PropertyInfoFirstActivity.this, "image preview dialog", true);
                    vehicleImagePreviewDialog.show(getSupportFragmentManager(), "image dialog");
                }
            }
        };
        addpropertyImageList.setAdapter(mPropertyImageAdapter);
        btnSubmit = (Button) findViewById(R.id.button_next);
        btnSkip = (Button) findViewById(R.id.skipBtn);

        if (isSkipOption) {
            btnSkip.setVisibility(View.VISIBLE);
        } else {
            btnSkip.setVisibility(View.GONE);
        }

        txtBack = (TextView) findViewById(R.id.textView_title);
        txtBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }

    private void setListeners() {

        mSharedStorage = SharedStorage.getInstance(this);
        mCommon = new Common();
        mValidation = new Validation(this);

        TextView txtBackTitle = (TextView) findViewById(R.id.textView_title);
        txtBackTitle.setOnClickListener(this);
        txtOwnerAddress.setOnClickListener(this);
        txtGoogleMap.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        // Search state data
        mArrayStates = new ArrayList<>();
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(PropertyInfoFirstActivity.this, countryId);
        mGetStatesAsyncTask.setCallBack(PropertyInfoFirstActivity.this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "SELECT * FROM state where country_id " +
                "= '" + countryId + "' And status = '1' ORDER BY state_name");
        mStateSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_property_state, mArrayStates);
        spinnerState.setAdapter(mStateSpinnerAdapter);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(PropertyInfoFirstActivity.this, stateId);
                    mGetCityAsyncTask.setCallBack(PropertyInfoFirstActivity.this);
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
        mArrayArea = new ArrayList<>();
        mArrayCity = new ArrayList<>();
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(PropertyInfoFirstActivity.this, stateId);
        mGetCityAsyncTask.setCallBack(PropertyInfoFirstActivity.this);
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

                if (position != 0) {

                    if (falgCitySelect != 1) {
                        areaId = "0";
                    } else {
                        falgCitySelect++;
                    }

                    cityId = mArrayCity.get(position).getStrSateId();
                    new GetAreaList(cityId, areaId).execute();
                    //GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(PropertyInfoFirstActivity.this, cityId);
                    // mGetAreaAsyncTask.setCallBack(PropertyInfoFirstActivity.this);
                    //mGetAreaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {

//                    if(!cityId.equals(mArrayCity.get(position).getStrSateId())){
//                        areaId = "0";
//                    }
                    mArrayArea.clear();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Search area data
       /*
        GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(PropertyInfoFirstActivity.this, cityId);
        mGetAreaAsyncTask.setCallBack(PropertyInfoFirstActivity.this);
        mGetAreaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
//        if (mArrayArea.size() == 0) {
//            StateModal mArea = new StateModal();
//            mArea.setStrSateId("0");
//            mArea.setStrStateName("Area");
//            mArrayArea.add(mArea);
//        }
//        mAreaSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_property_area, mArrayArea);
//        spinnerArea.setAdapter(mAreaSpinnerAdapter);
//        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                area = mArrayArea.get(position).getStrStateName();
//                if (position != 0) {
//                    areaId = mArrayArea.get(position).getStrSateId();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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

        mArrayPropertyType = new ArrayList<>();
        mArrayPropertyType.addAll(getPropertyType());
        mPropertyTypeAdapter = new StateSpinnerAdapter(this, R.id.spinner_property_type, mArrayPropertyType);
        spinnerPropertyType.setAdapter(mPropertyTypeAdapter);
        spinnerPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                propertyType = mArrayPropertyType.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                    propertyType_id = mArrayPropertyType.get(position).getStrSateId();
                    if (propertyType_id.equals("1") || propertyType_id.equals("2")) {
                        spinnerKitchenRelativeLayout.setVisibility(View.GONE);
                        kitchen = "";
                        modularKitchen = "";
                    } else {
                        spinnerKitchenRelativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArrayPropertyFace = new ArrayList<>();
        mArrayPropertyFace.addAll(getPropertyFace());
        mPropertyFaceAdapter = new StateSpinnerAdapter(this, R.id.spinner_property_face, mArrayPropertyFace);
        spinnerPropertyFace.setAdapter(mPropertyFaceAdapter);
        spinnerPropertyFace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                propertyFace = mArrayPropertyFace.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArrayWaterSupply = new ArrayList<>();
        mArrayWaterSupply.addAll(getPropertyWaterSupply());
        mWaterSupplayAdapter = new StateSpinnerAdapter(this, R.id.spinner_water_supplay, mArrayWaterSupply);
        spinnerWaterSupply.setAdapter(mWaterSupplayAdapter);
        spinnerWaterSupply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                waterSupplay = mArrayWaterSupply.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mArrayKitchen = new ArrayList<>();
        mArrayKitchen.addAll(getKitchen());
        mKitchen = new StateSpinnerAdapter(this, R.id.spinner_Kitchen, mArrayKitchen);
        spinnerKitchen.setAdapter(mKitchen);
        spinnerKitchen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kitchen = mArrayKitchen.get(position).getStrStateName();

                if (kitchen.equals("Yes")) {
                    modularNonModulerlayout.setVisibility(View.VISIBLE);
                } else {
                    modularNonModulerlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArrayPowerBackup = new ArrayList<>();
        mArrayPowerBackup.addAll(getPropertyPowerBackup());
        mPowerBackupAdapter = new StateSpinnerAdapter(this, R.id.spinner_power_backup, mArrayPowerBackup);
        spinnerPowerBackup.setAdapter(mPowerBackupAdapter);
        spinnerPowerBackup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                powerBackup = mArrayPowerBackup.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ////////////////////////////////// Features ////////////////////////////////////
        mArrayFeaturesList = new ArrayList<>();
        GetFeaturesAsyncTask mGetFeaturesAsyncTask = new GetFeaturesAsyncTask(this);
        mGetFeaturesAsyncTask.setCallBack(this);
        mGetFeaturesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        txtFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mFeaturesDialog.show(getSupportFragmentManager(), "Features");

                CustomDialogClass cdd = new CustomDialogClass(PropertyInfoFirstActivity.this, R.style.full_screen_dialog);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(PropertyInfoFirstActivity.this, "Features", mArrayFeaturesList);
                cdd.show();

            }
        });

        /*mFeaturesDialog = new AlertDialogMultiCheckbox() {
            @Override
            public void callBack(ArrayList<StateModal> modalArray) {


                if (modalArray.size() > 0) {
                    propertyFeatures = " ";
                    String features = "";
                    for (int i = 0; i < modalArray.size(); i++) {

                        modalArrayList.get(i).setChecked(modalArray.get(i).getChecked());
                        if (modalArray.get(i).getChecked() == 1) {

                            if (!modalArray.get(i).getStrStateName().equals("Select All")) {
                                propertyFeatures = propertyFeatures + modalArray.get(i).getStrSateId() + ", ";
                                features = features + modalArray.get(i).getStrStateName() + ", ";
                            }

                        }
                    }

                    Common.Config("list size    " + propertyFeatures);
                    txtFeatures.setText("" + features);

                }
            }
        };*/
        // mFeaturesDialog.getPerameter(this, "Features", mArrayFeaturesList);
        ///////////////////////////////// Features //////////////////////////////////////////


        //////////////////////////////////// Aminities //////////////////////////////////////
        mArrayAminities = new ArrayList<>();
        GetAminitiesAsyncTask mGetAminitiesAsyncTask = new GetAminitiesAsyncTask(this);
        mGetAminitiesAsyncTask.setCallBack(this);
        mGetAminitiesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        txtAminities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAminitiesDialog.show(getSupportFragmentManager(), "Other Amenities");
                CustomDialogClass cdd = new CustomDialogClass(PropertyInfoFirstActivity.this, R.style.full_screen_dialog);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(PropertyInfoFirstActivity.this, "Other Amenities", mArrayAminities);
                cdd.show();
            }
        });

      /*  mAminitiesDialog = new AlertDialogMultiCheckbox() {
            @Override
            public void callBack(ArrayList<StateModal> modalArray) {

                if (modalArray.size() > 0) {
                    otherAminities = "";
                    String aminities = "";
                    for (int i = 0; i < modalArray.size(); i++) {
                        mArrayAminities.get(i).setChecked(modalArray.get(i).getChecked());
                        if (modalArray.get(i).getChecked() == 1) {
                            if (!modalArray.get(i).getStrStateName().equals("Select All")) {
                                otherAminities = otherAminities + modalArray.get(i).getStrSateId() + ", ";
                                aminities = aminities + modalArray.get(i).getStrStateName() + ", ";
                            }
                        }
                    }
                    txtAminities.setText("" + aminities);

                }
            }
        };*/


        //  mAminitiesDialog.getPerameter(this, "Other Amenities", mArrayAminities);
        ////////////////////////////////////// Aminities //////////////////////////////////////////////////////


        radiogroupFurnish.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radiobutton = (RadioButton) radiogroupFurnish.findViewById(checkedId);
                furnish = radiobutton.getText().toString().trim();

                if (furnish.equals("Furnish")) {
                    relativelayout_feature.setVisibility(View.VISIBLE);
                    if (flag.equals("Edit")) {
                        if (setFeatures != 0) {
                            propertyFeatures = "";
                            txtFeatures.setText("");
                        } else {
                            setFeatures = 1;
                        }
                    } else {
                        propertyFeatures = "";
                        txtFeatures.setText("");
                    }

                    for (int i = 0; i < mArrayFeaturesList.size(); i++) {
                        mArrayFeaturesList.get(i).setChecked(0);
                    }

                } else {
                    relativelayout_feature.setVisibility(View.GONE);

                }
            }
        });


        modularNonModulerlayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radiobutton = (RadioButton) modularNonModulerlayout.findViewById(checkedId);
                modularKitchen = radiobutton.getText().toString().trim();
            }
        });
        setEditDataIfHaving();
    }


    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this, edtPinCode);
        switch (v.getId()) {
            case R.id.textView_title:

                if (!isSkipOption) {
                    this.finish();
                    this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {

                    Intent mIntent = new Intent(PropertyInfoFirstActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    this.finish();
                }
                break;
            case R.id.textview_sameasowner_address:

                if (!mSharedStorage.getUserAddress().equals("")) {
                    edtPropertyAddress.setText(mSharedStorage.getUserAddress());
                    stateId = mSharedStorage.getStateId();
                    cityId = mSharedStorage.getCityId();
                    areaId = mSharedStorage.getAreaId();
                    edtPropertyLandMark.setText(mSharedStorage.getLandMark());
                    edtPinCode.setText(mSharedStorage.getUserPincode());

                    changeStateCityArea();
                } else {
                    mCommon.displayAlert(PropertyInfoFirstActivity.this, "Sorry, Owner address not available.", false);
                }

                break;
            case R.id.textview_google_map:


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,  Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    }
                }

                Intent it = new Intent(this, MapViewActivity.class);

                if (city.equals("City")) {
                    it.putExtra("city", "Indore");
                    it.putExtra("city_id", "1");

                } else {
                    it.putExtra("city", city);
                    it.putExtra("city_id", cityId);
                }

                startActivityForResult(it, 10001);
                break;
            case R.id.button_next:
                if (mSharedStorage.getUserId().length() > 0) {
                    submitBtnClickEvent();
                } else {
                    Toast.makeText(this, "Please login, Then complete your process.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.skipBtn:
                Intent mIntent = new Intent(PropertyInfoFirstActivity.this, HostelListActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
                this.finish();
                break;
        }
    }


    public void setEditDataIfHaving() {

        if (flag.equals("Edit")) {

            stateId = "" + PropertyModal.getInstance().getProperty_state();
            cityId = "" + PropertyModal.getInstance().getProperty_city();
            areaId = "" + PropertyModal.getInstance().getProperty_area();

            propertyFeatures = PropertyModal.getInstance().getProperty_featuresStr().trim();
            propertyFeatures = propertyFeatures + " ";
            if (propertyFeatures != null && propertyFeatures.length() > 0) {

                String[] arrayFeature = propertyFeatures.split(", ");
                if (mArrayFeaturesList.size() > 0) {
                    String features = "";
                    for (int i = 0; i < arrayFeature.length; i++) {

                        for (int j = 0; j < mArrayFeaturesList.size(); j++) {
                            if (arrayFeature[i].equals(mArrayFeaturesList.get(j).getStrSateId())) {
                                mArrayFeaturesList.get(j).setChecked(1);
                                features = features + mArrayFeaturesList.get(j).getStrStateName() + ", ";
                            }
                        }
                    }


                    if (arrayFeature.length == (mArrayFeaturesList.size() - 1)) {

                        mArrayFeaturesList.get(0).setChecked(1);
                    }
                    txtFeatures.setText("" + features);
                }
            }

            otherAminities = PropertyModal.getInstance().getProperty_aminitiesStr();
            if (otherAminities != null && otherAminities.length() > 0) {

                String[] arrayAminities = otherAminities.split(", ");
                if (mArrayAminities.size() > 0) {
                    String aminities = "";
                    for (int i = 0; i < arrayAminities.length; i++) {
                        for (int j = 0; j < mArrayAminities.size(); j++) {
                            if (arrayAminities[i].trim().equals(mArrayAminities.get(j).getStrSateId())) {
                                mArrayAminities.get(j).setChecked(1);
                                aminities = aminities + mArrayAminities.get(j).getStrStateName() + ", ";
                            }
                        }
                    }

                    if (arrayAminities.length == (mArrayAminities.size() - 1)) {
                        mArrayAminities.get(0).setChecked(1);
                    }
                    txtAminities.setText("" + aminities);
                }
            }

            if (PropertyModal.getInstance().getProperty_Water_Supplay() != null &&
                    PropertyModal.getInstance().getProperty_Water_Supplay().length() > 0) {
                if (PropertyModal.getInstance().getProperty_Water_Supplay().equals("Municipal")) {
                    spinnerWaterSupply.setSelection(1);
                } else if (PropertyModal.getInstance().getProperty_Water_Supplay().equals("Tubewell")) {
                    spinnerWaterSupply.setSelection(2);
                } else if (PropertyModal.getInstance().getProperty_Water_Supplay().equals("Both")) {
                    spinnerWaterSupply.setSelection(3);
                } else {
                    spinnerWaterSupply.setSelection(0);
                }
            }

            if (PropertyModal.getInstance().getProperty_Power_Backup() != null &&
                    PropertyModal.getInstance().getProperty_Power_Backup().length() > 0) {
                if (PropertyModal.getInstance().getProperty_Power_Backup().equals("No")) {
                    spinnerPowerBackup.setSelection(2);
                } else if (PropertyModal.getInstance().getProperty_Power_Backup() != null &&
                        PropertyModal.getInstance().getProperty_Power_Backup().equals("Yes")) {
                    spinnerPowerBackup.setSelection(1);
                } else {
                    spinnerPowerBackup.setSelection(0);
                }
            }

            if (PropertyModal.getInstance().getProperty_kitchen() != null &&
                    PropertyModal.getInstance().getProperty_kitchen().length() > 0) {
                if (PropertyModal.getInstance().getProperty_kitchen().equals("No")) {
                    spinnerKitchen.setSelection(2);
                } else if (PropertyModal.getInstance().getProperty_kitchen() != null &&
                        PropertyModal.getInstance().getProperty_kitchen().equals("Yes")) {
                    spinnerKitchen.setSelection(1);
                } else {
                    spinnerKitchen.setSelection(0);
                }
            }

            edtPropertyAddress.setText(PropertyModal.getInstance().getProperty_address());
            edtPropertyName.setText(PropertyModal.getInstance().getProperty_name());
            edtPropertyAddress.setText(PropertyModal.getInstance().getProperty_address());
            edtPropertyDescription.setText(PropertyModal.getInstance().getProperty_description());

            if (PropertyModal.getInstance().getPropertyImage().size() >= 1) {
                if (!PropertyModal.getInstance().getPropertyImage().get(0).getPhoto_Url().equals("Not Available")) {
                    modalList.addAll(PropertyModal.getInstance().getPropertyImage());
                } else {


                }

            }

            mPropertyImageAdapter.notifyDataSetChanged();
            try {
                edtPropertyLat.setText("" + PropertyModal.getInstance().getProperty_latlng().latitude);
                edtPropertyLong.setText("" + PropertyModal.getInstance().getProperty_latlng().longitude);
            } catch (NullPointerException e) {
                edtPropertyLat.setText("");
                edtPropertyLong.setText("");
            }
            edtPinCode.setText("" + PropertyModal.getInstance().getProperty_pincode());
            edtPropertyLandMark.setText("" + PropertyModal.getInstance().getProperty_landmark());
            edtPropertyTotalArea.setText("" + PropertyModal.getInstance().getProperty_totalarea());
            edtPropertySuperArea.setText("" + PropertyModal.getInstance().getProperty_Super_builtup_area());

            RadioButton bolModuler = (RadioButton) findViewById(R.id.modular_kitchen);
            RadioButton bolNonModuler = (RadioButton) findViewById(R.id.non_modular_kitchen);
            if (PropertyModal.getInstance().getProperty_kitchen_type() != null &&
                    PropertyModal.getInstance().getProperty_kitchen_type().equals("Modular Kitchen")) {
                bolModuler.setChecked(true);
                bolNonModuler.setChecked(false);
            } else {
                bolModuler.setChecked(false);
                bolNonModuler.setChecked(true);
            }

            RadioButton bFurnish = (RadioButton) findViewById(R.id.person_user);
            RadioButton bNonFurnish = (RadioButton) findViewById(R.id.person_propertyManager);
            if (PropertyModal.getInstance().getProperty_furnish() != null &&
                    PropertyModal.getInstance().getProperty_furnish().equals("Furnish")) {
                bFurnish.setChecked(true);
                bNonFurnish.setChecked(false);
                relativelayout_feature.setVisibility(View.VISIBLE);
            } else {
                relativelayout_feature.setVisibility(View.GONE);
                bFurnish.setChecked(false);
                bNonFurnish.setChecked(true);
            }

            propertyType_id = PropertyModal.getInstance().getProperty_type_id();
            if (spinnerPropertyType != null && mArrayPropertyType.size() > 0
                    && propertyType_id != null && propertyType_id.length() > 0) {
                for (int i = 0; i < mArrayPropertyType.size(); i++) {
                    if (mArrayPropertyType.get(i).getStrSateId().equals(propertyType_id)) {
                        spinnerPropertyType.setSelection(i);
                    }
                }
            }

            String face = PropertyModal.getInstance().getProperty_face();
            if (face != null && face.trim().length() > 0) {
                for (int i = 0; i < mArrayPropertyFace.size(); i++) {
                    if (face.equals(mArrayPropertyFace.get(i).getStrStateName())) {
                        spinnerPropertyFace.setSelection(i);
                    }
                }
            }

            videoUrlInput.setText(PropertyModal.getInstance().getProperty_video_url());
            changeStateCityArea();

        }
    }


    public void submitBtnClickEvent() {

        String propertyName = edtPropertyName.getText().toString().trim();
        String propertyAddress = edtPropertyAddress.getText().toString().trim();
        String propertyLandMark = edtPropertyLandMark.getText().toString().trim();
        String propertyPinCode = edtPinCode.getText().toString().trim();
        String propertyLat = edtPropertyLat.getText().toString().trim();
        String propertyLong = edtPropertyLong.getText().toString().trim();
        String propertyTotalArea = edtPropertyTotalArea.getText().toString().trim();
        String propertySuperArea = edtPropertySuperArea.getText().toString().trim();
        String propertyDescription = edtPropertyDescription.getText().toString().trim();


        if (propertyType.length() == 0 || propertyType.equals("Property Type")) {
            mCommon.displayAlert(this, "Select property type.", false);
        } else if (propertyAddress.length() == 0) {
            mCommon.displayAlert(this, "Enter property address.", false);
        } else if (stateId.equals("0")) {
            mCommon.displayAlert(this, "Select property state.", false);
        } else if (cityId.equals("0")) {
            mCommon.displayAlert(this, "Select property city.", false);
        } else if (areaId.equals("0")) {
            mCommon.displayAlert(this, "Select property area.", false);
        } else {

            if (propertyLat.equals("") || propertyLong.equals("")) {
                if (cityId.equals("1")) {
                    propertyLat = "22.7196";
                    propertyLong = "75.8577";
                } else if (cityId.equals("2")) {
                    propertyLat = "25.2138";
                    propertyLong = "75.8648";
                } else if (cityId.equals("3")) {
                    propertyLat = "23.2599";
                    propertyLong = "77.4126";
                }
                if (cityId.equals("4")) {
                    //for jaipur
                    propertyLat = "26.9124";
                    propertyLong = "75.7872";
                } else if (cityId.equals("5")) {
                    //udaipur
                    propertyLat = "24.5854";
                    propertyLong = "73.7124";
                } else if (cityId.equals("6")) {
                    //bangalore
                    propertyLat = "12.9715";
                    propertyLong = "77.5945";
                } else if (cityId.equals("7")) {
                    //pune
                    propertyLat = "18.5204";
                    propertyLong = "73.8567";
                } else if (cityId.equals("8")) {
                    //greater noida
                    propertyLat = "28.4743";
                    propertyLong = "77.5039";
                } else if (cityId.equals("9")) {
                    //delhi
                    propertyLat = "28.7040";
                    propertyLong = "77.1024";
                }
                //changes by 11/8/17
                //Jabalpur
                else if (cityId.equals("10")) {
                    propertyLat = "23.1815";
                    propertyLong = "79.9864";
                }
                //Gwalior
                else if (cityId.equals("11")) {
                    propertyLat = "26.2183";
                    propertyLong = "78.1828";

                }
                //Ujjain
                else if (cityId.equals("12")) {
                    propertyLat = "23.1793";
                    propertyLong = "75.7849";

                } else if (cityId.equals("13")) {
                    propertyLat = "23.8388";
                    propertyLong = "78.7378";

                } else if (cityId.equals("14")) {
                    propertyLat = "22.9623";
                    propertyLong = "76.0508";
                } else if (cityId.equals("15")) {
                    propertyLat = "24.6005";
                    propertyLong = "80.8322";
                } else if (cityId.equals("16")) {
                    propertyLat = "23.3342";
                    propertyLong = "75.0376";

                } else if (cityId.equals("17")) {
                    propertyLat = "24.5373";
                    propertyLong = "81.3042";

                } else if (cityId.equals("18")) {
                    propertyLat = "23.8308";
                    propertyLong = "80.4072";

                } else if (cityId.equals("19")) {
                    propertyLat = "24.1992";
                    propertyLong = "82.6645";

                } else if (cityId.equals("20")) {
                    propertyLat = "21.3194";
                    propertyLong = "76.2224";

                } else if (cityId.equals("21")) {
                    propertyLat = "21.8257";
                    propertyLong = "76.3526";

                } else if (cityId.equals("22")) {
                    propertyLat = "26.4450";
                    propertyLong = "78.7476";

                } else if (cityId.equals("23")) {
                    propertyLat = "22.0574";
                    propertyLong = "78.9382";

                } else if (cityId.equals("24")) {
                    propertyLat = "24.6348";
                    propertyLong = "77.2980";

                } else if (cityId.equals("25")) {
                    propertyLat = "25.4358";
                    propertyLong = "77.6651";

                } else if (cityId.equals("26")) {
                    propertyLat = "23.9430";
                    propertyLong = "77.8367";

                } else if (cityId.equals("27")) {
                    propertyLat = "24.9164";
                    propertyLong = "79.5812";

                } else if (cityId.equals("28")) {
                    propertyLat = "23.8381";
                    propertyLong = "79.4422";

                } else if (cityId.equals("29")) {
                    propertyLat = "24.0768";
                    propertyLong = "75.0693";

                } else if (cityId.equals("30")) {
                    propertyLat = "21.9029";
                    propertyLong = "75.8069";

                } else if (cityId.equals("31")) {
                    propertyLat = "24.4764";
                    propertyLong = "74.8624";

                } else if (cityId.equals("32")) {
                    propertyLat = "22.6133";
                    propertyLong = "75.6823";

                } else if (cityId.equals("33")) {
                    propertyLat = "22.7441";
                    propertyLong = "77.7370";

                } else if (cityId.equals("34")) {
                    propertyLat = "22.6055";
                    propertyLong = "77.7535";

                } else if (cityId.equals("35")) {
                    propertyLat = "22.9473";
                    propertyLong = "77.1025";

                } else if (cityId.equals("36")) {
                    propertyLat = "21.9672";
                    propertyLong = "77.7452";

                } else if (cityId.equals("37")) {
                    propertyLat = "22.0869";
                    propertyLong = "79.543";
                } else if (cityId.equals("38")) {

                    propertyLat = "25.6845";
                    propertyLong = "78.5661";
                } else if (cityId.equals("39")) {
                    propertyLat = "23.4455";
                    propertyLong = "75.4170";

                }
                //Ajmer
                else if (cityId.equals("40")) {
                    propertyLat = "26.4499";
                    propertyLong = "74.6399";

                } else if (cityId.equals("41")) {
                    propertyLat = "27.5530";
                    propertyLong = "76.6346";

                } else if (cityId.equals("42")) {
                    propertyLat = "23.5461";
                    propertyLong = "74.4350";

                } else if (cityId.equals("43")) {
                    propertyLat = "25.1011";
                    propertyLong = "76.5132";

                } else if (cityId.equals("44")) {
                    propertyLat = "25.7532";
                    propertyLong = "71.4181";

                } else if (cityId.equals("45")) {
                    propertyLat = "27.2170";
                    propertyLong = "77.4895";

                } else if (cityId.equals("46")) {
                    propertyLat = "25.3214";
                    propertyLong = "74.5870";

                } else if (cityId.equals("47")) {
                    propertyLat = "28.0229";
                    propertyLong = "73.3119";
                } else if (cityId.equals("48")) {
                    propertyLat = "25.4305";
                    propertyLong = "75.6499";
                } else if (cityId.equals("49")) {
                    propertyLat = "24.8887";
                    propertyLong = "74.6269";
                } else if (cityId.equals("50")) {
                    propertyLat = "28.2920";
                    propertyLong = "74.9618";
                } else if (cityId.equals("51")) {
                    propertyLat = "26.8932";
                    propertyLong = "76.3375";
                } else if (cityId.equals("52")) {
                    propertyLat = "26.7025";
                    propertyLong = "77.8934";
                } else if (cityId.equals("53")) {
                    propertyLat = "23.8417";
                    propertyLong = "73.7147";
                } else if (cityId.equals("54")) {
                    propertyLat = "29.5815";
                    propertyLong = "74.3294";
                } else if (cityId.equals("55")) {
                    propertyLat = "26.9157";
                    propertyLong = "70.9083";
                } else if (cityId.equals("56")) {
                    propertyLat = "25.1257";
                    propertyLong = "72.1416";
                } else if (cityId.equals("57")) {
                    propertyLat = "24.5973";
                    propertyLong = "76.1610";
                } else if (cityId.equals("58")) {
                    propertyLat = "28.1289";
                    propertyLong = "75.3995";
                } else if (cityId.equals("59")) {
                    propertyLat = "26.2389";
                    propertyLong = "73.0243";
                } else if (cityId.equals("60")) {
                    propertyLat = "26.4883";
                    propertyLong = "74.6399";
                } else if (cityId.equals("61")) {
                    propertyLat = "27.1991";
                    propertyLong = "73.7409";
                } else if (cityId.equals("62")) {
                    propertyLat = "25.7711";
                    propertyLong = "73.3234";
                } else if (cityId.equals("63")) {
                    propertyLat = "25.8973";
                    propertyLong = "81.9453";
                } else if (cityId.equals("64")) {
                    propertyLat = "25.2235";
                    propertyLong = "73.7478";
                } else if (cityId.equals("65")) {
                    propertyLat = "26.0378";
                    propertyLong = "76.3522";
                } else if (cityId.equals("66")) {
                    propertyLat = "27.6094";
                    propertyLong = "75.1399";
                } else if (cityId.equals("67")) {
                    propertyLat = "24.7467";
                    propertyLong = "72.8043";
                } else if (cityId.equals("68")) {
                    propertyLat = "29.9038";
                    propertyLong = "73.8772";
                } else if (cityId.equals("69")) {
                    propertyLat = "26.1624";
                    propertyLong = "75.6208";
                }//Visakhapatnam
                else if (cityId.equals("70")) {
                    propertyLat = "17.6868";
                    propertyLong = "83.2185";
                } else if (cityId.equals("71")) {
                    propertyLat = "16.5062";
                    propertyLong = "80.6480";
                } else if (cityId.equals("72")) {
                    propertyLat = "16.3067";
                    propertyLong = "80.4365";
                } else if (cityId.equals("73")) {
                    propertyLat = "14.4426";
                    propertyLong = "79.9865";
                } else if (cityId.equals("74")) {
                    propertyLat = "15.8281";
                    propertyLong = "78.0373";
                } else if (cityId.equals("75")) {
                    propertyLat = "14.4674";
                    propertyLong = "78.8241";
                } else if (cityId.equals("76")) {
                    propertyLat = "17.0005";
                    propertyLong = "81.8040";
                } else if (cityId.equals("77")) {
                    propertyLat = "16.9891";
                    propertyLong = "82.2475";
                } else if (cityId.equals("78")) {
                    propertyLat = "13.6288";
                    propertyLong = "79.4192";
                } else if (cityId.equals("79")) {
                    propertyLat = "14.6819";
                    propertyLong = "77.6006";
                } else if (cityId.equals("80")) {
                    propertyLat = "18.4059";
                    propertyLong = "83.3362";
                } else if (cityId.equals("81")) {
                    propertyLat = "16.7107";
                    propertyLong = "81.0952";
                } else if (cityId.equals("82")) {
                    propertyLat = "15.5057";
                    propertyLong = "80.0499";
                } else if (cityId.equals("83")) {
                    propertyLat = "15.4786";
                    propertyLong = "78.4831";
                } else if (cityId.equals("84")) {
                    propertyLat = "16.1905";
                    propertyLong = "81.1362";
                } else if (cityId.equals("85")) {
                    propertyLat = "15.6322";
                    propertyLong = "77.2728";
                } else if (cityId.equals("86")) {
                    propertyLat = "16.2395";
                    propertyLong = "80.6493";
                } else if (cityId.equals("87")) {
                    propertyLat = "14.7492";
                    propertyLong = "78.5532";
                } else if (cityId.equals("88")) {
                    propertyLat = "13.2218";
                    propertyLong = "79.1010";
                } else if (cityId.equals("89")) {
                    propertyLat = "13.8185";
                    propertyLong = "77.4989";
                } else if (cityId.equals("90")) {
                    propertyLat = "16.5449";
                    propertyLong = "81.5212";
                } else if (cityId.equals("91")) {
                    propertyLat = "13.5603";
                    propertyLong = "78.5036";
                } else if (cityId.equals("92")) {
                    propertyLat = "15.1674";
                    propertyLong = "77.3736";
                } else if (cityId.equals("93")) {
                    propertyLat = "18.4285";
                    propertyLong = "84.0167";
                } else if (cityId.equals("94")) {
                    propertyLat = "14.4137";
                    propertyLong = "77.7126";
                } else if (cityId.equals("95")) {
                    propertyLat = "16.4410";
                    propertyLong = "80.9926";
                } else if (cityId.equals("96")) {
                    propertyLat = "16.2354";
                    propertyLong = "80.0479";
                } else if (cityId.equals("97")) {
                    propertyLat = "14.9070";
                    propertyLong = "78.0093";
                } else if (cityId.equals("98")) {
                    propertyLat = "16.8138";
                    propertyLong = "81.5212";
                } else if (cityId.equals("99")) {
                    propertyLat = "20.9374";
                    propertyLong = "77.7796";
                } else if (cityId.equals("100")) {
                    propertyLat = "16.0924";
                    propertyLong = "80.1624";

                }//Along
                else if (cityId.equals("101")) {
                    propertyLat = "28.1628";
                    propertyLong = "94.8054";
                } else if (cityId.equals("102")) {
                    propertyLat = "27.9833";
                    propertyLong = "94.6666";
                } else if (cityId.equals("103")) {
                    propertyLat = "27.2645";
                    propertyLong = "92.4159";
                } else if (cityId.equals("104")) {
                    propertyLat = "27.7422";
                    propertyLong = "96.6424";
                } else if (cityId.equals("105")) {
                    propertyLat = "27.9863";
                    propertyLong = "94.2205";
                } else if (cityId.equals("106")) {
                    propertyLat = "27.1935";
                    propertyLong = "95.4699";
                } else if (cityId.equals("107")) {
                    propertyLat = "27.0844";
                    propertyLong = "93.6053";
                } else if (cityId.equals("108")) {
                    propertyLat = "27.3513";
                    propertyLong = "96.0163";
                } else if (cityId.equals("109")) {
                    propertyLat = "26.9929";
                    propertyLong = "95.5014";
                } else if (cityId.equals("110")) {
                    propertyLat = "27.0986";
                    propertyLong = "93.6949";
                } else if (cityId.equals("111")) {
                    propertyLat = "27.6666";
                    propertyLong = "95.8699";
                } else if (cityId.equals("112")) {
                    propertyLat = "28.0619";
                    propertyLong = "95.3259";
                } else if (cityId.equals("113")) {
                    propertyLat = "28.1429";
                    propertyLong = "95.8431";
                } else if (cityId.equals("114")) {
                    propertyLat = "27.3610";
                    propertyLong = "93.0401";
                } else if (cityId.equals("115")) {
                    propertyLat = "27.6325";
                    propertyLong = "91.7539";
                } else if (cityId.equals("116")) {
                    propertyLat = "27.9339";
                    propertyLong = "96.1580";
                } else if (cityId.equals("117")) {
                    propertyLat = "27.5448";
                    propertyLong = "93.8197";
                    //Barpeta
                } else if (cityId.equals("118")) {
                    propertyLat = "26.3216";
                    propertyLong = "90.9821";
                } else if (cityId.equals("119")) {
                    propertyLat = "26.5010";
                    propertyLong = "90.5352";
                } else if (cityId.equals("120")) {
                    propertyLat = "27.4728";
                    propertyLong = "94.9120";
                } else if (cityId.equals("121")) {
                    propertyLat = "26.1445";
                    propertyLong = "91.7362";
                } else if (cityId.equals("122")) {
                    propertyLat = "26.7465";
                    propertyLong = "94.2026";
                } else if (cityId.equals("123")) {
                    propertyLat = "26.2997";
                    propertyLong = "92.6984";
                } else if (cityId.equals("124")) {
                    propertyLat = "24.8333";
                    propertyLong = "92.7789";
                } else if (cityId.equals("125")) {
                    propertyLat = "27.4922";
                    propertyLong = "95.3468";
                } else if (cityId.equals("126")) {
                    propertyLat = "26.6528";
                    propertyLong = "92.7926";
                }//Aurangabad
                else if (cityId.equals("136")) {
                    propertyLat = "24.7500";
                    propertyLong = "84.3700";
                } else if (cityId.equals("137")) {
                    propertyLat = "25.5560";
                    propertyLong = "84.6603";
                } else if (cityId.equals("138")) {
                    propertyLat = "27.1222";
                    propertyLong = "84.0722";
                } else if (cityId.equals("139")) {
                    propertyLat = "25.5647";
                    propertyLong = "83.9777";
                } else if (cityId.equals("140")) {
                    propertyLat = "26.8028";
                    propertyLong = "84.5170";
                } else if (cityId.equals("141")) {
                    propertyLat = "25.1982";
                    propertyLong = "85.5149";
                } else if (cityId.equals("142")) {
                    propertyLat = "25.2533";
                    propertyLong = "86.9890";
                } else if (cityId.equals("143")) {
                    propertyLat = "25.7796";
                    propertyLong = "84.7499";
                } else if (cityId.equals("144")) {
                    propertyLat = "26.1119";
                    propertyLong = "85.8960";
                } else if (cityId.equals("145")) {
                    propertyLat = "25.6117";
                    propertyLong = "85.0467";
                } else if (cityId.equals("146")) {
                    propertyLat = "26.2993";
                    propertyLong = "87.2666";
                } else if (cityId.equals("147")) {
                    propertyLat = "24.7800";
                    propertyLong = "84.9818";
                } else if (cityId.equals("148")) {
                    propertyLat = "25.6858";
                    propertyLong = "85.2146";
                } else if (cityId.equals("149")) {
                    propertyLat = "25.3127";
                    propertyLong = "86.4906";
                } else if (cityId.equals("150")) {
                    propertyLat = "25.1516";
                    propertyLong = "84.9818";
                } else if (cityId.equals("151")) {
                    propertyLat = "26.0917";
                    propertyLong = "87.9384";
                } else if (cityId.equals("152")) {
                    propertyLat = "25.5520";
                    propertyLong = "87.5719";
                } else if (cityId.equals("153")) {
                    propertyLat = "25.3747";
                    propertyLong = "86.4735";
                } else if (cityId.equals("154")) {
                    propertyLat = "26.6470";
                    propertyLong = "84.9089";
                } else if (cityId.equals("155")) {
                    propertyLat = "26.1209";
                    propertyLong = "85.3647";
                } else if (cityId.equals("156")) {
                    propertyLat = "24.7426";
                    propertyLong = "85.5200";
                } else if (cityId.equals("157")) {
                    propertyLat = "25.5941";
                    propertyLong = "85.1376";
                } else if (cityId.equals("158")) {
                    propertyLat = "25.7711";
                    propertyLong = "87.4821";
                } else if (cityId.equals("159")) {
                    propertyLat = "25.8835";
                    propertyLong = "86.6006";
                } else if (cityId.equals("160")) {
                    propertyLat = "24.9535";
                    propertyLong = "84.0117";
                } else if (cityId.equals("161")) {
                    propertyLat = "26.2196";
                    propertyLong = "84.3567";
                } else if (cityId.equals("162")) {
                    propertyLat = "26.5952";
                    propertyLong = "85.4808";
                } else if (cityId.equals("163")) {
                    propertyLat = "23.1355";
                    propertyLong = "83.1818";
                } else if (cityId.equals("164")) {
                    propertyLat = "21.1938";
                    propertyLong = "81.3509";
                } else if (cityId.equals("165")) {
                    propertyLat = "22.0796";
                    propertyLong = "82.1391";
                } else if (cityId.equals("166")) {
                    propertyLat = "21.7384";
                    propertyLong = "81.9480";
                } else if (cityId.equals("167")) {
                    propertyLat = "21.6894";
                    propertyLong = "81.5596";
                } else if (cityId.equals("168")) {
                    propertyLat = "23.2713";
                    propertyLong = "82.5565";
                } else if (cityId.equals("169")) {
                    propertyLat = "23.1880";
                    propertyLong = "82.3542";
                } else if (cityId.equals("170")) {
                    propertyLat = "22.0320";
                    propertyLong = "82.6537";
                } else if (cityId.equals("171")) {
                    propertyLat = "20.7015";
                    propertyLong = "81.5542";
                } else if (cityId.equals("172")) {
                    propertyLat = "20.5831";
                    propertyLong = "81.0810";
                } else if (cityId.equals("173")) {
                    propertyLat = "21.1802";
                    propertyLong = "80.7602";
                } else if (cityId.equals("174")) {
                    propertyLat = "20.97099";
                    propertyLong = "81.83163";
                } else if (cityId.equals("175")) {
                    propertyLat = "19.0741";
                    propertyLong = "82.0080";
                } else if (cityId.equals("176")) {
                    propertyLat = "22.0090";
                    propertyLong = "81.2243";
                } else if (cityId.equals("177")) {
                    propertyLat = "20.1990";
                    propertyLong = "81.0755";
                } else if (cityId.equals("178")) {
                    propertyLat = "19.5959";
                    propertyLong = "81.6638";
                } else if (cityId.equals("179")) {
                    propertyLat = "21.1091";
                    propertyLong = "82.0979";
                } else if (cityId.equals("180")) {
                    propertyLat = "22.0685";
                    propertyLong = "81.6857";
                } else if (cityId.equals("181")) {
                    propertyLat = "23.2139";
                    propertyLong = "82.2013";
                } else if (cityId.equals("182")) {
                    propertyLat = "22.0193";
                    propertyLong = "82.5700";
                } else if (cityId.equals("183")) {
                    propertyLat = "21.2514";
                    propertyLong = "81.6296";
                } else if (cityId.equals("184")) {
                    propertyLat = "21.0971";
                    propertyLong = "81.0302";
                } else if (cityId.equals("185")) {
                    propertyLat = "22.0078";
                    propertyLong = "83.3362";
                } else if (cityId.equals("186")) {
                    propertyLat = "21.5528";
                    propertyLong = "81.7842";
                } else if (cityId.equals("187")) {
                    propertyLat = "15.5889";
                    propertyLong = "73.9654";
                } else if (cityId.equals("188")) {
                    propertyLat = "14.9931";
                    propertyLong = "74.0476";
                } else if (cityId.equals("189")) {
                    propertyLat = "15.1742";
                    propertyLong = "73.9828";
                } else if (cityId.equals("190")) {
                    propertyLat = "15.2417";
                    propertyLong = "74.1123";
                } else if (cityId.equals("191")) {
                    propertyLat = "15.6002";
                    propertyLong = "73.8125";
                } else if (cityId.equals("192")) {
                    propertyLat = "15.2832";
                    propertyLong = "73.9862";
                } else if (cityId.equals("193")) {
                    propertyLat = "15.3874";
                    propertyLong = "73.8154";
                } else if (cityId.equals("194")) {
                    propertyLat = "15.4909";
                    propertyLong = "73.8278";
                } else if (cityId.equals("195")) {
                    propertyLat = "15.7198";
                    propertyLong = "73.7963";
                } else if (cityId.equals("196")) {
                    propertyLat = "15.3991";
                    propertyLong = "74.0124";
                } else if (cityId.equals("197")) {
                    propertyLat = "15.2282";
                    propertyLong = "74.0647";
                } else if (cityId.equals("198")) {
                    propertyLat = "15.2302";
                    propertyLong = "74.1504";
                } else if (cityId.equals("199")) {
                    propertyLat = "15.5583";
                    propertyLong = "74.0124";
                } else if (cityId.equals("200")) {
                    propertyLat = "15.5300";
                    propertyLong = "74.1301";
                } else if (cityId.equals("201")) {
                    propertyLat = "23.0225";
                    propertyLong = "72.5714";
                } else if (cityId.equals("202")) {
                    propertyLat = "21.6032";
                    propertyLong = "71.2221";
                } else if (cityId.equals("203")) {
                    propertyLat = "22.5645";
                    propertyLong = "72.9289";
                } else if (cityId.equals("204")) {
                    propertyLat = "25.2235";
                    propertyLong = "73.7478";
                } else if (cityId.equals("205")) {
                    propertyLat = "24.3455";
                    propertyLong = "71.7622";
                } else if (cityId.equals("206")) {
                    propertyLat = "21.7051";
                    propertyLong = "72.9959";
                } else if (cityId.equals("207")) {
                    propertyLat = "21.7645";
                    propertyLong = "72.1519";
                } else if (cityId.equals("208")) {
                    propertyLat = "22.1704";
                    propertyLong = "71.6684";
                } else if (cityId.equals("209")) {
                    propertyLat = "22.3085";
                    propertyLong = "74.0120";
                } else if (cityId.equals("210")) {
                    propertyLat = "22.8379";
                    propertyLong = "74.2531";
                } else if (cityId.equals("211")) {
                    propertyLat = "20.8254";
                    propertyLong = "73.7007";
                } else if (cityId.equals("212")) {
                    propertyLat = "22.1232";
                    propertyLong = "69.3831";
                } else if (cityId.equals("213")) {
                    propertyLat = "23.2156";
                    propertyLong = "72.6369";
                } else if (cityId.equals("214")) {
                    propertyLat = "21.0119";
                    propertyLong = "70.7168";
                } else if (cityId.equals("215")) {
                    propertyLat = "22.4707";
                    propertyLong = "70.0577";
                } else if (cityId.equals("216")) {
                    propertyLat = "21.5222";
                    propertyLong = "70.4579";
                } else if (cityId.equals("217")) {
                    propertyLat = "23.7337";
                    propertyLong = "69.8597";
                } else if (cityId.equals("218")) {
                    propertyLat = "22.9251";
                    propertyLong = "72.9933";
                } else if (cityId.equals("219")) {
                    propertyLat = "23.1711";
                    propertyLong = "73.5594";
                } else if (cityId.equals("220")) {
                    propertyLat = "23.5880";
                    propertyLong = "72.3693";
                } else if (cityId.equals("221")) {
                    propertyLat = "22.8120";
                    propertyLong = "70.8236";
                } else if (cityId.equals("222")) {
                    propertyLat = "21.8757";
                    propertyLong = "73.5594";
                } else if (cityId.equals("223")) {
                    propertyLat = "20.9467";
                    propertyLong = "72.9520";
                } else if (cityId.equals("224")) {
                    propertyLat = "22.8011";
                    propertyLong = "73.5594";
                } else if (cityId.equals("225")) {
                    propertyLat = "23.8493";
                    propertyLong = "72.1266";
                } else if (cityId.equals("226")) {
                    propertyLat = "21.6417";
                    propertyLong = "69.6293";
                } else if (cityId.equals("227")) {
                    propertyLat = "22.3039";
                    propertyLong = "70.8022";
                } else if (cityId.equals("228")) {
                    propertyLat = "23.8477";
                    propertyLong = "72.9933";
                } else if (cityId.equals("229")) {
                    propertyLat = "21.1702";
                    propertyLong = "72.8311";
                } else if (cityId.equals("230")) {
                    propertyLat = "22.7739";
                    propertyLong = "71.6673";
                } else if (cityId.equals("231")) {
                    propertyLat = "21.0885";
                    propertyLong = "73.4487";
                } else if (cityId.equals("232")) {
                    propertyLat = "22.3072";
                    propertyLong = "73.1812";
                } else if (cityId.equals("233")) {
                    propertyLat = "20.5992";
                    propertyLong = "72.9342";
                } else if (cityId.equals("234")) {
                    propertyLat = "30.3782";
                    propertyLong = "76.7767";
                } else if (cityId.equals("235")) {
                    propertyLat = "28.7752";
                    propertyLong = "75.9928";
                } else if (cityId.equals("236")) {
                    propertyLat = "28.6924";
                    propertyLong = "76.9240";
                } else if (cityId.equals("237")) {
                    propertyLat = "28.4089";
                    propertyLong = "77.3178";
                } else if (cityId.equals("238")) {
                    propertyLat = "28.4595";
                    propertyLong = "77.0266";
                } else if (cityId.equals("239")) {
                    propertyLat = "29.1492";
                    propertyLong = "75.7217";
                } else if (cityId.equals("240")) {
                    propertyLat = "29.3211";
                    propertyLong = "76.3058";
                } else if (cityId.equals("241")) {
                    propertyLat = "29.7857";
                    propertyLong = "76.3985";
                } else if (cityId.equals("242")) {
                    propertyLat = "29.6857";
                    propertyLong = "76.9905";
                } else if (cityId.equals("243")) {
                    propertyLat = "29.3909";
                    propertyLong = "76.9635";
                } else if (cityId.equals("244")) {
                    propertyLat = "30.6942";
                    propertyLong = "76.8606";
                } else if (cityId.equals("245")) {
                    propertyLat = "28.1487";
                    propertyLong = "77.3320";
                } else if (cityId.equals("246")) {
                    propertyLat = "28.8955";
                    propertyLong = "76.6066";
                } else if (cityId.equals("247")) {
                    propertyLat = "28.1928";
                    propertyLong = "76.6239";
                } else if (cityId.equals("248")) {
                    propertyLat = "29.9696";
                    propertyLong = "76.8198";
                } else if (cityId.equals("249")) {
                    propertyLat = "30.1290";
                    propertyLong = "77.2674";
                } else if (cityId.equals("250")) {
                    propertyLat = "31.1521";
                    propertyLong = "76.9686";
                } else if (cityId.equals("251")) {
                    propertyLat = "30.9578";
                    propertyLong = "76.7914";
                } else if (cityId.equals("252")) {
                    propertyLat = "32.4725";
                    propertyLong = "75.9245";
                } else if (cityId.equals("253")) {
                    propertyLat = "31.6377";
                    propertyLong = "77.3441";
                } else if (cityId.equals("254")) {
                    propertyLat = "31.6098";
                    propertyLong = "76.5676";
                } else if (cityId.equals("255")) {
                    propertyLat = "31.8843";
                    propertyLong = "77.1456";
                } else if (cityId.equals("256")) {
                    propertyLat = "31.3407";
                    propertyLong = "76.6875";
                } else if (cityId.equals("257")) {
                    propertyLat = "32.5534";
                    propertyLong = "76.1258";
                } else if (cityId.equals("258")) {
                    propertyLat = "30.9479";
                    propertyLong = "77.5901";
                } else if (cityId.equals("259")) {
                    propertyLat = "32.4312";
                    propertyLong = "76.0131";
                }//Dagshai Cantonment propertyLat long Himachal Pradesh
                else if (cityId.equals("260")) {
                    propertyLat = "30.8864";
                    propertyLong = "77.0521";
                } else if (cityId.equals("261")) {
                    propertyLat = "32.5387";
                    propertyLong = "75.9710";
                } else if (cityId.equals("262")) {
                    propertyLat = "32.5478";
                    propertyLong = "75.9590";
                } else if (cityId.equals("263")) {
                    propertyLat = "31.7747";
                    propertyLong = "75.9985";
                } else if (cityId.equals("264")) {
                    propertyLat = "31.8818";
                    propertyLong = "76.2146";
                } else if (cityId.equals("265")) {
                    propertyLat = "32.2190";
                    propertyLong = "76.3234";
                } else if (cityId.equals("266")) {
                    propertyLat = "31.6622";
                    propertyLong = "76.0595";
                } else if (cityId.equals("267")) {
                    propertyLat = "31.4491";
                    propertyLong = "76.7048";
                } else if (cityId.equals("268")) {
                    propertyLat = "31.6798";
                    propertyLong = "76.5026";
                } else if (cityId.equals("269")) {
                    propertyLat = "32.1433";
                    propertyLong = "75.6871";
                } else if (cityId.equals("270")) {
                    propertyLat = "31.8752";
                    propertyLong = "76.3203";
                } else if (cityId.equals("271")) {
                    propertyLat = "31.4972";
                    propertyLong = "77.7080";
                } else if (cityId.equals("272")) {
                    propertyLat = "31.9912";
                    propertyLong = "76.7899";
                } else if (cityId.equals("273")) {
                    propertyLat = "31.1117";
                    propertyLong = "77.6665";
                } else if (cityId.equals("274")) {
                    propertyLat = "31.1048";
                    propertyLong = "77.1126";
                } else if (cityId.equals("275")) {
                    propertyLat = "32.0998";
                    propertyLong = "76.2691";
                } else if (cityId.equals("276")) {
                    propertyLat = "30.8981";
                    propertyLong = "76.9462";
                } else if (cityId.equals("277")) {
                    propertyLat = "31.1172";
                    propertyLong = "77.5409";
                } else if (cityId.equals("278")) {
                    propertyLat = "31.8246";
                    propertyLong = "77.4702";
                } else if (cityId.equals("279")) {
                    propertyLat = "32.2396";
                    propertyLong = "77.1887";
                } else if (cityId.equals("280")) {
                    propertyLat = "31.5892";
                    propertyLong = "76.9182";
                } else if (cityId.equals("281")) {
                    propertyLat = "31.3819";
                    propertyLong = "76.3441";
                } else if (cityId.equals("282")) {
                    propertyLat = "31.7785";
                    propertyLong = "76.3445";
                } else if (cityId.equals("283")) {
                    propertyLat = "32.1054";
                    propertyLong = "76.3789";
                } else if (cityId.equals("284")) {
                    propertyLat = "30.5599";
                    propertyLong = "77.2955";
                } else if (cityId.equals("285")) {
                    propertyLat = "31.3064";
                    propertyLong = "76.5358";
                } else if (cityId.equals("286")) {
                    propertyLat = "27.8974";
                    propertyLong = "78.0880";
                } else if (cityId.equals("287")) {
                    propertyLat = "31.2578";
                    propertyLong = "77.4602";
                } else if (cityId.equals("288")) {
                    propertyLat = "32.3001";
                    propertyLong = "75.8853";
                } else if (cityId.equals("289")) {
                    propertyLat = "32.1109";
                    propertyLong = "76.5363";
                } else if (cityId.equals("290")) {
                    propertyLat = "30.4453";
                    propertyLong = "77.6021";
                } else if (cityId.equals("291")) {
                    propertyLat = "30.8372";
                    propertyLong = "76.9614";
                } else if (cityId.equals("292")) {
                    propertyLat = "23.8509";
                    propertyLong = "76.7337";
                } else if (cityId.equals("293")) {
                    propertyLat = "28.7893";
                    propertyLong = "79.0250";
                } else if (cityId.equals("294")) {
                    propertyLat = "31.6322";
                    propertyLong = "76.8332";
                } else if (cityId.equals("295")) {
                    propertyLat = "31.2046";
                    propertyLong = "77.7524";
                } else if (cityId.equals("296")) {
                    propertyLat = "30.9754";
                    propertyLong = "76.9902";
                } else if (cityId.equals("297")) {
                    propertyLat = "31.3562";
                    propertyLong = "76.3226";
                } else if (cityId.equals("298")) {
                    propertyLat = "31.6990";
                    propertyLong = "76.7324";
                } else if (cityId.equals("299")) {
                    propertyLat = "22.0869";
                    propertyLong = "79.5435";
                } else if (cityId.equals("300")) {
                    propertyLat = "31.8933";
                    propertyLong = "77.1384";
                } else if (cityId.equals("301")) {
                    propertyLat = "31.1048";
                    propertyLong = "77.1734";
                } else if (cityId.equals("302")) {
                    propertyLat = "30.9045";
                    propertyLong = "77.0967";
                } else if (cityId.equals("303")) {
                    propertyLat = "31.5332";
                    propertyLong = "76.8923";
                }//Talai propertyLatitude logitude himachal pradesh
                else if (cityId.equals("304")) {
                    propertyLat = "32.0842";
                    propertyLong = "77.5711";
                } else if (cityId.equals("305")) {
                    propertyLat = "31.1183";
                    propertyLong = "77.3597";
                } else if (cityId.equals("306")) {
                    propertyLat = "31.8339";
                    propertyLong = "76.5055";
                } else if (cityId.equals("307")) {
                    propertyLat = "31.4684";
                    propertyLong = "76.2708";
                } else if (cityId.equals("308")) {
                    propertyLat = "32.1648";
                    propertyLong = "76.1959";
                } else if (cityId.equals("309")) {
                    propertyLat = "32.8995";
                    propertyLong = "74.7425";
                } else if (cityId.equals("310")) {
                    propertyLat = "34.1595";
                    propertyLong = "74.3587";
                } else if (cityId.equals("311")) {
                    //32.56 74.95
                    propertyLat = "32.7679";
                    propertyLong = "75.0323";
                } else if (cityId.equals("312")) {
                    propertyLat = "33.7782";
                    propertyLong = "76.5762";
                } else if (cityId.equals("313")) {
                    propertyLat = "33.2427";
                    propertyLong = "75.2392";
                } else if (cityId.equals("314")) {
                    propertyLat = "32.5773";
                    propertyLong = "75.0144";
                } else if (cityId.equals("315")) {
                    propertyLat = "23.6693";
                    propertyLong = "86.1511";
                } else if (cityId.equals("316")) {
                    propertyLat = "23.7479";
                    propertyLong = "86.7869";
                } else if (cityId.equals("317")) {
                    propertyLat = "24.2065";
                    propertyLong = "84.8724";
                } else if (cityId.equals("318")) {
                    propertyLat = "22.6765";
                    propertyLong = "85.6255";
                } else if (cityId.equals("319")) {
                    propertyLat = "22.5474";
                    propertyLong = "85.8025";
                } else if (cityId.equals("320")) {
                    propertyLat = "24.4763";
                    propertyLong = "86.6913";
                } else if (cityId.equals("321")) {
                    propertyLat = "23.7957";
                    propertyLong = "86.4304";
                } else if (cityId.equals("322")) {
                    propertyLat = "24.2855";
                    propertyLong = "87.2419";
                } else if (cityId.equals("323")) {
                    propertyLat = "23.0441";
                    propertyLong = "84.5379";
                } else if (cityId.equals("324")) {
                    propertyLat = "23.8081";
                    propertyLong = "85.8230";
                } else if (cityId.equals("325")) {
                    propertyLat = "24.8255";
                    propertyLong = "87.2135";
                } else if (cityId.equals("326")) {
                    propertyLat = "24.2841";
                    propertyLong = "86.0937";
                } else if (cityId.equals("327")) {
                    propertyLat = "24.1549";
                    propertyLong = "83.7996";
                } else if (cityId.equals("328")) {
                    propertyLat = "23.9966";
                    propertyLong = "85.3691";
                } else if (cityId.equals("329")) {
                    propertyLat = "22.8046";
                    propertyLong = "86.2029";
                } else if (cityId.equals("330")) {
                    propertyLat = "24.4289";
                    propertyLong = "85.5355";
                } else if (cityId.equals("331")) {
                    propertyLat = "23.4338";
                    propertyLong = "84.6479";
                } else if (cityId.equals("332")) {
                    propertyLat = "24.2654";
                    propertyLong = "86.6480";
                } else if (cityId.equals("333")) {
                    propertyLat = "24.0420";
                    propertyLong = "84.0907";
                } else if (cityId.equals("334")) {
                    propertyLat = "24.6337";
                    propertyLong = "87.8501";
                } else if (cityId.equals("335")) {
                    propertyLat = "23.7623";
                    propertyLong = "86.0021";
                } else if (cityId.equals("336")) {
                    propertyLat = "23.6524";
                    propertyLong = "85.5612";
                } else if (cityId.equals("337")) {
                    propertyLat = "23.3441";
                    propertyLong = "85.3096";
                } else if (cityId.equals("338")) {
                    propertyLat = "24.9802";
                    propertyLong = "87.6186";
                } else if (cityId.equals("339")) {
                    propertyLat = "23.6586";
                    propertyLong = "85.3445";
                } else if (cityId.equals("340")) {
                    propertyLat = "15.8497";
                    propertyLong = "74.4977";
                } else if (cityId.equals("341")) {
                    propertyLat = "13.8330";
                    propertyLong = "75.7081";
                } else if (cityId.equals("342")) {
                    propertyLat = "15.1394";
                    propertyLong = "76.9214";
                } else if (cityId.equals("343")) {
                    propertyLat = "17.9149";
                    propertyLong = "77.5046";
                } else if (cityId.equals("344")) {
                    propertyLat = "15.9186";
                    propertyLong = "75.6761";
                } else if (cityId.equals("345")) {
                    propertyLat = "16.8302";
                    propertyLong = "75.7100";
                } else if (cityId.equals("346")) {
                    propertyLat = "17.8721";
                    propertyLong = "76.9470";
                } else if (cityId.equals("347")) {
                    propertyLat = "13.1623";
                    propertyLong = "75.8679";
                } else if (cityId.equals("348")) {
                    propertyLat = "17.8721";
                    propertyLong = "76.9470";
                } else if (cityId.equals("349")) {
                    propertyLat = "14.1823";
                    propertyLong = "76.5488";
                } else if (cityId.equals("350")) {
                    propertyLat = "12.6492";
                    propertyLong = "77.2003";
                } else if (cityId.equals("351")) {
                    propertyLat = "14.4663";
                    propertyLong = "75.9238";
                } else if (cityId.equals("352")) {
                    propertyLat = "15.4589";
                    propertyLong = "75.0078";
                } else if (cityId.equals("353")) {
                    propertyLat = "13.2417";
                    propertyLong = "77.7137";
                } else if (cityId.equals("354")) {
                    propertyLat = "17.3297";
                    propertyLong = "76.8343";
                } else if (cityId.equals("355")) {
                    propertyLat = "15.4319";
                    propertyLong = "76.5315";
                } else if (cityId.equals("356")) {
                    propertyLat = "15.4325";
                    propertyLong = "75.6380";
                } else if (cityId.equals("357")) {
                    propertyLat = "15.4026";
                    propertyLong = "75.6208";
                } else if (cityId.equals("358")) {
                    propertyLat = "13.0068";
                    propertyLong = "76.0996";
                } else if (cityId.equals("359")) {
                    propertyLat = "15.3350";
                    propertyLong = "76.4600";
                } else if (cityId.equals("360")) {
                    propertyLat = "15.2689";
                    propertyLong = "76.3909";
                } else if (cityId.equals("361")) {
                    propertyLat = "15.3647";
                    propertyLong = "75.1240";
                } else if (cityId.equals("362")) {
                    propertyLat = "14.5305";
                    propertyLong = "75.8011";
                } else if (cityId.equals("363")) {
                    propertyLat = "13.0730";
                    propertyLong = "77.7967";
                } else if (cityId.equals("364")) {
                    propertyLat = "13.2130";
                    propertyLong = "75.9942";
                } else if (cityId.equals("365")) {
                    propertyLat = "15.9563";
                    propertyLong = "76.1146";
                } else if (cityId.equals("366")) {
                    propertyLat = "12.9585";
                    propertyLong = "78.2710";
                } else if (cityId.equals("367")) {
                    propertyLat = "15.6219";
                    propertyLong = "76.1784";
                } else if (cityId.equals("368")) {
                    propertyLat = "13.1770";
                    propertyLong = "78.2020";
                } else if (cityId.equals("369")) {
                    propertyLat = "13.6316";
                    propertyLong = "74.6900";
                } else if (cityId.equals("370")) {
                    propertyLat = "14.8185";
                    propertyLong = "74.1416";
                } else if (cityId.equals("371")) {
                    propertyLat = "13.3605";
                    propertyLong = "74.7864";
                } else if (cityId.equals("372")) {
                    propertyLat = "12.9141";
                    propertyLong = "74.8560";
                } else if (cityId.equals("373")) {
                    propertyLat = "12.4244";
                    propertyLong = "75.7382";
                } else if (cityId.equals("374")) {
                    propertyLat = "12.2958";
                    propertyLong = "76.6394";
                } else if (cityId.equals("375")) {
                    propertyLat = "15.9488";
                    propertyLong = "75.8164";
                } else if (cityId.equals("376")) {
                    propertyLat = "12.9551";
                    propertyLong = "78.2699";
                } else if (cityId.equals("377")) {
                    propertyLat = "16.2120";
                    propertyLong = "77.3439";
                } else if (cityId.equals("378")) {
                    propertyLat = "14.6113";
                    propertyLong = "75.6383";
                } else if (cityId.equals("379")) {
                    propertyLat = "13.9299";
                    propertyLong = "75.5681";
                } else if (cityId.equals("380")) {
                    propertyLat = "15.0874";
                    propertyLong = "76.5477";
                } else if (cityId.equals("381")) {
                    propertyLat = "13.4198";
                    propertyLong = "75.2567";
                } else if (cityId.equals("382")) {
                    propertyLat = "12.4216";
                    propertyLong = "76.6931";
                } else if (cityId.equals("383")) {
                    propertyLat = "13.3392";
                    propertyLong = "77.1140";
                } else if (cityId.equals("384")) {
                    propertyLat = "13.3409";
                    propertyLong = "74.7421";
                } else if (cityId.equals("385")) {
                    propertyLat = "14.7937";
                    propertyLong = "74.6869";
                } else if (cityId.equals("386")) {
                    propertyLat = "16.7602";
                    propertyLong = "77.1428";
                } else if (cityId.equals("387")) {
                    propertyLat = "19.8762";
                    propertyLong = "75.3433";
                } else if (cityId.equals("388")) {
                    propertyLat = "20.7059";
                    propertyLong = "77.0219";
                } else if (cityId.equals("389")) {
                    propertyLat = "20.9042";
                    propertyLong = "74.7749";
                } else if (cityId.equals("390")) {
                    propertyLat = "21.0077";
                    propertyLong = "75.5626";
                } else if (cityId.equals("391")) {
                    propertyLat = "16.7050";
                    propertyLong = "74.2433";
                } else if (cityId.equals("392")) {
                    propertyLat = "20.5547";
                    propertyLong = "74.5100";
                } else if (cityId.equals("393")) {
                    propertyLat = "19.0760";
                    propertyLong = "72.8777";
                } else if (cityId.equals("394")) {
                    propertyLat = "21.1458";
                    propertyLong = "79.0882";
                } else if (cityId.equals("395")) {
                    propertyLat = "19.9975";
                    propertyLong = "73.7898";
                } else if (cityId.equals("396")) {
                    propertyLat = "17.6599";
                    propertyLong = "75.9064";
                } else if (cityId.equals("397")) {
                    propertyLat = "31.6340";
                    propertyLong = "74.8723";
                } else if (cityId.equals("398")) {
                    propertyLat = "30.2110";
                    propertyLong = "74.9455";
                } else if (cityId.equals("399")) {
                    propertyLat = "30.3819";
                    propertyLong = "75.5468";
                } else if (cityId.equals("400")) {
                    propertyLat = "30.9331";
                    propertyLong = "74.6225";
                } else if (cityId.equals("401")) {
                    propertyLat = "31.5143";
                    propertyLong = "75.9115";
                } else if (cityId.equals("402")) {
                    propertyLat = "31.3260";
                    propertyLong = "75.5762";
                } else if (cityId.equals("403")) {
                    propertyLat = "30.9010";
                    propertyLong = "75.8573";
                } else if (cityId.equals("404")) {
                    propertyLat = "30.7046";
                    propertyLong = "76.7179";
                } else if (cityId.equals("405")) {
                    propertyLat = "30.3398";
                    propertyLong = "76.3869";
                } else if (cityId.equals("406")) {
                    propertyLat = "32.2643";
                    propertyLong = "75.6421";
                } else if (cityId.equals("407")) {
                    propertyLat = "27.1767";
                    propertyLong = "78.0081";
                } else if (cityId.equals("408")) {
                    propertyLat = "25.4358";
                    propertyLong = "81.8463";
                } else if (cityId.equals("409")) {
                    propertyLat = "26.7880";
                    propertyLong = "82.1986";
                } else if (cityId.equals("410")) {
                    propertyLat = "25.8500";
                    propertyLong = "80.8987";
                } else if (cityId.equals("411")) {
                    propertyLat = "25.8500";
                    propertyLong = "80.8987";
                } else if (cityId.equals("412")) {
                    propertyLat = "26.7347";
                    propertyLong = "83.3362";
                } else if (cityId.equals("413")) {
                    propertyLat = "25.4484";
                    propertyLong = "78.5685";
                } else if (cityId.equals("414")) {
                    propertyLat = "26.4499";
                    propertyLong = "80.3319";
                } else if (cityId.equals("415")) {
                    propertyLat = "26.8467";
                    propertyLong = "80.9462";
                } else if (cityId.equals("416")) {
                    propertyLat = "28.9845";
                    propertyLong = "77.7064";
                } else if (cityId.equals("417")) {
                    propertyLat = "25.3176";
                    propertyLong = "82.9739";
                }
                //Kolkata
                else if (cityId.equals("418")) {
                    propertyLat = "22.5726";
                    propertyLong = "88.3639";
                } else if (cityId.equals("419")) {
                    propertyLat = "30.7333";
                    propertyLong = "76.7794";
                } else if (cityId.equals("420")) {
                    propertyLat = "13.0827";
                    propertyLong = "80.2707";
                } else if (cityId.equals("421")) {
                    propertyLat = "11.0168";
                    propertyLong = "76.9558";
                } else if (cityId.equals("422")) {
                    propertyLat = "9.9252";
                    propertyLong = "78.1198";
                } else if (cityId.equals("423")) {
                    propertyLat = "11.4064";
                    propertyLong = "76.6932";
                } else if (cityId.equals("424")) {
                    propertyLat = "9.2876";
                    propertyLong = "79.3129";
                } else if (cityId.equals("425")) {
                    propertyLat = "10.7905";
                    propertyLong = "78.7047";
                } else if (cityId.equals("426")) {
                    propertyLat = "11.1085";
                    propertyLong = "77.3411";
                }
            }


            String features = "", aminities = "";
            if (propertyFeatures != null && propertyFeatures.length() > 0) {
                features = propertyFeatures;
            }
            if (features.equals("Select Features")) {
                features = "0";
            }

            if (otherAminities != null && otherAminities.length() > 0) {
                aminities = otherAminities;
            }
            if (aminities.equals("Select Aminities")) {
                aminities = "0";
            }

            mCommon.hideKeybord(this, edtPropertySuperArea);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else if (!mValidation.isValidLat(Double.parseDouble(propertyLat))) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_valid_lat), false);
            } else if (!mValidation.isValidLng(Double.parseDouble(propertyLong))) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_valid_long), false);
            } else {
                mprogressBar.setVisibility(View.VISIBLE);
                AddPropertyInformationAsynctask service = new AddPropertyInformationAsynctask(this);
                service.setCallBack(this);
                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        mSharedStorage.getUserId(), mSharedStorage.getUserId(), propertyType_id,
                        propertyName, "nature", propertyAddress, propertyLandMark, stateId, cityId, areaId, propertyPinCode,
                        propertyLat, propertyLong, propertyTotalArea, propertySuperArea, propertyFace, features,
                        "property-list", waterSupplay, powerBackup, aminities, furnish, property_id, kitchen,
                        modularKitchen, propertyDescription, videoUrlInput.getText().toString());

            }

        }
    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        Common.Config("result         " + result);
        LogConfig.logd("Property information response =", "" + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    String propertyId = "" + jsonObject.getInt("data");
                    // mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                   /* DataBaseAdapter mDBAdapter = new DataBaseAdapter(this);
                    mDBAdapter.createDatabase();
                    mDBAdapter.open();
                    for (int i = 0; i < modalList.size(); i++) {
                        String image_id = modalList.get(i).getPhoto_id()*//* + "" + i*//*;
                        Cursor mCursor = mDBAdapter.getSqlqureies_new("SELECT * FROM property_images " +
                                "where image_no = '" + (1 + i) + "'" + "AND property_id='" + propertyId + "'");

                        if (mCursor.getCount() > 0) {


                        } else {

                            ContentValues cv = new ContentValues();
                            cv.put("user_id", mSharedStorage.getUserId());
                            cv.put("property_id", propertyId);
                            cv.put("property_image", modalList.get(i).getPhoto_Url());
                            cv.put("property_image_id", image_id);
                            if (modalList.get(i).getPhoto_from().equals("server")) {
                                cv.put("status", "1");
                            } else {
                                cv.put("status", "0");
                            }
                            cv.put("image_no", "" + (1 + i));
                            mDBAdapter.insertPropertyImagesInfo("property_images", cv);
                        }
                    }
                    mDBAdapter.close();*/
                    mSharedStorage.setUserPropertyId("" + propertyId);

                    /*Intent mIntent = new Intent(this, PropertyInfoSecondActivity.class);*/
                    Intent mIntent = new Intent(this, PropertyImageUploadActivity.class);
                    mIntent.putExtra("flag", flag);
                    mIntent.putExtra("PropertyId", "" + propertyId);
                    mIntent.putExtra("isSkipOption", isSkipOption);
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    callSlider();
                    finish();
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }

    }

    public void callSlider() {

        mSharedStorage.setAddCount("1");
        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }

    @Override
    public void requestFeaturesDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayFeaturesList.addAll(mArray);
        if (propertyFeatures != null && propertyFeatures.length() > 0) {
            String[] arrayFeature = propertyFeatures.split(", ");
            if (mArrayFeaturesList.size() > 0) {
                String features = "";
                for (int i = 0; i < arrayFeature.length; i++) {
                    for (int j = 0; j < mArrayFeaturesList.size(); j++) {
                        if (arrayFeature[i].equals(mArrayFeaturesList.get(j).getStrSateId())) {
                            mArrayFeaturesList.get(j).setChecked(1);
                            features = features + mArrayFeaturesList.get(j).getStrStateName() + ", ";
                        }
                    }
                }
                if (arrayFeature.length == (mArrayFeaturesList.size() - 1)) {

                    mArrayFeaturesList.get(0).setChecked(1);
                }
                txtFeatures.setText("" + features);
            }
        }
    }

    @Override
    public void requestAminitesDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayAminities.addAll(mArray);
        if (otherAminities != null && otherAminities.length() > 0) {

            String[] arrayAminities = otherAminities.split(", ");
            if (mArrayAminities.size() > 0) {
                String aminities = "";
                for (int i = 0; i < arrayAminities.length; i++) {
                    for (int j = 0; j < mArrayAminities.size(); j++) {
                        if (arrayAminities[i].trim().equals(mArrayAminities.get(j).getStrSateId())) {
                            mArrayAminities.get(j).setChecked(1);
                            aminities = aminities + mArrayAminities.get(j).getStrStateName() + ", ";
                        }
                    }
                }
                if (arrayAminities.length == (mArrayAminities.size() - 1)) {

                    mArrayAminities.get(0).setChecked(1);
                }
                txtAminities.setText("" + aminities);
            }
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

    @Override
    public void requestCityDataBaseResponce(ArrayList<StateModal> mArray) {
        try {
            mArrayCity.clear();
            mArrayCity.addAll(mArray);
            if (mArrayCity != null && mArrayCity.size() > 0) {
                for (int i = 0; i < mArrayCity.size(); i++) {
                    if (mArrayCity.get(i).getStrSateId().equals(cityId)) {
                        spinnerCity.setSelection(i);
                        city = mArrayCity.get(i).getStrStateName();
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
        }
    }

    public void changeStateCityArea() {
        try {
            if (mArrayStates != null && mArrayStates.size() > 0) {
                for (int i = 0; i < mArrayStates.size(); i++) {
                    if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                        spinnerState.setSelection(i);
                        state = mArrayStates.get(i).getStrStateName();
                    }
                }
            }
            mStateSpinnerAdapter.notifyDataSetChanged();

            if (mArrayCity != null && mArrayCity.size() > 0) {
                for (int i = 0; i < mArrayCity.size(); i++) {
                    if (mArrayCity.get(i).getStrSateId().equals(cityId)) {
                        spinnerCity.setSelection(i);
                        city = mArrayCity.get(i).getStrStateName();
                    }
                }
            }
            mCitySpinnerAdapter.notifyDataSetChanged();

            if (mArrayArea != null && mArrayArea.size() > 0) {
                for (int i = 0; i < mArrayArea.size(); i++) {
                    if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                        //spinnerArea.setSelection(i);
                        area = mArrayArea.get(i).getStrStateName();
                        myAutoComplete.setText("" + mArrayArea.get(i).getStrStateName());
                    }
                }
            }

            mAdapter.notifyDataSetChanged();
        } catch (IllegalStateException e) {
            LogConfig.logd("IllegalStateException ", "" + e.getMessage());
        }

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
        mStateModal.setStrStateName("Paying Guest");
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

    private ArrayList<StateModal> getPropertyPowerBackup() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Select Backup");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Yes");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("No");
        mArray.add(mStateModal);

        return mArray;
    }

    private ArrayList<StateModal> getKitchen() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Select Kitchen");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Yes");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("No");
        mArray.add(mStateModal);

        return mArray;
    }

    private ArrayList<StateModal> getPropertyWaterSupply() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Select Supply");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Municipal");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Tubewell");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Both");
        mArray.add(mStateModal);

        return mArray;
    }

    private ArrayList<StateModal> getPropertyFace() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Property Face");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("East");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("West");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("North");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("South");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("East-South");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("South-West");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("West-North");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("East-North");
        mArray.add(mStateModal);

        return mArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    case Common.CAMERA_CAPTURE_REQUEST:
//                        setCaptureImages();
                        setCaptureImage();
                        break;

                    case 10001:
                        edtPropertyLat.setText("" + data.getExtras().getString("lat"));
                        edtPropertyLong.setText("" + data.getExtras().getString("lng"));
                        break;
                  /*  case Common.CAMERA_CAPTURE_REQUEST_EDIT:

                        Common.Config("  image cpture   " + data);
                        updateCaptureImages();
                        break;*/
                }
            } else {
                switch (requestCode) {
                    case Common.CAMERA_CAPTURE_REQUEST:


//                        setCaptureImages();
                        setCaptureImage();
                        break;
                    /*case Common.CAMERA_CAPTURE_REQUEST_EDIT:

                        Common.Config("  image cpture   " + data);
                        updateCaptureImages();
                        break;*/
                }

            }
        } catch (Exception e) {
        }
    }

    private void setCaptureImage() {


        if (!Common.profileImagePath.equals("")) {

            Common.Config("image path     " + Common.profileImagePath);

            try {

                if (modalList.size() == 0) {

                    PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                    modal.setPhoto_id("");
                    modal.setPhoto_Url(Common.profileImagePath);
                    modal.setPhoto_from("local");
                    modalList.add(modal);

                    if (flag.equals("Edit")) {

                        AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                        mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                            @Override
                            public void requestResponce(String result) {
                                responseParse(result);
                            }
                        });
                        mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                mSharedStorage.getUserId(), PropertyModal.getInstance().getProperty_id(), Common.profileImagePath, "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                    }

                } else {
                    if (editImagePosition < modalList.size()) {

                        if (!modalList.get(editImagePosition).getPhoto_id().equals("")) {
                            PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                            modal.setPhoto_id(modalList.get(editImagePosition).getPhoto_id());
                            modal.setPhoto_Url(Common.profileImagePath);
                            modal.setPhoto_from("local");
                            modalList.set(editImagePosition, modal);
                            if (flag.equals("Edit")) {

                                AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                                mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                                    @Override
                                    public void requestResponce(String result) {
                                        responseParse(result);
                                    }
                                });
                                mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                        mSharedStorage.getUserId(), PropertyModal.getInstance().getProperty_id(), Common.profileImagePath, "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                            }
                        } else {


                            PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                            modal.setPhoto_id("");
                            modal.setPhoto_Url(Common.profileImagePath);
                            modal.setPhoto_from("local");
                            modalList.set(editImagePosition, modal);
                            if (flag.equals("Edit")) {

                                AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                                mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                                    @Override
                                    public void requestResponce(String result) {
                                        responseParse(result);
                                    }
                                });
                                mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                        mSharedStorage.getUserId(), PropertyModal.getInstance().getProperty_id(), Common.profileImagePath, "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                            }
                        }
                    } else {

                        PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                        modal.setPhoto_id("");
                        modal.setPhoto_Url(Common.profileImagePath);
                        modal.setPhoto_from("local");
                        modalList.add(modal);
                        if (flag.equals("Edit")) {

                            AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                            mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                                @Override
                                public void requestResponce(String result) {
                                    responseParse(result);
                                }
                            });
                            mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    mSharedStorage.getUserId(), PropertyModal.getInstance().getProperty_id(), Common.profileImagePath, "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                        }
                    }

                }

            } catch (NullPointerException e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                Common.profileImagePath = null;
                Log.e("Camera", e.toString());
            } catch (Exception e) {
                Common.profileImagePath = null;
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                Log.e("Camera", e.toString());
            }
        } else {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }

        mPropertyImageAdapter.notifyDataSetChanged();


    }


    public void responseParse(String result) {
        LogConfig.logd("Property image response =", "" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                /*{"status":"true","message":"Property image uploaded.","data":[{"id":"768","property_id":"46","user_id":"69","image_number":"3",
                        "path":"image\/user\/photo\/1459520110com.skype.android.app.main.SplashActivity.png","type":null,
                        "createdOn":"2016-04-01 07:12:23","status":"1"}]}*/
                JSONArray jArray = jsonObject.getJSONArray("data");
                String image_id = jArray.getJSONObject(0).getString("id");
                String image_path = jArray.getJSONObject(0).getString("path");


                PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                modal.setPhoto_id(image_id);
                modal.setPhoto_Url(image_path);
                modal.setPhoto_from("server");
                modalList.set(editImagePosition, modal);
                mPropertyImageAdapter.notifyDataSetChanged();

                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                Common.profileImagePath = "";
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        mCommon.hideKeybord(this, edtPinCode);
       /* AlertDialogWithTwoBtn callbackDialog = new AlertDialogWithTwoBtn() {
            @Override
            public void callBack() {
                if (!isSkipOption) {
                    Intent mIntent = new Intent(PropertyInfoFirstActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {

                    Intent mIntent = new Intent(PropertyInfoFirstActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                }

            }
        };*/

        CustomAlertDialogwithTwoBtn VehicleRcId = new CustomAlertDialogwithTwoBtn(PropertyInfoFirstActivity.this,R.style.full_screen_dialog);
        VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        VehicleRcId.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        VehicleRcId.show();



        /*callbackDialog.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        callbackDialog.show(getSupportFragmentManager(), "Exit ");*/
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString("flag", flag);
        outState.putString("property_id", property_id);
        outState.putBoolean("isSkipOption", isSkipOption);

        outState.putString("furnish", furnish);
        outState.putString("modularKitchen", modularKitchen);

        outState.putString("property_name", edtPropertyName.getText().toString());
        outState.putString("property_address", edtPropertyAddress.getText().toString());
        outState.putString("property_description", edtPropertyDescription.getText().toString());
        outState.putString("property_landmark", edtPropertyLandMark.getText().toString());
        outState.putString("property_pincode", edtPinCode.getText().toString());
        outState.putString("property_lat", edtPropertyLat.getText().toString());
        outState.putString("property_long", edtPropertyLong.getText().toString());
        outState.putString("property_totalarea", edtPropertyTotalArea.getText().toString());
        outState.putString("property_superarea", edtPropertySuperArea.getText().toString());
        outState.putString("property_videourl", videoUrlInput.getText().toString());

        outState.putString("property_typeid", propertyType_id);
        outState.putString("property_stateid", stateId);
        outState.putString("property_cityid", cityId);
        outState.putString("property_areaid", areaId);
        outState.putString("property_faceid", propertyFace);
        outState.putString("property_waterSupply", waterSupplay);
        outState.putString("property_powerbackup", powerBackup);
        outState.putString("property_kitchen", kitchen);
        outState.putInt("editImagePosition", editImagePosition);
        outState.putString("propertyFeatures", propertyFeatures);
        outState.putString("otherAminities", otherAminities);
        outState.putParcelableArrayList("modalList", modalList);

        PropertyModal.getInstance().setPropertyImage(modalList);
        outState.putParcelable("propertyModal", PropertyModal.getInstance());


    }

    public void setDataOnSavedState(Bundle savedInstanceState) {


        flag = "" + savedInstanceState.getString("flag");
        property_id = "" + savedInstanceState.getString("property_id");
        isSkipOption = savedInstanceState.getBoolean("isSkipOption");
        editImagePosition = savedInstanceState.getInt("editImagePosition");

        stateId = "" + savedInstanceState.getString("property_stateid");
        cityId = "" + savedInstanceState.getString("property_cityid");
        areaId = "" + savedInstanceState.getString("property_areaid");

        PropertyModal.propertyModal = (PropertyModal) savedInstanceState.getParcelable("propertyModal");


        otherAminities = savedInstanceState.getString("otherAminities");

        if (otherAminities != null && otherAminities.length() > 0) {

            String[] arrayAminities = otherAminities.split(", ");
            if (mArrayAminities.size() > 0) {
                String aminities = "";
                for (int i = 0; i < arrayAminities.length; i++) {
                    for (int j = 0; j < mArrayAminities.size(); j++) {
                        if (arrayAminities[i].trim().equals(mArrayAminities.get(j).getStrSateId())) {
                            mArrayAminities.get(j).setChecked(1);
                            aminities = aminities + mArrayAminities.get(j).getStrStateName() + ", ";
                        }
                    }
                }
                if (arrayAminities.length == (mArrayAminities.size() - 1)) {

                    mArrayAminities.get(0).setChecked(1);
                }
                txtAminities.setText("" + aminities);
            }
        }

        String waterSupply = savedInstanceState.getString("property_waterSupply");
        if (waterSupply != null &&
                waterSupply.length() > 0) {
            if (waterSupply.equals("Municipal")) {
                spinnerWaterSupply.setSelection(1);
            } else if (waterSupply.equals("Tubewell")) {
                spinnerWaterSupply.setSelection(2);
            } else if (waterSupply.equals("Both")) {
                spinnerWaterSupply.setSelection(3);
            } else {
                spinnerWaterSupply.setSelection(0);
            }
        }
        String powerBackup = savedInstanceState.getString("property_powerbackup");
        if (powerBackup != null &&
                powerBackup.length() > 0) {
            if (powerBackup.equals("No")) {
                spinnerPowerBackup.setSelection(2);
            } else if (powerBackup != null &&
                    powerBackup.equals("Yes")) {
                spinnerPowerBackup.setSelection(1);
            } else {
                spinnerPowerBackup.setSelection(0);
            }
        }

        String kitche = savedInstanceState.getString("property_kitchen");
        if (kitche != null &&
                kitche.length() > 0) {
            if (kitche.equals("No")) {
                spinnerKitchen.setSelection(2);
            } else if (kitche != null &&
                    kitche.equals("Yes")) {
                spinnerKitchen.setSelection(1);
            } else {
                spinnerKitchen.setSelection(0);
            }
        }

        edtPropertyAddress.setText(savedInstanceState.getString("property_address"));
        edtPropertyName.setText(savedInstanceState.getString("property_name"));
        edtPropertyDescription.setText(savedInstanceState.getString("property_description"));

        if (PropertyModal.getInstance().getPropertyImage().size() >= 1) {

            if (!PropertyModal.getInstance().getPropertyImage().get(0).getPhoto_Url().equals("Not Available")) {
                modalList.addAll(PropertyModal.getInstance().getPropertyImage());
            }

        }

        /*modalList = savedInstanceState.getParcelableArrayList("modalList");
*/


        mPropertyImageAdapter.notifyDataSetChanged();
        try {
            edtPropertyLat.setText("" + savedInstanceState.getString("property_lat"));
            edtPropertyLong.setText("" + savedInstanceState.getString("property_long"));
        } catch (NullPointerException e) {
            edtPropertyLat.setText("");
            edtPropertyLong.setText("");
        }
        edtPinCode.setText("" + savedInstanceState.getString("property_pincode"));
        edtPropertyLandMark.setText("" + savedInstanceState.getString("property_landmark"));
        edtPropertyTotalArea.setText("" + savedInstanceState.getString("property_totalarea"));
        edtPropertySuperArea.setText("" + savedInstanceState.getString("property_superarea"));

        RadioButton bolModuler = (RadioButton) findViewById(R.id.modular_kitchen);
        RadioButton bolNonModuler = (RadioButton) findViewById(R.id.non_modular_kitchen);
        String mModuler = savedInstanceState.getString("modularKitchen");
        if (mModuler != null) {
            if (mModuler.equals("Modular Kitchen")) {
                bolModuler.setChecked(true);
                bolNonModuler.setChecked(false);
            } else {
                bolModuler.setChecked(false);
                bolNonModuler.setChecked(true);
            }
        }

        RadioButton bFurnish = (RadioButton) findViewById(R.id.person_user);
        RadioButton bNonFurnish = (RadioButton) findViewById(R.id.person_propertyManager);
        String mFurnish = savedInstanceState.getString("furnish");
        if (mFurnish != null) {
            if (mFurnish.equals("Furnish")) {
                bFurnish.setChecked(true);
                bNonFurnish.setChecked(false);
                relativelayout_feature.setVisibility(View.VISIBLE);
            } else {
                relativelayout_feature.setVisibility(View.GONE);
                bFurnish.setChecked(false);
                bNonFurnish.setChecked(true);
            }
        }

        propertyFeatures = savedInstanceState.getString("propertyFeatures").trim();
        propertyFeatures = propertyFeatures + " ";
        Common.Config("feature list    " + propertyFeatures);
        if (propertyFeatures != null && propertyFeatures.length() > 0) {

            String[] arrayFeature = propertyFeatures.split(", ");
            if (mArrayFeaturesList.size() > 0) {
                String features = "";
                for (int i = 0; i < arrayFeature.length; i++) {
                    for (int j = 0; j < mArrayFeaturesList.size(); j++) {
                        if (arrayFeature[i].equals(mArrayFeaturesList.get(j).getStrSateId())) {
                            mArrayFeaturesList.get(j).setChecked(1);
                            features = features + mArrayFeaturesList.get(j).getStrStateName() + ", ";
                        }
                    }
                }
                if (arrayFeature.length == (mArrayFeaturesList.size() - 1)) {

                    mArrayFeaturesList.get(0).setChecked(1);
                }
                txtFeatures.setText("" + features);
            }
        }

        propertyType_id = savedInstanceState.getString("property_typeid");
        if (spinnerPropertyType != null && mArrayPropertyType.size() > 0
                && propertyType_id != null && propertyType_id.length() > 0) {
            for (int i = 0; i < mArrayPropertyType.size(); i++) {
                if (mArrayPropertyType.get(i).getStrSateId().equals(propertyType_id)) {
                    spinnerPropertyType.setSelection(i);
                }
            }
        }

        String face = savedInstanceState.getString("property_faceid");
        if (face != null && face.trim().length() > 0) {
            for (int i = 0; i < mArrayPropertyFace.size(); i++) {
                if (face.equals(mArrayPropertyFace.get(i).getStrStateName())) {
                    spinnerPropertyFace.setSelection(i);
                }
            }
        }

        videoUrlInput.setText(savedInstanceState.getString("property_videourl"));


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
            pd = new ProgressDialog(PropertyInfoFirstActivity.this);
            pd.setMessage("Please wait..........");
            pd.show();

        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getAreaList"));
                nameValuePairs.add(new BasicNameValuePair("city_id", cityId));
                //  nameValuePairs.add(new BasicNameValuePair("areaId",areaId));
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

                // mArrayArea.clear();
                //  mArrayArea.addAll(mArray);
                if (mArrayArea != null && mArrayArea.size() > 0) {
                    String areaidvalue = areaId;
                    Log.d("areaid", areaidvalue);
                    for (int i = 0; i < mArrayArea.size(); i++) {
                        if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                            //spinnerArea.setSelection(i);
                            myAutoComplete.setText("" + mArrayArea.get(i).getStrStateName());
                        }
                    }
                } else {
                    mArrayArea.clear();
                }


                mAdapter.notifyDataSetChanged();

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(PropertyInfoFirstActivity.this, message, Toast.LENGTH_LONG).show();
                mArrayArea.clear();
            }

        }
    }


    //new code changes b/c alert dialog not working show animites by ashish 20-02-2019


    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private Button alertCancelBtn;
        private Button alertAddBtn;
        private String titleStr;
        private ListView listViewCategory;
        public ArrayList<StateModal> modalArrayList;
        private MultiChoiceAdapter mAdapter;


        public CustomDialogClass(FragmentActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            this.mFActivity = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, ArrayList<StateModal> modalArray) {
            this.mFActivity = activity;
            this.titleStr = titleStr;
            this.modalArrayList = modalArray;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alert_multi_choice_dialog);
            //c.setTheme(R.style.CustomDialog);

            mFActivity.setTheme(R.style.CustomDialog);
            //c.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
            createView();
            //setListner();


            mAdapter = new MultiChoiceAdapter(mFActivity, R.id.listView_Category, modalArrayList) {
                @Override
                public void callBack(ArrayList<StateModal> mArrayList) {

                    if (mArrayList.size() > 0) {
                        for (int i = 0; i < mArrayList.size(); i++) {
                            modalArrayList.get(i).setChecked(mArrayList.get(i).getChecked());
                        }
                    }
                }
            };
            listViewCategory.setAdapter(mAdapter);
            title.setText(titleStr);

            alertAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //callBack(modalArrayList);


                    if (titleStr.equals("Other Amenities")) {
                        if (modalArrayList.size() > 0) {
                            otherAminities = "";
                            String aminities = "";
                            for (int i = 0; i < modalArrayList.size(); i++) {
                                mArrayAminities.get(i).setChecked(modalArrayList.get(i).getChecked());
                                if (modalArrayList.get(i).getChecked() == 1) {
                                    if (!modalArrayList.get(i).getStrStateName().equals("Select All")) {
                                        otherAminities = otherAminities + modalArrayList.get(i).getStrSateId() + ", ";
                                        aminities = aminities + modalArrayList.get(i).getStrStateName() + ", ";
                                    }
                                }
                            }
                            txtAminities.setText("" + aminities);
                        }
                    } else if (titleStr.equals("Features")) {

                        if (modalArrayList.size() > 0) {
                            propertyFeatures = " ";
                            String features = "";
                            for (int i = 0; i < modalArrayList.size(); i++) {

                                modalArrayList.get(i).setChecked(modalArrayList.get(i).getChecked());
                                if (modalArrayList.get(i).getChecked() == 1) {

                                    if (!modalArrayList.get(i).getStrStateName().equals("Select All")) {
                                        propertyFeatures = propertyFeatures + modalArrayList.get(i).getStrSateId() + ", ";
                                        features = features + modalArrayList.get(i).getStrStateName() + ", ";
                                    }

                                }
                            }

                            Common.Config("list size    " + propertyFeatures);
                            txtFeatures.setText("" + features);

                        }
                    }
                    dismiss();
                }
            });

            alertCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


        }

        public void createView() {
            title = (TextView) findViewById(R.id.alertTitle);
            alertCancelBtn = (Button) findViewById(R.id.alertCancelBtn);
            alertAddBtn = (Button) findViewById(R.id.alertAddBtn);
            listViewCategory = (ListView) findViewById(R.id.listView_Category);
        }

        public void setListner() {

            alertAddBtn.setOnClickListener(this);
            alertCancelBtn.setOnClickListener(this);
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

    //end


    //AlertDialogwithTwoBtn


    //new code changes b/c alert dialog not working show animites by ashish 20-02-2019


    public class CustomAlertDialogwithTwoBtn extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;
        private String titleStr;
        private String messageStr;

        public CustomAlertDialogwithTwoBtn(PropertyInfoFirstActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            this.mFActivity = a;
        }

        public void getPerameter(FragmentActivity activity, String titleStr, String messageStr) {
            mFActivity = activity;
            this.titleStr = titleStr;
            this.messageStr = messageStr;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alert_two_btn_dialog);
            //c.setTheme(R.style.CustomDialog);
            mFActivity.setTheme(R.style.CustomDialog);
            //c.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
            createView();
            setListner();


        }

        public void createView() {
            title = (TextView) findViewById(R.id.alertTitle);
            message = (TextView) findViewById(R.id.categoryNameInput);
            alertYesBtn = (Button) findViewById(R.id.alertYesBtn);
            alertNoBtn = (Button) findViewById(R.id.alertNoBtn);
            title.setText(titleStr);
            message.setText(messageStr);

        }

        public void setListner() {
            alertYesBtn.setOnClickListener(this);
            alertNoBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alertYesBtn:

                    if (!isSkipOption) {
                        Intent mIntent = new Intent(PropertyInfoFirstActivity.this, HostelListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    } else {

                        Intent mIntent = new Intent(PropertyInfoFirstActivity.this, HostelListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    }
                    dismiss();
                    break;
                case R.id.alertNoBtn:
                    dismiss();
                    break;
                default:
                    break;
            }
            //dismiss();
        }
    }

    //end

}
