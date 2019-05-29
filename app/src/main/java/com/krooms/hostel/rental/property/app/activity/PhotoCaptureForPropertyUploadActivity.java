package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.util.ScalingUtilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 3/10/2016.
 */
public class PhotoCaptureForPropertyUploadActivity extends Activity {

    private File fileDir = null;
    // captured picture uri
    private Uri picUri;
    private String cropedImageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_capture_photo);

        CreateViews();

        if (savedInstanceState != null) {
            picUri = savedInstanceState.getParcelable("file_uri");
            cropedImageName = savedInstanceState.getString("cropedImageName");
        }
    }

    private void CreateViews() {

        TextView txtSMS = (TextView) findViewById(R.id.textview_take_photo);
        txtSMS.setText(getIntent().getStringExtra("Select a photo source"));

        Button btnCamera = (Button) findViewById(R.id.button_takephoto_camera);
        btnCamera.setText("Take a Photo");
        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callPhotoCamera();
            }
        });

        Button btnLibrary = (Button) findViewById(R.id.button_takephoto_gallery);
        btnLibrary.setText("Pick from Library");
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.GALLERY_REQUEST);
            }
        });

        Button btnCancel = (Button) findViewById(R.id.button_takephoto_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Common.profileImagePath = "";
                PhotoCaptureForPropertyUploadActivity.this.finish();
            }
        });

    }

    public void callPhotoCamera() {

        try {
            // use standard intent to capture an image
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cropedImageName = "krooms_" + System.currentTimeMillis() + ".jpg";
            File file = new File(Environment.getExternalStorageDirectory(), "/" + cropedImageName);

            //File f = new File(fileDir,"/"+cropedImageName);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            picUri = Uri.fromFile(file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(cameraIntent, Common.CAMERA_CAPTURE_REQUEST);
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(PhotoCaptureForPropertyUploadActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                // user is returning from capturing an image using the camera
                if (requestCode == Common.CAMERA_CAPTURE_REQUEST) {

                    File file = new File(picUri.getPath());
//                   Common.profileImagePath = file.getPath();
                    Common.profileImagePath = decodeFile(file.getPath(), 700, 700);
                    Intent intent = getIntent();
                    intent.putExtra("ImageFilePath", file.getPath());
                    setResult(Common.CAMERA_CAPTURE_REQUEST, intent);
                    finish();

                } else if (requestCode == Common.GALLERY_REQUEST) {
                    // get the Uri for the captured image

                    //working code for other mobile
                    picUri = data.getData();
                    File file = new File(picUri.getPath());
                    Common.profileImagePath = decodeFile(getImagePath(data), 700, 700);
                    Intent intent = new Intent();
                    intent.putExtra("ImageFilePath", getImagePath(data));
                    setResult(Common.CAMERA_CAPTURE_REQUEST, intent);
                    finish();
                    //
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            //working in mi
                picUri = data.getData();
                File file1 = new File(picUri.getPath());
                Common.profileImagePath = decodeFile(file1.getPath(), 700, 700);
                Intent intent1 = new Intent();
                intent1.putExtra("ImageFilePath", file1.getPath());
                setResult(Common.CAMERA_CAPTURE_REQUEST, intent1);
                finish();
            //
        }
    }

    public String getImagePath(Intent data) {

        Uri uri = data.getData();
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();

        return picturePath;
    }


    private String decodeFile(String path, int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/Krooms");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "krooms_" + System.currentTimeMillis() + ".jpg";
            File f = new File(mFolder.getAbsolutePath(), s);
            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (Exception e) {

            }
            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }


    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    private void performCrop() {
        try {

            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image*//*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            /*cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 512);
			cropIntent.putExtra("outputY", 512);*/

            cropedImageName = "business_" + System.currentTimeMillis() + ".jpg";
            File file = new File(Environment.getExternalStorageDirectory(), "/" + cropedImageName);

            //File f = new File(fileDir,"/"+cropedImageName);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            Uri uri = Uri.fromFile(file);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cropIntent, Common.PIC_CROP_REQUEST);

        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PhotoCaptureForPropertyUploadActivity.this.finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", picUri);
        outState.putString("cropedImageName", cropedImageName);

    }

}
