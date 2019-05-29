package com.krooms.hostel.rental.property.app.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.MultipartEntity;
import com.krooms.hostel.rental.property.app.util.WebUrls;

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
import cz.msebera.android.httpclient.params.CoreProtocolPNames;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by user on 2/16/2016.
 */
public class TenantDetailUpdateAsynctask extends AsyncTask<String, Integer, String> {

    private Activity mActivity = null;
    private ServiceResponce mServiceResponce = null;
    public static String newroomid = "";
    public TenantDetailUpdateAsynctask(Activity activity) {
        this.mActivity = activity;
    }

    public void setCallBack(ServiceResponce serviceResponce) {
        this.mServiceResponce = serviceResponce;
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
        String action = "";
        try {

            MultipartEntity entity = new MultipartEntity();
            action = "rcu_booking_phase2";
            entity.addPart("action", "rcu_booking_UpdateDetails");
            entity.addPart("rcu_id", params[0]);
            entity.addPart("tenant_detail", params[1]);
            entity.addPart("tenant_fname", params[2]);
            entity.addPart("tenant_lname", params[3]);
            entity.addPart("tenant_father_name", params[4]);
            entity.addPart("tenant_father_contact_no", params[5]);
            entity.addPart("tenant_permanent_address", params[6]);
            entity.addPart("flat_number", params[7]);
            entity.addPart("landmark", params[8]);
            entity.addPart("location", params[9]);
            entity.addPart("state", params[10]);
            entity.addPart("city", params[11]);
            entity.addPart("pincode", params[12]);
            entity.addPart("contact_number", params[13]);
            entity.addPart("email_id", params[14]);
            entity.addPart("property_hire_date", params[15]);
            entity.addPart("property_leave_date", params[16]);
            entity.addPart("user_id", params[18]);
            entity.addPart("property_id", params[19]);
            entity.addPart("owner_id", params[20]);
            entity.addPart("room_id", params[21]);
            entity.addPart("parent_id", params[23]);
            newroomid = params[21];
            File sourceFile = new File(params[17]);
            if (sourceFile.exists()) {
                entity.addPart("tenant_photo", sourceFile);
            } else {
                entity.addPart("tenant_photo", params[17]);
            }

            httppost.setEntity((HttpEntity) entity);
            String data = "";
            for (String name : params) {
                data += name + ", ";
            }
            data += action;


            // Making server call
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "ConnectTimeoutException";
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
        mServiceResponce.requestResponce(result);
    }
}