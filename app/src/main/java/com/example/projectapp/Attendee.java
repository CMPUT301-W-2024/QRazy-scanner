package com.example.projectapp;

import android.media.Image;

public class Attendee {
    private Image profilePic;
    private String name;
    private String homepage;
    private String contactInfo;

    public Attendee(Image profilePic, String name, String homepage, String contactInfo) {
        this.profilePic = profilePic;
        this.name = name;
        this.homepage = homepage;
        this.contactInfo = contactInfo;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
