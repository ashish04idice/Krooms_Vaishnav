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
import android.os.Environment;
import android.support.annotation.IdRes;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.VerifyFingerAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;

public class Capture_FingerImage_Terms_Activity extends Activity
        implements View.OnClickListener, Runnable,ServiceResponce {

    private static final String TAG = "SecuGen USB";
    private static String PATH_OF_IMAGE ="" ;
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
    String base64uid = "";
    private boolean[] matched = new boolean[1];
    byte[] RegTemplate1 = new byte[1200];
    byte[] RegTemplate2 = new byte[1200];
    public static ArrayList<UserModel> alist;
    String uid, uname, fingerindex, tenant_id = "";
    CheckBox c1, c2, c3;
    public static int indexval = 0;
    String propertyid = "", date;
    private SharedStorage mSharedStorage;
    private RadioButton radiotenant, radioparent;
    RadioGroup group;
    String usertype = "";
    ImageView imageView;
    public static String GET_TEMP_FOLDER_PATH;
    public static String GET_MAIN_FOLDER_PATH;
    private static String FILESAVE_BASEURL;
    String imagepath="",terms="";
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
        setContentView(R.layout.capture_finger_terms_layout);
        mSharedStorage = SharedStorage.getInstance(this);

        // createDatabase();
        Intent ii = getIntent();
        tenant_id = ii.getStringExtra("Tenantid");
        propertyid = ii.getStringExtra("propertyid");
        usertype=ii.getStringExtra("Key");
        terms=ii.getStringExtra("Terms");
        DateFormat df = new SimpleDateFormat("d/MM/yyyy,HH:mm:ss", Locale.US);
        date = df.format(Calendar.getInstance().getTime());
        alist = new ArrayList<>();
        mCapture = (Button) findViewById(R.id.btncapture);
        msavebtn = (Button) findViewById(R.id.btnsave);
        group = (RadioGroup) findViewById(R.id.RadioGroup);
        radiotenant = (RadioButton) findViewById(R.id.tenantRadioBtn);
        radioparent = (RadioButton) findViewById(R.id.parentRadioBtn);
        mCapture.setOnClickListener(this);
        msavebtn.setOnClickListener(this);
        imageView=(ImageView)findViewById(R.id.imageshow1);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radiotenant.isChecked()) {
                    usertype = "1";//for tenant
                } else if (radioparent.isChecked()) {
                    usertype = "2";//for parent
                }
            }
        });

        mImageViewFingerprint = (ImageView) findViewById(R.id.imageshow);
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
            long result = sgfplib.GetImageEx(EKYCmRegisterImage, 15000, 40);
            if (result == 0) {
                Bitmap b = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
                b.setHasAlpha(false);
                int[] intbuffer = new int[mImageWidth * mImageHeight];
                for (int i = 0; i < intbuffer.length; ++i)
                    intbuffer[i] = (int) EKYCmRegisterImage[i];
                b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight);
                mImageViewFingerprint.setImageBitmap(this.toGrayscale(b));
                  Bitmap bitmap= this.toGrayscale(b);
                  //imageView.setImageBitmap(bitmap);
                  imagepath=createImage(bitmap);
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
                // write error handler code here
            }
        }


        if (v == msavebtn) {

            if (!base64uid.equals("")) {

                    if (NetworkConnection.isConnected(Capture_FingerImage_Terms_Activity.this)) {
                        //sendToServer();

                        VerifyFingerAsyncTask serviceAsynctask = new VerifyFingerAsyncTask(Capture_FingerImage_Terms_Activity.this, "");
                        serviceAsynctask.setCallBack(this);
                        serviceAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,propertyid,
                                tenant_id,usertype,imagepath,terms);
                    }else {
                        Toast.makeText(Capture_FingerImage_Terms_Activity.this, "Please Check Internet ", Toast.LENGTH_SHORT).show();
                    }


            } else {
                Toast.makeText(Capture_FingerImage_Terms_Activity.this, "Capture Finger Null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(ImageView bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        return stream.toByteArray();
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
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
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void getResponseData(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(Capture_FingerImage_Terms_Activity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Capture_FingerImage_Terms_Activity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        public String createImage(Bitmap bitmap){
            FILESAVE_BASEURL = Environment.getExternalStorageDirectory().getAbsolutePath();
            GET_MAIN_FOLDER_PATH = FILESAVE_BASEURL + File.separator + "KroomsFingerImages";
            //GET_TEMP_FOLDER_PATH = FILESAVE_BASEURL + File.separator + MAIN_FOLDER_NAME + File.separator + TEMP_FOLDER_NAME;
            File file = new File(GET_MAIN_FOLDER_PATH);
            if (!file.exists()) {
                file.mkdir();
            }

            FileOutputStream fOut;
            try {
                File f = new File(GET_MAIN_FOLDER_PATH, "photo_ticket_" + new Date().getTime() + ".jpg");
                fOut = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                bitmap.recycle();
                PATH_OF_IMAGE = f.getAbsolutePath();

            }
            catch (FileNotFoundException e){
            e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return PATH_OF_IMAGE;
        }


    @Override
    public void requestResponce(String result) {

        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(Capture_FingerImage_Terms_Activity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Capture_FingerImage_Terms_Activity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}