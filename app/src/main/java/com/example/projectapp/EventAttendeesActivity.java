package com.example.projectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Activity responsible for displaying lists of attendees for an Event.
 * Handles separating attendees into lists based on check-in status and
 * sync with Firebase data.
 */
public class EventAttendeesActivity extends AppCompatActivity {

    private ArrayList<Attendee> checkedInAttendees;
    private ArrayList<Attendee> signedUpAttendees;
    private EventAttendeeAdapter checkedInAttendeeAdapter;
    private EventAttendeeAdapter signedUpAttendeeAdapter;
    private ListenerRegistration checkedInAttendeeListener;
    private ListenerRegistration signedUpAttendeeListener;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendees_actvity);

        event = (Event) getIntent().getSerializableExtra("EVENT");

        RecyclerView checkedInAttendeesListView = findViewById(R.id.checkedInAttendeesList);
        RecyclerView signedUpAttendeesListView = findViewById(R.id.signedUpAttendeesList);

        checkedInAttendeesListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        signedUpAttendeesListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        checkedInAttendees = new ArrayList<>();
        signedUpAttendees = new ArrayList<>();

        checkedInAttendeeAdapter = new EventAttendeeAdapter(checkedInAttendees, event);
        signedUpAttendeeAdapter = new EventAttendeeAdapter(signedUpAttendees, event);

        checkedInAttendeesListView.setAdapter(checkedInAttendeeAdapter);
        signedUpAttendeesListView.setAdapter(signedUpAttendeeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkedInAttendeeListener = addAttendeesListener(true, checkedInAttendees, checkedInAttendeeAdapter);
        signedUpAttendeeListener = addAttendeesListener(false, signedUpAttendees, signedUpAttendeeAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkedInAttendeeListener.remove();
        signedUpAttendeeListener.remove();
    }

    /**
     * Adds a new Attendee to the specified list and updates the adapter.
     * Prevents duplicates based on attendeeId.
     * @param attendee The Attendee to add.
     * @param list The list to modify.
     * @param adapter The adapter to notify of changes.
     */
    public void addAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        for (int i=0; i<list.size(); i++){
            if (attendee.getAttendeeId() != null && list.get(i).getAttendeeId() != null && (attendee.getAttendeeId()).equals(list.get(i).getAttendeeId())){
                return;
            }
        }
        list.add(attendee);
        adapter.notifyDataSetChanged();
    }

    /**
     * Updates an existing Attendee in the specified list and updates the adapter.
     * @param attendee The updated Attendee.
     * @param list The list to modify.
     * @param adapter The adapter to notify of changes.
     */
    public void updateAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        for (int i=0; i<list.size(); i++){
            if (attendee.getAttendeeId() != null && (attendee.getAttendeeId()).equals(list.get(i).getAttendeeId())){
                list.set(i, attendee);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Removes an Attendee from the specified list and updates the adapter.
     * @param attendee The Attendee to remove.
     * @param list The list to modify.
     * @param adapter The adapter to notify of changes.
     */
    public void removeAttendee(Attendee attendee, ArrayList<Attendee> list, EventAttendeeAdapter adapter){
        Iterator<Attendee> i = list.iterator();
        while(i.hasNext()){
            Attendee e = i.next();
            if (attendee.getAttendeeId() != null && e.getAttendeeId() != null && (e.getAttendeeId()).equals(attendee.getAttendeeId())){
                i.remove();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private ListenerRegistration addAttendeesListener(boolean getCheckedIn, ArrayList<Attendee> attendees, EventAttendeeAdapter adapter){
        CollectionReference attendeeRef = FirebaseFirestore.getInstance().collection("attendees");

        Query query;
        if (getCheckedIn){
            query = attendeeRef.orderBy("checkedInEvents." + event.getEventId());
        }
        else {
            query = attendeeRef.whereArrayContains("signedUpEvents", event.getEventId());
        }

        ListenerRegistration attendeeListener = query.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Attendee attendee = dc.getDocument().toObject(Attendee.class);
                        switch (dc.getType()) {
                            case ADDED:
                                addAttendee(attendee, attendees, adapter);
                                break;
                            case MODIFIED:
                                updateAttendee(attendee, attendees, adapter);
                                break;
                            case REMOVED:
                                removeAttendee(attendee, attendees, adapter);
                                break;
                        }
                    }
                }
            }
        });
        return  attendeeListener;
    }
}