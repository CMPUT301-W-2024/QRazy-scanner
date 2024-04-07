package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.example.projectapp.View.CreateNewEventActivity;
import com.example.projectapp.View.MainActivity;
import com.example.projectapp.View.OrganizerPageActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class gsdgf {

    // launches main activity
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);


    /* Is called before every test
     * Set up an intent listener
     * */
    @Before
    public void setUp() throws Exception {
        // Initialize Espresso-Intents before each test
        Intents.init();
    }

    /* Is called after every test
     * Deletes/tearsdown the intent listener
     * */
    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    /**
     * Test to check if clicking create new event switches from
     * 'Organizer Page' activity to 'Create New Event'
     *  activity and back to 'Organizer Page'
     */
    @Test
    public void testCreateNewEventActivity() {
        // clicks on settings
        onView(withText("Create Event")).perform(click());

        onView(withId(R.id.organizerNameEditText)).perform(typeText("Page activities test"), closeSoftKeyboard());
        onView(withId(R.id.createNewEventButton)).perform(click());

        onView(withId(R.id.eventPosterImageView)).check(matches(isDisplayed()));

        onView(isRoot()).perform(ViewActions.pressBack());

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
    }

    /*
    @Test
    public void testGenerateQRCodeActivity() {
        // Launch OrganizerPageActivity
        //ActivityScenario<OrganizerPageActivity> attendeePageActivityScenario = ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.createNewEventButton)).perform(click());

        onView(withId(R.id.eventNameEditText)).perform(click());
        onView(withId(R.id.eventNameEditText)).perform(typeText("generate qr code activity test"), closeSoftKeyboard());

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

        onView(withId(R.id.eventDescriptionEditText)).perform(click());
        onView(withId(R.id.eventDescriptionEditText)).perform(typeText("activities test description"), closeSoftKeyboard());
        //onView(withId(R.id.attendanceLimitEditText)).perform(typeText("100"), closeSoftKeyboard());

        onView(withId(R.id.newQrButton)).perform(click());

        onView(withId(R.id.qrCodeImageView)).check(matches(isDisplayed()));

        onView(withId(R.id.generateQrCodeButton)).perform(click());
        onView(withId(R.id.finishButton)).perform(click());
    }

    /**
     * Test to check if clicking map button switches from
     * 'Organizer Page' activity to 'Map' activity and
     * back to 'Organizer Page'
     */
    /*@Test
    public void testMapActivity() {
        // Launch OrganizerPageActivity
        ActivityScenario<OrganizerPageActivity> attendeePageActivityScenario = ActivityScenario.launch(OrganizerPageActivity.class);

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.expandButton)).perform(click());

        onView(withId(R.id.viewMapButton)).perform(click());
        onView(withId(R.id.mapview)).check(matches(isDisplayed()));
        onView(withId(R.id.goBackButton)).perform(click());

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
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

        onView(withId(R.id.pdf_button)).perform(click());
        onView(withId(R.id.btn)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.organizerNameEditText)).check(matches(isDisplayed()));
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
    }*/

}
