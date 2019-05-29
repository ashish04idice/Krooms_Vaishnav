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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.activity.MyPropertyUsersListActivity;
import com.krooms.hostel.rental.property.app.activity.PaymentActivityOwner;
import com.krooms.hostel.rental.property.app.activity.UserOwner_RemoveActivity;
import com.krooms.hostel.rental.property.app.activity.UserRemoveActivity;
import com.krooms.hostel.rental.property.app.asynctask.GetTransactionTable;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.ccavenue_utility.Constants;
import com.krooms.hostel.rental.property.app.ccavenue_utility.RSAUtility;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceHandler;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.R;

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


public class WebViewUserRemoveActivity extends Activity {
    private ProgressDialog dialog;
    Intent mainIntent;
    String html, encVal;
    Common mCommon;
    private SharedStorage mSharedStorage;

    public static String uname, ufathername, ufathercontact,
            umobile, uaddress, uamount, uhiredate, currentdate_value1,
            uleavedate, uemail, UId = "", UBroomid, OwnerId, Dues, userid,
            propertyid, monthlyRoomRant;

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

        getIntentValues();

        new RenderView().execute();
    }

    private void getIntentValues() {

        Intent intent = getIntent();
        uname = intent.getStringExtra("Tname");
        ufathername = intent.getStringExtra("Tfather");
        umobile = intent.getStringExtra("Tmobile");
        uemail = intent.getStringExtra("Temail");
        uaddress = intent.getStringExtra("Taddress");
        uhiredate = intent.getStringExtra("Thire");
        uleavedate = intent.getStringExtra("Tleave");
        uamount = intent.getStringExtra("Tamount");
        UId = intent.getStringExtra("NTenantid");
        UBroomid = intent.getStringExtra("TBookRoom");
        OwnerId = intent.getStringExtra("Towner");
        userid = intent.getStringExtra("Tuserid");
        propertyid = intent.getStringExtra("sepratepropertyid");
        ufathercontact = intent.getStringExtra("TFatherCont");
        monthlyRoomRant = intent.getStringExtra("tmonthlyRoomRant");

    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewUserRemoveActivity.this);
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


                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                    } else if (html.indexOf("Success") != -1) {


                        status = "Transaction Successful!";
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    //etTransactionDetail();
                    new PaymentActivityOwnerJSONParse().execute();
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
                    if (url.indexOf("/ccavResponseHandler_booking.php") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            String REDIRECT_URL_value = "http://www.krooms.in/HostelMgmt/3.0/ccavenue/ccavResponseHandler_booking.php";
            String CANCEL_URL_value = "http://www.krooms.in/HostelMgmt/3.0/ccavenue/ccavResponseHandler_booking.php";

			/*An instance of this class will be registered as a JavaScript interface*/
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, REDIRECT_URL_value));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, CANCEL_URL_value));
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
                if (BillingShippingUserRemoveActivity.mActivity != null) {
                    BillingShippingUserRemoveActivity.mActivity.finish();
                }
                if (HostelDetailActivity.mActivity != null) {
                    HostelDetailActivity.mActivity.finish();
                }

                callSlider();
            } else {
                finish();
                if (BillingShippingUserRemoveActivity.mActivity != null) {
                    BillingShippingUserRemoveActivity.mActivity.finish();
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
        if (BillingShippingUserRemoveActivity.mActivity != null) {
            BillingShippingUserRemoveActivity.mActivity.finish();
        }
    }

    private class PaymentActivityOwnerJSONParse extends AsyncTask<String, String, String> {


        int count;
        String name;
        private boolean IsError = false;
        String message;
        String result;
        String monthnameheading = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            if (UserOwner_RemoveActivity.tenantreturnstatus.equals("1")) {
                monthnameheading = "Due Adj-Adv.ret";
            } else {

                monthnameheading = "Due Amount";
            }


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "cleardueamount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", UserOwner_RemoveActivity.userid));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", UserOwner_RemoveActivity.tenantid));
                nameValuePairs.add(new BasicNameValuePair("property_id", UserOwner_RemoveActivity.propertyid));
                nameValuePairs.add(new BasicNameValuePair("owner_id", MyPropertyUsersListActivity.owneridvaluemain));
                nameValuePairs.add(new BasicNameValuePair("month_id", "13"));
                nameValuePairs.add(new BasicNameValuePair("month_name", monthnameheading));
                nameValuePairs.add(new BasicNameValuePair("amount", UserOwner_RemoveActivity.totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", UserOwner_RemoveActivity.roomid));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", ""));
                nameValuePairs.add(new BasicNameValuePair("year", "" + UserOwner_RemoveActivity.yearname));
                nameValuePairs.add(new BasicNameValuePair("room_amount", UserOwner_RemoveActivity.dueAmountvalue));
                nameValuePairs.add(new BasicNameValuePair("current_date", UserOwner_RemoveActivity.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("month_date", UserOwner_RemoveActivity.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "O"));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", UserOwner_RemoveActivity.paymentcomment));
                //new chnages on 22/11/2018
                nameValuePairs.add(new BasicNameValuePair("tenantreturnstatus", UserOwner_RemoveActivity.tenantreturnstatus));//add in api
                nameValuePairs.add(new BasicNameValuePair("tamountfinal", UserOwner_RemoveActivity.tamountfinal));//add in api
                nameValuePairs.add(new BasicNameValuePair("tenantadjustamount", UserOwner_RemoveActivity.tenantadjustamount));//add in api
                nameValuePairs.add(new BasicNameValuePair("dueequaltotenantamt", UserOwner_RemoveActivity.amountequalstatus));//add in api

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
					/*Intent i=new Intent(WebViewUserRemoveActivity.this,HostelListActivity.class);
					startActivity(i);*/
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
                SendButtonStatus(monthnameheading);
                Toast.makeText(WebViewUserRemoveActivity.this, "Payment is successfull", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(WebViewUserRemoveActivity.this, "Payment is not success full", Toast.LENGTH_LONG).show();

            }
        }

    }


    //Add tenant Adv.adjust and no dues done status
    private void SendButtonStatus(final String monthnameheading) {

        final ProgressDialog dialog = new ProgressDialog(WebViewUserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "addButtonStatus");
            params.put("property_id", UserOwner_RemoveActivity.propertyid);
            params.put("tenant_id", UserOwner_RemoveActivity.tenantid);
            params.put("status", '2');//for due clear
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getButtonresponse(result, monthnameheading);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getButtonresponse(String result, String monthnameheading) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                Successdialog(monthnameheading);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                Successdialog(monthnameheading);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //


    public void Successdialog(final String monthnameheading) {
        Dialog successdialog = new Dialog(WebViewUserRemoveActivity.this);
        successdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successdialog.setContentView(R.layout.success_dialogpayment_owner);
        successdialog.setCanceledOnTouchOutside(false);
        final CardView card_view = (CardView) successdialog.findViewById(R.id.card_view);
        TextView username = (TextView) successdialog.findViewById(R.id.username);
        TextView userdate = (TextView) successdialog.findViewById(R.id.userdate);
        TextView paymentmonth = (TextView) successdialog.findViewById(R.id.paymentmonth);
        TextView userpaid = (TextView) successdialog.findViewById(R.id.userpaid);
        TextView usermode = (TextView) successdialog.findViewById(R.id.usermode);
        TextView usertid = (TextView) successdialog.findViewById(R.id.usertid);
        TextView userpid = (TextView) successdialog.findViewById(R.id.userpropertyid);
        TextView userroomno = (TextView) successdialog.findViewById(R.id.userroomno);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    String amt = "", mname = "";
                    //changes 21/12/2018
                    if (UserOwner_RemoveActivity.amountequalstatus.equals("1")) {
                        amt = UserOwner_RemoveActivity.TenantReturnAmount;
                        mname = "Due Adj-Adv.ret";
                    } else {

                        amt = UserOwner_RemoveActivity.totalAmount_value;
                        mname = monthnameheading;
                    }//end

                    Document document = new Document();
                    String path = Environment.getExternalStorageDirectory() + "/Krooms";
                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();

                    Log.d("PDFCreator", "PDF Path: " + path);

                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;

                    File file = new File(dir, "PaySlip" + i1 + ".pdf");
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();
                    CreatePdf(document);
                    addTitlePage(document, UserOwner_RemoveActivity.currentdate_value1,
                            UserRemoveActivity.uname, "", amt
                            , "Online",
                            mname, UserOwner_RemoveActivity.keyidv,
                            UserOwner_RemoveActivity.roomnov,
                            UserOwner_RemoveActivity.keypropertyid, UserOwner_RemoveActivity.paymentcomment);
                    addMetaData(document);
                    Toast.makeText(WebViewUserRemoveActivity.this, "Tenant Record downloaded succesfully ", Toast.LENGTH_LONG).show();
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
				/*Intent i = new Intent(WebViewUserRemoveActivity.this, HostelListActivity.class);
				startActivity(i);*/


                //old working
				/*Intent intent=new Intent(WebViewUserRemoveActivity.this,UserRemoveActivity.class);
				intent.putExtra("Tname",uname);
				intent.putExtra("Towner",OwnerId);
				intent.putExtra("Taddress",uaddress);
				intent.putExtra("Tamount",uamount);
				intent.putExtra("Tmobile",umobile);
				intent.putExtra("Tfather",ufathername);
				intent.putExtra("Thire",uhiredate);
				intent.putExtra("Tleave",uleavedate);
				intent.putExtra("Temail",uemail);
				intent.putExtra("NTenantid",UId);
				intent.putExtra("TBookRoom",UBroomid);
				intent.putExtra("TDue","0");
				intent.putExtra("sepratepropertyid",propertyid);
				intent.putExtra("TFatherCont", ufathercontact);
				intent.putExtra("Tuserid",userid);
				intent.putExtra("tmonthlyRoomRant",monthlyRoomRant);
				startActivity(intent);*/
                //


                //new chnages on 13/11/2018
                Intent intent = new Intent(WebViewUserRemoveActivity.this, MyPropertyUsersListActivity.class);
                startActivity(intent);
                //
            }
        });
        username.setText(PaymentActivityOwner.name);
        userdate.setText(PaymentActivityOwner.currentdate_value1);


        if (UserOwner_RemoveActivity.amountequalstatus.equals("1")) {
            paymentmonth.setText("Due Adj-Adv.ret");
            userpaid.setText(UserOwner_RemoveActivity.TenantReturnAmount);
        } else {

            paymentmonth.setText(monthnameheading);
            userpaid.setText(PaymentActivityOwner.payment);
        }//end


        userpid.setText(UserOwner_RemoveActivity.keypropertyid);
        usertid.setText(UserOwner_RemoveActivity.keyidv);
        userroomno.setText(UserOwner_RemoveActivity.roomnov);
        usermode.setText("Online");
        chequeno.setText(UserOwner_RemoveActivity.paymentcomment);
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //old working
				/*Intent intent=new Intent(WebViewUserRemoveActivity.this,UserRemoveActivity.class);
				intent.putExtra("Tname",uname);
				intent.putExtra("Towner",OwnerId);
				intent.putExtra("Taddress",uaddress);
				intent.putExtra("Tamount",uamount);
				intent.putExtra("Tmobile",umobile);
				intent.putExtra("Tfather",ufathername);
				intent.putExtra("Thire",uhiredate);
				intent.putExtra("Tleave",uleavedate);
				intent.putExtra("Temail",uemail);
				intent.putExtra("NTenantid",UId);
				intent.putExtra("TBookRoom",UBroomid);
				intent.putExtra("TDue","0");
				intent.putExtra("sepratepropertyid",propertyid);
				intent.putExtra("TFatherCont", ufathercontact);
				intent.putExtra("Tuserid",userid);
				intent.putExtra("tmonthlyRoomRant",monthlyRoomRant);
				startActivity(intent);
*/

                //new chnages on 13/11/2018
                Intent intent = new Intent(WebViewUserRemoveActivity.this, MyPropertyUsersListActivity.class);
                startActivity(intent);
                //

            }
        });

        successdialog.show();
    }


    public static void addMetaData(Document document) {
        document.addTitle("Payment Slip");
        document.addSubject("Details for payment transaction");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public void addTitlePage(Document document, String date, String name, String tId, String amount, String mode, String month, String keyid, String roomno, String keypropertyid, String paymentcomment)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);

        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(createValueCell1("Payment Slip"));
        preface.add(table1);
        addEmptyLine(preface, 1);

        PdfPTable table4 = new PdfPTable(1);
        table4.addCell(createValueCell5("Date :" + date));
        preface.add(table4);
        addEmptyLine(preface, 1);

        PdfPTable table2 = new PdfPTable(1);
        table2.addCell(createValueCell22("Dear Tenant,"));
        preface.add(table2);
        addEmptyLine(preface, 1);

        PdfPTable table3 = new PdfPTable(1);
        table3.addCell(createValueCell3("Thank you for using KROOMS ! Your tarnsaction details are as follows:"));
        preface.add(table3);
        addEmptyLine(preface, 1);

        PdfPTable table = new PdfPTable(2);
        table.addCell(createValueCell2("Tenant Name"));
        table.addCell(createValueCell4(name));

        table.addCell(createValueCell2("Tenant Id"));
        table.addCell(createValueCell4(keyid));

        table.addCell(createValueCell2("Room No."));
        table.addCell(createValueCell4(roomno));

        table.addCell(createValueCell2("Property Id"));
        table.addCell(createValueCell4(keypropertyid));

        table.addCell(createValueCell2("Paid Amount"));
        table.addCell(createValueCell4(amount));

        table.addCell(createValueCell2("Paid For Month"));
        table.addCell(createValueCell4(month));
        addEmptyLine(preface, 1);

        table.addCell(createValueCell2("Payment Mode"));
        table.addCell(createValueCell4(mode));
        addEmptyLine(preface, 1);

        table.addCell(createValueCell2("Comment"));
        table.addCell(createValueCell4(paymentcomment));
        addEmptyLine(preface, 1);


        PdfPTable table6 = new PdfPTable(1);
        table6.addCell(createValueCell6("Address : A-3, Pawansut Appts, Khamla road"));
        table6.addCell(createValueCell6("Dev Nagar,Nagpur - 440025, Maharashtra, India"));
        table6.addCell(createValueCell6("Contact No: 7067583243"));
        table6.addCell(createValueCell6("Email : booking@krooms.in"));
        addEmptyLine(preface, 7);

        preface.add(9, table);
        preface.add(17, table6);
        document.add(preface);
        document.newPage();
    }

    public void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void CreatePdf(Document document) {
        try {
            PdfPTable table1 = new PdfPTable(3);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.iconk1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setBackgroundColor(BaseColor.WHITE);
            table1.addCell(new PdfPCell(myImg, true));
            table1.addCell(createValueCell1("Finest Rental Solutions"));
            document.add(table1);
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
    }

    public PdfPCell createValueCell1(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellname4.setBorder(0);
        cellname4.setColspan(2);
        return cellname4;
    }

    public PdfPCell createValueCell2(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setPaddingBottom(5);
        cellname4.setPaddingLeft(5);
        cellname4.setPaddingRight(5);
        cellname4.setPaddingTop(5);
        return cellname4;
    }

    public PdfPCell createValueCell22(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setBorder(0);
        return cellname4;
    }

    public PdfPCell createValueCell3(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setBorder(0);
        return cellname4;
    }

    public PdfPCell createValueCell6(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellname4.setVerticalAlignment(Element.ALIGN_LEFT);
        cellname4.setBorder(0);
        return cellname4;
    }

    public PdfPCell createValueCell5(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(0, 0, 0));
        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellname4.setVerticalAlignment(Element.ALIGN_RIGHT);
        cellname4.setBorder(0);
        return cellname4;
    }

    public PdfPCell createValueCell4(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_BASELINE);
        cellname4.setVerticalAlignment(Element.ALIGN_BASELINE);
        cellname4.setPaddingBottom(5);
        cellname4.setPaddingLeft(5);
        cellname4.setPaddingRight(5);
        cellname4.setPaddingTop(5);
        return cellname4;
    }

}