package com.example.projectapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Activity to display events to attendees.
 * It provides functionality to view events the attendee is participating in, and to view all available events.
 * Attendees can interact with the events, such as signing up for them.
 */
public class AttendeePageActivity extends AppCompatActivity implements ProfileDeletedCallback{
    private ArrayList<Event> allEventsFiltered;
    private ArrayList<Event> attendeeEventsFiltered;
    private ArrayList<Event> allEventsFull;
    private ArrayList<Event> attendeeEventsFull;
    private String filter = "All";
    private AttendeeEventAdapter attendeeEventsAdapter;
    private AttendeeEventAdapter allEventsAdapter;
    private AnnouncementAdapter announcementAdapter;
    private ListenerRegistration attendeeEventsListener;
    private ListenerRegistration allEventsListener;
    private ArrayList<Announcement> announcements;
    private boolean enableListeners;
    private DataHandler dataHandler = DataHandler.getInstance();

    /**
     * Initializes the activity, sets up RecyclerViews for displaying events.
     *
     * @param savedInstanceState    If the activity is being re-initialized after
     *                              being shut down, this Bundle contains the data
     *                              most recently supplied in onSaveInstanceState.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        getNotificationPermission();

        dataHandler.addProfileDeletedListener(this);

        RecyclerView attendeeEventsList = findViewById(R.id.attendeeEventsList);
        RecyclerView allEventsList = findViewById(R.id.allEventsList);
        RecyclerView announcementsList = findViewById(R.id.announcementList);
        Button filterAllButton = findViewById(R.id.attendeeAllEvent);
        Button filterUpcomingButton = findViewById(R.id.attendeeUpcomingEvent);
        Button filterCompletedButton = findViewById(R.id.attendeeCompletedEvent);
        Button filterOngoingButton = findViewById(R.id.attendeeOngoingEvent);
        ImageButton menuButton = findViewById(R.id.menuButton);

        attendeeEventsFiltered = new ArrayList<>();
        allEventsFiltered = new ArrayList<>();
        attendeeEventsFull = new ArrayList<>();
        allEventsFull = new ArrayList<>();
        announcements = new ArrayList<>();

        attendeeEventsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        allEventsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        announcementsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        attendeeEventsAdapter = new AttendeeEventAdapter(attendeeEventsFiltered, new AttendeeEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                showDialogWithEventDetails(event, false);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AttendeePageActivity.this, ProfileEditActivity.class);

                startActivity(intent);
            }
        });

        allEventsAdapter = new AttendeeEventAdapter(allEventsFiltered, new AttendeeEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                showDialogWithEventDetails(event,true);
            }
        });

        announcementAdapter = new AnnouncementAdapter(announcements);

        attendeeEventsList.setAdapter(attendeeEventsAdapter);
        allEventsList.setAdapter(allEventsAdapter);
        announcementsList.setAdapter(announcementAdapter);

        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ScanActivity.class));
        });

        filterAllButton.setOnClickListener(v -> {
            filter = "All";
            filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
            filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
        });

        filterUpcomingButton.setOnClickListener(v -> {
            filter = "Upcoming";
            filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
            filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
        });

        filterCompletedButton.setOnClickListener(v -> {
            filter = "Complete";
            filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
            filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
        });

        filterOngoingButton.setOnClickListener(v -> {
            filter = "Ongoing";
            filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
            filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
        });

        Button promoQrCodeButton = findViewById(R.id.promoQrCodeButton);
        promoQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendeePageActivity.this, ScanActivity.class);
                intent.putExtra("usage", "promoQr");
                startActivity(intent);
            }
        });

        String eventId = getIntent().getStringExtra("EVENT_ID");
        if (eventId != null && !eventId.trim().isEmpty()) {
            fetchEventAndShowDetails(eventId);
        }


    }
    /**
     * Removes event listeners when the activity is paused
     *  to avoid unnecessary background processing.
     */
    @Override
    protected void onPause() {
        super.onPause();
        enableListeners = false;
        attendeeEventsListener.remove();
        allEventsListener.remove();
    }

    /**
     * Re-registers the event listeners when the activity
     *  resumes to keep the event lists up-to-date.
     */
    @Override
    protected void onResume() {
        super.onResume();
        enableListeners = true;
        addAttendeeEventsListener();
        addAllEventsListener();
    }

    public void addEvent(Event event, ArrayList<Event> list){
        if (!list.contains(event)){
            list.add(event);
        }
    }

    public void updateEvent(Event event, ArrayList<Event> list){
        list.set(list.indexOf(event), event);
    }

    public void removeEvent(Event event, ArrayList<Event> list){
        list.remove(event);
    }

