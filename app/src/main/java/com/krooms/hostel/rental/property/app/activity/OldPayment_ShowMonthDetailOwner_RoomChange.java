package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.ShowMonthDetailOwnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.MonthDetailModal;
import com.krooms.hostel.rental.property.app.modal.PdfUserModel;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


//Module :Monthly Payment : Show monthly payment ,Payment Edit ,Report download in pdf format
//Dev :Riya / Anuradha
// 8 Nov 2016

/**
 * Created by admin on 11/10/2016.
 */
public class OldPayment_ShowMonthDetailOwner_RoomChange extends Activity {
    ArrayList<MonthDetailModal> data_model_arraylist;
    MonthDetailModal data_model;
    ArrayList<PdfUserModel> list;
    RelativeLayout backbutton;
    Button downloadslip;
    ListView lvCartItems;
    Intent in;
    TextView headername, duetextview;
    Animation rotation;
    ImageView student_loader;
    public static String month_id_value = "", property_name = "", user_id_value = "", property_id_value = "", tanentuserid = "", yearvalue = "";
    PropertyUserModal tanentdatamain;
    String currentdate_value, tenant_name_value, type = "";
    LinearLayout duelayout;
    SharedStorage mShared;
    String propertyname = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listgnewitemactivitymonth);
        tanentdatamain = getIntent().getParcelableExtra("model");
        in = getIntent();
        type = in.getStringExtra("type");

        mShared = SharedStorage.getInstance(this);
        propertyname = mShared.getPropertyname();

        if (type.equalsIgnoreCase("1")) {

            user_id_value = in.getStringExtra("tanentuserid");
            tanentuserid = in.getStringExtra("tanantid");
            tenant_name_value = in.getStringExtra("user_name");
            property_name = in.getStringExtra("property_name");
            property_id_value = in.getStringExtra("tanentpropertyid");
            month_id_value = in.getStringExtra("monthid");
            yearvalue = in.getStringExtra("yearvalue");
        } else {
            user_id_value = in.getStringExtra("tanentuserid");
            tanentuserid = in.getStringExtra("tanantid");
            tenant_name_value = in.getStringExtra("user_name");
            property_name = in.getStringExtra("property_name");
            property_id_value = in.getStringExtra("tanentpropertyid");
            month_id_value = in.getStringExtra("monthid");
            yearvalue = in.getStringExtra("yearvalue");
        }

        String nm = TenantDetailActivity.tanentfinalname;
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        // these is for loader rotation
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

        //id get by xml
        duelayout = (LinearLayout) findViewById(R.id.duelayout);
        duelayout.setVisibility(View.INVISIBLE);
        duetextview = (TextView) findViewById(R.id.duetextview);
        duetextview.setVisibility(View.INVISIBLE);
