package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Appointment {
    private String userUid;
    private Service service;
    private Provider provider;
    private long scheduledTo; // using long for date object since it the best option to store and work with

    public Appointment() {
    }

    public Appointment(String userUid, Service service, Provider provider, long scheduledTo) {
        this.userUid = userUid;
        this.service = service;
        this.provider = provider;
        this.scheduledTo = scheduledTo;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public long getScheduledTo() {
        return scheduledTo;
    }

    public void setScheduledTo(long scheduledTo) {
        this.scheduledTo = scheduledTo;
    }
}
