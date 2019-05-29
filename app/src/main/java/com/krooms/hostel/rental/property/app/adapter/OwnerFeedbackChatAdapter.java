package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.OwnerChatmodel;

import java.util.ArrayList;

/**
 * Created by Raghveer on 09-02-2017.
 */
public class OwnerFeedbackChatAdapter extends BaseAdapter {

    ArrayList<OwnerChatmodel> mainarraylist;
    private Activity context;
    public OwnerFeedbackChatAdapter(Activity context, ArrayList<OwnerChatmodel> mainarraylist) {
        this.context = context;
        this.mainarraylist=mainarraylist;
    }
    @Override
    public int getCount() {
        return mainarraylist.size();
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
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater;
        inflater = context.getLayoutInflater();
        TextView mainname,name;

        View rowView ;

        if(mainarraylist.get(position).getMessage().equals("0"))
        {
            rowView=inflater.inflate(R.layout.item_odd_listview_feedback, null, true);
            mainname=(TextView)rowView.findViewById(R.id.mainnamefodd);
            name=(TextView)rowView.findViewById(R.id.namefodd);
            mainname.setVisibility(View.GONE);
            name.setText(mainarraylist.get(position).getDescription());
            mainname.setText(mainarraylist.get(position).getName());

        }else if(mainarraylist.get(position).getMessage().equals("2")){

            rowView=inflater.inflate(R.layout.item_even_listview, null, true);
            mainname=(TextView)rowView.findViewById(R.id.mainname);
            name=(TextView)rowView.findViewById(R.id.name);
            name.setText(mainarraylist.get(position).getDescription());
            mainname.setText("Parent-"+mainarraylist.get(position).getName());
        }


        else if(mainarraylist.get(position).getMessage().equals("3")){
            rowView=inflater.inflate(R.layout.item_even_listview, null, true);
            mainname=(TextView)rowView.findViewById(R.id.mainname);
            name=(TextView)rowView.findViewById(R.id.name);
            name.setText(mainarraylist.get(position).getDescription());
            mainname.setText("Warden-"+mainarraylist.get(position).getSender_name());
        }
        else
        {
            rowView=inflater.inflate(R.layout.item_even_listview_feedback, null, true);
            mainname=(TextView)rowView.findViewById(R.id.mainnamefeven);
            name=(TextView)rowView.findViewById(R.id.namefeven);
            name.setText(mainarraylist.get(position).getDescription());
            mainname.setText(mainarraylist.get(position).getName());
        }


        return rowView;
    }
}






