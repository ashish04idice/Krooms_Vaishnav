package com.krooms.hostel.rental.property.app.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.AreaDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by user on 2/26/2016.
 */
public class GetAreaAsyncTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    private ArrayList<StateModal> mArrayArea = null;
    private AreaDataBaseResponce mDataBaseResponce;
    private String cityId;
    private DataBaseAdapter mDBHealper;

    public GetAreaAsyncTask(Activity activity, String id) {
        this.mActivity = activity;
        this.cityId = id;
        this.mArrayArea = new ArrayList<>();
        mDBHealper = new DataBaseAdapter(mActivity);
        mDBHealper.createDatabase();
        mDBHealper.open();
    }

    public void setCallBack(AreaDataBaseResponce dataBaseResponce) {
        mDataBaseResponce = dataBaseResponce;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            Cursor mCursor = mDBHealper.getSqlqureies("SELECT * FROM area where city_id = '" + cityId + "' ORDER BY area_name");
            if (mCursor.getCount() > 0) {
                mArrayArea.clear();
                StateModal mCountry = new StateModal();
                /*mCountry.setStrSateId("0");
                mCountry.setStrStateName("");
                mArrayArea.add(mCountry);*/
                if (mCursor.moveToFirst()) {
                    do {
                        mCountry = new StateModal();
                        String countryId = mCursor.getString(mCursor.getColumnIndex("city_id"));
                        String stateId = mCursor.getString(mCursor.getColumnIndex("area_id"));
                        String stateName = mCursor.getString(mCursor.getColumnIndex("area_name"));
                        LogConfig.logd("Data Area id =", "" + stateId + " name =" + stateName);
                        mCountry.setCountryId(countryId);
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
            mDataBaseResponce.requestAreaDataBaseResponce(mArrayArea);
        }
    }
}
