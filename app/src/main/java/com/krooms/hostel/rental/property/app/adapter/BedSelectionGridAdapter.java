package com.krooms.hostel.rental.property.app.adapter;

import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.modal.RoomModal;
import com.krooms.hostel.rental.property.app.modal.RoomBedModal;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import java.util.ArrayList;

/**
 * Created by user on 2/22/2016.
 */
public abstract class BedSelectionGridAdapter extends BaseAdapter {
    FragmentActivity mActivity;
    public static ArrayList<RoomModal> mRoomList = new ArrayList<>();
    Common mCommon;
    String roomNO = "";
    String roomId = "";
    int capacity = 0;
    int vacency = 0;
    String rent = "";
    String latBath = "";
    boolean mFlag = false;
    public static BedListAdapter adapter;

    public BedSelectionGridAdapter(FragmentActivity activity, ArrayList<RoomModal> roomList, boolean flag) {
        mActivity = activity;
        if (mRoomList.size() != 0) {
            mRoomList.clear();
        }
        mRoomList = roomList;
        mCommon = new Common();
        this.mFlag = flag;
    }

    @Override
    public int getCount() {
        return mRoomList.size();
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
    public View getView(final int mPosition, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.bed_selection_item, null);
        final TextView room_no = (TextView) convertView.findViewById(R.id.room_no_id);
        room_no.setText("Room no. " + mRoomList.get(mPosition).getRoomNo());
        Button uploadBtn = (Button) convertView.findViewById(R.id.roomImageUploadBtn);
        Common.Config("     " + mRoomList.get(mPosition).getRoomImage());
        if (!mRoomList.get(mPosition).getRoomImage().equals("") && mRoomList.get(mPosition).getRoomImage() != null) {

            if (!mRoomList.get(mPosition).getRoomImage().equals("image/rooms/no_image.jpg")) {

                uploadBtn.setBackgroundResource(R.drawable.custom_green_btn);
                uploadBtn.setText("Uploaded");
            } else {
                uploadBtn.setBackgroundResource(R.drawable.custom_btn);
                uploadBtn.setText("Upload");
            }
        }

        if (mFlag) {
            uploadBtn.setVisibility(View.VISIBLE);
            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadImageaAction(mPosition);
                }
            });
        }
        try {
            if (mRoomList.get(mPosition).getRoomType().equals("Bed")) {
                capacity = 0;
                vacency = 0;
            } else if (mRoomList.get(mPosition).getVacantBed().equals("Vacant Bed")) {
                capacity = Integer.parseInt(mRoomList.get(mPosition).getRoomType());
                vacency = Integer.parseInt(mRoomList.get(mPosition).getRoomType());
            } else {
                capacity = Integer.parseInt(mRoomList.get(mPosition).getRoomType());
                vacency = Integer.parseInt(mRoomList.get(mPosition).getVacantBed());
            }
        } catch (NumberFormatException e) {
        }

        if (mRoomList.get(mPosition).getLat_bath().equals("1")) {
            latBath = "Yes";
        } else {

            latBath = "No";
        }

        roomNO = mRoomList.get(mPosition).getRoomNo();
        rent = mRoomList.get(mPosition).getBedPriceAmount();
        roomId = mRoomList.get(mPosition).getRoomId();
        ArrayList<RoomBedModal> mRoomBedList = new ArrayList<>();
        int bookedBed = capacity - vacency;
        for (int i = 0; i < capacity; i++) {
            RoomBedModal modal = new RoomBedModal(Parcel.obtain());
            if (i < bookedBed) {
                modal.setBedType("Booked");
            } else {
                modal.setBedType("Vacant");
            }
            modal.setBedIndex(i);
            modal.setRoomId(roomId);
            modal.setRoomNo(roomNO);
            mRoomBedList.add(modal);
        }
        mRoomList.get(mPosition).setmBedList(mRoomBedList);
        GridView gridView = (GridView) convertView.findViewById(R.id.bedGrid);
        adapter = new BedListAdapter(mActivity/*,capacity,vacency*/ /*, *//*mRoomBedList,*//*mRoomList.get(mPosition)*/, mPosition);
        gridView.setAdapter(adapter);
        return convertView;
    }

    public static void notifydata() {
        adapter.notifyDataSetChanged();
    }

    public abstract void uploadImageaAction(int index);

}
