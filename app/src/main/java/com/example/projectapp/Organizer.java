package com.example.projectapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

public class Organizer {
    private String organizerId;
    private ArrayList<String> events;

    /**
     * constructor
     */
    public Organizer() {
        organizerId = UUID.randomUUID().toString();
        events = new ArrayList<>();
    }

    /**
     * get organizer's Id
     * @return Organizer's ID
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * set organizer's ID
     * @param organizerId
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * get events
     * @return events
     */
    public ArrayList<String> getEvents() {
        return events;
    }

    /**
     * set events
     * @param events events
     */
    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    /**
     * add events
     * @param eventId event's id to add
     */
    public void addEvent(String eventId){
        if (!events.contains(eventId)){
            events.add(eventId);
            DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("organizers").document(organizerId);
            attendeeRef.update("events", events);
        }
    }
}
