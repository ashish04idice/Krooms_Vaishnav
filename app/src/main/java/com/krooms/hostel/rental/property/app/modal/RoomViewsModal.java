package com.krooms.hostel.rental.property.app.modal;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by user on 2/20/2016.
 */
public class RoomViewsModal implements Parcelable {

    public EditText edtRoomNo;
    public EditText edtRoomAmount;
    public Spinner spinnerNoofRoom;
    public Spinner spinnerVacantBad;
    public Spinner spinnerlatbath;
    public Button btnBrowse;
    public String roomId = "";


    public RoomViewsModal(Parcel in) {
        roomId = in.readString();
    }

    public static final Creator<RoomViewsModal> CREATOR = new Creator<RoomViewsModal>() {
        @Override
        public RoomViewsModal createFromParcel(Parcel in) {
            return new RoomViewsModal(in);
        }

        @Override
        public RoomViewsModal[] newArray(int size) {
            return new RoomViewsModal[size];
        }
    };

    public String getStrRoomId() {
        return roomId;
    }

    public void setStrRoomId(String roomId) {
        this.roomId = roomId;
    }

    public EditText getEdtRoomNo() {
        return edtRoomNo;
    }

    public void setEdtRoomNo(EditText edtRoomNo) {
        this.edtRoomNo = edtRoomNo;
    }

    public EditText getEdtRoomAmount() {
        return edtRoomAmount;
    }

    public void setEdtRoomAmount(EditText edtRoomAmount) {
        this.edtRoomAmount = edtRoomAmount;
    }

    public Spinner getSpinnerVacantBad() {
        return spinnerVacantBad;
    }

    public void setSpinnerVacantBad(Spinner spinnerVacantBad) {
        this.spinnerVacantBad = spinnerVacantBad;
    }

    public Spinner getSpinnerNoofRoom() {
        return spinnerNoofRoom;
    }

    public void setSpinnerNoofRoom(Spinner spinnerNoofRoom) {
        this.spinnerNoofRoom = spinnerNoofRoom;
    }

    public Spinner getSpinnerlatbath() {
        return spinnerlatbath;
    }

    public void setSpinnerlatbath(Spinner spinnerlatbath) {
        this.spinnerlatbath = spinnerlatbath;
    }

    public Button getBtnBrowse() {
        return btnBrowse;
    }

    public void setBtnBrowse(Button btnBrowse) {
        this.btnBrowse = btnBrowse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
    }
}
