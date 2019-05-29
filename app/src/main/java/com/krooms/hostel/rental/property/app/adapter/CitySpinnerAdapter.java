package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.modal.CityModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;

/**
 * Created by user on 2/16/2016.
 */

public class CitySpinnerAdapter extends ArrayAdapter<CityModal> {

    private Activity activity;
    private ArrayList<CityModal> mArray = null;
    private LayoutInflater inflater;

    /*************
     * CustomAdapter Constructor
     *****************/
    public CitySpinnerAdapter(Activity activitySpinner, int textViewResourceId, ArrayList<CityModal> objects) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
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
        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.textview_value);
        label.setText("" + mArray.get(position).getStrCityName());
        return row;
    }
}
