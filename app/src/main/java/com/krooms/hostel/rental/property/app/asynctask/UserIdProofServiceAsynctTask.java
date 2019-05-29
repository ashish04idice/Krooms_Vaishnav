package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.util.AndroidMultiPartEntity;
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
 * Created by user on 3/31/2016.
 */
public class UserIdProofServiceAsynctTask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ServiceResponce mServiceResponce = null;
    private ProgressDialog pDialog = null;

    public UserIdProofServiceAsynctTask(Activity activity) {
        this.mActivity = activity;
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
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

            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {

                }
            });

            entity.addPart("action", "uploadIdentificationDoc");
            entity.addPart("rcu_id",params[1]);
            entity.addPart("id_proof_type", params[2]);
            File sourceFile = new File(params[3]);

            Common.Config("file path      "+params[1]+"  "+params[2]+"  "+params[3] );
            if(sourceFile.exists()) {
                // Add the data to the multi_part entity
                entity.addPart("file", sourceFile);
            } else {
                entity.addPart("file",params[3]);
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
                responseString = "Error occurred! Http Status Code: "+ statusCode;
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