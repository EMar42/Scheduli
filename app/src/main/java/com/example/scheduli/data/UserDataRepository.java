package com.example.scheduli.data;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDataRepository {
    private static final String TAG_USER_REPOSITORY = "User repository";
    private static UserDataRepository instance;
    private final DatabaseReference dataBaseReference;

    public static UserDataRepository getInstance() {
        if (instance == null) {
            synchronized (UserDataRepository.class) {
                if (instance == null) {
                    instance = new UserDataRepository();
                }
            }
        }
        return instance;
    }

    private UserDataRepository() {
        this.dataBaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewUserInApp(String uid, User user) {
        Log.i(TAG_USER_REPOSITORY, "Created new User " + user);
        dataBaseReference.child("users").child(uid).setValue(user);
    }


}
