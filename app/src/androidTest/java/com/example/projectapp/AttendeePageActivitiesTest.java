package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Announcement;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.View.AttendeePageActivity;
import com.google.firebase.firestore.FieldValue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AttendeePageActivitiesTest {

    DataHandler dataHandler = DataHandler.getInstance();
    Attendee mockAttendee;

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.CAMERA,android.Manifest.permission.POST_NOTIFICATIONS);
    // Create a profile

    @Before
    public void setUp() throws InterruptedException {
        mockAttendee = new Attendee(null, "Admin Test Attendee", "test@gmail.com", "1234567890");
        dataHandler.setLocalAttendee(mockAttendee);
        dataHandler.addAttendee(mockAttendee, false,(a,e) -> {});
        Thread.sleep(500);
    }

    @After
    public void tearDown() {
        dataHandler.deleteAttendee(mockAttendee.getAttendeeId(), v -> {});
    }

    /**
     * Test to check if menu button switches from
     * 'Attendee Page' activity to 'Profile Edit' activity and
     * back to 'Attendee Page'
     */
    @Test
    public void testProfileEditActivitySwitch() throws InterruptedException {
        // Launch AttendeePageActivity
        ActivityScenario.launch(AttendeePageActivity.class);

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));

        onView(withId(R.id.menuButton)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.avatarImage)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
    }


    /**
     * Test to check if 'check into event' button switches from
     * 'Attendee Page' activity to 'Scan' activity and
     * back to 'Attendee Page'
     */
    @Test
    public void testScanActivitySwitch() {
        // Launch AttendeePageActivity
        ActivityScenario.launch(AttendeePageActivity.class);

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
        ActivityScenario.launch(AttendeePageActivity.class);

        onView(withId(R.id.promoQrCodeButton)).perform(click());
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.welcomeText)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventsDisplays() throws InterruptedException {
        ActivityScenario.launch(AttendeePageActivity.class);

        Organizer mockOrganizer = new Organizer("Test Organizer");
        Event mockEvent = new Event("Admin Test Event", "10-10-2024", "13:00", "14:00", mockOrganizer.getName(), mockOrganizer.getOrganizerId(), 100, "Des", null);
        dataHandler.addEvent(mockEvent, event -> {});
        Thread.sleep(500);
        onView(withIndex(withId(R.id.eventNameText), 0)).check(matches(isDisplayed()));

        dataHandler.deleteEvent(mockEvent.getEventId(), v -> {});
    }

    @Test
    public void testEventDialogDisplays() throws InterruptedException {
        ActivityScenario.launch(AttendeePageActivity.class);

        Organizer mockOrganizer = new Organizer("Test Organizer");
        Event mockEvent = new Event("Admin Test Event", "10-10-2024", "13:00", "14:00", mockOrganizer.getName(), mockOrganizer.getOrganizerId(), 100, "Des", null);
        dataHandler.addEvent(mockEvent, event -> {});
        Thread.sleep(500);
        onView(withIndex(withId(R.id.eventNameText), 0)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.dialogEventPoster)).check(matches(isDisplayed()));

        dataHandler.deleteEvent(mockEvent.getEventId(), v -> {});
    }

    @Test
    public void testAnnouncementDisplays() throws InterruptedException {
        ActivityScenario.launch(AttendeePageActivity.class);

        Organizer mockOrganizer = new Organizer("Test Organizer");
        Event mockEvent = new Event("Admin Test Event", "10-10-2024", "13:00", "14:00", mockOrganizer.getName(), mockOrganizer.getOrganizerId(), 100, "Des", null);
        dataHandler.addEvent(mockEvent, event -> {});
        dataHandler.updateEvent(mockEvent.getEventId(), "checkedAttendees." + dataHandler.getLocalAttendee().getAttendeeId(), FieldValue.increment(1), v->{});
        dataHandler.updateAttendee(dataHandler.getLocalAttendee().getAttendeeId(), "checkedInEvents." + mockEvent.getEventId(), FieldValue.increment(1), v->{});
        Announcement announcement = new Announcement("Test Announcement", "10-10-2024 14:00", mockEvent.getName(), mockOrganizer.getName());
        dataHandler.updateEvent(mockEvent.getEventId(), "announcements", FieldValue.arrayUnion(announcement), v -> {});
        Thread.sleep(500);

        onView(withId(R.id.announcementEvent)).check(matches(isDisplayed()));
        dataHandler.deleteEvent(mockEvent.getEventId(), v -> {});
    }



    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}
