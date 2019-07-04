package com.htn.dovanan.mabuuchinh.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadImageBitmap {

    /*
    tinh toan kich thuoc ty le so voi anh goc
    truyen vao: option, rong, dai
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        /*
            Kich thuoc rong va cao cua anh goc, cho do scale anh goc  = 1
         */
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        /*
        kich thuoc anh that len hon anh mong muon
         */
        if (height > reqHeight || width > reqWidth) {
            // chia do kich thuoc
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // tinh ra ty le chia inSampleSize anh mong muon voi anh goc
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
        thuoc tinh inJustDecodeBounds de kiem tra kich thuoc
         */
        options.inJustDecodeBounds = true;
//        decode buc anh goc
        BitmapFactory.decodeResource(res, resId, options);
//        tinh toan ti le so voi anh goc
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
//        decode anh da set
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
