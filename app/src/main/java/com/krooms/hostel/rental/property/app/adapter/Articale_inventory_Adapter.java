package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.Enventory_Activity_Owner;
import com.krooms.hostel.rental.property.app.activity.HelperAttedence;
import com.krooms.hostel.rental.property.app.modal.Articales_Model;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 11/10/2016.
 */
public class Articale_inventory_Adapter extends BaseAdapter {

    ArrayList<Articales_Model> objects = new ArrayList<Articales_Model>();
    ArrayList<Articales_Model> articalshow_list_value = new ArrayList<Articales_Model>();
    String itemcode_value;
    ListView artical_list;
    int listposition;
    int assign_quantity = 0;
    String assign_item_quantity = "0";
    String item_quantity_value = "0";
    private Activity context;

    public Articale_inventory_Adapter(Activity context, ArrayList<Articales_Model> objects, ArrayList<Articales_Model> articalshow_list_value, ListView artical_list) {
        this.context = context;
        this.objects = objects;
        this.articalshow_list_value = articalshow_list_value;
        this.artical_list = artical_list;
    }

    static class ViewHolder {
        private TextView itemname;
        private EditText item_code;
        private CheckBox itemcheckbox;
        private TextView item_id_textview;
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
            v = inflater.inflate(R.layout.artical_inventory_list_itemlayout, null);
            holder = new ViewHolder();
            String itemid_value = objects.get(position).getItem_id();
            String item_name = objects.get(position).getItem_name();
            assign_item_quantity = objects.get(position).getItem_assign_quantity();
            item_quantity_value = objects.get(position).getItem_quantity();
            holder.itemname = (TextView) v.findViewById(R.id.itemchildname);
            holder.item_id_textview = (TextView) v.findViewById(R.id.item_id_textview);
            holder.itemname.setText(item_name);
            holder.item_id_textview.setText(itemid_value);
            holder.itemcheckbox = (CheckBox) v.findViewById(R.id.itemcheckbox);
            holder.item_code = (EditText) v.findViewById(R.id.item_code);

            if (Integer.parseInt(objects.get(position).getItem_assign_quantity()) == Integer.parseInt(objects.get(position).getItem_quantity())) {
                holder.item_code.setText("Qty Not Available");
                holder.item_code.setEnabled(false);
                holder.itemcheckbox.setEnabled(false);

            }

            v.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }


        holder.itemcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemname_value = holder.itemname.getText().toString();
                String itmecode_value = holder.item_code.getText().toString();
                String item_id_value = holder.item_id_textview.getText().toString();
                if (holder.itemcheckbox.isChecked()) {
                    String codevalue = holder.item_code.getText().toString().trim();
                    String itemnamevalue = objects.get(position).getItem_name();
                    String item_id_new_value = objects.get(position).getItem_id();
                    Articales_Model articalmodel = new Articales_Model();
                    articalmodel.setCode(codevalue);
                    articalmodel.setItem_name(itemnamevalue);
                    articalmodel.setItem_id(item_id_new_value);
                    Enventory_Activity_Owner.articalshow_list_value.add(articalmodel);

                } else {
                    String codevalue = holder.item_code.getText().toString().trim();
                    String itemnamevalue = objects.get(position).getItem_name();
                    String item_id_new_value = objects.get(position).getItem_id();

                    for (int i = 0; i < Enventory_Activity_Owner.articalshow_list_value.size(); i++) {
                        String itemidvalaue = Enventory_Activity_Owner.articalshow_list_value.get(i).getItem_id();
                        if (itemidvalaue.equalsIgnoreCase(item_id_new_value)) {
                            Enventory_Activity_Owner.articalshow_list_value.remove(i);
                        }
                    }
                }
            }
        });

        return v;
    }
}















