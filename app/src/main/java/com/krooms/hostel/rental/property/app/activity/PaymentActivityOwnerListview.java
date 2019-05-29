package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
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
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.OwnerMonthNameAdapter;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingOwnerActivity_FromListView;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 11/17/2016.
 */
public class PaymentActivityOwnerListview extends Activity {

    private TextView termAndCondition;
    private Button bookProperty;
    private RadioButton caseOption, onlineOption, chequeOption;
    private CheckBox checkeBox;
    RelativeLayout back_button;
    String paymentoptionvalue = "";
    ImageView student_loader;
    public static String advanceamount, keypropertyid = "";
    SharedStorage mSharedStorage;
    String mHireDate = " ", mLeaveDate = " ";
    String rentAmount;
    Integer randomNum;
    String paymentOption = "";
    EditText totalAmount_output;
    int noOfBed = 1;
    String roomSelectedBed = "";
    String roomIds = "";
    PropertyModal mPropertyModal;
    private Common mCommon;
    Animation rotation;
    LinearLayout layoutothermonth;
    Dialog complaintype;
    TextView txtmonthname;
    ArrayList<OwnerStudentNameModel> arraymonth;
    OwnerStudentNameModel monthModel;
    OwnerMonthNameAdapter adapter_data_month;
    ListView month_list;
    ImageView month_loader;
    String monthname;
    String totalremainingamount;
    EditText chequeno;
    String paymode = "";

