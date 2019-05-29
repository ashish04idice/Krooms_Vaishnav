package com.krooms.hostel.rental.property.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.FileUtils;
import com.krooms.hostel.rental.property.app.Utility.ScalingUtilities;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.AttedanceadsModel;
import com.krooms.hostel.rental.property.app.modal.EnrollTenantModel;
import com.krooms.hostel.rental.property.app.modal.RowItem;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ashishdice on 22/4/17.
 */

public class Attandance_Home_Activity extends Activity {

    Button btn_gatattandance, btn_showtenant, btn_showfood, btn_matchattendance, btn_deleterecord,btn_enrollowner,btn_getphoto,btn_savephoto,btn_updatephoto,btn_ads,btn_insert;
    String formattedDate;
    String localTime;
    RelativeLayout backarraow;
    Intent in;
    String propertyid;
    private SQLiteDatabase db,database;
    Context context;
    DatabaseHandler handlerdb;
    public static ArrayList<EnrollTenantModel> listtenantrecord;
    public static ArrayList<EnrollTenantModel> listownerrecord;
    private Cursor c=null;
    boolean status=false,statusfood=false;
    String devicetype="";
    LinearLayout lay_enrollowner;
    DatabaseHandlerStartak_Sdk handlerdbstartek;
    public static ArrayList<UserModel> arrayListallimages;
    private ProgressDialog mProgressDialog;
    private AsyncTask mMyTask;
    private Activity mActivity;
    public static ArrayList<AttedanceadsModel> arraylistads;
    private ProgressDialog mProgressDialogads;
    private AsyncTask mMyTaskads;
    String PATH_OF_IMAGE="";
    public static String GET_MAIN_FOLDER_PATH;
    private static String FILESAVE_BASEURL;
    private final String MAIN_FOLDER_NAME = "Krooms Attendance Photo";
    GetXMLTask task;
    LinearLayout layfloor;
    EditText editfloor;
    Button btnsavefloor;

