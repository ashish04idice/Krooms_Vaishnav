package com.krooms.hostel.rental.property.app.ccavenue_activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.krooms.hostel.rental.property.app.activity.Electricity_Activity;
import com.krooms.hostel.rental.property.app.activity.PDFFileNewPage_owner_bedbooking;
import com.krooms.hostel.rental.property.app.activity.PDFFileNewPage_owner_bedbookingonline;
import com.krooms.hostel.rental.property.app.activity.PropertyRCUActivity;
import com.krooms.hostel.rental.property.app.modal.ElectricityModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceHandler;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.GetTransactionTable;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.ccavenue_utility.Constants;
import com.krooms.hostel.rental.property.app.ccavenue_utility.RSAUtility;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.Common;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WebViewActivity extends Activity {
    private ProgressDialog dialog;
    Intent mainIntent;
    String html, encVal;
    Common mCommon;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mainIntent = getIntent();
        mCommon = new Common();

        // Calling async task to get display content
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
            dialog = new ProgressDialog(WebViewActivity.this);
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
                    GetTransactionDetail();
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
                    + "," + mainIntent.getStringExtra(AvenuesParams.ROOM_AMOUNT)
                    + "," + mainIntent.getStringExtra(AvenuesParams.HIRING_DATE)
                    + "," + mainIntent.getStringExtra(AvenuesParams.LEAVING_DATE)
                    + "," + mainIntent.getStringExtra(AvenuesParams.comment)));

            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, mainIntent.getStringExtra(AvenuesParams.ROOM_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, mainIntent.getStringExtra(AvenuesParams.BOOKED_BED)));
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
                SharedStorage mSharedStorage = SharedStorage.getInstance(this);
                JSONArray data = jsonObject.getJSONArray(WebUrls.DATA_JSON);

                String transid = data.getJSONObject(0).getString("transaction_id");
                mSharedStorage.setAddCount("1");
                downloadReport(transid);

                if (BillingShippingActivity.mActivity != null) {
                    BillingShippingActivity.mActivity.finish();
                }
                if (HostelDetailActivity.mActivity != null) {
                    HostelDetailActivity.mActivity.finish();
                }

                callSlider();
            } else {
                finish();
                if (BillingShippingActivity.mActivity != null) {
                    BillingShippingActivity.mActivity.finish();
                }
                // mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
            }

        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }

    }

    //api calling
    private void downloadReport(final String data) {

        final ProgressDialog dialog = new ProgressDialog(WebViewActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getbookingreport");
            params.put("property_id", mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
            params.put("user_id", mainIntent.getStringExtra(AvenuesParams.USER_ID));
            params.put("room_id", mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseData(result, data);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseData(String result, String data) {
        try {
            String roomno = "", amount = "", paid = "", payment_type = "", comment = "", keyproperty_id = "";
            SharedStorage mSharedStorage = SharedStorage.getInstance(this);
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("status");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("true")) {

                JSONArray jsonarray = jsmain.getJSONArray("data");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    roomno = jsonobj.getString("room_no");
                    amount = jsonobj.getString("amount");
                    paid = jsonobj.getString("paid");
                    payment_type = jsonobj.getString("payment_type");
                    comment = jsonobj.getString("comment");
                    keyproperty_id = jsonobj.getString("key_property_information");
                }
                String pid = mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID);
                comment = mainIntent.getStringExtra(AvenuesParams.comment);
                Successdialogslip(pid, keyproperty_id, amount, paid, payment_type, comment, roomno, data);
            } else {
                Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_SHORT).show();
                mSharedStorage.setBookedPropertyId(mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
                Intent mIntent = new Intent(this, PropertyRCUActivity.class);
                mIntent.putExtra("property_id", mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
                mIntent.putExtra("transaction_id", data);
                mIntent.putExtra("room_id", mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
                startActivity(mIntent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //


    public void callSlider() {
        SharedStorage mSharedStorage = SharedStorage.getInstance(this);
        mSharedStorage.setAddCount("1");
        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (BillingShippingActivity.mActivity != null) {
            BillingShippingActivity.mActivity.finish();
        }
    }


    //dailog pdf file download

    public void Successdialogslip(final String property_id, final String keyproperty_id, final String amount, final String paid, final String payment_type, final String comment, final String room_no, final String data) {
        final Dialog successdialog = new Dialog(WebViewActivity.this, R.style.AppTheme);
        successdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successdialog.setContentView(R.layout.success_dialogpayment_bedbooking);
        successdialog.setCanceledOnTouchOutside(false);
        final android.support.v7.widget.CardView card_view = (CardView) successdialog.findViewById(R.id.card_view);
        TextView userdate = (TextView) successdialog.findViewById(R.id.userdate);
        TextView userpid = (TextView) successdialog.findViewById(R.id.userpropertyid);
        TextView userroomno = (TextView) successdialog.findViewById(R.id.userroomno);
        TextView paymenttotal = (TextView) successdialog.findViewById(R.id.paymenttotal);
        TextView userpaid = (TextView) successdialog.findViewById(R.id.userpaid);
        TextView userdueamount = (TextView) successdialog.findViewById(R.id.userdueamount);
        TextView usermode = (TextView) successdialog.findViewById(R.id.usermode);
        TextView propertyname = (TextView) successdialog.findViewById(R.id.propertyname);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");

        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        final String currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedStorage mSharedStorage = SharedStorage.getInstance(WebViewActivity.this);

                Intent i = new Intent(WebViewActivity.this, PDFFileNewPage_owner_bedbookingonline.class);
                i.putExtra("date", currentdate_value);
                i.putExtra("propertyid", keyproperty_id);
                i.putExtra("roomno", room_no);
                i.putExtra("totalamount", amount);
                i.putExtra("paid", paid);
                i.putExtra("due", "");
                i.putExtra("mode", payment_type);
                i.putExtra("Chequeno", comment);
                i.putExtra("PropertyIdMain", property_id);
                i.putExtra("transaction_id", data);
                i.putExtra("room_id", mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                startActivity(i);
                successdialog.dismiss();

            }
        });
        final SharedStorage mSharedStorage = SharedStorage.getInstance(this);

        userdate.setText(currentdate_value);
        userpid.setText(keyproperty_id);
        paymenttotal.setText(amount);
        userpaid.setText(paid);
        usermode.setText(payment_type);
        userroomno.setText(room_no);
        userdueamount.setText("");
        chequeno.setText(comment);
        propertyname.setText(mSharedStorage.getPropertyname());

        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mSharedStorage.setBookedPropertyId(mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
                Intent mIntent = new Intent(WebViewActivity.this, PropertyRCUActivity.class);
                mIntent.putExtra("property_id", mainIntent.getStringExtra(AvenuesParams.PROPERTY_ID));
                mIntent.putExtra("transaction_id", data);
                mIntent.putExtra("room_id", mainIntent.getStringExtra(AvenuesParams.ROOM_ID));
                startActivity(mIntent);
                finish();


                successdialog.dismiss();
            }
        });

        successdialog.show();
    }

    //
}