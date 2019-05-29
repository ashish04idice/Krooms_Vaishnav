package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.TenantModal;

import java.util.ArrayList;

/**
 * Created by anuradhaidice on 22/4/17.
 */

public class Tenant_Attendance_Notification_ListAdapter extends BaseAdapter {

    Context context;
    String[] roomno;
    int[] imageId;
    String[] tenantname;
    int[] imagestatus;
    int attendencecount;

    ArrayList<TenantModal> userlist;

    public Tenant_Attendance_Notification_ListAdapter(Context context, ArrayList<TenantModal> userlist) {
        this.context = context;
        this.userlist = userlist;

    }

    @Override
    public int getCount() {
        return userlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.attendance_notification_list_item, null, true);
        TextView txtroomno = (TextView) vi.findViewById(R.id.txtroomno);
        TextView txtname = (TextView) vi.findViewById(R.id.txttenantname);
        TextView txtdate = (TextView) vi.findViewById(R.id.txtdate);
        TextView txttime = (TextView) vi.findViewById(R.id.txttime);
        TextView txtattstatus = (TextView) vi.findViewById(R.id.attstatus);
        CardView card = (CardView) vi.findViewById(R.id.cardview);
        txtroomno.setText(userlist.get(position).getTenant_roomno());
        txtname.setText(userlist.get(position).getTenant_name());
        txtattstatus.setText(userlist.get(position).getAtt_status());
        txttime.setText(userlist.get(position).getTime());
        card.setBackgroundResource(R.drawable.blue_att);
        return vi;
    }
}
