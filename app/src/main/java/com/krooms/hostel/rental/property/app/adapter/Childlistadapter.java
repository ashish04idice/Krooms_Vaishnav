package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Childlistmodel;

import java.util.ArrayList;

/**
 * Created by ashish on 31/1/17.
 */

public class Childlistadapter extends BaseAdapter {

    Context context;
    Holder holder;
    ArrayList<Childlistmodel> childlistarray;

    public Childlistadapter(Context context, ArrayList<Childlistmodel> childlistarray) {

        this.context = context;
        this.childlistarray = childlistarray;

    }

    @Override
    public int getCount() {
        return childlistarray.size();
    }

    @Override
    public Object getItem(int position) {
        return childlistarray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)

    {
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = LayoutInflater.from(context.getApplicationContext());
            convertView = layoutInflater.inflate(R.layout.childlistview_item, null);
            holder.tenantname = (TextView) convertView.findViewById(R.id.itemchildname);
            holder.hostalname = (TextView) convertView.findViewById(R.id.itemhostalname);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.itemcheckbox);
            holder.tenantname.setText(childlistarray.get(position).getTenant_name());
            holder.hostalname.setText(childlistarray.get(position).getHostal_name());
            convertView.setTag(holder);
        } else {

            holder = (Holder) convertView.getTag();

        }
        return convertView;
    }

    class Holder {
        TextView tenantname, hostalname;
        CheckBox checkBox;
    }

}
