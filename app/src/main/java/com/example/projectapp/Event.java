package com.example.projectapp;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Event implements Serializable{

    private String eventId;
    private String name;
    private String date;
    private String organizer;
    private String description;
    private HashMap<String, Integer> checkedAttendees;
    private ArrayList<String> signedAttendees;
    private String poster;
    private Integer attendanceLimit;
    private String qrCode;

    /**
     * Event constructor
     */
    public Event(){

    }

    /**
     * Creates a new Event object with specified details.
     *
     * @param name            The name of the event.
     * @param date            The date the event occurs (consider a proper Date object).
     * @param organizer       The name of the event organizer.
     * @param attendanceLimit The maximum number of allowed attendees.
     * @param description     A description of the event.
     * @param poster          A poster image associated with the event.
     */
    public Event(String name, String date, String organizer, Integer attendanceLimit, String description, String poster) {
        this.name = name;
        this.date = date;
        this.organizer = organizer;
        this.attendanceLimit = attendanceLimit;
        this.description = description;
        this.poster = poster;
        eventId = UUID.randomUUID().toString();
        checkedAttendees = new HashMap<>();
        signedAttendees = new ArrayList<>();
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
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Sets the organizer of the event.
     *
     * @param organizer The new organizer's name.
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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
     * @return The event date (consider a proper Date object).
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date on which the event occurs.
     *
     * @param date The new event date (consider using a proper Date object).
     */
    public void setDate(String date) {
        this.date = date;
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
}
