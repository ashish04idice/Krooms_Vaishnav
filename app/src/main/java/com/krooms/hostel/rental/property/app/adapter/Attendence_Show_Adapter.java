package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.util.UniversalImageLoaderNew;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import java.util.ArrayList;

/**
 * Created by anuradhaidice on 24/12/16.
 */

public class Attendence_Show_Adapter extends BaseAdapter {
    String dateview_value, timeview_value, absentview_value;
    ArrayList<Attendence_Modal> show_arraylist;
    private Activity context;
    TextView tenantnameview, roomnoview, emailid_tenant;
    View rowView = null;
    ImageView imageview_data;
    String email_id;
    String resonevalue = "";

    public Attendence_Show_Adapter(Activity context, ArrayList<Attendence_Modal> show_arraylist, TextView tenantnameview, TextView roomnoview, ImageView imageview_data, TextView emailid_tenant) {
        this.context = context;
        this.show_arraylist = show_arraylist;
        this.tenantnameview = tenantnameview;
        this.roomnoview = roomnoview;
        this.imageview_data = imageview_data;
        this.emailid_tenant = emailid_tenant;
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
            rowView = inflater.inflate(R.layout.show_attendence_listview_item, null, true);
            final TextView dateview = (TextView) rowView.findViewById(R.id.dateview);
            final TextView timeview = (TextView) rowView.findViewById(R.id.timeview);
            final TextView presentabsentview = (TextView) rowView.findViewById(R.id.presentabsentview);
            final TextView resoneview = (TextView) rowView.findViewById(R.id.resoneview);
            tenantnameview.setText(show_arraylist.get(position).getAttendencetenantname().toString());
            roomnoview.setText(show_arraylist.get(position).getAttendenceroom_no().toString());
            dateview_value = show_arraylist.get(position).getDate();
            timeview_value = show_arraylist.get(position).getTime();
            absentview_value = show_arraylist.get(position).getAbsent_present();
            resonevalue = show_arraylist.get(position).getAttendecstatus();
            resoneview.setText(resonevalue);
            main_image = show_arraylist.get(position).getTenant_photo();
            String main_image_data_url = WebUrls.IMG_URL + main_image;
            UniversalImageLoaderNew.initUniversalImageLoaderOptions();
            if (main_image != null && !main_image.equals("")) {
                UniversalImageLoaderNew.initUniversalImageLoaderOptionsForRoundImage();
                UniversalImageLoaderNew.loadImageFromURI(imageview_data, main_image_data_url, null);
            }
            dateview.setText(dateview_value);
            timeview.setText(timeview_value);
            presentabsentview.setText(absentview_value);

        }
        return rowView;
    }
}
