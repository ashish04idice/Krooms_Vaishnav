package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.ShowAssign_Inventory_Activity;
import com.krooms.hostel.rental.property.app.modal.ShowInventory_item_model;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/20/2017.
 */
public class ShowAssignInventory_Adapter_Tenant extends BaseAdapter {

    ArrayList<ShowInventory_item_model> showinventory_arraylist = new ArrayList<>();
    private Activity context;
    LinearLayout iteam_remove;
    String item_code_value = "";
    String tenantidvalue, propertyid;

    public ShowAssignInventory_Adapter_Tenant(Activity context, ArrayList<ShowInventory_item_model> showinventory_arraylist, String tenantidvalue, String propertyid) {
        this.context = context;
        this.showinventory_arraylist = showinventory_arraylist;
        this.tenantidvalue = tenantidvalue;
        this.propertyid = propertyid;
    }

    @Override
    public int getCount() {
        return showinventory_arraylist.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater;

        inflater = context.getLayoutInflater();
        View rowView = null;
        {
            inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.show_add_layout_item_tenant, null, true);
            final TextView iteam_code = (TextView) rowView.findViewById(R.id.iteam_code_view);
            final TextView item_name = (TextView) rowView.findViewById(R.id.iteam_name);
            final TextView iteam_assign_date = (TextView) rowView.findViewById(R.id.iteam_assign_date);
            iteam_remove = (LinearLayout) rowView.findViewById(R.id.iteam_remove);
            final TextView artical_id = (TextView) rowView.findViewById(R.id.artical_id);
            String item_name_value = showinventory_arraylist.get(position).getItem_name();
            String item_code_value = showinventory_arraylist.get(position).getItem_code();
            String artical_id_value = showinventory_arraylist.get(position).getItem_id();
            String assign_datevalue = showinventory_arraylist.get(position).getAssign_date();
            iteam_assign_date.setText(assign_datevalue);
            iteam_code.setText(item_code_value);
            item_name.setText(item_name_value);
            artical_id.setText(artical_id_value);
        }
        return rowView;
    }


}
