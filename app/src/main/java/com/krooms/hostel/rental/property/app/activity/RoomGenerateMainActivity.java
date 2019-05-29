package com.krooms.hostel.rental.property.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.krooms.hostel.rental.property.app.adapter.RoomspinnerAdapter;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
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
public class RoomGenerateMainActivity extends AppCompatActivity implements View.OnClickListener {
    String slotvalue, totalroomno;
    Intent in;
    String propertyid, forwhom;
    ArrayList<Roommodel> roomnoarraylist;
    String[] roomnopoint;
    LinearLayout linearLayoutRooms;
    RelativeLayout topPanelBar;
    String countvalue;
    Button button_add_more;
    Context context;
    TextView room_no_id_1, room_no_id_2, room_no_id_3, room_no_id_4, room_no_id_5, room_no_id_6, room_no_id_7, room_no_id_8, room_no_id_9, room_no_id_10, room_no_id_11, room_no_id_12, room_no_id_13, room_no_id_14, room_no_id_15, room_no_id_16, room_no_id_17, room_no_id_18, room_no_id_19, room_no_id_20;
    EditText amount_of_bed_1, roomNo_input_1;
    LinearLayout bedselection_layout_1, vacant_bed_layout_1;
    Button createrooms_1;
    TextView no_bed_1, vacat_no_bed_1;
    EditText amount_of_bed_2, roomNo_input_2;
    LinearLayout bedselection_layout_2, vacant_bed_layout_2;
    Button createrooms_2;
    TextView no_bed_2, vacat_no_bed_2;
    EditText amount_of_bed_3, roomNo_input_3;
    LinearLayout bedselection_layout_3, vacant_bed_layout_3;
    Button createrooms_3;
    TextView no_bed_3, vacat_no_bed_3;
    EditText amount_of_bed_4, roomNo_input_4;
    LinearLayout bedselection_layout_4, vacant_bed_layout_4;
    Button createrooms_4;
    TextView no_bed_4, vacat_no_bed_4;
    EditText amount_of_bed_5, roomNo_input_5;
    LinearLayout bedselection_layout_5, vacant_bed_layout_5;
    Button createrooms_5;
    TextView no_bed_5, vacat_no_bed_5;
    EditText amount_of_bed_6, roomNo_input_6;
    LinearLayout bedselection_layout_6, vacant_bed_layout_6;
    Button createrooms_6;
    TextView no_bed_6, vacat_no_bed_6;
    EditText amount_of_bed_7, roomNo_input_7;
    LinearLayout bedselection_layout_7, vacant_bed_layout_7;
    Button createrooms_7;
    TextView no_bed_7, vacat_no_bed_7;
    EditText amount_of_bed_8, roomNo_input_8;
    LinearLayout bedselection_layout_8, vacant_bed_layout_8;
    Button createrooms_8;
    TextView no_bed_8, vacat_no_bed_8;
    EditText amount_of_bed_9, roomNo_input_9;
    LinearLayout bedselection_layout_9, vacant_bed_layout_9;
    Button createrooms_9;
    TextView no_bed_9, vacat_no_bed_9;
    EditText amount_of_bed_10, roomNo_input_10;
    LinearLayout bedselection_layout_10, vacant_bed_layout_10;
    Button createrooms_10;
    TextView no_bed_10, vacat_no_bed_10;
    EditText amount_of_bed_11, roomNo_input_11;
    LinearLayout bedselection_layout_11, vacant_bed_layout_11;
    Button createrooms_11;
    TextView no_bed_11, vacat_no_bed_11;
    EditText amount_of_bed_12, roomNo_input_12;
    LinearLayout bedselection_layout_12, vacant_bed_layout_12;
    Button createrooms_12;
    TextView no_bed_12, vacat_no_bed_12;
    EditText amount_of_bed_13, roomNo_input_13;
    LinearLayout bedselection_layout_13, vacant_bed_layout_13;
    Button createrooms_13;
    TextView no_bed_13, vacat_no_bed_13;
    EditText amount_of_bed_14, roomNo_input_14;
    LinearLayout bedselection_layout_14, vacant_bed_layout_14;
    Button createrooms_14;
    TextView no_bed_14, vacat_no_bed_14;
    EditText amount_of_bed_15, roomNo_input_15;
    LinearLayout bedselection_layout_15, vacant_bed_layout_15;
    Button createrooms_15;
    TextView no_bed_15, vacat_no_bed_15;
    RelativeLayout room_layout_15;
    EditText amount_of_bed_16, roomNo_input_16;
    LinearLayout bedselection_layout_16, vacant_bed_layout_16;
    Button createrooms_16;
    TextView no_bed_16, vacat_no_bed_16;
    RelativeLayout room_layout_16;
    EditText amount_of_bed_17, roomNo_input_17;
    LinearLayout bedselection_layout_17, vacant_bed_layout_17;
    Button createrooms_17;
    TextView no_bed_17, vacat_no_bed_17;
    RelativeLayout room_layout_17;
    EditText amount_of_bed_18, roomNo_input_18;
    LinearLayout bedselection_layout_18, vacant_bed_layout_18;
    Button createrooms_18;
    TextView no_bed_18, vacat_no_bed_18;
    RelativeLayout room_layout_18;
    RelativeLayout room_layout_1, room_layout_2, room_layout_3, room_layout_4, room_layout_5, room_layout_6, room_layout_7, room_layout_8, room_layout_9, room_layout_10, room_layout_11, room_layout_12, room_layout_13, room_layout_14;
    EditText amount_of_bed_19, roomNo_input_19;
    LinearLayout bedselection_layout_19, vacant_bed_layout_19;
    Button createrooms_19;
    TextView no_bed_19, vacat_no_bed_19;
    RelativeLayout room_layout_19;
    EditText amount_of_bed_20, roomNo_input_20;
    LinearLayout bedselection_layout_20, vacant_bed_layout_20;
    Button createrooms_20;
    TextView no_bed_20, vacat_no_bed_20;
    RelativeLayout room_layout_20;
    public static RoomGenerateMainActivity mActivityroom = null;
    TextView lat_bath_selector_view_1;
    TextView lat_bath_selector_view_2;
    TextView lat_bath_selector_view_3;
    TextView lat_bath_selector_view_4;
    TextView lat_bath_selector_view_5;
    TextView lat_bath_selector_view_6;
    TextView lat_bath_selector_view_7;
    TextView lat_bath_selector_view_8;
    TextView lat_bath_selector_view_9;
    TextView lat_bath_selector_view_10;
    TextView lat_bath_selector_view_11;
    TextView lat_bath_selector_view_12;
    TextView lat_bath_selector_view_13;
    TextView lat_bath_selector_view_14;
    TextView lat_bath_selector_view_15;
    TextView lat_bath_selector_view_16;
    TextView lat_bath_selector_view_17;
    TextView lat_bath_selector_view_18;
    TextView lat_bath_selector_view_19;
    TextView lat_bath_selector_view_20;

    LinearLayout lat_bath_selector_layout_1;
    LinearLayout lat_bath_selector_layout_2;
    LinearLayout lat_bath_selector_layout_3;
    LinearLayout lat_bath_selector_layout_4;
    LinearLayout lat_bath_selector_layout_5;
    LinearLayout lat_bath_selector_layout_6;
    LinearLayout lat_bath_selector_layout_7;
    LinearLayout lat_bath_selector_layout_8;
    LinearLayout lat_bath_selector_layout_9;
    LinearLayout lat_bath_selector_layout_10;
    LinearLayout lat_bath_selector_layout_11;
    LinearLayout lat_bath_selector_layout_12;
    LinearLayout lat_bath_selector_layout_13;
    LinearLayout lat_bath_selector_layout_14;
    LinearLayout lat_bath_selector_layout_15;
    LinearLayout lat_bath_selector_layout_16;
    LinearLayout lat_bath_selector_layout_17;
    LinearLayout lat_bath_selector_layout_18;
    LinearLayout lat_bath_selector_layout_19;
    LinearLayout lat_bath_selector_layout_20;

    int startpoint;

    //new changes
    public TextView txtfacility1, txtfacility2, txtfacility3, txtfacility4, txtfacility5, txtfacility6, txtfacility7, txtfacility8, txtfacility9, txtfacility10, txtfacility11, txtfacility12, txtfacility13, txtfacility14, txtfacility15, txtfacility16, txtfacility17, txtfacility18, txtfacility19, txtfacility20;
    private ArrayList mSelectedItems;
    String mainValue = "";
    public String items[] = {"Let Bath", "AC", "Balcony"};

