package com.smile.hungry;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riccardo on 9/24/2016.
 */
public class FoodSource {
    private final long id;
    private final String name;
    @SerializedName("working_hours")
    private final List<WeeklyFreeFoodSlot> workingHours;
    private final double latitude;
    private final double longitude;
    private final String address;
    private final String description;
    private final String phone;

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

    public FoodSource(String name, double latitude, double longitude) {
        id = 1;
        this.name = "Pizza " + name;
        workingHours = new ArrayList<>();
        workingHours.add(new WeeklyFreeFoodSlot(43200, 50400, "Monday"));
        workingHours.add(new WeeklyFreeFoodSlot(43200, 50400, "Tuesday"));
        this.latitude = latitude;
        this.longitude = longitude;
        address = name + "'s house, San Jose (CA), 95134";
        description = "Best pizza in San Jose, special guest Janice every saturday night";
        phone = "+1 (234) 4566 763";
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
}
