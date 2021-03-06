package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 11/11/2016.
 */
public class TanentactivityFrom extends Activity
{
    Animation rotation;
    ListView student_list_dialog;
    ImageView student_loader,loader;
    String studenttanentownerid,studenthiredate="",studenttanentpropertyid,
            studenttanentroomid,message,result="",studenttanentid,
            studenttanetname,studenttenantIdNew,studentkeyid,studentroomno,studentuserid,studentpropertyid;
    String month_value="";
    Button tanentpaid;
    SharedStorage mShared;
    LinearLayout studentnamelayout,monthlayout,yearlayout;
    TextView monthtextview,yeartextview,studentnametextview;
    EditText amount;
    String remainingamountfinalvalue="0";
    RelativeLayout backbutton;
    Dialog month_dialog;
    String month_id="";
    OwnerStudentNameModel ownerstudentmodel;
    ArrayList<OwnerStudentNameModel> ownerstudentarraylist,remainingamountmodelarraylist;
    String[] month_data= {/*"Fine",*/
            "Advance Payment",
            "January",
            "Febuary",
            "March",
            "April",
            "May",
            "June",
            "July","August","September","October","November","December"
    } ;
    String[] year_data= {
            "2016",
            "2017",
            "2018",
            "2019",
            "2020",
            "2021",
            "2022","2023","2024","2025","2026","2027"
    } ;
    String[] monthid={/*"13",*/"00","01","02","03","04","05","06","07","08","09","10","11","12"};
    ArrayList<String> listMonth=new ArrayList();
    String amountValue="";
    ArrayList<String> listMonthId=new ArrayList();
    String date1;
    int todaydate;
    Date datehire,datetoday;
    String useridvalue;
    LinearLayout amountlayout;
    int dateO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tanentactivityform);

        mShared = SharedStorage.getInstance(this);
        useridvalue=mShared.getUserId();
        Log.d("user id tanent end ",useridvalue);

       // new ListViewTenant().execute();

        date1 = mShared.getHireDate();

        Calendar cal=Calendar.getInstance();
        todaydate=cal.get(Calendar.DAY_OF_MONTH);
        dateO=cal.get(Calendar.DAY_OF_MONTH);
        Log.d("hire date va", "" + date1);

        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        studentnamelayout=(LinearLayout)findViewById(R.id.studentnamelayout);
        studentnametextview=(TextView)findViewById(R.id.studentname);
        monthlayout=(LinearLayout)findViewById(R.id.monthlayout);
        backbutton=(RelativeLayout)findViewById(R.id.back_button);
        monthtextview=(TextView)findViewById(R.id.month);
        yeartextview=(TextView)findViewById(R.id.yeartextview);
        yearlayout=(LinearLayout)findViewById(R.id.yearlayout);
        tanentpaid=(Button)findViewById(R.id.tanentpaid);
        amount=(EditText)findViewById(R.id.account);
        loader=(ImageView)findViewById(R.id.loader);
        amountlayout=(LinearLayout)findViewById(R.id.amountlayout);
        //student list
        studentnamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                studentdatadialogMethod();

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(TanentactivityFrom.this,HostelListActivity.class);
                startActivity(i);
            }
        });
        monthlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    monthdialogMethod();
            }
        });
        yearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                yeardialogMethod();
            }
        });


        tanentpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameValue=studentnametextview.getText().toString();
                amountValue=amount.getText().toString();
                String yearTextvalue=yeartextview.getText().toString();

                if (nameValue.equals(""))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please Select Tenant Name", Toast.LENGTH_SHORT).show();
                }else
                if(month_value.equals(""))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please Select Month", Toast.LENGTH_SHORT).show();
                }
                else
                if (yearTextvalue.equals(""))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please Select Year", Toast.LENGTH_SHORT).show();
                }
                else
                if(amountValue.equals(""))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                }else if(amountValue.startsWith("0")){

                    Toast.makeText(TanentactivityFrom.this,"Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                }

                else if(Integer.parseInt(amountValue) >Integer.parseInt(remainingamountfinalvalue) && (!remainingamountfinalvalue.equals("0")))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please Enter Valid amount  ", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if (month_id=="00")
                    {

                        Intent in=new Intent(TanentactivityFrom.this,PaymentActivityTanent.class);
                        in.putExtra("monthV",month_value);
                        in.putExtra("monthid",month_id);
                        in.putExtra("amountV",amountValue);
                        in.putExtra("yearv",yearTextvalue+"/"+month_id+"/"+"10");
                        in.putExtra("tiid",studenttenantIdNew);
                        in.putExtra("tanentname",nameValue);
                        in.putExtra("userid",studentuserid);
                        in.putExtra("propertyidtenant",studentpropertyid);
                        in.putExtra("roomno",studentroomno);
                        in.putExtra("keyid",studentkeyid);
                        startActivity(in);
                    }
                    else
                    {
                        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            datehire=formater.parse(studenthiredate);
                            datetoday=formater.parse(dateO+"/"+month_id+"/"+yearTextvalue);
                          //  if (datetoday.before(datehire))
                            //{
                           //     Toast.makeText(TanentactivityFrom.this, "Please Choose Valid Date After Hire Date", Toast.LENGTH_SHORT).show();
                          //  }else
                            //{

                                Intent in=new Intent(TanentactivityFrom.this,PaymentActivityTanent.class);
                                in.putExtra("monthV",month_value);
                                in.putExtra("monthid",month_id);
                                in.putExtra("amountV",amountValue);
                                in.putExtra("yearv",yearTextvalue+"/"+month_id+"/"+"10");
                                in.putExtra("tiid",studenttenantIdNew);
                                in.putExtra("tanentname",nameValue);
                                in.putExtra("userid",studentuserid);
                                in.putExtra("propertyidtenant",studentpropertyid);
                                in.putExtra("roomno",studentroomno);
                                in.putExtra("keyid",studentkeyid);
                                startActivity(in);
                           // }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    //these is for student name list
    public void  studentdatadialogMethod()
    {
        month_dialog=new Dialog(TanentactivityFrom.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading=(TextView)month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Tanent Name");
        student_list_dialog=(ListView)month_dialog.findViewById(R.id.list_country);
        student_loader=(ImageView)month_dialog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout=(RelativeLayout)month_dialog.findViewById(R.id.country_cross_layout);

        if(NetworkConnection.isConnected(TanentactivityFrom.this))
        {
            new StudentNameJsonParse().execute();
        }else
        {
            Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
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
                amount.setText("");
                yeartextview.setText("");
                amountlayout.setVisibility(View.GONE);


                studenttanentid=ownerstudentarraylist.get(position).getTanentuserid().toString();
                studenttanetname=ownerstudentarraylist.get(position).getTanentusername().toString();
                studenttanentroomid=ownerstudentarraylist.get(position).getTanentroomid().toString();
                studenttenantIdNew=ownerstudentarraylist.get(position).getTanentid().toString();
                studenthiredate=ownerstudentarraylist.get(position).getHiredate().toString();
                studentroomno=ownerstudentarraylist.get(position).getTenantroomno().toString();
                studentkeyid=ownerstudentarraylist.get(position).getTenantkeyid().toString();
                studentuserid=ownerstudentarraylist.get(position).getUserid().toString();
                studentpropertyid=ownerstudentarraylist.get(position).getProperty_id().toString();
                System.out.println(studenttenantIdNew);
               // Toast.makeText(TanentactivityFrom.this, ""+studenthiredate, Toast.LENGTH_SHORT).show();
                studentnametextview.setText(studenttanetname);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }
    //student dialog end


    public void yeardialogMethod()
    {
        month_dialog=new Dialog(TanentactivityFrom.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading=(TextView)month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Year");
        ListView month_list=(ListView)month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(TanentactivityFrom.this,year_data);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout=(RelativeLayout)month_dialog.findViewById(R.id.country_cross_layout);
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
                amountlayout.setVisibility(View.VISIBLE);
                yeartextview.setText(month_value);

                String nameValue1=studentnametextview.getText().toString();
                String yearTextvalue1=yeartextview.getText().toString();
                String monthValue1 = monthtextview.getText().toString();
                if(nameValue1.equals(""))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please select Tenant Name ", Toast.LENGTH_SHORT).show();
                }else if(monthValue1.equals(""))
                {
                    Toast.makeText(TanentactivityFrom.this, "Please Select Month Value", Toast.LENGTH_SHORT).show();
                }else
                {
                    new StudentMonthRemainignAmount(month_id,yearTextvalue1,studenttenantIdNew,studentpropertyid,studenttanentid).execute();
                }

                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }
    public void monthdialogMethod()
    {
        month_dialog=new Dialog(TanentactivityFrom.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        ListView month_list=(ListView)month_dialog.findViewById(R.id.list_country);

        Month_Custom_List adapter_data_month = new Month_Custom_List(TanentactivityFrom.this,month_data);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout=(RelativeLayout)month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });

        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                month_id=monthid[position].toString();
                month_value = month_data[position].toString();
                monthtextview.setText(month_value);

                amount.setText("");
                yeartextview.setText("");
                amountlayout.setVisibility(View.GONE);

                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }

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
            try
            {
                ownerstudentarraylist=new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getTanentInfoByUserId"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mShared.getBookedPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("user_id",useridvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain =new JSONObject(objectmain);
                result = jsmain.getString("flag");
               // message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i <jsonarray.length(); i++)
                    {
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        String statusvalue=jsonobj.getString("status");
                        String statusacvalue=jsonobj.getString("status_active");
                        if(statusvalue.equals("1")&&statusacvalue.equals(""))
                        {
                            ownerstudentmodel = new OwnerStudentNameModel();
                            ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname")+" "+jsonobj.getString("tenant_lname"));                            ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setTanentroomid(jsonobj.getString("room_id"));
                            ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                            ownerstudentmodel.setHiredate(jsonobj.getString("property_hire_date"));
                            ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                            ownerstudentmodel.setUserid(jsonobj.getString("user_id"));
                            ownerstudentmodel.setProperty_id(jsonobj.getString("property_id"));
                            ownerstudentmodel.setTenantkeyid(jsonobj.getString("key_rcu_booking"));
                            ownerstudentarraylist.add(ownerstudentmodel);
                        }
                    }
                    JSONArray jsonarrayuserrecords = jsmain.getJSONArray("user_records");
                    for (int i = 0; i <jsonarrayuserrecords.length(); i++)
                    {
                        JSONObject jsonobj =jsonarrayuserrecords.getJSONObject(i);
                        String statusvalue=jsonobj.getString("status");
                        String statusacvalue=jsonobj.getString("status_active");
                        if(statusvalue.equals("1")&&statusacvalue.equals(""))
                        {

                            if (! useridvalue.equals(jsonobj.getString("user_id"))) {
                                ownerstudentmodel = new OwnerStudentNameModel();
                                ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname") + " " + jsonobj.getString("tenant_lname"));
                                ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                                ownerstudentmodel.setTanentroomid(jsonobj.getString("room_id"));
                                ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                                ownerstudentmodel.setHiredate(jsonobj.getString("property_hire_date"));
                                ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                                ownerstudentmodel.setUserid(jsonobj.getString("user_id"));
                                ownerstudentmodel.setProperty_id(jsonobj.getString("property_id"));
                                ownerstudentmodel.setTenantkeyid(jsonobj.getString("key_rcu_booking"));
                                ownerstudentarraylist.add(ownerstudentmodel);
                            }
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
            if (!IsError) {
                if (result.equalsIgnoreCase("Y"))
                {
                    if (ownerstudentarraylist.size() > 0)
                    {
                        OwnerStudentNameTanentAdapter adapter_student = new OwnerStudentNameTanentAdapter(TanentactivityFrom.this,ownerstudentarraylist);
                        student_list_dialog.setAdapter(adapter_student);
                    }
                } else
                if (result.equalsIgnoreCase("N"))
                {
                    Toast.makeText(TanentactivityFrom.this,"No Tanent Found", Toast.LENGTH_LONG).show();
                }
            }
            else {
            }
        }
    }

    private class StudentMonthRemainignAmount  extends AsyncTask<String, String, String> {

        String name;
        private boolean IsError = false;
        String monthValue1,yearTextvalue1,studenttenantIdNew,studenttanentpropertyid,studenttanentid;

        public StudentMonthRemainignAmount(String monthValue1,String yearTextvalue1,String studenttenantIdNew,String studenttanentpropertyid,String studenttanentid) {
            this.monthValue1 = monthValue1;
            this.yearTextvalue1= yearTextvalue1;
            this.studenttenantIdNew =studenttenantIdNew;
            this.studenttanentpropertyid=studenttanentpropertyid;
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
                remainingamountmodelarraylist=new ArrayList<OwnerStudentNameModel>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getremaingamoutmonth"));
                nameValuePairs.add(new BasicNameValuePair("property_id",studenttanentpropertyid));
                nameValuePairs.add(new BasicNameValuePair("user_id",studenttanentid));
                nameValuePairs.add(new BasicNameValuePair("tenant_id",studenttenantIdNew));
                nameValuePairs.add(new BasicNameValuePair("month_id",month_id));
                nameValuePairs.add(new BasicNameValuePair("year",yearTextvalue1));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object= EntityUtils.toString(response.getEntity());
                String objectmain =object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain =new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                //{"flag":"Y","records":[{"transaction_id":"6","key_month_details":"KR\/MD\/6","tenant_id":"1","user_id":"3","room_id":"1","property_id":"1","owner_id":"1","month_id":"4","month_name":"April","year":"2017","month_date":"2017\/04\/10","total_amount":"14000","paid":"2000","remaining_amount":"12000","transaction_mode":"C\/C","transaction_date":"14\/4\/2017","status":"1","main_userid":""}],"message":"Get Record Details !"}

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i <jsonarray.length(); i++)
                    {
                        OwnerStudentNameModel remainingamountmodel=new OwnerStudentNameModel();
                        JSONObject jsonobj =jsonarray.getJSONObject(i);
                        remainingamountfinalvalue=jsonobj.getString("remaining_amount");
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
                        Toast.makeText(TanentactivityFrom.this,message, Toast.LENGTH_SHORT).show();
                        //  remainingamountfinalvalue
                        amount.setText(remainingamountfinalvalue);
                    }
                } else if (result.equalsIgnoreCase("N")) {
                  //  Toast.makeText(TanentactivityFrom.this,message, Toast.LENGTH_LONG).show();
                }

        }
    }

}
