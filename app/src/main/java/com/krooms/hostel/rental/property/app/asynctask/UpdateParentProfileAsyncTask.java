package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.MultipartEntity;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by user on 2/24/2016.
 */
public class UpdateParentProfileAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;

    public UpdateParentProfileAsyncTask(Activity activity, String str) {
        this.mActivity = activity;
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setTitle("Profile");
        pDialog.setMessage("Uploading file...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String responseString = null;

        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(WebUrls.MAIN_URL);

        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("action", "parentDetails");
            entity.addPart("user_id",params[0]);
            entity.addPart("fname",params[1]);
            entity.addPart("lname", params[2]);
            entity.addPart("husband_wife_name",params[3]);
            entity.addPart("husband_wife_mobileno",params[4]);
            entity.addPart("address", params[5]);
            entity.addPart("landmark", params[6]);
            entity.addPart("state", params[7]);
            entity.addPart("city", params[8]);
            entity.addPart("area", params[9]);
            entity.addPart("pincode", params[10]);
            entity.addPart("profession",params[11]);
            entity.addPart("organization",params[12]);
            entity.addPart("email_id", params[13]);
            entity.addPart("landline",params[14]);
            entity.addPart("mobile",params[15]);
            Common.Config(" area "+params[9]);
            File sourceFile = new File(params[16]);
            if(sourceFile.exists()) {
                entity.addPart("owner_pic", sourceFile);
            } else {
                entity.addPart("owner_pic",params[16]);
            }

            httppost.setEntity((HttpEntity) entity);
            // httpPost.get
            LogConfig.logd("ServiceHandler Post Url =", "" + params[16]);

            // Making server call
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "+ statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = "ConnectTimeoutException";
        } catch (ConnectTimeoutException e) {
            //Here Connection TimeOut exception
            responseString = "ConnectTimeoutException";
        } catch (SocketTimeoutException e) {
            //Here Connection TimeOut exception
            responseString = "ConnectTimeoutException";
        } catch (IOException e) {
            responseString = "ConnectTimeoutException";
        }
        return responseString;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pDialog.dismiss();
        mServiceResponce.requestResponce(result);
    }
}
