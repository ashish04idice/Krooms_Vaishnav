package com.krooms.hostel.rental.property.app.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by user on 2/1/2016.
 */
public class PropertyModal implements Parcelable {

    public String property_id;
    public String property_name;
    public String property_address;
    public String property_area;
    public String property_pincode;
    public String property_city;
    public String property_state;
    public String capacity_hosteler;
    public String vacancy_hosteler;
    public String no_of_rooms;
    public String property_for_whom;
    public String property_phone;
    public String property_type_id;
    public String owner_id;
    public String user_id;
    public LatLng property_latlng;
    public String property_rent_price;
    public String property_video_url = "NA";
    public String property_featuresStr;
    public String property_aminitiesStr;
    public String property_distance_railway;
    public String property_distance_busstand;
    public String property_distance_airport;
    public String property_nearest_square;
    public String property_nearest_square_distance;
    public String property_tenant_type;
    public String property_curfew_time;
    public String property_allow_no_vehicle;
    public String property_is_allowed_smoking;
    public String property_is_allowed_drinking;
    public String property_no_of_tenant;
    public String property_advance_rentAmount;
    public String property_maintenance_rentAmount;
    public String property_water_bill_rentAmount;
    public String property_other_expance_rentAmount;
    public String property_Electricity_rentAmount;
    public String property_Water_Supplay;
    public String property_List;
    public String property_Power_Backup;
    public String property_furnish;
    public String property_kitchen;
    public String property_kitchen_type;
    public String property_landmark;
    public String property_totalarea;
    public String super_builtup_area;
    public String property_face;
    public String property_description;
    public String colonyname;


    public static PropertyModal propertyModal;

    public PropertyModal(Parcel in) {
        property_id = in.readString();
        colonyname=in.readString();
        property_name = in.readString();
        property_address = in.readString();
        property_area = in.readString();
        property_pincode = in.readString();
        property_city = in.readString();
        property_state = in.readString();
        capacity_hosteler = in.readString();
        vacancy_hosteler = in.readString();
        no_of_rooms = in.readString();
        property_for_whom = in.readString();
        property_phone = in.readString();
        property_type_id = in.readString();
        owner_id = in.readString();
        user_id = in.readString();
        property_latlng = in.readParcelable(LatLng.class.getClassLoader());
        property_rent_price = in.readString();
        property_video_url = in.readString();
        property_featuresStr = in.readString();
        property_aminitiesStr = in.readString();
        property_distance_railway = in.readString();
        property_distance_busstand = in.readString();
        property_distance_airport = in.readString();
        property_nearest_square = in.readString();
        property_nearest_square_distance = in.readString();
        property_tenant_type = in.readString();
        property_curfew_time = in.readString();
        property_allow_no_vehicle = in.readString();
        property_is_allowed_smoking = in.readString();
        property_is_allowed_drinking = in.readString();
        property_no_of_tenant = in.readString();
        property_advance_rentAmount = in.readString();
        property_maintenance_rentAmount = in.readString();
        property_water_bill_rentAmount = in.readString();
        property_other_expance_rentAmount = in.readString();
        property_Electricity_rentAmount = in.readString();
        property_Water_Supplay = in.readString();
        property_List = in.readString();
        property_Power_Backup = in.readString();
        property_furnish = in.readString();
        property_kitchen = in.readString();
        property_kitchen_type = in.readString();
        property_landmark = in.readString();
        property_totalarea = in.readString();
        super_builtup_area = in.readString();
        property_face = in.readString();
        property_description = in.readString();
        searchResult_type = in.readInt();
        propertyImage = in.createTypedArrayList(PropertyPhotoModal.CREATOR);
    }

    public String getColonyname() {
        return colonyname;
    }

    public void setColonyname(String colonyname) {
        this.colonyname = colonyname;
    }

