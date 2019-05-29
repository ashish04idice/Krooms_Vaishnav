package com.krooms.hostel.rental.property.app.ccavenue_activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.activity.PaymentActivityOwner;
import com.krooms.hostel.rental.property.app.activity.PaymentActivityOwnerListview;
import com.krooms.hostel.rental.property.app.activity.TenantDetailActivity;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.ListViewActivity;
import com.krooms.hostel.rental.property.app.activity.PDFFileNewPage_fromlistpayment;
import com.krooms.hostel.rental.property.app.asynctask.GetTransactionTable;
import com.krooms.hostel.rental.property.app.ccavenue_utility.Constants;
import com.krooms.hostel.rental.property.app.ccavenue_utility.RSAUtility;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WebViewOwnerActivity_FromListView extends Activity {
    private ProgressDialog dialog;
    Intent mainIntent;
    String monthidvalue;
    String html, encVal;
    Common mCommon;
    private SharedStorage mSharedStorage;
    Dialog successdialog;
    int monthidvaluemain;
    public static String transaction_for = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);

        mSharedStorage = SharedStorage.getInstance(this);
        mainIntent = getIntent();
        mCommon = new Common();
        monthidvaluemain = Integer.parseInt(PaymentActivityOwnerListview.monthid_value);
        new RenderView().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewOwnerActivity_FromListView.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                /*vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CVV, mainIntent.getStringExtra(AvenuesParams.CVV)));*/
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                /*vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NUMBER, mainIntent.getStringExtra(AvenuesParams.CARD_NUMBER)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CUSTOMER_IDENTIFIER, mainIntent.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_YEAR, mainIntent.getStringExtra(AvenuesParams.EXPIRY_YEAR)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_MONTH, mainIntent.getStringExtra(AvenuesParams.EXPIRY_MONTH)));*/

                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (dialog.isShowing())
                dialog.dismiss();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html as needed by the app

                    Log.d(" web view 1", " in responce...");
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                        Log.d(" web view 2", " in responce... failure");
                    } else if (html.indexOf("Success") != -1) {

                        Log.d(" web view 3", " in responce... success");
                        status = "Transaction Successful!";

                    } else if (html.indexOf("Aborted") != -1) {
                        Log.d(" web view 4", " in responce... aborted");
                        status = "Transaction Cancelled!";
                    } else {
                        Log.d(" web view 55", " in responce... not know..");
                        status = "Status Not Known!";
                    }

                    String tenantid = PaymentActivityOwnerListview.tanatidV;
                    String userid = PaymentActivityOwnerListview.tanentusrid_value;
                    String roomid = PaymentActivityOwnerListview.tanentroomid_value;
                    String propertyid = PaymentActivityOwnerListview.tanentpropertyid_value;


                    new GetTransactionTablePaymentStatus(userid, propertyid, roomid, tenantid).execute();

