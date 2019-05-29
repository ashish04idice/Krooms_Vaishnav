package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hi on 3/29/2017.
 */
public class Owner_Fine_Activity extends Activity implements View.OnClickListener {

    LinearLayout laytenantname, layfinehead, finelayout;
    Dialog complaintype, month_dialog;
    ListView student_list_dialog;
    ImageView student_loader;
    String settenant_name;
    TextView tenantname, txtfinehead, monthtextview, monthfinetextview;
    EditText editfine, textfinehead;
    Animation rotation;
    ArrayList<OwnerStudentNameModel> ownerstudentarraylist;
    OwnerStudentNameModel ownerstudentmodel;
    SharedStorage mShared;
    OwnerStudentNameTanentAdapter adapter_data_month;
    Button sendbtn;
    RelativeLayout backarrow;
    EditText room_no_value, electricityamount;
    Button submit_electricity;
    LinearLayout electricitylayout, monthlayout, finemonthlayout;
    String textfineheadvalue, Tenantname, Finetype, Fineamount, datetime;
    String roomid, ownerid, studenttanentid, studenttanentpropertyid, studenttenantuserid, tenantownerid, tenantroomno, parentid;
    String room_no_value_main, electricityamount_value, monthname, monthname1;
    String monthidvalue, fineandmonth, subandmonth;
    Calendar calnder;
    String getyear = "";
    String[] fine = {
            "Fees",
            "Electricity",
            "Other"

    };


    String[] month_data = {
            "January",
            "Febuary",
            "March",
            "April",
            "May",
            "June",
            "July", "August", "September", "October", "November", "December"
    };


