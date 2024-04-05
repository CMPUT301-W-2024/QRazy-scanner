package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.google.firebase.firestore.DocumentChange;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Activity responsible for displaying lists of attendees for an Event.
 * Handles separating attendees into lists based on check-in status and
 * sync with Firebase data.
 */
public class EventAttendeesActivity extends AppCompatActivity implements EventAttendeesListenerCallback{

    private ArrayList<Attendee> checkedInAttendees;
    private ArrayList<Attendee> signedUpAttendees;
    private EventAttendeeAdapter checkedInAttendeeAdapter;
    private EventAttendeeAdapter signedUpAttendeeAdapter;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendees_actvity);

        event = (Event) getIntent().getSerializableExtra("EVENT");
        DataHandler.getInstance().addEventAttendeesListener(event, true, this); // for checked in attendees
        DataHandler.getInstance().addEventAttendeesListener(event, false, this); // for signed up attendees

        RecyclerView checkedInAttendeesListView = findViewById(R.id.checkedInAttendeesList);
        RecyclerView signedUpAttendeesListView = findViewById(R.id.signedUpAttendeesList);

        checkedInAttendeesListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        signedUpAttendeesListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        checkedInAttendees = new ArrayList<>();
        signedUpAttendees = new ArrayList<>();

        checkedInAttendeeAdapter = new EventAttendeeAdapter(checkedInAttendees, event, null);
        signedUpAttendeeAdapter = new EventAttendeeAdapter(signedUpAttendees, event, null);

        checkedInAttendeesListView.setAdapter(checkedInAttendeeAdapter);
        signedUpAttendeesListView.setAdapter(signedUpAttendeeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Adds a new Attendee to the specified list and updates the adapter.
     * Prevents duplicates based on attendeeId.
     * @param attendee The Attendee to add.
     * @param list The list to modify.
     * @param adapter The adapter to notify of changes.
     */
    public void addAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        if (!list.contains(attendee)){
            list.add(attendee);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Updates an existing Attendee in the specified list and updates the adapter.
     * @param attendee The updated Attendee.
     * @param list The list to modify.
     * @param adapter The adapter to notify of changes.
     */
    public void updateAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        if (list.contains(attendee)){
            list.set(list.indexOf(attendee), attendee);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Removes an Attendee from the specified list and updates the adapter.
     * @param attendee The Attendee to remove.
     * @param list The list to modify.
     * @param adapter The adapter to notify of changes.
     */
    public void removeAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        list.remove(attendee);
    }

    @Override
    public void onEventCheckedAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee) {
        switch (updateType) {
            case ADDED:
                addAttendee(attendee, checkedInAttendees, checkedInAttendeeAdapter);
                break;
            case MODIFIED:
                updateAttendee(attendee, checkedInAttendees, checkedInAttendeeAdapter);
                break;
            case REMOVED:
                removeAttendee(attendee, checkedInAttendees, checkedInAttendeeAdapter);
                break;
        }
    }

    @Override
    public void onEventSignedAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee) {
        switch (updateType) {
            case ADDED:
                addAttendee(attendee, signedUpAttendees, signedUpAttendeeAdapter);
                break;
            case MODIFIED:
                updateAttendee(attendee, signedUpAttendees, signedUpAttendeeAdapter);
                break;
            case REMOVED:
                removeAttendee(attendee, signedUpAttendees, signedUpAttendeeAdapter);
                break;
        }
    }
}