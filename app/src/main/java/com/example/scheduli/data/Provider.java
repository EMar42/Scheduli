package com.example.scheduli.data;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.DialogTitle;

import com.example.scheduli.ui.login.LoginActivity;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@IgnoreExtraProperties
public class Provider {

     private static final String SERVICE_TAG = "Provider activity";

    //Provider details
    private String imageUrl;
    private String companyName;
    private String profession;
    private String phoneNumber;
    private String address;
    private ArrayList<Service> services;


    //Constructor without initializing of services List (for existed list)
    public Provider(String imageUrl, String companyName, String profession, String phoneNumber, String address, ArrayList<Service> servicesList) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.services = servicesList; //get initialized services list

    }


    //This constructor should be used for creating new Provider:
    public Provider(String imageUrl, String companyName, String profession, String phoneNumber, String address) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.services = new ArrayList<>();

    }


    public Provider() {

    }

    public Provider(Provider provider) {

        this.companyName = provider.companyName;
        this.profession = provider.profession;
        this.address = provider.address;
        this.imageUrl = provider.getImageUrl();
        this.phoneNumber = provider.phoneNumber;
        this.services = provider.services;
    }


    public String getCompanyName() {
        return companyName;
    }

    public String getProfession() {
        return profession;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() { // TODO: get Image from DB
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean addService(String name, float cost, int singleSessionInMinutes, final String dayOfWeek, final String date, final Long workStart , final long workEnd, long start, long end, String userUid, boolean isAvailable) { //define time

        int day = Integer.valueOf(dayOfWeek);
        if(day < 0 || day >= 7) {

            //Create a new working days map:
            final WorkDay workDay = new WorkDay(workStart,workEnd);
            Map<String, WorkDay> tempWorkingDayMap = new HashMap<String, WorkDay>() {{ // work
                put(dayOfWeek, workDay);
            }};


            //Create a new dailySessions map:
            //  new Array<Sessions>:
            final ArrayList<Sessions> tempSessionsArray = new ArrayList<>();
            tempSessionsArray.add(new Sessions(start, end, userUid, isAvailable));

            Map<String, ArrayList<Sessions>> tempDailySessionsMap = new HashMap<String, ArrayList<Sessions>>() {{
                put(date, tempSessionsArray);
            }};


            //Create a new Service:
            Service service = new Service(name, cost, singleSessionInMinutes, tempWorkingDayMap, tempDailySessionsMap);

            this.services.add(service);
            Log.d(SERVICE_TAG, "Service Added: " + name);
            return true; //Service Added Succesfully

        }
        else {
            Log.e(SERVICE_TAG,"Failed to add a new service");
            return false;
        }

    }

}
