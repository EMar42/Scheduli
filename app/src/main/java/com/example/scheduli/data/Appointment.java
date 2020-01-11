package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Appointment {
    private String userUid;
    private String providerUid;

    private String serviceName;
    private long scheduledTo;

    public Appointment() {
    }

    public Appointment(String userUid, String service, String provider, long scheduledTo) {
        this.userUid = userUid;
        this.serviceName = service;
        this.providerUid = provider;
        this.scheduledTo = scheduledTo;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getService() {
        return serviceName;
    }

    public void setService(String service) {
        this.serviceName = service;
    }

    public String getProvider() {
        return providerUid;
    }

    public void setProvider(String provider) {
        this.providerUid = provider;
    }

    public long getScheduledTo() {
        return scheduledTo;
    }

    public void setScheduledTo(long scheduledTo) {
        this.scheduledTo = scheduledTo;
    }

}
