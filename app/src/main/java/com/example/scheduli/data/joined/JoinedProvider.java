package com.example.scheduli.data.joined;

import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;

import java.util.ArrayList;

public class JoinedProvider {

    private String imageUrl;
    private String companyName;
    private String profession;
    private String phoneNumber;
    private String address;
    private ArrayList<Service> services;
    private String pid;


    public JoinedProvider() {
    }

    public JoinedProvider(Provider provider) {
        this.companyName = provider.getCompanyName();
        this.phoneNumber = provider.getPhoneNumber();
        this.profession = provider.getProfession();
        this.imageUrl = provider.getImageUrl();
        this.address = provider.getAddress();
        this.services = provider.getServices();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
