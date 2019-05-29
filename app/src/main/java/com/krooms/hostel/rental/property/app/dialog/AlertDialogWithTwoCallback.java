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
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;


/**
 * Created by user on 1/14/2016.
 */
public abstract class AlertDialogWithTwoCallback extends DialogFragment {

    private FragmentActivity mFActivity = null;

    private TextView title;
    private TextView message;
    private Button alertYesBtn;
    private Button alertNoBtn;
    private String titleStr;
    private String messageStr;


    public AlertDialogWithTwoCallback(){}

    public void getPerameter(FragmentActivity activity, String titleStr, String messageStr) {

        mFActivity = activity;
        this.titleStr = titleStr;
        this.messageStr = messageStr;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alert_two_btn_dialog, container);

        title = (TextView) view.findViewById(R.id.alertTitle);
        message = (TextView) view.findViewById(R.id.categoryNameInput);
        alertYesBtn = (Button) view.findViewById(R.id.alertYesBtn);
        alertNoBtn = (Button) view.findViewById(R.id.alertNoBtn);

        title.setText(titleStr);
        message.setText(messageStr);

        alertYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack();
                dismiss();
            }
        });

        alertNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCallBack();
                dismiss();
            }
        });

        return view;
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

    public abstract void callBack();
    public abstract void cancelCallBack();

}

