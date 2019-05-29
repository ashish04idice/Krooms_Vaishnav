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
 * Created by user on 2/16/2016.
 */
public class CompleteUserProfileServiceAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;
    String mStr = "";

    public CompleteUserProfileServiceAsyncTask(Activity activity, String str) {
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
            entity.addPart("action", "addProperty");
            entity.addPart("name", params[0]);
            entity.addPart("address", params[1]);
            entity.addPart("area", params[2]);
            entity.addPart("city", params[3]);
            entity.addPart("state", Common.DEVICE_TYPE);
            entity.addPart("capacity", params[4]);
            entity.addPart("no_of_room", params[5]);
            entity.addPart("phone", params[6]);
            entity.addPart("owner_id", params[7]);
            entity.addPart("property_type_id", params[8]);
            entity.addPart("user_id", params[9]);
            entity.addPart("gender", params[10]);
            entity.addPart("featured_services", params[12]);
            entity.addPart("payment_term_option", params[12]);
            entity.addPart("video_link", params[12]);
            entity.addPart("availability", params[12]);
            entity.addPart("weightage", params[12]);
            File sourceFile = new File(params[11]);


            if (sourceFile.exists()) {

                // Add the data to the multi_part entity
                entity.addPart("product_logo", sourceFile);
            } else {
                entity.addPart("product_logo", params[11]);
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
