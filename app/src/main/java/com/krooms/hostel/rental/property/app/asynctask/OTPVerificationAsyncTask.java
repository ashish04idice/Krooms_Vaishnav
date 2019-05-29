package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import com.krooms.hostel.rental.property.app.callback.OTPServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.helper.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class OTPVerificationAsyncTask extends AsyncTask<String, Void, String> {

	private Activity mActivity = null;
	private ServiceHandler mServiceHandler = null;
	private OTPServiceResponce mOTPServiceResponce = null;

	public OTPVerificationAsyncTask(Activity activity) {
		this.mActivity = activity;
		this.mServiceHandler = new ServiceHandler();
	}

	public void setCallBack(OTPServiceResponce oTPServiceResponce) {
		this.mOTPServiceResponce = oTPServiceResponce;
	}

	@Override
	protected String doInBackground(String... params) {

		JSONObject locationData = new JSONObject();
		try {

			locationData.put("action", "otpVarification");
			locationData.put("user_id", params[0]);
			locationData.put("otp", params[1]);
		} catch (JSONException e) {
		}
		return mServiceHandler.makeServiceJsonCall(WebUrls.MAIN_URL, Common.POST, locationData);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mOTPServiceResponce.requestOTPServiceResponce(result);
	}
}