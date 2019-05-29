package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.asynctask.RCUTenantDetailAsynctask;
import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.GetStatesAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditRCUFromByOwnerActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce, StateDataBaseResponce/*, CityDataBaseResponce, AreaDataBaseResponce*/ {

    private EditText edtFirstName = null;
    private EditText edtLastName = null;
    private EditText edtFatherName = null;
    private EditText edtFatherContactno = null;
    private EditText edtFlatPlotno = null;
    private EditText edtLandMark = null;
    private EditText edtLocation = null;
    private EditText edtPinCode = null;
    private EditText edtContactno = null;
    private EditText edtEmail = null;
    private TextView txtHireDate, txtLeaveDate;
    private TextView txtRoom_no = null;
    private ImageView tenantProfilePic;
    private EditText spinnerCity = null;
    private Spinner spinnerState;
    private String imgTenanto = null;
    private String imgTenantoDisplay = null;
    private ProgressBar mprogressBar;
    private Common mCommon;
    private Validation mValidation;
    private Button btnSubmit, btnBrowseImage;
    private TextView txtBack;
    private LinearLayout mLayoutTenantView;
    private ArrayList<StateModal> mArrayStates = null;
    private StateSpinnerAdapter mStateSpinnerAdapter/*, mCitySpinnerAdapter, mAreaSpinnerAdapter*/;
    private String country = "India", countryId = "1", state, stateId = "0"/*, city,
            cityId = "0", area = "", areaId = "0"*/;
    private int year1, month, day;

    private PropertyUserModal tenantData;
    private SharedStorage mSharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rcufrom_by_owner);
        mCommon = new Common();

        mSharedStorage = SharedStorage.getInstance(this);
        tenantData = (PropertyUserModal) getIntent().getSerializableExtra("modal");
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

        if (savedInstanceState != null) {
            setDataOnSavedState(savedInstanceState);
        }
        setListeners();
        setData();
    }

    private void createViews() {

        edtFirstName = (EditText) findViewById(R.id.edittext_first_name);
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
        spinnerState = (Spinner) findViewById(R.id.spinner_state_ower);
        spinnerCity = (EditText) findViewById(R.id.spinner_city_ower);
        edtLocation = (EditText) findViewById(R.id.edittext_location_ower);
        tenantProfilePic = (ImageView) findViewById(R.id.tenantProfilePic);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);
        txtBack = (TextView) findViewById(R.id.textView_title);
        btnSubmit = (Button) findViewById(R.id.button_next);
        mLayoutTenantView = (LinearLayout) findViewById(R.id.layout_tent_view);
        Date cal = (Date) Calendar.getInstance().getTime();
        year1 = cal.getYear() + 1900;
        month = cal.getMonth();
        day = cal.getDate();

    }

    private void setListeners() {
        mValidation = new Validation(this);
        txtHireDate.setOnClickListener(this);
        txtLeaveDate.setOnClickListener(this);
        tenantProfilePic.setOnClickListener(this);
        btnBrowseImage.setOnClickListener(this);
        txtBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        mArrayStates = new ArrayList<>();
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(EditRCUFromByOwnerActivity.this, countryId);
        mGetStatesAsyncTask.setCallBack(EditRCUFromByOwnerActivity.this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "SELECT * FROM state where country_id = '" + countryId + "'ORDER BY state_name");
        mStateSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_state_ower, mArrayStates);
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
    }

    public void setData() {

        if (tenantData != null) {
            edtFirstName.setText(tenantData.getTenant_fname());
            edtLastName.setText(tenantData.getTenant_lname());
            edtFatherName.setText(tenantData.getTenant_father_name());
            edtFatherContactno.setText(tenantData.getTenant_father_contact_no());
            edtFlatPlotno.setText(tenantData.getFlat_number());
            edtLandMark.setText(tenantData.getLandmark());
            edtPinCode.setText(tenantData.getPincode());
            edtContactno.setText(tenantData.getContact_number());
            edtEmail.setText(tenantData.getEmail_id());
            txtHireDate.setText(tenantData.getProperty_hire_date());
            txtLeaveDate.setText(tenantData.getProperty_leave_date());
            stateId = tenantData.getState();
            spinnerCity.setText(tenantData.getCity());
            edtLocation.setText(tenantData.getLocation());
           /*cityId = tenantData.getCity();
            areaId = tenantData.getLocation();*/
            imgTenanto = tenantData.getTenant_photo();
            Common.Config("image name   " + imgTenanto);
            if (imgTenanto.trim().equals("")) {
                btnBrowseImage.setText("Browse");
            } else {
                btnBrowseImage.setText("" + imgTenanto);
            }
            if (!imgTenanto.trim().equals("")) {
                tenantProfilePic.setVisibility(View.VISIBLE);
                imgTenantoDisplay = WebUrls.IMG_URL + tenantData.getTenant_photo();
                txtRoom_no.setText("Room no. " + tenantData.getBookedRoom());
                Picasso.with(this)
                        .load(imgTenantoDisplay)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .transform(new RoundedTransformation(this))
                        .into(tenantProfilePic);
            }
            changeStateCityArea();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textView_title:
                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
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
                break;

            case R.id.tenantProfilePic:
                ImagePreviewDialog vehicleImagePreviewDialog1 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        int postion = getIntent().getExtras().getInt("tenant_no", 1) - 1;
                        String vehicleRcPhoto = "";

                        Picasso.with(EditRCUFromByOwnerActivity.this)
                                .load(imgTenantoDisplay)
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .transform(new RoundedTransformation(EditRCUFromByOwnerActivity.this))
                                .into(img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                vehicleImagePreviewDialog1.getPerameter(this, "Profile Pic", true);
                vehicleImagePreviewDialog1.show(this.getSupportFragmentManager(), "image dialog");
                break;
            case R.id.button_browse:
                takePhoto();
                break;
            case R.id.button_next:
                submitBtnClickEvent();
                break;
        }

    }


    public void takePhoto() {

        Intent mIntent = new Intent(this, PhotoCaptureActivity.class);
        mIntent.putExtra("photo_purpose", "profilePic");
        startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);
    }

    @Override
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
                        .transform(new RoundedTransformation(this))
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

    Calendar hireDateCal, leaveDateCal;

    protected Dialog onCreateDialogTest(final TextView txtDate, final String type) {

        DatePickerDialog datePicker = new DatePickerDialog(this, R.style.datepickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (type.equals("hire_date")) {

                            hireDateCal = Calendar.getInstance();
                            hireDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            Calendar currentDateCal = Calendar.getInstance();
                            currentDateCal.set(year1, (month + 1), day - 1);
                            if (getDateDiffStringBool(getDateFromCalender(currentDateCal), getDateFromCalender(hireDateCal))) {
                                txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                            } else {
                                mCommon.displayAlert(EditRCUFromByOwnerActivity.this, "Hire date will not be less than Current date.", false);
                                txtDate.setText("");
                                hireDateCal = null;
                            }


                        } else {

                            leaveDateCal = Calendar.getInstance();
                            leaveDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            if (getDateDiffStringBool(getDateFromCalender(hireDateCal), getDateFromCalender(leaveDateCal))) {
                                txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                            } else {

                                mCommon.displayAlert(EditRCUFromByOwnerActivity.this, "Leave date will not be equal or less than Hire date.", false);
                                txtDate.setText("");
                                leaveDateCal = null;
                            }

                        }

                    }
                }, year1, month, day);

        return datePicker;

    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        LogConfig.logd("Rcu tenant detail response =", "" + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    String rcuId = jsoArray.getJSONObject(0).getString("rcu_id");
                    Intent mIntent = new Intent(this, EditRCUTenantProfessionalDetailActivity.class);
                    mIntent.putExtra("modal", (Serializable) tenantData);
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                    finish();

                    if (TenantDetailActivity.mActivity != null) {
                        TenantDetailActivity.mActivity.finish();
                    }

                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Date getDateFromCalender(Calendar cal) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(cal.getTime());


        Date date1 = null;
        try {
            date1 = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
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

    public void submitBtnClickEvent() {

        if (imgTenanto == null) {
            imgTenanto = "";
        }


        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String fatherName = edtFatherName.getText().toString().trim();
        String fatherContactno = edtFatherContactno.getText().toString().trim();
        String flatPlotno = edtFlatPlotno.getText().toString().trim();
        String landMark = edtLandMark.getText().toString().trim();
        String pinCode = edtPinCode.getText().toString().trim();
        String contactNo = edtContactno.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String hireDate = txtHireDate.getText().toString().trim();
        String leaveDate = txtLeaveDate.getText().toString().trim();

        String property_id = mSharedStorage.getUserPropertyId();
        String owner_id = "";
        String room_id = tenantData.getBookedRoomId();
        String rcu_id = tenantData.getTenant_id();

        String cityId = spinnerCity.getText().toString().trim();
        String areaId = edtLocation.getText().toString().trim();

        String transaction_id = tenantData.getTransaction_id();


        if (firstName.length() == 0) {
            mCommon.displayAlert(this, "Enter Tenant's full name.", false);
        } else if (fatherName.length() == 0) {
            mCommon.displayAlert(this, "Enter father name.", false);
        } else if (fatherContactno.length() > 0 && fatherContactno.length() < 10) {
            mCommon.displayAlert(this, "Enter father valid contact no.", false);
        } else if (contactNo.length() > 0 && contactNo.length() < 10) {
            mCommon.displayAlert(this, "Enter valid contact no.", false);
        } else if (flatPlotno.length() == 0) {
            mCommon.displayAlert(this, "Enter flat/plot/building no.", false);
        } else {
            mCommon.hideKeybord(this, edtFatherContactno);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {
                mprogressBar.setVisibility(View.VISIBLE);
                RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(this);
                service.setCallBack(this);
                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, rcu_id, "", firstName,
                        lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                        cityId, pinCode, contactNo, email, hireDate, leaveDate, imgTenanto,
                        "", property_id, owner_id, room_id, transaction_id);
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        tenantData.setTenant_fname(edtFirstName.getText().toString().trim());
        tenantData.setTenant_lname(edtLastName.getText().toString().trim());
        tenantData.setTenant_father_name(edtFatherName.getText().toString().trim());
        tenantData.setTenant_father_contact_no(edtFatherContactno.getText().toString().trim());
        tenantData.setFlat_number(edtFlatPlotno.getText().toString().trim());
        tenantData.setLandmark(edtLandMark.getText().toString().trim());
        tenantData.setPincode(edtPinCode.getText().toString().trim());
        tenantData.setContact_number(edtContactno.getText().toString().trim());
        tenantData.setEmail_id(edtEmail.getText().toString().trim());
        tenantData.setProperty_hire_date(txtHireDate.getText().toString().trim());
        tenantData.setProperty_leave_date(txtLeaveDate.getText().toString().trim());
        tenantData.setState(stateId);
        tenantData.setCity(spinnerCity.getText().toString().trim());
        tenantData.setLocation(edtLocation.getText().toString().trim());
        tenantData.setBookedRoom(txtRoom_no.getText().toString().trim());
        outState.putParcelable("tenantData", tenantData);

    }

    public void setDataOnSavedState(Bundle savedInstanceState) {

        tenantData = savedInstanceState.getParcelable("tenantData");
        System.out.println("hireDate   " + tenantData.getProperty_hire_date());
        System.out.println("leaveDate   " + tenantData.getProperty_leave_date());
        System.out.println("state   " + tenantData.getState());

    }
}
