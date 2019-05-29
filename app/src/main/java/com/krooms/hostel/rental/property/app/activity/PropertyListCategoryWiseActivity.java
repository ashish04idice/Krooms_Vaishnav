package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.callback.AreaDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.CityDataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.SpinnerResponce;
import com.krooms.hostel.rental.property.app.ccavenue_adapter.ObjectAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.database.GetAreaAsyncTask;
import com.krooms.hostel.rental.property.app.database.GetCityAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.SearchFilterDialog;
import com.krooms.hostel.rental.property.app.dialog.SortKeyWordSelectionDialog;
import com.krooms.hostel.rental.property.app.fragments.PropertyListCategoryWiseFragment;
import com.krooms.hostel.rental.property.app.fragments.PropertyListMapCategoryWiseFragment;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
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

public class PropertyListCategoryWiseActivity extends FragmentActivity implements View.OnClickListener, CityDataBaseResponce, AreaDataBaseResponce {

    private Common mCommon = null;
    private RelativeLayout filterBtnLayout, sortBtnLayout, mapBtnLayout;
    private TextView mapText;
    private Spinner spinnerCity;
    private ImageButton searchBtn = null;
    private ImageButton main_header_menu = null;
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = null;
    private StateSpinnerAdapter mCitySpinnerAdapter/*, mAreaSpinnerAdapter*/;
    private String city, cityId = "", area, areaId = "0";
    private AutoCompleteTextView myAutoComplete;
    private ObjectAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_property_list_category_wise);
            mCommon = new Common();

            if (PropertyListCategoryWiseFragment.list != null) {
                PropertyListCategoryWiseFragment.list.clear();
            }

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

            filterBtnLayout = (RelativeLayout) findViewById(R.id.filterBtnLayout);
            sortBtnLayout = (RelativeLayout) findViewById(R.id.sortBtnLayout);
            mapBtnLayout = (RelativeLayout) findViewById(R.id.mapBtnLayout);
            mapText = (TextView) findViewById(R.id.mapText);
            spinnerCity = (Spinner) findViewById(R.id.cityInput);

            myAutoComplete = (AutoCompleteTextView) findViewById(R.id.areaInput);

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

            searchBtn = (ImageButton) findViewById(R.id.searchBtn);
            main_header_menu = (ImageButton) findViewById(R.id.main_header_menu);


            if (savedInstanceState == null) {
                displayView(7, "");
            }

            setListner();
        } catch (Exception e) {
            finish();
        }
    }


    public void setListner() {

        // Search area data
        mArrayArea = new ArrayList<>();
        // Search city data
        mArrayCity = new ArrayList<>();
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask(PropertyListCategoryWiseActivity.this, "");
        mGetCityAsyncTask.setCallBack(PropertyListCategoryWiseActivity.this);
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
                    cityId = mArrayCity.get(position).getStrSateId();
                    area = "";
                    areaId = "";
                    new GetAreaList(cityId, areaId).execute();

                   // GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(PropertyListCategoryWiseActivity.this, cityId);
                    //mGetAreaAsyncTask.setCallBack(PropertyListCategoryWiseActivity.this);
                   // mGetAreaAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }else {
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
                area = value;
                areaId = id;
                myAutoComplete.setText(area);
                myAutoComplete.dismissDropDown();
            }
        });
        myAutoComplete.setAdapter(mAdapter);
        myAutoComplete.setThreshold(0);
        filterBtnLayout.setOnClickListener(this);
        sortBtnLayout.setOnClickListener(this);
        mapBtnLayout.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        main_header_menu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.filterBtnLayout:
                SearchFilterDialog filterDialog = new SearchFilterDialog();
                filterDialog.getPerameter(this);
                filterDialog.show(this.getSupportFragmentManager(), "searchDialog");
                break;
            case R.id.sortBtnLayout:
                /*SortKeyWordSelectionDialog keyWordDialog = new SortKeyWordSelectionDialog() {
                    @Override
                    public void goBtnEvent(String str) {
                        displayView(1, str);

                    }
                };
                keyWordDialog.getPerameter(this);
                keyWordDialog.show(this.getSupportFragmentManager(), "sortKeyword");
*/

                CustomDialogClass VehicleRcId = new CustomDialogClass(PropertyListCategoryWiseActivity.this, R.style.full_screen_dialog);
                VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VehicleRcId.getPerameter(this);
                VehicleRcId.show();

                break;
            case R.id.mapBtnLayout:
                if (mapText.getText().equals("Map")) {
                    mapText.setText("List");
                    displayView(0, "");
                } else {
                    mapText.setText("Map");
                    displayView(1, "");

                }

                break;

            case R.id.main_header_menu:
                finish();
                break;

            case R.id.searchBtn:
                Intent it = new Intent(PropertyListCategoryWiseActivity.this, SearchedPropertyListActivity.class);
                it.putExtra("from", "normalSearch");
                it.putExtra("city", cityId);
                it.putExtra("area", areaId);
                startActivity(it);
                break;

        }
    }

    private void displayView(int position, String sortKeyWord) {

        // update the main content by replacing fragments
        Fragment mFragment = null;
        switch (position) {
            case 0:
                PropertyListMapCategoryWiseFragment mFragment1 = new PropertyListMapCategoryWiseFragment();
                mFragment1.getParameter(PropertyListCategoryWiseFragment.list);
                mFragment = mFragment1;
                break;
            case 1:
                PropertyListCategoryWiseFragment mFragment2 = new PropertyListCategoryWiseFragment();
                mFragment2.getPerameter(sortKeyWord);
                mFragment = mFragment2;
                break;
            default:
                PropertyListCategoryWiseFragment mFragment3 = new PropertyListCategoryWiseFragment();
                mFragment3.getPerameter(sortKeyWord);
                mFragment = mFragment3;
                break;

        }

        if (mFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                clearBackStack(fragmentManager);
            }
            fragmentManager.beginTransaction().replace(R.id.hostelList, mFragment).commit();
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
                for (int i = 0; i < mArrayArea.size(); i++) {
                    if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                        //spinnerArea.setSelection(i);
                        myAutoComplete.setText("" + mArrayArea.get(i).getStrStateName());
                    }
                }
            }else{
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


    public void clearBackStack(FragmentManager manager) {
        int rootFragment = manager.getBackStackEntryAt(0).getId();
        manager.popBackStack(rootFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        if (mapText.getText().equals("List")) {
            mapText.setText("Map");
            displayView(1, "");
        } else {
            finish();
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
            pd = new ProgressDialog(PropertyListCategoryWiseActivity.this);
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
                Toast.makeText(PropertyListCategoryWiseActivity.this, message, Toast.LENGTH_LONG).show();
                mArrayArea.clear();
            }

        }
    }



    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
public class CustomDialogClass extends Dialog{

    private FragmentActivity mFActivity = null;
    private RadioGroup keywordSelection = null;
    private RadioButton name, budget;
    private Button goBtn = null;

    String keyWord;
    boolean bool = false;
    public CustomDialogClass(PropertyListCategoryWiseActivity a, int full_screen_dialog) {
        super(a, full_screen_dialog);
        // TODO Auto-generated constructor stub
    }

    public void getPerameter(FragmentActivity activity) {
        this.mFActivity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.sort_keyword_selection);

        keywordSelection = (RadioGroup) findViewById(R.id.selectSortOption);

        name = (RadioButton) findViewById(R.id.sortName);
        budget = (RadioButton) findViewById(R.id.sortBudget);



//

        goBtn = (Button) findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                keyWord  = name.isChecked()?"property_name":budget.isChecked()?"rent_amount":"";
                displayView(1, keyWord);
                dismiss();
            }
        });

    }


}

}