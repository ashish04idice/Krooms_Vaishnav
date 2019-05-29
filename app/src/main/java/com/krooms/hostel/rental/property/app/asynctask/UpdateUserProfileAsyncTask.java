package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.MultipartEntity;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;

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
 * Created by user on 2/20/2016.
 */
public class UpdateUserProfileAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;
    private String mStr = "";

    public UpdateUserProfileAsyncTask(Activity activity, String str) {
        this.mActivity = activity;
        this.mStr = str;
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setTitle(mStr);
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
            // Path of the file to be uploaded

            // Extra parameters if you want to pass to server
            entity.addPart("action", "addUserDetails");
            entity.addPart("user_id", params[0]);
            entity.addPart("fname", params[1]);
            entity.addPart("gender", params[2]);
            entity.addPart("email", params[3]);
            entity.addPart("contact", params[4]);
            entity.addPart("address", params[5]);
            entity.addPart("identity", params[6]);
            entity.addPart("identity_number", params[7]);
            File sourceFile1 = new File(params[8]);
            if (sourceFile1.exists()) {
                // Add the data to the multi_part entity
                entity.addPart("identity_pic", sourceFile1);
            } else {
                entity.addPart("identity_pic", params[8]);
            }
            entity.addPart("father_name", params[9]);
            entity.addPart("mother_name", params[10]);
            entity.addPart("parent_email", params[11]);
            entity.addPart("parent_contact", params[12]);
            entity.addPart("parent_address", params[13]);
            entity.addPart("guardian_name", params[14]);
            entity.addPart("relation", params[15]);
            entity.addPart("guardian_email", params[16]);
            entity.addPart("guardian_contact", params[17]);
            entity.addPart("guardian_address", params[18]);
            File sourceFile = new File(params[19]);
            if (sourceFile.exists()) {
                // Add the data to the multi_part entity
                entity.addPart("photo", sourceFile);
            } else {
                entity.addPart("photo", params[19]);
            }

            httppost.setEntity((HttpEntity) entity);
            // httpPost.get


            // Making server call
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: " + statusCode;
            }

            LogConfig.logd("ServiceHandler Post Url =", "" + WebUrls.MAIN_URL + entity.getContent().toString());

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (ConnectTimeoutException e) {
            //Here Connection TimeOut exception
            responseString = "ConnectTimeoutException";
        } catch (SocketTimeoutException e) {
            //Here Connection TimeOut exception
            responseString = "ConnectTimeoutException";
        } catch (IOException e) {
            responseString = e.toString();
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