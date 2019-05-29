package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/20/2016.
 */
public class AddPropertyOwnerDetailAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;

    public AddPropertyOwnerDetailAsyncTask(Activity activity) {
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
            locationData.put("action", "addPropertyDistance");
            locationData.put("propertyId", params[0]);
            locationData.put("railway_station", params[1]);
            locationData.put("bus_stand", params[2]);
            locationData.put("airport", params[3]);
            locationData.put("nearest", params[4]);
            locationData.put("square", params[5]);
            locationData.put("tenant_type", params[6]);
            locationData.put("curfew_time", params[7]);
            locationData.put("smoking", params[8]);
            locationData.put("drinking", params[9]);
            locationData.put("no_of_people", params[10]);
            locationData.put("rent_amount", params[11]);
            locationData.put("advance", params[12]);
            locationData.put("maintenance", params[13]);
            locationData.put("water_bill", params[14]);
            locationData.put("other_expenses", params[15]);
            locationData.put("electricity", params[16]);
            locationData.put("policy_fix_sqr", params[17]);
            locationData.put("vehicle", params[18]);
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
