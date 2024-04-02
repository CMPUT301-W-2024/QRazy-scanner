package com.example.projectapp;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Announcement implements Serializable {
    private String announcement;
    private String time;
    private String event;
    private String organizer;

    public Announcement() {
    }

    public Announcement(String announcement, String time, String event, String organizer) {
        this.announcement = announcement;
        this.time = time;
        this.event = event;
        this.organizer = organizer;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == null){
            return false;
        }

        if (this == obj){
            return true;
        }

        return (obj instanceof Announcement) && ((Announcement) obj).getEvent().equals(event) && ((Announcement) obj).getAnnouncement().equals(announcement) && ((Announcement) obj).getTime().equals(time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, announcement, time);
    }
}
