package com.krooms.hostel.rental.property.app.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.DataBaseResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by user on 2/26/2016.
 */
public class GetWorkDetailAsyncTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    private ArrayList<StateModal> mArray = null;
    private DataBaseResponce mDataBaseResponce;

    public GetWorkDetailAsyncTask(Activity activity) {
        this.mActivity = activity;
        this.mArray = new ArrayList<>();

    }

    public void setCallBack(DataBaseResponce dataBaseResponce) {
        mDataBaseResponce = dataBaseResponce;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            DataBaseAdapter mDBHealper = new DataBaseAdapter(mActivity);
            mDBHealper.createDatabase();
            mDBHealper.open();
            Cursor mCursor = mDBHealper.getSqlqureies("SELECT * FROM work_detail ORDER BY work_name");
            if (mCursor.getCount() > 0) {
                mArray.clear();
                StateModal mCountry = new StateModal();
                mCountry.setStrSateId("0");
                mCountry.setStrStateName("Select Work Detail");
                mArray.add(mCountry);
                try {
                    if (mCursor.moveToFirst()) {
                        do {
                            mCountry = new StateModal();
                            String stateId = mCursor.getString(mCursor.getColumnIndex("work_id"));
                            String stateName = mCursor.getString(mCursor.getColumnIndex("work_name"));
                            LogConfig.logd("Data work id =", "" + stateId + " name =" + stateName);
                            mCountry.setStrSateId(stateId);
                            mCountry.setStrStateName(stateName);
                            mArray.add(mCountry);

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
        if (s.length() > 0) {
            mDataBaseResponce.requestDataBaseResponce(mArray);
        }
    }
}
