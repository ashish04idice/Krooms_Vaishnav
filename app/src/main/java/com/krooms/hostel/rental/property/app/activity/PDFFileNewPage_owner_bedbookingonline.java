package com.krooms.hostel.rental.property.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.SharedStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class PDFFileNewPage_owner_bedbookingonline extends AppCompatActivity {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, BaseColor.RED);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Context context;
    public static String date,keypropertyid,roomno,totalamount,paid,due,mode,Chequeno,PropertyIdMain,transaction_id,room_id,propertyname="";

    Animation rotation;

    private SharedStorage mSharedStorage;
   /* public static String currentdate_value, tanentroomid_value, monthid_value,
            tanentownerid_value, tanentpropertyid_value, tenantid, propertyid,
            roomno, keyidtenant;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedStorage = SharedStorage.getInstance(this);
        Intent in = getIntent();
        date = in.getStringExtra("date");
        keypropertyid = in.getStringExtra("propertyid");
        roomno = in.getStringExtra("roomno");
        totalamount = in.getStringExtra("totalamount");
        paid = in.getStringExtra("paid");
        due = in.getStringExtra("due");
        mode = in.getStringExtra("mode");
        Chequeno = in.getStringExtra("Chequeno");
        PropertyIdMain = in.getStringExtra("PropertyIdMain");
        transaction_id = in.getStringExtra("transaction_id");
        room_id = in.getStringExtra("room_id");
        propertyname= in.getStringExtra("propertyname");


        try {
            Document document = new Document();
            String path = Environment.getExternalStorageDirectory() + "/Krooms";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir, "BookingPaySlip" + i1 + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            CreatePdf(document);
            addTitlePage(document,date,keypropertyid,roomno,totalamount,paid,due,mode,Chequeno,propertyname);
            addMetaData(document);
            Toast.makeText(PDFFileNewPage_owner_bedbookingonline.this, "Paymentslip is download successfully", Toast.LENGTH_LONG).show();
            document.close();
              Intent mIntent = new Intent(this, PropertyRCUActivity.class);
                    mIntent.putExtra("property_id", PropertyIdMain);
                    mIntent.putExtra("transaction_id", transaction_id);
                    mIntent.putExtra("room_id",room_id);
                    startActivity(mIntent);
                    callSlider();
                    finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callSlider() {
        mSharedStorage.setAddCount("1");
        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }

    public static void addMetaData(Document document) {
        document.addTitle("Payment Slip");
        document.addSubject("Details for payment transaction");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }
    public void addTitlePage(Document document, String date, String keypropertyid, String roomno, String totalamount, String paid, String due, String mode, String Chequeno,String propertyname)
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
        table.addCell(createValueCell2("Property Id"));
        table.addCell(createValueCell4(keypropertyid));

        table.addCell(createValueCell2("Room No."));
        table.addCell(createValueCell4(roomno));

        table.addCell(createValueCell2("Total Amount"));
        table.addCell(createValueCell4(totalamount));

        table.addCell(createValueCell2("Paid"));
        table.addCell(createValueCell4(paid));

        table.addCell(createValueCell2("Due"));
        table.addCell(createValueCell4(""));
        addEmptyLine(preface, 1);

        table.addCell(createValueCell2("Payment Mode"));
        table.addCell(createValueCell4(mode));
        addEmptyLine(preface, 1);

        table.addCell(createValueCell2("Comment"));
        table.addCell(createValueCell4(Chequeno));
        addEmptyLine(preface, 1);

        PdfPTable table6 = new PdfPTable(1);
        table6.addCell(createValueCell6("Address : A-3, Pawansut Appts, Khamla road"));
        table6.addCell(createValueCell6("Dev Nagar,Nagpur - 440025, Maharashtra, India"));
        table6.addCell(createValueCell6("Contact No: 9926914699"));
        table6.addCell(createValueCell6("Email : booking@krooms.in"));
        addEmptyLine(preface,0);

        preface.add(11, table);//9
        preface.add(15, table6);
        document.add(preface);
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
}
