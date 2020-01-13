package com.example.scheduli.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.Instant;

@IgnoreExtraProperties
public class Sessions {
    //TODO implement class

    private Instant start;
    private Instant end;
    private String userUid;
    private boolean isAvailable;
}
