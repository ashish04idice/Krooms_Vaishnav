package com.krooms.hostel.rental.property.app.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;


/**
 * Created by user on 2/5/2016.
 */
public class PropertyRecleItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView mItemTextView;
    private final ImageView mItemImageView;
    private final RelativeLayout mCategoryTitlelayout;
    private final TextView mTitleTextView;
    private final Button mViewMoreBtn;
    private final RelativeLayout mMainListItem;
    private static Activity mActivity;


    public PropertyRecleItemViewHolder(final View parent, TextView itemTextView, ImageView ItemImageView, RelativeLayout categoryTitlelayout, TextView titleTextView, Button viewMoreBtn, RelativeLayout mainListItem) {
        super(parent);
        mItemTextView = itemTextView;
        mItemImageView = ItemImageView;
        mCategoryTitlelayout = categoryTitlelayout;
        mTitleTextView = titleTextView;
        mViewMoreBtn = viewMoreBtn;
        mMainListItem = mainListItem;
    }

    public static PropertyRecleItemViewHolder newInstance(View parent, Activity activity) {

        TextView itemTextView = (TextView) parent.findViewById(R.id.property_name_id);
        ImageView mItemImageView = (ImageView) parent.findViewById(R.id.property_icon_id);
        /*RelativeLayout mCategoryTitlelayout = (RelativeLayout) parent.findViewById(R.id.category_name_title_layout);
        TextView mTitleTextView = (TextView) parent.findViewById(R.id.category_name_title);*/
        Button mViewMoreBtn = (Button) parent.findViewById(R.id.viewMoreBtn);
        RelativeLayout mMainListItem = (RelativeLayout) parent.findViewById(R.id.mainListItem);
        mActivity = activity;
        return /*new PropertyRecleItemViewHolder(parent, itemTextView, mItemImageView, mCategoryTitlelayout, mTitleTextView,mViewMoreBtn,mMainListItem)*/null;
    }

    public void setHeader(boolean isHeader) {

        if (isHeader) {
            mCategoryTitlelayout.setVisibility(View.VISIBLE);
        } else {
            mCategoryTitlelayout.setVisibility(View.GONE);
        }

    }

    public void setItemText(CharSequence text) {
        mItemTextView.setText(text);
    }

    public void setItemImage(int image) {
        mItemImageView.setBackgroundResource(image);
    }

    public void clickListener(final int position) {

        mMainListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity, HostelDetailActivity.class);
                it.putExtra("selected_index", position);
                it.putExtra("list_type", "list_type_viewmore");
                mActivity.startActivity(it);
            }
        });
    }

}
