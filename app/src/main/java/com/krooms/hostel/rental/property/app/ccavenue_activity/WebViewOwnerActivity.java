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
import com.krooms.hostel.rental.property.app.activity.OwnerFromActivity;
import com.krooms.hostel.rental.property.app.activity.PaymentActivityOwnerListview;
import com.krooms.hostel.rental.property.app.adapter.Tenant_Show_FeedbackList_Adapter;
import com.krooms.hostel.rental.property.app.modal.Feedbackdetailtenant;
import com.krooms.hostel.rental.property.app.modal.Feedbackdetailtenantnew;
import com.krooms.hostel.rental.property.app.modal.Tenant_Feedbackcountmodel;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.activity.PDFFileNewPage_owner;
import com.krooms.hostel.rental.property.app.activity.PaymentActivityOwner;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.GetTransactionTable;
import com.krooms.hostel.rental.property.app.ccavenue_utility.Constants;
import com.krooms.hostel.rental.property.app.ccavenue_utility.RSAUtility;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.Common;

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


public class WebViewOwnerActivity extends Activity {
    private ProgressDialog dialog;
    Intent mainIntent;
    String html, encVal;
    Common mCommon;
    private SharedStorage mSharedStorage;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mSharedStorage = SharedStorage.getInstance(this);
        mainIntent = getIntent();
        mCommon = new Common();
        System.out.print("monthid" + PaymentActivityOwner.amountroom + "currentdate" +
                PaymentActivityOwner.currentdate_value + "owner id" + mSharedStorage.getUserId()
                + "user id" + PaymentActivityOwner.tanentusrid_value + "month" + PaymentActivityOwner.month +
                "amount" + PaymentActivityOwner.payment + "tenantid" + PaymentActivityOwner.tanentidValue +
                "roomid" + PaymentActivityOwner.tanentroomid_value +
                "property id" + mSharedStorage.getBookedPropertyId() + "year" + PaymentActivityOwner.yearvaluenew);

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
            dialog = new ProgressDialog(WebViewOwnerActivity.this);
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
                    //GetTransactionDetail();

