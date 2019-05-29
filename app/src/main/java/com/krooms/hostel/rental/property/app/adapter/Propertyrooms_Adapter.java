package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.activity.HostelListActivity;
import com.krooms.hostel.rental.property.app.activity.PropertyRoomsCreationActivity;
import com.krooms.hostel.rental.property.app.modal.Roommodel;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/2016.
 */
public class Propertyrooms_Adapter extends BaseAdapter {

    ArrayList<Roommodel> roomnostudentarraylist=new ArrayList<Roommodel>();
    Activity context;
    String  propertyid;

    public Propertyrooms_Adapter(PropertyRoomsCreationActivity context, ArrayList<Roommodel> roomnostudentarraylist,String propertyidvalue) {
        this.context = context;
        this.roomnostudentarraylist=roomnostudentarraylist;
        propertyid=propertyidvalue;
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
        final TextView monthname,totalpayment,paid,monthid;
        inflater = context.getLayoutInflater();
        View rowView = null;
         {
             inflater = context.getLayoutInflater();
             rowView= inflater.inflate(R.layout.roomgeneratedifferentlayout, null, true);
             TextView txtTitle = (TextView) rowView.findViewById(R.id.day_name);
             TextView txtroomimg=(TextView) rowView.findViewById(R.id.room_img);
             String roomnovalue=roomnostudentarraylist.get(position).getRoomnoslotvalue();
             txtTitle.setText(roomnovalue);

             txtroomimg.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                 }
             });
        }
        return rowView;
    }
}
