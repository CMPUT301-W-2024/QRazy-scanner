package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.Controller.AddEventCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.View.CreateNewEventActivity;
import com.example.projectapp.View.EventAttendeesActivity;
import com.example.projectapp.View.GenerateQrCodeActivity;
import com.example.projectapp.View.MainActivity;
import com.example.projectapp.View.MapActivity;
import com.example.projectapp.View.OrganizerPageActivity;
import com.example.projectapp.View.ReportActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class OrganizerPageActivitiesTest {
    DataHandler dataHandler = DataHandler.getInstance();
    Event event = new Event("organizer page activity","2024-01-01", "00:00", "01:00", "Organizer Activity", "123456789", 50, "Sample description", "poster.png");
    Organizer organizer = new Organizer("Organizer Activity");

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            "android.Manifest.permission.ACCESS_FINE_LOCATION",
            "android.Manifest.permission.ACCESS_COARSE_LOCATION",
            "android.Manifest.permission.ACCESS_BACKGROUND_LOCATION");
    // create organizer and event
    @Before
    public void setUp(){
        event.setQrCode("iVBORw0KGgoAAAANSUhEUgAAAfQAAAH0CAIAAABEtEjdAAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAACZhJREFUeJzt3EuOa0UQRdEyqvlPuWggIUQDJSZ5Ed53rQHY92NvZeu8fn5");
        event.setPromoQrCode("iVBORw0KGgoAAAANSUhEUgAAAfQAAAH0CAIAAABEtEjdAAAAAXNSR0IArs4c6QAAAANzQklUBQYFMwuNgAAACZNJREFUeJzt3EGu20YQRVEp0P63");

        event.setOrganizerName(organizer.getName());
        event.setOrganizerId(organizer.getOrganizerId());

        //DocumentReference eventDocRef = FirebaseFirestore.getInstance().collection("events").document(event.getEventId());
        //eventDocRef.set(event);

        ArrayList<Event> eventArray = new ArrayList<>(); // Create an array with a single slot
        eventArray.add(event); // Assign the event object to the first index of the array

        organizer.setEvents(eventArray);
        dataHandler.setLocalOrganizer(organizer);
        //dataHandler.addEvent(event, AddEventCallback );

    }

    @After
    public void tearDown(){
        DocumentReference eventDocRef = FirebaseFirestore.getInstance().collection("events").document(event.getEventId());
        eventDocRef.delete();

        DocumentReference OrganizerDocRef = FirebaseFirestore.getInstance().collection("organizers").document((dataHandler.getLocalOrganizer()).getOrganizerId());
        OrganizerDocRef.delete();
    }

    /**
     * Test to check if clicking create new event switches from
     * 'Organizer Page' activity to 'Create New Event'
     *  activity and back to 'Organizer Page'
     */
    @Test
    public void testCreateNewEventActivity() {
        ActivityScenario.launch(OrganizerPageActivity.class);
        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));

        onView(withId(R.id.createNewEventButton)).perform(click());
        onView(withId(R.id.eventNameEditText)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void testGenerateQRCodeActivity() {
        ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.createNewEventButton)).perform(click());

        ActivityScenario.launch(CreateNewEventActivity.class);

        fillEventDetails();

        onView(withId(R.id.newQrButton))
                .perform(scrollTo())
                .perform(click());

        ActivityScenario.launch(GenerateQrCodeActivity.class);

        onView(withId(R.id.qrCodeImageView)).check(matches(isDisplayed()));
        onView(withId(R.id.finishButton)).perform(click());

        ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if clicking map button switches from
     * 'Organizer Page' activity to 'Map' activity and
     * back to 'Organizer Page'
     */
    @Test
    public void testMapAndReportAndLiveAttendeeActivities() throws InterruptedException {
        ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.createNewEventButton)).perform(click());
        ActivityScenario.launch(CreateNewEventActivity.class);
        fillEventDetails();
        onView(withId(R.id.newQrButton)).perform(scrollTo()).perform(click());
        ActivityScenario.launch(GenerateQrCodeActivity.class);
        onView(withId(R.id.qrCodeImageView)).check(matches(isDisplayed()));
        onView(withId(R.id.finishButton)).perform(click());
        ActivityScenario.launch(OrganizerPageActivity.class);
        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));

        onView(withId(R.id.expandButton)).perform(click());
        onView(withId(R.id.viewMapButton)).perform(click());

        ActivityScenario.launch(MapActivity.class);

        Thread.sleep(100);
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
        onView(withId(R.id.goBackButton)).perform(click());

        ActivityScenario.launch(OrganizerPageActivity.class);
        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));

        onView(withId(R.id.attendeeCountTextView)).perform(click());

        ActivityScenario.launch(EventAttendeesActivity.class);
        onView(withId(R.id.checkedInAttendeesList)).check(matches(isDisplayed()));
        pressBack();

        ActivityScenario.launch(OrganizerPageActivity.class);
        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));

        ActivityScenario.launch(ReportActivity.class);
        onView(withId(R.id.pdfButton)).perform(click());
        onView(withId(R.id.reportButton)).check(matches(isDisplayed()));
        pressBack();

        ActivityScenario.launch(OrganizerPageActivity.class);
        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if clicking 'Live attendee count' switches from
     * 'Organizer Page' activity to 'Event Attendees' activity and
     * back to 'Organizer Page'
     */
    /*@Test
    public void testEventAttendeesActivity() {
        // Launch OrganizerPageActivity
        ActivityScenario<OrganizerPageActivity> attendeePageActivityScenario = ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.attendeeCountTextView)).perform(click());
        onView(withId(R.id.checkedInAttendeesList)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if clicking pdf button switches from
     * 'Organizer Page' activity to 'Report' activity and
     * back to 'Organizer Page'
     */
    /*@Test
    public void testReportActivity() {
        // Launch OrganizerPageActivity
        ActivityScenario<OrganizerPageActivity> attendeePageActivityScenario = ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.pdfButton)).perform(click());
        onView(withId(R.id.reportButton)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }*/

    public void fillEventDetails() {
        onView(withId(R.id.eventNameEditText)).perform(click());
        onView(withId(R.id.eventNameEditText)).perform(typeText("activity test"), closeSoftKeyboard());

        // Set date
        onView(withId(R.id.eventDateEditText)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(setDate(2024, 5, 1));
        onView(withId(android.R.id.button1)).perform(click());

        // Set start time
        onView(withId(R.id.eventStartTimeEditText)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(setTime(8, 0));
        onView(withId(android.R.id.button1)).perform(click());

        // Set end time
        onView(withId(R.id.eventEndTimeEditText)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());

        //onView(withId(R.id.eventDescriptionEditText)).perform(scrollTo()).perform(click());
        onView(withId(R.id.eventDescriptionEditText)).perform(typeText("activities test description"), closeSoftKeyboard());

        onView(withId(R.id.attendanceLimitEditText))
                .perform(scrollTo())
                .perform(typeText("100"), closeSoftKeyboard());

        onView(withId(R.id.checkInLocation))
                .perform(scrollTo())
                .perform(click());
    }

    public static ViewAction setDate(final int year, final int monthOfYear, final int dayOfMonth) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(DatePicker.class);
            }

            @Override
            public String getDescription() {
                return "set date";
            }

            @Override
            public void perform(UiController uiController, View view) {
                DatePicker datePicker = (DatePicker) view;
                datePicker.updateDate(year, monthOfYear, dayOfMonth);
            }
        };
    }

    public static ViewAction setTime(final int hourOfDay, final int minute) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }

            @Override
            public String getDescription() {
                return "set time";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TimePicker timePicker = (TimePicker) view;
                timePicker.setCurrentHour(hourOfDay);
                timePicker.setCurrentMinute(minute);
            }
        };
    }

}
