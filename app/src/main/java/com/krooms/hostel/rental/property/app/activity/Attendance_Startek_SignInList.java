
package com.krooms.hostel.rental.property.app.activity;


/*
 * *****************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p>
 * Copyright Â© 2008-2009 Access Computech Pvt. Ltd. All rights reserved.
 * This is proprietary information of Access Computech Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 * *****************************************************************************
 * <p>
 * project FM220_Android_SDK
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.Tenant_Absent_ListAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import java.util.ArrayList;
import java.util.Calendar;

public class Attendance_Startek_SignInList extends AppCompatActivity implements View.OnClickListener {
    TextView Capture_Match, txtcounttotal, txtcount;
    private TextView textMessage, property_name;
    private SQLiteDatabase db;
    Context context;
    DatabaseHandlerStartak_Sdk handlerdb;
    String propertyid = "", roomno = "", tenantname = "";
    Calendar calnder;
    String formattedDate = "";
    MediaPlayer mp;
    public static ArrayList<UserModel> attendance_list;
    public static ArrayList<UserModel> attendance_list_signin;
    //region Sen
    private byte[] t1, t2;
    private Cursor c;
    private Cursor attendance_cursor;
    LinearLayout listicon, bottom_lay;
    Handler handler, handlerhide;
    String devicetype = "";
    ListView listview;
    String propertyname = "", versionCode = "";
    Tenant_Absent_ListAdapter tenant_absent_listAdapter;
    Button btn_signin, btn_class, btn_lunch, btn_market, btn_vacation, btn_nightin;
    String finger_index = "";
    ImageView click;
    Dialog dialog;
    RelativeLayout dailog_cancle;
    public static ArrayList<UserModel> alist;
    GridView grid;
    public LinearLayout countlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_startek_signinlist_layout);
        context = getApplicationContext();
        handlerdb = new DatabaseHandlerStartak_Sdk(context);
        db = handlerdb.getWritableDatabase();
        SharedStorage mSharedStorage = SharedStorage.getInstance(Attendance_Startek_SignInList.this);
        Intent ii = getIntent();
        propertyid = ii.getStringExtra("propertyid");
        devicetype = ii.getStringExtra("device");
        attendance_list = new ArrayList<>();
        attendance_list_signin = new ArrayList<>();
        textMessage = (TextView) findViewById(R.id.textMessage);
        txtcounttotal = (TextView) findViewById(R.id.showcounttotal);
        txtcount = (TextView) findViewById(R.id.showcount);
        listview = (ListView) findViewById(R.id.listview);
        Capture_Match = (TextView) findViewById(R.id.getattedence);
        listicon = (LinearLayout) findViewById(R.id.list_icon);
        property_name = (TextView) findViewById(R.id.property_name);
        click = (ImageView) findViewById(R.id.click);
        bottom_lay = (LinearLayout) findViewById(R.id.buttom_lay);
        btn_signin = (Button) findViewById(R.id.btnsignin);
        btn_class = (Button) findViewById(R.id.btnclass);
        btn_lunch = (Button) findViewById(R.id.btnlunch);
        btn_market = (Button) findViewById(R.id.btnmarket);
        btn_vacation = (Button) findViewById(R.id.btnvacation);
        btn_nightin = (Button) findViewById(R.id.btnnight);
        dailog_cancle = (RelativeLayout) findViewById(R.id.dailog_cancle);
        countlayout = (LinearLayout) findViewById(R.id.countlayout);
        click.setOnClickListener(this);
        dailog_cancle.setOnClickListener(this);
        countlayout.setOnClickListener(this);
        grid = (GridView) findViewById(R.id.tenantgrid);
        propertyname = mSharedStorage.getPropertyname();
        property_name.setText(propertyname);
        getTotaluserCount();
        getOfflineAttendanceCount();
        showSignInlist();
        getVersionDevice();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);//11:59 AM
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 00);
        calnder = Calendar.getInstance();
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
            wakeLock.acquire();
        } catch (Exception e) {
        }

    }

    public void getTotaluserCount() {

        String satatus = "";
        Cursor cc1 = null;
        String query1 = "select distinct userid from persons where propertyid='" + propertyid + "'";
        cc1 = db.rawQuery(query1, null);
        if (cc1.getCount() != 0) {
            try {
                satatus = String.valueOf(cc1.getCount());
                if (satatus.equals("0")) {
                    txtcounttotal.setText("0");
                } else {
                    txtcounttotal.setText(satatus);
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppError.MethodLog("getTotaluserCount", "Get_Attendance_Startek", e.getMessage(), propertyid, versionCode);
            } finally {
                if (cc1 != null)
                    cc1.close();
            }
            if (cc1 != null && !cc1.isClosed()) {
                cc1.close();
            }
        } else {
            Toast.makeText(context, "No Record", Toast.LENGTH_SHORT).show();
        }
    }


    public void getOfflineAttendanceCount() {

        String satatus = "";
        int p = 0;
        String query1 = "select * from krooms_attendance where propertyid='" + propertyid + "'";
        Cursor cc1 = null, cc = null;
        cc1 = db.rawQuery(query1, null);
        if (cc1.getCount() != 0) {
            try {
                String query = "select * from krooms_attendance t1 inner join ( SELECT tenantid,max(id) max_id FROM `krooms_attendance` group by tenantid ) t2 on t1.id=t2.max_id where t1.propertyid='" + propertyid + "' and (t1.attendancestatus ='Sign in')";
                cc = db.rawQuery(query, null);
                satatus = String.valueOf(cc.getCount());
                if (satatus.equals("0")) {
                    txtcount.setText("0");

                } else {
                    txtcount.setText(satatus);
                }
                if (cc != null && !cc.isClosed()) {
                    cc.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppError.MethodLog("getOfflineAttendanceCount", "Get_Attendance_Startek", e.getMessage(), propertyid, versionCode);
            } finally {
                if (cc1 != null)
                    cc1.close();

                if (cc != null)
                    cc.close();
            }
            if (cc1 != null && !cc1.isClosed()) {
                cc1.close();
            }
        } else {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (devicetype.equalsIgnoreCase("Secugen")) {
                Intent i = new Intent(Attendance_Startek_SignInList.this, Tenant_Details_Activity.class);
                i.putExtra("propertyid", propertyid);
                i.putExtra("device", devicetype);
                startActivity(i);
            } else {
                Intent i = new Intent(Attendance_Startek_SignInList.this, Get_Attendance_Startek.class);
                i.putExtra("propertyid", propertyid);
                i.putExtra("device", devicetype);
                startActivity(i);
            }

            return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.click:
                try {
                    if (devicetype.equalsIgnoreCase("Secugen")) {
                        Intent i = new Intent(Attendance_Startek_SignInList.this, Tenant_Details_Activity.class);
                        i.putExtra("propertyid", propertyid);
                        i.putExtra("device", devicetype);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Attendance_Startek_SignInList.this, Get_Attendance_Startek.class);
                        i.putExtra("propertyid", propertyid);
                        i.putExtra("device", devicetype);
                        startActivity(i);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    AppError.handleUncaughtException(ex, "Get_Attendance_startek- - onClick click", propertyid, versionCode);
                }
                break;
        }
    }

    private void showSignInlist() {
        attendance_list_signin.clear();
        Cursor cc1 = null, cc = null;
        String query1 = "select * from krooms_attendance where propertyid='" + propertyid + "' ";
        cc1 = db.rawQuery(query1, null);
        if (cc1.getCount() != 0) {
            try {
                String query = "select t1.id ,t1.tenantid ,t1.propertyid ,t1.name ,t1.attendance ,t1.attendancestatus ,t1.date ,t1.time ,t1.timeinterval ,t1.status ,t1.roomno ,t1.timenew ,t1.timedate ,t3.photo " +
                        " from krooms_attendance t1 " +
                        "left join (select * from tenant_photo ) t3 on t1.tenantid=t3.tenantid and t1.propertyid=t3.propertyid " +
                        "" +
                        "" +
                        "inner join ( SELECT tenantid,max(id) max_id FROM krooms_attendance group by tenantid ) t2 on" +
                        " t1.id=t2.max_id where t1.propertyid='" + propertyid + "' AND t1.attendancestatus" +
                        "" +
                        "" +
                        "='Sign in' order by timedate asc";

                cc = db.rawQuery(query, null);
                if (cc.getCount() != 0) {
                    if (cc.moveToFirst()) {
                        do {
                            UserModel model = new UserModel();
                            model.setId(cc.getString(0));
                            model.setUserid(cc.getString(1));
                            model.setPropertyid(cc.getString(2));
                            model.setUsername(cc.getString(3));
                            model.setAttendance_status(cc.getString(4));
                            model.setAttendance_reasons(cc.getString(5));
                            model.setDate(cc.getString(6));
                            model.setTime(cc.getString(7));
                            model.setTimeinterval(cc.getString(8));
                            model.setStatus(cc.getString(9));
                            model.setRoomno(cc.getString(10));
                            model.setImageUrl(cc.getString(13));
                            attendance_list_signin.add(model);
                        } while (cc.moveToNext());
                    }
                    tenant_absent_listAdapter = new Tenant_Absent_ListAdapter(context, attendance_list_signin);
                    grid.setAdapter(tenant_absent_listAdapter);
                    tenant_absent_listAdapter.notifyDataSetChanged();
                } else {
                    tenant_absent_listAdapter = new Tenant_Absent_ListAdapter(context, attendance_list_signin);
                    grid.setAdapter(tenant_absent_listAdapter);
                    tenant_absent_listAdapter.notifyDataSetChanged();
                }
                if (cc != null && !cc.isClosed()) {
                    cc.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppError.MethodLog("getAbsentTenantList", "Get_Attendance_startek- getAbsentTenantList ", e.getMessage(), propertyid, versionCode);
            } finally {
                if (cc1 != null)
                    cc1.close();
                if (cc != null)
                    cc.close();
            }
            if (cc1 != null && !cc1.isClosed()) {
                cc1.close();
            }

        } else {
        }
    }
    private void getVersionDevice() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(Attendance_Startek_SignInList.this.getPackageName(), 0);
            versionCode = info.versionName;
            Utility.setSharedPreference(Attendance_Startek_SignInList.this, "GetVersion", versionCode);
        } catch (Exception e) {
            AppError.handleUncaughtException(e, "Get_Attendance_startek- getVersionDevice", propertyid, versionCode);
        }
    }


}

