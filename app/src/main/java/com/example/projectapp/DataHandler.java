package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Data handler. Handles interactions with Firebase,
 * stores attendee and organizer data, and sets up listeners for data changes.
 */
public class DataHandler {

    private static DataHandler instance;
    private FirebaseFirestore db;
    private Attendee attendee;
    private Organizer organizer;

    private DataHandler(){
        db = FirebaseFirestore.getInstance();
    }

    public static DataHandler getInstance(){
        if (instance == null){
            instance = new DataHandler();
        }
        return instance;
    }

    /**
     * Get the Attendee
     * @return the Attendee
     */
    public Attendee getAttendee() {
        return attendee;
    }

    /**
     * Set the attendee
     * @param attendee an attendee
     */
    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    /**
     * Get the organizer
     * @return the organizer
     */
    public Organizer getOrganizer() {return organizer;}

    /**
     * Set the organizer
     * @param organizer a organizer
     */
    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    /**
     * Adds an Attendee document to the "attendees" collection in Firebase.
     * @param attendee
     *      The Attendee object representing the data to add.
     */
    public void addAttendee(Attendee attendee){
        CollectionReference attendeesRef = db.collection("attendees");

        attendeesRef.document(attendee.getAttendeeId()).set(attendee);
    }

    /**
     * Adds an Event document to the "events" collection in Firebase.
     * @param event
     *      The Event object representing the data to add.
     */
    public void addEvent(Event event, AddEventCallback callback){
        CollectionReference attendeesRef = db.collection("events");

        attendeesRef.document(event.getEventId()).set(event).addOnSuccessListener(aVoid -> {
                    callback.onAddEvent(event);
                })
                .addOnFailureListener(e -> {
                    callback.onAddEvent(null);
                });
    }


    /**
     * Adds an Organizer document to the "organizers" collection in Firebase.
     * @param organizer
     *      The Organizer object representing the data to add.
     */
    public void addOrganizer(Organizer organizer){
        CollectionReference organizersRef = db.collection("organizers");

        organizersRef.document(organizer.getOrganizerId()).set(organizer);
    }

    public void updateEvent(String eventId, String field, String value){
        CollectionReference eventRef = FirebaseFirestore.getInstance().collection("events");
        eventRef.document(eventId).update(field, value);
    }

    public void updateAttendee(String attendeeId, String field, String value){
        CollectionReference attendeeRef = FirebaseFirestore.getInstance().collection("attendees");
        attendeeRef.document(attendeeId).update(field, value);
    }

    public void subscribeToTopic(String eventId){
        FirebaseMessaging.getInstance().subscribeToTopic(eventId);
    }
    public void unSubscribeToTopic(String eventId){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(eventId);
    }


