package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by Raghveer on 07-12-2016.
 */
public class MonthModal {

    String month_name;
    String month_id;

    MonthModal modal=new MonthModal();

    public MonthModal getModal() {
        return modal;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getMonth_id() {
        return month_id;
    }

    public void setMonth_id(String month_id) {
        this.month_id = month_id;
    }
}
