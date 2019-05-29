package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.callback.ServiceResponseBed;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.MultipartEntity;

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
 * Created by user on 7/1/2016.
 */
public class UploadRoomImageServiceAsyncTask extends AsyncTask<String, Integer, String> {


    private Activity mActivity = null;
    private ProgressDialog pDialog = null;
    private ServiceResponseBed mServiceResponce = null;

    public UploadRoomImageServiceAsyncTask(Activity activity) {
        this.mActivity = activity;
    }

    public void setCallBackbed(ServiceResponseBed serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setTitle("Room Image");
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



            entity.addPart("action", "uploadRoomImage");
            entity.addPart("room_id",params[0]);
            entity.addPart("propertyId",params[1]);


            File sourceFile = new File(params[2]);
            if(sourceFile.exists()) {
                // Add the data to the multi_part entity
                entity.addPart("room_pic", sourceFile);
            } else {
                entity.addPart("room_pic",params[2]);
            }

            httppost.setEntity((HttpEntity) entity);
            // httpPost.get
            LogConfig.logd("ServiceHandler Post Url =", "" + params[2]);

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

//            LogConfig.logd("ServiceHandler Post Url =", "" + WebUrls.MAIN_URL + entity.getContent().toString());

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
        mServiceResponce.requestResponcebed(result);
    }

}
