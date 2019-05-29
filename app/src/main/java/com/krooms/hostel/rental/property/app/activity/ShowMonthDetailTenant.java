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
import com.krooms.hostel.rental.property.app.adapter.ShowMonthDetailOwnerAdapter;
import com.krooms.hostel.rental.property.app.adapter.ShowMonthDetailTenantAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Data_Model;
import com.krooms.hostel.rental.property.app.modal.MonthDetailModal;
import com.krooms.hostel.rental.property.app.modal.MonthDetailModalTenant;
import com.krooms.hostel.rental.property.app.modal.PdfUserModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;

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


/**
 * Created by admin on 11/10/2016.
 */
public class ShowMonthDetailTenant extends Activity
{
    ArrayList<MonthDetailModalTenant> data_model_arraylist;
    MonthDetailModalTenant data_model;
    SharedStorage mShared;
    ArrayList<PdfUserModel> list;
    RelativeLayout backbutton;
    ArrayList<String> montharray;
    ArrayList<Data_Model>  dataarray;
    Data_Model datamodel;
    Button downloadslip;
    ListView lvCartItems;
    RelativeLayout editmaode;
    Intent in;
    TextView headername,duetextview;
    Animation rotation;
    ImageView student_loader;
    String user_id_value,property_id_value,tenant_idvalue,monthidv;
    String currentdate_value;
    String tenant_name_value,type="";
    LinearLayout duelayout;
    public  static String tiid="",property_name_value="",yearvalue="";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listgnewitemactivitymonth);

        duetextview=(TextView)findViewById(R.id.duetextview);
        duetextview.setVisibility(View.INVISIBLE);
