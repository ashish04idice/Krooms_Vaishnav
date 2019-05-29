package com.krooms.hostel.rental.property.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 8/24/2017.
 */
public class DatabaseHandlerStartak_Sdk extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "KroomsStartak";
    private static final String TABLE_RECORD = "persons";
    private static final String TABLE_OWNER = "ownerfingerprint";
    private static final String TABLE_ATTENDANCE = "krooms_attendance";
    private static final String TABLE_FOODATTENDANCE = "krooms_foodattendance";
    private static final String TABLE_NIGHTATTENDANCE = "krooms_nightattendance";
    private static final String TABLE_PHOTO = "tenant_photo";
    private static final String TABLE_ADS = "krooms_ads";
    public DatabaseHandlerStartak_Sdk(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //timedate 24 hours time date format
        db.execSQL("CREATE TABLE IF NOT EXISTS persons(userid VARCHAR,name VARCHAR,propertyid VARCHAR,thumbfinger VARCHAR,thumbindex VARCHAR,roomno VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS ownerfingerprint(propertyid VARCHAR,thumbfinger VARCHAR,thumbindex VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS krooms_attendance(id INTEGER PRIMARY KEY AUTOINCREMENT,tenantid VARCHAR,propertyid VARCHAR,name VARCHAR,attendance VARCHAR,attendancestatus VARCHAR,date VARCHAR,time VARCHAR,timeinterval VARCHAR,status VARCHAR,roomno VARCHAR,timenew VARCHAR,timedate VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS krooms_foodattendance(id INTEGER PRIMARY KEY AUTOINCREMENT,tenantid VARCHAR,propertyid VARCHAR,name VARCHAR,attendance VARCHAR,attendancestatus VARCHAR,date VARCHAR,time VARCHAR,timeinterval VARCHAR,status VARCHAR,roomno VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS krooms_nightattendance(id INTEGER PRIMARY KEY AUTOINCREMENT,tenantid VARCHAR,propertyid VARCHAR,name VARCHAR,attendance VARCHAR,attendancestatus VARCHAR,date VARCHAR,time VARCHAR,timeinterval VARCHAR,status VARCHAR,roomno VARCHAR,timenew VARCHAR,timedate VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS tenant_photo(tenantid INTEGER PRIMARY KEY,propertyid VARCHAR,photo VARCHAR,status VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS krooms_ads(id INTEGER PRIMARY KEY AUTOINCREMENT,propertyid VARCHAR,name VARCHAR,adstype VARCHAR,adimage VARCHAR,shortorder INTEGER,status VARCHAR);");
        //added by perminder sir for db first speed//15/04/2019
        db.execSQL("CREATE  INDEX  IF NOT EXISTS idx_krooms_attendance_1 ON krooms_attendance (propertyid,tenantid,attendancestatus);");
        db.execSQL("CREATE  INDEX  IF NOT EXISTS idx_krooms_attendance_2 ON krooms_attendance (propertyid,attendancestatus);");
        db.execSQL("CREATE  INDEX  IF NOT EXISTS idx_tenant_photo_1 ON tenant_photo (propertyid,tenantid);");
        db.execSQL("CREATE  INDEX  IF NOT EXISTS idx_persons_1 ON persons (propertyid);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NIGHTATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS);
        onCreate(db);

    }


}
