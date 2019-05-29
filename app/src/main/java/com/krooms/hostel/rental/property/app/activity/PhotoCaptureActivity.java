package com.krooms.hostel.rental.property.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.util.ScalingUtilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoCaptureActivity extends Activity {

	private File fileDir = null;
	// captured picture uri
	private Uri picUri;
	private String cropedImageName=null;
	private String photo_purpose = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_capture_photo);

		CreateViews();
		photo_purpose = getIntent().getStringExtra("photo_purpose");

		if (savedInstanceState != null) {
			picUri = savedInstanceState.getParcelable("file_uri");
			photo_purpose = savedInstanceState.getString("photo_purpose");
			cropedImageName = savedInstanceState.getString("cropedImageName");
		}
	}

	private void CreateViews() {

		TextView txtSMS = (TextView) findViewById(R.id.textview_take_photo);
		txtSMS.setText(getIntent().getStringExtra("Select a photo source"));
		
		Button btnCamera = (Button) findViewById(R.id.button_takephoto_camera);
		btnCamera.setText("Take a Photo");
		btnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				callPhotoCamera();
			}
		});

		Button btnLibrary = (Button) findViewById(R.id.button_takephoto_gallery);
		btnLibrary.setText("Pick from Library");
		btnLibrary.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.GALLERY_REQUEST);

			}
		});

		Button btnCancel = (Button) findViewById(R.id.button_takephoto_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PhotoCaptureActivity.this.finish();
			}
		});

	}

	public void callPhotoCamera() {

		try {
			// use standard intent to capture an image
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
					&& !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
				fileDir = new File(Environment.getExternalStorageDirectory(),"Business");
			} else {
				fileDir = getCacheDir();
			}

			// make sure the cache dir has been created
			if (!fileDir.exists())
				fileDir.mkdirs();

			String fileName = "profileImage" +System.currentTimeMillis()+ ".jpg";
			picUri = Uri.fromFile(new File(fileDir, fileName));
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
			startActivityForResult(cameraIntent, Common.CAMERA_CAPTURE_REQUEST);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support capturing images!";
			Toast toast = Toast.makeText(PhotoCaptureActivity.this,errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@SuppressLint("InlinedApi")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		try {
		   if(resultCode == RESULT_OK) {
			 // user is returning from capturing an image using the camera
			 if (requestCode == Common.CAMERA_CAPTURE_REQUEST) {
				// get the Uri for the captured image
				// picUri = data.getData();
				// carry out the crop operation
				performCrop();
			 } else if (requestCode == Common.GALLERY_REQUEST) {
				// get the Uri for the captured image
				picUri = data.getData();
				// carry out the crop operation
				performCrop();
			 } else if (requestCode == Common.PIC_CROP_REQUEST) {
				
				File file = new File(picUri.getPath());
				// Temporary capture image file
				if(file.exists()) {
					if(file.delete()) {

					}
				}

				String filePath = Environment.getExternalStorageDirectory()	+ "/"+cropedImageName;

				 decodeFile(filePath,500,500);

				Common.profileImagePath = decodeFile(filePath,700,700);//filePath;

				 Common.Config("  image path   "+Common.profileImagePath);
				
				Intent intent = new Intent();
				intent.putExtra("ImageFilePath", filePath);
				setResult(Common.CAMERA_CAPTURE_REQUEST, intent);
				finish();

			}
		  }
		} catch(NullPointerException e) {
		}
	}

	private void performCrop() {
		try {

			// call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			/*cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 512);
			cropIntent.putExtra("outputY", 512);*/

			if(photo_purpose.equals("profilePic")){
				cropIntent.putExtra("aspectX", 1);
				cropIntent.putExtra("aspectY", 1);
				cropIntent.putExtra("outputX", 512);
				cropIntent.putExtra("outputY", 512);
			}

			cropedImageName = "krooms_" + System.currentTimeMillis() + ".jpg";
			File file = new File(Environment.getExternalStorageDirectory(),"/"+cropedImageName);

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

	private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
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
			File mFolder = new File(extr + "/TMMFOLDER");
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



	public Uri getImageUri(Context inContext, Bitmap inImage) {
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
		
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", picUri);
		outState.putString("photo_purpose", photo_purpose);
		outState.putString("cropedImageName", cropedImageName);

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		PhotoCaptureActivity.this.finish();
	}

}
