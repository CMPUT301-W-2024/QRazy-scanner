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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import static java.util.EnumSet.allOf;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.Controller.AddEventCallback;
import com.example.projectapp.Controller.AddOrganizerCallback;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import java.util.ArrayList;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GenerateQrActivitiesTest {
    DataHandler dataHandler = DataHandler.getInstance();

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.CAMERA);

    // create organizer and event
    @Before
    public void setUp() throws InterruptedException {
        Organizer mockOrganizer = new Organizer("Test Organizer");
        dataHandler.setLocalOrganizer(mockOrganizer);
        dataHandler.addOrganizer(organizer -> {
        });
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        FirebaseFirestore.getInstance().collection("events").whereEqualTo("organizerId", dataHandler.getLocalOrganizer().getOrganizerId()).get().addOnSuccessListener(documentSnapshots -> {
            for (QueryDocumentSnapshot d : documentSnapshots) {
                d.getReference().delete();
            }
        });
    }

    @Test
    public void testQrCodeDisplays() throws InterruptedException {
        ActivityScenario.launch(OrganizerPageActivity.class);

        createNewEvent();
        onView(withId(R.id.generateQrCodeButton)).perform(click());
        onView(withId(R.id.qrCodeImageView)).check(((view, noViewFoundException) -> {
            ImageView imageView = (ImageView) view;
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageView.getDrawingCache();
            assertNotNull(bitmap);
        }));
    }

    @Test
    public void testPromoQrCodeDisplays() throws InterruptedException {
        ActivityScenario.launch(OrganizerPageActivity.class);

        createNewEvent();
        onView(withId(R.id.generatePromotionQrCodeButton)).perform(click());
        onView(withId(R.id.qrCodeImageView)).check(((view, noViewFoundException) -> {
            ImageView imageView = (ImageView) view;
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageView.getDrawingCache();
            assertNotNull(bitmap);
        }));
    }

    @Test
    public void testScanActivitySwitch() throws InterruptedException {
        ActivityScenario.launch(OrganizerPageActivity.class);

        createNewEvent();
        onView(withId(R.id.useOwnQrButton)).perform(click());
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.useOwnQrButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerPageActivitySwitch() throws InterruptedException {
        ActivityScenario.launch(OrganizerPageActivity.class);

        createNewEvent();
        onView(withId(R.id.finishButton)).perform(click());
        onView(withId(R.id.allEventsTextView)).check(matches(isDisplayed()));
    }


    public void createNewEvent() throws InterruptedException {
        onView(withId(R.id.createNewEventButton)).perform(click());
        fillEventDetails();
        onView(withId(R.id.newQrButton))
                .perform(scrollTo())
                .perform(click());
        Thread.sleep(1000);;
    }

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
