package com.example.projectapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateEventTest {

    // Launch app
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Before
    public void setup(){Intents.init();}

    @Test
    public void testCreateNewEvent() {
        // Click 'Create Event' on main page
        ViewInteraction materialButton = onView(allOf(withId(R.id.createEventButton), withText("Create Event"),
                childAtPosition(childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 0), 1),
                isDisplayed()));
        materialButton.perform(click());


        // Checks if activity is switched to organizer dashboard
        intended(hasComponent(OrganizerPageActivity.class.getName()));

        // Add organizer name
        ViewInteraction textInputEditText = onView(allOf(withId(R.id.organizerNameEditText),
                childAtPosition(childAtPosition(withId(R.id.organizerNameInputLayout), 0), 0),
                isDisplayed()));
        textInputEditText.perform(replaceText("Test Organizer"), closeSoftKeyboard());

        // Create New Event from organizer dashboard
        ViewInteraction materialButton2 = onView(allOf(withId(R.id.createNewEventButton), withText("Create New Event"),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 4),
                isDisplayed()));
        materialButton2.perform(click());



        // Checks if activity is switched to create new event
        intended(hasComponent(CreateNewEventActivity.class.getName()));

        // Input event name
        ViewInteraction textInputEditText7 = onView(allOf(withId(R.id.eventNameEditText),
                childAtPosition(childAtPosition(withId(R.id.eventNameInputLayout), 0), 0),
                isDisplayed()));
        textInputEditText7.perform(replaceText("Test Event"), closeSoftKeyboard());

        // Click event date field
        ViewInteraction textInputEditText8 = onView(allOf(withId(R.id.eventDateEditText),
                childAtPosition(childAtPosition(withId(R.id.eventDateInputLayout), 0), 0),
                isDisplayed()));
        textInputEditText8.perform(click());

        // Click 'OK' when date dialog pops up
        ViewInteraction materialButton3 = onView(allOf(withId(android.R.id.button1), withText("OK"),
                childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        materialButton3.perform(scrollTo(), click());

        // Click event time field
        ViewInteraction textInputEditText10 = onView(allOf(withId(R.id.eventTimeEditText),
                childAtPosition(childAtPosition(withId(R.id.eventTimeInputLayout), 0), 0),
                isDisplayed()));
        textInputEditText10.perform(click());

        // Click 'OK' when time dialog pops up
        ViewInteraction materialButton5 = onView(allOf(withId(android.R.id.button1), withText("OK"),
                childAtPosition(childAtPosition(withClassName(is("android.widget.ScrollView")), 0), 3)));
        materialButton5.perform(scrollTo(), click());

        // Input event description
        ViewInteraction textInputEditText11 = onView(allOf(withId(R.id.eventDescriptionEditText),
                childAtPosition(childAtPosition(withId(R.id.eventDescriptionInputLayout), 0), 0),
                isDisplayed()));
        textInputEditText11.perform(replaceText("This is a test description"), closeSoftKeyboard());

        // Input attendance limit
        ViewInteraction textInputEditText14 = onView(allOf(withId(R.id.attendanceLimitEditText),
                childAtPosition(childAtPosition(withId(R.id.attendanceLimitInputLayout), 0), 0),
                isDisplayed()));
        textInputEditText14.perform(replaceText("50"), closeSoftKeyboard());

        // Click 'New QR'
        ViewInteraction materialButton6 = onView(allOf(withId(R.id.newQrButton), withText("New Qr"),
                childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")), 7), 0),
                isDisplayed()));
        materialButton6.perform(click());


        // Checks if activity is switched to generate QR code
        intended(hasComponent(GenerateQrCodeActivity.class.getName()));

        // Click 'Generate Unique QR Code'
        ViewInteraction materialButton7 = onView(allOf(withId(R.id.generateQrCodeButton), withText("Generate Unique QR Code"),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2),
                isDisplayed()));
        materialButton7.perform(click());

        // Click 'Generate Promotion QR Code'
        ViewInteraction materialButton8 = onView(allOf(withId(R.id.generatePromotionQrCodeButton), withText("Generate Promotion QR Code"),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 3),
                isDisplayed()));
        materialButton8.perform(click());

        // Click 'Share QR Code'
        ViewInteraction materialButton10 = onView(allOf(withId(R.id.shareQrCodeButton), withText("Share QR Code"),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 4),
                isDisplayed()));
        materialButton10.perform(click());

        // Click 'Home Page'
        ViewInteraction materialButton11 = onView(allOf(withId(R.id.finishButton), withText("Home Page"),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 5),
                isDisplayed()));
        materialButton11.perform(click());

        // Checks if activity is switched back to organizer dashboard
        intended(hasComponent(OrganizerPageActivity.class.getName()));

        // Click arrow to expand event on organizer dashboard
        ViewInteraction appCompatImageButton = onView(allOf(withId(R.id.expandButton),
                childAtPosition(childAtPosition(withId(R.id.eventListRecyclerView), 0), 3),
                isDisplayed()));
        appCompatImageButton.perform(click());

        // Click to check map
        ViewInteraction materialButton12 = onView(allOf(withId(R.id.viewMapButton), withText("Check map"),
                childAtPosition(allOf(withId(R.id.expandEventLayout), childAtPosition(withClassName(is("android.widget.LinearLayout")), 2)), 2),
                isDisplayed()));
        materialButton12.perform(click());


        // Checks if activity is switched to create new event
        intended(hasComponent(MapActivity.class.getName()));

        // Click back to return from map to organizer dashboard
        ViewInteraction materialButton13 = onView(allOf(withId(R.id.goBackButton), withText("BACK"), childAtPosition(
                childAtPosition(withId(android.R.id.content), 0), 1),
                isDisplayed()));
        materialButton13.perform(click());

        // Checks if activity is switched back to organizer dashboard
        intended(hasComponent(OrganizerPageActivity.class.getName()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @After
    public void shutdown() {
        Intents.release();
    }
}
