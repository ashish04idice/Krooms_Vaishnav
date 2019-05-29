package com.krooms.hostel.rental.property.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
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
import java.util.List;
import java.util.Random;

public class PDFFileNewPage_Tanentform extends AppCompatActivity {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, BaseColor.RED);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Context context;
    public static String name,payment,month,tanentusrid_value;
    Animation rotation;
    public static String currentdate_value,tanentroomid_value,monthid_value,
            tanentownerid_value,tanentpropertyid_value,tenantkeyid,
            tenantroomno,tenantkeypropertyid;
    String roomnovalue,tanantidvalue;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        in=getIntent();
        tanentpropertyid_value=in.getStringExtra("property_id");
        roomnovalue=in.getStringExtra("roomno");
        tanantidvalue=in.getStringExtra("keyid");

        try{
            Document document = new Document();
            String path= Environment.getExternalStorageDirectory()+"/Krooms";
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir,"PaySlip"+i1+".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            CreatePdf(document);
            addTitlePage(document,in.getStringExtra("date"),
                    in.getStringExtra("name"), in.getStringExtra("uid"),
                    in.getStringExtra("amount"), in.getStringExtra("mode"),
                    in.getStringExtra("month"),
                    tenantkeyid,tenantroomno,tanentpropertyid_value,in.getStringExtra("propertyname"));
            addMetaData(document);
            Toast.makeText(PDFFileNewPage_Tanentform.this, "Paymentslip is download successfully ", Toast.LENGTH_LONG).show();
            document.close();
            startActivity(new Intent(PDFFileNewPage_Tanentform.this,TanentactivityFrom.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //new PropertyIdKeyParser().execute();

}

    public static void addMetaData(Document document) {
        document.addTitle("Payment Slip");
        document.addSubject("Details for payment transaction");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public void addTitlePage(Document document,String date,String name,String tId,String amount,String mode,String month,String tenantkeyid,String tenantroomno,String propertyid,String propertyname)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);

        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(createValueCell1("Payment Slip"));
        preface.add(table1);
        addEmptyLine(preface, 1);

        PdfPTable table22 = new PdfPTable(1);
        table22.addCell(createValueCell1(propertyname));
        preface.add(table22);
        addEmptyLine(preface,1);

        PdfPTable table4= new PdfPTable(1);
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
        table.addCell(createValueCell4(tanantidvalue));

        table.addCell(createValueCell2("Room No."));
        table.addCell(createValueCell4(roomnovalue));

        table.addCell(createValueCell2("Property Id"));
        table.addCell(createValueCell4(propertyid));

        table.addCell(createValueCell2("Paid Amount"));
        table.addCell(createValueCell4(amount));

        table.addCell(createValueCell2("Paid For Month"));
        table.addCell(createValueCell4(month));
        addEmptyLine(preface, 1);

        table.addCell(createValueCell2("Payment Mode"));
        table.addCell(createValueCell4(mode));
        addEmptyLine(preface, 1);

        PdfPTable table6 = new PdfPTable(1);
        table6.addCell(createValueCell6("Address : A-3, Pawansut Appts, Khamla road"));
        table6.addCell(createValueCell6("Dev Nagar,Nagpur - 440025, Maharashtra, India"));
        table6.addCell(createValueCell6("Contact No: 9926914699"));
        table6.addCell(createValueCell6("Email : booking@krooms.in"));
        addEmptyLine(preface, 0);

        preface.add(11,table);
        preface.add(14,table6);//15
        document.add(preface);
        document.newPage();
    }

    public void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void CreatePdf(Document document) {
        try
        {
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

    public  PdfPCell createValueCell1(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,22, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellname4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellname4.setBorder(0);
        cellname4.setColspan(2);
        return cellname4;
    }

    public  PdfPCell createValueCell2(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,18, Font.NORMAL, new BaseColor(255, 102, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
        cellname4.setPaddingBottom(5);
        cellname4.setPaddingLeft(5);
        cellname4.setPaddingRight(5);
        cellname4.setPaddingTop(5);
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

    public  PdfPCell createValueCell6(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,16, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name,font));
        cellname4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellname4.setVerticalAlignment(Element.ALIGN_LEFT);
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
    public  PdfPCell createValueCell4(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,18, Font.NORMAL, new BaseColor(0, 0, 0));

        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_BASELINE);
        cellname4.setVerticalAlignment(Element.ALIGN_BASELINE);
        cellname4.setPaddingBottom(5);
        cellname4.setPaddingLeft(5);
        cellname4.setPaddingRight(5);
        cellname4.setPaddingTop(5);
        return cellname4;
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
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("status");
                if (result.equals("true")) {
                    JSONArray jArray = jsmain.getJSONArray(WebUrls.DATA_JSON);
                    tenantkeypropertyid=jArray.getJSONObject(0).getString("key_property_information");
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

            if(result.equalsIgnoreCase("true"))
            {



            } else {
            }
        }

    }

}
