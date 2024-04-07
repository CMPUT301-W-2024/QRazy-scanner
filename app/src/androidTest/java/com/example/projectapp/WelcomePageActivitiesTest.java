package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.projectapp.View.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of the ‘Create Event’
 * and ‘Join Event’ buttons on the welcome page to
 * ensure they correctly switch activities.
 */
@RunWith(AndroidJUnit4.class)
public class WelcomePageActivitiesTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Verifies that the ‘Create Event’ button
     * transitions to 'Organizer Page' activity
     */
    @Test
    public void testCreateEventButton() {
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /**
     * Verifies that the ‘Join Event’ button transitions to
     * 'Profile Fragment' if new user or
     * 'Attendee Page' activity if returning user
     */
    @Test
    public void testJoinEventButton(){
        onView(withId(R.id.joinEventButton)).perform(click());

        try {
            // If new user, profile fragment will be displayed
            onView(withId(R.id.userNameEditText)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            // If returning user, attendee page activity will be displayed
            onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
        }

    }
}
