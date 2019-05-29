package com.krooms.hostel.rental.property.app.activity;

import java.io.File;
import java.text.ParseException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Attendence_ListAdapter;
import com.krooms.hostel.rental.property.app.adapter.Attendence_ListAdapterNew;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by anuradhaidice on 22/12/16.
 */

public class GetAttendenceActivity extends Activity {
    SharedStorage mSharedStorage;
    int arraylistsize;
    Animation rotation;
    ImageView loader;
    ListView roomno_listview;
    ArrayList<Attendence_Modal> roomno_arraylist;
    TextView date_textview, time_textview;
    Button submitbutton;
    RelativeLayout back_button;
    public static String master_time = "", currentdate_value1;
    SQLiteDatabase sq;
    String currentdate_value;
    ArrayList<Attendence_Modal> selectedarray;
    int cnt = 0;
    String mtime;
    SharedPreferences sp;
    SharedPreferences.Editor editorl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attandencetable_layout);

        sp = getSharedPreferences("Attendance", MODE_PRIVATE);
        editorl = sp.edit();
        editorl.putString("mtime", "");
        editorl.commit();
        sp = getSharedPreferences("Attendance", MODE_PRIVATE);
        mtime = sp.getString("mtime", "");
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        loader = (ImageView) findViewById(R.id.student_loader);
        roomno_listview = (ListView) findViewById(R.id.listview_attendence);
        date_textview = (TextView) findViewById(R.id.date_textview);
        time_textview = (TextView) findViewById(R.id.time_textview);
        submitbutton = (Button) findViewById(R.id.downloadslip);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        selectedarray = new ArrayList<Attendence_Modal>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        currentdate_value = df.format(c.getTime());
        String pattern1 = "HH:mm:ss";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern1, new Locale("da", "DK"));
        String time = simpleDateFormat1.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        Date dateObj = null;
        try {
            dateObj = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dateObj);
        currentdate_value1 = new SimpleDateFormat("K:mm a").format(dateObj);
        System.out.println(currentdate_value1);


        date_textview.setText(currentdate_value);
        time_textview.setText(currentdate_value1);

        HelperAttedence hdb = new HelperAttedence(GetAttendenceActivity.this);
        sq = hdb.getWritableDatabase();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        Cursor cs = sq.rawQuery("select * from Attendence ", null);
        cnt = cs.getCount();
        while (cs.moveToNext()) {
            String propertyid_value = cs.getString(0);
            String ownerid_value = cs.getString(1);
            String tenantid_value = cs.getString(2);
            String userid_value = cs.getString(3);
            String roomid_value = cs.getString(4);
            String roomno = cs.getString(5);
            String tenant_name = cs.getString(6);
            String absent_present = cs.getString(7);
            String date_value = cs.getString(8);
            String time_value = cs.getString(9);
            if (time_value.equals("")) {

            } else {
                Attendence_Modal model = new Attendence_Modal();
                model.setAttendencetenantid(tenantid_value);
                model.setAbsent_present(absent_present);
                selectedarray.add(model);
            }
        }

        new ListviewactivityJSONArray().execute();

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int value = 0;
                Cursor cs = sq.rawQuery("select * from Attendence", null);
                int cnt1 = cs.getCount();

                while (cs.moveToNext()) {
                    String time_value = cs.getString(9);

                    if (time_value.equals("")) {
                    } else {
                        value = value + 1;
                    }

                }

                System.out.print(value);

                if (arraylistsize != value) {
                    Toast.makeText(GetAttendenceActivity.this, "Please Select All Right", Toast.LENGTH_SHORT).show();
                } else {

                    ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo net = con.getActiveNetworkInfo();
                    if (net != null && net.isConnected() == true) {

                        Calendar calander = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                        master_time = simpleDateFormat.format(calander.getTime());

                        if ((Prefrences.Read(GetAttendenceActivity.this, "mtime_value")).equals(master_time)) {

                            Toast.makeText(GetAttendenceActivity.this, "Please get Attendence After 1 min", Toast.LENGTH_SHORT).show();

                        } else {

                            if (master_time.equals("")) {

                            } else {
                                Prefrences.Write(GetAttendenceActivity.this, "mtime_value", master_time);
                                startService(new Intent(GetAttendenceActivity.this, Background_sync_Service.class));
                                startActivity(new Intent(GetAttendenceActivity.this, HostelListActivity.class));
                            }
                        }


                    } else {
                        Toast.makeText(GetAttendenceActivity.this, "Please Check Network Connection", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }

    //this class is used getting value by webservices
    private class ListviewactivityJSONArray extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";
        int databasesizer = 0;
        String dataavailable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(GetAttendenceActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {

                roomno_arraylist = new ArrayList<Attendence_Modal>();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("action", "userListOfAPropertyNew"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getUserPropertyId()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("status");
                String message = jsmain.getString("message");
                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Attendence_Modal roomno_model = new Attendence_Modal();
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        String statusvalue = jsonobj.getString("status");
                        String statusacvalue = jsonobj.getString("status_active");
                        if (statusvalue.equals("1") && statusacvalue.equals("")) {
                            dataavailable = "true";
                            roomno_model.setAttendenceproperty_id(jsonobj.getString("property_id"));
                            roomno_model.setAttendenceroom_no(jsonobj.getString("room_no"));
                            roomno_model.setAttendenceroomid(jsonobj.getString("room_id"));
                            roomno_model.setAttendenceowner_id(jsonobj.getString("owner_id"));
                            fullname = jsonobj.getString("tenant_fname").toString() + " " + jsonobj.getString("tenant_lname").toString();
                            roomno_model.setAttendencetenantname(fullname);
                            roomno_model.setAttendencetenantid(jsonobj.getString("id"));
                            roomno_model.setAttendneceuserid(jsonobj.getString("user_id"));
                            String attende_propertyid_value = jsonobj.getString("property_id");
                            String attende_ownerid_value = jsonobj.getString("owner_id");
                            String attende_tenentid_value = jsonobj.getString("id");
                            String attende_userid_value = jsonobj.getString("user_id");
                            String attende_roomid_value = jsonobj.getString("room_id");
                            String attende_roomno_value = jsonobj.getString("room_no");

                            if (cnt == 0) {

                                databasesizer = databasesizer + 1;
                                sq.execSQL("insert into Attendence values('" + attende_propertyid_value + "'," +
                                        "'" + attende_ownerid_value + "'," +
                                        "'" + attende_tenentid_value + "'," +
                                        "'" + attende_userid_value + "'," +
                                        "'" + attende_roomid_value + "'," +
                                        "'" + attende_roomno_value + "','" + fullname + "','" + "" + "'," +
                                        "'" + currentdate_value + "','" + "" + "')");
                            }
                            getDatabasePath("AttendenceTable.sqlite").exists();
                            roomno_arraylist.add(roomno_model);

                        }

                    }
                    arraylistsize = roomno_arraylist.size();
                    if (arraylistsize == 0) {
                        dataavailable = "false";
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
            loader.clearAnimation();
            loader.setVisibility(View.GONE);

            if (result.equalsIgnoreCase("true")) {

                if (dataavailable.equals("true")) {
                    Attendence_ListAdapterNew adapter1 = new Attendence_ListAdapterNew(GetAttendenceActivity.this, roomno_arraylist, selectedarray);
                    roomno_listview.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                } else {
                    Toast.makeText(GetAttendenceActivity.this, "tenant is not available", Toast.LENGTH_SHORT).show();
                }

            } else if (result.equals("false")) {
                Toast.makeText(GetAttendenceActivity.this, "Data is not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
//............

