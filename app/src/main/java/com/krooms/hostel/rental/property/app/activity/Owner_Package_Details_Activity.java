package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Owner_Package_Report_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.OwnerPackageReportModel;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
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
 * Created by ashish on 22/03/18.
 */

public class Owner_Package_Details_Activity extends Activity {

    String formattedDate;
    Owner_Package_Report_Adapter adapter;
    ArrayList<OwnerPackageReportModel> arraylistpaid;
    LinearLayout paid_layout, unpaid_layout, paid_main_layout, unpaid_main_layout;
    RelativeLayout back_button;
    ListView list_paid, list_unpaid;
    Intent in;
    String propertyid;
    Context context;
    Calendar calnder;
    String month_id = "", year1 = "";
    int ptotal = 0;
    int paidamount = 0;
    int unpaidtotal = 0;
    int sumunpaid = 0;
    int sumtotal = 0, sumpaid = 0;
    SharedStorage mShared;
    String hostelname = "";
    public static ArrayList<UserModel> attendance_list;
    TextView paidamounttotal, paidtotal, unpaidamounttotal, downloadpaid, downloadunpaid,txtcalender;
    int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    String year, month, date, yearTo, monthTo, dateTo,finalResponse="";
    String DateFrom, DateTo;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_package_details_layout);
        context = this;
        mShared = SharedStorage.getInstance(this);
        in = getIntent();
        hostelname = mShared.getPropertyname();
        propertyid = in.getStringExtra("Propertyid");
        paid_layout = (LinearLayout) findViewById(R.id.paid_layout);
        unpaid_layout = (LinearLayout) findViewById(R.id.unpaid_layout);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        paid_main_layout = (LinearLayout) findViewById(R.id.paid_main_layout);
        unpaid_main_layout = (LinearLayout) findViewById(R.id.unpaid_main_layout);
        list_paid = (ListView) findViewById(R.id.list_paid);
        list_unpaid = (ListView) findViewById(R.id.list_unpaid);
        paidamounttotal = (TextView) findViewById(R.id.txtamounttotal);
        paidtotal = (TextView) findViewById(R.id.txtpaidtotal);
        unpaidamounttotal = (TextView) findViewById(R.id.txtunpaidtotal);
        downloadpaid = (TextView) findViewById(R.id.downloadpaid);
        downloadunpaid = (TextView) findViewById(R.id.downloadunpaid);
        txtcalender=(TextView) findViewById(R.id.txtcalender);
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.US);
        formattedDate = df.format(calnder.getTime());
        String[] splited = formattedDate.split("-");
        month_id = splited[1];
        year1 = splited[2];
        if (NetworkConnection.isConnected(Owner_Package_Details_Activity.this)) {
            GetPaidRecord();
        } else {
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(Owner_Package_Details_Activity.this, Home_Accountantactivity.class));
                } else {
                    Intent i = new Intent(Owner_Package_Details_Activity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                    i.putExtra("Propertyid", propertyid);
                    startActivity(i);
                }
            }
        });


        txtcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogClass cdd=new CustomDialogClass(Owner_Package_Details_Activity.this);
                cdd.show();
            }
        });

        paid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paid_layout.setBackgroundResource(R.color.grey);
                unpaid_layout.setBackgroundResource(R.color.lightgray);
                paid_main_layout.setVisibility(View.VISIBLE);
                downloadunpaid.setVisibility(View.GONE);
                downloadpaid.setVisibility(View.VISIBLE);
                calnder = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.US);
                formattedDate = df.format(calnder.getTime());
                String[] splited = formattedDate.split("-");
                month_id = splited[1];
                year1 = splited[2];
                if (NetworkConnection.isConnected(Owner_Package_Details_Activity.this)) {
                    GetPaidRecord();
                } else {
                    Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



        downloadpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arraylistpaid.size() != 0) {
                    downloadxl();
                    Toast.makeText(context, "Download successfully", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });


    }

    private void downloadxl() {

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;
       // String Fnamexls = "Paid" + i1 + " " + formattedDate + ".xls";
        String Fnamexls = "PaymentReceipt.xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/KroomsXlReport");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            int a = 1;
            int j = 0;
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("PaidReport", 0);
            WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
            cellFont.setColour(Colour.ORANGE);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(Alignment.CENTRE);
            Label header = new Label(0, 0, hostelname);
            Label labelid = new Label(0, 1, "SR");
            Label labeldate = new Label(1, 1, "Girls Name and Father Name");
            Label labelmonth = new Label(2, 1, "Room No");
            Label labelCaution_money_dept_date = new Label(3, 1, "Caution Money Deposit Date");
            Label labelCaution_money_dept_amount = new Label(4, 1, "Caution Money Deposit Amount");
            Label labelCaution_money_paid_date = new Label(5, 1, "Caution Money Paid Date");
            Label labelCaution_money_paid_amount = new Label(6, 1, "Caution Money Paid Amount");
            Label labelpackage1 = new Label(7, 1, "Package 1");
            Label labelpackage2 = new Label(8, 1, "Package 2");
            Label labelRoomAmount_received_date = new Label(9, 1, "Amount Received Date");
            Label labelRoomAmount_received_Amount = new Label(10, 1, "Amount Received");
            Label labelConsumption_dept_amount = new Label(11, 1, "Consumption of Deposit Amount");
            Label labelConsumption_dept_amount_date = new Label(12, 1, "Deposit Amount as On Date");
            try {
                sheet.mergeCells(0, 0, 12, 0);//4
                header.setCellFormat(cellFormat);
                sheet.addCell(header);
                sheet.addCell(labelid);
                sheet.addCell(labeldate);
                sheet.addCell(labelmonth);
                sheet.addCell(labelCaution_money_dept_date);
                sheet.addCell(labelCaution_money_dept_amount);
                sheet.addCell(labelCaution_money_paid_date);
                sheet.addCell(labelCaution_money_paid_amount);
                sheet.addCell(labelpackage1);
                sheet.addCell(labelpackage2);
                sheet.addCell(labelRoomAmount_received_date);
                sheet.addCell(labelRoomAmount_received_Amount);
                sheet.addCell(labelConsumption_dept_amount);
                sheet.addCell(labelConsumption_dept_amount_date);

                for (int i = 0; i < arraylistpaid.size(); i++) {
                    j = i + 2;
                    sheet.addCell(new Label(0, j, arraylistpaid.get(i).getSrno()));
                    sheet.addCell(new Label(1, j, arraylistpaid.get(i).getTenant_name()));
                    sheet.addCell(new Label(2, j, arraylistpaid.get(i).getRoom_no()));
                    sheet.addCell(new Label(3, j, arraylistpaid.get(i).getAdvance_date()));
                    sheet.addCell(new Label(4, j, arraylistpaid.get(i).getAdvance_Amount()));
                    sheet.addCell(new Label(5, j, arraylistpaid.get(i).getTenant_return_amount_Date()));
                    sheet.addCell(new Label(6, j, arraylistpaid.get(i).getTenant_return_amount()));
                    sheet.addCell(new Label(7, j, arraylistpaid.get(i).getPackage1()));
                    sheet.addCell(new Label(8, j, arraylistpaid.get(i).getPackage2()));
                    sheet.addCell(new Label(9, j, arraylistpaid.get(i).getRoom_amout_revied_date()));
                    sheet.addCell(new Label(10, j, arraylistpaid.get(i).getRoom_amout_revied()));
                    sheet.addCell(new Label(11, j, arraylistpaid.get(i).getConsuption_Dept_Amount()));
                    sheet.addCell(new Label(12, j, arraylistpaid.get(i).getConsuption_Dept_Amount_date()));

                }
                //j = j + 2;
               /* sheet.addCell(new Label(2, j, "Total=" + sumtotal));
                sheet.addCell(new Label(3, j, "Total=" + sumpaid));*/

            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            expandColumns(sheet, 12);//7
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/KroomsXlReport", "PaymentReceipt.xls");
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/xls");
            share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Owner_Package_Details_Activity.this, "com.krooms.hostel.rental.property.app",dir));
            startActivity(Intent.createChooser(share, "Share image using"));

        } else
        {
            File dir = new File(Environment.getExternalStorageDirectory() + "/KroomsXlReport", "PaymentReceipt.xls");
            Uri uri1 = Uri.fromFile(dir);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/xls");
            share.putExtra(Intent.EXTRA_STREAM, uri1);
            startActivity(share);
        }
    }

    private void expandColumns(WritableSheet sheet, int amountOfColumns) {

        int c = amountOfColumns;
        CellView cell = sheet.getColumnView(0);
        cell.setSize(2000);
        sheet.setColumnView(0, cell);

        CellView cell1 = sheet.getColumnView(1);
        cell1.setSize(10000);
        sheet.setColumnView(1, cell1);

        CellView cell2 = sheet.getColumnView(2);
        cell2.setSize(4000);
        sheet.setColumnView(2, cell2);

        CellView cell3 = sheet.getColumnView(3);
        cell3.setSize(8000);
        sheet.setColumnView(3, cell3);

        CellView cell4 = sheet.getColumnView(4);
        cell4.setSize(8000);
        sheet.setColumnView(4, cell4);

        CellView cell5 = sheet.getColumnView(5);
        cell5.setSize(8000);
        sheet.setColumnView(5, cell5);

        CellView cell6 = sheet.getColumnView(6);
        cell6.setSize(8000);
        sheet.setColumnView(6, cell6);

        CellView cell7 = sheet.getColumnView(7);
        cell7.setSize(8000);
        sheet.setColumnView(7, cell7);

        CellView cell8 = sheet.getColumnView(8);
        cell8.setSize(8000);
        sheet.setColumnView(8, cell8);

        CellView cell9 = sheet.getColumnView(9);
        cell9.setSize(8000);
        sheet.setColumnView(9, cell9);

        CellView cell10 = sheet.getColumnView(10);
        cell10.setSize(8000);
        sheet.setColumnView(10, cell10);

        CellView cell11 = sheet.getColumnView(11);
        cell11.setSize(8000);
        sheet.setColumnView(11, cell11);

        CellView cell12 = sheet.getColumnView(12);
        cell12.setSize(8000);
        sheet.setColumnView(12, cell12);

    }

    private void GetPaidRecord() {

        final ProgressDialog dialog = new ProgressDialog(Owner_Package_Details_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "paymentReport_default");
            params.put("property_id", propertyid);
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
            });//15000
            request_json.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseData(String result) {
        try {
            String status, message;
            sumtotal = 0;
            sumpaid = 0;
            int sr=1;

            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            arraylistpaid = new ArrayList();

            if (status.equalsIgnoreCase("Y")) {


                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    OwnerPackageReportModel recordmodel = new OwnerPackageReportModel();
                    String fname = jsonobj.getString("tenant_fname");
                    String lname = jsonobj.getString("tenant_lname");
                    String fathername = jsonobj.getString("tenant_father_name");
                    String fullname = fname + " " + lname+" "+fathername;
                    recordmodel.setTenant_name(fullname);
                    recordmodel.setRoom_no(jsonobj.getString("room_no"));
                    recordmodel.setAdvance_Amount(jsonobj.getString("cau_mon_dep_amt"));
                    recordmodel.setAdvance_date(jsonobj.getString("cau_mon_dep_date"));
                    recordmodel.setPackage1(jsonobj.getString("package1_amt"));
                    recordmodel.setPackage2(jsonobj.getString("package2_amt"));
                    recordmodel.setSrno(""+sr);
                    recordmodel.setRoom_amout_revied(jsonobj.getString("amt_recd"));
                    recordmodel.setRoom_amout_revied_date(jsonobj.getString("amt_recd_date"));
                    recordmodel.setConsuption_Dept_Amount(jsonobj.getString("amt_recdm"));
                    recordmodel.setConsuption_Dept_Amount_date(jsonobj.getString("amt_recdd"));
                    recordmodel.setTenant_return_amount(jsonobj.getString("amt_other"));
                    recordmodel.setTenant_return_amount_Date(jsonobj.getString("other_tranx_date"));
                    sr++;
                    arraylistpaid.add(recordmodel);
                }
                adapter = new Owner_Package_Report_Adapter(Owner_Package_Details_Activity.this, arraylistpaid);
                list_paid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Owner_Package_Details_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(Owner_Package_Details_Activity.this, Home_Accountantactivity.class));
                } else {
                    Intent i = new Intent(Owner_Package_Details_Activity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                    i.putExtra("Propertyid", propertyid);
                    startActivity(i);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView txt_startdate, txt_enddate;
        public  Button btnsend;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_datepicker);
            txt_startdate = (TextView) findViewById(R.id.txt_startdate);
            txt_enddate = (TextView) findViewById(R.id.txt_enddate);
            btnsend=(Button)findViewById(R.id.btnsend);
            txt_startdate.setOnClickListener(this);
            txt_enddate.setOnClickListener(this);
            btnsend.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_startdate:
                    final Calendar c = Calendar.getInstance();
                    mYear1 = c.get(Calendar.YEAR); // current year
                    mMonth1 = c.get(Calendar.MONTH); // current month
                    mDay1 = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(Owner_Package_Details_Activity.this,R.style.datepickerTheme,
                            new DatePickerDialog.OnDateSetListener(){

                                @Override
                                public void onDateSet(DatePicker view, int year1,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    year = String.valueOf(year1);
                                    month = String.valueOf(monthOfYear + 1);
                                    date = String.valueOf(dayOfMonth);

                                    txt_startdate.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year1);
                                    DateFrom = year1 + "-"
                                            + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                            }, mYear1, mMonth1, mDay1);

                    try {
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case R.id.txt_enddate:

                    final Calendar c1 = Calendar.getInstance();
                    mYear1 = c1.get(Calendar.YEAR); // current year
                    mMonth1 = c1.get(Calendar.MONTH); // current month
                    mDay1 = c1.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(Owner_Package_Details_Activity.this,R.style.datepickerTheme,
                            new DatePickerDialog.OnDateSetListener(){

                                @Override
                                public void onDateSet(DatePicker view, int year1,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    year = String.valueOf(year1);
                                    month = String.valueOf(monthOfYear + 1);
                                    date = String.valueOf(dayOfMonth);

                                    txt_enddate.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year1);
                                    DateFrom = year1 + "-"
                                            + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                            }, mYear1, mMonth1, mDay1);

                    try {
                        datePickerDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    break;
                case R.id.btnsend:
                    String fromdate=txt_startdate.getText().toString().trim();
                    String todate=txt_enddate.getText().toString().trim();

                    if(fromdate.equals("")){
                        Toast.makeText(Owner_Package_Details_Activity.this, "Please Select Form Date", Toast.LENGTH_SHORT).show();
                    }else if(todate.equals("")){

                        Toast.makeText(Owner_Package_Details_Activity.this, "Please Select End Date", Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(Owner_Package_Details_Activity.this, "Send", Toast.LENGTH_SHORT).show();

                    }

                    break;
            }
            //dismiss();
        }
    }



}




