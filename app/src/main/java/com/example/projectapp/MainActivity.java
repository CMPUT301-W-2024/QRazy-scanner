package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

<<<<<<< HEAD
=======
    private static Attendee attendee;
    private static Organizer organizer;


>>>>>>> 196777651a74db3aad87dfa2a8928aa65ff6ae0c
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the WelcomeFragment initially
        loadWelcomeFragment();
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