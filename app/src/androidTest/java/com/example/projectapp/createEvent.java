package com.example.projectapp;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class createEvent {
    @Rule
    public ActivityScenarioRule<CreateEventActivity> activityScenarioRule =
            new ActivityScenarioRule<>(CreateEventActivity.class);

    @Test
    public void createEventTest() {
        // Launch the activity
        ActivityScenario<CreateEventActivity> activityScenario = activityScenarioRule.getScenario();

        // Simulate setting organizer's name
        Espresso.onView(withId(R.id.organizerNameEditText))
                .perform(ViewActions.typeText("Sample Name"), ViewActions.closeSoftKeyboard());

        // Click the "Create New Event" button
        Espresso.onView(withId(R.id.createNewEventButton))
                .perform(click());

        // Simulate filling in event details
        Espresso.onView(withId(R.id.eventNameEditText))
                .perform(ViewActions.typeText("Sample Event"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.eventDateEditText))
                .perform(ViewActions.click()); // Simulate clicking on the date field
        Espresso.onView(ViewMatchers.withText("OK"))
                .perform(ViewActions.click()); // Simulate selecting a date (OK button)

        Espresso.onView(ViewMatchers.withId(R.id.eventDescriptionEditText))
                .perform(ViewActions.typeText("This is a sample event description"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.newQrButton))
                .perform(click());

    }
}
