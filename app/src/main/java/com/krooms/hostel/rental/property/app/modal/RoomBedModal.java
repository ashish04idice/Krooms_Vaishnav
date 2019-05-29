package com.krooms.hostel.rental.property.app.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 3/14/2016.
 */
public class RoomBedModal implements Parcelable {

    private String roomId;
    private String bedType;
    private String roomNo;
    private int bedIndex;
    private boolean isSelected = false;

    public  RoomBedModal(Parcel in) {
        roomId = in.readString();
        bedType = in.readString();
        roomNo = in.readString();
        bedIndex = in.readInt();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<RoomBedModal> CREATOR = new Creator<RoomBedModal>() {
        @Override
        public RoomBedModal createFromParcel(Parcel in) {
            return new RoomBedModal(in);
        }

        @Override
        public RoomBedModal[] newArray(int size) {
            return new RoomBedModal[size];
        }
    };

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setBedIndex(int bedIndex) {
        this.bedIndex = bedIndex;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getBedType() {
        return bedType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean getIsSelected(){
        return isSelected;
    }

    public int getBedIndex() {
        return bedIndex;
    }

    public String getRoomNo() {
        return roomNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
        dest.writeString(bedType);
        dest.writeString(roomNo);
        dest.writeInt(bedIndex);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