//					GetTransactionDetail();
                    //Toast.makeText(getApp
                    // licationContext(), status, Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
					intent.putExtra("transStatus", status);
					startActivity(intent);*/
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if (url.indexOf("/ccavResponseHandler.php") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            if (PaymentActivityOwnerListview.type.equalsIgnoreCase("1")) {

                transaction_for = "Electricitybill";

            } else {

                transaction_for = mainIntent.getStringExtra(AvenuesParams.TRANSACTION_FOR);
            }


			/*An instance of this class will be registered as a JavaScript interface*/
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.LANGUAGE, "EN"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, mainIntent.getStringExtra(AvenuesParams.BILLING_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.BILLING_CITY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.BILLING_STATE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, mainIntent.getStringExtra(AvenuesParams.BILLING_TEL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME, mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE, mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1, "additional Info."));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2, mainIntent.getStringExtra(AvenuesParams.USER_ID)
                    + "," + mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID)
                    + "," + mainIntent.getStringExtra(AvenuesParams.AMOUNT)
                    + "," + mainIntent.getStringExtra(AvenuesParams.HIRING_DATE)
                    + "," + mainIntent.getStringExtra(AvenuesParams.LEAVING_DATE)
                    + "," + mainIntent.getStringExtra(AvenuesParams.TENANT_ID)
                    + "," + mainIntent.getStringExtra(AvenuesParams.OWNER_ID)
                    + "," + mainIntent.getStringExtra(AvenuesParams.MONTH)
                    + "," + mainIntent.getStringExtra(AvenuesParams.TRANSACTION_DATE)
                    + "," + transaction_for));

            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, mainIntent.getStringExtra(AvenuesParams.ROOM_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, mainIntent.getStringExtra(AvenuesParams.BOOKED_BED)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM5, mainIntent.getStringExtra(AvenuesParams.OWNER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM6, PaymentActivityOwnerListview.tanatidV));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM7, ""));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.PAYMENT_OPTION, mainIntent.getStringExtra(AvenuesParams.PAYMENT_OPTION)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_TYPE, mainIntent.getStringExtra(AvenuesParams.CARD_TYPE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NAME, mainIntent.getStringExtra(AvenuesParams.CARD_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DATA_ACCEPTED_AT, mainIntent.getStringExtra(AvenuesParams.DATA_ACCEPTED_AT)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ISSUING_BANK, ""));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_PLAN_ID, mainIntent.getStringExtra(AvenuesParams.EMI_PLAN_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_TENURE_ID, mainIntent.getStringExtra(AvenuesParams.EMI_TENURE_ID)));
            if (mainIntent.getStringExtra(AvenuesParams.SAVE_CARD) != null)
                params.append(ServiceUtility.addToPostParams(AvenuesParams.SAVE_CARD, mainIntent.getStringExtra(AvenuesParams.SAVE_CARD)));

            String vPostParams = params.substring(0, params.length() - 1);
            try {
                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();

    }

    public void GetTransactionDetail() {
        GetTransactionTable service = new GetTransactionTable(this);
        service.setCallBack(new ServiceResponce() {
            @Override
            public void requestResponce(String result) {
                parseRespoce(result);
            }
        });
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mainIntent.getStringExtra(AvenuesParams.USER_ID)
                , mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID), mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
    }

    public void parseRespoce(String result) {

        try {
            Toast.makeText(WebViewOwnerActivity_FromListView.this, "r1" + result, Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);

					/*if(monthidvaluemain >12)
					{

						new FineOwnerpaymentJSONParse().execute();
					}else
					{
					//	new PaymentActivityOwnerJSONParse().execute();
					}*/

					/*if(PaymentActivityOwnerListview.month.equalsIgnoreCase("fees"))
					{
						monthidvalue="13";
						new FineOwnerpaymentJSONParse().execute();
					}else
					if(PaymentActivityOwnerListview.month.equalsIgnoreCase("electricity"))
					{
						monthidvalue="14";
						new FineOwnerpaymentJSONParse().execute();
					}else if(PaymentActivityOwnerListview.month.equalsIgnoreCase("other"))
					{
						monthidvalue="15";
						new FineOwnerpaymentJSONParse().execute();
					}else
					{
						new PaymentActivityOwnerJSONParse().execute();
					}*/

//					Intent mIntent = new Intent(this, HostelListActivity.class);
                //mIntent.putExtra("property_id", mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
                //mIntent.putExtra("transaction_id", data.getJSONObject(0).getString("transaction_id"));
                //mIntent.putExtra("room_id", mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
//					startActivity(mIntent);
//					finish();
                if (BillingShippingOwnerActivity_FromListView.mActivity != null) {
                    BillingShippingOwnerActivity_FromListView.mActivity.finish();
                }
                if (HostelDetailActivity.mActivity != null) {
                    HostelDetailActivity.mActivity.finish();
                }

                callSlider();
            } else {
                finish();
                if (BillingShippingOwnerActivity_FromListView.mActivity != null) {
                    BillingShippingOwnerActivity_FromListView.mActivity.finish();
                }
                // mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }

        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }

    }

    public void callSlider() {
        SharedStorage mSharedStorage = SharedStorage.getInstance(this);
        mSharedStorage.setAddCount("1");
        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (BillingShippingOwnerActivity_FromListView.mActivity != null) {
            BillingShippingOwnerActivity_FromListView.mActivity.finish();
        }
    }


    private class PaymentActivityOwnerJSONParse extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {

                System.out.print("monthid" + PaymentActivityOwnerListview.monthid_value + "currentdate" + PaymentActivityOwnerListview.currentdate_value + "owner id" + mSharedStorage.getUserId() + "user id" + PaymentActivityOwnerListview.tanentusrid_value + "month" + PaymentActivityOwnerListview.month + "amount" + PaymentActivityOwnerListview.totalAmount_value + "roomid" + PaymentActivityOwnerListview.tanentroomid_value + "property id" + mSharedStorage.getBookedPropertyId());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "updatepaidamount"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", PaymentActivityOwnerListview.tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwnerListview.tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", PaymentActivityOwnerListview.tanentusrid_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", PaymentActivityOwnerListview.monthid_value));
                //nameValuePairs.add(new BasicNameValuePair("year",PaymentActivityOwnerListview.month_date));
                nameValuePairs.add(new BasicNameValuePair("month_name", PaymentActivityOwnerListview.month));
                nameValuePairs.add(new BasicNameValuePair("amount", PaymentActivityOwnerListview.totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwnerListview.tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwnerListview.tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", PaymentActivityOwnerListview.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwnerListview.yearvalue));
                nameValuePairs.add(new BasicNameValuePair("room_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", PaymentActivityOwnerListview.paymentcomment));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "O"));


                Log.d("uu 2", "" + nameValuePairs);
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {


                }


            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("Y")) {
                Successdialog();
                Toast.makeText(WebViewOwnerActivity_FromListView.this, "Payment is successfull", Toast.LENGTH_LONG).show();

            } else if (result.equals("N")) {
                Toast.makeText(WebViewOwnerActivity_FromListView.this, message, Toast.LENGTH_LONG).show();
            }


        }
    }


    //for advance payment update chnages ashish 25-09-2018

    private class AdvanceAmountUpdatePayment extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {


                System.out.print("monthid" + PaymentActivityOwnerListview.monthid_value + "currentdate" + PaymentActivityOwnerListview.currentdate_value + "owner id" + mSharedStorage.getUserId() + "user id" + PaymentActivityOwnerListview.tanentusrid_value + "month" + PaymentActivityOwnerListview.month + "amount" + PaymentActivityOwnerListview.totalAmount_value + "roomid" + PaymentActivityOwnerListview.tanentroomid_value + "property id" + mSharedStorage.getBookedPropertyId());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "advanceamountupdateroomchange"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", PaymentActivityOwnerListview.tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwnerListview.tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", PaymentActivityOwnerListview.tanentusrid_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", PaymentActivityOwnerListview.monthid_value));
                //nameValuePairs.add(new BasicNameValuePair("year",PaymentActivityOwnerListview.month_date));
                nameValuePairs.add(new BasicNameValuePair("month_name", PaymentActivityOwnerListview.month));
                nameValuePairs.add(new BasicNameValuePair("amount", PaymentActivityOwnerListview.totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwnerListview.tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwnerListview.tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", PaymentActivityOwnerListview.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwnerListview.yearvalue));
                nameValuePairs.add(new BasicNameValuePair("room_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", PaymentActivityOwnerListview.paymentcomment));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "O"));


                Log.d("uu 2", "" + nameValuePairs);
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {


                }


            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("Y")) {
                Successdialog();
                Toast.makeText(WebViewOwnerActivity_FromListView.this, "Payment is successfull", Toast.LENGTH_LONG).show();

            } else if (result.equals("N")) {
                Toast.makeText(WebViewOwnerActivity_FromListView.this, message, Toast.LENGTH_LONG).show();
            }


        }
    }


    //


    private class FineOwnerpaymentJSONParse extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {

                System.out.print("monthid" + PaymentActivityOwnerListview.monthid_value + "currentdate" + PaymentActivityOwnerListview.currentdate_value + "owner id" + mSharedStorage.getUserId() + "user id" + PaymentActivityOwnerListview.tanentusrid_value + "month" + PaymentActivityOwnerListview.month + "amount" + PaymentActivityOwnerListview.totalAmount_value + "roomid" + PaymentActivityOwnerListview.tanentroomid_value + "property id" + mSharedStorage.getBookedPropertyId());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "otherfinepayment"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", PaymentActivityOwnerListview.tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwnerListview.tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", ListViewActivity.user_id_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", PaymentActivityOwnerListview.monthid_value));
                //nameValuePairs.add(new BasicNameValuePair("year",PaymentActivityOwnerListview.month_date));
                nameValuePairs.add(new BasicNameValuePair("month_name", PaymentActivityOwnerListview.month));
                nameValuePairs.add(new BasicNameValuePair("amount", PaymentActivityOwnerListview.totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwnerListview.tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwnerListview.tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", PaymentActivityOwnerListview.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwnerListview.yearvalue));
                nameValuePairs.add(new BasicNameValuePair("room_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", PaymentActivityOwnerListview.paymentcomment));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "O"));

                Log.d("uu 2", "" + nameValuePairs);
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                }


            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("Y")) {
                Successdialog();
                Toast.makeText(WebViewOwnerActivity_FromListView.this, "Payment is successfull", Toast.LENGTH_LONG).show();

            } else if (result.equals("N")) {
                Toast.makeText(WebViewOwnerActivity_FromListView.this, message, Toast.LENGTH_LONG).show();
            }


        }
    }


    public void Successdialog() {
        successdialog = new Dialog(WebViewOwnerActivity_FromListView.this);
        successdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successdialog.setContentView(R.layout.success_dialogpayment_owner);
        successdialog.setCanceledOnTouchOutside(false);
        final android.support.v7.widget.CardView card_view = (CardView) successdialog.findViewById(R.id.card_view);
        TextView username = (TextView) successdialog.findViewById(R.id.username);
        TextView userdate = (TextView) successdialog.findViewById(R.id.userdate);
        TextView paymentmonth = (TextView) successdialog.findViewById(R.id.paymentmonth);
        TextView userpaid = (TextView) successdialog.findViewById(R.id.userpaid);
        TextView usermode = (TextView) successdialog.findViewById(R.id.usermode);
        TextView usertid = (TextView) successdialog.findViewById(R.id.usertid);
        TextView userpid = (TextView) successdialog.findViewById(R.id.userpropertyid);
        TextView userroomno = (TextView) successdialog.findViewById(R.id.userroomno);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        TextView propertyname = (TextView) successdialog.findViewById(R.id.propertyname);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                successdialog.dismiss();
                Intent i = new Intent(WebViewOwnerActivity_FromListView.this, PDFFileNewPage_fromlistpayment.class);
                i.putExtra("name", TenantDetailActivity.tanentfinalname);
                i.putExtra("date", PaymentActivityOwnerListview.currentdate_value1);

                if (PaymentActivityOwnerListview.type.equalsIgnoreCase("1")) {
                    i.putExtra("month", "Elc-" + PaymentActivityOwnerListview.month);
                } else {
                    i.putExtra("month", PaymentActivityOwnerListview.month);
                }

                i.putExtra("p_name", ListViewActivity.property_name);
                i.putExtra("uid", PaymentActivityOwnerListview.tanentusrid_value);
                i.putExtra("amount", PaymentActivityOwnerListview.totalAmount_value);
                i.putExtra("roomno", PaymentActivityOwnerListview.roomnovalue);
                i.putExtra("keyid", PaymentActivityOwnerListview.keyid);
                i.putExtra("propertyid", PaymentActivityOwnerListview.Bookedpropertykeyid);
                i.putExtra("mode", "Online");
                i.putExtra("Comment", PaymentActivityOwnerListview.paymentcomment);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                startActivity(i);
            }
        });
        username.setText(TenantDetailActivity.tanentfinalname);
        userdate.setText(PaymentActivityOwnerListview.currentdate_value1);

        if (PaymentActivityOwnerListview.type.equalsIgnoreCase("1")) {

            paymentmonth.setText("Elc-" + PaymentActivityOwnerListview.month);

        } else {
            paymentmonth.setText(PaymentActivityOwnerListview.month);
        }
        userpaid.setText(PaymentActivityOwnerListview.totalAmount_value);
        userpid.setText(PaymentActivityOwnerListview.Bookedpropertykeyid);
        usertid.setText(PaymentActivityOwnerListview.keyid);
        userroomno.setText(PaymentActivityOwnerListview.roomnovalue);
        usermode.setText("Online");
        propertyname.setText(mSharedStorage.getPropertyname());
        chequeno.setText(PaymentActivityOwnerListview.paymentcomment);
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successdialog.dismiss();
                Intent i = new Intent(WebViewOwnerActivity_FromListView.this, ListViewActivity.class);
                i.putExtra("user_name", TenantDetailActivity.tanentfinalname);
                i.putExtra("user_id_data", PaymentActivityOwnerListview.tanentusrid_value);
                i.putExtra("tid", PaymentActivityOwnerListview.tanatidV);
                i.putExtra("property_id_data", PaymentActivityOwnerListview.tanentpropertyid_value);
                i.putExtra("property_name", "");
                startActivity(i);
                finish();
            }
        });

        successdialog.show();
    }

    //pdf method
    private void CreatePdf() {

        Document doc = new Document();

        try {

            // String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            String path = Environment.getExternalStorageDirectory() + "/Krooms";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir, TenantDetailActivity.tanentfinalname + " " + "PaySlip" + i1 + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            // PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            PdfWriter.getInstance(doc, fOut);


            //open the document
            doc.open();

            PdfPTable table1 = new PdfPTable(2);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.iconk1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setBackgroundColor(BaseColor.WHITE);


            table1.addCell(myImg);
            table1.addCell(createValueCell1("Pay Slip"));

            table1.addCell(createValueCell("Date"));
            table1.addCell(createValueCell(PaymentActivityOwnerListview.currentdate_value));

            table1.addCell(createValueCell("Name"));
            table1.addCell(TenantDetailActivity.tanentfinalname);

            table1.addCell(createValueCell("Pay For"));
            table1.addCell(createValueCell(PaymentActivityOwnerListview.month));

            table1.addCell(createValueCell("Paid"));
            table1.addCell(createValueCell(PaymentActivityOwnerListview.totalAmount_value));

            table1.addCell(createValueCell("Transaction Mode"));
            table1.addCell(createValueCell("Online"));
            doc.add(table1);


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }


    }

    private PdfPCell createValueCell(String s) {

        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.DARK_GRAY);
        // create cell
        PdfPCell cellname = new PdfPCell(new Phrase(s, font));
        cellname.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellname.setFixedHeight(50f);
        return cellname;
    }

    private PdfPCell createValueCell1(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 35, Font.BOLD, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        //cellname4.setFixedHeight(20f);
        //cellname4.setMinimumHeight(0);
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cellname4;
    }

    // for me
    public class GetTransactionTablePaymentStatus extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(WebViewOwnerActivity_FromListView.this);

        String userid, propertyid, roomid, tenantid;

        public GetTransactionTablePaymentStatus(String userid, String propertyid, String roomid, String tenantid) {

            this.userid = userid;
            this.propertyid = propertyid;
            this.roomid = roomid;
            this.tenantid = tenantid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getonlinetransactionmonthResponse"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", userid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
            listNameValuePairs.add(new BasicNameValuePair("room_id", roomid));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = obj.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String pstatus = object.getString("transaction_status");
                            if (pstatus.equalsIgnoreCase("success") || pstatus.equalsIgnoreCase("Successful") || pstatus.equalsIgnoreCase("successfully") || pstatus.equalsIgnoreCase("Panding")) {

                                //1 for electricity payment
                                if (PaymentActivityOwnerListview.type.equalsIgnoreCase("1")) {

                                    new ElectricityPaymentOwnerJSONParse().execute();
                                } else if (monthidvaluemain > 12) {
                                    new FineOwnerpaymentJSONParse().execute();
                                } else if (PaymentActivityOwnerListview.type.equals("Adv")) {
                                    //new PaymentActivityOwnerJSONParse().execute();
                                    new AdvanceAmountUpdatePayment().execute();
                                } else {
                                    new PaymentActivityOwnerJSONParse().execute();
                                }


                            } else {

                                Toast.makeText(WebViewOwnerActivity_FromListView.this, "Your transaction is failed! please try again", Toast.LENGTH_LONG).show();


                            }

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //

    //pdf method end


    //for electricity payment

    private class ElectricityPaymentOwnerJSONParse extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {

                System.out.print("monthid" + PaymentActivityOwnerListview.monthid_value + "currentdate" + PaymentActivityOwnerListview.currentdate_value + "owner id" + mSharedStorage.getUserId() + "user id" + PaymentActivityOwnerListview.tanentusrid_value + "month" + PaymentActivityOwnerListview.month + "amount" + PaymentActivityOwnerListview.totalAmount_value + "roomid" + PaymentActivityOwnerListview.tanentroomid_value + "property id" + mSharedStorage.getBookedPropertyId());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "electricitypaidamount"));
                //nameValuePairs.add(new BasicNameValuePair("owner_id", PaymentActivityOwnerListview.tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwnerListview.tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", PaymentActivityOwnerListview.tanentusrid_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", PaymentActivityOwnerListview.monthid_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwnerListview.tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("month_name", PaymentActivityOwnerListview.month));
                nameValuePairs.add(new BasicNameValuePair("amount", PaymentActivityOwnerListview.totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwnerListview.tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", PaymentActivityOwnerListview.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwnerListview.yearvalue));
                nameValuePairs.add(new BasicNameValuePair("comment", PaymentActivityOwnerListview.paymentcomment));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "O"));
                //


                Log.d("uu 2", "" + nameValuePairs);
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {


                }


            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("Y")) {
                Successdialog();
                Toast.makeText(WebViewOwnerActivity_FromListView.this, "Payment is successfull", Toast.LENGTH_LONG).show();

            } else if (result.equals("N")) {
                Toast.makeText(WebViewOwnerActivity_FromListView.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }


    //
}