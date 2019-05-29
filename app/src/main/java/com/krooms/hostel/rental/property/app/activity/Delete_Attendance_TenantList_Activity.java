package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 7/1/2017.
 */
public class Delete_Attendance_TenantList_Activity extends AppCompatActivity {

    ListView listView;
    Delete_Tenant_Attendance_Adapter adapter;
    ArrayList<TenantModal> userlist;
    Context context;
    RelativeLayout back;
    Intent in;
    String propertyid;
    Calendar calnder;
    String formattedDate="";
    private SQLiteDatabase db,database;
    DatabaseHandler handlerdb;
    DatabaseHandlerStartak_Sdk databaseHandlerStartak_sdk;
    public static ArrayList<TenantModal> attendance_list;
    TextView textView;
    String devicetype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.night_attendance_layout);
        in=getIntent();
        propertyid=in.getStringExtra("propertyid");
        devicetype=in.getStringExtra("device");
        context=this;
        handlerdb = new DatabaseHandler(context);
        db = handlerdb.getWritableDatabase();
        databaseHandlerStartak_sdk=new DatabaseHandlerStartak_Sdk(context);
        database=databaseHandlerStartak_sdk.getWritableDatabase();
        attendance_list = new ArrayList<>();
        listView=(ListView)findViewById(R.id.tenantlist);
        back=(RelativeLayout)findViewById(R.id.flback_button);
        textView=(TextView)findViewById(R.id.textheader);
        textView.setText("Tenant List");
        calnder = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(calnder.getTime());

        if(devicetype.equalsIgnoreCase("Secugen")){
            deleteRecord();
        }else {
            deleteRecordstartek();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Delete_Attendance_TenantList_Activity.this,Attandance_Home_Activity.class);
                intent.putExtra("propertyid",propertyid);
                intent.putExtra("device",devicetype);
                startActivity(intent);
            }
        });


    }

    private void deleteRecord() {

            attendance_list.clear();
            String satatus="";
            Cursor cc1=null;
            String query="select distinct userid,name,propertyid,roomno from persons where propertyid='"+propertyid+"' ";
            cc1 = db.rawQuery(query, null);
            if (cc1.getCount()!=0) {
                try {
                    int i=0;
                        if (cc1.moveToFirst()) {
                            do {
                                int j=i+1;
                                TenantModal recordmodel = new TenantModal();
                                recordmodel.setTenant_id(cc1.getString(0));
                                recordmodel.setPropertyid(cc1.getString(2));
                                recordmodel.setTenant_name(cc1.getString(1));
                                recordmodel.setTenant_roomno(cc1.getString(3));
                                recordmodel.setNumber(String.valueOf(j));
                                attendance_list.add(recordmodel);
                                i++;
                            } while (cc1.moveToNext());
                        }
                    adapter = new Delete_Tenant_Attendance_Adapter(Delete_Attendance_TenantList_Activity.this, attendance_list);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (cc1 != null && !cc1.isClosed()) {
                        cc1.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                      if(cc1!= null)
                        cc1.close();
                }

            }else {
                 Toast.makeText(context, "No Record Found",Toast.LENGTH_SHORT).show();
            }
    }


    private void deleteRecordstartek() {

        attendance_list.clear();
        String satatus="";
        Cursor cc1=null;
        String query="select distinct userid,name,propertyid,roomno from persons where propertyid='"+propertyid+"' ";
        cc1 = database.rawQuery(query, null);
        if (cc1.getCount()!=0) {
            try {
                int i=0;
                if (cc1.moveToFirst()) {
                    do {
                        int j=i+1;
                        TenantModal recordmodel = new TenantModal();
                        recordmodel.setTenant_id(cc1.getString(0));
                        recordmodel.setPropertyid(cc1.getString(2));
                        recordmodel.setTenant_name(cc1.getString(1));
                        recordmodel.setTenant_roomno(cc1.getString(3));
                        recordmodel.setNumber(String.valueOf(j));
                        attendance_list.add(recordmodel);
                        i++;
                    } while (cc1.moveToNext());
                }
                adapter = new Delete_Tenant_Attendance_Adapter(Delete_Attendance_TenantList_Activity.this, attendance_list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (cc1 != null && !cc1.isClosed()) {
                    cc1.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                // this gets called even if there is an exception somewhere above
                if(cc1!= null)
                    cc1.close();
            }
        }else {
            Toast.makeText(context, "No Record Found",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent ii=new Intent(Delete_Attendance_TenantList_Activity.this,Attandance_Home_Activity.class);
            ii.putExtra("propertyid",propertyid);
            ii.putExtra("device",devicetype);
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
            TextView userName,Roomno,Txtnum;
            LinearLayout laydelete;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(null == convertView) {
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.delete_tenant_attendance_item,null);
                 Delete_Tenant_Attendance_Adapter.ViewHolder _viewHolder = new Delete_Tenant_Attendance_Adapter.ViewHolder();
                _viewHolder.userName = (TextView) convertView.findViewById(R.id.property_user_name);
                _viewHolder.Roomno = (TextView) convertView.findViewById(R.id.property_user_book_room);
                _viewHolder.Txtnum = (TextView) convertView.findViewById(R.id.txtnum);
                _viewHolder.laydelete=(LinearLayout)convertView.findViewById(R.id.laydelete);
                convertView.setTag(_viewHolder);
            }
             Delete_Tenant_Attendance_Adapter.ViewHolder _viewHolder = (Delete_Tenant_Attendance_Adapter.ViewHolder) convertView.getTag();
            _viewHolder.userName.setText(_listArray.get(position).getTenant_name());
            _viewHolder.Roomno.setText("Room no:- " + _listArray.get(position).getTenant_roomno());
            _viewHolder.Txtnum.setText( _listArray.get(position).getNumber());

            _viewHolder.laydelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenantid=  _listArray.get(position).getTenant_id();
                    String propertyid=  _listArray.get(position).getPropertyid();
                    if (NetworkConnection.isConnected(Delete_Attendance_TenantList_Activity.this)) {

                        if(devicetype.equalsIgnoreCase("Secugen")){
                            String querydelete = "delete from persons where userid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            db.execSQL(querydelete);
                            //krooms_attendance
                            String querydeleteatt = "delete from krooms_attendance where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            db.execSQL(querydeleteatt);
                            //krooms_food
                            String querydeletefood = "delete from krooms_foodattendance where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            db.execSQL(querydeletefood);
                            //krooms_night
                            String querydeletenight = "delete from krooms_nightattendance where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            db.execSQL(querydeletenight);

                            String querydeletephoto = "delete from tenant_photo where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            db.execSQL(querydeletephoto);

                            adapter.notifyDataSetChanged();

                            updateRecord(tenantid,propertyid);
                            deleteRecord();
                        }else {


                            String querydelete = "delete from persons where userid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            database.execSQL(querydelete);
                            //krooms_attendance
                            String querydeleteatt = "delete from krooms_attendance where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            database.execSQL(querydeleteatt);
                            //krooms_food
                            String querydeletefood = "delete from krooms_foodattendance where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            database.execSQL(querydeletefood);
                            //krooms_night
                            String querydeletenight = "delete from krooms_nightattendance where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            database.execSQL(querydeletenight);

                            String querydeletephoto = "delete from tenant_photo where tenantid='" +tenantid + "' and propertyid='"+propertyid+"' ";
                            database.execSQL(querydeletephoto);

                            adapter.notifyDataSetChanged();
                            deleteRecordstartek();
                            updateRecordstartek(tenantid,propertyid);
                        }


                    }else {
                        Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return convertView;
        }
    }

    private void updateRecord(String tenantid, String propertyid) {

        final ProgressDialog dialog = new ProgressDialog(Delete_Attendance_TenantList_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action","attendanceexittenant");
            params.put("property_id", propertyid);
            params.put("tenant_id",tenantid);
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
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(context, "Exit Successfully", Toast.LENGTH_SHORT).show();
               }else{

                Toast.makeText(context, "Not Exit", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Delete tenant startek
    private void updateRecordstartek(String tenantid, String propertyid) {

        final ProgressDialog dialog = new ProgressDialog(Delete_Attendance_TenantList_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            JSONObject params = new JSONObject();
            params.put("action","attendanceexittenantstartek");
            params.put("property_id", propertyid);
            params.put("tenant_id",tenantid);
            String url = WebUrls.MAIN_ATTENDACE_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDatastartek(result);
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

    public void getResponseDatastartek(String result) {
        try {
            String status,message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                Toast.makeText(context, "Exit Successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Not Exit", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //

}
