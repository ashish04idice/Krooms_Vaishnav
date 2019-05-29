/*

 * Copyright (C) 2013 SecuGen Corporation
 *
 */

package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import library.ashish.example.com.jarauthentication.Encodingformat;

public class Unlock_Attendance_Owner_Secugen extends Activity
        implements View.OnClickListener, Runnable {
    private static final String TAG = "SecuGen USB";
    private Button mCapture, msavebtn, mcomparebtn;
    private PendingIntent mPermissionIntent;
    private ImageView mImageViewFingerprint;
    private int[] mMaxTemplateSize;
    private int mImageWidth;
    private int mImageHeight;
    private int[] grayBuffer;
    private Bitmap grayBitmap;
    private IntentFilter filter;
    private JSGFPLib sgfplib;
    private byte[] EKYCmRegisterImage;
    private byte[] EKYCmRegisterTemplate;
    private EditText meditText1;
    private SQLiteDatabase db;
    private Cursor c;
    String aa, localimage;
    String EKYCmdatabase;
    private byte[] EKYCmRegisterTemplateOne;
    String base64uid;
    private boolean[] matched = new boolean[1];
    byte[] RegTemplate1 = new byte[1200];
    byte[] RegTemplate2 = new byte[1200];
    public static ArrayList<UserModel> alist;
    String fingerindex;
    CheckBox c1, c2, c3;
    public static int indexval = 0;
    String propertyid;
    Context context;
    DatabaseHandler handlerdb;
    String devicetype = "";
    RelativeLayout back;
    //This broadcast receiver is necessary to get user permissions to access the attached USB device
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                        } else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_owner_secugen);
        context = getApplicationContext();
        handlerdb = new DatabaseHandler(context);
        db = handlerdb.getWritableDatabase();
        Intent ii = getIntent();
        propertyid = ii.getStringExtra("propertyid");
        devicetype = ii.getStringExtra("device");
        alist = new ArrayList<>();
        mCapture = (Button) findViewById(R.id.btncapture);
        msavebtn = (Button) findViewById(R.id.btnsave);
        mCapture.setOnClickListener(this);
        msavebtn.setOnClickListener(this);
        mImageViewFingerprint = (ImageView) findViewById(R.id.imageshow);
        back = (RelativeLayout) findViewById(R.id.flback_button);
        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES * JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i = 0; i < grayBuffer.length; ++i)
            grayBuffer[i] = Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
        mImageViewFingerprint.setImageBitmap(grayBitmap);
        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2) * (JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2)];
        for (int i = 0; i < sintbuffer.length; ++i)
            sintbuffer[i] = Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2);
        mMaxTemplateSize = new int[1];
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        sgfplib = new JSGFPLib((UsbManager) getSystemService(Context.USB_SERVICE));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Unlock_Attendance_Owner_Secugen.this, Tenant_Details_Activity.class);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("device", devicetype);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        sgfplib.CloseDevice();
        unregisterReceiver(mUsbReceiver);
        mImageViewFingerprint.setImageBitmap(grayBitmap);
        EKYCmRegisterImage = null;
        EKYCmRegisterTemplate = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
            else
                dlgAlert.setMessage("Fingerprint device initialization failed!");
            dlgAlert.setTitle("SecuGen Demo");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                            return;
                        }
                    }
            );
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        } else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Secugen fingerprint sensor not found!");
                dlgAlert.setTitle("SecuGen Demo");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                                return;
                            }
                        }
                );
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } else {
                sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                error = sgfplib.OpenDevice(0);
                SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
                error = sgfplib.GetDeviceInfo(deviceInfo);
                mImageWidth = deviceInfo.imageWidth;
                mImageHeight = deviceInfo.imageHeight;
                sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                EKYCmRegisterTemplate = new byte[mMaxTemplateSize[0]];
            }
        }
    }

    @Override
    public void onDestroy() {

        sgfplib.CloseDevice();
        EKYCmRegisterImage = null;
        EKYCmRegisterTemplate = null;
        sgfplib.Close();
        super.onDestroy();
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int color = bmpOriginal.getPixel(x, y);
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;
                int gray = (r + g + b) / 3;
                color = Color.rgb(gray, gray, gray);
                bmpGrayscale.setPixel(x, y, color);
            }
        }
        return bmpGrayscale;
    }

    public void onClick(View v) {

        if (v == mCapture) {
            byte[] EKYCmRegisterImage = new byte[mImageWidth * mImageHeight];
            long result = sgfplib.GetImageEx(EKYCmRegisterImage, 15000, 40);//15000
            if (result == 0) {
                Bitmap b = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
                b.setHasAlpha(false);
                int[] intbuffer = new int[mImageWidth * mImageHeight];
                for (int i = 0; i < intbuffer.length; ++i)
                    intbuffer[i] = (int) EKYCmRegisterImage[i];
                b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight);
                mImageViewFingerprint.setImageBitmap(this.toGrayscale(b));
                result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                SGFingerInfo fpInfo = new SGFingerInfo();
                for (int i = 0; i < EKYCmRegisterTemplate.length; ++i)
                    EKYCmRegisterTemplate[i] = 0;
                result = sgfplib.CreateTemplate(fpInfo, EKYCmRegisterImage, EKYCmRegisterTemplate);
                int[] len = new int[1];
                result = sgfplib.GetTemplateSize(EKYCmRegisterTemplate, len);
                int flags = Base64.NO_WRAP;
                // base64uid is the base64 ISO template for UIDAI AUTH/EKYC
                base64uid = Base64.encodeToString(EKYCmRegisterTemplate, 0, len[0], flags);
                Context context = getApplicationContext();

            } else {
            }
        }

        if (!base64uid.equals("")) {
            callThumbMatch(base64uid);
        } else {
            Toast.makeText(Unlock_Attendance_Owner_Secugen.this, "Please Insert all record", Toast.LENGTH_SHORT).show();
        }
    }


    private void callThumbMatch(String base64uid) {
        boolean status = false;

        String query = "select * from ownerfingerprint where propertyid='" + propertyid + "'";

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
        } else {
        }
        int pos = 0;
        if (alist.size() != 0) {

            for (int k = 0; k < alist.size(); k++) {

                String datathumbfinger = alist.get(k).getThumbfinger();
                try {
                    Encodingformat encodingformat = new Encodingformat();
                    byte[] arrayOfByte = encodingformat.convertToStream(base64uid);
                    byte[] arrayOfBytedata = encodingformat.convertToStream(datathumbfinger);
                    long ll = sgfplib.MatchTemplate(arrayOfBytedata, arrayOfByte, 5L, this.matched);

                    if (matched[0]) {
                        status = true;
                        pos = k;
                        break;
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AppError.handleUncaughtException(e, "Tenant_Detail_Activity-Secugen- callThumbMatch", propertyid, "");
                }
            }

            if (status == true) {

                Intent ii = new Intent(Unlock_Attendance_Owner_Secugen.this, HostelListActivity.class);
                startActivity(ii);

            } else {

                Toast toast = Toast.makeText(getBaseContext(), "Not Match", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Intent ii = new Intent(Unlock_Attendance_Owner_Secugen.this, Tenant_Details_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", devicetype);
                startActivity(ii);
            }
        } else {

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
            TextView txt = (TextView) layout.findViewById(R.id.toastmsg);
            txt.setText("First Enroll User");
            ImageView image = (ImageView) layout.findViewById(R.id.image);
            image.setImageResource(R.drawable.green_right);
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    public void run() {
        while (true) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(Unlock_Attendance_Owner_Secugen.this, Tenant_Details_Activity.class);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("device", devicetype);
                startActivity(intent);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


}
