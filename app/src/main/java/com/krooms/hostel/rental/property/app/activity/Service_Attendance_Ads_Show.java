package com.krooms.hostel.rental.property.app.activity;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.view.View;

import com.krooms.hostel.rental.property.app.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2/2/2018.
 */

public class Service_Attendance_Ads_Show extends Service {

    private SQLiteDatabase db;
    private Cursor c;
    public static Handler handler1;

    Timer timer = new Timer(); // changed
    int i = 0;

    Thread t;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler1 = new Handler();
        handler1.postDelayed(new Runnable() {

           // int i = 0;

            @Override
            public void run() {

                byte[] arrayOfByte = convertToStream(Get_Attendance_Startek.imagesads[i]);
                Bitmap bmp = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
                Get_Attendance_Startek.imageViewads.setImageBitmap(bmp);
                Get_Attendance_Startek.grid.setVisibility(View.GONE);
                Get_Attendance_Startek.attendanceads.setVisibility(View.VISIBLE);
                i++;
                if (i >Get_Attendance_Startek.imagesads.length-1) {
                    i = 0;
                    Get_Attendance_Startek.grid.setVisibility(View.VISIBLE);
                    Get_Attendance_Startek.attendanceads.setVisibility(View.GONE);
                }

            /*   for(int j=0;j<Get_Attendance_Startek.arraylistads.size();j++){

                int j=1;

                    if (Get_Attendance_Startek.arraylistads.get(j).getAdsurl() == null || Get_Attendance_Startek.arraylistads.get(j).getAdsurl().isEmpty() || Get_Attendance_Startek.arraylistads.get(j).getAdsurl().equals("null") || Get_Attendance_Startek.arraylistads.get(j).getAdsurl().equals("0")) {

                        Get_Attendance_Startek.imageViewads.setImageResource(R.drawable.user_xl);

                    } else {

                        Get_Attendance_Startek.grid.setVisibility(View.GONE);
                        Get_Attendance_Startek.attendanceads.setVisibility(View.VISIBLE);
                        byte[] arrayOfByte = convertToStream(Get_Attendance_Startek.arraylistads.get(j).getAdsurl());
                        Bitmap bmp = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
                        Get_Attendance_Startek.imageViewads.setImageBitmap(bmp);

                    }
               }*/

                   /* if (j > Get_Attendance_Startek.arraylistads.size()- 1) {
                        i = 0;
                        Get_Attendance_Startek.grid.setVisibility(View.VISIBLE);
                        Get_Attendance_Startek.attendanceads.setVisibility(View.GONE);
                    }*/

               /* Get_Attendance_Startek.imageViewads.setImageResource(Get_Attendance_Startek.imageArray[i]);
                Get_Attendance_Startek.grid.setVisibility(View.GONE);
                Get_Attendance_Startek.attendanceads.setVisibility(View.VISIBLE);
                i++;
                if (i > Get_Attendance_Startek.imageArray.length - 1) {
                    i = 0;
                    Get_Attendance_Startek.grid.setVisibility(View.VISIBLE);
                    Get_Attendance_Startek.attendanceads.setVisibility(View.GONE);
                }*/

                handler1.postDelayed(this, 10000);

            }
        }, 10000);//10000


/*        t = new Thread() {

            @Override
            public void run() {
                try {

                    Get_Attendance_Startek.imageViewads.setImageResource(Get_Attendance_Startek.imageArray[i]);

                    Get_Attendance_Startek.grid.setVisibility(View.GONE);
                    Get_Attendance_Startek.attendanceads.setVisibility(View.VISIBLE);

                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Get_Attendance_Startek.grid.setVisibility(View.VISIBLE);
                                Get_Attendance_Startek.attendanceads.setVisibility(View.GONE);

                                i++;
                                if(i >Get_Attendance_Startek.imageArray.length-1){
                                    i = 0;
                                }}});}}
                catch (InterruptedException e) {
                }}

            private void runOnUiThread(Runnable runnable) {
            }
        };

t.start();*/
        return START_STICKY;
    }


    public byte[] convertToStream(String base64uid) {
        byte[] arrayOfByte = Base64.decode(base64uid, 2);
        return arrayOfByte;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
