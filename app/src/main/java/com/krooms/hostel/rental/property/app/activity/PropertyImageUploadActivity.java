package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.adapter.AddPropertyImagesAdapter;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.AddPropertyImageServiceAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PropertyImageUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView propertyGrid;
    private Button continueBtn;
    private TextView txtBack;
    private AddPropertyImagesAdapter mPropertyImageAdapter;
    private ArrayList<PropertyPhotoModal> modalList = null;
    private int editImagePosition = 0;
    public String flag;
    private String property_id;
    private boolean isSkipOption;
    private Common mCommon;
    public SharedStorage mSharedStorage;
    String ImagePath = "";
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_image_upload);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }


        LogConfig.logd("P", "PropertyImageUploadActivity ");
        initView();
        if (savedInstanceState != null) {
            setDataOnSavedState(savedInstanceState);
        } else {

            flag = getIntent().getExtras().getString("flag");
            property_id = getIntent().getExtras().getString("PropertyId");
            isSkipOption = getIntent().getBooleanExtra("isSkipOption", false);
            setEditDataIfHaving();
        }
    }

    public void setEditDataIfHaving() {

        if (flag.equals("Edit")) {
            if (PropertyModal.getInstance().getPropertyImage().size() >= 1) {
                if (!PropertyModal.getInstance().getPropertyImage().get(0).getPhoto_Url().equals("Not Available")) {
                    modalList.addAll(PropertyModal.getInstance().getPropertyImage());
                }
            }
            mPropertyImageAdapter.notifyDataSetChanged();
        }
    }


    public void initView() {

        mSharedStorage = SharedStorage.getInstance(this);
        mCommon = new Common();
        modalList = new ArrayList<>();
        propertyGrid = (GridView) findViewById(R.id.propertyImageGridView);
        continueBtn = (Button) findViewById(R.id.button_next);
        txtBack = (TextView) findViewById(R.id.textView_title);

        mPropertyImageAdapter = new AddPropertyImagesAdapter(this, modalList) {
            @Override
            public void updatePropertyImage(int position) {
                editImagePosition = position;
              /*  Intent it = new Intent(PropertyImageUploadActivity.this, PhotoCaptureForPropertyUploadActivity.class);
                PropertyImageUploadActivity.this.startActivityForResult(it, Common.CAMERA_CAPTURE_REQUEST);
*/
                selectImage();
            }

            @Override
            public void viewPropertyImage(final int position) {

                if (modalList.size() < 10) {
                    if (position == modalList.size()) {
                        editImagePosition = position;
                      /*  Intent it = new Intent(PropertyImageUploadActivity.this, PhotoCaptureForPropertyUploadActivity.class);
                        PropertyImageUploadActivity.this.startActivityForResult(it, Common.CAMERA_CAPTURE_REQUEST);
*/
                        selectImage();
                    } else {

                        /*ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                            @Override
                            public void showImage(ImageView img) {

                                String image_path;

                                if (modalList.get(position).getPhoto_from().equals("server")) {
                                    image_path = WebUrls.IMG_URL + "" + modalList.get(position).getPhoto_Url();
                                } else {
                                    image_path = "file://" + modalList.get(position).getPhoto_Url();
                                }

                                LogConfig.logd(" item clicked..........1   ", "" + image_path);
                                Picasso.with(PropertyImageUploadActivity.this)
                                        .load(image_path)
                                        .placeholder(R.drawable.ic_default_background)
                                        .error(R.drawable.ic_default_background)
                                        .into(img);

                            }

                            @Override
                            public void downloadImage() {

                            }
                        };*/
                       // vehicleImagePreviewDialog.getPerameter(PropertyImageUploadActivity.this, "Image preview dialog", true);
                        //vehicleImagePreviewDialog.show(getSupportFragmentManager(), "image dialog");

                        CustomDialogClass VehicleRcId = new CustomDialogClass(PropertyImageUploadActivity.this);
                        VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        VehicleRcId.getPerameter(PropertyImageUploadActivity.this, "Image preview dialog", true,position);
                        VehicleRcId.show();


                    }
                } else {

                   /* LogConfig.logd(" item clicked.......... 1  ", "");
                    ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                        @Override
                        public void showImage(ImageView img) {
                            String image_path;
                            LogConfig.logd(" item clicked..........2   ", "" + modalList.get(position).getPhoto_Url());
                            if (modalList.get(position).getPhoto_from().equals("server")) {
                                image_path = WebUrls.IMG_URL + "" + modalList.get(position).getPhoto_Url();
                            } else {
                                image_path = "file://" + modalList.get(position).getPhoto_Url();
                            }
                            Picasso.with(PropertyImageUploadActivity.this)
                                    .load(image_path)
                                    .placeholder(R.drawable.ic_default_background)
                                    .error(R.drawable.ic_default_background)
                                    .into(img);
                        }

                        @Override
                        public void downloadImage() {

                        }
                    };*/


                    CustomDialogClass VehicleRcId = new CustomDialogClass(PropertyImageUploadActivity.this);
                    VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    VehicleRcId.getPerameter(PropertyImageUploadActivity.this, "Image preview dialog", true,position);
                    VehicleRcId.show();

                    //vehicleImagePreviewDialog.getPerameter(PropertyImageUploadActivity.this, "Image preview dialog", true);
                    //vehicleImagePreviewDialog.show(getSupportFragmentManager(), "image dialog");
                }
            }
        };
        propertyGrid.setAdapter(mPropertyImageAdapter);

        continueBtn.setOnClickListener(this);
        txtBack.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("flag", flag);
        outState.putString("property_id", property_id);
        outState.putBoolean("isSkipOption", isSkipOption);
        outState.putInt("editImagePosition", editImagePosition);
        outState.putParcelableArrayList("modalList", modalList);
        PropertyModal.getInstance().setPropertyImage(modalList);
        outState.putParcelable("propertyModal", PropertyModal.getInstance());

    }

    public void setDataOnSavedState(Bundle savedInstanceState) {

        flag = "" + savedInstanceState.getString("flag");
        property_id = "" + savedInstanceState.getString("property_id");
        isSkipOption = savedInstanceState.getBoolean("isSkipOption");
        editImagePosition = savedInstanceState.getInt("editImagePosition");

        PropertyModal.propertyModal = (PropertyModal) savedInstanceState.getParcelable("propertyModal");
        if (PropertyModal.getInstance().getPropertyImage().size() >= 1) {

            if (!PropertyModal.getInstance().getPropertyImage().get(0).getPhoto_Url().equals("Not Available")) {
                modalList.addAll(PropertyModal.getInstance().getPropertyImage());
            }

        } else {
            modalList = savedInstanceState.getParcelableArrayList("modalList");
        }

        mPropertyImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_next:
                Intent mIntent = new Intent(this, PropertyInfoSecondActivity.class);
                mIntent.putExtra("flag", flag);
                mIntent.putExtra("PropertyId", "" + property_id);
                mIntent.putExtra("isSkipOption", isSkipOption);
                startActivity(mIntent);
                finish();
                break;

            case R.id.textView_title:

                Intent mIntent1 = new Intent(PropertyImageUploadActivity.this, HostelListActivity.class);
                mIntent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent1);
                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);

                break;
        }

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    case Common.CAMERA_CAPTURE_REQUEST:
                        setCaptureImage();
                        break;

                }
            } else {
                switch (requestCode) {
                    case Common.CAMERA_CAPTURE_REQUEST:
                        setCaptureImage();
                        break;
                }

            }
        } catch (Exception e) {
        }
    }*/


    private void setCaptureImage() {

        Common.Config("setCaptureImage path = " + Common.profileImagePath + "" + modalList.size());

        if (Common.profileImagePath != null && !Common.profileImagePath.equals("")) {

            try {

                if (modalList.size() == 0) {

                    PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                    modal.setPhoto_id("");
                    modal.setPhoto_Url(Common.profileImagePath);
                    modal.setPhoto_from("local");
                    modalList.add(modal);

                    AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                    mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                        @Override
                        public void requestResponce(String result) {
                            responseParse(result);
                        }
                    });
                    mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            mSharedStorage.getUserId(), property_id, Common.profileImagePath,
                            "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                } else {
                    if (editImagePosition < modalList.size()) {

                        if (!modalList.get(editImagePosition).getPhoto_id().equals("")) {
                            PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                            modal.setPhoto_id(modalList.get(editImagePosition).getPhoto_id());
                            modal.setPhoto_Url(Common.profileImagePath);
                            modal.setPhoto_from("local");
                            modalList.set(editImagePosition, modal);

                            AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                            mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                                @Override
                                public void requestResponce(String result) {
                                    responseParse(result);
                                }
                            });
                            mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    mSharedStorage.getUserId(), property_id, Common.profileImagePath,
                                    "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                        } else {

                            PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                            modal.setPhoto_id("");
                            modal.setPhoto_Url(Common.profileImagePath);
                            modal.setPhoto_from("local");
                            modalList.set(editImagePosition, modal);


                            AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                            mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                                @Override
                                public void requestResponce(String result) {
                                    responseParse(result);
                                }
                            });
                            mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    mSharedStorage.getUserId(), property_id, Common.profileImagePath,
                                    "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());

                        }
                    } else {

                        PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                        modal.setPhoto_id("");
                        modal.setPhoto_Url(Common.profileImagePath);
                        modal.setPhoto_from("local");
                        modalList.add(modal);

                        AddPropertyImageServiceAsyncTask mSyncDatabaseTask = new AddPropertyImageServiceAsyncTask(this);
                        mSyncDatabaseTask.setCallBack(new ServiceResponce() {
                            @Override
                            public void requestResponce(String result) {
                                responseParse(result);
                            }
                        });
                        mSyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                mSharedStorage.getUserId(), property_id, Common.profileImagePath,
                                "" + (editImagePosition + 1), modalList.get(editImagePosition).getPhoto_id());
                    }
                }
            } catch (NullPointerException e) {
                //Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                Common.profileImagePath = null;
                //Log.e("Camera", e.toString());
            } catch (Exception e) {
                Common.profileImagePath = null;
                //Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                // Log.e("Camera", e.toString());
            }
        } else {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }

        mPropertyImageAdapter.notifyDataSetChanged();
    }

    public void responseParse(String result) {
        LogConfig.logd("Property image response =", "" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                JSONArray jArray = jsonObject.getJSONArray("data");
                String image_id = jArray.getJSONObject(0).getString("id");
                String image_path = jArray.getJSONObject(0).getString("path");

                PropertyPhotoModal modal = new PropertyPhotoModal(Parcel.obtain());
                modal.setPhoto_id(image_id);
                modal.setPhoto_Url(image_path);
                modal.setPhoto_from("server");
                modalList.set(editImagePosition, modal);
                mPropertyImageAdapter.notifyDataSetChanged();

                mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                Common.profileImagePath = "";
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }




    //new

    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private ImageView imageView;
        private String titleStr;
        private TextView message;
        private Button downloadImageBtn;
        private Button alertNoBtn;
        private Boolean downloadBtnHide;
        private String photo = "";
        private String type = "";
        private  int position;

        public CustomDialogClass(PropertyImageUploadActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }

        /*public CustomDialogClass(FragmentManager supportFragmentManager) {
            super();
        }*/


        public void getPerameter(FragmentActivity activity, String titleStr, Boolean flag, int position) {
            this.mFActivity = activity;
            this.titleStr = titleStr;
            this.downloadBtnHide = flag;
            //this.photo = tenantphoto;
            //this.type = vehicleRcId;
            this.position=position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.list_group);
            title = (TextView) findViewById(R.id.dailogTitiel);
            imageView = (ImageView) findViewById(R.id.imageView);
            downloadImageBtn = (Button) findViewById(R.id.downloadImageBtn);

            try {
                //showImage(imageView);
                String image_path;

                if (modalList.get(position).getPhoto_from().equals("server")) {
                    image_path = WebUrls.IMG_URL + "" + modalList.get(position).getPhoto_Url();
                } else {
                    image_path = "file://" + modalList.get(position).getPhoto_Url();
                }

                //LogConfig.logd(" item clicked..........1   ", "" + image_path);
                Picasso.with(PropertyImageUploadActivity.this)
                        .load(image_path)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(imageView);


            } catch (Exception e) {

            }


            title.setText(titleStr);

            if (downloadBtnHide) {
                downloadImageBtn.setVisibility(View.GONE);
            } else {
                downloadImageBtn.setVisibility(View.VISIBLE);
            }

            downloadImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equals("TenantPic")) {

                    }

                }
            });


        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    break;
                case R.id.btn2:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }


    }

    ///nnnn



    /*image capture code start*/
    public void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyImageUploadActivity.this);
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PropertyImageUploadActivity.this, android.R.layout.simple_list_item_1);
        adapter.add("Take Picture");
        adapter.add("Choose from gallery");
        adapter.add("Cancel");

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (UtilityImg.isExternalStorageAvailable()) {
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileURI());
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, 1);
                            }
                        } else {
                            Toast toast = Toast.makeText(PropertyImageUploadActivity.this, "Please give internal storage permission", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        break;

                    case 1:
                        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent1, 2);
                        break;

                    case 2:
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.show();
    }

    private Uri getPhotoFileURI() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssZ", Locale.ENGLISH);
        Date currentDate = new Date();
        String photoFileName = "photo.jpg";
        String fileName = simpleDateFormat.format(currentDate) + "_" + photoFileName;

        String APP_TAG = "ImageFolder";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uri = UtilityImg.getExternalFilesDirForVersion24Above(PropertyImageUploadActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(PropertyImageUploadActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setImage();
            }

            if (requestCode == 2) {
                Uri select = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(select, filepath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnindex = c.getColumnIndex(filepath[0]);
                String picturepath = c.getString(columnindex);
                ImagePath = picturepath;
                c.close();

                CropImage.activity(Uri.fromFile(new File(ImagePath)))
                        .start(this);

            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    if (resultUri != null) {


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        //ownerProfilePic.setImageBitmap(bitmap);


                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));

                        Common.profileImagePath = String.valueOf(finalFile);

                        setCaptureImage();

                    }
                } catch (Exception e) {
                    //handle exception
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void setImage() {
        ImagePath = UtilityImg.getFile().getAbsolutePath();
        Log.d("string path of file", "" + ImagePath);
        CropImage.activity(Uri.fromFile(new File(ImagePath)))
                // .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    //end


}
