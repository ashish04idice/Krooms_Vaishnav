package com.krooms.hostel.rental.property.app.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.krooms.hostel.rental.property.app.R;
/**
 * Created by user on 4/6/2016.
 */
public abstract class PaymentConfirmationDialog   extends DialogFragment implements View.OnClickListener {

    private FragmentActivity mFActivity = null;

    EditText paymetInput;

    Button yesBtn,noBtn;
    int amount = 0;


    public PaymentConfirmationDialog(){}

    public void getPerameter(FragmentActivity activity, String remainingAmount) {
        this.mFActivity = activity;
        this.amount = Integer.parseInt(remainingAmount);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pyament_confirmation_dialog, container);
        createView(view);
        setListner();
        return view;
    }


    public void createView(View v){
        paymetInput = (EditText) v.findViewById(R.id.payment_input);
        yesBtn = (Button) v.findViewById(R.id.btn1);
        noBtn = (Button) v.findViewById(R.id.btn2);
    }

    public void setListner(){
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
    }


    private static boolean permissionDialogShown = false;

    @Override
    public void show(FragmentManager manager, String tag) {
        if (permissionDialogShown) {
            return;
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        super.show(manager, tag);
        permissionDialogShown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        permissionDialogShown = false;
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn1:
                if(paymetInput.getText().toString().trim().length()!=0){
                    int newAmount = Integer.parseInt(paymetInput.getText().toString().trim());

                    if(newAmount<=amount){
                        paymentConfirmBtn(""+newAmount);
                        dismiss();
                    }else{
                        paymetInput.setError("Amount should be less or equal to Remaining amount.");
                    }
                }else{
                    paymetInput.setError("Please enter correct amount.");
                }

                break;
            case R.id.btn2:
                dismiss();
                break;


        }

    }

    public abstract void paymentConfirmBtn(String payment);

}
