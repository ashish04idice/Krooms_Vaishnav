package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;
import com.squareup.picasso.Picasso;

/**
 * Created by user on 2/22/2016.
 */
public class BedListAdapter extends BaseAdapter {
    FragmentActivity mActivity;
    public static boolean permissionDialogShown = false;
    public static int newposition;
    public int selectedBed = -1;
    Common mCommon;
    int mPosition;
    int bookedBed;

    public BedListAdapter(FragmentActivity activity, int position/*,int cap, int vac*//*, ArrayList<RoomBedModal> list*//*, RoomModal modal*/) {
        mActivity = activity;
        this.mPosition = position;
        mCommon = new Common();
    }

    @Override
    public int getCount() {
        return BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().size();
    }

    @Override
    public Object getItem(int position) {
        return BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (null == convertView) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.bedgrid_item, null);
//        }

        ImageView bed = (ImageView) convertView.findViewById(R.id.bed);

        newposition = position;

        if (BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getBedType().equals("Booked")) {
            bed.setImageResource(R.drawable.ic_bed_booked);
        } else if (BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getBedType().equals("Vacant")) {
            bed.setImageResource(R.drawable.ic_bed_vacant);
            if (BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getIsSelected()) {
                bed.setBackgroundColor(mActivity.getResources().getColor(R.color.orange_color));
            } else {
                bed.setBackgroundColor(mActivity.getResources().getColor(R.color.transparent));
            }
        }


        for (int i = 0; i < Common.selectedBedInfo.size(); i++) {

            if (Common.selectedBedInfo.get(i).getRoomId().equals(BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getRoomId())) {
                if (Common.selectedBedInfo.get(i).getBedIndex() == BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getBedIndex()) {

                    BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).setIsSelected(true);
                    bed.setBackgroundColor(mActivity.getResources().getColor(R.color.orange_color));
                    notifyDataSetChanged();
                }
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getBedType().equals("Vacant")) {


                    if (BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getIsSelected()) {

                        setUnSelectedBed(position);

                    } else {
                        CustomDialogClass cdd = new CustomDialogClass(mActivity);
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.setDataForBed(BedSelectionGridAdapter.mRoomList.get(mPosition).getRoomNo(), BedSelectionGridAdapter.mRoomList.get(mPosition).getBedPriceAmount(), BedSelectionGridAdapter.mRoomList.get(mPosition).getLat_bath(), BedSelectionGridAdapter.mRoomList.get(mPosition).getRoomac(), BedSelectionGridAdapter.mRoomList.get(mPosition).getRoombalcony(), "Do you want to book this room ?", BedSelectionGridAdapter.mRoomList.get(mPosition).getRoomImage(), position);
                        cdd.show();
                    }

                } else {
                    mCommon.displayAlert(mActivity, "This bed is already booked", false);
                }

            }
        });


        return convertView;
    }


    public void setSelectedBed(int position) {
        boolean isHaving = false;
        for (int i = 0; i < Common.selectedBedInfo.size(); i++) {
            if (Common.selectedBedInfo.get(i).getRoomId().equals(BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getRoomId())) {
                if (Common.selectedBedInfo.get(i).getBedIndex() == BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getBedIndex()) {
                    isHaving = true;
                    break;
                } else {
                    isHaving = false;
                }
            }
        }
        if (!isHaving) {
            BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).setIsSelected(true);

            Common.selectedBedInfo.add(BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position));
            if (!BedSelectionGridAdapter.mRoomList.get(mPosition).getBedPriceAmount().equals("")) {
                HostelDetailActivity.room_rent = HostelDetailActivity.room_rent + Double.parseDouble(BedSelectionGridAdapter.mRoomList.get(mPosition).getBedPriceAmount());
            }
            notifyDataSetChanged();
            callReciver();
        }
    }


    public void setUnSelectedBed(int position) {

        for (int i = 0; i < Common.selectedBedInfo.size(); i++) {

            if (Common.selectedBedInfo.get(i).getRoomId().equals(BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getRoomId())) {
                if (Common.selectedBedInfo.get(i).getBedIndex() == BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).getBedIndex()) {
                    BedSelectionGridAdapter.mRoomList.get(mPosition).getmBedList().get(position).setIsSelected(false);
                    Common.selectedBedInfo.remove(i);
                    if (!BedSelectionGridAdapter.mRoomList.get(mPosition).getBedPriceAmount().equals("")) {
                        HostelDetailActivity.room_rent = HostelDetailActivity.room_rent - Double.parseDouble(BedSelectionGridAdapter.mRoomList.get(mPosition).getBedPriceAmount())/* * Common.selectedBedInfo.size()*/;
                    }
                    notifyDataSetChanged();
                    callReciver();
                }
            }
        }
    }

    public void callReciver() {
        Intent it = new Intent("com.krooms.hostel.rental.property.app.BED_SELECTION_CHANGE_ACTION");
        mActivity.sendBroadcast(it);
    }

    //new Room change dailog on click  bed changes on 18/2/2019 by ashish
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;

        private FragmentActivity mFActivity = null;
        String mRent, mRoomNo, mLatBath, mMsg, room_image_path, mroomac, mroombalcony;
        TextView roomRent, roomNo, roomLatBath, roomac, roombalcony;
        TextView textMsg;
        Button btn1, btn2;
        ImageView room_image;
        int positions;

        public CustomDialogClass(FragmentActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }


        public void setDataForBed(String roomNoStr, String rent, String latBath, String room_ac, String room_balcony, String msg, String room_image, int position) {

            mRent = rent;
            mRoomNo = roomNoStr;
            mLatBath = latBath;
            mMsg = msg;
            room_image_path = room_image;
            mroomac = room_ac;
            mroombalcony = room_balcony;
            positions = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.bed_information_dialog);
            c.setTheme(R.style.CustomDialog);
            c.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            createView();
            setListner();
        }

        public void createView() {
            roomNo = (TextView) findViewById(R.id.roomNoText_id);
            roomRent = (TextView) findViewById(R.id.roomRentText_id);
            roomLatBath = (TextView) findViewById(R.id.roomLatBathText_id);
            room_image = (ImageView) findViewById(R.id.room_image);
            room_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textMsg = (TextView) findViewById(R.id.bedRelatedMsg);
            roomac = (TextView) findViewById(R.id.ac_id);
            roombalcony = (TextView) findViewById(R.id.balcony_id);
            btn1 = (Button) findViewById(R.id.btn1);
            btn2 = (Button) findViewById(R.id.btn2);
            roomNo.setText(mRoomNo);
            roomRent.setText(mRent);
            roomLatBath.setText(mLatBath);
            roomac.setText(mroomac);
            roombalcony.setText(mroombalcony);
            Picasso.with(mFActivity)
                    .load(WebUrls.IMG_URL + "" + room_image_path)
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    .into(room_image);
            room_image.setOnClickListener(this);
            textMsg.setText(mMsg);
        }

        public void setListner() {

            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    if (Common.selectedBedInfo.size() <= 3) {
                        setSelectedBed(positions);
                    } else {
                        mCommon.displayAlert(mActivity, "You can only select 4 beds.", false);

                    }
                    break;
                case R.id.btn2:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}
