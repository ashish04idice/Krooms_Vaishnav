package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.krooms.hostel.rental.property.app.R;


/**
 * Created by Aarna Shree on 8/12/2016.
 */
public class Ads_CustomPagerAdapter extends PagerAdapter {
    private int[] mResources = {
            R.drawable.k_rooms,
            R.drawable.logo,
            R.drawable.k_logo,
            R.drawable.k_rooms,
            R.drawable.attendance_machine
    };
    Context mContext;
    LayoutInflater mLayoutInflater;

    public Ads_CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        imageView.setBackgroundResource(mResources[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}