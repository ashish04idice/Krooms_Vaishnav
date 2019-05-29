package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by ashish on 31/1/17.
 */

public class Childlistmodel {


    String Tenant_name,Hostal_name;
    boolean box;


   /* Childlistmodel(String _name, String _hostal, boolean _box) {
        Tenant_name = _name;
        Hostal_name = _hostal;
        box = _box;
    }*/

    public String getTenant_name() {
        return Tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        Tenant_name = tenant_name;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public String getHostal_name() {
        return Hostal_name;
    }

    public void setHostal_name(String hostal_name) {
        Hostal_name = hostal_name;
    }

}
