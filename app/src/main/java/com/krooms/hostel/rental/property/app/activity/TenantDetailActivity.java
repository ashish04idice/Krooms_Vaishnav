package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.MyBookedUserListAdapter;
import com.krooms.hostel.rental.property.app.asynctask.DownloadRCUFromServiceAsyncTask;
import com.krooms.hostel.rental.property.app.asynctask.PaymentConfirmationServiceAsynctask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.FileDownloader;
import com.krooms.hostel.rental.property.app.common.PrintDialogActivity;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.PaymentConfirmationDialog;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TenantDetailActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce {

    private ImageView tenantProfilePic;
    private TextView fullName;
    private TextView tenantAddress;
    private TextView duePaymentValue;
    private TextView title_head;
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
    String noOfTenant = "";
    private MyBookedUserListAdapter mAdapterBookedUser;
    private ArrayList<PropertyUserModal> mArrayBookedUser = new ArrayList<>();
    private LinearLayout paymentOrder_IdLayout;
    private LinearLayout paymentTransaction_IdLayout;
    private TextView paymentMode;
    private TextView paymentOrder_Id;
    private TextView paymentTransaction_Id;
    private TextView totalAmount;
    private TextView remainingAmount;
    private TextView txtterms;
    public static String tanentfinalname, hiredate, leavingdate;
    String statusactivevalue = "";
    private ImageView vehicleImage;
    private ImageView voterIamge;
    private ImageView drivingLicenceImage;
    private ImageView aadharCardImage;
    private ImageView armImage;
    private ImageView passportImage;
    private ImageView rashanImage;
    private ImageView otherImage;
    private Button changeroom;
    private Button downloadRcuFrom, tanentpayment, userexitid, attendancereport, bookroomagain;
    private Button viewRcuFrom;
    private PropertyUserModal tenantData;
    private ImageButton main_header_back;
    private ImageButton editPaymentStatus;
    private ImageButton editPropertyBtn;
    Common mCommon;
    SharedStorage mSharedStorage;
    private Context mContext;
    Intent ii;
    String adavnce, UType = "";
    SharedStorage mShared, mSharedStore;
    Boolean isPrintActivieted = false;
    String uidt, T_id = null, tbookroomid = null;
    public static String Dueamount = "", useridnew;
    boolean isProfilePicEdited = false;
    public static String tuser, tenantid, roomidnewvalue = "", owneridmain, propertyidforseparate = "";
    public static Activity mActivity = null;
    Button removechild;
    String parentidvalue, aa;
    Button ShowAssignItem;
    public static String statusforpayment = "false";
    public static String statusforattendance = "false";
    public static String statusforinventory = "false";
    public static String paidarray[];
    String valuefrom, bparentid, buserid, monthlyRoomRant = "";
    public static String holduserstatus = "";
    public static String GET_MAIN_FOLDER_PATH;
    private static String FILESAVE_BASEURL;
    private final String MAIN_FOLDER_NAME = "IRC Magazine";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_detail);
        mActivity = this;
        mContext = this;
        mCommon = new Common();
        mSharedStorage = SharedStorage.getInstance(this);

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

        UType = Utility.getSharedPreferences(mContext, "Utype");
        tenantData = getIntent().getParcelableExtra("modal");
        uidt = getIntent().getStringExtra("useridv");
        ii = getIntent();
        useridnew = ii.getStringExtra("NEWTID");
        valuefrom = ii.getStringExtra("KEY");
        monthlyRoomRant = ii.getStringExtra("tmonthlyRoomRant");
        holduserstatus = ii.getStringExtra("holduserstatus");
        Utility.setSharedPreference(TenantDetailActivity.this, "AdvanceStatus", "");
        Utility.setSharedPreference(TenantDetailActivity.this, "DueStatus", "");
        Utility.setSharedPreference(TenantDetailActivity.this, "TenantReturnStatus", "");

        if (valuefrom.equalsIgnoreCase("Parent")) {

            buserid = ii.getStringExtra("useridd");
            bparentid = ii.getStringExtra("parentidd");
        }
        tuser = useridnew;
        tenantid = ii.getStringExtra("tid");
        roomidnewvalue = ii.getStringExtra("roomid");
        parentidvalue = ii.getStringExtra("tParent_id");
        owneridmain = ii.getStringExtra("ownerid");
        propertyidforseparate = ii.getStringExtra("propertyidforseparate");
        Common.Config("rashan" + roomidnewvalue);
        Common.Config("passport " + tenantData.getPassport_no());
        // aa=ii.getStringExtra("tdrivinglicphoto");
        ShowAssignItem = (Button) findViewById(R.id.ShowAssignItem);

        initView();

        if (mSharedStorage.getUserType().equals("2")) {
            new DueAmountGetTenant().execute();
        } else if (mSharedStorage.getUserType().equals("4")) {

            new DueAmountGetTenant().execute();

        } else {
            new DueAmountGetOwner().execute();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 1000);

        getTerms();
    }


    public void initView() {

        tenantProfilePic = (ImageView) findViewById(R.id.tenantProfilePic);
        title_head = (TextView) findViewById(R.id.PropertyNameTitle);
        removechild = (Button) findViewById(R.id.removechild);
        changeroom = (Button) findViewById(R.id.changeroom);
        fullName = (TextView) findViewById(R.id.tenantNameValue);
        tanentpayment = (Button) findViewById(R.id.tanentpayment);
        userexitid = (Button) findViewById(R.id.userexitid);
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
        downloadRcuFrom = (Button) findViewById(R.id.downloadRcuFrom);
        viewRcuFrom = (Button) findViewById(R.id.viewRcuFrom);
        attendancereport = (Button) findViewById(R.id.attendance_report);
        bookroomagain = (Button) findViewById(R.id.book_room_again);
        vehicleId = (TextView) findViewById(R.id.VehicleRcIdValue);
        voterId = (TextView) findViewById(R.id.VoterIdValue);
        drivingLicenceId = (TextView) findViewById(R.id.DrivingLicenceValue);
        aadharCardId = (TextView) findViewById(R.id.aadharCardValue);
        armId = (TextView) findViewById(R.id.armLicenceIdValue);
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
        txtterms = (TextView) findViewById(R.id.termsverify);
        if (holduserstatus.equals("1")) {

            editPropertyBtn.setVisibility(View.GONE);
        } else {

            editPropertyBtn.setVisibility(View.VISIBLE);
        }

        vehicleImage.setOnClickListener(this);
        attendancereport.setOnClickListener(this);
        bookroomagain.setOnClickListener(this);
        voterIamge.setOnClickListener(this);
        drivingLicenceImage.setOnClickListener(this);
        aadharCardImage.setOnClickListener(this);
        armImage.setOnClickListener(this);
        passportImage.setOnClickListener(this);
        rashanImage.setOnClickListener(this);
        otherImage.setOnClickListener(this);


        changeroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dueamountnew = 0;
                dueamountnew = Integer.parseInt(Dueamount);
                if (dueamountnew == 0) {
                    Intent ii = new Intent(TenantDetailActivity.this, Change_Room.class);
                    ii.putExtra("property_id", propertyidforseparate);
                    ii.putExtra("roomchnage", "1");
                    ii.putExtra("roomidnewvalue", roomidnewvalue);
                    ii.putExtra("tenantid", tenantid);
                    startActivity(ii);
                } else {
                    Toast.makeText(mContext, "Please Clear Room Amount", Toast.LENGTH_SHORT).show();
                    Intent ii = new Intent(TenantDetailActivity.this, Change_Room.class);
                    ii.putExtra("property_id", propertyidforseparate);
                    ii.putExtra("roomchnage", "1");
                    ii.putExtra("roomidnewvalue", roomidnewvalue);
                    ii.putExtra("tenantid", tenantid);
                    startActivity(ii);
                }
            }
        });


        ShowAssignItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mSharedStorage.getUserType().equals("3")) {
                    Intent i = new Intent(TenantDetailActivity.this, ShowAssign_Inventory_Activity.class);
                    i.putExtra("tenantid", tenantid);
                    i.putExtra("propertyidseprate", propertyidforseparate);
                    startActivity(i);
                } else if (mSharedStorage.getUserType().equals("6")) {
                    Intent i = new Intent(TenantDetailActivity.this, ShowAssign_Inventory_Activity.class);
                    i.putExtra("tenantid", tenantid);
                    i.putExtra("propertyidseprate", propertyidforseparate);
                    startActivity(i);
                } else {
                    Intent i = new Intent(TenantDetailActivity.this, ShowAssign_Inventory_Activity_Tenant.class);
                    i.putExtra("tenantid", tenantid);
                    i.putExtra("propertyidseprate", propertyidforseparate);
                    startActivity(i);
                }
            }
        });

        removechild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RemoveChildFromtenant().execute();

            }
        });

        userexitid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DueAmountGetOwnerNew().execute();
            }
        });

        tanentpayment.setOnClickListener(this);
        downloadRcuFrom.setOnClickListener(this);
        viewRcuFrom.setOnClickListener(this);
        main_header_back.setOnClickListener(this);
        editPaymentStatus.setOnClickListener(this);
        editPropertyBtn.setOnClickListener(this);
        tenantProfilePic.setOnClickListener(this);
        if (mSharedStorage.getUserType().equals("3")) {
            attendancereport.setVisibility(View.GONE);
            downloadRcuFrom.setVisibility(View.VISIBLE);
            userexitid.setVisibility(View.VISIBLE);
            viewRcuFrom.setVisibility(View.VISIBLE);
            changeroom.setVisibility(View.GONE);
            editPropertyBtn.setVisibility(View.VISIBLE);
            ShowAssignItem.setVisibility(View.GONE);
            new PaidUnpaidLogicJson(owneridmain, propertyidforseparate).execute();

        } else if (mSharedStorage.getUserType().equals("2")) {
            downloadRcuFrom.setVisibility(View.GONE);
            editPaymentStatus.setVisibility(View.GONE);
            viewRcuFrom.setVisibility(View.GONE);
            editPropertyBtn.setVisibility(View.GONE);
            ShowAssignItem.setVisibility(View.GONE);
            new PaidUnpaidLogicMonthly(owneridmain, propertyidforseparate).execute();

        } else if (mSharedStorage.getUserType().equals("4")) {
            downloadRcuFrom.setVisibility(View.GONE);
            editPaymentStatus.setVisibility(View.GONE);
            viewRcuFrom.setVisibility(View.GONE);
            removechild.setVisibility(View.VISIBLE);
            editPropertyBtn.setVisibility(View.VISIBLE);
            ShowAssignItem.setVisibility(View.VISIBLE);

            new PaidUnpaidLogicMonthly(owneridmain, propertyidforseparate).execute();

        } else if (mSharedStorage.getUserType().equals("5")) {
            editPropertyBtn.setVisibility(View.GONE);
            //tanentpayment.setVisibility(View.VISIBLE);
            userexitid.setVisibility(View.VISIBLE);
            String propertyidlastvalue = mSharedStorage.getUserPropertyId();
            String ownerid = mSharedStorage.getPropertyOwnerId();
            ShowAssignItem.setVisibility(View.GONE);


            new PaidUnpaidLogicJson(ownerid, propertyidlastvalue).execute();


        } else if (mSharedStorage.getUserType().equals("6")) { //for property warden
            tanentpayment.setVisibility(View.GONE);
            attendancereport.setVisibility(View.GONE);
            downloadRcuFrom.setVisibility(View.VISIBLE);
            userexitid.setVisibility(View.VISIBLE);
            viewRcuFrom.setVisibility(View.VISIBLE);
            editPropertyBtn.setVisibility(View.VISIBLE);

            new PaidUnpaidLogicJson(owneridmain, propertyidforseparate).execute();
        } else if (mSharedStorage.getUserType().equals("3")) {
            if (Integer.parseInt(tenantData.getRemainingAmount()) > 0) {
                editPaymentStatus.setVisibility(View.GONE);
            }
        } else if (mSharedStorage.getUserType().equals("6")) {
            if (Integer.parseInt(tenantData.getRemainingAmount()) > 0) {
                editPaymentStatus.setVisibility(View.GONE);
            }
        }
    }

    public void setData() {

        tanentfinalname = ii.getStringExtra("tfname") + " " + ii.getStringExtra("tlname");
        fullName.setText(ii.getStringExtra("tfname") + " " + ii.getStringExtra("tlname"));
        tenantAddress.setText(ii.getStringExtra("taddress"));
        duePaymentValue.setText(ii.getStringExtra("tpstatus"));
        tenantContact.setText(ii.getStringExtra("tcontact"));
        tenantEmail.setText(ii.getStringExtra("temail"));
        tenantFatherName.setText(ii.getStringExtra("tfathername"));
        tenantFatherContactNo.setText(ii.getStringExtra("tfather_no"));
        tenantPropertyHireDate.setText(ii.getStringExtra("thiredate"));
        tenantPropertyleaveDate.setText(ii.getStringExtra("tleavedate"));
        tenantInstituteAddress.setText(ii.getStringExtra("tofc_institute"));
        tenantInstituteContactNo.setText(ii.getStringExtra("tofc_contact"));
        tenantWorkDetail.setText(ii.getStringExtra("twork_detail"));
        tenantGuarantorName.setText(ii.getStringExtra("tguarantorname"));
        tenantGuarantorAddress.setText(ii.getStringExtra("tguarantoraddress"));
        tenantGuarantorContact.setText(ii.getStringExtra("tguarantorcontact"));

        paymentMode.setText(ii.getStringExtra("tpmode"));
        paymentOrder_Id.setText(ii.getStringExtra("tporderid"));
        paymentTransaction_Id.setText(ii.getStringExtra("tptransactionid"));
        totalAmount.setText(ii.getStringExtra("troomamount"));//advance amount

        // remainingAmount.setText(Dueamount);
        vehicleId.setText(ii.getStringExtra("tvehicalid"));
        voterId.setText(ii.getStringExtra("tvoterid"));
        drivingLicenceId.setText(ii.getStringExtra("tdrivingid"));
        aadharCardId.setText(ii.getStringExtra("tadharid"));
        armId.setText(ii.getStringExtra("tarmid"));
        drivingLicIssueOffice.setText(ii.getStringExtra("tdriving_issue_ofc"));
        armLicIssueOffice.setText(ii.getStringExtra("tarm_issue_ofc"));
        rashanId.setText(ii.getStringExtra("trashanid"));
        passportId.setText(ii.getStringExtra("tpassportid"));
        otherId.setText(ii.getStringExtra("totherid"));
        detailOfVerification.setText(ii.getStringExtra("tdetailverif"));

        try {
            /*Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + tenantData.getVehicle_registration_photo())
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(vehicleImage);*/
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tvehicalphoto"), vehicleImage);
            /*Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + tenantData.getVoter_id_card_photo())
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(voterIamge);*/
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tvoteridphoto"), voterIamge);
            /*Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + tenantData.getDriving_license_photo())
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(drivingLicenceImage);*/
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tdrivinglicphoto"), drivingLicenceImage);
            /*Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + tenantData.getAadhar_card_photo())
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(aadharCardImage);*/
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("taadharphoto"), aadharCardImage);
            /*Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo())
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(armImage);*/
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tarmphoto"), armImage);

            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tpassportphoto"), passportImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("trashanphoto"), rashanImage);
            Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("totherphoto"), otherImage);

            Picasso.with(this)
                    .load(WebUrls.IMG_URL + "" + ii.getStringExtra("tenantphoto"))
                    .placeholder(R.drawable.user_xl)
                    .error(R.drawable.user_xl)
                    //.transform(new RoundedTransformation(this))
                    .into(tenantProfilePic);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_header_back:
