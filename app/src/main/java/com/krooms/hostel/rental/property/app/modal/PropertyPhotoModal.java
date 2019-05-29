package com.krooms.hostel.rental.property.app.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2/1/2016.
 */
public class PropertyPhotoModal implements Parcelable {

    private String photo_id;
    private String photo_Url;
    private String photo_from;

    public PropertyPhotoModal(Parcel in) {
        photo_id = in.readString();
        photo_Url = in.readString();
        photo_from = in.readString();
    }

    public static final Creator<PropertyPhotoModal> CREATOR = new Creator<PropertyPhotoModal>() {
        @Override
        public PropertyPhotoModal createFromParcel(Parcel in) {
            return new PropertyPhotoModal(in);
        }

        @Override
        public PropertyPhotoModal[] newArray(int size) {
            return new PropertyPhotoModal[size];
        }
    };

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public void setPhoto_Url(String photo_Url) {
        this.photo_Url = photo_Url;
    }

    public void setPhoto_from(String photo_from) {
        this.photo_from = photo_from;
    }

    public String getPhoto_id() {
        return photo_id;
    }
    public String getPhoto_Url() {
        photo_Url = photo_Url.replaceAll(" ", "%20");
        return photo_Url;
    }

    public String getPhoto_from() {
        return photo_from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo_id);
        dest.writeString(photo_Url);
        dest.writeString(photo_from);
    }
}
