package com.example.projectapp.Model;


import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Setting up Event class
 */
public class Event implements Serializable{
    private String eventId;
    private String name;
    private String date;
    private String startTime;
    private String endTime;
    private String organizerName;
    private String organizerId;
    private String description;
    private HashMap<String, Integer> checkedAttendees;
    private ArrayList<String> signedAttendees;
    private String poster;
    private Integer attendanceLimit;
    private String qrCode;
    private String promoQrCode;
    private transient HashMap<String, ArrayList<GeoPoint>> geopoints;
    private ArrayList<Announcement> announcements;
    private Boolean trackLocation;

    /**
     * Empty Event constructor required for firebase
     */
    public Event(){

    }

    /**
     * Creates a new Event object with specified details.
     *
     * @param name          The name of the event.
     * @param date          The date the event occurs
     * @param startTime     The time the event starts
     * @param endTime       The time the event ends.
     * @param organizerName The name of the event organizer.
     * @param organizerId   The id of the event organizer.
     * @param attendanceLimit The maximum number of allowed attendees.
     * @param description   A description of the event.
     * @param poster        A poster image associated with the event.
     */
    public Event(String name, String date, String startTime, String endTime, String organizerName, String organizerId, Integer attendanceLimit, String description, String poster) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizerName = organizerName;
        this.organizerId = organizerId;
        this.attendanceLimit = attendanceLimit;
        this.description = description;
        this.poster = poster;
        eventId = UUID.randomUUID().toString();
        checkedAttendees = new HashMap<>();
        signedAttendees = new ArrayList<>();
        geopoints = new HashMap<>();
        announcements = new ArrayList<>();
    }

    /**
     * Get Event ID
     *
     * @return
     *      ID of the event
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Set event's ID
     *
     * @param eventId
     *      ID of the event
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the name of the event.
     *
     * @return
     *      The event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name
     *      The new name for the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organizer of the event.
     *
     * @return
     *      The organizer's name.
     */
    public String getOrganizerName() {
        return organizerName;
    }

    /**
     * Sets the organizer name of the event.
     *
     * @param organizerName
     *      The new organizer's name.
     */
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    /**
     * Gets the organizer ID of the event.
     *
     * @return
     *      The organizer's ID.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer ID of the event.
     *
     * @param organizerId
     *      The new organizer's ID.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Gets a HashMap representing event attendees and their check-in counts.
     *
     * @return
     *      A HashMap where keys are attendee IDs and values are check-in counts.
     */
    public HashMap<String, Integer> getCheckedAttendees() {
        return checkedAttendees;
    }

    /**
     * Sets checked event attendees and their check-in counts.
     *
     * @param checkedAttendees
     *      The new number of attendees that have checked in.
     */
    public void setCheckedAttendees(HashMap<String, Integer> checkedAttendees) {
        this.checkedAttendees = checkedAttendees;
    }

    /**
     * Gets the Event's poster.
     *
     * @return
     *      The event's poster.
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Sets the Event's poster
     *
     * @param poster
     *      The event's new poster.
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Gets the number of attendees.
     *
     * @return
     *      The number of Attendees.
     */
    public Integer getAttendance() {
        return checkedAttendees.size();
    }

    /**
     * Gets the event's date.
     *
     * @return
     *      The event date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date on which the event occurs.
     *
     * @param date
     *      The new event date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get time at which event starts.
     *
     * @return
     *      The event start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the time at which the event starts.
     *
     * @param startTime
     *      The new event start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Get time at which event ends.
     *
     * @return
     *      The event end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the time at which the event ends.
     *
     * @param endTime
     *      The new event end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the attendance limit for the event.
     *
     * @return
     *      The maximum number of allowed attendees.
     */
    public Integer getAttendanceLimit() {
        return attendanceLimit;
    }

    /**
     * Sets the attendance limit for the event.
     *
     * @param attendanceLimit
     *      The new maximum number of allowed attendees.
     */
    public void setAttendanceLimit(Integer attendanceLimit) {
        this.attendanceLimit = attendanceLimit;
    }

    /**
     * Gets event description.
     *
     * @return
     *      A description of the event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets event description
     *
     * @param description
     *      The new description of the event.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the event's QR Code
     *
     * @return
     *      The event's QRCode encoded as a string.
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets the event's QR Code
     *
     * @param qrCode
     *      The new encoded event QR code.
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Gets a list of signed up attendees.
     *
     * @return
     *      Attendees signed up for events.
     */
    public ArrayList<String> getSignedAttendees() {
        return signedAttendees;
    }

    /**
     * Sets a list of signed up attendees.
     *
     * @param signedAttendees
     *      The new list of signed up attendees.
     */
    public void setSignedAttendees(ArrayList<String> signedAttendees) {
        this.signedAttendees = signedAttendees;
    }

    /**
     * Gets the geographical location from
     * where each attendee checked in.
     *
     * @return
     *      The geographical point of check in for each attendee.
     */
    public HashMap<String, ArrayList<GeoPoint>> getGeopoints() {
        return geopoints;
    }

    /**
     * Sets the new geographical location of check in for each attendee
     *
     * @param geopoints
     *      The new geographical points of check in.
     */
    public void setGeopoints(HashMap<String, ArrayList<GeoPoint>> geopoints) {
        this.geopoints = geopoints;
    }

    /**
     * Gets a list of the announcements.
     *
     * @return
     *      A list of the announcements
     */
    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    /**
     * Sets a list of the announcements.
     *
     * @param announcements
     *      A list of the new announcements
     */
    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    /**
     * Gets the event's promotional QR code.
     *
     * @return
     *      The event's promotional QR code as a String.
     */
    public String getPromoQrCode() {
        return promoQrCode;
    }

    /**
     * Sets the event's promotional QR code.
     *
     * @param promoQrCode
     *      The new event promotional QR code.
     */
    public void setPromoQrCode(String promoQrCode) {
        this.promoQrCode = promoQrCode;
    }

    /**
     * Determines whether the provided object is equal to this Event.
     * Two Events are considered equal if they have the same event ID.
     *
     * @param obj       The object to compare with this Event.
     * @return true     If the object is an Event with the same ID,
     *         false    otherwise.
     */
    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == null){
            return false;
        }

        if (this == obj){
            return true;
        }

        return (obj instanceof Event) && ((Event) obj).getEventId().equals(eventId);
    }

    /**
     * Gets a hashcode of event ID.
     *
     * @return
     *      A hashcode of the event ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    /**
     * Gets if location tracking is enabled for the event.
     *
     * @return true     if tracking is enabled,
     *         false    otherwise.
     */
    public Boolean getTrackLocation() {
        return trackLocation;
    }

    /**
     * Sets location tracking for the event.
     *
     * @param trackLocation
     *      true    to enable tracking,
     *      false   to disable.
     */
    public void setTrackLocation(Boolean trackLocation) {
        this.trackLocation = trackLocation;
    }
}

