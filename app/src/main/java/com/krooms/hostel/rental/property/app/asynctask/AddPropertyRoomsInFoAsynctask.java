package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
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
public class AddPropertyRoomsInFoAsynctask extends AsyncTask<String, Integer, String> {


    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;
    private ProgressDialog pDialog = null;


    public AddPropertyRoomsInFoAsynctask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected String doInBackground(String... params) {

        Common mCommon = new Common();

        JSONObject locationData = new JSONObject();
        try {

            locationData.put("action", "addPropertyRooms");
            locationData.put("propertyId", params[0]);
            locationData.put("noOfRoom", params[1]);
            locationData.put("forWhom", params[2]);
            locationData.put("room_info", params[3]);


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