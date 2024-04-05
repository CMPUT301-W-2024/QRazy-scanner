package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AttendeePageActivitiesTest {
    @Rule
    public ActivityScenarioRule<AttendeePageActivity> ActivityScenarioRule =
            new ActivityScenarioRule<>(AttendeePageActivity.class);

    // Create a profile
    @Before
    public void setUp() {
        try {
            // If new user, profile fragment will be displayed
            onView(withId(R.id.userNameEditText)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            // If returning user, attendee page activity will be displayed
            onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
            onView(withId(R.id.menuButton)).perform(click());
        }

        onView(withId(R.id.userNameEditText)).perform(typeText("Page activities test"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("attendee101@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.phoneEditText)).perform(typeText("123456789"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
    }

    /**
     * Test to check if menu button switches from
     * 'Attendee Page' activity to 'Profile Edit' activity and
     * back to 'Attendee Page'
     */
    @Test
    public void testProfileEditActivity() {
        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        onView(withId(R.id.menuButton)).perform(click());
        onView(withId(R.id.avatarImage)).check(matches(isDisplayed()));
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if 'check into event' button switches from
     * 'Attendee Page' activity to 'Scan' activity and
     * back to 'Attendee Page'
     */
    @Test
    public void testScanActivity() {
        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        onView(withId(R.id.scanButton)).perform(click());
        onView(withText("Allow")).perform(click());
        onView(withId(R.id.scanner_view)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if 'Promo QR code' button switches from
     * 'Attendee Page' activity to 'Scan' activity and
     * back to 'Attendee Page'
     */
    @Test
    public void testPromoScanActivity() {
        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        onView(withId(R.id.promoQrCodeButton)).perform(click());
        // onView(withText("Allow")).perform(click());
        onView(withId(R.id.scanner_view)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
    }
}
