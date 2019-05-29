package com.krooms.hostel.rental.property.app.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by user on 2/26/2016.
 */
public class GetStatesAsyncTask extends AsyncTask<String, Void, String> {

    private String countryId = "";
    private Activity mActivity;
    private ArrayList<StateModal> mArrayStates = null;
    private StateDataBaseResponce mDataBaseResponce;

    public GetStatesAsyncTask(Activity activity, String id) {
        this.mActivity = activity;
        this.countryId = id;
        mArrayStates = new ArrayList<>();
    }

    public void setCallBack(StateDataBaseResponce stateDataBaseResponce) {
        mDataBaseResponce = stateDataBaseResponce;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            DataBaseAdapter mDBHealper = new DataBaseAdapter(mActivity);
            mDBHealper.createDatabase();
            mDBHealper.open();
            Cursor mCursor = mDBHealper.getSqlqureies(/*"SELECT * FROM state where country_id = '" + countryId + "'"*/params[0]);
            if (mCursor.getCount() > 0) {
                mArrayStates.clear();
                StateModal mCountry = new StateModal();
                mCountry.setStrSateId("0");
                mCountry.setStrStateName("State");
                mArrayStates.add(mCountry);
                try {
                    if (mCursor.moveToFirst()) {
                        do {
                            mCountry = new StateModal();
                            String countryId = mCursor.getString(mCursor.getColumnIndex("country_id"));
                            String stateId = mCursor.getString(mCursor.getColumnIndex("state_id"));
                            String stateName = mCursor.getString(mCursor.getColumnIndex("state_name"));
                            LogConfig.logd("Data States id =", "" + stateId + " name =" + stateName);
                            mCountry.setCountryId(countryId);
                            mCountry.setStrSateId(stateId);
                            mCountry.setStrStateName(stateName);
                            mArrayStates.add(mCountry);

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
            return "";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mArrayStates.size() > 0) {
            mDataBaseResponce.requestStateDataBaseResponce(mArrayStates);
        }
    }
}