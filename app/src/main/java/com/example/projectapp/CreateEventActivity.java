package com.example.projectapp;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    private EditText organizerNameEditText;
    private DataHandler dataHandler;

    /**
     * Handles activity initialization. Sets up UI elements, retrieves the DataHandler instance,
     * and sets up the "Create New Event" button functionality.
     *
     * @param savedInstanceState
     *      The saved instance state of the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_page);
        dataHandler = DataHandler.getInstance();
        organizerNameEditText = findViewById(R.id.organizerNameEditText);

        Button createNewEventButton = findViewById(R.id.createNewEventButton);
        createNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataHandler.getOrganizer() == null){
                    Organizer organizer = new Organizer(organizerNameEditText.getText().toString().trim());
                    dataHandler.setOrganizer(organizer);
                    saveOrganizerId();
                    dataHandler.addOrganizer(organizer);
                }
                Intent intent = new Intent(CreateEventActivity.this, CreateNewEventActivity.class);
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


