package com.example.projectapp.Controller;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Data handler. Handles interactions with Firebase,
 * stores attendee and organizer data, and sets up listeners for data changes.
 */
public class DataHandler {

    private static DataHandler instance;
    private FirebaseFirestore db;
    private CollectionReference attendeesRef;
    private CollectionReference organizersRef;
    private CollectionReference eventsRef;
    private Attendee localAttendee;
    private Organizer localOrganizer;

    /**
     * Private constructor initializes the DataHandler with
     * Firestore database, collection references, and settings.
     */
    public DataHandler(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setLocalCacheSettings(MemoryCacheSettings.newBuilder().build()).build();
        db.setFirestoreSettings(settings);
        attendeesRef = db.collection("attendees");
        organizersRef = db.collection("organizers");
        eventsRef = db.collection("events");
    }

    /**
     * Retrieves the singleton instance of DataHandler.
     *
     * @return
     *      The singleton instance of DataHandler.
     */
    public static DataHandler getInstance(){
        if (instance == null){
            instance = new DataHandler();
        }
        return instance;
    }

    /**
     * Get the local attendee object.
     *
     * @return
     *      The local attendee object.
     */
    public Attendee getLocalAttendee() {
        return localAttendee;
    }

    /**
     * Set the local attendee object.
     *
     * @param attendee
     *      The local attendee object to set.
     */
    public void setLocalAttendee(Attendee attendee) {
        this.localAttendee = attendee;
    }

    /**
     * Get the local organizer object.
     *
     * @return
     *      The local organizer object.
     */
    public Organizer getLocalOrganizer() {return localOrganizer;}

    /**
     * Set the local organizer object.
     *
     * @param organizer
     *      The local organizer object to set.
     */
    public void setLocalOrganizer(Organizer organizer) {
        this.localOrganizer = organizer;
    }

    /**
     * Adds an Attendee document to the "attendees" collection in Firebase.
     *
     * @param attendee    The Attendee object representing the data to add.
     * @param newAttendee Indicates if the attendee is new or existing.
     * @param callback    Callback to handle the result of the operation.
     */
    public void addAttendee(Attendee attendee, boolean newAttendee, AddAttendeeCallback callback){
        DocumentReference attendeeDocRef = attendeesRef.document(attendee.getAttendeeId());

        attendeeDocRef.set(attendee).addOnSuccessListener(aVoid -> {
                    callback.onAddAttendee(attendee, newAttendee);
                })
                .addOnFailureListener(e -> {
                    callback.onAddAttendee(null, newAttendee);
                });
    }

    /**
     * Adds an Event document to the "events" collection in Firebase.
     *
     * @param event    The Event object representing the data to add.
     * @param callback Callback to handle the result of the operation.
     */
    public void addEvent(Event event, AddEventCallback callback){
        DocumentReference eventDocRef = eventsRef.document(event.getEventId());
        eventDocRef.set(event).addOnSuccessListener(aVoid -> {
                    callback.onAddEvent(event);
                })
                .addOnFailureListener(e -> {
                    callback.onAddEvent(null);
                });
    }

    /**
     * Adds the local organizer to the Firebase Firestore database.
     *
     * @param callback
     *      The callback for the operation result.
     */
    public void addOrganizer(AddOrganizerCallback callback){
        DocumentReference organizerDocRef =  organizersRef.document(localOrganizer.getOrganizerId());

        organizerDocRef.set(localOrganizer).addOnSuccessListener(aVoid -> {
                    callback.onAddOrganizer(localOrganizer);
                })
                .addOnFailureListener(e -> {
                    callback.onAddOrganizer(null);
                });
    }

