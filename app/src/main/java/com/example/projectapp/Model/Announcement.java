package com.example.projectapp.Model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Announcements class
 */
public class Announcement implements Serializable {
    private String announcement;
    private String dateTime;
    private String event;
    private String organizer;

    /**
     * Default constructor for creating an instance of Announcement.
     */
    public Announcement() {
    }

    /**
     * Constructs an Announcement with specified details.
     *
     * @param announcement the announcement text
     * @param dateTime     the date and time of the announcement
     * @param event        the event name
     * @param organizer    the organizer of the event
     */
    public Announcement(String announcement, String dateTime, String event, String organizer) {
        this.announcement = announcement;
        this.dateTime = dateTime;
        this.event = event;
        this.organizer = organizer;
    }

    /**
     * Gets the announcement text.
     *
     * @return
     *      the announcement text
     */
    public String getAnnouncement() {
        return announcement;
    }

    /**
     * Sets the announcement text.
     *
     * @param announcement
     *      the new announcement text
     */
    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    /**
     * Gets the date and time of the announcement.
     *
     * @return
     *      the date and time of the announcement
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time of the announcement.
     *
     * @param dateTime
     *      the new date and time of the announcement
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets the event name.
     *
     * @return
     *      the event name
     */
    public String getEvent() {
        return event;
    }

    /**
     * Sets the event name.
     *
     * @param event
     *      the new event name
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Gets the organizer of the event.
     *
     * @return
     *      the organizer of the event
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Sets the organizer of the event.
     *
     * @param organizer
     *      the new organizer of the event
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    /**
     * Compares this announcement to the specified object.
     *
     * @param obj       the object to compare this Announcement against
     * @return true     if the given object represents an equivalent Announcement,
     *         false    otherwise.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) obj;
        return Objects.equals(announcement, other.announcement) &&
                Objects.equals(dateTime, other.dateTime) &&
                Objects.equals(event, other.event);
    }

    /**
     * Returns a hash code value for the announcement.
     *
     * @return
     *      a hash code value for this Announcement
     */
    @Override
    public int hashCode() {
        return Objects.hash(announcement, dateTime, event);
    }
}
