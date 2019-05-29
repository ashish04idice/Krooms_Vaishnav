package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/16/2016.
 */
public class ChangePasswordServiceAsynctask extends AsyncTask<String, Void, String> {
    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;
    private String deviceRegistrationId = null;
    private SharedStorage mSharedStorage = null;

    public ChangePasswordServiceAsynctask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();

        mSharedStorage = SharedStorage.getInstance(mActivity);
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

            locationData.put("action","changePassword");
            locationData.put("user_id", mSharedStorage.getUserId());
            locationData.put("old_password",params[0]);
            locationData.put("new_password",params[1]);
            locationData.put("device_type", Common.DEVICE_TYPE);

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
