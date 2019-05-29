package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;

import java.util.ArrayList;

/**
 * Created by anuradhaidice on 24/12/16.
 */

public class Attendence_Show_Owner_End_Adapter extends BaseAdapter {
    String roomnoview_value, tenantnameview_value, absentview_value;
    ArrayList<Attendence_Modal> show_arraylist;
    private Activity context;
    TextView tenantnameview, roomnoview, emailid_tenant;
    View rowView = null;
    ImageView imageview_data;
    String email_id;
    String timeview_value, resonevalue = "";

    public Attendence_Show_Owner_End_Adapter(Activity context, ArrayList<Attendence_Modal> show_arraylist) {
        this.context = context;
        this.show_arraylist = show_arraylist;
    }

    @Override
    public int getCount() {
        return show_arraylist.size();
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
            rowView = inflater.inflate(R.layout.attendence_show_item_owner, null, true);
            final TextView roomnoview = (TextView) rowView.findViewById(R.id.roomnoview);
            final TextView tenantnameview = (TextView) rowView.findViewById(R.id.tenantnameview);
            final TextView presentabsentview = (TextView) rowView.findViewById(R.id.presentabsentview);
            final TextView timeview = (TextView) rowView.findViewById(R.id.timeview);
            final TextView resoneview = (TextView) rowView.findViewById(R.id.resoneview);
            roomnoview_value = show_arraylist.get(position).getAttendenceroom_no();
            tenantnameview_value = show_arraylist.get(position).getAttendencetenantname();
            absentview_value = show_arraylist.get(position).getAbsent_present();
            timeview_value = show_arraylist.get(position).getTime();
            resonevalue = show_arraylist.get(position).getAttendecstatus();
            resoneview.setText(resonevalue);
            roomnoview.setText(roomnoview_value);
            timeview.setText(timeview_value);
            tenantnameview.setText(tenantnameview_value);
            presentabsentview.setText(absentview_value);
        }
        return rowView;
    }
}
