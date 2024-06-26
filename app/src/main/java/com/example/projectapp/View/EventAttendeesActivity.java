package com.example.projectapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Controller.EventAttendeesListenerCallback;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.R;
import com.google.firebase.firestore.DocumentChange;
import java.util.ArrayList;

/**
 * Activity responsible for displaying lists of attendees for an Event.
 * Handles separating attendees into lists based on check-in status and
 * sync with Firebase data.
 */
public class EventAttendeesActivity extends AppCompatActivity implements EventAttendeesListenerCallback {

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

    /**
     * call super class
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * call super class
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Adds a new Attendee to the specified list and updates the adapter.
     *
     * Prevents duplicates based on attendeeId.
     * @param attendee  The Attendee to add.
     * @param list      The list to modify.
     * @param adapter   The adapter to notify of changes.
     */
    public void addAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        if (!list.contains(attendee)){
            list.add(attendee);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Updates an existing Attendee in the specified list and updates the adapter.
     *
     * @param attendee  The updated Attendee.
     * @param list      The list to modify.
     * @param adapter   The adapter to notify of changes.
     */
    public void updateAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        if (list.contains(attendee)){
            list.set(list.indexOf(attendee), attendee);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Removes an Attendee from the specified list and updates the adapter.
     *
     * @param attendee  The Attendee to remove.
     * @param list      The list to modify.
     * @param adapter   The adapter to notify of changes.
     */
    public void removeAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        list.remove(attendee);
        adapter.notifyDataSetChanged();
    }

    /**
     * Responds to updates in the list of attendees who have checked in.
     * It handles the addition, modification, or removal of an attendee's check-in status.
     *
     * @param updateType The type of update (ADDED, MODIFIED, REMOVED).
     * @param attendee   The attendee whose check-in status has changed.
     */
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

    /**
     * Responds to updates in the list of attendees who have signed up for an event.
     * It manages the addition, modification, or removal of an attendee's sign-up status.
     *
     * @param updateType The type of update (ADDED, MODIFIED, REMOVED).
     * @param attendee   The attendee whose sign-up status has changed.
     */
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