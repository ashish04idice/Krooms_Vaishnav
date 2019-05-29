package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.ScalingUtilities;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.asynctask.RCUTenantDetailAsynctask;
import com.krooms.hostel.rental.property.app.asynctask.TenantDetailUpdateAsynctask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.callback.StateDataBaseResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.database.GetStatesAsyncTask;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.krooms.hostel.rental.property.app.Utility.ScalingUtilities.getResizedBitmap;

/**
 * Created by Hi on 3/22/2017.
 */
public class Tenant_Detail_Update_FirstActivity extends Activity implements View.OnClickListener, ServiceResponce,StateDataBaseResponce {


    private EditText edtFirstName = null;
    private EditText edtLastName = null;
    private EditText edtFatherName = null;
    private EditText edtFatherContactno = null;
    private EditText edtFlatPlotno = null;
    private EditText edtLandMark = null;
    private EditText edtLocation = null;
    private EditText edtPinCode = null;
    private EditText edtContactno = null;
    private EditText edtEmail = null;
    private TextView txtHireDate, txtLeaveDate;
    private TextView txtRoom_no = null;
    private ImageView tenantProfilePic;
    private EditText spinnerCity;
    private Spinner spinnerNoofCotenant, spinnerState;
    private String imgTenanto = null;
    private String imgTenantoDisplay = null;
    private ProgressBar mprogressBar;
    private Common mCommon;
    private Validation mValidation;
    private Button btnSubmit, btnBrowseImage;
    private TextView txtBack;
    private LinearLayout mLayoutTenant, mLayoutTenantView;

    String firstName,lastName,fatherName,fatherContactno,flatPlotno,landMark,pinCode,contactNo,email,hireDate,leaveDate,areaId,cityId;
    String property_id_new,room_id_new,ownerid_new,suserid,tenantroomno,transaction_id_new,Parent_id;
    String Tfname,Tlname,Tcontact,Temail,Tfathername,Tfather_no,Thiredate,Tleavedate,TCity,TPincode,TFlatno,Tlocation,TState,TPhoto;
    String alreadyno,alreadyemail,stateId = "0",state, countryId = "1";
    private ArrayList<StateModal> mArrayStates = null;
    private StateSpinnerAdapter mStateSpinnerAdapter;
    private int year1, month, day;
    String tenantId;
    SharedStorage msharedobj;

    String Tofc_institute,Tofc_contact,Twork_detail,Tguarantorname,Tguarantoraddress,Tguarantorcontact,TDrivinglicenseNo,TDrivinglicensePhoto,TDrivinglicenseIssuename;
    String TAadharcardNo,TAadharcardPhoto,TArmlicenceNo,TArmlicencePhoto,TArmlicenceIssuename,TOtherIdno,TOtherIdPhoto,TVoteridcardno,TVoteridcardPhoto;
    String TVehicleRegistrationNo,TVehicleRegistrationPhoto,TPassportNo,TPassportPhoto,TRashanCardNo,TRashanCardPhoto,TsformTelephoneNo,TsformMobileNo;
    String Tofficeaddress,Totherdetail;
   String usertypevalue;
    int imagegallery = 1;
    int imageCamera = 2;

    Intent CamIntent, GalIntent, CropIntent ;

    File file;
    Uri uri;
    String profileImageUpload = "";

