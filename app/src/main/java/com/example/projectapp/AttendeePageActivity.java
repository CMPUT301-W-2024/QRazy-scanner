package com.example.projectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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
            if (event.getEventId() != null && list.get(i).getEventId() != null && (event.getEventId()).equals(list.get(i).getEventId())){
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
            if (event.getEventId() != null && e.getEventId() != null && (e.getEventId()).equals(event.getEventId())){
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
                    event.addAttendee(DataHandler.getInstance().getAttendee().getAttendeeId());
                    eventDetailDialog.dismiss();
                }
            });
        }

        eventDetailDialog.show();
    }
}