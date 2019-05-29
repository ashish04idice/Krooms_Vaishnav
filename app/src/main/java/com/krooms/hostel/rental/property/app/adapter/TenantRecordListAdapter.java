package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.TenantModal;

import java.util.ArrayList;


/**
 * Created by user on 2/1/2016.
 */
public class TenantRecordListAdapter extends BaseAdapter {

    private Activity context;
    private Activity activity;
    private ArrayList<TenantModal> _listArray;

    public TenantRecordListAdapter(Activity context, ArrayList<TenantModal> userlist) {

        this.context = context;
        this.activity = (Activity) context;
        this._listArray = userlist;

    }

    @Override
    public int getCount() {
        return _listArray.size();
    }

    @Override
    public Object getItem(int position) {
        return _listArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {

        ImageView userImage;
        TextView userName, bookRoom, proprtyName, bookDate, userId, userAddress;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.myproeprty_user_list_item_new_tenant, null);
            final ViewHolder _viewHolder = new ViewHolder();
            _viewHolder.userName = (TextView) convertView.findViewById(R.id.tenant_name);
            _viewHolder.bookRoom = (TextView) convertView.findViewById(R.id.tenant_roomno);
            _viewHolder.proprtyName = (TextView) convertView.findViewById(R.id.property_name);
            _viewHolder.bookDate = (TextView) convertView.findViewById(R.id.text_date);
            _viewHolder.userAddress = (TextView) convertView.findViewById(R.id.property_user_address);
            _viewHolder.userId = (TextView) convertView.findViewById(R.id.user_id);
            _viewHolder.userImage = (ImageView) convertView.findViewById(R.id.tenant_image);
            convertView.setTag(_viewHolder);
        }
        final ViewHolder _viewHolder = (ViewHolder) convertView.getTag();
        _viewHolder.userName.setText(_listArray.get(position).getTenant_name());
        _viewHolder.bookRoom.setText("Room no: " + _listArray.get(position).getTenant_roomno());
        _viewHolder.proprtyName.setText("Property: " + "Hostel");
        _viewHolder.userId.setText("Id: " + _listArray.get(position).getTenant_id());
        _viewHolder.userAddress.setText("Address: " + _listArray.get(position).getTenant_address());
        return convertView;
    }
}