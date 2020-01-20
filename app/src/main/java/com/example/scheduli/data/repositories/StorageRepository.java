package com.example.scheduli.data.repositories;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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


    //Upload file to user storage /user/uid/
    public void uploadBitmapImageToUSer(final String uid, final String imageName, Bitmap picture, final DataBaseCallBackOperation callBackOperation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final StorageReference userReference = this.scheduliStorage.getReference("user").child(uid).child(imageName + ".jpg");
        userReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG_STORAGE, "Failed to upload file to storage");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                callBackOperation.callBack(progress);
                Log.i(TAG_STORAGE, "Upload progress: " + progress);
            }
        });
    }
}
