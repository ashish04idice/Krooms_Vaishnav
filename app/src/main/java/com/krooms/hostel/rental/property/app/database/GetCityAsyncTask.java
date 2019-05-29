package com.krooms.hostel.rental.property.app.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.callback.CityDataBaseResponce;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by user on 2/26/2016.
 */
public class GetCityAsyncTask extends AsyncTask<String, Void, String> {

    private String stateId = "";
    private Activity mActivity;
    private ArrayList<StateModal> mArrayCity = null;
    private CityDataBaseResponce mDataBaseResponce;
    private DataBaseAdapter mDBHealper;
    private String sqlQuery = "";

    public GetCityAsyncTask(Activity activity, String id) {
        this.mActivity = activity;
        this.stateId = id;
        mArrayCity = new ArrayList<>();

        if (stateId.length() == 0) {
            sqlQuery = "SELECT * FROM city ORDER BY city_name";
        } else {
            sqlQuery = "SELECT * FROM city where state_id = '" + stateId + "' ORDER BY city_name";
        }
        mDBHealper = new DataBaseAdapter(mActivity);
        mDBHealper.createDatabase();
        mDBHealper.open();
    }

    public void setCallBack(CityDataBaseResponce dataBaseResponce) {
        mDataBaseResponce = dataBaseResponce;
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            Cursor mCursor = mDBHealper.getSqlqureies(sqlQuery);
            if (mCursor.getCount() > 0) {
                mArrayCity.clear();
                StateModal mCountry = new StateModal();
                mCountry.setStrSateId("0");
                mCountry.setStrStateName("City");
                mArrayCity.add(mCountry);
                if (mCursor.moveToFirst()) {
                    do {
                        mCountry = new StateModal();
                        String countryId = mCursor.getString(mCursor.getColumnIndex("state_id"));
                        String stateId = mCursor.getString(mCursor.getColumnIndex("city_id"));
                        String stateName = mCursor.getString(mCursor.getColumnIndex("city_name"));
                        LogConfig.logd("Data City id =", "" + stateId + " name =" + stateName);
                        mCountry.setCountryId(countryId);
                        mCountry.setStrSateId(stateId);
                        mCountry.setStrStateName(stateName);
                        mArrayCity.add(mCountry);

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
        if (mArrayCity.size() > 0) {
            mDataBaseResponce.requestCityDataBaseResponce(mArrayCity);
        }
    }
}
