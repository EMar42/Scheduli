package com.example.scheduli.data;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderDataRepository {
    private static final String TAG_USER_REPOSITORY = "Provider repository";
    private static ProviderDataRepository instance;
    private final DatabaseReference dataBaseReference;

    public static ProviderDataRepository getInstance() {
        if (instance == null) {
            synchronized (ProviderDataRepository.class) {
                if (instance == null) {
                    instance = new ProviderDataRepository();
                }
            }
        }
        return instance;
    }

    private ProviderDataRepository() {
        this.dataBaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewProviderInApp(String uid, Provider provider) {
        Log.i(TAG_USER_REPOSITORY, "Created new Provider " + provider);
        dataBaseReference.child("providers").child(uid).setValue(provider);
    }





}