    Intent in;
    String owneridvalue, propertyidvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_fine_activity);
        mShared = SharedStorage.getInstance(this);
        in = getIntent();
        owneridvalue = in.getStringExtra("Ownerid");
        propertyidvalue = in.getStringExtra("Propertyid");
        laytenantname = (LinearLayout) findViewById(R.id.complaintenantnamelayowner);
        submit_electricity = (Button) findViewById(R.id.submit_electricity);
        room_no_value = (EditText) findViewById(R.id.room_no_value);
        electricityamount = (EditText) findViewById(R.id.electricityamount);
        electricitylayout = (LinearLayout) findViewById(R.id.electricitylayout);
        monthlayout = (LinearLayout) findViewById(R.id.monthlayout);
        layfinehead = (LinearLayout) findViewById(R.id.complainttypelayout);
        finemonthlayout = (LinearLayout) findViewById(R.id.monthlayoutfine);
        tenantname = (TextView) findViewById(R.id.txtcomptenantnameowner);
        txtfinehead = (TextView) findViewById(R.id.txtcomplaintypeowner);
        editfine = (EditText) findViewById(R.id.editamount);
        backarrow = (RelativeLayout) findViewById(R.id.lback_button);
        sendbtn = (Button) findViewById(R.id.submit_complaintowner);
        textfinehead = (EditText) findViewById(R.id.textfinehead);
        finelayout = (LinearLayout) findViewById(R.id.finelayout);
        monthtextview = (TextView) findViewById(R.id.month);
        monthfinetextview = (TextView) findViewById(R.id.monthfine);
        tenantname.setText("Tenant Name");
        txtfinehead.setText("Select Fine Type");
        monthtextview.setText("Select Month");
        monthfinetextview.setText("Select Month");
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        datetime = df.format(Calendar.getInstance().getTime());


        calnder = Calendar.getInstance();
        SimpleDateFormat df4 = new SimpleDateFormat("yyyy", Locale.US);
        getyear = df4.format(calnder.getTime());


        submit_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                room_no_value_main = room_no_value.getText().toString().trim();
                electricityamount_value = electricityamount.getText().toString().trim();
                Finetype = txtfinehead.getText().toString().trim();
                monthname = monthtextview.getText().toString().trim();

                if (monthname.equals("Select Month")) {

                    Toast.makeText(Owner_Fine_Activity.this, "Please Select Month", Toast.LENGTH_SHORT).show();

                } else if (room_no_value_main.equals("")) {

                    Toast.makeText(Owner_Fine_Activity.this, "Please Enter Room No", Toast.LENGTH_SHORT).show();

                } else if (electricityamount_value.equals("")) {

                    Toast.makeText(Owner_Fine_Activity.this, "Please enter Amount", Toast.LENGTH_SHORT).show();

                } else if (electricityamount_value.startsWith("0")) {

                    Toast.makeText(Owner_Fine_Activity.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                } else {

                    fineandmonth = Finetype + "-" + monthname;
                    new Electricityamountsend().execute();
                }

            }
        });


        laytenantname.setOnClickListener(this);
        layfinehead.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
        backarrow.setOnClickListener(this);
        monthlayout.setOnClickListener(this);
        finemonthlayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.complaintenantnamelayowner:
                GetTenantName();
                break;

            case R.id.complainttypelayout:
                getFineheadDialog();
                break;

            case R.id.submit_complaintowner:
                Sendtoserver();
                break;

            case R.id.monthlayout:
                monthdialogMethod();
                break;

            case R.id.monthlayoutfine:
                monthdialogFineMethod();
                break;

            case R.id.lback_button:
                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(Owner_Fine_Activity.this, Home_Accountantactivity.class));

                } else {
                    Intent ii = new Intent(Owner_Fine_Activity.this, HostelListActivity.class);
                    startActivity(ii);
                }
                break;
        }
    }

    private void monthdialogFineMethod() {


        month_dialog = new Dialog(Owner_Fine_Activity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Month Name");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Owner_Fine_Activity.this, month_data);
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
                // month_id=monthid[position].toString();
                monthfinetextview.setText(month_value);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();


    }


    //this method show month list
    private void monthdialogMethod() {

        month_dialog = new Dialog(Owner_Fine_Activity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Month Name");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Owner_Fine_Activity.this, month_data);
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
                // month_id=monthid[position].toString();
                monthtextview.setText(month_value);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();


    }

    public void getFineheadDialog() {
        complaintype = new Dialog(Owner_Fine_Activity.this);
        complaintype.requestWindowFeature(Window.FEATURE_NO_TITLE);
        complaintype.setContentView(R.layout.custom_dialog_country);
        ListView month_list = (ListView) complaintype.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Owner_Fine_Activity.this, fine);
        month_list.setAdapter(adapter_data_month);
        RelativeLayout month_cross_layout = (RelativeLayout) complaintype.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintype.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fine_value = fine[position].toString();

                if (fine_value.equalsIgnoreCase("fees")) {
                    monthidvalue = "13";
                    finelayout.setVisibility(View.VISIBLE);
                    electricitylayout.setVisibility(View.GONE);

                } else if (fine_value.equalsIgnoreCase("Electricity")) {
                    monthidvalue = "14";
                    finelayout.setVisibility(View.GONE);
                    electricitylayout.setVisibility(View.VISIBLE);
                } else {
                    monthidvalue = "15";
                    finelayout.setVisibility(View.VISIBLE);
                    electricitylayout.setVisibility(View.GONE);
                }
                txtfinehead.setText(fine_value);
                complaintype.dismiss();
            }
        });

        complaintype.show();
    }

    private void Sendtoserver() {

        Tenantname = tenantname.getText().toString().trim();
        Finetype = txtfinehead.getText().toString().trim();
        Fineamount = editfine.getText().toString().trim();
        textfineheadvalue = textfinehead.getText().toString().trim();
        monthname1 = monthfinetextview.getText().toString().trim();


        if (Tenantname.equals("Tenant Name")) {

            Toast.makeText(this, "Please Select Tenant", Toast.LENGTH_SHORT).show();

        } else if (monthname1.equals("Select Month")) {

            Toast.makeText(Owner_Fine_Activity.this, "Please Select Month", Toast.LENGTH_SHORT).show();

        } else if (Finetype.equals("Select Fine Type")) {

            Toast.makeText(Owner_Fine_Activity.this, "Please Select Fine Type", Toast.LENGTH_SHORT).show();

        } else if (Fineamount.length() == 0) {

            Toast.makeText(Owner_Fine_Activity.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
        } else if (Fineamount.startsWith("0")) {

            Toast.makeText(Owner_Fine_Activity.this, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

        } else if (textfineheadvalue.equals("")) {
            Toast.makeText(this, "Please Enter fine Subject", Toast.LENGTH_SHORT).show();
        } else {

            subandmonth = textfineheadvalue + "-" + monthname1;

            new SendFine().execute();

        }
    }

    private void GetTenantName() {
        month_dialog = new Dialog(Owner_Fine_Activity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Tenant Name");
        student_list_dialog = (ListView) month_dialog.findViewById(R.id.list_country);
        student_loader = (ImageView) month_dialog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);

        if (NetworkConnection.isConnected(Owner_Fine_Activity.this)) {
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

                settenant_name = ownerstudentarraylist.get(position).getTanentusername().toString();
                studenttanentid = ownerstudentarraylist.get(position).getTanentid().toString();
                studenttanentpropertyid = ownerstudentarraylist.get(position).getTanentpropertyid().toString();
                studenttenantuserid = ownerstudentarraylist.get(position).getTanentuserid().toString();
                tenantownerid = ownerstudentarraylist.get(position).getTanentlistownerid().toString();
                tenantroomno = ownerstudentarraylist.get(position).getTenantroomno().toString();
                parentid = ownerstudentarraylist.get(position).getParentid().toString();
                ownerid = ownerstudentarraylist.get(position).getOwner_id().toString();
                roomid = ownerstudentarraylist.get(position).getTanentroomid().toString();
                //System.out.println(studenttenantIdNew);
                tenantname.setText(settenant_name);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();

    }


    //Get Tenant details from api
    private class StudentNameJsonParse extends AsyncTask<String, String, String> {

        String name, result, message;
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
                            ownerstudentmodel.setTanentroomid(jsonobj.getString("room_id"));
                            ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                            ownerstudentmodel.setParentid(jsonobj.getString("parent_id"));
                            ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                            ownerstudentmodel.setTanentpropertyid(jsonobj.getString("property_id"));
                            ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                            ownerstudentmodel.setHiredate(jsonobj.getString("property_hire_date"));
                            ownerstudentmodel.setTenantkeyid(jsonobj.getString("key_rcu_booking"));
                            ownerstudentmodel.setOwner_id(jsonobj.getString("owner_id"));
                            ownerstudentarraylist.add(ownerstudentmodel);
                        }
                    }
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
            if (result.equalsIgnoreCase("true")) {
                if (ownerstudentarraylist.size() > 0) {

                    adapter_data_month = new OwnerStudentNameTanentAdapter(Owner_Fine_Activity.this, ownerstudentarraylist);
                    student_list_dialog.setAdapter(adapter_data_month);
                }
            } else if (result.equalsIgnoreCase("false")) {
                Toast.makeText(Owner_Fine_Activity.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    //end
    //this class used to send Fine to Server
    private class SendFine extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Owner_Fine_Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "otherpaymentamount"));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", studenttanentid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", studenttanentpropertyid));
            listNameValuePairs.add(new BasicNameValuePair("user_id", studenttenantuserid));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", tenantownerid));
            listNameValuePairs.add(new BasicNameValuePair("payement_type", subandmonth));
            listNameValuePairs.add(new BasicNameValuePair("transaction_date", datetime));
            listNameValuePairs.add(new BasicNameValuePair("amount", Fineamount));
            listNameValuePairs.add(new BasicNameValuePair("room_id", roomid));
            listNameValuePairs.add(new BasicNameValuePair("month_id", monthidvalue));
            listNameValuePairs.add(new BasicNameValuePair("header_month", Finetype));
            listNameValuePairs.add(new BasicNameValuePair("year", getyear));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            //Log.e("result is ----", result);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                        if (mShared.getUserType().equals("5")) {

                            Intent ii = new Intent(Owner_Fine_Activity.this, Owner_Fine_Activity.class);
                            ii.putExtra("Ownerid", owneridvalue);
                            ii.putExtra("Propertyid", propertyidvalue);
                            startActivity(ii);
                        } else {
                            Intent ii = new Intent(Owner_Fine_Activity.this, Owner_Fine_Activity.class);
                            ii.putExtra("Ownerid", owneridvalue);
                            ii.putExtra("Propertyid", propertyidvalue);
                            startActivity(ii);
                        }
                        hideSoftKeyboard(Owner_Fine_Activity.this);

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

    }


    private class Electricityamountsend extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Owner_Fine_Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "getvacantroom"));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyidvalue));
            listNameValuePairs.add(new BasicNameValuePair("owner_id", owneridvalue));
            listNameValuePairs.add(new BasicNameValuePair("month_name", fineandmonth));
            listNameValuePairs.add(new BasicNameValuePair("transaction_date", datetime));
            listNameValuePairs.add(new BasicNameValuePair("total_amount", electricityamount_value));
            listNameValuePairs.add(new BasicNameValuePair("room_no", room_no_value_main));
            listNameValuePairs.add(new BasicNameValuePair("month_id", monthidvalue));
            listNameValuePairs.add(new BasicNameValuePair("month_date", ""));
            listNameValuePairs.add(new BasicNameValuePair("paid", "0"));
            listNameValuePairs.add(new BasicNameValuePair("year", getyear));
            listNameValuePairs.add(new BasicNameValuePair("transaction_mode", ""));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            //Log.e("result is ----", result);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                        if (mShared.getUserType().equals("5")) {

                            Intent ii = new Intent(Owner_Fine_Activity.this, Owner_Fine_Activity.class);
                            ii.putExtra("Ownerid", owneridvalue);
                            ii.putExtra("Propertyid", propertyidvalue);
                            startActivity(ii);
                        } else {
                            Intent ii = new Intent(Owner_Fine_Activity.this, Owner_Fine_Activity.class);
                            ii.putExtra("Ownerid", owneridvalue);
                            ii.putExtra("Propertyid", propertyidvalue);
                            startActivity(ii);

                        }


                        hideSoftKeyboard(Owner_Fine_Activity.this);

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }


    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            if (mShared.getUserType().equals("5")) {

                startActivity(new Intent(Owner_Fine_Activity.this, Home_Accountantactivity.class));

            } else {
                Intent ii = new Intent(Owner_Fine_Activity.this, HostelListActivity.class);
                startActivity(ii);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
