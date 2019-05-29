
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
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Tenant_Absent_ListAdapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.customViewGroup;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.UserModel;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Unlock_Attendance_Owner_Startek extends AppCompatActivity implements FM220_Scanner_Interface, View.OnClickListener {
    private acpl_FM220_SDK FM220SDK;
    private Button Capture_No_Preview, Capture_PreView, Capture_BackGround;
    TextView Capture_Match, txtcounttotal, txtcount;
    private Button Enroll_finger, abort_button;
    private TextView textMessage, property_name;
    private ImageView imageView;
    private SQLiteDatabase db;
    Context context;
    DatabaseHandlerStartak_Sdk handlerdb;
    String propertyid = "", roomno = "", tenantname = "";
    Calendar calnder;
    String localTime = "", formattedDate = "", time_new = "", timeanddate = "";
    MediaPlayer mp;
    public static ArrayList<UserModel> attendance_list;
    private static final String Telecom_Device_Key = "";
    private byte[] t1, t2;
    private Cursor c;
    private Cursor attendance_cursor;
    LinearLayout listicon, bottom_lay;
    Handler handler, handlerhide;
    RelativeLayout back;
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
    public static ArrayList<UserModel> alist;
    public static final int OVERLAY_PERMISSION_REQ_CODE = 4545;

    private void showMessageDialogue(String s, String message) {
        new AlertDialog.Builder(Unlock_Attendance_Owner_Startek.this)
                .setCancelable(false)
                .setTitle(s)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

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
                }
            }
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        // call method to set up device communication
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
                    // call method to set up device communication & Check pid
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mUsbReceiver);
            FM220SDK.unInitFM220();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_tartek_owner);
        context = getApplicationContext();
        handlerdb = new DatabaseHandlerStartak_Sdk(context);
        db = handlerdb.getWritableDatabase();
        SharedStorage mSharedStorage = SharedStorage.getInstance(Unlock_Attendance_Owner_Startek.this);

        Intent ii = getIntent();
        propertyid = ii.getStringExtra("propertyid");
        devicetype = ii.getStringExtra("device");
        attendance_list = new ArrayList<>();
        textMessage = (TextView) findViewById(R.id.textMessage);
        Capture_Match = (TextView) findViewById(R.id.btncapture);
        back = (RelativeLayout) findViewById(R.id.flback_button);
        propertyname = mSharedStorage.getPropertyname();
        SharedPreferences sp = getSharedPreferences("last_FM220_type", Activity.MODE_PRIVATE);
        boolean oldDevType = sp.getBoolean("FM220type", true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(Unlock_Attendance_Owner_Startek.this, Get_Attendance_Startek.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", devicetype);
                startActivity(ii);
            }
        });

        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        final Intent piIntent = new Intent(ACTION_USB_PERMISSION);
        if (Build.VERSION.SDK_INT >= 16) piIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        mPermissionIntent = PendingIntent.getBroadcast(getBaseContext(), 1, piIntent, 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(mUsbReceiver, filter);
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
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean FunctionBase64(String temp1, String temp2) {
        try {
            if (FM220SDK.MatchFM220String(temp1, temp2)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void DisableCapture() {
    }

    private void EnableCapture() {
    }

    @Override
    public void ScannerProgressFM220(final boolean DisplayImage, final Bitmap ScanImage, final boolean DisplayText, final String statusMessage) {
        Unlock_Attendance_Owner_Startek.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (DisplayText) {
                    textMessage.setText(statusMessage);
                    textMessage.invalidate();
                }
                if (DisplayImage) {

                }
            }
        });
    }

    @Override
    public void ScanCompleteFM220(final fm220_Capture_Result result) {
        Unlock_Attendance_Owner_Startek.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (FM220SDK.FM220Initialized()) EnableCapture();
                if (result.getResult()) {
                    if (result.isEnroll()) {  // if isEnroll return true then result.getISO_Template() return enrolled finger data .
                        textMessage.setText("Finger Enroll Success");

                    } else {
                        textMessage.setText("Success NFIQ:" + Integer.toString(result.getNFIQ()) + "  SrNo:" + result.getSerialNo());
                    }
                    t1 = result.getISO_Template();
                    MatchFinger();
                } else {

                }
            }
        });
    }

    @Override
    public void ScanMatchFM220(final fm220_Capture_Result result) {
        Unlock_Attendance_Owner_Startek.this.runOnUiThread(new Runnable() {
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
            String fingerimage = "";
            alist = new ArrayList<>();
            alist.clear();
            String query = "select * from ownerfingerprint where propertyid='" + propertyid + "' ";
            c = db.rawQuery(query, null);
            String img;
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        UserModel model = new UserModel();
                        model.setPropertyid(c.getString(0));
                        model.setThumbfinger(c.getString(1));
                        model.setThumbindex(c.getString(2));
                        alist.add(model);
                    } while (c.moveToNext());
                }
                String t1base64, t2base64;
                t2base64 = Base64.encodeToString(t1, Base64.NO_WRAP);
                int pos = 0;
                boolean matchval = false;

                if (alist.size() != 0) {
                    for (int k = 0; k < alist.size(); k++) {

                        String datathumbfinger = alist.get(k).getThumbfinger();
                        matchval = FunctionBase64(datathumbfinger, t2base64);

                        if (matchval) {
                            pos = k;
                            status = true;
                            break;
                        } else {

                        }
                    }

                    if (status == true) {
                        Intent ii = new Intent(Unlock_Attendance_Owner_Startek.this, HostelListActivity.class);
                        startActivity(ii);
                    } else {

                        Toast toast = Toast.makeText(getBaseContext(), "Not Match", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Intent ii = new Intent(Unlock_Attendance_Owner_Startek.this, Get_Attendance_Startek.class);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);
                    }

                } else {
                    Toast toast = Toast.makeText(getBaseContext(), "First Enroll User", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                t1 = null;
                t2 = null;

            } else {
                Toast toast = Toast.makeText(getBaseContext(), "No Record Found!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ii = new Intent(Unlock_Attendance_Owner_Startek.this, Get_Attendance_Startek.class);
            ii.putExtra("propertyid", propertyid);
            ii.putExtra("device", devicetype);
            startActivity(ii);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnsignin:

                finger_index = "1";
                try {
                    DisableCapture();
                    textMessage.setText("Pl wait..");
                    FM220SDK.CaptureFM220(2, true, false);

                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }
}

