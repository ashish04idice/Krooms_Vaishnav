package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;

import java.util.ArrayList;

/**
 * Created by user on 2/24/2016.
 */
public class InformationPagerAdapter extends PagerAdapter {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PropertyModal> mList = new ArrayList<PropertyModal>();

    public InformationPagerAdapter(Activity context/*,ArrayList<PropertyModal> list*/) {
        mContext = context;
        mActivity = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*mList = list;*/
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.app_info_pager, container, false);

        ImageView pagerImage = (ImageView) itemView.findViewById(R.id.pagerImage);

        if (position % 2 == 0) {
            pagerImage.setBackgroundResource(R.drawable.ic_buiding_icon);
        } else {
            pagerImage.setBackgroundResource(R.drawable.logo_in_orange);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
