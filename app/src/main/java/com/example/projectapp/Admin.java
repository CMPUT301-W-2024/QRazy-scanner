package com.example.projectapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends AppCompatActivity {

    LinearLayout horizontalLayout;
    LinearLayout verticalLayout;

    /**
     *  Handles activity creation; sets up layout and initializes data loading.
     *
     *  @param savedInstanceState
     *      The saved instance state of the activity.
     */
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
        loadImagesFromFirebase();
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

    /**
     *  Handles adding an Attendee view to the vertical scroll view.
     *
     *  @param attendee
     *      The Attendee object to be displayed.
     */
    private void addAttendeeToScrollView(Attendee attendee) {
        View attendeeView = LayoutInflater.from(this).inflate(R.layout.profile_widget, verticalLayout, false);

        TextView attendeeNameView = attendeeView.findViewById(R.id.attendee);
        attendeeNameView.setText(attendee.getName());

        verticalLayout.addView(attendeeView);
    }


    /**
     * Loads events from Firebase and displays them in the horizontal scroll view.
     * Sets up a real-time listener for updates to the 'events' collection.
     */
    private void loadEventsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Admin", "Listen failed.", e);
                    return;
                }

                if (snapshots != null && !snapshots.isEmpty()) {
                    horizontalLayout.removeAllViews(); // Clear existing views
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        Event event = document.toObject(Event.class);
                        addEventToScrollView(event);
                    }
                } else {
                    Log.d("Admin", "Current data: null");
                }
            }
        });
    }

    /**
     * Loads images from Firebase, decodes them, and displays them in a bottom scroll view.
     */
    private void loadImagesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Admin", "Listen failed.", e);
                    return;
                }

                if (snapshots != null && !snapshots.isEmpty()) {
                   
                    LinearLayout imagesLayout = findViewById(R.id.imagesLayout);
                    imagesLayout.removeAllViews();

                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        String encodedImage = document.getString("poster");
                        if (encodedImage != null && !encodedImage.isEmpty()) {
                            addImageToBottomScrollView(encodedImage);
                        }
                    }
                } else {
                    Log.d("Admin", "No data found");
                }
            }
        });
    }

    /**
     *  Handles adding an Event view to the horizontal scroll view.
     *
     *  @param event The Event object to be displayed.
     */
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

    /**
     * Loads attendees from Firebase and displays them in the vertical scroll view.
     */
    private void loadAttendeesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> docMap = document.getData();
                        Attendee attendee = new Attendee();
                        attendee.setAttendeeId(document.getId());
                        attendee.setName((String) docMap.get("name"));


                        addAttendeeToScrollView(attendee);
                    }
                }
            }
        });
    }

    /**
     * Converts a Base64 encoded String to a Bitmap.
     *
     * @param encodedString
     *      The Base64 encoded string representation of an image.
     * @return
     *      The decoded Bitmap image, or null on error.
     */
    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds an image to the bottom scroll view.
     *
     * @param encodedImageString
     *      The Base64 encoded string representation of an image.
     */
    private void addImageToBottomScrollView(String encodedImageString) {
        View imageLayoutView = LayoutInflater.from(this).inflate(R.layout.image_layout, null, false);
        ImageView imageView = imageLayoutView.findViewById(R.id.image_view);

        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }

        LinearLayout imagesLayout = findViewById(R.id.imagesLayout);
        imagesLayout.addView(imageLayoutView);
    }


}
