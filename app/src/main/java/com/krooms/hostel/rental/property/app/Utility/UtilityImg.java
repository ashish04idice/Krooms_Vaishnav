package com.krooms.hostel.rental.property.app.Utility;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;


/**
 * Created by asus on 02-09-2017.
 */
public class UtilityImg {

    public static final String TAG = UtilityImg.class.getSimpleName();
    public static File file;

    public static boolean isExternalStorageAvailable() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    public static Uri getExternalFilesDirForVersion24Above(@NonNull Context context, @NonNull String directoryPath,
                                                           @NonNull String folderName, @NonNull String fileName) {
        Uri uri = null;
        File mediaStorageDir = null;
        file = null;

        if (directoryPath.equals(Environment.DIRECTORY_PICTURES)) {

            mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folderName);

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdir()) {
                Log.d(TAG, "Not able to create directory");
            }
        }

        if (mediaStorageDir != null) {

            file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            Log.d("fileName", file + "");

            uri = FileProvider.getUriForFile(context, "com.krooms.hostel.rental.property.app", file);
            Log.d("uriName", uri + "");
        }
        return uri;
    }


    public static Uri getExternalFilesDirForVersion24Below(@NonNull Context context, @NonNull String directoryPath, @NonNull String folderName, @NonNull String fileName) {
        Uri uri = null;
        File mediaStorageDir = null;
        if (directoryPath.equals(Environment.DIRECTORY_PICTURES)) {
            mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folderName);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdir()) {
                Log.d(TAG, "Not able to create directory");
            }
        }
        if (mediaStorageDir != null) {
            file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static File getFile() {
        return file;
    }
}
