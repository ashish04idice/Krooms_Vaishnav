package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 28/9/2016.
 */

public class AutoCompleteAdapter extends ArrayAdapter<StateModal> {
    private Filter mFilter;
    private List<StateModal> mSubData = new ArrayList<StateModal>();
    static int counter = 0;

    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        setNotifyOnChange(false);

        mFilter = new Filter() {
            private int c = ++counter;
            private List<StateModal> mData = new ArrayList<StateModal>();

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // This method is called in a worker thread
                mData.clear();
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (c == counter) {
                    mSubData.clear();
                    if (results != null && results.count > 0) {
                        ArrayList<StateModal> obejcts = (ArrayList<StateModal>) results.values;
                        for (StateModal v : obejcts)
                            mSubData.add(v);

                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            }
        };
    }

    @Override
    public int getCount() {
        return mSubData.size();
    }

    @Override
    public StateModal getItem(int index) {
        return mSubData.get(index);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
