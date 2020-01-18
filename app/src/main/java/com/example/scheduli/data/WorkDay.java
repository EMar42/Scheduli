package com.example.scheduli.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkDay implements Parcelable {
    private long startTime;
    private long endTime;

    public WorkDay(long start, long end) {
        this.startTime = start;
        this.endTime = end;
    }

    public WorkDay() {
    }

    protected WorkDay(Parcel in) {
        startTime = in.readLong();
        endTime = in.readLong();
    }

    public static final Creator<WorkDay> CREATOR = new Creator<WorkDay>() {
        @Override
        public WorkDay createFromParcel(Parcel in) {
            return new WorkDay(in);
        }

        @Override
        public WorkDay[] newArray(int size) {
            return new WorkDay[size];
        }
    };

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
        dest.writeLong(endTime);
    }
}
