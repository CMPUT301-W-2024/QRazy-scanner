package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.View.OrganizerPageActivity;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EventAttendeeActivityTest {
    DataHandler dataHandler = DataHandler.getInstance();
    Attendee mockAttendee;

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_MEDIA_IMAGES);

    // create organizer
    @Before
    public void setUp() throws InterruptedException {
        Organizer mockOrganizer = new Organizer("Test Organizer");
        dataHandler.setLocalOrganizer(mockOrganizer);
        dataHandler.addOrganizer(organizer -> {});
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        FirebaseFirestore.getInstance().collection("events").whereEqualTo("organizerId", dataHandler.getLocalOrganizer().getOrganizerId()).get().addOnSuccessListener(documentSnapshots -> {
            for (QueryDocumentSnapshot d : documentSnapshots) {
                d.getReference().delete();
            }
        });
        FirebaseFirestore.getInstance().collection("organizers").document(dataHandler.getLocalOrganizer().getOrganizerId()).delete();
    }

    @Test
    public void testAttendeesDisplayed() throws InterruptedException{
        ActivityScenario.launch(OrganizerPageActivity.class);

        Event mockEvent = new Event("Admin Test Event", "10-10-2024", "13:00", "14:00", dataHandler.getLocalOrganizer().getName(), dataHandler.getLocalOrganizer().getOrganizerId(), 100, "Des", null);
        Attendee mockAttendee = new Attendee(null, "Admin Test Attendee", "test@gmail.com", "1234567890");
        dataHandler.addEvent(mockEvent, event -> {});
        dataHandler.addAttendee(mockAttendee, false, (a,e)->{});
        dataHandler.updateEvent(mockEvent.getEventId(), "checkedAttendees." + mockAttendee.getAttendeeId(), FieldValue.increment(1), v->{});
        dataHandler.updateAttendee(mockAttendee.getAttendeeId(), "checkedInEvents." + mockEvent.getEventId(), FieldValue.increment(1), v->{});
        Thread.sleep(1000);

        onView(withId(R.id.attendeeCountTextView)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.attendeeNameText)).check(matches(isDisplayed()));
        dataHandler.deleteEvent(mockEvent.getEventId(), v -> {});
        dataHandler.deleteAttendee(mockAttendee.getAttendeeId(), v -> {});
    }
}
