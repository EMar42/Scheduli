package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.Instant;


@IgnoreExtraProperties
public class Appointment {
    private String providerUid;
    private String serviceName;
    private Instant start;
    private Instant end;

    public Appointment() {
    }

    public Appointment(String providerUid, String serviceName, Instant start, Instant end) {
        this.providerUid = providerUid;
        this.serviceName = serviceName;
        this.start = start;
        this.end = end;
    }

    public String getProviderUid() {
        return providerUid;
    }

    public void setProviderUid(String providerUid) {
        this.providerUid = providerUid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }
}
