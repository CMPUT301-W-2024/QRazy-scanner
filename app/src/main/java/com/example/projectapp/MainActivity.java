package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        // Load the WelcomeFragment initially
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //Toast.makeText(this, "Android ID: " + androidId, Toast.LENGTH_LONG).show();
        if (isSpecificDevice(androidId)) {
            // If it's the admin device, start the Admin activity
            Intent adminIntent = new Intent(this, Admin.class);
            startActivity(adminIntent);
            finish(); // Close MainActivity
        } else {
            // If it's not the admin device, load the WelcomeFragment
            loadWelcomeFragment();
        }

    }

    // Method to load the WelcomeFragment

    /**
     * Load the Fragment
     */
    private void loadWelcomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with WelcomeFragment
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        fragmentTransaction.replace(android.R.id.content, welcomeFragment);
        fragmentTransaction.commit();
    }

    /**
     * Check if it is admin's device
     * @param androidId Device's Id
     * @return true or false
     */
    private boolean isSpecificDevice(String androidId) {

        // Replace "SpecificAndroidID" with the actual Android ID of the admin device
        return androidId.equals("917cc646adc0623");
    }

}