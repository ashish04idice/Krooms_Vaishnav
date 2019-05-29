
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
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acpl.access_computech_fm220_sdk.FM220_Scanner_Interface;
import com.acpl.access_computech_fm220_sdk.acpl_FM220_SDK;
import com.acpl.access_computech_fm220_sdk.fm220_Capture_Result;
import com.acpl.access_computech_fm220_sdk.fm220_Init_Result;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class Capture_ImageActivity_Startek extends AppCompatActivity implements FM220_Scanner_Interface {
    private acpl_FM220_SDK FM220SDK;
    private Button Capture_No_Preview, Capture_PreView, Capture_BackGround, Capture_Match;
    private Button Enroll_finger, abort_button;
    private TextView textMessage;
    private ImageView imageView;
    private SQLiteDatabase db;
    Context context;
    DatabaseHandlerStartak_Sdk handlerdb;
    String tenantid="",roomno="",tenantname="",propertyid="",fingerindex="",devicetype="";
    private static final String Telecom_Device_Key = "";
    private byte[] t1, t2;
    private Cursor c=null;
    private UsbManager manager;
    private PendingIntent mPermissionIntent;
    private UsbDevice usb_Dev;
    private static final String ACTION_USB_PERMISSION = "com.ACPL.FM220_Telecom.USB_PERMISSION";
    private String pidDataStr = "";
    private Intent intentClaim;
    private Intent intentRelease;
    private static boolean isLocalConn = false;
    RelativeLayout back;
    public static ArrayList<UserModel> alist;
    private void showMessageDialogue(String s, String message) {
        new AlertDialog.Builder(Capture_ImageActivity_Startek.this)
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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_imagestartek_layout);
        context = getApplicationContext();
        handlerdb = new DatabaseHandlerStartak_Sdk(context);
        db = handlerdb.getWritableDatabase();
        alist = new ArrayList<>();
        Intent ii=getIntent();
        tenantid=ii.getStringExtra("ID");
        roomno=ii.getStringExtra("Roomno");
        tenantname=ii.getStringExtra("NAME");
        fingerindex=ii.getStringExtra("Finger");
        propertyid=ii.getStringExtra("propertyid");
        devicetype=ii.getStringExtra("device");
        textMessage = (TextView) findViewById(R.id.textMessage);
        Capture_No_Preview = (Button) findViewById(R.id.btncapture);
        imageView = (ImageView) findViewById(R.id.imageshow);
        back = (RelativeLayout) findViewById(R.id.flback_button);
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

        Capture_No_Preview.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisableCapture();
                textMessage.setText("Pl wait..");
                FM220SDK.CaptureFM220(2, true, false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Capture_ImageActivity_Startek.this, Finger_Selction_Activity.class);
                intent.putExtra("ID", tenantid);
                intent.putExtra("NAME", tenantname);
                intent.putExtra("Finger", fingerindex);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("Roomno", roomno);
                intent.putExtra("Akey", "Home");
                intent.putExtra("device", devicetype);
                startActivity(intent);

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
        Capture_No_Preview.setEnabled(false);
        imageView.setImageBitmap(null);
    }

    private void EnableCapture() {
        Capture_No_Preview.setEnabled(true);
    }

    @Override
    public void ScannerProgressFM220(final boolean DisplayImage, final Bitmap ScanImage, final boolean DisplayText, final String statusMessage) {
        Capture_ImageActivity_Startek.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (DisplayText) {
                    textMessage.setText(statusMessage);
                    textMessage.invalidate();
                }
                if (DisplayImage) {
                    imageView.setImageBitmap(ScanImage);
                    imageView.invalidate();
                }
            }
        });
    }

    @Override
    public void ScanCompleteFM220(final fm220_Capture_Result result) {
        Capture_ImageActivity_Startek.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (FM220SDK.FM220Initialized()) EnableCapture();
                if (result.getResult()) {
                    imageView.setImageBitmap(result.getScanImage());
                    if (result.isEnroll()) {  // if isEnroll return true then result.getISO_Template() return enrolled finger data .
                        textMessage.setText("Finger Enroll Success");
                    } else {
                        textMessage.setText("Success NFIQ:" + Integer.toString(result.getNFIQ()) + "  SrNo:" + result.getSerialNo());
                    }
                    if (t1 == null) {
                        t1 = result.getISO_Template();
                        String t1base64, t2base64;
                        t1base64 = Base64.encodeToString(t1, Base64.NO_WRAP);
                        InsertIntoDB(t1base64);
                    }

               } else {
                    imageView.setImageBitmap(null);
                    textMessage.setText(result.getError());
                }
                imageView.invalidate();
                textMessage.invalidate();
            }
        });
    }

    @Override
    public void ScanMatchFM220(final fm220_Capture_Result result) {
        Capture_ImageActivity_Startek.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (FM220SDK.FM220Initialized()) EnableCapture();
                if (result.getResult()) {
                    imageView.setImageBitmap(result.getScanImage());
                    textMessage.setText("Finger matched\n" + "Success NFIQ:" + Integer.toString(result.getNFIQ()));
                } else {
                    imageView.setImageBitmap(null);
                    textMessage.setText("Finger not matched\n" + result.getError());
                }
                imageView.invalidate();
                textMessage.invalidate();
            }
        });
    }


    private void InsertIntoDB(String fingerimage) {

        if (!fingerimage.equals("") ) {

            try {

                String query1 = "select * from persons where userid='"+tenantid+"' and propertyid='"+propertyid+"' and thumbindex='"+fingerindex+"'";
                c = db.rawQuery(query1, null);

                if (c.getCount() != 0) {
                   // Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show();
                    String query2 = null;
                    query2 = "UPDATE persons  set thumbfinger='" + fingerimage + "',name='"+tenantname+"' where userid='"+tenantid+"' and propertyid='"+propertyid+"' and  thumbindex='"+fingerindex+"';";
                    db.execSQL(query2);
                     new SendRecordtoserver(tenantid,tenantname,fingerimage,fingerindex,propertyid,roomno).execute();

                }else{
                     String query = "INSERT INTO persons (userid,name,propertyid,thumbfinger,thumbindex,roomno) VALUES('"+tenantid+"','"+tenantname+"','"+propertyid+"','" + fingerimage+ "','"+fingerindex+"','"+roomno+"')";
                    db.execSQL(query);
                    new SendRecordtoserver(tenantid,tenantname,fingerimage,fingerindex,propertyid,roomno).execute();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            finally {
                 if(c!= null)
                    c.close();
            }
        }
    }

    // this class is used to save Tenantfingerrecord to server
    public class SendRecordtoserver extends AsyncTask<String, String, String> {

        String tenant_id, tenant_name, property_id, fingerimage, fingerindex,roomno;

        public SendRecordtoserver(String uid, String uname, String base64uid, String fingerindex, String propertyid,String roomno) {

            tenant_id = uid;
            tenant_name = uname;
            property_id = propertyid;
            fingerimage = base64uid;
            this.fingerindex = fingerindex;
            this.roomno=roomno;
        }

        private ProgressDialog dialog = new ProgressDialog(Capture_ImageActivity_Startek.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action","enrolltenantattendencestartek"));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id", tenant_id));
            listNameValuePairs.add(new BasicNameValuePair("tenant_name", tenant_name));
            listNameValuePairs.add(new BasicNameValuePair("property_id", property_id));
            listNameValuePairs.add(new BasicNameValuePair("finger_image", fingerimage));
            listNameValuePairs.add(new BasicNameValuePair("finger_index", fingerindex));
            listNameValuePairs.add(new BasicNameValuePair("room_no",roomno));
            url = WebUrls.MAIN_ATTENDACE_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
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
                        Intent intent = new Intent(Capture_ImageActivity_Startek.this, Finger_Selction_Activity.class);
                        intent.putExtra("ID", tenantid);
                        intent.putExtra("NAME", tenantname);
                        intent.putExtra("Finger", fingerindex);
                        intent.putExtra("propertyid", propertyid);
                        intent.putExtra("Roomno", roomno);
                        intent.putExtra("Akey", "Home");
                        intent.putExtra("device", devicetype);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(Capture_ImageActivity_Startek.this, Finger_Selction_Activity.class);
                intent.putExtra("ID", tenantid);
                intent.putExtra("NAME", tenantname);
                intent.putExtra("Finger", fingerindex);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("Roomno", roomno);
                intent.putExtra("Akey", "Home");
                intent.putExtra("device", devicetype);
                startActivity(intent);
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

}

