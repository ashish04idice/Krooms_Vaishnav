package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.modal.FeatureListModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.List;

/**
 * Created by user on 2/17/2016.
 */
public class FeatureListAdapter extends BaseAdapter {
    Context context;
    Activity activity;
    List<FeatureListModal> list;

    public FeatureListAdapter(Activity context, List<FeatureListModal> objects) {

        this.context = context;
        this.activity = context;
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.feature_list_item, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.featuresText);
        ImageView Icon = (ImageView) convertView.findViewById(R.id.featuresIcon);
        title.setText(list.get(position).getFeatureTitle());
        if (list.get(position).getFeatureIcon() == 0) {
            Icon.setBackgroundResource(R.drawable.feature);
        } else {
            Icon.setBackgroundResource(list.get(position).getFeatureIcon());
        }
        return convertView;
    }
}
