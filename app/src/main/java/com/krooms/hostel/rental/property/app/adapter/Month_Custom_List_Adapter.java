package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;

/**
 * this is created by month list adapter
 */

public class Month_Custom_List_Adapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> month_data;

    public Month_Custom_List_Adapter(Activity context, ArrayList month_data) {
        super(context, R.layout.day_custom_dialog_items, month_data);
        this.context = context;
        this.month_data = month_data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.day_custom_dialog_items, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.day_name);
        txtTitle.setText(month_data.get(position));
        return rowView;
    }
}