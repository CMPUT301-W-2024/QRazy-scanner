package com.example.projectapp;

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
import java.util.Locale;

/**
 * This activity handles the creation of new events within the application.
 * It allows users to set event information, upload an image, and save the
 * event to Firebase.
 */
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

    /**
     * Initializes the activity, sets up UI elements, and prepares date picker functionality.
     *
     * @param savedInstanceState
     *      The saved instance state of the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);
        attendanceLimitEditText = findViewById(R.id.attendanceLimitEditText);
        calendar = Calendar.getInstance();


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

                            }
                        }, year, month, day);
                datePickerDialog.show();
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

    /**
     * Extracts entered event information, creates a new Event object,
     * and saves the event data to Firebase.
     */
    private void saveEvent() {
        // Get the event details from the input fields
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDate = eventDateEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();
        String attendanceLimitStr = attendanceLimitEditText.getText().toString().trim();
        Integer attendanceLimit = attendanceLimitStr.isEmpty() ? 0 : Integer.parseInt(attendanceLimitStr);

        // Set the event details to the newEvent object

        Event newEvent = new Event(eventName, eventDate, DataHandler.getInstance().getOrganizer().getOrganizerId(), attendanceLimit, eventDescription, encodedImage);



        // Upload the event details to Firebase
        db.collection("events").document(newEvent.getEventId())
                .set(newEvent)
                .addOnSuccessListener(aVoid -> {
                    // Success handling
                    Toast.makeText(CreateNewEventActivity.this, "Event saved successfully", Toast.LENGTH_SHORT).show();

                    // After showing the toast, start the GenerateQrCodeActivity
                    Intent intent = new Intent(CreateNewEventActivity.this, GenerateQrCodeActivity.class);
                    intent.putExtra("EVENT", newEvent); // Pass the event ID to the next activity
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                    Toast.makeText(CreateNewEventActivity.this, "Failed to save event", Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Launches an image picker intent to allow users to select an image for the event poster.
     */
    private void pickImage(){
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        }
        resultLauncher.launch(intent);
    }

    /**
     * Registers an ActivityResultLauncher to handle the result of the image selection process.
     * Within this, updates the ImageView and encodes the selected image for storage.
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
     * Converts a bitmap image into a Base64 encoded string representation.
     *
     * @param bitmap
     *      The bitmap to be encoded.
     * @return
     *      Base64 encoded string of the bitmap.
     */
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}

