package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/16/2016.
 */
public class RCUInformationAsynctask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;

    public RCUInformationAsynctask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }


    @Override
    protected String doInBackground(String... params) {

        JSONObject locationData = new JSONObject();
        try {

            locationData.put("action", "rcu_booking_phase1");
            locationData.put("user_id", params[0]);
            locationData.put("owner_id", params[1]);
            locationData.put("property_id", params[2]);
            locationData.put("room_id", params[3]);
            locationData.put("transaction_id", params[4]);
            locationData.put("tenant_form_no", params[5]);
            locationData.put("property_owner_fname", params[6]);
            locationData.put("property_owner_lname", params[7]);
            locationData.put("property_owner_father_name", params[8]);
            locationData.put("property_owner_address", params[9]);
            locationData.put("property_owner_contact_no", params[10]);
            locationData.put("property_owner_email", params[11]);
            locationData.put("usertype", params[12]);//send parent id static 0 incase parent not book any bed
        } catch (JSONException e) {
        }

        LogConfig.logd("Request time", "RRRRRRRRRRRRRRR ");

        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //pDialog.dismiss();
        LogConfig.logd("Responce time","RRRRRRRRRRRRRR ");
        mServiceResponce.requestResponce(result);
    }
}