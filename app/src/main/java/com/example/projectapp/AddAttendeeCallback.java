package com.example.projectapp;

import com.example.projectapp.Model.Attendee;

public interface AddAttendeeCallback {
    void onAddAttendee(Attendee attendee, boolean newAttendee);
}