    int floorvalue=0,studentcount=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_home_layout);
        context=getApplicationContext();
        //folder path internal storage
        FILESAVE_BASEURL = Environment.getExternalStorageDirectory().getAbsolutePath();
        GET_MAIN_FOLDER_PATH = FILESAVE_BASEURL + File.separator + MAIN_FOLDER_NAME;
        CheckFolderAvailability();
        mActivity = Attandance_Home_Activity.this;
        handlerdb = new DatabaseHandler(context);
        handlerdbstartek=new DatabaseHandlerStartak_Sdk(context);
        db = handlerdb.getWritableDatabase();
        database= handlerdbstartek.getWritableDatabase();
        listtenantrecord = new ArrayList<>();
        listownerrecord = new ArrayList<>();
        in = getIntent();
        propertyid = in.getStringExtra("propertyid");
        devicetype=in.getStringExtra("device");
        arrayListallimages=new ArrayList<>();
        arraylistads=new ArrayList<>();

       // studentcount=Utility.getIngerSharedPreferences(context,"StudentCount");

        btn_gatattandance = (Button) findViewById(R.id.btn_getattandance);
        btn_showtenant = (Button) findViewById(R.id.btn_showtenant);
        btn_showfood = (Button) findViewById(R.id.btn_showfood);
        btn_matchattendance = (Button) findViewById(R.id.btn_matchattendance);
        btn_deleterecord= (Button)findViewById(R.id.btn_delete);
        backarraow = (RelativeLayout) findViewById(R.id.flback_button);
        btn_enrollowner= (Button)findViewById(R.id.btn_enrollowner);
        lay_enrollowner= (LinearLayout)findViewById(R.id.lay_enrollowner);
        btn_getphoto=(Button)findViewById(R.id.btn_getphoto);
        btn_savephoto=(Button)findViewById(R.id.btn_savephoto);
        btn_updatephoto=(Button)findViewById(R.id.btn_updateimg);
        btn_ads=(Button)findViewById(R.id.btn_ads);
        btn_insert=(Button)findViewById(R.id.btn_recordincrease);
        layfloor=(LinearLayout)findViewById(R.id.layfloor);
        editfloor=(EditText)findViewById(R.id.editfloor);
        btnsavefloor=(Button)findViewById(R.id.btnsavefloor);


        if(devicetype.equalsIgnoreCase("Secugen")){
            btn_enrollowner.setVisibility(View.VISIBLE);
            lay_enrollowner.setVisibility(View.VISIBLE);
        }else{
            btn_enrollowner.setVisibility(View.VISIBLE);
            lay_enrollowner.setVisibility(View.VISIBLE);
        }

        layfloor.setVisibility(View.VISIBLE);

        /*if(studentcount>50){
            layfloor.setVisibility(View.VISIBLE);
        }else{

            layfloor.setVisibility(View.VISIBLE);
        }*/

        btnsavefloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             floorvalue= Integer.parseInt(editfloor.getText().toString().trim());

                if(floorvalue>10){
                    Toast.makeText(context, "Not allow maximum 10 Floor", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                    Utility.setIntegerSharedPreference(context,"Floorvalue",floorvalue);
                }


            }
        });

        backarraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Attandance_Home_Activity.this, HostelListActivity.class);
                startActivity(ii);
            }
        });

        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("AsyncTask");
        mProgressDialog.setMessage("Please wait, we are downloading your image files...");
        mProgressDialog.setCancelable(true);

        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mMyTask.cancel(false);
            }
        });


        mProgressDialogads = new ProgressDialog(mActivity);
        mProgressDialogads.setIndeterminate(false);
        mProgressDialogads.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialogads.setTitle("AsyncTask");
        mProgressDialogads.setMessage("Please wait, we are downloading your image files...");
        mProgressDialogads.setCancelable(true);
        mProgressDialogads.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mMyTaskads.cancel(false);
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:a");
        localTime = df1.format(c.getTime());

        btn_matchattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.isConnected(Attandance_Home_Activity.this)) {
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_gatattandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.isConnected(Attandance_Home_Activity.this)) {
                    Intent ii = new Intent(Attandance_Home_Activity.this, Tenant_Records_FirstActivity.class);
                    ii.putExtra("propertyid", propertyid);
                    ii.putExtra("device",devicetype);
                    startActivity(ii);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_showtenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(devicetype.equalsIgnoreCase("Secugen")) {


                        if(floorvalue==0){

                            Toast.makeText(context, "Please Enter Number of Floor", Toast.LENGTH_SHORT).show();
                        }else{

                            Intent ii = new Intent(Attandance_Home_Activity.this, Tenant_Details_Activity.class);
                            ii.putExtra("device",devicetype);
                            ii.putExtra("propertyid", propertyid);
                            startActivity(ii);
                        }


                }else{

                    if(floorvalue==0){

                        Toast.makeText(context, "Please Enter Number of Floor", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent ii = new Intent(Attandance_Home_Activity.this, Get_Attendance_Startek.class);
                        ii.putExtra("device",devicetype);
                        ii.putExtra("propertyid", propertyid);
                        startActivity(ii);
                    }

                }
            }
        });


        btn_showfood.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {

                if(devicetype.equalsIgnoreCase("Secugen")) {
                    if (NetworkConnection.isConnected(Attandance_Home_Activity.this)) {
                        new GetenrollTenantRecord().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (NetworkConnection.isConnected(Attandance_Home_Activity.this)) {
                        new GetenrollTenantRecordStartek().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btn_deleterecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(Attandance_Home_Activity.this,Delete_Attendance_TenantList_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device",devicetype);
                startActivity(ii);
                finish();

            }
        });


        btn_updatephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(Attandance_Home_Activity.this,Update_Attendance_TenantList_Photos_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device",devicetype);
                startActivity(ii);
                finish();

            }
        });

        btn_enrollowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(Attandance_Home_Activity.this,Finger_Selction_Owner_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device",devicetype);
                ii.putExtra("key","Selection");
                startActivity(ii);
                finish();

            }
        });


        btn_getphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(devicetype.equalsIgnoreCase("Secugen")){
                    String querydelete = "delete from tenant_photo where propertyid='" + propertyid+ "' ";
                    db.execSQL(querydelete);
                    gettenantImagesSecugen();
                }else {
                    String querydelete = "delete from tenant_photo where propertyid='" + propertyid + "' ";
                    database.execSQL(querydelete);
                    gettenantImages();
                }
            }
        });


        btn_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String querydelete = "delete from krooms_ads";
                database.execSQL(querydelete);
                getImageads();

            }
        });


        btn_savephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void gettenantImagesSecugen(){

        final ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action","gettenantPhotosSecugen");
            params.put("property_id",propertyid);
            String url =  WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataSecugen(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                 }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void getResponseDataSecugen(String result) {
        try {
            String status,message;
            String[] urls ;
            String[] id;

            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                urls=new String[jsonarray.length()];
                id=new String[jsonarray.length()];
                for (int i = 0; i <jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String url1= jsonobj.getString("tenant_photo");
                    String property_id= jsonobj.getString("property_id");
                    String tenant_id= jsonobj.getString("id");
                    String url="http://www.krooms.in/hostel_management/"+url1;
                    UserModel userModel=new UserModel();
                    userModel.setImageUrl(url);
                    userModel.setPropertyid(property_id);
                    userModel.setUserid(tenant_id);
                    arrayListallimages.add(userModel);
                    urls[i]=url;
                    id[i]=tenant_id;
                }

                task = new GetXMLTask(Attandance_Home_Activity.this,id,propertyid);
                task.execute(urls);

                String urll= "http://www.krooms.in/images/test.jpeg";
                if(arrayListallimages.size()>1) {
                    UserModel userModel1 = new UserModel();
                    userModel1.setImageUrl(urll);
                    userModel1.setPropertyid("a10");
                    userModel1.setUserid("a10");
                    arrayListallimages.add(userModel1);
                }
                Toast.makeText(Attandance_Home_Activity.this,message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Attandance_Home_Activity.this,message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gettenantImages(){

        final ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action","gettenantPhotos");
            params.put("property_id",propertyid);
            String url =  WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseData(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void getResponseData(String result) {
        try {
            String status,message;
            String[] urls;
            String[] id;

            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                urls=new String[jsonarray.length()];
                id=new String[jsonarray.length()];
                for (int i = 0; i <jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String url1= jsonobj.getString("tenant_photo");
                    String property_id= jsonobj.getString("property_id");
                    String tenant_id= jsonobj.getString("id");
                    String url="http://www.krooms.in/hostel_management/"+url1;
                    UserModel userModel=new UserModel();
                    userModel.setImageUrl(url);
                    userModel.setPropertyid(property_id);
                    userModel.setUserid(tenant_id);
                    arrayListallimages.add(userModel);
                    urls[i]=url;
                    id[i]=tenant_id;
                }

                task = new GetXMLTask(Attandance_Home_Activity.this,id,propertyid);
                task.execute(urls);

                String urll= "http://www.krooms.in/images/test.jpeg";
                if(arrayListallimages.size()>1) {
                    UserModel userModel1 = new UserModel();
                    userModel1.setImageUrl(urll);
                    userModel1.setPropertyid("a10");
                    userModel1.setUserid("a10");
                    arrayListallimages.add(userModel1);
                }
                Toast.makeText(Attandance_Home_Activity.this,message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Attandance_Home_Activity.this,message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent ii = new Intent(Attandance_Home_Activity.this, HostelListActivity.class);
                startActivity(ii);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //this class used to show tenant list
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class GetenrollTenantRecord extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action","showatalltenantrecordsfinger"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
            url = WebUrls.MAIN_ATTENDACE_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            return result;

        }

        @TargetApi(Build.VERSION_CODES.CUPCAKE)
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
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            EnrollTenantModel model = new EnrollTenantModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setTenantid(jsonObject.getString("tenant_id"));
                            model.setPropertyid(jsonObject.getString("property_id"));
                            model.setTenantname(jsonObject.getString("tenant_name"));
                            model.setThumbimage(jsonObject.getString("thumb_image"));
                            model.setThumbindex(jsonObject.getString("thumb_index"));
                            model.setRoomno(jsonObject.getString("room_no"));
                            listtenantrecord.add(model);
                        }
                        insertIntoLocaldb(listtenantrecord);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertIntoLocaldb(ArrayList<EnrollTenantModel> listtenantrecord) {

        String tid, tname, propertyidnew, thumbimg, thumbindex, indeximg, indexfingerindex, middleimg, middleindex, ringimg, ringindex, littleimg, littleindex,rightthumb,rightthumbindex,room_no;
        if (listtenantrecord.size() > 0) {

            for (int i = 0; i < listtenantrecord.size(); i++) {

                tid = listtenantrecord.get(i).getTenantid();
                propertyidnew = listtenantrecord.get(i).getPropertyid();
                tname = listtenantrecord.get(i).getTenantname();
                thumbimg = listtenantrecord.get(i).getThumbimage();
                thumbindex = listtenantrecord.get(i).getThumbindex();
                room_no=listtenantrecord.get(i).getRoomno();
                try {
                    String query1 = "select * from persons where userid='" +tid +"' and thumbindex='"+thumbindex+ "'";
                    c = db.rawQuery(query1, null);
                    if (c.getCount() != 0) {
                        while (c.moveToNext()) {
                            String duserid = c.getString(0);
                            String dpropertyid = c.getString(2);
                            String dfingerindex=c.getString(4);
                            String query = "UPDATE persons  set thumbfinger='" +thumbimg+ "',roomno='" +room_no+ "',name='" +tname+ "' where userid='" +duserid+ "' and propertyid='"+dpropertyid+"' and thumbindex='"+dfingerindex+"';";
                            db.execSQL(query);
                        }
                    }else{
                        String query = "INSERT INTO persons (userid,name,propertyid,thumbfinger,thumbindex,roomno) VALUES('" +tid+ "','" +tname+ "','" +propertyidnew+ "','" +thumbimg+ "','" +thumbindex+ "','"+room_no+"')";
                        db.execSQL(query);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(c!= null)
                        c.close();
                }
            }
        }

        new GetenrollOwnerRecordSecugen().execute();
        Toast.makeText(Attandance_Home_Activity.this, "Successfully Save Record!", Toast.LENGTH_SHORT).show();
    }

    //this class used to show tenant list
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class GetenrollTenantRecordStartek extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);
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
            listNameValuePairs.add(new BasicNameValuePair("action","showatalltenantrecordsfingerstartek"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
            url = WebUrls.MAIN_ATTENDACE_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
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
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EnrollTenantModel model = new EnrollTenantModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setTenantid(jsonObject.getString("tenant_id"));
                            model.setPropertyid(jsonObject.getString("property_id"));
                            model.setTenantname(jsonObject.getString("tenant_name"));
                            model.setThumbimage(jsonObject.getString("thumb_image"));
                            model.setThumbindex(jsonObject.getString("thumb_index"));
                            model.setRoomno(jsonObject.getString("room_no"));
                            listtenantrecord.add(model);
                        }
                        insertIntoLocaldbStartek(listtenantrecord);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void insertIntoLocaldbStartek(ArrayList<EnrollTenantModel> listtenantrecord) {

        String tid, tname, propertyidnew, thumbimg, thumbindex, indeximg, indexfingerindex, middleimg, middleindex, ringimg, ringindex, littleimg, littleindex,rightthumb,rightthumbindex,room_no;
        if (listtenantrecord.size() > 0) {

            for (int i = 0; i < listtenantrecord.size(); i++) {

                tid = listtenantrecord.get(i).getTenantid();
                propertyidnew = listtenantrecord.get(i).getPropertyid();
                tname = listtenantrecord.get(i).getTenantname();
                thumbimg = listtenantrecord.get(i).getThumbimage();
                thumbindex = listtenantrecord.get(i).getThumbindex();
                room_no=listtenantrecord.get(i).getRoomno();
                try {
                    String query1 = "select * from persons where userid='" +tid +"' and thumbindex='"+thumbindex+ "'";
                    c = database.rawQuery(query1, null);
                    if (c.getCount() != 0) {
                        while (c.moveToNext()) {
                            String duserid = c.getString(0);
                            String dpropertyid = c.getString(2);
                            String dfingerindex=c.getString(4);
                            String query = "UPDATE persons  set thumbfinger='" +thumbimg+ "',roomno='" +room_no+ "',name='" +tname+ "' where userid='" +duserid+ "' and propertyid='"+dpropertyid+"' and thumbindex='"+dfingerindex+"';";
                            database.execSQL(query);
                        }
                    }else{
                        String query = "INSERT INTO persons (userid,name,propertyid,thumbfinger,thumbindex,roomno) VALUES('" +tid+ "','" +tname+ "','" +propertyidnew+ "','" +thumbimg+ "','" +thumbindex+ "','"+room_no+"')";
                        database.execSQL(query);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                     if(c!= null)
                        c.close();
                }

            }
        }
        Toast.makeText(Attandance_Home_Activity.this, "Successfully Save Record!", Toast.LENGTH_SHORT).show();

        new   GetenrollOwnerRecordStartek().execute();
    }
    //get owner enroll finger
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class GetenrollOwnerRecordStartek extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);

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
            listownerrecord.clear();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action","showownerfingerstartek"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
            url = WebUrls.MAIN_ATTENDACE_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
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
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EnrollTenantModel model = new EnrollTenantModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setPropertyid(jsonObject.getString("property_id"));
                            model.setThumbimage(jsonObject.getString("thumb_image"));
                            model.setThumbindex(jsonObject.getString("thumb_index"));
                            listownerrecord.add(model);
                        }
                        insertIntoLocaldbOwnerStartek(listownerrecord);

                    } else {
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertIntoLocaldbOwnerStartek(ArrayList<EnrollTenantModel> listtenantrecord) {

        String tid, tname, propertyidnew, thumbimg, thumbindex, indeximg, indexfingerindex, middleimg, middleindex, ringimg, ringindex, littleimg, littleindex,rightthumb,rightthumbindex,room_no;
        if (listtenantrecord.size() > 0) {

            for (int i = 0; i < listtenantrecord.size(); i++) {

                tid = listtenantrecord.get(i).getTenantid();
                propertyidnew = listtenantrecord.get(i).getPropertyid();
                tname = listtenantrecord.get(i).getTenantname();
                thumbimg = listtenantrecord.get(i).getThumbimage();
                thumbindex = listtenantrecord.get(i).getThumbindex();
                room_no=listtenantrecord.get(i).getRoomno();
                try {
                    String query1 = "select * from ownerfingerprint where propertyid='" +propertyid+"' and thumbindex='"+thumbindex+ "'";
                    c = database.rawQuery(query1, null);
                    if (c.getCount() != 0) {
                        while (c.moveToNext()) {
                            String query = "UPDATE ownerfingerprint  set thumbfinger='" +thumbimg+ "' where  propertyid='"+propertyid+"' and thumbindex='"+thumbindex+"';";
                            database.execSQL(query);
                        }
                    }else{
                        String query = "INSERT INTO ownerfingerprint (propertyid,thumbfinger,thumbindex) VALUES('" +propertyidnew+ "','" +thumbimg+ "','" +thumbindex+ "')";
                        database.execSQL(query);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                     if(c!= null)
                        c.close();
                }
            }
        }
        Toast.makeText(Attandance_Home_Activity.this, "Successfully Save Record!", Toast.LENGTH_SHORT).show();
    }

    //get owner finger print detail secugen
    public class GetenrollOwnerRecordSecugen extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);
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
            listownerrecord.clear();
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action","showownerfingersecugen"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",propertyid));
            url = WebUrls.MAIN_ATTENDACE_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
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
                        JSONArray jsonArray = obj.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EnrollTenantModel model = new EnrollTenantModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            model.setPropertyid(jsonObject.getString("property_id"));
                            model.setThumbimage(jsonObject.getString("thumb_image"));
                            model.setThumbindex(jsonObject.getString("thumb_index"));
                            listownerrecord.add(model);
                        }
                        insertIntoLocaldbOwnerSecugen(listownerrecord);

                    } else {
                       }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertIntoLocaldbOwnerSecugen(ArrayList<EnrollTenantModel> listtenantrecord) {

        String tid, tname, propertyidnew, thumbimg, thumbindex, indeximg, indexfingerindex, middleimg, middleindex, ringimg, ringindex, littleimg, littleindex,rightthumb,rightthumbindex,room_no;
        if (listtenantrecord.size() > 0) {

            for (int i = 0; i < listtenantrecord.size(); i++) {

                tid = listtenantrecord.get(i).getTenantid();
                propertyidnew = listtenantrecord.get(i).getPropertyid();
                tname = listtenantrecord.get(i).getTenantname();
                thumbimg = listtenantrecord.get(i).getThumbimage();
                thumbindex = listtenantrecord.get(i).getThumbindex();
                room_no=listtenantrecord.get(i).getRoomno();
                try {
                    String query1 = "select * from ownerfingerprint where propertyid='" +propertyid+"' and thumbindex='"+thumbindex+ "'";
                    c = db.rawQuery(query1, null);
                    if (c.getCount() != 0) {
                        while (c.moveToNext()) {
                            String query = "UPDATE ownerfingerprint  set thumbfinger='" +thumbimg+ "' where  propertyid='"+propertyid+"' and thumbindex='"+thumbindex+"';";
                            db.execSQL(query);
                        }
                    }else{
                        String query = "INSERT INTO ownerfingerprint (propertyid,thumbfinger,thumbindex) VALUES('" +propertyidnew+ "','" +thumbimg+ "','" +thumbindex+ "')";
                        db.execSQL(query);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                     if(c!= null)
                        c.close();
                }

            }
        }
        Toast.makeText(Attandance_Home_Activity.this, "Successfully Save Record!", Toast.LENGTH_SHORT).show();
    }
    //end
    //get attendance ads
    private void getImageads(){

        final ProgressDialog dialog = new ProgressDialog(Attandance_Home_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action","getattendanceads");
            params.put("property_id",propertyid);
            String url =  WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataads(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void getResponseDataads(String result) {
        try {
            String status,message;

            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i <jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String url1= jsonobj.getString("ads_image");
                    String property_id= jsonobj.getString("property_id");
                    String adsname= jsonobj.getString("name");
                    String adsorder= jsonobj.getString("short_order");
                    String adstype= jsonobj.getString("ads_type");
                    AttedanceadsModel userModel=new AttedanceadsModel();
                    userModel.setAdsurl(url1);
                    userModel.setProperty_id(property_id);
                    userModel.setAdstype(adstype);
                    userModel.setAds_order(adsorder);
                    userModel.setAdsname(adsname);
                    arraylistads.add(userModel);
                }

                if(arraylistads.size()!=0) {

                    for (int m = 0; m < arraylistads.size(); m++) {
                        String ur = arraylistads.get(m).getAdsurl();
                        String pid = arraylistads.get(m).getProperty_id();
                        String aname = arraylistads.get(m).getAdsname();
                        String atype = arraylistads.get(m).getAdstype();
                        String aorder= arraylistads.get(m).getAds_order();
                        URL url5 = stringToURL(ur);
                        mMyTaskads = new DownloadTaskAds(pid,aname,atype,aorder).execute(url5);

                    }
                }else{

                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Attandance_Home_Activity.this,message, Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(Attandance_Home_Activity.this,message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //downloads ads
    public class DownloadTaskAds extends AsyncTask<URL,Integer,List<Bitmap>>{
        int size=0;
        String property_id="",adsname="",adstype="",adsorder="";

        public DownloadTaskAds(String property_id, String adsname, String adstype, String adsorder) {

            this.property_id=property_id;
            this.adsname=adsname;
            this.adstype=adstype;
            this.adsorder=adsorder;
        }
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressDialogads.show();
            mProgressDialogads.setProgress(0);
        }
        // Do the task in background/non UI thread
        protected List<Bitmap> doInBackground(URL...urls){
            int count = urls.length;
            //URL url = urls[0];
            HttpURLConnection connection = null;
            List<Bitmap> bitmaps = new ArrayList<>();
            // Loop through the urls
            for(int i=0;i<count;i++){
                URL currentURL = urls[i];
                // So download the image from this url
                try{
                    // Initialize a new http url connection
                    connection = (HttpURLConnection) currentURL.openConnection();
                    // Connect the http url connection
                    connection.connect();
                    // Get the input stream from http url connection
                    InputStream inputStream = connection.getInputStream();
                    // Initialize a new BufferedInputStream from InputStream
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    // Convert BufferedInputStream to Bitmap object
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    bitmaps.add(bmp);
                    // Publish the async task progress
                    // Added 1, because index start from 0
                    publishProgress((int) (((i+1) / (float) count) * 100));
                    if(isCancelled()){
                        break;
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    // Disconnect the http url connection
                    connection.disconnect();
                }
            }
            // Return bitmap list
            return bitmaps;
        }

        // On progress update
        protected void onProgressUpdate(Integer... progress){
            // Update the progress bar
            mProgressDialogads.setProgress(progress[0]);
        }
        // On AsyncTask cancelled
        protected void onCancelled(){
            // Snackbar.make(mCLayout,"Task Cancelled.",Snackbar.LENGTH_LONG).show();
        }
        // When all async task done
        protected void onPostExecute(List<Bitmap> result){
            mProgressDialogads.dismiss();
            for(int i=0;i<result.size();i++){
                Bitmap bitmap = result.get(i);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String t1base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                if(devicetype.equalsIgnoreCase("Secugen")){
                    try {
                        String query1 = "select * from krooms_ads where propertyid='" +property_id +"' and name='" +adsname+"'";
                        c = db.rawQuery(query1, null);
                        if (c.getCount() != 0) {
                            String query = "UPDATE krooms_ads  set adimage='" +t1base64+ "',adstype='" +adstype+"',shortorder='" +adsorder+"' where propertyid='"+property_id+ "' and name='"+adsname+"';";
                            db.execSQL(query);
                        }else{
                            String query = "INSERT INTO krooms_ads(propertyid,name,adimage,adstype,shortorder,status) VALUES('" +property_id+ "','"+adsname+"','"+t1base64+"','" + adstype + "','"+adsorder+"','1')";
                            db.execSQL(query);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (c!=null)
                            c.close();
                    }
                }else {

                    try {
                        String query1 = "select * from krooms_ads where propertyid='" +property_id +"' and name='" +adsname+"'";
                        c = database.rawQuery(query1, null);
                        if (c.getCount() != 0) {
                            String query = "UPDATE krooms_ads  set adimage='" +t1base64+ "',adstype='" +adstype+"' ,name='" +adsname+"' ,shortorder='" +adsorder+"' where propertyid='"+property_id+ "' and name='" +adsname+"';";
                            database.execSQL(query);
                        }else{
                            String query = "INSERT INTO krooms_ads(propertyid,name,adimage,adstype,shortorder,status) VALUES('" +property_id+ "','"+adsname+"','"+t1base64+"','" + adstype + "','"+adsorder+"','1')";
                            database.execSQL(query);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if(c!= null)
                            c.close();
                    }
                }
            }
        }
    }


    private void CheckFolderAvailability() {
        File file = new File(GET_MAIN_FOLDER_PATH);
        if (!file.exists())  {
            file.mkdir();
        }
    }
    //new image download code 19-02-2019
    private class GetXMLTask extends AsyncTask<String, Integer, List<RowItem>> {
        ProgressDialog progressDialog;
        private Activity context;
        List<RowItem> rowItems;
        int noOfURLs;
        int countOf;
        String[] id;
        String property_id;

        public GetXMLTask(Activity context,String[] id,String property_id) {
            this.context = context;
            this.id=id;
            this.property_id=property_id;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("In progress...");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setIcon(R.drawable.k_rooms);
            progressDialog.show();
        }

        @Override
        protected List<RowItem> doInBackground(String... urls) {
            noOfURLs = urls.length;
            rowItems = new ArrayList<RowItem>();
            Bitmap map = null;
            for (String url : urls) {
                //taking 1 url at a time and downloading
                map = downloadImage(url);
                //after downloading the bitmap is added to rowitems
                rowItems.add(new RowItem(map));
                countOf++;
                if (isCancelled()==true){
                    Log.d("GettingCancelled","isCancelled");
                    break;
                }
            }
            return rowItems;
        }

        private Bitmap downloadImage(String urlString) {

            int count = 0;
            Bitmap bitmap = null;

            URL url;
            InputStream inputStream = null;
            BufferedOutputStream outputStream = null;

            try {
                url = new URL(urlString);
                URLConnection connection = url.openConnection();
                int lenghtOfFile = connection.getContentLength();

                inputStream = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();

                outputStream = new BufferedOutputStream(dataStream);

                byte data[] = new byte[512];
                long total = 0;

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    Log.d("Downloading","total "+total+" Count "+count);
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;

                byte[] bytes = dataStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bmOptions);
                bitmap = ScalingUtilities.scaleDown(bitmap, 320, true);
                FileOutputStream fOut;
                File f = new File(GET_MAIN_FOLDER_PATH, id[countOf]+".jpg");
                fOut = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                String PATH_OF_IMAGE = f.getAbsolutePath();
                if(devicetype.equalsIgnoreCase("Secugen")){

                    try {
                        String query1 = "select * from tenant_photo where tenantid='" +id[countOf] +"' and propertyid='"+property_id+ "'";
                        c = db.rawQuery(query1, null);
                        if (c.getCount() != 0) {
                            String query = "UPDATE tenant_photo  set photo='" +PATH_OF_IMAGE+ "' where  tenantid='" +id[countOf] +"' and  propertyid='"+property_id+ "';";
                            db.execSQL(query);
                        }else{
                            String query = "INSERT INTO tenant_photo(tenantid,propertyid,photo,status) VALUES('" + id[countOf] + "','"+property_id+"','" + PATH_OF_IMAGE + "','1')";
                            db.execSQL(query);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if(c!= null)
                            c.close();
                    }

                }else {

                    try {
                        String query1 = "select * from tenant_photo where tenantid='" +id[countOf] +"' and propertyid='"+property_id+ "'";
                        c =  database.rawQuery(query1, null);
                        if (c.getCount() != 0) {
                            String query = "UPDATE tenant_photo  set photo='" +PATH_OF_IMAGE+ "'  where  tenantid='" +id[countOf] +"' and propertyid='"+property_id+ "';";
                            database.execSQL(query);
                        }else{
                            String query = "INSERT INTO tenant_photo(tenantid,propertyid,photo,status) VALUES('" + id[countOf] + "','"+property_id+"','" + PATH_OF_IMAGE + "','1')";
                            database.execSQL(query);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if(c!= null)
                            c.close();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.close(inputStream);
                FileUtils.close(outputStream);
            }
            return bitmap;
        }
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
            if (rowItems != null) {
                progressDialog.setMessage("Loading " + (rowItems.size() + 1) + "/" + noOfURLs);
                Log.d("DownloadingFile", "Loading " + (rowItems.size() + 1) + "/" + noOfURLs);
            }
        }
        @Override
        protected void onPostExecute(List<RowItem> rowItems) {
            progressDialog.dismiss();
        }
    }
}
