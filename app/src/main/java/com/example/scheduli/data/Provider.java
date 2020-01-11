package com.example.scheduli.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.UUID;

@IgnoreExtraProperties

public class Provider {

    //Provider details
    private String id;
    private String imageUrl;
    private String companyName;
    private String profession;
    private String phoneNumber;
    private ArrayList<Service> services;

    public Provider(String imageUrl, String companyName, String profession, String phoneNumber) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.services = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public Provider(String id, String imageUrl, String companyName, String profession, String phoneNumber) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.services = new ArrayList<>();
    }

    public Provider(String companyName, String profession) {

        this.companyName = companyName;
        this.profession = profession;
        this.services = new ArrayList<Service>();
        this.id = UUID.randomUUID().toString();

    }

    public Provider(String companyName, String profession, String phoneNumber) {
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.id = UUID.randomUUID().toString();

    }

    public Provider() {

    }

    public Provider(Provider provider) {

        this.companyName = provider.companyName;
        this.profession = provider.profession;
        this.imageUrl = provider.getImageUrl();
        this.phoneNumber = provider.phoneNumber;
        this.services = provider.services;
        this.id = provider.id;
    }

    public String getId() {
        return id;
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

    public String getImageUrl() {
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

}
