package com.example.projectapp;


import android.media.Image;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

// temporary event class so that conflicts dont occur
public class Event {
    private String eventId;
    private String name;
    private String organizer;
    private String description;
    private HashMap<String, Integer> attendees;
    private String poster;
    public Event(){
        eventId = UUID.randomUUID().toString();
        attendees = new HashMap<>();
    }

    public String getEventId() {
        return eventId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, Integer> getAttendees() {
        return attendees;
    }

    public void setAttendees(HashMap<String, Integer> attendees) {
        this.attendees = attendees;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Integer getAttendance() {
        return attendees.size();
    }

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