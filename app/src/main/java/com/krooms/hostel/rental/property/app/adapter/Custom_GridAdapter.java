package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;

/**
 * Created by Hi on 3/20/2017.
 */
public class Custom_GridAdapter extends ArrayAdapter {

    Context context;
    String[] web;
    int[] imageId;

    public Custom_GridAdapter(Context context, String[] web, int[] imageId) {
        super(context, R.layout.grid_home_item_layout, web);
        this.context = context;
        this.imageId = imageId;
        this.web = web;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.grid_home_item_layout, null, true);
        TextView textView = (TextView) vi.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView) vi.findViewById(R.id.grid_image);
        textView.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        return vi;
    }
}
