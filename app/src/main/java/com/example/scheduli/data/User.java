package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String userName;
    private String fullName;
    private String email;
    //TODO Might need to add appointments to here in the future.

    public User() {
    }

    public User(String userName, String fullName, String email) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
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
}
