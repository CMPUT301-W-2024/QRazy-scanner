package com.example.projectapp.Controller;

import com.example.projectapp.Model.Event;

/**
 * An interface defining a callback mechanism to signal the getting of an event.
 */
public interface GetEventCallback {
    void onGetEvent(Event event);
}
