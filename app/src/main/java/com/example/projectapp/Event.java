package com.example.projectapp;


import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Setting up event class
 */
public class Event implements Serializable{

    private String eventId;
    private String promoQrId;

    private String name;
    private String date;
    private String startTime;
    private String endTime;
    private String organizerName;
    private String organizerId;
    private String description;
    private HashMap<String, Integer> checkedAttendees;
    private ArrayList<String> signedAttendees;
    private String poster;
    private Integer attendanceLimit;
    private String qrCode;
    private String promoQrCode;
    private ArrayList<GeoPoint> geopoints;
    private ArrayList<Announcement> announcements;

    /**
     * Empty Event constructor required for firebase
     */
    public Event(){

    }

    /**
     * Creates a new Event object with specified details.
     *
     * @param name            The name of the event.
     * @param date            The date the event occurs
     * @param startTime             The time the event starts
     * @param endTime       The time the event ends.
     * @param organizerName       The name of the event organizer.
     * @param organizerId     The id of the event organizer.
     * @param attendanceLimit The maximum number of allowed attendees.
     * @param description     A description of the event.
     * @param poster          A poster image associated with the event.
     */
    public Event(String name, String date, String startTime, String endTime, String organizerName, String organizerId, Integer attendanceLimit, String description, String poster) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizerName = organizerName;
        this.organizerId = organizerId;
        this.attendanceLimit = attendanceLimit;
        this.description = description;
        this.poster = poster;
        eventId = UUID.randomUUID().toString();
        promoQrId = "Promo" + eventId;
        checkedAttendees = new HashMap<>();
        signedAttendees = new ArrayList<>();
        geopoints = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Get Event ID
     * @return EVENT ID
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Set event's ID
     * @param eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPromoQrId() {
        return promoQrId;
    }

    public void setPromoQrId(String promoQrId) {
        this.promoQrId = promoQrId;
    }

    /**
     * Gets the name of the event.
     *
     * @return The event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name The new name for the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organizer of the event.
     *
     * @return The organizer's name.
     */
    public String getOrganizerName() {
        return organizerName;
    }

    /**
     * Sets the organizer name of the event.
     *
     * @param organizerName The new organizer's name.
     */
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    /**
     * Gets the organizer ID of the event.
     *
     * @return The organizer's ID.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer ID of the event.
     *
     * @param organizerId The new organizer's ID.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Gets a HashMap representing event attendees and their check-in counts.
     *
     * @return A HashMap where keys are attendee IDs and values are check-in counts.
     */
    public HashMap<String, Integer> getCheckedAttendees() {
        return checkedAttendees;
    }

    /**
     * Set Attendees
     * @param checkedAttendees
     */
    public void setCheckedAttendees(HashMap<String, Integer> checkedAttendees) {
        this.checkedAttendees = checkedAttendees;
    }

    /**
     * get the Poster
     * @return poster
     */
    public String getPoster() {
        return poster;
    }

  /**
     * Set Event's poster
     * @param poster a poster Image
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    

    /**
     * Get Attendence
     * @return number of Attendees
     */
    public Integer getAttendance() {
        return checkedAttendees.size();
    }

    /**
     * Get date on which event occurs.
     *
     * @return The event date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date on which the event occurs.
     *
     * @param date The new event date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get time at which event starts.
     *
     * @return The event start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the time at which the event starts.
     *
     * @param startTime The new event start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Get time at which event ends.
     *
     * @return The event end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the time at which the event ends.
     *
     * @param endTime The new event end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the attendance limit for the event.
     *
     * @return The maximum number of allowed attendees.
     */
    public Integer getAttendanceLimit() {
        return attendanceLimit;
    }

    /**
     * Sets the attendance limit for the event.
     *
     * @param attendanceLimit The new maximum number of allowed attendees.
     */
    public void setAttendanceLimit(Integer attendanceLimit) {
        this.attendanceLimit = attendanceLimit;
    }


    /**
     * get event description
     * @return a description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set event's description
     * @param description a description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get event QR Code
     * @return a qrCode encoded as a string
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * set event's QR Code
     * @param qrCode an encoded QR code
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * get signed up attendees
     * @return user's signed up for events
     */
    public ArrayList<String> getSignedAttendees() {
        return signedAttendees;
    }

    /**
     * set checked in attendees
     * @param  signedAttendees list of signed up attendees
     */
    public void setSignedAttendees(ArrayList<String> signedAttendees) {
        this.signedAttendees = signedAttendees;
    }

    /**
     * Check in an attendee
     * @param attendeeId the attendee who checked in
     */
    public void addCheckedAttendee(String attendeeId){
        if (!checkedAttendees.containsKey(attendeeId)){
            checkedAttendees.put(attendeeId, 1);
        }
        else{
            Integer checkIns = checkedAttendees.get(attendeeId) + 1;
            checkedAttendees.put(attendeeId, checkIns);
        }
        DocumentReference eventRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
        eventRef.update("checkedAttendees", checkedAttendees);
    }

    /**
     * Sign up an attendee
     * @param attendeeId the attendee who signed
     */
    public void addSignedAttendee(String attendeeId){
        if (!signedAttendees.contains(attendeeId)){
            signedAttendees.add(attendeeId);
            DocumentReference eventRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
            eventRef.update("signedAttendees", signedAttendees);
        }
    }

    public void addGeopoint(GeoPoint geopoint){
        DocumentReference eventRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ArrayList<GeoPoint> existingLatitudes = documentSnapshot.toObject(Event.class).getGeopoints();
                if (existingLatitudes == null) {
                    existingLatitudes = new ArrayList<>();
                }
                // Add the new latitude to the existing array
                existingLatitudes.add(geopoint);
                // Update the Firestore document with the modified latitude array
                eventRef.update("latitude", existingLatitudes);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors
        });
    }

    public ArrayList<GeoPoint> getGeopoints() {
        return geopoints;
    }

    public void setGeopoints(ArrayList<GeoPoint> geopoints) {
        this.geopoints = geopoints;
    }

    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    public void addAnnouncements(String announcement){
        LocalTime time = LocalTime.now();
        announcements.add(new Announcement(announcement, time.toString(), name, organizerName));
        DocumentReference eventRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
        eventRef.update("announcements", announcements);
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == null){
            return false;
        }

        if (this == obj){
            return true;
        }

        return (obj instanceof Event) && ((Event) obj).getEventId().equals(eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    public String getPromoQrCode() {
        return promoQrCode;
    }

    public void setPromoQrCode(String promoQrCode) {
        this.promoQrCode = promoQrCode;
    }
}

