package com.example.projectapp;

import android.media.Image;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class for Attendee
 */
public class Attendee {
    private String profilePic;
    private String name;
    private String homepage;
    private String contactInfo;
    private String attendeeId;
    private ArrayList<String> events;

    /**
     * Attendee Constructor
     */
    public Attendee(){
    }

    public Attendee(String profilePic, String name, String homepage, String contactInfo) {
        this.profilePic = profilePic;
        this.name = name;
        this.homepage = homepage;
        this.contactInfo = contactInfo;
        attendeeId = UUID.randomUUID().toString();
        events = new ArrayList<>();
    }

    /**
     * Get the attendee's profile picture
     * @return Attendee's picture
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Set the attendee's profile picture
     * @param profilePic an Image for profile pic
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Get the attendee's name
     * @return Attendee's name
     */
    public String getName() {
        return name;
    }


    /**
     * Set the attendee's name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the attendee's homepage
     * @return Attendee's homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Set the attendee's homepage
     * @param homepage new homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Get the attendee's contact information
     * @return
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the attendee's homepage
     * @param contactInfo new Contact Information
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Get the attendee's ID
     * @return ID
     */
    public String getAttendeeId() {
        return attendeeId;
    }

    /**
     * get Events
     * @return user's events
     */
    public ArrayList<String> getEvents() {
        return events;
    }

    /**
     * Add event
     * @param eventId the event added
     */
    public void addEvent(String eventId){
        if (!events.contains(eventId)){
            events.add(eventId);
            DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("attendees").document(attendeeId);
            attendeeRef.update("events", events);
        }
    }

}
