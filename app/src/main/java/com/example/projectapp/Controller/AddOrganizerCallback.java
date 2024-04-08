package com.example.projectapp.Controller;

import com.example.projectapp.Model.Organizer;

/**
 * An interface defining a callback mechanism to signal the addition of an organizer.
 */
public interface AddOrganizerCallback {
    void onAddOrganizer(Organizer organizer);
}
