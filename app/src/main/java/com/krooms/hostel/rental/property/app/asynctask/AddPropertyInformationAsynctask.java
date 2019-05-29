package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/16/2016.
 */
public class AddPropertyInformationAsynctask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;

    public AddPropertyInformationAsynctask(Activity activity) {
        this.mActivity = activity;
        this.mServiceHandler =  new ServiceHandler();
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }
    @Override
    protected String doInBackground(String... params) {

        JSONObject locationData = new JSONObject();
        try {

            System.out.println("  lat       "+params[11]+"  long        "+params[12]);
            locationData.put("action", "addPropertyInformation");
            locationData.put("ownerId", params[0]);
            locationData.put("userId", params[1]);
            locationData.put("propertyType", params[2]);
            locationData.put("propertyName", params[3]);
            locationData.put("propertyNature", params[4]);
            locationData.put("ownerAddress", params[5]);
            locationData.put("landmark", params[6]);
            locationData.put("state", params[7]);
            locationData.put("city", params[8]);
            locationData.put("colony", params[9]);
            locationData.put("pincode", params[10]);
            locationData.put("lat", params[11]);
            locationData.put("long", params[12]);
            locationData.put("totalArea", params[13]);
            locationData.put("superBuiltUpArea", params[14]);
            locationData.put("propertyFace", params[15]);
            locationData.put("propertyFeature", params[16]);
            locationData.put("propertyList", params[17]);
            locationData.put("waterSupply", params[18]);
            locationData.put("powerBackup", params[19]);
            locationData.put("otherAminities",params[20]);
            locationData.put("furnish_type",params[21]);
            locationData.put("propertyId",params[22]);
            locationData.put("kitchen",params[23]);
            locationData.put("kitchen_type",params[24]);
            locationData.put("description",params[25]);
            locationData.put("video_url",params[26]);
            Common.Config("propertyFeature    "+params[16]);

        } catch (JSONException e) {
        }
        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //pDialog.dismiss();
        mServiceResponce.requestResponce(result);
    }
}