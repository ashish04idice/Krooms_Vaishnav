package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
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
import com.krooms.hostel.rental.property.app.adapter.Payment_Paid_Unpaid_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.PaymentPaidUnpaidModel;
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

public class AdvancePayment_Paid_Unpaid_Activity extends Activity {

    String formattedDate;
    Payment_Paid_Unpaid_Adapter adapter;
    ArrayList<PaymentPaidUnpaidModel> arraylistpaid, arraylistunpaid;
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
    TextView paidamounttotal, paidtotal, unpaidamounttotal, downloadpaid, downloadunpaid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advancepayment_paid_unpaid_layout);
        context = this;
        mShared = SharedStorage.getInstance(this);
        arraylistunpaid = new ArrayList();
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
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.US);
        formattedDate = df.format(calnder.getTime());
        String[] splited = formattedDate.split("-");
        month_id = splited[1];
        year1 = splited[2];
        if (NetworkConnection.isConnected(AdvancePayment_Paid_Unpaid_Activity.this)) {
            GetPaidRecord();
        } else {
            Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(AdvancePayment_Paid_Unpaid_Activity.this, Home_Accountantactivity.class));
                } else {
                    Intent i = new Intent(AdvancePayment_Paid_Unpaid_Activity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                    i.putExtra("Propertyid", propertyid);
                    startActivity(i);
                }
            }
        });

        paid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paid_layout.setBackgroundResource(R.color.grey);
                unpaid_layout.setBackgroundResource(R.color.lightgray);
                paid_main_layout.setVisibility(View.VISIBLE);
                // unpaid_main_layout.setVisibility(View.GONE);
                downloadunpaid.setVisibility(View.GONE);
                downloadpaid.setVisibility(View.VISIBLE);
                calnder = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.US);
                formattedDate = df.format(calnder.getTime());
                String[] splited = formattedDate.split("-");
                month_id = splited[1];
                year1 = splited[2];
                if (NetworkConnection.isConnected(AdvancePayment_Paid_Unpaid_Activity.this)) {
                    GetPaidRecord();
                } else {
                    Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        unpaid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unpaid_layout.setBackgroundResource(R.color.grey);
                paid_layout.setBackgroundResource(R.color.lightgray);
                paid_main_layout.setVisibility(View.GONE);
                unpaid_main_layout.setVisibility(View.VISIBLE);
                downloadunpaid.setVisibility(View.VISIBLE);
                downloadpaid.setVisibility(View.GONE);
                calnder = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.US);
                formattedDate = df.format(calnder.getTime());
                String[] splited = formattedDate.split("-");
                month_id = splited[1];
                year1 = splited[2];
                if (NetworkConnection.isConnected(AdvancePayment_Paid_Unpaid_Activity.this)) {
                    GetUnPaidRecord();
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


        downloadunpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arraylistunpaid.size() != 0) {
                    downloadunpaidxl();
                    Toast.makeText(context, "Download successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void downloadunpaidxl() {

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;

        String Fnamexls = "Unpaid" + i1 + " " + formattedDate + ".xls";
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
            WritableSheet sheet = workbook.createSheet("UnpaidReport", 0);
            WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
            cellFont.setColour(Colour.ORANGE);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(Alignment.CENTRE);
            Label header = new Label(0, 0, hostelname);
            Label labelid = new Label(0, 1, "Room no");
            Label labeldate = new Label(1, 1, "Tenant Name");
            Label labelmonth = new Label(2, 1, "Amount");
            Label labelmonthly = new Label(3, 1, "Paid");
            Label labelpaid = new Label(4, 1, "Date");
            try {
                sheet.mergeCells(0, 0, 4, 0);
                header.setCellFormat(cellFormat);
                sheet.addCell(header);
                sheet.addCell(labelid);
                sheet.addCell(labeldate);
                sheet.addCell(labelmonth);
                sheet.addCell(labelmonthly);
                sheet.addCell(labelpaid);
                int j = 0;
                for (int i = 0; i < arraylistunpaid.size(); i++) {
                    j = i + 2;
                    sheet.addCell(new Label(0, j, arraylistunpaid.get(i).getRoom_no()));
                    sheet.addCell(new Label(1, j, arraylistunpaid.get(i).getTenant_name()));
                    sheet.addCell(new Label(2, j, arraylistunpaid.get(i).getTotalamount()));
                    sheet.addCell(new Label(3, j, arraylistunpaid.get(i).getPaid()));
                    sheet.addCell(new Label(4, j, arraylistunpaid.get(i).getPaydate()));
                }
                j = j + 2;
                sheet.addCell(new Label(2, j, "Total=" + sumunpaid));

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

    private void downloadxl() {

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;
        String Fnamexls = "Paid" + i1 + " " + formattedDate + ".xls";
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
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("PaidReport", 0);
            WritableFont cellFont = new WritableFont(WritableFont.TIMES, 12);
            cellFont.setColour(Colour.ORANGE);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(Alignment.CENTRE);
            Label header = new Label(0, 0, hostelname);
            Label labelid = new Label(0, 1, "Room no");
            Label labeldate = new Label(1, 1, "Tenant Name");
            Label labelmonth = new Label(2, 1, "Amount");
            Label labelmonthly = new Label(3, 1, "Paid");
            Label labelpaid = new Label(4, 1, "Date");
            try {
                sheet.mergeCells(0, 0, 4, 0);
                header.setCellFormat(cellFormat);
                sheet.addCell(header);
                sheet.addCell(labelid);
                sheet.addCell(labeldate);
                sheet.addCell(labelmonth);
                sheet.addCell(labelmonthly);
                sheet.addCell(labelpaid);

                for (int i = 0; i < arraylistpaid.size(); i++) {
                    j = i + 2;
                    sheet.addCell(new Label(0, j, arraylistpaid.get(i).getRoom_no()));
                    sheet.addCell(new Label(1, j, arraylistpaid.get(i).getTenant_name()));
                    sheet.addCell(new Label(2, j, arraylistpaid.get(i).getTotalamount()));
                    sheet.addCell(new Label(3, j, arraylistpaid.get(i).getPaid()));
                    sheet.addCell(new Label(4, j, arraylistpaid.get(i).getPaydate()));
                }
                j = j + 2;
                sheet.addCell(new Label(2, j, "Total=" + sumtotal));
                sheet.addCell(new Label(3, j, "Total=" + sumpaid));

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
        cell.setSize(2000);
        sheet.setColumnView(0, cell);
        CellView cell1 = sheet.getColumnView(1);
        cell1.setSize(6000);
        sheet.setColumnView(1, cell1);
        CellView cell2 = sheet.getColumnView(2);
        cell2.setSize(4000);
        sheet.setColumnView(2, cell2);
        CellView cell3 = sheet.getColumnView(3);
        cell3.setSize(4000);
        sheet.setColumnView(3, cell3);
        CellView cell4 = sheet.getColumnView(4);
        cell4.setSize(3000);
        sheet.setColumnView(4, cell4);

    }

    private void GetPaidRecord() {

        final ProgressDialog dialog = new ProgressDialog(AdvancePayment_Paid_Unpaid_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "advancepaiduserlist");
            params.put("property_id", propertyid);
            params.put("month_id", month_id);
            params.put("year", year1);
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
            sumtotal = 0;
            sumpaid = 0;

            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            arraylistpaid = new ArrayList();

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    PaymentPaidUnpaidModel recordmodel = new PaymentPaidUnpaidModel();
                    String fname = jsonobj.getString("tenant_fname");
                    String lname = jsonobj.getString("tenant_lname");
                    String fullname = fname + " " + lname;
                    recordmodel.setTenant_name(fullname);
                    recordmodel.setRoom_no(jsonobj.getString("room_no"));
                    recordmodel.setTotalamount(jsonobj.getString("total_amount"));
                    String transdate = jsonobj.getString("transaction_date");
                    recordmodel.setPaydate(transdate);
                    String tpaid = jsonobj.getString("paid");

                    if (tpaid.equals("") || tpaid.equals(null) || tpaid.equals("null")) {
                        recordmodel.setPaid("0");
                    } else {
                        recordmodel.setPaid(jsonobj.getString("paid"));
                    }

                    String paidtotalnew = jsonobj.getString("total_amount");

                    if (paidtotalnew.equals("") || paidtotalnew.equals(null) || paidtotalnew.equals("null")) {

                    } else {
                        ptotal = Integer.parseInt(jsonobj.getString("total_amount"));
                        sumtotal = sumtotal + ptotal;
                    }
                    String paid = jsonobj.getString("paid");
                    if (paid.equals("") || paid.equals(null) || paid.equals("null")) {

                    } else {
                        paidamount = Integer.parseInt(jsonobj.getString("paid"));
                        sumpaid = sumpaid + paidamount;
                    }
                    arraylistpaid.add(recordmodel);
                }
                paidamounttotal.setText("" + sumtotal);
                paidtotal.setText("" + sumpaid);

                adapter = new Payment_Paid_Unpaid_Adapter(AdvancePayment_Paid_Unpaid_Activity.this, arraylistpaid);
                list_paid.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(AdvancePayment_Paid_Unpaid_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetUnPaidRecord() {

        final ProgressDialog dialog = new ProgressDialog(AdvancePayment_Paid_Unpaid_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "unpaiduserlistelectricity");
            params.put("property_id", propertyid);
            params.put("month_id", month_id);
            params.put("year", year1);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getUnpaidResponseData(result);
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

    public void getUnpaidResponseData(String result) {
        try {
            String status, message;
            sumunpaid = 0;

            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            arraylistunpaid = new ArrayList();

            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    PaymentPaidUnpaidModel recordmodel = new PaymentPaidUnpaidModel();
                    recordmodel.setTenant_name(jsonobj.getString("name"));
                    recordmodel.setRoom_no(jsonobj.getString("roomno"));
                    recordmodel.setTotalamount(jsonobj.getString("amount"));
                    recordmodel.setPaid("");
                    recordmodel.setPaydate("");
                    String unpaidtotalnew = jsonobj.getString("amount");
                    if (unpaidtotalnew.equals("") || unpaidtotalnew.equals(null) || unpaidtotalnew.equals("null")) {

                    } else {
                        unpaidtotal = Integer.parseInt(jsonobj.getString("amount"));
                        sumunpaid = sumunpaid + unpaidtotal;
                    }
                    arraylistunpaid.add(recordmodel);
                }

                try {
                    unpaidamounttotal.setText("" + sumunpaid);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                adapter = new Payment_Paid_Unpaid_Adapter(AdvancePayment_Paid_Unpaid_Activity.this, arraylistunpaid);
                list_unpaid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(AdvancePayment_Paid_Unpaid_Activity.this, message, Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(AdvancePayment_Paid_Unpaid_Activity.this, Home_Accountantactivity.class));
                } else {
                    Intent i = new Intent(AdvancePayment_Paid_Unpaid_Activity.this, Owner_Paid_Unpaid_Selection_Activity.class);
                    i.putExtra("Propertyid", propertyid);
                    startActivity(i);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}




