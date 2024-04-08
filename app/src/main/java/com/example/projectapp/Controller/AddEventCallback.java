package com.example.projectapp.Controller;

import com.example.projectapp.Model.Event;

/**
 * An interface defining a callback mechanism to signal the addition of an event.
 */
public interface AddEventCallback {
    void onAddEvent(Event event);
}
