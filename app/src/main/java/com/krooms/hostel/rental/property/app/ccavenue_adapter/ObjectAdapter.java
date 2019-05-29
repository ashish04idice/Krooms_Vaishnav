package com.krooms.hostel.rental.property.app.ccavenue_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.callback.SpinnerResponce;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;

/**
 * Created by user on 9/2/2016.
 */

public class ObjectAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private Activity mActivity;
    private ArrayList<StateModal> originalList;
    private ArrayList<StateModal> suggestions = new ArrayList<>();
    private Filter filter = new CustomFilter();
    private SpinnerResponce mSpinnerResponce;

    static int counter = 0;
    /**
     * @param context      Context
     * @param originalList Original list used to compare in constraints.
     */
    public ObjectAdapter(Activity context, ArrayList<StateModal> originalList) {
        this.context = context;
        this.mActivity = context;
        this.originalList = originalList;
    }

    @Override
    public int getCount() {
        return suggestions.size(); // Return the size of the suggestions list.
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * This is where you inflate the layout and also where you set what you want to display.
     * Here we also implement a View Holder in order to recycle the views.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            holder = new ViewHolder();
            holder.autoText = (TextView) convertView.findViewById(R.id.textview_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try{
        holder.autoText.setText(suggestions.get(position).getStrStateName());
        holder.autoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                mSpinnerResponce.requestOTPServiceResponce(suggestions.get(position).getStrSateId(),
                        suggestions.get(position).getStrStateName());
                }catch (Exception e){}
            }
        });
        }catch (IndexOutOfBoundsException e){

        }catch (Exception e){

        }

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setCallBack(SpinnerResponce callBack) {
        this.mSpinnerResponce = callBack;
    }

    private static class ViewHolder {
        TextView autoText;
    }

    /**
     * Our Custom Filter Class.
     */
    private class CustomFilter extends Filter {

        private int c = ++counter;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            ArrayList<StateModal> tempList = new ArrayList<>();
            try{
            if (originalList != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < originalList.size(); i++) {
                    if (originalList.get(i).getStrStateName().toLowerCase().contains(constraint.toString().toLowerCase())) { // Compare item in original list if it contains constraints.
                        tempList.add(originalList.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
             // Create new Filter Results and return this to publishResults;
            results.values = tempList;
            results.count = tempList.size();
            }catch (Exception e){

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (c == counter) {
                suggestions.clear();
                if (results != null && results.count > 0) {
                    ArrayList<StateModal> obejcts = (ArrayList<StateModal>) results.values;
                    for (StateModal v : obejcts)
                        suggestions.add(v);

                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
            /*if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }*/
        }
    }
}
