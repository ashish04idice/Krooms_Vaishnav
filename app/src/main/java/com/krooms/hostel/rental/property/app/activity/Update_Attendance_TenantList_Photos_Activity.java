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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.ScalingUtilities;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
import com.krooms.hostel.rental.property.app.modal.UserModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 7/1/2017.
 */
public class Update_Attendance_TenantList_Photos_Activity extends AppCompatActivity {

    ListView listView;
    Delete_Tenant_Attendance_Adapter adapter;
    ArrayList<TenantModal> userlist;
    Context context;
    RelativeLayout back;
    Intent in;
    String propertyid;
    Calendar calnder;
    String formattedDate = "";
    private SQLiteDatabase db, database;
    private Cursor c = null;
    DatabaseHandler handlerdb;
    DatabaseHandlerStartak_Sdk databaseHandlerStartak_sdk;
    public static ArrayList<TenantModal> attendance_list;
    TextView textView;
    String devicetype = "";
    public static ArrayList<UserModel> arrayListallimages;
    private AsyncTask mMyTask;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    Animation rotation;
    public static int apistatus = 0;
    public static String GET_MAIN_FOLDER_PATH;
    private static String FILESAVE_BASEURL;
    private final String MAIN_FOLDER_NAME = "Krooms Attendance Photo";
    String PATH_OF_IMAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.night_attendance_layout);
        in = getIntent();
        FILESAVE_BASEURL = Environment.getExternalStorageDirectory().getAbsolutePath();
        GET_MAIN_FOLDER_PATH = FILESAVE_BASEURL + File.separator + MAIN_FOLDER_NAME;
        CheckFolderAvailability();
        apistatus = 0;
        propertyid = in.getStringExtra("propertyid");
        devicetype = in.getStringExtra("device");
        context = this;
        mActivity = Update_Attendance_TenantList_Photos_Activity.this;
        handlerdb = new DatabaseHandler(context);
        db = handlerdb.getWritableDatabase();
        databaseHandlerStartak_sdk = new DatabaseHandlerStartak_Sdk(context);
        database = databaseHandlerStartak_sdk.getWritableDatabase();
        attendance_list = new ArrayList<>();
        arrayListallimages = new ArrayList<>();
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        listView = (ListView) findViewById(R.id.tenantlist);
        back = (RelativeLayout) findViewById(R.id.flback_button);
        textView = (TextView) findViewById(R.id.textheader);
        textView.setText("Tenant List");
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(calnder.getTime());
        // Initialize the progress dialog
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIndeterminate(false);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Progress dialog title
        mProgressDialog.setTitle("AsyncTask");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are downloading your image files...");
        mProgressDialog.setCancelable(true);
        // Set a progress dialog dismiss listener
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Cancel the AsyncTask
                mMyTask.cancel(false);
            }
        });

        if (devicetype.equalsIgnoreCase("Secugen")) {
            deleteRecord();
        } else {
            deleteRecordstartek();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Update_Attendance_TenantList_Photos_Activity.this, Attandance_Home_Activity.class);
                intent.putExtra("propertyid", propertyid);
                intent.putExtra("device", devicetype);
                startActivity(intent);
            }
        });


    }

    private void deleteRecord() {

        attendance_list.clear();
        String satatus = "";
        Cursor cc1 = null;
        String query = "select distinct t1.userid ,t1.name ,t1.propertyid ,t1.roomno,t3.photo " +
                " from persons t1 " +
                "left join (select * from tenant_photo ) t3 on t1.userid=t3.tenantid and t1.propertyid=" +
                "t3.propertyid where t1.propertyid='" + propertyid + "'";


        cc1 = db.rawQuery(query, null);
        if (cc1.getCount() != 0) {
            try {
                int i = 0;
                if (cc1.moveToFirst()) {
                    do {
                        int j = i + 1;
                        TenantModal recordmodel = new TenantModal();
                        recordmodel.setTenant_id(cc1.getString(0));
                        recordmodel.setPropertyid(cc1.getString(2));
                        recordmodel.setTenant_name(cc1.getString(1));
                        recordmodel.setTenant_roomno(cc1.getString(3));
                        recordmodel.setTenant_img(cc1.getString(4));
                        attendance_list.add(recordmodel);
                        i++;
                    } while (cc1.moveToNext());
                }
                adapter = new Delete_Tenant_Attendance_Adapter(Update_Attendance_TenantList_Photos_Activity.this, attendance_list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (cc1 != null && !cc1.isClosed()) {
                    cc1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cc1 != null)
                    cc1.close();
            }

        } else {
            Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRecordstartek() {
        attendance_list.clear();
        String satatus = "";
        Cursor cc1 = null;
        String query = "select distinct t1.userid ,t1.name ,t1.propertyid ,t1.roomno,t3.photo " +
                " from persons t1 " +
                "left join (select * from tenant_photo ) t3 on t1.userid=t3.tenantid and t1.propertyid=" +
                "t3.propertyid where t1.propertyid='" + propertyid + "'";

        cc1 = database.rawQuery(query, null);
        if (cc1.getCount() != 0) {
            try {
                int i = 0;
                if (cc1.moveToFirst()) {
                    do {
                        int j = i + 1;
                        TenantModal recordmodel = new TenantModal();
                        recordmodel.setTenant_id(cc1.getString(0));
                        recordmodel.setPropertyid(cc1.getString(2));
                        recordmodel.setTenant_name(cc1.getString(1));
                        recordmodel.setTenant_roomno(cc1.getString(3));
                        recordmodel.setTenant_img(cc1.getString(4));
                        // recordmodel.setNumber(String.valueOf(j));
                        attendance_list.add(recordmodel);
                        i++;
                    } while (cc1.moveToNext());
                }
                adapter = new Delete_Tenant_Attendance_Adapter(Update_Attendance_TenantList_Photos_Activity.this, attendance_list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (cc1 != null && !cc1.isClosed()) {
                    cc1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cc1 != null)
                    cc1.close();
            }

        } else {
            Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ii = new Intent(Update_Attendance_TenantList_Photos_Activity.this, Attandance_Home_Activity.class);
            ii.putExtra("propertyid", propertyid);
            ii.putExtra("device", devicetype);
            startActivity(ii);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //adapter
    public class Delete_Tenant_Attendance_Adapter extends BaseAdapter {

        private Context context;
        private Activity activity;
        private ArrayList<TenantModal> _listArray;

        public Delete_Tenant_Attendance_Adapter(Activity context, ArrayList<TenantModal> objects) {
            this.context = context;
            this.activity = context;
            this._listArray = objects;
        }
        @Override
        public int getCount() {
            return _listArray.size();
        }

        @Override
        public Object getItem(int position) {
            return _listArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            ImageView userImage;
            TextView userName, Roomno, imgrotate;
            ImageView Txtnum;
            LinearLayout laydelete;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.update_attendance_tenant_image, null);
                Delete_Tenant_Attendance_Adapter.ViewHolder _viewHolder = new Delete_Tenant_Attendance_Adapter.ViewHolder();
                _viewHolder.userName = (TextView) convertView.findViewById(R.id.property_user_name);
                _viewHolder.Roomno = (TextView) convertView.findViewById(R.id.property_user_book_room);
                _viewHolder.Txtnum = (ImageView) convertView.findViewById(R.id.txtphoto);
                _viewHolder.laydelete = (LinearLayout) convertView.findViewById(R.id.laydelete);
                _viewHolder.imgrotate = (TextView) convertView.findViewById(R.id.imgrotate);
                convertView.setTag(_viewHolder);
            }
            final Delete_Tenant_Attendance_Adapter.ViewHolder _viewHolder = (Delete_Tenant_Attendance_Adapter.ViewHolder) convertView.getTag();
            _viewHolder.userName.setText(_listArray.get(position).getTenant_name());
            _viewHolder.Roomno.setText("Room no:- " + _listArray.get(position).getTenant_roomno());

            if (_listArray.get(position).getTenant_img() == null || _listArray.get(position).getTenant_img().isEmpty() || _listArray.get(position).getTenant_img().equals("null") || _listArray.get(position).getTenant_img().equals("0")) {

                _viewHolder.Txtnum.setImageResource(R.drawable.user_xl);

            } else {
                File imgFile = new File(_listArray.get(position).getTenant_img());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    _viewHolder.Txtnum.setImageBitmap(myBitmap);
                }

            }


            _viewHolder.laydelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenantid = _listArray.get(position).getTenant_id();
                    String propertyid = _listArray.get(position).getPropertyid();
                    if (NetworkConnection.isConnected(Update_Attendance_TenantList_Photos_Activity.this)) {

                        if (devicetype.equalsIgnoreCase("Secugen")) {
                            gettenantImagesSecugen(tenantid, propertyid);
                        } else {
                            gettenantImages(tenantid, propertyid);
                        }
                    } else {
                        Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return convertView;
        }

    }


    private void updateRecord(String tenantid, String propertyid) {

        final ProgressDialog dialog = new ProgressDialog(Update_Attendance_TenantList_Photos_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "attendanceexittenant");
            params.put("property_id", propertyid);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_ATTENDACE_URL;
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

    public void getResponseData(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(context, "Exit Successfully", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(context, "Not Exit", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //
    private void gettenantImagesSecugen(String tenantid, String propertyid) {

        final ProgressDialog dialog = new ProgressDialog(Update_Attendance_TenantList_Photos_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "gettenantPhotosbyidSecugen");
            params.put("property_id", propertyid);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataImageSecugen(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    apistatus = 1;
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
    public void getResponseDataImageSecugen(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String url1 = jsonobj.getString("tenant_photo");
                    String property_id = jsonobj.getString("property_id");
                    String tenant_id = jsonobj.getString("id");
                    String url = "http://www.krooms.girlshostels.in/hostel_management/" + url1;
                    UserModel userModel = new UserModel();
                    userModel.setImageUrl(url);
                    userModel.setPropertyid(property_id);
                    userModel.setUserid(tenant_id);
                    arrayListallimages.add(userModel);
                }

                if (arrayListallimages.size() != 0) {

                    for (int m = 0; m < arrayListallimages.size(); m++) {

                        String ur = arrayListallimages.get(m).getImageUrl();
                        String pid = arrayListallimages.get(m).getPropertyid();
                        String tid = arrayListallimages.get(m).getUserid();

                        URL url5 = stringToURL(ur);
                        mMyTask = new DownloadTask(pid, tid).execute(url5);

                    }
                } else {

                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Update_Attendance_TenantList_Photos_Activity.this, message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Update_Attendance_TenantList_Photos_Activity.this, message, Toast.LENGTH_SHORT).show();
                apistatus = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            apistatus = 1;
        }
    }


    //get image startek
    private void gettenantImages(String tenantid, String propertyid) {

        final ProgressDialog dialog = new ProgressDialog(Update_Attendance_TenantList_Photos_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action", "gettenantPhotosbyid");
            params.put("property_id", propertyid);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataImage(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    apistatus = 1;
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
    public void getResponseDataImage(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    String url1 = jsonobj.getString("tenant_photo");
                    String property_id = jsonobj.getString("property_id");
                    String tenant_id = jsonobj.getString("id");
                    String url = "http://www.krooms.girlshostels.in/hostel_management/" + url1;
                    UserModel userModel = new UserModel();
                    userModel.setImageUrl(url);
                    userModel.setPropertyid(property_id);
                    userModel.setUserid(tenant_id);
                    arrayListallimages.add(userModel);
                }

                if (arrayListallimages.size() != 0) {

                    for (int m = 0; m < arrayListallimages.size(); m++) {

                        String ur = arrayListallimages.get(m).getImageUrl();
                        String pid = arrayListallimages.get(m).getPropertyid();
                        String tid = arrayListallimages.get(m).getUserid();

                        URL url5 = stringToURL(ur);
                        mMyTask = new DownloadTask(pid, tid).execute(url5);

                    }
                } else {

                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Update_Attendance_TenantList_Photos_Activity.this, message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Update_Attendance_TenantList_Photos_Activity.this, message, Toast.LENGTH_SHORT).show();
                apistatus = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            apistatus = 1;
        }
    }

    //

    // Custom method to convert string to url
    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public class DownloadTask extends AsyncTask<URL, Integer, List<Bitmap>> {


        int size = 0;
        String pid = "", tid = "";

        public DownloadTask(String pid, String tid) {

            this.pid = pid;
            this.tid = tid;
        }


        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            mProgressDialog.show();
            mProgressDialog.setProgress(0);
        }

        // Do the task in background/non UI thread
        protected List<Bitmap> doInBackground(URL... urls) {
            int count = urls.length;
            HttpURLConnection connection = null;
            List<Bitmap> bitmaps = new ArrayList<>();

            Log.d("count", "" + count);


            // Loop through the urls
            for (int i = 0; i < count; i++) {
                URL currentURL = urls[i];
                // So download the image from this url
                try {
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

                    // Add the bitmap to list
                    bitmaps.add(bmp);

                    // Publish the async task progress
                    // Added 1, because index start from 0
                    publishProgress((int) (((i + 1) / (float) count) * 100));
                    if (isCancelled()) {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Disconnect the http url connection
                    connection.disconnect();
                }
            }
            // Return bitmap list
            return bitmaps;
        }

        // On progress update
        protected void onProgressUpdate(Integer... progress) {
            // Update the progress bar
            mProgressDialog.setProgress(progress[0]);
        }

        // On AsyncTask cancelled
        protected void onCancelled() {
            // Snackbar.make(mCLayout,"Task Cancelled.",Snackbar.LENGTH_LONG).show();
        }

        // When all async task done
        protected void onPostExecute(List<Bitmap> result) {
            // Hide the progress dialog
            // Remove all views from linear layout
            mProgressDialog.dismiss();
            apistatus = 1;
            // Loop through the bitmap list
            // counter++;
            //Log.d("result.size",""+result.size());
            // Toast.makeText(mContext, "result.size"+result.size(), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < result.size(); i++) {
                Bitmap bitmap = result.get(i);
                // Save the bitmap to internal storage
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String t1base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                /*new code for bitmap to file save*/
                bitmap = ScalingUtilities.scaleDown(bitmap, 320, true);
                FileOutputStream fOut;
                try {
                    File f = new File(GET_MAIN_FOLDER_PATH, tid + ".jpg");
                    fOut = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    bitmap.recycle();
                    PATH_OF_IMAGE = f.getAbsolutePath();
                    Log.d("loadbitmap", PATH_OF_IMAGE + "");
                    //Capture.setText(getResources().getString(R.string.login_button_name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (devicetype.equalsIgnoreCase("Secugen")) {

                    try {
                        String query1 = "select * from tenant_photo where tenantid='" + tid + "' and propertyid='" + pid + "'";
                        c = db.rawQuery(query1, null);
                        if (c.getCount() != 0) {
                            String query = "UPDATE tenant_photo  set photo='" + PATH_OF_IMAGE + "' where tenantid='" + tid + "' and propertyid='" + pid + "';";
                            db.execSQL(query);
                        } else {
                            String query = "INSERT INTO tenant_photo(tenantid,propertyid,photo,status) VALUES('" + tid + "','" + pid + "','" + PATH_OF_IMAGE + "','1')";
                            db.execSQL(query);
                        }

                        adapter.notifyDataSetChanged();
                        deleteRecord();
                        Intent ii = new Intent(Update_Attendance_TenantList_Photos_Activity.this, Update_Attendance_TenantList_Photos_Activity.class);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // this gets called even if there is an exception somewhere above
                        if (c != null)
                            c.close();
                    }


                } else {

                    try {
                        String query1 = "select * from tenant_photo where tenantid='" + tid + "' and propertyid='" + pid + "'";
                        c = database.rawQuery(query1, null);
                        if (c.getCount() != 0) {
                            String query = "UPDATE tenant_photo  set photo='" + PATH_OF_IMAGE + "' where tenantid='" + tid + "' and propertyid='" + pid + "';";
                            database.execSQL(query);
                        } else {
                            String query = "INSERT INTO tenant_photo(tenantid,propertyid,photo,status) VALUES('" + tid + "','" + pid + "','" + PATH_OF_IMAGE + "','1')";
                            database.execSQL(query);
                        }
                        adapter.notifyDataSetChanged();
                        deleteRecordstartek();
                        Intent ii = new Intent(Update_Attendance_TenantList_Photos_Activity.this, Update_Attendance_TenantList_Photos_Activity.class);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // this gets called even if there is an exception somewhere above
                        if (c != null)
                            c.close();
                    }
                }
            }
        }
    }

    private void CheckFolderAvailability() {
        File file = new File(GET_MAIN_FOLDER_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
