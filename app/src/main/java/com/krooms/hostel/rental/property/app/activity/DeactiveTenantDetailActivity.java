package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.asynctask.DownloadRCUFromServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.PaymentConfirmationServiceAsynctask;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.dialog.ImagePreviewDialog;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.FileDownloader;
import com.krooms.hostel.rental.property.app.common.PrintDialogActivity;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeactiveTenantDetailActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce {

    private ImageView tenantProfilePic;
    private TextView fullName;
    private TextView tenantAddress;
    private TextView duePaymentValue;
    private TextView tenantContact;
    private TextView tenantEmail;
    private TextView tenantFatherName;
    private TextView tenantFatherContactNo;
    private TextView tenantPropertyHireDate;
    private TextView tenantPropertyleaveDate;
    private TextView tenantInstituteAddress;
    private TextView tenantInstituteContactNo;
    private TextView tenantWorkDetail;
    private TextView tenantGuarantorName;
    private TextView tenantGuarantorAddress;
    private TextView tenantGuarantorContact;
    private TextView vehicleId;
    private TextView voterId;
    private TextView drivingLicenceId;
    private TextView drivingLicIssueOffice;
    private TextView aadharCardId;
    private TextView armId;
    private TextView armLicIssueOffice;
    private TextView detailOfVerification;
    private TextView passportId;
    private TextView rashanId;
    private TextView otherId;
    private LinearLayout paymentOrder_IdLayout;
    private LinearLayout paymentTransaction_IdLayout;
    private TextView paymentMode;
    private TextView paymentOrder_Id;
    private TextView paymentTransaction_Id;
    private TextView totalAmount;
    private TextView remainingAmount;
    public static String tanentfinalname, hiredate, leavingdate;
    private ImageView vehicleImage;
    private ImageView voterIamge;
    private ImageView drivingLicenceImage;
    private ImageView aadharCardImage;
    private ImageView armImage;
    private ImageView passportImage;
    private ImageView rashanImage;
    private ImageView otherImage;
    private Button downloadRcuFrom, tanentpayment, userexitid, user_activate;
    private Button viewRcuFrom;
    private PropertyUserModal tenantData;
    private ImageButton main_header_back;
    private ImageButton editPaymentStatus;
    private ImageButton editPropertyBtn;
    Common mCommon;
    SharedStorage mSharedStorage;
    private Context mContext;
    String adavnce;
    SharedStorage mShared, mSharedStore;
    Boolean isPrintActivieted = false;
    String uidt, T_id = null, tbookroomid = null;
    public static String Dueamount = "";
    boolean isProfilePicEdited = false;
    public static String tuser, tenantid;
    public static Activity mActivity = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dective_tenant_detail);
        mActivity = this;
        mContext = this;
        mCommon = new Common();
        mSharedStorage = SharedStorage.getInstance(this);
        tenantData = getIntent().getParcelableExtra("modal");
        uidt = getIntent().getStringExtra("useridv");
        Intent ii = getIntent();
        tuser = ii.getStringExtra("NEWTID");
        tenantid = tenantData.getTenant_id();
        Common.Config("   rashan       " + tenantData.getRashan_card_no());
        Common.Config("   passport       " + tenantData.getPassport_no());
        initView();

        new DueAmountGetOwner().execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 1000);
    }

    public void initView() {

        tenantProfilePic = (ImageView) findViewById(R.id.tenantProfilePic);
        fullName = (TextView) findViewById(R.id.tenantNameValue);
        tenantAddress = (TextView) findViewById(R.id.tenantAddressValue);
        duePaymentValue = (TextView) findViewById(R.id.duePaymentValue);
        tenantContact = (TextView) findViewById(R.id.tenantContactNoValue);
        tenantEmail = (TextView) findViewById(R.id.tenantEmailValue);
        tenantFatherName = (TextView) findViewById(R.id.fatherNameValue);
        tenantFatherContactNo = (TextView) findViewById(R.id.fatherContactNoValue);
        tenantPropertyHireDate = (TextView) findViewById(R.id.hiringDateValue);
        tenantPropertyleaveDate = (TextView) findViewById(R.id.leaveDateValue);
        tenantInstituteAddress = (TextView) findViewById(R.id.InstituteAddressValue);
        tenantInstituteContactNo = (TextView) findViewById(R.id.InstituteContactNoValue);
        tenantWorkDetail = (TextView) findViewById(R.id.workDetailValue);
        tenantGuarantorName = (TextView) findViewById(R.id.guarantorNameValue);
        tenantGuarantorAddress = (TextView) findViewById(R.id.guarantorAddressValue);
        tenantGuarantorContact = (TextView) findViewById(R.id.guarantorContactNoValue);
        main_header_back = (ImageButton) findViewById(R.id.main_header_back);
        editPaymentStatus = (ImageButton) findViewById(R.id.editPaymentStatus);
        editPropertyBtn = (ImageButton) findViewById(R.id.editPropertyBtn);
        vehicleId = (TextView) findViewById(R.id.VehicleRcIdValue);
        voterId = (TextView) findViewById(R.id.VoterIdValue);
        drivingLicenceId = (TextView) findViewById(R.id.DrivingLicenceValue);
        aadharCardId = (TextView) findViewById(R.id.aadharCardValue);
        armId = (TextView) findViewById(R.id.armLicenceIdValue);
        downloadRcuFrom = (Button) findViewById(R.id.downloadRcuFrom);
        user_activate = (Button) findViewById(R.id.user_activate);
        viewRcuFrom = (Button) findViewById(R.id.viewRcuFrom);
        tanentpayment = (Button) findViewById(R.id.tanentpayment);
        userexitid = (Button) findViewById(R.id.userexitid);
        drivingLicIssueOffice = (TextView) findViewById(R.id.drivingLicIssuingOfficeValue);
        armLicIssueOffice = (TextView) findViewById(R.id.armLicIssuingOfficeValue);
        passportId = (TextView) findViewById(R.id.passPortValue);
        rashanId = (TextView) findViewById(R.id.rashanCardValue);
        otherId = (TextView) findViewById(R.id.otherIdValue);
        detailOfVerification = (TextView) findViewById(R.id.detailVerificationValue);
        paymentOrder_IdLayout = (LinearLayout) findViewById(R.id.paymentOrderId);
        paymentTransaction_IdLayout = (LinearLayout) findViewById(R.id.paymentTransaction);
        paymentMode = (TextView) findViewById(R.id.paymentMode);
        paymentOrder_Id = (TextView) findViewById(R.id.paymentOrderIdVelue);
        paymentTransaction_Id = (TextView) findViewById(R.id.paymentTransactionIdVelue);
        totalAmount = (TextView) findViewById(R.id.totalAmountLayoutVelue);
        remainingAmount = (TextView) findViewById(R.id.remainingTransactionIdVelue);
        vehicleImage = (ImageView) findViewById(R.id.VehicleRcId_img);
        voterIamge = (ImageView) findViewById(R.id.VoterId_img);
        drivingLicenceImage = (ImageView) findViewById(R.id.DrivingLicence_img);
        aadharCardImage = (ImageView) findViewById(R.id.aadharCard_img);
        armImage = (ImageView) findViewById(R.id.armLicence_img);
        passportImage = (ImageView) findViewById(R.id.passPort_img);
        rashanImage = (ImageView) findViewById(R.id.rashanCard_img);
        otherImage = (ImageView) findViewById(R.id.otherId_img);
        vehicleImage.setOnClickListener(this);
        voterIamge.setOnClickListener(this);
        drivingLicenceImage.setOnClickListener(this);
        aadharCardImage.setOnClickListener(this);
        armImage.setOnClickListener(this);
        passportImage.setOnClickListener(this);
        rashanImage.setOnClickListener(this);
        otherImage.setOnClickListener(this);
        tanentpayment.setOnClickListener(this);
        user_activate.setOnClickListener(this);
        downloadRcuFrom.setOnClickListener(this);
        viewRcuFrom.setOnClickListener(this);
        main_header_back.setOnClickListener(this);
        editPaymentStatus.setOnClickListener(this);
        editPropertyBtn.setOnClickListener(this);
        tenantProfilePic.setOnClickListener(this);
        tanentpayment.setVisibility(View.GONE);
        downloadRcuFrom.setVisibility(View.GONE);
        userexitid.setVisibility(View.GONE);
        viewRcuFrom.setVisibility(View.GONE);
        editPropertyBtn.setVisibility(View.GONE);
        user_activate.setVisibility(View.VISIBLE);
    }

    public void setData() {
        tanentfinalname = tenantData.getTenant_fname();
        fullName.setText(tenantData.getTenant_fname() + " " + tenantData.getTenant_lname());
        tenantAddress.setText(tenantData.getTenant_permanent_address());
        duePaymentValue.setText(tenantData.getPaymentStatus());
        tenantContact.setText(tenantData.getContact_number());
        tenantEmail.setText(tenantData.getEmail_id());
        tenantFatherName.setText(tenantData.getTenant_father_name());
        tenantFatherContactNo.setText(tenantData.getTenant_father_contact_no());
        tenantPropertyHireDate.setText(tenantData.getProperty_hire_date());
        tenantPropertyleaveDate.setText(tenantData.getProperty_leave_date());
        tenantInstituteAddress.setText(tenantData.getTenant_office_institute());
        tenantInstituteContactNo.setText(tenantData.getTenant_contact_number());
        tenantWorkDetail.setText(tenantData.getTenant_work_detail());
        tenantGuarantorName.setText(tenantData.getGuarantor_name());
        tenantGuarantorAddress.setText(tenantData.getGuarantor_address());
        tenantGuarantorContact.setText(tenantData.getGuarantor_contact_number());
        paymentMode.setText(tenantData.getPaymentMode());
        paymentOrder_Id.setText(tenantData.getPaymentOrderId());
        paymentTransaction_Id.setText(tenantData.getPaymentTransactionId());
        totalAmount.setText(tenantData.getRoomAmount());
        tbookroomid = tenantData.getBookedRoomId();
        vehicleId.setText(tenantData.getVehicle_registration_no());
        voterId.setText(tenantData.getVoter_id_card_no());
        drivingLicenceId.setText(tenantData.getDriving_license_no());
        aadharCardId.setText(tenantData.getAadhar_card_no());
        armId.setText(tenantData.getArm_licence_no());
        drivingLicIssueOffice.setText(tenantData.getDriving_licence_issu_ofc_name());
        armLicIssueOffice.setText(tenantData.getArm_licence_issu_ofc_name());
        rashanId.setText(tenantData.getRashan_card_no());
        passportId.setText(tenantData.getPassport_no());
        otherId.setText(tenantData.getOtherid_no());
        detailOfVerification.setText(tenantData.getDetail_verification());

        try {
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getVehicle_registration_photo(), vehicleImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getVoter_id_card_photo(), voterIamge);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getDriving_license_photo(), drivingLicenceImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getAadhar_card_photo(), aadharCardImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo(), armImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getPassport_photo(), passportImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getRashan_card_photo(), rashanImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getOtherid_photo(), otherImage);

            Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + tenantData.getTenant_photo())
                    .placeholder(R.drawable.user_xl)
                    .error(R.drawable.user_xl)
                    .transform(new RoundedTransformation(this))
                    .into(tenantProfilePic);


        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_header_back:
                startActivity(new Intent(DeactiveTenantDetailActivity.this, HostelListActivity.class));
                break;

            case R.id.downloadRcuFrom:

                isPrintActivieted = true;
                downLoadRCU();

                break;
            case R.id.tanentpayment:
                mSharedStorage = SharedStorage.getInstance(this);
                Intent i = new Intent(DeactiveTenantDetailActivity.this, ListViewActivity.class);
                i.putExtra("user_name", tenantData.getTenant_fname() + " " + tenantData.getTenant_lname());
                i.putExtra("user_id_data", tuser);
                i.putExtra("tid", tenantData.getTenant_id());
                i.putExtra("property_id_data", mSharedStorage.getUserPropertyId());
                i.putExtra("property_name", mSharedStorage.getPropertyname());
                startActivity(i);
                break;

            case R.id.user_activate:
                mSharedStorage = SharedStorage.getInstance(this);

                new ActiveTenant().execute();
                startActivity(new Intent(DeactiveTenantDetailActivity.this, HostelListActivity.class));

                break;
            case R.id.viewRcuFrom:
                isPrintActivieted = false;
                downLoadRCU();
                break;

            case R.id.VehicleRcId_img:
                ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getVehicle_registration_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getVehicle_registration_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getVehicle_registration_photo();
                            new DownloadImage().execute(url, "vehicle_no_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog.getPerameter(this, "Vehicle No", false);
                vehicleImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
                break;
            case R.id.VoterId_img:

                ImagePreviewDialog vehicleImagePreviewDialog1 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getVoter_id_card_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getVoter_id_card_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getVoter_id_card_photo();
                            new DownloadImage().execute(url, "voter_id_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog1.getPerameter(this, "Voter id", false);
                vehicleImagePreviewDialog1.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.DrivingLicence_img:

                ImagePreviewDialog vehicleImagePreviewDialog2 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getDriving_license_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getDriving_license_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getDriving_license_photo();
                            new DownloadImage().execute(url, "driving_licence_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog2.getPerameter(this, "Driving licence", false);
                vehicleImagePreviewDialog2.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.aadharCard_img:

                ImagePreviewDialog vehicleImagePreviewDialog3 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getAadhar_card_photo(), img);

                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getAadhar_card_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getAadhar_card_photo();
                            new DownloadImage().execute(url, "aadhar_card_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog3.getPerameter(this, "Aadhar card", false);
                vehicleImagePreviewDialog3.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.armLicence_img:

                ImagePreviewDialog vehicleImagePreviewDialog4 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getArm_licence_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo();
                            new DownloadImage().execute(url, "arm_licence_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog4.getPerameter(this, "Arm Licence", false);
                vehicleImagePreviewDialog4.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.passPort_img:

                ImagePreviewDialog vehicleImagePreviewDialog5 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getPassport_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getPassport_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getPassport_photo();
                            new DownloadImage().execute(url, "passport_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog5.getPerameter(this, "Passport", false);
                vehicleImagePreviewDialog5.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.rashanCard_img:

                ImagePreviewDialog vehicleImagePreviewDialog6 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getRashan_card_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getRashan_card_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getRashan_card_photo();
                            new DownloadImage().execute(url, "rashan_card_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog6.getPerameter(this, "Rashan Card", false);
                vehicleImagePreviewDialog6.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.otherId_img:

                ImagePreviewDialog vehicleImagePreviewDialog7 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + tenantData.getOtherid_photo(), img);
                    }

                    @Override
                    public void downloadImage() {
                        if (!tenantData.getOtherid_photo().equals("")) {
                            String url = WebUrls.IMG_URL + "" + tenantData.getOtherid_photo();
                            new DownloadImage().execute(url, "other_id_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };
                vehicleImagePreviewDialog7.getPerameter(this, "Other Id", false);
                vehicleImagePreviewDialog7.show(this.getSupportFragmentManager(), "image dialog");

                break;

            case R.id.tenantProfilePic:

                ImagePreviewDialog profilePicPreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        Picasso.with(DeactiveTenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getTenant_photo())
                                .placeholder(R.drawable.user_xl)
                                .error(R.drawable.user_xl)
                                .into(img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                profilePicPreviewDialog.getPerameter(this, "Profile Pic", true);
                profilePicPreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
                break;


            case R.id.editPropertyBtn:
                Intent mIntent = new Intent(this, EditRCUFromByOwnerActivity.class);
                mIntent.putExtra("modal", (Serializable) tenantData);
                startActivity(mIntent);
                break;

        }
    }

    public void downLoadRCU() {

        DownloadRCUFromServiceAsyncTask serviceAsyncTask = new DownloadRCUFromServiceAsyncTask(this);
        serviceAsyncTask.setCallBack(new ServiceResponce() {
            @Override
            public void requestResponce(String result) {
                serviceResponse(result);
            }
        });
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tenantData.getTenant_id(), mSharedStorage.getUserPropertyId(), tenantData.getUserId(), "Police Station");
    }

    public void callService(String payment) {

        PaymentConfirmationServiceAsynctask service = new PaymentConfirmationServiceAsynctask(this);
        service.setCallBack(this);
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tenantData.getTransaction_id(), payment);
    }


    @Override
    public void requestResponce(String result) {
        Common.Config(" string     " + result);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                mCommon.displayAlert(this, jsonObject.getString(WebUrls.STATUS_JSON), true);

            } else {
                mCommon.displayAlert(this, jsonObject.getString(WebUrls.STATUS_JSON), false);
            }
        } catch (JSONException e) {
        }
    }

    public void serviceResponse(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {

                String pdf = WebUrls.IMG_URL + "" + jsonObject.getString(WebUrls.DATA_JSON);
                new DownloadFile().execute(pdf, "rcu_form" + System.currentTimeMillis() + ".pdf");
            }
        } catch (JSONException e) {
        }
    }

    class DownloadImage extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DeactiveTenantDetailActivity.this);
            pDialog.setTitle("Downloading file");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            return mCommon.downloadImageAndSave(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            mCommon.displayAlert(DeactiveTenantDetailActivity.this, "Image dowmloaded succesfully", false);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Krooms");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return fileName;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if (isPrintActivieted) {
                print(aVoid);
            } else {
                view(aVoid);
            }
        }
    }


    public void view(String pdfName) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() +
                "/Krooms/" + pdfName);
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DeactiveTenantDetailActivity.this,
                    "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public void print(String pdfName) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() +
                "/Krooms/" + pdfName);  // -> filename = maven.pdf

        Intent printIntent = new Intent(this, PrintDialogActivity.class);
        printIntent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        printIntent.putExtra("title", pdfName);
        startActivity(printIntent);
    }

    // API calling to get due amount at tenant end..
    public class ActiveTenant extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(DeactiveTenantDetailActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "updatetenantstatus"));
                nameValuePairs.add(new BasicNameValuePair("user_id", tenantData.getT_user_id()));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getUserPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantData.getTenant_id()));
                nameValuePairs.add(new BasicNameValuePair("status", "1"));
                nameValuePairs.add(new BasicNameValuePair("status_active", "active"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        startActivity(new Intent(DeactiveTenantDetailActivity.this, HostelListActivity.class));
                        Dueamount = jsonarray.getJSONObject(i).getString("remaining_amount");
                    }

                }
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("Y")) {
            } else if (result.equals("N")) {
            }

        }
    }

    //....end of API
    //API to get Due Amount at Owner ent for tenant
    public class DueAmountGetOwner extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(DeactiveTenantDetailActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "tenantremaining_amount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", tuser));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getUserPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantData.getTenant_id()));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Dueamount = jsonarray.getJSONObject(i).getString("remaining_amount");
                    }
                }
            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equalsIgnoreCase("Y")) {
                remainingAmount.setText(Dueamount);
            } else if (result.equals("N")) {
            }

        }
    }
    //...end of API
}

