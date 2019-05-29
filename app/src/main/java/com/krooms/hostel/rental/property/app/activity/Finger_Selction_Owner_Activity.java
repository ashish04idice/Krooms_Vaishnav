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
public class Finger_Selction_Owner_Activity extends Activity {


    RadioButton radio1, radio2;
    String finger_value = "";
    Button btnok;
    String uid, uname, propertyid, keys, roomno, type, devicetype = "";
    RelativeLayout back;
    private SQLiteDatabase database, db;
    int count = 0;
    ImageView thumb_layout, index_layout;
    boolean status = false;
    Context context;
    DatabaseHandler handlerdb;

    DatabaseHandlerStartak_Sdk databaseHandlerStartak_sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_selection_startek_layout);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        btnok = (Button) findViewById(R.id.btnok);
        back = (RelativeLayout) findViewById(R.id.flback_button);
        thumb_layout = (ImageView) findViewById(R.id.thumb_layout);
        index_layout = (ImageView) findViewById(R.id.index_layout);
        context = getApplicationContext();
        handlerdb = new DatabaseHandler(context);
        db = handlerdb.getWritableDatabase();
        databaseHandlerStartak_sdk = new DatabaseHandlerStartak_Sdk(context);
        database = databaseHandlerStartak_sdk.getWritableDatabase();
        Intent ii = getIntent();
        propertyid = ii.getStringExtra("propertyid");
        devicetype = ii.getStringExtra("device");
        keys = ii.getStringExtra("key");

        if (devicetype.equals("Secugen")) {
            Cursor cs = null;
            try {
                cs = db.rawQuery("select * from ownerfingerprint where propertyid='" + propertyid + "'", null);
                count = cs.getCount();
                if (count > 0) {
                    while (cs.moveToNext()) {
                        String Thumb_index;
                        Thumb_index = cs.getString(2);
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
                        Toast.makeText(Finger_Selction_Owner_Activity.this, "Thank You For Enrollment", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Finger_Selction_Owner_Activity.this, Attandance_Home_Activity.class);
                        intent.putExtra("propertyid", propertyid);
                        intent.putExtra("device", devicetype);
                        startActivity(intent);
                    }

                }
            } catch (Exception ee) {

            } finally {
                if (cs != null)
                    cs.close();
            }


        } else {
            Cursor cs = null;
            try {
                cs = database.rawQuery("select * from ownerfingerprint where propertyid='" + propertyid + "'", null);
                count = cs.getCount();
                if (count > 0) {
                    while (cs.moveToNext()) {
                        String Thumb_index;
                        Thumb_index = cs.getString(2);
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
                        Toast.makeText(Finger_Selction_Owner_Activity.this, "Thank You For Enrollment", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Finger_Selction_Owner_Activity.this, Attandance_Home_Activity.class);
                        intent.putExtra("propertyid", propertyid);
                        intent.putExtra("device", devicetype);
                        startActivity(intent);
                    }

                }
            } catch (Exception ee) {

            } finally {
                if (cs != null)
                    cs.close();
            }

        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii = new Intent(Finger_Selction_Owner_Activity.this, Attandance_Home_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", devicetype);
                startActivity(ii);

            }
        });


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finger_value.equals("")) {
                    Toast.makeText(Finger_Selction_Owner_Activity.this, "Please Select Atleast One Finger", Toast.LENGTH_SHORT).show();
                } else {

                    if (devicetype.equals("Secugen")) {
                        Intent ii = new Intent(Finger_Selction_Owner_Activity.this, Capture_ImageActivity_Owner_Secugen.class);
                        ii.putExtra("Finger", finger_value);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);
                    } else {
                        Intent ii = new Intent(Finger_Selction_Owner_Activity.this, Capture_ImageActivity_Owner_Startek.class);
                        ii.putExtra("Finger", finger_value);
                        ii.putExtra("propertyid", propertyid);
                        ii.putExtra("device", devicetype);
                        startActivity(ii);

                    }
                }
            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(true);
                radio2.setChecked(false);
                finger_value = "1";
            }
        });


        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radio1.setChecked(false);
                radio2.setChecked(true);
                finger_value = "2";
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent ii = new Intent(Finger_Selction_Owner_Activity.this, Attandance_Home_Activity.class);
                ii.putExtra("propertyid", propertyid);
                ii.putExtra("device", devicetype);
                startActivity(ii);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}