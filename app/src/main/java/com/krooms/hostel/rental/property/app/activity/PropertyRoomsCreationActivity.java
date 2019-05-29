package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.krooms.hostel.rental.property.app.adapter.Propertyrooms_Adapter;
import com.krooms.hostel.rental.property.app.listener.RecyclerItemClickListener;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.Roommodel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/2/2017.
 */
public class PropertyRoomsCreationActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,View.OnClickListener
{
    ArrayList<Roommodel> roomnoarraylist;
    Button createrooms;
    int slotsno;
    RelativeLayout topPanelBar;
    int totalroomnovalue=20;
    GridView gridview;
    EditText no_of_rooms_selection_edit;
    Spinner for_whom_selection_spinner;
    String[] whoms= {"For Whom","Boys", "Girls", "Family", "Businessman", "Employee"};
     int roomnovaluemainno=0;
    LinearLayout mainlayout;
    String propertyidvalue;
    Button button_add_more,btnaddroomimage;
    String countvalue,for_whom;
    String maincountval="0";
    Intent in;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomnolayout);
        in=getIntent();
        context=getApplicationContext();
        propertyidvalue=in.getStringExtra("PropertyId");
        countvalue=in.getStringExtra("countvalue");
        for_whom=in.getStringExtra("for_whom");
        roomnovaluemainno= Integer.parseInt(countvalue);
        maincountval=countvalue;
        no_of_rooms_selection_edit=(EditText)findViewById(R.id.no_of_rooms_selection_edit);
        for_whom_selection_spinner=(Spinner)findViewById(R.id.for_whom_selection);
        createrooms=(Button)findViewById(R.id.createrooms);
        createrooms.setClickable(true);
        gridview=(GridView)findViewById(R.id.gridview);
        button_add_more=(Button)findViewById(R.id.button_add_more);
        topPanelBar=(RelativeLayout)findViewById(R.id.topPanelBar);
        mainlayout=(LinearLayout)findViewById(R.id.mainlayout);
        button_add_more=(Button)findViewById(R.id.button_add_more);
        btnaddroomimage=(Button)findViewById(R.id.btn_roomimg);
        btnaddroomimage.setOnClickListener(this);

        if(roomnovaluemainno>0)
        {
            mainlayout.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            button_add_more.setVisibility(View.VISIBLE);
            btnaddroomimage.setVisibility(View.VISIBLE);
            roomnoarraylist=new ArrayList<Roommodel>();
            String valuesetcount= String.valueOf(roomnovaluemainno);
            no_of_rooms_selection_edit.setEnabled(false);
            no_of_rooms_selection_edit.setText(valuesetcount);
            createSlotMethod(roomnovaluemainno);
        }else
        {
            mainlayout.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);
            btnaddroomimage.setVisibility(View.GONE);
            button_add_more.setVisibility(View.GONE);
        }


        for_whom_selection_spinner.setOnItemSelectedListener(PropertyRoomsCreationActivity.this);
        ArrayAdapter adaptersim = new ArrayAdapter(this,android.R.layout.simple_spinner_item,whoms);
        adaptersim.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for_whom_selection_spinner.setAdapter(adaptersim);
        if (for_whom != null && !for_whom.isEmpty() && !for_whom.equals("null"))
        {
            int spinnerPosition = adaptersim.getPosition(for_whom);
            for_whom_selection_spinner.setSelection(spinnerPosition);
        }

        createrooms.setOnClickListener(PropertyRoomsCreationActivity.this);
        button_add_more.setOnClickListener(PropertyRoomsCreationActivity.this);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String totalroomno = roomnoarraylist.get(position).getTotalnoofrooms();
                String slotsvalue = roomnoarraylist.get(position).getRoomnoslotvalue();
               // String maintoalvalue= String.valueOf(roomnovaluemainno);
                if (totalroomno.equals("0")) {
                    totalroomno = "20";
                }

                Intent in = new Intent(PropertyRoomsCreationActivity.this, RoomGenerateMainActivity.class);
                in.putExtra("slotvalue",slotsvalue);
                in.putExtra("countvalue",maincountval);
                in.putExtra("propertyid", propertyidvalue);
                in.putExtra("totalroomno", totalroomno);
                in.putExtra("forwhom",for_whom);
                startActivity(in);

            }
        });

        topPanelBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PropertyRoomsCreationActivity.this, HostelListActivity.class);
                startActivity(i);
            }
        });

        button_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(PropertyRoomsCreationActivity.this,AddAdainRoomActivity.class);
                i.putExtra("countvalue",maincountval);
                i.putExtra("propertyid", propertyidvalue);
                i.putExtra("for_whom",for_whom);
                startActivity(i);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(getApplicationContext(),whoms[position] , Toast.LENGTH_LONG).show();
        for_whom=whoms[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent i = new Intent(PropertyRoomsCreationActivity.this, HostelListActivity.class);
                startActivity(i);

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {

            case R.id.createrooms:

                int roomcountvalue=0;

                roomnoarraylist = new ArrayList<Roommodel>();
                String roomnovalue = no_of_rooms_selection_edit.getText().toString();

                if (roomnovalue.equals("")){
                   }else{
                    roomcountvalue = Integer.parseInt(roomnovalue);
                    }

                if(roomnovalue.equals(""))
                {
                    Toast.makeText(PropertyRoomsCreationActivity.this, "Please Enter No Of Room ", Toast.LENGTH_SHORT).show();
                }else if(roomcountvalue > 500)
                {
                    Toast.makeText(PropertyRoomsCreationActivity.this, "You Can not Create more than 500 Rooms ", Toast.LENGTH_SHORT).show();
                }else if(for_whom.equalsIgnoreCase("For Whom"))
                {
                    Toast.makeText(PropertyRoomsCreationActivity.this, "Please Select For whom", Toast.LENGTH_SHORT).show();
                }else
                {
                    int valuesetcount= Integer.parseInt(roomnovalue);
                    createrooms.setClickable(false);
                    createRoomsApi(valuesetcount, propertyidvalue,for_whom);
                }

                break;

            case R.id.btn_roomimg:
                uploadRoomImg();
                break;
        }
    }

    private void uploadRoomImg() {

        //add room image upload code on this aaded by me
        final Dialog dialog = new Dialog(PropertyRoomsCreationActivity.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_two_btn_dialog);
        TextView maintitle = (TextView) dialog.findViewById(R.id.alertTitle);
        TextView text = (TextView) dialog.findViewById(R.id.categoryNameInput);
        text.setText("Rooms added successfully.Do you want to add room images or view your property ?");
        Button yes = (Button) dialog.findViewById(R.id.alertYesBtn);
        Button no = (Button) dialog.findViewById(R.id.alertNoBtn);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HostelDetailActivity.mActivity != null) {
                    HostelDetailActivity.mActivity.finish();
                }
                // Common.Config("property_id" + mSharedStorage.getUserPropertyId());
                Intent mIntent = new Intent(PropertyRoomsCreationActivity.this, HostelDetailActivity.class);
                mIntent.putExtra("property_id",propertyidvalue);
                mIntent.putExtra("ShowRooms", true);
                startActivity(mIntent);
                finish();
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();


     //   Toast.makeText(PropertyRoomsCreationActivity.this, "Click", Toast.LENGTH_SHORT).show();
    }

    public void createSlotMethod(int roomnovaluemainno)
    {
        maincountval=String.valueOf(roomnovaluemainno);
        int roomnocount= roomnovaluemainno;
        int countsize=roomnocount%20;
        if(countsize == 0)
        {
            slotsno=roomnocount/20;
        }else
        {
            int slotsnovalue=roomnocount/20;
            slotsno=slotsnovalue+1;
        }

        for(int i=1;i<=slotsno;i++)
        {
            Roommodel roomnomodel=new Roommodel();
            roomnomodel.setRoomno("" + i);

            switch (i)
            {
                case 1:
                    roomnomodel.setRoomnoslotvalue("1-20");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 2:
                    roomnomodel.setRoomnoslotvalue("21-40");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 3:
                    roomnomodel.setRoomnoslotvalue("41-60");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 4:
                    roomnomodel.setRoomnoslotvalue("61-80");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 5:
                    roomnomodel.setRoomnoslotvalue("81-100");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 6:
                    roomnomodel.setRoomnoslotvalue("101-120");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 7:
                    roomnomodel.setRoomnoslotvalue("121-140");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 8:
                    roomnomodel.setRoomnoslotvalue("141-160");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 9:
                    roomnomodel.setRoomnoslotvalue("161-180");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 10:
                    roomnomodel.setRoomnoslotvalue("181-200");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 11:
                    roomnomodel.setRoomnoslotvalue("201-220");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 12:
                    roomnomodel.setRoomnoslotvalue("221-240");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 13:
                    roomnomodel.setRoomnoslotvalue("241-260");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 14:
                    roomnomodel.setRoomnoslotvalue("261-280");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 15:
                    roomnomodel.setRoomnoslotvalue("281-300");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 16:
                    roomnomodel.setRoomnoslotvalue("301-320");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 17:
                    roomnomodel.setRoomnoslotvalue("321-340");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 18:
                    roomnomodel.setRoomnoslotvalue("341-360");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 19:
                    roomnomodel.setRoomnoslotvalue("361-380");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 20:
                    roomnomodel.setRoomnoslotvalue("381-400");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 21:
                    roomnomodel.setRoomnoslotvalue("401-420");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 22:
                    roomnomodel.setRoomnoslotvalue("421-440");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 23:
                    roomnomodel.setRoomnoslotvalue("441-460");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 24:
                    roomnomodel.setRoomnoslotvalue("461-480");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;
                case 25:
                    roomnomodel.setRoomnoslotvalue("481-500");
                    if(slotsno == i)
                    {
                        roomnomodel.setTotalnoofrooms(""+countsize);
                    }else
                    {
                        int countsizevalue=20;
                        roomnomodel.setTotalnoofrooms(""+countsizevalue);
                    }
                    break;

            }
            roomnoarraylist.add(roomnomodel);

        }
        int arraylistvalue=roomnoarraylist.size();
        Propertyrooms_Adapter adapter=new Propertyrooms_Adapter(PropertyRoomsCreationActivity.this,roomnoarraylist,propertyidvalue);
        gridview.setAdapter(adapter);

    }

    public void createRoomsApi(final int valuesetcount, final String propertyidvalue,final String forwhom)
    {
        final ProgressDialog progressDialog=new ProgressDialog(PropertyRoomsCreationActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url= WebUrls.MAIN_URL;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        getResponsedata(response,valuesetcount);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(PropertyRoomsCreationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action","createAllRoomsFirst");
                params.put("property_id",propertyidvalue);
                params.put("noofrooms", String.valueOf(valuesetcount));
                params.put("forwhom",forwhom);
                return params;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(PropertyRoomsCreationActivity.this);
        queue.add(strRequest);
    }
    public void getResponsedata(String result,int valuesetcount)
    {
        try
        {
            JSONObject jo = new JSONObject(result);
            result = jo.getString("flag");
            String message = jo.getString("message");
            createrooms.setClickable(true);

            if (result.equalsIgnoreCase("Y"))
            {

                Toast.makeText(PropertyRoomsCreationActivity.this,message, Toast.LENGTH_SHORT).show();
                mainlayout.setVisibility(View.GONE);
                button_add_more.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.VISIBLE);
                createSlotMethod(valuesetcount);


            } else
            {
                Toast.makeText(PropertyRoomsCreationActivity.this, message, Toast.LENGTH_LONG).show();
            }
            Log.d("result..", result);
        } catch (JSONException e)
        {
            AppExeptionFile.handleUncaughtException(e);

            e.printStackTrace();
        }
    }
}
