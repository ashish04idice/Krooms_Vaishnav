package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.ListViewActivity;
import com.krooms.hostel.rental.property.app.activity.PaymentActivityOwnerListview;
import com.krooms.hostel.rental.property.app.activity.OldPayment_ShowMonthDetailOwner_RoomChange;
import com.krooms.hostel.rental.property.app.modal.Data_Model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 11/10/2016.
 */
public class OldPaymnet_ListAdapter_RoomChange extends BaseAdapter {
    String yearvalue = "";
    String monthname_value = "", totalpayment_value = "", paid_value = "", due_value = "", totalpaid_value = "", mode_value = "", date_value = "", monthly_payment = "";
    String room_id, owner_id, month_id_value, transactiondate;
    ArrayList<Data_Model> data_model_arraylist;
    private Activity context;
    String property_id_value, user_id_value;
    String transactionid = "";
    String month_date_value = "";

    public OldPaymnet_ListAdapter_RoomChange(Activity context, ArrayList<Data_Model> data_model_arraylist, String property_id_value, String user_id_value) {
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
        final RelativeLayout editmaode, mode_option;
        String title_value;
        final ImageView imagedata;
        TextView editmodeva;
        {
            rowView = inflater.inflate(R.layout.listviewitem, null, true);
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
            editmaode = (RelativeLayout) rowView.findViewById(R.id.editmaode);
            mode_option = (RelativeLayout) rowView.findViewById(R.id.mode_option);
            editmaode.setVisibility(View.GONE);
            transactiondate = data_model_arraylist.get(position).getTransactiondate();
            paid_value = data_model_arraylist.get(position).getPaid();
            due_value = data_model_arraylist.get(position).getDue();
            totalpaid_value = data_model_arraylist.get(position).getMonthlypayment();
            mode_value = data_model_arraylist.get(position).getMode();
            date_value = data_model_arraylist.get(position).getDate();
            monthname_value = data_model_arraylist.get(position).getMonth() + " - " + data_model_arraylist.get(position).getYear();
            room_id = data_model_arraylist.get(position).getRoom_id();
            owner_id = data_model_arraylist.get(position).getOwner_id();
            month_id_value = data_model_arraylist.get(position).getMonth_id();
            yearvalue = data_model_arraylist.get(position).getYear();
            Log.d("year valeu...", "" + yearvalue);
            transactionid = data_model_arraylist.get(position).getTransaction_id();
            month_date_value = data_model_arraylist.get(position).getMonth_date();
            title_value = data_model_arraylist.get(position).getTitle();

            if (monthname_value != null && !monthname_value.equals("")) {

                int idvalue = Integer.parseInt(data_model_arraylist.get(position).getMonth_id());
                if (idvalue == 13) {
                    monthname.setText(data_model_arraylist.get(position).getTitle());
                } else if (idvalue == 15) {
                    monthname.setText(data_model_arraylist.get(position).getTitle());
                } else {
                    monthname.setText(monthname_value);
                }

                // totalpayment.setText(totalpayment_value);
                paid.setText(paid_value);
                due.setText(due_value);
                totalpaid.setText(totalpaid_value);
                // mode.setText(mode_value);
                date.setText(transactiondate);
                monthid.setText(month_id_value);
                Log.d("riya bigno", "" + monthno_data);
            }
            editmaode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newmonthvalue = data_model_arraylist.get(position).getMonth().toString();
                    String newmonthid = monthid.getText().toString();
                    String dueamountvalue = data_model_arraylist.get(position).getDue().toString();
                    Intent intent = new Intent(context, PaymentActivityOwnerListview.class);
                    intent.putExtra("month", newmonthvalue);
                    intent.putExtra("tanentuserid", user_id_value);
                    intent.putExtra("tanentroomid", room_id);  //no need
                    intent.putExtra("monthid", data_model_arraylist.get(position).getMonth_id());
                    intent.putExtra("dueamountvalue", data_model_arraylist.get(position).getDue());
                    intent.putExtra("tranDate", date_value);
                    intent.putExtra("yearvalue", data_model_arraylist.get(position).getYear());
                    intent.putExtra("month_date", month_date_value);
                    intent.putExtra("tanantid", ListViewActivity.tanentuserid);
                    intent.putExtra("tanentpropertyid", property_id_value);
                    intent.putExtra("tanentownerid", owner_id); //no need
                    intent.putExtra("monthlypayment", data_model_arraylist.get(position).getMonthlypayment());
                    intent.putExtra("transactionid", transactionid); //no need
                    intent.putExtra("type", "0");
                    context.startActivity(intent);
                }
            });

            if (!mode.getText().toString().equals("")) {
                mode_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newmonthvalue = monthname.getText().toString();
                        String newmonthid = monthid.getText().toString();
                        Intent intent = new Intent(context, OldPayment_ShowMonthDetailOwner_RoomChange.class);
                        intent.putExtra("month", newmonthvalue);
                        intent.putExtra("tanentuserid", user_id_value);
                        intent.putExtra("tanentroomid", room_id);
                        intent.putExtra("monthid", newmonthid);
                        intent.putExtra("tanantid", ListViewActivity.tanentuserid);
                        intent.putExtra("tanentpropertyid", property_id_value);
                        intent.putExtra("tanentownerid", owner_id);
                        intent.putExtra("monthlypayment", totalpaid_value);
                        //new changes 20/3/18
                        intent.putExtra("yearvalue", data_model_arraylist.get(position).getYear());
                        intent.putExtra("type", "0");
                        context.startActivity(intent);
                    }
                });

            }
            //for electricity
            if (data_model_arraylist.get(position).getType().equalsIgnoreCase("1")) {

                transactiondate = data_model_arraylist.get(position).getTransactiondate();
                paid_value = data_model_arraylist.get(position).getPaid();
                due_value = data_model_arraylist.get(position).getDue();
                totalpaid_value = data_model_arraylist.get(position).getMonthlypayment();////unit difference
                mode_value = data_model_arraylist.get(position).getMode();
                date_value = data_model_arraylist.get(position).getDate();
                monthname_value = "Elc - " + data_model_arraylist.get(position).getMonth() + " - " + data_model_arraylist.get(position).getYear();
                month_id_value = data_model_arraylist.get(position).getMonth_id();
                yearvalue = data_model_arraylist.get(position).getYear();
                title_value = data_model_arraylist.get(position).getTitle();

                if (monthname_value != null && !monthname_value.equals("")) {

                    monthname.setText(monthname_value);
                    paid.setText(paid_value);
                    due.setText(due_value);
                    totalpaid.setText(totalpaid_value); //unit differenc
                    date.setText(date_value);
                    monthid.setText(month_id_value);

                }
                editmaode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newmonthvalue = data_model_arraylist.get(position).getMonth().toString();
                        Intent intent = new Intent(context, PaymentActivityOwnerListview.class);
                        intent.putExtra("month", newmonthvalue);
                        intent.putExtra("tanentuserid", user_id_value);
                        intent.putExtra("tanentroomid", room_id);
                        intent.putExtra("monthid", data_model_arraylist.get(position).getMonth_id());
                        intent.putExtra("dueamountvalue", data_model_arraylist.get(position).getDue());
                        intent.putExtra("tranDate", date_value);
                        Log.d("year value new", "" + data_model_arraylist.get(position).getYear());
                        intent.putExtra("yearvalue", data_model_arraylist.get(position).getYear());
                        //intent.putExtra("month_date",month_date_value);
                        Log.d("month value", yearvalue);
                        intent.putExtra("tanantid", ListViewActivity.tanentuserid);
                        intent.putExtra("tanentpropertyid", property_id_value);
                        intent.putExtra("monthlypayment", data_model_arraylist.get(position).getMonthlypayment());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    }
                });

                if (!mode.getText().toString().equals("")) {
                    mode_option.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newmonthvalue = monthname.getText().toString();
                            String newmonthid = monthid.getText().toString();
                            Intent intent = new Intent(context, OldPayment_ShowMonthDetailOwner_RoomChange.class);
                            intent.putExtra("month", newmonthvalue);
                            intent.putExtra("tanentuserid", user_id_value);
                            intent.putExtra("monthid", newmonthid);
                            intent.putExtra("tanantid", ListViewActivity.tanentuserid);
                            intent.putExtra("tanentpropertyid", property_id_value);
                            intent.putExtra("monthlypayment", totalpaid_value);
                            intent.putExtra("yearvalue", data_model_arraylist.get(position).getYear());
                            intent.putExtra("type", "1");
                            context.startActivity(intent);
                        }
                    });

                }

            }//
        }
        return rowView;
    }
}
