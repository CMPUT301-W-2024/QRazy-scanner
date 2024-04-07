package com.example.projectapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Singleton class used to convert images bitmap to strings and vice versa
 */

public class ImageHandler {
    private static ImageHandler instance;

    /**
     * Empty constructor
     */
    private ImageHandler(){}

    /**
     * Retrieves the singleton instance of ImageHandler.
     *
     * @return
     *      The singleton instance of DataHandler.
     */
    public static ImageHandler getInstance(){
        if (instance == null){
            instance = new ImageHandler();
        }
        return instance;
    }

    /**
     * Converts a bitmap image into a Base64 encoded string representation.
     *
     * @param bitmap
     *      The bitmap to be encoded.
     * @return
     *      Base64 encoded string of the bitmap.
     */
    public String bitmapToString(Bitmap bitmap) {
        int maxSize = 3072; // Maximum dimension (width or height) for the resized bitmap
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Check if resizing is needed
        if (width > maxSize || height > maxSize) {
            float scale = Math.min(((float) maxSize) / width, ((float) maxSize) / height);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);

            // Rotate the resized bitmap by 90 degrees (adjust as needed)
            matrix.postRotate(90); // You can change the rotation angle here

            Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);

            // Convert the rotated bitmap to a Base64 encoded string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int quality = 100; // Initial quality
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            while (baos.size() > 1024 * 1024) { // 1 MiB in bytes
                baos.reset(); // Reset the stream
                quality -= 10; // Reduce quality by 10 each time
                if (quality <= 0) {
                    break; // Exit loop if quality reaches 0
                }
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }

            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            // No resizing needed, directly convert the original bitmap to a Base64 encoded string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }

    /**
     * Converts a Base64 encoded string to a Bitmap object.
     *
     * @param encodedString
     *      The Base64 encoded string representing the bitmap.
     * @return
     *      The decoded Bitmap object, or null if the input string is null or empty.
     */
    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}