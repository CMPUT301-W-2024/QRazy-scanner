package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class AttendeePageActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        recyclerView = findViewById(R.id.events_recycler_view);

        ArrayList<Event> data = new ArrayList<>();
        Event event = new Event();
        event.setName("Hello");
        event.setOrganizer("Vought");
        event.setDescription("afsdsadasdasdasdasdasd adsdasd asdasfrjgbds fjksdbfsdhfb");
        Event event1 = new Event();
        event1.setName("Hello");
        event1.setOrganizer("Vought");
        event1.setDescription("afsdsadasdasdasdasdasd adsdasd asdasfrjgbds fjksdbfsdhfb");
        Event event2 = new Event();
        event2.setName("Hello");
        event2.setOrganizer("Vought");
        event2.setDescription("afsdsadasdasdasdasdasd adsdasd asdasfrjgbds fjksdbfsdhfb");
        Event event3 = new Event();
        event3.setName("Hello");
        event3.setOrganizer("Vought");
        event3.setDescription("afsdsadasdasdasdasdasd adsdasd asdasfrjgbds fjksdbfsdhfb");
        data.add(event);
        data.add(event1);
        data.add(event2);
        data.add(event3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        displayEvents(data);
    }

    public void displayEvents(ArrayList<Event> events){
        recyclerView.setAdapter(new EventAdapter(events));
    }

}