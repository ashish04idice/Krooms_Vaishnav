package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krooms.hostel.rental.property.app.ViewHolder.PropertyRecleItemViewHolder;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;

import java.util.List;

/**
 * Created by user on 2/5/2016.
 */
public class PropertyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PropertyModal> mItemList;
    boolean isHeaderDisplayed = false;
    Activity mActivity;
    private String[] id = {"1", "2", "3", "4", "5"};
    private String[] type = {"Hostel", "PGs", "Service app.", "Studio app.", "Rooms"};

    public PropertyRecyclerAdapter(List<PropertyModal> itemList, Activity activity) {
        mItemList = itemList;
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_list_item, parent, false);
        return PropertyRecleItemViewHolder.newInstance(view, mActivity);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PropertyRecleItemViewHolder holder = (PropertyRecleItemViewHolder) viewHolder;
        String itemText = mItemList.get(position).getProperty_name();
        int image = 0;
        if (position % 2 == 0) {

            image = R.drawable.ic_default_background;

        } else {

            image = R.drawable.ic_default_background;


        }

        String title = "";
        for (int i = 0; i < id.length; i++) {
            if (mItemList.get(position).getProperty_type_id().equals(id[i])) {
                title = type[i];
            }
        }
        holder.setHeader(false);
        holder.setItemText(itemText);
        holder.setItemImage(image);
        holder.clickListener(position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

}
