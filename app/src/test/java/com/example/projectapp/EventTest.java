package com.example.projectapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Setting up a class for Event related unit tests
 */
@RunWith(JUnit4.class)
public class EventTest {
    private Event testEvent;
    private HashMap<String, Integer> checkedAttendees;
    private ArrayList<String> signedAttendees;

    @BeforeEach
    public void setUp() {
        testEvent = new Event("Sample Name","2025-01-01", "00:00", "01:00", "Test Organizer", "123456789", 50, "Test Description", "Poster.png");
        checkedAttendees = new HashMap<>();
        signedAttendees = new ArrayList<>();
    }


    /**
     * Test the event constructor with no parameters provided
     */
    @Test
    public void testEventConstructor() {
        Event newEvent = new Event();

        // Checks if every parameter is NULL
        assertNull(newEvent.getName());
        assertNull(newEvent.getDate());
        assertNull(newEvent.getStartTime());
        assertNull(newEvent.getEndTime());
        assertNull(newEvent.getOrganizerId());
        assertNull(newEvent.getOrganizerName());
        assertNull(newEvent.getAttendanceLimit());
        assertNull(newEvent.getDescription());
        assertNull(newEvent.getPoster());
    }

    /**
     * Tests to check if name is valid using getters and setters
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
     * Tests for event ID
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
     * Tests for event organizer name
     */
    @Test
    public void testOrganizerNameGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getOrganizerName());

        // Checks getter
        assertNotEquals("Not Test Organizer", testEvent.getOrganizerName());
        assertEquals("Test Organizer", testEvent.getOrganizerName());

        // Checks setter
        testEvent.setOrganizerName("New Test Organizer");
        assertEquals("New Test Organizer", testEvent.getOrganizerName());

    }

    /**
     * Tests for event organizer ID
     */
    @Test
    public void testOrganizerIDGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getOrganizerId());

        // Checks getter
        assertNotEquals("987654321", testEvent.getOrganizerId());
        assertEquals("123456789", testEvent.getOrganizerId());

        // Checks setter
        testEvent.setOrganizerId("555");
        assertEquals("555", testEvent.getOrganizerId());

    }

    /**
     * Tests for checked attendees hashmap
     */
    @Test
    public void testCheckedAttendeesGetterAndSetter() {
        // Create a mock hashmap
        checkedAttendees.put("attendee1", 1);
        checkedAttendees.put("attendee2", 2);

        // Checks getter and setter
        testEvent.setCheckedAttendees(checkedAttendees);
        HashMap<String, Integer> actualAttendees = testEvent.getCheckedAttendees();

        // Confirms if hashmap is correct
        assertNotNull(actualAttendees);
        assertEquals(checkedAttendees.size(), actualAttendees.size());
        for (String key : checkedAttendees.keySet()) {
            assertTrue(actualAttendees.containsKey(key));
            assertEquals(checkedAttendees.get(key), actualAttendees.get(key));
        }
    }

    /**
     * Tests for event poster
     */
    @Test
    public void testPosterGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getPoster());

        // Checks getter
        assertNotEquals("NotAPoster.png", testEvent.getPoster());
        assertEquals("Poster.png", testEvent.getPoster());

        // Checks setter
        testEvent.setPoster("NewPoster.png");
        assertEquals("NewPoster.png", testEvent.getPoster());
    }

    /**
     * Tests for the number of checked in attendees
     */
    @Test
    public void testGetAttendance() {
        // Create mock hashmap
        checkedAttendees.put("attendee1", 1);
        checkedAttendees.put("attendee2", 2);

        // Getter and Setter
        testEvent.setCheckedAttendees(checkedAttendees);
        int attendanceCount = testEvent.getAttendance();

        // Checks that attendance is not null
        assertNotNull(attendanceCount);

        // Checks if attendance count is correct
        assertEquals(checkedAttendees.size(), attendanceCount);
    }

    /**
     * Tests for event date
     */
    @Test
    public void testEventDateGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getDate());

        // Checks getter
        assertNotEquals("2000-01-01", testEvent.getDate());
        assertEquals("2025-01-01", testEvent.getDate());

        // Checks setter
        testEvent.setDate("2025-12-31");
        assertEquals("2025-12-31", testEvent.getDate());

    }

    /**
     * Tests for event start time
     */
    @Test
    public void testEventStartTimeGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getStartTime());

        // Checks getter
        assertNotEquals("12:00", testEvent.getStartTime());
        assertEquals("00:00", testEvent.getStartTime());

        // Checks setter
        testEvent.setStartTime("00:30");
        assertEquals("00:30", testEvent.getStartTime());

    }

    /**
     * Tests for event end time
     */
    @Test
    public void testEventEndTimeGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getEndTime());

        // Checks getter
        assertNotEquals("12:00", testEvent.getEndTime());
        assertEquals("01:00", testEvent.getEndTime());

        // Checks setter
        testEvent.setEndTime("02:00");
        assertEquals("02:00", testEvent.getEndTime());

    }

    /**
     * Tests for event attendance limit
     */
    @Test
    public void testEventAttendanceLimitGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getAttendanceLimit());

        // Checks getter
        assertNotEquals(Optional.of(0), testEvent.getAttendanceLimit());
        assertEquals(Optional.of(50), testEvent.getAttendanceLimit());

        // Checks setter
        testEvent.setAttendanceLimit(100);
        assertEquals(100, Optional.ofNullable(testEvent.getAttendanceLimit()));

    }

    /**
     * Tests for event description
     */
    @Test
    public void testEventDescriptionGetterAndSetter(){
        // Check if not null
        assertNotNull(testEvent.getDescription());

        // Checks getter
        assertNotEquals("Not a Description", testEvent.getDescription());
        assertEquals("Test Description", testEvent.getDescription());

        // Checks setter
        testEvent.setDescription("New Test Description");
        assertEquals("New Test Description", testEvent.getDescription());

    }

    /**
     * Tests for event QR code
     */
    @Test
    public void testEventQRCodeGetterAndSetter(){
        // Check if null
        assertNull(testEvent.getQrCode());

        // Checks setter
        testEvent.setQrCode("Test QR Code");

        // Checks getter
        assertNotEquals("Not a QR code", testEvent.getQrCode());
        assertEquals("Test QR Code", testEvent.getQrCode());
    }

    /**
     * Tests for list of signed up attendees
     */
    @Test
    public void testSignedAttendeesGetterAndSetter() {
        // Create mock list of signed attendees
        signedAttendees.add("attendee1");
        signedAttendees.add("attendee2");

        // Getter and setting
        testEvent.setSignedAttendees(signedAttendees);
        ArrayList<String> actualSignedAttendees = testEvent.getSignedAttendees();

        // Check not null and correct number of signed attendees
        assertNotNull(actualSignedAttendees);
        assertEquals(signedAttendees, actualSignedAttendees);
    }

    /**
     * Tests the addCheckedAttendee method to ensure it correctly
     * adds a new attendee or increments an existing one.
     */
    @Test
    public void testAddCheckedAttendee() {
        // Set checked attendees
        checkedAttendees.put("attendee1", 0);
        testEvent.setCheckedAttendees(checkedAttendees);

        // Add attendees
        testEvent.addCheckedAttendee("attendee1");
        testEvent.addCheckedAttendee("attendee2");
        testEvent.addCheckedAttendee("attendee1"); // Check-in attendee1 again

        // Tests
        assertNotNull(testEvent.getCheckedAttendees());
        assertEquals(2, testEvent.getCheckedAttendees().size());
        assertTrue(testEvent.getCheckedAttendees().containsKey("attendee1"));
        assertTrue(testEvent.getCheckedAttendees().containsKey("attendee2"));
        assertEquals(Integer.valueOf(2), testEvent.getCheckedAttendees().get("attendee1"));
        assertEquals(Integer.valueOf(1), testEvent.getCheckedAttendees().get("attendee2"));
    }

    /**
     * Tests the addSignedAttendee method to ensure it correctly adds a new attendee.
     */
    @Test
    public void testAddSignedAttendee() {
        // Set signed up attendees
        signedAttendees.add("attendee1");
        testEvent.setSignedAttendees(signedAttendees);

        // Add attendees
        testEvent.addSignedAttendee("attendee2");
        testEvent.addSignedAttendee("attendee1"); // Attempt to sign up attendee1 again

        // Tests
        assertNotNull(testEvent.getSignedAttendees());
        assertEquals(2, testEvent.getSignedAttendees().size());
        assertTrue(testEvent.getSignedAttendees().contains("attendee1"));
        assertTrue(testEvent.getSignedAttendees().contains("attendee2"));
    }

    /**
     * Tests for geopoints
     */
    @Test
    public void testGeopointsGetterAndSetter() {
        ArrayList<GeoPoint> geopoints = new ArrayList<>();
        geopoints.add(new GeoPoint(37.422, -122.084)); // Add a test geopoint

        // Test setGeopoints
        testEvent.setGeopoints(geopoints);

        // Test getGeopoints
        ArrayList<GeoPoint> retrievedGeopoints = testEvent.getGeopoints();
        assertNotNull(retrievedGeopoints);
        assertFalse(retrievedGeopoints.isEmpty());
        assertEquals(geopoints, retrievedGeopoints);
    }

    /**
     * Tests for setGeopoint
     */
    @Test
    public void testSetGeopoint() {
        // Initialize mocks
        FirebaseFirestore mockedFirestore = mock(FirebaseFirestore.class);
        DocumentReference mockedDocumentReference = mock(DocumentReference.class);
        DocumentSnapshot mockedDocumentSnapshot = mock(DocumentSnapshot.class);

        GeoPoint testGeoPoint = new GeoPoint(37.422, -122.084); // Example coordinates

        // Setup the mock behavior
        when(mockedFirestore.collection("events")
                .document(anyString()))
                .thenReturn(mockedDocumentReference);

        when(mockedDocumentReference.get())
                .thenReturn(Tasks.forResult(mockedDocumentSnapshot));

        when(mockedDocumentSnapshot
                .exists()).thenReturn(true);

        when(mockedDocumentSnapshot.toObject(Event.class))
                .thenReturn(testEvent);

        ArrayList<GeoPoint> initialGeopoints = new ArrayList<>();
        testEvent.setGeopoints(initialGeopoints); // Set the initial state of geopoints

        // Execute the method to test
        testEvent.setGeopoint(testGeoPoint);

        // Capture the argument to verify the update operation
        ArgumentCaptor<ArrayList<GeoPoint>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(mockedDocumentReference).update(eq("latitude"), argumentCaptor.capture());

        // Verify the results
        ArrayList<GeoPoint> updatedGeopoints = argumentCaptor.getValue();
        assertTrue(updatedGeopoints.contains(testGeoPoint));
    }

    /**
     * Tests for announcements
     */
    @Test
    public void testAnnouncementsGetterAndSetter() {
        ArrayList<Announcement> expectedAnnouncements = new ArrayList<>();
        expectedAnnouncements.add(new Announcement("Announcement", "16:00", "Event", "Organizer")); // Add a test announcement

        testEvent.setAnnouncements(expectedAnnouncements);
        ArrayList<Announcement> actualAnnouncements = testEvent.getAnnouncements();

        assertNotNull(actualAnnouncements);
        assertFalse(actualAnnouncements.isEmpty());
        assertEquals(expectedAnnouncements, actualAnnouncements);
    }

    /**
     * Tests to add announcement
     */
    @Test
    public void testAddAnnouncements() {
        // Initialize mocks
        FirebaseFirestore mockedFirestore = mock(FirebaseFirestore.class);
        DocumentReference mockedDocumentReference = mock(DocumentReference.class);

        // Setup the mock behavior
        when(FirebaseFirestore.getInstance()).thenReturn(mockedFirestore);
        when(mockedFirestore.collection("events")
                .document(testEvent.getEventId()))
                .thenReturn(mockedDocumentReference);

        String announcementText = "New Announcement";
        testEvent.addAnnouncements(announcementText);

        // Capture the argument to verify the update operation
        ArgumentCaptor<ArrayList<Announcement>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(mockedDocumentReference).update(eq("announcements"), argumentCaptor.capture());

        // Verify the results
        ArrayList<Announcement> capturedAnnouncements = argumentCaptor.getValue();
        assertNotNull(capturedAnnouncements);
        assertFalse(capturedAnnouncements.isEmpty());
        assertEquals(announcementText, capturedAnnouncements.get(0).getAnnouncement());
    }

    /**
     * Tests for equals method
     */
    @Test
    public void testEquals() {
        Event event1 = new Event("EventName1", "2024-04-04", "10:00", "18:00", "OrganizerName", "OrganizerId", 100, "Description", "PosterURL");
        Event event2 = new Event("EventName2", "2024-04-05", "11:00", "19:00", "OrganizerName", "OrganizerId", 150, "Another Description", "AnotherPosterURL");
        Event event3 = new Event("EventName3", "2024-04-06", "12:00", "20:00", "OrganizerName", "DifferentOrganizerId", 200, "Different Description", "DifferentPosterURL");

        // Test for equality based on organizerId
        assertTrue(event1.equals(event2));
        assertFalse(event1.equals(event3));
        assertFalse(event1.equals(null));
        assertFalse(event1.equals(new Object()));
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
     * Tests for Promo QR code
     */
    @Test
    public void testPromoQrCodeGetterAndSetter(){
        // Checks if field is null
        assertNull(testEvent.getPromoQrCode());

        // Checks setter
        testEvent.setPromoQrCode("Sample PromoQrCode");
        assertEquals("Sample PromoQrCode", testEvent.getPromoQrCode());

        // Checks getter
        assertNotEquals("Not Sample PromoQrCode", testEvent.getPromoQrCode());
    }
}