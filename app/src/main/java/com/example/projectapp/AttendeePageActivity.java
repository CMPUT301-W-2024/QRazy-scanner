package com.example.projectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class AttendeePageActivity extends AppCompatActivity {
    private ArrayList<Event> allEvents;
    private ArrayList<Event> attendeeEvents;
    private AttendeeEventAdapter attendeeEventsAdapter;
    private AttendeeEventAdapter allEventsAdapter;
    private ListenerRegistration attendeeEventsListener;
    private ListenerRegistration allEventsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        RecyclerView attendeeEventsList = findViewById(R.id.attendeeEventsList);
        RecyclerView allEventsList = findViewById(R.id.allEventsList);

        attendeeEvents = new ArrayList<>();
        allEvents = new ArrayList<>();

        attendeeEventsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        allEventsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        attendeeEventsAdapter = new AttendeeEventAdapter(attendeeEvents);
        allEventsAdapter = new AttendeeEventAdapter(allEvents);

        attendeeEventsList.setAdapter(attendeeEventsAdapter);
        allEventsList.setAdapter(allEventsAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        attendeeEventsListener.remove();
        allEventsListener.remove();
    }


    @Override
    protected void onResume() {
        super.onResume();
        addAttendeeEventsListener();
        addAllEventsListener();
    }

    public void addEvent(Event event, ArrayList<Event> list, AttendeeEventAdapter adapter){
        for (int i=0; i<list.size(); i++){
            if (event.getEventId() != null && (event.getEventId()).equals(list.get(i).getEventId())){
                return;
            }
        }
        list.add(event);
        adapter.notifyDataSetChanged();
    }

    public void updateEvent(Event event, ArrayList<Event> list, AttendeeEventAdapter adapter){
        for (int i=0; i<list.size(); i++){
            if (event.getEventId() != null && (event.getEventId()).equals(list.get(i).getEventId())){
                list.set(i, event);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void removeEvent(Event event, ArrayList<Event> list, AttendeeEventAdapter adapter){
        Iterator<Event> i = list.iterator();
        while(i.hasNext()){
            Event e = i.next();
            if ((e.getEventId()).equals(event.getEventId())){
                i.remove();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void addAttendeeEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        attendeeEventsListener = eventsRef.orderBy("attendees."+ DataHandler.getInstance().getAttendee().getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
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
        });
    }

    private void addAllEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");
        System.out.println("Got till here");
        allEventsListener = eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
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
        });
    }
}