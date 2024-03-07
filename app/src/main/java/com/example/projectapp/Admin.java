package com.example.projectapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends AppCompatActivity {

    LinearLayout horizontalLayout;
    LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        horizontalLayout = findViewById(R.id.horizontalLayout);
        verticalLayout = findViewById(R.id.verticalLayout);
        loadEventsFromFirebase();
        //loadMockEvents();
        //loadMockProfiles();
        loadAttendeesFromFirebase();
    }

    private void loadMockProfiles() {
        List<Attendee> mockProfiles = generateMockProfiles();
        for (Attendee attendee : mockProfiles) {
            addAttendeeToScrollView(attendee);
        }
    }
    private List<Event> generateMockEvents() {
        List<Event> mockEvents = new ArrayList<>();
        Event event1 = new Event();
        event1.setName("Mock Event 1");
        event1.setOrganizer("Mock Organizer 1");
        event1.setDescription("This is a mock description for Event 1.");
        mockEvents.add(event1);

        Event event2 = new Event();
        event2.setName("Mock Event 2");
        event2.setOrganizer("Mock Organizer 2");
        event2.setDescription("This is a mock description for Event 2.");
        mockEvents.add(event2);


        return mockEvents;
    }
    private void loadMockEvents() {
        List<Event> mockEvents = generateMockEvents();
        for (Event event : mockEvents) {
            addEventToScrollView(event);
        }
    }

    private List<Attendee> generateMockProfiles() {
        List<Attendee> mockProfiles = new ArrayList<>();

        Attendee attendee1 = new Attendee();
        attendee1.setName("John Doe");

        mockProfiles.add(attendee1);

        Attendee attendee2 = new Attendee();
        attendee2.setName("Jane Smith");
        mockProfiles.add(attendee2);

        return mockProfiles;
    }

    private void addAttendeeToScrollView(Attendee attendee) {
        View attendeeView = LayoutInflater.from(this).inflate(R.layout.profile_widget, verticalLayout, false);

        TextView attendeeNameView = attendeeView.findViewById(R.id.attendee);
        attendeeNameView.setText(attendee.getName());

        verticalLayout.addView(attendeeView);
    }

    private void loadEventsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> docMap = document.getData();
                        Event event = new Event();
                        event.setEventId(document.getId());
                        event.setName((String) docMap.get("name"));
                        event.setDate((String) docMap.get("date"));
                        event.setOrganizer((String) docMap.get("organizer"));
                        event.setDescription((String) docMap.get("description"));
                        event.setPoster((String) docMap.get("poster"));

                        if(docMap.get("attendanceLimit") != null) {
                            event.setAttendanceLimit(((Long) docMap.get("attendanceLimit")).intValue());
                        }

                        if (docMap.get("attendees") instanceof Map) {
                            Map<String, Long> attendeesMap = (Map<String, Long>) docMap.get("attendees");
                            HashMap<String, Integer> attendees = new HashMap<>();
                            for (Map.Entry<String, Long> entry : attendeesMap.entrySet()) {
                                attendees.put(entry.getKey(), entry.getValue().intValue());
                            }
                            event.setAttendees(attendees);
                        }

                        addEventToScrollView(event);
                    }
                }
            }
        });
    }


    private void addEventToScrollView(Event event) {
        View eventView = LayoutInflater.from(this).inflate(R.layout.event_widget, horizontalLayout, false);

        TextView eventNameView = eventView.findViewById(R.id.event_name_text);
        eventNameView.setText(event.getName());

        TextView eventOrganizerView = eventView.findViewById(R.id.event_organizer_name_text);
        eventOrganizerView.setText(event.getOrganizer());

        TextView eventInfoView = eventView.findViewById(R.id.event_info_text);
        eventInfoView.setText(event.getDescription());


        horizontalLayout.addView(eventView);
    }

    private void loadAttendeesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> docMap = document.getData();

                        // Create a new Attendee object and set its fields from the map
                        Attendee attendee = new Attendee();
                        attendee.setAttendeeId(document.getId()); // Assuming the document ID is the attendee ID
                        attendee.setName((String) docMap.get("name"));
                        // Set other fields of Attendee as needed

                        addAttendeeToScrollView(attendee);
                    }
                }
            }
        });
    }


}
