package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.Utility.NetworkConnection;
import com.krooms.hostel.rental.property.app.asynctask.LocalDataInsertUpdateAsyntask;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 3000;//500
    private String deviceRegistrationId = "";
    private Common mCommon;
    private Validation mValidation;
    ImageLoader imageLoader;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = getApplicationContext();
        mCommon = new Common();

        deleteCache(context);
        if (NetworkConnection.isConnected(SplashActivity.this)) {
            Intent intent = new Intent(SplashActivity.this, GetdataService.class);
            startService(intent);
        } else {
            mCommon.displayAlert(SplashActivity.this, getResources().getString(R.string.str_no_network), false);
        }


        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheSize(41943040)
                .discCacheSize(104857600)
                .threadPoolSize(20)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);


        if ( ContextCompat.checkSelfPermission(this,  Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mValidation = new Validation(SplashActivity.this);
                    if (NetworkConnection.isConnected(SplashActivity.this)) {
                        //GetAppVersion();
                        callMethods();
                    } else {
                        mCommon.displayAlert(SplashActivity.this, getResources().getString(R.string.str_no_network), false);
                    }
                }
            }, 3000);

        } else {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    private void callMethods() {

        SharedStorage mSharedStorage = SharedStorage.getInstance(SplashActivity.this);
        // deviceRegistrationId = mSharedStorage.getPushRegistrationID();

        if (mSharedStorage.getLoginStatus()) {

            if (mSharedStorage.getUserType().equals("5")) {
                Intent mIntent = new Intent(SplashActivity.this, Home_Accountantactivity.class);
                startActivity(mIntent);
            } else {
                Intent mIntent = new Intent(SplashActivity.this, HostelListActivity.class);
                startActivity(mIntent);
            }
        } else {
            mSharedStorage.clearUserData();
            Intent mIntent = new Intent(SplashActivity.this, LandingActivityWithoutLogin.class);
            startActivity(mIntent);
        }
        finish();
    }

    //for permission
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[4] == PackageManager.PERMISSION_GRANTED && grantResults[5] == PackageManager.PERMISSION_GRANTED
                        ) {
                    // permission was granted
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mValidation = new Validation(SplashActivity.this);
                            if (NetworkConnection.isConnected(SplashActivity.this)) {
                                // GetAppVersion();
                                callMethods();
                            } else {
                                mCommon.displayAlert(SplashActivity.this, getResources().getString(R.string.str_no_network), false);
                            }

                        }
                    },3000);
                } else {
                    finish();
                }
            }
        }
    }
    /*code to check app version*/
    private void GetAppVersion() {
        final ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getAppVersion");

            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        String message = response.getString("message");
                        if (!message.equals("3.0")) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                            builder.setTitle("A New Update is Available");
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                            ("https://play.google.com/store/apps/details?id=com.krooms.hostel.rental.property.app&hl=en")));
                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ActivityCompat.finishAffinity(SplashActivity.this);
                                }
                            });

                            builder.setCancelable(false);
                            builder.show();
                        } else {
                            callMethods();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
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
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}