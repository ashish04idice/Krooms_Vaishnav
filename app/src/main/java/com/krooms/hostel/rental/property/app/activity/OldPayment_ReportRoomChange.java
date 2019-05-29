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
import com.krooms.hostel.rental.property.app.adapter.OldPaymnet_ListAdapter_RoomChange;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Data_Model;
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
import java.util.Locale;
import java.util.Random;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


//Module :Monthly Payment : Show monthly payment ,Payment Edit ,Report download in pdf format
//Dev :Riya / Anuradha
// 8 Nov 2016

/**
 * Created by admin on 11/10/2016.
 */
public class OldPayment_ReportRoomChange extends Activity {
    ArrayList<Data_Model> data_model_arraylist;
    Data_Model data_model;
    String Dueamount = "";
    ArrayList<PdfUserModel> list;
    RelativeLayout backbutton, btn_oldpaymentreport;
    Button downloadslip, downloadxl;
    ListView lvCartItems;
    Intent in;
    TextView headername, duetextview;
    Animation rotation;
    ImageView student_loader;
    public static String property_name = "", user_id_value = "", property_id_value = "", tanentuserid = "";
    PropertyUserModal tanentdatamain;
    String currentdate_value, tenant_name_value;
    String Activitystatus = "0";
    SharedStorage mShared;
    String propertyname = "";

