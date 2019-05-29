package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Foodmodel;

import java.util.ArrayList;


/**
 * Created by ashishidice on 22/4/17.
 */

public class Food_Attendance_RecordsAdapter extends BaseAdapter {

    Activity context;
    ArrayList<Foodmodel> list;

    public Food_Attendance_RecordsAdapter(Activity context, ArrayList<Foodmodel> list) {

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
        View vi = inflater.inflate(R.layout.foodlistview_item_layout, null, true);
        TextView txtroomno = (TextView) vi.findViewById(R.id.txtroomno);

        TextView txtname = (TextView) vi.findViewById(R.id.txtname);

        ImageView imgtime1 = (ImageView) vi.findViewById(R.id.txt1);
        ImageView imgtime2 = (ImageView) vi.findViewById(R.id.txt2);
        ImageView imgtime3 = (ImageView) vi.findViewById(R.id.txt3);
        ImageView imgtime4 = (ImageView) vi.findViewById(R.id.txt4);
        ImageView imgtime5 = (ImageView) vi.findViewById(R.id.txt5);
        txtroomno.setText(list.get(position).getRoomno());
        txtname.setText(list.get(position).getTname());
        String statusvaluedata = list.get(position).getAttendencestatus();
        String statustarray[] = statusvaluedata.split(",");
        for (int i = 0; i < statustarray.length; i++) {
            String statusvalue = statustarray[i];
            if (statusvalue.equalsIgnoreCase("Breakfast")) {
                imgtime1.setImageResource(R.drawable.green_present);
            } else if (statusvalue.equalsIgnoreCase("Lunch")) {
                imgtime2.setImageResource(R.drawable.green_present);
            } else if (statusvalue.equalsIgnoreCase("Tea Time")) {
                imgtime3.setImageResource(R.drawable.green_present);
            } else if (statusvalue.equals("Dinner")) {
                imgtime4.setImageResource(R.drawable.green_present);
            }
        }
        return vi;
    }
}
