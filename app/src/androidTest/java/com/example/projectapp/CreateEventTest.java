package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;

public class CreateEventTest {
    @Rule
    public ActivityScenarioRule<CreateEventActivity> activityScenarioRule =
            new ActivityScenarioRule<>(CreateEventActivity.class);

    @Test
    public void createEventTest() {
        // Launch the activity
        ActivityScenario<CreateEventActivity> activityScenario = activityScenarioRule.getScenario();

        // Simulate setting organizer's name
        onView(withId(R.id.organizerNameEditText))
                .perform(typeText("Sample Name"), closeSoftKeyboard());

        // Click the "Create New Event" button
        onView(withId(R.id.createNewEventButton))
                .perform(click());

        // Simulate filling in event details
        onView(withId(R.id.eventNameEditText))
                .perform(typeText("Sample Event"), closeSoftKeyboard());

        onView(withId(R.id.eventDateEditText))
                .perform(click()); // Simulate clicking on the date field
        onView(withText("OK"))
                .perform(click()); // Simulate selecting a date (OK button)

        onView(withId(R.id.eventDescriptionEditText))
                .perform(typeText("This is a sample event description"), closeSoftKeyboard());

        // Set the attendance limit
        onView(withId(R.id.attendanceLimitEditText))
                .perform(typeText("50"), closeSoftKeyboard());

        // Generate QR Code for the event
        onView(withId(R.id.newQrButton))
                .perform(click());
        onView(withId(R.id.generateQrCodeButton))
                .perform(click());

        // Finish creating the event
        onView(withId(R.id.finishButton))
                .perform(click());
    }
}