    static String a = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomnogeneratemaillayout);
        in = getIntent();
        mActivityroom = this;
        slotvalue = in.getStringExtra("slotvalue");
        totalroomno = in.getStringExtra("totalroomno");
        propertyid = in.getStringExtra("propertyid");
        forwhom = in.getStringExtra("forwhom");
        countvalue = in.getStringExtra("countvalue");

        //new changes
        mSelectedItems = new ArrayList();
        //

        int totlaroomimnt = Integer.parseInt(totalroomno);
        roomnopoint = slotvalue.split("-");
        startpoint = Integer.parseInt(roomnopoint[0]);
        int endpoint = Integer.parseInt(roomnopoint[1]);
        topPanelBar = (RelativeLayout) findViewById(R.id.topPanelBar);
        linearLayoutRooms = (LinearLayout) findViewById(R.id.linearLayout_rooms);
        button_add_more = (Button) findViewById(R.id.button_add_more);
        //createRoomsApi(propertyid, startpoint);
        context = getApplicationContext();
        allViewId();

        topPanelBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(RoomGenerateMainActivity.this, PropertyRoomsCreationActivity.class);
                mIntent.putExtra("PropertyId", propertyid);
                mIntent.putExtra("countvalue", countvalue);
                mIntent.putExtra("for_whom", forwhom);
                startActivity(mIntent);
            }
        });

        button_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RoomGenerateMainActivity.this, HostelListActivity.class);
                startActivity(in);
            }
        });

    }

    public void allViewId() {
        //new chnages
        txtfacility1 = (TextView) findViewById(R.id.txtfacility1);
        txtfacility2 = (TextView) findViewById(R.id.txtfacility2);
        txtfacility3 = (TextView) findViewById(R.id.txtfacility3);
        txtfacility4 = (TextView) findViewById(R.id.txtfacility4);
        txtfacility5 = (TextView) findViewById(R.id.txtfacility5);
        txtfacility6 = (TextView) findViewById(R.id.txtfacility6);
        txtfacility7 = (TextView) findViewById(R.id.txtfacility7);
        txtfacility8 = (TextView) findViewById(R.id.txtfacility8);
        txtfacility9 = (TextView) findViewById(R.id.txtfacility9);
        txtfacility10 = (TextView) findViewById(R.id.txtfacility10);
        txtfacility11 = (TextView) findViewById(R.id.txtfacility11);
        txtfacility12 = (TextView) findViewById(R.id.txtfacility12);
        txtfacility13 = (TextView) findViewById(R.id.txtfacility13);
        txtfacility14 = (TextView) findViewById(R.id.txtfacility14);
        txtfacility15 = (TextView) findViewById(R.id.txtfacility15);
        txtfacility16 = (TextView) findViewById(R.id.txtfacility16);
        txtfacility17 = (TextView) findViewById(R.id.txtfacility17);
        txtfacility18 = (TextView) findViewById(R.id.txtfacility18);
        txtfacility19 = (TextView) findViewById(R.id.txtfacility19);
        txtfacility20 = (TextView) findViewById(R.id.txtfacility20);

        //


        room_no_id_1 = (TextView) findViewById(R.id.room_no_id_1);
        room_layout_1 = (RelativeLayout) findViewById(R.id.room_layout_1);
        amount_of_bed_1 = (EditText) findViewById(R.id.amount_of_bed_1);
        roomNo_input_1 = (EditText) findViewById(R.id.roomNo_input_1);
        bedselection_layout_1 = (LinearLayout) findViewById(R.id.bedselection_layout_1);
        vacant_bed_layout_1 = (LinearLayout) findViewById(R.id.vacant_bed_layout_1);
        createrooms_1 = (Button) findViewById(R.id.createrooms_1);
        no_bed_1 = (TextView) findViewById(R.id.no_bed_1);
        vacat_no_bed_1 = (TextView) findViewById(R.id.vacat_no_bed_1);

        //2 room
        room_no_id_2 = (TextView) findViewById(R.id.room_no_id_2);
        room_layout_2 = (RelativeLayout) findViewById(R.id.room_layout_2);
        amount_of_bed_2 = (EditText) findViewById(R.id.amount_of_bed_2);
        roomNo_input_2 = (EditText) findViewById(R.id.roomNo_input_2);
        bedselection_layout_2 = (LinearLayout) findViewById(R.id.bedselection_layout_2);
        vacant_bed_layout_2 = (LinearLayout) findViewById(R.id.vacant_bed_layout_2);
        createrooms_2 = (Button) findViewById(R.id.createrooms_2);
        no_bed_2 = (TextView) findViewById(R.id.no_bed_2);
        vacat_no_bed_2 = (TextView) findViewById(R.id.vacat_no_bed_2);

        //3
        room_no_id_3 = (TextView) findViewById(R.id.room_no_id_3);
        room_layout_3 = (RelativeLayout) findViewById(R.id.room_layout_3);
        amount_of_bed_3 = (EditText) findViewById(R.id.amount_of_bed_3);
        roomNo_input_3 = (EditText) findViewById(R.id.roomNo_input_3);
        bedselection_layout_3 = (LinearLayout) findViewById(R.id.bedselection_layout_3);
        vacant_bed_layout_3 = (LinearLayout) findViewById(R.id.vacant_bed_layout_3);
        createrooms_3 = (Button) findViewById(R.id.createrooms_3);
        no_bed_3 = (TextView) findViewById(R.id.no_bed_3);
        vacat_no_bed_3 = (TextView) findViewById(R.id.vacat_no_bed_3);

        //.4
        room_no_id_4 = (TextView) findViewById(R.id.room_no_id_4);
        room_layout_4 = (RelativeLayout) findViewById(R.id.room_layout_4);
        amount_of_bed_4 = (EditText) findViewById(R.id.amount_of_bed_4);
        roomNo_input_4 = (EditText) findViewById(R.id.roomNo_input_4);
        bedselection_layout_4 = (LinearLayout) findViewById(R.id.bedselection_layout_4);
        vacant_bed_layout_4 = (LinearLayout) findViewById(R.id.vacant_bed_layout_4);
        createrooms_4 = (Button) findViewById(R.id.createrooms_4);
        no_bed_4 = (TextView) findViewById(R.id.no_bed_4);
        vacat_no_bed_4 = (TextView) findViewById(R.id.vacat_no_bed_4);

        //5
        room_no_id_5 = (TextView) findViewById(R.id.room_no_id_5);
        room_layout_5 = (RelativeLayout) findViewById(R.id.room_layout_5);
        amount_of_bed_5 = (EditText) findViewById(R.id.amount_of_bed_5);
        roomNo_input_5 = (EditText) findViewById(R.id.roomNo_input_5);
        bedselection_layout_5 = (LinearLayout) findViewById(R.id.bedselection_layout_5);
        vacant_bed_layout_5 = (LinearLayout) findViewById(R.id.vacant_bed_layout_5);
        createrooms_5 = (Button) findViewById(R.id.createrooms_5);
        no_bed_5 = (TextView) findViewById(R.id.no_bed_5);
        vacat_no_bed_5 = (TextView) findViewById(R.id.vacat_no_bed_5);

        //6
        room_no_id_6 = (TextView) findViewById(R.id.room_no_id_6);
        room_layout_6 = (RelativeLayout) findViewById(R.id.room_layout_6);
        amount_of_bed_6 = (EditText) findViewById(R.id.amount_of_bed_6);
        roomNo_input_6 = (EditText) findViewById(R.id.roomNo_input_6);
        bedselection_layout_6 = (LinearLayout) findViewById(R.id.bedselection_layout_6);
        vacant_bed_layout_6 = (LinearLayout) findViewById(R.id.vacant_bed_layout_6);
        createrooms_6 = (Button) findViewById(R.id.createrooms_6);
        no_bed_6 = (TextView) findViewById(R.id.no_bed_6);
        vacat_no_bed_6 = (TextView) findViewById(R.id.vacat_no_bed_6);

        //7
        room_no_id_7 = (TextView) findViewById(R.id.room_no_id_7);
        room_layout_7 = (RelativeLayout) findViewById(R.id.room_layout_7);
        amount_of_bed_7 = (EditText) findViewById(R.id.amount_of_bed_7);
        roomNo_input_7 = (EditText) findViewById(R.id.roomNo_input_7);
        bedselection_layout_7 = (LinearLayout) findViewById(R.id.bedselection_layout_7);
        vacant_bed_layout_7 = (LinearLayout) findViewById(R.id.vacant_bed_layout_7);
        createrooms_7 = (Button) findViewById(R.id.createrooms_7);
        no_bed_7 = (TextView) findViewById(R.id.no_bed_7);
        vacat_no_bed_7 = (TextView) findViewById(R.id.vacat_no_bed_7);

        //8
        room_no_id_8 = (TextView) findViewById(R.id.room_no_id_8);
        room_layout_8 = (RelativeLayout) findViewById(R.id.room_layout_8);
        amount_of_bed_8 = (EditText) findViewById(R.id.amount_of_bed_8);
        roomNo_input_8 = (EditText) findViewById(R.id.roomNo_input_8);
        bedselection_layout_8 = (LinearLayout) findViewById(R.id.bedselection_layout_8);
        vacant_bed_layout_8 = (LinearLayout) findViewById(R.id.vacant_bed_layout_8);
        createrooms_8 = (Button) findViewById(R.id.createrooms_8);
        no_bed_8 = (TextView) findViewById(R.id.no_bed_8);
        vacat_no_bed_8 = (TextView) findViewById(R.id.vacat_no_bed_8);

        //9
        room_no_id_9 = (TextView) findViewById(R.id.room_no_id_9);
        room_layout_9 = (RelativeLayout) findViewById(R.id.room_layout_9);
        amount_of_bed_9 = (EditText) findViewById(R.id.amount_of_bed_9);
        roomNo_input_9 = (EditText) findViewById(R.id.roomNo_input_9);
        bedselection_layout_9 = (LinearLayout) findViewById(R.id.bedselection_layout_9);
        vacant_bed_layout_9 = (LinearLayout) findViewById(R.id.vacant_bed_layout_9);
        createrooms_9 = (Button) findViewById(R.id.createrooms_9);
        no_bed_9 = (TextView) findViewById(R.id.no_bed_9);
        vacat_no_bed_9 = (TextView) findViewById(R.id.vacat_no_bed_9);

        //10
        room_no_id_10 = (TextView) findViewById(R.id.room_no_id_10);
        room_layout_10 = (RelativeLayout) findViewById(R.id.room_layout_10);
        amount_of_bed_10 = (EditText) findViewById(R.id.amount_of_bed_10);
        roomNo_input_10 = (EditText) findViewById(R.id.roomNo_input_10);
        bedselection_layout_10 = (LinearLayout) findViewById(R.id.bedselection_layout_10);
        vacant_bed_layout_10 = (LinearLayout) findViewById(R.id.vacant_bed_layout_10);
        createrooms_10 = (Button) findViewById(R.id.createrooms_10);
        no_bed_10 = (TextView) findViewById(R.id.no_bed_10);
        vacat_no_bed_10 = (TextView) findViewById(R.id.vacat_no_bed_10);

        //11
        room_no_id_11 = (TextView) findViewById(R.id.room_no_id_11);
        room_layout_11 = (RelativeLayout) findViewById(R.id.room_layout_11);
        amount_of_bed_11 = (EditText) findViewById(R.id.amount_of_bed_11);
        roomNo_input_11 = (EditText) findViewById(R.id.roomNo_input_11);
        bedselection_layout_11 = (LinearLayout) findViewById(R.id.bedselection_layout_11);
        vacant_bed_layout_11 = (LinearLayout) findViewById(R.id.vacant_bed_layout_11);
        createrooms_11 = (Button) findViewById(R.id.createrooms_11);
        no_bed_11 = (TextView) findViewById(R.id.no_bed_11);
        vacat_no_bed_11 = (TextView) findViewById(R.id.vacat_no_bed_11);

        //12
        room_no_id_12 = (TextView) findViewById(R.id.room_no_id_12);
        room_layout_12 = (RelativeLayout) findViewById(R.id.room_layout_12);
        amount_of_bed_12 = (EditText) findViewById(R.id.amount_of_bed_12);
        roomNo_input_12 = (EditText) findViewById(R.id.roomNo_input_12);
        bedselection_layout_12 = (LinearLayout) findViewById(R.id.bedselection_layout_12);
        vacant_bed_layout_12 = (LinearLayout) findViewById(R.id.vacant_bed_layout_12);
        createrooms_12 = (Button) findViewById(R.id.createrooms_12);
        no_bed_12 = (TextView) findViewById(R.id.no_bed_12);
        vacat_no_bed_12 = (TextView) findViewById(R.id.vacat_no_bed_12);

        //13
        room_no_id_13 = (TextView) findViewById(R.id.room_no_id_13);
        room_layout_13 = (RelativeLayout) findViewById(R.id.room_layout_13);
        amount_of_bed_13 = (EditText) findViewById(R.id.amount_of_bed_13);
        roomNo_input_13 = (EditText) findViewById(R.id.roomNo_input_13);
        bedselection_layout_13 = (LinearLayout) findViewById(R.id.bedselection_layout_13);
        vacant_bed_layout_13 = (LinearLayout) findViewById(R.id.vacant_bed_layout_13);
        createrooms_13 = (Button) findViewById(R.id.createrooms_13);
        no_bed_13 = (TextView) findViewById(R.id.no_bed_13);
        vacat_no_bed_13 = (TextView) findViewById(R.id.vacat_no_bed_13);

        //14
        room_no_id_14 = (TextView) findViewById(R.id.room_no_id_14);
        room_layout_14 = (RelativeLayout) findViewById(R.id.room_layout_14);
        amount_of_bed_14 = (EditText) findViewById(R.id.amount_of_bed_14);
        roomNo_input_14 = (EditText) findViewById(R.id.roomNo_input_14);
        bedselection_layout_14 = (LinearLayout) findViewById(R.id.bedselection_layout_14);
        vacant_bed_layout_14 = (LinearLayout) findViewById(R.id.vacant_bed_layout_14);
        createrooms_14 = (Button) findViewById(R.id.createrooms_14);
        no_bed_14 = (TextView) findViewById(R.id.no_bed_14);
        vacat_no_bed_14 = (TextView) findViewById(R.id.vacat_no_bed_14);

        //15
        room_no_id_15 = (TextView) findViewById(R.id.room_no_id_15);
        room_layout_15 = (RelativeLayout) findViewById(R.id.room_layout_15);
        amount_of_bed_15 = (EditText) findViewById(R.id.amount_of_bed_15);
        roomNo_input_15 = (EditText) findViewById(R.id.roomNo_input_15);
        bedselection_layout_15 = (LinearLayout) findViewById(R.id.bedselection_layout_15);
        vacant_bed_layout_15 = (LinearLayout) findViewById(R.id.vacant_bed_layout_15);
        createrooms_15 = (Button) findViewById(R.id.createrooms_15);
        no_bed_15 = (TextView) findViewById(R.id.no_bed_15);
        vacat_no_bed_15 = (TextView) findViewById(R.id.vacat_no_bed_15);

        //16
        room_no_id_16 = (TextView) findViewById(R.id.room_no_id_16);
        room_layout_16 = (RelativeLayout) findViewById(R.id.room_layout_16);
        amount_of_bed_16 = (EditText) findViewById(R.id.amount_of_bed_16);
        roomNo_input_16 = (EditText) findViewById(R.id.roomNo_input_16);
        bedselection_layout_16 = (LinearLayout) findViewById(R.id.bedselection_layout_16);
        vacant_bed_layout_16 = (LinearLayout) findViewById(R.id.vacant_bed_layout_16);
        createrooms_16 = (Button) findViewById(R.id.createrooms_16);
        no_bed_16 = (TextView) findViewById(R.id.no_bed_16);
        vacat_no_bed_16 = (TextView) findViewById(R.id.vacat_no_bed_16);

        //17
        room_no_id_17 = (TextView) findViewById(R.id.room_no_id_17);
        room_layout_17 = (RelativeLayout) findViewById(R.id.room_layout_17);
        amount_of_bed_17 = (EditText) findViewById(R.id.amount_of_bed_17);
        roomNo_input_17 = (EditText) findViewById(R.id.roomNo_input_17);
        bedselection_layout_17 = (LinearLayout) findViewById(R.id.bedselection_layout_17);
        vacant_bed_layout_17 = (LinearLayout) findViewById(R.id.vacant_bed_layout_17);
        createrooms_17 = (Button) findViewById(R.id.createrooms_17);
        no_bed_17 = (TextView) findViewById(R.id.no_bed_17);
        vacat_no_bed_17 = (TextView) findViewById(R.id.vacat_no_bed_17);

        //18
        room_no_id_18 = (TextView) findViewById(R.id.room_no_id_18);
        room_layout_18 = (RelativeLayout) findViewById(R.id.room_layout_18);
        amount_of_bed_18 = (EditText) findViewById(R.id.amount_of_bed_18);
        roomNo_input_18 = (EditText) findViewById(R.id.roomNo_input_18);
        bedselection_layout_18 = (LinearLayout) findViewById(R.id.bedselection_layout_18);
        vacant_bed_layout_18 = (LinearLayout) findViewById(R.id.vacant_bed_layout_18);
        createrooms_18 = (Button) findViewById(R.id.createrooms_18);
        no_bed_18 = (TextView) findViewById(R.id.no_bed_18);
        vacat_no_bed_18 = (TextView) findViewById(R.id.vacat_no_bed_18);

        //19
        room_no_id_19 = (TextView) findViewById(R.id.room_no_id_19);
        room_layout_19 = (RelativeLayout) findViewById(R.id.room_layout_19);
        amount_of_bed_19 = (EditText) findViewById(R.id.amount_of_bed_19);
        roomNo_input_19 = (EditText) findViewById(R.id.roomNo_input_19);
        bedselection_layout_19 = (LinearLayout) findViewById(R.id.bedselection_layout_19);
        vacant_bed_layout_19 = (LinearLayout) findViewById(R.id.vacant_bed_layout_19);
        createrooms_19 = (Button) findViewById(R.id.createrooms_19);
        no_bed_19 = (TextView) findViewById(R.id.no_bed_19);
        vacat_no_bed_19 = (TextView) findViewById(R.id.vacat_no_bed_19);

        //20
        room_no_id_20 = (TextView) findViewById(R.id.room_no_id_20);
        room_layout_20 = (RelativeLayout) findViewById(R.id.room_layout_20);
        amount_of_bed_20 = (EditText) findViewById(R.id.amount_of_bed_20);
        roomNo_input_20 = (EditText) findViewById(R.id.roomNo_input_20);
        bedselection_layout_20 = (LinearLayout) findViewById(R.id.bedselection_layout_20);
        vacant_bed_layout_20 = (LinearLayout) findViewById(R.id.vacant_bed_layout_20);
        createrooms_20 = (Button) findViewById(R.id.createrooms_20);
        no_bed_20 = (TextView) findViewById(R.id.no_bed_20);
        vacat_no_bed_20 = (TextView) findViewById(R.id.vacat_no_bed_20);


        lat_bath_selector_view_1 = (TextView) findViewById(R.id.lat_bath_selector_view_1);
        lat_bath_selector_layout_1 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_1);
        lat_bath_selector_view_2 = (TextView) findViewById(R.id.lat_bath_selector_view_2);
        lat_bath_selector_layout_2 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_2);
        lat_bath_selector_view_3 = (TextView) findViewById(R.id.lat_bath_selector_view_3);
        lat_bath_selector_layout_3 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_3);
        lat_bath_selector_view_4 = (TextView) findViewById(R.id.lat_bath_selector_view_4);
        lat_bath_selector_layout_4 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_4);
        lat_bath_selector_view_5 = (TextView) findViewById(R.id.lat_bath_selector_view_5);
        lat_bath_selector_layout_5 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_5);
        lat_bath_selector_view_6 = (TextView) findViewById(R.id.lat_bath_selector_view_6);
        lat_bath_selector_layout_6 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_6);
        lat_bath_selector_view_7 = (TextView) findViewById(R.id.lat_bath_selector_view_7);
        lat_bath_selector_layout_7 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_7);
        lat_bath_selector_view_8 = (TextView) findViewById(R.id.lat_bath_selector_view_8);
        lat_bath_selector_layout_8 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_8);
        lat_bath_selector_view_9 = (TextView) findViewById(R.id.lat_bath_selector_view_9);
        lat_bath_selector_layout_9 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_9);
        lat_bath_selector_view_10 = (TextView) findViewById(R.id.lat_bath_selector_view_10);
        lat_bath_selector_layout_10 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_10);
        lat_bath_selector_view_11 = (TextView) findViewById(R.id.lat_bath_selector_view_11);
        lat_bath_selector_layout_11 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_11);
        lat_bath_selector_view_12 = (TextView) findViewById(R.id.lat_bath_selector_view_12);
        lat_bath_selector_layout_12 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_12);
        lat_bath_selector_view_13 = (TextView) findViewById(R.id.lat_bath_selector_view_13);
        lat_bath_selector_layout_13 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_13);
        lat_bath_selector_view_14 = (TextView) findViewById(R.id.lat_bath_selector_view_14);
        lat_bath_selector_layout_14 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_14);
        lat_bath_selector_view_15 = (TextView) findViewById(R.id.lat_bath_selector_view_15);
        lat_bath_selector_layout_15 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_15);
        lat_bath_selector_view_16 = (TextView) findViewById(R.id.lat_bath_selector_view_16);
        lat_bath_selector_layout_16 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_16);
        lat_bath_selector_view_17 = (TextView) findViewById(R.id.lat_bath_selector_view_17);
        lat_bath_selector_layout_17 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_17);
        lat_bath_selector_view_18 = (TextView) findViewById(R.id.lat_bath_selector_view_18);
        lat_bath_selector_layout_18 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_18);
        lat_bath_selector_view_19 = (TextView) findViewById(R.id.lat_bath_selector_view_19);
        lat_bath_selector_layout_19 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_19);
        lat_bath_selector_view_20 = (TextView) findViewById(R.id.lat_bath_selector_view_20);
        lat_bath_selector_layout_20 = (LinearLayout) findViewById(R.id.lat_bath_selector_layout_20);



        createRoomsApi(propertyid, startpoint);

        bedselection_layout_1.setOnClickListener(this);
        bedselection_layout_2.setOnClickListener(this);
        bedselection_layout_3.setOnClickListener(this);
        bedselection_layout_4.setOnClickListener(this);
        bedselection_layout_5.setOnClickListener(this);
        bedselection_layout_6.setOnClickListener(this);
        bedselection_layout_7.setOnClickListener(this);
        bedselection_layout_8.setOnClickListener(this);
        bedselection_layout_9.setOnClickListener(this);
        bedselection_layout_10.setOnClickListener(this);
        bedselection_layout_11.setOnClickListener(this);
        bedselection_layout_12.setOnClickListener(this);
        bedselection_layout_13.setOnClickListener(this);
        bedselection_layout_14.setOnClickListener(this);
        bedselection_layout_15.setOnClickListener(this);
        bedselection_layout_16.setOnClickListener(this);
        bedselection_layout_17.setOnClickListener(this);
        bedselection_layout_18.setOnClickListener(this);
        bedselection_layout_19.setOnClickListener(this);
        bedselection_layout_20.setOnClickListener(this);
        vacant_bed_layout_1.setOnClickListener(this);
        vacant_bed_layout_2.setOnClickListener(this);
        vacant_bed_layout_3.setOnClickListener(this);
        vacant_bed_layout_4.setOnClickListener(this);
        vacant_bed_layout_5.setOnClickListener(this);
        vacant_bed_layout_6.setOnClickListener(this);
        vacant_bed_layout_7.setOnClickListener(this);
        vacant_bed_layout_8.setOnClickListener(this);
        vacant_bed_layout_9.setOnClickListener(this);
        vacant_bed_layout_10.setOnClickListener(this);
        vacant_bed_layout_11.setOnClickListener(this);
        vacant_bed_layout_12.setOnClickListener(this);
        vacant_bed_layout_13.setOnClickListener(this);
        vacant_bed_layout_14.setOnClickListener(this);
        vacant_bed_layout_15.setOnClickListener(this);
        vacant_bed_layout_16.setOnClickListener(this);
        vacant_bed_layout_17.setOnClickListener(this);
        vacant_bed_layout_18.setOnClickListener(this);
        vacant_bed_layout_19.setOnClickListener(this);
        vacant_bed_layout_20.setOnClickListener(this);
        lat_bath_selector_layout_1.setOnClickListener(this);
        lat_bath_selector_layout_2.setOnClickListener(this);
        lat_bath_selector_layout_3.setOnClickListener(this);
        lat_bath_selector_layout_4.setOnClickListener(this);
        lat_bath_selector_layout_5.setOnClickListener(this);
        lat_bath_selector_layout_6.setOnClickListener(this);
        lat_bath_selector_layout_7.setOnClickListener(this);
        lat_bath_selector_layout_8.setOnClickListener(this);
        lat_bath_selector_layout_9.setOnClickListener(this);
        lat_bath_selector_layout_10.setOnClickListener(this);
        lat_bath_selector_layout_11.setOnClickListener(this);
        lat_bath_selector_layout_12.setOnClickListener(this);
        lat_bath_selector_layout_13.setOnClickListener(this);
        lat_bath_selector_layout_14.setOnClickListener(this);
        lat_bath_selector_layout_15.setOnClickListener(this);
        lat_bath_selector_layout_16.setOnClickListener(this);
        lat_bath_selector_layout_17.setOnClickListener(this);
        lat_bath_selector_layout_18.setOnClickListener(this);
        lat_bath_selector_layout_19.setOnClickListener(this);
        lat_bath_selector_layout_20.setOnClickListener(this);

        createrooms_1.setOnClickListener(this);
        createrooms_2.setOnClickListener(this);
        createrooms_3.setOnClickListener(this);
        createrooms_4.setOnClickListener(this);
        createrooms_5.setOnClickListener(this);
        createrooms_6.setOnClickListener(this);
        createrooms_7.setOnClickListener(this);
        createrooms_8.setOnClickListener(this);
        createrooms_9.setOnClickListener(this);
        createrooms_10.setOnClickListener(this);
        createrooms_11.setOnClickListener(this);
        createrooms_12.setOnClickListener(this);
        createrooms_13.setOnClickListener(this);
        createrooms_14.setOnClickListener(this);
        createrooms_15.setOnClickListener(this);
        createrooms_16.setOnClickListener(this);
        createrooms_17.setOnClickListener(this);
        createrooms_18.setOnClickListener(this);
        createrooms_19.setOnClickListener(this);
        createrooms_20.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent mIntent = new Intent(RoomGenerateMainActivity.this, PropertyRoomsCreationActivity.class);
                mIntent.putExtra("PropertyId", propertyid);
                mIntent.putExtra("countvalue", countvalue);
                mIntent.putExtra("for_whom", forwhom);
                startActivity(mIntent);

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bedselection_layout_1:
                showDialog_bed(no_bed_1, vacat_no_bed_1);
                break;

            case R.id.bedselection_layout_2:
                showDialog_bed(no_bed_2, vacat_no_bed_2);
                break;

            case R.id.bedselection_layout_3:
                showDialog_bed(no_bed_3, vacat_no_bed_3);
                break;

            case R.id.bedselection_layout_4:
                showDialog_bed(no_bed_4, vacat_no_bed_4);
                break;

            case R.id.bedselection_layout_5:
                showDialog_bed(no_bed_5, vacat_no_bed_5);
                break;

            case R.id.bedselection_layout_6:
                showDialog_bed(no_bed_6, vacat_no_bed_6);
                break;

            case R.id.bedselection_layout_7:
                showDialog_bed(no_bed_7, vacat_no_bed_7);
                break;

            case R.id.bedselection_layout_8:
                showDialog_bed(no_bed_8, vacat_no_bed_8);
                break;

            case R.id.bedselection_layout_9:
                showDialog_bed(no_bed_9, vacat_no_bed_9);
                break;

            case R.id.bedselection_layout_10:
                showDialog_bed(no_bed_10, vacat_no_bed_10);
                break;

            case R.id.bedselection_layout_11:
                showDialog_bed(no_bed_11, vacat_no_bed_11);
                break;

            case R.id.bedselection_layout_12:
                showDialog_bed(no_bed_12, vacat_no_bed_12);
                break;

            case R.id.bedselection_layout_13:
                showDialog_bed(no_bed_13, vacat_no_bed_13);
                break;

            case R.id.bedselection_layout_14:
                showDialog_bed(no_bed_14, vacat_no_bed_14);
                break;

            case R.id.bedselection_layout_15:
                showDialog_bed(no_bed_15, vacat_no_bed_15);
                break;

            case R.id.bedselection_layout_16:
                showDialog_bed(no_bed_16, vacat_no_bed_16);
                break;

            case R.id.bedselection_layout_17:
                showDialog_bed(no_bed_17, vacat_no_bed_17);
                break;

            case R.id.bedselection_layout_18:
                showDialog_bed(no_bed_18, vacat_no_bed_18);
                break;

            case R.id.bedselection_layout_19:
                showDialog_bed(no_bed_19, vacat_no_bed_19);
                break;

            case R.id.bedselection_layout_20:
                showDialog_bed(no_bed_20, vacat_no_bed_20);
                break;

            case R.id.vacant_bed_layout_1:
                showVacantDialog(no_bed_1, vacat_no_bed_1);
                break;
            case R.id.vacant_bed_layout_2:
                showVacantDialog(no_bed_2, vacat_no_bed_2);
                break;
            case R.id.vacant_bed_layout_3:
                showVacantDialog(no_bed_3, vacat_no_bed_3);
                break;
            case R.id.vacant_bed_layout_4:
                showVacantDialog(no_bed_4, vacat_no_bed_4);
                break;
            case R.id.vacant_bed_layout_5:
                showVacantDialog(no_bed_5, vacat_no_bed_5);
                break;
            case R.id.vacant_bed_layout_6:
                showVacantDialog(no_bed_6, vacat_no_bed_6);
                break;
            case R.id.vacant_bed_layout_7:
                showVacantDialog(no_bed_7, vacat_no_bed_7);
                break;
            case R.id.vacant_bed_layout_8:
                showVacantDialog(no_bed_8, vacat_no_bed_8);
                break;
            case R.id.vacant_bed_layout_9:
                showVacantDialog(no_bed_9, vacat_no_bed_9);
                break;
            case R.id.vacant_bed_layout_10:
                showVacantDialog(no_bed_10, vacat_no_bed_10);
                break;
            case R.id.vacant_bed_layout_11:
                showVacantDialog(no_bed_11, vacat_no_bed_11);
                break;
            case R.id.vacant_bed_layout_12:
                showVacantDialog(no_bed_12, vacat_no_bed_12);
                break;
            case R.id.vacant_bed_layout_13:
                showVacantDialog(no_bed_13, vacat_no_bed_13);
                break;
            case R.id.vacant_bed_layout_14:
                showVacantDialog(no_bed_14, vacat_no_bed_14);
                break;
            case R.id.vacant_bed_layout_15:
                showVacantDialog(no_bed_15, vacat_no_bed_15);
                break;
            case R.id.vacant_bed_layout_16:
                showVacantDialog(no_bed_16, vacat_no_bed_16);
                break;
            case R.id.vacant_bed_layout_17:
                showVacantDialog(no_bed_17, vacat_no_bed_17);
                break;
            case R.id.vacant_bed_layout_18:
                showVacantDialog(no_bed_18, vacat_no_bed_18);
                break;
            case R.id.vacant_bed_layout_19:
                showVacantDialog(no_bed_19, vacat_no_bed_19);
                break;
            case R.id.vacant_bed_layout_20:
                showVacantDialog(no_bed_20, vacat_no_bed_20);
                break;

            case R.id.lat_bath_selector_layout_1:
                //showDialog_Lath(lat_bath_selector_view_1);
                showFalicity(txtfacility1);
                break;
            case R.id.lat_bath_selector_layout_2:
                //showDialog_Lath(lat_bath_selector_view_2);
                showFalicity(txtfacility2);
                break;
            case R.id.lat_bath_selector_layout_3:
               // showDialog_Lath(lat_bath_selector_view_3);
                showFalicity(txtfacility3);
                break;
            case R.id.lat_bath_selector_layout_4:
                //showDialog_Lath(lat_bath_selector_view_4);
                showFalicity(txtfacility4);
                break;
            case R.id.lat_bath_selector_layout_5:
               // showDialog_Lath(lat_bath_selector_view_5);
                showFalicity(txtfacility5);
                break;
            case R.id.lat_bath_selector_layout_6:
                showFalicity(txtfacility6);
                break;
            case R.id.lat_bath_selector_layout_7:
                showFalicity(txtfacility7);
                break;
            case R.id.lat_bath_selector_layout_8:
                showFalicity(txtfacility8);
                break;
            case R.id.lat_bath_selector_layout_9:
                showFalicity(txtfacility9);
                break;
            case R.id.lat_bath_selector_layout_10:
                showFalicity(txtfacility10);
                break;
            case R.id.lat_bath_selector_layout_11:
                showFalicity(txtfacility11);
                break;
            case R.id.lat_bath_selector_layout_12:
                showFalicity(txtfacility12);
                break;
            case R.id.lat_bath_selector_layout_13:
                showFalicity(txtfacility13);
                break;
            case R.id.lat_bath_selector_layout_14:
                showFalicity(txtfacility14);
                break;
            case R.id.lat_bath_selector_layout_15:
                showFalicity(txtfacility15);
                break;
            case R.id.lat_bath_selector_layout_16:
                showFalicity(txtfacility16);
                break;
            case R.id.lat_bath_selector_layout_17:
                showFalicity(txtfacility17);
                break;
            case R.id.lat_bath_selector_layout_18:
                showFalicity(txtfacility18);
                break;
            case R.id.lat_bath_selector_layout_19:
                showFalicity(txtfacility19);
                break;
            case R.id.lat_bath_selector_layout_20:
                showFalicity(txtfacility20);
                break;

            case R.id.createrooms_1:
                String textviewroom_1 = room_no_id_1.getText().toString();
                String roomno_value_1 = roomNo_input_1.getText().toString();
                String amount_value_1 = amount_of_bed_1.getText().toString();
                String vacant_value_1 = vacat_no_bed_1.getText().toString();
                String Lathbath_value_1 = lat_bath_selector_view_1.getText().toString();
                String bedvalue_1 = no_bed_1.getText().toString();
                String[] roomnoparticulat = textviewroom_1.split(" ");
                String roomno1 = roomnoparticulat[1];
                 String facility1= txtfacility1.getText().toString();

                if (bedvalue_1.equals("")) {
                    bedvalue_1 = "0";
                }

                if (vacant_value_1.equals("")) {
                    vacant_value_1 = "0";
                }
                if (Lathbath_value_1.equals("")) {
                    Lathbath_value_1 = "No";
                }

                saveRoomno(roomno1, roomno_value_1, amount_value_1, vacant_value_1, Lathbath_value_1, bedvalue_1,facility1);
                break;

            case R.id.createrooms_2:
                String textviewroom_2 = room_no_id_2.getText().toString();
                String roomno_value_2 = roomNo_input_2.getText().toString();
                String amount_value_2 = amount_of_bed_2.getText().toString();
                String vacant_value_2 = vacat_no_bed_2.getText().toString();
                String Lathbath_value_2 = lat_bath_selector_view_2.getText().toString();
                String bedvalue_2 = no_bed_2.getText().toString();
                String[] roomnoparticulat2 = textviewroom_2.split(" ");
                String roomno2 = roomnoparticulat2[1];
                String facility2= txtfacility2.getText().toString();

                if (bedvalue_2.equals("")) {
                    bedvalue_2 = "0";
                }

                if (vacant_value_2.equals("")) {
                    vacant_value_2 = "0";
                }
                if (Lathbath_value_2.equals("")) {
                    Lathbath_value_2 = "No";
                }
                // Toast.makeText(RoomGenerateMainActivity.this,Lathbath_value_2, Toast.LENGTH_SHORT).show();
                saveRoomno(roomno2, roomno_value_2, amount_value_2, vacant_value_2, Lathbath_value_2, bedvalue_2, facility2);
                break;

            case R.id.createrooms_3:
                String textviewroom_3 = room_no_id_3.getText().toString();
                String roomno_value_3 = roomNo_input_3.getText().toString();
                String amount_value_3 = amount_of_bed_3.getText().toString();
                String vacant_value_3 = vacat_no_bed_3.getText().toString();
                String Lathbath_value_3 = lat_bath_selector_view_3.getText().toString();
                String bedvalue_3 = no_bed_3.getText().toString();
                String[] roomnoparticulat3 = textviewroom_3.split(" ");
                String roomno3 = roomnoparticulat3[1];
                String facility3= txtfacility3.getText().toString();

                if (bedvalue_3.equals("")) {
                    bedvalue_3 = "0";
                }

                if (vacant_value_3.equals("")) {
                    vacant_value_3 = "0";
                }
                if (Lathbath_value_3.equals("")) {
                    Lathbath_value_3 = "No";
                }

                saveRoomno(roomno3, roomno_value_3, amount_value_3, vacant_value_3, Lathbath_value_3, bedvalue_3, facility3);
                break;

            case R.id.createrooms_4:
                String textviewroom_4 = room_no_id_4.getText().toString();
                String roomno_value_4 = roomNo_input_4.getText().toString();
                String amount_value_4 = amount_of_bed_4.getText().toString();
                String vacant_value_4 = vacat_no_bed_4.getText().toString();
                String Lathbath_value_4 = lat_bath_selector_view_4.getText().toString();
                String[] roomnoparticulat4 = textviewroom_4.split(" ");
                String roomno4 = roomnoparticulat4[1];
                String bedvalue_4 = no_bed_4.getText().toString();
                String facility4= txtfacility4.getText().toString();


                if (bedvalue_4.equals("")) {
                    bedvalue_4 = "0";
                }

                if (vacant_value_4.equals("")) {
                    vacant_value_4 = "0";
                }
                if (Lathbath_value_4.equals("")) {
                    Lathbath_value_4 = "No";
                }

                saveRoomno(roomno4, roomno_value_4, amount_value_4, vacant_value_4, Lathbath_value_4, bedvalue_4, facility4);
                break;

            case R.id.createrooms_5:
                String textviewroom_5 = room_no_id_5.getText().toString();
                String roomno_value_5 = roomNo_input_5.getText().toString();
                String amount_value_5 = amount_of_bed_5.getText().toString();
                String vacant_value_5 = vacat_no_bed_5.getText().toString();
                String Lathbath_value_5 = lat_bath_selector_view_5.getText().toString();
                String[] roomnoparticulat5 = textviewroom_5.split(" ");
                String roomno5 = roomnoparticulat5[1];
                String bedvalue_5 = no_bed_5.getText().toString();
                String facility5= txtfacility5.getText().toString();


                if (bedvalue_5.equals("")) {
                    bedvalue_5 = "0";
                }

                if (vacant_value_5.equals("")) {
                    vacant_value_5 = "0";
                }
                if (Lathbath_value_5.equals("")) {
                    Lathbath_value_5 = "No";
                }

                saveRoomno(roomno5, roomno_value_5, amount_value_5, vacant_value_5, Lathbath_value_5, bedvalue_5, facility5);
                break;

            case R.id.createrooms_6:
                String textviewroom_6 = room_no_id_6.getText().toString();
                String roomno_value_6 = roomNo_input_6.getText().toString();
                String amount_value_6 = amount_of_bed_6.getText().toString();
                String vacant_value_6 = vacat_no_bed_6.getText().toString();
                String Lathbath_value_6 = lat_bath_selector_view_6.getText().toString();
                String[] roomnoparticulat6 = textviewroom_6.split(" ");
                String roomno6 = roomnoparticulat6[1];
                String bedvalue_6 = no_bed_6.getText().toString();
                String facility6= txtfacility6.getText().toString();


                if (bedvalue_6.equals("")) {
                    bedvalue_6 = "0";
                }

                if (vacant_value_6.equals("")) {
                    vacant_value_6 = "0";
                }
                if (Lathbath_value_6.equals("")) {
                    Lathbath_value_6 = "No";
                }

                saveRoomno(roomno6, roomno_value_6, amount_value_6, vacant_value_6, Lathbath_value_6, bedvalue_6, facility6);
                break;

            case R.id.createrooms_7:
                String textviewroom_7 = room_no_id_7.getText().toString();
                String roomno_value_7 = roomNo_input_7.getText().toString();
                String amount_value_7 = amount_of_bed_7.getText().toString();
                String vacant_value_7 = vacat_no_bed_7.getText().toString();
                String Lathbath_value_7 = lat_bath_selector_view_7.getText().toString();
                String[] roomnoparticula7 = textviewroom_7.split(" ");
                String roomno7 = roomnoparticula7[1];
                String bedvalue_7 = no_bed_7.getText().toString();
                String facility7= txtfacility7.getText().toString();


                if (bedvalue_7.equals("")) {
                    bedvalue_7 = "0";
                }

                if (vacant_value_7.equals("")) {
                    vacant_value_7 = "0";
                }
                if (Lathbath_value_7.equals("")) {
                    Lathbath_value_7 = "No";
                }

                saveRoomno(roomno7, roomno_value_7, amount_value_7, vacant_value_7, Lathbath_value_7, bedvalue_7, facility7);
                break;

            case R.id.createrooms_8:
                String textviewroom_8 = room_no_id_8.getText().toString();
                String roomno_value_8 = roomNo_input_8.getText().toString();
                String amount_value_8 = amount_of_bed_8.getText().toString();
                String vacant_value_8 = vacat_no_bed_8.getText().toString();
                String Lathbath_value_8 = lat_bath_selector_view_8.getText().toString();
                String[] roomnoparticulat8 = textviewroom_8.split(" ");
                String roomno8 = roomnoparticulat8[1];
                String bedvalue_8 = no_bed_8.getText().toString();
                String facility8= txtfacility8.getText().toString();


                if (bedvalue_8.equals("")) {
                    bedvalue_8 = "0";
                }

                if (vacant_value_8.equals("")) {
                    vacant_value_8 = "0";
                }
                if (Lathbath_value_8.equals("")) {
                    Lathbath_value_8 = "No";
                }

                saveRoomno(roomno8, roomno_value_8, amount_value_8, vacant_value_8, Lathbath_value_8, bedvalue_8, facility8);
                break;

            case R.id.createrooms_9:
                String textviewroom_9 = room_no_id_9.getText().toString();
                String roomno_value_9 = roomNo_input_9.getText().toString();
                String amount_value_9 = amount_of_bed_9.getText().toString();
                String vacant_value_9 = vacat_no_bed_9.getText().toString();
                String Lathbath_value_9 = lat_bath_selector_view_9.getText().toString();
                String[] roomnoparticulat9 = textviewroom_9.split(" ");
                String roomno9 = roomnoparticulat9[1];
                String bedvalue_9 = no_bed_9.getText().toString();
                String facility9= txtfacility9.getText().toString();


                if (bedvalue_9.equals("")) {
                    bedvalue_9 = "0";
                }

                if (vacant_value_9.equals("")) {
                    vacant_value_9 = "0";
                }
                if (Lathbath_value_9.equals("")) {
                    Lathbath_value_9 = "No";
                }

                saveRoomno(roomno9, roomno_value_9, amount_value_9, vacant_value_9, Lathbath_value_9, bedvalue_9, facility9);
                break;

            case R.id.createrooms_10:
                String textviewroom_10 = room_no_id_10.getText().toString();
                String roomno_value_10 = roomNo_input_10.getText().toString();
                String amount_value_10 = amount_of_bed_10.getText().toString();
                String vacant_value_10 = vacat_no_bed_10.getText().toString();
                String Lathbath_value_10 = lat_bath_selector_view_10.getText().toString();
                String[] roomnoparticulat10 = textviewroom_10.split(" ");
                String roomno10 = roomnoparticulat10[1];
                String bedvalue_10 = no_bed_10.getText().toString();
                String facility10= txtfacility10.getText().toString();


                if (bedvalue_10.equals("")) {
                    bedvalue_10 = "0";
                }

                if (vacant_value_10.equals("")) {
                    vacant_value_10 = "0";
                }
                if (Lathbath_value_10.equals("")) {
                    Lathbath_value_10 = "No";
                }

                saveRoomno(roomno10, roomno_value_10, amount_value_10, vacant_value_10, Lathbath_value_10, bedvalue_10, facility10);
                break;

            case R.id.createrooms_11:
                String textviewroom_11 = room_no_id_11.getText().toString();
                String roomno_value_11 = roomNo_input_11.getText().toString();
                String amount_value_11 = amount_of_bed_11.getText().toString();
                String vacant_value_11 = vacat_no_bed_11.getText().toString();
                String Lathbath_value_11 = lat_bath_selector_view_11.getText().toString();
                String[] roomnoparticulat11 = textviewroom_11.split(" ");
                String roomno11 = roomnoparticulat11[1];
                String bedvalue_11 = no_bed_11.getText().toString();
                String facility11= txtfacility11.getText().toString();

                if (bedvalue_11.equals("")) {
                    bedvalue_11 = "0";
                }

                if (vacant_value_11.equals("")) {
                    vacant_value_11 = "0";
                }
                if (Lathbath_value_11.equals("")) {
                    Lathbath_value_11 = "No";
                }

                saveRoomno(roomno11, roomno_value_11, amount_value_11, vacant_value_11, Lathbath_value_11, bedvalue_11, facility11);
                break;

            case R.id.createrooms_12:
                String textviewroom_12 = room_no_id_12.getText().toString();
                String roomno_value_12 = roomNo_input_12.getText().toString();
                String amount_value_12 = amount_of_bed_12.getText().toString();
                String vacant_value_12 = vacat_no_bed_12.getText().toString();
                String Lathbath_value_12 = lat_bath_selector_view_12.getText().toString();
                String[] roomnoparticulat12 = textviewroom_12.split(" ");
                String roomno12 = roomnoparticulat12[1];
                String bedvalue_12 = no_bed_12.getText().toString();
                String facility12= txtfacility12.getText().toString();
                if (bedvalue_12.equals("")) {
                    bedvalue_12 = "0";
                }

                if (vacant_value_12.equals("")) {
                    vacant_value_12 = "0";
                }
                if (Lathbath_value_12.equals("")) {
                    Lathbath_value_12 = "No";
                }

                saveRoomno(roomno12, roomno_value_12, amount_value_12, vacant_value_12, Lathbath_value_12, bedvalue_12, facility12);
                break;

            case R.id.createrooms_13:
                String textviewroom_13 = room_no_id_13.getText().toString();
                String roomno_value_13 = roomNo_input_13.getText().toString();
                String amount_value_13 = amount_of_bed_13.getText().toString();
                String vacant_value_13 = vacat_no_bed_13.getText().toString();
                String Lathbath_value_13 = lat_bath_selector_view_13.getText().toString();
                String[] roomnoparticulat13 = textviewroom_13.split(" ");
                String roomno13 = roomnoparticulat13[1];
                String bedvalue_13 = no_bed_13.getText().toString();
                String facility13= txtfacility13.getText().toString();

                if (bedvalue_13.equals("")) {
                    bedvalue_13 = "0";
                }

                if (vacant_value_13.equals("")) {
                    vacant_value_13 = "0";
                }
                if (Lathbath_value_13.equals("")) {
                    Lathbath_value_13 = "No";
                }

                saveRoomno(roomno13, roomno_value_13, amount_value_13, vacant_value_13, Lathbath_value_13, bedvalue_13, facility13);
                break;

            case R.id.createrooms_14:
                String textviewroom_14 = room_no_id_14.getText().toString();
                String roomno_value_14 = roomNo_input_14.getText().toString();
                String amount_value_14 = amount_of_bed_14.getText().toString();
                String vacant_value_14 = vacat_no_bed_14.getText().toString();
                String Lathbath_value_14 = lat_bath_selector_view_14.getText().toString();
                String[] roomnoparticulat14 = textviewroom_14.split(" ");
                String roomno14 = roomnoparticulat14[1];
                String bedvalue_14 = no_bed_14.getText().toString();
                String facility14= txtfacility14.getText().toString();

                if (bedvalue_14.equals("")) {
                    bedvalue_14 = "0";
                }

                if (vacant_value_14.equals("")) {
                    vacant_value_14 = "0";
                }
                if (Lathbath_value_14.equals("")) {
                    Lathbath_value_14 = "No";
                }

                saveRoomno(roomno14, roomno_value_14, amount_value_14, vacant_value_14, Lathbath_value_14, bedvalue_14, facility14);
                break;

            case R.id.createrooms_15:
                String textviewroom_15 = room_no_id_15.getText().toString();
                String roomno_value_15 = roomNo_input_15.getText().toString();
                String amount_value_15 = amount_of_bed_15.getText().toString();
                String vacant_value_15 = vacat_no_bed_15.getText().toString();
                String Lathbath_value_15 = lat_bath_selector_view_15.getText().toString();
                String[] roomnoparticulat15 = textviewroom_15.split(" ");
                String roomno15 = roomnoparticulat15[1];
                String bedvalue_15 = no_bed_15.getText().toString();
                String facility15= txtfacility15.getText().toString();

                if (bedvalue_15.equals("")) {
                    bedvalue_15 = "0";
                }

                if (vacant_value_15.equals("")) {
                    vacant_value_15 = "0";
                }
                if (Lathbath_value_15.equals("")) {
                    Lathbath_value_15 = "No";
                }

                saveRoomno(roomno15, roomno_value_15, amount_value_15, vacant_value_15, Lathbath_value_15, bedvalue_15, facility15);
                break;

            case R.id.createrooms_16:
                String textviewroom_16 = room_no_id_16.getText().toString();
                String roomno_value_16 = roomNo_input_16.getText().toString();
                String amount_value_16 = amount_of_bed_16.getText().toString();
                String vacant_value_16 = vacat_no_bed_16.getText().toString();
                String Lathbath_value_16 = lat_bath_selector_view_16.getText().toString();
                String[] roomnoparticulat16 = textviewroom_16.split(" ");
                String roomno16 = roomnoparticulat16[1];
                String bedvalue_16 = no_bed_16.getText().toString();
                String facility16= txtfacility16.getText().toString();

                if (bedvalue_16.equals("")) {
                    bedvalue_16 = "0";
                }

                if (vacant_value_16.equals("")) {
                    vacant_value_16 = "0";
                }
                if (Lathbath_value_16.equals("")) {
                    Lathbath_value_16 = "No";
                }

                saveRoomno(roomno16, roomno_value_16, amount_value_16, vacant_value_16, Lathbath_value_16, bedvalue_16, facility16);
                break;

            case R.id.createrooms_17:
                String textviewroom_17 = room_no_id_17.getText().toString();
                String roomno_value_17 = roomNo_input_17.getText().toString();
                String amount_value_17 = amount_of_bed_17.getText().toString();
                String vacant_value_17 = vacat_no_bed_17.getText().toString();
                String Lathbath_value_17 = lat_bath_selector_view_17.getText().toString();
                String[] roomnoparticulat17 = textviewroom_17.split(" ");
                String roomno17 = roomnoparticulat17[1];
                String bedvalue_17 = no_bed_17.getText().toString();
                String facility17= txtfacility17.getText().toString();

                if (bedvalue_17.equals("")) {
                    bedvalue_17 = "0";
                }

                if (vacant_value_17.equals("")) {
                    vacant_value_17 = "0";
                }
                if (Lathbath_value_17.equals("")) {
                    Lathbath_value_17 = "No";
                }

                saveRoomno(roomno17, roomno_value_17, amount_value_17, vacant_value_17, Lathbath_value_17, bedvalue_17, facility17);
                break;

            case R.id.createrooms_18:
                String textviewroom_18 = room_no_id_18.getText().toString();
                String roomno_value_18 = roomNo_input_18.getText().toString();
                String amount_value_18 = amount_of_bed_18.getText().toString();
                String vacant_value_18 = vacat_no_bed_18.getText().toString();
                String Lathbath_value_18 = lat_bath_selector_view_18.getText().toString();
                String[] roomnoparticulat18 = textviewroom_18.split(" ");
                String roomno18 = roomnoparticulat18[1];
                String bedvalue_18 = no_bed_18.getText().toString();
                String facility18= txtfacility18.getText().toString();

                if (bedvalue_18.equals("")) {
                    bedvalue_18 = "0";
                }

                if (vacant_value_18.equals("")) {
                    vacant_value_18 = "0";
                }
                if (Lathbath_value_18.equals("")) {
                    Lathbath_value_18 = "No";
                }

                saveRoomno(roomno18, roomno_value_18, amount_value_18, vacant_value_18, Lathbath_value_18, bedvalue_18, facility18);
                break;

            case R.id.createrooms_19:
                String textviewroom_19 = room_no_id_19.getText().toString();
                String roomno_value_19 = roomNo_input_19.getText().toString();
                String amount_value_19 = amount_of_bed_19.getText().toString();
                String vacant_value_19 = vacat_no_bed_19.getText().toString();
                String Lathbath_value_19 = lat_bath_selector_view_19.getText().toString();
                String[] roomnoparticulat19 = textviewroom_19.split(" ");
                String roomno19 = roomnoparticulat19[1];
                String bedvalue_19 = no_bed_19.getText().toString();
                String facility19= txtfacility19.getText().toString();

                if (bedvalue_19.equals("")) {
                    bedvalue_19 = "0";
                }

                if (vacant_value_19.equals("")) {
                    vacant_value_19 = "0";
                }
                if (Lathbath_value_19.equals("")) {
                    Lathbath_value_19 = "No";
                }

                saveRoomno(roomno19, roomno_value_19, amount_value_19, vacant_value_19, Lathbath_value_19, bedvalue_19, facility19);
                break;

            case R.id.createrooms_20:
                String textviewroom_20 = room_no_id_20.getText().toString();
                String roomno_value_20 = roomNo_input_20.getText().toString();
                String amount_value_20 = amount_of_bed_20.getText().toString();
                String vacant_value_20 = vacat_no_bed_20.getText().toString();
                String Lathbath_value_20 = lat_bath_selector_view_20.getText().toString();
                String[] roomnoparticulat20 = textviewroom_20.split(" ");
                String roomno20 = roomnoparticulat20[1];
                String bedvalue_20 = no_bed_20.getText().toString();
                String facility20= txtfacility20.getText().toString();

                if (bedvalue_20.equals("")) {
                    bedvalue_20 = "0";
                }

                if (vacant_value_20.equals("")) {
                    vacant_value_20 = "0";
                }
                if (Lathbath_value_20.equals("")) {
                    Lathbath_value_20 = "No";
                }

                saveRoomno(roomno20, roomno_value_20, amount_value_20, vacant_value_20, Lathbath_value_20, bedvalue_20, facility20);
                break;

        }
    }

    private void showFalicity(final TextView txtfacility) {


        mSelectedItems.clear();
        mainValue = "";
        AlertDialog.Builder ab = new AlertDialog.Builder(RoomGenerateMainActivity.this);
        ab.setTitle("Please Select Facility")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(items[which]);
                        } else if (mSelectedItems.contains(items[which])) {
                            mSelectedItems.remove(items[which]);
                        }
                    }

                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog


                for (int i = 0; i < mSelectedItems.size(); i++) {
                    if (mainValue.equals("")) {
                        mainValue = mSelectedItems.get(i).toString();
                    } else {
                        mainValue = mainValue + "," + mSelectedItems.get(i).toString();
                    }
                }

                if (mainValue.equals("")) {

                    Toast.makeText(RoomGenerateMainActivity.this, "Please Select Facility", Toast.LENGTH_SHORT).show();

                } else {

                    txtfacility.setText(mainValue);
                }

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        ab.create();
        ;
        ab.show();
    }


    public void saveRoomno(final String sequenceno, final String roomno_value, final String amount_value, final String vacant_value, final String Lathbath_value, final String bedvalue, final String facility) {
        //Toast.makeText(context,"sequenceno -"+sequenceno+"roomno_value -" + roomno_value + "amount_value -" + amount_value+ "vacant_value -" + vacant_value+"Lathbath_value -"+Lathbath_value+ "bedvalue -"+bedvalue, Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(RoomGenerateMainActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = WebUrls.MAIN_URL;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        String result = response.toString();
                        getResponsedataApi(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.getMessage() != null) {
                            Toast.makeText(context, "error VOLLEY " + error.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "addPropertyRoosSeprate");
                params.put("sequenceno", sequenceno);
                params.put("roomno", roomno_value);
                params.put("amount", amount_value);
                params.put("vacant", vacant_value);
                params.put("Lathbath", Lathbath_value);
                params.put("bed", bedvalue);
                params.put("property_id", propertyid);
                params.put("facility", facility);

                return params;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(strRequest);
    }

    public void getResponsedataApi(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            result = jo.getString("flag");
            String message = jo.getString("message");

            if (result.equalsIgnoreCase("Y")) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            Log.d("result..", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showDialog_bed(final TextView no_bed_view, final TextView vacat_no_bed) {
        final Dialog showDialog_bed = new Dialog(RoomGenerateMainActivity.this);
        showDialog_bed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showDialog_bed.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) showDialog_bed.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Bed");
        ListView bed_list = (ListView) showDialog_bed.findViewById(R.id.list_country);
        ImageView student_loader = (ImageView) showDialog_bed.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) showDialog_bed.findViewById(R.id.country_cross_layout);

        final ArrayList<OwnerStudentNameModel> ownerstudent_arraylist = new ArrayList<>();
        OwnerStudentNameModel model = new OwnerStudentNameModel();
        model.setTanentusername("1");
        ownerstudent_arraylist.add(model);
        OwnerStudentNameModel model1 = new OwnerStudentNameModel();
        model1.setTanentusername("2");
        ownerstudent_arraylist.add(model1);
        OwnerStudentNameModel model2 = new OwnerStudentNameModel();
        model2.setTanentusername("3");
        ownerstudent_arraylist.add(model2);
        OwnerStudentNameModel model3 = new OwnerStudentNameModel();
        model3.setTanentusername("4");
        ownerstudent_arraylist.add(model3);

        RoomspinnerAdapter adapter_data_month = new RoomspinnerAdapter(RoomGenerateMainActivity.this, ownerstudent_arraylist);
        bed_list.setAdapter(adapter_data_month);

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_bed.dismiss();
            }
        });
        bed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Toast.makeText(context, "" + ownerstudent_arraylist.get(position).getTanentusername().toString(), Toast.LENGTH_SHORT).show();
                vacat_no_bed.setText("");
                String vacantbedvalue = ownerstudent_arraylist.get(position).getTanentusername().toString();
                no_bed_view.setText(vacantbedvalue);
                showDialog_bed.dismiss();
            }
        });
        showDialog_bed.show();
    }

    public void showVacantDialog(final TextView no_bed_view, final TextView vacat_no_bed_view) {
        final Dialog vacantbed = new Dialog(RoomGenerateMainActivity.this);
        vacantbed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vacantbed.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) vacantbed.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Vacant Bed");
        ListView vacantbed_list = (ListView) vacantbed.findViewById(R.id.list_country);
        ImageView student_loader = (ImageView) vacantbed.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) vacantbed.findViewById(R.id.country_cross_layout);

        final ArrayList<OwnerStudentNameModel> ownerstudent_arraylist = new ArrayList<>();

        String roombed = no_bed_view.getText().toString();
        if (roombed.equals("1")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);

        } else if (roombed.equals("2")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);
            OwnerStudentNameModel model1 = new OwnerStudentNameModel();
            model1.setTanentusername("2");
            ownerstudent_arraylist.add(model1);

        } else if (roombed.equals("3")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);
            OwnerStudentNameModel model1 = new OwnerStudentNameModel();
            model1.setTanentusername("2");
            ownerstudent_arraylist.add(model1);
            OwnerStudentNameModel model2 = new OwnerStudentNameModel();
            model2.setTanentusername("3");
            ownerstudent_arraylist.add(model2);
        } else if (roombed.equals("4")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);
            OwnerStudentNameModel model1 = new OwnerStudentNameModel();
            model1.setTanentusername("2");
            ownerstudent_arraylist.add(model1);
            OwnerStudentNameModel model2 = new OwnerStudentNameModel();
            model2.setTanentusername("3");
            ownerstudent_arraylist.add(model2);
            OwnerStudentNameModel model3 = new OwnerStudentNameModel();
            model3.setTanentusername("4");
            ownerstudent_arraylist.add(model3);

        } else {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("Vacant Bed");
            ownerstudent_arraylist.add(model);
        }

        RoomspinnerAdapter adapter_data_month = new RoomspinnerAdapter(RoomGenerateMainActivity.this, ownerstudent_arraylist);
        vacantbed_list.setAdapter(adapter_data_month);

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vacantbed.dismiss();
            }
        });
        vacantbed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String vacantbedvalue = ownerstudent_arraylist.get(position).getTanentusername().toString();
                vacat_no_bed_view.setText(vacantbedvalue);
                vacantbed.dismiss();
            }
        });
        vacantbed.show();
    }

    public void showDialog_Lath(final TextView latbath_view) {
        final Dialog Dialog_Lath_dialog = new Dialog(RoomGenerateMainActivity.this);
        Dialog_Lath_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog_Lath_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) Dialog_Lath_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Lath Bath");
        ListView bed_list = (ListView) Dialog_Lath_dialog.findViewById(R.id.list_country);
        ImageView student_loader = (ImageView) Dialog_Lath_dialog.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) Dialog_Lath_dialog.findViewById(R.id.country_cross_layout);

        final ArrayList<OwnerStudentNameModel> ownerstudent_arraylist = new ArrayList<>();
        OwnerStudentNameModel model = new OwnerStudentNameModel();
        model.setTanentusername("Yes");
        ownerstudent_arraylist.add(model);
        OwnerStudentNameModel model1 = new OwnerStudentNameModel();
        model1.setTanentusername("No");
        ownerstudent_arraylist.add(model1);

        RoomspinnerAdapter adapter_data_month = new RoomspinnerAdapter(RoomGenerateMainActivity.this, ownerstudent_arraylist);
        bed_list.setAdapter(adapter_data_month);

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Lath_dialog.dismiss();
            }
        });
        bed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Toast.makeText(context, "" + ownerstudent_arraylist.get(position).getTanentusername().toString(), Toast.LENGTH_SHORT).show();
                String latbath_viewvalue = ownerstudent_arraylist.get(position).getTanentusername().toString();
                latbath_view.setText(latbath_viewvalue);
                Dialog_Lath_dialog.dismiss();
            }
        });
        Dialog_Lath_dialog.show();
    }

    public void createRoomsApi(final String propertyid, final int startpoint) {
        final ProgressDialog progressDialog = new ProgressDialog(RoomGenerateMainActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url = WebUrls.MAIN_URL;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        getResponsedata(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(RoomGenerateMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "showRoomDetails");
                params.put("property_id", propertyid);
                params.put("startpoint", String.valueOf(startpoint));
                return params;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(RoomGenerateMainActivity.this);
        queue.add(strRequest);
    }

    public void getResponsedata(String result) {
        try {
            roomnoarraylist = new ArrayList<>();
            JSONObject jo = new JSONObject(result);
            result = jo.getString("flag");
            String message = jo.getString("message");

            if (result.equalsIgnoreCase("Y")) {
                //{"flag":"Y","records":[{"id":"1179","sequenceroomno":"21","property_id":"10","room_no":"21","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1180","sequenceroomno":"22","property_id":"10","room_no":"22","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1181","sequenceroomno":"23","property_id":"10","room_no":"23","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1182","sequenceroomno":"24","property_id":"10","room_no":"24","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1183","sequenceroomno":"25","property_id":"10","room_no":"25","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1184","sequenceroomno":"26","property_id":"10","room_no":"26","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1185","sequenceroomno":"27","property_id":"10","room_no":"27","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1186","sequenceroomno":"28","property_id":"10","room_no":"28","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1187","sequenceroomno":"29","property_id":"10","room_no":"29","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1188","sequenceroomno":"30","property_id":"10","room_no":"30","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1189","sequenceroomno":"31","property_id":"10","room_no":"31","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1190","sequenceroomno":"32","property_id":"10","room_no":"32","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1191","sequenceroomno":"33","property_id":"10","room_no":"33","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1192","sequenceroomno":"34","property_id":"10","room_no":"34","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1193","sequenceroomno":"35","property_id":"10","room_no":"35","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1194","sequenceroomno":"36","property_id":"10","room_no":"36","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1195","sequenceroomno":"37","property_id":"10","room_no":"37","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1196","sequenceroomno":"38","property_id":"10","room_no":"38","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1197","sequenceroomno":"39","property_id":"10","room_no":"39","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"},{"id":"1198","sequenceroomno":"40","property_id":"10","room_no":"40","room_type":"0","vacant":"0","amount":"0","lat_bath":"No","image":null,"rating_status":"0","created_date":"2017-08-04 06:18:35"}],"message":"Get Rooms !"}
                JSONArray json_main = jo.getJSONArray("records");
                for (int i = 0; i < json_main.length(); i++) {
                    JSONObject jobj = json_main.getJSONObject(i);
                    Roommodel roomnomodel = new Roommodel();
                    roomnomodel.setRoomno(jobj.getString("room_no"));
                    roomnomodel.setSequenceno("Room " + jobj.getString("sequenceroomno"));
                    roomnomodel.setLath_bath(jobj.getString("lat_bath"));
                    roomnomodel.setVacant_bed(jobj.getString("vacant"));
                    roomnomodel.setNobed(jobj.getString("room_type"));
                    roomnomodel.setAmount(jobj.getString("amount"));
                    roomnomodel.setAc(jobj.getString("ac"));
                    roomnomodel.setBalcony(jobj.getString("balcony"));
                    roomnomodel.setFacility(jobj.getString("facility"));
                    roomnoarraylist.add(roomnomodel);
                }

                if (roomnoarraylist.size() > 0) {

                    for (int i = 0; i < roomnoarraylist.size(); i++) {

                        String textviewroom = roomnoarraylist.get(i).getSequenceno();
                        String roomno_value = roomnoarraylist.get(i).getRoomno();
                        String amount_value = roomnoarraylist.get(i).getAmount();
                        String vacant_value = roomnoarraylist.get(i).getVacant_bed();
                        String Lathbath_value = roomnoarraylist.get(i).getLath_bath();
                        String ac = roomnoarraylist.get(i).getAc();
                        String balcony = roomnoarraylist.get(i).getBalcony();
                        String bedvalue = roomnoarraylist.get(i).getNobed();
                        String facility = roomnoarraylist.get(i).getFacility();
                        if (bedvalue.equalsIgnoreCase("0")) {
                            bedvalue = "";
                        }

                        if (vacant_value.equalsIgnoreCase("0")) {
                            vacant_value = "";
                        }

                        if (amount_value.equalsIgnoreCase("0")) {
                            amount_value = "";
                        }

                        if (Lathbath_value.equalsIgnoreCase("0")) {
                            Lathbath_value = "";
                        } else {

                            //facility="Let Bath";
                        }

                        String newfacility = "";
                        if (facility.equalsIgnoreCase("0"))//""
                        {
                            Lathbath_value = "";
                        } else {

                            //facility="Let Bath";
                        }

                        if (i == 0) {
                            room_layout_1.setVisibility(View.VISIBLE);
                            room_no_id_1.setText(textviewroom);
                            roomNo_input_1.setText(roomno_value);
                            amount_of_bed_1.setText(amount_value);
                            vacat_no_bed_1.setText(vacant_value);
                            txtfacility1.setText(facility);
                            no_bed_1.setText(bedvalue);
                        } else if (i == 1) {
                            room_layout_2.setVisibility(View.VISIBLE);
                            room_no_id_2.setText(textviewroom);
                            roomNo_input_2.setText(roomno_value);
                            amount_of_bed_2.setText(amount_value);
                            vacat_no_bed_2.setText(vacant_value);
                            txtfacility2.setText(facility);
                            no_bed_2.setText(bedvalue);
                        } else if (i == 2) {
                            room_layout_3.setVisibility(View.VISIBLE);
                            room_no_id_3.setText(textviewroom);
                            roomNo_input_3.setText(roomno_value);
                            amount_of_bed_3.setText(amount_value);
                            vacat_no_bed_3.setText(vacant_value);
                            txtfacility3.setText(facility);
                            no_bed_3.setText(bedvalue);
                        } else if (i == 3) {
                            room_layout_4.setVisibility(View.VISIBLE);
                            room_no_id_4.setText(textviewroom);
                            roomNo_input_4.setText(roomno_value);
                            amount_of_bed_4.setText(amount_value);
                            vacat_no_bed_4.setText(vacant_value);
                            txtfacility4.setText(facility);
                            no_bed_4.setText(bedvalue);
                        } else if (i == 4) {
                            room_layout_5.setVisibility(View.VISIBLE);
                            room_no_id_5.setText(textviewroom);
                            roomNo_input_5.setText(roomno_value);
                            amount_of_bed_5.setText(amount_value);
                            vacat_no_bed_5.setText(vacant_value);
                            txtfacility5.setText(facility);
                            no_bed_5.setText(bedvalue);
                        } else if (i == 5) {
                            room_layout_6.setVisibility(View.VISIBLE);
                            room_no_id_6.setText(textviewroom);
                            roomNo_input_6.setText(roomno_value);
                            amount_of_bed_6.setText(amount_value);
                            vacat_no_bed_6.setText(vacant_value);
                            txtfacility6.setText(facility);
                            no_bed_6.setText(bedvalue);
                        } else if (i == 6) {
                            room_layout_7.setVisibility(View.VISIBLE);
                            room_no_id_7.setText(textviewroom);
                            roomNo_input_7.setText(roomno_value);
                            amount_of_bed_7.setText(amount_value);
                            vacat_no_bed_7.setText(vacant_value);
                            txtfacility7.setText(facility);
                            no_bed_7.setText(bedvalue);
                        } else if (i == 7) {
                            room_layout_8.setVisibility(View.VISIBLE);
                            room_no_id_8.setText(textviewroom);
                            roomNo_input_8.setText(roomno_value);
                            amount_of_bed_8.setText(amount_value);
                            vacat_no_bed_8.setText(vacant_value);
                            txtfacility8.setText(facility);
                            no_bed_8.setText(bedvalue);
                        } else if (i == 8) {
                            room_layout_9.setVisibility(View.VISIBLE);
                            room_no_id_9.setText(textviewroom);
                            roomNo_input_9.setText(roomno_value);
                            amount_of_bed_9.setText(amount_value);
                            vacat_no_bed_9.setText(vacant_value);
                            txtfacility9.setText(facility);
                            no_bed_9.setText(bedvalue);
                        } else if (i == 9) {
                            room_layout_10.setVisibility(View.VISIBLE);
                            room_no_id_10.setText(textviewroom);
                            roomNo_input_10.setText(roomno_value);
                            amount_of_bed_10.setText(amount_value);
                            vacat_no_bed_10.setText(vacant_value);
                            txtfacility10.setText(facility);
                            no_bed_10.setText(bedvalue);
                        } else if (i == 10) {
                            room_layout_11.setVisibility(View.VISIBLE);
                            room_no_id_11.setText(textviewroom);
                            roomNo_input_11.setText(roomno_value);
                            amount_of_bed_11.setText(amount_value);
                            vacat_no_bed_11.setText(vacant_value);
                            txtfacility11.setText(facility);
                            no_bed_11.setText(bedvalue);
                        } else if (i == 11) {
                            room_layout_12.setVisibility(View.VISIBLE);
                            room_no_id_12.setText(textviewroom);
                            roomNo_input_12.setText(roomno_value);
                            amount_of_bed_12.setText(amount_value);
                            vacat_no_bed_12.setText(vacant_value);
                            txtfacility12.setText(facility);
                            no_bed_12.setText(bedvalue);
                        } else if (i == 12) {
                            room_layout_13.setVisibility(View.VISIBLE);
                            room_no_id_13.setText(textviewroom);
                            roomNo_input_13.setText(roomno_value);
                            amount_of_bed_13.setText(amount_value);
                            vacat_no_bed_13.setText(vacant_value);
                            txtfacility13.setText(facility);
                            no_bed_13.setText(bedvalue);
                        } else if (i == 13) {
                            room_layout_14.setVisibility(View.VISIBLE);
                            room_no_id_14.setText(textviewroom);
                            roomNo_input_14.setText(roomno_value);
                            amount_of_bed_14.setText(amount_value);
                            vacat_no_bed_14.setText(vacant_value);
                            txtfacility14.setText(facility);
                            no_bed_14.setText(bedvalue);
                        } else if (i == 14) {
                            room_layout_15.setVisibility(View.VISIBLE);
                            room_no_id_15.setText(textviewroom);
                            roomNo_input_15.setText(roomno_value);
                            amount_of_bed_15.setText(amount_value);
                            vacat_no_bed_15.setText(vacant_value);
                            txtfacility15.setText(facility);
                            no_bed_15.setText(bedvalue);
                        } else if (i == 15) {
                            room_layout_16.setVisibility(View.VISIBLE);
                            room_no_id_16.setText(textviewroom);
                            roomNo_input_16.setText(roomno_value);
                            amount_of_bed_16.setText(amount_value);
                            vacat_no_bed_16.setText(vacant_value);
                            txtfacility16.setText(facility);
                            no_bed_16.setText(bedvalue);
                        } else if (i == 16) {
                            room_layout_17.setVisibility(View.VISIBLE);
                            room_no_id_17.setText(textviewroom);
                            roomNo_input_17.setText(roomno_value);
                            amount_of_bed_17.setText(amount_value);
                            vacat_no_bed_17.setText(vacant_value);
                            txtfacility17.setText(facility);
                            no_bed_17.setText(bedvalue);
                        } else if (i == 17) {
                            room_layout_18.setVisibility(View.VISIBLE);
                            room_no_id_18.setText(textviewroom);
                            roomNo_input_18.setText(roomno_value);
                            amount_of_bed_18.setText(amount_value);
                            vacat_no_bed_18.setText(vacant_value);
                            txtfacility18.setText(facility);
                            no_bed_18.setText(bedvalue);
                        } else if (i == 18) {
                            room_layout_19.setVisibility(View.VISIBLE);
                            room_no_id_19.setText(textviewroom);
                            roomNo_input_19.setText(roomno_value);
                            amount_of_bed_19.setText(amount_value);
                            vacat_no_bed_19.setText(vacant_value);
                            txtfacility19.setText(facility);
                            no_bed_19.setText(bedvalue);
                        } else if (i == 19) {
                            room_layout_20.setVisibility(View.VISIBLE);
                            room_no_id_20.setText(textviewroom);
                            roomNo_input_20.setText(roomno_value);
                            amount_of_bed_20.setText(amount_value);
                            vacat_no_bed_20.setText(vacant_value);
                            txtfacility20.setText(facility);
                            no_bed_20.setText(bedvalue);
                        }


                    }

                }

                //  Toast.makeText(RoomGenerateActivity.this,message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(RoomGenerateMainActivity.this, message, Toast.LENGTH_LONG).show();
            }
            Log.d("result..", result);
        } catch (JSONException e) {
            e.printStackTrace();

            AppExeptionFile.handleUncaughtException(e);

        }
    }


}
