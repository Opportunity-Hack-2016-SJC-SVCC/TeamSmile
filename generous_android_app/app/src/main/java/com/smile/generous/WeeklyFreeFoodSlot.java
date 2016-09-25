package com.smile.generous;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Riccardo on 9/24/2016.
 */
public class WeeklyFreeFoodSlot {
    @SerializedName("start_time")
    private int startTime;
    @SerializedName("end_time")
    private int endTime;
    private String day;

    public WeeklyFreeFoodSlot(int startTime, int endTime, String day) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public WeeklyFreeFoodSlot() {
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getDay() {
        return day;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
