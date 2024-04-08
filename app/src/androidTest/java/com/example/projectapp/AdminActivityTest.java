package com.example.projectapp;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import android.view.View;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.View.Admin;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminActivityTest {
    DataHandler dataHandler = DataHandler.getInstance();
    Organizer mockOrganizer;
    Event mockEvent;
    Attendee mockAttendee;

    @Rule
    public ActivityScenarioRule<Admin> activityRule =
            new ActivityScenarioRule<>(Admin.class);

    @Before
    public void setUp() throws InterruptedException {
        mockOrganizer = new Organizer("Test Organizer");
        mockEvent = new Event("Admin Test Event", "10-10-2024", "13:00", "14:00", mockOrganizer.getName(), mockOrganizer.getOrganizerId(), 100, "Des", null);
        mockEvent.setEventId("0");
        mockAttendee = new Attendee(null, "Admin Test Attendee", "test@gmail.com", "1234567890");
        mockAttendee.setAttendeeId("0");
        dataHandler.addEvent(mockEvent, event -> {});
        dataHandler.addAttendee(mockAttendee, false, (a,e) -> {});
        Thread.sleep(5000); // events take time to pop up
    }

    @After
    public void tearDown(){
        dataHandler.deleteAttendee(mockAttendee.getAttendeeId(), v -> {});
        dataHandler.deleteEvent(mockEvent.getEventId(), v -> {});
    }

    @Test
    public void testEventDisplays(){
        onView(withIndex(withId(R.id.eventNameText), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void testAttendeeDisplays(){
        onView(withIndex(withId(R.id.attendeeNameText), 0)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventDialogDisplays(){
        onView(withIndex(withId(R.id.eventNameText), 0)).perform(click());
        onView(withId(R.id.dialogEventDeleteButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testAttendeeDialogDisplays(){
        onView(withIndex(withId(R.id.attendeeNameText), 0)).perform(click());
        onView(withId(R.id.attendeeDialogDeleteButton)).check(matches(isDisplayed()));
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

}