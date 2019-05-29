package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 7/28/2016.
 */
public class GetParentDetailServiceAsyncTask extends AsyncTask<String, Void, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;
    private ProgressDialog pDialog = null;
    private String deviceRegistrationId = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }


    public GetParentDetailServiceAsyncTask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();
        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
        deviceRegistrationId = mSharedStorage.getPushRegistrationID();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected String doInBackground(String... params) {

        Common mCommon = new Common();
        JSONObject locationData = new JSONObject();
        try {

            locationData.put("action", "getparentdetails");
            locationData.put("user_id", params[0]);

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
