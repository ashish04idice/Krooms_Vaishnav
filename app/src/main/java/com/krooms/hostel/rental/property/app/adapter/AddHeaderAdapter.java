package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.PropertyListCategoryWiseActivity;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.fragments.PropertyListFragment;
import com.krooms.hostel.rental.property.app.indicator.CirclePageIndicator;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raghveer on 12-01-2017.
 */
public class AddHeaderAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private String[] titleArray_id = {"1", "2", "3", "4", "5"};
    private String[] titleArray = {"Hostels", "PGs", "Service Apartments", "Rooms", "Studio Apartments"};

    private HashMap<String, ArrayList<PropertyModal>> list = new HashMap<String, ArrayList<PropertyModal>>();
    private RelativeLayout categoryTitlelayout;
    LayoutInflater inflater;


    public AddHeaderAdapter(Activity context, HashMap<String, ArrayList<PropertyModal>> objects) {
        this.context = context;
        this.activity = context;
        inflater = activity.getLayoutInflater();
        list = objects;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
//        holder = null;
        if (null == convertView) {
//            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.viewpager_layout, null);

//            convertView.setTag(holder);
        }/*else{
            holder = (ViewHolder) convertView.getTag();
        }*/
        final TextView textView = (TextView) convertView.findViewById(R.id.propertyTypeTitle);
        ViewPager pager = (ViewPager) convertView.findViewById(R.id.pager);
        CirclePageIndicator titleIndicatorTop = (CirclePageIndicator) convertView.findViewById(R.id.pagerIndicator);


//        final TextView textView = (TextView) convertView.findViewById(R.id.propertyTypeTitle);
        textView.setText(titleArray[PropertyListFragment.propertyListIndex.get(position)]);
        pager.setAdapter(new HeaderCustomPagerAdapter(activity, list.get(titleArray[PropertyListFragment.propertyListIndex.get(position)])));
        titleIndicatorTop.setViewPager(pager);
        pager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE && parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        Button viewMoreBtn = (Button) convertView.findViewById(R.id.viewMoreBtn);

        viewMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String propertyType = "";
                if (textView.getText().equals("Hostels")) {
                    propertyType = "1";
                } else if (textView.getText().equals("PGs")) {
                    propertyType = "2";

                } else if (textView.getText().equals("Service Apartments")) {
                    propertyType = "3";

                } else if (textView.getText().equals("Rooms")) {
                    propertyType = "4";

                } else if (textView.getText().equals("Studio Apartments")) {
                    propertyType = "5";
                }
                Intent it = new Intent(activity, PropertyListCategoryWiseActivity.class);
                it.putExtra("keyword", "");
                it.putExtra("property_type", propertyType);
                activity.startActivity(it);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.Config(" on Text view click    " + textView.getText());

                String propertyType = "";

                if (textView.getText().equals("Hostels")) {
                    propertyType = "1";
                } else if (textView.getText().equals("PGs")) {
                    propertyType = "2";

                } else if (textView.getText().equals("Service Apartments")) {
                    propertyType = "3";

                } else if (textView.getText().equals("Rooms")) {
                    propertyType = "4";

                } else if (textView.getText().equals("Studio Apartments")) {
                    propertyType = "5";
                }

                Intent it = new Intent(activity, PropertyListCategoryWiseActivity.class);
                it.putExtra("keyword", "");
                it.putExtra("property_type", propertyType);
                activity.startActivity(it);
            }
        });

        return convertView;
    }

}
