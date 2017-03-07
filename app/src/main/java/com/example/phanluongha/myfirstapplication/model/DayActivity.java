package com.example.phanluongha.myfirstapplication.model;

import android.app.*;

import java.util.ArrayList;

/**
 * Created by haphan on 3/7/2017.
 */
public class DayActivity {
    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<Activity> activities = new ArrayList<Activity>();
}
