package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.List;

/**
 * Created by user on 2/1/2016.
 */
public class PropertyPhotoListAdapter  extends BaseAdapter {


    Context context;
    Activity activity;
    List<PropertyPhotoModal> list;

    public PropertyPhotoListAdapter(Activity context, List<PropertyPhotoModal> objects) {

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
        if(null == convertView) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.property_photo_list_item,null);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.property_image);
        if(position%2 == 0){
            img.setBackgroundResource(R.drawable.ic_default_background);
        }else{

            img.setBackgroundResource(R.drawable.ic_default_background);
        }

        return convertView;
    }

}
