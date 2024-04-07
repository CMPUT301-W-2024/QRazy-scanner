package com.example.projectapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.projectapp.Model.Organizer;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class OrganizerTest {
    public Organizer organizer = new Organizer("Test Organizer Name");

    /**
     * Tests for Organizer Name getter and setter.
     */
    @Test
    public void testNameGetterAndSetter() {
        // Checks if field is not empty and not null
        assertNotEquals("", organizer.getName());
        assertNotNull(organizer.getName());

        // Checks getter
        assertNotEquals("Not Organizer Name", organizer.getName());
        assertEquals("Test Organizer Name", organizer.getName());

        // Checks setter
        organizer.setName("New Organizer Name");
        assertEquals("New Organizer Name", organizer.getName());
    }

    /**
     * Tests for Organizer ID getter and setter.
     */
    @Test
    public void testOrganizerIdGetterAndSetter() {
        // Check if null
        assertNotNull(organizer.getOrganizerId());

        // Checks setter
        organizer.setOrganizerId("New Organizer ID");
        assertEquals("New Organizer ID", organizer.getOrganizerId());

        // Checks getter
        assertNotEquals("Not New Organizer ID", organizer.getOrganizerId());}

    /**
     * Tests for Events getter and setter.
     */
    @Test
    public void testEventsGetterAndSetter() {
        // Check if not null
        assertNotNull(organizer.getEvents());

        ArrayList<String> newEvents = new ArrayList<>();
        newEvents.add("Event1");
        newEvents.add("Event2");

        organizer.setEvents(newEvents);

        assertEquals(newEvents, organizer.getEvents());
        assertTrue(organizer.getEvents().contains("Event1")
                && organizer.getEvents().contains("Event2"));
    }
}
