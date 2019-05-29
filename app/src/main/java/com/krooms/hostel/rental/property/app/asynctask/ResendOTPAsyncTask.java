package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class ResendOTPAsyncTask extends AsyncTask<String, Void, String> {

	private Activity mActivity = null;
	private ServiceHandler mServiceHandler = null;
	private ServiceResponce mServiceResponce = null;

	public ResendOTPAsyncTask(Activity activity) {
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
			locationData.put("action", "resendOtp");
			locationData.put("mobile", params[0]);
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