package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 11/11/2016.
 */
public class OwnerFromActivity extends Activity {
    String month_id, studenttanentownerid, studenthiredate = "",
            studenttanentpropertyid, studenttanentroomid, message,
            result = "", studenttanentid, studenttanetname,
            studenttenantIdNew, studentroomno, studentkeyid, studentremaing_amount;
    ImageView student_loader;
    ListView student_list_dialog;
    OwnerStudentNameModel ownerstudentmodel;
    ArrayList<OwnerStudentNameModel> ownerstudentarraylist, remainingamountmodelarraylist;
    Animation rotation;
    Button paidowner;
    LinearLayout studentnamelayout, monthlayout, yearlayout, amount_layout;
    TextView studentnametextview, monthtextview, yeartextview;
    Dialog month_dialog;
    EditText payment;
    RelativeLayout backbutton;
    SharedStorage mShared;
    OwnerStudentNameTanentAdapter adapter_data_month;
    PropertyModal modal;
    String remainingamountfinalvalue = "0";
    int dateO;
    String[] month_data = {
            "Advance Payment",
            "January",
            "Febuary",
            "March",
            "April",
            "May",
            "June",
            "July", "August", "September", "October", "November", "December"
    };
    String[] monthid = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    String[] year_data = {
            "2016",
            "2017",
            "2018",
            "2019",
            "2020",
            "2021",
            "2022", "2023", "2024", "2025", "2026", "2027"
    };
    Date datehire, datetoday;
    ImageView loader;
    String[] monthvalue, yearvalue, datevalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owneractivityform);
        mShared = SharedStorage.getInstance(this);
        mShared.getPropertyname();
        Log.d("owne ", mShared.getPropertyname());
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        paidowner = (Button) findViewById(R.id.paidowner);
        loader = (ImageView) findViewById(R.id.loader);
        studentnamelayout = (LinearLayout) findViewById(R.id.studentnamelayout);
        monthlayout = (LinearLayout) findViewById(R.id.monthlayout);
        studentnametextview = (TextView) findViewById(R.id.studentname);
        yeartextview = (TextView) findViewById(R.id.yeartextview);
        yearlayout = (LinearLayout) findViewById(R.id.yearlayout);
        monthtextview = (TextView) findViewById(R.id.month);
        backbutton = (RelativeLayout) findViewById(R.id.back_button);
        payment = (EditText) findViewById(R.id.payment_owner);
        amount_layout = (LinearLayout) findViewById(R.id.amount_layout);
        Calendar calendar = Calendar.getInstance();
        dateO = calendar.get(Calendar.DAY_OF_MONTH);
        int monthO = calendar.get(Calendar.MONTH);
        int yearO = calendar.get(Calendar.YEAR);


        //this is used for paid functionality and also apply validation
        paidowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthValue = monthtextview.getText().toString();
                String paymentValue = payment.getText().toString();
                String nameValue = studentnametextview.getText().toString();
                String yearTextvalue = yeartextview.getText().toString();
                int remiangamountvalue = Integer.parseInt(remainingamountfinalvalue);

                if (nameValue.equals("")) {
                    Toast.makeText(OwnerFromActivity.this, "Please Select Name", Toast.LENGTH_SHORT).show();
                } else if (monthValue.equals("")) {
                    Toast.makeText(OwnerFromActivity.this, "Please Select Month", Toast.LENGTH_SHORT).show();
                } else if (yearTextvalue.equals("")) {
                    Toast.makeText(OwnerFromActivity.this, "Please Select Year", Toast.LENGTH_SHORT).show();
                } else if (paymentValue.equals("")) {
                    Toast.makeText(OwnerFromActivity.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (paymentValue.startsWith("0")) {

                    Toast.makeText(OwnerFromActivity.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(paymentValue) > Integer.parseInt(remainingamountfinalvalue) && (!remainingamountfinalvalue.equals("0"))) {
                    Toast.makeText(OwnerFromActivity.this, "Please Enter Valid Amount  ", Toast.LENGTH_SHORT).show();
                } else {
                    if (month_id == "00") {
                        Intent in = new Intent(OwnerFromActivity.this, PaymentActivityOwner.class);
                        in.putExtra("month", monthValue);
                        in.putExtra("payment", paymentValue);
                        in.putExtra("tanentname", nameValue);
                        in.putExtra("tenantid", studenttenantIdNew);
                        in.putExtra("tanentusrid", studenttanentid);
                        in.putExtra("yearv", yearTextvalue + "/" + month_id + "/" + "10");
                        in.putExtra("tanentroomid", studenttanentroomid);
                        in.putExtra("tanentpropertyid", studenttanentpropertyid);
                        Log.d("property id 1", studenttanentpropertyid);
                        in.putExtra("tanentownerid", studenttanentownerid);
                        in.putExtra("monthid", month_id);
                        in.putExtra("keyid", studentkeyid);
                        in.putExtra("roomno", studentroomno);
                        startActivity(in);
                    } else {
                        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            datehire = formater.parse(studenthiredate);
                            datetoday = formater.parse(dateO + "/" + month_id + "/" + yearTextvalue);
                            Intent in = new Intent(OwnerFromActivity.this, PaymentActivityOwner.class);
                            in.putExtra("month", monthValue);
                            in.putExtra("payment", paymentValue);
                            in.putExtra("tanentname", nameValue);
                            in.putExtra("tenantid", studenttenantIdNew);
                            in.putExtra("tanentusrid", studenttanentid);
                            in.putExtra("yearv", yearTextvalue + "/" + month_id + "/" + "10");
                            in.putExtra("tanentroomid", studenttanentroomid);
                            in.putExtra("tanentpropertyid", studenttanentpropertyid);
                            Log.d("property id 11", studenttanentpropertyid);
                            in.putExtra("tanentownerid", studenttanentownerid);
                            in.putExtra("monthid", month_id);
                            in.putExtra("roomno", studentroomno);
                            in.putExtra("keyid", studentkeyid);
                            startActivity(in);
                            //}
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mShared.getUserType().equals("5")) {
                    Intent in = new Intent(OwnerFromActivity.this, Home_Accountantactivity.class);
                    startActivity(in);
                    finish();

                } else {

                    Intent in = new Intent(OwnerFromActivity.this, HostelListActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });

        //student list
        studentnamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                studentdatadialogMethod();

            }
        });
        yearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yeardialogMethod();
            }
        });
        // month name list
        monthlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monthdialogMethod();
            }
        });
    }

    //year layout
    public void yeardialogMethod() {
        month_dialog = new Dialog(OwnerFromActivity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Year");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(OwnerFromActivity.this, year_data);
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
                String month_value = year_data[position].toString();
                yeartextview.setText(month_value);
                amount_layout.setVisibility(View.VISIBLE);
                String monthValue1 = monthtextview.getText().toString();
                String nameValue1 = studentnametextview.getText().toString();
                String yearTextvalue1 = yeartextview.getText().toString();
                if (nameValue1.equals("")) {
                    Toast.makeText(OwnerFromActivity.this, "Please select Tenant Name ", Toast.LENGTH_SHORT).show();
                } else if (monthValue1.equals("")) {
                    Toast.makeText(OwnerFromActivity.this, "Please Select Month Value", Toast.LENGTH_SHORT).show();
                } else {
                    new StudentMonthRemainignAmount(month_id, yearTextvalue1, studenttenantIdNew, studenttanentpropertyid, studenttanentid).execute();
                }

                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }

    //this method for month data
    public void monthdialogMethod() {
        month_dialog = new Dialog(OwnerFromActivity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Month Name");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(OwnerFromActivity.this, month_data);
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
                monthtextview.setText(month_value);

                payment.setText("");
                yeartextview.setText("");
                amount_layout.setVisibility(View.GONE);

                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }
    //month dialog end

    //these is for student name list
    public void studentdatadialogMethod() {
        month_dialog = new Dialog(OwnerFromActivity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Tenant Name");
        student_list_dialog = (ListView) month_dialog.findViewById(R.id.list_country);
        student_loader = (ImageView) month_dialog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);

        if (NetworkConnection.isConnected(OwnerFromActivity.this)) {
            new StudentNameJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        student_list_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                monthtextview.setText("");
                payment.setText("");
                yeartextview.setText("");
                amount_layout.setVisibility(View.GONE);

                studentremaing_amount = ownerstudentarraylist.get(position).getRemaining_amount().toString();
                studenttanentid = ownerstudentarraylist.get(position).getTanentuserid().toString();
                studenttanetname = ownerstudentarraylist.get(position).getTanentusername().toString();
                studenttanentroomid = ownerstudentarraylist.get(position).getTanentroomid().toString();
                studenttanentpropertyid = ownerstudentarraylist.get(position).getTanentpropertyid().toString();
                studenttanentownerid = ownerstudentarraylist.get(position).getTanentlistownerid().toString();
                studenttenantIdNew = ownerstudentarraylist.get(position).getTanentid().toString();
                studenthiredate = ownerstudentarraylist.get(position).getHiredate().toString();
                studentroomno = ownerstudentarraylist.get(position).getTenantroomno().toString();
                studentkeyid = ownerstudentarraylist.get(position).getTenantkeyid().toString();
                System.out.println(studenttenantIdNew);
                studentnametextview.setText(studenttanetname);
                //  payment.setText(studentremaing_amount);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }
    //student dialog end
    // thses is jsonparse class through these class we get student name
    private class StudentNameJsonParse extends AsyncTask<String, String, String> {

        String name;
        private boolean IsError = false;

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
                ownerstudentarraylist = new ArrayList<OwnerStudentNameModel>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "userListOfAPropertyNew"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mShared.getUserPropertyId()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("status");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        String statusvalue = jsonobj.getString("status");
                        String statusacvalue = jsonobj.getString("status_active");
                        if (statusvalue.equals("1") && statusacvalue.equals("")) {

                            ownerstudentmodel = new OwnerStudentNameModel();
                            ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname") + " " + jsonobj.getString("tenant_lname"));
                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setRemaining_amount(jsonobj.getString("remaining_amount"));
                            ownerstudentmodel.setTanentroomid(jsonobj.getString("room_id"));
                            ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                            ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                            ownerstudentmodel.setTanentpropertyid(jsonobj.getString("property_id"));
                            ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                            ownerstudentmodel.setHiredate(jsonobj.getString("property_hire_date"));
                            ownerstudentmodel.setTenantkeyid(jsonobj.getString("key_rcu_booking"));
                            ownerstudentarraylist.add(ownerstudentmodel);
                        }
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

            if (result.equalsIgnoreCase("true")) {
                if (ownerstudentarraylist.size() > 0) {

                    adapter_data_month = new OwnerStudentNameTanentAdapter(OwnerFromActivity.this, ownerstudentarraylist);
                    student_list_dialog.setAdapter(adapter_data_month);
                }
            } else if (result.equalsIgnoreCase("false")) {
                Toast.makeText(OwnerFromActivity.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class StudentMonthRemainignAmount extends AsyncTask<String, String, String> {

        String name;
        private boolean IsError = false;
        String monthValue1, yearTextvalue1, studenttenantIdNew, studenttanentpropertyid, studenttanentid;

        public StudentMonthRemainignAmount(String monthValue1, String yearTextvalue1, String studenttenantIdNew, String studenttanentpropertyid, String studenttanentid) {
            this.monthValue1 = monthValue1;
            this.yearTextvalue1 = yearTextvalue1;
            this.studenttenantIdNew = studenttenantIdNew;
            this.studenttanentpropertyid = studenttanentpropertyid;
            this.studenttanentid = studenttanentid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                remainingamountmodelarraylist = new ArrayList<OwnerStudentNameModel>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getremaingamoutmonth"));
                nameValuePairs.add(new BasicNameValuePair("property_id", studenttanentpropertyid));
                nameValuePairs.add(new BasicNameValuePair("user_id", studenttanentid));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", studenttenantIdNew));
                nameValuePairs.add(new BasicNameValuePair("month_id", month_id));
                nameValuePairs.add(new BasicNameValuePair("year", yearTextvalue1));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        OwnerStudentNameModel remainingamountmodel = new OwnerStudentNameModel();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        remainingamountfinalvalue = jsonobj.getString("remaining_amount");
                        remainingamountmodel.setRemaining_amount(jsonobj.getString("remaining_amount"));
                        remainingamountmodelarraylist.add(remainingamountmodel);
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
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("Y")) {
                if (remainingamountmodelarraylist.size() > 0) {
                    Toast.makeText(OwnerFromActivity.this, message, Toast.LENGTH_SHORT).show();
                    //remainingamountfinalvalue
                    payment.setText(remainingamountfinalvalue);
                }
            } else if (result.equalsIgnoreCase("N")) {
             }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(OwnerFromActivity.this, Home_Accountantactivity.class));
                } else {
                    startActivity(new Intent(OwnerFromActivity.this, HostelListActivity.class));
                }
                break;
        }
        return true;
    }
    //class end...........
}
