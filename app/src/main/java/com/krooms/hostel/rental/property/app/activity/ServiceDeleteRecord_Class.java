package com.krooms.hostel.rental.property.app.activity;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;

import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.database.DatabaseHandler;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 2/2/2018.
 */

public class ServiceDeleteRecord_Class extends Service {

    private SQLiteDatabase db;
    private Cursor c;
    DatabaseHandler handlerdb;
    boolean status=false,statusfood=false,statusnight=false;

    String propertyid="";
    private SharedStorage mSharedStorage;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerdb = new DatabaseHandler(ServiceDeleteRecord_Class.this);
        db = handlerdb.getWritableDatabase();
        mSharedStorage = SharedStorage.getInstance(this);
        propertyid = mSharedStorage.getUserPropertyId();
        deleteRecord();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void deleteRecord(){
        Cursor c=null,cc=null,c1=null;
        try {
            String query = "Select * from krooms_attendance order by id DESC LIMIT 0,1";
            c1 = db.rawQuery(query, null);
            String datedb = "";

            if (c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    do {
                        datedb = c1.getString(6);
                    } while (c1.moveToNext());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.US);

                Date date = null;
                String olddate = null;
                try {
                    date = sdf.parse(datedb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE,-5);
                Date yesterday = calendar.getTime();
                olddate = sdf.format(yesterday);
                String query1 = "Select * from krooms_attendance where date='"+olddate+"' order by id DESC LIMIT 0,1 ";
                cc = db.rawQuery(query1,null);
                String id = "";
                if (cc.getCount() != 0) {
                    if (cc.moveToFirst()) {
                        do {
                            id=cc.getString(0);
                        } while (cc.moveToNext());
                    }
                    status=true;
                } else {
                    status=false;
                }

                if(status==true) {
                    String queryselect = "select * from krooms_attendance where id <='" + id + "' ";
                    c = db.rawQuery(queryselect, null);
                    try {
                        File dir = new File(Environment.getExternalStorageDirectory(),"_Krooms_Exception");
                        if (dir.isDirectory())
                        {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(dir, children[i]).delete();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(c.getCount()!=0) {
                        String querydelete = "delete from krooms_attendance where id <='" + id + "' ";
                        db.execSQL(querydelete);
                    }
                }
                else {
                }

            } else {
            }

            deleteFoodRecord();
        }catch (Exception e){

            AppError.handleUncaughtException(e," ServiceDeleteRecord_Class_Secugen - deleteRecord",propertyid, "");

        }finally {
            if (cc != null)
                cc.close();

            if (c!= null)
                c.close();

            if (c1!= null)
                c1.close();
        }


    }
    private void deleteFoodRecord() {


        Cursor c=null,cc=null,c1=null;
        try {
            String query = "Select * from krooms_foodattendance order by id DESC LIMIT 0,1 ";
            c = db.rawQuery(query, null);
            String id = "", datedb = "";
            //
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        datedb=c.getString(6);
                    } while (c.moveToNext());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
                Date date = null;
                String olddate = null;
                try {
                    date = sdf.parse(datedb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, -2);
                Date yesterday = calendar.getTime();
                olddate = sdf.format(yesterday);

                String query1 = "Select * from krooms_foodattendance where date='"+olddate+"' order by id DESC LIMIT 0,1 ";

                cc = db.rawQuery(query1, null);
                String id1 = "";
                if (cc.getCount() != 0) {
                    if (cc.moveToFirst()) {
                        do {
                            id1=cc.getString(0);
                        } while (cc.moveToNext());
                    }
                    statusfood=true;
                } else {
                    statusfood=false;
                }


                if(statusfood==true) {

                    String queryselect = "select * from krooms_foodattendance where id <='" + id1 + "' ";
                    c1 = db.rawQuery(queryselect, null);

                    try {
                        File dir = new File(Environment.getExternalStorageDirectory(),"_Krooms_Exception");
                        if (dir.isDirectory())
                        {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(dir, children[i]).delete();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    
                    if(c1.getCount()!=0) {
                        String querydelete = "delete from krooms_foodattendance where id <='" + id1 + "' ";
                        db.execSQL(querydelete);
                    }
                } else
                {
                }

            } else {
            }

            deleteRecordNight();

        }catch (Exception e){

            AppError.handleUncaughtException(e," ServiceDeleteRecord_Class_Secugen - deleteFoodRecord",propertyid, "");

        }finally {
            if (c1 != null)
                c1.close();

            if (cc != null)
                cc.close();

            if(c!=null)
                c.close();
        }

         }

    private void deleteRecordNight(){

        Cursor c=null,cc=null;
        try {
            String query = "Select * from krooms_nightattendance order by id DESC LIMIT 0,1";
            c = db.rawQuery(query, null);
            String datedb = "";
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        datedb = c.getString(6);
                    } while (c.moveToNext());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.US);

                Date date = null;
                String olddate = null;
                try {
                    date = sdf.parse(datedb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE,-2);
                Date yesterday = calendar.getTime();
                olddate = sdf.format(yesterday);
                String query1 = "Select * from krooms_nightattendance where date='"+olddate+"' order by id DESC LIMIT 0,1 ";
                 cc = db.rawQuery(query1,null);
                String id = "";
                if (cc.getCount() != 0) {
                    if (cc.moveToFirst()) {
                        do {
                            id=cc.getString(0);
                        } while (cc.moveToNext());
                    }
                    statusnight=true;
                } else {
                    statusnight=false;
                }

                if(statusnight==true) {
                    String queryselect = "select * from krooms_nightattendance where id <='" + id + "' ";
                    c = db.rawQuery(queryselect, null);
                    if(c.getCount()!=0) {
                        String querydelete = "delete from krooms_nightattendance where id <='" + id + "' ";
                        db.execSQL(querydelete);
                    }
                }
                else {
                }

            } else {
            }

        }catch (Exception e){
            AppError.handleUncaughtException(e," ServiceDeleteRecord_Class_Secugen - deleteRecordNight",propertyid, "");
        }
        finally {
            if (c != null)
                c.close();
            if (cc != null)
                cc.close();
        }


    }
}
