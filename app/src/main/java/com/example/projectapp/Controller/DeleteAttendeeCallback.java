package com.example.projectapp.Controller;

/**
 * An interface defining a callback mechanism to signal the delete of an attendee.
 */
public interface DeleteAttendeeCallback {
    void onDeleteAttendee(String attendeeId);
}
