package com.aminought.bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class Bitmap {
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static android.graphics.Bitmap decodeSampledBitmapFromResource(String image, int reqWidth, int reqHeight) {
        int rotationInDegrees = 0;
        try {
            ExifInterface exif = new ExifInterface(image);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if(rotation == ExifInterface.ORIENTATION_ROTATE_90) rotationInDegrees = 90;
            if(rotation == ExifInterface.ORIENTATION_ROTATE_180) rotationInDegrees = 180;
            if(rotation == ExifInterface.ORIENTATION_ROTATE_270) rotationInDegrees = 270;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        android.graphics.Bitmap bitmap =  BitmapFactory.decodeFile(image, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationInDegrees);
        return android.graphics.Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
