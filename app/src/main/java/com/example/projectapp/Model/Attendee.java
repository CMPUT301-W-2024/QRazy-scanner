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
    private HashMap<String, Integer> checkedInEvents;
    private ArrayList<String> signedUpEvents;

    /**
     * Attendee Constructor
     */
    public Attendee(){
    }

    /**
     * Create new attendee with the following attributes
     * @param profilePic encoded string of the profile picture
     * @param name name of attendee
     * @param homepage homepage of attendee
     * @param contactInfo number of attendee
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
     * @return Attendee's picture
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Set the attendee's profile picture
     * @param profilePic an Image for profile pic
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Get the attendee's name
     * @return Attendee's name
     */
    public String getName() {
        return name;
    }


    /**
     * Set the attendee's name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the attendee's homepage
     * @return Attendee's homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Set the attendee's homepage
     * @param homepage new homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Get the attendee's contact information
     * @return
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the attendee's homepage
     * @param contactInfo new Contact Information
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Get the attendee's ID
     * @return ID
     */
    public String getAttendeeId() {
        return attendeeId;
    }

    /**
     * set the attendee's ID
     * @param attendeeId the ID to set
     */
    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    /**
     * get checked in Events with number of times checked in it
     * @return user's checked in events
     */
    public HashMap<String, Integer> getCheckedInEvents() {
        return checkedInEvents;
    }

    /**
     * set checked in Events
     * @param  checkedInEvents list of checked in events with number of times checked in the event
     */
    public void setCheckedInEvents(HashMap<String,Integer> checkedInEvents) {
        this.checkedInEvents = checkedInEvents;
    }

    /**
     * get signed up Events
     * @return events signed up fro by user
     */
    public ArrayList<String> getSignedUpEvents() {
        return signedUpEvents;
    }

    /**
     * set checked in Events
     * @param  signedUpEvents list of signed up for events
     */
    public void setSignedUpEvents(ArrayList<String> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }

    /**
     * Add checked into event

     * @param eventId the event added
     */
    public void addCheckedEvent(String eventId){
        if (!checkedInEvents.containsKey(eventId)){
            checkedInEvents.put(eventId, 1);
        }
        else{
            Integer checkIns = checkedInEvents.get(eventId) + 1;
            checkedInEvents.put(eventId, checkIns);
        }
    }

    /**
     * Add signed into event
     * @param eventId the event added
     */
    public void addSignedEvent(String eventId){
        if (!signedUpEvents.contains(eventId)){
            signedUpEvents.add(eventId);
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == null){
            return false;
        }

        if (this == obj){
            return true;
        }

        return (obj instanceof Attendee) && ((Attendee) obj).getAttendeeId().equals(attendeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendeeId);
    }
}
