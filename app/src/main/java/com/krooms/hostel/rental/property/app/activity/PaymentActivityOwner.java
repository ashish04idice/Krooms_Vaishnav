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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingOwnerActivity;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by admin on 11/14/2016.
 */
public class PaymentActivityOwner extends Activity {
    Dialog successdialog;
    private TextView totalAmount;
    private TextView termAndCondition;
    public static String amountroom;
    public static Button bookProperty;
    public static String advanceamount;
    private RadioButton caseOption, onlineOption, chequeOption;
    private CheckBox checkeBox;
    RelativeLayout back_button;
    LinearLayout layout_cheque_details;
    String paymentoptionvalue = "";
    ImageView student_loader;
    String result = null;
    SharedStorage mSharedStorage;
    Integer randomNum;
    EditText chequeno, bankname;
    String chequenovalue = "", banknamevalue = "", chequeNo = "";
    public static String name, payment, month, tanentusrid_value, currentdate_value, currentdate_value1, tanentroomid_value,
            monthid_value, tanentownerid_value, tanentpropertyid_value,
            tanentidValue, yearvaluenew, roomno, keyid, keypropertyid = "", paymentcomment = "";

    Animation rotation;
    Intent in;
    RadioGroup group;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_owner_actvity);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        mSharedStorage = SharedStorage.getInstance(this);
        in = getIntent();
        name = in.getStringExtra("tanentname");
        month = in.getStringExtra("month");
        payment = in.getStringExtra("payment");
        keyid = in.getStringExtra("keyid");
        tanentusrid_value = in.getStringExtra("tanentusrid");
        tanentidValue = in.getStringExtra("tenantid");
        tanentroomid_value = in.getStringExtra("tanentroomid");
        monthid_value = in.getStringExtra("monthid");
        tanentpropertyid_value = in.getStringExtra("tanentpropertyid");
        Log.d("property id2", tanentpropertyid_value);
        tanentownerid_value = in.getStringExtra("tanentownerid");
        yearvaluenew = in.getStringExtra("yearv");
        roomno = in.getStringExtra("roomno");
        Log.d("year..234569", "" + yearvaluenew);

        new AdvanceAmountGet().execute();
        new PropertyIdKeyParser().execute();
        //get current date time
        DatePicker datePicker = new DatePicker(PaymentActivityOwner.this);
        int date = datePicker.getMonth();
        int year = datePicker.getYear();
        int monthone = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        currentdate_value1 = dateO + "/" + (monthO + 1) + "/" + yearO;
        //.....
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        student_loader = (ImageView) findViewById(R.id.student_loader);
        totalAmount = (TextView) findViewById(R.id.totalAmount_output);
        termAndCondition = (TextView) findViewById(R.id.termsAndConditionTextView);
        bookProperty = (Button) findViewById(R.id.submitBtn);
        bookProperty.setClickable(true);
        caseOption = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
        onlineOption = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
        chequeOption = (RadioButton) findViewById(R.id.chequePaymentRadioBtn);
        checkeBox = (CheckBox) findViewById(R.id.checkeBox);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        layout_cheque_details = (LinearLayout) findViewById(R.id.cheque_details);
        chequeno = (EditText) findViewById(R.id.txt_chequeno);
        group = (RadioGroup) findViewById(R.id.paymentRadioGroup);
        totalAmount.setText(payment);
        // layout_cheque_details.setVisibility(View.GONE);

        // back move
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent i = new Intent(PaymentActivityOwner.this, OwnerFromActivity.class);
                startActivity(i);
            }
        });

        //cash or payment option selection
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d("payment 1", "in group");
                if (caseOption.isChecked()) {
                    // layout_cheque_details.setVisibility(View.GONE);
                    paymentoptionvalue = "Cash";

                } else if (onlineOption.isChecked()) {
                    //layout_cheque_details.setVisibility(View.GONE);
                    paymentoptionvalue = "Online";
                } else if (chequeOption.isChecked()) {
                    // layout_cheque_details.setVisibility(View.VISIBLE);
                    paymentoptionvalue = "Cheque";
                    // Toast.makeText(PaymentActivityOwner.this, "Cheque Option", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //apply terms and condition dialog
        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PaymentActivityOwner.this);
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

                if (totalAmount_value.equals("")) {
                    Toast.makeText(PaymentActivityOwner.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (totalAmount_value.startsWith("0")) {

                    Toast.makeText(PaymentActivityOwner.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                } else if (paymentoptionvalue.equals("")) {
                    Toast.makeText(PaymentActivityOwner.this, "please select mode ", Toast.LENGTH_SHORT).show();
                } else if (!checkeBox.isChecked()) {
                    Toast.makeText(PaymentActivityOwner.this, "please select Terms and Condition", Toast.LENGTH_SHORT).show();
                } else {
                    //data
                    if (paymentoptionvalue.equalsIgnoreCase("Cash")) {
                        bookProperty.setClickable(false);
                        paymentcomment = chequeno.getText().toString().trim();
                        new PaymentActivityOwnerJSONParse(paymentcomment).execute();
                    } else if (paymentoptionvalue.equalsIgnoreCase("Cheque")) {
                        bookProperty.setClickable(false);
                        chequenovalue = chequeno.getText().toString().trim();
                        paymentcomment = chequeno.getText().toString().trim();
                        new PaymentActivityOwnerThroughCheque(chequenovalue).execute();
                    } else {
                        bookProperty.setClickable(false);
                        paymentcomment = chequeno.getText().toString().trim();
                        Log.d("roomid oi", "" + tanentroomid_value + "owner id" + tanentownerid_value + "Property id" + tanentpropertyid_value);
                        Intent intent = new Intent(PaymentActivityOwner.this, BillingShippingOwnerActivity.class);
                        intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                        intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                        intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                        intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                        intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                        intent.putExtra(AvenuesParams.AMOUNT, payment);
                        intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                        intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                        intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                        intent.putExtra(AvenuesParams.USER_ID, tanentusrid_value);
                        intent.putExtra(AvenuesParams.PROPERTY_ID, tanentpropertyid_value);
                        intent.putExtra(AvenuesParams.ROOM_ID, tanentroomid_value);
                        intent.putExtra(AvenuesParams.ROOM_AMOUNT, "");
                        intent.putExtra(AvenuesParams.HIRING_DATE, tanentidValue);
                        intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                        intent.putExtra(AvenuesParams.BOOKED_BED, "");
                        intent.putExtra(AvenuesParams.TENANT_ID, tanentidValue);
                        intent.putExtra(AvenuesParams.OWNER_ID, tanentownerid_value);
                        intent.putExtra(AvenuesParams.MONTH, month);
                        intent.putExtra(AvenuesParams.TRANSACTION_DATE, currentdate_value);
                        intent.putExtra(AvenuesParams.TRANSACTION_FOR, "MONTHLY");
                        startActivity(intent);
                        //....
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
        String paymentcomment = "";

        public PaymentActivityOwnerJSONParse(String paymentcomment) {

            this.paymentcomment = paymentcomment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            student_loader.startAnimation(rotation);
            student_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            Integer randomNum = ServiceUtility.randInt(0, 9999999);
            Long transNum = ServiceUtility.numbGen();

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
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
                nameValuePairs.add(new BasicNameValuePair("order_id", randomNum.toString()));
                nameValuePairs.add(new BasicNameValuePair("transition_number", transNum.toString()));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "C"));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", paymentcomment));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");//
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
                Successdialog();
                Toast.makeText(PaymentActivityOwner.this, "Payment is successful", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(PaymentActivityOwner.this, "Payment is not success", Toast.LENGTH_LONG).show();
            }
        }

    }


    private class PaymentActivityOwnerThroughCheque extends AsyncTask<String, String, String> {

        int count;
        String name;
        private boolean IsError = false;
        String message, chequenovalue = "";

        public PaymentActivityOwnerThroughCheque(String chequenovalue) {

            this.chequenovalue = chequenovalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            student_loader.startAnimation(rotation);
            student_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            Integer randomNum = ServiceUtility.randInt(0, 9999999);
            Long transNum = ServiceUtility.numbGen();

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
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
                nameValuePairs.add(new BasicNameValuePair("order_id", randomNum.toString()));
                nameValuePairs.add(new BasicNameValuePair("transition_number", transNum.toString()));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", "Cheque"));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", chequenovalue));
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
                SuccessdialogCheque();
                Toast.makeText(PaymentActivityOwner.this, "Payment is successful", Toast.LENGTH_LONG).show();
            } else if (result.equals("N")) {
                Toast.makeText(PaymentActivityOwner.this, "Payment is not success", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void Successdialog() {
        successdialog = new Dialog(PaymentActivityOwner.this);
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
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");
        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentcomment = chequeno.getText().toString().trim();
                Intent i = new Intent(PaymentActivityOwner.this, PDFFileNewPage_owner.class);
                i.putExtra("name", name);
                i.putExtra("date", currentdate_value1);
                i.putExtra("month", month);
                i.putExtra("uid", tanentusrid_value);
                i.putExtra("tenantid", tanentidValue);
                i.putExtra("amount", payment);
                i.putExtra("mode", "Cash");
                i.putExtra("propertyid", keypropertyid);
                i.putExtra("roomno", roomno);
                i.putExtra("keyid", keyid);
                i.putExtra("Comment", paymentcomment);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                startActivity(i);
            }
        });
        username.setText(name);
        userdate.setText(currentdate_value1);
        paymentmonth.setText(month);
        userpaid.setText(payment);
        usermode.setText("Cash");
        userroomno.setText(roomno);
        userpid.setText(keypropertyid);
        usertid.setText(keyid);
        chequeno.setText(paymentcomment);
        propertyname.setText(mSharedStorage.getPropertyname());
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivityOwner.this, OwnerFromActivity.class);
                startActivity(i);
            }
        });

        successdialog.show();
    }


    public void SuccessdialogCheque() {
        successdialog = new Dialog(PaymentActivityOwner.this);
        successdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successdialog.setContentView(R.layout.success_dialogpayment_cheque_owner);
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

                chequeNo = chequeno.getText().toString().trim();

                Intent i = new Intent(PaymentActivityOwner.this, PDFFileNewPage_owner_cheque.class);
                i.putExtra("name", name);
                i.putExtra("date", currentdate_value1);
                i.putExtra("month", month);
                i.putExtra("uid", tanentusrid_value);
                i.putExtra("tenantid", tanentidValue);
                i.putExtra("amount", payment);
                i.putExtra("mode", "Cheque");
                i.putExtra("propertyid", keypropertyid);
                i.putExtra("roomno", roomno);
                i.putExtra("keyid", keyid);
                i.putExtra("Chequeno", chequeNo);
                i.putExtra("propertyname", mSharedStorage.getPropertyname());
                startActivity(i);
            }
        });
        username.setText(name);
        userdate.setText(currentdate_value1);
        paymentmonth.setText(month);
        userpaid.setText(payment);
        usermode.setText("Cheque");
        userroomno.setText(roomno);
        userpid.setText(keypropertyid);
        usertid.setText(keyid);
        chequeno.setText(chequenovalue);
        propertyname.setText(mSharedStorage.getPropertyname());

        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivityOwner.this, OwnerFromActivity.class);
                startActivity(i);
            }
        });

        successdialog.show();
    }

    //pdf method
    private void CreatePdf() {

        Document doc = new Document();

        try {

            String path = Environment.getExternalStorageDirectory() + "/Krooms";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir, name + " " + "PaySlip" + i1 + ".pdf");
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
            table1.addCell(createValueCell(name));

            table1.addCell(createValueCell("Pay For"));
            table1.addCell(createValueCell(month));

            table1.addCell(createValueCell("Paid"));
            table1.addCell(createValueCell(payment));

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
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                } else if (result.equals("N")) {
                }
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

}
