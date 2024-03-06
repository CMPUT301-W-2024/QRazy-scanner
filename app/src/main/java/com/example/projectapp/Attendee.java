package com.example.projectapp;

import android.media.Image;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class Attendee {
    private Image profilePic;
    private String name;
    private String homepage;
    private String contactInfo;
    private String attendeeId;
    private HashMap<String, Integer> events;

    public Attendee(){
        attendeeId = UUID.randomUUID().toString();
        events = new HashMap<>();
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public HashMap<String, Integer> getEvents() {
        return events;
    }

    public void addEvent(String eventId){
        if (!events.containsKey(eventId)){
            events.put(eventId, 0);
        }
        else{
            Integer checkIns = events.get(eventId) + 1;
            events.put(eventId, checkIns);
        }
        DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("attendees").document(attendeeId);
        attendeeRef.update("events", events);
    }

}
