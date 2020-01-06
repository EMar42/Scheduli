package com.example.scheduli.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersUtils {
    private FirebaseAuth firebaseAuth;

    private static UsersUtils instance;
    public static UsersUtils getInstance(){
        if(instance == null){
            synchronized (UsersUtils.class){
                if(instance == null){
                    instance = new UsersUtils();
                }
            }
        }
        return instance;
    }

    private UsersUtils(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFireBaseAuth(){
        return firebaseAuth;
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

}
