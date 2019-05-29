package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Propertynamemodel;

import java.util.ArrayList;

/**
 * Created by admin on 3/28/2017.
 */
public class SelectPropertymultiple_Adapter extends BaseAdapter {
    View rowView = null;
    ArrayList<Propertynamemodel> ownerlist=new ArrayList<>();
    private Activity context;

    public SelectPropertymultiple_Adapter(Activity context, ArrayList<Propertynamemodel> ownerlist) {
        this.context = context;
        this.ownerlist=ownerlist;
    }

    @Override
    public int getCount() {
        return ownerlist.size();
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
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater;
        String main_image;
        inflater = context.getLayoutInflater();
        {
            rowView = inflater.inflate(R.layout.day_custom_dialog_items, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.day_name);
            TextView propertyidview=(TextView)rowView.findViewById(R.id.propertyidview);
            TextView propertyidname=(TextView)rowView.findViewById(R.id.propertyname);
            propertyidname.setText(ownerlist.get(position).getPropertyname().toString());
            txtTitle.setText(ownerlist.get(position).getPropertyname().toString());
            propertyidview.setText(ownerlist.get(position).getPropertuid().toString());
        }
        return rowView;
    }
}
