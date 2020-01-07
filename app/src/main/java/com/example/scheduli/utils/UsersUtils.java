package com.example.scheduli.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Utility class for FireBase authentication
 * Use this if your activity require something from the app authentication
 */
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

    public void logout(){
        firebaseAuth.signOut();
    }
}
