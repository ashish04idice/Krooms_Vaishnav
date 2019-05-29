package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.adapter.OwnerMonthNameAdapter;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingTanentActivity_FromListView;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.PaymentDetailModal;

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
 * Created by admin on 11/17/2016.
 */
public class PaymentActivtyTanent_FromListview extends Activity
{
    String monthidvalue;
    private TextView termAndCondition;
    private Button bookProperty;
    private RadioButton caseOption, onlineOption;
    private CheckBox checkeBox;
    RelativeLayout back_button;
    String paymentoptionvalue="";
    ImageView student_loader;
    public static String monthlypaymentvalue,advancepay="",roompay="",year_value_te="",keypropertyid="",keyid="",roomno="",Bookedpropertykeyid;
    SharedStorage mSharedStorage;
    String rentAmount,termsdata="";
    Integer randomNum;
    EditText totalAmount_output;

    private Common mCommon;
    String name;
    Animation rotation;
    public static String month_date,tanentname,month,currentdate_value,currentdate_value1,tanentusrid_value,totalAmount_value="",tanentroomid_value,monthid_value,tanentownerid_value,tanentpropertyid_value,tenantIdValue,type="";
    Intent in;
    PaymentDetailModal p_modal;
    int dueamountvalue;
    String propertyidmainavlue;

    LinearLayout layoutothermonth;
    Dialog complaintype;
    TextView txtmonthname;
    ArrayList<OwnerStudentNameModel> arraymonth;
    OwnerStudentNameModel monthModel;
    ListView month_list;
    ImageView month_loader;
    OwnerMonthNameAdapter adapter_data_month;
    String totalremainingamount;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_tanentactivity_fromlistview);

        layoutothermonth=(LinearLayout)findViewById(R.id.monthnameouterlay);
        txtmonthname=(TextView)findViewById(R.id.txtmonth);
        txtmonthname.setText("Select Month");
        mSharedStorage = SharedStorage.getInstance(this);
        tanentname=mSharedStorage.getUserFirstName();
        Log.d("username",""+tanentname);
        in=getIntent();


        type=in.getStringExtra("type");

        if(type.equalsIgnoreCase("1")) {
            month_date="0";
            tanentownerid_value="0";
            dueamountvalue = Integer.parseInt(in.getStringExtra("dueamountvalue"));
            Log.d("due value",""+dueamountvalue);
            month=in.getStringExtra("month");
            tanentusrid_value=in.getStringExtra("tanentuserid");
            tanentroomid_value=in.getStringExtra("tanentroomid");
            monthid_value=in.getStringExtra("monthid");
            monthlypaymentvalue=in.getStringExtra("monthlypayment");
            //propertyidmainavlue=tanentpropertyid_value;
            year_value_te=in.getStringExtra("yearvalue");
            tanentpropertyid_value=in.getStringExtra("tanentpropertyid");
            tenantIdValue=ListViewActivityTanent.tenant_idvalue;
        }
        else{
            dueamountvalue = Integer.parseInt(in.getStringExtra("dueamountvalue"));
            Log.d("due value",""+dueamountvalue);
            month=in.getStringExtra("month");
            tanentusrid_value=in.getStringExtra("tanentuserid");
            tanentroomid_value=in.getStringExtra("tanentroomid");
            monthid_value=in.getStringExtra("monthid");
            monthlypaymentvalue=in.getStringExtra("monthlypayment");
            //propertyidmainavlue=tanentpropertyid_value;
            tanentownerid_value=in.getStringExtra("tanentownerid");
            year_value_te=in.getStringExtra("yearvalue");
            month_date=in.getStringExtra("month_date");
            tanentpropertyid_value=in.getStringExtra("tanentpropertyid");
            tenantIdValue=ListViewActivityTanent.tenant_idvalue;
        }


        if(monthid_value.equalsIgnoreCase("13")){

            layoutothermonth.setVisibility(View.VISIBLE);
            //Calldailog();
        }

        if(monthid_value.equalsIgnoreCase("14")){

            layoutothermonth.setVisibility(View.VISIBLE);
            //Calldailog();
        }

        if(monthid_value.equalsIgnoreCase("15")){

            layoutothermonth.setVisibility(View.VISIBLE);
            //Calldailog();
        }

        layoutothermonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(PaymentActivityOwnerListview.this, "Electric pay", Toast.LENGTH_SHORT).show();
                getFineheadDialog();

            }
        });


        new Bookedpropertykeyid().execute();

        new AdvanceAmountGet().execute();

        Calendar calendar = Calendar.getInstance();
        int dateO=calendar.get(Calendar.DAY_OF_MONTH);
        int monthO=calendar.get(Calendar.MONTH);
        int yearO=calendar.get(Calendar.YEAR);
        currentdate_value=dateO+"/"+(monthO+1)+"/"+yearO;
        currentdate_value1 =dateO+"/"+(monthO+1)+"/"+yearO;

        Log.d("propertyid",tanentpropertyid_value);
        Log.d("total amount",roompay);
