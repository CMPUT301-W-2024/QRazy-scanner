package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.projectapp.View.Admin;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class AdminTest {
    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public static class AdminActivityTest {

        @Rule
        public ActivityScenarioRule<Admin> activityRule =
                new ActivityScenarioRule<>(Admin.class);

        @Test
        public void textViewDisplaysCorrectText() {
            onView(withId(R.id.EventsView)).check(matches(withText(R.string.events)));
            onView(withId(R.id.ProfileView)).check(matches(withText(R.string.profiles)));
            onView(withId(R.id.textView2)).check(matches(withText(R.string.Images)));
        }

        @Test
        public void verticalScrollViewIsScrollable() {
            onView(withId(R.id.adminAttendeesLayout)).perform(swipeUp());
            onView(withId(R.id.adminAttendeesLayout)).perform(swipeDown());
        }

        @Test
        public void deleteEventButton() {
            onView(withId(R.id.adminEventsLayout)).perform(click());
            onView(withId(R.id.dialogEventDeleteButton)).perform(click());
        }

        @Test
        public void deleteAttendeeButton() {
            onView(withId(R.id.adminAttendeesLayout)).perform(click());
            onView(withId(R.id.dialog_delete_button)).perform(click());
        }

    }
}