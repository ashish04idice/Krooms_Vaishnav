package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingParentActivity;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingTanentActivity;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.TanentStudentNameModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 3/14/2017.
 */
public class PaymentActivityParent extends Activity {
    String userid_main;
    private TextView totalAmount;
    private TextView termAndCondition;
    private Button submitBtn;
    private RadioButton caseOption, onlineOption;
    private CheckBox checkeBox;
    RelativeLayout back_button;
    String paymentoptionvalue = "";
    public static String advanceamountvalue = "", roomamountva = "", yearvaluet = "";
    ArrayList<TanentStudentNameModel> Tanentstudentarraylist;
    String mHireDate = " ", mLeaveDate = " ";
    String rentAmount;
    Integer randomNum;
    String paymentOption = "";
    PropertyModal mPropertyModal;
    Intent in;
    public static String monthV, amountV, monthid, currentdate_value,
            currentdate_value1, totalAmount_value, tenant_id_value_new,
            keyidtenant, roomnotenant, useridvalue, bookedpropertyidvalue, Bookedpropertykeyid;
    private Common mCommon;
    ImageView student_loader;
    Animation rotation;
    SharedStorage mSharedStorage;
    public static String ownerid_value, propertyid_value, roomid_value;
    TanentStudentNameModel tanentstudentmodel;
    public static String tanentname;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_tanent_activity);

        mSharedStorage = SharedStorage.getInstance(this);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

        student_loader = (ImageView) findViewById(R.id.student_loader);
        totalAmount = (TextView) findViewById(R.id.totalAmount_output);
        termAndCondition = (TextView) findViewById(R.id.termsAndConditionTextView);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setClickable(true);
        caseOption = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
        onlineOption = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
        checkeBox = (CheckBox) findViewById(R.id.checkeBox);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        in = getIntent();
        keyidtenant = in.getStringExtra("keyid");
        monthV = in.getStringExtra("monthV");
        amountV = in.getStringExtra("amountV");
        monthid = in.getStringExtra("monthid");
        yearvaluet = in.getStringExtra("yearv");
        tenant_id_value_new = in.getStringExtra("tiid");
        tanentname = in.getStringExtra("tanentname");
        roomnotenant = in.getStringExtra("roomno");
        useridvalue = in.getStringExtra("userid");
        bookedpropertyidvalue = in.getStringExtra("bookedpropertyid");
        totalAmount.setText(amountV);

        new Bookedpropertykeyid().execute();
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        final int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        // currentdate_value =yearO+"/"+(monthO+1)+"/"+dateO;
        currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        currentdate_value1 = dateO + "/" + (monthO + 1) + "/" + yearO;

        new TanentdetailsJsonParse().execute();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (mSharedStorage.getUserType().equals("2")) {

            caseOption.setVisibility(View.GONE);
        }

        RadioGroup group = (RadioGroup) findViewById(R.id.paymentRadioGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d("payment 1", "in group");

                if (onlineOption.isChecked()) {
                    paymentoptionvalue = "Online";

                }
            }
        });


        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PaymentActivityParent.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.terms_condition_dialog);
                dialog.show();
                TextView text = (TextView) dialog.findViewById(R.id.termText);
                text.setMovementMethod(new ScrollingMovementMethod());

                if (mSharedStorage.getUserType().equals(3)) {

                    text.setText(Html.fromHtml(TermAndConditionDialog.ownerHtmlTC));
                } else {
                    text.setText(Html.fromHtml(TermAndConditionDialog.htmlTC));
                }
                Button agreeBtn = (Button) dialog.findViewById(R.id.agreeBtn);
                agreeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("payment 2", "in button" + mSharedStorage.getUserId() + "" + mSharedStorage.getBookedPropertyId());
                totalAmount_value = totalAmount.getText().toString();

                if (totalAmount_value.equals("")) {
                    Toast.makeText(PaymentActivityParent.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (totalAmount_value.startsWith("0")) {

                    Toast.makeText(PaymentActivityParent.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                } else if (paymentoptionvalue.equals("")) {
                    Toast.makeText(PaymentActivityParent.this, "please select online", Toast.LENGTH_SHORT).show();
                } else if (!checkeBox.isChecked()) {
                    Toast.makeText(PaymentActivityParent.this, "please Accept Terms and Condition ", Toast.LENGTH_SHORT).show();

                } else {
                    submitBtn.setClickable(false);
                    submitBtn.setEnabled(true);
                    //online payment
                    Log.d("roomid oi", "" + roomid_value + "owner id" + ownerid_value + "Property id" + propertyid_value);
                    Intent intent = new Intent(PaymentActivityParent.this, BillingShippingParentActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                    intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                    intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                    intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                    intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                    intent.putExtra(AvenuesParams.AMOUNT, totalAmount_value);
                    intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                    intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                    intent.putExtra(AvenuesParams.USER_ID, useridvalue);
                    intent.putExtra(AvenuesParams.PROPERTY_ID, bookedpropertyidvalue);
                    intent.putExtra(AvenuesParams.ROOM_ID, roomid_value);
                    intent.putExtra(AvenuesParams.ROOM_AMOUNT, "");
                    intent.putExtra(AvenuesParams.HIRING_DATE, tenant_id_value_new);
                    intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                    intent.putExtra(AvenuesParams.BOOKED_BED, "");
                    String tenantvaluedata = tenant_id_value_new;
                    intent.putExtra(AvenuesParams.TENANT_ID, tenant_id_value_new);
                    intent.putExtra(AvenuesParams.OWNER_ID, ownerid_value);
                    intent.putExtra(AvenuesParams.MONTH, monthV);
                    intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                    intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                    startActivity(intent);
                    //........
                }
            }
        });
    }

    private class TanentdetailsJsonParse extends AsyncTask<String, String, String> {

        int count;
        String name;
        private boolean IsError = false;
        String result, message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            student_loader.startAnimation(rotation);
            student_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {
                Tanentstudentarraylist = new ArrayList<TanentStudentNameModel>();
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "tenantpaidstatus"));
                nameValuePairs.add(new BasicNameValuePair("user_id", useridvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    // array = new String[jsonarray.length()];
                    for (int i = 0; i < jsonarray.length(); i++) {
                        tanentstudentmodel = new TanentStudentNameModel();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);

                        tanentstudentmodel.setRoom_id(jsonobj.getString("room_id"));
                        tanentstudentmodel.setOwner_id(jsonobj.getString("owner_id"));
                        tanentstudentmodel.setProperty_id(jsonobj.getString("property_id"));
                        roomid_value = jsonobj.getString("room_id");
                        ownerid_value = jsonobj.getString("owner_id");
                        propertyid_value = jsonobj.getString("property_id");
                        Log.d("roomid", "" + roomid_value);
                        Tanentstudentarraylist.add(tanentstudentmodel);

                    }
                }
                Log.d("resultmain", "" + tanentstudentmodel);
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                    new AdvanceAmountGet().execute();

                } else if (result.equalsIgnoreCase("N")) {
                    Toast.makeText(PaymentActivityParent.this, "Tanent not Found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(PaymentActivityParent.this, "Please Check APi", Toast.LENGTH_LONG).show();
            }

        }
    }

    public class AdvanceAmountGet extends AsyncTask<String, String, String> {

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
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "getadvanceamount"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", roomid_value));


                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                Log.d("logv ", "" + jsmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        advanceamountvalue = jsonarray.getJSONObject(i).getString("advance");
                        roomamountva = jsonarray.getJSONObject(i).getString("amount");
                    }
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
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                } else if (result.equals("N")) {
//                    Toast.makeText(TenantDetailActivity.this,"No data found", Toast.LENGTH_LONG).show();
                }
            } else {
//                Toast.makeText(TenantDetailActivity.this,"please check network connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean Validation() {
        boolean valid = true;

        String totalAmount_value = totalAmount.getText().toString();
        if (totalAmount_value.equals("")) {
            totalAmount.setError("Enter Amount");
            valid = false;
        } else {
            totalAmount.setError(null);
        }

        return valid;
    }


    public class Bookedpropertykeyid extends AsyncTask<String, String, String> {

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
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id", bookedpropertyidvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {
                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        //  Bookedpropertykeyid
                        Bookedpropertykeyid = jsonobj.getString("key_property_information");

                    }
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

            if (result.equalsIgnoreCase("true")) {

            } else if (result.equals("false")) {

                Toast.makeText(PaymentActivityParent.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }


}
