package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/23/2016.
 */
public class SearchPropertyServiceAsyncTask extends AsyncTask<String, Void, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;
    private String deviceRegistrationId = null;
    public SearchPropertyServiceAsyncTask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();
        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
        deviceRegistrationId = mSharedStorage.getPushRegistrationID();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        Common mCommon = new Common();

        JSONObject locationData = new JSONObject();
        try {
            locationData.put("action", "searchProperty");
            locationData.put("propertytype", params[0]);
            locationData.put("state", params[1]);
            locationData.put("city", params[2]);
            locationData.put("landmark", params[3]);
            locationData.put("tenantType",params[4]);
            locationData.put("status", params[5]);
            locationData.put("propertyFeature",params[6]);
            locationData.put("record_per_page", params[7]);
            locationData.put("rentAmount",params[8]);
            locationData.put("page_no",params[9]);
            locationData.put("sort",params[10]);
            locationData.put("property_id",params[11]);// add parameter for search propertyid

        } catch (JSONException e) {
        }

        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        pDialog.dismiss();
        mServiceResponce.requestResponce(result);

    }
}