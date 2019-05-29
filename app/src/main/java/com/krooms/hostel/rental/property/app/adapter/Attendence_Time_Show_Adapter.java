package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;

import java.util.ArrayList;

/**
 * Created by anuradhaidice on 24/12/16.
 */

public class Attendence_Time_Show_Adapter extends BaseAdapter {
    String Timevalue;
    ArrayList<Attendence_Modal> time_show_arraylist;
    private Activity context;
    TextView timetextview;
    View rowView = null;

    public Attendence_Time_Show_Adapter(Activity context, ArrayList<Attendence_Modal> time_show_arraylist) {
        this.context = context;
        this.time_show_arraylist = time_show_arraylist;
    }

    @Override
    public int getCount() {
        return time_show_arraylist.size();
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
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater;
        String main_image;
        inflater = context.getLayoutInflater();
        {
            rowView = inflater.inflate(R.layout.timelistview_item, null, true);
            timetextview = (TextView) rowView.findViewById(R.id.timevalue);
            Timevalue = time_show_arraylist.get(position).getTime();
            timetextview.setText(Timevalue);
        }
        return rowView;
    }
}