//        duetextview.setText(TenantDetailActivity.Dueamount);
        headername = (TextView) findViewById(R.id.headername);
        student_loader = (ImageView) findViewById(R.id.student_loader);
        backbutton = (RelativeLayout) findViewById(R.id.back_button);
        downloadslip = (Button) findViewById(R.id.downloadslip);
        lvCartItems = (ListView) findViewById(R.id.lvCartItems);


        //value set in text view
        headername.setText("Tanent Monthly Report RoomChange");

        if (type.equalsIgnoreCase("1")) {
            //  new ElectricityListviewactivityJSONArray().execute();
        } else {
            //here call list activity class
            new ListviewactivityJSONArray().execute();
        }

        //this is for back press button
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        // for pdf download
        downloadslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pdf creater method for monthly report download
                createPDF();
                Toast.makeText(OldPayment_ShowMonthDetailOwner_RoomChange.this, "Payment Report Downloaded ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //this class is used getting value by ownerendmonthdetails webservice for
    private class ListviewactivityJSONArray extends AsyncTask<String, String, String> {

        String result = null;
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

            //connection timeout
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            //.......

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                data_model_arraylist = new ArrayList<MonthDetailModal>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "monthlytransactionRoomchange"));
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id_value));//1318
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanentuserid));//1218
                nameValuePairs.add(new BasicNameValuePair("month_id", month_id_value));//3
                nameValuePairs.add(new BasicNameValuePair("year_id", yearvalue));//2018
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        data_model = new MonthDetailModal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        data_model.setDate(jsonobj.getString("transaction_date"));
                        data_model.setMonth(jsonobj.getString("month"));
                        data_model.setMonthlypayment(jsonobj.getString("total_amount"));
                        data_model.setPaid(jsonobj.getString("paid"));
                        data_model.setDue(jsonobj.getString("remaining_amount"));
                        data_model.setMode(jsonobj.getString("transaction_mode"));
                        data_model.setOwner_id(jsonobj.getString("owner_id"));
                        data_model.setRoom_id(jsonobj.getString("room_id"));
                        data_model.setMonth_id(jsonobj.getString("month_id"));
                        data_model.setPaymentcomment(jsonobj.getString("payment_comment"));
                        data_model_arraylist.add(data_model);
                    }
                }
            } catch (Exception e) {
                IsError = true;
                // Log.v("Class Name Function Name Exception", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);

            if (result.equalsIgnoreCase("Y")) {
                ShowMonthDetailOwnerAdapter adapter = new ShowMonthDetailOwnerAdapter(OldPayment_ShowMonthDetailOwner_RoomChange.this, data_model_arraylist, property_id_value, user_id_value);
                lvCartItems.setAdapter(adapter);
            } else if (result.equals("N")) {
                Toast.makeText(OldPayment_ShowMonthDetailOwner_RoomChange.this, "not data", Toast.LENGTH_LONG).show();
            }

        }
    }
    //............
    //pdf creator <code>


    //electricity transaction


    //this class is used getting value by ownerendmonthdetails webservice for
    private class ElectricityListviewactivityJSONArray extends AsyncTask<String, String, String> {

        String result = null;
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

            //connection timeout
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, Common.timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, Common.timeoutSocket);
            //.......

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                data_model_arraylist = new ArrayList<MonthDetailModal>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "electricitytransaction"));
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id_value));//1318
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanentuserid));//1218
                nameValuePairs.add(new BasicNameValuePair("month_id", month_id_value));//3
                nameValuePairs.add(new BasicNameValuePair("year_id", yearvalue));//2018
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        data_model = new MonthDetailModal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        String transaction_date = jsonobj.getString("transaction_date");
                        try {
                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String inputDateStr = transaction_date;
                            Date date = inputFormat.parse(inputDateStr);
                            String outputDateStr = outputFormat.format(date);
                            data_model.setDate(outputDateStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data_model.setMonth(jsonobj.getString("month"));
                        data_model.setMonthlypayment(jsonobj.getString("unit_difference"));//unit difference
                        data_model.setPaid(jsonobj.getString("paid_amount"));
                        data_model.setDue(jsonobj.getString("remaining_amount"));
                        data_model.setMode(jsonobj.getString("transaction_mode"));
                        data_model.setOwner_id("0");
                        data_model.setRoom_id("0");
                        data_model.setMonth_id(jsonobj.getString("month_id"));
                        data_model.setPaymentcomment(jsonobj.getString("payment_comment"));
                        data_model_arraylist.add(data_model);
                    }
                }
            } catch (Exception e) {
                IsError = true;
                // Log.v("Class Name Function Name Exception", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                ShowMonthDetailOwnerAdapter adapter = new ShowMonthDetailOwnerAdapter(OldPayment_ShowMonthDetailOwner_RoomChange.this, data_model_arraylist, property_id_value, user_id_value);
                lvCartItems.setAdapter(adapter);
            } else if (result.equals("N")) {
                Toast.makeText(OldPayment_ShowMonthDetailOwner_RoomChange.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }
    //............
    //pdf creator <code>


    //

    private void createPDF() {

        Document doc = new Document();
        try {

            String path = Environment.getExternalStorageDirectory() + "/Krooms";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir, "MonthlyReportRoomChange" + i1 + TenantDetailActivity.tanentfinalname + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);


            //open the document
            doc.open();
            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 1);

            PdfPTable table8 = new PdfPTable(3);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.iconk1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setBackgroundColor(BaseColor.WHITE);
            table8.addCell(new PdfPCell(myImg, true));
            table8.addCell(createValueCell11("Fineest Business Solution"));
            preface.add(table8);
            addEmptyLine(preface, 1);


            PdfPTable table7 = new PdfPTable(1);
            table7.addCell(createValueCell1("Monthly Payment"));
            preface.add(table7);
            addEmptyLine(preface, 1);

            PdfPTable table22 = new PdfPTable(1);
            table22.addCell(createValueCell1(propertyname));
            preface.add(table22);
            addEmptyLine(preface, 1);

            PdfPTable table4 = new PdfPTable(1);
            table4.addCell(createValueCell5("Date :" + currentdate_value));
            preface.add(table4);
            addEmptyLine(preface, 1);

            PdfPTable table2 = new PdfPTable(1);
            table2.addCell(createValueCell22("Dear " + TenantDetailActivity.tanentfinalname + ","));
            preface.add(table2);
            addEmptyLine(preface, 1);

            PdfPTable table3 = new PdfPTable(1);
            table3.addCell(createValueCell3("Thank you for using KROOMS ! Your monthly tarnsaction report details are as follows:"));
            preface.add(table3);
            addEmptyLine(preface, 1);

            PdfPTable table1 = new PdfPTable(7);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
            PdfPCell ch, c1, c2, c3, c4, c5, c6, c7, c8;
            float[] columnWidths = new float[]{50f, 35f, 40f, 40f, 30f, 30f, 50f};
            table1.setWidths(columnWidths);

            ch = new PdfPCell(new Paragraph("Payment Report"));
            ch.setColspan(7);
            ch.setHorizontalAlignment(Element.ALIGN_CENTER);
            ch.setPadding(10.0f);
            ch.setBackgroundColor(BaseColor.WHITE);
            table1.addCell(ch);

            c1 = new PdfPCell(new Phrase("Date", font));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            //c1.setNoWrap(false);
            c1.setFixedHeight(35f);
            table1.addCell(c1);

            c2 = new PdfPCell(new Phrase("Month", font));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(35f);
            table1.addCell(c2);

            c3 = new PdfPCell(new Phrase("Monthly Payment", font));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            //c3.setNoWrap(false);
            c3.setFixedHeight(35f);
            table1.addCell(c3);

            c4 = new PdfPCell(new Phrase("Total Payment", font));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setFixedHeight(35f);
            //table1.addCell(c4);

            c5 = new PdfPCell(new Phrase("Paid", font));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setFixedHeight(35f);
            table1.addCell(c5);

            c6 = new PdfPCell(new Phrase("Due", font));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setFixedHeight(35f);
            table1.addCell(c6);

            c7 = new PdfPCell(new Phrase("Mode", font));
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setFixedHeight(35f);
            table1.addCell(c7);


            c8 = new PdfPCell(new Phrase("Comment", font));
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setFixedHeight(50f);
            table1.addCell(c8);


            for (int i = 0; i < data_model_arraylist.size(); i++) {

                table1.addCell(ChangeSize(data_model_arraylist.get(i).getDate())); // rows for first column
                table1.addCell(data_model_arraylist.get(i).getMonth());  // rows for seconds column
                table1.addCell(data_model_arraylist.get(i).getMonthlypayment());
                table1.addCell(data_model_arraylist.get(i).getPaid());
                table1.addCell(data_model_arraylist.get(i).getDue());
                table1.addCell(data_model_arraylist.get(i).getMode());
                table1.addCell(data_model_arraylist.get(i).getPaymentcomment());
            }
            doc.add(preface);
            doc.add(table1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

    }

    public PdfPCell createValueCell6(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellname4.setVerticalAlignment(Element.ALIGN_LEFT);
        cellname4.setBorder(0);
        return cellname4;
    }

    private PdfPCell ChangeSize(String name1) {

        PdfPCell cell1 = new PdfPCell(new Phrase(name1));
        cell1.setFixedHeight(25f);
        return cell1;
    }
    //pdf code end


    public void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
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

    public PdfPCell createValueCell5(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(0, 0, 0));
        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellname4.setVerticalAlignment(Element.ALIGN_RIGHT);
        cellname4.setBorder(0);
        return cellname4;
    }

    public PdfPCell createValueCell11(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellname4.setBorder(0);
        cellname4.setColspan(2);
        return cellname4;
    }

}
