package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class CreateAndDisplayEventTest {

    // Launch app
    @Rule
    public ActivityScenarioRule<OrganizerPageActivity> ActivityScenarioRule =
            new ActivityScenarioRule<>(OrganizerPageActivity.class);


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

        // Click 'New QR'
        onView(withId(R.id.newQrButton)).perform(click());

        // Click 'Generate Unique QR code'
        onView(withId(R.id.generateQrCodeButton)).perform(click());

        // Click Home Page Button
        onView(withId(R.id.finishButton)).perform(click());
    }

    @Test
    public void testEventDisplay() {
        // Click Expand Button
        onView(withId(R.id.expandButton)).perform(click());

        onView(withId(R.id.eventNameOrgText))
                .check(matches(isDisplayed()))
                .check(matches(withText("Create New Event Test")));

        onView(withId(R.id.attendeeCountTextView))
                .check(matches(isDisplayed()))
                .check(matches(withText("0")));

        onView(withId(R.id.eventDateOrgText))
                .check(matches(isDisplayed()))
                .check(matches(withText("2024-05-30")));

        onView(withId(R.id.eventTimeOrgText))
                .check(matches(isDisplayed()))
                .check(matches(withText("12:00 - 16:00")));

        onView(withId(R.id.eventDetailOrgText))
                .check(matches(isDisplayed()))
                .check(matches(withText("This is a test description")));

        onView(withId(R.id.viewMapButton)).check(matches(isDisplayed()));
        onView(withId(R.id.pdf_button)).check(matches(isDisplayed()));
        onView(withId(R.id.eventQrText)).check(matches(isDisplayed()));
        onView(withId(R.id.eventQrView)).check(matches(isDisplayed()));
        onView(withId(R.id.promoQrText)).check(matches(isDisplayed()));
        onView(withId(R.id.promoQrView)).check(matches(isDisplayed()));
    }

    @Test
    public void testPoster(){
        //onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
    }
}
