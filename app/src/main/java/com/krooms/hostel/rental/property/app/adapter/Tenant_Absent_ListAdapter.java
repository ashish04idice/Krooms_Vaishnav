package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.UserModel;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by anuradhaidice on 22/4/17.
 */

public class Tenant_Absent_ListAdapter extends BaseAdapter {

    Context context;
    String[] tenantname;
    ArrayList<UserModel> userlist;

    public Tenant_Absent_ListAdapter(Context context, ArrayList<UserModel> userlist) {
        this.context = context;
        this.userlist = userlist;
    }

    @Override
    public int getCount() {
        return userlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.grid_itemview_layout_new, null, true);
        TextView txtroomno = (TextView) vi.findViewById(R.id.txtroomno);
        TextView txtname = (TextView) vi.findViewById(R.id.txtname);
        TextView txttime = (TextView) vi.findViewById(R.id.txttime);
        TextView txtdate = (TextView) vi.findViewById(R.id.txtdate);
        ImageView profileimg = (ImageView) vi.findViewById(R.id.tenant_pic);
        ImageView imageborder = (ImageView) vi.findViewById(R.id.border);
        txtroomno.setText(userlist.get(position).getRoomno());
        txtname.setText(userlist.get(position).getUsername());


        try {
            String input = userlist.get(position).getTime();
            DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
            //Desired format: 24 hour format: Change the pattern as per the need
            DateFormat outputformat = new SimpleDateFormat("hh:mm: aa");
            Date date = null;
            String output = null;
            try {
                //Converting the input String to Date
                date = df.parse(input);
                //Changing the format of date and storing it in String
                output = outputformat.format(date);
                //Displaying the date
                txttime.setText(output);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        try {
            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM");
            String inputDateStr = userlist.get(position).getDate();
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            txtdate.setText(outputDateStr);

        } catch (Exception e) {

            e.printStackTrace();
        }

        String attendcestatusvalue = userlist.get(position).getAttendance_reasons();
        try {

            if (userlist.get(position).getImageUrl() == null || userlist.get(position).getImageUrl().isEmpty() || userlist.get(position).getImageUrl().equals("null") || userlist.get(position).getImageUrl().equals("0")) {

            } else {

                File imgFile = new File(userlist.get(position).getImageUrl());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    profileimg.setImageBitmap(myBitmap);
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        if (attendcestatusvalue.equalsIgnoreCase("Sign in")) {
            txtroomno.setBackgroundResource(R.drawable.frame_attendance_roomno_green);
            imageborder.setBackgroundResource(R.drawable.frame_attendance_green);
        } else if (attendcestatusvalue.equalsIgnoreCase("sign out")) {
        } else if (attendcestatusvalue.equalsIgnoreCase("Market")) {
            txtroomno.setBackgroundResource(R.drawable.frame_attendance_roomno_blue);
            imageborder.setBackgroundResource(R.drawable.frame_attendance_blue);
        } else if (attendcestatusvalue.equalsIgnoreCase("Class")) {
            txtroomno.setBackgroundResource(R.drawable.frame_attendance_roomno_yellow);
            imageborder.setBackgroundResource(R.drawable.frame_attendance_yellow);
        } else if (attendcestatusvalue.equalsIgnoreCase("Vacation")) {
            txtroomno.setBackgroundResource(R.drawable.frame_attendance_roomno_black);
            imageborder.setBackgroundResource(R.drawable.frame_attendance_black);
        }

        return vi;
    }

}
