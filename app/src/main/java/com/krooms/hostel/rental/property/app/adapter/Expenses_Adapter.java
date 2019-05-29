package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.ExpensesModel;

import java.util.ArrayList;


/**
 * Created by ashishidice on 22/4/17.
 */

public class Expenses_Adapter extends BaseAdapter {

    Context context;
    ArrayList<ExpensesModel> list;

    public Expenses_Adapter(Context context, ArrayList<ExpensesModel> list) {
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
        View vi = inflater.inflate(R.layout.expenses_adapter_item, null, true);
        TextView txtdate = (TextView) vi.findViewById(R.id.txtdate);
        TextView txtname = (TextView) vi.findViewById(R.id.txtname);
        TextView txtamount = (TextView) vi.findViewById(R.id.txtamount);
        TextView txtremark = (TextView) vi.findViewById(R.id.txtremark);
        try {
            txtname.setText(list.get(position).getName());
            txtamount.setText(list.get(position).getPrice());
            txtremark.setText(list.get(position).getRemark());
            txtdate.setText(list.get(position).getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vi;
    }
}
