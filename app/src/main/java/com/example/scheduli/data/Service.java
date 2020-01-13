package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

@IgnoreExtraProperties
public class Service {

    private String name;
    private float cost;
    private int singleSessionInMinutes;
    private Map<String, Duration> workingDays; // key is of type DayOfWeek enum
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).

    public Service() {
    }

}
