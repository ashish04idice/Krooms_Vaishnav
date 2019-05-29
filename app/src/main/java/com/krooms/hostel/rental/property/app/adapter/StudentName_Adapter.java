package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/2016.
 */
public class StudentName_Adapter extends BaseAdapter {

    ArrayList<OwnerStudentNameModel> roomnostudentarraylist = new ArrayList<OwnerStudentNameModel>();
    private Activity context;

    public StudentName_Adapter(Activity context, ArrayList<OwnerStudentNameModel> roomnostudentarraylist) {
        this.context = context;
        this.roomnostudentarraylist = roomnostudentarraylist;
    }

    @Override
    public int getCount() {
        return roomnostudentarraylist.size();
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
        {
            inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.day_custom_dialog_items, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.day_name);
            String roomnovalue = roomnostudentarraylist.get(position).getTanentusername();
            txtTitle.setText(roomnovalue);

        }
        return rowView;
    }
}
