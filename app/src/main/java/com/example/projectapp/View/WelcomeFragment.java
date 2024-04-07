package com.example.projectapp.View;


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

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Controller.GetAttendeeCallback;
import com.example.projectapp.Controller.GetOrganizerCallback;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.R;

/**
 * The WelcomeFragment class provides the initial welcome screen for the application.
 * It presents buttons for users to either join an event (as an attendee) or create a new event (as an organizer).
 */
public class WelcomeFragment extends Fragment implements GetOrganizerCallback, GetAttendeeCallback {
    private final DataHandler dataHandler = DataHandler.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.welcome_fragment, container, false);


        // Initialize button on the welcome screen
        Button joinEventButton = rootView.findViewById(R.id.joinEventButton);
        Button createEventButton = rootView.findViewById(R.id.createEventButton);

        // If 'Join Event' button is clicked
        joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User has logged in before, get their info
                if (getLocalAttendeeId() != null) {
                    dataHandler.getAttendee(getLocalAttendeeId(), WelcomeFragment.this);
                }
                else {
                    Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                    startActivity(intent);
                }
            }
        });

        // If 'Create Event' button was clicked
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User has logged in before, get their info
                if (getLocalOrganizerId() != null) {
                    dataHandler.getOrganizer(getLocalOrganizerId(), WelcomeFragment.this);
                }
                else {
                    Intent intent = new Intent(getActivity(), OrganizerPageActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onGetOrganizer(Organizer organizer) {
        if (organizer != null){
            dataHandler.setLocalOrganizer(organizer);
            Intent intent = new Intent(getActivity(), OrganizerPageActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getActivity(), "Couldn't access firebase", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetAttendee(Attendee attendee, boolean deleted) {
        if (attendee != null && !deleted){
            dataHandler.setLocalAttendee(attendee);
            Intent intent = new Intent(getActivity(), AttendeePageActivity.class);
            startActivity(intent);
        }
        else if (deleted){
            Toast.makeText(getActivity(), "Your account was deleted you will need to create a new account", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getActivity(), "Couldn't access firebase", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets attendee's ID.
     * @return      The ID of the attendee as a String,
     *              or null if no ID is found.
     */
    public String getLocalAttendeeId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("AttendeePref", Context.MODE_PRIVATE);
        return prefs.getString("attendeeId", null);
    }

    /**
     * Gets organizer's ID.
     * @return      The ID of the organizer as a String,
     *              or null if no ID is found.
     */
    public String getLocalOrganizerId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        return prefs.getString("organizerId", null);
    }

}