				/*	String userid=mainIntent.getStringExtra(AvenuesParams.USER_ID);
                    String propertyid=mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID);
					String roomid=mainIntent.getStringExtra(AvenuesParams.ROOM_ID);
					String tenantid=PaymentActivityOwner.tanentidValue;
*/
                    new GetTransactionTablePaymentStatus().execute();
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
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
                    + "," + mainIntent.getStringExtra(AvenuesParams.TRANSACTION_FOR)));

            Log.d("Tenant id", "" + AvenuesParams.MERCHANT_PARAM6 + "   " + PaymentActivityOwner.tanentidValue);
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, mainIntent.getStringExtra(AvenuesParams.ROOM_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, mainIntent.getStringExtra(AvenuesParams.BOOKED_BED)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM5, mainIntent.getStringExtra(AvenuesParams.OWNER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM6, PaymentActivityOwner.tanentidValue));
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

        // for me
        public class GetTransactionTablePaymentStatus extends AsyncTask<String, String, String> {

            private ProgressDialog dialog = new ProgressDialog(WebViewOwnerActivity.this);

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
                listNameValuePairs.add(new BasicNameValuePair("user_id", PaymentActivityOwner.tanentusrid_value));
                listNameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwner.tanentpropertyid_value));
                listNameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwner.tanentroomid_value));
                listNameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwner.tanentidValue));
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
                                    new PaymentActivityOwnerJSONParse().execute();
                                } else {
                                    Toast.makeText(WebViewOwnerActivity.this, "Your transaction is failed! please try again", Toast.LENGTH_LONG).show();
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
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                new PaymentActivityOwnerJSONParse().execute();
                    /*Intent mIntent = new Intent(this, HostelListActivity.class);
                      mIntent.putExtra("property_id", mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
					  mIntent.putExtra("transaction_id", data.getJSONObject(0).getString("transaction_id"));
					  mIntent.putExtra("room_id", mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
					  startActivity(mIntent);*/
//					finish();
                if (BillingShippingOwnerActivity.mActivity != null) {
                    BillingShippingOwnerActivity.mActivity.finish();
                }
                if (HostelDetailActivity.mActivity != null) {
                    HostelDetailActivity.mActivity.finish();
                }

                callSlider();
            } else {
                finish();
                if (BillingShippingOwnerActivity.mActivity != null) {
                    BillingShippingOwnerActivity.mActivity.finish();
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
        if (BillingShippingOwnerActivity.mActivity != null) {
            BillingShippingOwnerActivity.mActivity.finish();
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
                //ownerstudentarraylist=new ArrayList<OwnerStudentNameModel>();
                // Add your data
                System.out.print("monthid" + PaymentActivityOwner.amountroom + "currentdate" +
                        PaymentActivityOwner.currentdate_value + "owner id" + mSharedStorage.getUserId()
                        + "user id" + PaymentActivityOwner.tanentusrid_value + "month" + PaymentActivityOwner.month +
                        "amount" + PaymentActivityOwner.payment + "tenantid" + PaymentActivityOwner.tanentidValue +
                        "roomid" + PaymentActivityOwner.tanentroomid_value +
                        "property id" + mSharedStorage.getBookedPropertyId() + "year" + PaymentActivityOwner.yearvaluenew);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "ownerpaidamount"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", PaymentActivityOwner.tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("user_id", PaymentActivityOwner.tanentusrid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwner.tanentidValue));
                nameValuePairs.add(new BasicNameValuePair("month_id", PaymentActivityOwner.monthid_value));
                nameValuePairs.add(new BasicNameValuePair("month_name", PaymentActivityOwner.month));
                nameValuePairs.add(new BasicNameValuePair("amount", PaymentActivityOwner.payment));
                nameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwner.tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwner.yearvaluenew));
                nameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwner.tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", PaymentActivityOwner.advanceamount));
                nameValuePairs.add(new BasicNameValuePair("room_amount", PaymentActivityOwner.amountroom));
                nameValuePairs.add(new BasicNameValuePair("current_date", PaymentActivityOwner.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", PaymentActivityOwner.paymentcomment));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "O"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

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
            PaymentActivityOwner.bookProperty.setClickable(true);
            if (result.equalsIgnoreCase("Y")) {
                Successdialog();
                Toast.makeText(WebViewOwnerActivity.this, "Payment is successfull", Toast.LENGTH_LONG).show();

            } else if (result.equals("N")) {
                Toast.makeText(WebViewOwnerActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void Successdialog() {
        Dialog successdialog = new Dialog(WebViewOwnerActivity.this);
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
        TextView propertyname = (TextView) successdialog.findViewById(R.id.propertyname);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WebViewOwnerActivity.this, PDFFileNewPage_owner.class);
                i.putExtra("name", PaymentActivityOwner.name);
                i.putExtra("date", PaymentActivityOwner.currentdate_value1);
                i.putExtra("month", PaymentActivityOwner.month);
                i.putExtra("uid", PaymentActivityOwner.tanentusrid_value);
                i.putExtra("amount", PaymentActivityOwner.payment);
                i.putExtra("mode", "Online");
                i.putExtra("propertyid", PaymentActivityOwner.keypropertyid);
                i.putExtra("roomno", PaymentActivityOwner.roomno);
                i.putExtra("keyid", PaymentActivityOwner.keyid);
                i.putExtra("Comment", PaymentActivityOwner.paymentcomment);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                startActivity(i);
            }
        });
        username.setText(PaymentActivityOwner.name);
        userdate.setText(PaymentActivityOwner.currentdate_value1);
        paymentmonth.setText(PaymentActivityOwner.month);
        userpaid.setText(PaymentActivityOwner.payment);
        userpid.setText(PaymentActivityOwner.keypropertyid);
        usertid.setText(PaymentActivityOwner.keyid);
        userroomno.setText(PaymentActivityOwner.roomno);
        usermode.setText("Online");
        chequeno.setText(PaymentActivityOwner.paymentcomment);
        propertyname.setText(mSharedStorage.getPropertyname());
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WebViewOwnerActivity.this, OwnerFromActivity.class);
                startActivity(i);
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

            File file = new File(dir, PaymentActivityOwner.name + " " + "PaySlip" + i1 + ".pdf");
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
            table1.addCell(createValueCell(PaymentActivityOwner.currentdate_value));

            table1.addCell(createValueCell("Name"));
            table1.addCell(createValueCell(PaymentActivityOwner.name));

            table1.addCell(createValueCell("Pay For"));
            table1.addCell(createValueCell(PaymentActivityOwner.month));

            table1.addCell(createValueCell("Paid"));
            table1.addCell(createValueCell(PaymentActivityOwner.payment));

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

    //pdf method end

}