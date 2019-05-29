package com.krooms.hostel.rental.property.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Expenses_Adapter;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.modal.ExpensesModel;

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

/**
 * Created by user on 18-Mar-19.
 */

public class Catering_Management_Owner extends AppCompatActivity implements View.OnClickListener {

    EditText item_name, item_quantity, item_price;
    TextView expensestxtview, subexpensestxtview, show_expensestxtview, selectmonthexpensestxtview, txtamounttotal, downloadpaid;
    Button submit_item, btn_submit;
    String itemname = "", itemqty = "", itempreice = "", expenseshead = "", subexpenseshead = "", propertyid = "", month_id = "", monthyear = "";
    LinearLayout additem_layout, showitem_layout, expenseslayout, subexpenseslayout, show_expenseslayout, selectmonthexpenseslayout, selectexpenseslayout;
    ScrollView scrollview_Additem;
    Dialog month_dialog;
    ListView listView;
    RelativeLayout lback_button;

    String[] expenses_data = {
            "Building Maintenance",
            "Borrowing",
            "Electricity",
            "General Expenses",
            "House Keeping",
            "Laundry",
            "Legal Taxes", "Mes", "Salary", "Stationary", "Telephone & Internet"
    };
    String[] building_data = {"Plumbring", "Sanitary", "White work"};
    String[] electricity_data = {"Bill", "Maintenance"};
    String[] mes_data = {"Equipment repair and maintenance", "Gas", "kirana goods", "Stuff salary", "Vegetables"};


    String[] month_data = {
            "January",
            "Febuary",
            "March",
            "April",
            "May",
            "June",
            "July", "August", "September", "October", "November", "December"
    };

