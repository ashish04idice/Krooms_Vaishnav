package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by admin on 8/2/2017.
 */
public class Roommodel
{
    String roomno;
    String roomnoslotvalue;
    String totalnoofrooms;
    String sequenceno;
    String Lath_bath;

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    String Vacant_bed;
    String facility;

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getBalcony() {
        return balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    String Nobed;
    String amount;
    String ac;
    String balcony;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNobed() {
        return Nobed;
    }

    public void setNobed(String nobed) {
        Nobed = nobed;
    }

    public String getVacant_bed() {
        return Vacant_bed;
    }

    public void setVacant_bed(String vacant_bed) {
        Vacant_bed = vacant_bed;
    }

    public String getLath_bath() {
        return Lath_bath;
    }

    public void setLath_bath(String lath_bath) {
        Lath_bath = lath_bath;
    }

    public String getSequenceno() {
        return sequenceno;
    }

    public void setSequenceno(String sequenceno) {
        this.sequenceno = sequenceno;
    }


    public String getTotalnoofrooms() {
        return totalnoofrooms;
    }

    public void setTotalnoofrooms(String totalnoofrooms) {
        this.totalnoofrooms = totalnoofrooms;
    }

    public String getRoomnoslotvalue() {
        return roomnoslotvalue;
    }

    public void setRoomnoslotvalue(String roomnoslotvalue) {
        this.roomnoslotvalue = roomnoslotvalue;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }
}
