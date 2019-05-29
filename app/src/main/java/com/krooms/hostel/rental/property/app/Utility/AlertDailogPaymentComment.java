package com.krooms.hostel.rental.property.app.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.HostelListActivity;
import com.krooms.hostel.rental.property.app.activity.PropertyInfoThirdRoomActivity;

/**
 * Created by admin on 3/12/2018.
 */

public class AlertDailogPaymentComment {


    public static void displayAlert(final Activity mActivity, String message) {

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_with_paymentdailog);
        dialog.setCancelable(false);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        txtTitle.setText("Remark");
        TextView txtSms = (TextView) dialog.findViewById(R.id.categoryNameInput);
        txtSms.setText(message);
        Button btnOk = (Button) dialog.findViewById(R.id.alertOkBtn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (!dialog.isShowing())
            dialog.show();
    }


}
