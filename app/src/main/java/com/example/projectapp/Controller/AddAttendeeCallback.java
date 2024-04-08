package com.example.projectapp.Controller;

import com.example.projectapp.Model.Attendee;

/**
 * An interface defining a callback mechanism to signal the addition of an attendee.
 */
public interface AddAttendeeCallback {
    void onAddAttendee(Attendee attendee, boolean newAttendee);
}
