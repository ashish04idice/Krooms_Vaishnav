package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.Utility.AppExeptionFile;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PropertyInfoSecondActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce {

    private EditText distance_railwaystation, distance_busstand, distance_airport;
    private EditText name_nearest_square, distance_nearest_square;
    private EditText amount_rent, amount_advance, maintenanse, water_bill, other_expenses, electricity;
    private Spinner noOfVehicle, allowSmoking, allowDrinking, noOfPeople, spinnerTenantType;
    private LinearLayout mprogressBar = null;
    private TextView curfew_time;
    private TextView txtBack;
    private ArrayList<StateModal> mArraynoOfVehicle = null;
    private ArrayList<StateModal> mArrayallowSmoking = null;
    private ArrayList<StateModal> mArrayallowDrinking = null;
    private ArrayList<StateModal> mArraynoOfPeople = null;
    private ArrayList<StateModal> mArraynoTenantType = null;

    private StateSpinnerAdapter mNoOfVehicleAdapter;
    private StateSpinnerAdapter mAllowSmokingAdapter;
    private StateSpinnerAdapter mAllowDrinkingAdapter;
    private StateSpinnerAdapter mNoOfPeopleAdapter;
    private StateSpinnerAdapter mAdapterTenantType;

    private Button btnNext = null;
    private String strNoOfVehicle = "", smoking = "", drinking = "", noOfPeoples = "", tenantType = "0";
    private String propertyId = "";
    private Validation mValidation;
    private Common mCommon;
    private int hour;
    private int minute;
    private boolean isSkipOption = false;
    String amountRent,advance;
    public String flag;
    public String property_id;

    public static ProgressDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info_second);

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

        flag = getIntent().getExtras().getString("flag");
        property_id = getIntent().getExtras().getString("PropertyId");
        isSkipOption = getIntent().getBooleanExtra("isSkipOption", false);

        createviews();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Uopdate form data to set views
                setListners();
                setDataIfHaving();
            }
        }, 1000);
    }


    public void setDataIfHaving(){

        if(flag.equals("Edit")){
            distance_railwaystation.setText(PropertyModal.getInstance().getProperty_distance_railway());
            distance_busstand.setText(PropertyModal.getInstance().getProperty_distance_busstand());
            distance_airport.setText(PropertyModal.getInstance().getProperty_distance_airport());
            name_nearest_square.setText(PropertyModal.getInstance().getProperty_nearest_square());
            distance_nearest_square.setText(PropertyModal.getInstance().getProperty_nearest_square_distance());
            amount_rent.setText(PropertyModal.getInstance().getProperty_rent_price());
            amount_advance.setText(PropertyModal.getInstance().getProperty_advance_rentAmount());
            maintenanse.setText(PropertyModal.getInstance().getProperty_maintenance_rentAmount());
            water_bill.setText(PropertyModal.getInstance().getProperty_water_bill_rentAmount());
            other_expenses.setText(PropertyModal.getInstance().getProperty_other_expance_rentAmount());
            electricity.setText(PropertyModal.getInstance().getProperty_Electricity_rentAmount());
            curfew_time.setText(PropertyModal.getInstance().getProperty_curfew_time());

            if(PropertyModal.getInstance().getProperty_is_allowed_smoking() != null &&
                    PropertyModal.getInstance().getProperty_is_allowed_smoking().length()>0) {
                if (PropertyModal.getInstance().getProperty_is_allowed_smoking().equals("Yes")) {
                    allowSmoking.setSelection(1);
                } else if (PropertyModal.getInstance().getProperty_is_allowed_smoking().equals("No")) {
                    allowSmoking.setSelection(2);
                } else {
                    allowSmoking.setSelection(0);
                }
            }


            if(PropertyModal.getInstance().getProperty_is_allowed_drinking() != null &&
                    PropertyModal.getInstance().getProperty_is_allowed_drinking().length()>0) {
                if (PropertyModal.getInstance().getProperty_is_allowed_drinking().equals("Yes")) {
                    allowDrinking.setSelection(1);
                } else if (PropertyModal.getInstance().getProperty_is_allowed_drinking().equals("No")) {
                    allowDrinking.setSelection(2);
                } else {
                    allowDrinking.setSelection(0);
                }
            }

            if(PropertyModal.getInstance().getProperty_no_of_tenant() != null &&
                    PropertyModal.getInstance().getProperty_no_of_tenant().length()>0) {
                if (PropertyModal.getInstance().getProperty_no_of_tenant().equals("No Of People")) {
                    noOfPeople.setSelection(0);
                } else {
                    noOfPeople.setSelection(1+Integer.parseInt(PropertyModal.getInstance().getProperty_no_of_tenant()));
                }
            }

            if(PropertyModal.getInstance().getProperty_allow_no_vehicle() != null &&
                    PropertyModal.getInstance().getProperty_allow_no_vehicle().length()>0) {
                if (PropertyModal.getInstance().getProperty_allow_no_vehicle().equals("No Of Vehicle")) {
                    noOfVehicle.setSelection(0);
                } else {
                    noOfVehicle.setSelection(1+Integer.parseInt(PropertyModal.getInstance().getProperty_allow_no_vehicle()));
                }
            }

            tenantType = PropertyModal.getInstance().getProperty_tenant_type();
            if(spinnerTenantType != null && mArraynoTenantType.size()>0 && tenantType != null && tenantType.length()>0) {
                for(int i = 0; i < mArraynoTenantType.size(); i++) {
                    if(mArraynoTenantType.get(i).getStrSateId().equals(tenantType)) {
                        spinnerTenantType.setSelection(i);
                    }
                }
            }
        }
    }

    public void createviews() {

        distance_railwaystation = (EditText) findViewById(R.id.railwaystatio_input);
        distance_busstand = (EditText) findViewById(R.id.busstand_input);
        distance_airport = (EditText) findViewById(R.id.airport_input);
        name_nearest_square = (EditText) findViewById(R.id.nearestSquarName_input);
        distance_nearest_square = (EditText) findViewById(R.id.distance_input);
        spinnerTenantType = (Spinner) findViewById(R.id.spinner_tenant_type);
        curfew_time = (TextView) findViewById(R.id.curfew_time_selection);
        amount_rent = (EditText) findViewById(R.id.rentAmount_input);
        amount_advance = (EditText) findViewById(R.id.advance_input);
        maintenanse = (EditText) findViewById(R.id.maintenance_input);
        water_bill = (EditText) findViewById(R.id.water_bill_input);
        other_expenses = (EditText) findViewById(R.id.otherexpenses_input);
        electricity = (EditText) findViewById(R.id.electricity_input);

        noOfVehicle = (Spinner) findViewById(R.id.no_of_vehicles_selection);
        allowSmoking = (Spinner) findViewById(R.id.smoking_allow_selection);
        allowDrinking = (Spinner) findViewById(R.id.drinking_allow_selection);
        noOfPeople = (Spinner) findViewById(R.id.no_of_people_selection);

        mprogressBar = (LinearLayout) findViewById(R.id.layout_progressBar);
        mprogressBar.setVisibility(View.GONE);

        btnNext = (Button) findViewById(R.id.button_next);
        txtBack = (TextView) findViewById(R.id.textView_title);
        txtBack.setOnClickListener(this);
        curfew_time.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);
    }


    public void setListners() {

        propertyId = getIntent().getStringExtra("PropertyId");
        mCommon = new Common();
        mValidation = new Validation(this);

        btnNext.setOnClickListener(this);
        mArraynoOfVehicle = new ArrayList<>();
        mArraynoOfVehicle.addAll(getNoOfvehicle());
        mNoOfVehicleAdapter = new StateSpinnerAdapter(this, R.id.no_of_vehicles_selection, mArraynoOfVehicle);
        noOfVehicle.setAdapter(mNoOfVehicleAdapter);
        noOfVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strNoOfVehicle = mArraynoOfVehicle.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mArrayallowSmoking = new ArrayList<>();
        mArrayallowSmoking.addAll(getAllowSmoking());
        mAllowSmokingAdapter = new StateSpinnerAdapter(this, R.id.no_of_vehicles_selection, mArrayallowSmoking);
        allowSmoking.setAdapter(mAllowSmokingAdapter);
        allowSmoking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smoking = mArrayallowSmoking.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mArrayallowDrinking = new ArrayList<>();
        mArrayallowDrinking.addAll(getAllowDrinking());
        mAllowDrinkingAdapter = new StateSpinnerAdapter(this, R.id.no_of_vehicles_selection, mArrayallowDrinking);
        allowDrinking.setAdapter(mAllowDrinkingAdapter);
        allowDrinking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drinking = mArrayallowDrinking.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArraynoOfPeople = new ArrayList<>();
        mArraynoOfPeople.addAll(getNoOfPeople());
        mNoOfPeopleAdapter = new StateSpinnerAdapter(this, R.id.no_of_vehicles_selection, mArraynoOfPeople);
        noOfPeople.setAdapter(mNoOfPeopleAdapter);
        noOfPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfPeoples = mArraynoOfPeople.get(position).getStrStateName();
                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArraynoTenantType = new ArrayList<>();
        mArraynoTenantType.addAll(getTenantType());
        mAdapterTenantType = new StateSpinnerAdapter(this, R.id.spinner_tenant_type, mArraynoTenantType);
        spinnerTenantType.setAdapter(mAdapterTenantType);
        spinnerTenantType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tenantType = mArraynoTenantType.get(position).getStrStateName();
                tenantType = mArraynoTenantType.get(position).getStrSateId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this,distance_railwaystation);
        switch (v.getId()) {
            case R.id.button_next:
                callPropertyDetail();
                break;
            case R.id.textView_title:
                if(!isSkipOption) {
                    this.finish();
                    this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {
                    Intent mIntent = new Intent(PropertyInfoSecondActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    this.finish();
                }
                break;

            case R.id.curfew_time_selection:
                TimePickerDialog timeDialog = new TimePickerDialog(this, timePickerListener, hour, minute, false);
                timeDialog.show();

                break;
        }
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hour = hourOfDay;
            minute = minutes;
            updateTime(hour, minute);
        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        curfew_time.setText(aTime);
    }


    private void callPropertyDetail() {

        String railway_station = distance_railwaystation.getText().toString().trim();
        String bus_stand = distance_busstand.getText().toString().trim();
        String airport = distance_airport.getText().toString().trim();
        String nearest = name_nearest_square.getText().toString().trim();
        String square = distance_nearest_square.getText().toString().trim();
        //String tenantType = tenant_type.getText().toString().trim();
        String curfewTime = curfew_time.getText().toString().trim();
        amountRent = amount_rent.getText().toString().trim();
        advance = amount_advance.getText().toString().trim();
        String maintenanses = maintenanse.getText().toString().trim();
        String waterBill = water_bill.getText().toString().trim();
        String otherExpenses = other_expenses.getText().toString().trim();
        String strElectricity = electricity.getText().toString().trim();

        if (amountRent.length() == 0 ) {
            mCommon.displayAlert(this, "Enter property rent amount.", false);
        }
        else if(advance.length() == 0 ){
            mCommon.displayAlert(this, "Enter property advance amount.", false);

        }
        else{
            mCommon.hideKeybord(this, distance_railwaystation);
            if (mValidation.checkNetworkRechability()) {
                mprogressBar.setVisibility(View.VISIBLE);
                createRoomsApi(propertyId, railway_station,
                        bus_stand, airport, nearest, square, tenantType, curfewTime, smoking, drinking, noOfPeoples,
                        amountRent, advance, maintenanses, waterBill, otherExpenses, strElectricity,strNoOfVehicle);
                //AddPropertyOwnerDetailAsyncTask service = new AddPropertyOwnerDetailAsyncTask(this);
                //service.setCallBack(this);
                //service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyId, railway_station,
                  //      bus_stand, airport, nearest, square, tenantType, curfewTime, smoking, drinking, noOfPeoples,
                    //    amountRent, advance, maintenanses, waterBill, otherExpenses, strElectricity, "", strNoOfVehicle);
            } else {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            }
        }
    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        Common.Config("response ....   " + result);
     //   App.MethodLog(amountRent,advance,"PropertyInfoSecondActivity","callPropertyDetail in requestResponce",result);
        LogConfig.logd("Property owner response =", "" + result);
        try {
            if(result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                  //  String propertyId = "" + jsonObject.getInt("data");

                   JSONObject obj= jsonObject.getJSONObject("data");

                  //  String obj=jsonObject.getString("data");
                    String propertyId=obj.getString("propertyid");
                    String count=obj.getString("count");
                    String for_whom=obj.getString("for_whom");

                    int roomcount= Integer.parseInt(count);
                   //Toast.makeText(PropertyInfoSecondActivity.this, count+"for_whom:-"+for_whom, Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(this,PropertyRoomsCreationActivity.class);
                    mIntent.putExtra("PropertyId", "" + propertyId);
                    mIntent.putExtra("countvalue",count);
                    mIntent.putExtra("for_whom",for_whom);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

            AppExeptionFile.handleUncaughtException(e);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void createRoomsApi(final String propertyId,final String  railway_station,
                               final String  bus_stand,final String  airport,final String  nearest,final String  square,final String  tenantType,final String  curfewTime,final String  smoking,final String  drinking,final String  noOfPeoples,
                               final String  amountRent,final String  advance,final String  maintenanses, final String waterBill,final String  otherExpenses,final String  strElectricity,final String  strNoOfVehicle)
    {
        final ProgressDialog progressDialog=new ProgressDialog(PropertyInfoSecondActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url= WebUrls.MAIN_URL;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        getResponsedata(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(PropertyInfoSecondActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> locationData = new HashMap<String, String>();
                locationData.put("action", "addPropertyDistance");
                locationData.put("propertyId",propertyId);
                locationData.put("railway_station",railway_station);
                locationData.put("bus_stand", bus_stand);
                locationData.put("airport", airport);
                locationData.put("nearest",nearest);
                locationData.put("square", square);
                locationData.put("tenant_type",tenantType);
                locationData.put("curfew_time",curfewTime);
                locationData.put("smoking",smoking);
                locationData.put("drinking",drinking);
                locationData.put("no_of_people",noOfPeoples);
                locationData.put("rent_amount",amountRent);
                locationData.put("advance",advance);
                locationData.put("maintenance", maintenanses);
                locationData.put("water_bill", waterBill);
                locationData.put("other_expenses",otherExpenses);
                locationData.put("electricity",strElectricity);
                locationData.put("policy_fix_sqr","");
                locationData.put("vehicle",strNoOfVehicle);

                return locationData;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(PropertyInfoSecondActivity.this);
        queue.add(strRequest);
    }
    public void getResponsedata(String result)
    {
       // App.MethodLog(amountRent,advance,"PropertyInfoSecondActivity","callPropertyDetail in requestResponce",result);

        try
        {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                //  String propertyId = "" + jsonObject.getInt("data");
                JSONObject obj= jsonObject.getJSONObject("data");
                //  String obj=jsonObject.getString("data");
                String propertyId=obj.getString("propertyid");
                String count=obj.getString("count");
                String for_whom=obj.getString("for_whom");

                int roomcount= Integer.parseInt(count);
                //Toast.makeText(PropertyInfoSecondActivity.this, count+"for_whom:-"+for_whom, Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(this,PropertyRoomsCreationActivity.class);
                mIntent.putExtra("PropertyId", "" + propertyId);
                mIntent.putExtra("countvalue",count);
                mIntent.putExtra("for_whom",for_whom);
                startActivity(mIntent);
                finish();
                overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
            } else {
                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }
            Log.d("result..", result);
        } catch (JSONException e)
        {
            AppExeptionFile.handleUncaughtException(e);

            e.printStackTrace();
        }
    }

    public ArrayList<StateModal> getNoOfvehicle() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("No Of Vehicle");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("0");
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

        mStateModal = new StateModal();
        mStateModal.setStrStateName("5");
        mArray.add(mStateModal);

        return mArray;
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

    public ArrayList<StateModal> getAllowSmoking() {
        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Smoking");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Yes");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("No");
        mArray.add(mStateModal);


        return mArray;
    }

    public ArrayList<StateModal> getAllowDrinking() {
        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Drinking");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Yes");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("No");
        mArray.add(mStateModal);


        return mArray;
    }

    public ArrayList<StateModal> getNoOfPeople() {
        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("No Of People");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("0");
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        mCommon.hideKeybord(this,distance_railwaystation);
        /*AlertDialogWithTwoBtn callbackDialog = new AlertDialogWithTwoBtn() {
            @Override
            public void callBack() {
                if(!isSkipOption) {
                    Intent mIntent = new Intent(PropertyInfoSecondActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {
                    Intent mIntent = new Intent(PropertyInfoSecondActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                }
            }
        };
        callbackDialog.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        callbackDialog.show(getSupportFragmentManager(), "Exit ");*/


        CustomAlertDialogwithTwoBtn VehicleRcId = new CustomAlertDialogwithTwoBtn(PropertyInfoSecondActivity.this,R.style.full_screen_dialog);
        VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        VehicleRcId.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        VehicleRcId.show();
    }


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

        public CustomAlertDialogwithTwoBtn(PropertyInfoSecondActivity a, int full_screen_dialog) {
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

                    if(!isSkipOption) {
                        Intent mIntent = new Intent(PropertyInfoSecondActivity.this, HostelListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    } else {
                        Intent mIntent = new Intent(PropertyInfoSecondActivity.this, HostelListActivity.class);
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