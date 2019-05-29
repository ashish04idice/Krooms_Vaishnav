package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by admin on 11/18/2016.
 */
public class TanentStudentNameModel
{
     String owner_id;
    String room_id;
    String property_id;

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
