package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WelcomePageTest {

    public ActivityScenario<MainActivity> scenario;

    @Before
    public void setup(){

        Intents.init();

        scenario = ActivityScenario.launch(MainActivity.class);
    }


    /**
     * Verifies that the "Create Event" button
     *  on main page opens to organizer page.
     */
    @Test
    public void createEventButtonTest() {
        onView(withId(R.id.createEventButton)).perform(click());

        intended(hasComponent(OrganizerPageActivity.class.getName()));
    }

    /**
     * Verifies that the "Join Event" button
     *  on main page opens to profile fragment.
     */
    @Test
    public void joinEventButtonTest() throws InterruptedException {
        onView(withId(R.id.joinEventButton)).perform(click());

        // Check that a unique view in ProfileFragment is displayed
        onView(withId(R.id.avatarImageButton)).check(matches(isDisplayed()));
    }

    /**
     * Release intents on shutdown
     */
    @After
    public void shutdown() {
        Intents.release();
    }
}
