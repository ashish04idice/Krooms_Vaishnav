package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.database.GetWorkDetailAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.RCUTenantProfessionalDetailAsynctask;
import com.krooms.hostel.rental.property.app.asynctask.UserIdProofServiceAsynctTask;
import com.krooms.hostel.rental.property.app.callback.DataBaseResponce;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.modal.TenantUserIdProofModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditRCUTenantProfessionalDetailActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce, DataBaseResponce {
    private EditText edtOfficeAddress, edtUserName, edtProfContactno, edtGuarantorContactno,
            edtAdddress, edtInfoPhoneno, edtMobileno, edtVehicleno, edtVoterId, edtDrivingLicno,
            edtArmLicenseno, edtAadhaarCardno, nameOfIssuingOfcDrivingLicens, nameOfIssuingOfcArmLicens, edtPassport, edtRashanCard, edtOtherId, edtDetailVerification;
    private Spinner spinnerWorkDetail;
    private Button btnSubmit, btnVehicleImage, btnVoteridImage, btnDrivingLicImage,
            btnAadhaarCardImage, btnArmLicensImage, btnPassportImage, btnRashanCardImage, btnOtherIdImage;
    private TextView txtBack;
    private ProgressBar mprogressBar;
    private Common mCommon;
    private Validation mValidation;
    private String imgVehicleId = "", imgVoterId = "", imgDrivingLicense = "", imgAadhaarCard = "", imgArmLicId = "", workDetail = "", imgPassport = "", imgRashanCard = "", imgOtherId = "";
    private String _isImageFlag = "";
    private ArrayList<StateModal> mArrayWorkDetail;
    private StateSpinnerAdapter mWorkSpinnerAdapter;
    private Context mContext;

    ArrayList<TenantUserIdProofModal> idProofModal = new ArrayList<>();
    private SharedStorage mSharedStorage;

    private ImageView vehicle_nodoc_img, votar_iddoc_img, driving_lic_nodoc_img,
            aadhaar_card_nodoc_img, arm_licences_nodoc_img, passport_doc_img, rashanCard_doc_img, other_doc_img;


    private PropertyUserModal tenantData;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rcutenant_professional_detail);
        mContext = this;
        mSharedStorage = SharedStorage.getInstance(this);
        tenantData = (PropertyUserModal) getIntent().getSerializableExtra("modal");

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

        createViews();
        setListeners();

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
        }, 1000);
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
        txtBack = (TextView) findViewById(R.id.textView_title);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void setDataUpdata() {

        edtOfficeAddress.setText(tenantData.getTenant_office_institute());
        edtUserName.setText(tenantData.getGuarantor_name());
        edtProfContactno.setText(tenantData.getTenant_contact_number());
        edtGuarantorContactno.setText(tenantData.getGuarantor_contact_number());
        edtAdddress.setText(tenantData.getGuarantor_address());
        edtVehicleno.setText(tenantData.getVehicle_registration_no());
        edtVoterId.setText(tenantData.getVoter_id_card_no());
        edtDrivingLicno.setText(tenantData.getDriving_license_no());
        edtAadhaarCardno.setText(tenantData.getAadhar_card_no());
        edtArmLicenseno.setText(tenantData.getArm_licence_no());
        nameOfIssuingOfcDrivingLicens.setText(tenantData.getDriving_licence_issu_ofc_name());
        nameOfIssuingOfcArmLicens.setText(tenantData.getArm_licence_issu_ofc_name());
        edtPassport.setText(tenantData.getPassport_no());
        edtRashanCard.setText(tenantData.getRashan_card_no());
        edtOtherId.setText(tenantData.getOtherid_no());
        edtDetailVerification.setText(tenantData.getDetail_verification());
        edtInfoPhoneno.setText(tenantData.getTelephone_number());
        edtMobileno.setText(tenantData.getMobile_number());
        workDetail = tenantData.getTenant_work_detail();

        if (mArrayWorkDetail.size() > 0 && workDetail != null && workDetail.length() > 0) {
            for (int i = 0; i < mArrayWorkDetail.size(); i++) {
                if (mArrayWorkDetail.get(i).getStrStateName().equals(workDetail)) {
                    spinnerWorkDetail.setSelection(i);
                }
            }
        } else if (mArrayWorkDetail != null && mArrayWorkDetail.size() > 0) {
            spinnerWorkDetail.setSelection(0);
        }

        if (!tenantData.getVehicle_registration_photo().contains("no_image.jpg")
                && !tenantData.getVehicle_registration_photo().equals("")) {


            String filename = tenantData.getVehicle_registration_photo()
                    .substring(tenantData.getVehicle_registration_photo().lastIndexOf("/") + 1);
            btnVehicleImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getVehicle_registration_photo());
            modal.setIdProofType("vehicle");
            idProofModal.add(modal);

            imgVehicleId = WebUrls.IMG_URL + "" + tenantData.getVehicle_registration_photo();

            vehicle_nodoc_img.setVisibility(View.VISIBLE);

            Common.loadImage(mContext, imgVehicleId, vehicle_nodoc_img);
        }
        if (!tenantData.getVoter_id_card_photo().contains("no_image.jpg")
                && !tenantData.getVoter_id_card_photo().equals("")) {


            String filename = tenantData.getVoter_id_card_photo().substring
                    (tenantData.getVoter_id_card_photo().lastIndexOf("/") + 1);
            btnVoteridImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getVoter_id_card_photo());
            modal.setIdProofType("voter");
            idProofModal.add(modal);
            votar_iddoc_img.setVisibility(View.VISIBLE);

            imgVoterId = WebUrls.IMG_URL + "" + tenantData.getVoter_id_card_photo();

            Common.loadImage(mContext, imgVoterId, votar_iddoc_img);
        }
        if (!tenantData.getDriving_license_photo().contains("no_image.jpg")
                && !tenantData.getDriving_license_photo().equals("")) {
            String filename = tenantData.getDriving_license_photo().substring(
                    tenantData.getDriving_license_photo().lastIndexOf("/") + 1);
            btnDrivingLicImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getDriving_license_photo());

            modal.setIdProofType("driving");
            idProofModal.add(modal);
            driving_lic_nodoc_img.setVisibility(View.VISIBLE);

            imgDrivingLicense = WebUrls.IMG_URL + "" + tenantData.getDriving_license_photo();

            Common.loadImage(mContext, imgDrivingLicense, driving_lic_nodoc_img);
        }
        if (!tenantData.getAadhar_card_photo().contains("no_image.jpg")
                && !tenantData.getAadhar_card_photo().equals("")) {
            String filename = tenantData.getAadhar_card_photo().substring(
                    tenantData.getAadhar_card_photo().lastIndexOf("/") + 1);
            btnAadhaarCardImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getAadhar_card_photo());
            modal.setIdProofType("aadhar");
            idProofModal.add(modal);
            aadhaar_card_nodoc_img.setVisibility(View.VISIBLE);
            imgAadhaarCard = WebUrls.IMG_URL + "" + tenantData.getAadhar_card_photo();

            Common.loadImage(mContext, imgAadhaarCard, aadhaar_card_nodoc_img);
        }
        if (!tenantData.getArm_licence_photo().contains("no_image.jpg")
                && !tenantData.getArm_licence_photo().equals("")) {
            String filename = tenantData.getArm_licence_photo().substring(
                    tenantData.getArm_licence_photo().lastIndexOf("/") + 1);
            btnArmLicensImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getArm_licence_photo());
            modal.setIdProofType("arm");
            idProofModal.add(modal);
            arm_licences_nodoc_img.setVisibility(View.VISIBLE);
            imgArmLicId = WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo();

            Common.loadImage(mContext, imgArmLicId, arm_licences_nodoc_img);
        }

        if (!tenantData.getPassport_photo().contains("no_image.jpg")
                && !tenantData.getPassport_photo().equals("")) {
            String filename = tenantData.getPassport_photo().substring(
                    tenantData.getPassport_photo().lastIndexOf("/") + 1);
            btnPassportImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getPassport_photo());
            modal.setIdProofType("passport");
            idProofModal.add(modal);
            passport_doc_img.setVisibility(View.VISIBLE);
            imgPassport = WebUrls.IMG_URL + "" + tenantData.getPassport_photo();

            Common.loadImage(mContext, imgPassport, passport_doc_img);
        }

        if (!tenantData.getRashan_card_photo().contains("no_image.jpg")
                && !tenantData.getRashan_card_photo().equals("")) {
            String filename = tenantData.getRashan_card_photo().substring(
                    tenantData.getRashan_card_photo().lastIndexOf("/") + 1);
            btnRashanCardImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getRashan_card_photo());
            modal.setIdProofType("rashan");
            idProofModal.add(modal);
            rashanCard_doc_img.setVisibility(View.VISIBLE);
            imgRashanCard = WebUrls.IMG_URL + "" + tenantData.getRashan_card_photo();

            Common.loadImage(mContext, imgRashanCard, rashanCard_doc_img);
        }

        if (!tenantData.getOtherid_photo().contains("no_image.jpg")
                && !tenantData.getOtherid_photo().equals("")) {
            String filename = tenantData.getOtherid_photo().substring(
                    tenantData.getOtherid_photo().lastIndexOf("/") + 1);
            btnOtherIdImage.setText("" + filename);
            TenantUserIdProofModal modal = new TenantUserIdProofModal();
            modal.setFromLocalOrServer(TenantUserIdProofModal.FROMSERVER);
            modal.setIdProofImagePath(tenantData.getOtherid_photo());
            modal.setIdProofType("other");
            idProofModal.add(modal);
            other_doc_img.setVisibility(View.VISIBLE);
            imgOtherId = WebUrls.IMG_URL + "" + tenantData.getOtherid_photo();

            Common.loadImage(mContext, imgOtherId, other_doc_img);
        }

    }

    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this, txtBack);
        switch (v.getId()) {
            case R.id.textView_title:
                this.finish();
                this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
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
                ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
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

                break;
            case R.id.votar_iddoc_img:

                ImagePreviewDialog votarImagePreviewDialog = new ImagePreviewDialog() {
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

                break;
            case R.id.driving_lic_nodoc_img:

                ImagePreviewDialog drivingImagePreviewDialog = new ImagePreviewDialog() {
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

                break;
            case R.id.aadhaar_card_nodoc_img:

                ImagePreviewDialog aadhaarImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, imgAadhaarCard, img);

                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                aadhaarImagePreviewDialog.getPerameter(this, "Aadhaar Card", true);
                aadhaarImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.arm_licences_nodoc_img:

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
                armImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
                break;

            case R.id.pasport_doc_img:

                ImagePreviewDialog passportImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, imgPassport, img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                passportImagePreviewDialog.getPerameter(this, "PassPort", true);
                passportImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
                break;

            case R.id.RashanCard_doc_img:

                ImagePreviewDialog rashanCardImagePreviewDialog = new ImagePreviewDialog() {
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
                break;
            case R.id.otherIdentityCard_doc_img:

                ImagePreviewDialog otherIdPreviewDialog = new ImagePreviewDialog() {
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
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                callIdProofImage(tenantData.getUserId(), tenantData.getTenant_id(), _isImageFlag, Common.profileImagePath);


            }
        } catch (NullPointerException e) {

            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        LogConfig.logd("Rcu professional detail response =", "" + result);
        Common.Config("Rcu professional detail response =   " + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
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

        Intent mIntent = new Intent(this, PhotoCaptureActivity.class);
        mIntent.putExtra("photo_purpose", "idProof");
        startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);
    }


    public void submitBtnClickEvent() {

        if (imgVehicleId == null) {
            imgVehicleId = "";
        }


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



        /*if (guarantorName.length() == 0) {
            mCommon.displayAlert(this, "Enter guarantor name.", false);
        } else if (guarantorContactNo.length()>0 && guarantorContactNo.length() < 10) {
            mCommon.displayAlert(this, "Enter guarantor valid contact no.", false);
        } else*/
        if (vehicleNo.length() == 0 && voterId.length() == 0 && drivingLincenceno.length() == 0 &&
                aadhaarCardno.length() == 0 && armLicenceno.length() == 0 && passPortNo.length() == 0 &&
                rashanCardNo.length() == 0 && otherIdNo.length() == 0) {
            mCommon.displayAlert(this, "Enter atleast one id proof no/id.", false);
        } else {
            mCommon.hideKeybord(this, edtOfficeAddress);
            if (!mValidation.checkNetworkRechability()) {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            } else {
                mprogressBar.setVisibility(View.VISIBLE);
                RCUTenantProfessionalDetailAsynctask service = new RCUTenantProfessionalDetailAsynctask(this);
                service.setCallBack(this);
                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tenantData.getTenant_id(), officeAddress, profContactNo, workDetail,
                        guarantorName, guarantorAddress, guarantorContactNo, vehicleNo, voterId,
                        drivingLincenceno, infoPhoneno, mobileno, aadhaarCardno, armLicenceno,
                        drivingIssuOfc, armIssuOfc, passPortNo, rashanCardNo, otherIdNo, detailOfVerification);

            }

        }
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


    }


    public void setDataOnSavedState(Bundle savedInstanceState) {

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
                        btnArmLicensImage.setText("" + Common.profileImagePath);
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

}
