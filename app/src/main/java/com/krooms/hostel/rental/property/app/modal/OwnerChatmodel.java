package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by Raghveer on 09-02-2017.
 */
public class OwnerChatmodel {

    private  String description;
    private String name;
    private boolean gender;
    public String message;
    public String Parenttenantname;
    String sender_name;

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getParenttenantname() {
        return Parenttenantname;
    }

    public void setParenttenantname(String parenttenantname) {
        Parenttenantname = parenttenantname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
