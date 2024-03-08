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
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;

public class WelcomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DataHandler dataHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.welcome_fragment, container, false);
        dataHandler = DataHandler.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setLocalCacheSettings(MemoryCacheSettings.newBuilder().build()).build();
        db.setFirestoreSettings(settings);

        // Find the "Join Event" button
        Button joinEventButton = rootView.findViewById(R.id.joinEventButton);
        Button createEventButton = rootView.findViewById(R.id.createEventButton);
        joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User has logged in before, get their info
                if (getAttendeeId() != null) {
                    db.collection("attendees").document(getAttendeeId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Attendee attendee = documentSnapshot.toObject(Attendee.class);
                            dataHandler.setAttendee(attendee);
                            Intent intent = new Intent(getActivity(), AttendeePageActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else {
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
                            Organizer organizer = documentSnapshot.toObject(Organizer.class);
                            dataHandler.setOrganizer(organizer);
                            Intent intent = new Intent(getActivity(), OrganizerPageActivity.class);
                            startActivity(intent);
                        }
                    });
                }else {
                    Intent intent = new Intent(getActivity(), OrganizerPageActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }


    /**
     * get attendee id
     * @return
     */
    public String getAttendeeId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("AttendeePref", Context.MODE_PRIVATE);
        return prefs.getString("attendeeId", null);
    }

    /**
     * get organizer's ID
     * @return ORGANIZER'S id
     */
    public String getOrganizerId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        return prefs.getString("organizerId", null);
    }



}
