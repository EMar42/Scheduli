package com.example.scheduli.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@IgnoreExtraProperties
public class Appointment implements Parcelable {
    private String providerUid;
    private String serviceName;
    private String serviceCost;
    private long start;
    private long end;
    private long alarmReminderTime;

    public Appointment() {
    }

    public Appointment(String providerUid, String serviceName, String serviceCost, long start, long end, long alarmReminderTime) {
        this.providerUid = providerUid;
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
        this.start = start;
        this.end = end;
        this.alarmReminderTime = alarmReminderTime;
    }

    public Appointment(String providerUid, String serviceName, String serviceCost, long start, long end) {
        this(providerUid, serviceName, serviceCost, start, end, 0);
    }

    public Appointment(Appointment other) {
        this(other.providerUid, other.serviceName, other.serviceCost, other.start, other.end);
    }

    protected Appointment(Parcel in) {
        providerUid = in.readString();
        serviceName = in.readString();
        serviceCost = in.readString();
        start = in.readLong();
        end = in.readLong();
        alarmReminderTime = in.readLong();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

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

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
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

    public long getAlarmReminderTime() {
        return alarmReminderTime;
    }

    public void setAlarmReminderTime(long alarmReminderTime) {
        this.alarmReminderTime = alarmReminderTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerUid);
        dest.writeString(serviceName);
        dest.writeString(serviceCost);
        dest.writeLong(start);
        dest.writeLong(end);
        dest.writeLong(alarmReminderTime);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("providerUid", providerUid);
        map.put("serviceName", serviceName);
        map.put("serviceCost", serviceCost);
        map.put("start", start);
        map.put("end", end);
        map.put("alarmReminderTime", alarmReminderTime);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return getStart() == that.getStart() &&
                getEnd() == that.getEnd() &&
                getProviderUid().equals(that.getProviderUid()) &&
                getServiceName().equals(that.getServiceName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProviderUid(), getServiceName(), getServiceCost(), getStart(), getEnd());
    }
}