//        tenantpaidamount = user_id,month,amount,room_id,property_id
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

        student_loader=(ImageView)findViewById(R.id.student_loader);
        totalAmount_output =(EditText)findViewById(R.id.totalAmount_output);
        termAndCondition = (TextView)findViewById(R.id.termsAndConditionTextView);
        bookProperty =(Button)findViewById(R.id.submitBtn);
        bookProperty.setClickable(true);
        caseOption = (RadioButton)findViewById(R.id.casePaymentRadioBtn);
        onlineOption = (RadioButton)findViewById(R.id.onlinePaymentRadioBtn);
        checkeBox = (CheckBox)findViewById(R.id.checkeBox);
        back_button=(RelativeLayout)findViewById(R.id.back_button);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        RadioGroup group = (RadioGroup)findViewById(R.id.paymentRadioGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d("payment 1", "in group");
                 if(onlineOption.isChecked())
                {
                    paymentoptionvalue="Online";

                }
            }
        });

        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog=new Dialog(PaymentActivtyTanent_FromListview.this);
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


        bookProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String monthname1 = txtmonthname.getText().toString();

                if (layoutothermonth.getVisibility() == View.VISIBLE) {
                    // bookProperty.setEnabled(false);
                    Log.d("payment 2", "in button" + mSharedStorage.getUserId() + "" + mSharedStorage.getBookedPropertyId());
                    totalAmount_value = totalAmount_output.getText().toString();

                    if (monthname1.equalsIgnoreCase("Select Month")) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please Select Month ", Toast.LENGTH_SHORT).show();

                    }else

                    if (totalAmount_value.equals("")) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please enter amount", Toast.LENGTH_SHORT).show();

                    } else if (totalAmount_value.startsWith("0")) {

                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                    } else if (Integer.parseInt(totalAmount_value) > dueamountvalue) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please Enter Amount Lessthen Due Amount ", Toast.LENGTH_SHORT).show();

                    } else if (paymentoptionvalue.equals("")) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "please select online", Toast.LENGTH_SHORT).show();

                    } else if (!checkeBox.isChecked()) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "please Accept Terms and Condition ", Toast.LENGTH_SHORT).show();

                    } else {

                        if(month.equalsIgnoreCase("Electricity")){

                            month=monthname1;
                            //Calldailog();
                        }
                        else{
                            month = monthname1;
                        }

                        int totalremain= Integer.parseInt(totalremainingamount);
                        int userenteramount= Integer.parseInt(totalAmount_value);
                        if(totalremain < userenteramount)
                        {
                            bookProperty.setClickable(true);
                            bookProperty.setEnabled(true);
                            Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            bookProperty.setClickable(false);
                            bookProperty.setEnabled(true);
                            //online payment
                            Log.d("roomid oi", totalAmount_value + "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                            Intent intent = new Intent(PaymentActivtyTanent_FromListview.this, BillingShippingTanentActivity_FromListView.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                            intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                            intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                            intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                            intent.putExtra(AvenuesParams.AMOUNT, totalAmount_value);
                            intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                            intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                            intent.putExtra(AvenuesParams.USER_ID, tanentusrid_value);
                            intent.putExtra(AvenuesParams.PROPERTY_ID, tanentpropertyid_value);
                            intent.putExtra(AvenuesParams.ROOM_ID, tanentroomid_value);
                            intent.putExtra(AvenuesParams.ROOM_AMOUNT, "");
                            intent.putExtra(AvenuesParams.HIRING_DATE, "");
                            intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                            intent.putExtra(AvenuesParams.BOOKED_BED, "");
                            intent.putExtra(AvenuesParams.TENANT_ID, tenantIdValue);
                            intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                            intent.putExtra(AvenuesParams.MONTH, monthid_value);
                            intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                            intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                            startActivity(intent);
                            // Toast.makeText(PaymentActivtyTanent_FromListview.this, "please  ", Toast.LENGTH_SHORT).show();
                            //....
                        }


                    }
                }
                else{

                    totalAmount_value = totalAmount_output.getText().toString();

                    if (totalAmount_value.equals("")) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please enter amount", Toast.LENGTH_SHORT).show();

                    } else if (totalAmount_value.startsWith("0")) {

                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                    } else if (Integer.parseInt(totalAmount_value) > dueamountvalue) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "Please Enter Amount Lessthen Due Amount ", Toast.LENGTH_SHORT).show();

                    } else if (paymentoptionvalue.equals("")) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "please select online", Toast.LENGTH_SHORT).show();

                    } else if (!checkeBox.isChecked()) {
                        Toast.makeText(PaymentActivtyTanent_FromListview.this, "please Accept Terms and Condition ", Toast.LENGTH_SHORT).show();

                    } else {

                            bookProperty.setClickable(false);
                            bookProperty.setEnabled(true);
                           //online payment
                            Log.d("roomid oi", totalAmount_value + "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                            Intent intent = new Intent(PaymentActivtyTanent_FromListview.this, BillingShippingTanentActivity_FromListView.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                            intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                            intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                            intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                            intent.putExtra(AvenuesParams.AMOUNT, totalAmount_value);
                            intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                            intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                            intent.putExtra(AvenuesParams.USER_ID, tanentusrid_value);
                            intent.putExtra(AvenuesParams.PROPERTY_ID, tanentpropertyid_value);
                            intent.putExtra(AvenuesParams.ROOM_ID, tanentroomid_value);
                            intent.putExtra(AvenuesParams.ROOM_AMOUNT, "");
                            intent.putExtra(AvenuesParams.HIRING_DATE, "");
                            intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                            intent.putExtra(AvenuesParams.BOOKED_BED, "");
                            intent.putExtra(AvenuesParams.TENANT_ID, tenantIdValue);
                            intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                            intent.putExtra(AvenuesParams.MONTH, monthid_value);
                            intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                            intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                            startActivity(intent);
                            // Toast.makeText(PaymentActivtyTanent_FromListview.this, "please  ", Toast.LENGTH_SHORT).show();

                    }

                }
            }

        });
    }
    //for month name selection:-
    public void getFineheadDialog() {
        complaintype = new Dialog(PaymentActivtyTanent_FromListview.this);
        complaintype.requestWindowFeature(Window.FEATURE_NO_TITLE);
        complaintype.setContentView(R.layout.custom_dialog_country);
        month_list = (ListView) complaintype.findViewById(R.id.list_country);
        month_loader=(ImageView)complaintype.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) complaintype.findViewById(R.id.country_cross_layout);

        if(NetworkConnection.isConnected(PaymentActivtyTanent_FromListview.this))
        {
            new MonthNameJsonParse().execute();
        }else
        {
            Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintype.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  String fine_value = fine[position].toString();

                String month_value=  arraymonth.get(position).getMonthname().toString();
                totalremainingamount=arraymonth.get(position).getRemaining_amount().toString();
                txtmonthname.setText(month_value);
                complaintype.dismiss();
            }
        });

        complaintype.show();
    }


    public boolean Validation()
    {
        boolean valid=true;

        String totalAmount_value = totalAmount_output.getText().toString();
        if(totalAmount_value.equals(""))
        {
            totalAmount_output.setError("Enter Amount");
            valid=false;
        }
        else
        {
            totalAmount_output.setError(null);
        }
     /*   if(caseOption.isChecked())
        {
            paymentoptionvalue="Cash";
        }
        else
        {
            paymentoptionvalue="";
            valid=false;
        }
        if(onlineOption.isChecked())
        {
            paymentoptionvalue="Online";
        }
        else
        {
            paymentoptionvalue="";
            valid=false;
        }*/
        return valid;
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

            System.out.print(propertyidmainavlue);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);

            SharedStorage ss=SharedStorage.getInstance(PaymentActivtyTanent_FromListview.this);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action","getadvanceamount"));
                nameValuePairs.add(new BasicNameValuePair("property_id",tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));


                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain =new JSONObject(object);
                Log.d("logv ",""+jsmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y"))
                {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i =0; i <jsonarray.length(); i++)
                    {
                        advancepay=jsonarray.getJSONObject(i).getString("advance");
                        roompay=jsonarray.getJSONObject(i).getString("amount");
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

                if (result.equalsIgnoreCase("Y"))
                {
                    new GetUserKeyDetail().execute();
                } else if (result.equals("N")) {
//                    Toast.makeText(TenantDetailActivity.this,"No data found", Toast.LENGTH_LONG).show();
                }
        }
    }



    public class GetUserKeyDetail extends AsyncTask<String, String, String> {

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
                nameValuePairs.add(new BasicNameValuePair("action","getuserkeydetails"));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",ListViewActivityTanent.tenant_idvalue));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain =new JSONObject(object);
                Log.d("logv ",""+jsmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y"))
                {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i =0; i <jsonarray.length(); i++)
                    {
                        roomno=jsonarray.getJSONObject(i).getString("room_no");
                        keyid=jsonarray.getJSONObject(i).getString("tenant_id");
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

                if (result.equalsIgnoreCase("Y"))
                {
                    new PropertyIdKeyParser().execute();
                } else if (result.equals("N")) {
                }
            }
    }

    private class PropertyIdKeyParser extends AsyncTask<String, String, String> {

        int count;
        String name,result;
        private boolean IsError =false;
        String message;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args)
        {


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action","propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id",tanentpropertyid_value));
                Log.d("prop 3", tanentpropertyid_value);

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("status");
                if (result.equals("true")) {
                    JSONArray jArray = jsmain.getJSONArray(WebUrls.DATA_JSON);
                    keypropertyid=jArray.getJSONObject(0).getString("key_property_information");
                }
            } catch (Exception e)
            {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("Y"))
            {
            } else if (result.equals("N")) {
            }
        }

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
                nameValuePairs.add(new BasicNameValuePair("action","propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id",tanentpropertyid_value));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                //   {"status":"true","message":"Get Data Successfull","data":[{"property_id":"2","key_property_information":"KR\/H\/2","owner_id":"2","rating":"0","user_id":"14","property_type":"1","property_name":"Queen hostel","property_nature":"nature","paid_service":"","owner_address":"12 kalani nagar","landmark":"","state":"1","city":"1","colony":"172","pincode":"452005","lat":"22.721550852965173","long":"75.85809286683798","total_area":"1500","video_url":"","google_url":"","super_built_up_area":"1400","property_face":"East-South","kitchen":"","kitchen_type":"","furnish_type":"","property_feature":" ","property_list":"property-list","water_supply":"Both","power_backup":"Yes","other_aminities":"","no_of_room":"5","for_whom":"Boys","railway_station":"10","bus_stand":"12","airport":"4","nearest":"","square":"","tenant_type":"1","curfew_time":"10:41 AM","vehicle":"2","smoking":"Yes","drinking":"Yes","no_of_people":"2","rent_amount":"5000","advance":"4000","maintenance":"100","water_bill":"200","other_expenses":"500","electricity":"300","policy_fix_sqr":"","created_date":"2017-03-27 22:11:28","weightage":"","property_image":"","description":"","status":"1","room_info":[{"id":"8","property_id":"2","room_no":"1","room_type":"4","vacant":"2","amount":"2000","lat_bath":"Yes","image":"","rating_status":"0","created_date":"2017-03-27 22:13:52"},{"id":"9","property_id":"2","room_no":"2","room_type":"4","vacant":"2","amount":"1200","lat_bath":"Yes","image":"","rating_status":"0","created_date":"2017-03-27 22:13:52"},{"id":"10","property_id":"2","room_no":"3","room_type":"4","vacant":"3","amount":"1300","lat_bath":"Yes","image":"","rating_status":"0","created_date":"2017-03-27 22:13:53"},{"id":"11","property_id":"2","room_no":"4","room_type":"4","vacant":"3","amount":"1500","lat_bath":"No","image":"","rating_status":"0","created_date":"2017-03-27 22:13:53"},{"id":"12","property_id":"2","room_no":"5","room_type":"4","vacant":"4","amount":"1800","lat_bath":"Yes","image":"","rating_status":"0","created_date":"2017-03-27 22:13:53"}],"property_images":[{"id":"2","property_id":"2","user_id":"14","image_number":"1","path":"image\/user\/photo\/1490677900Android trainee grp 20170125_222421.jpg","type":null,"createdOn":"2017-03-27 22:11:40","status":"1"}]}]}

                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("status");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true"))
                {
                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i =0; i <jsonarray.length(); i++)
                    {
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        //  Bookedpropertykeyid
                        Bookedpropertykeyid=jsonobj.getString("key_property_information");

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

            if (result.equalsIgnoreCase("true"))
            {

            } else if (result.equals("false")) {

                Toast.makeText(PaymentActivtyTanent_FromListview.this,message, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class MonthNameJsonParse extends AsyncTask<String, String, String> {

        String name,result,message;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            month_loader.startAnimation(rotation);
            month_loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... args) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                arraymonth=new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action","getMonthPaymentList"));
                nameValuePairs.add(new BasicNameValuePair("property_id",tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("head",month));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",tenantIdValue));
                nameValuePairs.add(new BasicNameValuePair("month_id",monthid_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain =new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i <jsonarray.length(); i++)
                    {
                        JSONObject jsonobj =jsonarray.getJSONObject(i);

                        monthModel = new OwnerStudentNameModel();
                        monthModel.setMonthname(jsonobj.getString("month_name"));
                        monthModel.setRemaining_amount(jsonobj.getString("remaining_amount"));
                        arraymonth.add(monthModel);
                    }
                }
            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            month_loader.clearAnimation();
            month_loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {

                adapter_data_month = new OwnerMonthNameAdapter(PaymentActivtyTanent_FromListview.this,arraymonth);
                month_list.setAdapter(adapter_data_month);

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(PaymentActivtyTanent_FromListview.this,"Month Not Found", Toast.LENGTH_LONG).show();
            }

        }
    }

}
