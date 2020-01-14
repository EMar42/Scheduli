package com.example.scheduli.data;

public class WorkDay {
    private long startTime;
    private long endTime;

    public WorkDay(long start, long end) {
        this.startTime = start;
        this.endTime = end;
    }

    public WorkDay() {
    }

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
}
