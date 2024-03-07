package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private static Attendee attendee;
    private static Organizer organizer;


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
    private void loadWelcomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with WelcomeFragment
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        fragmentTransaction.replace(android.R.id.content, welcomeFragment);
        fragmentTransaction.commit();
    }

    private boolean isSpecificDevice(String androidId) {

        // Replace "SpecificAndroidID" with the actual Android ID of the admin device
        return androidId.equals("d2d85ef1c954de7b");
    }


    public static Attendee getAttendee() {
        return attendee;
    }

    public static void setAttendee(Attendee attendee) {
        MainActivity.attendee = attendee;
    }

    public static Organizer getOrganizer() {return organizer;}

    public static void setOrganizer(Organizer organizer) {
        MainActivity.organizer = organizer;
    }

}