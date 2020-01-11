package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.Duration;

@IgnoreExtraProperties
public class Meeting {
    private Duration meetingDuration;
    private Duration workingHours;

    public Meeting(Duration meetingDuration, Duration workingHours) {
        this.meetingDuration = meetingDuration;
        this.workingHours = workingHours;
    }

    public Meeting() {
    }

    public java.time.Duration getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(Duration meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public Duration getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Duration workingHours) {
        this.workingHours = workingHours;
    }
}
