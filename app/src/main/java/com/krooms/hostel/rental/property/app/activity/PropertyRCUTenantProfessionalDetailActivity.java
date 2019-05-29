package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.Utility.UtilityImg;
import com.krooms.hostel.rental.property.app.asynctask.RCUTenantProfessionalDetailAsynctask;
import com.krooms.hostel.rental.property.app.asynctask.UserIdProofServiceAsynctTask;
import com.krooms.hostel.rental.property.app.callback.DataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.database.GetWorkDetailAsyncTask;
import com.krooms.hostel.rental.property.app.dialog.TermsAndCondition_ActivityDailog;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.modal.TenantUserIdProofModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
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


public class PropertyRCUTenantProfessionalDetailActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceResponce, DataBaseResponce {

    PropertyUserModal propertyUserModal;
    private EditText edtOfficeAddress, edtUserName, edtProfContactno, edtGuarantorContactno,
            edtAdddress, edtInfoPhoneno, edtMobileno, edtVehicleno, edtVoterId, edtDrivingLicno,
            edtArmLicenseno, edtAadhaarCardno, nameOfIssuingOfcDrivingLicens, nameOfIssuingOfcArmLicens, edtPassport, edtRashanCard, edtOtherId, edtDetailVerification;
    private Spinner spinnerWorkDetail;
    private Button btnSubmit, btnVehicleImage, btnVoteridImage, btnDrivingLicImage,btntermscondition,
            btnAadhaarCardImage, btnArmLicensImage, btnPassportImage, btnRashanCardImage, btnOtherIdImage;
    private TextView txtBack;
    private ProgressBar mprogressBar;
    private Common mCommon;
    private Validation mValidation;
    private String imgVehicleId = "", imgVoterId = "", imgDrivingLicense = "", imgAadhaarCard = "", imgArmLicId = "", workDetail = "", imgPassport = "", imgRashanCard = "", imgOtherId = "";
    private String _isImageFlag = "";
    private ArrayList<StateModal> mArrayWorkDetail;
    private StateSpinnerAdapter mWorkSpinnerAdapter;

    private ImageView vehicle_nodoc_img, votar_iddoc_img, driving_lic_nodoc_img,
            aadhaar_card_nodoc_img, arm_licences_nodoc_img, passport_doc_img, rashanCard_doc_img, other_doc_img;
    private boolean isUpdate = false;

    ArrayList<TenantUserIdProofModal> idProofModal = new ArrayList<>();
    private SharedStorage mSharedStorage;

    private Context mContext;

