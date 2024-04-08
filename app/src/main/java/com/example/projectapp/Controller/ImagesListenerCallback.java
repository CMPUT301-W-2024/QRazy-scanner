package com.example.projectapp.Controller;

import android.widget.LinearLayout;

import java.util.HashMap;

/**
 * An interface defining a callback mechanism to signal updates related to images
 * associated with documents within a Firestore collection.
 */
public interface ImagesListenerCallback {
    void onImagesUpdated(HashMap<String, String> images, LinearLayout layout, String collection, String field); // maps document to image
}
