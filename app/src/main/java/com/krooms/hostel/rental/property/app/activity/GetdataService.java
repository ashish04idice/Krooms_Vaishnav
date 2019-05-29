package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.LocalDataInsertUpdateAsyntask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.modal.ElectricityModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 26-Feb-19.
 */

public class GetdataService extends Service {

    private String deviceRegistrationId = "";
    private Common mCommon;
    private Validation mValidation;
    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        callLocalDataInsertUpdate();

        return START_NOT_STICKY;
    }

    private void callLocalDataInsertUpdate() {

        mCommon = new Common();
        mValidation = new Validation(this);

        DataBaseAdapter mDBHealper = new DataBaseAdapter(this);
        mDBHealper.createDatabase();
        mDBHealper.open();
        String lastUpdateDate = "0000-00-00 00:00:00";
        Cursor mCursor = mDBHealper.getSqlqureies("Select *From sync_history where sync_type = 'local'");
        SharedStorage mSharedStorage = SharedStorage.getInstance(this);
        deviceRegistrationId = mSharedStorage.getPushRegistrationID();
        mDBHealper.close();
        getLocalDataInsertUpdate(lastUpdateDate);

    }


    //get tenant details update//

    private void getLocalDataInsertUpdate(String lastUpdateDate) {


        try {
            JSONObject params = new JSONObject();
            params.put("action", "syncMobileData");
            params.put("last_modified_timestamp", lastUpdateDate);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //dialog.dismiss();
                    String result = response.toString();
                    getResponseDataupdate(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDataupdate(String result) {
        try {
            String status, message;
            JSONObject jsonObject = new JSONObject(result);
            status = jsonObject.getString(WebUrls.STATUS_JSON);

            if (status.equalsIgnoreCase("true")) {

                DataBaseAdapter mDBHealper = new DataBaseAdapter(this);
                mDBHealper.createDatabase();
                mDBHealper.open();
                Cursor mCursor = mDBHealper.getSqlqureies("Select *From sync_history where sync_type = 'local'");
                if (mCursor.getCount() > 0) {
                    ContentValues cv = new ContentValues();
                    cv.put("sync_date", "" + jsonObject.getString("last_timestamp"));
                    mDBHealper.updateSynHistoryData("sync_history", cv, "local");
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("sync_type", "local");
                    cv.put("sync_date", "" + jsonObject.getString("last_timestamp"));
                    mDBHealper.savedSynHistoryData(cv);
                }

                JSONObject data = jsonObject.getJSONObject(WebUrls.DATA_JSON);
                int countAminities = data.getInt("count_aminities");
                Cursor mCursorAminites = mDBHealper.getSqlqureies("Select *From aminities");
                if (countAminities > mCursorAminites.getCount()) {
                    String aminities = data.getString("aminities");
                    if (aminities != null && aminities.length() > 0) {
                        JSONArray aminiJsonArray = data.getJSONArray("aminities");
                        for (int i = 0; i < aminiJsonArray.length(); i++) {
                            JSONObject aminitiesData = aminiJsonArray.getJSONObject(i);
                            ContentValues cv = new ContentValues();
                            cv.put("aminities_id", aminitiesData.getString("amenity_id"));
                            cv.put("aminities_name", aminitiesData.getString("amenity_name"));
                            mDBHealper.savedRoomsInfo("aminities", cv);
                        }
                    }
                }

                int countFeature = data.getInt("count_countries");
                Cursor mCursorFeature = mDBHealper.getSqlqureies("Select *From features");
                if (countFeature > mCursorFeature.getCount()) {
                    String features = data.getString("features");
                    if (features != null && features.length() > 0) {
                        JSONArray featuresJsonArray = data.getJSONArray("features");
                        for (int i = 0; i < featuresJsonArray.length(); i++) {
                            JSONObject featuresData = featuresJsonArray.getJSONObject(i);
                            ContentValues cv = new ContentValues();
                            cv.put("features_id", featuresData.getString("feature_id"));
                            cv.put("features_name", featuresData.getString("feature_name"));
                            mDBHealper.savedRoomsInfo("features", cv);
                        }
                    }
                }
                int countCountry = data.getInt("count_countries");
                Cursor mCursorCountry = mDBHealper.getSqlqureies("Select *From country");
                if (countCountry > mCursorCountry.getCount()) {
                    mDBHealper.getSqlqureies("DELETE FROM country");
                    String countries = data.getString("countries");
                    if (countries != null && countries.length() > 0) {
                        JSONArray countriesJsonArray = data.getJSONArray("countries");
                        for (int i = 0; i < countriesJsonArray.length(); i++) {
                            JSONObject countryData = countriesJsonArray.getJSONObject(i);
                            ContentValues cv = new ContentValues();
                            cv.put("country_id", countryData.getString("id"));
                            cv.put("country_name", countryData.getString("country_name"));
                            mDBHealper.savedRoomsInfo("country", cv);
                        }
                    }
                }

                int countState = data.getInt("count_state");
                Cursor mCursorState = mDBHealper.getSqlqureies("Select *From state");
                if (countState > mCursorState.getCount()) {
                    mDBHealper.getSqlqureies("DELETE FROM state");
                    String states = data.getString("states");
                    if (states != null && states.length() > 0) {
                        JSONArray statesJsonArray = data.getJSONArray("states");
                        for (int i = 0; i < statesJsonArray.length(); i++) {
                            JSONObject stateData = statesJsonArray.getJSONObject(i);
                            ContentValues cv = new ContentValues();
                            cv.put("country_id", stateData.getString("country_id"));
                            cv.put("state_id", stateData.getString("id"));
                            cv.put("state_name", stateData.getString("state_name"));
                            cv.put("status", stateData.getString("status"));
                            mDBHealper.savedRoomsInfo("state", cv);
                        }
                    }
                }

                int countCity = data.getInt("count_city");
                Cursor mCursorCity = mDBHealper.getSqlqureies("Select *From city");
                if (countCity > mCursorCity.getCount()) {
                    mDBHealper.getSqlqureies("DELETE FROM city");
                    String cities = data.getString("cities");
                    if (cities != null && cities.length() > 0) {
                        JSONArray citiesJsonArray = data.getJSONArray("cities");
                        for (int i = 0; i < citiesJsonArray.length(); i++) {

                            JSONObject cityData = citiesJsonArray.getJSONObject(i);
                            ContentValues cv = new ContentValues();
                            Common.Config("city name ...... " + cityData.getString("city_name"));
                            cv.put("state_id", cityData.getString("state_id"));
                            cv.put("city_id", cityData.getString("id"));
                            cv.put("city_name", cityData.getString("city_name"));
                            mDBHealper.savedRoomsInfo("city", cv);
                        }
                    }
                }
                mDBHealper.close();

            } else {


            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