    public static final int DATA_LOAD_TIME = 1000;
    String ImagePath = "";
    Uri uri = null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_rcu_tenant_professional_detail);
        mContext = this;
        mSharedStorage = SharedStorage.getInstance(this);
        isUpdate = getIntent().getExtras().getBoolean("isUpdate", false);

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

        if (getIntent().getParcelableExtra("modaltenant")!=null)
        {
            propertyUserModal=getIntent().getParcelableExtra("modaltenant");
            String tid= propertyUserModal.getTenant_id();
            Log.d("td",tid);
        }
        createViews();
        setListeners();


        btntermscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String propertyid="";
                mSharedStorage = SharedStorage.getInstance(PropertyRCUTenantProfessionalDetailActivity.this);
                if (mSharedStorage.getUserType().equals("2") || mSharedStorage.getUserType().equals("4")){
                     propertyid= mSharedStorage.getBookedPropertyId();
                }else{
                    propertyid= mSharedStorage.getUserPropertyId();
                }

                String rcuId = getIntent().getStringExtra("RCUId");
                Intent intent=new Intent(PropertyRCUTenantProfessionalDetailActivity.this, TermsAndCondition_ActivityDailog.class);
                intent.putExtra("Tenantid",rcuId);
                intent.putExtra("propertyid",propertyid);
                startActivity(intent);


            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // call local update data method


                if (savedInstanceState != null) {
                    setDataOnSavedState(savedInstanceState);
                } else {
                    setDataUpdata();
                }
            }
        }, DATA_LOAD_TIME);
    }

    private void createViews() {

        edtOfficeAddress = (EditText) findViewById(R.id.edittext_institute_office_address);
        edtProfContactno = (EditText) findViewById(R.id.edittext_prof_contact_no);

        edtUserName = (EditText) findViewById(R.id.edittext_name);
        edtGuarantorContactno = (EditText) findViewById(R.id.edittext_guarantor_contact_no);
        edtAdddress = (EditText) findViewById(R.id.edittext_prmanent_address);

        edtInfoPhoneno = (EditText) findViewById(R.id.edittext_info_phoneno);
        edtMobileno = (EditText) findViewById(R.id.edittext_info_mobileno);
        edtVehicleno = (EditText) findViewById(R.id.edittext_vehicle_no);
        edtVoterId = (EditText) findViewById(R.id.edittext_votar_id);
        edtDrivingLicno = (EditText) findViewById(R.id.edittext_driving_lic_no);
        edtAadhaarCardno = (EditText) findViewById(R.id.edittext_aadhaar_card_no);
        edtArmLicenseno = (EditText) findViewById(R.id.edittext_arm_licences_no);

        nameOfIssuingOfcDrivingLicens = (EditText) findViewById(R.id.driving_lic_issuing_office);
        nameOfIssuingOfcArmLicens = (EditText) findViewById(R.id.armLicenceIssuing_office);
        edtPassport = (EditText) findViewById(R.id.pasport_no);
        edtRashanCard = (EditText) findViewById(R.id.RashanCard_no);
        edtOtherId = (EditText) findViewById(R.id.otherIdentityCard_no);
        edtDetailVerification = (EditText) findViewById(R.id.detailsVerificationInput);


        vehicle_nodoc_img = (ImageView) findViewById(R.id.vehicle_nodoc_img);
        votar_iddoc_img = (ImageView) findViewById(R.id.votar_iddoc_img);
        driving_lic_nodoc_img = (ImageView) findViewById(R.id.driving_lic_nodoc_img);
        aadhaar_card_nodoc_img = (ImageView) findViewById(R.id.aadhaar_card_nodoc_img);
        arm_licences_nodoc_img = (ImageView) findViewById(R.id.arm_licences_nodoc_img);

        passport_doc_img = (ImageView) findViewById(R.id.pasport_doc_img);
        rashanCard_doc_img = (ImageView) findViewById(R.id.RashanCard_doc_img);
        other_doc_img = (ImageView) findViewById(R.id.otherIdentityCard_doc_img);


        spinnerWorkDetail = (Spinner) findViewById(R.id.spinner_work_detail);

        btnVehicleImage = (Button) findViewById(R.id.button_browse_vehicle_no);
        btnVoteridImage = (Button) findViewById(R.id.button_browse_voter_id);
        btnDrivingLicImage = (Button) findViewById(R.id.button_browse_driving_lic_no);
        btnAadhaarCardImage = (Button) findViewById(R.id.button_browse_aadhaar_card_no);
        btnArmLicensImage = (Button) findViewById(R.id.button_browse_arm_licences_no);

        btnPassportImage = (Button) findViewById(R.id.button_browse_pasport);
        btnRashanCardImage = (Button) findViewById(R.id.button__browse_RashanCard);
        btnOtherIdImage = (Button) findViewById(R.id.button__browse_otherIdentityCard);

        btnSubmit = (Button) findViewById(R.id.button_submit);
        btnSubmit.setClickable(true);
        txtBack = (TextView) findViewById(R.id.textView_title);

        btntermscondition=(Button)findViewById(R.id.button_terms);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);
    }

    public void setDataUpdata() {

        int postion = getIntent().getExtras().getInt("tenant_no", 1) - 1;
      //  Toast.makeText(PropertyRCUTenantProfessionalDetailActivity.this, "tenant count-"+postion+"Rcu Id"+getIntent().getStringExtra("RCUId"), Toast.LENGTH_SHORT).show();
//        if (isUpdate) {
        edtOfficeAddress.setText(Common.rcuDetails.get(postion).getTenant_office_institute());
        edtUserName.setText(Common.rcuDetails.get(postion).getGuarantor_name());
        edtProfContactno.setText(Common.rcuDetails.get(postion).getTenant_contact_number());
        edtGuarantorContactno.setText(Common.rcuDetails.get(postion).getGuarantor_contact_number());
        edtAdddress.setText(Common.rcuDetails.get(postion).getGuarantor_address());
        edtVehicleno.setText(Common.rcuDetails.get(postion).getVehicle_registration_no());
        edtVoterId.setText(Common.rcuDetails.get(postion).getVoter_id_card_no());
        edtDrivingLicno.setText(Common.rcuDetails.get(postion).getDriving_license_no());
        edtAadhaarCardno.setText(Common.rcuDetails.get(postion).getAadhar_card_no());
        edtArmLicenseno.setText(Common.rcuDetails.get(postion).getArm_licence_no());
        edtInfoPhoneno.setText(Common.rcuDetails.get(postion).getTelephone_number());

        nameOfIssuingOfcDrivingLicens.setText(Common.rcuDetails.get(postion).getDriving_licence_issu_ofc_name());
        nameOfIssuingOfcArmLicens.setText(Common.rcuDetails.get(postion).getArm_licence_issu_ofc_name());
        edtPassport.setText(Common.rcuDetails.get(postion).getPassport_no());
        edtRashanCard.setText(Common.rcuDetails.get(postion).getRashan_card_no());
        edtOtherId.setText(Common.rcuDetails.get(postion).getOtherid_no());
        edtDetailVerification.setText(Common.rcuDetails.get(postion).getDetail_verification());
        edtMobileno.setText(Common.rcuDetails.get(postion).getMobile_number());
        workDetail = Common.rcuDetails.get(postion).getTenant_work_detail();

        if (mArrayWorkDetail.size() > 0 && workDetail != null && workDetail.length() > 0) {
            for (int i = 0; i < mArrayWorkDetail.size(); i++) {
                if (mArrayWorkDetail.get(i).getStrStateName().equals(workDetail)) {
                    spinnerWorkDetail.setSelection(i);
                }
            }
        } else if (mArrayWorkDetail != null && mArrayWorkDetail.size() > 0) {
            System.out.println("   workdetail    ");
            spinnerWorkDetail.setSelection(0);
        }

        if (!Common.rcuDetails.get(postion).getVehicle_registration_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getVehicle_registration_photo().equals("")) {


            String filename = Common.rcuDetails.get(postion).getVehicle_registration_photo()
                    .substring(Common.rcuDetails.get(postion).getVehicle_registration_photo().lastIndexOf("/") + 1);
            btnVehicleImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getVehicle_registration_photo());
            modal.setIdProofType("vehicle");
            idProofModal.add(modal);

            imgVehicleId = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getVehicle_registration_photo();


            vehicle_nodoc_img.setVisibility(View.VISIBLE);
                /*Picasso.with(this)
                        .load(imgVehicleId)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(vehicle_nodoc_img);*/
            Common.loadImage(mContext, imgVehicleId, vehicle_nodoc_img);
        }
        if (!Common.rcuDetails.get(postion).getVoter_id_card_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getVoter_id_card_photo().equals("")) {


            String filename = Common.rcuDetails.get(postion).getVoter_id_card_photo().substring
                    (Common.rcuDetails.get(postion).getVoter_id_card_photo().lastIndexOf("/") + 1);
            btnVoteridImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getVoter_id_card_photo());
            modal.setIdProofType("voter");
            idProofModal.add(modal);
                /*String filename=imgVoterId.substring(imgVoterId.lastIndexOf("/")+1);
                btnVoteridImage.setText(""+filename);*/
            votar_iddoc_img.setVisibility(View.VISIBLE);

            imgVoterId = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getVoter_id_card_photo();
                /*Picasso.with(this)
                        .load(imgVoterId)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(votar_iddoc_img);*/
            Common.loadImage(mContext, imgVoterId, votar_iddoc_img);
        }
        if (!Common.rcuDetails.get(postion).getDriving_license_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getDriving_license_photo().equals("")) {
                /*imgDrivingLicense = Common.profileImagePath;
                String filename=imgDrivingLicense.substring(imgDrivingLicense.lastIndexOf("/")+1);
                btnDrivingLicImage.setText(""+filename);*/
            String filename = Common.rcuDetails.get(postion).getDriving_license_photo().substring(
                    Common.rcuDetails.get(postion).getDriving_license_photo().lastIndexOf("/") + 1);
            btnDrivingLicImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getDriving_license_photo());

            modal.setIdProofType("driving");
            idProofModal.add(modal);
            driving_lic_nodoc_img.setVisibility(View.VISIBLE);

            imgDrivingLicense = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getDriving_license_photo();
                /*Picasso.with(this)
                        .load(imgDrivingLicense)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(driving_lic_nodoc_img);*/
            Common.loadImage(mContext, imgDrivingLicense, driving_lic_nodoc_img);
        }
        if (!Common.rcuDetails.get(postion).getAadhar_card_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getAadhar_card_photo().equals("")) {
            String filename = Common.rcuDetails.get(postion).getAadhar_card_photo().substring(
                    Common.rcuDetails.get(postion).getAadhar_card_photo().lastIndexOf("/") + 1);
            btnAadhaarCardImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getAadhar_card_photo());
