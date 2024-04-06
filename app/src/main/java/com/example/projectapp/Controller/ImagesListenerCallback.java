package com.example.projectapp.Controller;

import android.widget.LinearLayout;

import java.util.HashMap;

public interface ImagesListenerCallback {
    void onImagesUpdated(HashMap<String, String> images, LinearLayout layout, String collection, String field); // maps document to image
}