    public static final Creator<PropertyModal> CREATOR = new Creator<PropertyModal>() {
        @Override
        public PropertyModal createFromParcel(Parcel in) {
            return new PropertyModal(in);
        }

        @Override
        public PropertyModal[] newArray(int size) {
            return new PropertyModal[size];
        }
    };

    public static PropertyModal getInstance() {
        if (propertyModal == null) {
            propertyModal = new PropertyModal(Parcel.obtain());
        }
        return propertyModal;
    }

    private int searchResult_type = 0;


    private ArrayList<RoomModal> roomList = new ArrayList<>();
    private ArrayList<PropertyPhotoModal> propertyImage = new ArrayList<>();


    //////////////////////////// Setter method /////////////////////////////////
    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public void setProperty_address(String property_address) {
        this.property_address = property_address;
    }

    public void setProperty_area(String property_area) {
        this.property_area = property_area;
    }

    public void setProperty_city(String property_city) {
        this.property_city = property_city;
    }

    public void setProperty_state(String property_state) {
        this.property_state = property_state;
    }

    public void setCapacity_hosteler(String capacity_hosteler) {
        this.capacity_hosteler = capacity_hosteler;
    }

    public void setVacancy_hosteler(String vacancy_hosteler) {
        this.vacancy_hosteler = vacancy_hosteler;
    }

    public void setNo_of_rooms(String no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }

    public void setProperty_phone(String property_phone) {
        this.property_phone = property_phone;
    }

