package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class Admin_test {
    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public class AdminActivityTest {

        @Rule
        public ActivityScenarioRule<Admin> activityRule =
                new ActivityScenarioRule<>(Admin.class);

        @Test
        public void textViewDisplaysCorrectText() {
            onView(withId(R.id.EventsView)).check(matches(withText(R.string.events)));
            onView(withId(R.id.ProfileView)).check(matches(withText(R.string.profiles)));
            onView(withId(R.id.textView2)).check(matches(withText(R.string.Images)));
        }

/*        @Test
        public void horizontalScrollViewIsScrollable() {
            onView(withId(R.id.horizontalScrollView)).perform(swipeLeft());
            onView(withId(R.id.horizontalScrollView)).perform(swipeRight());
        }*/


        @Test
        public void verticalScrollViewIsScrollable() {
            onView(withId(R.id.adminAttendeesLayout)).perform(swipeUp());
            onView(withId(R.id.adminAttendeesLayout)).perform(swipeDown());
        }
    }


}
