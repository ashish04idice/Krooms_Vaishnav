package com.krooms.hostel.rental.property.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krooms.hostel.rental.property.app.R;


/**
 * Created by admin on 4/18/2017.
 */
public class Add_Envertory_Fragment extends Fragment {

    public Add_Envertory_Fragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_enventory_layout, container, false);

        return rootView;
    }

}
