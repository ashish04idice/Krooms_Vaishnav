package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.PaymentPaidUnpaidModel;
import com.krooms.hostel.rental.property.app.modal.TenantRecordmodel;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by ashishidice on 22/4/17.
 */

public class Payment_Paid_Unpaid_Adapter extends BaseAdapter {

    Context context;
    ArrayList<PaymentPaidUnpaidModel> list;

    public Payment_Paid_Unpaid_Adapter(Context context, ArrayList<PaymentPaidUnpaidModel> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.payment_paid_unpaid_item, null, true);
        TextView txtroomno = (TextView) vi.findViewById(R.id.txtroomno);
        TextView txtname = (TextView) vi.findViewById(R.id.txtname);
        TextView txtamount = (TextView) vi.findViewById(R.id.txtamount);
        TextView txtpay = (TextView) vi.findViewById(R.id.txtpay);
        TextView txtdate = (TextView) vi.findViewById(R.id.txtdate);
        try {
            txtroomno.setText(list.get(position).getRoom_no());
            txtname.setText(list.get(position).getTenant_name());
            txtamount.setText(list.get(position).getTotalamount());
            txtpay.setText(list.get(position).getPaid());
            txtdate.setText(list.get(position).getPaydate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }
}
