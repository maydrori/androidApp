package com.example.may.myapplication.dal.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.may.myapplication.dal.Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by May on 4/4/2018.
 */

public class ModelFirebase {

    private static ModelFirebase instance = new ModelFirebase();

    public static ModelFirebase getInstance() {return instance;}

    public WorkshopsFirebase workshops;
    public WorkshopsMembersFirebase workshopsMembers;
    public UsersFirebase users;
    StorageReference imagesStorage;

    public ModelFirebase() {
        FirebaseDatabase fbInstance = FirebaseDatabase.getInstance();
        workshops = new WorkshopsFirebase(fbInstance.getReference("workshops"));
        workshopsMembers = new WorkshopsMembersFirebase(fbInstance.getReference("workshopsMembers"));
        users = new UsersFirebase(fbInstance.getReference("users"));

        imagesStorage = FirebaseStorage.getInstance().getReference().child("images"); ;
    }

    public void saveImage(final Bitmap imageBitmap, final String name, final Model.SaveImageListener listener) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesStorage.child(name).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }

    public void getImage(String name, final Model.GetImageListener listener) {

        final long ONE_MEGABYTE = 1024 * 1024;
        imagesStorage.child(name).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listener.onSuccess(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                listener.onFail();
            }
        });
    }

    public interface GetDataListener<T> {
        void onComplete(T data);
    }
}