//        duetextview.setText(TenantDetailActivity.Dueamount);
        headername=(TextView)findViewById(R.id.headername);
        lvCartItems=(ListView)findViewById(R.id.lvCartItems);
        student_loader=(ImageView)findViewById(R.id.student_loader);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        backbutton=(RelativeLayout)findViewById(R.id.back_button);
        downloadslip=(Button)findViewById(R.id.downloadslip);
        duelayout=(LinearLayout)findViewById(R.id.duelayout);
        duelayout.setVisibility(View.INVISIBLE);

        Calendar calendar = Calendar.getInstance();
        int dateO=calendar.get(Calendar.DAY_OF_MONTH);
        int monthO=calendar.get(Calendar.MONTH);
        int yearO=calendar.get(Calendar.YEAR);
        currentdate_value =dateO+"/"+(monthO+1)+"/"+yearO;

        Intent inn=getIntent();
        //get ids by shared preference
        mShared = SharedStorage.getInstance(this);

        type= inn.getStringExtra("type");
        user_id_value=inn.getStringExtra("tanentuserid");
        tenant_idvalue=inn.getStringExtra("tenantid");
        tenant_name_value=inn.getStringExtra("user_name");
        property_name_value=inn.getStringExtra("property_name");
        monthidv=inn.getStringExtra("monthid");
        property_id_value=inn.getStringExtra("tanentpropertyid");
        yearvalue=inn.getStringExtra("yearvalue");

        headername.setText("Monthly Report");


        if(type.equalsIgnoreCase("1")){
            new ElectricityListviewactivityJSONArray().execute();

        }else {
            // this is tanent detail class calling
            new ListviewactivityJSONArray().execute();
        }
        //this is for back move
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        dataarray=new ArrayList<Data_Model>();
        datamodel=new Data_Model();
        datamodel.setDue("Due");
        datamodel.setMode("Cash");
        datamodel.setPaid("Paid");
        datamodel.setDate("Payment Date");
        datamodel.setTotalamount("Total Paid");
        datamodel.setMonthlypayment("Total Payment");
        dataarray.add(datamodel);
        datamodel=new Data_Model();
        // static data for pdf dowenload slip
        list=new ArrayList<PdfUserModel>();

        downloadslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPDF();
                Toast.makeText(ShowMonthDetailTenant.this,"Payment Report Downloaded ",Toast.LENGTH_SHORT).show();
            }
        });

    }
    //pdf creator <code>
    public class ListviewactivityJSONArray extends AsyncTask<String, String, String> {
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
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                data_model_arraylist=new ArrayList<MonthDetailModalTenant>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action","monthlytransaction"));
                nameValuePairs.add(new BasicNameValuePair("user_id",user_id_value));
                nameValuePairs.add(new BasicNameValuePair("property_id",property_id_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",tenant_idvalue));
                nameValuePairs.add(new BasicNameValuePair("month_id",monthidv));
                nameValuePairs.add(new BasicNameValuePair("year_id",yearvalue));

                Log.d("lkl ",""+tiid);
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y"))
                {
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i =0; i <jsonarray.length(); i++)
                    {
                        data_model=new MonthDetailModalTenant();
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
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
                if (result.equalsIgnoreCase("Y"))
                {
                    ShowMonthDetailTenantAdapter adapter=new ShowMonthDetailTenantAdapter(ShowMonthDetailTenant.this,data_model_arraylist,property_id_value,user_id_value);
                    lvCartItems.setAdapter(adapter);
                } else if (result.equals("N")) {
                    Toast.makeText(ShowMonthDetailTenant.this,message, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(ShowMonthDetailTenant.this,"please check network connection", Toast.LENGTH_LONG).show();
            }
        }
    }
    //............

    private void createPDF() {

        Document doc = new Document();
        try {

            String path= Environment.getExternalStorageDirectory()+"/Krooms";
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir,"Monthly Report"+i1+" "+TenantDetailActivity.tanentfinalname+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
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
            table7.addCell(createValueCell1("Monthly Report"));
            preface.add(table7);
            addEmptyLine(preface, 1);
            PdfPTable table4= new PdfPTable(1);
            table4.addCell(createValueCell5("Date :" + currentdate_value));
            preface.add(table4);
            addEmptyLine(preface, 1);

            PdfPTable table2 = new PdfPTable(1);
            table2.addCell(createValueCell22("Dear "+TenantDetailActivity.tanentfinalname+","));
            preface.add(table2);
            addEmptyLine(preface, 1);

            PdfPTable table3 = new PdfPTable(1);
            table3.addCell(createValueCell3("Thank you for using KROOMS ! Your monthly tarnsaction report details are as follows:"));
            preface.add(table3);
            addEmptyLine(preface, 1);

            PdfPTable table1 = new PdfPTable(7);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
            PdfPCell ch,c1,c2,c3,c4,c5,c6,c7,c8;
            float[] columnWidths = new float[] {50f,35f,40f,40f,30f,30f,50f};
            table1.setWidths(columnWidths);

            ch=new PdfPCell(new Paragraph("Monthly Report"));
            ch.setColspan(7);
            ch.setHorizontalAlignment(Element.ALIGN_CENTER);
            ch.setPadding(10.0f);
            ch.setBackgroundColor(BaseColor.WHITE);
            table1.addCell(ch);

            c1 = new PdfPCell(new Phrase("Date",font));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setFixedHeight(35f);
            table1.addCell(c1);

            c2 = new PdfPCell(new Phrase("Month",font));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(35f);
            table1.addCell(c2);

            c3 = new PdfPCell(new Phrase("Monthly Payment",font));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setFixedHeight(35f);
            table1.addCell(c3);

            c4 = new PdfPCell(new Phrase("Total Payment",font));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setFixedHeight(35f);

            c5 = new PdfPCell(new Phrase("Paid",font));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setFixedHeight(35f);
            table1.addCell(c5);

            c6 = new PdfPCell(new Phrase("Due",font));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setFixedHeight(35f);
            table1.addCell(c6);

            c7 = new PdfPCell(new Phrase("Mode",font));
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setFixedHeight(35f);
            table1.addCell(c7);

            c8 = new PdfPCell(new Phrase("Comment",font));
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setFixedHeight(50f);
            table1.addCell(c8);

            String name1=null,address=null,city=null,state=null,country=null,img=null,due=null;
            Data_Model userList=new Data_Model();
            for(int i=0;i<data_model_arraylist.size();i++){
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
        }
        finally
        {
            doc.close();
        }
    }

    private PdfPCell ChangeSize(String name1) {
        PdfPCell cell1 = new PdfPCell(new Phrase(name1));
        cell1.setFixedHeight(25f);
        return cell1;
    }

    public void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public  PdfPCell createValueCell1(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,22, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellname4.setBorder(0);
        cellname4.setColspan(2);
        return cellname4;
    }

    public  PdfPCell createValueCell22(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,18, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setBorder(0);
        return cellname4;
    }

    public  PdfPCell createValueCell3(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,18, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setBorder(0);
        return cellname4;
    }

    public  PdfPCell createValueCell5(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,18, Font.NORMAL, new BaseColor(0, 0, 0));
        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellname4.setVerticalAlignment(Element.ALIGN_RIGHT);
        cellname4.setBorder(0);
        return cellname4;
    }
    public  PdfPCell createValueCell11(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,22, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellname4.setBorder(0);
        cellname4.setColspan(2);
        return cellname4;
    }

    //this class is used getting value by ownerendmonthdetails webservice for
    public class ElectricityListviewactivityJSONArray extends AsyncTask<String, String, String> {
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

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                data_model_arraylist=new ArrayList<MonthDetailModalTenant>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action","electricitytransaction"));
                nameValuePairs.add(new BasicNameValuePair("user_id",user_id_value));
                nameValuePairs.add(new BasicNameValuePair("property_id",property_id_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",tenant_idvalue));
                nameValuePairs.add(new BasicNameValuePair("month_id",monthidv));
                nameValuePairs.add(new BasicNameValuePair("year_id",yearvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y"))
                {
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i =0; i <jsonarray.length(); i++)
                    {
                        data_model=new MonthDetailModalTenant();
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        String transaction_date=  jsonobj.getString("transaction_date");
                        try{
                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String inputDateStr=transaction_date;
                            Date date = inputFormat.parse(inputDateStr);
                            String outputDateStr = outputFormat.format(date);
                            data_model.setDate(outputDateStr);
                        }catch (Exception e){
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
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);

                if (result.equalsIgnoreCase("Y"))
                {
                    ShowMonthDetailTenantAdapter adapter=new ShowMonthDetailTenantAdapter(ShowMonthDetailTenant.this,data_model_arraylist,property_id_value,user_id_value);
                    lvCartItems.setAdapter(adapter);
                } else if (result.equals("N")) {
                    Toast.makeText(ShowMonthDetailTenant.this,message, Toast.LENGTH_LONG).show();
                }
        }
    }
}
