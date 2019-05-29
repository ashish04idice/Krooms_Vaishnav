package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.TenantDetailActivity;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.krooms.hostel.rental.property.app.R.id.imageView;

/**
 * Created by user on 2/1/2016.
 */
public class MyBookedUserListAdapter_newAttendence extends BaseAdapter {

    private Context context;
    private Activity activity;
    private ArrayList<PropertyUserModal> _listArray;

    public MyBookedUserListAdapter_newAttendence(Activity context, ArrayList<PropertyUserModal> objects) {
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
        TextView userName, bookRoom, bookBad, bookDate, userAmount, userAddress;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.myproeprty_user_list_item, null);
            final ViewHolder _viewHolder = new ViewHolder();
            _viewHolder.userName = (TextView) convertView.findViewById(R.id.property_user_name);
            _viewHolder.bookRoom = (TextView) convertView.findViewById(R.id.property_user_book_room);
            _viewHolder.bookBad = (TextView) convertView.findViewById(R.id.property_user_book_room_bad);
            _viewHolder.bookDate = (TextView) convertView.findViewById(R.id.property_user_book_date);
            _viewHolder.userAddress = (TextView) convertView.findViewById(R.id.property_user_address);
            _viewHolder.userAmount = (TextView) convertView.findViewById(R.id.property_user_book_amount);
            _viewHolder.userImage = (ImageView) convertView.findViewById(R.id.imageview_itm_user);
            convertView.setTag(_viewHolder);
        }

        final ViewHolder _viewHolder = (ViewHolder) convertView.getTag();

        _viewHolder.userName.setText(_listArray.get(position).getTenant_fname() + " " + _listArray.get(position).getTenant_lname());
        _viewHolder.bookRoom.setText("Room no: " + _listArray.get(position).getBookedRoom());
        _viewHolder.bookBad.setText("Room Amount: " + _listArray.get(position).getRoomAmount());
        _viewHolder.userAmount.setText("Status: " + _listArray.get(position).getPaymentStatus());
        _viewHolder.bookDate.setText("Join Date: " + _listArray.get(position).getProperty_hire_date());
        _viewHolder.userAddress.setText("Address: " + _listArray.get(position).getTenant_permanent_address());

        Picasso.with(context)
                .load(WebUrls.IMG_URL + _listArray.get(position).getTenant_photo())
                .placeholder(R.drawable.user_xl)
                .error(R.drawable.user_xl)
                .transform(new RoundedTransformation(activity))
                .into(_viewHolder.userImage);

        return convertView;
    }
}