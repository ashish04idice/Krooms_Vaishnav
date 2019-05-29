package com.krooms.hostel.rental.property.app.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;

public class AttendanceSmsAlert {


    public void showDialog(String title, String txt, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView txt1 = (TextView) layout.findViewById(R.id.toastmsg);
        txt1.setText(txt);
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.green_right);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
