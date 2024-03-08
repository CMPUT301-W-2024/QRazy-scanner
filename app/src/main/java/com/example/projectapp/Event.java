package com.example.projectapp;


import android.media.Image;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

// temporary event class so that conflicts dont occur
public class Event {

    private String eventId;
    private String name;
    private String date;
    private String organizer;
    private String description;
    private HashMap<String, Integer> attendees;
    private String poster;
    private Integer attendanceLimit;
    public interface eventListener{

    }

    /**
     * Event constructor
     */
    public Event(){
/*        eventId = UUID.randomUUID().toString();
        attendees = new HashMap<>();*/
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
        attendees = new HashMap<>();
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
    public HashMap<String, Integer> getAttendees() {
        return attendees;
    }

    /**
     * Set Attendees
     * @param attendees
     */
    public void setAttendees(HashMap<String, Integer> attendees) {
        this.attendees = attendees;
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
        return attendees.size();
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
     * Add an attendee
     * @param attendeeId
     */
    public void addAttendee(String attendeeId){
        if (!attendees.containsKey(attendeeId)){
            attendees.put(attendeeId, 1);
        }
        else{
            Integer checkIns = attendees.get(attendeeId) + 1;
            attendees.put(attendeeId, checkIns);
        }
        DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
        attendeeRef.update("attendees", attendees);
    }
}
