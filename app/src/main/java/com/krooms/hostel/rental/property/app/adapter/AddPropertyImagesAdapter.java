package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/1/2016.
 */
public abstract class AddPropertyImagesAdapter extends BaseAdapter {

    private Context context;
    private Activity activity;
    private List<PropertyPhotoModal> list = new ArrayList<>();
    private ViewHolder holder;
    private LayoutInflater inflater;

    public AddPropertyImagesAdapter(Activity context, List<PropertyPhotoModal> objects) {

        this.context = context;
        this.activity = context;
        this.list = objects;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {

        if (list.size() == 1) {
            if (list.get(0).getPhoto_Url().equals("Not%20Available")) {
                list.clear();
            }
        }
        if (list.size() < 10) {

            return list.size() + 1;
        } else {

            return list.size();
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.add_property_images, null);
            holder.img = (ImageView) convertView.findViewById(R.id.addPropertyImage);
            holder.imgEdit = (ImageButton) convertView.findViewById(R.id.image_edit_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {

            holder.imgEdit.setVisibility(View.VISIBLE);
            String image_path;

            if (list.get(position).getPhoto_from().equals("server")) {
                image_path = WebUrls.IMG_URL + "" + list.get(position).getPhoto_Url();
            } else {
                image_path = "file://" + list.get(position).getPhoto_Url();
            }
            Picasso.with(activity)
                    .load(image_path)
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .resize(300, 300)
                    .into(holder.img);

        } catch (IndexOutOfBoundsException e) {
            holder.img.setImageResource(R.drawable.ic_add_icon);
            holder.imgEdit.setVisibility(View.GONE);
        }

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePropertyImage(position);
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPropertyImage(position);
            }
        });

        return convertView;

    }

    public abstract void updatePropertyImage(int position);

    public abstract void viewPropertyImage(int position);

    class ViewHolder {
        ImageView img;
        ImageButton imgEdit;
    }

}
