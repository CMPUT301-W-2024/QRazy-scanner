package com.example.projectapp;

import java.util.ArrayList;

/**
 * Holds a list of attendee IDs to keep track of all attendees
 */
public class AttendeeTracker {
    private ArrayList<String> attendees;

    public AttendeeTracker(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(String attendee){
        if (!attendees.contains(attendee)){
            attendees.add(attendee);
        }
    }
}
