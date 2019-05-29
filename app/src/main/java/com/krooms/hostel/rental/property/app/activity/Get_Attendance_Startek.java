
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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acpl.access_computech_fm220_sdk.FM220_Scanner_Interface;
import com.acpl.access_computech_fm220_sdk.acpl_FM220_SDK;
import com.acpl.access_computech_fm220_sdk.fm220_Capture_Result;
import com.acpl.access_computech_fm220_sdk.fm220_Init_Result;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.App;
import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.Tenant_Absent_ListAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.customViewGroup;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.dialog.AttendanceSmsAlert;
import com.krooms.hostel.rental.property.app.modal.AttedanceadsModel;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Get_Attendance_Startek extends AppCompatActivity implements FM220_Scanner_Interface, View.OnClickListener {
    private acpl_FM220_SDK FM220SDK;
    private Button Capture_No_Preview, Capture_PreView, Capture_BackGround;
    TextView Capture_Match, txtcounttotal, txtcount;
    private Button Enroll_finger, abort_button;
    private TextView textMessage, property_name;
    private ImageView imageView;
    private SQLiteDatabase db;
    Context context;
    DatabaseHandlerStartak_Sdk handlerdb;
    String propertyid = "", roomno = "", tenantname = "", versionCode = "";
    Calendar calnder;
    String localTime = "", formattedDate = "", time_new = "", timeanddate = "";
    MediaPlayer mp = null;
    public static ArrayList<UserModel> attendance_list;
    public static ArrayList<UserModel> attendance_list_signin;
    public static ArrayList<UserModel> attendance_list_sendserver;
    public static ArrayList<UserModel> foodattendance_list;
    public static ArrayList<UserModel> nightattendance_list;
    public static ArrayList<AttedanceadsModel> arraylistads;
    //region Sen
    private static final String Telecom_Device_Key = "";
    private byte[] t1, t2;
    private Cursor c = null;
    private Cursor attendance_cursor = null;
    LinearLayout listicon, bottom_lay;
    Handler handler, handlerhide;
    //region USB intent and functions
    private UsbManager manager;
    private PendingIntent mPermissionIntent;
    private UsbDevice usb_Dev;
    private static final String ACTION_USB_PERMISSION = "com.ACPL.FM220_Telecom.USB_PERMISSION";
    private String pidDataStr = "";
    private Intent intentClaim;
    private Intent intentRelease;
    private static boolean isLocalConn = false;
    String devicetype = "";
    ListView listview;
    String propertyname = "";
    Tenant_Absent_ListAdapter tenant_absent_listAdapter;
    Button btn_signin, btn_class, btn_lunch, btn_market, btn_vacation, btn_nightin;
    String finger_index = "";
    ImageView click;
    Dialog dialog;
    RelativeLayout dailog_cancle;
    static int page = 1;
    public static ArrayList<UserModel> alist;
    public static GridView grid;
    public static int limtstart = 0, limtend = 6, totalpage;
    public static LinearLayout countlayout, attendanceads, attendancelayout;
    public static ImageView imageViewads;
    public static int[] imageArray = {R.drawable.dominos_ads, R.drawable.nokia_ads,
            R.drawable.swiggy_ads, R.drawable.nokia_ads,
    };
    public static String[] imagesads = new String[3];
    public static final int OVERLAY_PERMISSION_REQ_CODE = 4545;
    AttendanceSmsAlert attendanceSmsAlert;
    Button btng, btn1, btn2,btn3, btn4, btn5,btn6,btn7,btn8,btn9;
    LinearLayout layfloorsecond,lay,lay_firstfloor,room_no_layout;
    public static String floorno="";
    int sharedfloorvalue=0;


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {


        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                int pid, vid;
                pid = device.getProductId();
                vid = device.getVendorId();
                if ((pid == 0x8225 || pid == 0x8220) && (vid == 0x0bca)) {
                    FM220SDK.stopCaptureFM220();
                    FM220SDK.unInitFM220();
                    usb_Dev = null;
                    textMessage.setText("FM220 disconnected");
                    isLocalConn = false;
                    DisableCapture();
                }
            }
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    try {
                        if (intent.getBooleanExtra(
                                UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                // call method to set up device communication
                                int pid, vid;
                                pid = device.getProductId();
                                vid = device.getVendorId();
                                if ((pid == 0x8225 || pid == 0x8220) && (vid == 0x0bca)) {
                                    fm220_Init_Result res = FM220SDK.InitScannerFM220(manager, device, Telecom_Device_Key);
                                    if (res.getResult()) {
                                        textMessage.setText("FM220 ready. " + res.getSerialNo());
                                        EnableCapture();
                                        isLocalConn = true;
                                    } else {
                                        textMessage.setText("Error :-" + res.getError());
                                        DisableCapture();
                                        isLocalConn = false;
                                    }
                                }
                            }
                        } else {
                            textMessage.setText("User Blocked USB connection");
                            textMessage.setText("FM220 ready");
                            DisableCapture();
                        }
                    } catch (Exception x) {
                        x.printStackTrace();
                        AppError.handleUncaughtException(x, "Get_Attendance_startek- ACTION_USB_PERMISSION", propertyid, versionCode);
                    }
                }
            }

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    try {
                        if (device != null) {
                            int pid, vid;
                            pid = device.getProductId();
                            vid = device.getVendorId();
                            if ((pid == 0x8225) && (vid == 0x0bca) && !FM220SDK.FM220isTelecom()) {
                                Toast.makeText(context, "Wrong device type application restart required!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            if ((pid == 0x8220) && (vid == 0x0bca) && FM220SDK.FM220isTelecom()) {
                                Toast.makeText(context, "Wrong device type application restart required!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            if ((pid == 0x8225 || pid == 0x8220) && (vid == 0x0bca)) {
                                if (!manager.hasPermission(device)) {
                                    textMessage.setText("FM220 requesting permission");
                                    manager.requestPermission(device, mPermissionIntent);
                                } else {
                                    fm220_Init_Result res = FM220SDK.InitScannerFM220(manager, device, Telecom_Device_Key);
                                    if (res.getResult()) {
                                        textMessage.setText("FM220 ready. " + res.getSerialNo());
                                        EnableCapture();
                                        isLocalConn = true;
                                    } else {
                                        textMessage.setText("Error :-" + res.getError());
                                        DisableCapture();
                                        isLocalConn = false;
                                    }
                                }
                            }
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                        AppError.handleUncaughtException(f, "Get_Attendance_startek- UsbManager.ACTION_USB_DEVICE_ATTACHED", propertyid, versionCode);


                    }
                }
            }
        }

    };

    @Override
    protected void onNewIntent(Intent intent) {
        if (getIntent() != null) {
            return;
        }
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED) && usb_Dev == null) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    int pid, vid;
                    pid = device.getProductId();
                    vid = device.getVendorId();
                    if ((pid == 0x8225) && (vid == 0x0bca)) {
                        if (manager != null) {
                            if (!manager.hasPermission(device)) {
                                textMessage.setText("FM220 requesting permission");
                                manager.requestPermission(device, mPermissionIntent);
                            }
                        }
                    }
                }
            }
        } catch (Exception xx) {
            xx.printStackTrace();
            AppError.handleUncaughtException(xx, "Get_Attendance_startek- onNewIntent", propertyid, versionCode);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mUsbReceiver);
            FM220SDK.unInitFM220();
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_startek- onDestroy ", propertyid, versionCode);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
            WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
            localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            localLayoutParams.gravity = Gravity.TOP;
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            localLayoutParams.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
            localLayoutParams.format = PixelFormat.TRANSPARENT;
            customViewGroup view = new customViewGroup(this);
            manager.addView(view, localLayoutParams);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        setContentView(R.layout.get_attendance_startek_layout);
        context = getApplicationContext();
        handlerdb = new DatabaseHandlerStartak_Sdk(context);
        db = handlerdb.getWritableDatabase();
        SharedStorage mSharedStorage = SharedStorage.getInstance(Get_Attendance_Startek.this);

        Intent ii = getIntent();
        propertyid = ii.getStringExtra("propertyid");
        devicetype = ii.getStringExtra("device");

        sharedfloorvalue=Utility.getIngerSharedPreferences(context,"Floorvalue");

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
        imageViewads = (ImageView) findViewById(R.id.imageviewads);
        attendanceads = (LinearLayout) findViewById(R.id.attendance_ads);
        layfloorsecond=(LinearLayout)findViewById(R.id.layfloorsecond);
        lay_firstfloor=(LinearLayout)findViewById(R.id.lay_firstfloor);
        room_no_layout=(LinearLayout) findViewById(R.id.room_no_layout);

        btng = (Button) findViewById(R.id.btng);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);

        arraylistads = new ArrayList<>();
        btn_signin.setOnClickListener(this);
        btn_class.setOnClickListener(this);
        btn_lunch.setOnClickListener(this);
        btn_market.setOnClickListener(this);
        btn_vacation.setOnClickListener(this);
        btn_nightin.setOnClickListener(this);
        click.setOnClickListener(this);
        dailog_cancle.setOnClickListener(this);
        countlayout.setOnClickListener(this);

        btng.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        if(sharedfloorvalue==0){
            layfloorsecond.setVisibility(View.GONE);
            lay_firstfloor.setVisibility(View.GONE);
            room_no_layout.setVisibility(View.VISIBLE);
        }else
        if(sharedfloorvalue>5){
            layfloorsecond.setVisibility(View.VISIBLE);
            lay_firstfloor.setVisibility(View.VISIBLE);
            room_no_layout.setVisibility(View.GONE);
            btng.setVisibility(View.VISIBLE);
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);

            if(sharedfloorvalue==6){
                btn5.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==7){
                btn6.setVisibility(View.VISIBLE);
                btn5.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==8){
                btn6.setVisibility(View.VISIBLE);
                btn5.setVisibility(View.VISIBLE);
                btn7.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==9){
                btn5.setVisibility(View.VISIBLE);
                btn6.setVisibility(View.VISIBLE);
                btn7.setVisibility(View.VISIBLE);
                btn8.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==10){
                btn5.setVisibility(View.VISIBLE);
                btn6.setVisibility(View.VISIBLE);
                btn7.setVisibility(View.VISIBLE);
                btn8.setVisibility(View.VISIBLE);
                btn9.setVisibility(View.VISIBLE);
            }


        }else {
            layfloorsecond.setVisibility(View.GONE);
            lay_firstfloor.setVisibility(View.VISIBLE);
            room_no_layout.setVisibility(View.GONE);

            if(sharedfloorvalue==1){
                btng.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==2){
                btn1.setVisibility(View.VISIBLE);
                btng.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==3){
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btng.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==4){
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btng.setVisibility(View.VISIBLE);
            }else if(sharedfloorvalue==5){
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
                btng.setVisibility(View.VISIBLE);
            }
        }

        grid = (GridView) findViewById(R.id.tenantgrid);
        propertyname = mSharedStorage.getPropertyname();
        property_name.setText(propertyname);
        getTotaluserCount();
        getOfflineAttendanceCount();
        getAbsentTenantList();
        getVersionDevice();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);//11:59 AM
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 00);

        Intent intentt = new Intent(Get_Attendance_Startek.this, ServiceDeleteRecord_Startek.class);
        PendingIntent pintent = PendingIntent.getService(Get_Attendance_Startek.this, 0, intentt,
                0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pintent);

        if (Utility.getSharedPreferences(Get_Attendance_Startek.this, "deviceMode").equals("Online")) {

            Intent intentsync = new Intent(Get_Attendance_Startek.this, ServiceSyncRecord_Attendance_Startek.class);
            intentsync.putExtra("PropertyId", propertyid);
            intentsync.putExtra("versionCode", versionCode);
            startService(intentsync);

            Intent intentnight = new Intent(Get_Attendance_Startek.this, ServiceSyncRecord_Night_Attendance_Startek.class);
            intentnight.putExtra("PropertyId", propertyid);
            intentnight.putExtra("versionCode", versionCode);
            startService(intentnight);

        } else {
            Intent intentsync = new Intent(Get_Attendance_Startek.this, ServiceSyncRecord_Attendance_Startek.class);
            intentsync.putExtra("PropertyId", propertyid);
            intentsync.putExtra("versionCode", versionCode);
            startService(intentsync);

            Intent intentnight = new Intent(Get_Attendance_Startek.this, ServiceSyncRecord_Night_Attendance_Startek.class);
            intentnight.putExtra("PropertyId", propertyid);
            intentnight.putExtra("versionCode", versionCode);
            startService(intentnight);
        }

        calnder = Calendar.getInstance();

        try {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
            wakeLock.acquire();
        } catch (Exception e) {
            AppError.handleUncaughtException(e, "Get_Attendance_Startek- PowerManager", propertyid, versionCode);
        }
        SharedPreferences sp = getSharedPreferences("last_FM220_type", Activity.MODE_PRIVATE);
        boolean oldDevType = sp.getBoolean("FM220type", true);
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        final Intent piIntent = new Intent(ACTION_USB_PERMISSION);
        if (Build.VERSION.SDK_INT >= 16) piIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        mPermissionIntent = PendingIntent.getBroadcast(getBaseContext(), 1, piIntent, 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(mUsbReceiver, filter); //working cmd only for testing
        UsbDevice device = null;
        for (UsbDevice mdevice : manager.getDeviceList().values()) {
            int pid, vid;
            pid = mdevice.getProductId();
            vid = mdevice.getVendorId();
            boolean devType;
            if ((pid == 0x8225) && (vid == 0x0bca)) {
                FM220SDK = new acpl_FM220_SDK(getApplicationContext(), this, true);
                devType = true;
            } else if ((pid == 0x8220) && (vid == 0x0bca)) {
                FM220SDK = new acpl_FM220_SDK(getApplicationContext(), this, false);
                devType = false;
            } else {
                FM220SDK = new acpl_FM220_SDK(getApplicationContext(), this, oldDevType);
                devType = oldDevType;
            }
            if (oldDevType != devType) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("FM220type", devType);
                editor.apply();
            }
            if ((pid == 0x8225 || pid == 0x8220) && (vid == 0x0bca)) {
                device = mdevice;
                if (!manager.hasPermission(device)) {
                    textMessage.setText("FM220 requesting permission");
                    Toast.makeText(context, "FM220 requesting permission", Toast.LENGTH_SHORT).show();
                    manager.requestPermission(device, mPermissionIntent);
                } else {
                    Intent intent = this.getIntent();
                    if (intent != null) {
                    }
                    fm220_Init_Result res = FM220SDK.InitScannerFM220(manager, device, Telecom_Device_Key);
                    if (res.getResult()) {
                        textMessage.setText("FM220 ready. " + res.getSerialNo());
                        EnableCapture();
                        isLocalConn = true;

                    } else {
                        textMessage.setText("Error :-" + res.getError());
                        DisableCapture();
                        isLocalConn = false;
                    }
                }
                break;
            }
        }

        if (device == null) {
            textMessage.setText("Pl connect FM220");
            FM220SDK = new acpl_FM220_SDK(getApplicationContext(), this, oldDevType);
        }
        Capture_Match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
            }
        });


        handlerhide = new Handler();
        handlerhide.postDelayed(new Runnable() {
            public void run() {
                bottom_lay.setVisibility(View.GONE);
                handlerhide.postDelayed(this, 100000);//180000
            }

        }, 100000);//3min//180000

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Date mToday = new Date();
                Date userDate = null, start = null, end = null;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String curTime = sdf.format(mToday);
                try {
                    start = sdf.parse("5:00:00");
                    end = sdf.parse("5:02:01");
                    userDate = sdf.parse(curTime);
                    if (userDate.after(start) && userDate.before(end)) {
                        deleteCache(context, propertyid, versionCode);
                    }
                    start = sdf.parse("15:00:00");
                    end = sdf.parse("15:02:01");
                    userDate = sdf.parse(curTime);
                    if (userDate.after(start) && userDate.before(end)) {
                        deleteCache(context, propertyid, versionCode);
                    }
                    start = sdf.parse("10:00:00");
                    end = sdf.parse("10:02:01");
                    userDate = sdf.parse(curTime);
                    if (userDate.after(start) && userDate.before(end)) {
                        deleteCache(context, propertyid, versionCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- handler", propertyid, versionCode);
                }
                handler.postDelayed(this, 15000); //15000
            }


        }, 15000);//15000


        listicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(Get_Attendance_Startek.this, listicon);
                popup.getMenuInflater().inflate(R.menu.poupup_attendance_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Food Attendance")) {
                            Intent intent = new Intent(context, Attendance_Food_Activity_Offline.class);
                            intent.putExtra("device", devicetype);
                            intent.putExtra("propertyid", propertyid);
                            startActivity(intent);
                            finish();

                        } else if (item.getTitle().equals("View Attendance")) {
                            Intent ii = new Intent(Get_Attendance_Startek.this, Tenant_Records_Show_In_Tablayout_Offlinemode.class);
                            ii.putExtra("device", devicetype);
                            ii.putExtra("propertyid", propertyid);
                            startActivity(ii);
                            finish();
                        } else if (item.getTitle().equals("Sync Attendance")) {
                            sendOfflineattendance();
                            sendOfflineattendanceFood();
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });
    }


    //select total count
    public void getTotaluserCount() {

        String satatus = "";
        String query1 = "select distinct userid from persons where propertyid='" + propertyid + "'";
        Cursor cc1 = null;
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

    private boolean FunctionBase64(String temp1, String temp2) {
        try {
            if (FM220SDK.MatchFM220String(temp1, temp2)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_startek- FunctionBase64", propertyid, versionCode);

        }
        return false;
    }

    private void DisableCapture() {
        btn_signin.setEnabled(false);
        btn_class.setEnabled(false);
        btn_lunch.setEnabled(false);
        btn_market.setEnabled(false);
        btn_nightin.setEnabled(false);
        btn_vacation.setEnabled(false);

        if(sharedfloorvalue==0){
            Capture_Match.setVisibility(View.GONE);
        }


    }

    private void EnableCapture() {
        btn_signin.setEnabled(true);
        btn_class.setEnabled(true);
        btn_lunch.setEnabled(true);
        btn_market.setEnabled(true);
        btn_nightin.setEnabled(true);
        btn_vacation.setEnabled(true);

        if(sharedfloorvalue==0){
            Capture_Match.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void ScannerProgressFM220(final boolean DisplayImage, final Bitmap ScanImage, final boolean DisplayText, final String statusMessage) {
        Get_Attendance_Startek.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (DisplayText) {
                        textMessage.setText(statusMessage);
                        textMessage.invalidate();
                    }
                    if (DisplayImage) {
                    }
                } catch (Exception w) {
                    w.printStackTrace();
                    AppError.handleUncaughtException(w, "Get_Attendance_startek-ScannerProgressFM220", propertyid, versionCode);
                }
            }
        });
    }

    @Override
    public void ScanCompleteFM220(final fm220_Capture_Result result) {
        Get_Attendance_Startek.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (FM220SDK.FM220Initialized()) EnableCapture();
                    if (result.getResult()) {
                        if (result.isEnroll()) {
                            textMessage.setText("Finger Enroll Success");
                        } else {
                            textMessage.setText("Success NFIQ:" + Integer.toString(result.getNFIQ()) + "  SrNo:" + result.getSerialNo());
                        }
                        t1 = result.getISO_Template();
                        MatchFinger();
                    } else {
                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);
                        if(sharedfloorvalue==0){
                            Capture_Match.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception d) {
                    d.printStackTrace();
                    AppError.handleUncaughtException(d, "Get_Attendance_startek- ScanCompleteFM220", propertyid, versionCode);
                }
            }
        });
    }

    @Override
    public void ScanMatchFM220(final fm220_Capture_Result result) {
        Get_Attendance_Startek.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (FM220SDK.FM220Initialized()) EnableCapture();
                if (result.getResult()) {
                    textMessage.setText("Finger matched\n" + "Success NFIQ:" + Integer.toString(result.getNFIQ()));
                } else {
                    textMessage.setText("Finger not matched\n" + result.getError());
                }
                textMessage.invalidate();
            }
        });
    }


    public void MatchFinger() {

        try {
            boolean status = false;
            alist = new ArrayList<>();
            alist.clear();
            String query;

            if(sharedfloorvalue==0){
                 query = "select * from persons where propertyid='" + propertyid + "' ";
            }else{
                 query = "select * from persons where propertyid='" + propertyid + "' and  roomno LIKE '" + floorno + "%'  collate nocase  ";
            }
           c = db.rawQuery(query, null);
            String img;
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        try {
                            UserModel model = new UserModel();
                            model.setUserid(c.getString(0));
                            model.setUsername(c.getString(1));
                            model.setPropertyid(c.getString(2));
                            model.setThumbfinger(c.getString(3));
                            model.setThumbindex(c.getString(4));
                            model.setRoomno(c.getString(5));
                            alist.add(model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (c.moveToNext());
                }
                // String teamplet match example using FunctionBAse64 function .....
                String t1base64, t2base64;
                t1base64 = Base64.encodeToString(t1, Base64.NO_WRAP);
                int pos = 0;
                boolean matchval = false;
                if (alist.size() != 0) {
                    for (int k = 0; k < alist.size(); k++) {
                        String datathumbfinger = alist.get(k).getThumbfinger();
                        matchval = FunctionBase64(datathumbfinger, t1base64);
                        if (matchval) {
                            pos = k;
                            status = true;
                            break;
                        } else {

                        }
                    }
                    if (status == true) {
                        String id1 = alist.get(pos).getUserid();
                        String name1 = alist.get(pos).getUsername();
                        String propertyid = alist.get(pos).getPropertyid();
                        String fingerindex = alist.get(pos).getThumbindex();
                        String roomno = alist.get(pos).getRoomno();
                        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss aa", Locale.US);
                        calnder = Calendar.getInstance();
                        localTime = df1.format(calnder.getTime());
                        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
                        time_new = df2.format(calnder.getTime());
                        calnder = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        formattedDate = df.format(calnder.getTime());
                        // for 24 hrs time and date format
                        calnder = Calendar.getInstance();
                        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
                        timeanddate = df3.format(calnder.getTime());
                        if (finger_index.equals("3")) {
                            storeOfflineFoodAttendance(id1, propertyid, name1, formattedDate, localTime, finger_index, roomno, time_new);
                            if(sharedfloorvalue==0){
                                Capture_Match.setVisibility(View.VISIBLE);
                            }
                        } else if (finger_index.equals("6")) {
                            Calendar calendar1 = Calendar.getInstance();
                            int hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
                            if (hour1 >= 20 || hour1 < 6) {
                                storeOfflineAttendanceNight(id1, propertyid, name1, formattedDate, localTime, finger_index, roomno, time_new);
                                if(sharedfloorvalue==0){
                                    Capture_Match.setVisibility(View.VISIBLE);
                                }
                            } else {
                                attendanceSmsAlert = new AttendanceSmsAlert();
                                attendanceSmsAlert.showDialog("Alert!", "Night attendance work 8 pm to 6 am", Get_Attendance_Startek.this);
                                btn_signin.setEnabled(true);
                                btn_class.setEnabled(true);
                                btn_lunch.setEnabled(true);
                                btn_market.setEnabled(true);
                                btn_nightin.setEnabled(true);
                                btn_vacation.setEnabled(true);
                                if(sharedfloorvalue==0){
                                    Capture_Match.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            storeOfflineAttendance(id1, propertyid, name1, formattedDate, localTime, finger_index, roomno, time_new, timeanddate);
                            if(sharedfloorvalue==0){
                                Capture_Match.setVisibility(View.VISIBLE);
                            }
                        }
                        bottom_lay.setVisibility(View.GONE);

                    } else {
                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);
                        bottom_lay.setVisibility(View.GONE);

                        attendanceSmsAlert = new AttendanceSmsAlert();
                        attendanceSmsAlert.showDialog("Alert!", "Not Match Please Try Again", Get_Attendance_Startek.this);
                    }

                } else {
                    Toast toast = Toast.makeText(getBaseContext(), "First Enroll User", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    btn_signin.setEnabled(true);
                    btn_class.setEnabled(true);
                    btn_lunch.setEnabled(true);
                    btn_market.setEnabled(true);
                    btn_nightin.setEnabled(true);
                    btn_vacation.setEnabled(true);
                }
                t1 = null;
                t2 = null;
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "No Record Found!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                btn_signin.setEnabled(true);
                btn_class.setEnabled(true);
                btn_lunch.setEnabled(true);
                btn_market.setEnabled(true);
                btn_nightin.setEnabled(true);
                btn_vacation.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_startek- MatchFinger", propertyid, versionCode);
        } finally {
            if (c != null)
                c.close();
        }
    }

    //offline foodattendance
    private void storeOfflineFoodAttendance(String tenantid, String propertyid, String name1, String formattedDate, String localTime, String fingerindex, String roomno, String time_new) {
        try {
            Date mToday = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa", Locale.US);
            String curTime = sdf.format(mToday);
            String[] splited = curTime.split(" ");
            String split_one = splited[0];
            String split_second = splited[1];
            Date start, end;
            if (split_second.equals("a.m.") || split_second.equals("p.m.")) {
                start = sdf.parse("06:30:00 a.m.");
                end = sdf.parse("10:00:01 p.m.");
            } else if (split_second.equals("am") || split_second.equals("pm")) {
                start = sdf.parse("06:30:00 am");
                end = sdf.parse("10:00:01 pm");
            } else {
                start = sdf.parse("06:30:00 AM");
                end = sdf.parse("10:00:01 AM");
            }
            Date userDate = sdf.parse(curTime);
            if (userDate.after(start) && userDate.before(end)) {//Breakfast

                playSound();
                attendanceSmsAlert = new AttendanceSmsAlert();
                attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Breakfast", Get_Attendance_Startek.this);
                String query = "INSERT INTO krooms_foodattendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Breakfast','" + formattedDate + "','" + localTime + "','','1','" + roomno + "')";
                db.execSQL(query);
                btn_signin.setEnabled(true);
                btn_class.setEnabled(true);
                btn_lunch.setEnabled(true);
                btn_market.setEnabled(true);
                btn_nightin.setEnabled(true);
                btn_vacation.setEnabled(true);

            } else {
                Date mToday1 = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa", Locale.US);
                String curTime1 = sdf1.format(mToday1);
                String[] splited1 = curTime1.split(" ");
                String split_one1 = splited1[0];
                String split_second1 = splited1[1];
                Date start1, end1;

                if (split_second1.equals("a.m.") || split_second1.equals("p.m.")) {
                    start1 = sdf1.parse("10:00:00 a.m.");
                    end1 = sdf1.parse("03:00:01 p.m.");
                } else if (split_second1.equals("am") || split_second1.equals("pm")) {
                    start1 = sdf1.parse("10:00:00 am");
                    end1 = sdf1.parse("03:00:01 pm");
                } else {
                    start1 = sdf1.parse("10:00:00 AM");
                    end1 = sdf1.parse("03:00:01 PM");
                }

                Date userDate1 = sdf1.parse(curTime1);

                if (userDate1.after(start1) && userDate1.before(end1)) {//Lunch

                    playSound();
                    attendanceSmsAlert = new AttendanceSmsAlert();
                    attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Lunch", Get_Attendance_Startek.this);
                    String query = "INSERT INTO krooms_foodattendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Lunch','" + formattedDate + "','" + localTime + "','','1','" + roomno + "')";
                    db.execSQL(query);
                    btn_signin.setEnabled(true);
                    btn_class.setEnabled(true);
                    btn_lunch.setEnabled(true);
                    btn_market.setEnabled(true);
                    btn_nightin.setEnabled(true);
                    btn_vacation.setEnabled(true);

                } else {
                    Date mToday2 = new Date();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss aa", Locale.US);
                    String curTime2 = sdf2.format(mToday2);
                    String[] splited2 = curTime2.split(" ");
                    String split_one2 = splited2[0];
                    String split_second2 = splited2[1];
                    Date start2, end2;
                    if (split_second2.equals("a.m.") || split_second2.equals("p.m.")) {
                        start2 = sdf2.parse("03:00:00 p.m.");
                        end2 = sdf2.parse("06:00:01 p.m.");
                    } else if (split_second2.equals("am") || split_second2.equals("pm")) {
                        start2 = sdf2.parse("03:00:00 pm");
                        end2 = sdf2.parse("06:00:01 pm");
                    } else {
                        start2 = sdf2.parse("03:00:00 PM");
                        end2 = sdf2.parse("06:00:01 PM");
                    }
                    Date userDate2 = sdf2.parse(curTime2);
                    if (userDate2.after(start2) && userDate2.before(end2)) {//Tea Time
                        playSound();
                        attendanceSmsAlert = new AttendanceSmsAlert();
                        attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Tea", Get_Attendance_Startek.this);
                        String query = "INSERT INTO krooms_foodattendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Tea Time','" + formattedDate + "','" + localTime + "','','1','" + roomno + "')";
                        db.execSQL(query);
                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);

                    } else {
                        Date mToday3 = new Date();
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm:ss aa", Locale.US);
                        String curTime3 = sdf3.format(mToday3);

                        String[] splited3 = curTime3.split(" ");
                        String split_one3 = splited3[0];
                        String split_second3 = splited3[1];
                        Date start3, end3;

                        if (split_second3.equals("a.m.") || split_second3.equals("p.m.")) {
                            start3 = sdf3.parse("06:00:00 p.m.");
                            end3 = sdf3.parse("11:00:01 p.m.");
                        } else if (split_second3.equals("am") || split_second3.equals("pm")) {
                            start3 = sdf3.parse("06:00:00 pm");
                            end3 = sdf3.parse("11:00:01 pm");
                        } else {
                            start3 = sdf3.parse("06:00:00 PM");
                            end3 = sdf3.parse("11:00:01 PM");
                        }

                        Date userDate3 = sdf3.parse(curTime3);

                        if (userDate3.after(start3) && userDate3.before(end3)) {//Dinner

                            playSound();
                            attendanceSmsAlert = new AttendanceSmsAlert();
                            attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Dinner", Get_Attendance_Startek.this);
                            String query = "INSERT INTO krooms_foodattendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Dinner','" + formattedDate + "','" + localTime + "','','1','" + roomno + "')";
                            db.execSQL(query);
                            btn_signin.setEnabled(true);
                            btn_class.setEnabled(true);
                            btn_lunch.setEnabled(true);
                            btn_market.setEnabled(true);
                            btn_nightin.setEnabled(true);
                            btn_vacation.setEnabled(true);
                        } else {

                            attendanceSmsAlert = new AttendanceSmsAlert();
                            attendanceSmsAlert.showDialog("Alert!", "Invalid Time", Get_Attendance_Startek.this);
                            btn_signin.setEnabled(true);
                            btn_class.setEnabled(true);
                            btn_lunch.setEnabled(true);
                            btn_market.setEnabled(true);
                            btn_nightin.setEnabled(true);
                            btn_vacation.setEnabled(true);
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_startek- storeOfflineFoodAttendance", propertyid, versionCode);
        }
    }

    private void storeOfflineAttendanceNight(String tenantid, String propertyid, String name1, String formattedDate, String localTime, String fingerindex, String roomno, String time_new) {
        try {
            String attendance_status = "", dbtime = "", id = "";
            String differenceInsec = "";
            String query1 = "select * from krooms_nightattendance where propertyid='" + propertyid + "' and tenantid='" + tenantid + "' and date='" + formattedDate + "' order by id DESC LIMIT 0,1";
            attendance_cursor = db.rawQuery(query1, null);
            if (attendance_cursor.getCount() != 0) {
                if (attendance_cursor.moveToFirst()) {
                    do {
                        attendance_status = attendance_cursor.getString(5);
                        dbtime = attendance_cursor.getString(7);
                        id = attendance_cursor.getString(0);
                    } while (attendance_cursor.moveToNext());
                }
                if (attendance_status.equalsIgnoreCase("Night in")) {

                    attendanceSmsAlert = new AttendanceSmsAlert();
                    attendanceSmsAlert.showDialog("Alert!", "Already Get Night Attendance", Get_Attendance_Startek.this);
                    btn_signin.setEnabled(true);
                    btn_class.setEnabled(true);
                    btn_lunch.setEnabled(true);
                    btn_market.setEnabled(true);
                    btn_nightin.setEnabled(true);
                    btn_vacation.setEnabled(true);

                } else {
                    playSound();
                    attendanceSmsAlert = new AttendanceSmsAlert();
                    attendanceSmsAlert.showDialog("Thankyou!", "Get Attendance Successfully", Get_Attendance_Startek.this);
                    String query = "INSERT INTO krooms_nightattendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Night in','" + formattedDate + "','" + localTime + "','','1','" + roomno + "','" + time_new + "')";
                    db.execSQL(query);

                    btn_signin.setEnabled(true);
                    btn_class.setEnabled(true);
                    btn_lunch.setEnabled(true);
                    btn_market.setEnabled(true);
                    btn_nightin.setEnabled(true);
                    btn_vacation.setEnabled(true);

                }
            } else if (fingerindex.equalsIgnoreCase("6")) {

                playSound();

                attendanceSmsAlert = new AttendanceSmsAlert();
                attendanceSmsAlert.showDialog("Thankyou!", "Get Attendance Successfully", Get_Attendance_Startek.this);
                String query = "INSERT INTO krooms_nightattendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Night in','" + formattedDate + "','" + localTime + "','','1','" + roomno + "','" + time_new + "')";
                db.execSQL(query);
                btn_signin.setEnabled(true);
                btn_class.setEnabled(true);
                btn_lunch.setEnabled(true);
                btn_market.setEnabled(true);
                btn_nightin.setEnabled(true);
                btn_vacation.setEnabled(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_startek-storeOfflineAttendanceNight", propertyid, versionCode);
        }
    }

    private void storeOfflineAttendance(String tenantid, String propertyid, String name1, String formattedDate, String localTime, String fingerindex, String roomno, String time_new, String timeanddate) {
        try {
            String attendance_status = "", dbtime = "", id = "";
            String differenceInsec = "";

            String query1 = "select * from krooms_attendance where propertyid='" + propertyid + "' and tenantid='" + tenantid + "' order by id DESC LIMIT 0,1";
            attendance_cursor = db.rawQuery(query1, null);

            if (attendance_cursor.getCount() != 0) {

                if (attendance_cursor.moveToFirst()) {
                    do {
                        attendance_status = attendance_cursor.getString(5);
                        dbtime = attendance_cursor.getString(12);
                        id = attendance_cursor.getString(0);
                    } while (attendance_cursor.moveToNext());
                }
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
                Date date2 = null, date1 = null;
                try {
                    date1 = format.parse(timeanddate);
                    date2 = format.parse(dbtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek-storeOfflineAttendance Time", propertyid, versionCode);
                }
                long mills = date1.getTime() - date2.getTime();
                int hours = (int) (mills / (1000 * 60 * 60));
                int mins = (int) (mills / (1000 * 60)) % 60;
                int sec = (int) (mills / (1000)) % 60;
                int hours1 = Math.abs(hours);
                int mins1 = Math.abs(mins);
                int sec1 = Math.abs(sec);
                differenceInsec = hours1 + ":" + mins1 + ":" + sec1;

                if (!attendance_status.equalsIgnoreCase("Sign in") && fingerindex.equalsIgnoreCase("1")) {

                    playSound();
                    attendanceSmsAlert = new AttendanceSmsAlert();
                    attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Successfully", Get_Attendance_Startek.this);
                    String query = "INSERT INTO krooms_attendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew,timedate) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Sign in','" + formattedDate + "','" + localTime + "','" + differenceInsec + "','1','" + roomno + "','" + time_new + "','" + timeanddate + "')";
                    db.execSQL(query);

                    btn_signin.setEnabled(true);
                    btn_class.setEnabled(true);
                    btn_lunch.setEnabled(true);
                    btn_market.setEnabled(true);
                    btn_nightin.setEnabled(true);
                    btn_vacation.setEnabled(true);
                    getOfflineAttendanceCount();
                    getAbsentTenantList();

                } else if (attendance_status.equalsIgnoreCase("Sign in")) {//|| attendance_status.equalsIgnoreCase("Night in")

                    if (fingerindex.equalsIgnoreCase("2"))//Class
                    {
                        playSound();
                        attendanceSmsAlert = new AttendanceSmsAlert();
                        attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Successfully", Get_Attendance_Startek.this);

                        String query = "INSERT INTO krooms_attendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew,timedate) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Class','" + formattedDate + "','" + localTime + "','" + differenceInsec + "','1','" + roomno + "','" + time_new + "','" + timeanddate + "')";
                        db.execSQL(query);

                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);

                        getOfflineAttendanceCount();
                        getAbsentTenantList();


                    } else if (fingerindex.equalsIgnoreCase("4")) {//Market

                        playSound();
                        attendanceSmsAlert = new AttendanceSmsAlert();
                        attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Successfully", Get_Attendance_Startek.this);
                        String query = "INSERT INTO krooms_attendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew,timedate) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Market','" + formattedDate + "','" + localTime + "','" + differenceInsec + "','1','" + roomno + "','" + time_new + "','" + timeanddate + "')";
                        db.execSQL(query);
                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);

                        getOfflineAttendanceCount();
                        getAbsentTenantList();

                    } else if (fingerindex.equalsIgnoreCase("5")) {//Vacation
                        playSound();
                        attendanceSmsAlert = new AttendanceSmsAlert();
                        attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Successfully", Get_Attendance_Startek.this);
                        String query = "INSERT INTO krooms_attendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew,timedate) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Vacation','" + formattedDate + "','" + localTime + "','" + differenceInsec + "','1','" + roomno + "','" + time_new + "','" + timeanddate + "')";
                        db.execSQL(query);
                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);

                        getOfflineAttendanceCount();
                        getAbsentTenantList();

                    } else {

                        playSoundInvalidFinger();
                        attendanceSmsAlert = new AttendanceSmsAlert();
                        attendanceSmsAlert.showDialog("Alert!", "Please Press Valid Button", Get_Attendance_Startek.this);
                        btn_signin.setEnabled(true);
                        btn_class.setEnabled(true);
                        btn_lunch.setEnabled(true);
                        btn_market.setEnabled(true);
                        btn_nightin.setEnabled(true);
                        btn_vacation.setEnabled(true);

                    }
                } else {

                    playSoundInvalidFinger();
                    attendanceSmsAlert = new AttendanceSmsAlert();
                    attendanceSmsAlert.showDialog("Alert!", "Please Press Valid Button", Get_Attendance_Startek.this);
                    btn_signin.setEnabled(true);
                    btn_class.setEnabled(true);
                    btn_lunch.setEnabled(true);
                    btn_market.setEnabled(true);
                    btn_nightin.setEnabled(true);
                    btn_vacation.setEnabled(true);
                }

            } else if (fingerindex.equalsIgnoreCase("1")) {

                playSound();
                attendanceSmsAlert = new AttendanceSmsAlert();
                attendanceSmsAlert.showDialog("ThankYou!", "Get Attendance Successfully", Get_Attendance_Startek.this);

                String query = "INSERT INTO krooms_attendance(tenantid,propertyid,name,attendance,attendancestatus,date,time,timeinterval,status,roomno,timenew,timedate) VALUES('" + tenantid + "','" + propertyid + "','" + name1 + "','P','Sign in','" + formattedDate + "','" + localTime + "','','1','" + roomno + "','" + time_new + "','" + timeanddate + "')";
                db.execSQL(query);
                btn_signin.setEnabled(true);
                btn_class.setEnabled(true);
                btn_lunch.setEnabled(true);
                btn_market.setEnabled(true);
                btn_nightin.setEnabled(true);
                btn_vacation.setEnabled(true);
                getOfflineAttendanceCount();
                getAbsentTenantList();

            } else {
                playSoundInvalidFinger();
                attendanceSmsAlert = new AttendanceSmsAlert();
                attendanceSmsAlert.showDialog("Alert!", "Please Press Valid Button", Get_Attendance_Startek.this);

                btn_signin.setEnabled(true);
                btn_class.setEnabled(true);
                btn_lunch.setEnabled(true);
                btn_market.setEnabled(true);
                btn_nightin.setEnabled(true);
                btn_vacation.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_startek- storeOfflineAttendance", propertyid, versionCode);
        }
    }

    public void getOfflineAttendanceCount() {

        String satatus = "";
        int p = 0;
        Cursor cc1 = null, cc = null;
        String query1 = "select * from krooms_attendance where propertyid='" + propertyid + "'";
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

    private void getAbsentTenantList() {
        attendance_list.clear();
        String satatus = "";
        String query1 = "select * from krooms_attendance where propertyid='" + propertyid + "' ";
        Cursor cc1 = null, cc = null;
        cc1 = db.rawQuery(query1, null);
        if (cc1.getCount() != 0) {
            try {
                //old working
                String query = "select t1.id ,t1.tenantid ,t1.propertyid ,t1.name ,t1.attendance ,t1.attendancestatus ,t1.date ,t1.time ,t1.timeinterval ,t1.status ,t1.roomno ,t1.timenew ,t1.timedate ,t3.photo " +
                        " from krooms_attendance t1 " +
                        "left join (select * from tenant_photo ) t3 on t1.tenantid=t3.tenantid and t1.propertyid=t3.propertyid " +
                        "" +
                        "" +
                        "inner join ( SELECT tenantid,max(id) max_id FROM krooms_attendance group by tenantid ) t2 on" +
                        " t1.id=t2.max_id where t1.propertyid='" + propertyid + "' AND t1.attendancestatus!='Sign in'  order by timedate asc  ";

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
                            attendance_list.add(model);
                        } while (cc.moveToNext());
                    }
                    tenant_absent_listAdapter = new Tenant_Absent_ListAdapter(context, attendance_list);
                    grid.setAdapter(tenant_absent_listAdapter);
                    tenant_absent_listAdapter.notifyDataSetChanged();

                } else {
                    tenant_absent_listAdapter = new Tenant_Absent_ListAdapter(context, attendance_list);
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

    private void playSound() {
        try {
            mp = MediaPlayer.create(this, R.raw.attendance);
            if (mp != null && mp.isPlaying()) {//If music is playing already
                mp.stop();//Stop playing the music
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }

                ;
            });

        } catch (Exception w) {
            w.printStackTrace();
            AppError.handleUncaughtException(w, "Get_Attendance_startek- playSound", propertyid, versionCode);
        }
    }

    private void playSoundInvalidFinger() {
        try {
            mp = MediaPlayer.create(this, R.raw.validfinger);
            if (mp != null && mp.isPlaying()) {//If music is playing already
                mp.stop();//Stop playing the music
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (Exception w) {
            w.printStackTrace();
            AppError.handleUncaughtException(w, "Get_Attendance_startek- playSoundInvalidFinger", propertyid, versionCode);
        }
    }

    public static void deleteCache(Context context, String propertyid, String versionCode) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            freeMemory();
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "Get_Attendance_starteke- deleteCache", propertyid, versionCode);
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnsignin:
                finger_index = "1";
                btn_signin.setEnabled(false);
                btn_class.setEnabled(false);
                btn_lunch.setEnabled(false);
                btn_market.setEnabled(false);
                btn_nightin.setEnabled(false);
                btn_vacation.setEnabled(false);
                bottom_lay.setVisibility(View.GONE);
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- - onClick btnsignin", propertyid, versionCode);
                }
                break;

            case R.id.btnclass:
                finger_index = "2";
                btn_signin.setEnabled(false);
                btn_class.setEnabled(false);
                btn_lunch.setEnabled(false);
                btn_market.setEnabled(false);
                btn_nightin.setEnabled(false);
                btn_vacation.setEnabled(false);
                bottom_lay.setVisibility(View.GONE);
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- - onClick btnclass", propertyid, versionCode);
                }
                break;

            case R.id.btnlunch:
                finger_index = "3";
                btn_signin.setEnabled(false);
                btn_class.setEnabled(false);
                btn_lunch.setEnabled(false);
                btn_market.setEnabled(false);
                btn_nightin.setEnabled(false);
                btn_vacation.setEnabled(false);
                bottom_lay.setVisibility(View.GONE);
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- - onClick btnlunch", propertyid, versionCode);
                }
                break;

            case R.id.btnvacation:
                finger_index = "5";
                bottom_lay.setVisibility(View.GONE);
                btn_signin.setEnabled(false);
                btn_class.setEnabled(false);
                btn_lunch.setEnabled(false);
                btn_market.setEnabled(false);
                btn_nightin.setEnabled(false);
                btn_vacation.setEnabled(false);
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- - onClick btnvacation", propertyid, versionCode);
                }

                break;

            case R.id.btnmarket:
                finger_index = "4";
                btn_signin.setEnabled(false);
                btn_class.setEnabled(false);
                btn_lunch.setEnabled(false);
                btn_market.setEnabled(false);
                btn_nightin.setEnabled(false);
                btn_vacation.setEnabled(false);
                bottom_lay.setVisibility(View.GONE);
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- - onClick btnmarket", propertyid, versionCode);
                }
                break;

            case R.id.btnnight:
                finger_index = "6";
                btn_signin.setEnabled(false);
                btn_class.setEnabled(false);
                btn_lunch.setEnabled(false);
                btn_market.setEnabled(false);
                btn_nightin.setEnabled(false);
                btn_vacation.setEnabled(false);
                bottom_lay.setVisibility(View.GONE);
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Get_Attendance_startek- - onClick btnnight", propertyid, versionCode);
                }
                break;
            case R.id.dailog_cancle:
                try {
                    bottom_lay.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    AppError.handleUncaughtException(ex, "Get_Attendance_startek- - onClick dailog_cancle", propertyid, versionCode);
                }
                break;
            case R.id.countlayout:
                showSignInlist();
                break;
            case R.id.click:
                try {
                    Intent ii = new Intent(Get_Attendance_Startek.this, Unlock_Attendance_Owner_Startek.class);
                    ii.putExtra("propertyid", propertyid);
                    ii.putExtra("device", devicetype);
                    startActivity(ii);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    AppError.handleUncaughtException(ex, "Get_Attendance_startek- - onClick click", propertyid, versionCode);
                }
                break;


            case R.id.btng:
                try {
                    floorno="g";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;


            case R.id.btn1:
                try {
                    floorno="1";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;


            case R.id.btn2:
                try {
                    floorno="2";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;


            case R.id.btn3:
                try {
                    floorno="3";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;


            case R.id.btn4:
                try {
                    floorno="4";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;


            case R.id.btn5:
                try {
                    floorno="5";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;

            case R.id.btn6:
                try {
                    floorno="6";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;



            case R.id.btn7:
                try {
                    floorno="7";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;



            case R.id.btn8:
                try {
                    floorno="8";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;




            case R.id.btn9:
                try {
                    floorno="9";
                    bottom_lay.setVisibility(View.VISIBLE);
                    deleteCache(context, propertyid, versionCode);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    AppError.handleUncaughtException(ee, "Get_Attendance_startek- Capture_Match", propertyid, versionCode);
                }
                break;


        }
    }

    private void showSignInlist() {
        Intent ii = new Intent(Get_Attendance_Startek.this, Attendance_Startek_SignInList.class);
        ii.putExtra("device", devicetype);
        ii.putExtra("propertyid", propertyid);
        startActivity(ii);
        finish();
    }


    private void getVersionDevice() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(Get_Attendance_Startek.this.getPackageName(), 0);
            versionCode = info.versionName;
            Utility.setSharedPreference(Get_Attendance_Startek.this, "GetVersion", versionCode);
        } catch (Exception e) {
            AppError.handleUncaughtException(e, "Get_Attendance_startek- getVersionDevice", propertyid, versionCode);
        }
    }

    //send online hostel attendance attendance on 27-12-2018 me
    private void sendOfflineattendance() {
        attendance_list_sendserver = new ArrayList<>();
        Cursor cc = null;
        String query = "select * from krooms_attendance where status='1' and propertyid='" + propertyid + "'";
        cc = db.rawQuery(query, null);
        attendance_list_sendserver.clear();
        JSONArray jArray = null;
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
                    attendance_list_sendserver.add(model);
                } while (cc.moveToNext());
            }

            if (cc != null) {
                cc.close();
            }


            jArray = new JSONArray();
            for (int i = 0; i < attendance_list_sendserver.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("id", attendance_list_sendserver.get(i).getId());
                    jGroup.put("tenant_id", attendance_list_sendserver.get(i).getUserid());
                    jGroup.put("property_id", attendance_list_sendserver.get(i).getPropertyid());
                    jGroup.put("name", attendance_list_sendserver.get(i).getUsername());
                    jGroup.put("attendance", attendance_list_sendserver.get(i).getAttendance_status());
                    jGroup.put("reasons", attendance_list_sendserver.get(i).getAttendance_reasons());
                    jGroup.put("date", attendance_list_sendserver.get(i).getDate());
                    jGroup.put("time", attendance_list_sendserver.get(i).getTime());
                    jGroup.put("timeinterval", attendance_list_sendserver.get(i).getTimeinterval());
                    jGroup.put("roomno", attendance_list_sendserver.get(i).getRoomno());
                    jArray.put(jGroup);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek - attendance_listhostel.size()", propertyid, versionCode);
                }
            }

        } else {
        }
        try {
            if (attendance_list_sendserver.size() != 0) {
                Sendofflinehostelattendance(jArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek - sendOfflineattendancehostel()", propertyid, versionCode);
        }
    }

    public void Sendofflinehostelattendance(final JSONArray attendencelist) {
        try {
            StringRequest postRequest = new StringRequest(Request.Method.POST, WebUrls.MAIN_ATTENDACE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String result = response.toString();
                            getResponsetenantlist(result);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            AppError.handleUncaughtException(error, "ServiceSyncRecord_Attendance_Startek - onErrorResponsehostel", propertyid, versionCode);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parames = new HashMap<String, String>();
                    parames.put("action", "insertattendencesystemofflineHostel");
                    parames.put("arraylisthostel", String.valueOf(attendencelist));
                    parames.put("property_id", propertyid);
                    return parames;
                }
            };
            DefaultRetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);//1000*60=60000//1min
            postRequest.setRetryPolicy(policy);
           /* RequestQueue requestQueue = Volley.newRequestQueue(Get_Attendance_Startek.this);
            requestQueue.add(postRequest);
            */

            App.getInstance().addToRequestQueue(postRequest, "tag7");
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek  -Sendofflinehostelattendancehostel", propertyid, versionCode);
        }
    }

    public void getResponsetenantlist(String resultobj) {
        try {
            JSONObject jsmain = new JSONObject(resultobj);
            String result = jsmain.getString("flag");
            String message = jsmain.getString("message");
            if (result.equalsIgnoreCase("Y")) {
                String attendance = jsmain.getString("listhostel_localid");
                if (attendance.equalsIgnoreCase("0") || attendance.equalsIgnoreCase(null) || attendance.equals("") || attendance.equals("null")) {

                } else {
                    JSONArray jsonArray = jsmain.getJSONArray("listhostel_localid");
                    String id = jsonArray.getJSONObject(0).getString("local_id");
                    String query = "UPDATE krooms_attendance  set status='0' where id <= '" + id + "' and propertyid='" + propertyid + "'";
                    db.execSQL(query);
                }

            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek - getResponsetenantlisthostel", propertyid, versionCode);
        }
    }

    //send online food attendance on 24/01/2019
    //send online hostel attendance attendance on 27-12-2018 me
    private void sendOfflineattendanceFood() {
        foodattendance_list = new ArrayList<>();
        foodattendance_list.clear();
        JSONArray jArrayfood = null;
        String queryfood = "select * from krooms_foodattendance where status='1' and propertyid='" + propertyid + "'";
        Cursor cursor = db.rawQuery(queryfood, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    UserModel model = new UserModel();
                    model.setId(cursor.getString(0));
                    model.setUserid(cursor.getString(1));
                    model.setPropertyid(cursor.getString(2));
                    model.setUsername(cursor.getString(3));
                    model.setAttendance_status(cursor.getString(4));
                    model.setAttendance_reasons(cursor.getString(5));
                    model.setDate(cursor.getString(6));
                    model.setTime(cursor.getString(7));
                    model.setStatus(cursor.getString(9));
                    model.setRoomno(cursor.getString(10));
                    foodattendance_list.add(model);
                } while (cursor.moveToNext());
            }
            jArrayfood = new JSONArray();
            for (int i = 0; i < foodattendance_list.size(); i++) {
                JSONObject jGroup = new JSONObject();
                try {
                    jGroup.put("id", foodattendance_list.get(i).getId());
                    jGroup.put("tenant_id", foodattendance_list.get(i).getUserid());
                    jGroup.put("property_id", foodattendance_list.get(i).getPropertyid());
                    jGroup.put("name", foodattendance_list.get(i).getUsername());
                    jGroup.put("attendance", foodattendance_list.get(i).getAttendance_status());
                    jGroup.put("reasons", foodattendance_list.get(i).getAttendance_reasons());
                    jGroup.put("date", foodattendance_list.get(i).getDate());
                    jGroup.put("time", foodattendance_list.get(i).getTime());
                    jGroup.put("timeinterval", foodattendance_list.get(i).getTimeinterval());
                    jGroup.put("roomno", foodattendance_list.get(i).getRoomno());
                    jArrayfood.put(jGroup);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
        }
        try {
            if (foodattendance_list.size() != 0) {
                SendofflinehostelattendanceFood(jArrayfood);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek - sendOfflineattendanceFood()", propertyid, versionCode);

        }
    }

    public void SendofflinehostelattendanceFood(final JSONArray attendancefoodlist) {
        try {
            StringRequest postRequest = new StringRequest(Request.Method.POST, WebUrls.MAIN_ATTENDACE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String result = response.toString();
                            getResponsetenantlistFood(result);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            AppError.handleUncaughtException(error, "ServiceSyncRecord_Attendance_Startek - onErrorResponsefood", propertyid, versionCode);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parames = new HashMap<String, String>();
                    parames.put("action", "insertattendencesystemofflineFood");
                    parames.put("arraylistfood", String.valueOf(attendancefoodlist));
                    parames.put("property_id", propertyid);
                    return parames;
                }
            };
            DefaultRetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);//1000*60=60000//1min
            postRequest.setRetryPolicy(policy);
          /*  RequestQueue requestQueue = Volley.newRequestQueue(Get_Attendance_Startek.this);
            requestQueue.add(postRequest);*/

            App.getInstance().addToRequestQueue(postRequest, "tag8");

        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek  -Sendofflinehostelattendancefood", propertyid, versionCode);
        }
    }

    public void getResponsetenantlistFood(String resultobj) {
        try {
            JSONObject jsmain = new JSONObject(resultobj);
            String result = jsmain.getString("flag");
            String message = jsmain.getString("message");
            if (result.equalsIgnoreCase("Y")) {
                String food = jsmain.getString("food_localid");
                if (food.equalsIgnoreCase("0") || food.equalsIgnoreCase(null) || food.equals("") || food.equals("null")) {
                } else {
                    JSONArray jsonArrayfood = jsmain.getJSONArray("food_localid");
                    String idfood = jsonArrayfood.getJSONObject(0).getString("local_id");
                    String query = "UPDATE krooms_foodattendance  set status='0' where id <= '" + idfood + "' and propertyid='" + propertyid + "'";
                    db.execSQL(query);
                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
            AppError.handleUncaughtException(e, "ServiceSyncRecord_Attendance_Startek - getResponsetenantlistfood", propertyid, versionCode);
        }
    }
}

