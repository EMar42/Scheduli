package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Sessions {
    //TODO implement class

    private long start;
    private long end;
    private String userUid;
    private boolean isAvailable;

    public Sessions() {
    }

    public Sessions(long start, long end, String userUid, boolean isAvailable) {
        this.start = start;
        this.end = end;
        this.userUid = userUid;
        this.isAvailable = isAvailable;
    }

    public Sessions(long start, long end, boolean isAvailable) {
        this.start = start;
        this.end = end;
        this.isAvailable = isAvailable;

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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
