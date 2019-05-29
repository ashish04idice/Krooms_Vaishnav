package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import java.util.List;

/**
 * Created by user on 2/1/2016.
 */
public class HolstelListAdapter extends BaseAdapter {


    Context context;
    Activity activity;
    List<PropertyModal> list;
    ViewHolder holder;
    LayoutInflater inflater;

    public HolstelListAdapter(Activity context, List<PropertyModal> objects) {
        this.context = context;
        this.activity = context;
        list = objects;
        inflater = activity.getLayoutInflater();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.hostel_list_item, null);
            holder.hostelThumb = (ImageView) convertView.findViewById(R.id.property_icon_id);

            holder.hostelName = (TextView) convertView.findViewById(R.id.property_name_id);


            holder.hostelAddress = (TextView) convertView.findViewById(R.id.property_address_id);
            holder.rentProperty = (TextView) convertView.findViewById(R.id.property_price_id);
            holder.propertyType = (TextView) convertView.findViewById(R.id.property_type_id);
            holder.resultTypeTitle = (TextView) convertView.findViewById(R.id.resultTypeTitle);
            holder.propertyLocation = (ImageButton) convertView.findViewById(R.id.propertyLocation);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.hostelThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.hostelName.setText(list.get(position).getProperty_name());

        DataBaseAdapter mDBAdapter = new DataBaseAdapter(activity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        String area = "";

        if (list.get(position).getProperty_area().equals("")) {
            area = "Area";
        } else {
            String areanamevalue = list.get(position).getColonyname();
            {
                area = areanamevalue;
            }
        }

        holder.hostelAddress.setText(area);
        mDBAdapter.close();
        holder.rentProperty.setText("Rent: " + list.get(position).getProperty_rent_price());

        if (list.get(position).getProperty_type_id().equals("1")) {
            holder.propertyType.setText("Hostel");
        } else if (list.get(position).getProperty_type_id().equals("2")) {
            holder.propertyType.setText("PG");
        } else if (list.get(position).getProperty_type_id().equals("3")) {
            holder.propertyType.setText("Service Apartment");
        } else if (list.get(position).getProperty_type_id().equals("4")) {
            holder.propertyType.setText("Room");
        } else if (list.get(position).getProperty_type_id().equals("5")) {
            holder.propertyType.setText("Studio Apartment");
        } else {
            holder.propertyType.setText("Other");
        }

        if (list.get(position).getSearchResult_type() == 1) {
            if (!Common.searchResultTypeTitleShown) {
                Common.searchResultTypeTitleShown = true;
                holder.resultTypeTitle.setVisibility(View.VISIBLE);

            }
        }

        holder.propertyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //by me
        Common.loadImage(activity, list.get(position).getPropertyImage().get(0).getPhoto_Url(), holder.hostelThumb);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, HostelDetailActivity.class);
                it.putExtra("selected_index", position);
                it.putExtra("property_id", list.get(position).getProperty_id());
                it.putExtra("list_type", "list_type_searchResult");
                activity.startActivity(it);
            }
        });

        return convertView;
    }

    class ViewHolder {
        public ImageView hostelThumb;
        private TextView hostelName;
        private TextView hostelAddress;
        private TextView rentProperty;
        private TextView propertyType;
        private TextView resultTypeTitle;
        private ImageButton propertyLocation;
    }
}