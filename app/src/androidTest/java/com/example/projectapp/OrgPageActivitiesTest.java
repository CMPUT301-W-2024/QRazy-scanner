package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OrgPageActivitiesTest {
    // Launch app
    @Rule
    public androidx.test.ext.junit.rules.ActivityScenarioRule<OrganizerPageActivity> ActivityScenarioRule =
            new ActivityScenarioRule<>(OrganizerPageActivity.class);

    @Before
    public void setUp() {
        onView(withId(R.id.organizerNameEditText)).perform(typeText("Page activities test"), closeSoftKeyboard());
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.eventNameEditText)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.eventDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 05, 30));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.eventStartTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.eventEndTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.eventDescriptionEditText)).perform(typeText("This is a test description"), closeSoftKeyboard());
        onView(withId(R.id.attendanceLimitEditText)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.newQrButton)).perform(click());
        onView(withId(R.id.generateQrCodeButton)).perform(click());
        onView(withId(R.id.finishButton)).perform(click());
    }

    /**
     * Test to check if clicking create new event switches from
     * 'Organizer Page' activity to 'Create New Event'
     *  activity and back to 'Organizer Page'
     */
    @Test
    public void testCreateNewEventActivity() {
        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.eventNameEditText)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if clicking map button switches from
     * 'Organizer Page' activity to 'Map' activity and
     * back to 'Organizer Page'
     */
    @Test
    public void testMapActivity() {
        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.viewMapButton)).perform(click());
        onView(withId(R.id.mapview)).check(matches(isDisplayed()));
        onView(withId(R.id.goBackButton)).perform(click());

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if clicking map button switches from
     * 'Organizer Page' activity to 'Event Attendees' activity and
     * back to 'Organizer Page'
     */
    @Test
    public void testEventAttendeesActivity() {
        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.attendeeCountTextView)).perform(click());
        onView(withId(R.id.checkedInAttendeesList)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if clicking pdf button switches from
     * 'Organizer Page' activity to 'Report' activity and
     * back to 'Organizer Page'
     */
    @Test
    public void testReportActivity() {
        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.pdf_button)).perform(click());
        onView(withId(R.id.btn)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }
}
