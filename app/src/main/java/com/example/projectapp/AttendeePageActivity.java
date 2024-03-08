package com.example.projectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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
    private static AttendeePageActivity instance;
    private RecyclerView recyclerView;
    private ArrayList<Event> allEvents;
    private ArrayList<Event> attendeeEvents;
    private AttendeeEventAdapter eventAdapter;
    private ListenerRegistration attendeeEventsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        instance = this;

        recyclerView = findViewById(R.id.events_recycler_view);

        allEvents = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventAdapter = new AttendeeEventAdapter(allEvents);
        recyclerView.setAdapter(eventAdapter);

        findViewById(R.id.test_button).setOnClickListener(v -> {
            startActivity(new Intent(getApplication(), CreateNewEventActivity.class));
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeAttendeeEventsListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        addAttendeeEventsListener();
    }

    public void addEvent(Event event){
        for (int i=0; i<allEvents.size(); i++){
            if (event.getEventId() != null && (event.getEventId()).equals(allEvents.get(i).getEventId())){
                return;
            }
        }
        allEvents.add(event);
        eventAdapter.notifyDataSetChanged();
    }

    public void updateEvent(Event event){
        for (int i=0; i<allEvents.size(); i++){
            if (event.getEventId() != null && (event.getEventId()).equals(allEvents.get(i).getEventId())){

            }
        }
    }

    public void removeEvent(Event event){
        Iterator<Event> i = allEvents.iterator();
        while(i.hasNext()){
            Event e = i.next();
            if ((e.getEventId()).equals(event.getEventId())){
                i.remove();
            }
        }
        eventAdapter.notifyDataSetChanged();
    }

    private void addAttendeeEventsListener(){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        attendeeEventsListener = eventsRef.orderBy("attendees."+ DataHandler.getInstance().getAttendee().getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            addEvent(dc.getDocument().toObject(Event.class));
                            break;
                        case MODIFIED:
                            System.out.println("Modified event: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            removeEvent(dc.getDocument().toObject(Event.class));
                            break;
                    }
                }
            }
        });
    }

    private void removeAttendeeEventsListener(){
        attendeeEventsListener.remove();
    }
}