package com.smile.hungry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Riccardo on 9/24/2016.
 */
public class WeeklyFreeFoodSlot {
    @SerializedName("start_time")
    private final int startTime;
    @SerializedName("end_time")
    private final int endTime;
    private final String day;

    public WeeklyFreeFoodSlot(int startTime, int endTime, String day) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
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
}
