package com.krooms.hostel.rental.property.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.SharedStorage;

/**
 * Created by admin on 5/3/2018.
 */

public class Owner_Paid_Unpaid_Selection_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btn_monthly, btn_electricity, btn_advancepayment, btn_othercharge,btn_report;
    Context context;
    Intent mIntent;
    String propertyidvalue_new = "";
    RelativeLayout back_button;
    SharedStorage mShared;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paid_unpaid_selection_layout);
        context = getApplicationContext();
        mShared = SharedStorage.getInstance(this);
        findViewById();
        Intent ii = getIntent();
        propertyidvalue_new = ii.getStringExtra("Propertyid");
        btn_monthly.setOnClickListener(this);
        btn_electricity.setOnClickListener(this);
        btn_advancepayment.setOnClickListener(this);
        btn_othercharge.setOnClickListener(this);
        btn_report.setOnClickListener(this);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(Owner_Paid_Unpaid_Selection_Activity.this, Home_Accountantactivity.class));

                } else {
                    Intent i = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, HostelListActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void findViewById() {
        btn_monthly = (Button) findViewById(R.id.btn_monthly);
        btn_electricity = (Button) findViewById(R.id.btn_electricity);
        btn_advancepayment = (Button) findViewById(R.id.btn_advance);
        btn_othercharge = (Button) findViewById(R.id.btn_othercharge);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
        btn_report=(Button)findViewById(R.id.btn_report);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_advance:
                mIntent = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, AdvancePayment_Paid_Unpaid_Activity.class);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);
                break;

            case R.id.btn_monthly:
                mIntent = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, Payment_Paid_Unpaid_Activity.class);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);
                break;

            case R.id.btn_electricity:
                mIntent = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, Electricity_Paid_Unpaid_Activity.class);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);
                break;

            case R.id.btn_othercharge:
                mIntent = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, Other_Payment_Paid_Unpaid_Activity.class);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);
                break;

            case R.id.btn_report:

                mIntent = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, Owner_Package_Details_Activity.class);
                mIntent.putExtra("Propertyid", propertyidvalue_new);
                startActivity(mIntent);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mShared.getUserType().equals("5")) {
                    startActivity(new Intent(Owner_Paid_Unpaid_Selection_Activity.this, Home_Accountantactivity.class));

                } else {
                    Intent i = new Intent(Owner_Paid_Unpaid_Selection_Activity.this, HostelListActivity.class);
                    startActivity(i);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
