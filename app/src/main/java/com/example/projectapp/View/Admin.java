package com.example.projectapp.View;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectapp.Controller.AttendeesListenerCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Controller.DeleteAttendeeCallback;
import com.example.projectapp.Controller.DeleteEventCallback;
import com.example.projectapp.Controller.EventsListenerCallback;
import com.example.projectapp.Controller.ImagesListenerCallback;
import com.example.projectapp.ImageHandler;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.R;
import com.example.projectapp.Controller.UpdateAttendeeCallback;
import com.example.projectapp.Controller.UpdateEventCallback;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Admin class provides a central view for administrators to manage events and attendees within an application.
 */
public class Admin extends AppCompatActivity implements EventsListenerCallback, AttendeesListenerCallback, ImagesListenerCallback, UpdateEventCallback, UpdateAttendeeCallback, DeleteEventCallback, DeleteAttendeeCallback {
    private ArrayList<Event> events;
    private ArrayList<Attendee> attendees;
    private AttendeeEventAdapter eventsAdapter;
    private EventAttendeeAdapter attendeesAdapter;
    private RecyclerView eventsLayout, attendeesLayout;
    private LinearLayout  postersLayout, profilePicsLayout, qrCodesLayout, promoQrCodesLayout;
    private final DataHandler dataHandler = DataHandler.getInstance();
    private final ImageHandler imageHandler = ImageHandler.getInstance();

    /**
     * Initializes the Admin view, setting up UI elements and listeners for real-time database updates.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        eventsLayout = findViewById(R.id.adminEventsLayout);
        attendeesLayout = findViewById(R.id.adminAttendeesLayout);
        postersLayout = findViewById(R.id.adminPostersLayout);
        profilePicsLayout = findViewById(R.id.adminProfilePicsLayout);
        qrCodesLayout = findViewById(R.id.adminQrCodesLayout);
        promoQrCodesLayout = findViewById(R.id.adminPromoQrCodesLayout);

        dataHandler.addEventsListener(this);
        dataHandler.addAttendeesListener(this);
        dataHandler.addImagesListener("events", "poster", postersLayout, this);
        dataHandler.addImagesListener("attendees", "profilePic", profilePicsLayout, this);
        dataHandler.addImagesListener("events", "qrCode", qrCodesLayout, this);
        dataHandler.addImagesListener("events", "promoQrCode", promoQrCodesLayout, this);

        eventsLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        attendeesLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        events = new ArrayList<>();
        attendees = new ArrayList<>();

        eventsAdapter = new AttendeeEventAdapter(events, new AttendeeEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                showDialogWithEventDetails(event);
            }
        });

        attendeesAdapter = new EventAttendeeAdapter(attendees, null, new EventAttendeeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Attendee attendee) {
                showDialogWithAttendeeDetails(attendee);
            }
        });

        eventsLayout.setAdapter(eventsAdapter);
        attendeesLayout.setAdapter(attendeesAdapter);
    }


    /**
     * Callback triggered when changes are detected in the events collection.
     * @param updateType The type of change (ADDED, MODIFIED, REMOVED)
     * @param event The updated Event object
     */
    @Override
    public void onEventsUpdated(DocumentChange.Type updateType, Event event) {
        switch (updateType) {
            case ADDED:
                if (!events.contains(event)){
                    events.add(event);
                }
                break;
            case MODIFIED:
                if (events.contains(event)){
                    events.set(events.indexOf(event), event);
                }
                break;
            case REMOVED:
                events.remove(event);
                break;
        }
        eventsAdapter.notifyDataSetChanged();
    }

