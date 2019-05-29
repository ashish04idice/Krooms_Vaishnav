package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/5/2016.
 */
public class LocalDataInsertUpdateAsyntask extends AsyncTask<String, Void, String> {

    private Service mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;

    public LocalDataInsertUpdateAsyntask(Service activity) {
        this.mActivity = activity;
        this.mServiceHandler = new ServiceHandler();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();//
    }

    @Override
    protected String doInBackground(String... params) {

        JSONObject locationData = new JSONObject();
        try {

            locationData.put("action", "syncMobileData");
            locationData.put("last_modified_timestamp", params[0]); // last modify date

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);

      //
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
       // pDialog.dismiss();
        mServiceResponce.requestResponce(result);
    }
}
