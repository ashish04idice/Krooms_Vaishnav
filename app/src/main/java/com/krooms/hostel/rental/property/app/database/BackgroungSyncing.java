package com.krooms.hostel.rental.property.app.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.asynctask.AddPropertyImageServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.AddPropertyRoomsAsynctask;
import com.krooms.hostel.rental.property.app.asynctask.UserIdProofServiceAsynctTask;
import com.krooms.hostel.rental.property.app.callback.RoomServiceResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("HandlerLeak")
public class BackgroungSyncing implements RoomServiceResponce {

    private Context mContext;
    private Activity mActivity = null;
    private Timer timer = null;
    private Validation mValidation = null;
    private SharedStorage mSharedStorage = null;

    public BackgroungSyncing(Activity activity) {
        this.mContext = activity;
        this.mActivity = activity;
        // Start Database Syncing
        this.mValidation = new Validation(mContext);
        mSharedStorage = SharedStorage.getInstance(mContext);
    }

    public void startBGSyncing() {
        if (timer == null) {
            // Declare the timer
            timer = new Timer();
            // Set the schedule function and rate
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // Called each time when 1000 milliseconds (1 second) (the period parameter)
                            LogConfig.logd("Timer scheduler ", "BackgroungSyncing syncing.........");
                            handler.sendEmptyMessage(0);

                        } // Set how long before to start calling the TimerTask (in milliseconds)
                    }, 0,// Set the amount of time between each execution (in milliseconds)
                    120000); // 120000 run after every 15 minutes 900000; 2 min
        }
    }

    public void stopBGSyncing() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {

           /* try {

                if (mValidation.checkNetworkRechability()) {
                    if (Common.isDatabaseSyncingInProgress && Common._isDatabaseSyncingFromBackground) {
                        Common.isDatabaseSyncingInProgress = false;
                        LogConfig.logd("BackGround Syncking", "Data BackGroundSync Start");

                        callPropertyImagesService();

                        *//*callRoomsService();
                        callUserIdProofImagesService();*//*
                    } else {
                        LogConfig.logd("BackGround Syncking", "Allready DataBase BackGroundSync running");
                    }

                } else {
                    LogConfig.logd("BackGround Syncking", "No network connection");
                    Common._isDatabaseSyncingFromBackground = true;
                }
            } catch (NullPointerException e) {
                LogConfig.logd("BackGround Syncking NullPointerException =", "" + e.getMessage());
            }*/
        }

    };

    public void callRoomsService() {

        DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        Cursor cursor = mDBAdapter.getSqlqureies_new("Select *from add_rooms where status='0'");


        if (cursor.getCount() > 0) {

            if (!mSharedStorage.getRoomInfoUploadInProccess()) {
                mSharedStorage.setRoomInfoUploadInProccess(true);
                mSharedStorage.setRCUImageUploadInProccess(false);
                String propertyId = cursor.getString(cursor.getColumnIndex("property_id"));
                String roomId = cursor.getString(cursor.getColumnIndex("room_id"));
                String noOfBad = cursor.getString(cursor.getColumnIndex("no_of_bads"));
                String vacantBad = cursor.getString(cursor.getColumnIndex("vacant_bad"));
                if (vacantBad.equals("Vacant Bed")) {
                    if (!noOfBad.equals("Bed")) {
                        vacantBad = noOfBad;
                    } else {
                        vacantBad = vacantBad;
                    }
                } else {
                    vacantBad = vacantBad;
                }
                String amount = cursor.getString(cursor.getColumnIndex("room_amunt"));
                String latbath = cursor.getString(cursor.getColumnIndex("room_lat_bath"));
                String image = cursor.getString(cursor.getColumnIndex("room_image"));
                String roomNo = cursor.getString(cursor.getColumnIndex("room_number"));
                mDBAdapter.close();

                if (roomId == null) {
                    roomId = "";
                }
                AddPropertyRoomsAsynctask mSyncDatabaseTask = new AddPropertyRoomsAsynctask(mActivity);
                mSyncDatabaseTask.setCallBack(BackgroungSyncing.this);
                mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        propertyId, roomNo, noOfBad, vacantBad, amount, latbath, image, "", roomId);
            }

        } else {
            callUserIdProofImagesService();
        }
    }

    @Override
    public void requestRoomServiceResponce(String result) {

        LogConfig.logd("Property synking rooms response =", "" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                JSONArray jArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
                mDBAdapter.createDatabase();
                mDBAdapter.open();
                ContentValues cv = new ContentValues();
                cv.put("room_id", "" + jArray.getJSONObject(0).getString("room_id"));
                cv.put("room_image", jArray.getJSONObject(0).getString("room_pic"));
                cv.put("status", "1");
                mDBAdapter.updateRoomsInfo("add_rooms", cv,
                        jArray.getJSONObject(0).getString("property_id"),
                        jArray.getJSONObject(0).getString("room_no"));
                mDBAdapter.close();
            }
            Common.isDatabaseSyncingInProgress = true;
            mSharedStorage.setRoomInfoUploadInProccess(false);
            callRoomsService();
        } catch (JSONException e) {

        } catch (NullPointerException e) {
        }
    }

    public void callPropertyImagesService() {

        DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        Cursor cursorPropertyImage = mDBAdapter.getSqlqureies_new("Select *from property_images where status='0'");

        if (cursorPropertyImage.getCount() > 0) {

            if (!mSharedStorage.getPropertyImageUploadInProccess()) {
                mSharedStorage.setPropertyImageUploadInProccess(true);
                mSharedStorage.setRoomInfoUploadInProccess(false);
                mSharedStorage.setRCUImageUploadInProccess(false);
                String propertyId = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("property_id"));
                String userId = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("user_id"));
                String property_image = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("property_image"));
                String image_no = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("image_no"));

                String property_image_id = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("property_image_id"));
                if (property_image_id == null) {
                    property_image_id = "";
                }
                mDBAdapter.close();
                AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(mActivity);
                mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                    @Override
                    public void requestResponce(String result) {
                        responseParse(result);
                    }
                });
                mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        userId, propertyId, property_image, image_no, property_image_id);
            }
        }
    }

    public void callUserIdProofImagesService() {

        DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        Cursor cursorPropertyImage = mDBAdapter.getSqlqureiesIdProof("Select *from user_id_proof_images where status='0'");

        if (cursorPropertyImage.getCount() > 0) {
            Common.Config("cursor count       "+cursorPropertyImage.getCount()+"   "+mSharedStorage.getRCUImageUploadInProccess());
            /*if (!mSharedStorage.getRCUImageUploadInProccess()) {
                mSharedStorage.setRCUImageUploadInProccess(true);*/
                String userId = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("user_id"));
                String rcu_id = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("rcu_id"));
                String id_proof_type = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("id_proof_type"));
                String id_proof_image = cursorPropertyImage.getString(cursorPropertyImage.getColumnIndex("id_proof_image"));

                mDBAdapter.close();

                UserIdProofServiceAsynctTask mySyncDatabaseTask = new UserIdProofServiceAsynctTask(mActivity);
                mySyncDatabaseTask.setCallBack(new ServiceResponce() {
                    @Override
                    public void requestResponce(String result) {
                        userIdResponse(result);
                    }
                });

                mySyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        userId, rcu_id, id_proof_type.toLowerCase(), id_proof_image);
            }
        /*} else {
            mSharedStorage.setRCUImageUploadInProccess(false);
            mSharedStorage.setPropertyImageUploadInProccess(false);
            mSharedStorage.setRoomInfoUploadInProccess(false);

        }*/
        /*}*/
    }

    public void userIdResponse(String result) {
        Common.Config("Property image response =" + result);
        LogConfig.logd("Property image response =", "" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                JSONArray jArray = jsonObject.getJSONArray("data");
                DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
                mDBAdapter.createDatabase();
                mDBAdapter.open();
                ContentValues cv = new ContentValues();
                Cursor mCursor = mDBAdapter.getSqlqureiesIdProof("Select * from user_id_proof_images where status='0'");
                cv.put("id_proof_image", jArray.getJSONObject(0).getString("file_path"));
                cv.put("status", "1");
                if (mCursor.getCount() > 0) {
                    mDBAdapter.updateUserIdProofImagesInfo("user_id_proof_images", cv,
                            jArray.getJSONObject(0).getString("rcu_id"), jArray.getJSONObject(0).getString("id_proof_type"));
                }
                mDBAdapter.close();
            }
            Common.isDatabaseSyncingInProgress = true;
            mSharedStorage.setRCUImageUploadInProccess(false);
            callUserIdProofImagesService();

        } catch (JSONException e) {
            e.printStackTrace();
            mSharedStorage.setRCUImageUploadInProccess(false);
            callUserIdProofImagesService();
        } catch (NullPointerException e) {
            e.printStackTrace();
            mSharedStorage.setRCUImageUploadInProccess(false);
            callUserIdProofImagesService();
        }

    }


    public void responseParse(String result) {

        LogConfig.logd("Property image response =", "" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                JSONArray jArray = jsonObject.getJSONArray("data");
                DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
                mDBAdapter.createDatabase();
                mDBAdapter.open();
                ContentValues cv = new ContentValues();
                String image_id = jArray.getJSONObject(0).getString("id");
                Cursor mCursor = mDBAdapter.getSqlqureies_new("SELECT * FROM property_images " +
                        "where status='0'");
                cv.put("property_image", jArray.getJSONObject(0).getString("path"));
                cv.put("image_no", "" + jArray.getJSONObject(0).getString("image_number"));
                cv.put("status", "" + jArray.getJSONObject(0).getString("status"));

                if (mCursor.getCount() > 0) {

                    mDBAdapter.updatePropertyImagesInfo("property_images", cv,
                            jArray.getJSONObject(0).getString("property_id"), jArray.getJSONObject(0).getString("image_number"));
                }
                mDBAdapter.close();
            }
            Common.isDatabaseSyncingInProgress = true;
            mSharedStorage.setPropertyImageUploadInProccess(false);
            callPropertyImagesService();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}