package com.smile.generous;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riccardo on 9/24/2016.
 */
public class FoodSource {
    private long id;
    private String name;
    @SerializedName("working_hours")
    private List<WeeklyFreeFoodSlot> workingHours;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
    private String phone;

    public FoodSource(long id, String name, List<WeeklyFreeFoodSlot> workingHours, double latitude,
                      double longitude, String address, String description, String phone) {
        this.id = id;
        this.name = name;
        this.workingHours = workingHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.phone = phone;
    }

    public FoodSource() {
        workingHours = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<WeeklyFreeFoodSlot> getWorkingHours() {
        return workingHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkingHours(List<WeeklyFreeFoodSlot> workingHours) {
        this.workingHours = workingHours;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
