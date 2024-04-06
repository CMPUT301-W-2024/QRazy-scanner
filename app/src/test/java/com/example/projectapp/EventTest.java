package com.example.projectapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import com.example.projectapp.Model.Event;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Setting up a class for Event related unit tests
 */
@RunWith(JUnit4.class)
public class EventTest {
    /**
     * Test the event constructor with no parameters provided
     */
    @Test
    public void testEventConstructor() {
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

    public Event testEvent = new Event("Sample Name","2024-01-01", "00:00", "01:00", "Test Organizer", "123456789", 50, "Sample description", "poster.png");

    /**
     * Tests to check if name is valid using getters and setters
     */
    @Test
    public void testName(){
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
    public void testEventID(){
        // Check if not null
        assertNotNull(testEvent.getEventId());

        // Checks setter
        testEvent.setEventId("Sample Event ID");
        assertEquals("Sample Event ID", testEvent.getEventId());

        // Checks getter
        assertNotEquals("Not Sample Event ID", testEvent.getEventId());

    }

    /**
     * Tests to check if promoQrCode is valid
     */
    @Test
    public void testPromoQrCode(){
        // Checks if field is null
        assertNull(testEvent.getPromoQrCode());

        // Checks setter
        testEvent.setPromoQrCode("Sample PromoQrCode");
        assertEquals("Sample PromoQrCode", testEvent.getPromoQrCode());

        // Checks getter
        assertNotEquals("Not Sample PromoQrCode", testEvent.getPromoQrCode());
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
}