    public static String monthlypayment = "", month_date = "", trandate = "", yearvalue = "",
            totalAmount_value = "", currentdate_value = "", ele_currentdate_value = "", currentdate_value1 = "",
            name = "", month = "", tanentusrid_value = "", tanentroomid_value = "",
            monthid_value = "", tanentownerid_value = "",
            tanentpropertyid_value = "", tanatidV = "", roomnovalue = "", keyid = "", Bookedpropertykeyid = "", paymentcomment = "", type = "", typeadvance = "";
    Intent in;
    int dueamountvalue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentowneractivity_fromlistview);

        layoutothermonth = (LinearLayout) findViewById(R.id.monthnameouterlay);
        txtmonthname = (TextView) findViewById(R.id.txtmonth);
        txtmonthname.setText("Select Month");
        mSharedStorage = SharedStorage.getInstance(this);
        in = getIntent();

        type = in.getStringExtra("type");

        if (type.equalsIgnoreCase("1")) {

            tanentownerid_value = "0";
            tanentroomid_value = in.getStringExtra("tanentroomid");
            month_date = "0";
            dueamountvalue = Integer.parseInt(in.getStringExtra("dueamountvalue"));
            //Log.d("due value", "" + dueamountvalue);
            month = in.getStringExtra("month");
            tanentusrid_value = in.getStringExtra("tanentuserid");
            //tanentroomid_value = in.getStringExtra("tanentroomid");
            monthid_value = in.getStringExtra("monthid");
            tanentpropertyid_value = in.getStringExtra("tanentpropertyid");
            //tanentownerid_value = in.getStringExtra("tanentownerid");
            tanatidV = in.getStringExtra("tanantid");
            monthlypayment = in.getStringExtra("monthlypayment");
            yearvalue = in.getStringExtra("yearvalue");
            trandate = in.getStringExtra("tranDate");
            //month_date = in.getStringExtra("month_date");

        } else {

            dueamountvalue = Integer.parseInt(in.getStringExtra("dueamountvalue"));
            Log.d("due value", "" + dueamountvalue);
            month = in.getStringExtra("month");
            tanentusrid_value = in.getStringExtra("tanentuserid");
            tanentroomid_value = in.getStringExtra("tanentroomid");
            monthid_value = in.getStringExtra("monthid");
            tanentpropertyid_value = in.getStringExtra("tanentpropertyid");
            tanentownerid_value = in.getStringExtra("tanentownerid");
            tanatidV = in.getStringExtra("tanantid");
            monthlypayment = in.getStringExtra("monthlypayment");
            yearvalue = in.getStringExtra("yearvalue");
            trandate = in.getStringExtra("tranDate");
            month_date = in.getStringExtra("month_date");

        }
        Log.d("month val newnn", "" + month_date);
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        final int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        ele_currentdate_value = yearO + "-" + (monthO + 1) + "-" + dateO;
        currentdate_value1 = dateO + "/" + (monthO + 1) + "/" + yearO;

        String electric_check = PaymentActivityOwnerListview.month;


        if (monthid_value.equalsIgnoreCase("13")) {

            layoutothermonth.setVisibility(View.VISIBLE);
            //Calldailog();
        }

        if (monthid_value.equalsIgnoreCase("14")) {

            layoutothermonth.setVisibility(View.VISIBLE);
            //Calldailog();
        }

        if (monthid_value.equalsIgnoreCase("15")) {

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

        new AdvanceAmountGet().execute();
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        student_loader = (ImageView) findViewById(R.id.student_loader);
        totalAmount_output = (EditText) findViewById(R.id.totalAmount_output);
        termAndCondition = (TextView) findViewById(R.id.termsAndConditionTextView);
        bookProperty = (Button) findViewById(R.id.submitBtn);
        bookProperty.setClickable(true);
        caseOption = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
        onlineOption = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
        chequeOption = (RadioButton) findViewById(R.id.chequePaymentRadioBtn);
        checkeBox = (CheckBox) findViewById(R.id.checkeBox);
        chequeno = (EditText) findViewById(R.id.txt_chequeno);
        //layout_cheque_details=(LinearLayout)findViewById(R.id.cheque_details);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        new Bookedpropertykeyid().execute();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.paymentRadioGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d("payment 1", "in group");
                if (caseOption.isChecked()) {
                    paymentoptionvalue = "Cash";

                } else if (onlineOption.isChecked()) {
                    paymentoptionvalue = "Online";

                } else if (chequeOption.isChecked()) {
                    // layout_cheque_details.setVisibility(View.VISIBLE);
                    paymentoptionvalue = "Cheque";
                }

            }
        });

        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PaymentActivityOwnerListview.this);
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

                // bookProperty.setEnabled(false);
                Log.d("payment 2", "in button" + mSharedStorage.getUserId() + "" + mSharedStorage.getBookedPropertyId());
                totalAmount_value = totalAmount_output.getText().toString();
                String monthname1 = txtmonthname.getText().toString();
                paymentcomment = chequeno.getText().toString().trim();
                typeadvance = "";
                if (monthid_value.equals("0")) {

                    if (totalAmount_value.equals("")) {
                        Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount ", Toast.LENGTH_SHORT).show();
                    } else if (totalAmount_value.startsWith("0")) {

                        Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                    } else if (paymentoptionvalue.equals("")) {
                        Toast.makeText(PaymentActivityOwnerListview.this, "Please Select Mode ", Toast.LENGTH_SHORT).show();
                    } else if (!checkeBox.isChecked()) {
                        Toast.makeText(PaymentActivityOwnerListview.this, "Please Accept Terms And Condition ", Toast.LENGTH_SHORT).show();
                    } else {
                        //data main
                        if (paymentoptionvalue.equalsIgnoreCase("Cash")) {
                            paymode = "C";
                            int monthidvaluemain = Integer.parseInt(monthid_value);

                            // bookProperty.setEnabled(false);
                            bookProperty.setClickable(false);
                            new AdvanceAmountUpdatePayment(paymentcomment, paymode).execute();

                        }
                        //cheque payment
                        else
                            //payment through cheque
                            if (paymentoptionvalue.equalsIgnoreCase("Cheque")) {
                                paymode = "Cheque";
                                int monthidvaluemain = Integer.parseInt(monthid_value);
                                // bookProperty.setEnabled(false);
                                bookProperty.setClickable(false);
                                new AdvanceAmountUpdatePayment(paymentcomment, paymode).execute();

                            } else {

                                Toast.makeText(PaymentActivityOwnerListview.this, "Edit Adv Online", Toast.LENGTH_SHORT).show();
                                typeadvance = "Adv";
                                bookProperty.setClickable(false);

                                Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                                Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                startActivity(intent);
                                //....
                            }

                    } // end advance payment update

                } else {  //month id not equal to 0
                    //
                    if (layoutothermonth.getVisibility() == View.VISIBLE) {

                        if (monthname1.equalsIgnoreCase("Select Month")) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Select Month ", Toast.LENGTH_SHORT).show();

                        } else if (totalAmount_value.equals("")) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount ", Toast.LENGTH_SHORT).show();
                        } else if (totalAmount_value.startsWith("0")) {

                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                        } else if (Integer.parseInt(totalAmount_value) > dueamountvalue) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount Lessthen Due Amount ", Toast.LENGTH_SHORT).show();

                        } else if (paymentoptionvalue.equals("")) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Select Mode ", Toast.LENGTH_SHORT).show();
                        } else if (!checkeBox.isChecked()) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Accept Terms And Condition ", Toast.LENGTH_SHORT).show();
                        } else {
                            //data main
                            if (paymentoptionvalue.equalsIgnoreCase("Cash")) {

                                paymode = "C";
                                int monthidvaluemain = Integer.parseInt(monthid_value);
                                if (monthidvaluemain > 12) {

                                    if (month.equalsIgnoreCase("Electricity")) {
                                        month = monthname1;
                                    } else {
                                        month = monthname1;
                                    }

                                    int totalremain = Integer.parseInt(totalremainingamount);
                                    int userenteramount = Integer.parseInt(totalAmount_value);
                                    if (totalremain < userenteramount) {
                                        Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        bookProperty.setClickable(false);
                                        bookProperty.setEnabled(false);
                                        new FineOwnerpaymentJSONParse(month, paymentcomment, paymode).execute();
                                    }

                                } else {
                                    bookProperty.setClickable(false);
                                    bookProperty.setEnabled(false);
                                    new PaymentActivityOwnerJSONParse(paymentcomment, paymode).execute();
                                }

                                Log.d("payment 2", "in cash mode");
                            } else
                                //payment through cheque
                                if (paymentoptionvalue.equalsIgnoreCase("Cheque")) {

                                    paymode = "Cheque";
                                    if (type.equalsIgnoreCase("1")) {

                                    } else {

                                        int monthidvaluemain = Integer.parseInt(monthid_value);
                                        if (monthidvaluemain > 12) {

                                            if (month.equalsIgnoreCase("Electricity")) {
                                                month = monthname1;
                                            } else {
                                                month = monthname1;
                                            }

                                            int totalremain = Integer.parseInt(totalremainingamount);
                                            int userenteramount = Integer.parseInt(totalAmount_value);
                                            if (totalremain < userenteramount) {
                                                Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                bookProperty.setClickable(false);
                                                bookProperty.setEnabled(false);
                                                new FineOwnerpaymentJSONParse(month, paymentcomment, paymode).execute();
                                            }
                                        } else {
                                            bookProperty.setClickable(false);
                                            bookProperty.setEnabled(false);
                                            new PaymentActivityOwnerJSONParse(paymentcomment, paymode).execute();
                                        }
                                    }
                                    Log.d("payment 2", "in cash mode");
                                }


                                //
                                else {
                                    //online payment
                                    //ashish changes
                                    int monthidvaluemain = Integer.parseInt(monthid_value);
                                    if (monthidvaluemain > 12) {
                                        if (month.equalsIgnoreCase("Electricity")) {
                                            month = monthname1;
                                        } else {
                                            month = monthname1;
                                        }
                                        int totalremain = Integer.parseInt(totalremainingamount);
                                        int userenteramount = Integer.parseInt(totalAmount_value);
                                        if (totalremain < userenteramount) {
                                            bookProperty.setEnabled(true);
                                            bookProperty.setClickable(true);
                                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            bookProperty.setClickable(false);
                                            Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                                            Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                            intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                            intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                            intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                            intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                            intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                            intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                            intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                            intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                            startActivity(intent);
                                        }
                                        // new FineOwnerpaymentJSONParse(month).execute();
                                    } else {
                                        bookProperty.setClickable(false);
                                        // bookProperty.setEnabled(false);
                                        //  new PaymentActivityOwnerJSONParse().execute();
                                        Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                                        Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                        intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                        intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                        intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                        intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                        intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                        intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                        intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                        intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                        startActivity(intent);
                                    }
                                }
                            //data end by mode
                        }
                    } else {
                        if (totalAmount_value.equals("")) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount ", Toast.LENGTH_SHORT).show();
                        } else if (totalAmount_value.startsWith("0")) {

                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                        } else if (Integer.parseInt(totalAmount_value) > dueamountvalue) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Amount Lessthen Due Amount ", Toast.LENGTH_SHORT).show();

                        } else if (paymentoptionvalue.equals("")) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Select Mode ", Toast.LENGTH_SHORT).show();
                        } else if (!checkeBox.isChecked()) {
                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Accept Terms And Condition ", Toast.LENGTH_SHORT).show();
                        } else {
                            //data main
                            if (paymentoptionvalue.equalsIgnoreCase("Cash")) {

                                paymode = "C";

                                if (type.equalsIgnoreCase("1")) {
                                    bookProperty.setClickable(false);
                                    bookProperty.setEnabled(false);
                                    new ElectricityPaymentOwnerJSONParse(paymentcomment, paymode).execute();
                                } else {

                                    int monthidvaluemain = Integer.parseInt(monthid_value);
                                    if (monthidvaluemain > 12) {

                                        if (month.equalsIgnoreCase("Electricity")) {
                                            month = monthname1;
                                        } else {
                                            month = monthname1;
                                        }

                                        int totalremain = Integer.parseInt(totalremainingamount);
                                        int userenteramount = Integer.parseInt(totalAmount_value);
                                        if (totalremain < userenteramount) {
                                            bookProperty.setClickable(true);
                                            bookProperty.setEnabled(true);
                                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            bookProperty.setClickable(false);
                                            bookProperty.setEnabled(false);
                                            new FineOwnerpaymentJSONParse(month, paymentcomment, paymode).execute();
                                        }

                                    } else {
                                        bookProperty.setClickable(false);
                                        bookProperty.setEnabled(false);
                                        new PaymentActivityOwnerJSONParse(paymentcomment, paymode).execute();
                                    }

                                    Log.d("payment 2", "in cash mode");
                                }
                            }
                            //cheque payment

                            else if (paymentoptionvalue.equalsIgnoreCase("Cheque")) {

                                paymode = "Cheque";

                                if (type.equalsIgnoreCase("1")) {
                                    bookProperty.setClickable(false);
                                    bookProperty.setEnabled(false);
                                    new ElectricityPaymentOwnerJSONParse(paymentcomment, paymode).execute();

                                } else {

                                    int monthidvaluemain = Integer.parseInt(monthid_value);
                                    if (monthidvaluemain > 12) {

                                        if (month.equalsIgnoreCase("Electricity")) {
                                            month = monthname1;
                                        } else {
                                            month = monthname1;
                                        }

                                        int totalremain = Integer.parseInt(totalremainingamount);
                                        int userenteramount = Integer.parseInt(totalAmount_value);
                                        if (totalremain < userenteramount) {
                                            bookProperty.setClickable(true);
                                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            bookProperty.setClickable(false);
                                            bookProperty.setEnabled(false);
                                            new FineOwnerpaymentJSONParse(month, paymentcomment, paymode).execute();
                                        }
                                    } else {
                                        bookProperty.setClickable(false);
                                        bookProperty.setEnabled(false);
                                        new PaymentActivityOwnerJSONParse(paymentcomment, paymode).execute();
                                    }
                                }
                            } else {
                                if (type.equalsIgnoreCase("1")) {
                                    //online payment
                                    int monthidvaluemain = Integer.parseInt(monthid_value);
                                    if (monthidvaluemain > 12) {

                                        if (month.equalsIgnoreCase("Electricity")) {
                                            month = monthname1;
                                        } else {
                                            month = monthname1;
                                        }

                                        int totalremain = Integer.parseInt(totalremainingamount);
                                        int userenteramount = Integer.parseInt(totalAmount_value);
                                        if (totalremain < userenteramount) {
                                            bookProperty.setClickable(true);
                                            bookProperty.setEnabled(true);
                                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            bookProperty.setClickable(false);
                                            bookProperty.setEnabled(false);
                                            Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                                            Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                            intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                            intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                            intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                            intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                            intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                            intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                            intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                            intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                            startActivity(intent);
                                            //....
                                        }
                                        // new FineOwnerpaymentJSONParse(month).execute();
                                    } else {
                                        bookProperty.setClickable(false);
                                        // bookProperty.setEnabled(false);
                                        //  new PaymentActivityOwnerJSONParse().execute();
                                        Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                                        Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                        intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                        intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                        intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                        intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                        intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                        intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                        intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                        intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                        startActivity(intent);
                                        //....
                                    }

                                } else {

                                    //online payment
                                    int monthidvaluemain = Integer.parseInt(monthid_value);
                                    if (monthidvaluemain > 12) {

                                        if (month.equalsIgnoreCase("Electricity")) {
                                            month = monthname1;
                                        } else {
                                            month = monthname1;
                                        }

                                        int totalremain = Integer.parseInt(totalremainingamount);
                                        int userenteramount = Integer.parseInt(totalAmount_value);
                                        if (totalremain < userenteramount) {
                                            bookProperty.setClickable(true);
                                            bookProperty.setEnabled(true);
                                            Toast.makeText(PaymentActivityOwnerListview.this, "Please Enter Valid Amount ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            bookProperty.setClickable(false);
                                            bookProperty.setEnabled(false);
                                            Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                                            Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                            intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                            intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                            intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                            intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                            intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                            intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                            intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                            intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                            startActivity(intent);
                                            //....
                                        }
                                    } else {
                                        bookProperty.setClickable(false);
                                        Intent intent = new Intent(PaymentActivityOwnerListview.this, BillingShippingOwnerActivity_FromListView.class);
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
                                        intent.putExtra(AvenuesParams.HIRING_DATE, tanatidV);
                                        intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                        intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                        intent.putExtra(AvenuesParams.TENANT_ID, tanatidV);
                                        intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                                        intent.putExtra(AvenuesParams.MONTH, monthid_value);
                                        intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                                        intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                                        startActivity(intent);
                                        //....
                                    }
                                }

                            }
                        }

                    }
                }

            }

        });


    }

    public void getFineheadDialog() {
        complaintype = new Dialog(PaymentActivityOwnerListview.this);
        complaintype.requestWindowFeature(Window.FEATURE_NO_TITLE);
        complaintype.setContentView(R.layout.custom_dialog_country);
        month_list = (ListView) complaintype.findViewById(R.id.list_country);
        month_loader = (ImageView) complaintype.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) complaintype.findViewById(R.id.country_cross_layout);

        if (NetworkConnection.isConnected(PaymentActivityOwnerListview.this)) {
            new MonthNameJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
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
                String month_value = arraymonth.get(position).getMonthname().toString();
                totalremainingamount = arraymonth.get(position).getRemaining_amount().toString();
                txtmonthname.setText(month_value);
                complaintype.dismiss();
            }
        });

        complaintype.show();
    }


    private class FineOwnerpaymentJSONParse extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String paymentcomment = "", paymode = "";

        public FineOwnerpaymentJSONParse(String monthname, String paymentcomment, String paymode) {

            this.paymentcomment = paymentcomment;
            this.paymode = paymode;
        }

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

                System.out.print("monthid" + PaymentActivityOwnerListview.monthid_value + "currentdate" + PaymentActivityOwnerListview.currentdate_value + "owner id" + mSharedStorage.getUserId() + "user id" + PaymentActivityOwnerListview.tanentusrid_value + "month" + PaymentActivityOwnerListview.month + "amount" + PaymentActivityOwnerListview.totalAmount_value + "roomid" + PaymentActivityOwnerListview.tanentroomid_value + "property id" + mSharedStorage.getBookedPropertyId());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "otherfinepayment"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", PaymentActivityOwnerListview.tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", PaymentActivityOwnerListview.tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", ListViewActivity.user_id_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", monthid_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwnerListview.month_date));
                // nameValuePairs.add(new BasicNameValuePair("month_name", PaymentActivityOwnerListview.month));//old
                nameValuePairs.add(new BasicNameValuePair("month_name", month));
                nameValuePairs.add(new BasicNameValuePair("amount", PaymentActivityOwnerListview.totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", PaymentActivityOwnerListview.tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", PaymentActivityOwnerListview.tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", PaymentActivityOwnerListview.currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("year", PaymentActivityOwnerListview.yearvalue));
                nameValuePairs.add(new BasicNameValuePair("room_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", PaymentActivityOwnerListview.monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", paymode));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", paymentcomment));//paymentcomment
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
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);
            bookProperty.setClickable(true);

            if (result.equalsIgnoreCase("Y")) {

                final int a = 1;
                Successdialog(a, paymode);
                Toast.makeText(PaymentActivityOwnerListview.this, "Payment is successfull", Toast.LENGTH_LONG).show();

            } else if (result.equals("N")) {
                Toast.makeText(PaymentActivityOwnerListview.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class PaymentActivityOwnerJSONParse extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String paymentcomment = "", paymode = "";

        public PaymentActivityOwnerJSONParse(String paymentcomment, String paymode) {

            this.paymentcomment = paymentcomment;
            this.paymode = paymode;
        }

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
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "updatepaidamount"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", ListViewActivity.user_id_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", monthid_value));
                nameValuePairs.add(new BasicNameValuePair("month_name", month));
                nameValuePairs.add(new BasicNameValuePair("amount", totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("year", yearvalue));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("room_amount", monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", advanceamount));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", paymode));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", paymentcomment));
                Log.d("uu 2", "" + nameValuePairs);
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
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);

            bookProperty.setClickable(true);

            if (result.equalsIgnoreCase("Y")) {
                bookProperty.setEnabled(true);
                final int a = 0;
                Successdialog(a, paymode);
                Toast.makeText(PaymentActivityOwnerListview.this, "Payment is successfull", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(PaymentActivityOwnerListview.this, "Not Successful", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Successdialog(final int a, final String paymode) {

        final Dialog successdialog = new Dialog(PaymentActivityOwnerListview.this);
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
        TextView propertyname = (TextView) successdialog.findViewById(R.id.propertyname);
        TextView userroomno = (TextView) successdialog.findViewById(R.id.userroomno);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                successdialog.dismiss();
                Intent i = new Intent(PaymentActivityOwnerListview.this, PDFFileNewPage_fromlistpayment.class);
                i.putExtra("name", TenantDetailActivity.tanentfinalname);
                i.putExtra("date", currentdate_value1);
                if (a == 1) {

                    if (type.equalsIgnoreCase("1")) {
                        i.putExtra("month", "Elc-" + month);
                    } else {
                        i.putExtra("month", month);
                    }
                } else {
                    if (type.equalsIgnoreCase("1")) {
                        i.putExtra("month", "Elc-" + month);
                    } else {
                        i.putExtra("month", month);
                    }
                }
                i.putExtra("p_name", ListViewActivity.property_name);
                i.putExtra("uid", tanentusrid_value);
                i.putExtra("amount", totalAmount_value);
                i.putExtra("roomno", roomnovalue);
                i.putExtra("keyid", keyid);
                i.putExtra("propertyid", keypropertyid);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                if (paymode.equalsIgnoreCase("C")) {
                    i.putExtra("mode", "Cash");
                } else {
                    i.putExtra("mode", "Cheque");
                }
                i.putExtra("Comment", paymentcomment);
                startActivity(i);
            }
        });
        username.setText(TenantDetailActivity.tanentfinalname);
        userdate.setText(currentdate_value1);
        //paymentmonth.setText(month);

        if (a == 1) {

            if (type.equalsIgnoreCase("1")) {
                paymentmonth.setText("Elc-" + month);
            } else {
                paymentmonth.setText(month);
            }
        } else {

            if (type.equalsIgnoreCase("1")) {
                paymentmonth.setText("Elc-" + month);
            } else {
                paymentmonth.setText(month);
            }

        }
        userpaid.setText(totalAmount_value);
        userpid.setText(keypropertyid);
        usertid.setText(keyid);
        userroomno.setText(roomnovalue);
        propertyname.setText(mSharedStorage.getPropertyname());

        if (paymode.equalsIgnoreCase("C")) {
            usermode.setText("Cash");
        } else {
            usermode.setText("Cheque");
        }

        chequeno.setText(paymentcomment);
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                successdialog.dismiss();
                Intent i = new Intent(PaymentActivityOwnerListview.this, ListViewActivity.class);
                i.putExtra("user_name", TenantDetailActivity.tanentfinalname);
                i.putExtra("user_id_data", tanentusrid_value);
                i.putExtra("tid", tanatidV);
                i.putExtra("property_id_data", tanentpropertyid_value);
                i.putExtra("property_name", "");
                startActivity(i);

            }
        });

        successdialog.show();
    }

    //Advance Amount Update on room no chnages:
    private class AdvanceAmountUpdatePayment extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String paymentcomment = "", paymode = "";

        public AdvanceAmountUpdatePayment(String paymentcomment, String paymode) {

            this.paymentcomment = paymentcomment;
            this.paymode = paymode;
        }

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
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "advanceamountupdateroomchange"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", ListViewActivity.user_id_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", monthid_value));
                nameValuePairs.add(new BasicNameValuePair("month_name", month));
                nameValuePairs.add(new BasicNameValuePair("amount", totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("year", yearvalue));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("room_amount", monthlypayment));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", advanceamount));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", paymode));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", paymentcomment));
                Log.d("uu 2", "" + nameValuePairs);
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
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);

            if (result.equalsIgnoreCase("Y")) {
                bookProperty.setEnabled(true);
                final int a = 0;
                Successdialogadv(a, paymode);
                Toast.makeText(PaymentActivityOwnerListview.this, "Payment is successfull", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(PaymentActivityOwnerListview.this, "Not Successful", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Successdialogadv(final int a, final String paymode) {

        final Dialog successdialog = new Dialog(PaymentActivityOwnerListview.this);
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
        TextView propertyname = (TextView) successdialog.findViewById(R.id.propertyname);
        TextView userroomno = (TextView) successdialog.findViewById(R.id.userroomno);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                successdialog.dismiss();
                Intent i = new Intent(PaymentActivityOwnerListview.this, PDFFileNewPage_fromlistpayment.class);
                i.putExtra("name", TenantDetailActivity.tanentfinalname);
                i.putExtra("date", currentdate_value1);
                if (a == 1) {

                    if (type.equalsIgnoreCase("1")) {
                        i.putExtra("month", "Elc-" + month);
                    } else {
                        i.putExtra("month", month);
                    }
                } else {
                    if (type.equalsIgnoreCase("1")) {
                        i.putExtra("month", "Elc-" + month);
                    } else {
                        i.putExtra("month", month);
                    }
                }
                i.putExtra("p_name", ListViewActivity.property_name);
                i.putExtra("uid", tanentusrid_value);
                i.putExtra("amount", totalAmount_value);
                i.putExtra("roomno", roomnovalue);
                i.putExtra("keyid", keyid);
                i.putExtra("propertyid", keypropertyid);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                if (paymode.equalsIgnoreCase("C")) {
                    i.putExtra("mode", "Cash");
                } else {
                    i.putExtra("mode", "Cheque");
                }
                i.putExtra("Comment", paymentcomment);
                startActivity(i);
            }
        });
        username.setText(TenantDetailActivity.tanentfinalname);
        userdate.setText(currentdate_value1);
        //paymentmonth.setText(month);

        if (a == 1) {

            if (type.equalsIgnoreCase("1")) {
                paymentmonth.setText("Elc-" + month);
            } else {
                paymentmonth.setText(month);
            }
        } else {

            if (type.equalsIgnoreCase("1")) {
                paymentmonth.setText("Elc-" + month);
            } else {
                paymentmonth.setText(month);
            }

        }
        userpaid.setText(totalAmount_value);
        userpid.setText(keypropertyid);
        usertid.setText(keyid);
        userroomno.setText(roomnovalue);
        propertyname.setText(mSharedStorage.getPropertyname());

        if (paymode.equalsIgnoreCase("C")) {
            usermode.setText("Cash");
        } else {
            usermode.setText("Cheque");
        }

        chequeno.setText(paymentcomment);
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                successdialog.dismiss();
                Intent i = new Intent(PaymentActivityOwnerListview.this, ListViewActivity.class);
                i.putExtra("user_name", TenantDetailActivity.tanentfinalname);
                i.putExtra("user_id_data", tanentusrid_value);
                i.putExtra("tid", tanatidV);
                i.putExtra("property_id_data", tanentpropertyid_value);
                i.putExtra("property_name", "");
                startActivity(i);
                finish();

            }
        });

        successdialog.show();
    }


    //

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
            table1.addCell(createValueCell(currentdate_value));

            table1.addCell(createValueCell("Name"));
            table1.addCell(createValueCell(TenantDetailActivity.tanentfinalname));

            table1.addCell(createValueCell("Pay For"));
            table1.addCell(createValueCell(month));

            table1.addCell(createValueCell("Paid"));
            table1.addCell(createValueCell(totalAmount_value));

            table1.addCell(createValueCell("Transaction Mode"));
            table1.addCell(createValueCell("Cash"));
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


    public boolean Validation() {
        boolean valid = true;

        String totalAmount_value = totalAmount_output.getText().toString();
        if (totalAmount_value.equals("")) {
            totalAmount_output.setError("Enter Amount");
            valid = false;
        } else {
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

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "getadvanceamount"));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));
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
                        advanceamount = jsonarray.getJSONObject(i).getString("advance");
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

                    new GetUserKeyDetail().execute();
                } else if (result.equals("N")) {
//                    Toast.makeText(TenantDetailActivity.this,"No data found", Toast.LENGTH_LONG).show();
                }
            } else {
//                Toast.makeText(TenantDetailActivity.this,"please check network connection", Toast.LENGTH_LONG).show();
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
                nameValuePairs.add(new BasicNameValuePair("action", "getuserkeydetails"));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanatidV));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));

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
                        roomnovalue = jsonarray.getJSONObject(i).getString("room_no");
                        keyid = jsonarray.getJSONObject(i).getString("tenant_id");
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
            if (result.equalsIgnoreCase("Y")) {
                new PropertyIdKeyParser().execute();

            } else {
            }
        }
    }

    private class PropertyIdKeyParser extends AsyncTask<String, String, String> {

        int count;
        String name, result;
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

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                Log.d("prop 3", tanentpropertyid_value);

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                if (result.equals("true")) {
                    JSONArray jArray = jsmain.getJSONArray(WebUrls.DATA_JSON);
                    keypropertyid = jArray.getJSONObject(0).getString("key_property_information");
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
                nameValuePairs.add(new BasicNameValuePair("action", "propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
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

                Toast.makeText(PaymentActivityOwnerListview.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MonthNameJsonParse extends AsyncTask<String, String, String> {

        String name, result, message;
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
                arraymonth = new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getMonthPaymentList"));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("head", month));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanatidV));
                nameValuePairs.add(new BasicNameValuePair("month_id", monthid_value));
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
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);

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

                adapter_data_month = new OwnerMonthNameAdapter(PaymentActivityOwnerListview.this, arraymonth);
                month_list.setAdapter(adapter_data_month);

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(PaymentActivityOwnerListview.this, "Month Not Found", Toast.LENGTH_LONG).show();
            }

        }
    }

    //send electricity payment
    private class ElectricityPaymentOwnerJSONParse extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String paymentcomment = "", paymode = "";

        public ElectricityPaymentOwnerJSONParse(String paymentcomment, String paymode) {

            this.paymentcomment = paymentcomment;
            this.paymode = paymode;
        }

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
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "electricitypaidamount"));
                //nameValuePairs.add(new BasicNameValuePair("owner_id", tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanatidV));
                nameValuePairs.add(new BasicNameValuePair("user_id", ListViewActivity.user_id_value));
                nameValuePairs.add(new BasicNameValuePair("month_id", monthid_value));
                nameValuePairs.add(new BasicNameValuePair("month_name", month));
                nameValuePairs.add(new BasicNameValuePair("amount", totalAmount_value));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("year", yearvalue));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("current_date", ele_currentdate_value));
                //nameValuePairs.add(new BasicNameValuePair("room_amount", monthlypayment));
                // nameValuePairs.add(new BasicNameValuePair("advance_amount", advanceamount));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", paymode));
                nameValuePairs.add(new BasicNameValuePair("comment", paymentcomment));
                Log.d("uu 2", "" + nameValuePairs);
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
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);

            bookProperty.setClickable(true);
            if (result.equalsIgnoreCase("Y")) {
                bookProperty.setEnabled(true);
                final int a = 0;
                Successdialog(a, paymode);
                Toast.makeText(PaymentActivityOwnerListview.this, "Payment is successfull", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(PaymentActivityOwnerListview.this, "Not Successful", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent i = new Intent(PaymentActivityOwnerListview.this, ListViewActivity.class);
                i.putExtra("user_name", TenantDetailActivity.tanentfinalname);
                i.putExtra("user_id_data", tanentusrid_value);
                i.putExtra("tid", tanatidV);
                i.putExtra("property_id_data", tanentpropertyid_value);
                i.putExtra("property_name", "");
                startActivity(i);
                finish();

                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
