package com.example.projectapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.projectapp.Model.Announcement;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AnnouncementTest {
    private Announcement announcement = new Announcement("Test Announcement", "15:00", "Test Event", "Test Organizer");

    /**
     * Tests for announcement getter and setter.
     */
    @Test
    void testAnnouncementGetterAndSetter() {
        assertNotNull(announcement.getAnnouncement());

        String newAnnouncement = "New Test Announcement";
        announcement.setAnnouncement(newAnnouncement);

        assertNotEquals("Not New Announcement", announcement.getAnnouncement());
        assertEquals(newAnnouncement, announcement.getAnnouncement());
    }

    /**
     * Tests for date time getter and setter.
     */
    @Test
    void testDateTimeGetterAndSetter() {
        assertNotNull(announcement.getDateTime());

        String newTime = "21:00";
        announcement.setDateTime(newTime);

        assertNotEquals("00:00", announcement.getAnnouncement());
        assertEquals(newTime, announcement.getDateTime());
    }

    /**
     * Tests for event getter and setter.
     */
    @Test
    void testEventGetterAndSetter() {
        assertNotNull(announcement.getEvent());

        String newEvent = "New Event";
        announcement.setEvent(newEvent);

        assertNotEquals("Not New Event", announcement.getEvent());
        assertEquals(newEvent, announcement.getEvent());
    }

    /**
     * Tests for organizer getter and setter.
     */
    @Test
    void testOrganizerGetterAndSetter() {
        assertNotNull(announcement.getOrganizer());

        String newOrganizer = "New Organizer";
        announcement.setOrganizer(newOrganizer);

        assertNotEquals("Not New Event", announcement.getEvent());
        assertEquals(newOrganizer, announcement.getOrganizer());
    }

    /**
     * Tests for getting and setting the date and time.
     */
    @Test
    void testEquals() {
        // Equals With Null
        assertFalse(announcement.equals(null));

        // Equals With Self
        assertTrue(announcement.equals(announcement));

        // Equals With Different Class
        assertFalse(announcement.equals("some string"));

        // Equals With Equal Announcements
        Announcement anotherAnnouncement = new Announcement("Test Announcement", "15:00", "Test Event", "Test Organizer");
        assertTrue(announcement.equals(anotherAnnouncement));

        // Equals With Different Announcements
        Announcement differentAnnouncement = new Announcement("Different Announcement", "16:00", "Different Event", "Different Organizer");
        assertFalse(announcement.equals(differentAnnouncement));
    }

    /**
     * Tests the hashCode method for Announcement objects.
     */
    @Test
    void testHashCode() {
        // Same Objects
        Announcement anotherAnnouncement = new Announcement("Test Announcement", "15:00", "Test Event", "Test Organizer");
        assertEquals(announcement.hashCode(), anotherAnnouncement.hashCode());

        // Different Objects
        Announcement differentAnnouncement = new Announcement("Different Announcement", "16:00", "Different Event", "Different Organizer");
        assertNotEquals(announcement.hashCode(), differentAnnouncement.hashCode());
    }
}
