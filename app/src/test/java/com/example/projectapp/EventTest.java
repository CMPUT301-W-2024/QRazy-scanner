package com.example.projectapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.google.firebase.firestore.GeoPoint;

import com.example.projectapp.Model.Announcement;
import com.example.projectapp.Model.Event;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Setting up a class for Event related unit tests
 */
@RunWith(JUnit4.class)
public class EventTest {
    public Event testEvent = new Event("Sample Name","2024-01-01", "00:00", "01:00", "Test Organizer", "123456789", 50, "Sample description", "poster.png");
    public HashMap<String, Integer> testCheckedAttendees;

    /**
     * Test the event constructor with no parameters provided
     */
    @Test
    public void testEmptyEventConstructor() {
        Event event = new Event();

        // Checks if every parameter is NULL
        assertNull(event.getName());
        assertNull(event.getDate());
        assertNull(event.getStartTime());
        assertNull(event.getEndTime());
        assertNull(event.getOrganizerId());
        assertNull(event.getOrganizerName());
        assertNull(event.getAttendanceLimit());
        assertNull(event.getDescription());
        assertNull(event.getPoster());
    }

    /**
     * Tests for name getter and setter.
     */
    @Test
    public void testNameGetterAndSetter(){
        // Checks if field is not empty and not null
        assertNotEquals("", testEvent.getName());
        assertNotNull(testEvent.getName());

        // Checks getter
        assertNotEquals("Not Sample Name", testEvent.getName());
        assertEquals("Sample Name", testEvent.getName());

        // Checks setter
        testEvent.setName("New Sample Name");
        assertEquals("New Sample Name", testEvent.getName());
    }

