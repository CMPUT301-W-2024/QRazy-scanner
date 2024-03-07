package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static Attendee attendee;
    private static Organizer organizer;
    private FirebaseFirestore db;
    private DataHandler dataHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the WelcomeFragment initially
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Toast.makeText(this, "Android ID: " + androidId, Toast.LENGTH_LONG).show();
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
        return androidId.equals("d2d85ef1c954de7b");
    }


    /**
     * Get the Attendee
     * @return the Attendee
     */
    public static Attendee getAttendee() {
        return attendee;
    }

    /**
     * Set the attendee
     * @param attendee an attendee
     */
    public static void setAttendee(Attendee attendee) {
        MainActivity.attendee = attendee;
    }

    /**
     * Get the organizer
     * @return the organizer
     */
    public static Organizer getOrganizer() {return organizer;}

    /**
     * Set the organizer
     * @param organizer a organizer
     */
    public static void setOrganizer(Organizer organizer) {
        MainActivity.organizer = organizer;
    }

}