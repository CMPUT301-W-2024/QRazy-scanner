package com.example.projectapp;

import java.util.ArrayList;

public class AttendeeTracker {
    private ArrayList<Attendee> attendees;

    public AttendeeTracker(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(Attendee attendee){
        if (!attendees.contains(attendee)){
            attendees.add(attendee);
        }
    }
}
