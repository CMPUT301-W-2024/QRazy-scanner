package com.example.projectapp;

import com.example.projectapp.Model.Attendee;

public interface GetAttendeeCallback {
    void onGetAttendee(Attendee attendee, boolean deleted);
}
