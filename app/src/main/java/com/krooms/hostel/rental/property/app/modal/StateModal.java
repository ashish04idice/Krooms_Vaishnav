package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by user on 2/16/2016.
 */
public class StateModal {

    public String strSateId;
    public String strCountryId;
    public String strStateName;
    public String CountryId;
    public int checkItem;

    public void setStrCountryId(String strCountryId) {
        this.strCountryId = strCountryId;
    }

    public String getStrCountryId() {
        return strCountryId;
    }

    public String getStrSateId() {
        return strSateId;
    }

    public void setStrSateId(String strSateId) {
        this.strSateId = strSateId;
    }

    public String getStrStateName() {
        return strStateName;
    }

    public void setStrStateName(String strStateName) {
        this.strStateName = strStateName;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        this.CountryId = countryId;
    }

    public int getChecked() {
        return checkItem;
    }

    public void setChecked(int check) {
        this.checkItem = check;
    }

}
