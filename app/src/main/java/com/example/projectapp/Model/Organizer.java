package com.example.projectapp.Model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Setting up Organizer class
 */
public class Organizer {
    private String organizerId;
    private ArrayList<String> events;
    private String name;

    /**
     * Default constructor for creating an instance of Organizer with no initial values.
     */
    public Organizer() {

    }

    /**
     * Constructs an Organizer with a specified name.
     * A unique organizer ID is generated,
     * and an empty list of events is initialized.
     *
     * @param name
     *      The name of the organizer.
     */
    public Organizer(String name){
        this.name = name;
        organizerId = UUID.randomUUID().toString();
        events = new ArrayList<>();
    }

    /**
     * Gets the unique ID of the organizer.
     *
     * @return
     *      The organizer's unique ID.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the unique ID of the organizer.
     *
     * @param organizerId
     *      The new unique ID for the organizer.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Gets the list of events associated with the organizer.
     *
     * @return
     *      The list of events.
     */
    public ArrayList<String> getEvents() {
        return events;
    }

    /**
     * Sets the list of events associated with the organizer.
     *
     * @param events
     *      The new list of events.
     */
    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    /**
     * Gets the name of the organizer.
     *
     * @return
     *      The name of the organizer.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the organizer.
     *
     * @param name
     *      The new name for the organizer.
     */
    public void setName(String name) {
        this.name = name;
    }
}
