package com.krooms.hostel.rental.property.app.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by user on 2/20/2016.
 */
public class RoomModal implements Parcelable {

    public String roomId;
    public String ownerId;
    public String propertyId;
    public String roomNo;
    public String roomType;
    public String vacantBed;
    public String bedPriceAmount;
    public String lat_bath;
    public String roomImage;

    public String roomac;
    public  String roombalcony;

    public ArrayList<RoomBedModal> mBedList = new ArrayList<>();

    public RoomModal(Parcel in) {
        roomId = in.readString();
        ownerId = in.readString();
        propertyId = in.readString();
        roomNo = in.readString();
        roomType = in.readString();
        vacantBed = in.readString();
        bedPriceAmount = in.readString();
        lat_bath = in.readString();
        roomImage = in.readString();
        roomac = in.readString();
        roombalcony = in.readString();
    }

    public static final Creator<RoomModal> CREATOR = new Creator<RoomModal>() {
        @Override
        public RoomModal createFromParcel(Parcel in) {
            return new RoomModal(in);
        }

        @Override
        public RoomModal[] newArray(int size) {
            return new RoomModal[size];
        }
    };

    public String getRoomac() {
        return roomac;
    }

    public void setRoomac(String roomac) {
        this.roomac = roomac;
    }

    public String getRoombalcony() {
        return roombalcony;
    }

    public void setRoombalcony(String roombalcony) {
        this.roombalcony = roombalcony;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setBedPriceAmount(String bedPriceAmount) {
        this.bedPriceAmount = bedPriceAmount;
    }

    public void setLat_bath(String lat_bath) {
        this.lat_bath = lat_bath;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setVacantBed(String vacantBed) {
        this.vacantBed = vacantBed;
    }

    public void setmBedList(ArrayList<RoomBedModal> mBedList) {
        this.mBedList = mBedList;
    }

    public String getBedPriceAmount() {

        return bedPriceAmount;
    }

    public String getLat_bath() {
        return lat_bath;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomImage() {
        roomImage = roomImage.replaceAll(" ", "%20");
        return roomImage;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getVacantBed() {
        return vacantBed;
    }

    public ArrayList<RoomBedModal> getmBedList() {
        return mBedList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
        dest.writeString(ownerId);
        dest.writeString(propertyId);
        dest.writeString(roomNo);
        dest.writeString(roomType);
        dest.writeString(vacantBed);
        dest.writeString(bedPriceAmount);
        dest.writeString(lat_bath);
        dest.writeString(roomImage);
        dest.writeString(roomac);
        dest.writeString(roombalcony);
    }
}
