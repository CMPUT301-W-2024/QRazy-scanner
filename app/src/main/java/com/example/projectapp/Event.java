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
    private String date;
    private String organizer;
    private String description;
    private HashMap<String, Integer> attendees;
    private Integer attendanceLimit;
    private Image poster;
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

    public Image getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }

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
    public void eventtodb(){
        DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("events").document(eventId);
    }
}