    public void getEvent(String eventId, GetEventCallback callback){
        CollectionReference eventsRef = db.collection("events");
        eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Event event = documentSnapshot.toObject(Event.class);
                    if (event != null) {
                        callback.onGetEvent(event);
                    }
                } else {
                    callback.onGetEvent(null);
                }
            }
        }).addOnFailureListener(e -> {
            callback.onGetEvent(null);
        });
    }


    public void addProfileDeletedListener(ProfileDeletedListenerCallback callback){
        CollectionReference attendeesRef = db.collection("attendees");
        attendeesRef.whereEqualTo("attendeeId", attendee.getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null && !snapshots.isEmpty()){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.REMOVED){
                            callback.onProfileDeleted();
                        }
                    }
                }
            }
        });
    }

    public void addAttendeeEventsListener(AttendeeEventsListenerCallback callback) {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.whereArrayContains("signedAttendees", getAttendee().getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null && !snapshots.isEmpty()) {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onAttendeeEventUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

    public void addAllEventsListener(AllEventsListenerCallback callback){
        CollectionReference eventsRef = db.collection("events");
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null && !snapshots.isEmpty()){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onAllEventsUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

    public void addOrganizerEventsListener(OrganizerEventsListenerCallback callback){
        CollectionReference eventsRef = FirebaseFirestore.getInstance().collection("events");

        eventsRef.whereEqualTo("organizerId", getOrganizer().getOrganizerId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null && !snapshots.isEmpty()){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onOrganizerEventsUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }


    public void requestAPI(Event event, String announcement) throws Exception{
        String projectId = "qrazy-scanner";
        String topic = event.getEventId();

        String url = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
        String payload = "{\"message\": {\"topic\": \"" + topic + "\", \"data\": {\"event\": \""+ event.getName() +"\", \"announcement\": \"" + announcement + "\"}}}";
        byte[] output = payload.getBytes(StandardCharsets.UTF_8);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setFixedLengthStreamingMode(output.length);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; UTF-8");
        con.setRequestProperty("Authorization", "Bearer " + getAccessToken());

        try (OutputStream os = con.getOutputStream()) {
            os.write(output);
        }

        con.getResponseCode();
    }

    private String getAccessToken() throws IOException {

        String[] SCOPES = {"https://www.googleapis.com/auth/firebase.messaging"};
        String serviceAccount = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"qrazy-scanner\",\n" +
                "  \"private_key_id\": \"204fcee81c3441fefd193251b9e1d1e88149b826\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDkqx3U6J3btsY\\nX3pnzqz4h39TduDs1cHvFh6l0Ut+2hMHrsDl2mS/wh1FRrG9gfwG7zCVBIbsMBjy\\nVZj7SPQZpeq/X8CKz3EcUW2EqqhRyhMNxwZaPOJd0WH4XXsV4TPLKHvvmqzpIcRs\\naGkm1NKMzpthFnrbI0EAgn6WpJCI50/xKnOjgdgxA210BKf0vT10cNd7Pf3jwfiE\\nntwgvFTUTrbGPH/OuEKNfUHY7GbHia1eaqy2VMlpKi1GDZ4w1IqMCvOjJS1zYaMH\\nRBJGAZjpmET8mJ0X+q2CpI7snq16SHnkObuVALHLfdd75OlLD+Eg9JGCIuilWsdl\\nHSyq3Wm7AgMBAAECggEAAvf/g+IySv4qTOuUx+2fD44S/zkvPzkcHrJ8bZzzOJjm\\nhWKAOB0BaYMbVE63P6lbojP6+KV0cjJlYTcDwaYWWoCmbcLuK81BVMzhojbATlmI\\nbNhIFDzsshWNwf+Sm5RAZ9rp+Ar7iUsYww0vXMZfRIGVtzq9oQT0etBVoUNx6Y9p\\nNGjXNdIlNbinyny5Myd92bQ7HtqN5K7qd4OYsZcgMgp0Xklm9eMZg9hyFwWv7r5o\\nhXSpyirwiCZ26WqNPOhzSuBY5L1uicX12E0RYbzF9drO18tgBvHZR4BQe1k4VycB\\nItvGO/5ouG7bdMl0zwqRLahx2mL4dBW/rUmlbmG45QKBgQDnQglVS/mXBMRG7VYj\\nUb9Apxw0LBGjHwfe902/4jFnF+ViweXlauxQM5AWDCD3WcpUUiDE7esfX0Aqv5CO\\nfwvf8oMIzMzL5roQGI21GgmZUaUKHTCEhbn+bEYfdYH4qfq57p6QOPzV7IaQdZzq\\ncuXlFOvT/XKaYhJjhOFkiT4JbwKBgQDYf0EgDwudB+l/3ldzIHoP4UCyeE0OwJKw\\nlGCKHmxGmbgYKysX+lyiSwLum1DqR0ZVseAqY5D7+Z4U6YbizQMoQ5b8yPdW0Uaz\\nrLrR9rRbCpBzSYq1t6PpoCUOJx3Qz0V741vXqIig8LOg1pys17ljpmZyw7yn4o+E\\nazGwiR2GdQKBgQC/8CVC8E31s/UcUTwfEGhGRuy3uKPi2Yx02JllW11ZjZHLh9dB\\ntJ7yafl68xIhehreJVQhXr65SRs+38QhIP1AIE31bdXEnnlrhpWG7FdvMz5hyJxO\\nQZd/vWnuDl+TfbElxRFB7qqa+zcsixFz3W1F1zlst3z4+dD9XHqeMPKWbQKBgH+v\\nLb22oebPT8t2WqUvtk2/T+TyRqA4u0shd35+SuWoq4a1jwjpQ9ED5IrNV3+U4cqQ\\nyeC2MEAsDCvRPxhsSTxqAJa+AAJYExbM/LHwipZXOLKF4SUjVazoInKiZ1dLp3NV\\nuEkMwOgKjiaB7I2T/WbkMO/muVFascIrZnbzp1IxAoGADKodaq7Wi9gWZde6MY6S\\nT/R8pCDYoyQ0p7YUFz05ZzZrjfJ6GWi0N0e/znjJElYPswJYlNLcsSUR7INGJEHB\\npPhdh6pJrL13NMboCUPdHa1KRMq9GcH0TTrYiuHwssq2V3OYifhNsR5G2/70/XSx\\nFnUiD9n4lg5/2Roalmk5+oo=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"firebase-adminsdk-cfln6@qrazy-scanner.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"115043820653373096359\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-cfln6%40qrazy-scanner.iam.gserviceaccount.com\",\n" +
                "  \"universe_domain\": \"googleapis.com\"\n" +
                "}";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(serviceAccount.getBytes()))
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

}
