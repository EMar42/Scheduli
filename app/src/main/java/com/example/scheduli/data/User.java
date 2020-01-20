package com.example.scheduli.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Parcelable {
    private String userName;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String profileImage;
    private ArrayList<Appointment> appointments;

    public User() {
    }

    public User(String userName, String fullName, String phoneNumber, String email, String profileImage) {
        this.userName = userName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profileImage = profileImage;
    }

    protected User(Parcel in) {
        userName = in.readString();
        fullName = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        profileImage = in.readString();
        appointments = in.createTypedArrayList(Appointment.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(fullName);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(profileImage);
        dest.writeTypedList(appointments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("fullName", fullName);
        map.put("email", email);
        map.put("profileImage", profileImage);
        map.put("phoneNumber", phoneNumber);
        if (appointments != null) {
            HashMap<String, Appointment> appointmentHashMap = new HashMap<>();
            for (int i = 0; i < appointments.size(); i++) {
                appointmentHashMap.put(Integer.toString(i), appointments.get(i));
            }

            map.put("appointments", appointmentHashMap);
        }

        return map;
    }
}
