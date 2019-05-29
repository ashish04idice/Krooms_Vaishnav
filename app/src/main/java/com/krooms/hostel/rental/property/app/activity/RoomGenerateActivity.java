package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.AppExeptionFile;
import com.krooms.hostel.rental.property.app.adapter.ListViewadapter_forroomshow;
import com.krooms.hostel.rental.property.app.modal.Roommodel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/2/2017.
 */
public class RoomGenerateActivity extends AppCompatActivity
{
    String slotvalue,totalroomno;
    Intent in;
    String propertyid,forwhom;
    ListView listviewroomno;
    ArrayList<Roommodel> roomnoarraylist;
    String[] roomnopoint;
    LinearLayout linearLayoutRooms;
    RelativeLayout topPanelBar;
    String countvalue;
    Button button_add_more;
    Context context;
    public static RoomGenerateActivity mActivityroom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomnogeneratelayout);
        in=getIntent();
        mActivityroom = this;
        slotvalue=in.getStringExtra("slotvalue");
        totalroomno=in.getStringExtra("totalroomno");
        propertyid=in.getStringExtra("propertyid");
        forwhom=in.getStringExtra("forwhom");
        countvalue=in.getStringExtra("countvalue");
        int totlaroomimnt= Integer.parseInt(totalroomno);
        roomnopoint=slotvalue.split("-");
        int startpoint= Integer.parseInt(roomnopoint[0]);
        int endpoint= Integer.parseInt(roomnopoint[1]);
        topPanelBar=(RelativeLayout)findViewById(R.id.topPanelBar);
        listviewroomno=(ListView)findViewById(R.id.listviewroomno);
        linearLayoutRooms = (LinearLayout) findViewById(R.id.linearLayout_rooms);
        button_add_more=(Button)findViewById(R.id.button_add_more);
        createRoomsApi(propertyid, startpoint);
        context=getApplicationContext();

        topPanelBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(RoomGenerateActivity.this, PropertyRoomsCreationActivity.class);
                mIntent.putExtra("PropertyId", propertyid);
                mIntent.putExtra("countvalue", countvalue);
                mIntent.putExtra("for_whom", forwhom);
                startActivity(mIntent);
            }
        });

        button_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(RoomGenerateActivity.this,HostelListActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent mIntent = new Intent(RoomGenerateActivity.this, PropertyRoomsCreationActivity.class);
                mIntent.putExtra("PropertyId", propertyid);
                mIntent.putExtra("countvalue", countvalue);
                mIntent.putExtra("for_whom", forwhom);
                startActivity(mIntent);

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void createRoomsApi(final String propertyid,final int startpoint)
    {
        final ProgressDialog progressDialog=new ProgressDialog(RoomGenerateActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url= WebUrls.MAIN_URL;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                       getResponsedata(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RoomGenerateActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action","showRoomDetails");
                params.put("property_id",propertyid);
                params.put("startpoint", String.valueOf(startpoint));
                return params;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(RoomGenerateActivity.this);
        queue.add(strRequest);
    }
    public void getResponsedata(String result)
    {
        try
        {
            roomnoarraylist=new ArrayList<>();
            JSONObject jo = new JSONObject(result);
            result = jo.getString("flag");
            String message = jo.getString("message");

            if (result.equalsIgnoreCase("Y"))
            {
                //{"flag":"Y","records":[{"id":"1179","sequenceroomno":"21","property_id":"10","room_no":"21","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1180","sequenceroomno":"22","property_id":"10","room_no":"22","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1181","sequenceroomno":"23","property_id":"10","room_no":"23","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1182","sequenceroomno":"24","property_id":"10","room_no":"24","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1183","sequenceroomno":"25","property_id":"10","room_no":"25","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1184","sequenceroomno":"26","property_id":"10","room_no":"26","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1185","sequenceroomno":"27","property_id":"10","room_no":"27","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1186","sequenceroomno":"28","property_id":"10","room_no":"28","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1187","sequenceroomno":"29","property_id":"10","room_no":"29","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1188","sequenceroomno":"30","property_id":"10","room_no":"30","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1189","sequenceroomno":"31","property_id":"10","room_no":"31","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1190","sequenceroomno":"32","property_id":"10","room_no":"32","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1191","sequenceroomno":"33","property_id":"10","room_no":"33","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1192","sequenceroomno":"34","property_id":"10","room_no":"34","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1193","sequenceroomno":"35","property_id":"10","room_no":"35","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1194","sequenceroomno":"36","property_id":"10","room_no":"36","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1195","sequenceroomno":"37","property_id":"10","room_no":"37","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1196","sequenceroomno":"38","property_id":"10","room_no":"38","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1197","sequenceroomno":"39","property_id":"10","room_no":"39","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1198","sequenceroomno":"40","property_id":"10","room_no":"40","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"}],"message":"Get Rooms !"}
                JSONArray json_main= jo.getJSONArray("records");
                for(int i=0;i<json_main.length();i++)
                {
                    JSONObject jobj=json_main.getJSONObject(i);
                    Roommodel roomnomodel=new Roommodel();
                    roomnomodel.setRoomno(jobj.getString("room_no"));
                    roomnomodel.setSequenceno(jobj.getString("sequenceroomno"));
                    roomnomodel.setLath_bath(jobj.getString("lat_bath"));
                    roomnomodel.setVacant_bed(jobj.getString("vacant"));
                    roomnomodel.setNobed(jobj.getString("room_type"));
                    roomnomodel.setAmount(jobj.getString("amount"));
                    roomnoarraylist.add(roomnomodel);
                }

                if(roomnoarraylist.size()>0)
                {
                    ListViewadapter_forroomshow adapter=new ListViewadapter_forroomshow(RoomGenerateActivity.this,roomnoarraylist,propertyid,forwhom);
                    listviewroomno.setAdapter(adapter);
                    listviewroomno.setItemsCanFocus(true);

                }

              //  Toast.makeText(RoomGenerateActivity.this,message, Toast.LENGTH_SHORT).show();

            } else
            {
                Toast.makeText(RoomGenerateActivity.this, message, Toast.LENGTH_LONG).show();
            }
            Log.d("result..", result);
        } catch (JSONException e)
        {
            e.printStackTrace();

            AppExeptionFile.handleUncaughtException(e);

        }
    }


}
