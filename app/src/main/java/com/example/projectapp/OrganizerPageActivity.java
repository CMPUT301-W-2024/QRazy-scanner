package com.example.projectapp;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class OrganizerPageActivity extends AppCompatActivity {

    private EditText organizerNameEditText;
    private DataHandler dataHandler;
    private ArrayList<Event> events;
    private OrganizerEventAdapter eventAdapter;
    private ListenerRegistration organizerEventsListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dataHandler = DataHandler.getInstance();
        organizerNameEditText = findViewById(R.id.organizerNameEditText);
        organizerNameEditText.setText(dataHandler.getOrganizer().getName());

        RecyclerView recyclerView = findViewById(R.id.eventListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button createNewEventButton = findViewById(R.id.createNewEventButton);

        events = new ArrayList<>();
        eventAdapter = new OrganizerEventAdapter(events);
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

    @Override
    protected void onResume() {
        super.onResume();
        addOrganizerEventsListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeOrganizerEventsListener();
    }

    /**
     * save organizer's ID
     */
    public void saveOrganizerId(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("organizerId", dataHandler.getOrganizer().getOrganizerId());
        editor.apply();
    }

    public void addEvent(Event event){
        for (int i=0; i<events.size(); i++){
            if (event.getEventId().equals(events.get(i).getEventId())){
                return;
            }
        }
        events.add(event);
        eventAdapter.notifyDataSetChanged();

    }

    public void updateEvent(Event event){
        for (int i=0; i<events.size(); i++){
            if (event.getEventId().equals(events.get(i).getEventId())){
                events.set(i, event);
            }
        }
        eventAdapter.notifyDataSetChanged();
    }

    public void removeEvent(Event event){
        Iterator<Event> i = events.iterator();
        while(i.hasNext()){
            Event e = i.next();
            if ((e.getEventId()).equals(event.getEventId())){
                i.remove();
            }
        }
        eventAdapter.notifyDataSetChanged();
    }

    private void addOrganizerEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        organizerEventsListener = eventsRef.whereEqualTo("organizer", DataHandler.getInstance().getOrganizer().getOrganizerId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void removeOrganizerEventsListener(){
        organizerEventsListener.remove();
    }
}

