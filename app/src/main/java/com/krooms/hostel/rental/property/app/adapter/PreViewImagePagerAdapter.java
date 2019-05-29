package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.custom.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 3/25/2016.
 */
public class PreViewImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PropertyPhotoModal> mList = new ArrayList<PropertyPhotoModal>();

    public PreViewImagePagerAdapter(Activity context, ArrayList<PropertyPhotoModal> list) {
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

        View itemView;
        itemView = mLayoutInflater.inflate(R.layout.preview_imagepager_item, container, false);
        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.property_image);


        if (!mList.get(position).getPhoto_Url().equals("Not Available")) {
            try {
                Picasso.with(mActivity)
                        .load(WebUrls.IMG_URL + "" + mList.get(position).getPhoto_Url())
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .into(imageView);
            } catch (Exception e) {
            }
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.drawable.ic_default_background);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
