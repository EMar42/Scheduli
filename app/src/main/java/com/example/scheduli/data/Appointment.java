package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Comparator;
import java.util.Date;


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

    public Appointment(Appointment other) {
        this(other.providerUid, other.serviceName, other.start, other.end);
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

    public static final Comparator<Appointment> BY_DATETIME_DESCENDING = new Comparator<Appointment>() {
        @Override
        public int compare(Appointment o1, Appointment o2) {
            Date first = new Date(o1.getStart());
            Date second = new Date(o2.getStart());

            return second.compareTo(first);
        }
    };

}
