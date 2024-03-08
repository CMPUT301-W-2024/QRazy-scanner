package com.example.projectapp;

import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNewEventActivity extends AppCompatActivity {

    private TextInputEditText eventDateEditText;
    private Calendar calendar;

    private MaterialButton uploadButton;
    ActivityResultLauncher<Intent> resultLauncher;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView poster;
    private String encodedImage;
    private Event newEvent;
    private TextInputEditText eventNameEditText, eventDescriptionEditText, attendanceLimitEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_fragment);

        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);
        attendanceLimitEditText = findViewById(R.id.attendanceLimitEditText);
        calendar = Calendar.getInstance();
        newEvent = new Event();
        db.collection("events").document(newEvent.getEventId()).set(newEvent);



        eventDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the current date as the default date in the picker
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and show it
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateNewEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set the chosen date to the EditText
                                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                                eventDateEditText.setText(selectedDate);
                                db.collection("events").document(newEvent.getEventId()).update("date", selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                db.collection("events").document(newEvent.getEventId()).update("date", eventDateEditText.getText().toString());

            }
        });

        MaterialButton saveEventButton = findViewById(R.id.newQrButton);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });



        uploadButton = findViewById(R.id.upload);
        poster = findViewById(R.id.eventPosterImageView);
        registerResult();
        uploadButton.setOnClickListener(v -> pickImage());


    }

    private void saveEvent() {
        // Get the event details from the input fields
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDate = eventDateEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();
        String attendanceLimitStr = attendanceLimitEditText.getText().toString().trim();
        int attendanceLimit = attendanceLimitStr.isEmpty() ? 0 : Integer.parseInt(attendanceLimitStr);

        // Set the event details to the newEvent object
        newEvent.setName(eventName);
        newEvent.setDate(eventDate);
        newEvent.setDescription(eventDescription);
        newEvent.setAttendanceLimit(attendanceLimit);

        // Convert the Event object to a Map to save to Firebase
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("event id", newEvent.getEventId());
        eventMap.put("name", newEvent.getName());
        eventMap.put("date", newEvent.getDate());
        eventMap.put("description", newEvent.getDescription());
        eventMap.put("attendanceLimit", newEvent.getAttendanceLimit());

        // Add the image string to the map if it's not null
        if (encodedImage != null && !encodedImage.isEmpty()) {
            eventMap.put("poster", encodedImage);
        }

        // Upload the event details to Firebase
        db.collection("events").document(newEvent.getEventId())
                .set(eventMap)
                .addOnSuccessListener(aVoid -> {
                    // Success handling
                    Toast.makeText(CreateNewEventActivity.this, "Event saved successfully", Toast.LENGTH_SHORT).show();

                    // After showing the toast, start the GenerateQrCodeActivity
                    Intent intent = new Intent(CreateNewEventActivity.this, GenerateQrCodeActivity.class);
                    intent.putExtra("EVENT_ID", newEvent.getEventId()); // Pass the event ID to the next activity
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                    Toast.makeText(CreateNewEventActivity.this, "Failed to save event", Toast.LENGTH_SHORT).show();
                });
    }


    private void pickImage(){
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        }
        resultLauncher.launch(intent);
    }

    /**
     * Update view and poster field in firestore
     */
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            if (imageUri != null) {
                                poster.setImageURI(imageUri);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                    // Set the encodedImage string here
                                    encodedImage = bitmapToString(bitmap);
                                    // Now we can save the event details including the image string
                                    saveEvent();
                                } catch (Exception e) {
                                    Toast.makeText(CreateNewEventActivity.this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
        );
    }

    /**
     * Convert Bitmap to String
     * @param bitmap the bitmap for converting
     * @return Bitmap in String
     */
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}