    /**
     * Tests for event ID getter and setter.
     */
    @Test
    public void testEventIDGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getEventId());

        // Checks setter
        testEvent.setEventId("Sample Event ID");
        assertEquals("Sample Event ID", testEvent.getEventId());

        // Checks getter
        assertNotEquals("Not Sample Event ID", testEvent.getEventId());

    }

    /**
     * Tests for organizer name getter and setter.
     */
    @Test
    public void testOrganizerNameGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getOrganizerName());

        // Checks setter
        testEvent.setOrganizerName("Sample Organizer Name");
        assertEquals("Sample Organizer Name", testEvent.getOrganizerName());

        // Checks getter
        assertNotEquals("Not Sample Organizer Name", testEvent.getOrganizerName());

    }

    /**
     * Tests for organizer ID getter and setter
     */
    @Test
    public void testOrganizerIDGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getOrganizerId());

        // Checks setter
        testEvent.setOrganizerId("555");
        assertEquals("555", testEvent.getOrganizerId());

        // Checks getter
        assertNotEquals("999", testEvent.getOrganizerId());

    }

    /**
     * Tests for checked in attendees hashmap getter and setter
     */
    @Test
    public void testCheckedAttendeesGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getOrganizerId());

        testCheckedAttendees = new HashMap<>();
        testCheckedAttendees.put("attendee1", 1);
        testCheckedAttendees.put("attendee2", 2);

        // Checks setter
        testEvent.setCheckedAttendees(testCheckedAttendees);

        // Checks getter
        assertEquals(testCheckedAttendees, testEvent.getCheckedAttendees());
        assertNotNull(testEvent.getCheckedAttendees());
        assertEquals(Integer.valueOf(1), testEvent.getCheckedAttendees().get("attendee1"));
        assertEquals(Integer.valueOf(2), testEvent.getCheckedAttendees().get("attendee2"));

    }

    /**
     * Tests for poster getter and setter.
     */
    @Test
    public void testPosterGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getPoster());

        // Checks setter
        testEvent.setPoster("NewPoster.png");
        assertEquals("NewPoster.png", testEvent.getPoster());

        // Checks getter
        assertNotEquals("Not Sample Organizer Name", testEvent.getPoster());

    }

    /**
     * Test the attendance getter.
     */
    @Test
    public void testAttendanceGetterAndSetter() {
        assertNotNull(testEvent.getAttendance());
    }

    /**
     * Tests for the event date getter and setter.
     */
    @Test
    public void testDateGetterAndSetter() {
        assertNotNull(testEvent.getDate());
        testEvent.setDate("2024-05-01");
        assertEquals("2024-05-01", testEvent.getDate());
    }

    /**
     * Tests for the start time getter and setter.
     */
    @Test
    public void testStartTimeGetterAndSetter() {
        assertNotNull(testEvent.getStartTime());
        testEvent.setStartTime("10:00");
        assertEquals("10:00", testEvent.getStartTime());
    }

    /**
     * Tests for end time getter and setter.
     */
    @Test
    public void testEndTimeGetterAndSetter() {
        assertNotNull(testEvent.getEndTime());
        testEvent.setEndTime("14:00");
        assertEquals("14:00", testEvent.getEndTime());
    }

    /**
     * Tests for attendance limit getter and setter.
     */
    @Test
    public void testAttendanceLimitGetterAndSetter() {
        assertNotNull(testEvent.getAttendanceLimit());
        testEvent.setAttendanceLimit(50);
        assertEquals(Integer.valueOf(50), testEvent.getAttendanceLimit());
    }

    /**
     * Tests for description getter and setter.
     */
    @Test
    public void testDescriptionGetterAndSetter() {
        assertNotNull(testEvent.getDescription());
        testEvent.setDescription("New Sample Description");
        assertEquals("New Sample Description", testEvent.getDescription());
    }

    /**
     * Tests for the QR code getter and setter.
     */
    @Test
    public void testQrCodeGetterAndSetter() {
        assertNull(testEvent.getQrCode());
        testEvent.setQrCode("SampleQrCode");
        assertEquals("SampleQrCode", testEvent.getQrCode());
    }

    /**
     * Tests for the signed attendees getter and setter.
     */
    @Test
    public void testSignedAttendeesGetterAndSetter() {
        assertNotNull(testEvent.getSignedAttendees());

        ArrayList<String> signedAttendees = new ArrayList<>();
        signedAttendees.add("attendee1");

        testEvent.setSignedAttendees(signedAttendees);
        assertEquals(signedAttendees, testEvent.getSignedAttendees());
    }

    /**
     * Tests for the geopoints getter and setter.
     */
    @Test
    public void testGeopointsGetterAndSetter() {
        assertNotNull(testEvent.getGeopoints());
        HashMap<String, ArrayList<GeoPoint>> geopoints = new HashMap<>();
        geopoints.put("location1", new ArrayList<>());
        testEvent.setGeopoints(geopoints);
        assertEquals(geopoints, testEvent.getGeopoints());
    }

    /**
     * Tests for the announcements getter and setter.
     */
    @Test
    public void testAnnouncementsGetterAndSetter() {
        assertNotNull(testEvent.getAnnouncements());

        ArrayList<Announcement> announcements = new ArrayList<>();
        announcements.add(new Announcement());

        testEvent.setAnnouncements(announcements);
        assertEquals(announcements, testEvent.getAnnouncements());
    }

    /**
     * Tests for the  promotional QR code getter and setter.
     */
    @Test
    public void testPromoQrCodeGetterAndSetter() {
        assertNull(testEvent.getPromoQrCode());

        testEvent.setPromoQrCode("SamplePromoQrCode");
        assertEquals("SamplePromoQrCode", testEvent.getPromoQrCode());
        assertNotEquals("Not Sample PromoQrCode", testEvent.getPromoQrCode());
    }

    /**
     * Test the equality of two event objects.
     */
    @Test
    public void testEquals() {
        Event event1 = new Event("Sample Name", "2024-01-01", "00:00", "01:00", "Test Organizer", "123456789", 50, "Sample description", "poster.png");
        Event event2 = new Event();

        // Event IDs are not the same, just the details
        assertFalse(event1.equals(testEvent));

        // Confirm both events are same if they have the same event ID
        testEvent.setEventId("123");
        event1.setEventId("123");
        assertTrue(event1.equals(testEvent));

        // Null condition
        assertFalse(event2.equals(testEvent));
    }

    /**
     * Tests for hashCode method
     */
    @Test
    public void testHashCode(){
        Event testHashCode = new Event();

        // Checks if field is not null
        assertNotNull(testHashCode.hashCode());

        // Checks if two different objects have different hashCodes
        assertNotEquals(testEvent.hashCode(), testHashCode.hashCode());

        // Checks if an object's hashCode is equal to itself
        assertEquals(testHashCode.hashCode(), testHashCode.hashCode());
    }

    /**
     * Tests for track location getter and setter
     */
    @Test
    public void testTrackLocationGetterAndSetter(){
        assertNull(testEvent.getTrackLocation());

        testEvent.setTrackLocation(Boolean.TRUE);

        assertTrue(testEvent.getTrackLocation());
        assertNotEquals(Boolean.FALSE, testEvent.getTrackLocation());
    }

}