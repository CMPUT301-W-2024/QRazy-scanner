package com.example.projectapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Admin extends AppCompatActivity implements EventsListenerCallback, AttendeesListenerCallback {

    LinearLayout eventsLayout, attendeesLayout, postersLayout, profilePicsLayout, qrCodesLayout;
    private final DataHandler dataHandler = DataHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        eventsLayout = findViewById(R.id.adminEventsLayout);
        attendeesLayout = findViewById(R.id.adminAttendeesLayout);
        postersLayout = findViewById(R.id.adminPostersLayout);
        profilePicsLayout = findViewById(R.id.adminProfilePicsLayout);
        qrCodesLayout = findViewById(R.id.adminQrCodesLayout);

        dataHandler.addEventsListener(this);
        dataHandler.addAttendeesListener(this);
        dataHandler.addImagesListener();
/*        loadImagesFromFirebase("events", "poster", R.id.adminPostersLayout);
        loadImagesFromFirebase("attendees", "profilePic", R.id.adminProfilePicsLayout);*/
    }

    @Override
    public void onEventsUpdated(DocumentChange.Type updateType, Event event) {
        addEventToScrollView(event);

        String poster = event.getPoster();
        String qrCode = event.getQrCode();
        String promoQrCode = event.getPromoQrCode();

        if (poster != null && !poster.isEmpty()){
            addImageToLayout(poster, postersLayout, event, null, "poster");
        }
        if (qrCode != null && !qrCode.isEmpty()){
            addImageToLayout(qrCode, qrCodesLayout, event, null, "qrCode");
        }
        if (promoQrCode != null && !promoQrCode.isEmpty()){
            addImageToLayout(promoQrCode, qrCodesLayout, event, null, "promoQrCode");
        }
    }

    @Override
    public void onAttendeesUpdated(Attendee attendee) {
        addAttendeeToScrollView(attendee);

        String profilePic = attendee.getProfilePic();

        if (profilePic != null && !profilePic.isEmpty()){
            addImageToLayout(profilePic, profilePicsLayout, null, attendee, "profilePic");
        }
    }

    void addEventToScrollView(Event event) {
        View eventView = LayoutInflater.from(this).inflate(R.layout.event_widget, eventsLayout, false);
        TextView eventNameView = eventView.findViewById(R.id.eventNameText);
        eventNameView.setText(event.getName());
        TextView eventOrganizerView = eventView.findViewById(R.id.eventOrganizerNameText);
        eventOrganizerView.setText(event.getOrganizerName());
        TextView eventInfoView = eventView.findViewById(R.id.eventInfoText);
        eventInfoView.setText(event.getDescription());

        // Assuming your Event class has a method to get the encoded image string
        String encodedImage = event.getPoster();

        eventView.setOnClickListener(v -> showDialogWithEventDetails(event.getName(), event.getOrganizerName(), event.getDescription(), encodedImage));
        eventsLayout.addView(eventView);
    }

    void deleteEventByNameAndDetails(String eventName, String eventOrganizer, String eventDescription) {
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
        Button deleteButton = eventDetailDialog.findViewById(R.id.dialogEventDeleteButton);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> {
            deleteEventByNameAndDetails(name, organizer, description);
            eventDetailDialog.dismiss();
        });
        eventDetailDialog.show();
    }

    void addAttendeeToScrollView(Attendee attendee) {
        View attendeeView = LayoutInflater.from(this).inflate(R.layout.profile_widget, attendeesLayout, false);
        TextView attendeeNameView = attendeeView.findViewById(R.id.attendee);
        attendeeNameView.setText(attendee.getName());
        attendeeView.setOnClickListener(v -> showDialogWithAttendeeDetails(attendee.getAttendeeId(), attendee.getName(), attendee.getContactInfo(), attendee.getProfilePic()));
        attendeesLayout.addView(attendeeView);
    }

    void deleteAttendeeById(String attendeeId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").document(attendeeId).delete()
                .addOnSuccessListener(aVoid -> Log.d("Admin", "Attendee successfully deleted!"))
                .addOnFailureListener(e -> Log.w("Admin", "Error deleting attendee", e));
    }

    private void showDialogWithAttendeeDetails(String attendeeId, String name, String contactInfo, String encodedImageString) {
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

    void addImageToLayout(String encodedImageString, LinearLayout layout, Event event, Attendee attendee, String field) {
        View imageLayoutView = LayoutInflater.from(this).inflate(R.layout.image_layout, null, false);
        ImageView imageView = imageLayoutView.findViewById(R.id.image_view);

        Bitmap imageBitmap = stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);

            // Set OnClickListener to show the image in a dialog when clicked
            imageView.setOnClickListener(v -> showDialogWithImage(imageBitmap, event, attendee, field));
            layout.addView(imageLayoutView);
        }

    }



    Bitmap stringToBitmap(String encodedString) {
        if (encodedString == null || encodedString.isEmpty()) {
            Log.e("Admin", "Encoded string is null or empty");
            return null;
        }
        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void showDialogWithImage(Bitmap image, Event event, Attendee attendee, String field) {
        Dialog imageDialog = new Dialog(this);
        imageDialog.setContentView(R.layout.image_dialog);

        ImageView imageView = imageDialog.findViewById(R.id.dialog_image_view);
        imageView.setImageBitmap(image);

        Button deleteButton = imageDialog.findViewById(R.id.delete_image_button);
        deleteButton.setOnClickListener(v -> {
            deleteImage(event, attendee, field);
            imageDialog.dismiss();
        });

        imageDialog.show();
    }

    void deleteImage(Event event, Attendee attendee, String field) {
        if (event != null){
            //dataHandler.updateEvent(event.getEventId(), field, null, this);
        }
        else {
            //dataHandler.updateAttendee(attendee.getAttendeeId(), field, null, this);
        }
    }



}

