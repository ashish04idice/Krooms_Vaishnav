package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.asynctask.PaymentOptionThroughCase;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.ccavenue_activity.BillingShippingActivity;
import com.krooms.hostel.rental.property.app.ccavenue_utility.AvenuesParams;
import com.krooms.hostel.rental.property.app.ccavenue_utility.ServiceUtility;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.TermAndConditionDialog;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

/**
 * Created by user on 18-Feb-19.
 */

public class BedBookingPayment extends Activity implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private HostelDetailActivity mFActivity = null;
    private EditText totalAmount, totalAmount_paid;
    private TextView termAndCondition;
    private Button bookProperty;
    private RadioButton caseOption, onlineOption, chequeOption;
    private CheckBox checkeBox;
    TextView paidamounttext;
    SharedStorage mSharedStorage;
    String paymentmode = "";
    public String paidvalue, paymenttype, statusvalue;
    String paymentOption = "";
    PropertyModal mPropertyModal;
    LinearLayout layout_cheque_details;
    public String mHireDate = " ", mLeaveDate = " ", rentAmount = "";
    private ImageButton main_header_menu;
    public String propertyidmain = "";
    Integer randomNum;
    public String fromroomchnage = "";
    private Common mCommon;
    public String roomSelectedBed = "";
    public String roomIds = "";
    String advanceamount;
    EditText txtchequeno;
    String chequenovalue = "";
    RelativeLayout mainlayout;


    public BedBookingPayment(HostelDetailActivity hostelDetailActivity, PropertyModal propertyModal, String hireDate, String leaveDate, String rent, String advanceamount, String fromroomchangevalue) {

        mFActivity = hostelDetailActivity;
        mPropertyModal = propertyModal;
        mSharedStorage = SharedStorage.getInstance(mFActivity);
        mHireDate = hireDate;
        mLeaveDate = leaveDate;
        rentAmount = advanceamount;
        advanceamount = rent;
        fromroomchnage = fromroomchangevalue;
        randomNum = ServiceUtility.randInt(0, 9999999);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_selection_dialog);
        createView();
    }

    public void createView() {
        mCommon = new Common();
        totalAmount = (EditText) findViewById(R.id.totalAmount_output);
        paidamounttext = (TextView) findViewById(R.id.paidamounttext);
        termAndCondition = (TextView) findViewById(R.id.termsAndConditionTextView);
        bookProperty = (Button) findViewById(R.id.submitBtn);
        caseOption = (RadioButton) findViewById(R.id.casePaymentRadioBtn);
        onlineOption = (RadioButton) findViewById(R.id.onlinePaymentRadioBtn);
        chequeOption = (RadioButton) findViewById(R.id.chequePaymentRadioBtn);
        checkeBox = (CheckBox) findViewById(R.id.checkeBox);
        main_header_menu = (ImageButton) findViewById(R.id.main_header_menu);
        totalAmount_paid = (EditText) findViewById(R.id.totalAmount_paid);
        layout_cheque_details = (LinearLayout) findViewById(R.id.cheque_details);
        txtchequeno = (EditText) findViewById(R.id.txt_chequeno);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
        totalAmount.setText(rentAmount);
        if (mSharedStorage.getUserType().equalsIgnoreCase("2")) {
            layout_cheque_details.setVisibility(View.GONE);
        } else if (mSharedStorage.getUserType().equalsIgnoreCase("4")) {
            layout_cheque_details.setVisibility(View.GONE);
        } else {
            layout_cheque_details.setVisibility(View.VISIBLE);
        }

        totalAmount.setBackgroundColor(mFActivity.getResources().getColor(R.color.white));
        RadioGroup group = (RadioGroup) findViewById(R.id.paymentRadioGroup);

        String usertype = mSharedStorage.getUserType();
        if (usertype.equalsIgnoreCase("2")) {
            caseOption.setVisibility(View.GONE);
            chequeOption.setVisibility(View.GONE);
        }

        if (usertype.equalsIgnoreCase("4")) {
            caseOption.setVisibility(View.GONE);
            chequeOption.setVisibility(View.GONE);
        }

        if (fromroomchnage.equals("1")) {
            mainlayout.setVisibility(View.GONE);
            txtchequeno.setText("Room Change");
            checkeBox.setChecked(true);
            caseOption.setChecked(true);
            totalAmount_paid.setText("1");
            String paidamount = "1", chequenovalue = "Room Change";
            paidvalue = paidamount;
            paymentmode = "C";
            propertyidmain = mPropertyModal.getProperty_id();

            PaymentOptionThroughCase service = new PaymentOptionThroughCase(mFActivity);
            service.setCallBack(new ServiceResponce() {
                @Override
                public void requestResponce(String result) {

                }
            });

            int noOfBed = 1;
            for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                if (i == 0) {
                    roomIds = Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = "1";
                } else {
                    roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = roomSelectedBed + "," + 1;
                }
            }

            int rentamountvalue = Integer.parseInt(rentAmount);
            int sizemain = Common.selectedBedInfo.size();
            int rentamountvaluenew = rentamountvalue / sizemain;
            String renatmainvalue = String.valueOf(rentamountvaluenew);

            service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mPropertyModal.getProperty_id(),
                    /*Common.selectedBedInfo.get(0).getRoomId()*/roomIds, renatmainvalue, mHireDate, mLeaveDate,/*""+noOfBed*/roomSelectedBed, paidvalue, "C", "PENDING", chequenovalue);

        }


        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (caseOption.isChecked()) {
                    // layout_cheque_details.setVisibility(View.GONE);
                    Common.Config("case option selected     ");
                    totalAmount.setFocusable(false);
                    totalAmount.setFocusableInTouchMode(false);
                    totalAmount.setClickable(false);
                    totalAmount.setBackgroundColor(mFActivity.getResources().getColor(R.color.white));
                    totalAmount.setText(rentAmount);
                    totalAmount_paid.setVisibility(View.VISIBLE);
                    paidamounttext.setVisibility(View.VISIBLE);

                } else if (onlineOption.isChecked()) {
                    // layout_cheque_details.setVisibility(View.GONE);
                    Common.Config("online option selected     ");
                    totalAmount.setFocusable(true);
                    totalAmount.setFocusableInTouchMode(true);
                    totalAmount.setBackgroundResource(R.drawable.input_text_background);
                    totalAmount.setClickable(true);
                    totalAmount_paid.setVisibility(View.INVISIBLE);
                    paidamounttext.setVisibility(View.INVISIBLE);
                } else if (chequeOption.isChecked()) {
                    //layout_cheque_details.setVisibility(View.VISIBLE);
                    totalAmount.setFocusable(false);
                    totalAmount.setFocusableInTouchMode(false);
                    totalAmount.setClickable(false);
                    totalAmount.setBackgroundColor(mFActivity.getResources().getColor(R.color.white));
                    totalAmount.setText(rentAmount);
                    totalAmount_paid.setVisibility(View.VISIBLE);
                    paidamounttext.setVisibility(View.VISIBLE);
                }
            }
        });


        setViewListners();

    }


    public void setViewListners() {

        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermAndConditionDialog dialog = new TermAndConditionDialog() {
                    @Override
                    public void agreeBtnClicked() {
                        checkeBox.setChecked(true);
                    }
                };
                dialog.getPerameter(mFActivity, false);
                dialog.show(mFActivity.getSupportFragmentManager(), "termDialog");
            }
        });

        bookProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentOption = caseOption.isChecked() ? "cash" : onlineOption.isChecked() ? "online" : chequeOption.isChecked() ? "cheque" : "";
                paidvalue = totalAmount_paid.getText().toString();

                if (paymentOption.equals("")) {
                    Toast.makeText(mFActivity, "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else if (checkeBox.isChecked()) {

                    if (paymentOption.equalsIgnoreCase("cash")) {

                        chequenovalue = txtchequeno.getText().toString().trim();

                        if (paidvalue.equals("")) {
                            Toast.makeText(mFActivity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                        } else if (paidvalue.startsWith("0")) {

                            Toast.makeText(mFActivity, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                        } else {
                            paidvalue = totalAmount_paid.getText().toString();
                            paymentmode = "C";
                            paymentThroughCash(chequenovalue);
                        }

                    } else if (paymentOption.equalsIgnoreCase("online")) {
                        chequenovalue = txtchequeno.getText().toString().trim();
                        paidvalue = totalAmount.getText().toString();
                        int mainamount = Integer.parseInt(paidvalue);

                        //by me only testing
                        if (mainamount > 1500) {
                            paymentmode = "O";
                            paymentThroughCash(chequenovalue);
                        } else {
                            Toast.makeText(mFActivity, "Please Enter Amount Above 1500", Toast.LENGTH_SHORT).show();
                        }

                    } else if (paymentOption.equalsIgnoreCase("cheque")) {

                        chequenovalue = txtchequeno.getText().toString().trim();

                        if (paidvalue.equals("")) {
                            Toast.makeText(mFActivity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                        } else if (paidvalue.startsWith("0")) {
                            Toast.makeText(mFActivity, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                        } else {

                            if (paidvalue.equals("")) {
                                Toast.makeText(mFActivity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                            } else if (paidvalue.startsWith("0")) {

                                Toast.makeText(mFActivity, "Please Enter Amount Without Zero", Toast.LENGTH_LONG).show();

                            } else {
                                paidvalue = totalAmount_paid.getText().toString();
                                paymentmode = "Cheque";
                                paymentThroughCash(chequenovalue);
                            }
                        }
                    }
                } else {
                    mCommon.displayAlert(mFActivity, "In order to use our services, you must agree to Terms and Condition.", false);
                }

            }
        });

        main_header_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


    public void paymentThroughCash(String chequenovalue) {

        propertyidmain = mPropertyModal.getProperty_id();
        if (paymentOption.equals("cash")) {

            PaymentOptionThroughCase service = new PaymentOptionThroughCase(mFActivity);
            service.setCallBack(new ServiceResponce() {
                @Override
                public void requestResponce(String result) {

                }
            });

            int noOfBed = 1;

            for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                if (i == 0) {
                    roomIds = Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = "1";
                } else {
                    roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = roomSelectedBed + "," + 1;
                }
            }


            int rentamountvalue = Integer.parseInt(rentAmount);
            int sizemain = Common.selectedBedInfo.size();
            int rentamountvaluenew = rentamountvalue / sizemain;
            String renatmainvalue = String.valueOf(rentamountvaluenew);

            service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mPropertyModal.getProperty_id(),
                    /*Common.selectedBedInfo.get(0).getRoomId()*/roomIds, renatmainvalue, mHireDate, mLeaveDate,/*""+noOfBed*/roomSelectedBed, paidvalue, "C", "PENDING", chequenovalue);

        } else if (paymentOption.equals("cheque")) {

            PaymentOptionThroughCase service = new PaymentOptionThroughCase(mFActivity);
            service.setCallBack(new ServiceResponce() {
                @Override
                public void requestResponce(String result) {

                }
            });

            int noOfBed = 1;

            for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                if (i == 0) {
                    roomIds = Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = "1";
                } else {
                    roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = roomSelectedBed + "," + 1;
                }
            }


            int rentamountvalue = Integer.parseInt(rentAmount);
            int sizemain = Common.selectedBedInfo.size();
            int rentamountvaluenew = rentamountvalue / sizemain;
            String renatmainvalue = String.valueOf(rentamountvaluenew);

            service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSharedStorage.getUserId(), mPropertyModal.getProperty_id(),
                    roomIds, renatmainvalue, mHireDate, mLeaveDate, roomSelectedBed, paidvalue, "Cheque", "PENDING", chequenovalue);

        } else {


            int noOfBed = 1;
            for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
                if (i == 0) {
                    roomIds = Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = "1";
                } else {
                    roomIds = roomIds + "," + Common.selectedBedInfo.get(i).getRoomId();
                    roomSelectedBed = roomSelectedBed + "," + 1;
                }
            }


            int rentamountvalue = Integer.parseInt(rentAmount);
            int sizemain = Common.selectedBedInfo.size();
            int rentamountvaluenew = rentamountvalue / sizemain;
            String renatmainvalue = String.valueOf(rentamountvaluenew);

            if (totalAmount.getText().toString().trim().length() > 0) {
                double amount = Double.parseDouble(totalAmount.getText().toString());
                if (amount <= Double.parseDouble(rentAmount)) {
                    if (amount >= 1) {
                        Intent intent = new Intent(mFActivity, BillingShippingActivity.class);
                        intent.putExtra(AvenuesParams.ACCESS_CODE, WebUrls.ACCESS_CODE);
                        intent.putExtra(AvenuesParams.MERCHANT_ID, WebUrls.MERCHANT_ID);
                        intent.putExtra(AvenuesParams.ORDER_ID, "" + randomNum);
                        intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "stg");
                        intent.putExtra(AvenuesParams.CURRENCY, WebUrls.CURRENCY);
                        intent.putExtra(AvenuesParams.AMOUNT, amount + "");
                        intent.putExtra(AvenuesParams.REDIRECT_URL, WebUrls.REDIRECT_URL);
                        intent.putExtra(AvenuesParams.CANCEL_URL, WebUrls.CANCEL_URL);
                        intent.putExtra(AvenuesParams.RSA_KEY_URL, WebUrls.RSA_KEY_URL);
                        intent.putExtra(AvenuesParams.USER_ID, mSharedStorage.getUserId());
                        intent.putExtra(AvenuesParams.PROPERTY_ID, mPropertyModal.getProperty_id());
                        intent.putExtra(AvenuesParams.ROOM_ID, roomIds);
                        intent.putExtra(AvenuesParams.ROOM_AMOUNT, renatmainvalue);
                        intent.putExtra(AvenuesParams.HIRING_DATE, mHireDate);
                        intent.putExtra(AvenuesParams.LEAVING_DATE, mLeaveDate);
                        intent.putExtra(AvenuesParams.BOOKED_BED, "" + roomSelectedBed);
                        intent.putExtra(AvenuesParams.comment, "" + chequenovalue);//for comment entry

                        startActivity(intent);
                    } else {
                        mCommon.displayAlert(mFActivity, "Amount should be greater or equal then Minimum Amount (1500). ", false);
                    }
                } else {
                    mCommon.displayAlert(mFActivity, "Amount should be less or equal then Total Amount (" + rentAmount + ") ", false);
                }
            } else {
                mCommon.displayAlert(mFActivity, "Please enter correct amount", false);
                totalAmount.setText(rentAmount);
            }
        }
    }


    public void setListner() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }
}
