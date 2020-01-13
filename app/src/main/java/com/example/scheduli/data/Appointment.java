package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Appointment {
    private String providerUid;
    private String serviceName;
    private long start;
    private long end;

    public Appointment() {
    }

    public Appointment(String providerUid, String serviceName, long start, long end) {
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

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
