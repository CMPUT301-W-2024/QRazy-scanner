package com.example.projectapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {

    LinearLayout horizontalLayout;
    LinearLayout verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        horizontalLayout = findViewById(R.id.horizontalLayout);
        verticalLayout = findViewById(R.id.verticalLayout);

        loadEventsFromFirebase();
        loadAttendeesFromFirebase();
        loadImagesFromFirebase("events", "poster", R.id.firstImagesLayout);
        loadImagesFromFirebase("attendees", "profilePic", R.id.secondImagesLayout);
    }

    // Testing-related functions
    private List<Event> generateMockEvents() {
        List<Event> mockEvents = new ArrayList<>();
        Event event1 = new Event();
        event1.setName("Mock Event 1");
        event1.setOrganizerName("Mock Organizer 1");
        event1.setDescription("This is a mock description for Event 1.");
        mockEvents.add(event1);

        Event event2 = new Event();
        event2.setName("Mock Event 2");
        event2.setOrganizerName("Mock Organizer 2");
        event2.setDescription("This is a mock description for Event 2.");
        mockEvents.add(event2);

        return mockEvents;
    }

    private List<Attendee> generateMockProfiles() {
        List<Attendee> mockProfiles = new ArrayList<>();
        Attendee attendee1 = new Attendee("Test Attendee");
        attendee1.setName("John Doe");
        mockProfiles.add(attendee1);

        Attendee attendee2 = new Attendee("Test Attendee");
        attendee2.setName("Jane Smith");
        mockProfiles.add(attendee2);

        return mockProfiles;
    }

    private void loadMockEvents() {
        List<Event> mockEvents = generateMockEvents();
        for (Event event : mockEvents) {
            addEventToScrollView(event);
        }
    }

    private void loadMockProfiles() {
        List<Attendee> mockProfiles = generateMockProfiles();
        for (Attendee attendee : mockProfiles) {
            addAttendeeToScrollView(attendee);
        }
    }

    // Event-related functions
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
                    horizontalLayout.removeAllViews();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        Event event = document.toObject(Event.class);
                        addEventToScrollView(event);
                    }
                }
            }
        });
    }

    private void addEventToScrollView(Event event) {
        View eventView = LayoutInflater.from(this).inflate(R.layout.event_widget, horizontalLayout, false);
        TextView eventNameView = eventView.findViewById(R.id.eventNameText);
        eventNameView.setText(event.getName());
        TextView eventOrganizerView = eventView.findViewById(R.id.eventOrganizerNameText);
        eventOrganizerView.setText(event.getOrganizerName());
        TextView eventInfoView = eventView.findViewById(R.id.eventInfoText);
        eventInfoView.setText(event.getDescription());

        // Assuming your Event class has a method to get the encoded image string
        String encodedImage = event.getPoster();

        eventView.setOnClickListener(v -> showDialogWithEventDetails(event.getName(), event.getOrganizerName(), event.getDescription(), encodedImage));
        horizontalLayout.addView(eventView);
    }

    private void deleteEventByNameAndDetails(String eventName, String eventOrganizer, String eventDescription) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereEqualTo("name", eventName)
                .whereEqualTo("organizer", eventOrganizer)
                .whereEqualTo("description", eventDescription)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("events").document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> Log.d("Admin", "Event successfully deleted!"))
                                    .addOnFailureListener(e -> Log.w("Admin", "Error deleting event", e));
                        }
                    } else {
                        Log.w("Admin", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void showDialogWithEventDetails(String name, String organizer, String description, String encodedImageString) {
        Dialog eventDetailDialog = new Dialog(this);
        eventDetailDialog.setContentView(R.layout.event_dialog);

        TextView eventNameView = eventDetailDialog.findViewById(R.id.dialogEventName);
        eventNameView.setText(name);
        TextView eventOrganizerView = eventDetailDialog.findViewById(R.id.dialogEventOrganizer);
        eventOrganizerView.setText(organizer);
        TextView eventDescriptionView = eventDetailDialog.findViewById(R.id.dialogEventDescription);
        eventDescriptionView.setText(description);

        ImageView eventPosterView = eventDetailDialog.findViewById(R.id.dialogEventPoster);
        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            eventPosterView.setImageBitmap(imageBitmap);
        }

        Button closeButton = eventDetailDialog.findViewById(R.id.dialogEventCloseButton);
        closeButton.setOnClickListener(v -> eventDetailDialog.dismiss());
        Button deleteButton = eventDetailDialog.findViewById(R.id.eventDeleteButton);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> {
            deleteEventByNameAndDetails(name, organizer, description);
            eventDetailDialog.dismiss();
        });
        eventDetailDialog.show();
    }

    // Profile (attendee)-related functions
    private void loadAttendeesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("loadAttendees", "Listen failed.", e);
                    return;
                }

                if (snapshots != null && !snapshots.isEmpty()) {
                    verticalLayout.removeAllViews();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        Attendee attendee = document.toObject(Attendee.class);
                        addAttendeeToScrollView(attendee);
                    }
                }
            }
        });
    }

    private void addAttendeeToScrollView(Attendee attendee) {
        View attendeeView = LayoutInflater.from(this).inflate(R.layout.profile_widget, verticalLayout, false);
        TextView attendeeNameView = attendeeView.findViewById(R.id.attendee);
        attendeeNameView.setText(attendee.getName());
        attendeeView.setOnClickListener(v -> showDialogWithDetails(attendee.getAttendeeId(), attendee.getName(), attendee.getContactInfo(), attendee.getProfilePic()));
        verticalLayout.addView(attendeeView);
    }

    private void deleteAttendeeById(String attendeeId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").document(attendeeId).delete()
                .addOnSuccessListener(aVoid -> Log.d("Admin", "Attendee successfully deleted!"))
                .addOnFailureListener(e -> Log.w("Admin", "Error deleting attendee", e));
    }

    private void showDialogWithDetails(String attendeeId, String name, String contactInfo, String encodedImageString) {
        Dialog detailDialog = new Dialog(this);
        detailDialog.setContentView(R.layout.attendee_dialog);
        TextView nameView = detailDialog.findViewById(R.id.dialog_name);
        nameView.setText(name);
        TextView contactInfoView = detailDialog.findViewById(R.id.dialog_contact_info);
        contactInfoView.setText(contactInfo);
        ImageView profilePicView = detailDialog.findViewById(R.id.dialog_profile_pic);
        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            profilePicView.setImageBitmap(imageBitmap);
        }
        Button closeButton = detailDialog.findViewById(R.id.dialog_close_button);
        closeButton.setOnClickListener(v -> detailDialog.dismiss());
        Button deleteButton = detailDialog.findViewById(R.id.dialog_delete_button);
        deleteButton.setOnClickListener(v -> {
            deleteAttendeeById(attendeeId);
            detailDialog.dismiss();
        });
        detailDialog.show();
    }

    // Image-related functions

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
                            // Pass the document ID, field, and collection to addImageToLayout
                            addImageToLayout(encodedImage, imagesLayout, document.getId(), field, collection);
                        }
                    }
                }
            }
        });
    }


    private void addImageToLayout(String encodedImageString, LinearLayout layout, String documentId, String field, String collection) {
        View imageLayoutView = LayoutInflater.from(this).inflate(R.layout.image_layout, null, false);
        ImageView imageView = imageLayoutView.findViewById(R.id.image_view);

        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);

            // Set OnClickListener to show the image in a dialog when clicked
            imageView.setOnClickListener(v -> showDialogWithImage(imageBitmap, documentId, field, collection));
        }

        layout.addView(imageLayoutView);
    }



    private Bitmap stringToBitmap(String encodedString) {
        if (encodedString == null || encodedString.isEmpty()) {
            Log.e("Admin", "Encoded string is null or empty");
            return null;
        }
        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void showDialogWithImage(Bitmap image, String documentId, String field, String collection) {
        Dialog imageDialog = new Dialog(this);
        imageDialog.setContentView(R.layout.image_dialog);

        ImageView imageView = imageDialog.findViewById(R.id.dialog_image_view);
        imageView.setImageBitmap(image);

        Button deleteButton = imageDialog.findViewById(R.id.delete_image_button);
        deleteButton.setOnClickListener(v -> {
            deleteImage(documentId, field, collection);
            imageDialog.dismiss();
        });

        imageDialog.show();
    }

    private void deleteImage(String documentId, String field, String collection) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection).document(documentId)
                .update(field, null)
                .addOnSuccessListener(aVoid -> Log.d("Admin", "Image successfully deleted"))
                .addOnFailureListener(e -> Log.w("Admin", "Error deleting image", e));
    }



}

