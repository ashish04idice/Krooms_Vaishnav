package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;

/**
 * Created by admin on 11/15/2016.
 */
public class OwnerStudentNameTanentAdapter extends BaseAdapter {

    ArrayList<OwnerStudentNameModel> ownerstudent_arraylist;
    OwnerStudentNameModel ownerstudentmodel;
    private Activity context;
    public OwnerStudentNameTanentAdapter(Activity context,ArrayList<OwnerStudentNameModel> ownerstudent_arraylist) {
        this.context = context;
        this.ownerstudent_arraylist=ownerstudent_arraylist;
    }

    @Override
    public int getCount() {
        return ownerstudent_arraylist.size();
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
        LayoutInflater inflater;
        inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.day_custom_dialog_items, null, true);

        TextView country_data = (TextView)rowView.findViewById(R.id.day_name);
        String countryname =ownerstudent_arraylist.get(position).getTanentusername();
        String countryid = ownerstudent_arraylist.get(position).getTanentuserid();
        if (countryname!= null && !countryid.equals("")) {
            country_data.setText(countryname);
        }
        return rowView;
    }

}

