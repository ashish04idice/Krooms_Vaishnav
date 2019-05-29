package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.krooms.hostel.rental.property.app.modal.FeatureListModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/5/2016.
 */
public class AminitiesAndFeaturesPagerAdapter extends PagerAdapter {

    Activity activity;

    List<FeatureListModal> list;
    LayoutInflater mLayoutInflater;

    public AminitiesAndFeaturesPagerAdapter(Activity activity, List<FeatureListModal> objects) {
        this.activity = activity;
        list = objects;
        mLayoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        int div = list.size() % 3;


        if (div != 0) {
            return (list.size() / 3) + 1;
        } else {
            return list.size() / 3;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.aminities_grid_item, container, false);
        GridView featureList = (GridView) itemView.findViewById(R.id.aminitiesGrid);

        container.addView(itemView);
        List<FeatureListModal> innerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            if ((i + (position * 3) < list.size())) {
                innerList.add(list.get((i + (position * 3))));
            }
        }
        featureList.setAdapter(new FeatureListAdapter(activity, innerList));
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
