package com.krooms.hostel.rental.property.app.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pc on 29/11/16.
 */
public class HelperAttedence extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "AttendenceTable.sqlite";


    public HelperAttedence(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Attendence(propertyid varchar(50),owner_id varchar(50)," +
                "tenant_id varchar(50),user_id varchar(50),room_id varchar(50)," +
                "room_no varchar(100),tenant_name varchar(250),attendence varchar(100)," +
                "date varchar(350),time varchar(200))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Attendence");
        onCreate(db);
    }
}
