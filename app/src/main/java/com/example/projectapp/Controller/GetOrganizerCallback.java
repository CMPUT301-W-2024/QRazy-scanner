package com.example.projectapp.Controller;

import com.example.projectapp.Model.Organizer;

/**
 * An interface defining a callback mechanism to signal the getting of an organizer.
 */
public interface GetOrganizerCallback {
    void onGetOrganizer(Organizer organizer);
}
