package com.example.projectapp;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentChange;
import java.util.ArrayList;

/**
 * Activity for an organizer to manage their events. Provides action to set an
 * organizer name, create new events, and displays a list of existing events.
 * Sync with Firebase for real-time updates.
 */
public class OrganizerPageActivity extends AppCompatActivity implements OrganizerEventsListenerCallback {

    private EditText organizerNameEditText;
    private final DataHandler dataHandler = DataHandler.getInstance();
    private ArrayList<Event> events;
    private OrganizerEventAdapter eventAdapter;
    private ArrayList<Integer> mileStones;

    /**
     * Initializes the activity and sets up UI and event listeners.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_page);

        if (dataHandler.getOrganizer() != null){
            dataHandler.addOrganizerEventsListener(this);
        }

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
    }

    /**
     * Removes the event listener if it's not null.
     */
    @Override
    protected void onPause() {
        super.onPause();
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
     * @param event
     *      The Event object to add.
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
     * @param event
     *      The updated Event object.
     */
    public void updateEvent(Event event){
        events.set(events.indexOf(event), event);
        checkMilestone(event);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Removes an Event from the UI list and updates the RecyclerView's adapter.
     *
     * @param event
     *      The Event object to remove.
     */
    public void removeEvent(Event event){
        events.remove(event);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Checks if an Event's attendance count has reached one of the predefined milestones.
     * Displays a Toast message if a milestone has been reached.
     *
     * @param event
     *      The Event to check.
     */
    private void checkMilestone(Event event){
        if (mileStones.contains(event.getAttendance())){
            mileStones.remove(event.getAttendance());
            Toast.makeText(OrganizerPageActivity.this, "Milestone Reached!! " + event.getAttendance() + " attendees in " + event.getName(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Callback for changes to organizer events.
     * Handles the addition, modification, or removal of an event.
     *
     * @param updateType The type of update that occurred (ADDED, MODIFIED, REMOVED).
     * @param event      The event that was changed.
     */
    @Override
    public void onOrganizerEventsUpdated(DocumentChange.Type updateType, Event event) {
        switch (updateType) {
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


