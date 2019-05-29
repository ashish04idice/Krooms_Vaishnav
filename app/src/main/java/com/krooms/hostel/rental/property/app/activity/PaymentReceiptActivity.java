package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.asynctask.GetTransactionReport;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentReceiptActivity extends AppCompatActivity implements ServiceResponce {

    private TextView propertyName = null;
    private TextView propertyAdress = null;
    private TextView roomNo = null;
    private TextView propertyType = null;
    private TextView tenantName = null;
    private TextView contactNo = null;
    private TextView emailId = null;
    private TextView transactionNo = null;
    private TextView orderId = null;
    private TextView totalAmount = null;
    private TextView paidAmount = null;
    private TextView RemainingAmount = null;
    private Common mCommon;
    private Activity mActivity;
    private SharedStorage mSharedStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);
        mActivity = this;
        mCommon = new Common();
        mSharedStorage = SharedStorage.getInstance(this);
        intiView();
        callWebservice();
    }

    public void intiView() {

        propertyName = (TextView) findViewById(R.id.propertyName);
        propertyAdress = (TextView) findViewById(R.id.propertyAdress);
        roomNo = (TextView) findViewById(R.id.roomNo);
        propertyType = (TextView) findViewById(R.id.propertyType);
        tenantName = (TextView) findViewById(R.id.tenantName);
        contactNo = (TextView) findViewById(R.id.contactNumber);
        emailId = (TextView) findViewById(R.id.emailId);
        transactionNo = (TextView) findViewById(R.id.transactionNumber);
        orderId = (TextView) findViewById(R.id.orderId);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        paidAmount = (TextView) findViewById(R.id.paidAmount);
        RemainingAmount = (TextView) findViewById(R.id.remainingAmount);

    }

    public void callWebservice() {

        GetTransactionReport service = new GetTransactionReport(this);
        service.setCallBack(this);
        service.execute(mSharedStorage.getUserId(), mSharedStorage.getBookedPropertyId());

    }

    @Override
    public void requestResponce(String result) {

        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(mActivity, Common.TimeOut_Message, false);
            } else {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    JSONObject data = jsonObject.getJSONObject(WebUrls.DATA_JSON);
                    JSONObject PropertyData = data.getJSONObject("0");
                    propertyName.setText("Property Name : " + data.getString("property_name"));
                    propertyAdress.setText("Address : " + data.getString("owner_address"));
                    roomNo.setText("Room No :");
                    propertyType.setText("Property Type : " + PropertyData.getString("property_type_title"));
                    tenantName.setText("Primary Tenant : " + data.getString("fname"));
                    contactNo.setText("");
                    emailId.setText("Email : " + data.getString("email"));
                    transactionNo.setText("Transaction No. : " + data.getString("transition_number"));
                    orderId.setText("Order Id  : " + data.getString("order_id"));
                    totalAmount.setText("Total Amount  : " + data.getString("amount"));
                    int paidAmountVar = 0;
                    if (!data.getString("amount").equals("")) {
                        if (data.getString("remaining_amount").equals("")) {
                            paidAmountVar = Integer.parseInt(data.getString("amount"));
                        } else {
                            paidAmountVar = Integer.parseInt(data.getString("amount")) - Integer.parseInt(data.getString("remaining_amount"));
                        }

                    }

                    paidAmount.setText("Paid Amount : " + paidAmountVar);
                    RemainingAmount.setText("Remaining Amount  : " + data.getString("remaining_amount"));
                }
            }
        } catch (JSONException e) {

        } catch (NullPointerException e) {
        }

    }

}
