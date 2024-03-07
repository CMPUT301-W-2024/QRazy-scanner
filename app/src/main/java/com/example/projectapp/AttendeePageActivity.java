package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;

public class AttendeePageActivity extends AppCompatActivity {
    private static AttendeePageActivity instance;
    RecyclerView recyclerView;
    ArrayList<Event> events;
    EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        instance = this;

        recyclerView = findViewById(R.id.events_recycler_view);

        events = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventAdapter = new EventAdapter(events);
        recyclerView.setAdapter(eventAdapter);

        findViewById(R.id.test_button).setOnClickListener(v -> {
            startActivity(new Intent(getApplication(), CreateNewEventActivity.class));
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataHandler.getInstance().removeEventsListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataHandler.getInstance().addEventsListener();
    }

    public void addEvent(Event event){
        System.out.println(event.getEventId() + " Event sdded here");
        for (int i=0; i<events.size(); i++){
            if (event.getEventId() != null && event.getEventId().equals(events.get(i).getEventId())){
                System.out.println("same here letsgooo  " + event.getName());
                return;
            }
        }
        events.add(event);
        eventAdapter.notifyDataSetChanged();
    }

    public void removeEvent(Event event){
        Iterator<Event> i = events.iterator();
        while(i.hasNext()){
            Event e = i.next();
            if (e.getEventId().equals(event.getEventId())){
                i.remove();
            }
        }
        eventAdapter.notifyDataSetChanged();
    }

    public static AttendeePageActivity getInstance(){
        return instance;
    }
}