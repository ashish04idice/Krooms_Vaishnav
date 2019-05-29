package com.krooms.hostel.rental.property.app.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.callback.FeaturesDataBaseResponce;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by user on 2/26/2016.
 */
public class GetFeaturesAsyncTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    private ArrayList<StateModal> mArrayArea = null;
    private FeaturesDataBaseResponce mDataBaseResponce;
    private DataBaseAdapter mDBHealper;

    public GetFeaturesAsyncTask(Activity activity) {
        this.mActivity = activity;
        this.mArrayArea = new ArrayList<>();
        mDBHealper = new DataBaseAdapter(mActivity);
        mDBHealper.createDatabase();
        mDBHealper.open();
    }

    public void setCallBack(FeaturesDataBaseResponce dataBaseResponce) {
        mDataBaseResponce = dataBaseResponce;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            Cursor mCursor = mDBHealper.getSqlqureies("SELECT * FROM features ORDER BY features_name");
            if (mCursor.getCount() > 0) {
                mArrayArea.clear();
                StateModal mCountry = new StateModal();
                mCountry.setStrSateId("0");
                mCountry.setStrStateName("Select All");
                mArrayArea.add(mCountry);
                if (mCursor.moveToFirst()) {
                    do {
                        mCountry = new StateModal();
                        //String countryId = mCursor.getString(mCursor.getColumnIndex("city_id"));
                        String stateId = mCursor.getString(mCursor.getColumnIndex("features_id"));
                        String stateName = mCursor.getString(mCursor.getColumnIndex("features_name"));
                        LogConfig.logd("Data Features id =", "" + stateId + " name =" + stateName);
                        mCountry.setCountryId("");
                        mCountry.setStrSateId(stateId);
                        mCountry.setStrStateName(stateName);
                        mArrayArea.add(mCountry);

                    } while (mCursor.moveToNext());
                    mCursor.close();
                }
                return "Record";
            } else {
                return "";
            }
        } catch (SQLiteException e) {
            return "";
        } finally {
            mDBHealper.close();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mArrayArea.size() > 0) {
            mDataBaseResponce.requestFeaturesDataBaseResponce(mArrayArea);
        }
    }
}
