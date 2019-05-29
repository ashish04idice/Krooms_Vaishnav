package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;

import java.util.ArrayList;

/**
 * Created by user on 2/19/2016.
 */
public class HeaderCustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PropertyModal> mList = new ArrayList<PropertyModal>();
    ViewHolder holder;
    View convertView;

    public HeaderCustomPagerAdapter(Activity context, ArrayList<PropertyModal> list) {
        mContext = context;
        mActivity = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        holder = null;
        convertView = mLayoutInflater.inflate(R.layout.header, container, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.property_icon_id);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TextView propertyName = (TextView) convertView.findViewById(R.id.property_name_id);
        TextView propertyAddress = (TextView) convertView.findViewById(R.id.property_address_id);
        TextView propertyType = (TextView) convertView.findViewById(R.id.property_type_id);
        TextView rentProperty = (TextView) convertView.findViewById(R.id.property_price_id);
        ImageButton propertyLocation = (ImageButton) convertView.findViewById(R.id.propertyLocation);
        propertyName.setText(mList.get(position).getProperty_name());
        DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        String area = "";

        if (mList.get(position).getProperty_area().equals("")) {
            area = "Area";
        } else {
            Cursor cursor = mDBAdapter.getSqlqureies_new("Select *from area where area_id=" + mList.get(position).getProperty_area());

            if (cursor.getCount() > 0) {
                area = cursor.getString(cursor.getColumnIndex("area_name"));
            } else {
                area = "Area";
            }
        }

        propertyAddress.setText(area);
        mDBAdapter.close();
        /*Common.callImageLoaderSmall(mActivity,"http://www.planwallpaper.com/static/images/Winter-Tiger-Wild-Cat-Images.jpg",imageView,options);*/
        rentProperty.setText("Rent: " + mList.get(position).getProperty_rent_price());
        if (mList.get(position).getProperty_type_id().equals("1")) {
            propertyType.setText("Hostel");
        } else if (mList.get(position).getProperty_type_id().equals("2")) {
            propertyType.setText("PG");
        } else if (mList.get(position).getProperty_type_id().equals("3")) {
            propertyType.setText("Service Apartment");
        } else if (mList.get(position).getProperty_type_id().equals("4")) {
            propertyType.setText("Room");
        } else if (mList.get(position).getProperty_type_id().equals("5")) {
            propertyType.setText("Studio Apartment");
        }

        propertyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if (mList.get(position).getPropertyImage().size() != 0) {

            if (!mList.get(position).getPropertyImage().get(0).getPhoto_Url().equals("Not Available")) {
                Common.loadImage(mActivity, mList.get(position).getPropertyImage().get(0).getPhoto_Url(), imageView);
            } else {

                imageView.setImageResource(R.drawable.ic_default_background);
            }
        }

        container.addView(convertView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Common.CommonPropertList.size(); i++) {
                    if (mList.get(position).getProperty_id().equals(Common.CommonPropertList.get(i).getProperty_id())) {
                        Intent it = new Intent(mActivity, HostelDetailActivity.class);
                        it.putExtra("selected_index", i);
                        it.putExtra("property_id", mList.get(position).getProperty_id());
                        mActivity.startActivity(it);
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    class ViewHolder {
    }

}
