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
    
    private String Description;

    /**
     * get event description
     * @return a description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * set event's description
     * @param description a description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Event constructor
     */

    public Event(){
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

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


    

    /**
     * Get Attendence
     * @return number of Attendees
     */
    public Integer getAttendance() {
        return attendees.size();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAttendanceLimit() {
        return attendanceLimit;
    }

    public void setAttendanceLimit(Integer attendanceLimit) {
        this.attendanceLimit = attendanceLimit;
    }

    /**
     * Add an attendee
     * @param attendeeId
     */
    public void addAttendee(String attendeeId){
        if (!attendees.containsKey(attendeeId)){
            attendees.put(attendeeId, 0);
        }
        else{
            Integer checkIns = attendees.get(attendeeId) + 1;
            attendees.put(attendeeId, checkIns);
        }
        DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
        attendeeRef.update("attendees", attendees);
    }
}
