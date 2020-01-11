package com.example.scheduli.data;

import android.util.Log;

import com.example.scheduli.utils.UsersUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class ProviderDataRepository {
    private static final String TAG_PROVIDER_REPOSITORY = "Provider repository";
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
        this.dataBaseReference = FirebaseDatabase.getInstance().getReference("providers");
    }

    public void createNewProviderInApp(String uid, Provider provider) {
        Log.i(TAG_PROVIDER_REPOSITORY, "Created new Provider " + provider);
        uid = UsersUtils.getInstance().getCurrentUserUid();
        //TODO: check if the provider is exist
        dataBaseReference.child(uid).setValue(provider);
    }



}
