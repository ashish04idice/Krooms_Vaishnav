package com.krooms.hostel.rental.property.app.activity;

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
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingUserRemoveActivity;
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
import org.json.JSONException;
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
 * Created by admin on 11/29/2016.
 */
public class UserOwner_RemoveActivity extends Activity {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, BaseColor.RED);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    TextView dueamout, termandcondition, paidamount_outputhint1;
    Button payment;
    RadioButton cash, online, chequeOption;
    CheckBox term;
    RelativeLayout backbtn;
    RadioGroup rgroup;
    Integer randomNum;
    public static String monthid, keypropertyid, dueAmountvalue, propertyid, roomid, userid,
            ownerid, monthname = "", tenantid, paymentoptionvalue = "",
            totalAmount_value = "", currentdate_value = "", currentdate_value1 = "", paymentcomment = "", paymentmode = "", paymentmodepdf = "",
            roomnov = "", keyidv = "";
    SharedStorage mSharedStorage;
    EditText dueamountvaluepaid, tenantreturn_output_paid;
    public static int yearO;
    EditText chequeno;
    public static String chequenovalue = "", chequeNo = "", TenantReturnAmount = "", tamountfinal = "", tenantadjustamount = "";

    public static String monthnameheading = "", tenantreturnstatus = "", amountequalstatus = "";

    public static String uname, ufathername, ufathercontact,
            umobile, uaddress, uamount, uhiredate,
            uleavedate, uemail, monthlyRoomRant, yearname = "";

    public int totablepaybleamount;

    LinearLayout paymentOption, termsAndCondition, txtcomment;

    int tamountt, intdueamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owneractivityfrom_removedata);
        mSharedStorage = SharedStorage.getInstance(this);
        dueamout = (TextView) findViewById(R.id.totalAmount_output);
        paidamount_outputhint1 = (TextView) findViewById(R.id.paidamount_outputhint1);
        dueamountvaluepaid = (EditText) findViewById(R.id.totalAmount_output_paid);
        tenantreturn_output_paid = (EditText) findViewById(R.id.tenantreturn_output_paid);
        termandcondition = (TextView) findViewById(R.id.termsAndConditionTextView);
        payment = (Button) findViewById(R.id.submitBtn);
        cash = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
        online = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
        chequeOption = (RadioButton) findViewById(R.id.chequePaymentRadioBtn);
        chequeno = (EditText) findViewById(R.id.txt_chequeno);
        term = (CheckBox) findViewById(R.id.checkeBox);
        backbtn = (RelativeLayout) findViewById(R.id.back_button);
        rgroup = (RadioGroup) findViewById(R.id.paymentRadioGroup);
        paymentOption = (LinearLayout) findViewById(R.id.paymentOption);
        termsAndCondition = (LinearLayout) findViewById(R.id.termsAndCondition);
        txtcomment = (LinearLayout) findViewById(R.id.txtcomment);
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value = yearO + "/" + (monthO + 1) + "/" + dateO;
        currentdate_value1 = dateO + "/" + (monthO + 1) + "/" + yearO;
        Intent in = getIntent();
        dueAmountvalue = in.getStringExtra("DuePayment");
        dueamout.setText(dueAmountvalue);
        propertyid = in.getStringExtra("propertyid");
        userid = in.getStringExtra("userid");
        tenantid = in.getStringExtra("tenantid");
        ownerid = in.getStringExtra("ownerid");
        roomid = in.getStringExtra("roomid");

        //new changes on 6-9-18
        if (in.hasExtra("Tname")) {
            uname = in.getStringExtra("Tname");
        }


        if (in.hasExtra("TenantReturnAmount")) {
            TenantReturnAmount = in.getStringExtra("TenantReturnAmount");
        }
        if (in.hasExtra("Tfather")) {
            ufathername = in.getStringExtra("Tfather");
        }

        if (in.hasExtra("Tmobile")) {
            umobile = in.getStringExtra("Tmobile");
        }

        if (in.hasExtra("Temail")) {
            uemail = in.getStringExtra("Temail");
        }

        if (in.hasExtra("Taddress")) {
            uaddress = in.getStringExtra("Taddress");
        }
        if (in.hasExtra("Thire")) {
            uhiredate = in.getStringExtra("Thire");
        }

        if (in.hasExtra("Tleave")) {
            uleavedate = in.getStringExtra("Tleave");
        }

        if (in.hasExtra("Tamount")) {
            uamount = in.getStringExtra("Tamount");
        }
        if (in.hasExtra("TFatherCont")) {
            ufathercontact = in.getStringExtra("TFatherCont");
        }

        if (in.hasExtra("tmonthlyRoomRant")) {
            monthlyRoomRant = in.getStringExtra("tmonthlyRoomRant");
        }

        tenantreturn_output_paid.setText("" + TenantReturnAmount);
        tamountt = Integer.parseInt(TenantReturnAmount);
        intdueamount = Integer.parseInt(dueAmountvalue);

        if (tamountt > intdueamount) {
            totablepaybleamount = tamountt - intdueamount;
            paidamount_outputhint1.setText("This Amount Return To Tenant");
            tenantreturnstatus = "1";
            paymentOption.setVisibility(View.GONE);
            termsAndCondition.setVisibility(View.GONE);
            txtcomment.setVisibility(View.GONE);

        } else {
            totablepaybleamount = intdueamount - tamountt;
            paidamount_outputhint1.setText("This Amount Paid To Owner");
            tenantreturnstatus = "";
            paymentOption.setVisibility(View.VISIBLE);
            termsAndCondition.setVisibility(View.VISIBLE);
            txtcomment.setVisibility(View.VISIBLE);
        }

        if (tamountt == intdueamount) {
            amountequalstatus = "1";
        } else {
            amountequalstatus = "";
        }

        dueamountvaluepaid.setText("" + totablepaybleamount);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserOwner_RemoveActivity.this, UserRemoveActivity.class);
                intent.putExtra("Tname", uname);
                intent.putExtra("Towner", ownerid);
                intent.putExtra("Taddress", uaddress);
                intent.putExtra("Tamount", uamount);
                intent.putExtra("Tmobile", umobile);
                intent.putExtra("Tfather", ufathername);
                intent.putExtra("Thire", uhiredate);
                intent.putExtra("Tleave", uleavedate);
                intent.putExtra("Temail", uemail);
                intent.putExtra("NTenantid", tenantid);
                intent.putExtra("TBookRoom", roomid);
                intent.putExtra("TDue", dueAmountvalue);
                intent.putExtra("sepratepropertyid", propertyid);
                intent.putExtra("TFatherCont", ufathercontact);
                intent.putExtra("Tuserid", userid);
                intent.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                startActivity(intent);

            }
        });

        new GetUserKeyDetail().execute();
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d("payment 1", "in group");
                if (cash.isChecked()) {
                    paymentoptionvalue = "Cash";

                } else if (online.isChecked()) {
                    paymentoptionvalue = "Online";
                } else if (chequeOption.isChecked()) {
                    paymentoptionvalue = "Cheque";
                }
            }
        });

        //apply terms and condition dialog
        termandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserOwner_RemoveActivity.this);
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

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("payment 2", "in button" + mSharedStorage.getUserId() + "" + mSharedStorage.getBookedPropertyId());
                totalAmount_value = dueamountvaluepaid.getText().toString();

                Calendar calendar = Calendar.getInstance();
                int yearO = calendar.get(Calendar.YEAR);

                yearname = String.valueOf(yearO);

                if (tenantreturnstatus.equals("1")) {
                    if (totalAmount_value.length() != 0) {
                        paymentcomment = chequeno.getText().toString().trim();
                        paymentmode = "C";
                        paymentmodepdf = "Cash";
                        new PaymentActivityOwnerJSONParse(paymentcomment, paymentmode, paymentmodepdf, yearname, tenantadjustamount, tenantreturnstatus).execute();
                    } else {
                        Toast.makeText(UserOwner_RemoveActivity.this, "please enter paid amount", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    if (paymentoptionvalue.equals("")) {
                        Toast.makeText(UserOwner_RemoveActivity.this, "please select mode ", Toast.LENGTH_SHORT).show();
                    } else if (!term.isChecked()) {
                        Toast.makeText(UserOwner_RemoveActivity.this, "please select Terms and Condition", Toast.LENGTH_SHORT).show();
                    } else {

                        String tamountnew = tenantreturn_output_paid.getText().toString();
                        if (tamountnew.equals("") || tamountnew.equals("0") || tamountnew.equals(null)) {
                            tenantadjustamount = "";
                        } else {

                            tenantadjustamount = tenantreturn_output_paid.getText().toString();
                        }

                        if (totalAmount_value.length() != 0) {
                            if (Integer.parseInt(totalAmount_value) > Integer.parseInt(dueAmountvalue)) {
                                Toast.makeText(UserOwner_RemoveActivity.this, "Amount should be less or equal to due amount", Toast.LENGTH_SHORT).show();
                            } else {

                                if (paymentoptionvalue.equalsIgnoreCase("Cash")) {
                                    paymentcomment = chequeno.getText().toString().trim();
                                    paymentmode = "C";
                                    paymentmodepdf = "Cash";
                                    new PaymentActivityOwnerJSONParse(paymentcomment, paymentmode, paymentmodepdf, yearname, tenantadjustamount, tenantreturnstatus).execute();

                                } else if (paymentoptionvalue.equalsIgnoreCase("Cheque")) {

                                    chequenovalue = chequeno.getText().toString().trim();
                                    paymentmode = "Cheque";
                                    paymentmodepdf = "Cheque";
                                    new PaymentActivityOwnerJSONParse(chequenovalue, paymentmode, paymentmodepdf, yearname, tenantadjustamount, tenantreturnstatus).execute();

                                } else {
                                    if (tenantreturnstatus.equals("1")) {
                                        totalAmount_value = "";
                                        totalAmount_value = dueamout.getText().toString();
                                        tamountfinal = dueamountvaluepaid.getText().toString();
                                        monthnameheading = "Due Adj-Adv.ret";

                                    } else {
                                        totalAmount_value = "";
                                        totalAmount_value = dueamountvaluepaid.getText().toString();
                                        monthnameheading = "Due Amount";
                                    }

                                    paymentcomment = chequeno.getText().toString().trim();
                                    Intent intent = new Intent(UserOwner_RemoveActivity.this, BillingShippingUserRemoveActivity.class);
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
                                    intent.putExtra(AvenuesParams.ROOM_ID, roomid);
                                    intent.putExtra(AvenuesParams.ROOM_AMOUNT, "");
                                    intent.putExtra(AvenuesParams.HIRING_DATE, "");
                                    intent.putExtra(AvenuesParams.LEAVING_DATE, "");
                                    intent.putExtra(AvenuesParams.BOOKED_BED, "");
                                    //new changes by ashish on 8-9-2018
                                    intent.putExtra("Tname", uname);
                                    intent.putExtra("Towner", ownerid);
                                    intent.putExtra("Taddress", uaddress);
                                    intent.putExtra("Tamount", uamount);
                                    intent.putExtra("Tmobile", umobile);
                                    intent.putExtra("Tfather", ufathername);
                                    intent.putExtra("Thire", uhiredate);
                                    intent.putExtra("Tleave", uleavedate);
                                    intent.putExtra("Temail", uemail);
                                    intent.putExtra("NTenantid", tenantid);
                                    intent.putExtra("TBookRoom", roomid);
                                    intent.putExtra("sepratepropertyid", propertyid);
                                    intent.putExtra("TFatherCont", ufathercontact);
                                    intent.putExtra("Tuserid", userid);
                                    intent.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                                    startActivity(intent);
                                }
                                //........
                            }

                        } else {
                            Toast.makeText(UserOwner_RemoveActivity.this, "please enter paid amount", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private class PaymentActivityOwnerJSONParse extends AsyncTask<String, String, String> {

        int count;
        String name;
        private boolean IsError = false;
        String message;
        String result;
        ProgressDialog pd;
        String paymentcomment = "", paymentmode = "", paymentmodepdf = "", yearname = "", tenantadjustamount = "";

        public PaymentActivityOwnerJSONParse(String paymentcomment, String paymentmode, String paymentmodepdf, String yearname, String tenantadjustamount, String tenantreturnstatus) {

            this.paymentcomment = paymentcomment;
            this.paymentmode = paymentmode;
            this.paymentmodepdf = paymentmodepdf;
            this.yearname = yearname;
            this.tenantadjustamount = tenantadjustamount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(UserOwner_RemoveActivity.this);
            pd.setMessage("Processing...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {

            if (tenantreturnstatus.equals("1")) {
                totalAmount_value = "";
                totalAmount_value = dueamout.getText().toString();
                tamountfinal = dueamountvaluepaid.getText().toString();
                monthnameheading = "Due Adj-Adv.ret";
            } else {
                totalAmount_value = "";
                totalAmount_value = dueamountvaluepaid.getText().toString();
                monthnameheading = "Due Amount";
            }


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "cleardueamount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", userid));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
                nameValuePairs.add(new BasicNameValuePair("owner_id", MyPropertyUsersListActivity.owneridvaluemain));
                nameValuePairs.add(new BasicNameValuePair("month_id", ""));
                nameValuePairs.add(new BasicNameValuePair("month_name", monthnameheading));
                nameValuePairs.add(new BasicNameValuePair("amount", totalAmount_value));//totalAmount_value
                nameValuePairs.add(new BasicNameValuePair("room_id", roomid));
                nameValuePairs.add(new BasicNameValuePair("advance_amount", ""));
                nameValuePairs.add(new BasicNameValuePair("year", yearname));
                nameValuePairs.add(new BasicNameValuePair("room_amount", dueAmountvalue));
                nameValuePairs.add(new BasicNameValuePair("current_date", currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("month_date", currentdate_value));
                nameValuePairs.add(new BasicNameValuePair("transaction_mode", paymentmode));
                nameValuePairs.add(new BasicNameValuePair("cheque_no", paymentcomment));//add in api
                nameValuePairs.add(new BasicNameValuePair("tenantreturnstatus", tenantreturnstatus));//add in api
                nameValuePairs.add(new BasicNameValuePair("tamountfinal", tamountfinal));//add in api
                nameValuePairs.add(new BasicNameValuePair("tenantadjustamount", tenantadjustamount));//add in api
                nameValuePairs.add(new BasicNameValuePair("dueequaltotenantamt", amountequalstatus));//add in api
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
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            if (result.equalsIgnoreCase("Y")) {
                Toast.makeText(UserOwner_RemoveActivity.this, "Payment is successfull", Toast.LENGTH_LONG).show();
                SendButtonStatus(paymentcomment, paymentmodepdf, monthnameheading, totalAmount_value);
            } else if (result.equals("N")) {
                Toast.makeText(UserOwner_RemoveActivity.this, "Payment is not success full", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Add tenant Adv.adjust and no dues done status
    private void SendButtonStatus(final String paymentcomment, final String paymentmodepdf, final String monthnameheading, final String totalAmount_value) {

        final ProgressDialog dialog = new ProgressDialog(UserOwner_RemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "addButtonStatus");
            params.put("property_id", propertyid);
            params.put("tenant_id", tenantid);
            params.put("status", "2");//for due clear
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getButtonresponse(result, paymentcomment, paymentmodepdf, monthnameheading, totalAmount_value);
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

    public void getButtonresponse(String result, String paymentcomment, String paymentmodepdf, String monthnameheading, String totalAmount_value) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Successdialog(paymentcomment, paymentmodepdf, monthnameheading, totalAmount_value);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Successdialog(paymentcomment, paymentmodepdf, monthnameheading, totalAmount_value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //


    public void Successdialog(final String paymentcomment, final String paymentmode, final String monthnameheading, final String totalAmount_value) {
        final Dialog successdialog = new Dialog(UserOwner_RemoveActivity.this);
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
        final TextView chequeno = (TextView) successdialog.findViewById(R.id.chequeno);
        TextView statepopup_heading = (TextView) successdialog.findViewById(R.id.statepopup_heading);
        Button download_data_button = (Button) successdialog.findViewById(R.id.download_data_button);
        Button nobutton = (Button) successdialog.findViewById(R.id.nobutton);
        statepopup_heading.setText("Success Dialog");

        download_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String amt = "", mname = "";
                    //changes 21/12/2018
                    if (amountequalstatus.equals("1")) {
                        amt = TenantReturnAmount;
                        mname = "Due Adj-Adv.ret";
                    } else {

                        amt = totalAmount_value;
                        mname = monthnameheading;
                    }//end

                    Document document = new Document();
                    String path = Environment.getExternalStorageDirectory() + "/Krooms";
                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();
                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;
                    File file = new File(dir, "Tenant record" + i1 + ".pdf");
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();
                    CreatePdf(document);
                    addTitlePage(document, currentdate_value1,
                            UserRemoveActivity.uname, "",
                            amt, paymentmode,
                            mname, keyidv, roomnov, keypropertyid, paymentcomment);
                    addMetaData(document);
                    Toast.makeText(UserOwner_RemoveActivity.this, "Tenant Record downloaded succesfully ", Toast.LENGTH_LONG).show();
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                successdialog.dismiss();
                //new chnages on 13/11/2018
                Intent intent = new Intent(UserOwner_RemoveActivity.this, MyPropertyUsersListActivity.class);
                startActivity(intent);

                //

            }
        });
        username.setText(UserRemoveActivity.uname);
        userdate.setText(currentdate_value1);

        //changes 21/12/2018
        if (amountequalstatus.equals("1")) {
            userpaid.setText(TenantReturnAmount);
            paymentmonth.setText("Due Adj-Adv.ret");
        } else {

            userpaid.setText(totalAmount_value);
            paymentmonth.setText(monthnameheading);
        }//end

        usermode.setText(paymentmode);
        userpid.setText(keypropertyid);
        usertid.setText(keyidv);
        userroomno.setText(roomnov);
        chequeno.setText(paymentcomment);
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new chnages on 13/11/2018

                Intent intent = new Intent(UserOwner_RemoveActivity.this, MyPropertyUsersListActivity.class);
                startActivity(intent);

                //
                //
            }
        });

        successdialog.show();
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
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));
                nameValuePairs.add(new BasicNameValuePair("room_id", roomid));

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
                        roomnov = jsonarray.getJSONObject(i).getString("room_no");
                        keyidv = jsonarray.getJSONObject(i).getString("tenant_id");
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
            } else if (result.equals("N")) {
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
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
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
        table6.addCell(createValueCell6("Contact No: 9926914699"));
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(UserOwner_RemoveActivity.this, UserRemoveActivity.class);
                intent.putExtra("Tname", uname);
                intent.putExtra("Towner", ownerid);
                intent.putExtra("Taddress", uaddress);
                intent.putExtra("Tamount", uamount);
                intent.putExtra("Tmobile", umobile);
                intent.putExtra("Tfather", ufathername);
                intent.putExtra("Thire", uhiredate);
                intent.putExtra("Tleave", uleavedate);
                intent.putExtra("Temail", uemail);
                intent.putExtra("NTenantid", tenantid);
                intent.putExtra("TBookRoom", roomid);
                intent.putExtra("TDue", dueAmountvalue);
                intent.putExtra("sepratepropertyid", propertyid);
                intent.putExtra("TFatherCont", ufathercontact);
                intent.putExtra("Tuserid", userid);
                intent.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                startActivity(intent);

                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
