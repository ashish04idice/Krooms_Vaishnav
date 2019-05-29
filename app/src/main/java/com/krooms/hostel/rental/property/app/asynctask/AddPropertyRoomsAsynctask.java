package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.RoomServiceResponce;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/20/2016.
 */
public class AddPropertyRoomsAsynctask extends AsyncTask<String, Integer, String> {


    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private RoomServiceResponce mRoomServiceResponce = null;
    private ProgressDialog pDialog = null;

    public AddPropertyRoomsAsynctask(Activity activity) {
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

    public void setCallBack(RoomServiceResponce roomServiceResponce) {
        this.mRoomServiceResponce = roomServiceResponce;
    }

    @Override
    protected String doInBackground(String... params) {

        Common mCommon = new Common();

        JSONObject locationData = new JSONObject();
        try {
            locationData.put("action", "addPropertyRoomsWithoutBasicInfo");
            locationData.put("propertyId", params[0]);
            locationData.put("room_no", params[1]);
            locationData.put("room_type", params[2]);
            locationData.put("vacant", params[3]);
            locationData.put("amount", params[4]);
            locationData.put("lat_bath", params[5]);
            locationData.put("no_of_room", params[6]);
            locationData.put("room_id", params[7]);
        } catch (JSONException e) {
        }

        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pDialog.dismiss();
        mRoomServiceResponce.requestRoomServiceResponce(result);
    }
}