    String[] monthid = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static String status = "";
    public static ArrayList<ExpensesModel> expensesModelArrayList;
    Expenses_Adapter adapter;
    String formattedDate;
    Calendar calnder;
    String hostelname = "";
    SharedStorage mShared;
    Intent in;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catering_management_owner_layout);
        findViewById();
        expensesModelArrayList = new ArrayList<>();
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.US);
        formattedDate = df.format(calnder.getTime());
        mShared = SharedStorage.getInstance(this);
        in = getIntent();
        hostelname = mShared.getPropertyname();
        Intent intent = getIntent();
        propertyid = intent.getStringExtra("Propertyid");
        submit_item.setOnClickListener(this);
        additem_layout.setOnClickListener(this);
        showitem_layout.setOnClickListener(this);
        expenseslayout.setOnClickListener(this);
        expensestxtview.setText("Select Expenses");
        subexpensestxtview.setText("Select Sub Expenses");
        show_expensestxtview.setText("Select Expenses");
        selectmonthexpensestxtview.setText("Select Month");
        subexpenseslayout.setOnClickListener(this);
        subexpenseslayout.setVisibility(View.GONE);
        show_expenseslayout.setVisibility(View.GONE);
        selectmonthexpenseslayout.setVisibility(View.GONE);
        show_expenseslayout.setOnClickListener(this);
        selectmonthexpenseslayout.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        lback_button.setOnClickListener(this);
        downloadpaid.setOnClickListener(this);
    }

    private void findViewById() {
        item_name = (EditText) findViewById(R.id.item_name);
        item_quantity = (EditText) findViewById(R.id.item_field);
        item_price = (EditText) findViewById(R.id.item_price);
        submit_item = (Button) findViewById(R.id.submit_item);
        scrollview_Additem = (ScrollView) findViewById(R.id.scrollview_Additem);
        additem_layout = (LinearLayout) findViewById(R.id.additem_layout);
        showitem_layout = (LinearLayout) findViewById(R.id.showitem_layout);
        expensestxtview = (TextView) findViewById(R.id.expensestxtview);
        expenseslayout = (LinearLayout) findViewById(R.id.expenseslayout);
        subexpenseslayout = (LinearLayout) findViewById(R.id.subexpenseslayout);
        subexpensestxtview = (TextView) findViewById(R.id.subexpensestxtview);
        show_expenseslayout = (LinearLayout) findViewById(R.id.show_expenseslayout);
        selectmonthexpenseslayout = (LinearLayout) findViewById(R.id.selectmonthexpenseslayout);
        selectexpenseslayout = (LinearLayout) findViewById(R.id.selectexpenseslayout);
        show_expensestxtview = (TextView) findViewById(R.id.selectexpensestxtview);
        selectmonthexpensestxtview = (TextView) findViewById(R.id.selectmonthexpensestxtview);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        listView = (ListView) findViewById(R.id.listview);
        txtamounttotal = (TextView) findViewById(R.id.txtamounttotal);
        lback_button = (RelativeLayout) findViewById(R.id.lback_button);
        downloadpaid = (TextView) findViewById(R.id.downloadpaid);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submit_item:
                itemname = item_name.getText().toString().trim();
                itempreice = item_price.getText().toString().trim();
                itemqty = item_quantity.getText().toString().trim();
                expenseshead = expensestxtview.getText().toString().trim();

                if (expenseshead.equalsIgnoreCase("Select Expenses")) {
                    Toast.makeText(this, "Please Select Expenses", Toast.LENGTH_SHORT).show();
                } else if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("2") || status.equalsIgnoreCase("3")) {
                    subexpenseshead = subexpensestxtview.getText().toString().trim();
                    if (subexpenseshead.equalsIgnoreCase("Select Sub Expenses")) {
                        Toast.makeText(this, "Please Select Sub Expenses", Toast.LENGTH_SHORT).show();
                    } else if (itemname.length() == 0) {
                        Toast.makeText(this, "Please Enter field", Toast.LENGTH_SHORT).show();
                    } else if (itempreice.length() == 0) {
                        Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (itemqty.length() == 0) {
                        Toast.makeText(this, "Please Enter Remark", Toast.LENGTH_SHORT).show();
                    } else {
                        addExpenses(itemname, itempreice, itemqty, expenseshead, subexpenseshead);
                    }
                } else if (itemname.length() == 0) {
                    Toast.makeText(this, "Please Enter field", Toast.LENGTH_SHORT).show();
                } else if (itempreice.length() == 0) {
                    Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (itemqty.length() == 0) {
                    Toast.makeText(this, "Please Enter Remark", Toast.LENGTH_SHORT).show();
                } else {
                    addExpenses(itemname, itempreice, itemqty, expenseshead, subexpenseshead);
                }
                break;

            case R.id.additem_layout:
                downloadpaid.setVisibility(View.GONE);
                additem_layout.setBackgroundResource(R.color.grey);
                showitem_layout.setBackgroundResource(R.color.lightgray);
                show_expenseslayout.setVisibility(View.GONE);
                scrollview_Additem.setVisibility(View.VISIBLE);
                selectmonthexpenseslayout.setVisibility(View.GONE);
                selectexpenseslayout.setVisibility(View.GONE);
                break;

            case R.id.showitem_layout:
                downloadpaid.setVisibility(View.VISIBLE);
                showitem_layout.setBackgroundResource(R.color.grey);
                additem_layout.setBackgroundResource(R.color.lightgray);
                show_expenseslayout.setVisibility(View.VISIBLE);
                scrollview_Additem.setVisibility(View.GONE);
                selectmonthexpenseslayout.setVisibility(View.VISIBLE);
                selectexpenseslayout.setVisibility(View.VISIBLE);
                break;

            case R.id.expenseslayout:
                selectExpenses();
                break;

            case R.id.subexpenseslayout:
                selectSubExpenses();
                break;

            case R.id.show_expenseslayout:
                selectShowExpenses();
                break;

            case R.id.selectmonthexpenseslayout:
                monthdialogMethod();
                break;

            case R.id.btn_submit:
                String select_month = selectmonthexpensestxtview.getText().toString().trim();
                String select_expanses = show_expensestxtview.getText().toString().trim();

                if (select_month.equalsIgnoreCase("Select Month")) {
                    Toast.makeText(this, "Please Select Month", Toast.LENGTH_SHORT).show();
                } else if (select_expanses.equalsIgnoreCase("Select Expenses")) {
                    Toast.makeText(this, "Please Select Expenses", Toast.LENGTH_SHORT).show();
                } else {
                    showExpenses(select_expanses);
                }
                break;

            case R.id.lback_button:
                Intent i = new Intent(Catering_Management_Owner.this, HostelListActivity.class);
                startActivity(i);
                break;

            case R.id.downloadpaid:


                PopupMenu popup = new PopupMenu(Catering_Management_Owner.this, downloadpaid);
                popup.getMenuInflater().inflate(R.menu.poupup_download_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Pdf")) {
                            if (expensesModelArrayList.size() != 0) {
                                createPDF();
                                Toast.makeText(Catering_Management_Owner.this, "Expenses Report Downloaded", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Catering_Management_Owner.this, "Please Select Head", Toast.LENGTH_SHORT).show();
                            }
                        } else if (item.getTitle().equals("Excel")) {
                            if (expensesModelArrayList.size() != 0) {
                                downloadxl();
                                Toast.makeText(Catering_Management_Owner.this, "Expenses Report Downloaded", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Catering_Management_Owner.this, "Please Select Head", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                });
                popup.show();
                break;

        }
    }

    //pdf creator <code>
    //ashish
    private void createPDF() {

        Document doc = new Document();
        try {
            String headname = show_expensestxtview.getText().toString();
            String month = selectmonthexpensestxtview.getText().toString();
            String path = Environment.getExternalStorageDirectory() + "/Krooms";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            File file = new File(dir, "ExpensesReport" + i1 + formattedDate + ".pdf");
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
            PdfPTable table22 = new PdfPTable(1);
            table22.addCell(createValueCell1(hostelname));
            preface.add(table22);
            addEmptyLine(preface, 1);
            PdfPTable table4 = new PdfPTable(1);
            table4.addCell(createValueCell5("Date :" + formattedDate));
            preface.add(table4);
            addEmptyLine(preface, 1);

            PdfPTable table2 = new PdfPTable(1);
            table2.addCell(createValueCellhead("Head:-" + headname));
            preface.add(table2);
            PdfPTable table3 = new PdfPTable(1);
            table3.addCell(createValueCellhead("Month:-" + month));
            preface.add(table3);
            addEmptyLine(preface, 1);
            PdfPTable table1 = new PdfPTable(4);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
            PdfPCell ch, c1, c2, c3, c4, c5, c6, c7;
            float[] columnWidths = new float[]{50f, 50f, 50f, 50f};
            table1.setWidths(columnWidths);
            ch = new PdfPCell(new Paragraph("Expenses Report"));
            ch.setColspan(4);
            ch.setHorizontalAlignment(Element.ALIGN_CENTER);
            ch.setPadding(10.0f);
            ch.setBackgroundColor(BaseColor.WHITE);
            table1.addCell(ch);
            c1 = new PdfPCell(new Phrase("Name", font));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setFixedHeight(30f);
            table1.addCell(c1);
            c2 = new PdfPCell(new Phrase("Price", font));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(30f);
            table1.addCell(c2);

            c3 = new PdfPCell(new Phrase("Remark", font));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setFixedHeight(30f);
            table1.addCell(c3);

            c4 = new PdfPCell(new Phrase("Date", font));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setFixedHeight(30f);
            table1.addCell(c4);

            for (int i = 0; i < expensesModelArrayList.size(); i++) {
                table1.addCell(expensesModelArrayList.get(i).getName());
                table1.addCell(expensesModelArrayList.get(i).getPrice());
                table1.addCell(expensesModelArrayList.get(i).getRemark());
                table1.addCell(expensesModelArrayList.get(i).getDate());
            }


            c6 = new PdfPCell(new Paragraph("Total Price:- " + txtamounttotal.getText().toString()));
            c6.setColspan(4);
            c6.setHorizontalAlignment(Element.ALIGN_LEFT);
            c6.setPadding(10.0f);
            c6.setBackgroundColor(BaseColor.WHITE);
            table1.addCell(c6);
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


    public PdfPCell createValueCellhead(String first_name) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, new BaseColor(0, 0, 0));
        PdfPCell cellname4 = new PdfPCell(new Phrase(first_name, font));
        cellname4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellname4.setVerticalAlignment(Element.ALIGN_LEFT);
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

    private void downloadxl() {

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;
        String Fnamexls = "Expenses" + i1 + " " + formattedDate + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/KroomsXl");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            int a = 1;
            int j = 0;

            String headname = show_expensestxtview.getText().toString();
            String month = selectmonthexpensestxtview.getText().toString();
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("ExpensesReport", 0);
            WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
            cellFont.setColour(Colour.ORANGE);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(Alignment.CENTRE);
            Label header = new Label(0, 0, hostelname);
            Label header1 = new Label(1, 1, headname + " , " + month);
            Label labelname = new Label(0, 2, "Name");
            Label labelprice = new Label(1, 2, "Price");
            Label labelremark = new Label(2, 2, "Remark");
            Label labeldate = new Label(3, 2, "Date");
            //Label labelpaid = new Label(4, 1, "Date");
            try {
                sheet.mergeCells(0, 0, 4, 0);
                header.setCellFormat(cellFormat);
                header1.setCellFormat(cellFormat);
                sheet.addCell(header);
                sheet.addCell(header1);
                sheet.addCell(labelname);
                sheet.addCell(labelprice);
                sheet.addCell(labelremark);
                sheet.addCell(labeldate);
                //sheet.addCell(labelpaid);

                for (int i = 0; i < expensesModelArrayList.size(); i++) {
                    j = i + 3;
                    sheet.addCell(new Label(0, j, expensesModelArrayList.get(i).getName()));
                    sheet.addCell(new Label(1, j, expensesModelArrayList.get(i).getPrice()));
                    sheet.addCell(new Label(2, j, expensesModelArrayList.get(i).getRemark()));
                    sheet.addCell(new Label(3, j, expensesModelArrayList.get(i).getDate()));
                    //sheet.addCell(new Label(4, j, arraylistpaid.get(i).getPaydate()));
                }
                j = j + 2;
                sheet.addCell(new Label(1, j, "Total=" + txtamounttotal.getText().toString()));
                //sheet.addCell(new Label(3, j, "Total=" + sumpaid));

            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            expandColumns(sheet, 8);
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
        cell.setSize(4000);
        sheet.setColumnView(0, cell);
        CellView cell1 = sheet.getColumnView(1);
        cell1.setSize(5000);
        sheet.setColumnView(1, cell1);
        CellView cell2 = sheet.getColumnView(2);
        cell2.setSize(5000);
        sheet.setColumnView(2, cell2);
        CellView cell3 = sheet.getColumnView(3);
        cell3.setSize(4000);
        sheet.setColumnView(3, cell3);

    }


    private void addExpenses(String itemname, String itempreice, String itemremark, String expenseshead, String subexpenseshead) {

        final ProgressDialog dialog = new ProgressDialog(Catering_Management_Owner.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "addExpenses");
            params.put("property_id", propertyid);
            params.put("expenseshead", expenseshead);
            params.put("subexpenseshead", subexpenseshead);
            params.put("itemname", itemname);
            params.put("itempreice", itempreice);
            params.put("itemremark", itemremark);

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
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
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
                Toast.makeText(Catering_Management_Owner.this, message, Toast.LENGTH_SHORT).show();
                item_name.setText("");
                item_price.setText("");
                item_quantity.setText("");
                expensestxtview.setText("Select Expenses");
                subexpensestxtview.setText("Select Sub Expenses");
            } else {
                Toast.makeText(Catering_Management_Owner.this, message, Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void selectExpenses() {

        month_dialog = new Dialog(Catering_Management_Owner.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Select Expenses");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Catering_Management_Owner.this, expenses_data);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item_name.setText("");
                item_price.setText("");
                item_quantity.setText("");

                String month_value = expenses_data[position].toString();
                expensestxtview.setText(month_value);

                status = "";
                if (expensestxtview.getText().toString().equalsIgnoreCase("Building Maintenance")) {
                    status = "1";
                    subexpenseslayout.setVisibility(View.VISIBLE);
                    subexpensestxtview.setText("Select Sub Expenses");

                } else if (expensestxtview.getText().toString().equalsIgnoreCase("Electricity")) {
                    status = "2";
                    subexpenseslayout.setVisibility(View.VISIBLE);
                    subexpensestxtview.setText("Select Sub Expenses");
                } else if (expensestxtview.getText().toString().equalsIgnoreCase("Mes")) {
                    status = "3";
                    subexpenseslayout.setVisibility(View.VISIBLE);
                    subexpensestxtview.setText("Select Sub Expenses");
                } else {
                    status = "";
                    subexpenseslayout.setVisibility(View.GONE);
                    subexpensestxtview.setText("Select Sub Expenses");
                }
                month_dialog.dismiss();
            }
        });
        month_dialog.show();

    }

    private void selectShowExpenses() {

        month_dialog = new Dialog(Catering_Management_Owner.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Select Expenses");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Catering_Management_Owner.this, expenses_data);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String month_value = expenses_data[position].toString();
                show_expensestxtview.setText(month_value);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();


    }

    private void selectSubExpenses() {

        month_dialog = new Dialog(Catering_Management_Owner.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Select Expenses");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);

        if (status.equalsIgnoreCase("1")) {
            Month_Custom_List adapter_data_month = new Month_Custom_List(Catering_Management_Owner.this, building_data);
            month_list.setAdapter(adapter_data_month);

        } else if (status.equalsIgnoreCase("2")) {
            Month_Custom_List adapter_data_month = new Month_Custom_List(Catering_Management_Owner.this, electricity_data);
            month_list.setAdapter(adapter_data_month);


        } else if (status.equalsIgnoreCase("3")) {

            Month_Custom_List adapter_data_month = new Month_Custom_List(Catering_Management_Owner.this, mes_data);
            month_list.setAdapter(adapter_data_month);

        }

        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item_name.setText("");
                item_price.setText("");
                item_quantity.setText("");

                String month_value = "";
                if (status.equalsIgnoreCase("1")) {
                    month_value = building_data[position].toString();

                } else if (status.equalsIgnoreCase("2")) {
                    month_value = electricity_data[position].toString();

                } else if (status.equalsIgnoreCase("3")) {

                    month_value = mes_data[position].toString();

                }

                subexpensestxtview.setText(month_value);

                month_dialog.dismiss();
            }
        });
        month_dialog.show();

    }

    private void monthdialogMethod() {

        month_dialog = new Dialog(Catering_Management_Owner.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Month Name");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Catering_Management_Owner.this, month_data);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String month_value = month_data[position].toString();
                month_id = monthid[position].toString();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                monthyear = month_id + "-" + year;
                selectmonthexpensestxtview.setText(month_value);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();


    }

    //show expanses
    private void showExpenses(String select_expanses) {

        final ProgressDialog dialog = new ProgressDialog(Catering_Management_Owner.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "showExpenses");
            params.put("property_id", propertyid);
            params.put("expenseshead", select_expanses);
            params.put("monthyear", monthyear);

            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    showResponseData(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showResponseData(String result) {
        try {
            String status, message, total;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            expensesModelArrayList = new ArrayList<>();
            expensesModelArrayList.clear();
            if (status.equalsIgnoreCase("Y")) {
                total = jsmain.getString("total");
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    ExpensesModel recordmodel = new ExpensesModel();
                    recordmodel.setName(jsonobj.getString("name"));
                    recordmodel.setPrice(jsonobj.getString("price"));
                    recordmodel.setRemark(jsonobj.getString("remark"));
                    recordmodel.setDate(jsonobj.getString("created_date"));
                    expensesModelArrayList.add(recordmodel);
                }

                txtamounttotal.setText(total);
                adapter = new Expenses_Adapter(Catering_Management_Owner.this, expensesModelArrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Catering_Management_Owner.this, message, Toast.LENGTH_SHORT).show();
                adapter = new Expenses_Adapter(Catering_Management_Owner.this, expensesModelArrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                txtamounttotal.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(Catering_Management_Owner.this, HostelListActivity.class);
                startActivity(i);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}