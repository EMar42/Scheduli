package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {
    private String userName;
    private String fullName;
    private String email;
    private Provider provider; //TODO remove
    private ArrayList<Appointment> appointments;

    public User() {
    }


    public User(String userName, String fullName, String email, Provider provider, ArrayList<Appointment> appointments) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.provider = provider;
        this.appointments = appointments;
    }

    public User(String userName, String fullName, String email) {
        this(userName, fullName, email, null, new ArrayList<Appointment>());
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointmentToUser(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void removeAppointmentFromUser(Appointment appointment) {
        this.appointments.remove(appointment);
    }
}