//                finish();
                if (mSharedStorage.getUserType().equals("5")) {
                    startActivity(new Intent(TenantDetailActivity.this, Home_Accountantactivity.class));

                } else {
                    //change by me 8/9/2017
                    if (valuefrom.equalsIgnoreCase("Tenant")) {
                        startActivity(new Intent(TenantDetailActivity.this, CoTenantListActivity.class));
                        finish();
                    } else if (valuefrom.equalsIgnoreCase("Owner")) {
                        startActivity(new Intent(TenantDetailActivity.this, MyPropertyUsersListActivity.class));
                        finish();
                    } else {
                        Intent intent = new Intent(TenantDetailActivity.this, ViewChildWorkingActivity.class);
                        intent.putExtra("useridvalue", buserid);
                        intent.putExtra("parentvalueid", bparentid);
                        finish();
                    }

                    //
                    //old
                   /* startActivity(new Intent(TenantDetailActivity.this,HostelListActivity.class));
*/
                }
                break;

            case R.id.editPaymentStatus:

                Common.Config("Remaining amount  " + Integer.getInteger(tenantData.getRemainingAmount()));
                PaymentConfirmationDialog confirmPaymentDialog = new PaymentConfirmationDialog() {
                    @Override
                    public void paymentConfirmBtn(String payment) {

                        callService(payment);

                    }
                };
                confirmPaymentDialog.getPerameter(this, tenantData.getRemainingAmount());
                confirmPaymentDialog.show(getSupportFragmentManager(), "");

                break;

            case R.id.downloadRcuFrom:
                isPrintActivieted = true;
                downLoadRCU();
                break;
            case R.id.book_room_again:
                Intent intent = new Intent(TenantDetailActivity.this, HostelDetailActivity.class);
                intent.putExtra("tenantmodal", (Parcelable) tenantData);
                startActivity(intent);
                break;
            case R.id.attendance_report:
                mShared = SharedStorage.getInstance(this);
                if (mShared.getUserType().equals("2")) {
                    Intent i = new Intent(TenantDetailActivity.this, Dateselection_AttendenceActivity.class);
                    i.putExtra("user_name", tanentfinalname);
                    i.putExtra("user_id_data", tuser);
                    i.putExtra("tid", tenantid);
                    i.putExtra("ownerid", owneridmain);
                    i.putExtra("property_id_data", propertyidforseparate);
                    startActivity(i);
                } else if (mShared.getUserType().equals("4")) {
                    Intent i = new Intent(TenantDetailActivity.this, Dateselection_AttendenceActivity.class);
                    i.putExtra("user_name", tanentfinalname);
                    i.putExtra("user_id_data", tuser);
                    i.putExtra("tid", tenantid);
                    i.putExtra("ownerid", owneridmain);
                    i.putExtra("property_id_data", propertyidforseparate);
                    startActivity(i);
                } else {
                    Intent i = new Intent(TenantDetailActivity.this, Dateselection_AttendenceActivity.class);
                    i.putExtra("user_name", tanentfinalname);
                    i.putExtra("user_id_data", tuser);
                    i.putExtra("tid", tenantid);
                    i.putExtra("ownerid", MyPropertyUsersListActivity.owneridvaluemain);
                    i.putExtra("property_id_data", propertyidforseparate);
                    i.putExtra("property_name", "");
                    startActivity(i);
                }
                break;

            case R.id.tanentpayment:
                mShared = SharedStorage.getInstance(this);
                if (mShared.getUserType().equals("2")) {
                    Intent i = new Intent(TenantDetailActivity.this, ListViewActivityTanent.class);
                    i.putExtra("user_name", tanentfinalname);
                    i.putExtra("user_id_data", tuser);
                    i.putExtra("tid", tenantid);
                    i.putExtra("property_id_data", propertyidforseparate);
                    i.putExtra("property_name", mShared.getPropertyname());
                    startActivity(i);
                } else if (mShared.getUserType().equals("4")) {
                    Intent i = new Intent(TenantDetailActivity.this, ListViewActivityTanent.class);
                    i.putExtra("user_name", tanentfinalname);
                    i.putExtra("user_id_data", tuser);
                    i.putExtra("tid", tenantid);
                    i.putExtra("property_id_data", propertyidforseparate);
                    i.putExtra("property_name", mShared.getPropertyname());
                    startActivity(i);
                } else {
                    Intent i = new Intent(TenantDetailActivity.this, ListViewActivity.class);
                    i.putExtra("user_name", tanentfinalname);
                    i.putExtra("user_id_data", tuser);
                    i.putExtra("tid", tenantid);
                    i.putExtra("property_id_data", propertyidforseparate);
                    i.putExtra("property_name", mShared.getPropertyname());
                    i.putExtra("holduserstatus", holduserstatus);
                    startActivity(i);
                }
                break;

            case R.id.viewRcuFrom:
                isPrintActivieted = false;
                downLoadRCU();
                break;

            case R.id.VehicleRcId_img:
               /* ImagePreviewDialog vehicleImagePreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {


                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getVehicle_registration_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tvehicalphoto"), img);
                    }

                    @Override
                    public void downloadImage() {


                        String vchale = ii.getStringExtra("tvehicalphoto");

                        if (!vchale.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tvehicalphoto");
                            new DownloadImage().execute(url, "vehicle_no_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/


                CustomDialogClass VehicleRcId = new CustomDialogClass(TenantDetailActivity.this);
                VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VehicleRcId.getPerameter(this, "Vehicle No", false, ii.getStringExtra("tvehicalphoto"), "VehicleRcId");
                VehicleRcId.show();

                //vehicleImagePreviewDialog.getPerameter(this, "Vehicle No", false);
                //vehicleImagePreviewDialog.show(this.getSupportFragmentManager(), "image dialog");
                break;
            case R.id.VoterId_img:

                /*ImagePreviewDialog vehicleImagePreviewDialog1 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getVoter_id_card_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tvoteridphoto"), img);
                    }

                    @Override
                    public void downloadImage() {

                        String voteridvalue = ii.getStringExtra("tvoteridphoto");

                        if (!voteridvalue.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tvoteridphoto");
                            new DownloadImage().execute(url, "voter_id_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/


                CustomDialogClass VoterId = new CustomDialogClass(TenantDetailActivity.this);
                VoterId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VoterId.getPerameter(this, "Voter id", false, ii.getStringExtra("tvoteridphoto"), "VoterId");
                VoterId.show();


                //vehicleImagePreviewDialog1.getPerameter(this, "Voter id", false);
                //vehicleImagePreviewDialog1.show(this.getSupportFragmentManager(), "image dialog");

                break;

            case R.id.DrivingLicence_img:

              /*  ImagePreviewDialog vehicleImagePreviewDialog2 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getDriving_license_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tdrivinglicphoto"), img);

                    }

                    @Override
                    public void downloadImage() {


                        String aa = ii.getStringExtra("tdrivinglicphoto");

                        if (!aa.equals("")) {

                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tdrivinglicphoto");
                            new DownloadImage().execute(url, "driving_licence_" + System.currentTimeMillis() + ".jpg");
                        } else {

                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };

*/
                CustomDialogClass DrivingLicence = new CustomDialogClass(TenantDetailActivity.this);
                DrivingLicence.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                DrivingLicence.getPerameter(this, "Driving licence", false, ii.getStringExtra("tdrivinglicphoto"), "DrivingLicence");
                DrivingLicence.show();


                // vehicleImagePreviewDialog2.getPerameter(this, "Driving licence", false);
                // vehicleImagePreviewDialog2.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.aadharCard_img:

                /*ImagePreviewDialog vehicleImagePreviewDialog3 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getAadhar_card_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("taadharphoto"), img);

                    }

                    @Override
                    public void downloadImage() {

                        String aadharcardvalue = ii.getStringExtra("taadharphoto");


                        if (!aadharcardvalue.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("taadharphoto");
                            new DownloadImage().execute(url, "aadhar_card_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/


                CustomDialogClass aadharCard = new CustomDialogClass(TenantDetailActivity.this);
                aadharCard.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                aadharCard.getPerameter(this, "Aadhar card", false, ii.getStringExtra("taadharphoto"), "aadharCard");
                aadharCard.show();

                // vehicleImagePreviewDialog3.getPerameter(this, "Aadhar card", false);
                //  vehicleImagePreviewDialog3.show(this.getSupportFragmentManager(), "image dialog");
                break;

            case R.id.armLicence_img:
               /* ImagePreviewDialog vehicleImagePreviewDialog4 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tarmphoto"), img);
                    }

                    @Override
                    public void downloadImage() {

                        String tarm = ii.getStringExtra("tarmphoto");

                        if (!tarm.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tarmphoto");
                            new DownloadImage().execute(url, "arm_licence_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/


                CustomDialogClass armLicence = new CustomDialogClass(TenantDetailActivity.this);
                armLicence.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                armLicence.getPerameter(this, "Arm Licence", false, ii.getStringExtra("tarmphoto"), "armLicence");
                armLicence.show();


                //vehicleImagePreviewDialog4.getPerameter(this, "Arm Licence", false);
                // vehicleImagePreviewDialog4.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.passPort_img:

              /*  ImagePreviewDialog vehicleImagePreviewDialog5 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("tpassportphoto"), img);
                    }

                    @Override
                    public void downloadImage() {

                        String tpasswort = ii.getStringExtra("tpassportphoto");

                        if (!tpasswort.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tpassportphoto");
                            new DownloadImage().execute(url, "passport_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/


                CustomDialogClass passPort = new CustomDialogClass(TenantDetailActivity.this);
                passPort.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                passPort.getPerameter(this, "Passport", false, ii.getStringExtra("tpassportphoto"), "passPort");
                passPort.show();


                //vehicleImagePreviewDialog5.getPerameter(this, "Passport", false);
                //vehicleImagePreviewDialog5.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.rashanCard_img:

/*                ImagePreviewDialog vehicleImagePreviewDialog6 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*

                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("trashanphoto"), img);
                    }

                    @Override
                    public void downloadImage() {

                        String trashanvalue = ii.getStringExtra("trashanphoto");

                        if (!trashanvalue.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("trashanphoto");
                            new DownloadImage().execute(url, "rashan_card_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/

                CustomDialogClass rashanCard = new CustomDialogClass(TenantDetailActivity.this);
                rashanCard.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                rashanCard.getPerameter(this, "Rashan Card", false, ii.getStringExtra("trashanphoto"), "trashanphoto");
                rashanCard.show();


                //vehicleImagePreviewDialog6.getPerameter(this, "Rashan Card", false);
                //vehicleImagePreviewDialog6.show(this.getSupportFragmentManager(), "image dialog");

                break;
            case R.id.otherId_img:

              /*  ImagePreviewDialog vehicleImagePreviewDialog7 = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {

                        *//*Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + tenantData.getArm_licence_photo())
                                .placeholder(R.drawable.ic_default_background)
                                .error(R.drawable.ic_default_background)
                                .into(img);*//*
                        Common.loadImage(mContext, WebUrls.IMG_URL + "" + ii.getStringExtra("totherphoto"), img);
                    }

                    @Override
                    public void downloadImage() {

                        String totherphoto = ii.getStringExtra("totherphoto");

                        if (!totherphoto.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("totherphoto");
                            new DownloadImage().execute(url, "other_id_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    }
                };*/


                CustomDialogClass otherId = new CustomDialogClass(TenantDetailActivity.this);
                otherId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                otherId.getPerameter(this, "Other Id", false, ii.getStringExtra("totherphoto"), "otherId");
                otherId.show();


                // vehicleImagePreviewDialog7.getPerameter(this, "Other Id", false);
                //vehicleImagePreviewDialog7.show(this.getSupportFragmentManager(), "image dialog");

                break;

            case R.id.tenantProfilePic:

               /* ImagePreviewDialog profilePicPreviewDialog = new ImagePreviewDialog() {
                    @Override
                    public void showImage(ImageView img) {


                        Picasso.with(TenantDetailActivity.this)
                                .load(WebUrls.IMG_URL + "" + ii.getStringExtra("tenantphoto"))
                                .placeholder(R.drawable.user_xl)
                                .error(R.drawable.user_xl)
                                .into(img);
                    }

                    @Override
                    public void downloadImage() {

                    }
                };
                profilePicPreviewDialog.getPerameter(this, "Profile Pic", true);
                profilePicPreviewDialog.show(this.getSupportFragmentManager(), "image dialog");*/


                CustomDialogClass cdd = new CustomDialogClass(TenantDetailActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(this, "Profile Pic", true, ii.getStringExtra("tenantphoto"), "TenantPic");
                cdd.show();


                break;


            case R.id.editPropertyBtn:
                Intent mIntent = new Intent(this, Tenant_Detail_Update_FirstActivity.class);
                mSharedStorage = SharedStorage.getInstance(this);
                mIntent.putExtra("isUpdate", true);
                mIntent.putExtra("RCUId", tenantid);
                mIntent.putExtra("room_id", roomidnewvalue);
                String value = propertyidforseparate;
                mIntent.putExtra("property_id", propertyidforseparate);
                mIntent.putExtra("owner_id", owneridmain);
                mIntent.putExtra("user_id", tuser);
                mIntent.putExtra("TRoomno", ii.getStringExtra("tbookedroomno"));
                mIntent.putExtra("Tfname", ii.getStringExtra("tfname"));
                mIntent.putExtra("Tlname", ii.getStringExtra("tlname"));
                mIntent.putExtra("Taddress", ii.getStringExtra("taddress"));
                mIntent.putExtra("Tpstatus", ii.getStringExtra("tpstatus"));
                mIntent.putExtra("Tcontact", ii.getStringExtra("tcontact"));
                mIntent.putExtra("Temail", ii.getStringExtra("temail"));
                mIntent.putExtra("Tfathername", ii.getStringExtra("tfathername"));
                mIntent.putExtra("Tfather_no", ii.getStringExtra("tfather_no"));
                mIntent.putExtra("Thiredate", ii.getStringExtra("thiredate"));
                mIntent.putExtra("Tleavedate", ii.getStringExtra("tleavedate"));
                mIntent.putExtra("Tpmode", ii.getStringExtra("tpmode"));
                mIntent.putExtra("Tporderid", ii.getStringExtra("tporderid"));
                mIntent.putExtra("Tptransactionid", ii.getStringExtra("tptransactionid"));
                //totalAmount.setText(ii.getStringExtra("troomamount"));
                mIntent.putExtra("Troomamount", ii.getStringExtra("troomamount"));
                mIntent.putExtra("TRoomno", ii.getStringExtra("tbookedroomno"));
                mIntent.putExtra("Tvehicalid", ii.getStringExtra("tvehicalid"));
                mIntent.putExtra("Tvoterid", ii.getStringExtra("tvoterid"));
                mIntent.putExtra("Tdrivingid", ii.getStringExtra("tdrivingid"));
                mIntent.putExtra("Tadharid", ii.getStringExtra("tadharid"));
                mIntent.putExtra("Tarmid", ii.getStringExtra("tarmid"));
                mIntent.putExtra("Tdriving_issue_ofc", ii.getStringExtra("tdriving_issue_ofc"));
                mIntent.putExtra("Tarm_issue_ofc", ii.getStringExtra("tarm_issue_ofc"));
                mIntent.putExtra("Trashanid", ii.getStringExtra("trashanid"));
                mIntent.putExtra("Tpassportid", ii.getStringExtra("tpassportid"));
                mIntent.putExtra("Totherid", ii.getStringExtra("totherid"));
                mIntent.putExtra("Tdetailverif", ii.getStringExtra("tdetailverif"));
                mIntent.putExtra("TCity", ii.getStringExtra("tcity"));
                mIntent.putExtra("TState", ii.getStringExtra("tstate"));
                mIntent.putExtra("TPincode", ii.getStringExtra("tpincode"));
                mIntent.putExtra("TFlatno", ii.getStringExtra("tflatno"));
                mIntent.putExtra("Tlandmark", ii.getStringExtra("tlandmark"));
                mIntent.putExtra("Tarea", ii.getStringExtra("tarea"));
                //mIntent.putExtra("TRcu_id",ii.getStringExtra("tRcu_id"));
                mIntent.putExtra("TTransaction_id", ii.getStringExtra("tTransaction_id"));
                mIntent.putExtra("Troomid", ii.getStringExtra("roomid"));
                mIntent.putExtra("TParent_id", ii.getStringExtra("tParent_id"));
                mIntent.putExtra("TPhoto", ii.getStringExtra("tPhoto"));
                //second from details
                mIntent.putExtra("TofficeAddress", ii.getStringExtra("tofficeAddress"));
                mIntent.putExtra("TotherVerifaction", ii.getStringExtra("totherVerifaction"));
                mIntent.putExtra("Tofc_institute", ii.getStringExtra("tofc_institute"));
                mIntent.putExtra("Tofc_contact", ii.getStringExtra("tofc_contact"));
                mIntent.putExtra("Twork_detail", ii.getStringExtra("twork_detail"));
                mIntent.putExtra("Tguarantorname", ii.getStringExtra("tguarantorname"));
                mIntent.putExtra("Tguarantoraddress", ii.getStringExtra("tguarantoraddress"));
                mIntent.putExtra("Tguarantorcontact", ii.getStringExtra("tguarantorcontact"));
                mIntent.putExtra("TDrivinglicenseNo", ii.getStringExtra("tDrivinglicenseNo"));
                mIntent.putExtra("TDrivinglicensePhoto", ii.getStringExtra("tDrivinglicensePhoto"));
                mIntent.putExtra("TDrivinglicenseIssuename", ii.getStringExtra("tDrivinglicenseIssuename"));
                mIntent.putExtra("TAadharcardNo", ii.getStringExtra("tAadharcardNo"));

                mIntent.putExtra("TAadharcardPhoto", ii.getStringExtra("tAadharcardPhoto"));
                mIntent.putExtra("TArmlicenceNo", ii.getStringExtra("tArmlicenceNo"));
                mIntent.putExtra("TArmlicencePhoto", ii.getStringExtra("tArmlicencePhoto"));
                mIntent.putExtra("TArmlicenceIssuename", ii.getStringExtra("tArmlicenceIssuename"));

                mIntent.putExtra("TOtherIdno", ii.getStringExtra("tOtherIdno"));
                mIntent.putExtra("TOtherIdPhoto", ii.getStringExtra("tOtherIdPhoto"));

                mIntent.putExtra("TVoteridcardno", ii.getStringExtra("tVoteridcardno"));
                mIntent.putExtra("TVoteridcardPhoto", ii.getStringExtra("tVoteridcardPhoto"));

                mIntent.putExtra("TVehicleRegistrationNo", ii.getStringExtra("tVehicleRegistrationNo"));
                mIntent.putExtra("TVehicleRegistrationPhoto", ii.getStringExtra("tVehicleRegistrationPhoto"));

                mIntent.putExtra("TPassportNo", ii.getStringExtra("tPassportNo"));
                mIntent.putExtra("TPassportPhoto", ii.getStringExtra("tPassportPhoto"));

                mIntent.putExtra("TRashanCardNo", ii.getStringExtra("tRashanCardNo"));
                mIntent.putExtra("TRashanCardPhoto", ii.getStringExtra("tRashanCardPhoto"));

                mIntent.putExtra("TsformTelephoneNo", ii.getStringExtra("tsformTelephoneNo"));
                mIntent.putExtra("TsformMobileNo", ii.getStringExtra("tsformMobileNo"));
                startActivity(mIntent);

                break;

        }
    }

    private void removedatamethod() {
        mSharedStore = SharedStorage.getInstance(this);
        String tfname, townerid, taddress, tamount, tmobile, tfathername,
                tfathercontact, thiredate, tleavedate, temail;
        tfname = fullName.getText().toString();
        tfathercontact = tenantFatherContactNo.getText().toString();
        taddress = tenantAddress.getText().toString();
        tamount = totalAmount.getText().toString();
        tmobile = tenantContact.getText().toString();
        tfathername = tenantFatherName.getText().toString();
        thiredate = tenantPropertyHireDate.getText().toString();
        tleavedate = tenantPropertyleaveDate.getText().toString();
        temail = tenantEmail.getText().toString();
        townerid = owneridmain;
        //new chnages on 12/11/2018
        getButtonStatus(tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact);
        //end
    }


    private void getAdvanceAmountHold(final String tfname, final String townerid, final String taddress, final String tmobile, final String tfathername, final String thiredate, final String tleavedate, final String temail, final String tfathercontact, final String AdvanceStatus, final String DueStatus) {

        final ProgressDialog dialog = new ProgressDialog(TenantDetailActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getUpdateAdvanceAmountHoldTenant");
            params.put("property_id", propertyidforseparate);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDatapayment(result, tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact, AdvanceStatus, DueStatus);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getAdvanceAmount(final String tfname, final String townerid, final String taddress, final String tmobile, final String tfathername, final String thiredate, final String tleavedate, final String temail, final String tfathercontact, final String AdvanceStatus, final String DueStatus) {

        final ProgressDialog dialog = new ProgressDialog(TenantDetailActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getUpdateAdvanceAmount");
            params.put("property_id", propertyidforseparate);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDatapayment(result, tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact, AdvanceStatus, DueStatus);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDatapayment(String result, String tfname, String townerid, String taddress, String tmobile, String tfathername, String thiredate, String tleavedate, String temail, String tfathercontact, String AdvanceStatus, String DueStatus) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                //vacant_btn.setEnabled(false);

                String Advance = jsmain.getString("records");
                //
                Intent ii = new Intent(TenantDetailActivity.this, UserRemoveActivity.class);
                ii.putExtra("Tname", tfname);
                ii.putExtra("Towner", townerid);
                ii.putExtra("Taddress", taddress);
                ii.putExtra("Tamount", Advance);//advance amount
                ii.putExtra("Tmobile", tmobile);
                ii.putExtra("Tfather", tfathername);
                ii.putExtra("Thire", thiredate);
                ii.putExtra("Tleave", tleavedate);
                ii.putExtra("Temail", temail);
                ii.putExtra("NTenantid", tenantid);
                ii.putExtra("TBookRoom", roomidnewvalue);
                ii.putExtra("TDue", Dueamount);
                ii.putExtra("sepratepropertyid", propertyidforseparate);
                ii.putExtra("TFatherCont", tfathercontact);
                ii.putExtra("Tuserid", tuser);
                ii.putExtra("tmonthlyRoomRant", monthlyRoomRant);
                //changes on 16/10/2018 ashish
                ii.putExtra("holduserstatus", holduserstatus);
                ii.putExtra("advstatus", AdvanceStatus);
                ii.putExtra("duestatus", DueStatus);
                //

                //no use only testing

                //ii.putExtra("modal", (Parcelable)getIntent().getParcelableExtra("modal"));

                ii.putExtra("modal", (Parcelable) tenantData);


                //
                startActivity(ii);

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tenantid, propertyidforseparate, tuser, "Police Station");
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

               // String pdf = "https://www.icicibank.com/managed-assets/docs/about-us/2016/eazypay-Indias-first-mobile-app-for-merchants-to-accept-payments-on-mobile-phone-through-multiple-digital-modes.pdf";

           /*     BackTask bt = new BackTask(pdf);
                bt.execute();*/
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
            pDialog = new ProgressDialog(TenantDetailActivity.this);
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
            mCommon.displayAlert(TenantDetailActivity.this, "Image dowmloaded succesfully", false);
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

        Uri path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path = FileProvider.getUriForFile(TenantDetailActivity.this, "com.krooms.hostel.rental.property.app", pdfFile);
        } else {
            path = Uri.fromFile(pdfFile);
        }
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TenantDetailActivity.this,
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
    public class DueAmountGetTenant extends AsyncTask<String, String, String> {

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

            mSharedStorage = SharedStorage.getInstance(TenantDetailActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "tenantremaining_amount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", tuser));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyidforseparate));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));
                Log.d("uu ", "" + tenantData.getPropertyId());

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
                    Log.d("id 1", "" + adavnce);
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

            mSharedStorage = SharedStorage.getInstance(TenantDetailActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "tenantremaining_amount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", tuser));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getUserPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));

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
    //api calling for remove child
    // for separte tenant login
    public class RemoveChildFromtenant extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(TenantDetailActivity.this);
        String namemainvalue, emailmainvalue, mobileNomainvalue, message;
        String result;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "childbreakrelation"));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("status");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("true")) {

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
            dialog.dismiss();
            if (!IsError) {
                if (result.equalsIgnoreCase("true")) {
                    Toast.makeText(TenantDetailActivity.this, "Child Remove Successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(TenantDetailActivity.this, message, Toast.LENGTH_LONG).show();
                }
            } else {
            }
        }
    }
    //.........

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (mSharedStorage.getUserType().equals("5")) {
                    startActivity(new Intent(TenantDetailActivity.this, Home_Accountantactivity.class));

                } else {
                    //change by me 8/9/2017
                    if (valuefrom.equalsIgnoreCase("Tenant")) {
                        startActivity(new Intent(TenantDetailActivity.this, CoTenantListActivity.class));
                        finish();
                    } else if (valuefrom.equalsIgnoreCase("Owner")) {
                        startActivity(new Intent(TenantDetailActivity.this, MyPropertyUsersListActivity.class));
                        finish();
                    } else {
                        Intent intent = new Intent(TenantDetailActivity.this, ViewChildWorkingActivity.class);
                        intent.putExtra("useridvalue", buserid);
                        intent.putExtra("parentvalueid", bparentid);
                        finish();
                    }

                    //
                    //old
                   /* startActivity(new Intent(TenantDetailActivity.this,HostelListActivity.class));
*/
                }

                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    //for tenant details get
    private class TenantupdateDetailsNewLogic extends AsyncTask<String, String, String> {

        String result, message;
        String name;
        private boolean IsError = false;
        private ProgressDialog dialog = new ProgressDialog(TenantDetailActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {

                String parentuserid = mSharedStorage.getUserId();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getchildlist"));
                nameValuePairs.add(new BasicNameValuePair("parent_user_id", parentuserid));
                nameValuePairs.add(new BasicNameValuePair("parent_id", parentidvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsonObject = new JSONObject(objectmain);
                result = jsonObject.getString("flag");
                if (result.equals("Y")) {
                    if (mArrayBookedUser.size() != 0) {
                        mArrayBookedUser.clear();

                    }

                    if (Common.rcuDetails.size() != 0) {
                        Common.rcuDetails.clear();
                    }

                    JSONArray jsoArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsoArray.length(); i++) {
                        String statusbooking = jsoArray.getJSONObject(i).getString("status");
                        String statuactivate = jsoArray.getJSONObject(i).getString("status_active");
                        if (statusbooking.equals("1") && statuactivate.equals("")) {


                            if (tenantid.equals(jsoArray.getJSONObject(i).getString("id"))) {
                                noOfTenant = "1";
                                PropertyUserModal propertyUserModal = new PropertyUserModal(Parcel.obtain());
                                propertyUserModal.setPropertyId(jsoArray.getJSONObject(i).getString("property_id"));
                                propertyUserModal.setTenant_id(jsoArray.getJSONObject(i).getString("id"));
                                propertyUserModal.setTenant_form_no(jsoArray.getJSONObject(i).getString("tenant_form_no"));
                                propertyUserModal.setT_user_id((jsoArray.getJSONObject(i).getString("user_id")));
                                propertyUserModal.setTenant_fname(jsoArray.getJSONObject(i).getString("tenant_fname"));
                                propertyUserModal.setTenant_lname(jsoArray.getJSONObject(i).getString("tenant_lname"));
                                propertyUserModal.setTenant_father_name(jsoArray.getJSONObject(i).getString("tenant_father_name"));
                                propertyUserModal.setLandmark(jsoArray.getJSONObject(i).getString("landmark"));
                                propertyUserModal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
                                propertyUserModal.setFlat_number(jsoArray.getJSONObject(i).getString("flat_number"));
                                propertyUserModal.setTenant_contact_number(jsoArray.getJSONObject(i).getString("tenant_contact_number"));
                                propertyUserModal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                                propertyUserModal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                                propertyUserModal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                                propertyUserModal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("flat_number"));
                                propertyUserModal.setAadhar_card_no(jsoArray.getJSONObject(i).getString("aadhar_card_no"));
                                propertyUserModal.setAadhar_card_photo(jsoArray.getJSONObject(i).getString("aadhar_card_photo"));
                                propertyUserModal.setArm_licence_no(jsoArray.getJSONObject(i).getString("arm_licence_no"));
                                propertyUserModal.setArm_licence_photo(jsoArray.getJSONObject(i).getString("arm_licence_photo"));
                                propertyUserModal.setVehicle_registration_no(jsoArray.getJSONObject(i).getString("vehicle_registration_no"));
                                propertyUserModal.setVehicle_registration_photo(jsoArray.getJSONObject(i).getString("vehicle_registration_photo"));
                                propertyUserModal.setVoter_id_card_no(jsoArray.getJSONObject(i).getString("voter_id_card_no"));
                                propertyUserModal.setVoter_id_card_photo(jsoArray.getJSONObject(i).getString("voter_id_card_photo"));
                                propertyUserModal.setDriving_license_no(jsoArray.getJSONObject(i).getString("driving_license_no"));
                                propertyUserModal.setDriving_license_photo(jsoArray.getJSONObject(i).getString("driving_license_photo"));
                                propertyUserModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                                propertyUserModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                                propertyUserModal.setGuarantor_name(jsoArray.getJSONObject(i).getString("guarantor_name"));
                                propertyUserModal.setGuarantor_address(jsoArray.getJSONObject(i).getString("guarantor_address"));
                                propertyUserModal.setGuarantor_contact_number(jsoArray.getJSONObject(i).getString("guarantor_contact_number"));
                                propertyUserModal.setState(jsoArray.getJSONObject(i).getString("state"));
                                propertyUserModal.setCity(jsoArray.getJSONObject(i).getString("city"));
                                propertyUserModal.setLocation(jsoArray.getJSONObject(i).getString("location"));
                                propertyUserModal.setTelephone_number(jsoArray.getJSONObject(i).getString("telephone_number"));
                                propertyUserModal.setMobile_number(jsoArray.getJSONObject(i).getString("mobile_number"));
                                propertyUserModal.setPincode(jsoArray.getJSONObject(i).getString("pincode"));
                                propertyUserModal.setContact_number(jsoArray.getJSONObject(i).getString("contact_number"));
                                propertyUserModal.setEmail_id(jsoArray.getJSONObject(i).getString("email_id"));
                                propertyUserModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                                propertyUserModal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                                propertyUserModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));
                                propertyUserModal.setBookedRoom(jsoArray.getJSONObject(i).getString("room_no"));
                                propertyUserModal.setBookedRoomId(jsoArray.getJSONObject(i).getString("room_id"));
                                propertyUserModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                                propertyUserModal.setRoomAmount(jsoArray.getJSONObject(i).getString("amount"));
                                propertyUserModal.setRemainingAmount(jsoArray.getJSONObject(i).getString("remaining_amount"));
                                propertyUserModal.setPaymentStatus(jsoArray.getJSONObject(i).getString("transaction_status"));
                                propertyUserModal.setUserAddress(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                                propertyUserModal.setPaymentStatus(jsoArray.getJSONObject(i).getString("transaction_status"));
                                propertyUserModal.setOwnerId(jsoArray.getJSONObject(i).getString("owner_id"));
                                propertyUserModal.setStatus_active(jsoArray.getJSONObject(i).getString("status_active"));
                                String owner_idnew = propertyUserModal.getOwnerId();
                                String owneridvaluemain_tenant = jsoArray.getJSONObject(i).getString("owner_id");
                                Log.d("owner id", owneridvaluemain_tenant);

                                propertyUserModal.setDriving_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("driving_license_issue_office"));
                                propertyUserModal.setArm_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("arm_name_issue_office"));
                                propertyUserModal.setPassport_no(jsoArray.getJSONObject(i).getString("passport_no"));
                                propertyUserModal.setPassport_photo(jsoArray.getJSONObject(i).getString("passport_photo"));
                                propertyUserModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                                propertyUserModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                                propertyUserModal.setOtherid_no(jsoArray.getJSONObject(i).getString("other_identity_card"));
                                propertyUserModal.setOtherid_photo(jsoArray.getJSONObject(i).getString("other_identity_photo"));
                                propertyUserModal.setDetail_verification(jsoArray.getJSONObject(i).getString("other_verification"));

                                if (jsoArray.getJSONObject(i).has("payment_type")) {
                                    propertyUserModal.setPaymentMode(jsoArray.getJSONObject(i).getString("payment_type"));
                                }
                                if (jsoArray.getJSONObject(i).has("order_id")) {
                                    propertyUserModal.setPaymentOrderId(jsoArray.getJSONObject(i).getString("order_id"));
                                }
                                if (jsoArray.getJSONObject(i).has("transition_number")) {
                                    propertyUserModal.setPaymentTransactionId(jsoArray.getJSONObject(i).getString("transition_number"));
                                }

                                RCUDetailModal rcuDetailModal = new RCUDetailModal(Parcel.obtain());
                                rcuDetailModal.setTenant_id(jsoArray.getJSONObject(i).getString("id"));
                                rcuDetailModal.setTenant_form_no(jsoArray.getJSONObject(i).getString("tenant_form_no"));
                                rcuDetailModal.setTenant_fname(jsoArray.getJSONObject(i).getString("tenant_fname"));
                                rcuDetailModal.setTenant_lname(jsoArray.getJSONObject(i).getString("tenant_lname"));
                                rcuDetailModal.setTenant_father_name(jsoArray.getJSONObject(i).getString("tenant_father_name"));
                                rcuDetailModal.setLandmark(jsoArray.getJSONObject(i).getString("landmark"));
                                rcuDetailModal.setTenant_father_contact_no(jsoArray.getJSONObject(i).getString("tenant_father_contact_no"));
                                rcuDetailModal.setFlat_number(jsoArray.getJSONObject(i).getString("flat_number"));
                                rcuDetailModal.setTenant_contact_number(jsoArray.getJSONObject(i).getString("tenant_contact_number"));
                                rcuDetailModal.setTenant_work_detail(jsoArray.getJSONObject(i).getString("tenant_work_detail"));
                                rcuDetailModal.setTenant_detail(jsoArray.getJSONObject(i).getString("tenant_detail"));
                                rcuDetailModal.setTenant_office_institute(jsoArray.getJSONObject(i).getString("tenant_office_institute"));
                                rcuDetailModal.setTenant_permanent_address(jsoArray.getJSONObject(i).getString("tenant_permanent_address"));
                                rcuDetailModal.setAadhar_card_no(jsoArray.getJSONObject(i).getString("aadhar_card_no"));
                                rcuDetailModal.setAadhar_card_photo(jsoArray.getJSONObject(i).getString("aadhar_card_photo"));
                                rcuDetailModal.setArm_licence_no(jsoArray.getJSONObject(i).getString("arm_licence_no"));
                                rcuDetailModal.setArm_licence_photo(jsoArray.getJSONObject(i).getString("arm_licence_photo"));
                                rcuDetailModal.setVehicle_registration_no(jsoArray.getJSONObject(i).getString("vehicle_registration_no"));
                                rcuDetailModal.setVehicle_registration_photo(jsoArray.getJSONObject(i).getString("vehicle_registration_photo"));
                                rcuDetailModal.setVoter_id_card_no(jsoArray.getJSONObject(i).getString("voter_id_card_no"));
                                rcuDetailModal.setVoter_id_card_photo(jsoArray.getJSONObject(i).getString("voter_id_card_photo"));
                                rcuDetailModal.setDriving_license_no(jsoArray.getJSONObject(i).getString("driving_license_no"));
                                rcuDetailModal.setDriving_license_photo(jsoArray.getJSONObject(i).getString("driving_license_photo"));

                                rcuDetailModal.setGuarantor_name(jsoArray.getJSONObject(i).getString("guarantor_name"));
                                rcuDetailModal.setGuarantor_address(jsoArray.getJSONObject(i).getString("guarantor_address"));
                                rcuDetailModal.setGuarantor_contact_number(jsoArray.getJSONObject(i).getString("guarantor_contact_number"));
                                rcuDetailModal.setState(jsoArray.getJSONObject(i).getString("state"));
                                rcuDetailModal.setCity(jsoArray.getJSONObject(i).getString("city"));
                                rcuDetailModal.setLocation(jsoArray.getJSONObject(i).getString("location"));
                                rcuDetailModal.setTelephone_number(jsoArray.getJSONObject(i).getString("telephone_number"));
                                rcuDetailModal.setMobile_number(jsoArray.getJSONObject(i).getString("mobile_number"));
                                rcuDetailModal.setPincode(jsoArray.getJSONObject(i).getString("pincode"));
                                rcuDetailModal.setContact_number(jsoArray.getJSONObject(i).getString("contact_number"));
                                rcuDetailModal.setEmail_id(jsoArray.getJSONObject(i).getString("email_id"));
                                rcuDetailModal.setProperty_hire_date(jsoArray.getJSONObject(i).getString("property_hire_date"));
                                rcuDetailModal.setProperty_leave_date(jsoArray.getJSONObject(i).getString("property_leave_date"));
                                rcuDetailModal.setTenant_room_no(jsoArray.getJSONObject(i).getString("room_no"));
                                rcuDetailModal.setTenant_room_id(jsoArray.getJSONObject(i).getString("room_id"));
                                rcuDetailModal.setTenant_photo(jsoArray.getJSONObject(i).getString("tenant_photo"));

                                rcuDetailModal.setDriving_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("driving_license_issue_office"));
                                rcuDetailModal.setArm_licence_issu_ofc_name(jsoArray.getJSONObject(i).getString("arm_name_issue_office"));
                                rcuDetailModal.setPassport_no(jsoArray.getJSONObject(i).getString("passport_no"));
                                rcuDetailModal.setPassport_photo(jsoArray.getJSONObject(i).getString("passport_photo"));
                                rcuDetailModal.setRashan_card_no(jsoArray.getJSONObject(i).getString("rashan_card_no"));
                                rcuDetailModal.setRashan_card_photo(jsoArray.getJSONObject(i).getString("rashan_card_photo"));
                                rcuDetailModal.setOtherid_no(jsoArray.getJSONObject(i).getString("other_identity_card"));
                                rcuDetailModal.setOtherid_photo(jsoArray.getJSONObject(i).getString("other_identity_photo"));
                                rcuDetailModal.setDetail_verification(jsoArray.getJSONObject(i).getString("other_verification"));


                                if (jsoArray.getJSONObject(i).has("payment_type")) {
                                    rcuDetailModal.setPaymentMode(jsoArray.getJSONObject(i).getString("payment_type"));
                                }
                                if (jsoArray.getJSONObject(i).has("order_id")) {
                                    rcuDetailModal.setPaymentOrderId(jsoArray.getJSONObject(i).getString("order_id"));
                                }
                                if (jsoArray.getJSONObject(i).has("transition_number")) {
                                    rcuDetailModal.setPaymentTransactionId(jsoArray.getJSONObject(i).getString("transition_number"));
                                }
                                mArrayBookedUser.add(propertyUserModal);
                                Common.rcuDetails.add(rcuDetailModal);
                            }

                        }

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
            dialog.dismiss();

            if (result.equalsIgnoreCase("Y")) {
                if (mArrayBookedUser.size() > 0) {

                    String rcuId = "";
                    String roomId = "";
                    if (Common.rcuDetails.size() != 0) {
                        for (int i = 0; i < Common.rcuDetails.size(); i++) {
                            rcuId = rcuId + Common.rcuDetails.get(i).getTenant_id() + ",";
                        }
                        for (int i = 0; i < mArrayBookedUser.size(); i++) {
                            roomId = roomId + mArrayBookedUser.get(i).getBookedRoomId() + ",";
                        }

                        Intent mIntent = new Intent(TenantDetailActivity.this, PropertyRCUTenantDetailActivity.class);
                        mIntent.putExtra("RCUId", "" + rcuId);
                        mIntent.putExtra("noOfTenant", noOfTenant);
                        mIntent.putExtra("isUpdate", true);
                        mIntent.putExtra("property_id", propertyidforseparate);
                        mIntent.putExtra("owner_id", owneridmain);
                        mIntent.putExtra("room_id", roomId);
                        startActivity(mIntent);
                    } else {
                        mCommon.displayAlert(TenantDetailActivity.this, "Data not found", true);
                    }
                }
            } else {
                Toast.makeText(TenantDetailActivity.this, "No Tanent Found", Toast.LENGTH_LONG).show();
            }
        }

    }


    private class PaidUnpaidLogicJson extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String ownerid, propertyidlastvalue;

        public PaidUnpaidLogicJson(String ownerid, String propertyidlastvalue) {
            this.ownerid = ownerid;
            this.propertyidlastvalue = propertyidlastvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(TenantDetailActivity.this);
            String userId_value = mSharedStorage.getUserId();
            //propertyidvalue_new=mSharedStorage.getUserPropertyId();
            String owneridvalue = ownerid;
            // Log.d("user id value",propertyidvalue_new);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getpaidservice"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyidlastvalue));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridvalue));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                //  {"flag":"Y","records":[{"paid_service":"attendence,video streaming"}],"message":"Get Paid Services !"}

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String paid_service = jsonObject.getString("paid_service");
                        paidarray = paid_service.split(",");
                        for (int j = 0; j < paidarray.length; j++) {
                            String paidvalue = paidarray[j];
                        }


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
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {

                    /*for(int j=0;j<paidarray.length;j++)
                    {
                        String paidvalue=paidarray[j];
                        if(paidvalue.equalsIgnoreCase("Payment"))
                        {
                            statusforpayment="true";
                            break;
                        }else
                        {
                            statusforpayment="false";
                        }

                    }
*/
                    // if(statusforpayment.equals("true")){

                    tanentpayment.setVisibility(View.VISIBLE);

                    // }else{
                    //     tanentpayment.setVisibility(View.GONE);
                    // }

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];

                        if (paidvalue.equalsIgnoreCase("attendance")) {

                            statusforattendance = "true";
                            break;
                        } else {

                            statusforattendance = "false";
                        }

                    }

                    if (statusforattendance.equals("true")) {

                        attendancereport.setVisibility(View.VISIBLE);

                    } else {
                        attendancereport.setVisibility(View.GONE);
                    }

                    for (int j = 0; j < paidarray.length; j++) {
                        String paidvalue = paidarray[j];

                        if (paidvalue.equalsIgnoreCase("inventory")) {

                            statusforinventory = "true";
                            break;
                        } else {

                            statusforinventory = "false";
                        }

                    }

                    if (statusforinventory.equals("true")) {

                        ShowAssignItem.setVisibility(View.VISIBLE);

                    } else {
                        ShowAssignItem.setVisibility(View.GONE);
                    }


                } else if (result.equals("N")) {
                }
            } else {
                Toast.makeText(TenantDetailActivity.this, "please check network connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class PaidUnpaidLogicMonthly extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String ownerid, propertyidlastvalue;

        public PaidUnpaidLogicMonthly(String ownerid, String propertyidlastvalue) {
            this.ownerid = ownerid;
            this.propertyidlastvalue = propertyidlastvalue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {

            mSharedStorage = SharedStorage.getInstance(TenantDetailActivity.this);
            String userId_value = mSharedStorage.getUserId();
            String owneridvalue = ownerid;
            String pid = propertyidlastvalue;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "getpaidservice"));
                nameValuePairs.add(new BasicNameValuePair("property_id", pid));
                nameValuePairs.add(new BasicNameValuePair("owner_id", owneridvalue));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);

                //  {"flag":"Y","records":[{"paid_service":"attendence,video streaming"}],"message":"Get Paid Services !"}

                JSONObject jsmain = new JSONObject(object);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String paid_service = jsonObject.getString("paid_service");
                        paidarray = paid_service.split(",");
                        for (int j = 0; j < paidarray.length; j++) {
                            String paidvalue = paidarray[j];
                        }


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


                for (int j = 0; j < paidarray.length; j++) {
                    String paidvalue = paidarray[j];

                    if (paidvalue.equalsIgnoreCase("attendance")) {

                        statusforattendance = "true";
                        break;
                    } else {

                        statusforattendance = "false";
                    }

                }

                tanentpayment.setVisibility(View.VISIBLE);

                if (statusforattendance.equals("true")) {

                    attendancereport.setVisibility(View.VISIBLE);

                } else {
                    attendancereport.setVisibility(View.GONE);
                }

                for (int j = 0; j < paidarray.length; j++) {
                    String paidvalue = paidarray[j];

                    if (paidvalue.equalsIgnoreCase("inventory")) {

                        statusforinventory = "true";
                        break;
                    } else {

                        statusforinventory = "false";
                    }
                }

                if (statusforinventory.equals("true")) {

                    ShowAssignItem.setVisibility(View.VISIBLE);

                } else {
                    ShowAssignItem.setVisibility(View.GONE);
                }

            } else if (result.equals("N")) {
            }

        }

    }

    //

    private void getTerms() {

        final ProgressDialog dialog = new ProgressDialog(TenantDetailActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "gettermsandconditionstatus");
            params.put("property_id", propertyidforseparate);
            params.put("tenant_id", tenantid);
            //params.put("terms", URLEncoder.encode(termsandcondition, "utf-8"));
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseData(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(TenantDetailActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                }
            });

            //1 min time out
            request_json.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseData(String result) {
        try {
            String status, message, terms = "";
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");

            if (status.equalsIgnoreCase("Y")) {
                txtterms.setText("Verify");
                //Toast.makeText(TermsAndCondition_ActivityDailog.this,message, Toast.LENGTH_SHORT).show();

            } else {
                txtterms.setText(" ");
                //Toast.makeText(TermsAndCondition_ActivityDailog.this,message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Get tenant Adv.adjust and no dues done status

    private void getButtonStatus(final String tfname, final String townerid, final String taddress, final String tmobile, final String tfathername, final String thiredate, final String tleavedate, final String temail, final String tfathercontact) {

        final ProgressDialog dialog = new ProgressDialog(TenantDetailActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getButtonStatus");
            params.put("property_id", propertyidforseparate);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getButtonresponse(result, tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(Tenant_Details_Activity.this, "studentlist" +error, Toast.LENGTH_LONG).show();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getButtonresponse(String result, String tfname, String townerid, String taddress, String tmobile, String tfathername, String thiredate, String tleavedate, String temail, String tfathercontact) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                String AdvanceStatus = jsmain.getString("Advancestatus");
                String DueStatus = jsmain.getString("Duestatus");
                String TenantReturnstatus = jsmain.getString("return_status");
                Utility.setSharedPreference(TenantDetailActivity.this, "AdvanceStatus", AdvanceStatus);
                Utility.setSharedPreference(TenantDetailActivity.this, "DueStatus", DueStatus);
                Utility.setSharedPreference(TenantDetailActivity.this, "TenantReturnStatus", TenantReturnstatus);
                if (holduserstatus.equals("1")) {
                    getAdvanceAmountHold(tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact, AdvanceStatus, DueStatus);
                } else {
                    getAdvanceAmount(tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact, AdvanceStatus, DueStatus);
                }

            } else {
                String AdvanceStatus = "";
                String DueStatus = "";
                String TenantReturnstatus = "";
                Utility.setSharedPreference(TenantDetailActivity.this, "AdvanceStatus", AdvanceStatus);
                Utility.setSharedPreference(TenantDetailActivity.this, "DueStatus", DueStatus);
                Utility.setSharedPreference(TenantDetailActivity.this, "TenantReturnStatus", TenantReturnstatus);

                if (holduserstatus.equals("1")) {
                    getAdvanceAmountHold(tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact, AdvanceStatus, DueStatus);
                } else {
                    getAdvanceAmount(tfname, townerid, taddress, tmobile, tfathername, thiredate, tleavedate, temail, tfathercontact, AdvanceStatus, DueStatus);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //


    //get due amount 13-12-2018
    //API to get Due Amount at Owner ent for tenant
    public class DueAmountGetOwnerNew extends AsyncTask<String, String, String> {

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

            mSharedStorage = SharedStorage.getInstance(TenantDetailActivity.this);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("action", "tenantremaining_amount"));
                nameValuePairs.add(new BasicNameValuePair("user_id", tuser));
                nameValuePairs.add(new BasicNameValuePair("property_id", mSharedStorage.getUserPropertyId()));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantid));

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

                removedatamethod();
            } else if (result.equals("N")) {

                removedatamethod();
            }

        }
    }

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

        public CustomDialogClass(TenantDetailActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, Boolean flag, String tenantphoto, String vehicleRcId) {
            this.mFActivity = activity;
            this.titleStr = titleStr;
            this.downloadBtnHide = flag;
            this.photo = tenantphoto;
            this.type = vehicleRcId;
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
                Picasso.with(TenantDetailActivity.this)
                        .load(WebUrls.IMG_URL + "" + photo)
                        .placeholder(R.drawable.user_xl)
                        .error(R.drawable.user_xl)
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

                    } else if (type.equals("VehicleRcId")) {
                        String vchale = ii.getStringExtra("tvehicalphoto");
                        if (!vchale.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tvehicalphoto");
                            new DownloadImage().execute(url, "vehicle_no_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    } else if (type.equals("VoterId")) {

                        String voteridvalue = ii.getStringExtra("tvoteridphoto");
                        if (!voteridvalue.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tvoteridphoto");
                            new DownloadImage().execute(url, "voter_id_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    } else if (type.equals("DrivingLicence")) {

                        String aa = ii.getStringExtra("tdrivinglicphoto");

                        if (!aa.equals("")) {

                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tdrivinglicphoto");
                            new DownloadImage().execute(url, "driving_licence_" + System.currentTimeMillis() + ".jpg");
                        } else {

                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }
                    } else if (type.equals("aadharCard")) {

                        String aadharcardvalue = ii.getStringExtra("taadharphoto");

                        if (!aadharcardvalue.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("taadharphoto");
                            new DownloadImage().execute(url, "aadhar_card_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }

                    } else if (type.equals("armLicence")) {

                        String tarm = ii.getStringExtra("tarmphoto");

                        if (!tarm.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tarmphoto");
                            new DownloadImage().execute(url, "arm_licence_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }

                    } else if (type.equals("passPort")) {

                        String tpasswort = ii.getStringExtra("tpassportphoto");

                        if (!tpasswort.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("tpassportphoto");
                            new DownloadImage().execute(url, "passport_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }

                    } else if (type.equals("trashanphoto")) {
                        String trashanvalue = ii.getStringExtra("trashanphoto");

                        if (!trashanvalue.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("trashanphoto");
                            new DownloadImage().execute(url, "rashan_card_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }


                    } else if (type.equals("otherId")) {

                        String totherphoto = ii.getStringExtra("totherphoto");

                        if (!totherphoto.equals("")) {
                            String url = WebUrls.IMG_URL + "" + ii.getStringExtra("totherphoto");
                            new DownloadImage().execute(url, "other_id_" + System.currentTimeMillis() + ".jpg");
                        } else {
                            mCommon.displayAlert(TenantDetailActivity.this, "Image not available", false);
                        }

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

    private class BackTask extends AsyncTask<String, Integer, Void> {
        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;
        File file = null;
        String name;

        public BackTask(String name) {
            this.name = name;
        }

        protected void onPreExecute() {
            super.onPreExecute();


            FILESAVE_BASEURL = Environment.getExternalStorageDirectory().getAbsolutePath();
            GET_MAIN_FOLDER_PATH = FILESAVE_BASEURL + File.separator + "KroomsPdf";
            //GET_TEMP_FOLDER_PATH = FILESAVE_BASEURL + File.separator + MAIN_FOLDER_NAME + File.separator + TEMP_FOLDER_NAME;
            File file = new File(GET_MAIN_FOLDER_PATH);
            if (!file.exists()) {
                file.mkdir();
            }

            Intent intent = new Intent();
            Uri uri = Uri.parse(GET_MAIN_FOLDER_PATH);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(uri, "file/*");

         /*   final PendingIntent contentIntent = PendingIntent.getActivity(TenantDetailActivity.this, 0, intent, 0);
            mBuilder = new NotificationCompat.Builder(TenantDetailActivity.this);
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(TenantDetailActivity.this);
            mBuilder.setContentTitle("File Download")
                    .setContentText(name)
                    .setSmallIcon(R.drawable.ic_add_icon)
                    .setContentIntent(contentIntent);*/
        }

        protected Void doInBackground(String... params) {
            URL url;
            int count;
            //                url = new URL(params[0]);
            try {
//                name="icici.pdf";
                URL ur = new URL(name);
                file = new File(GET_MAIN_FOLDER_PATH, "krrompdf.pdf");
                HttpURLConnection con = (HttpURLConnection) ur.openConnection();
                InputStream is = con.getInputStream();

                FileOutputStream fos = new FileOutputStream(file);
                int lenghtOfFile = con.getContentLength();
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = is.read(data)) != -1) {
                    total += count;
                    // publishing the progress
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                    // writing data to output file
                    fos.write(data, 0, count);
                }

                is.close();
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mBuilder.setProgress(100, progress[0], false);
            mNotifyManager.notify(0, mBuilder.build());
        }

        protected void onPostExecute(Void result) {
            mBuilder.setContentText("Download complete");
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(0, mBuilder.build());
        }
    }


    private void CheckFolderAvailability() {
        File file = new File(GET_MAIN_FOLDER_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        File vfile = new File(GET_MAIN_FOLDER_PATH, "iicc.pdf");
        if (!vfile.exists()) {
            vfile.mkdir();
        }
    }

    ///nnnn

}


