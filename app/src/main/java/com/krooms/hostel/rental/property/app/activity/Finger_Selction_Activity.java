package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;
import com.krooms.hostel.rental.property.app.database.DatabaseHandlerStartak_Sdk;

/**
 * Created by admin on 6/3/2017.
 */
public class Finger_Selction_Activity extends Activity {

    RadioGroup radioGroup;
    RadioButton radio1, radio2, radio3, radio4, radio5, radio6;
    String finger_value = "";
    Button btnok;
    String uid, uname, propertyid, keys, roomno, type, devicetype = "";
    RelativeLayout back;
    SQLiteDatabase sq;
    private SQLiteDatabase db, database;
    int cnt = 0;
    int count = 0;
    ImageView thumb_layout, index_layout;
    boolean status = false;
    Context context;
    DatabaseHandler handlerdb;
    DatabaseHandlerStartak_Sdk databaseHandlerStartak_sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        handlerdb = new DatabaseHandler(context);
        db = handlerdb.getWritableDatabase();
        databaseHandlerStartak_sdk = new DatabaseHandlerStartak_Sdk(context);
        database = databaseHandlerStartak_sdk.getWritableDatabase();
        Intent ii = getIntent();
        uid = ii.getStringExtra("ID");
        uname = ii.getStringExtra("NAME");
        propertyid = ii.getStringExtra("propertyid");
        keys = ii.getStringExtra("Akey");
        roomno = ii.getStringExtra("Roomno");
        devicetype = ii.getStringExtra("device");

        if (devicetype.equalsIgnoreCase("Secugen")) {
            //new changes on 04/12/2018
            setContentView(R.layout.finger_selection_startek_layout);
            radio1 = (RadioButton) findViewById(R.id.radio1);
            radio2 = (RadioButton) findViewById(R.id.radio2);
            btnok = (Button) findViewById(R.id.btnok);
            back = (RelativeLayout) findViewById(R.id.flback_button);
            thumb_layout = (ImageView) findViewById(R.id.thumb_layout);
            index_layout = (ImageView) findViewById(R.id.index_layout);

        } else {
            setContentView(R.layout.finger_selection_startek_layout);
            radio1 = (RadioButton) findViewById(R.id.radio1);
            radio2 = (RadioButton) findViewById(R.id.radio2);
            btnok = (Button) findViewById(R.id.btnok);
            back = (RelativeLayout) findViewById(R.id.flback_button);
            thumb_layout = (ImageView) findViewById(R.id.thumb_layout);
            index_layout = (ImageView) findViewById(R.id.index_layout);
        }


        if (devicetype.equalsIgnoreCase("Secugen")) {

            Cursor cs = null;
            try {

                cs = db.rawQuery("select * from persons where userid='" + uid + "'", null);
                cnt = cs.getCount();
                if (cnt > 0) {
                    while (cs.moveToNext()) {
                        String Thumb_index;

                        Thumb_index = cs.getString(4);

                        if (Thumb_index != null) {
                            if (Thumb_index.equals("1")) {
                                thumb_layout.setBackgroundResource(R.drawable.green_right);
                            }
                        }
                        if (Thumb_index != null) {

                            if (Thumb_index.equals("2")) {
                                index_layout.setBackgroundResource(R.drawable.green_right);
                                status = true;
                            }
                        }
                    }

                }

                if (status == true) {

                    if (keys.equals("Selection")) {

                    } else if (keys.equals("Home")) {
                        Toast.makeText(Finger_Selction_Activity.this, "Thank You For Enrollment", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Finger_Selction_Activity.this, Tenant_Records_FirstActivity.class);
                        intent.putExtra("propertyid", propertyid);
                        intent.putExtra("device", devicetype);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {

            } finally {
                if (cs != null)
                    cs.close();
            }
        } else {


            Cursor cs = null;
            try {
                cs = database.rawQuery("select * from persons where userid='" + uid + "'", null);
                count = cs.getCount();
                if (count > 0) {
                    while (cs.moveToNext()) {
                        String Thumb_index;
                        Thumb_index = cs.getString(4);
                        if (Thumb_index != null) {
                            if (Thumb_index.equals("1")) {
                                thumb_layout.setBackgroundResource(R.drawable.green_right);
                            }
                        }
                        if (Thumb_index != null) {

                            if (Thumb_index.equals("2")) {
                                index_layout.setBackgroundResource(R.drawable.green_right);
                                status = true;
                            }
                        }
                    }
                }

                if (status == true) {

                    if (keys.equals("Selection")) {

                    } else if (keys.equals("Home")) {
                        Toast.makeText(Finger_Selction_Activity.this, "Thank You For Enrollment", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Finger_Selction_Activity.this, Tenant_Records_FirstActivity.class);
                        intent.putExtra("propertyid", propertyid);
                        intent.putExtra("device", devicetype);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
            } finally {
                if (cs != null)
                    cs.close();
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii = new Intent(Finger_Selction_Activity.this, Tenant_Records_FirstActivity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", devicetype);
                startActivity(ii);

            }
        });


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finger_value.equals("")) {
                    Toast.makeText(Finger_Selction_Activity.this, "Please Select Atleast One Finger", Toast.LENGTH_SHORT).show();
                } else {

                    if (devicetype.equalsIgnoreCase("Secugen")) {
                        Intent ii = new Intent(Finger_Selction_Activity.this, Home_activity.class);
                        ii.putExtra("ID", uid);
                        ii.putExtra("NAME", uname);
                        ii.putExtra("Finger", finger_value);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("Roomno", roomno);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);
                    } else {

                        Intent ii = new Intent(Finger_Selction_Activity.this, Capture_ImageActivity_Startek.class);
                        ii.putExtra("ID", uid);
                        ii.putExtra("NAME", uname);
                        ii.putExtra("Finger", finger_value);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("Roomno", roomno);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);
                    }

                }
            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (devicetype.equalsIgnoreCase("Secugen")) {
                    radio1.setChecked(true);
                    radio2.setChecked(false);
                    finger_value = "1";
                } else {

                    radio1.setChecked(true);
                    radio2.setChecked(false);
                    finger_value = "1";
                }

            }
        });


        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (devicetype.equalsIgnoreCase("Secugen")) {
                    radio1.setChecked(false);
                    radio2.setChecked(true);
                    finger_value = "2";
                } else {
                    radio1.setChecked(false);
                    radio2.setChecked(true);
                    finger_value = "2";
                }

            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent ii = new Intent(Finger_Selction_Activity.this, Tenant_Records_FirstActivity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", devicetype);
                startActivity(ii);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}