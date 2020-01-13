package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Provider {

    //TODO fix class

    //Provider details
    private String imageUrl;
    private String companyName;
    private String profession;
    private String phoneNumber;
    private String address;
    private ArrayList<Service> services;

    public Provider(String imageUrl, String companyName, String profession, String phoneNumber, String address, ArrayList<Service> servicesList) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.services = new ArrayList<>();

        //TODO: get a services list

    }

    public Provider(String imageUrl, String companyName, String profession, String phoneNumber, String address) {
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.address = address;

    }

    public Provider(String companyName, String profession) {

        this.companyName = companyName;
        this.profession = profession;
        this.services = new ArrayList<Service>();

    }

    public Provider(String companyName, String profession, String phoneNumber) {
        this.companyName = companyName;
        this.profession = profession;
        this.phoneNumber = phoneNumber;

    }

    public Provider() {

    }

    public Provider(Provider provider) {

        this.companyName = provider.companyName;
        this.profession = provider.profession;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
