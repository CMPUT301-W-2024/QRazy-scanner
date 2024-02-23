package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createEventButton = findViewById(R.id.createEventButton);
        Button joinEventButton = findViewById(R.id.joinEventButton);

        createEventButton.setOnClickListener(view -> {

        });


    }
}