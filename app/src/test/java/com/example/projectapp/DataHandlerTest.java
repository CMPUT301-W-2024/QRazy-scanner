package com.example.projectapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

public class DataHandlerTest {
    private FirebaseFirestore db;
    private CollectionReference attendeesRef;
    private CollectionReference eventsRef;
    private CollectionReference organizersRef;
    private DocumentReference attendeeDocRef;
    private DocumentReference eventDocRef;
    private DocumentReference organizerDocRef;
    private Attendee attendee;
    private Event event;
    private Organizer organizer;
    private AddAttendeeCallback addAttendeeCallback;
    private AddEventCallback addEventCallback;
    private UpdateEventCallback updateEventCallback;

    @Before
    public void setUp() {
        // Initialize mocks
        db = mock(FirebaseFirestore.class);
        attendeesRef = mock(CollectionReference.class);
        eventsRef = mock(CollectionReference.class);
        organizersRef = mock(CollectionReference.class);
        attendeeDocRef = mock(DocumentReference.class);
        eventDocRef = mock(DocumentReference.class);
        organizerDocRef = mock(DocumentReference.class);
        attendee = mock(Attendee.class);
        event = mock(Event.class);
        organizer = mock(Organizer.class);
        addAttendeeCallback = mock(AddAttendeeCallback.class);
        addEventCallback = mock(AddEventCallback.class);
        updateEventCallback = mock(UpdateEventCallback.class);

        // Setup behavior
        when(db.collection("attendees")).thenReturn(attendeesRef);
        when(db.collection("events")).thenReturn(eventsRef);
        when(db.collection("organizers")).thenReturn(organizersRef);
        when(attendeesRef.document(anyString())).thenReturn(attendeeDocRef);
        when(eventsRef.document(anyString())).thenReturn(eventDocRef);
        when(organizersRef.document(anyString())).thenReturn(organizerDocRef);
        when(attendee.getAttendeeId()).thenReturn("attendeeId");
        when(event.getEventId()).thenReturn("eventId");
        when(organizer.getOrganizerId()).thenReturn("organizerId");

        // Inject the mock Firestore instance into DataHandler
        DataHandler.setInstance(new DataHandler(db));
    }



    @Test
    public void testAddAttendee() {
        // Call method under test
        DataHandler.getInstance().addAttendee(attendee, addAttendeeCallback);

        // Verify interactions
        verify(attendeeDocRef).set(attendee);
        verify(addAttendeeCallback, never()).onAddAttendee(null);
    }

    @Test
    public void testAddEvent() {
        // Call method under test
        DataHandler.getInstance().addEvent(event, addEventCallback);

        // Verify interactions
        verify(eventDocRef).set(event);
        verify(addEventCallback, never()).onAddEvent(null);
    }

    @Test
    public void testAddOrganizer() {
        // Call method under test
        DataHandler.getInstance().addOrganizer(organizer);

        // Verify interactions
        verify(organizerDocRef).set(organizer);
    }

    @Test
    public void testUpdateEvent() {
        // Call method under test
        String field = "fieldName";
        Object value = "fieldValue";
        DataHandler.getInstance().updateEvent(event.getEventId(), field, value, updateEventCallback);

        // Verify interactions
        verify(eventDocRef).update(field, value);
        verify(updateEventCallback, never()).onUpdateEvent(null);
    }
    @Test
    public void testGetAndSetAttendee() {
        DataHandler dataHandler = DataHandler.getInstance();
        Attendee expectedAttendee = mock(Attendee.class);

        dataHandler.setAttendee(expectedAttendee);
        Attendee actualAttendee = dataHandler.getAttendee();

        assertNotNull(actualAttendee);
        assertEquals(expectedAttendee, actualAttendee);
    }

    @Test
    public void testGetAndSetOrganizer() {
        DataHandler dataHandler = DataHandler.getInstance();
        Organizer expectedOrganizer = mock(Organizer.class);

        dataHandler.setOrganizer(expectedOrganizer);
        Organizer actualOrganizer = dataHandler.getOrganizer();

        assertNotNull(actualOrganizer);
        assertEquals(expectedOrganizer, actualOrganizer);
    }
}

