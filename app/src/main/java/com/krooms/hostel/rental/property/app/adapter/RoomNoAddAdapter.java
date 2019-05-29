package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.RoomNoMdelNew;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.modal.Tenantviewfeedbackmodel;

import java.util.ArrayList;

/**
 * Created by admin on 3/2/2017.
 */
public class RoomNoAddAdapter extends ArrayAdapter<RoomNoMdelNew> {

    private Activity activity;
    private ArrayList<RoomNoMdelNew> mArray = null;
    private LayoutInflater inflater;

    public RoomNoAddAdapter(Activity activitySpinner, int textViewResourceId, ArrayList<RoomNoMdelNew> objects) {
        super( activitySpinner, textViewResourceId, objects);
        activity = activitySpinner;
        mArray = objects;
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.textview_value);
        try {
            label.setText("" + mArray.get(position).getRoomnoqueantity());
        } catch(IndexOutOfBoundsException e) {
        }catch (Exception e){
        }

        return row;
    }



}
