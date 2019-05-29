package com.krooms.hostel.rental.property.app.dialog;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by user on 3/2/2016.
 */
public abstract class SearchPropertyDialog extends DialogFragment {

    private FragmentActivity mFActivity = null;

    private Button goBtn = null;

    Spinner spinnerCity, spinnerArea;

    private StateSpinnerAdapter mCitySpinnerAdapter, mAreaSpinnerAdapter;

    private String city, cityId = "0", area, areaId = "0";
    private ArrayList<StateModal> mArrayCity = null;
    private ArrayList<StateModal> mArrayArea = null;


    public SearchPropertyDialog() {
    }

    public void getPerameter(FragmentActivity activity) {
        this.mFActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_property_dialog, container);
        createView(view);
        return view;

    }

    String keyWord;
    boolean bool = false;

    public void createView(View v) {

        spinnerCity = (Spinner) v.findViewById(R.id.spinner_city);
        spinnerArea = (Spinner) v.findViewById(R.id.spinner_area);


        mArrayCity = new ArrayList<>();
        GetCityAsyncTask mGetCityAsyncTask = new GetCityAsyncTask();
//        mGetCityAsyncTask.setCallBack(this);
        mGetCityAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        StateModal mCity = new StateModal();
        mCity.setStrSateId("0");
        mCity.setStrStateName("City");
        mArrayCity.add(mCity);
        mCitySpinnerAdapter = new StateSpinnerAdapter(mFActivity, R.id.spinner_property_city, mArrayCity);
        spinnerCity.setAdapter(mCitySpinnerAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = mArrayCity.get(position).getStrStateName();
                if (position != 0) {
                    cityId = mArrayCity.get(position).getStrSateId();
                    GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(cityId);
//                    mGetAreaAsyncTask.setCallBack(SearchPropertyDialog.this);
                    mGetAreaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Search area data
        mArrayArea = new ArrayList<>();
        GetAreaAsyncTask mGetAreaAsyncTask = new GetAreaAsyncTask(cityId);
//        mGetAreaAsyncTask.setCallBack(this);
        mGetAreaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        StateModal mArea = new StateModal();
        mArea.setStrSateId("0");
        mArea.setStrStateName("Area");
        mArrayArea.add(mArea);
        mAreaSpinnerAdapter = new StateSpinnerAdapter(mFActivity, R.id.spinner_property_area, mArrayArea);
        spinnerArea.setAdapter(mAreaSpinnerAdapter);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = mArrayArea.get(position).getStrStateName();
                if (position != 0) {
                    areaId = mArrayArea.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        goBtn = (Button) v.findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBtnEvent(cityId, areaId);
                dismiss();
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

    private class GetCityAsyncTask extends AsyncTask<String, Void, String> {


        public GetCityAsyncTask() {


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                DataBaseAdapter mDBHealper = new DataBaseAdapter(mFActivity);
                mDBHealper.createDatabase();
                mDBHealper.open();
                Cursor mCursor = mDBHealper.getsqlRecords("SELECT * FROM city");
                if (mCursor.getCount() > 0) {
                    mArrayCity.clear();
                    StateModal mCountry = new StateModal();
                    mCountry.setStrSateId("0");
                    mCountry.setStrStateName("City");
                    mArrayCity.add(mCountry);
                    try {
                        if (mCursor.moveToFirst()) {
                            do {
                                mCountry = new StateModal();
                                String countryId = mCursor.getString(mCursor.getColumnIndex("state_id"));
                                String stateId = mCursor.getString(mCursor.getColumnIndex("city_id"));
                                String stateName = mCursor.getString(mCursor.getColumnIndex("city_name"));
                                LogConfig.logd("Data City id =", "" + stateId + " name =" + stateName);
                                mCountry.setStrCountryId(countryId);
                                mCountry.setStrSateId(stateId);
                                mCountry.setStrStateName(stateName);
                                mArrayCity.add(mCountry);

                            } while (mCursor.moveToNext());
                            mCursor.close();
                        }
                    } catch (SQLiteException se) {
                        LogConfig.logd(getClass().getSimpleName(), "Could not create or Open the database");
                    } finally {
                        mDBHealper.close();
                    }
                    return "Record";
                } else {
                    return "";
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
                return "";
            } catch (RuntimeException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.length() > 0) {
                if (mArrayCity != null && mArrayCity.size() > 0) {
                    for (int i = 0; i < mArrayCity.size(); i++) {
                        if (mArrayCity.get(i).getStrSateId().equals(cityId)) {
                            spinnerCity.setSelection(i);
                        }
                    }
                }
                mCitySpinnerAdapter.notifyDataSetChanged();
            }
        }
    }

    private class GetAreaAsyncTask extends AsyncTask<String, Void, String> {

        private String cityId = "";

        public GetAreaAsyncTask(String id) {

            this.cityId = id;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                DataBaseAdapter mDBHealper = new DataBaseAdapter(mFActivity);
                mDBHealper.createDatabase();
                mDBHealper.open();
                Cursor mCursor = mDBHealper.getsqlRecords("SELECT * FROM area where city_id = '" + cityId + "'");
                if (mCursor.getCount() > 0) {
                    mArrayArea.clear();
                    StateModal mCountry = new StateModal();
                    mCountry.setStrSateId("0");
                    mCountry.setStrStateName("Area");
                    mArrayArea.add(mCountry);
                    try {
                        if (mCursor.moveToFirst()) {
                            do {
                                mCountry = new StateModal();
                                String countryId = mCursor.getString(mCursor.getColumnIndex("city_id"));
                                String stateId = mCursor.getString(mCursor.getColumnIndex("area_id"));
                                String stateName = mCursor.getString(mCursor.getColumnIndex("area_name"));
                                LogConfig.logd("Data Area id =", "" + stateId + " name =" + stateName);
                                mCountry.setStrCountryId(countryId);
                                mCountry.setStrSateId(stateId);
                                mCountry.setStrStateName(stateName);
                                mArrayArea.add(mCountry);

                            } while (mCursor.moveToNext());
                            mCursor.close();
                        }
                    } catch (SQLiteException se) {
                        LogConfig.logd(getClass().getSimpleName(), "Could not create or Open the database");
                    } finally {
                        mDBHealper.close();
                    }
                    return "Record";
                } else {
                    return "";
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
                return "";
            } catch (RuntimeException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.length() > 0) {
                if (mArrayArea != null && mArrayArea.size() > 0) {
                    for (int i = 0; i < mArrayArea.size(); i++) {
                        if (mArrayArea.get(i).getStrSateId().equals(areaId)) {
                            spinnerArea.setSelection(i);
                        }
                    }
                }
                mAreaSpinnerAdapter.notifyDataSetChanged();
            }
        }
    }


    public abstract void goBtnEvent(String cityId, String areaId);
}
