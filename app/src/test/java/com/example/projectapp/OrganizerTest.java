package com.example.projectapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrganizerTest {

    private Organizer organizer;

    @BeforeEach
    void setUp() {
        organizer = new Organizer("Test Name");
    }

    @Test
    void testNameGetterAndSetter() {
        String newName = "New Test Name";
        organizer.setName(newName);
        assertEquals(newName, organizer.getName(), "Names do not match");
    }

    @Test
    void testOrganizerIdGetterAndSetter() {
        String newOrganizerId = "newOrganizerId123";
        organizer.setOrganizerId(newOrganizerId);
        assertEquals(newOrganizerId, organizer.getOrganizerId(), "Organizer IDs do not match");
    }

    @Test
    void testEventsGetterAndSetter() {
        ArrayList<String> newEvents = new ArrayList<>();
        newEvents.add("Event1");
        newEvents.add("Event2");
        organizer.setEvents(newEvents);
        assertEquals(newEvents, organizer.getEvents(), "Events lists do not match");
        assertTrue(organizer.getEvents().contains("Event1") && organizer.getEvents().contains("Event2"), "Events list does not contain the correct events");
    }
}