//                vehicle ,  voter ,  driving ,  rashan ,  aadhar ,  arm
            modal.setIdProofType("aadhar");
            idProofModal.add(modal);
            aadhaar_card_nodoc_img.setVisibility(View.VISIBLE);
            imgAadhaarCard = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getAadhar_card_photo();
                /*Picasso.with(this)
                        .load(imgAadhaarCard)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(aadhaar_card_nodoc_img);*/
            Common.loadImage(mContext, imgAadhaarCard, aadhaar_card_nodoc_img);
        }
        if (!Common.rcuDetails.get(postion).getArm_licence_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getArm_licence_photo().equals("")) {
            String filename = Common.rcuDetails.get(postion).getArm_licence_photo().substring(
                    Common.rcuDetails.get(postion).getArm_licence_photo().lastIndexOf("/") + 1);
            btnArmLicensImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getArm_licence_photo());
            modal.setIdProofType("arm");
            idProofModal.add(modal);
            arm_licences_nodoc_img.setVisibility(View.VISIBLE);
            imgArmLicId = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getArm_licence_photo();
                /*Picasso.with(this)
                        .load(imgArmLicId)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(arm_licences_nodoc_img);*/
            Common.loadImage(mContext, imgArmLicId, arm_licences_nodoc_img);
        }

        if (!Common.rcuDetails.get(postion).getPassport_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getPassport_photo().equals("")) {
            String filename = Common.rcuDetails.get(postion).getPassport_photo().substring(
                    Common.rcuDetails.get(postion).getPassport_photo().lastIndexOf("/") + 1);
            btnPassportImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getPassport_photo());
            modal.setIdProofType("passport");
            idProofModal.add(modal);
            passport_doc_img.setVisibility(View.VISIBLE);
            imgPassport = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getPassport_photo();
                /*Picasso.with(this)
                        .load(imgPassport)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(passport_doc_img);*/
            Common.loadImage(mContext, imgPassport, passport_doc_img);
        }

        if (!Common.rcuDetails.get(postion).getRashan_card_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getRashan_card_photo().equals("")) {
            String filename = Common.rcuDetails.get(postion).getRashan_card_photo().substring(
                    Common.rcuDetails.get(postion).getRashan_card_photo().lastIndexOf("/") + 1);
            btnRashanCardImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getRashan_card_photo());
            modal.setIdProofType("rashan");
            idProofModal.add(modal);
            rashanCard_doc_img.setVisibility(View.VISIBLE);
            imgRashanCard = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getRashan_card_photo();
                /*Picasso.with(this)
                        .load(imgRashanCard)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(rashanCard_doc_img);*/
            Common.loadImage(mContext, imgRashanCard, rashanCard_doc_img);
        }

        if (!Common.rcuDetails.get(postion).getOtherid_photo().contains("no_image.jpg")
                && !Common.rcuDetails.get(postion).getOtherid_photo().equals("")) {
            String filename = Common.rcuDetails.get(postion).getOtherid_photo().substring(
                    Common.rcuDetails.get(postion).getOtherid_photo().lastIndexOf("/") + 1);
            btnOtherIdImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(Common.rcuDetails.get(postion).getOtherid_photo());
            modal.setIdProofType("other");
            idProofModal.add(modal);
            other_doc_img.setVisibility(View.VISIBLE);
            imgOtherId = WebUrls.IMG_URL + "" + Common.rcuDetails.get(postion).getOtherid_photo();
                /*Picasso.with(this)
                        .load(imgOtherId)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(other_doc_img);*/
            Common.loadImage(mContext, imgOtherId, other_doc_img);
        }


