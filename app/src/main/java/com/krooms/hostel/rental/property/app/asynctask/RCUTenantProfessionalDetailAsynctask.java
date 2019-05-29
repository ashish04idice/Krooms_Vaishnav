package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2/16/2016.
 */
public class RCUTenantProfessionalDetailAsynctask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ServiceHandler mServiceHandler = null;
    private ServiceResponce mServiceResponce = null;

    public RCUTenantProfessionalDetailAsynctask(Activity activity) {
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
            locationData.put("action","rcu_booking_phase3");
            locationData.put("rcu_id", params[0]);
            locationData.put("tenant_office_institute", params[1]);
            locationData.put("tenant_contact_number", params[2]);
            locationData.put("tenant_work_detail", params[3]);
            locationData.put("guarantor_name", params[4]);
            locationData.put("guarantor_address", params[5]);
            locationData.put("guarantor_contact_number", params[6]);
            locationData.put("vehicle_registration_no", params[7]);
            locationData.put("voter_id_card_no", params[8]);
            locationData.put("driving_license_no", params[9]);
            locationData.put("telephone_number", params[10]);
            locationData.put("mobile_number", params[11]);
            locationData.put("aadhar_card_no", params[12]);
            locationData.put("arm_licence_no", params[13]);
            locationData.put("driving_license_issue_office", params[14]);
            locationData.put("arm_name_issue_office", params[15]);
            locationData.put("passport_no", params[16]);
            locationData.put("rashan_card_no", params[17]);
            locationData.put("other_identity_card", params[18]);
            locationData.put("other_verification", params[19]);

        } catch (JSONException e) {
        }
        Common.Config(" url  data   " + locationData);

        return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mServiceResponce.requestResponce(result);
    }
}