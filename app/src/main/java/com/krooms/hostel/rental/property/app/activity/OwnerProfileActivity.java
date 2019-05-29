package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
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
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.asynctask.GetOwnerDetailServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.UpdateOwnerProfileAsyncTask;
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
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.StateModal;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OwnerProfileActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce,
        StateDataBaseResponce, CityDataBaseResponce, AreaDataBaseResponce, DataBaseResponce {

    private Validation mValidation = null;
    private Common mCommon = null;
    private EditText ownerFirstName = null, ownerLastName = null, ownerAddressInput = null, ownerFatherNameInput = null;
    private EditText ownerLandmarkInput = null, pinCodeInput = null, ownerOrganizationInput = null, ownerEmailId = null;
    private EditText ownerLandlineNumber = null;
    private TextView ownerMobileNumber = null;
    private ImageView ownerProfilePic = null, applogoImgEdit = null;
    private Spinner spinnerStates = null, spinnerCity = null, spinnerProfession = null;
    private Button btnSubmit = null;
    private String professionStr = "";
    private String country = "India", countryId = "1";
    private String state, stateId = "0";
    private String city, cityId = "0";
    private String area = "", areaId = "0";
    private int falgCitySelect = 0;
    private ArrayList<StateModal> mArrayStates = null;
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = null;
    private ArrayList<StateModal> mArrayProfession = null;
    private StateSpinnerAdapter mStateSpinnerAdapter, mCitySpinnerAdapter, mProfessionSpinnerAdapter;
    private SharedStorage mSharedStorage;
    private String userName, lastName, ownerfathername, ownerAddress, ownerLandmark, pinCode,
            ownerOrganization, ownerEmail, ownerLandline, ownerMobile;
    private String flagFirstTime = "", profileImage = "", profileImageUpload = "";
    private AutoCompleteTextView myAutoComplete;
    private ObjectAdapter mAdapter = null;
    String formattedDate;
    Calendar calnder;
    String ImagePath = "";
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);
        Common.profileImagePath = null;
        initView();
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
        if (savedInstanceState != null) {
            setSavedData(savedInstanceState);
        } else {
            getOwnerDetail();
        }
        setListner();
    }

    private void initView() {
        //First time login/signup
        flagFirstTime = getIntent().getExtras().getString("flag");
        if (flagFirstTime == null) {
            flagFirstTime = "";
        }
        mValidation = new Validation(this);
        mCommon = new Common();
        mSharedStorage = SharedStorage.getInstance(this);
        ownerFirstName = (EditText) findViewById(R.id.ownerFirstNameInput);
        ownerLastName = (EditText) findViewById(R.id.ownerLastNameInput);
        ownerFatherNameInput = (EditText) findViewById(R.id.ownerFatherNameinput);
        ownerAddressInput = (EditText) findViewById(R.id.ownerAddressinput);
        ownerLandmarkInput = (EditText) findViewById(R.id.ownerLandmarkinput);
        pinCodeInput = (EditText) findViewById(R.id.pinCodeinput);
        ownerOrganizationInput = (EditText) findViewById(R.id.ownerOrganizationInput);
        ownerEmailId = (EditText) findViewById(R.id.ownerEmailInput);
        ownerLandlineNumber = (EditText) findViewById(R.id.owner_landline_input);
        ownerMobileNumber = (TextView) findViewById(R.id.owner_mobile_input);
        ownerProfilePic = (ImageView) findViewById(R.id.applogoImg);
        applogoImgEdit = (ImageView) findViewById(R.id.applogoImgEdit);
        spinnerStates = (Spinner) findViewById(R.id.stateSelection);
        spinnerCity = (Spinner) findViewById(R.id.citySelection);
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


        btnSubmit = (Button) findViewById(R.id.button_submit);
        btnSubmit.setOnClickListener(this);
        ownerProfilePic.setOnClickListener(this);
        applogoImgEdit.setOnClickListener(this);
        TextView backTitle = (TextView) findViewById(R.id.textView_title);
        backTitle.setOnClickListener(this);
    }

    public void setData() {

        ownerFirstName.setText(mSharedStorage.getUserFirstName());
        ownerLastName.setText(mSharedStorage.getUserLastName());
        ownerFatherNameInput.setText(mSharedStorage.getUserFatherName());
        ownerEmailId.setText(mSharedStorage.getUserEmail());
        ownerMobileNumber.setText(mSharedStorage.getUserMobileNumber());
        ownerAddressInput.setText(mSharedStorage.getUserAddress());
        ownerLandmarkInput.setText(mSharedStorage.getLandMark());
        pinCodeInput.setText(mSharedStorage.getUserPincode());
        ownerOrganizationInput.setText(mSharedStorage.getOrganizationName());
        ownerLandlineNumber.setText(mSharedStorage.getLandlineNo());
        stateId = mSharedStorage.getStateId();
        cityId = mSharedStorage.getCityId();
        areaId = mSharedStorage.getAreaId();
        professionStr = mSharedStorage.getProfession();
        profileImage = WebUrls.IMG_URL + mSharedStorage.getUserImage();
        profileImageUpload = "";
        if (profileImage != null && profileImage.length() > 0) {
            Picasso.with(this)
                    .load(profileImage)
                    .placeholder(R.drawable.user_xl)
                    .error(R.drawable.user_xl)
                    .transform(new RoundedTransformation(this))
                    .into(ownerProfilePic);
        }
        changeStateCityArea();
    }


    public void setSavedData(Bundle savedInstanceState) {
        ownerFirstName.setText(savedInstanceState.getString("ownerFirstName"));
        ownerLastName.setText(savedInstanceState.getString("ownerLastName"));
        ownerFatherNameInput.setText(savedInstanceState.getString("ownerFatherNameInput"));
        ownerEmailId.setText(savedInstanceState.getString("ownerEmailId"));
        ownerMobileNumber.setText(savedInstanceState.getString("ownerMobileNumber"));
        ownerAddressInput.setText(savedInstanceState.getString("ownerAddressInput"));
        ownerLandmarkInput.setText(savedInstanceState.getString("ownerLandmarkInput"));
        pinCodeInput.setText(savedInstanceState.getString("pinCodeInput"));
        ownerOrganizationInput.setText(savedInstanceState.getString("ownerOrganizationInput"));
        ownerLandlineNumber.setText(savedInstanceState.getString("ownerLandlineNumber"));
        stateId = savedInstanceState.getString("stateId");
        cityId = savedInstanceState.getString("cityId");
        areaId = savedInstanceState.getString("areaId");
        professionStr = savedInstanceState.getString("professionStr");
        profileImageUpload = savedInstanceState.getString("profileImageUpload");
        profileImage = savedInstanceState.getString("profileImage");
        if (profileImage != null && profileImage.length() > 0) {
            Picasso.with(this)
                    .load(profileImage)
                    .placeholder(R.drawable.user_xl)
                    .error(R.drawable.user_xl)
                    .transform(new RoundedTransformation(this))
                    .into(ownerProfilePic);
        }

    }

    public void getOwnerDetail() {
        GetOwnerDetailServiceAsyncTask service = new GetOwnerDetailServiceAsyncTask(this);
        service.setCallBack(new ServiceResponce() {
            @Override
            public void requestResponce(String result) {
                parseOwnerDetail(result);
            }
        });
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId());
    }

    public void parseOwnerDetail(String result) {
        Common.Config("result    " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                mSharedStorage.setUserImage(data.getJSONObject(0).getString("owner_pic"));
                mSharedStorage.setUserFirstName(data.getJSONObject(0).getString("fname"));
                mSharedStorage.setUserLastName(data.getJSONObject(0).getString("lname"));
                mSharedStorage.setUserFatherName(data.getJSONObject(0).getString("father_name"));
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
                setData();
            } else {
                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {

        }
    }

    public void setListner() {
        mArrayStates = new ArrayList<>();
        mArrayArea = new ArrayList<>();
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(OwnerProfileActivity.this, countryId);
        mGetStatesAsyncTask.setCallBack(OwnerProfileActivity.this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "SELECT * FROM state where country_id = '" + countryId + "' And status = '1' ORDER BY state_name");
        mStateSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_property_state, mArrayStates);
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
                    GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(OwnerProfileActivity.this, stateId);
                    mGetCityAsyncTask.setCallBack(OwnerProfileActivity.this);
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
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(OwnerProfileActivity.this, stateId);
        mGetCityAsyncTask.setCallBack(OwnerProfileActivity.this);
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
                    new GetAreaList(cityId, areaId).execute();
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
        mProfessionSpinnerAdapter = new StateSpinnerAdapter(this, R.id.profession, mArrayProfession);
        spinnerProfession.setAdapter(mProfessionSpinnerAdapter);
        spinnerProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                professionStr = mArrayProfession.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_title:
                if (!flagFirstTime.equals("Owner_First_Time")) {
                    this.finish();
                    this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {

                    Intent mIntent = new Intent(OwnerProfileActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    this.finish();
                }
                break;
            case R.id.button_submit:
                createPropertyDetail();
                break;
            case R.id.applogoImg:
               /* ImagePreviewDialog profilePicPreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Picasso.with(OwnerProfileActivity.this)
                                .load(profileImage)
                                .placeholder(R.drawable.user_xl)
                                .error(R.drawable.user_xl)
                                .into(img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };*/
                // profilePicPreviewDialog.getPerameter(this, "Profile Pic", true);
                //profilePicPreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
                CustomDialogClass VehicleRcId = new CustomDialogClass(OwnerProfileActivity.this);
                VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VehicleRcId.getPerameter(this, "Profile Pic", true);
                VehicleRcId.show();

                break;
            case R.id.applogoImgEdit:
                //old working
               /* Intent mIntent = new Intent(this, PhotoCaptureActivity.class);
                mIntent.putExtra("photo_purpose", "profilePic");
                this.startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);
               */
                selectImage();
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

        public CustomDialogClass(OwnerProfileActivity a) {
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
                Picasso.with(OwnerProfileActivity.this)
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


    //Select profile image form camera and gallery
    /*image capture code start*/
    public void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerProfileActivity.this);
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(OwnerProfileActivity.this, android.R.layout.simple_list_item_1);
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
                            Toast toast = Toast.makeText(OwnerProfileActivity.this, "Please give internal storage permission", Toast.LENGTH_SHORT);
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
            uri = UtilityImg.getExternalFilesDirForVersion24Above(OwnerProfileActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(OwnerProfileActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
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
                        ownerProfilePic.setImageBitmap(bitmap);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
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


    public void createPropertyDetail() {

        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(calnder.getTime());
        if (profileImage == null) {
            profileImage = "";
        }
        userName = ownerFirstName.getText().toString().trim();
        lastName = ownerLastName.getText().toString().trim();
        ownerfathername = ownerFatherNameInput.getText().toString().trim();
        ownerAddress = ownerAddressInput.getText().toString().trim();
        ownerLandmark = ownerLandmarkInput.getText().toString().trim();
        pinCode = pinCodeInput.getText().toString().trim();
        ownerOrganization = ownerOrganizationInput.getText().toString().trim();
        ownerEmail = ownerEmailId.getText().toString().trim();
        ownerLandline = ownerLandlineNumber.getText().toString().trim();
        ownerMobile = ownerMobileNumber.getText().toString().trim();
        if (myAutoComplete.getText().equals("") || myAutoComplete.getText().length() == 0) {
            areaId = "0";
        }

        if (userName.length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_your_name_alert), false);
        } else if (ownerfathername.trim().length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_father_name_alert), false);
        } else if (ownerMobile.trim().length() == 0) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_your_mobile_no_alert), false);
        } else if (ownerMobile.trim().length() < 10) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_valid_mobile), false);
        } else if (ownerEmail.length() > 0 && !mValidation.checkEmail(ownerEmail)) {
            mCommon.displayAlert(this, getResources().getString(R.string.str_please_enter_valid_email), false);
        } else {
            mCommon.hideKeybord(this, ownerFirstName);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {
                //old working
                UpdateOwnerProfileAsyncTask serviceAsynctask = new UpdateOwnerProfileAsyncTask(OwnerProfileActivity.this, "");
                serviceAsynctask.setCallBack(this);
                serviceAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(),
                        userName, lastName, ownerAddress, ownerLandmark, stateId, cityId, areaId, pinCode, professionStr,
                        ownerOrganization, ownerEmail, ownerLandline, ownerMobile, profileImageUpload, ownerfathername, formattedDate);
            }
        }

    }

    @Override
    public void requestResponce(String result) {

        Common.Config("result   " + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                mSharedStorage.setUserImage(data.getJSONObject(0).getString("owner_pic"));
                mSharedStorage.setUserFirstName(userName);
                mSharedStorage.setUserLastName(lastName);
                mSharedStorage.setUserFatherName(ownerfathername);
                mSharedStorage.setUserEmail(ownerEmail);
                mSharedStorage.setUserMobileNumber(ownerMobile);
                mSharedStorage.setLandlineNo(ownerLandline);
                mSharedStorage.setOrganizationName(ownerOrganization);
                mSharedStorage.setUserAddress(ownerAddress);
                mSharedStorage.setLandMark(ownerLandmark);
                mSharedStorage.setUserPincode(pinCode);
                mSharedStorage.setProfession(professionStr);
                mSharedStorage.setStateId(stateId);
                mSharedStorage.setCityId(cityId);
                mSharedStorage.setAreaId(areaId);

                if (flagFirstTime.equals("Owner_First_Time")) {
                    Intent mIntent = new Intent(this, PropertyInfoFirstActivity.class);
                    mIntent.putExtra("isSkipOption", true);
                    mIntent.putExtra("flag", "Owner_First_Time");
                    mIntent.putExtra("property_id", "");
                    startActivity(mIntent);
                    finish();
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                }
            } else {
                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
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
    public void onBackPressed() {

        CustomDialogClassBack cdd = new CustomDialogClassBack(OwnerProfileActivity.this, R.style.full_screen_dialog);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        cdd.show();
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

        public CustomDialogClassBack(OwnerProfileActivity a, int full_screen_dialog) {
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
                    Common.profileImagePath = null;
                    if (!flagFirstTime.equals("Owner_First_Time")) {
                        finish();
                        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    } else {
                        Intent mIntent = new Intent(OwnerProfileActivity.this, HostelListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finish();
                    }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("stateId", stateId);
        outState.putString("cityId", cityId);
        outState.putString("areaId", areaId);
        outState.putString("professionStr", professionStr);
        outState.putString("profileImage", profileImage);
        outState.putString("profileImageUpload", profileImageUpload);
        outState.putString("ownerFirstName", ownerFirstName.getText().toString().trim());
        outState.putString("ownerLastName", ownerLastName.getText().toString().trim());
        outState.putString("ownerFatherNameInput", ownerFatherNameInput.getText().toString().trim());
        outState.putString("ownerEmailId", ownerEmailId.getText().toString().trim());
        outState.putString("ownerMobileNumber", ownerMobileNumber.getText().toString().trim());
        outState.putString("ownerAddressInput", ownerAddressInput.getText().toString().trim());
        outState.putString("ownerLandmarkInput", ownerLandmarkInput.getText().toString().trim());
        outState.putString("pinCodeInput", pinCodeInput.getText().toString().trim());
        outState.putString("ownerOrganizationInput", ownerOrganizationInput.getText().toString().trim());
        outState.putString("ownerLandlineNumber", ownerLandlineNumber.getText().toString().trim());
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
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
            pd = new ProgressDialog(OwnerProfileActivity.this);
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
                Toast.makeText(OwnerProfileActivity.this, message, Toast.LENGTH_LONG).show();
                mArrayArea.clear();
            }
        }
    }
}