//        }
    }

    private void setListeners() {

        mCommon = new Common();
        mValidation = new Validation(this);

        txtBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        btnVehicleImage.setOnClickListener(this);
        btnVoteridImage.setOnClickListener(this);
        btnDrivingLicImage.setOnClickListener(this);
        btnAadhaarCardImage.setOnClickListener(this);
        btnArmLicensImage.setOnClickListener(this);

        btnPassportImage.setOnClickListener(this);
        btnRashanCardImage.setOnClickListener(this);
        btnOtherIdImage.setOnClickListener(this);


        vehicle_nodoc_img.setOnClickListener(this);
        votar_iddoc_img.setOnClickListener(this);
        driving_lic_nodoc_img.setOnClickListener(this);
        aadhaar_card_nodoc_img.setOnClickListener(this);
        arm_licences_nodoc_img.setOnClickListener(this);

        passport_doc_img.setOnClickListener(this);
        rashanCard_doc_img.setOnClickListener(this);
        other_doc_img.setOnClickListener(this);
       // btntermscondition.setOnClickListener(this);


        // Search area data
        mArrayWorkDetail = new ArrayList<>();
        GetWorkDetailAsyncTask mGetAreaAsyncTask = new GetWorkDetailAsyncTask(this);
        mGetAreaAsyncTask.setCallBack(this);
        mGetAreaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        StateModal mWorkDetail = new StateModal();
        mWorkDetail.setStrSateId("0");
        mWorkDetail.setStrStateName("Select Work Detail");
        mArrayWorkDetail.add(mWorkDetail);
        mWorkSpinnerAdapter = new StateSpinnerAdapter(this, R.id.spinner_work_detail, mArrayWorkDetail);
        spinnerWorkDetail.setAdapter(mWorkSpinnerAdapter);
        spinnerWorkDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workDetail = mArrayWorkDetail.get(position).getStrStateName();
                if (position != 0) {
                    //areaId = mArrayWorkDetail.get(position).getStrSateId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this, txtBack);
        switch (v.getId()) {
            case R.id.textView_title:

                mCommon.hideKeybord(this, txtBack);
                backpreedialog();
               // this.finish();
               // this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                break;
            case R.id.button_browse_vehicle_no:
                _isImageFlag = "vehicle";
                takePhoto();
                break;
            case R.id.button_browse_voter_id:
                _isImageFlag = "voter";
                takePhoto();
                break;
            case R.id.button_browse_driving_lic_no:
                _isImageFlag = "driving";
                takePhoto();
                break;
            case R.id.button_browse_aadhaar_card_no:
                _isImageFlag = "aadhar";
                takePhoto();
                break;
            case R.id.button_browse_arm_licences_no:
                _isImageFlag = "arm";
                takePhoto();
                break;
            case R.id.button_browse_pasport:
                _isImageFlag = "passport";
                takePhoto();
                break;

            case R.id.button__browse_RashanCard:
                _isImageFlag = "rashan";
                takePhoto();
                break;

            case R.id.button__browse_otherIdentityCard:
                _isImageFlag = "other";
                takePhoto();
                break;
            case R.id.button_submit:
                submitBtnClickEvent();
                break;
            case R.id.vehicle_nodoc_img:
               /* ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgVehicleId, img);

                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                vehicleImagePreviewDialog.getPerameter(this, "Vehicle No", true);
                vehicleImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
*/
                CustomDialogClassFullScreen cdd = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(this, "Vehicle No", true);
                cdd.show();

                break;
            case R.id.votar_iddoc_img:

               /* ImagePreviewDialog votarImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgVoterId, img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                votarImagePreviewDialog.getPerameter(this, "Voter Id", true);
                votarImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
*/
                CustomDialogClassFullScreen cddVoterid = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddVoterid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddVoterid.getPerameter(this, "Voter Id", true);
                cddVoterid.show();

                break;
            case R.id.driving_lic_nodoc_img:

              /*  ImagePreviewDialog drivingImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgDrivingLicense, img);


                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                drivingImagePreviewDialog.getPerameter(this, "Driving Licences", true);
                drivingImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
*/
                CustomDialogClassFullScreen cddDriving = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddDriving.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddDriving.getPerameter(this, "Driving Licences", true);
                cddDriving.show();
                break;
            case R.id.aadhaar_card_nodoc_img:

                /*ImagePreviewDialog aadhaarImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgAadhaarCard, img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                aadhaarImagePreviewDialog.getPerameter(this, "Aadhaar Card", true);
                aadhaarImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");*/


                CustomDialogClassFullScreen cddAadhaar = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddAadhaar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddAadhaar.getPerameter(this, "Aadhaar Card", true);
                cddAadhaar.show();

                break;
            case R.id.arm_licences_nodoc_img:
/*
                ImagePreviewDialog armImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgArmLicId, img);

                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                armImagePreviewDialog.getPerameter(this, "Arm Licences", true);
                armImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");*/

                CustomDialogClassFullScreen cddArm = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddArm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddArm.getPerameter(this, "Arm Licences", true);
                cddArm.show();
                break;

            case R.id.pasport_doc_img:

               /* ImagePreviewDialog passportImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgPassport, img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                passportImagePreviewDialog.getPerameter(this, "PassPort", true);
                passportImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");*/

                CustomDialogClassFullScreen cddPassPort = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddPassPort.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddPassPort.getPerameter(this, "PassPort", true);
                cddPassPort.show();
                break;

            case R.id.RashanCard_doc_img:

             /*   ImagePreviewDialog rashanCardImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgRashanCard, img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                rashanCardImagePreviewDialog.getPerameter(this, "Rashan Card", true);
                rashanCardImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
*/
                CustomDialogClassFullScreen cddRashan = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddRashan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddRashan.getPerameter(this, "Rashan Card", true);
                cddRashan.show();
                break;
            case R.id.otherIdentityCard_doc_img:

               /* ImagePreviewDialog otherIdPreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, imgOtherId, img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                otherIdPreviewDialog.getPerameter(this, "Other Id", true);
                otherIdPreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
*/
                CustomDialogClassFullScreen cddOther = new CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity.this);
                cddOther.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cddOther.getPerameter(this, "Other Id", true);
                cddOther.show();

                break;

           /* //open terms dailog
            case R.id.button_terms:


                break;
*/

        }
    }

    @Override
    public void requestDataBaseResponce(ArrayList<StateModal> mArray) {
        mArrayWorkDetail.clear();
        mArrayWorkDetail.addAll(mArray);
        if (mArrayWorkDetail.size() > 0 && workDetail != null && workDetail.length() > 0) {
            for (int i = 0; i < mArrayWorkDetail.size(); i++) {
                if (mArrayWorkDetail.get(i).getStrStateName().equals(workDetail)) {
                    spinnerWorkDetail.setSelection(i);
                }
            }
        } else if (mArrayWorkDetail != null && mArrayWorkDetail.size() > 0) {
            spinnerWorkDetail.setSelection(0);
        }
        mWorkSpinnerAdapter.notifyDataSetChanged();
    }

    public void takePhoto() {

     /*   Intent mIntent = new Intent(this, PhotoCaptureActivity.class);
        mIntent.putExtra("photo_purpose", "idProof");
        startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);*/

        selectImage();
    }


    /*image capture code start*/
    public void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyRCUTenantProfessionalDetailActivity.this);
        builder.setTitle("Select Image");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PropertyRCUTenantProfessionalDetailActivity.this, android.R.layout.simple_list_item_1);
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
                            Toast toast = Toast.makeText(PropertyRCUTenantProfessionalDetailActivity.this, "Please give internal storage permission", Toast.LENGTH_SHORT);
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
            uri = UtilityImg.getExternalFilesDirForVersion24Above(PropertyRCUTenantProfessionalDetailActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
        } else {
            uri = UtilityImg.getExternalFilesDirForVersion24Below(PropertyRCUTenantProfessionalDetailActivity.this, Environment.DIRECTORY_PICTURES, APP_TAG, fileName);
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
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String profileImageUpload = String.valueOf(finalFile);
                        Common.profileImagePath = profileImageUpload;


                        if (idProofModal.size() != 0) {
                            for (int i = 0; i < idProofModal.size(); i++) {
                                if (idProofModal.get(i).getIdProofType().equals(_isImageFlag)) {
                                    idProofModal.get(i).setIdProofImagePath(Common.profileImagePath);
                                    idProofModal.get(i).setFromLocalOrServer(TenantUserIdProofModal.FROMLOCAL);
                                } else {
                                    TenantUserIdProofModal modal = new TenantUserIdProofModal();
                                    modal.setIdProofType(_isImageFlag);
                                    modal.setIdProofImagePath(Common.profileImagePath);
                                    modal.setFromLocalOrServer(TenantUserIdProofModal.FROMLOCAL);
                                    idProofModal.add(modal);
                                }
                            }


                        } else {
                            TenantUserIdProofModal modal = new TenantUserIdProofModal();
                            modal.setIdProofType(_isImageFlag);
                            modal.setIdProofImagePath(Common.profileImagePath);
                            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMLOCAL);
                            idProofModal.add(modal);

                        }

                        String rcuId = getIntent().getStringExtra("RCUId");
                        callIdProofImage(mSharedStorage.getUserId(), rcuId, _isImageFlag, Common.profileImagePath);

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






   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Common.Config(" Image path      " + Common.profileImagePath);

        try {
            if (resultCode == Common.CAMERA_CAPTURE_REQUEST) {

                if (idProofModal.size() != 0) {
                    for (int i = 0; i < idProofModal.size(); i++) {
                        if (idProofModal.get(i).getIdProofType().equals(_isImageFlag)) {
                            idProofModal.get(i).setIdProofImagePath(Common.profileImagePath);
                            idProofModal.get(i).setFromLocalOrServer(TenantUserIdProofModal.FROMLOCAL);
                        } else {
                            TenantUserIdProofModal modal = new TenantUserIdProofModal();
                            modal.setIdProofType(_isImageFlag);
                            modal.setIdProofImagePath(Common.profileImagePath);
                            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMLOCAL);
                            idProofModal.add(modal);
                        }
                    }


                } else {
                    TenantUserIdProofModal modal = new TenantUserIdProofModal();
                    modal.setIdProofType(_isImageFlag);
                    modal.setIdProofImagePath(Common.profileImagePath);
                    modal.setFromLocalOrServer(TenantUserIdProofModal.FROMLOCAL);
                    idProofModal.add(modal);

                }

                String rcuId = getIntent().getStringExtra("RCUId");
                callIdProofImage(mSharedStorage.getUserId(), rcuId, _isImageFlag, Common.profileImagePath);


            } else {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            LogConfig.logd("Exception =", "" + e.getMessage());
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LogConfig.logd("Exception =", "" + e.getMessage());
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }


    }
