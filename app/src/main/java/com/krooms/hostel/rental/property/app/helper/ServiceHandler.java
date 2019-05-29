package com.krooms.hostel.rental.property.app.helper;


import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.CoreProtocolPNames;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

@SuppressWarnings("deprecation")
public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        return null/*this.makeServiceCall(url, method, null)*/;
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                // httpPost.get
                if (params == null)
                    LogConfig.logd("ServiceHandler Post Url =", "" + url);
                else LogConfig.logd("ServiceHandler Post Url =", "" + url + params.toString());
                // httpPost.get
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                LogConfig.logd("ServiceHandler Get Url =", "" + url.toString());
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);


        } catch (UnsupportedEncodingException e) {
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        return response;

    }

    public String makeServiceJsonCall(String url, int method, JSONObject locationData) {
        try {
            // http client
            //DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            httpParameters.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

                if (locationData != null) {
                    StringEntity entity = new StringEntity(locationData.toString(), HTTP.UTF_8);
                    httpPost.setEntity(entity);
                }
                // httpPost.get
                if (locationData == null)
                    LogConfig.logd("ServiceHandler Post Url =", "" + url);
                else
                    LogConfig.logd("ServiceHandler Post Url =", "" + url + locationData.toString());
                // httpPost.get
                httpResponse = httpClient.execute(httpPost);

            }
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);


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
            //e.printStackTrace();
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
            return "ConnectTimeoutException";
        } catch (IOException e) {
            //e.printStackTrace();
            return "ConnectTimeoutException";
        } catch (Exception e) {
            //e.printStackTrace();
            return "ConnectTimeoutException";
        }

        return response;

    }

    public String SendHttpPost(String URL, JSONArray jsonarraySend) {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPostRequest = new HttpPost(URL);

            StringEntity se;
            se = new StringEntity(jsonarraySend.toString());

            // Set HTTP parameters
            httpPostRequest.setEntity(se);
            httpPostRequest.setHeader("Accept", "application/json");
            httpPostRequest.setHeader("Content-type", "application/json;charset=UTF-8");

            //long t = System.currentTimeMillis();
            HttpResponse httpResponse = (HttpResponse) httpclient
                    .execute(httpPostRequest);
            // Log.i(TAG, "HTTPResponse received in [" +
            // (System.currentTimeMillis()-t) + "ms]");

            // Get hold of the response entity (-> the data):
            HttpEntity entity = httpResponse.getEntity();
            response = EntityUtils.toString(entity);

            return response;
        } catch (Exception e) {
        }
        return null;
    }
}
