package com.example.scheduli.data.joined;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.scheduli.data.Appointment;

import java.util.Comparator;
import java.util.Date;

public class JoinedAppointment implements Parcelable {
    private Appointment appointment;
    private String providerImageUrl;
    private String providerCompanyName;
    private String providerProfession;
    private String providerPhoneNumber;
    private String providerAddress;

    public JoinedAppointment(Appointment appointment, String providerImageUrl, String providerCompanyName,
                             String providerProfession, String providerPhoneNumber, String providerAddress) {
        this.appointment = appointment;
        this.providerImageUrl = providerImageUrl;
        this.providerCompanyName = providerCompanyName;
        this.providerProfession = providerProfession;
        this.providerPhoneNumber = providerPhoneNumber;
        this.providerAddress = providerAddress;
    }

    public JoinedAppointment() {
    }

    protected JoinedAppointment(Parcel in) {
        appointment = in.readParcelable(Appointment.class.getClassLoader());
        providerImageUrl = in.readString();
        providerCompanyName = in.readString();
        providerProfession = in.readString();
        providerPhoneNumber = in.readString();
        providerAddress = in.readString();
    }

    public static final Creator<JoinedAppointment> CREATOR = new Creator<JoinedAppointment>() {
        @Override
        public JoinedAppointment createFromParcel(Parcel in) {
            return new JoinedAppointment(in);
        }

        @Override
        public JoinedAppointment[] newArray(int size) {
            return new JoinedAppointment[size];
        }
    };

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getProviderImageUrl() {
        return providerImageUrl;
    }

    public void setProviderImageUrl(String providerImageUrl) {
        this.providerImageUrl = providerImageUrl;
    }

    public String getProviderCompanyName() {
        return providerCompanyName;
    }

    public void setProviderCompanyName(String providerCompanyName) {
        this.providerCompanyName = providerCompanyName;
    }

    public String getProviderProfession() {
        return providerProfession;
    }

    public void setProviderProfession(String providerProfession) {
        this.providerProfession = providerProfession;
    }

    public String getProviderPhoneNumber() {
        return providerPhoneNumber;
    }

    public void setProviderPhoneNumber(String providerPhoneNumber) {
        this.providerPhoneNumber = providerPhoneNumber;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public static final Comparator<JoinedAppointment> BY_DATETIME_DESCENDING = new Comparator<JoinedAppointment>() {
        @Override
        public int compare(JoinedAppointment o1, JoinedAppointment o2) {
            Date first = new Date(o1.getAppointment().getStart());
            Date second = new Date(o2.getAppointment().getStart());

            return second.compareTo(first);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(appointment, flags);
        dest.writeString(providerImageUrl);
        dest.writeString(providerCompanyName);
        dest.writeString(providerProfession);
        dest.writeString(providerPhoneNumber);
        dest.writeString(providerAddress);
    }
}
