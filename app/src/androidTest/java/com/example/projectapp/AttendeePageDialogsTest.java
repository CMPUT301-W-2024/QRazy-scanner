package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Event;
import com.example.projectapp.View.AttendeePageActivity;
import com.example.projectapp.View.MainActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AttendeePageDialogsTest {

    Event event = new Event("activity test", "2024-01-01", "00:00", "01:00", "Test Organizer", "123456789", 50, "Sample description", "poster.png");
    DataHandler dataHandler = DataHandler.getInstance();

    /**
     * Sets up the testing environment before each test.
     */
    @Before
    public void setUp() {
        DocumentReference eventDocRef = FirebaseFirestore.getInstance().collection("events").document(event.getEventId());
        eventDocRef.set(event);

        // Clear SharedPreferences
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
        mainActivityScenario.onActivity(activity -> {
            activity.getSharedPreferences("AttendeePref", Context.MODE_PRIVATE)
                    .edit().clear().apply();
        });

        onView(withId(R.id.joinEventButton)).perform(click());
        onView(withId(R.id.userNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.userNameEditText)).perform(typeText("attendee dialog activities"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("attendee101@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText)).perform(typeText("123456789"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        // Launch the AttendeePageActivity
        ActivityScenario.launch(AttendeePageActivity.class);
    }

    /**
     * Verifies the sign-up dialog functionality in the UI.
     *
     * @throws InterruptedException
     *      if the thread is interrupted while sleeping
     */
    @Test
    public void testSignUpDialog() throws InterruptedException {
        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        Thread.sleep(1000);
        onView((withText("activity test")))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.dialogEventSignButton)).perform(click());
    }

    /**
     * Cleans up the Firestore database after tests by deleting the event and attendee documents.
     */
    @After
    public void tearDown(){
        DocumentReference eventDocRef = FirebaseFirestore.getInstance().collection("events").document(event.getEventId());
        eventDocRef.delete();

        DocumentReference attendeeDocRef = FirebaseFirestore.getInstance().collection("attendees").document((dataHandler.getLocalAttendee()).getAttendeeId());
        attendeeDocRef.delete();
    }

}
