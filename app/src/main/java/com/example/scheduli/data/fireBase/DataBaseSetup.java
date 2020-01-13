package com.example.scheduli.data.fireBase;

import com.google.firebase.database.FirebaseDatabase;

public class DataBaseSetup {
    public static void setDatabasePersistentOn() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static void setDataBasePersistentOff() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }


}