    String  adavnce;
    String tenantroomamount;
    String userid_main_json;
    String resultmain=null;
    String result1="";
    String message="";
    ProgressDialog dialog,dialogbooking;
    String ImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tenant_detailupdate);
        msharedobj= SharedStorage.getInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
        }
        findViewById();
        getIntentValues();
        SetInformation();

        Date cal = (Date) Calendar.getInstance().getTime();
        year1 = cal.getYear() + 1900;
        month = cal.getMonth();
        day = cal.getDate();


        if(msharedobj.getUserType().equals("4")) {
            Parent_id =msharedobj.getParent_Id();
        }else
        {
            Parent_id ="0";
        }

        mCommon = new Common();
        mValidation = new Validation(this);


        btnBrowseImage.setOnClickListener(this);
        txtHireDate.setOnClickListener(this);
        txtLeaveDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        txtBack.setOnClickListener(this);

    }

    private void SetInformation() {

        edtFirstName.setText(Tfname);
        edtLastName.setText(Tlname);
        edtFatherName.setText(Tfathername);
        edtFatherContactno.setText(Tfather_no);
        edtFlatPlotno.setText(TFlatno);
        edtLandMark.setText(Tlocation);
        edtPinCode.setText(TPincode);
        edtContactno.setText(Tcontact);
        edtEmail.setText(Temail);
        alreadyno=Tcontact;
        alreadyemail=Temail;

        if(alreadyemail.equals("") || alreadyemail ==null)
        {
            edtEmail.setEnabled(true);
        }else
        {
            edtEmail.setEnabled(false);
        }

        if(alreadyno.equals("") || alreadyno==null)
        {
            edtContactno.setEnabled(true);
        }else
        {
            edtContactno.setEnabled(false);
        }
        txtHireDate.setText(Thiredate);
        txtLeaveDate.setText(Tleavedate);
        //mm stateId = Common.rcuDetails.get(1).getState();
        stateId =TState;
        txtRoom_no.setText("Room no." + tenantroomno);
        spinnerCity.setText(TCity);
//            cityId = Common.rcuDetails.get(position).getCity();
        edtLocation.setText(Tlocation);
//            areaId = Common.rcuDetails.get(position).getLocation();
       // imgTenanto = Common.rcuDetails.get(1).getTenant_photo();
       // imgTenanto="";

        imgTenanto=TPhoto;


        if (imgTenanto.trim().equals("")) {
            btnBrowseImage.setText("Browse");
        } else {
            btnBrowseImage.setText("" + imgTenanto);
        }
        if (!imgTenanto.trim().equals("")) {
            tenantProfilePic.setVisibility(View.VISIBLE);
            imgTenantoDisplay="";

            //imgTenantoDisplay = WebUrls.IMG_URL + Common.rcuDetails.get(1).getTenant_photo();

            imgTenantoDisplay = WebUrls.IMG_URL +TPhoto;

            Picasso.with(this)
                    .load(imgTenantoDisplay)
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(tenantProfilePic);
        } else {
            tenantProfilePic.setVisibility(View.GONE);
        }

        changeStateCityArea();


    }

    private void changeStateCityArea() {

        if (mArrayStates != null && mArrayStates.size() > 0) {
            for (int i = 0; i < mArrayStates.size(); i++) {
                if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                    spinnerState.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();
    }

    private void getIntentValues() {


        userid_main_json=msharedobj.getUserId();

        Intent in=getIntent();
        tenantId=in.getStringExtra("RCUId");
        property_id_new=in.getStringExtra("property_id");
        room_id_new=in.getStringExtra("room_id");
        ownerid_new=in.getStringExtra("owner_id");
        suserid=in.getStringExtra("user_id");
        tenantroomno=in.getStringExtra("TRoomno");
        Tfname=in.getStringExtra("Tfname");
        Tlname=in.getStringExtra("Tlname");
        Tcontact=in.getStringExtra("Tcontact");
        Temail=in.getStringExtra("Temail");
        Tfathername=in.getStringExtra("Tfathername");
        Tfather_no=in.getStringExtra("Tfather_no");
        Thiredate=in.getStringExtra("Thiredate");
        Tleavedate=in.getStringExtra("Tleavedate");
        TCity=in.getStringExtra("TCity");
        TPincode=in.getStringExtra("TPincode");
        TFlatno=in.getStringExtra("TFlatno");
        Tlocation=in.getStringExtra("Tlandmark");
        TState=in.getStringExtra("TState");
        TPhoto=in.getStringExtra("TPhoto");
        transaction_id_new = getIntent().getExtras().getString("TTransaction_id");

        //second form information

        Tofficeaddress= in.getStringExtra("TofficeAddress");
        Totherdetail= in.getStringExtra("TotherVerifaction");

        Tofc_institute=in.getStringExtra("Tofc_institute");
        Tofc_contact=in.getStringExtra("Tofc_contact");
        Twork_detail=in.getStringExtra("Twork_detail");
        Tguarantorname= in.getStringExtra("Tguarantorname");
        Tguarantoraddress= in.getStringExtra("Tguarantoraddress");
        Tguarantorcontact=in.getStringExtra("Tguarantorcontact");
        TDrivinglicenseNo=in.getStringExtra("TDrivinglicenseNo");
        TDrivinglicensePhoto=in.getStringExtra("TDrivinglicensePhoto");
        TDrivinglicenseIssuename=in.getStringExtra("TDrivinglicenseIssuename");
        TAadharcardNo=in.getStringExtra("TAadharcardNo");
        TAadharcardPhoto=in.getStringExtra("TAadharcardPhoto");
        TArmlicenceNo= in.getStringExtra("TArmlicenceNo");
        TArmlicencePhoto=in.getStringExtra("TArmlicencePhoto");
        TArmlicenceIssuename=in.getStringExtra("TArmlicenceIssuename");
        TOtherIdno=in.getStringExtra("TOtherIdno");
        TOtherIdPhoto=in.getStringExtra("TOtherIdPhoto");
        TVoteridcardno=in.getStringExtra("TVoteridcardno");
        TVoteridcardPhoto=in.getStringExtra("TVoteridcardPhoto");
        TVehicleRegistrationNo=in.getStringExtra("TVehicleRegistrationNo");
        TVehicleRegistrationPhoto=in.getStringExtra("TVehicleRegistrationPhoto");
        TPassportNo=in.getStringExtra("TPassportNo");
        TPassportPhoto=in.getStringExtra("TPassportPhoto");
        TRashanCardNo=in.getStringExtra("TRashanCardNo");
        TRashanCardPhoto=in.getStringExtra("TRashanCardPhoto");
        TsformTelephoneNo=in.getStringExtra("TsformTelephoneNo");
        TsformMobileNo=in.getStringExtra("TsformMobileNo");
        in.getStringExtra("TsformMobileNo");
        setStateInformation();
    }

    private void setStateInformation() {

        // Search state data
        mArrayStates = new ArrayList<>();
        GetStatesAsyncTask mGetStatesAsyncTask = new GetStatesAsyncTask(Tenant_Detail_Update_FirstActivity.this, countryId);
        mGetStatesAsyncTask.setCallBack(Tenant_Detail_Update_FirstActivity.this);
        mGetStatesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                "SELECT * FROM state where country_id = '" + countryId + "' ORDER BY state_name");
        mStateSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_state, mArrayStates);
        spinnerState.setAdapter(mStateSpinnerAdapter);

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                state = mArrayStates.get(position).getStrStateName();
                if (position != 0) {
                    stateId = mArrayStates.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void findViewById() {

        edtFirstName=(EditText) findViewById(R.id.edittext_first_name);
        edtLastName = (EditText) findViewById(R.id.edittext_last_name);
        edtFatherName = (EditText) findViewById(R.id.edittext_father_name);
        edtFatherContactno = (EditText) findViewById(R.id.edittext_father_contact_no);
        edtFlatPlotno = (EditText) findViewById(R.id.edittext_flat_plot_no);
        edtLandMark = (EditText) findViewById(R.id.edittext_landmark);
        edtPinCode = (EditText) findViewById(R.id.edittext_pincode);
        edtContactno = (EditText) findViewById(R.id.edittext_contact_no);
        edtEmail = (EditText) findViewById(R.id.edittext_email);
        txtRoom_no = (TextView) findViewById(R.id.text_RoomNo);
        txtHireDate = (TextView) findViewById(R.id.textview_hire_date);
        txtLeaveDate = (TextView) findViewById(R.id.textview_leave_date);
        btnBrowseImage = (Button) findViewById(R.id.button_browse);
        spinnerNoofCotenant = (Spinner) findViewById(R.id.spinner_no_of_cotenant);
        spinnerState = (Spinner) findViewById(R.id.spinner_state);
        spinnerCity = (EditText) findViewById(R.id.spinner_city);
        edtLocation = (EditText) findViewById(R.id.edittext_location);
        tenantProfilePic = (ImageView) findViewById(R.id.tenantProfilePic);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);
        txtBack = (TextView) findViewById(R.id.textView_title);
        btnSubmit = (Button) findViewById(R.id.button_next);
        btnSubmit.setClickable(true);
        mLayoutTenant = (LinearLayout) findViewById(R.id.layout_tent_button);
        mLayoutTenantView = (LinearLayout) findViewById(R.id.layout_tent_view);

        if(msharedobj.getUserType().equals("4"))
        {
            usertypevalue =msharedobj.getParent_Id();
        }else
        {
            usertypevalue ="0";
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_browse:
                //new code by ashish
                selectImage();
                break;


            case R.id.textview_hire_date:
                txtLeaveDate.setText("");
                leaveDateCal = null;
                onCreateDialogTest(txtHireDate, "hire_date").show();
                break;

            case R.id.textview_leave_date:

                if (hireDateCal != null) {
                    onCreateDialogTest(txtLeaveDate, "leave_date").show();
                } else {
                    mCommon.displayAlert(this, "Please select Hire date first.", false);
                }
                break;

            case R.id.button_next:
               // Toast.makeText(nouseActivity.this, "SubmitButton", Toast.LENGTH_SHORT).show();
                submitBtnClickEvent();
                break;


            case R.id.textView_title:

                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);

               // Toast.makeText(nouseActivity.this, "Back Press", Toast.LENGTH_SHORT).show();
               /* Intent intent=new Intent(nouseActivity.this,TenantDetailActivity.class);
                startActivity(intent);
              */  break;




        }
    }

    //new code profile image capture
    /*image capture code start*/
    public void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Tenant_Detail_Update_FirstActivity.this);
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tenant_Detail_Update_FirstActivity.this, android.R.layout.simple_list_item_1);
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
                            Toast toast=Toast.makeText(Tenant_Detail_Update_FirstActivity.this,"Please give internal storage permission", Toast.LENGTH_SHORT);
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
            uri = UtilityImg.getExternalFilesDirForVersion24Above(Tenant_Detail_Update_FirstActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(Tenant_Detail_Update_FirstActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
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
                Log.e("Imageg-", "" + ImagePath);
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
                    if(  resultUri!=null   ){

                        Log.e("resultUri",""+resultUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , resultUri);
                        tenantProfilePic.setImageBitmap(bitmap);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        Bitmap bitmap1;
                        bitmap1 = ScalingUtilities.scaleDown(bitmap, 420, true);
                        FileOutputStream fOut;
                        try {
                            File f = new File(getRealPathFromURI(tempUri));
                            fOut = new FileOutputStream(f);
                            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            imgTenanto= f.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (tenantProfilePic.getVisibility() == View.GONE) {
                            tenantProfilePic.setVisibility(View.VISIBLE);
                        }

                     /*  String filename = imgTenantoDisplay.substring(imgTenantoDisplay.lastIndexOf("/") + 1);
                        btnBrowseImage.setText("" + filename);*/
                    }
                }
                catch (Exception e) {
                    //handle exception
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
       // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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
    //new take image from camera gallery
    public void gotoAddProfileImage(final int galleryvalue,final int Cameravalue) {
        final Dialog dialog = new Dialog(Tenant_Detail_Update_FirstActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_upload_pic_alert_view);
        dialog.setCanceledOnTouchOutside(false);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf");
        Button image_cross_layout=(Button)dialog.findViewById(R.id.image_cross_layout);
        image_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button gallerytext = (Button) dialog.findViewById(R.id.upload_gallery);
        Button camera = (Button) dialog.findViewById(R.id.take_camera);
        image_cross_layout.setTypeface(font);
        gallerytext.setTypeface(font);
        camera.setTypeface(font);
        gallerytext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    GetImageFromGallery();
                    dialog.dismiss();
                } catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }
              /*  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,galleryvalue);
                dialog.dismiss();*/
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    ClickImageFromCamera();
                } catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }
                dialog.dismiss();


               /* try {
                    clearChacheFile();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageCaptureUri = Uri.fromFile(new File(SINROM_TEMP_PATH_FROM_CAMERA));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent,Cameravalue);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }*/

            }
        });

        dialog.show();
    }
    //new image from Camera
    public void ClickImageFromCamera() {

        CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    //new image from Gallery
    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }


    //Send User Details to server
    private void submitBtnClickEvent() {

        if (imgTenanto == null) {
            imgTenanto = "";
        }
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        fatherName = edtFatherName.getText().toString().trim();
        fatherContactno = edtFatherContactno.getText().toString().trim();
        flatPlotno = edtFlatPlotno.getText().toString().trim();
        landMark = edtLandMark.getText().toString().trim();
        pinCode = edtPinCode.getText().toString().trim();
        contactNo = edtContactno.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        hireDate =txtHireDate.getText().toString().trim();
        leaveDate = txtLeaveDate.getText().toString().trim();
        areaId = edtLocation.getText().toString().trim();
        cityId = spinnerCity.getText().toString().trim();

        if (firstName.length() == 0) {
            mCommon.displayAlert(this, "Enter Tenant's full name.", false);
        } else if (fatherName.length() == 0) {
            mCommon.displayAlert(this, "Enter father name.", false);
        } else if (fatherContactno.length() > 0 && fatherContactno.length() < 10) {
            mCommon.displayAlert(this, "Enter father valid contact no.", false);
        } else if (fatherContactno.length() ==0) {
            mCommon.displayAlert(this, "Enter father contact no.", false);
        } else if (contactNo.length() > 0 && contactNo.length() < 10) {
            mCommon.displayAlert(this, "Enter valid contact no.", false);
        } else if (contactNo.length()==0) {
            mCommon.displayAlert(this, "Enter contact no.", false);
        } else if(email.length() > 0 && !mValidation.checkEmail(email) ) {
            mCommon.displayAlert(this, "Enter valid email id.", false);
        } else if (pinCode.length() > 0 && pinCode.length() < 6) {
            mCommon.displayAlert(this,"Enter valid pincode number.", false);
        } else if (flatPlotno.length() == 0) {
            mCommon.displayAlert(this, "Enter flat/plot/building no.", false);
        } else {
            mCommon.hideKeybord(this, edtFatherContactno);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {

                mprogressBar.setVisibility(View.VISIBLE);
                String mainuserid= SharedStorage.getInstance(Tenant_Detail_Update_FirstActivity.this).getUserId();

               // profileImageUpload change with imgTenanto
                String namemainvalue = firstName;
                String emailmainvalue = email;
                String mobileNomainvalue = contactNo;

                if(hireDate.equals(""))
                {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    String yearv = "" + year;
                    int monthv = cal.get(Calendar.MONTH);
                    int datev = cal.get(Calendar.DAY_OF_MONTH);

                    hireDate=datev+"/"+(monthv+1)+"/"+year;


                }

                btnSubmit.setClickable(false);
                    //owner end tenant detials update
                    new UpdateUserDetail(namemainvalue, emailmainvalue, mobileNomainvalue).execute();
            }

        }


    }
    //end

               //User details api response
    @Override
        public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        Common.Config("result      " + result);
        LogConfig.logd("Rcu t enant detail response =", "" + result);
        try {
            btnSubmit.setClickable(true);
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    //working

                    edtFirstName.requestFocus();
                    Intent intent=new Intent(Tenant_Detail_Update_FirstActivity.this,Service_handle_paymentapi.class);
                    intent.putExtra("Propertyid",property_id_new);
                    intent.putExtra("Roomid",room_id_new);
                    intent.putExtra("Ownerid",ownerid_new);
                    intent.putExtra("Userid", userid_main_json);
                    intent.putExtra("Tenantid", tenantId);
                    intent.putExtra("Suserid",suserid);
                    //intent.putExtra("Hiredate",hireDate);
                    startService(intent);
                    dialog.dismiss();


                    dialogbooking.dismiss();

                    Intent mIntent = new Intent(this,Tenant_Professionnal_Details_update_Activity.class);
                    mIntent.putExtra("user_id_tenant",suserid);
                    mIntent.putExtra("isUpdate","true");
                    mIntent.putExtra("Tofficeaddress",Tofficeaddress);
                    mIntent.putExtra("Totherdetail",Totherdetail);
                    mIntent.putExtra("RcuTenantID",tenantId);
                    mIntent.putExtra("Tofc_institutename",Tofc_institute);
                    mIntent.putExtra("Tofc_contact",Tofc_contact);
                    mIntent.putExtra("Twork_detail",Twork_detail);
                    mIntent.putExtra("Tguarantorname",Tguarantorname);
                    mIntent.putExtra("Tguarantoraddress",Tguarantoraddress);
                    mIntent.putExtra("Tguarantorcontact",Tguarantorcontact);
                    mIntent.putExtra("TDrivinglicenseNo",TDrivinglicenseNo);
                    mIntent.putExtra("TDrivinglicensePhoto",TDrivinglicensePhoto);
                    mIntent.putExtra("TDrivinglicenseIssuename",TDrivinglicenseIssuename);
                    mIntent.putExtra("TAadharcardNo",TAadharcardNo);
                    mIntent.putExtra("TAadharcardPhoto",TAadharcardPhoto);
                    mIntent.putExtra("TArmlicenceNo",TArmlicenceNo);
                    mIntent.putExtra("TArmlicencePhoto",TArmlicencePhoto);
                    mIntent.putExtra("TArmlicenceIssuename",TArmlicenceIssuename);
                    mIntent.putExtra("TOtherIdno",TOtherIdno);
                    mIntent.putExtra("TOtherIdPhoto",TOtherIdPhoto);
                    mIntent.putExtra("TVoteridcardno",TVoteridcardno);
                    mIntent.putExtra("TVoteridcardPhoto",TVoteridcardPhoto);
                    mIntent.putExtra("TVehicleRegistrationNo",TVehicleRegistrationNo);
                    mIntent.putExtra("TVehicleRegistrationPhoto",TVehicleRegistrationPhoto);
                    mIntent.putExtra("TPassportNo",TPassportNo);
                    mIntent.putExtra("TPassportPhoto",TPassportPhoto);
                    mIntent.putExtra("TRashanCardNo",TRashanCardNo);
                    mIntent.putExtra("TRashanCardPhoto",TRashanCardPhoto);
                    mIntent.putExtra("TsformTelephoneNo",TsformTelephoneNo);
                    mIntent.putExtra("TsformMobileNo",TsformMobileNo);
                    startActivity(mIntent);

                    //

                } else {
                    dialogbooking.dismiss();
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
//

    Calendar hireDateCal, leaveDateCal;

    protected Dialog onCreateDialogTest(final TextView txtDate, final String type) {

        DatePickerDialog datePicker = new DatePickerDialog(this,R.style.datepickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //showDate(year, monthOfYear + 1, dayOfMonth, v);
                        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        if (type.equals("hire_date")) {

                            hireDateCal = Calendar.getInstance();
                            hireDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            Calendar currentDateCal = Calendar.getInstance();
                            currentDateCal.set(year1, (month + 1), day - 1);
                          //  if (getDateDiffStringBool(getDateFromCalender(currentDateCal), getDateFromCalender(hireDateCal))) {
                                txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                          /*  } else {
                                mCommon.displayAlert(Tenant_Detail_Update_FirstActivity.this, "Hire date will not be less than Current date.", false);
                                txtDate.setText("");
                                hireDateCal = null;
                            }*/


                        } else {

                            leaveDateCal = Calendar.getInstance();
                            leaveDateCal.set(year, (monthOfYear + 1), dayOfMonth);
                            if (getDateDiffStringBool(getDateFromCalender(hireDateCal), getDateFromCalender(leaveDateCal))) {
                                txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                        .append(monthOfYear + 1).append("/").append(year));
                            } else {

                                mCommon.displayAlert(Tenant_Detail_Update_FirstActivity.this, "Leave date will not be equal or less than Hire date.", false);
                                txtDate.setText("");
                                leaveDateCal = null;
                            }

                        }

                        /*txtDate.setText("" + new StringBuilder().append(dayOfMonth).append("/")
                                .append(monthOfYear + 1).append("/").append(year));*/
                    }
                }, year1, month, day);

        return datePicker;

    }


    public boolean getDateDiffStringBool(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
//        getDay(delta);
        if (delta > 0) {
            return true;
        } else {
            delta *= -1;
            return false;
        }


    }



    public Date getDateFromCalender(Calendar cal) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(cal.getTime());


        Date date1 = null;
        try {
            date1 = df.parse(formattedDate);
        } catch (ParseException e) {
//            e.printStackTrace();
        }

        return date1;

    }



    public void takePhoto() {

        Intent mIntent = new Intent(this, PhotoCaptureActivity.class);
        mIntent.putExtra("photo_purpose", "profilePic");
        startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);
    }




   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if(requestCode == 2) {

            if (data != null) {

                uri = data.getData();
                ImageCropFunction();

            }
        }
        else if(requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,bitmap.getWidth() / 2, paint);
                tenantProfilePic.setImageBitmap(bitmap);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Krooms");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-"+ n +".jpg";
                File file = new File(myDir, fname);
               // profileImage = "file://" + file;

                imgTenantoDisplay= "file://" + file;//Image path
                profileImageUpload= String.valueOf(file);//new file path send to server
             //imgTenanto
                imgTenanto=profileImageUpload;

                if(file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (tenantProfilePic.getVisibility() == View.GONE) {
                    tenantProfilePic.setVisibility(View.VISIBLE);
                }

                *//*Picasso.with(this)
                        .load(imgTenantoDisplay)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)

                        .into(tenantProfilePic);*//*//imgTenanto
                String filename = imgTenantoDisplay.substring(imgTenantoDisplay.lastIndexOf("/") + 1);
                btnBrowseImage.setText("" + filename);
            }

        }
    }*/


    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    public void requestStateDataBaseResponce(ArrayList<StateModal> mArray) {

        mArrayStates.addAll(mArray);
        if (mArrayStates != null && mArrayStates.size() > 0) {
            for (int i = 0; i < mArrayStates.size(); i++) {
                if (mArrayStates.get(i).getStrSateId().equals(stateId)) {
                    spinnerState.setSelection(i);
                }
            }
        }
        mStateSpinnerAdapter.notifyDataSetChanged();
    }

    //new changes on  02/2/2019

    public class UpdateUserDetail extends AsyncTask<String, String, String> {
        String namemainvalue,emailmainvalue,mobileNomainvalue;

        public UpdateUserDetail(String namemainvalue, String emailmainvalue, String mobileNomainvalue) {

            this.namemainvalue =namemainvalue;
            this.emailmainvalue=emailmainvalue;
            this.mobileNomainvalue=mobileNomainvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Tenant_Detail_Update_FirstActivity.this);
            dialog.setMessage("Please wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action","tenantlogin"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",property_id_new));
            listNameValuePairs.add(new BasicNameValuePair("room_id",room_id_new));
            listNameValuePairs.add(new BasicNameValuePair("name",namemainvalue));
            listNameValuePairs.add(new BasicNameValuePair("email",emailmainvalue));
            listNameValuePairs.add(new BasicNameValuePair("tenantid",tenantId));
            listNameValuePairs.add(new BasicNameValuePair("mobile",mobileNomainvalue));
            listNameValuePairs.add(new BasicNameValuePair("user_role_id","2"));
            listNameValuePairs.add(new BasicNameValuePair("device_id",mCommon.getDeviceId(Tenant_Detail_Update_FirstActivity.this)));
            listNameValuePairs.add(new BasicNameValuePair("gsm_registration_id",""));
            listNameValuePairs.add(new BasicNameValuePair("device_type",Common.DEVICE_TYPE));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            String msg = null;
            String status = null;
            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("true"))
                    {

                        JSONArray jsonarraydata = obj.getJSONArray("data");
                        for (int i = 0; i <jsonarraydata.length(); i++) {
                            JSONObject jsonobj = jsonarraydata.getJSONObject(i);
                            suserid = jsonobj.getString("user_id");
                        }

                        {
                            Toast.makeText(getApplicationContext(),"Separate Login Provided and Primary details Are Update", Toast.LENGTH_SHORT).show();

                        }


                        dialogbooking = new ProgressDialog(Tenant_Detail_Update_FirstActivity.this);
                        dialogbooking.setMessage("Please wait");
                        dialogbooking.setCancelable(false);
                        dialogbooking.setCanceledOnTouchOutside(false);
                        dialogbooking.show();

                        RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(Tenant_Detail_Update_FirstActivity.this);
                        service.setCallBack(Tenant_Detail_Update_FirstActivity.this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,tenantId, "" + tenantId, firstName,
                                lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                                cityId, pinCode,contactNo, email, hireDate,leaveDate,imgTenanto,
                                suserid, property_id_new,ownerid_new, room_id_new,transaction_id_new, usertypevalue);
                    }
                    else if(status.equalsIgnoreCase("false"))
                    {
                        JSONArray jsonarraydata = obj.getJSONArray("data");
                        for (int i = 0; i <jsonarraydata.length(); i++) {
                            JSONObject jsonobj = jsonarraydata.getJSONObject(i);
                            suserid = jsonobj.getString("user_id");
                        }
                        Toast.makeText(getApplicationContext(),"Only Booking", Toast.LENGTH_SHORT).show();
                        String mainuserid=SharedStorage.getInstance(Tenant_Detail_Update_FirstActivity.this).getUserId();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();//Registerd Successfully mes

                        dialogbooking = new ProgressDialog(Tenant_Detail_Update_FirstActivity.this);
                        dialogbooking.setMessage("Please wait");
                        dialogbooking.setCancelable(false);
                        dialogbooking.setCanceledOnTouchOutside(false);
                        dialogbooking.show();

                        RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(Tenant_Detail_Update_FirstActivity.this);
                        service.setCallBack(Tenant_Detail_Update_FirstActivity.this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,tenantId, "" + tenantId, firstName,
                                lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                                cityId, pinCode, contactNo, email, hireDate, leaveDate, imgTenanto,
                                suserid, property_id_new,ownerid_new, room_id_new,transaction_id_new, usertypevalue);
                    } else {
                        Toast.makeText(getApplicationContext(),"Already booking with this mobile no,Please Change", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //

    // for separte tenant login  old working
   /* public class UpdateUserDetail extends AsyncTask<String, String, String> {
        String namemainvalue,emailmainvalue,mobileNomainvalue;

        public UpdateUserDetail(String namemainvalue, String emailmainvalue, String mobileNomainvalue) {

            this.namemainvalue =namemainvalue;
            this.emailmainvalue=emailmainvalue;
            this.mobileNomainvalue=mobileNomainvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Tenant_Detail_Update_FirstActivity.this);
            dialog.setMessage("Please wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action","tenantlogin"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",property_id_new));
            listNameValuePairs.add(new BasicNameValuePair("room_id",room_id_new));
            listNameValuePairs.add(new BasicNameValuePair("name",namemainvalue));
            listNameValuePairs.add(new BasicNameValuePair("email",emailmainvalue));
            listNameValuePairs.add(new BasicNameValuePair("mobile",mobileNomainvalue));
            listNameValuePairs.add(new BasicNameValuePair("user_role_id","2"));
            listNameValuePairs.add(new BasicNameValuePair("device_id",mCommon.getDeviceId(Tenant_Detail_Update_FirstActivity.this)));
            listNameValuePairs.add(new BasicNameValuePair("gsm_registration_id",""));
            listNameValuePairs.add(new BasicNameValuePair("device_type",Common.DEVICE_TYPE));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            String msg = null;
            String status = null;
            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("true"))
                    {

                        JSONArray jsonarraydata = obj.getJSONArray("data");
                        for (int i = 0; i <jsonarraydata.length(); i++) {
                            JSONObject jsonobj = jsonarraydata.getJSONObject(i);
                            suserid = jsonobj.getString("user_id");
                        }

                        {
                            Toast.makeText(getApplicationContext(),"Separate Login Provided and Primary details Are Update", Toast.LENGTH_SHORT).show();

                        }
                        RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(Tenant_Detail_Update_FirstActivity.this);
                        service.setCallBack(Tenant_Detail_Update_FirstActivity.this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,tenantId, "" + tenantId, firstName,
                                lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                                cityId, pinCode,contactNo, email, hireDate,leaveDate,imgTenanto,
                                suserid, property_id_new,ownerid_new, room_id_new,transaction_id_new, usertypevalue);
                    }
                    else if(status.equalsIgnoreCase("false"))
                    {
                        JSONArray jsonarraydata = obj.getJSONArray("data");
                        for (int i = 0; i <jsonarraydata.length(); i++) {
                            JSONObject jsonobj = jsonarraydata.getJSONObject(i);
                            suserid = jsonobj.getString("user_id");
                        }
                        Toast.makeText(getApplicationContext(),"Only Booking", Toast.LENGTH_SHORT).show();
                        String mainuserid=SharedStorage.getInstance(Tenant_Detail_Update_FirstActivity.this).getUserId();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();//Registerd Successfully mes
                        RCUTenantDetailAsynctask service = new RCUTenantDetailAsynctask(Tenant_Detail_Update_FirstActivity.this);
                        service.setCallBack(Tenant_Detail_Update_FirstActivity.this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,tenantId, "" + tenantId, firstName,
                                lastName, fatherName, fatherContactno, "", flatPlotno, landMark, areaId, stateId,
                                cityId, pinCode, contactNo, email, hireDate, leaveDate, imgTenanto,
                                suserid, property_id_new,ownerid_new, room_id_new,transaction_id_new, usertypevalue);
                    } else {
                        Toast.makeText(getApplicationContext(),"Already booking with this mobile no,Please Change", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
    //

}
