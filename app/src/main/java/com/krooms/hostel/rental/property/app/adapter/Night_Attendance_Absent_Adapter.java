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
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 2/1/2016.
 */
public class Night_Attendance_Absent_Adapter extends BaseAdapter {

    private Context context;
    private Activity activity;
    private ArrayList<TenantModal> _listArray;

    public Night_Attendance_Absent_Adapter(Activity context, ArrayList<TenantModal> objects) {
        this.context = context;
        this.activity = context;
        this._listArray = objects;
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
        TextView userName, Roomno, Txtnum;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.night_absent_user_list_item, null);
            final ViewHolder _viewHolder = new ViewHolder();
            _viewHolder.userName = (TextView) convertView.findViewById(R.id.property_user_name);
            _viewHolder.Roomno = (TextView) convertView.findViewById(R.id.property_user_book_room);
            _viewHolder.Txtnum = (TextView) convertView.findViewById(R.id.txtnum);
            convertView.setTag(_viewHolder);
        }

        final ViewHolder _viewHolder = (ViewHolder) convertView.getTag();
        _viewHolder.userName.setText("Name:- " + _listArray.get(position).getTenant_name());
        _viewHolder.Roomno.setText("Room no:- " + _listArray.get(position).getTenant_roomno());
        _viewHolder.Txtnum.setText(_listArray.get(position).getNumber());
        return convertView;
    }
}