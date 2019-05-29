package com.krooms.hostel.rental.property.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.Utility.Utility;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 11/24/2016.
 */
public class UserRemoveActivity extends FragmentActivity implements View.OnClickListener {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, BaseColor.RED);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    Animation rotation;
    String keyidv = "", keypropertyid = "", roomnov = "", holduserstatus = "";

    EditText txtdueamout;
    RelativeLayout back;
    Button btndueclear, btngeneratepdf, btnexituser, btnyes, btnno;
    public static String uname, ufathername, ufathercontact,
            umobile, uaddress, uamount, uhiredate, currentdate_value1,
            uleavedate, uemail, UId = "", UBroomid, OwnerId, Dues, userid,
            propertyid, monthlyRoomRant = "";
    SharedStorage mSharedStorage;
    Dialog month_dialog;
    Button btn_roomchange, btn_adv_adjust, btn_tenanthold, btn_tenantunhold, btn_return_tenant;
    public static String buttonadvstatus = "", buttonduestatus = "", TenantReturnStatus = "";
    private PropertyUserModal tenantData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userremove);
        mSharedStorage = SharedStorage.getInstance(this);

        txtdueamout = (EditText) findViewById(R.id.txtdueamout);
        txtdueamout.setEnabled(false);
        btndueclear = (Button) findViewById(R.id.due_clear);
        btnexituser = (Button) findViewById(R.id.exituserid);
        btngeneratepdf = (Button) findViewById(R.id.exitdownload);
        btn_roomchange = (Button) findViewById(R.id.btn_roomchange);
        btn_adv_adjust = (Button) findViewById(R.id.btn_adv_adjust);
        btn_tenanthold = (Button) findViewById(R.id.btn_tenanthold);
        btn_tenantunhold = (Button) findViewById(R.id.btn_tenantunhold);
        btn_return_tenant = (Button) findViewById(R.id.btn_return_tenant);
        back = (RelativeLayout) findViewById(R.id.back_button);
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
        Dues = intent.getStringExtra("TDue");
        OwnerId = intent.getStringExtra("Towner");
        userid = intent.getStringExtra("Tuserid");
        propertyid = intent.getStringExtra("sepratepropertyid");
        ufathercontact = intent.getStringExtra("TFatherCont");
        monthlyRoomRant = intent.getStringExtra("tmonthlyRoomRant");
        buttonadvstatus = Utility.getSharedPreferences(UserRemoveActivity.this, "AdvanceStatus");
        buttonduestatus = Utility.getSharedPreferences(UserRemoveActivity.this, "DueStatus");
        TenantReturnStatus = Utility.getSharedPreferences(UserRemoveActivity.this, "TenantReturnStatus");

        if (intent.hasExtra("holduserstatus")) {

            holduserstatus = intent.getStringExtra("holduserstatus");
        }

        if (holduserstatus.equals("1")) {
            btn_tenantunhold.setVisibility(View.VISIBLE);
            btn_tenanthold.setVisibility(View.GONE);
            btn_roomchange.setVisibility(View.INVISIBLE);
        } else {
            btn_roomchange.setVisibility(View.VISIBLE);
            btn_tenantunhold.setVisibility(View.GONE);
            btn_tenanthold.setVisibility(View.VISIBLE);
        }

        txtdueamout.setText(Dues);
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value1 = dateO + "/" + (monthO + 1) + "/" + yearO;
        new GetUserKeyDetail().execute();
        txtdueamout.setOnClickListener(this);
        btndueclear.setOnClickListener(this);
        btnexituser.setOnClickListener(this);
        btngeneratepdf.setOnClickListener(this);
        btn_roomchange.setOnClickListener(this);
        btn_adv_adjust.setOnClickListener(this);
        btn_tenanthold.setOnClickListener(this);
        btn_tenantunhold.setOnClickListener(this);
        btn_return_tenant.setOnClickListener(this);
        back.setOnClickListener(this);

        if (txtdueamout.getText().toString().equals("0") || txtdueamout.getText().toString().equals("")) {
            btnexituser.setClickable(true);
            btndueclear.setClickable(false);
            btndueclear.setBackgroundColor(Color.parseColor("#1Aff5d27"));
            btn_roomchange.setClickable(true);
            btn_tenanthold.setClickable(true);
        } else {
            btnexituser.setClickable(false);
            btndueclear.setClickable(true);
            btn_roomchange.setClickable(false);
            btn_tenanthold.setClickable(false);
            btnexituser.setBackgroundColor(Color.parseColor("#1Aff5d27"));
            btn_roomchange.setBackgroundColor(Color.parseColor("#1Aff5d27"));
            btn_tenanthold.setBackgroundColor(Color.parseColor("#1Aff5d27"));
        }


        if (buttonadvstatus.equals("1")) {
            btn_adv_adjust.setClickable(false);
            btn_adv_adjust.setBackgroundColor(Color.parseColor("#1Aff5d27"));
        }
        if (buttonduestatus.equals("1")) {
            btn_adv_adjust.setClickable(false);
            btn_adv_adjust.setBackgroundColor(Color.parseColor("#1Aff5d27"));
        }
        if (TenantReturnStatus.equals("1")) {
            btn_return_tenant.setClickable(false);
            btn_return_tenant.setBackgroundColor(Color.parseColor("#1Aff5d27"));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtdueamout:
                break;

            case R.id.due_clear:
                if (holduserstatus.equals("1")) {

                } else {
                    //new changes on 23/10/2018 by ashish
                    getTenantReturnAmount();
                }
                break;

            case R.id.exituserid:
                ShowAlertDialog();
                break;

            case R.id.exitdownload:
                try {
                    Document document = new Document();
                    String path = Environment.getExternalStorageDirectory() + "/Krooms";
                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();
                    Log.d("PDFCreator", "PDF Path: " + path);
                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;
                    File file = new File(dir, "Tenant record" + i1 + ".pdf");
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();
                    CreatePdf(document);
                    addTitlePage(document);
                    addMetaData(document);
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(UserRemoveActivity.this, "Tenant Record downloaded succesfully", Toast.LENGTH_LONG).show();
                break;

            case R.id.back_button:
                Intent intenti = new Intent(UserRemoveActivity.this, MyPropertyUsersListActivity.class);
                startActivity(intenti);
                break;

            case R.id.btn_roomchange:
                vacantRoom();
                break;

            case R.id.btn_adv_adjust:

                if (holduserstatus.equals("1")) {

                } else {
                    getMonthlyrent();
                }
                break;

            case R.id.btn_tenanthold:
                vacantRoomonHold();
                break;

            case R.id.btn_tenantunhold:
                UnHoldHold();
                break;

            case R.id.btn_return_tenant:
                Intent intent = new Intent(UserRemoveActivity.this, Tenant_Return_Activity.class);
                intent.putExtra("DuePayment", txtdueamout.getText().toString());
                intent.putExtra("tenantid", UId);
                intent.putExtra("userid", userid);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("ownerid", OwnerId);
                intent.putExtra("roomid", UBroomid);
                intent.putExtra("Tname", uname);
                intent.putExtra("Tfather", ufathername);
                intent.putExtra("Tmobile", umobile);
                intent.putExtra("Temail", uemail);
                intent.putExtra("Taddress", uaddress);
                intent.putExtra("Thire", uhiredate);
                intent.putExtra("Tleave", uleavedate);
                intent.putExtra("Tamount", uamount);//  room advance amount
                intent.putExtra("TFatherCont", ufathercontact);
                intent.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                intent.putExtra("MonthPaidAmount", "0");
                startActivity(intent);
                break;
        }
    }

    private void ShowAlertDialog() {

        month_dialog = new Dialog(UserRemoveActivity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_alertdialog_exit);
        btnyes = (Button) month_dialog.findViewById(R.id.alertYesBtnUexit);
        btnno = (Button) month_dialog.findViewById(R.id.alertNoBtnUexit);
        month_dialog.show();
        month_dialog.setCanceledOnTouchOutside(false);

        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeactiveTenant().execute();
            }
        });

        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });

    }


    public class DeactiveTenant extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserRemoveActivity.this);
            pDialog.setMessage("Processing...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;

            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "exittenant"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", userid));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", UId));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
            listNameValuePairs.add(new BasicNameValuePair("room_id", UBroomid));
            listNameValuePairs.add(new BasicNameValuePair("transaction_date", currentdate_value1));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            String objectmain = result.replace("\t", "").replace("\n", "");
            System.out.print("" + result);
            listNameValuePairs.clear();
            return objectmain;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            String msg = null;
            String status = null;
            String image = null;
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Data not found ", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equals("Y")) {
                        month_dialog.dismiss();
                        startActivity(new Intent(UserRemoveActivity.this, HostelListActivity.class));
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = jsonArray.getJSONObject(i);
                        }

                        Toast.makeText(getApplicationContext(), "Tenant Removed Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //tenant hold set status hold and remove tenant from mail list
    public class HoldDeactiveTenant extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserRemoveActivity.this);
            pDialog.setMessage("Processing...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "exittenantonhold"));
            listNameValuePairs.add(new BasicNameValuePair("user_id", userid));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", UId));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
            listNameValuePairs.add(new BasicNameValuePair("room_id", UBroomid));
            listNameValuePairs.add(new BasicNameValuePair("transaction_date", currentdate_value1));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            String objectmain = result.replace("\t", "").replace("\n", "");
            System.out.print("" + result);
            listNameValuePairs.clear();
            return objectmain;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            String msg = null;
            String status = null;
            String image = null;
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Data not found ", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");
                    if (status.equals("Y")) {
                        Toast.makeText(getApplicationContext(), "Tenant Hold Successfully", Toast.LENGTH_SHORT).show();
                        Intent intenti = new Intent(UserRemoveActivity.this, MyPropertyUsersListActivity.class);
                        startActivity(intenti);

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //


    public static void addMetaData(Document document) {
        document.addTitle("Payment Slip");
        document.addSubject("Details for payment transaction");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(createValueCell1("Tenant Detail"));
        preface.add(table1);
        addEmptyLine(preface, 1);
        PdfPTable table4 = new PdfPTable(1);
        table4.addCell(createValueCell5("Date :" + currentdate_value1));
        preface.add(table4);
        addEmptyLine(preface, 1);

        PdfPTable table2 = new PdfPTable(1);
        table2.addCell(createValueCell22("Dear Tenant,"));
        preface.add(table2);
        addEmptyLine(preface, 1);

        PdfPTable table3 = new PdfPTable(1);
        table3.addCell(createValueCell3("Thank you for using KROOMS ! Your all details are as follows:"));
        preface.add(table3);
        addEmptyLine(preface, 1);

        PdfPTable table = new PdfPTable(2);
        table.addCell(createValueCell2("Tenant Id"));
        table.addCell(createValueCell4(keyidv));

        table.addCell(createValueCell2("Tenant Name"));
        table.addCell(createValueCell4(uname));

        table.addCell(createValueCell2("Contact_no"));
        table.addCell(createValueCell4(umobile));

        table.addCell(createValueCell2("Email-Id"));
        table.addCell(createValueCell4(uemail));

        table.addCell(createValueCell2("Father Name"));
        table.addCell(createValueCell4(ufathername));

        table.addCell(createValueCell2("Father ContactNo"));
        table.addCell(createValueCell4(ufathercontact));

        table.addCell(createValueCell2("Address"));
        table.addCell(createValueCell4(uaddress));

        table.addCell(createValueCell2("Property Id"));
        table.addCell(createValueCell4(keypropertyid));

        table.addCell(createValueCell2("Room No"));
        table.addCell(createValueCell4(roomnov));

        table.addCell(createValueCell2("Hire Date"));
        table.addCell(createValueCell4(uhiredate));

        table.addCell(createValueCell2("Leave Date"));
        table.addCell(createValueCell4(uleavedate));

        table.addCell(createValueCell2("Total Amount"));
        table.addCell(createValueCell4(uamount));
        addEmptyLine(preface, 1);


        preface.add(9, table);
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
            table1.addCell(createValueCell1("Finest Rental Solution"));
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
                startActivity(new Intent(UserRemoveActivity.this, MyPropertyUsersListActivity.class));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class GetUserKeyDetail extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(UserRemoveActivity.this);
            pd.setMessage("Processing...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "getuserkeydetails"));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", UId));
                nameValuePairs.add(new BasicNameValuePair("room_id", UBroomid));

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
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                    new PropertyIdKeyParser().execute();
                    pd.dismiss();
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
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(UserRemoveActivity.this);
            pd.setMessage("Processing...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {
            mSharedStorage = SharedStorage.getInstance(UserRemoveActivity.this);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "propertyDetailPage"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getUserPropertyId()));
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
            pd.dismiss();

            if (result.equalsIgnoreCase("Y")) {
            } else if (result.equals("N")) {
            }
        }
    }

    //

    private void getMonthlyrent() {

        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        int month_idd = (monthO + 1);
        //new changes on 6-9-18
        final ProgressDialog dialog = new ProgressDialog(UserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getroompaidamount");
            params.put("property_id", propertyid);
            //params.put("room_id",UBroomid);
            params.put("tenant_id", UId);
            params.put("month_id", month_idd);
            params.put("year", yearO);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDatapayment(result);
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

    public void getResponseDatapayment(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                String monthpaidamount = jsmain.getString("records");
                Intent intent = new Intent(UserRemoveActivity.this, UserOwner_AdvancePaymnetAdjustActivity.class);
                intent.putExtra("DuePayment", txtdueamout.getText().toString());
                intent.putExtra("tenantid", UId);
                intent.putExtra("userid", userid);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("ownerid", OwnerId);
                intent.putExtra("roomid", UBroomid);
                intent.putExtra("Tname", uname);
                intent.putExtra("Tfather", ufathername);
                intent.putExtra("Tmobile", umobile);
                intent.putExtra("Temail", uemail);
                intent.putExtra("Taddress", uaddress);
                intent.putExtra("Thire", uhiredate);
                intent.putExtra("Tleave", uleavedate);
                intent.putExtra("Tamount", uamount);//  room advance amount
                intent.putExtra("TFatherCont", ufathercontact);
                intent.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                intent.putExtra("MonthPaidAmount", monthpaidamount);
                startActivity(intent);

            } else {
                Intent intent = new Intent(UserRemoveActivity.this, UserOwner_AdvancePaymnetAdjustActivity.class);
                intent.putExtra("DuePayment", txtdueamout.getText().toString());
                intent.putExtra("tenantid", UId);
                intent.putExtra("userid", userid);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("ownerid", OwnerId);
                intent.putExtra("roomid", UBroomid);
                intent.putExtra("Tname", uname);
                intent.putExtra("Tfather", ufathername);
                intent.putExtra("Tmobile", umobile);
                intent.putExtra("Temail", uemail);
                intent.putExtra("Taddress", uaddress);
                intent.putExtra("Thire", uhiredate);
                intent.putExtra("Tleave", uleavedate);
                intent.putExtra("Tamount", uamount);//  room advance amount
                intent.putExtra("TFatherCont", ufathercontact);
                intent.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                intent.putExtra("MonthPaidAmount", "0");
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTenantReturnAmount() {

        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        int month_idd = (monthO + 1);

        //new changes on 6-9-18
        final ProgressDialog dialog = new ProgressDialog(UserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getTenantReturnAmount");
            params.put("property_id", propertyid);
            //params.put("room_id",UBroomid);
            params.put("tenant_id", UId);
            params.put("month_id", "17");
            params.put("year", yearO);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseTenantReturnAmount(result);
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

    public void getResponseTenantReturnAmount(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                String tenantreturnamount = jsmain.getString("records");
                Intent ii = new Intent(UserRemoveActivity.this, UserOwner_RemoveActivity.class);
                ii.putExtra("DuePayment", txtdueamout.getText().toString());
                ii.putExtra("tenantid", UId);
                ii.putExtra("userid", userid);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("ownerid", OwnerId);
                ii.putExtra("roomid", UBroomid);
                ii.putExtra("Tname", uname);
                ii.putExtra("Tfather", ufathername);
                ii.putExtra("Tmobile", umobile);
                ii.putExtra("Temail", uemail);
                ii.putExtra("Taddress", uaddress);
                ii.putExtra("Thire", uhiredate);
                ii.putExtra("Tleave", uleavedate);
                ii.putExtra("Tamount", uamount);
                ii.putExtra("TFatherCont", ufathercontact);
                ii.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                ii.putExtra("TenantReturnAmount", tenantreturnamount);
                startActivity(ii);

            } else {
                Intent ii = new Intent(UserRemoveActivity.this, UserOwner_RemoveActivity.class);
                ii.putExtra("DuePayment", txtdueamout.getText().toString());
                ii.putExtra("tenantid", UId);
                ii.putExtra("userid", userid);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("ownerid", OwnerId);
                ii.putExtra("roomid", UBroomid);
                ii.putExtra("Tname", uname);
                ii.putExtra("Tfather", ufathername);
                ii.putExtra("Tmobile", umobile);
                ii.putExtra("Temail", uemail);
                ii.putExtra("Taddress", uaddress);
                ii.putExtra("Thire", uhiredate);
                ii.putExtra("Tleave", uleavedate);
                ii.putExtra("Tamount", uamount);
                ii.putExtra("TFatherCont", ufathercontact);
                ii.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                ii.putExtra("TenantReturnAmount", "0");
                startActivity(ii);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //
    private void vacantRoom() {

        final ProgressDialog dialog = new ProgressDialog(UserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "Vacantroom");
            params.put("property_id", propertyid);
            params.put("room_id", UBroomid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseData(result);
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

    public void getResponseData(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                //vacant_btn.setEnabled(false);
                vacantbackupRoom();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //backup room change
    //vacant room
    private void vacantbackupRoom() {

        final ProgressDialog dialog = new ProgressDialog(UserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getroomchangebackup");
            params.put("property_id", propertyid);
            params.put("tenant_id", UId);
            params.put("room_id", UBroomid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataRoomchange(result);
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

    public void getResponseDataRoomchange(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, "Please Select new Room", Toast.LENGTH_SHORT).show();
                Intent ii = new Intent(UserRemoveActivity.this, HostelDetailActivity.class);
                ii.putExtra("property_id", propertyid);
                ii.putExtra("roomchnage", "1");
                ii.putExtra("roomidnewvalue", UBroomid);
                ii.putExtra("tenantid", UId);
                startActivity(ii);

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //vancant current room on tenant hold condition:-
    private void vacantRoomonHold() {

        final ProgressDialog dialog = new ProgressDialog(UserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "Vacantroom");
            params.put("property_id", propertyid);
            params.put("room_id", UBroomid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseHoldtenant(result);
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

    public void getResponseHoldtenant(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                new HoldDeactiveTenant().execute();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //


    //Un Hold  tenant by 16/10/2018 by ashish

    private void UnHoldHold() {

        final ProgressDialog dialog = new ProgressDialog(UserRemoveActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {

            JSONObject params = new JSONObject();
            params.put("action", "tenantUNhold");
            params.put("user_id", userid);
            params.put("tenant_id", UId);
            params.put("property_id", propertyid);
            params.put("transaction_date", currentdate_value1);
            params.put("room_id", UBroomid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseUNHoldtenant(result);
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

    public void getResponseUNHoldtenant(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                vacantbackupRoom();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

