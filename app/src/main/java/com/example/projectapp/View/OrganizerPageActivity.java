package com.example.projectapp.View;



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

import com.example.projectapp.Controller.AddOrganizerCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.Controller.OrganizerEventsListenerCallback;
import com.example.projectapp.R;
import com.google.firebase.firestore.DocumentChange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Activity for an organizer to manage their events. Provides action to set an
 * organizer name, create new events, and displays a list of existing events.
 * Sync with Firebase for real-time updates.
 */
public class OrganizerPageActivity extends AppCompatActivity implements OrganizerEventsListenerCallback, AddOrganizerCallback {

    private EditText organizerNameEditText;
    private final DataHandler dataHandler = DataHandler.getInstance();
    private ArrayList<Event> events;
    private OrganizerEventAdapter eventAdapter;
    private List<Integer> mileStones;
    private HashMap<Event, List<Integer>> eventMileStones; // event and all its milestones

    /**
     * Initializes the activity and sets up UI and event listeners.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_page);

        organizerNameEditText = findViewById(R.id.organizerNameEditText);

        mileStones = new ArrayList<>();
        eventMileStones = new HashMap<>();
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
                if (dataHandler.getLocalOrganizer() == null){
                    String organizerName = organizerNameEditText.getText().toString().trim();
                    if (organizerName.isEmpty()){
                        Toast.makeText(OrganizerPageActivity.this, "Organizer name can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Organizer organizer = new Organizer(organizerName);
                    dataHandler.setLocalOrganizer(organizer);
                    saveLocalOrganizerId();
                    dataHandler.addOrganizer(OrganizerPageActivity.this);
                }
                else {
                    Intent intent = new Intent(OrganizerPageActivity.this, CreateNewEventActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (dataHandler.getLocalOrganizer() != null) {
            String organizerName = dataHandler.getLocalOrganizer().getName();
                dataHandler.addOrganizerEventsListener(this);
                TextView headTextView = findViewById(R.id.headerTextView);
                headTextView.setText("Welcome back, " + organizerName);
                organizerNameEditText.setText("");
                organizerNameEditText.setVisibility(View.GONE);

        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Saves the organizer's ID
     */
    public void saveLocalOrganizerId(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("organizerId", dataHandler.getLocalOrganizer().getOrganizerId());
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
        Integer attendance = event.getAttendance();
        if (eventMileStones.get(event) == null){
            eventMileStones.put(event, new ArrayList<>());
        }
        if (mileStones.contains(attendance) && !eventMileStones.get(event).contains(attendance)){
            eventMileStones.get(event).add(attendance);
            Toast.makeText(OrganizerPageActivity.this, "Milestone Reached!! " + event.getAttendance() + " attendees in " + event.getName(), Toast.LENGTH_LONG).show();
        }
    }

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

    @Override
    public void onAddOrganizer(Organizer organizer) {
        if (organizer != null){
            Intent intent = new Intent(OrganizerPageActivity.this, CreateNewEventActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(OrganizerPageActivity.this, "Error accessing firebase", Toast.LENGTH_SHORT).show();
        }
    }
}


