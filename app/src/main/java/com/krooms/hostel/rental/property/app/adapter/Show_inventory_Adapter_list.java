package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.Enventory_Activity_Owner;
import com.krooms.hostel.rental.property.app.modal.Articales_Model;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/2016.
 */
public class Show_inventory_Adapter_list extends BaseAdapter {

    ArrayList<Articales_Model> objects = new ArrayList<Articales_Model>();
    ArrayList<Articales_Model> articalshow_list_value = new ArrayList<Articales_Model>();

    String itemcode_value;
    ListView artical_list;
    int listposition;
    int assign_quantity = 0;
    String assign_item_quantity = "0";
    String item_quantity_value = "0";
    private Activity context;

    public Show_inventory_Adapter_list(Activity context, ArrayList<Articales_Model> objects) {
        this.context = context;
        this.objects = objects;

    }

    static class ViewHolder {
        private TextView itemname;
        private EditText item_code;
        private CheckBox itemcheckbox;
        private TextView item_id_textview;
        private TextView item_price;
        private TextView remaing_quntity;

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return (position);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public View getView(final int position, View vi, ViewGroup parent) {
        View v = vi;
        final ViewHolder holder;
        if (vi == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.show_inventory_layout_item, null);
            holder = new ViewHolder();
            String itemid_value = objects.get(position).getItem_id();
            String item_name = objects.get(position).getItem_name();
            String item_price1 = objects.get(position).getItem_price();
            String remaing_item1 = objects.get(position).getItem_assign_quantity();
            item_quantity_value = objects.get(position).getItem_quantity();
            TextView item_quantity_view = (TextView) v.findViewById(R.id.iteam_quantity1);
            holder.itemname = (TextView) v.findViewById(R.id.iteam_name_view1);
            holder.item_price = (TextView) v.findViewById(R.id.iteam_price1);
            holder.remaing_quntity = (TextView) v.findViewById(R.id.iteam_remaing_quantity1);
            item_quantity_view.setText(item_quantity_value);
            holder.itemname.setText(item_name);
            holder.item_price.setText(item_price1);
            holder.remaing_quntity.setText(remaing_item1);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        return v;
    }


}















