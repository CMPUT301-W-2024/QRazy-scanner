package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of the ‘Create Event’
 * and ‘Join Event’ buttons on the welcome page to
 * ensure they correctly switch activities.
 */
@RunWith(AndroidJUnit4.class)
public class WelcomePageTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Verifies that the ‘Create Event’ button
     * transitions to the organizer dashboard activity
     */
    @Test
    public void testCreateEventButton() {
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /**
     * Verifies that the ‘Join Event’ button transitions to
     * the create profile fragment if new user or
     * the attendee dashboard activity if returning user
     */
    @Test
    public void testJoinEventButton(){
        onView(withId(R.id.joinEventButton)).perform(click());
        onView(withId(R.id.userNameEditText)).check(matches(isDisplayed()));
    }

}
