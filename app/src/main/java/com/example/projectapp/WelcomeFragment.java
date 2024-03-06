package com.example.projectapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.welcome_fragment, container, false);

        // Find the "Join Event" button
        Button joinEventButton = rootView.findViewById(R.id.joinEventButton);
        Button createEventButton = rootView.findViewById(R.id.createEventButton);
        joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User has logged in before, get their info
                if (getAttendeeId() != null){
                    db.collection("attendees").document(getAttendeeId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            MainActivity.setAttendee(documentSnapshot.toObject(Attendee.class));
                        }
                    });

                    Intent i = new Intent(getActivity(), ScanActivity.class);
                    startActivity(i);
                }
                else {

                    MainActivity.setAttendee(new Attendee());
                    saveAttendeeId();
                    db.collection("attendees").document(MainActivity.getAttendee().getAttendeeId()).set(MainActivity.getAttendee());

                    ProfileFragment profileFragment = new ProfileFragment();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(android.R.id.content, profileFragment)
                            // Use android.R.id.content as the container
                            .addToBackStack(null)
                            .commit();
                }
                // Create and navigate to the ScanFragment
/*                ScanFragment scanFragment = new ScanFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, scanFragment) // Use android.R.id.content as the container
                        .addToBackStack(null) // Optional: Add to back stack for navigation
                        .commit();*/
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User has logged in before, get their info
                if (getOrganizerId() != null) {
                    db.collection("organizers").document(getOrganizerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            MainActivity.setOrganizer(documentSnapshot.toObject(Organizer.class));
                        }
                    });
                } else {

                    MainActivity.setOrganizer(new Organizer());
                    saveOrganizerId();
                    db.collection("organizers").document(getOrganizerId()).set(MainActivity.getOrganizer());
                }

                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
/*=======
        // "Create Event" button
        Button createEventButton = rootView.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
>>>>>>> e8a8cc9b92e3ff3bae27612236861a158523ab3f*/
            }
        });

        return rootView;
    }


    public String getAttendeeId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("AttendeePref", Context.MODE_PRIVATE);
        return prefs.getString("attendeeId", null);
    }

    public void saveAttendeeId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("AttendeePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("attendeeId", MainActivity.getAttendee().getAttendeeId());
        editor.apply();
    }

    public String getOrganizerId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        return prefs.getString("organizerId", null);
    }

    public void saveOrganizerId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("organizerId", MainActivity.getOrganizer().getOrganizerId());
        editor.apply();
    }



}
