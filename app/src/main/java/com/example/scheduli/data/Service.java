package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@IgnoreExtraProperties
public class Service {

    private String name;
    private float cost;
    private int singleSessionInMinutes;
    private Map<String, WorkDay> workingDays; // key is of type DayOfWeek enum
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).

    public Service() {
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

    public int getSingleSessionInMinutes() {
        return singleSessionInMinutes;
    }

    public void setSingleSessionInMinutes(int singleSessionInMinutes) {
        this.singleSessionInMinutes = singleSessionInMinutes;
    }

    public Map<String, WorkDay> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Map<String, WorkDay> workingDays) {
        this.workingDays = workingDays;
    }

    public Map<String, ArrayList<Sessions>> getDailySessions() {
        return dailySessions;
    }

    public void setDailySessions(Map<String, ArrayList<Sessions>> dailySessions) {
        this.dailySessions = dailySessions;
    }

    //Getting map values:
    public Set getSetOfMap(){

        Set set = this.workingDays.entrySet();

        //************************ use to print Map values: ****************************//
//        Iterator itr=set.iterator();
//        while(itr.hasNext()){
//            //Converting to Map.Entry so that we can get key and value separately
//            Map.Entry entry=(Map.Entry)itr.next();
//            System.out.println("key:" entry.getKey()+" value: "+entry.getValue());
//        }
        //****************************************************************************//


        return set;

    }
}
