package com.example.may.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.Model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by May on 4/18/2018.
 */

public class ImageHelper {

    public static byte[] bitmapToBytes (Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytesToBitmap (byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap readImageFromLocalStorage(String fileName) {

        try {
            File file = new File(MyApp.context.getFilesDir(), fileName);

            FileInputStream input = MyApp.context.openFileInput(fileName);

            byte[] bytes = new byte[(int) file.length()];
            input.read(bytes);

            return bytesToBitmap(bytes);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveImageToLocalStorage(String filename, Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FileOutputStream outputStream;

        try {
            outputStream = MyApp.context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
