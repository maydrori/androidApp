package com.example.may.myapplication.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.may.myapplication.dal.Model;

/**
 * Created by May on 4/18/2018.
 */

public class ImageHelper {

    public static void loadImageToView(final String url, String userId, final ImageView imageView) {

        if (url != null) {
            imageView.setTag(url);

            Model.instance().getImage("image-" + userId, new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {

//                            if (progressBarFinishLoading.getVisibility() == View.VISIBLE) progressBarFinishLoading.setVisibility(View.INVISIBLE);

                    if (imageView.getTag().equals(url)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFail() {

                    // TODO : alert
                }
            });
        }
    }
}
