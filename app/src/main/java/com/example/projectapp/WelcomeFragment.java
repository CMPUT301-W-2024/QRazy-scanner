package com.example.projectapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

        // Initialize button on the welcome screen
        Button joinEventButton = rootView.findViewById(R.id.joinEventButton);
        Button createEventButton = rootView.findViewById(R.id.createEventButton);
        Button promoQrButton = rootView.findViewById(R.id.promoQrCodeButton);

        joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User has logged in before, get their info
                if (getAttendeeId() != null) {
                    db.collection("attendees").document(getAttendeeId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Attendee attendee = document.toObject(Attendee.class);
                                    dataHandler.setAttendee(attendee);
                                    Intent intent = new Intent(getActivity(), AttendeePageActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "Your account was deleted you will need to create a new account", Toast.LENGTH_SHORT).show();
                                    ProfileFragment profileFragment = new ProfileFragment();
                                    requireActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(android.R.id.content, profileFragment)
                                            // Use android.R.id.content as the container
                                            .addToBackStack(null)
                                            .commit();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Couldn't retrieve account", Toast.LENGTH_SHORT).show();
                            }
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

        promoQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScanActivity
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
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
