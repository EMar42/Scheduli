package com.example.scheduli.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Sessions implements Parcelable {
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

    public Sessions(Sessions s){
        this.start = s.getStart();
        this.end = s.getEnd();
        this.isAvailable = s.isAvailable();
        this.userUid = s.getUserUid();
    }

    protected Sessions(Parcel in) {
        start = in.readLong();
        end = in.readLong();
        userUid = in.readString();
        isAvailable = in.readByte() != 0;
    }

    public static final Creator<Sessions> CREATOR = new Creator<Sessions>() {
        @Override
        public Sessions createFromParcel(Parcel in) {
            return new Sessions(in);
        }

        @Override
        public Sessions[] newArray(int size) {
            return new Sessions[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(start);
        dest.writeLong(end);
        dest.writeString(userUid);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
    }
}
