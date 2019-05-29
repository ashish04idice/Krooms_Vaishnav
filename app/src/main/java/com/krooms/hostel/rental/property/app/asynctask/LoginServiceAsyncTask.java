package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/16/2016.
 */
public class LoginServiceAsyncTask extends AsyncTask<String, Void, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;
    private String deviceRegistrationId = null;
    String refreshedToken = null;

    public LoginServiceAsyncTask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();

        SharedStorage mSharedStorage = SharedStorage.getInstance(mActivity);
        //cmd by ashish on 21/02/2019 in working
        deviceRegistrationId = mSharedStorage.getPushRegistrationID();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected String doInBackground(String... params) {

        Common mCommon = new Common();

        JSONObject locationData = new JSONObject();
        try {
            locationData.put("action", "login");
            locationData.put("mobile", params[0]);
            locationData.put("gsm_registration_id", refreshedToken);//deviceRegistrationId
            locationData.put("device_id", mCommon.getDeviceId(mActivity));
            locationData.put("device_type", Common.DEVICE_TYPE);

        } catch (JSONException e) {
        }

        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mServiceResponce.requestResponce(result);
    }
}