*/

    public void submitBtnClickEvent() {
        try {
            if (imgVehicleId == null) {
                imgVehicleId = "";
            }

            String rcuId = getIntent().getStringExtra("RCUId");

            String officeAddress = edtOfficeAddress.getText().toString().trim();
            String profContactNo = edtProfContactno.getText().toString().trim();
            String guarantorName = edtUserName.getText().toString().trim();
            String guarantorContactNo = edtGuarantorContactno.getText().toString().trim();
            String guarantorAddress = edtAdddress.getText().toString().trim();
            String infoPhoneno = edtInfoPhoneno.getText().toString().trim();
            String mobileno = edtMobileno.getText().toString().trim();
            String vehicleNo = edtVehicleno.getText().toString().trim();
            String voterId = edtVoterId.getText().toString().trim();
            String drivingLincenceno = edtDrivingLicno.getText().toString().trim();
            String aadhaarCardno = edtAadhaarCardno.getText().toString().trim();
            String armLicenceno = edtArmLicenseno.getText().toString().trim();

            String drivingIssuOfc = nameOfIssuingOfcDrivingLicens.getText().toString().trim();
            String armIssuOfc = nameOfIssuingOfcArmLicens.getText().toString().trim();
            String passPortNo = edtPassport.getText().toString().trim();
            String rashanCardNo = edtRashanCard.getText().toString().trim();
            String otherIdNo = edtOtherId.getText().toString().trim();
            String detailOfVerification = edtDetailVerification.getText().toString().trim();

            if (vehicleNo.length() == 0 && voterId.length() == 0 && drivingLincenceno.length() == 0 &&
                    aadhaarCardno.length() == 0 && armLicenceno.length() == 0 && passPortNo.length() == 0 &&
                    rashanCardNo.length() == 0 && otherIdNo.length() == 0) {
                mCommon.displayAlert(this, "Enter atleast one id proof no/id.", false);
            } else {
                mCommon.hideKeybord(this, edtOfficeAddress);
                if (!mValidation.checkNetworkRechability()) {
                    mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
                } else {
                    btnSubmit.setClickable(false);
                    mprogressBar.setVisibility(View.VISIBLE);
                    RCUTenantProfessionalDetailAsynctask service = new RCUTenantProfessionalDetailAsynctask(this);
                    service.setCallBack(this);
                    service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, rcuId, officeAddress, profContactNo, workDetail,
                            guarantorName, guarantorAddress, guarantorContactNo, vehicleNo, voterId,
                            drivingLincenceno, infoPhoneno, mobileno, aadhaarCardno, armLicenceno,
                            drivingIssuOfc, armIssuOfc, passPortNo, rashanCardNo, otherIdNo, detailOfVerification);
                }

            }
        } catch (NullPointerException e) {

        } catch (IndexOutOfBoundsException e) {

        }

    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        LogConfig.logd("Rcu professional detail response =", "" + result);
        Common.Config("result    " + result);
        try {
            btnSubmit.setClickable(true);
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    reSetData();
                    finish();
                    overridePendingTransition(R.anim.slide_for_in, R.anim.slide_for_out);
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }

    public void reSetData() {
        int position = getIntent().getExtras().getInt("tenant_no", 1) - 1;
        if (Common.rcuDetails.size() > position) {
            Common.rcuDetails.get(position).setTenant_office_institute(edtOfficeAddress.getText().toString().trim());
            Common.rcuDetails.get(position).setGuarantor_name(edtUserName.getText().toString().trim());
            Common.rcuDetails.get(position).setTenant_contact_number(edtProfContactno.getText().toString().trim());
            Common.rcuDetails.get(position).setGuarantor_contact_number(edtGuarantorContactno.getText().toString().trim());
            Common.rcuDetails.get(position).setGuarantor_address(edtAdddress.getText().toString().trim());
            Common.rcuDetails.get(position).setTelephone_number(edtInfoPhoneno.getText().toString().trim());
            Common.rcuDetails.get(position).setMobile_number(edtMobileno.getText().toString().trim());
            Common.rcuDetails.get(position).setVehicle_registration_no(edtVehicleno.getText().toString().trim());
            Common.rcuDetails.get(position).setVoter_id_card_no(edtVoterId.getText().toString().trim());
            Common.rcuDetails.get(position).setDriving_license_no(edtDrivingLicno.getText().toString().trim());
            Common.rcuDetails.get(position).setAadhar_card_no(edtAadhaarCardno.getText().toString().trim());
            Common.rcuDetails.get(position).setArm_licence_no(edtArmLicenseno.getText().toString().trim());
            Common.rcuDetails.get(position).setDriving_licence_issu_ofc_name(nameOfIssuingOfcDrivingLicens.getText().toString().trim());
            Common.rcuDetails.get(position).setArm_licence_issu_ofc_name(nameOfIssuingOfcArmLicens.getText().toString().trim());
            Common.rcuDetails.get(position).setPassport_no(edtPassport.getText().toString().trim());
            Common.rcuDetails.get(position).setRashan_card_no(edtRashanCard.getText().toString().trim());
            Common.rcuDetails.get(position).setOtherid_no(edtOtherId.getText().toString().trim());
            Common.rcuDetails.get(position).setDetail_verification(edtDetailVerification.getText().toString().trim());
            Common.rcuDetails.get(position).setTenant_work_detail(workDetail);

            for (int i = 0; i < idProofModal.size(); i++) {

                if (idProofModal.get(i).getIdProofType().equals("vehicle")) {
                    Common.rcuDetails.get(position).setVehicle_registration_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("voter")) {
                    Common.rcuDetails.get(position).setVoter_id_card_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("driving")) {
                    Common.rcuDetails.get(position).setDriving_license_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("aadhar")) {
                    Common.rcuDetails.get(position).setAadhar_card_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("arm")) {
                    Common.rcuDetails.get(position).setArm_licence_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("passport")) {
                    Common.rcuDetails.get(position).setPassport_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("rashan")) {
                    Common.rcuDetails.get(position).setRashan_card_photo(idProofModal.get(i).getIdProofImagePath());
                } else if (idProofModal.get(i).getIdProofType().equals("other")) {
                    Common.rcuDetails.get(position).setOtherid_photo(idProofModal.get(i).getIdProofImagePath());
                }

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                mCommon.hideKeybord(this, txtBack);
                backpreedialog();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void  backpreedialog()
    {
        final Dialog backpressdialog=new Dialog(PropertyRCUTenantProfessionalDetailActivity.this);
        backpressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        backpressdialog.setContentView(R.layout.alert_two_btn_dialog);
        backpressdialog.setCancelable(false);
        TextView title = (TextView)backpressdialog.findViewById(R.id.alertTitle);
        TextView message = (TextView)backpressdialog.findViewById(R.id.categoryNameInput);
        Button alertYesBtn = (Button)backpressdialog.findViewById(R.id.alertYesBtn);
        Button alertNoBtn  = (Button)backpressdialog.findViewById(R.id.alertNoBtn);

        title.setText("User Conformation");
        message.setText("Do you want to exit from this screen");

        alertYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backpressdialog.dismiss();
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);

            }
        });

        alertNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backpressdialog.dismiss();
                //Intent i=new Intent(PropertyRCUActivity.this,HostelListActivity.class);
                //startActivity(i);
            }
        });

        backpressdialog.show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("edtOfficeAddress", edtOfficeAddress.getText().toString().trim());
        outState.putString("edtUserName", edtUserName.getText().toString().trim());
        outState.putString("edtProfContactno", edtProfContactno.getText().toString().trim());
        outState.putString("edtGuarantorContactno", edtGuarantorContactno.getText().toString().trim());
        outState.putString("edtAdddress", edtAdddress.getText().toString().trim());
        outState.putString("edtInfoPhoneno", edtInfoPhoneno.getText().toString().trim());
        outState.putString("edtMobileno", edtMobileno.getText().toString().trim());
        outState.putString("edtVehicleno", edtVehicleno.getText().toString().trim());
        outState.putString("edtVoterId", edtVoterId.getText().toString().trim());
        outState.putString("edtDrivingLicno", edtDrivingLicno.getText().toString().trim());
        outState.putString("edtAadhaarCardno", edtAadhaarCardno.getText().toString().trim());
        outState.putString("edtArmLicenseno", edtArmLicenseno.getText().toString().trim());

        outState.putString("nameOfIssuingOfcDrivingLicens", nameOfIssuingOfcDrivingLicens.getText().toString().trim());
        outState.putString("nameOfIssuingOfcArmLicens", nameOfIssuingOfcArmLicens.getText().toString().trim());
        outState.putString("edtPassport", edtPassport.getText().toString().trim());
        outState.putString("edtRashanCard", edtRashanCard.getText().toString().trim());
        outState.putString("edtOtherId", edtOtherId.getText().toString().trim());
        outState.putString("edtDetailVerification", edtDetailVerification.getText().toString().trim());


        outState.putString("workDetail", workDetail);
        outState.putString("_isImageFlag", _isImageFlag);

        outState.putString("imgVehicleId", imgVehicleId);
        outState.putString("imgVoterId", imgVoterId);
        outState.putString("imgDrivingLicense", imgDrivingLicense);
        outState.putString("imgAadhaarCard", imgAadhaarCard);
        outState.putString("imgArmLicId", imgArmLicId);

        outState.putString("imgPassport", imgPassport);
        outState.putString("imgRashanCard", imgRashanCard);
        outState.putString("imgOtherId", imgOtherId);

        outState.putParcelableArrayList("rcuDetailList", Common.rcuDetails);


    }


    public void setDataOnSavedState(Bundle savedInstanceState) {

        try {

            Common.rcuDetails = savedInstanceState.getParcelableArrayList("rcuDetailList");

            edtOfficeAddress.setText(savedInstanceState.getString("edtOfficeAddress"));
            edtUserName.setText(savedInstanceState.getString("edtUserName"));
            edtProfContactno.setText(savedInstanceState.getString("edtProfContactno"));
            edtGuarantorContactno.setText(savedInstanceState.getString("edtGuarantorContactno"));
            edtAdddress.setText(savedInstanceState.getString("edtAdddress"));
            edtInfoPhoneno.setText(savedInstanceState.getString("edtInfoPhoneno"));


            edtMobileno.setText(savedInstanceState.getString("edtMobileno"));
            edtVehicleno.setText(savedInstanceState.getString("edtVehicleno"));
            edtVoterId.setText(savedInstanceState.getString("edtVoterId"));
            edtDrivingLicno.setText(savedInstanceState.getString("edtDrivingLicno"));
            edtAadhaarCardno.setText(savedInstanceState.getString("edtAadhaarCardno"));
            edtArmLicenseno.setText(savedInstanceState.getString("edtArmLicenseno"));

            nameOfIssuingOfcDrivingLicens.setText(savedInstanceState.getString("nameOfIssuingOfcDrivingLicens"));
            nameOfIssuingOfcArmLicens.setText(savedInstanceState.getString("nameOfIssuingOfcArmLicens"));
            edtPassport.setText(savedInstanceState.getString("edtPassport"));
            edtRashanCard.setText(savedInstanceState.getString("edtRashanCard"));
            edtOtherId.setText(savedInstanceState.getString("edtOtherId"));
            edtDetailVerification.setText(savedInstanceState.getString("edtDetailVerification"));

            workDetail = savedInstanceState.getString("workDetail");
            _isImageFlag = savedInstanceState.getString("_isImageFlag");

            imgVehicleId = savedInstanceState.getString("imgVehicleId");
            imgVoterId = savedInstanceState.getString("imgVoterId");
            imgDrivingLicense = savedInstanceState.getString("imgDrivingLicense");
            imgAadhaarCard = savedInstanceState.getString("imgAadhaarCard");
            imgArmLicId = savedInstanceState.getString("imgArmLicId");

            imgPassport = savedInstanceState.getString("imgPassport");
            imgRashanCard = savedInstanceState.getString("imgRashanCard");
            imgOtherId = savedInstanceState.getString("imgOtherId");


            if (mArrayWorkDetail.size() > 0 && workDetail != null && workDetail.length() > 0) {
                for (int i = 0; i < mArrayWorkDetail.size(); i++) {
                    if (mArrayWorkDetail.get(i).getStrStateName().equals(workDetail)) {
                        spinnerWorkDetail.setSelection(i);
                    }
                }
            } else if (mArrayWorkDetail != null && mArrayWorkDetail.size() > 0) {
                System.out.println("   workdetail    ");
                spinnerWorkDetail.setSelection(0);
            }

            if (!imgVehicleId.equals("")) {
                vehicle_nodoc_img.setVisibility(View.VISIBLE);
                btnVehicleImage.setText("" + imgVehicleId.substring(imgVehicleId.lastIndexOf("/") + 1));
            } else {
                btnVehicleImage.setText("Browse...");

            }

            if (vehicle_nodoc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgVehicleId, vehicle_nodoc_img);
            }
            if (!imgVoterId.equals("")) {
                votar_iddoc_img.setVisibility(View.VISIBLE);
                btnVoteridImage.setText("" + imgVoterId.substring(imgVoterId.lastIndexOf("/") + 1));
            } else {
                btnVoteridImage.setText("Browse...");

            }

            if (votar_iddoc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgVoterId, votar_iddoc_img);
            }

            if (!imgDrivingLicense.equals("")) {
                driving_lic_nodoc_img.setVisibility(View.VISIBLE);
                btnDrivingLicImage.setText("" + imgDrivingLicense.substring(imgDrivingLicense.lastIndexOf("/") + 1));
            } else {
                btnDrivingLicImage.setText("Browse...");

            }

            if (driving_lic_nodoc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgDrivingLicense, driving_lic_nodoc_img);
            }
            if (!imgAadhaarCard.equals("")) {
                aadhaar_card_nodoc_img.setVisibility(View.VISIBLE);
                btnAadhaarCardImage.setText("" + imgAadhaarCard.substring(imgAadhaarCard.lastIndexOf("/") + 1));
            } else {
                btnAadhaarCardImage.setText("Browse...");

            }

            if (aadhaar_card_nodoc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgAadhaarCard, aadhaar_card_nodoc_img);
            }
            if (!imgArmLicId.equals("")) {
                arm_licences_nodoc_img.setVisibility(View.VISIBLE);
                btnArmLicensImage.setText("" + imgArmLicId.substring(imgArmLicId.lastIndexOf("/") + 1));
            } else {
                btnArmLicensImage.setText("Browse...");

            }

            if (arm_licences_nodoc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgArmLicId, arm_licences_nodoc_img);
            }

            if (!imgPassport.equals("")) {
                passport_doc_img.setVisibility(View.VISIBLE);
                btnPassportImage.setText("" + imgPassport.substring(imgPassport.lastIndexOf("/") + 1));
            } else {
                btnPassportImage.setText("Browse...");

            }

            if (passport_doc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgPassport, passport_doc_img);
            }

            if (!imgRashanCard.equals("")) {
                rashanCard_doc_img.setVisibility(View.VISIBLE);
                btnRashanCardImage.setText("" + imgRashanCard.substring(imgRashanCard.lastIndexOf("/") + 1));
            } else {
                btnRashanCardImage.setText("Browse...");

            }
            if (rashanCard_doc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgRashanCard, rashanCard_doc_img);
            }

            if (!imgOtherId.equals("")) {
                other_doc_img.setVisibility(View.VISIBLE);
                btnOtherIdImage.setText("" + imgOtherId.substring(imgOtherId.lastIndexOf("/") + 1));
            } else {
                btnOtherIdImage.setText("Browse...");

            }
            if (other_doc_img.getVisibility() == View.VISIBLE) {
                Common.loadImage(mContext, imgOtherId, other_doc_img);
            }
        } catch (Exception e) {
        }
    }


    public void callIdProofImage(String... params) {
        UserIdProofServiceAsynctTask mySyncDatabaseTask = new UserIdProofServiceAsynctTask(this);
        mySyncDatabaseTask.setCallBack(new ServiceResponce() {
            @Override
            public void requestResponce(String result) {
                userIdResponse(result);
            }
        });

        mySyncDatabaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                /*userId*/params[0], /*rcu_id*/params[1], /*id_proof_type*/params[2].toLowerCase(), /*id_proof_image*/params[3]);
    }

    public void userIdResponse(String result) {

        Common.Config("  image upload      " + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);

                if (status.equals("true")) {
                    JSONArray jArray = jsonObject.getJSONArray(WebUrls.DATA_JSON);
                    for (int i = 0; i < idProofModal.size(); i++) {
                        if (idProofModal.get(i).getIdProofType().equals(_isImageFlag)) {
                            idProofModal.get(i).setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
                            idProofModal.get(i).setIdProofImagePath(jArray.getJSONObject(0).getString("file_path"));
                        }
                    }
                    if (_isImageFlag.equals("vehicle")) {

                        imgVehicleId = "file://" + Common.profileImagePath;
                        String filename = imgVehicleId.substring(imgVehicleId.lastIndexOf("/") + 1);
                        btnVehicleImage.setText("" + filename);
                        vehicle_nodoc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgVehicleId, vehicle_nodoc_img);
                    } else if (_isImageFlag.equals("voter")) {
                        imgVoterId = "file://" + Common.profileImagePath;
                        String filename = imgVoterId.substring(imgVoterId.lastIndexOf("/") + 1);
                        btnVoteridImage.setText("" + filename);
                        votar_iddoc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgVoterId, votar_iddoc_img);
                    } else if (_isImageFlag.equals("driving")) {
                        imgDrivingLicense = "file://" + Common.profileImagePath;
                        String filename = imgDrivingLicense.substring(imgDrivingLicense.lastIndexOf("/") + 1);
                        btnDrivingLicImage.setText("" + filename);
                        driving_lic_nodoc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgDrivingLicense, driving_lic_nodoc_img);
                    } else if (_isImageFlag.equals("aadhar")) {
                        imgAadhaarCard = "file://" + Common.profileImagePath;
                        String filename = imgAadhaarCard.substring(imgAadhaarCard.lastIndexOf("/") + 1);
                        btnAadhaarCardImage.setText("" + filename);
                        aadhaar_card_nodoc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgAadhaarCard, aadhaar_card_nodoc_img);
                    } else if (_isImageFlag.equals("arm")) {
                        imgArmLicId = "file://" + Common.profileImagePath;
                        String filename = imgArmLicId.substring(imgArmLicId.lastIndexOf("/") + 1);
                        btnArmLicensImage.setText("" + filename);
                        arm_licences_nodoc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgArmLicId, arm_licences_nodoc_img);
                    } else if (_isImageFlag.equals("passport")) {
                        imgPassport = "file://" + Common.profileImagePath;
                        String filename = imgPassport.substring(imgPassport.lastIndexOf("/") + 1);
                        btnPassportImage.setText("" + filename);
                        passport_doc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgPassport, passport_doc_img);
                    } else if (_isImageFlag.equals("rashan")) {
                        imgRashanCard = "file://" + Common.profileImagePath;
                        String filename = imgRashanCard.substring(imgRashanCard.lastIndexOf("/") + 1);
                        btnRashanCardImage.setText("" + filename);
                        rashanCard_doc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgRashanCard, rashanCard_doc_img);
                    } else if (_isImageFlag.equals("other")) {
                        imgOtherId = "file://" + Common.profileImagePath;
                        String filename = imgOtherId.substring(imgOtherId.lastIndexOf("/") + 1);
                        btnOtherIdImage.setText("" + filename);
                        other_doc_img.setVisibility(View.VISIBLE);
                        Common.loadImage(mContext, imgOtherId, other_doc_img);
                    }
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }

    }



    //show image full screen on upload
    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClassFullScreen extends Dialog {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private ImageView imageView;
        private String titleStr;
        private TextView message;
        private Button downloadImageBtn;
        private Button alertNoBtn;
        private Boolean downloadBtnHide;

        public CustomDialogClassFullScreen(PropertyRCUTenantProfessionalDetailActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, Boolean flag) {
            this.mFActivity = activity;
            this.titleStr = titleStr;
            this.downloadBtnHide = flag;
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
                if (titleStr.equals("Vehicle No")) {
                    Common.loadImage(mContext, imgVehicleId, imageView);
                } else if (titleStr.equals("Voter Id")) {
                    Common.loadImage(mContext, imgVoterId, imageView);
                } else if (titleStr.equals("Driving Licences")) {
                    Common.loadImage(mContext, imgDrivingLicense, imageView);
                } else if (titleStr.equals("Aadhaar Card")) {
                    Common.loadImage(mContext, imgAadhaarCard, imageView);
                } else if (titleStr.equals("Arm Licences")) {
                    Common.loadImage(mContext, imgArmLicId, imageView);
                } else if (titleStr.equals("PassPort")) {
                    Common.loadImage(mContext, imgPassport, imageView);
                } else if (titleStr.equals("Rashan Card")) {
                    Common.loadImage(mContext, imgRashanCard, imageView);
                } else if (titleStr.equals("Other Id")) {
                    Common.loadImage(mContext, imgOtherId, imageView);//
                }
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
                }
            });
        }

    }

    ///nnnn

}