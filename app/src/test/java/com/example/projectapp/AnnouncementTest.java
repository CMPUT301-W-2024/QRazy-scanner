package com.example.projectapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AnnouncementTest {
    private Announcement announcement;

    @BeforeEach
    void setUp() {
        announcement = new Announcement("Test Announcement", "15:00", "Test Event", "Test Organizer");
    }

    @Test
    void testAnnouncementGetterAndSetter() {
        String newAnnouncement = "New Test Announcement";
        announcement.setAnnouncement(newAnnouncement);
        assertEquals(newAnnouncement, announcement.getAnnouncement(), "Announcements do not match");
    }

    @Test
    void testTimeGetterAndSetter() {
        String newTime = "21:00";
        announcement.setTime(newTime);
        assertEquals(newTime, announcement.getTime(), "Announcement times do not match");
    }

    @Test
    void testEventsGetterAndSetter() {
        String newEvent = "New Event";
        announcement.setEvent(newEvent);
        assertEquals(newEvent, announcement.getEvent(), "Events do not match");
    }

    @Test
    void testOrganizerGetterAndSetter() {
        String newOrganizer = "New Organizer";
        announcement.setOrganizer(newOrganizer);
        assertEquals(newOrganizer, announcement.getOrganizer(), "Organizers do not match");
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(announcement.equals(null), "Announcement should not be equal to null");
    }

    @Test
    void testEqualsWithSelf() {
        assertTrue(announcement.equals(announcement), "Announcement should be equal to itself");
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(announcement.equals("some string"), "Announcement should not be equal to a different class");
    }

    @Test
    void testEqualsWithEqualAnnouncements() {
        Announcement anotherAnnouncement = new Announcement("Test Announcement", "15:00", "Test Event", "Test Organizer");
        assertTrue(announcement.equals(anotherAnnouncement), "Announcements with the same state should be equal");
    }

    @Test
    void testEqualsWithDifferentAnnouncements() {
        Announcement differentAnnouncement = new Announcement("Different Announcement", "16:00", "Different Event", "Different Organizer");
        assertFalse(announcement.equals(differentAnnouncement), "Announcements with different states should not be equal");
    }

    @Test
    void testHashCodeConsistency() {
        Announcement anotherAnnouncement = new Announcement("Test Announcement", "15:00", "Test Event", "Test Organizer");
        assertEquals(announcement.hashCode(), anotherAnnouncement.hashCode(), "Hash codes should be consistent for equal objects");
    }

    @Test
    void testHashCodeConsistencyWithDifferentObjects() {
        Announcement differentAnnouncement = new Announcement("Different Announcement", "16:00", "Different Event", "Different Organizer");
        assertNotEquals(announcement.hashCode(), differentAnnouncement.hashCode(), "Hash codes should be different for non-equal objects");
    }
}
