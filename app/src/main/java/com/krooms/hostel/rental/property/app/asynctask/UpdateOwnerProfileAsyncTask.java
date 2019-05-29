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
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.CoreProtocolPNames;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by user on 2/24/2016.
 */
public class UpdateOwnerProfileAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ProgressDialog pDialog = null;
    private ServiceResponce mServiceResponce = null;

    public UpdateOwnerProfileAsyncTask(Activity activity, String str) {
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
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParameters = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
        httpParameters.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setTcpNoDelay(httpParameters, true);
        HttpPost httppost = new HttpPost(WebUrls.MAIN_URL);

        try {
            MultipartEntity entity = new MultipartEntity();
            // Path of the file to be uploaded
            entity.addPart("action", "ownerDetails");
            entity.addPart("user_id", params[0]);
            entity.addPart("fname", params[1]);
            entity.addPart("lname", params[2]);
            entity.addPart("address", params[3]);
            entity.addPart("landmark", params[4]);
            entity.addPart("state", params[5]);
            entity.addPart("city", params[6]);
            entity.addPart("area", params[7]);
            entity.addPart("pincode", params[8]);
            entity.addPart("profession", params[9]);
            entity.addPart("organization", params[10]);
            entity.addPart("email_id", params[11]);
            entity.addPart("landline", params[12]);
            entity.addPart("mobile", params[13]);
            entity.addPart("father_name", params[15]);
            entity.addPart("date", params[16]);
            Common.Config("area" + params[7]);
            File sourceFile = new File(params[14]);
            if (sourceFile.exists()) {
                entity.addPart("owner_pic", sourceFile);
            } else {
                entity.addPart("owner_pic", sourceFile);
            }

            httppost.setEntity((HttpEntity) entity);
            // httpPost.get
            LogConfig.logd("ServiceHandler Post Url =", "" + params[14]);

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

        } catch (ConnectTimeoutException e) {
            //Here Connection TimeOut excepion
            return "ConnectTimeoutException";
        } catch (SocketTimeoutException e) {
            //Here Connection TimeOut excepion
            return "ConnectTimeoutException";
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            return "ConnectTimeoutException";
        } catch (UnsupportedEncodingException e) {
            return "ConnectTimeoutException";
        } catch (ClientProtocolException e) {
            return "ConnectTimeoutException";
        } catch (IOException e) {
            return "ConnectTimeoutException";
        } catch (Exception e) {
            return "ConnectTimeoutException";
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
