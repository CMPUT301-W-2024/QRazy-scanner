package com.example.projectapp;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Activity for an organizer to manage their events. Provides action to set an
 * organizer name, create new events, and displays a list of existing events.
 * Sync with Firebase for real-time updates.
 */
public class OrganizerPageActivity extends AppCompatActivity {

    private EditText organizerNameEditText;
    private DataHandler dataHandler;
    private ArrayList<Event> events;
    private OrganizerEventAdapter eventAdapter;
    private ListenerRegistration organizerEventsListener;
    private ArrayList<Integer> mileStones;

    /**
     * Initializes the activity and sets up UI and event listeners.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_page);

        dataHandler = DataHandler.getInstance();
        organizerNameEditText = findViewById(R.id.organizerNameEditText);

        if (dataHandler.getOrganizer() != null){
            organizerNameEditText.setText(dataHandler.getOrganizer().getName());
            organizerNameEditText.setFocusable(false);
            organizerNameEditText.setCursorVisible(false);
        }

        mileStones = new ArrayList<>();
        mileStones.add(5);
        mileStones.add(10);
        mileStones.add(20);

        RecyclerView recyclerView = findViewById(R.id.eventListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button createNewEventButton = findViewById(R.id.createNewEventButton);

        events = new ArrayList<>();
        eventAdapter = new OrganizerEventAdapter(events, this);
        recyclerView.setAdapter(eventAdapter);

        createNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataHandler.getOrganizer() == null){
                    Organizer organizer = new Organizer(organizerNameEditText.getText().toString().trim());
                    dataHandler.setOrganizer(organizer);
                    saveOrganizerId();
                    dataHandler.addOrganizer(organizer);
                }
                Intent intent = new Intent(OrganizerPageActivity.this, CreateNewEventActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Adds an event listener if the organizer is not null.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (dataHandler.getOrganizer() != null){
            addOrganizerEventsListener();
        }
    }

    /**
     * Removes the event listener if it's not null.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (organizerEventsListener != null){
            organizerEventsListener.remove();
        }
    }

    /**
     * Saves the organizer's ID
     */
    public void saveOrganizerId(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("organizerId", dataHandler.getOrganizer().getOrganizerId());
        editor.apply();
    }

    /**
     * Adds a new Event to the UI and updates the RecyclerView's adapter.
     * Prevents duplicates based on event ID.
     *
     * @param event The Event object to add.
     */
    public void addEvent(Event event){
        if (!events.contains(event)){
            events.add(event);
            eventAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Updates an existing Event in the UI and updates the RecyclerView's adapter.
     * Checks if the updated attendance count reached a milestone.
     *
     * @param event The updated Event object.
     */
    public void updateEvent(Event event){
        events.set(events.indexOf(event), event);
        checkMilestone(event);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Removes an Event from the UI list and updates the RecyclerView's adapter.
     *
     * @param event The Event object to remove.
     */
    public void removeEvent(Event event){
        events.remove(event);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Checks if an Event's attendance count has reached one of the predefined milestones.
     * Displays a Toast message if a milestone has been reached.
     *
     * @param event The Event to check.
     */
    private void checkMilestone(Event event){
        if (mileStones.contains(event.getAttendance())){
            mileStones.remove(event.getAttendance());
            Toast.makeText(OrganizerPageActivity.this, "Milestone Reached!! " + event.getAttendance() + " attendees in " + event.getName(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets up a listener for changes to the events organized by the current organizer.
     * The listener is triggered when an event is added, modified, or removed.
     */
    private void addOrganizerEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        organizerEventsListener = eventsRef.whereEqualTo("organizerId", dataHandler.getOrganizer().getOrganizerId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    Event event = dc.getDocument().toObject(Event.class);
                    switch (dc.getType()) {
                        case ADDED:
                            addEvent(event);
                            break;
                        case MODIFIED:
                            updateEvent(event);
                            break;
                        case REMOVED:
                            removeEvent(event);
                            break;
                    }
                }
            }
        });
    }
}


