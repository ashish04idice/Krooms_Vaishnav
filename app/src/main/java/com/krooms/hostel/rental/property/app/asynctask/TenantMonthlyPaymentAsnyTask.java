package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 11/14/2016.
 */
public class TenantMonthlyPaymentAsnyTask extends AsyncTask<String, Void, String> {
    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;
    private Common mCommon = null;
    private SharedStorage mSharedStorage = null;
    public TenantMonthlyPaymentAsnyTask(Activity activity) {
        this.mActivity = activity;
        this.mCommon = new Common();
        this.mSharedStorage = SharedStorage.getInstance(mActivity);
        this.mServiceHandler = new ServiceHandler();
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

        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        Long transNum = ServiceUtility.numbGen();

        JSONObject locationData = new JSONObject();
        try {

            locationData.put("action","bookRoomOnCashNew");
            locationData.put("user_id", params[0]);
            locationData.put("property_id", params[1]);
            locationData.put("room_id", params[2]);
            locationData.put("amount", params[3]);
            locationData.put("hiring_date", params[4]);
            locationData.put("leaving_date", params[5]);
            locationData.put("booked_bed", params[6]);
            locationData.put("order_id", randomNum.toString());
            locationData.put("transition_number", transNum.toString());

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