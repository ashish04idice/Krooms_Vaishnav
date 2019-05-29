package com.krooms.hostel.rental.property.app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.activity.SearchedPropertyListActivity;
import com.krooms.hostel.rental.property.app.adapter.MultiChoiceAdapter;
import com.krooms.hostel.rental.property.app.callback.AreaDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.SpinnerResponce;
import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.database.GetCityAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetFeaturesAsyncTask;
import com.krooms.hostel.rental.property.app.callback.FeaturesDataBaseResponce;
import com.krooms.hostel.rental.property.app.database.GetStatesAsyncTask;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.callback.CityDataBaseResponce;
import com.krooms.hostel.rental.property.app.ccavenue_adapter.ObjectAdapter;
import com.krooms.hostel.rental.property.app.database.GetAreaAsyncTask;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/22/2016.
 */
public class SearchFilterDialog extends DialogFragment implements AreaDataBaseResponce, CityDataBaseResponce,
        StateDataBaseResponce, CompoundButton.OnCheckedChangeListener, FeaturesDataBaseResponce {

    private FragmentActivity mFActivity = null;
    private Spinner spinnerState, spinnerCity, typeOfPerson, status;
    //private Spinner area;
    private CheckBox checkBox, checkBox2, checkBox3, checkBox4, checkBox5;
    private TextView inputFacility;
    private SeekBar seekbar;
    private TextView budgetTitle;
    private Button filterGo;
    private ArrayList<String> propertyListArray = new ArrayList<>();

    private String countryStr = "India", countryId = "1", stateStr, stateId = "0", cityStr,
            cityId = "0", areaStr, areaId = "0", typeOfPersonStr = "", typeOfPersonStrId = "",
            statusStr, typeOfProperties = "", budget = "";

    private ArrayList<StateModal> mArrayStates = null;
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = new ArrayList<>();
    ;
    private ArrayList<StateModal> mArrayPersonType = null;
    private ArrayList<StateModal> mArrayStatus = null;

    private StateSpinnerAdapter mStateSpinnerAdapter, mCitySpinnerAdapter,
            mPersonTypeSpinnerAdapter, mStatusAdapter; //mAreaSpinnerAdapter,
    private String propertyFeatures;
    private ArrayList<StateModal> mArrayFeaturesList = null;
    //  private AlertDialogMultiCheckbox mFeaturesDialog;
    private AutoCompleteTextView myAutoComplete;
    private EditText edittextpropertyid;
    private ObjectAdapter mAdapter = null;

    public void getPerameter(FragmentActivity activity) {
        this.mFActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_filter_dialog, container);
        spinnerState = (Spinner) view.findViewById(R.id.spinner2);
        spinnerCity = (Spinner) view.findViewById(R.id.spinner);
        //area = (Spinner) view.findViewById(R.id.spinner3);
        typeOfPerson = (Spinner) view.findViewById(R.id.spinner4);
        status = (Spinner) view.findViewById(R.id.spinner5);
        inputFacility = (TextView) view.findViewById(R.id.inputFacility);

        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        budgetTitle = (TextView) view.findViewById(R.id.budgetTitle);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
        checkBox.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);
        checkBox5.setOnCheckedChangeListener(this);


        edittextpropertyid = (EditText) view.findViewById(R.id.Edittext);
        myAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.myautocomplete);
        myAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 666) {
                    InputMethodManager in = (InputMethodManager) mFActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(myAutoComplete.getWindowToken(), 0);
                }
                return false;
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                budgetTitle.setText("Budget: " + (progress * 1000));
                budget = "" + (progress * 1000);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        filterGo = (Button) view.findViewById(R.id.filterGoBtn);

        setListner();


        return view;
    }


    public void setListner() {

        mArrayStates = new ArrayList<>();
        String stateQ = "SELECT * FROM state where country_id = '" + countryId + "' And status = '1' ORDER BY state_name";
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(mFActivity, countryId);
        mGetStatesAsyncTask.setCallBack(this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stateQ);

        mStateSpinnerAdapter = new StateSpinnerAdapter(mFActivity, R.id.spinner2, mArrayStates);
        spinnerState.setAdapter(mStateSpinnerAdapter);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateStr = mArrayStates.get(position).getStrStateName();
                if (position != 0) {
                    stateId = mArrayStates.get(position).getStrSateId();
                    GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(mFActivity, stateId);
                    mGetCityAsyncTask.setCallBack(SearchFilterDialog.this);
                    mGetCityAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Search city data
        mArrayCity = new ArrayList<>();
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(mFActivity, "");
        mGetCityAsyncTask.setCallBack(this);
        mGetCityAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (mArrayCity.size() == 0) {
            StateModal mCity = new StateModal();
            mCity.setStrSateId("0");
            mCity.setStrStateName("City");
            mArrayCity.add(mCity);
        }

        mCitySpinnerAdapter = new StateSpinnerAdapter(mFActivity, R.id.spinner, mArrayCity);
        spinnerCity.setAdapter(mCitySpinnerAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityStr = mArrayCity.get(position).getStrStateName();
                myAutoComplete.setText("");
                if (position != 0) {
                    cityId = mArrayCity.get(position).getStrSateId();
                    areaStr = "";
                    areaId = "";
                    new GetAreaList(cityId, areaId).execute();
                   /* GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(mFActivity, cityId);
                    mGetAreaAsyncTask.setCallBack(SearchFilterDialog.this);
                    mGetAreaAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/
                } else {
                    mArrayArea.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Search area data
        mAdapter = new ObjectAdapter(mFActivity, mArrayArea);
        mAdapter.setCallBack(new SpinnerResponce() {
            @Override
            public void requestOTPServiceResponce(String id, String value) {
                areaStr = value;
                areaId = id;
                myAutoComplete.setText(areaStr);
                myAutoComplete.dismissDropDown();
            }
        });
        myAutoComplete.setAdapter(mAdapter);
        myAutoComplete.setThreshold(0);

        mArrayPersonType = new ArrayList<>();
        mArrayPersonType.addAll(getTypeOfPerson());
        mPersonTypeSpinnerAdapter = new StateSpinnerAdapter(mFActivity, R.id.spinner4, mArrayPersonType);
        typeOfPerson.setAdapter(mPersonTypeSpinnerAdapter);
        typeOfPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    typeOfPersonStr = mArrayPersonType.get(position).getStrStateName();
                    typeOfPersonStrId = mArrayPersonType.get(position).getStrSateId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArrayStatus = new ArrayList<>();
        mArrayStatus.addAll(getStatus());
        mStatusAdapter = new StateSpinnerAdapter(mFActivity, R.id.spinner5, mArrayStatus);
        status.setAdapter(mStatusAdapter);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    // areaId = mArrayPropertyType.get(position).getStrSateId();
                    statusStr = mArrayStatus.get(position).getStrStateName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfProperties = "";
                for (int i = 0; i < propertyListArray.size(); i++) {
                    typeOfProperties = typeOfProperties + "" + propertyListArray.get(i).toString();
                }

                searchUsingFilter();
                dismiss();

            }
        });


        mArrayFeaturesList = new ArrayList<>();
        GetFeaturesAsyncTask mGetFeaturesAsyncTask = new GetFeaturesAsyncTask(mFActivity);
        mGetFeaturesAsyncTask.setCallBack(this);
        mGetFeaturesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        inputFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mFeaturesDialog.show(mFActivity.getSupportFragmentManager(), "Features");


                CustomDialogClass cdd = new CustomDialogClass(mFActivity, R.style.full_screen_dialog);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(mFActivity, "Features", mArrayFeaturesList);
                cdd.show();

            }
        });

       /* mFeaturesDialog = new AlertDialogMultiCheckbox() {
            @Override
            public void callBack(ArrayList<StateModal> modalArray) {

                if (modalArray.size() > 0) {
                    propertyFeatures = "";
                    String features = "";
                    for (int i = 0; i < modalArray.size(); i++) {
                        modalArrayList.get(i).setChecked(modalArray.get(i).getChecked());
                        if (modalArray.get(i).getChecked() == 1) {
                            if (!modalArray.get(i).getStrStateName().equals("Select All")) {
                                propertyFeatures = propertyFeatures + modalArray.get(i).getStrSateId() + ",";

                                features = features + modalArray.get(i).getStrStateName() + ", ";
                            }
                        }
                    }
                    inputFacility.setText("" + features);

                }
            }
        };
        mFeaturesDialog.getPerameter(mFActivity, "Features", mArrayFeaturesList);*/
    }


    @Override
    public void requestStateDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayStates.clear();
        mArrayStates.addAll(mArray);
        if (mArrayStates != null && mArrayStates.size() > 0) {
            for (int i = 0; i < mArrayStates.size(); i++) {
                if (mArrayStates.get(i).getStrSateId().equals(cityId)) {
                    spinnerState.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();
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
                    //spinnerArea.setSelection(i);
                    myAutoComplete.setText("" + mArrayArea.get(i).getStrSateId());
                }
            }
        } else {
            mArrayArea.clear();
        }
        mFActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private static boolean permissionDialogShown = false;

    @Override
    public void show(FragmentManager manager, String tag) {
        if (permissionDialogShown) {
            return;
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        super.show(manager, tag);
        permissionDialogShown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        permissionDialogShown = false;
        super.onDismiss(dialog);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.checkBox:

                if (isChecked) {
                    if (propertyListArray.size() == 0) {
                        propertyListArray.add("1");
                    } else {
                        propertyListArray.add(",1");
                    }
                } else {
                    if (propertyListArray.contains(",1")) {
                        propertyListArray.remove(",1");
                    } else if (propertyListArray.contains("1")) {
                        propertyListArray.remove("1");
                    }
                }

                break;
            case R.id.checkBox2:

                if (isChecked) {
                    if (propertyListArray.size() == 0) {
                        propertyListArray.add("2");
                    } else {
                        propertyListArray.add(",2");
                    }
                } else {
                    if (propertyListArray.contains(",2")) {
                        propertyListArray.remove(",2");
                    } else if (propertyListArray.contains("2")) {
                        propertyListArray.remove("2");
                    }
                }

                break;
            case R.id.checkBox3:

                if (isChecked) {
                    if (propertyListArray.size() == 0) {
                        propertyListArray.add("3");
                    } else {
                        propertyListArray.add(",3");
                    }
                } else {
                    if (propertyListArray.contains(",3")) {
                        propertyListArray.remove(",3");
                    } else if (propertyListArray.contains("3")) {
                        propertyListArray.remove("3");
                    }
                }

                break;
            case R.id.checkBox4:

                if (isChecked) {
                    if (propertyListArray.size() == 0) {
                        propertyListArray.add("4");
                    } else {
                        propertyListArray.add(",4");
                    }
                } else {
                    if (propertyListArray.contains(",4")) {
                        propertyListArray.remove(",4");
                    } else if (propertyListArray.contains("4")) {
                        propertyListArray.remove("4");
                    }
                }

                break;
            case R.id.checkBox5:
                if (isChecked) {
                    if (propertyListArray.size() == 0) {
                        propertyListArray.add("5");
                    } else {
                        propertyListArray.add(",5");
                    }
                } else {
                    if (propertyListArray.contains(",5")) {
                        propertyListArray.remove(",5");
                    } else if (propertyListArray.contains("5")) {
                        propertyListArray.remove("5");
                    }
                }

                break;

        }

    }

    @Override
    public void requestFeaturesDataBaseResponce(ArrayList<StateModal> mArray) {

        mArrayFeaturesList.addAll(mArray);
        if (propertyFeatures != null && propertyFeatures.length() > 0) {
            String[] arrayFeature = propertyFeatures.split(",");
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
                inputFacility.setText("" + features);
            }
        }

    }

    public ArrayList<StateModal> getTypeOfPerson() {
        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Preferred Co-tenant");
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

    public ArrayList<StateModal> getStatus() {
        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Room type");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Single");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Sharing");
        mArray.add(mStateModal);

        return mArray;
    }


    public void searchUsingFilter() {

        String pid = edittextpropertyid.getText().toString().trim();
        String features = "";
        if (propertyFeatures != null && propertyFeatures.length() > 0) {
            features = propertyFeatures;
        }
        if (features.equals("Select Features")) {
            features = "0";
        }

        if (SearchedPropertyListActivity.searchPropertyActivity != null) {
            SearchedPropertyListActivity.searchPropertyActivity.finish();
        }
        Intent it = new Intent(mFActivity, SearchedPropertyListActivity.class);
//        it.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        it.putExtra("from", "filterDialog");
        it.putExtra("propertytype", typeOfProperties);
        it.putExtra("state", stateId);
        it.putExtra("city", cityId);
        it.putExtra("landmark", areaId);
        it.putExtra("PropertyIdSearch", pid);//change by as
        it.putExtra("tenantType", typeOfPersonStrId);
        it.putExtra("status", statusStr);
        if (features.equals("")) {
            it.putExtra("propertyFeature", features);
        } else {
            it.putExtra("propertyFeature", features.subSequence(0, features.lastIndexOf(",")));
        }
        it.putExtra("rentAmount", budget);
        mFActivity.startActivity(it);

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
            pd = new ProgressDialog(getActivity());
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                mArrayArea.clear();
            }

        }
    }


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

                    } else if (titleStr.equals("Features")) {
                        if (modalArrayList.size() > 0) {
                            propertyFeatures = "";
                            String features = "";
                            for (int i = 0; i < modalArrayList.size(); i++) {
                                modalArrayList.get(i).setChecked(modalArrayList.get(i).getChecked());
                                if (modalArrayList.get(i).getChecked() == 1) {
                                    if (!modalArrayList.get(i).getStrStateName().equals("Select All")) {
                                        propertyFeatures = propertyFeatures + modalArrayList.get(i).getStrSateId() + ",";

                                        features = features + modalArrayList.get(i).getStrStateName() + ", ";
                                    }
                                }
                            }
                            inputFacility.setText("" + features);

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


}


