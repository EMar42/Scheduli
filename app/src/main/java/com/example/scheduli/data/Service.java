package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class Service {

    private String name;
    private float cost;
    private Map<String, Meeting> workingDays; // key is of type DayOfWeek enum

    public Service() {
    }

    public Service(String name, float cost, Map<String, Meeting> workingDays) {
        this.name = name;
        this.cost = cost;
        this.workingDays = workingDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Map<String, Meeting> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Map<String, Meeting> workingDays) {
        this.workingDays = workingDays;
    }
}
