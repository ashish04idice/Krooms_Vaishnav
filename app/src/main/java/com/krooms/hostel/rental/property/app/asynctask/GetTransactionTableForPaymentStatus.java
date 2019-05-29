package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 4/11/2016.
 */
public class GetTransactionTableForPaymentStatus extends AsyncTask<String, Void, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;
    private String deviceRegistrationId = null;

    public GetTransactionTableForPaymentStatus(Activity activity) {
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

            locationData.put("action","transactionResponseInfo");
            locationData.put("user_id", params[0]);
            locationData.put("property_id",params[1]);
            locationData.put("room_id",params[2]);

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
