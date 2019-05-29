package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by admin on 4/19/2017.
 */
public class Articales_Model
{
    public String item_name;
    public String item_id;
    public String code;
    public boolean box;
    public String item_assign_quantity;
    public String item_quantity;
    public String item_price;
    public String item_remaining_quantity;


    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_remaining_quantity() {
        return item_remaining_quantity;
    }

    public void setItem_remaining_quantity(String item_remaining_quantity) {
        this.item_remaining_quantity = item_remaining_quantity;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_assign_quantity() {
        return item_assign_quantity;
    }

    public void setItem_assign_quantity(String item_assign_quantity) {
        this.item_assign_quantity = item_assign_quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Articales_Model(String item_name, String item_id,String code,boolean _box) {
        this.item_name = item_name;
        this.item_id=item_id;
        this.code=code;
        box = _box;
    }


    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public Articales_Model() {

    }
}
