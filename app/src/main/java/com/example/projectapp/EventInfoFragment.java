package com.example.projectapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * This fragment displays information about an event from the perspective of an attendee.
 */
public class EventInfoFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize any data or setup specific to this fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_info_attendee_fragment, container, false);


        // Continue button click listener
        Button continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            AttendeeEventsFragment attendeeEventsFragment = new AttendeeEventsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, attendeeEventsFragment) // Use android.R.id.content as the container
                    .addToBackStack(null) // Optional: Add to back stack for navigation
                    .commit();
        });

        // Go Back button click listener
        Button goBackButton = view.findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate(); // Navigate back to the previous fragment
            }
        });


        return view;
    }
}
