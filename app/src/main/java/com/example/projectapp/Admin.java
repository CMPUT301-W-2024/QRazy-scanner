package com.example.projectapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {

    LinearLayout horizontalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        horizontalLayout = findViewById(R.id.horizontalLayout);

        // loadEventsFromFirebase(); // Comment this out to use mock data instead.
        loadMockEvents(); // Call this to load mock data.
    }

    private void loadMockEvents() {
        List<Event> mockEvents = generateMockEvents();
        for (Event event : mockEvents) {
            addEventToScrollView(event);
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

        // ... add as many mock events as you need

        return mockEvents;
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
}