    /**
     * Callback triggered when changes are detected in the attendees collection.
     * @param updateType    The type of change (ADDED, MODIFIED, REMOVED)
     * @param attendee      The updated Attendee object
     */
    @Override
    public void onAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee) {
        switch (updateType) {
            case ADDED:
                if (!attendees.contains(attendee)){
                    attendees.add(attendee);
                }
                break;
            case MODIFIED:
                if (attendees.contains(attendee)){
                    attendees.set(attendees.indexOf(attendee), attendee);
                }
                break;
            case REMOVED:
                attendees.remove(attendee);
                break;
        }
        attendeesAdapter.notifyDataSetChanged();
    }

    /**
     *  Callback triggered when changes are detected in image references.
     *  @param images       A map of document IDs to their image data
     *  @param layout       The layout to dynamically update with the images
     *  @param collection   The Firestore collection containing the images
     *  @param field        The image field within the documents
     */
    @Override
    public void onImagesUpdated(HashMap<String, String> images, LinearLayout layout,String collection, String field) {
        layout.removeAllViews();
        for (String document : images.keySet()){
            addImageToLayout(images.get(document), layout, document, field, collection);
        }
    }

    /**
     * Displays a dialog with details of an event. Allows the user to view more information about the event.
     * If the event is available for sign-up, it displays a sign-up button.
     * @param event The Event object containing details to be displayed.
     */
    private void showDialogWithEventDetails(Event event) {
        Dialog eventDetailDialog = new Dialog(this);
        eventDetailDialog.setContentView(R.layout.event_dialog);


        TextView eventNameView = eventDetailDialog.findViewById(R.id.dialogEventName);
        TextView eventOrganizerView = eventDetailDialog.findViewById(R.id.dialogEventOrganizer);
        TextView eventDescriptionView = eventDetailDialog.findViewById(R.id.dialogEventDescription);
        TextView eventDateView = eventDetailDialog.findViewById(R.id.dialogEventDate);
        TextView eventTimeView = eventDetailDialog.findViewById(R.id.dialogEventTime);
        ImageView eventPosterView = eventDetailDialog.findViewById(R.id.dialogEventPoster);
        Button closeButton = eventDetailDialog.findViewById(R.id.dialogEventCloseButton);
        Button deleteButton = eventDetailDialog.findViewById(R.id.dialogEventDeleteButton);


        eventNameView.setText(event.getName());

        eventOrganizerView.setText(event.getOrganizerName());

        eventDescriptionView.setText(event.getDescription());

        if (event.getPoster() != null){
            eventPosterView.setImageBitmap(imageHandler.stringToBitmap(event.getPoster()));
        }
        eventDateView.setText(event.getDate());
        eventTimeView.setText(event.getStartTime() + " - " + event.getEndTime());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetailDialog.dismiss();
            }
        });

        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(event);
                eventDetailDialog.dismiss();
            }
        });

        eventDetailDialog.show();
    }

    /**
     * Displays a dialog with detailed information about the selected attendee.
     * @param attendee The Attendee object to display details for
     */
    private void showDialogWithAttendeeDetails(Attendee attendee) {
        Dialog detailDialog = new Dialog(this);
        detailDialog.setContentView(R.layout.attendee_dialog);
        TextView nameView = detailDialog.findViewById(R.id.attendeeDialogName);
        nameView.setText(attendee.getName());
        TextView emailView = detailDialog.findViewById(R.id.attendeeDialogEmail);
        emailView.setText(attendee.getHomepage());
        TextView contactInfoView = detailDialog.findViewById(R.id.attendeeDialogContact);
        contactInfoView.setText(attendee.getContactInfo());
        ImageView profilePicView = detailDialog.findViewById(R.id.attendeeDialogProfilePic);
        Bitmap imageBitmap = imageHandler.stringToBitmap(attendee.getProfilePic());
        if (imageBitmap != null) {
            profilePicView.setImageBitmap(imageBitmap);
        }
        Button closeButton = detailDialog.findViewById(R.id.attendeeDialogCloseButton);
        closeButton.setOnClickListener(v -> detailDialog.dismiss());
        Button deleteButton = detailDialog.findViewById(R.id.attendeeDialogDeleteButton);

        deleteButton.setOnClickListener(v -> {
            deleteAttendee(attendee);
            detailDialog.dismiss();
        });

        detailDialog.show();
    }

    /**
     * Displays a dialog with a larger view  of the image.
     * @param image         The Bitmap of the image to display
     * @param documentId    The document ID for deleting the image
     * @param field         The field name for deleting the image
     * @param collection    The collection name for deleting the image
     */
    private void showDialogWithImage(Bitmap image, String documentId, String field, String collection) {
        Dialog imageDialog = new Dialog(this);
        imageDialog.setContentView(R.layout.image_dialog);

        ImageView imageView = imageDialog.findViewById(R.id.imageDialogImageView);
        imageView.setImageBitmap(image);

        Button deleteButton = imageDialog.findViewById(R.id.imageDialogDeleteButton);
        deleteButton.setOnClickListener(v -> {
            deleteImage(documentId, field, collection);
            imageDialog.dismiss();
        });

        imageDialog.show();
    }

    /**
     * Helper method to add images to a layout with a click listener for viewing larger images.
     */
    private void addImageToLayout(String encodedImageString, LinearLayout layout, String documentId, String field, String collection) {
        View imageLayoutView = LayoutInflater.from(this).inflate(R.layout.image_layout, null, false);
        ImageView imageView = imageLayoutView.findViewById(R.id.image_view);
        Bitmap imageBitmap = imageHandler.stringToBitmap(encodedImageString);
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);

            // Set OnClickListener to show the image in a dialog when clicked
            imageView.setOnClickListener(v -> showDialogWithImage(imageBitmap, documentId, field, collection));
            layout.addView(imageLayoutView);
        }

    }



    /**
     * Deletes an image field from a Firestore document in either the "events" or "attendees" collection.
     *
     * @param documentId    The ID of the document containing the image
     * @param field         The name of the image field within the document
     * @param collection    The Firestore collection containing the document ("events" or "attendees")
     */

    private void deleteImage(String documentId, String field, String collection) {
        if (collection.equals("events")){
            dataHandler.updateEvent(documentId, field, null, this);
        }
        else {
            dataHandler.updateAttendee(documentId, field, null, this);
        }
    }

    /**
     *  Deletes an attendee from the database and removes them from all associated events.
     *
     *  @param attendee The Attendee object to be deleted
     */
    private void deleteAttendee(Attendee attendee){
        dataHandler.deleteAttendee(attendee.getAttendeeId(), this);
        // remove attendee from all of its events
        for (String event : attendee.getSignedUpEvents()){
            dataHandler.updateEvent(event, "signedAttendees", FieldValue.arrayRemove(attendee.getAttendeeId()), null);
        }

        for (String event : attendee.getCheckedInEvents().keySet()){
            dataHandler.updateEvent(event, "checkedAttendees." + attendee.getAttendeeId(), FieldValue.delete(), null);
        }

        // remove geopoints for attendees all check ins
        for (String event : attendee.getCheckedInEvents().keySet()){
            dataHandler.updateEvent(event, "geopoints." + attendee.getAttendeeId(), FieldValue.delete(), null);
        }
    }

    /**
     *  Deletes an event from the database and removes it from the associated attendees.
     *
     *  @param event The Event object to be deleted.
     */
    private void deleteEvent(Event event){
        dataHandler.deleteEvent(event.getEventId(), this);
        // remove event from all of its attendees
        for (String attendee : event.getSignedAttendees()){
            dataHandler.updateAttendee(attendee, "signedUpEvents", FieldValue.arrayRemove(event.getEventId()), null);
        }

        for (String attendee : event.getCheckedAttendees().keySet()){
            dataHandler.updateAttendee(attendee, "checkedInEvents." + event.getEventId(), FieldValue.delete(), null);
        }
    }

    /**
     * Callback invoked when an attendee document has been updated in the database.
     * Displays a message indicating success or failure of the image deletion.
     *
     * @param attendeeId The ID of the attendee document that was updated (or null if the update failed)
     */
    @Override
    public void onUpdateAttendee(String attendeeId) {
        if (attendeeId == null){
            Toast.makeText(this, "Couldn't delete image", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback invoked when an event document has been updated in the database.
     * Displays a message indicating success or failure of the image deletion.
     *
     * @param eventId The ID of the event document that was updated (or null if the update failed)
     */
    @Override
    public void onUpdateEvent(String eventId) {
        if (eventId == null){
            Toast.makeText(this, "Couldn't delete image", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  Callback invoked when an attendee has been deleted from the database.
     *  Displays a message indicating success or failure of the deletion.
     *
     *  @param attendeeId The ID of the attendee that was deleted (or null if the deletion failed)
     */
    @Override
    public void onDeleteAttendee(String attendeeId) {
        if (attendeeId == null){
            Toast.makeText(this, "Couldn't delete attendee", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Attendee deleted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  Callback invoked when an event has been deleted from the database.
     *  Displays a message indicating success or failure of the deletion.
     *
     *  @param eventId The ID of the event that was deleted (or null if the deletion failed)
     */
    @Override
    public void onDeleteEvent(String eventId) {
        if (eventId == null){
            Toast.makeText(this, "Couldn't delete event", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
        }
    }
}

