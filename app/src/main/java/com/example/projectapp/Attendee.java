package com.example.projectapp;

import android.media.Image;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;

public class Attendee {
    private Image profilePic;
    private String name;
    private String homepage;
    private String contactInfo;
    private String attendeeID;
    private HashMap<String, Integer> events;

    public Attendee(){
        this.attendeeID = UUID.randomUUID().toString();
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

    public String getAttendeeID() {
        return attendeeID;
    }

    public HashMap<String, Integer> getEvents() {
        return events;
    }

    public void addEvent(String event){
        if (!events.containsKey(event)){
            events.put(event, 0);
        }
        else{
            Integer checkIns = events.get(event) + 1;
            events.put(event, checkIns);
        }
        DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("attendees").document(attendeeID);
        attendeeRef.update("events", events);
    }

}
