package com.example.projectapp;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrganizerPageActivity extends AppCompatActivity {

    private EditText organizerNameEditText;
    private DataHandler dataHandler;
    ArrayList<Event> events;
    OrganizerEventAdapter eventAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dataHandler = DataHandler.getInstance();
        organizerNameEditText = findViewById(R.id.organizerNameEditText);

        RecyclerView recyclerView = findViewById(R.id.eventListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button createNewEventButton = findViewById(R.id.createNewEventButton);

        events = new ArrayList<>();
        Event event = new Event("Event Name", "10-10-2027", "VPM", 2, "This is a very good description", "fasd");
        events.add(event);
        eventAdapter = new OrganizerEventAdapter(events);
        recyclerView.setAdapter(eventAdapter);
        Event event2 = new Event("Event45 Name", "19-10-2027", "VPM", 2, "This is a very good description", "fasd");
        events.add(event);
        eventAdapter.notifyDataSetChanged();

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


    /**
     * save organizer's ID
     */
    public void saveOrganizerId(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("OrganizerPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("organizerId", dataHandler.getOrganizer().getOrganizerId());
        editor.apply();
    }
}


