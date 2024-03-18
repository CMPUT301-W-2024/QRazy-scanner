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
/**
 * Admin activity for managing events and attendees.
 * This activity provides functionalities to load and display event and attendee data from Firebase,
 * and handle image display and details dialog for both events and attendees.
 */
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
        loadImagesFromFirebase("events", "poster", R.id.firstImagesLayout);
        loadImagesFromFirebase("attendees", "profilePic", R.id.secondImagesLayout);

        //fetchDocumentById("05b586d8-b902-498f-ac88-31d6cc4c2d5b");
    }

    private void fetchDocumentById(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d("Admin", "Document data: " + document.getData());
                        String profilePic = document.getString("profilePic");
                        if (profilePic != null) {
                            Log.d("Admin", "Profile Pic: " + profilePic);
                            // You can now use the profilePic string as needed
                        } else {
                            Log.d("Admin", "No profile pic found in the document");
                        }
                    } else {
                        Log.d("Admin", "No such document");
                    }
                } else {
                    Log.d("Admin", "get failed with ", task.getException());
                }
            }
        });
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

    private void deleteEventByNameAndDetails(String eventName, String eventOrganizer, String eventDescription) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereEqualTo("name", eventName)
                .whereEqualTo("organizer", eventOrganizer)
                .whereEqualTo("description", eventDescription)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming name, organizer and description combination is unique
                                db.collection("events").document(document.getId()).delete()
                                        .addOnSuccessListener(aVoid -> Log.d("Admin", "Event successfully deleted!"))
                                        .addOnFailureListener(e -> Log.w("Admin", "Error deleting event", e));
                            }
                        } else {
                            Log.w("Admin", "Error getting documents: ", task.getException());
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
        Button deleteButton = eventDetailDialog.findViewById(R.id.event_delete_button);

        eventNameView.setText(name);
        eventOrganizerView.setText(organizer);
        eventDescriptionView.setText(description);

        closeButton.setOnClickListener(v -> eventDetailDialog.dismiss());
        deleteButton.setOnClickListener(v -> {
            deleteEventByNameAndDetails(name, organizer, description);
            eventDetailDialog.dismiss();
        });

        eventDetailDialog.show();
    }




    private void loadImagesFromFirebase(String collection, String field, int layoutId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Admin", "Listen failed.", e);
                    return;
                }

                if (snapshots != null && !snapshots.isEmpty()) {
                    LinearLayout imagesLayout = findViewById(layoutId);
                    imagesLayout.removeAllViews();

                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        String encodedImage = document.getString(field);
                        if (encodedImage != null && !encodedImage.isEmpty()) {
                            addImageToLayout(encodedImage, imagesLayout);
                        }
                    }
                } else {
                    Log.d("Admin", "No data found");
                }
            }
        });
    }


    private void addImageToLayout(String encodedImageString, LinearLayout layout) {
        View imageLayoutView = LayoutInflater.from(this).inflate(R.layout.image_layout, null, false);
        ImageView imageView = imageLayoutView.findViewById(R.id.image_view);

        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }

        layout.addView(imageLayoutView);
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

    /**
     * Converts a Base64 encoded string to a Bitmap object.
     * @param encodedString The Base64 encoded string representing an image.
     * @return A Bitmap object or null if the conversion fails.
     */
    public Bitmap stringToBitmap(String encodedString) {
        if (encodedString == null || encodedString.isEmpty()) {
            Log.e("Admin", "Encoded string is null or empty");
            return null;
        }
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e("Admin", "Error decoding bitmap", e);
            return null;
        }
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
