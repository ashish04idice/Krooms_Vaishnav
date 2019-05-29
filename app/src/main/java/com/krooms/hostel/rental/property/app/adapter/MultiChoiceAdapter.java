package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import java.util.ArrayList;

/**
 * Created by user on 1/29/2016.
 */
public abstract class MultiChoiceAdapter extends ArrayAdapter<StateModal> {

    private Activity activity;
    private ArrayList<StateModal> mArray = null;
    private LayoutInflater inflater;

    public MultiChoiceAdapter(Activity activitySpinner, int textViewResourceId, ArrayList<StateModal> objects) {
        super(activitySpinner, textViewResourceId, objects);
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

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.multi_choice_item, parent, false);
        TextView label = (TextView) convertView.findViewById(R.id.textview_value);
        label.setText("" + mArray.get(position).getStrStateName());

        final CheckBox chkChoice = (CheckBox) convertView.findViewById(R.id.checkBox_add);


        if (mArray.get(position).getChecked() == 1) {
            chkChoice.setChecked(true);
        } else {
            chkChoice.setChecked(false);
        }

        chkChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mArray.get(position).getChecked() == 1) {
                    if (position == 0) {
                        for (int i = 0; i < mArray.size(); i++) {
                            mArray.get(i).setChecked(0);
                        }

                    } else {
                        mArray.get(position).setChecked(0);
                        if (mArray.get(0).getChecked() == 1) {
                            mArray.get(0).setChecked(0);
                        }
                    }
                } else {
                    Common.Config(" unChecked............  ");
                    if (position == 0) {
                        for (int i = 0; i < mArray.size(); i++) {
                            mArray.get(i).setChecked(1);
                        }

                    } else {
                        mArray.get(position).setChecked(1);
                        for (int i = 1; i < mArray.size(); i++) {
                            if (mArray.get(i).getChecked() == 0) {
                                return;
                            }
                            if (mArray.get(0).getChecked() == 0) {
                                mArray.get(0).setChecked(1);
                            }

                        }
                    }
                }
                notifyDataSetChanged();

                callBack(mArray);
            }
        });

        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mArray.get(position).getChecked() == 1) {
                    chkChoice.setChecked(false);
                    mArray.get(position).setChecked(0);

                } else {
                    chkChoice.setChecked(true);
                    mArray.get(position).setChecked(1);
                }
                callBack(mArray);
            }
        });

        return convertView;
    }

    public abstract void callBack(ArrayList<StateModal> modalArrayList);

}
