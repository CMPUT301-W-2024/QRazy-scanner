package com.example.projectapp.Model;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Class for Attendee
 */
public class Attendee {

    private String profilePic;
    private String name;
    private String homepage;
    private String contactInfo;
    private String attendeeId;
    private String fcmToken;
    private HashMap<String, Integer> checkedInEvents;
    private ArrayList<String> signedUpEvents;

    /**
     * Attendee Constructor
     */
    public Attendee(){
    }

    /**
     * Create new attendee with the following attributes
     * @param profilePic    encoded string of the profile picture
     * @param name          name of attendee
     * @param homepage      homepage of attendee
     * @param contactInfo   number of attendee
     */
    public Attendee(String profilePic, String name, String homepage, String contactInfo) {
        this.profilePic = profilePic;
        this.name = name;
        this.homepage = homepage;
        this.contactInfo = contactInfo;
        attendeeId = UUID.randomUUID().toString();
        checkedInEvents = new HashMap<>();
        signedUpEvents = new ArrayList<>();
    }

    /**
     * Get the attendee's profile picture
     *
     * @return
     *      Attendee's picture
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Set the attendee's profile picture
     *
     * @param profilePic
     *      New profile pic image
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Get the attendee's name
     *
     * @return
     *      Attendee's name
     */
    public String getName() {
        return name;
    }


    /**
     * Set the attendee's name
     *
     * @param name
     *      Attendee's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the attendee's homepage
     *
     * @return
     *      Attendee's homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Set the attendee's homepage
     *
     * @param homepage
     *      Attendee's new homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Get the attendee's contact information
     *
     * @return
     *      Attendee's contact information
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the attendee's homepage
     *
     * @param contactInfo
     *      Attendee's new contact information
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Get the attendee's ID
     *
     * @return
     *      Attendee's ID
     */
    public String getAttendeeId() {
        return attendeeId;
    }

    /**
     * Set the attendee's ID
     *
     * @param attendeeId
     *      New attendee ID to set
     */
    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    /**
     * Gets the events the user has checked into,
     * and the number of times they've checked in.
     *
     * @return
     *      HashMap containing event names and number of check-ins
     */
    public HashMap<String, Integer> getCheckedInEvents() {
        return checkedInEvents;
    }

    /**
     * Sets the list of events the user has checked into.
     *
     * @param checkedInEvents
     *      New HashMap containing event names and number of check-ins
     */
    public void setCheckedInEvents(HashMap<String, Integer> checkedInEvents) {
        this.checkedInEvents = checkedInEvents;
    }

    /**
     * Gets the list of events the user has signed up for.
     *
     * @return
     *      ArrayList containing the names of the signed-up events.
     */
    public ArrayList<String> getSignedUpEvents() {
        return signedUpEvents;
    }

    /**
     * Sets the list of events the user has signed up for.
     *
     * @param signedUpEvents
     *      ArrayList containing the names of the events to sign up for.
     */
    public void setSignedUpEvents(ArrayList<String> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }

    /**
     * Gets the Firebase Cloud Messaging (FCM) token for notifications.
     *
     * @return
     *      The FCM token associated with this attendee.
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * Sets the Firebase Cloud Messaging (FCM) token for notifications.
     *
     * @param fcmToken
     *      The new FCM token to be associated with this attendee.
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    /**
     * Compares this attendee to the specified object for equality.
     *
     * @param obj       the object to compare with this Attendee
     * @return true     if the given object represents an equivalent Attendee,
     *         false    otherwise.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Attendee attendee = (Attendee) obj;
        return Objects.equals(attendeeId, attendee.attendeeId);
    }

    /**
     * Returns a hash code value for the attendee.
     *
     * @return
     *      Hash code value for this attendee
     */
    @Override
    public int hashCode() {
        return Objects.hash(attendeeId);
    }
}