    private void addAnnouncements(Event event){
        announcements.addAll(event.getAnnouncements());
        announcementAdapter.notifyDataSetChanged();
    }

    private void updateAnnouncements(Event event){
        for (int i=0; i < event.getAnnouncements().size(); i++){
            if (!announcements.contains(event.getAnnouncements().get(i))){
                announcements.add(event.getAnnouncements().get(i));
            }
        }
        announcementAdapter.notifyDataSetChanged();
    }

    private void removeAnnouncements(Event event){
        for (int i=0; i < event.getAnnouncements().size(); i++){
            announcements.remove(event.getAnnouncements().get(i));
        }
        announcementAdapter.notifyDataSetChanged();
    }

    private void addAttendeeEventsListener() {
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
                                addEvent(event, attendeeEventsFull);
                                filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
                                addAnnouncements(event);
                                break;
                            case MODIFIED:
                                updateEvent(event, attendeeEventsFull);
                                filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
                                updateAnnouncements(event);
                                break;
                            case REMOVED:
                                removeEvent(event, attendeeEventsFull);
                                filterEvents(attendeeEventsFiltered, attendeeEventsFull, attendeeEventsAdapter);
                                removeAnnouncements(event);
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
                                addEvent(event, allEventsFull);
                                filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
                                break;
                            case MODIFIED:
                                updateEvent(event, allEventsFull);
                                filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
                                break;
                            case REMOVED:
                                removeEvent(event, allEventsFull);
                                filterEvents(allEventsFiltered, allEventsFull, allEventsAdapter);
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


        TextView eventNameView = eventDetailDialog.findViewById(R.id.dialogEventName);
        TextView eventOrganizerView = eventDetailDialog.findViewById(R.id.dialogEventOrganizer);
        TextView eventDescriptionView = eventDetailDialog.findViewById(R.id.dialogEventDescription);
        TextView eventDateView = eventDetailDialog.findViewById(R.id.dialogEventDate);
        TextView eventTimeView = eventDetailDialog.findViewById(R.id.dialogEventTime);
        ImageView eventPosterView = eventDetailDialog.findViewById(R.id.dialogEventPoster);
        Button closeButton = eventDetailDialog.findViewById(R.id.dialogEventCloseButton);
        Button signUpButton = eventDetailDialog.findViewById(R.id.dialogEventSignButton);


        eventNameView.setText(event.getName());

        eventOrganizerView.setText(event.getOrganizerName());

        eventDescriptionView.setText(event.getDescription());

        eventPosterView.setImageBitmap(stringToBitmap(event.getPoster()));
        eventDateView.setText(event.getDate());
        eventTimeView.setText(event.getStartTime() + " - " + event.getEndTime());

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

    private void getNotificationPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            // permission is not already granted
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }

    private Bitmap stringToBitmap(String encodedString) {
        if (encodedString == null || encodedString.isEmpty()) {
            Log.e("Admin", "Encoded string is null or empty");
            return null;
        }
        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onProfileDeleted() {
        if (enableListeners){
            restart();
            unsubscribeFromNotis();
        }
    }

    private void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

    private void unsubscribeFromNotis(){
        for (Event event : attendeeEventsFiltered){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(event.getEventId());
        }
    }

    private void filterEvents(ArrayList<Event> events, ArrayList<Event> eventsFull, AttendeeEventAdapter adapter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentDateTime = new Date();
        events.clear();
        if (filter.equals("All")) {
            events.addAll(eventsFull);
        } else {
            for (Event event : eventsFull) {
                Date eventStartDateTime;
                Date eventEndDateTime;
                try {
                    eventStartDateTime = sdf.parse(event.getDate() + " " + event.getStartTime());
                    eventEndDateTime = sdf.parse(event.getDate() + " " + event.getEndTime());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (filter.equals("Upcoming") && currentDateTime.before(eventStartDateTime)) {
                    events.add(event);
                } else if (filter.equals("Complete") && currentDateTime.after(eventEndDateTime)) {
                    System.out.println("Got till here:  " + eventEndDateTime.toString());
                    events.add(event);
                } else if (filter.equals("Ongoing") && currentDateTime.after(eventStartDateTime) && currentDateTime.before(eventEndDateTime)){
                    events.add(event);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchEventAndShowDetails(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Event event = documentSnapshot.toObject(Event.class);
                    if (event != null) {
                        showDialogWithEventDetails(event, true);
                    }
                } else {
                    Toast.makeText(AttendeePageActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(AttendeePageActivity.this, "Error fetching event details", Toast.LENGTH_SHORT).show();
        });
    }
}

