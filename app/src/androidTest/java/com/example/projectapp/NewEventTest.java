package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewEventTest {

    // Launch app
    @Rule
    public ActivityScenarioRule<OrganizerPageActivity> ActivityScenarioRule =
            new ActivityScenarioRule<>(OrganizerPageActivity.class);

    /**
     * Test to check if clicking create new event switches from
     * organizer page to create new event activity
     */
    @Test
    public void testSwitchActivity() {
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.eventNameEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void testCreateNewEvent() {
        // Add organizer name on organizer page
        onView(withId(R.id.organizerNameEditText)).perform(typeText("Test Organizer"), closeSoftKeyboard());

        // Click create new event button
        onView(withId(R.id.createEventButton)).perform(click());

        // Enter event title
        onView(withId(R.id.eventNameEditText)).perform(typeText("Create New Event Test"), closeSoftKeyboard());

        // Pick an event date
        onView(withId(R.id.eventDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 05, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // Pick an event start time
        onView(withId(R.id.eventStartTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());

        // Pick an event end time
        onView(withId(R.id.eventEndTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16, 0));
        onView(withId(android.R.id.button1)).perform(click());

        // Enter event description
        onView(withId(R.id.eventDescriptionEditText)).perform(typeText("This is a test description"), closeSoftKeyboard());

        // Enter event capacity
        onView(withId(R.id.attendanceLimitEditText)).perform(typeText("100"), closeSoftKeyboard());

        //onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));

        // Click 'New QR'
        onView(withId(R.id.newQrButton)).perform(click());

        // Click 'Generate Unique QR code'
        onView(withId(R.id.generateQrCodeButton)).perform(click());

        // Click Home Page Button
        onView(withId(R.id.finishButton)).perform(click());
    }

    @Test
    public void testCorrectDisplay() {

    }

}
