package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;

/**
 * Created by user on 1/29/2016.
 */
public class StateSpinnerAdapter extends ArrayAdapter<StateModal> {

    private Activity activity;
    private ArrayList<StateModal> mArray = null;
    private LayoutInflater inflater;

    public StateSpinnerAdapter(Activity activitySpinner, int textViewResourceId, ArrayList<StateModal> objects) {
        super( activitySpinner, textViewResourceId, objects);
        activity = activitySpinner;
        mArray = objects;
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

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.textview_value);
        try {
            label.setText("" + mArray.get(position).getStrStateName());
        } catch(IndexOutOfBoundsException e) {
        }catch (Exception e){
        }
        return row;
    }

}
