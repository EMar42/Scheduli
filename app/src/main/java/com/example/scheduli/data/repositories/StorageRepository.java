package com.example.scheduli.data.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.scheduli.data.User;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class StorageRepository {

    private static final String TAG_STORAGE = "Storage repository";
    private static StorageRepository instance;
    private final FirebaseStorage scheduliStorage;

    public static StorageRepository getInstance() {
        if (instance == null) {
            synchronized (StorageRepository.class) {
                if (instance == null) {
                    instance = new StorageRepository();
                }
            }
        }
        return instance;
    }

    private StorageRepository() {
        scheduliStorage = FirebaseStorage.getInstance();
    }


    //Upload file to user storage /user/uid/filename
    // Just pass the uid of the user, the string of the file name with the type "myFile.jpg", and the bitmap of the picture (this can be taken from BitmapFactory)
    public void uploadBitmapImageToFireBase(final String uid, final String fileNameWithPostFix, Bitmap picture) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference userReference = this.scheduliStorage.getReference("user").child(uid).child(fileNameWithPostFix);
        UploadTask uploadTask = userReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG_STORAGE, "Successful image upload to storage");

                UserDataRepository.getInstance().getUserFromUid(uid, new DataBaseCallBackOperation() {
                    @Override
                    public void callBack(Object object) {
                        User user = (User) object;
                        user.setProfileImage(fileNameWithPostFix);
                        UserDataRepository.getInstance().updateUserProfile(uid, user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG_STORAGE, "Failed to upload file to storage");
            }
        });

    }

    //Give pictureName and user id to download from
    // pass the uid of the user, give the file name + type "myFile.jpg", pass call back action to get the Bitmap of the picture
    public void downloadImageFromStorage(String uid, String pictureName, final DataBaseCallBackOperation callBackOperation) {
        StorageReference userImageReference = this.scheduliStorage.getReference("user/" + uid + "/" + pictureName);
        final long FOUR_MEGABYTE = 1024 * 1024 * 4;
        try {
            userImageReference.getBytes(FOUR_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    callBackOperation.callBack(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG_STORAGE, "Failed to load provided image from storage");
                }
            });
        } catch (Exception e) {
            Log.e(TAG_STORAGE, e.getMessage());
        }
    }
}
