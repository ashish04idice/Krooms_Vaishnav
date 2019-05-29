package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.AlertDailogPaymentComment;
import com.krooms.hostel.rental.property.app.modal.MonthDetailModal;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 11/10/2016.
 */
public class ShowMonthDetailOwnerAdapter extends BaseAdapter {

    String monthname_value = "", totalpayment_value = "", paid_value = "", due_value = "", totalpaid_value = "", mode_value = "", date_value = "";
    String room_id, owner_id, month_id_value, paymentcomment = "";
    ArrayList<MonthDetailModal> data_model_arraylist;
    private Activity context;
    String property_id_value, user_id_value;

    public ShowMonthDetailOwnerAdapter(Activity context, ArrayList<MonthDetailModal> data_model_arraylist, String property_id_value, String user_id_value) {
        this.context = context;
        this.data_model_arraylist = data_model_arraylist;
        this.property_id_value = property_id_value;
        this.user_id_value = user_id_value;
    }

    @Override
    public int getCount() {
        return data_model_arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return (position);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater;
        final TextView monthname, totalpayment, paid, monthid;
        inflater = context.getLayoutInflater();
        View rowView = null;
        int type = getItemViewType(position);
        Calendar calendar = Calendar.getInstance();
        int month_no = (calendar.get(Calendar.MONTH)) + 1;
        String monthno_data = String.valueOf(month_no);
        RelativeLayout editmaode, mode_option;
        ImageView imagedata;
        {
            rowView = inflater.inflate(R.layout.listviewitemmonth, null, true);
            monthname = (TextView) rowView.findViewById(R.id.monthname);
            imagedata = (ImageView) rowView.findViewById(R.id.imagedata);
            totalpayment = (TextView) rowView.findViewById(R.id.totalpayment);
            paid = (TextView) rowView.findViewById(R.id.paid);
            TextView due = (TextView) rowView.findViewById(R.id.due);
            TextView totalpaid = (TextView) rowView.findViewById(R.id.totalpaid);
            TextView mode = (TextView) rowView.findViewById(R.id.mode);
            mode.setTextColor(Color.BLACK);
            TextView date = (TextView) rowView.findViewById(R.id.date);
            monthid = (TextView) rowView.findViewById(R.id.monthid);
            paid_value = data_model_arraylist.get(position).getPaid();
            due_value = data_model_arraylist.get(position).getDue();
            totalpaid_value = data_model_arraylist.get(position).getMonthlypayment();
            mode_value = data_model_arraylist.get(position).getMode();
            date_value = data_model_arraylist.get(position).getDate();
            monthname_value = data_model_arraylist.get(position).getMonth();
            room_id = data_model_arraylist.get(position).getRoom_id();
            owner_id = data_model_arraylist.get(position).getOwner_id();
            month_id_value = data_model_arraylist.get(position).getMonth_id();

            mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paymentcomment = data_model_arraylist.get(position).getPaymentcomment();
                    if (paymentcomment.equalsIgnoreCase("") || paymentcomment.equalsIgnoreCase(null)) {

                    } else {
                        AlertDailogPaymentComment.displayAlert(context, paymentcomment);
                    }

                }
            });


            if (monthname_value != null && !monthname_value.equals("")) {
                monthname.setText(monthname_value);
                paid.setText(paid_value);
                due.setText(due_value);
                totalpaid.setText(totalpaid_value);
                mode.setText(mode_value);
                date.setText(date_value);
                monthid.setText(month_id_value);
                Log.d("riya bigno", "" + monthno_data);
            }
        }
        return rowView;
    }
}
