package com.example.projectapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;

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
    void testSignedEventsGetterAndSetter() {
        ArrayList<String> newEvents = new ArrayList<>();
        newEvents.add("Event1");
        newEvents.add("Event2");
        attendee.setSignedUpEvents(newEvents);
        assertEquals(newEvents, attendee.getSignedUpEvents(), "Events lists do not match");
        assertTrue(attendee.getSignedUpEvents().contains("Event1") && attendee.getSignedUpEvents().contains("Event2"), "Events list does not contain the correct events");
    }

    @Test
    void testCheckedEventsGetterAndSetter() {
        HashMap<String, Integer> newEvents = new HashMap<>();
        newEvents.put("Event1", 2);
        newEvents.put("Event2", 3);
        attendee.setCheckedInEvents(newEvents);
        assertEquals(newEvents, attendee.getCheckedInEvents(), "Events lists do not match");
        assertTrue(attendee.getCheckedInEvents().containsKey("Event1") && attendee.getCheckedInEvents().containsKey("Event2"), "Events list does not contain the correct events");
    }

    @Test
    void testAddCheckedEvent(){

    }
}
