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
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingOwnerActivity;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;

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
 * Created by admin on 11/14/2016.
 */
public class ExitTenanPaymentActivityOwner extends Activity {
    Dialog successdialog;
    private TextView totalAmount;
    private TextView termAndCondition;
    public static String amountroom;
    private Button bookProperty;
    public static String advanceamount;
    private RadioButton caseOption, onlineOption;
    private CheckBox checkeBox;
    RelativeLayout back_button;
    String paymentoptionvalue = "";
    ImageView student_loader;
    String result = null;
    SharedStorage mSharedStorage;
    Integer randomNum;
    public static String name, payment, month, tanentusrid_value, currentdate_value, currentdate_value1, tanentroomid_value,
            monthid_value, tanentownerid_value, tanentpropertyid_value,
            tanentidValue, yearvaluenew;

    Animation rotation;
    Intent in;
    RadioGroup group;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_owner_actvity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        // get shared perference
        mSharedStorage = SharedStorage.getInstance(this);
        in = getIntent();
        name = in.getStringExtra("tanentname");
        month = in.getStringExtra("month");
        payment = in.getStringExtra("payment");
        tanentusrid_value = in.getStringExtra("tanentusrid");
        tanentidValue = in.getStringExtra("tenantid");
        tanentroomid_value = in.getStringExtra("tanentroomid");
        monthid_value = in.getStringExtra("monthid");
        tanentpropertyid_value = in.getStringExtra("tanentpropertyid");
        tanentownerid_value = in.getStringExtra("tanentownerid");
        yearvaluenew = in.getStringExtra("yearv");
        //get current date time
        DatePicker datePicker = new DatePicker(ExitTenanPaymentActivityOwner.this);
        int date = datePicker.getMonth();
        int year = datePicker.getYear();
        int month = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value = yearO + "/" + (monthO + 1) + "/" + dateO;
        currentdate_value1 = dateO + "/" + (monthO + 1) + "/" + yearO;
        //.....
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        student_loader = (ImageView) findViewById(R.id.student_loader);
        totalAmount = (TextView) findViewById(R.id.totalAmount_output);
        termAndCondition = (TextView) findViewById(R.id.termsAndConditionTextView);
        bookProperty = (Button) findViewById(R.id.submitBtn);
        caseOption = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
        onlineOption = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
        checkeBox = (CheckBox) findViewById(R.id.checkeBox);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        group = (RadioGroup) findViewById(R.id.paymentRadioGroup);
        totalAmount.setText(payment);

