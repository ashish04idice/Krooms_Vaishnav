package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.krooms.hostel.rental.property.app.dialog.ImagePreviewPagerDialog;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import java.util.ArrayList;

/**
 * Created by user on 2/23/2016.
 */
public class PropertyDetatilViewpagerAdapter extends PagerAdapter/* implements YouTubePlayer.OnInitializedListener*/ {

    private Context mContext;
    private FragmentActivity mActivity;
    private LayoutInflater mLayoutInflater;
    public static final String API_KEY = "AIzaSyArdXbSRPiGcSmLTh750_fcPSTGyKEQCkk";
    public static final String VIDEO_ID = "zXgUgvkVNdQ";
    private ArrayList<PropertyPhotoModal> mList = new ArrayList<PropertyPhotoModal>();

    public PropertyDetatilViewpagerAdapter(FragmentActivity context, ArrayList<PropertyPhotoModal> list) {
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
        itemView = mLayoutInflater.inflate(R.layout.property_photo_list_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.property_image);

        if (!mList.get(position).getPhoto_Url().equals("Not Available")) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Common.loadImage(mActivity, WebUrls.IMG_URL + "" + mList.get(position).getPhoto_Url(), imageView);

        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.drawable.ic_default_background);
        }

        container.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePreviewPagerDialog pagerDialog = new ImagePreviewPagerDialog();
                pagerDialog.getPerameter(mActivity, mList, position);
                pagerDialog.show(mActivity.getSupportFragmentManager(), "");

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
