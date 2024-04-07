package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.View.AttendeePageActivity;
import com.example.projectapp.View.MainActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AttendeePageActivitiesTest {

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant("android.permission.CAMERA");

    // Create a profile
    @BeforeClass
    public static void setUp() {
        // Launch MainActivity
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.joinEventButton)).perform(click());
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
        // Launch AttendeePageActivity
        ActivityScenario<AttendeePageActivity> attendeePageActivityScenario = ActivityScenario.launch(AttendeePageActivity.class);

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
        // Launch AttendeePageActivity
        ActivityScenario<AttendeePageActivity> attendeePageActivityScenario = ActivityScenario.launch(AttendeePageActivity.class);
        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        onView(withId(R.id.scanButton)).perform(click());
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
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
        // Launch AttendeePageActivity
        ActivityScenario<AttendeePageActivity> attendeePageActivityScenario = ActivityScenario.launch(AttendeePageActivity.class);
        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        onView(withId(R.id.promoQrCodeButton)).perform(click());
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
    }
}
