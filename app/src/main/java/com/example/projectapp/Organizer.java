package com.example.projectapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Setting up Organizer
 */
public class Organizer {
    private String organizerId;
    private ArrayList<String> events;
    private String name;

    /**
     * constructor
     */
    public Organizer() {

    }

    public Organizer(String name){
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