    LinearLayout laydue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listgnewitemactivity);
        //find value through get insatance
        tanentdatamain = getIntent().getParcelableExtra("model");
        in = getIntent();
        mShared = SharedStorage.getInstance(this);
        propertyname = mShared.getPropertyname();
        user_id_value = in.getStringExtra("user_id_data");
        tanentuserid = in.getStringExtra("tid");
        tenant_name_value = in.getStringExtra("user_name");
        property_name = in.getStringExtra("property_name");
        String nm = TenantDetailActivity.tanentfinalname;
        Calendar calendar = Calendar.getInstance();
        int dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);
        currentdate_value = dateO + "/" + (monthO + 1) + "/" + yearO;
        // these is for loader rotation
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        //id get by xml
        duetextview = (TextView) findViewById(R.id.duetextview);
        headername = (TextView) findViewById(R.id.headername);
        student_loader = (ImageView) findViewById(R.id.student_loader);
        backbutton = (RelativeLayout) findViewById(R.id.back_button);
        downloadslip = (Button) findViewById(R.id.downloadslip);
        downloadxl = (Button) findViewById(R.id.downloadxl);
        lvCartItems = (ListView) findViewById(R.id.lvCartItems);
        btn_oldpaymentreport = (RelativeLayout) findViewById(R.id.btn_oldpaymentreport);
        property_id_value = in.getStringExtra("property_id_data");
        headername.setText("Tenant Monthly Report RoomChange");
        laydue = (LinearLayout) findViewById(R.id.duelayout);
        laydue.setVisibility(View.GONE);
        btn_oldpaymentreport.setVisibility(View.GONE);
        //here call list activity class
        new ListviewactivityJSONArray().execute();
        new DueAmountGetOwner().execute();
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
                createPDF();
                Toast.makeText(OldPayment_ReportRoomChange.this, "Payment Report Downloaded ", Toast.LENGTH_SHORT).show();
            }
        });

        downloadxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createXL();
                Toast.makeText(OldPayment_ReportRoomChange.this, "Payment Report Downloaded ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createXL() {

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;

        String Fnamexls = tenant_name_value + i1 + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/KroomsXl");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            int a = 1;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.createSheet("MonthlyReport_RoomChnage", 0);
                   /* Label label = new Label(0,2, "SECOND");
                    Label label1 = new Label(0,1,"first");*/
            WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
            cellFont.setColour(Colour.ORANGE);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(Alignment.CENTRE);

            Label header = new Label(0, 0, propertyname);
            Label labelid = new Label(0, 1, "Tenant_Name");
            Label labeldate = new Label(1, 1, "Date");
            Label labelmonth = new Label(2, 1, "Month");
            Label labelmonthly = new Label(3, 1, "Monthly Payment");
            // Label labeltotal = new Label(4, 0, "Total Payment");
            Label labelpaid = new Label(4, 1, "Paid");
            Label labeldue = new Label(5, 1, "Due");
            Label labelmode = new Label(6, 1, "Mode");
                   /* Label label4 = new Label(1,1,String.valueOf(a));*/
            try {
                sheet.mergeCells(0, 0, 6, 0);
                header.setCellFormat(cellFormat);
                sheet.addCell(header);
                sheet.addCell(labelid);
                sheet.addCell(labeldate);
                sheet.addCell(labelmonth);
                sheet.addCell(labelmonthly);
                //sheet.addCell(labeltotal);
                sheet.addCell(labelpaid);
                sheet.addCell(labeldue);
                sheet.addCell(labelmode);
                sheet.addCell(new Label(0, 2, tenant_name_value));
                for (int i = 0; i < data_model_arraylist.size(); i++) {

                    int j = i + 2;
                    sheet.addCell(new Label(1, j, data_model_arraylist.get(i).getTransactiondate()));

                    if (data_model_arraylist.get(i).getType().equalsIgnoreCase("1")) {
                        sheet.addCell(new Label(2, j, "Elc/" + data_model_arraylist.get(i).getMonth() + "/" + data_model_arraylist.get(i).getYear()));
                    } else {
                        sheet.addCell(new Label(2, j, data_model_arraylist.get(i).getMonth() + "/" + data_model_arraylist.get(i).getYear()));
                        // rows for seconds column
                    }
                    sheet.addCell(new Label(3, j, data_model_arraylist.get(i).getMonthlypayment()));
                    sheet.addCell(new Label(4, j, data_model_arraylist.get(i).getPaid()));
                    sheet.addCell(new Label(5, j, data_model_arraylist.get(i).getDue()));
                    sheet.addCell(new Label(6, j, data_model_arraylist.get(i).getMode()));
                }

            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            expandColumns(sheet, 7);
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //createExcel(excelSheet);
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    private void expandColumns(WritableSheet sheet, int amountOfColumns) {

        int c = amountOfColumns;

        CellView cell = sheet.getColumnView(0);
        cell.setSize(5000);

        sheet.setColumnView(0, cell);

        CellView cell1 = sheet.getColumnView(1);
        cell1.setSize(3000);
        sheet.setColumnView(1, cell1);

        CellView cell2 = sheet.getColumnView(2);
        cell2.setSize(6000);
        sheet.setColumnView(2, cell2);

        CellView cell3 = sheet.getColumnView(3);
        cell3.setSize(4000);
        sheet.setColumnView(3, cell3);

        CellView cell4 = sheet.getColumnView(4);
        cell4.setSize(3000);
        sheet.setColumnView(4, cell4);


        CellView cell5 = sheet.getColumnView(5);
        cell5.setSize(3000);
        sheet.setColumnView(5, cell5);

        CellView cell6 = sheet.getColumnView(6);
        cell6.setSize(6000);
        sheet.setColumnView(6, cell5);

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
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                data_model_arraylist = new ArrayList<Data_Model>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "ownerendoldmonthdetailsafterroomchange"));
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanentuserid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {
                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        data_model = new Data_Model();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        if (jsonobj.getString("paid").equals("0")) {
                            data_model.setDate("");
                            data_model.setMonth(jsonobj.getString("month"));
                            data_model.setMonthlypayment(jsonobj.getString("total_amount"));
                            data_model.setPaid(jsonobj.getString("paid"));
                            data_model.setYear(jsonobj.getString("year"));
                            data_model.setTransactiondate("");
                            data_model.setDue(jsonobj.getString("remaining_amount"));
                            data_model.setMode(jsonobj.getString("transaction_mode"));
                            data_model.setMonth_date(jsonobj.getString("month_date"));
                            data_model.setOwner_id(jsonobj.getString("owner_id"));
                            data_model.setRoom_id(jsonobj.getString("room_id"));
                            data_model.setMonth_id(jsonobj.getString("month_id"));
                            data_model.setType("0");
                            data_model_arraylist.add(data_model);
                        } else {
                            data_model.setDate(jsonobj.getString("transaction_date"));
                            data_model.setMonth(jsonobj.getString("month"));
                            data_model.setTransactiondate(jsonobj.getString("transaction_date"));
                            data_model.setMonthlypayment(jsonobj.getString("total_amount"));
                            data_model.setPaid(jsonobj.getString("paid"));
                            data_model.setYear(jsonobj.getString("year"));
                            data_model.setDue(jsonobj.getString("remaining_amount"));
                            data_model.setMode(jsonobj.getString("transaction_mode"));
                            data_model.setMonth_date(jsonobj.getString("month_date"));
                            data_model.setOwner_id(jsonobj.getString("owner_id"));
                            data_model.setRoom_id(jsonobj.getString("room_id"));
                            data_model.setMonth_id(jsonobj.getString("month_id"));
                            data_model.setType("0");
                            data_model_arraylist.add(data_model);
                        }


                        //new chnages 21/12/2018
                        if (jsonobj.getString("month_id").equals("0")) {
                            data_model.setTransactiondate(jsonobj.getString("transaction_date"));
                        }
                        //end
                    }
                    //fine data array list
                    JSONArray json_fine_array = jsmain.getJSONArray("user_records");
                    for (int i = 0; i < json_fine_array.length(); i++) {
                        data_model = new Data_Model();
                        JSONObject jsonobj = json_fine_array.getJSONObject(i);
                        data_model.setDate(jsonobj.getString("transaction_date"));
                        data_model.setMonth(jsonobj.getString("month"));
                        String datetime = jsonobj.getString("transaction_date");
                        data_model.setTransactiondate(datetime);
                        data_model.setMonthlypayment(jsonobj.getString("total_amount"));
                        data_model.setPaid(jsonobj.getString("paid"));
                        data_model.setYear(jsonobj.getString("year"));
                        data_model.setDue(jsonobj.getString("remaining_amount"));
                        data_model.setMode(jsonobj.getString("transaction_mode"));
                        data_model.setMonth_date(jsonobj.getString("month_date"));
                        data_model.setOwner_id(jsonobj.getString("owner_id"));
                        data_model.setRoom_id(jsonobj.getString("room_id"));
                        data_model.setMonth_id(jsonobj.getString("month_id"));
                        data_model.setTitle(jsonobj.getString("header_month_value"));
                        data_model.setType("0");
                        data_model_arraylist.add(data_model);
                    }
                    //for electricity bill
                    JSONArray json_electricity_array = jsmain.getJSONArray("electricity_records");
                    for (int i = 0; i < json_electricity_array.length(); i++) {
                        data_model = new Data_Model();
                        JSONObject jsonobj = json_electricity_array.getJSONObject(i);

                        data_model.setMonth_id(jsonobj.getString("month_id"));
                        data_model.setMonth(jsonobj.getString("month"));
                        // String datetime = jsonobj.getString("transaction_date");
                        //data_model.setTransactiondate(datetime);
                        data_model.setYear(jsonobj.getString("year"));


                        data_model.setMonthlypayment(jsonobj.getString("unit_difference"));//unit difference
                        data_model.setPaid(jsonobj.getString("paid_amount"));

                        data_model.setDue(jsonobj.getString("remaining_amount"));
                        // data_model.setDue(jsonobj.getString("amount"));
                        data_model.setMode(jsonobj.getString("transaction_mode"));
                        //data_model.setMonth_date(jsonobj.getString("month_date"));
                        //data_model.setOwner_id(jsonobj.getString("owner_id"));
                        data_model.setRoom_id(jsonobj.getString("room_id"));

                        String added_date = jsonobj.getString("added_date");
                        try {

                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String inputDateStr = added_date;
                            Date date = inputFormat.parse(inputDateStr);
                            String outputDateStr = outputFormat.format(date);
                            data_model.setDate(outputDateStr);

                            data_model.setTransactiondate(outputDateStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        // data_model.setTitle(jsonobj.getString("header_month_value"));
                        data_model.setType("1"); //electricity
                        data_model_arraylist.add(data_model);
                    }
                    //

                }
            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            student_loader.clearAnimation();
            student_loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                OldPaymnet_ListAdapter_RoomChange adapter = new OldPaymnet_ListAdapter_RoomChange(OldPayment_ReportRoomChange.this, data_model_arraylist, property_id_value, user_id_value);
                lvCartItems.setAdapter(adapter);
            } else if (result.equals("N")) {
                Toast.makeText(OldPayment_ReportRoomChange.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }

    //............
    //pdf creator <code>
    //ashish
    private void createPDF() {

        Document doc = new Document();
        try {

            String path = Environment.getExternalStorageDirectory() + "/Krooms";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir, "MonthlyReport_RoomChange" + i1 + tenant_name_value + ".pdf");
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
            table8.addCell(createValueCell11("Finest Business Solution"));
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
            table2.addCell(createValueCell22("Dear " + tenant_name_value + ","));
            preface.add(table2);
            addEmptyLine(preface, 1);

            PdfPTable table3 = new PdfPTable(1);
            table3.addCell(createValueCell3("Thank you for using KROOMS ! Your monthly tarnsaction report details are as follows:"));
            preface.add(table3);
            addEmptyLine(preface, 1);

            PdfPTable table1 = new PdfPTable(6);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
            PdfPCell ch, c1, c2, c3, c4, c5, c6, c7;
            float[] columnWidths = new float[]{50f, 50f, 40f, 40f, 30f, 30f};
            table1.setWidths(columnWidths);

            ch = new PdfPCell(new Paragraph("Payment Report"));
            ch.setColspan(6);
            ch.setHorizontalAlignment(Element.ALIGN_CENTER);
            ch.setPadding(10.0f);
            ch.setBackgroundColor(BaseColor.WHITE);
            table1.addCell(ch);

            c1 = new PdfPCell(new Phrase("Date", font));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setFixedHeight(50f);
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


            for (int i = 0; i < data_model_arraylist.size(); i++) {

                table1.addCell(ChangeSize(data_model_arraylist.get(i).getTransactiondate())); // rows for first column

                if (data_model_arraylist.get(i).getType().equalsIgnoreCase("1")) {
                    table1.addCell("Elc/" + data_model_arraylist.get(i).getMonth() + "/" + data_model_arraylist.get(i).getYear());
                } else {
                    table1.addCell(data_model_arraylist.get(i).getMonth() + "/" + data_model_arraylist.get(i).getYear());
                    // rows for seconds column
                }
                table1.addCell(data_model_arraylist.get(i).getMonthlypayment());
                table1.addCell(data_model_arraylist.get(i).getPaid());
                table1.addCell(data_model_arraylist.get(i).getDue());
                table1.addCell(data_model_arraylist.get(i).getMode());
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


    public class DueAmountGetOwner extends AsyncTask<String, String, String> {

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
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "tenantremaining_amount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id_value));
                nameValuePairs.add(new BasicNameValuePair("property_id", property_id_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tanentuserid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Dueamount = jsonarray.getJSONObject(i).getString("remaining_amount");
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
                duetextview.setText(Dueamount);
            } else if (result.equals("N")) {
            }

        }
    }
}
