package com.example.projectapp.Controller;

import com.example.projectapp.Model.Attendee;

/**
 * An interface defining a callback mechanism to signal the getting of an attendee.
 */
public interface GetAttendeeCallback {
    void onGetAttendee(Attendee attendee, boolean deleted);
}