    /**
     * Retrieves an attendee from the Firebase Firestore database.
     *
     * @param attendeeId The ID of the attendee to retrieve.
     * @param callback   The callback for the retrieved attendee.
     */
    public void getAttendee(String attendeeId, GetAttendeeCallback callback){
        DocumentReference attendeeDocRef = attendeesRef.document(attendeeId);

        attendeeDocRef.get().addOnSuccessListener( documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                if (attendee != null) {
                    callback.onGetAttendee(attendee, false);
                }
            }
            else {
                callback.onGetAttendee(null, true);
            }
        }).addOnFailureListener(e -> callback.onGetAttendee(null, false));
    }

    /**
     * Retrieves an event document from Firebase based on the event ID.
     *
     * @param eventId  The ID of the event to retrieve.
     * @param callback Callback to handle the result of the operation.
     */
    public void getEvent(String eventId, GetEventCallback callback){
        DocumentReference eventDocRef = eventsRef.document(eventId);
        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    callback.onGetEvent(event);
                }
            } else {
                callback.onGetEvent(null);
            }
        }).addOnFailureListener(e -> {
            callback.onGetEvent(null);
        });
    }

    /**
     * Retrieves an organizer document from Firebase based on the organizer ID.
     *
     * @param organizerId The ID of the organizer to retrieve.
     * @param callback    Callback to handle the result of the operation.
     */
    public void getOrganizer(String organizerId, GetOrganizerCallback callback){
        DocumentReference organizerDocRef = organizersRef.document(organizerId);

        organizerDocRef.get().addOnSuccessListener( documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Organizer organizer = documentSnapshot.toObject(Organizer.class);
                if (organizer != null) {
                    callback.onGetOrganizer(organizer);
                }
            }
        }).addOnFailureListener(e -> callback.onGetOrganizer(null));
    }

    /**
     * Updates an event with the specified eventId, field, and value.
     *
     * @param eventId  The ID of the event to update.
     * @param field    The field in the event to update.
     * @param value    The new value for the field.
     * @param callback Callback to handle the update event operation result.
     */
    public void updateEvent(String eventId, String field, Object value, UpdateEventCallback callback){
        DocumentReference eventDocRef = eventsRef.document(eventId);
        eventDocRef.update(field, value).addOnSuccessListener(aVoid -> {
                    if (callback != null)
                        callback.onUpdateEvent(eventId);})
                .addOnFailureListener(e -> {
                    if (callback != null)
                        callback.onUpdateEvent(null);
                });
    }

    /**
     * Updates an attendee with the specified attendeeId, field, and value.
     *
     * @param attendeeId The ID of the attendee to update.
     * @param field      The field in the attendee to update.
     * @param value      The new value for the field.
     * @param callback   Callback to handle the update attendee operation result.
     */
    public void updateAttendee(String attendeeId, String field, Object value, UpdateAttendeeCallback callback){
        DocumentReference attendeeDocRef = attendeesRef.document(attendeeId);
        attendeeDocRef.update(field, value).addOnSuccessListener(aVoid -> {
                    if (callback != null)
                        callback.onUpdateAttendee(attendeeId);})
                .addOnFailureListener(e -> {
                    if (callback != null)
                        callback.onUpdateAttendee(null);
                });
    }

    /**
     * Deletes an attendee with the specified attendeeId.
     *
     * @param attendeeId The ID of the attendee to delete.
     * @param callback   Callback to handle the delete attendee operation result.
     */
    public void deleteAttendee(String attendeeId, DeleteAttendeeCallback callback){
        DocumentReference attendeeDocRef = attendeesRef.document(attendeeId);
        attendeeDocRef.delete().addOnSuccessListener(aVoid -> callback.onDeleteAttendee(attendeeId))
                .addOnFailureListener(e -> callback.onDeleteAttendee(null));
    }

    /**
     * Deletes an event with the specified eventId.
     *
     * @param eventId  The ID of the event to delete.
     * @param callback Callback to handle the delete event operation result.
     */
    public void deleteEvent(String eventId, DeleteEventCallback callback){
        DocumentReference eventDocRef = eventsRef.document(eventId);
        eventDocRef.delete().addOnSuccessListener(aVoid -> callback.onDeleteEvent(eventId))
                .addOnFailureListener(e -> callback.onDeleteEvent(null));
    }

    /**
     * Retrieves the QR code for the specified event ID and type.
     *
     * @param eventId     The ID of the event to retrieve the QR code for.
     * @param qrCodeType  The type of QR code to retrieve.
     * @param callback    Callback to handle the QR code retrieval result.
     */
    public void getQRCode(String eventId, String qrCodeType, GetQrCodeCallback callback) {
        DocumentReference eventDocRef = eventsRef.document(eventId);

        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String qrCodeString = documentSnapshot.getString(qrCodeType);
                        if (qrCodeString != null && !qrCodeString.isEmpty()) {
                            callback.onGetQrCode(qrCodeString);
                        } else {
                            callback.onGetQrCode(null);
                        }
                    } else {
                        callback.onGetQrCode(null);
                    }
                })
                .addOnFailureListener(e -> callback.onGetQrCode(null));
    }

    /**
     * Adds a listener for changes to the local attendee's data.
     *
     * @param callback
     *      Callback to handle local attendee updates.
     */
    public void addLocalAttendeeListener(LocalAttendeeListenerCallback callback){
        attendeesRef.whereEqualTo("attendeeId", localAttendee.getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.REMOVED){
                            if (callback != null){
                                callback.onLocalAttendeeDeleted();
                            }
                        }
                        else {
                            localAttendee = dc.getDocument().toObject(Attendee.class);
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds a listener for changes to the attendee's events.
     *
     * @param getCheckedIn Whether to retrieve checked-in events.
     * @param callback     Callback to handle attendee events updates.
     */
    public void addAttendeeEventsListener(boolean getCheckedIn, AttendeeEventsListenerCallback callback) {

        Query query;
        if (getCheckedIn){
            query = eventsRef.orderBy("checkedAttendees." + localAttendee.getAttendeeId());
        }
        else {
            query = eventsRef.whereArrayContains("signedAttendees", localAttendee.getAttendeeId());
        }
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null) {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onAttendeeEventsUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

    /**
     * Adds a listener for changes to the events data.
     *
     * @param callback
     *      Callback to handle events updates.
     */
    public void addEventsListener(EventsListenerCallback callback){
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onEventsUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

    /**
     * Adds a listener for changes to the attendees data.
     *
     * @param callback
     *      Callback to handle attendees updates.
     */
    public void addAttendeesListener(AttendeesListenerCallback callback){
        attendeesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Attendee attendee = dc.getDocument().toObject(Attendee.class);
                        callback.onAttendeesUpdated(dc.getType(), attendee);
                    }
                }
            }
        });
    }

    /**
     * Adds a listener for changes to the organizer's events.
     *
     * @param callback
     *      Callback to handle organizer events updates.
     */
    public void addOrganizerEventsListener(OrganizerEventsListenerCallback callback){

        eventsRef.whereEqualTo("organizerId", localOrganizer.getOrganizerId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onOrganizerEventsUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

    /**
     * Adds a listener for changes to the attendees of a specific event.
     *
     * @param event         The event to listen for attendee changes.
     * @param getCheckedIn  Whether to retrieve checked-in attendees.
     * @param callback      Callback to handle event attendees updates.
     */
    public void addEventAttendeesListener(Event event, boolean getCheckedIn, EventAttendeesListenerCallback callback){

        Query query;
        if (getCheckedIn){
            query = attendeesRef.orderBy("checkedInEvents." + event.getEventId());
        }
        else {
            query = attendeesRef.whereArrayContains("signedUpEvents", event.getEventId());
        }

        query.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Attendee attendee = dc.getDocument().toObject(Attendee.class);
                        if (getCheckedIn){
                            callback.onEventCheckedAttendeesUpdated(dc.getType(), attendee);
                        }
                        else {
                            callback.onEventSignedAttendeesUpdated(dc.getType(), attendee);
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds an event listener for geopoint updates related to a specific event.
     *
     * @param eventId   The unique ID of the event to monitor.
     * @param callback  The callback object to be invoked when geopoint updates occur.
     */
    public void addEventGeoPointsListener(String eventId, EventGeoPointsListenerCallback callback){
        DocumentReference eventDocRef = eventsRef.document(eventId);

        eventDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot != null && snapshot.exists()) {
                    // Document exists, access the geopoints field
                    HashMap<String, List<GeoPoint>> map = (HashMap<String, List<GeoPoint>>) snapshot.get("geopoints");
                    List<GeoPoint> geoPoints = new ArrayList<>();
                    for (String attendee : map.keySet()){
                        geoPoints.addAll(map.get(attendee));
                    }
                    callback.onEventGeoPointsUpdated(geoPoints);

                } else {
                    // Document does not exist
                    callback.onEventGeoPointsUpdated(null);
                }
            }
        });
    }

    /**
     * Adds a listener for changes to the images in a collection.
     *
     * @param collection The name of the collection to listen for image changes.
     * @param field      The field containing image data.
     * @param layout     The layout to update with images.
     * @param callback   Callback to handle image updates.
     */
    public void addImagesListener(String collection, String field, LinearLayout layout, ImagesListenerCallback callback) {
        db.collection(collection).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (snapshots != null) {
                    HashMap<String, String> images = new HashMap<>();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        String encodedImage = document.getString(field);
                        if (encodedImage != null && !encodedImage.isEmpty()) {
                            images.put(document.getId(), encodedImage);
                        }
                    }
                    callback.onImagesUpdated(images, layout,collection, field);
                }
            }
        });
    }

    /**
     * Retrieves the event associated with the given QR code data.
     *
     * @param qrData    The QR code data.
     * @param checkInto Whether to check into the event.
     * @param callback  Callback to handle the event retrieval result.
     */
    public void getQrCodeEvent(String qrData, boolean checkInto, GetQrCodeEventCallback callback){

        Query query = eventsRef.where(Filter.or(Filter.equalTo("eventId", qrData), Filter.equalTo("qrCode", hashEventCode(qrData))));
        query.get().addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()){
                callback.onGetQrCodeEvent(null, checkInto, hashEventCode(qrData));
            }
            else {
                List<Event> events = documentSnapshots.toObjects(Event.class);
                callback.onGetQrCodeEvent(events.get(0), checkInto, hashEventCode(qrData));
            }
        });
    }

    /**
     * Hashes the event code using SHA-256 algorithm.
     *
     * @param code
     *      The event code to hash.
     * @return
     *      The hashed event code.
     */
    private String hashEventCode(String code){
        return Hashing.sha256()
                .hashString(code, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     * Gets device token for notification and adds to database
     */
    public void addFcmToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String token = task.getResult().toString();
                updateAttendee(localAttendee.getAttendeeId(), "fcmToken", token, null);
            }
        });
    }

    /**
     * Sends a notification to subscribers of a specific event.
     *
     * @param token         The device token to send send the notification to
     * @param eventName         The event for which to send the notification.
     * @param announcement  The announcement to include in the notification.
     * @throws Exception    If there is an error sending the notification.
     */
    public void sendNotification(String token, String eventName, String announcement) throws Exception{
        String projectId = "qrazy-scanner";

        String url = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
        String payload = "{\"message\": {\"token\": \"" + token + "\", \"data\": {\"event\": \""+ eventName +"\", \"announcement\": \"" + announcement + "\"}}}";
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

    /**
     * Retrieves the access token required for sending notifications.
     * NOT SAFE. BUT PROF ALLOWED
     * @return
     *      The access token.
     * @throws IOException
     *      If there is an error retrieving the access token.
     */
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
