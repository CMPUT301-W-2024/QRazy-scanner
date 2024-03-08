package com.example.projectapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttendeeTest {

    private Attendee attendee;

    @BeforeEach
    void setUp() {
        // Initialize Attendee object with some default values before each test
        attendee = new Attendee("defaultPic.png", "Default Name", "default@gmail.com", "+123456789");
    }

    @Test
    void testProfilePicGetterAndSetter() {
        String newProfilePic = "newPic.png";
        attendee.setProfilePic(newProfilePic);
        assertEquals(newProfilePic, attendee.getProfilePic(), "Profile pictures do not match");
    }

    @Test
    void testNameGetterAndSetter() {
        String newName = "New Name";
        attendee.setName(newName);
        assertEquals(newName, attendee.getName(), "Names do not match");
    }

    @Test
    void testHomepageGetterAndSetter() {
        String newHomepage = "newHomepage@gmail.com";
        attendee.setHomepage(newHomepage);
        assertEquals(newHomepage, attendee.getHomepage(), "Homepages do not match");
    }

    @Test
    void testContactInfoGetterAndSetter() {
        String newContactInfo = "+0987654321";
        attendee.setContactInfo(newContactInfo);
        assertEquals(newContactInfo, attendee.getContactInfo(), "Contact information does not match");
    }

    @Test
    void testAttendeeIdGetterAndSetter() {
        String newAttendeeId = "newAttendeeId123";
        attendee.setAttendeeId(newAttendeeId);
        assertEquals(newAttendeeId, attendee.getAttendeeId(), "Attendee IDs do not match");
    }

    @Test
    void testEventsGetterAndSetter() {
        ArrayList<String> newEvents = new ArrayList<>();
        newEvents.add("Event1");
        newEvents.add("Event2");
        attendee.setEvents(newEvents);
        assertEquals(newEvents, attendee.getEvents(), "Events lists do not match");
        assertTrue(attendee.getEvents().contains("Event1") && attendee.getEvents().contains("Event2"), "Events list does not contain the correct events");
    }
}
