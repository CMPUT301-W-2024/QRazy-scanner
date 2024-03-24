package com.example.projectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * Activity to display events to attendees.
 * It provides functionality to view events the attendee is participating in, and to view all available events.
 * Attendees can interact with the events, such as signing up for them.
 */
public class AttendeePageActivity extends AppCompatActivity {
    private ArrayList<Event> allEvents;
    private ArrayList<Event> attendeeEvents;
    private AttendeeEventAdapter attendeeEventsAdapter;
    private AttendeeEventAdapter allEventsAdapter;
    private ListenerRegistration attendeeEventsListener;
    private ListenerRegistration allEventsListener;
    private DataHandler dataHandler = DataHandler.getInstance();
  
    /**
     * Initializes the activity, sets up RecyclerViews for displaying events.
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the data most recently supplied in onSaveInstanceState.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        getNotificationPermission();

        RecyclerView attendeeEventsList = findViewById(R.id.attendeeEventsList);
        RecyclerView allEventsList = findViewById(R.id.allEventsList);

        attendeeEvents = new ArrayList<>();
        allEvents = new ArrayList<>();

        attendeeEventsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        allEventsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        attendeeEventsAdapter = new AttendeeEventAdapter(attendeeEvents, new AttendeeEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                showDialogWithEventDetails(event, false);
            }
        });

        allEventsAdapter = new AttendeeEventAdapter(allEvents, new AttendeeEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                showDialogWithEventDetails(event,true);
            }
        });

        attendeeEventsList.setAdapter(attendeeEventsAdapter);
        allEventsList.setAdapter(allEventsAdapter);

        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ScanActivity.class));
        });

        addAnouncmentToScrollView();
    }
    /**
     * Removes event listeners when the activity is paused to avoid unnecessary background processing.
     */
    @Override
    protected void onPause() {
        super.onPause();
        attendeeEventsListener.remove();
        allEventsListener.remove();
    }

    /**
     * Re-registers the event listeners when the activity resumes to keep the event lists up-to-date.
     */
    @Override
    protected void onResume() {
        super.onResume();
        addAttendeeEventsListener();
        addAllEventsListener();
    }

    public void addEvent(Event event, ArrayList<Event> list, AttendeeEventAdapter adapter){
        if (!list.contains(event)){
            list.add(event);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateEvent(Event event, ArrayList<Event> list, AttendeeEventAdapter adapter){
        list.set(list.indexOf(event), event);
        adapter.notifyDataSetChanged();
    }

    public void removeEvent(Event event, ArrayList<Event> list, AttendeeEventAdapter adapter){
        list.remove(event);
        adapter.notifyDataSetChanged();
    }

    private void addAttendeeEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");
        attendeeEventsListener = eventsRef.whereArrayContains("signedAttendees", dataHandler.getAttendee().getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null) {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        switch (dc.getType()) {
                            case ADDED:
                                addEvent(event, attendeeEvents, attendeeEventsAdapter);
                                break;
                            case MODIFIED:
                                updateEvent(event, attendeeEvents, attendeeEventsAdapter);
                                break;
                            case REMOVED:
                                removeEvent(event, attendeeEvents, attendeeEventsAdapter);
                                break;
                        }
                    }
                }
            }
        });
    }

    private void addAllEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");
        allEventsListener = eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        switch (dc.getType()) {
                            case ADDED:
                                addEvent(event, allEvents, allEventsAdapter);
                                break;
                            case MODIFIED:
                                updateEvent(event, allEvents, allEventsAdapter);
                                break;
                            case REMOVED:
                                removeEvent(event, allEvents, allEventsAdapter);
                                break;
                        }
                    }
                }
            }
        });
    }
    /**
     * Displays a dialog with details of an event. Allows the user to view more information about the event.
     * If the event is available for sign-up, it displays a sign-up button.
     * @param event The Event object containing details to be displayed.
     * @param canSignUp Boolean to indicate if the sign-up option should be available for this event.
     */
    private void showDialogWithEventDetails(Event event, boolean canSignUp) {
        Dialog eventDetailDialog = new Dialog(this);
        eventDetailDialog.setContentView(R.layout.event_dialog);

        TextView eventNameView = eventDetailDialog.findViewById(R.id.dialog_event_name);
        TextView eventOrganizerView = eventDetailDialog.findViewById(R.id.dialog_event_organizer);
        TextView eventDescriptionView = eventDetailDialog.findViewById(R.id.dialog_event_description);
        Button closeButton = eventDetailDialog.findViewById(R.id.dialog_event_close_button);
        Button signUpButton = eventDetailDialog.findViewById(R.id.dialog_event_sign_button);

        eventNameView.setText(event.getName());
        eventOrganizerView.setText(event.getOrganizer());
        eventDescriptionView.setText(event.getDescription());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetailDialog.dismiss();
            }
        });

        if (canSignUp){
            signUpButton.setVisibility(View.VISIBLE);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // only add if attendance limit is not reached
                    if (event.getAttendanceLimit() == 0 || event.getSignedAttendees().size() < event.getAttendanceLimit()){
                        event.addSignedAttendee(dataHandler.getAttendee().getAttendeeId());
                        dataHandler.getAttendee().addSignedEvent(event.getEventId());
                        FirebaseMessaging.getInstance().subscribeToTopic(event.getEventId());
                        eventDetailDialog.dismiss();
                    }
                    else {
                        Toast.makeText(AttendeePageActivity.this, "Event has reached attendance limit", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        eventDetailDialog.show();
    }

    /**
     * Adds announcment to scroll view, currently hard coded since we haven't added the functionality for organizer to push announcments
     */
    private void addAnouncmentToScrollView() {
        View anouncmentView = LayoutInflater.from(this).inflate(R.layout.announcement_scroll_view, findViewById(R.id.announcementView), false);
        TextView announcment = anouncmentView.findViewById(R.id.announcement_content);
        TextView announcer = anouncmentView.findViewById(R.id.announcer);
        announcment.setText("This is an announcment");
        announcer.setText("Announcer");
        ScrollView announcmentView = findViewById(R.id.announcementView);
        announcmentView.addView(anouncmentView);
    }

    private void getNotificationPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            // permission is not already granted
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }
}