        // back move
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent i = new Intent(ExitTenanPaymentActivityOwner.this, OwnerFromActivity.class);
                startActivity(i);
            }
        });

        //cash or payment option selection
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d("payment 1", "in group");
                if (caseOption.isChecked()) {
                    paymentoptionvalue = "Cash";

                } else if (onlineOption.isChecked()) {
                    paymentoptionvalue = "Online";
                }
            }
        });

        //apply terms and condition dialog
        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ExitTenanPaymentActivityOwner.this);
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

        // data submit
        bookProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("payment 2", "in button" + mSharedStorage.getUserId() + "" + mSharedStorage.getBookedPropertyId());
                String totalAmount_value = totalAmount.getText().toString();

                if (paymentoptionvalue.equals("")) {
                    Toast.makeText(ExitTenanPaymentActivityOwner.this, "please select mode ", Toast.LENGTH_SHORT).show();
                } else if (!checkeBox.isChecked()) {
                    Toast.makeText(ExitTenanPaymentActivityOwner.this, "please select Terms and Condition", Toast.LENGTH_SHORT).show();
                } else {
                    //data
                    if (paymentoptionvalue.equalsIgnoreCase("Cash")) {
                        new AdvanceAmountGet().execute();
                        Log.d("payment 2", "in cash mode");
                    } else {
                        Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                        Intent intent = new Intent(ExitTenanPaymentActivityOwner.this, BillingShippingOwnerActivity.class);
                        intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                        intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                        intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                        intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                        intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                        intent.putExtra(AvenuesParams.AMOUNT, totalAmount_value);
                        intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                        intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                        intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                        intent.putExtra(AvenuesParams.USER_ID, mSharedStorage.getUserId());
                        intent.putExtra(AvenuesParams.PROPERTY_ID, mSharedStorage.getBookedPropertyId());
                        intent.putExtra(AvenuesParams.ROOM_ID, tanentroomid_value);
                        intent.putExtra(AvenuesParams.ROOM_AMOUNT, "");
                        intent.putExtra(AvenuesParams.HIRING_DATE, "");
                        intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                        intent.putExtra(AvenuesParams.BOOKED_BED, "");
                        startActivity(intent);
                    }
                    //........
                }
            }
        });
    }


    private class PaymentActivityOwnerJSONParse extends AsyncTask<String, String, String> {


        int count;
        String name;
        private boolean IsError = false;
        String message;

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
            Log.d("uu 1", "" + post);
            try {

                System.out.print("monthid" + monthid_value + "currentdate" + currentdate_value + "owner id" + mSharedStorage.getUserId() + "user id" + tanentusrid_value + "month" + month + "amount" + payment + "roomid" + tanentroomid_value + "property id" + mSharedStorage.getBookedPropertyId());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "ownerpaidamount"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", tanentownerid_value));
                nameValuePairs.add(new BasicNameValuePair("user_id", tanentusrid_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanentidValue));
                nameValuePairs.add(new BasicNameValuePair("month_id", monthid_value));
                nameValuePairs.add(new BasicNameValuePair("month_name", month));
                nameValuePairs.add(new BasicNameValuePair("amount", payment));
                nameValuePairs.add(new BasicNameValuePair("room_id", tanentroomid_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", tanentpropertyid_value));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", advanceamount));
                nameValuePairs.add(new BasicNameValuePair("year", yearvaluenew));
                nameValuePairs.add(new BasicNameValuePair("room_amount", amountroom));
                nameValuePairs.add(new BasicNameValuePair("current_date", currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "C"));
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
                Successdialog();
                Toast.makeText(ExitTenanPaymentActivityOwner.this, "Payment is successfull", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(ExitTenanPaymentActivityOwner.this, "Payment is not success full", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void Successdialog() {
        successdialog = new Dialog(ExitTenanPaymentActivityOwner.this);
        successdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successdialog.setContentView(R.layout.success_dialogpayment_owner);
        successdialog.setCanceledOnTouchOutside(false);
        final CardView card_view = (CardView) successdialog.findViewById(R.id.card_view);
        TextView username = (TextView) successdialog.findViewById(R.id.username);
        TextView userdate = (TextView) successdialog.findViewById(R.id.userdate);
        TextView paymentmonth = (TextView) successdialog.findViewById(R.id.paymentmonth);
        TextView userpaid = (TextView) successdialog.findViewById(R.id.userpaid);
        TextView usermode = (TextView) successdialog.findViewById(R.id.usermode);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExitTenanPaymentActivityOwner.this, PDFFileNewPage_owner.class);
                i.putExtra("name", name);
                i.putExtra("date", currentdate_value1);
                i.putExtra("month", month);
                i.putExtra("uid", tanentusrid_value);
                i.putExtra("amount", payment);
                i.putExtra("mode", "Cash");
                startActivity(i);
            }
        });
        username.setText(name);
        userdate.setText(currentdate_value1);
        paymentmonth.setText(month);
        userpaid.setText(payment);
        usermode.setText("Cash");
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExitTenanPaymentActivityOwner.this, OwnerFromActivity.class);
                startActivity(i);
            }
        });

        successdialog.show();
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
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cellname4;
    }


    //pdf method end


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
                        amountroom = jsonarray.getJSONObject(i).getString("amount");
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
                    new PaymentActivityOwnerJSONParse().execute();
                } else if (result.equals("N")) {
                }
        }
    }
}
