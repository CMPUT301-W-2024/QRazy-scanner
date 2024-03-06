package com.example.projectapp;

<<<<<<< HEAD
public class CreateEventActivity {
}
=======
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    private EditText organizerNameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        organizerNameEditText = findViewById(R.id.organizerNameEditText);

        Button createNewEventButton = findViewById(R.id.createNewEventButton);
        createNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventActivity.this, CreateNewEventActivity.class);
                startActivity(intent);

            }

        });


    }


    private void createEvent(String organizerName) {
    }
}

>>>>>>> 1c0a295649cdfe947a589935a1507772f95bb224