    public void setProperty_type_id(String property_type_id) {
        this.property_type_id = property_type_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setProperty_latlng(LatLng property_latlng) {
        this.property_latlng = property_latlng;
    }

    public void setProperty_rent_price(String property_rent_price) {
        this.property_rent_price = property_rent_price;
    }

    public void setRoomList(ArrayList<RoomModal> roomList) {
        this.roomList = roomList;
    }

    public void setPropertyImage(ArrayList<PropertyPhotoModal> propertyImage) {
        this.propertyImage = propertyImage;
    }

    public void setProperty_video_url(String property_video_url) {
        this.property_video_url = property_video_url;
    }

    public void setProperty_featuresStr(String property_featuresStr) {
        this.property_featuresStr = property_featuresStr;
    }

    public void setProperty_aminitiesStr(String property_aminitiesStr) {
        this.property_aminitiesStr = property_aminitiesStr;
    }

    public void setProperty_totalarea(String property_totalarea) {
        this.property_totalarea = property_totalarea;
    }




    public void setSearchResult_type(int searchResult_type) {
        this.searchResult_type = searchResult_type;
    }

    public void setProperty_advance_rentAmount(String property_advance_rentAmount) {
        this.property_advance_rentAmount = property_advance_rentAmount;
    }

    public void setProperty_allow_no_vehicle(String property_allow_no_vehicle) {
        this.property_allow_no_vehicle = property_allow_no_vehicle;
    }

    public void setProperty_curfew_time(String property_curfew_time) {
        this.property_curfew_time = property_curfew_time;
    }

    public void setProperty_distance_airport(String property_distance_airport) {
        this.property_distance_airport = property_distance_airport;
    }

    public void setProperty_distance_busstand(String property_distance_busstand) {
        this.property_distance_busstand = property_distance_busstand;
    }

    public void setProperty_distance_railway(String property_distance_railway) {
        this.property_distance_railway = property_distance_railway;
    }

    public void setProperty_Electricity_rentAmount(String property_Electricity_rentAmount) {
        this.property_Electricity_rentAmount = property_Electricity_rentAmount;
    }

    public void setProperty_Water_Supplay(String property_Water_Supplay) {
        this.property_Water_Supplay = property_Water_Supplay;
    }

    public void setProperty_Power_Backup(String property_Power_Backup) {
        this.property_Power_Backup = property_Power_Backup;
    }

    public void setProperty_List(String property_List) {
        this.property_List = property_List;
    }



    public void setProperty_is_allowed_drinking(String property_is_allowed_drinking) {
        this.property_is_allowed_drinking = property_is_allowed_drinking;
    }

    public void setProperty_is_allowed_smoking(String property_is_allowed_smoking) {
        this.property_is_allowed_smoking = property_is_allowed_smoking;
    }

    public void setProperty_maintenance_rentAmount(String property_maintenance_rentAmount) {
        this.property_maintenance_rentAmount = property_maintenance_rentAmount;
    }

    public void setProperty_nearest_square(String property_nearest_square) {
        this.property_nearest_square = property_nearest_square;
    }

    public void setProperty_nearest_square_distance(String property_nearest_square_distance) {
        this.property_nearest_square_distance = property_nearest_square_distance;
    }

    public void setProperty_no_of_tenant(String property_no_of_tenant) {
        this.property_no_of_tenant = property_no_of_tenant;
    }

    public void setProperty_other_expance_rentAmount(String property_other_expance_rentAmount) {
        this.property_other_expance_rentAmount = property_other_expance_rentAmount;
    }

    public void setProperty_tenant_type(String property_tenant_type) {
        this.property_tenant_type = property_tenant_type;
    }

    public void setProperty_water_bill_rentAmount(String property_water_bill_rentAmount) {
        this.property_water_bill_rentAmount = property_water_bill_rentAmount;
    }

    public void setProperty_landmark(String property_landmark) {
        this.property_landmark = property_landmark;
    }


    public void setProperty_for_whom(String property_for_whom) {
        this.property_for_whom = property_for_whom;
    }

    public void setProperty_pincode(String property_pincode) {
        this.property_pincode = property_pincode;
    }

    public void setProperty_furnish(String property_furnish) {
        this.property_furnish = property_furnish;
    }

    public void setProperty_Description(String property_description) {
        this.property_description = property_description;
    }



    public void setProperty_kitchen(String property_kitchen) {
        this.property_kitchen = property_kitchen;
    }

    public void setProperty_kitchen_type(String property_kitchen_type) {
        this.property_kitchen_type = property_kitchen_type;
    }

    public void setProperty_Super_builtup_area(String super_builtup_area) {
        this.super_builtup_area = super_builtup_area;
    }

    public void setProperty_face(String property_face) {
        this.property_face = property_face;
    }

    /////////////////////////////// getter methods ////////////////////////////////////

    public String getProperty_id() {
        return property_id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public String getProperty_address() {
        return property_address;
    }


    public String getProperty_area() {
        return property_area;
    }

    public String getProperty_city() {
        return property_city;
    }

    public String getProperty_state() {
        return property_state;
    }

    public String getCapacity_hosteler() {
        return capacity_hosteler;
    }

    public String getNo_of_rooms() {
        return no_of_rooms;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getProperty_phone() {
        return property_phone;
    }

    public String getProperty_type_id() {
        return property_type_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getVacancy_hosteler() {
        return vacancy_hosteler;
    }

    public LatLng getProperty_latlng() {
        return property_latlng;
    }

    public String getProperty_rent_price() {
        return property_rent_price;
    }

    public ArrayList<RoomModal> getRoomList() {
        return roomList;
    }

    public ArrayList<PropertyPhotoModal> getPropertyImage() {
        return propertyImage;
    }

    public String getProperty_totalarea() {
        return property_totalarea;
    }

    public String getProperty_video_url() {
        return property_video_url;
    }

    public String getProperty_featuresStr() {
        return property_featuresStr;
    }

    public String getProperty_aminitiesStr() {
        return property_aminitiesStr;
    }

    public int getSearchResult_type() {
        return searchResult_type;
    }

    public String getProperty_advance_rentAmount() {
        return property_advance_rentAmount;
    }

    public String getProperty_allow_no_vehicle() {
        return property_allow_no_vehicle;
    }

    public String getProperty_curfew_time() {
        return property_curfew_time;
    }

    public String getProperty_distance_airport() {
        return property_distance_airport;
    }

    public String getProperty_distance_busstand() {
        return property_distance_busstand;
    }

    public String getProperty_distance_railway() {
        return property_distance_railway;
    }

    public String getProperty_Electricity_rentAmount() {
        return property_Electricity_rentAmount;
    }

    public String getProperty_is_allowed_drinking() {
        return property_is_allowed_drinking;
    }

    public String getProperty_is_allowed_smoking() {
        return property_is_allowed_smoking;
    }

    public String getProperty_maintenance_rentAmount() {
        return property_maintenance_rentAmount;
    }

    public String getProperty_nearest_square() {
        return property_nearest_square;
    }

    public String getProperty_nearest_square_distance() {
        return property_nearest_square_distance;
    }

    public String getProperty_no_of_tenant() {
        return property_no_of_tenant;
    }

    public String getProperty_other_expance_rentAmount() {
        return property_other_expance_rentAmount;
    }

    public String getProperty_tenant_type() {
        return property_tenant_type;
    }

    public String getProperty_water_bill_rentAmount() {
        return property_water_bill_rentAmount;
    }

    public String getProperty_for_whom() {
        return property_for_whom;
    }

    public String getProperty_pincode() {
        return property_pincode;
    }

    public String getProperty_List() {
        return property_List;
    }

    public String getProperty_Water_Supplay() {
        return property_Water_Supplay;
    }

    public String getProperty_Power_Backup() {
        return property_Power_Backup;
    }

    public static PropertyModal getPropertyModal() {
        return propertyModal;
    }

    public String getProperty_furnish() {
        return property_furnish;
    }

    public String getProperty_kitchen() {
        return property_kitchen;
    }

    public String getProperty_kitchen_type() {
        return property_kitchen_type;
    }

    public String getProperty_landmark() {
        return property_landmark;
    }

    public String getProperty_Super_builtup_area() {
        return super_builtup_area;
    }

    public String getProperty_face() {
        return property_face;
    }

    public String getProperty_description() {
        return property_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(property_id);
        dest.writeString(property_name);
        dest.writeString(property_address);
        dest.writeString(property_area);
        dest.writeString(property_pincode);
        dest.writeString(property_city);
        dest.writeString(property_state);
        dest.writeString(capacity_hosteler);
        dest.writeString(vacancy_hosteler);
        dest.writeString(no_of_rooms);
        dest.writeString(property_for_whom);
        dest.writeString(property_phone);
        dest.writeString(property_type_id);
        dest.writeString(owner_id);
        dest.writeString(user_id);
        dest.writeParcelable(property_latlng, flags);
        dest.writeString(property_rent_price);
        dest.writeString(property_video_url);
        dest.writeString(property_featuresStr);
        dest.writeString(property_aminitiesStr);
        dest.writeString(property_distance_railway);
        dest.writeString(property_distance_busstand);
        dest.writeString(property_distance_airport);
        dest.writeString(property_nearest_square);
        dest.writeString(property_nearest_square_distance);
        dest.writeString(property_tenant_type);
        dest.writeString(property_curfew_time);
        dest.writeString(property_allow_no_vehicle);
        dest.writeString(property_is_allowed_smoking);
        dest.writeString(property_is_allowed_drinking);
        dest.writeString(property_no_of_tenant);
        dest.writeString(property_advance_rentAmount);
        dest.writeString(property_maintenance_rentAmount);
        dest.writeString(property_water_bill_rentAmount);
        dest.writeString(property_other_expance_rentAmount);
        dest.writeString(property_Electricity_rentAmount);
        dest.writeString(property_Water_Supplay);
        dest.writeString(property_List);
        dest.writeString(property_Power_Backup);
        dest.writeString(property_furnish);
        dest.writeString(property_kitchen);
        dest.writeString(property_kitchen_type);
        dest.writeString(property_landmark);
        dest.writeString(property_totalarea);
        dest.writeString(super_builtup_area);
        dest.writeString(property_face);
        dest.writeString(property_description);
        dest.writeInt(searchResult_type);
        dest.writeTypedList(propertyImage);
    }
}
