package com.example.projectapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Button;
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
        attendeeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogWithDetails(attendee.getAttendeeId(), attendee.getName(), attendee.getContactInfo(), attendee.getProfilePic());
            }
        });
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


    private void showDialogWithEventDetails(String name, String organizer, String description) {
        Dialog eventDetailDialog = new Dialog(this);
        eventDetailDialog.setContentView(R.layout.event_dialog);

        TextView eventNameView = eventDetailDialog.findViewById(R.id.dialog_event_name);
        TextView eventOrganizerView = eventDetailDialog.findViewById(R.id.dialog_event_organizer);
        TextView eventDescriptionView = eventDetailDialog.findViewById(R.id.dialog_event_description);
        Button closeButton = eventDetailDialog.findViewById(R.id.dialog_event_close_button);

        eventNameView.setText(name);
        eventOrganizerView.setText(organizer);
        eventDescriptionView.setText(description);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetailDialog.dismiss();
            }
        });

        eventDetailDialog.show();
    }



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

        // Set OnClickListener for the event view
        eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWithEventDetails(event.getName(), event.getOrganizer(), event.getDescription());
            }
        });

        horizontalLayout.addView(eventView);
    }


    private void loadAttendeesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("loadAttendees", "Listen failed.", e);
                    return;
                }

                if (snapshots != null && !snapshots.isEmpty()) {
                    verticalLayout.removeAllViews(); // Clear the layout before adding updated data

                    for (DocumentSnapshot document : snapshots.getDocuments()) {
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

    private void showDialogWithDetails(String attendeeId, String name, String contactInfo, String encodedImageString) {
        Dialog detailDialog = new Dialog(this);
        detailDialog.setContentView(R.layout.attendee_dialog);

        TextView attendeeIdView = detailDialog.findViewById(R.id.dialog_attendee_id);
        TextView nameView = detailDialog.findViewById(R.id.dialog_name);
        TextView contactInfoView = detailDialog.findViewById(R.id.dialog_contact_info);
        ImageView profilePicView = detailDialog.findViewById(R.id.dialog_profile_pic);
        Button closeButton = detailDialog.findViewById(R.id.dialog_close_button);
        nameView.setText(name);
        contactInfoView.setText(contactInfo);

        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            profilePicView.setImageBitmap(imageBitmap);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.dismiss();
            }
        });

        detailDialog.show();
    }
}
