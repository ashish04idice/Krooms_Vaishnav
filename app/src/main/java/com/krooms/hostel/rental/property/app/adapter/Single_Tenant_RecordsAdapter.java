package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.TenantRecordmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by ashishidice on 22/4/17.
 */

public class Single_Tenant_RecordsAdapter extends BaseAdapter {

    Context context;
    ArrayList<TenantRecordmodel> list;
    Date date1 = null, date2 = null;
    String val = "";

    public Single_Tenant_RecordsAdapter(Context context, ArrayList<TenantRecordmodel> list) {

        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.listview_item_layout, null, true);
        TextView txtroomno = (TextView) vi.findViewById(R.id.txtroomno);
        TextView txtimg = (TextView) vi.findViewById(R.id.txtimg);
        TextView txtname = (TextView) vi.findViewById(R.id.txtname);
        TextView txtintime = (TextView) vi.findViewById(R.id.txtintime);
        TextView txtouttime = (TextView) vi.findViewById(R.id.txtouttime);
        TextView txtcount = (TextView) vi.findViewById(R.id.txtcount);
        TextView txtreason = (TextView) vi.findViewById(R.id.txtreason);

        try {
            val = list.get(position).getTime_interval();
            txtcount.setText(val);
            txtroomno.setText(list.get(position).getRoom_no());
            txtname.setText(list.get(position).getTenant_name());
            txtintime.setText(list.get(position).getTime());
            txtreason.setText(list.get(position).getAttendancestatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }
}
