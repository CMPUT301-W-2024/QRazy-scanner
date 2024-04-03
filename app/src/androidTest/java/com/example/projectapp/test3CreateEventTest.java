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
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class test3CreateEventTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void test3CreateEventTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.createEventButton), withText("Create Event"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        // Checks if activity is switched to organizer dashboard
        intended(hasComponent(OrganizerPageActivity.class.getName()));

        public void testCreateNewEvent() {
            ViewInteraction materialButton2 = onView(
                    allOf(withId(R.id.createNewEventButton), withText("Create New Event"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    4),
                            isDisplayed()));
            materialButton2.perform(click());

            // Checks if activity is switched to create new event
            intended(hasComponent(CreateNewEventActivity.class.getName()));

            ViewInteraction textInputEditText = onView(
                    allOf(withId(R.id.eventNameEditText),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventNameInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText.perform(replaceText("test 3"), closeSoftKeyboard());

            ViewInteraction textInputEditText2 = onView(
                    allOf(withId(R.id.eventDateEditText),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventDateInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText2.perform(click());

            // Set the date in the DatePicker
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                    .perform(PickerActions.setDate(2024, 05, 30));

            ViewInteraction materialButton3 = onView(
                    allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton3.perform(scrollTo(), click());

            ViewInteraction textInputEditText3 = onView(
                    allOf(withId(R.id.eventStartTimeEditText),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventStartTimeInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText3.perform(click());

            ViewInteraction appCompatImageButton = onView(
                    allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageButton")), withContentDescription("Switch to text input mode for the time input."),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.LinearLayout")),
                                            4),
                                    0),
                            isDisplayed()));
            appCompatImageButton.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")),
                            childAtPosition(
                                    allOf(withClassName(is("android.widget.RelativeLayout")),
                                            childAtPosition(
                                                    withClassName(is("android.widget.TextInputTimePickerView")),
                                                    1)),
                                    0),
                            isDisplayed()));
            appCompatEditText.perform(replaceText("2"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")),
                            childAtPosition(
                                    allOf(withClassName(is("android.widget.RelativeLayout")),
                                            childAtPosition(
                                                    withClassName(is("android.widget.TextInputTimePickerView")),
                                                    1)),
                                    3),
                            isDisplayed()));
            appCompatEditText2.perform(replaceText("00"), closeSoftKeyboard());

            ViewInteraction materialButton4 = onView(
                    allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton4.perform(scrollTo(), click());

            ViewInteraction textInputEditText4 = onView(
                    allOf(withId(R.id.eventEndTimeEditText),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventEndTimeInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText4.perform(click());

            ViewInteraction appCompatImageButton2 = onView(
                    allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageButton")), withContentDescription("Switch to text input mode for the time input."),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.LinearLayout")),
                                            4),
                                    0),
                            isDisplayed()));
            appCompatImageButton2.perform(click());

            ViewInteraction appCompatEditText3 = onView(
                    allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")),
                            childAtPosition(
                                    allOf(withClassName(is("android.widget.RelativeLayout")),
                                            childAtPosition(
                                                    withClassName(is("android.widget.TextInputTimePickerView")),
                                                    1)),
                                    0),
                            isDisplayed()));
            appCompatEditText3.perform(replaceText("2"), closeSoftKeyboard());

            ViewInteraction appCompatEditText4 = onView(
                    allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")),
                            childAtPosition(
                                    allOf(withClassName(is("android.widget.RelativeLayout")),
                                            childAtPosition(
                                                    withClassName(is("android.widget.TextInputTimePickerView")),
                                                    1)),
                                    3),
                            isDisplayed()));
            appCompatEditText4.perform(replaceText("30"), closeSoftKeyboard());

            ViewInteraction materialButton5 = onView(
                    allOf(withId(android.R.id.button1), withText("OK"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            materialButton5.perform(scrollTo(), click());

            ViewInteraction textInputEditText5 = onView(
                    allOf(withId(R.id.eventDescriptionEditText),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventDescriptionInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText5.perform(replaceText("pls work"), closeSoftKeyboard());

            ViewInteraction textInputEditText6 = onView(
                    allOf(withId(R.id.attendanceLimitEditText),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.attendanceLimitInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText6.perform(replaceText("50"), closeSoftKeyboard());

            ViewInteraction textInputEditText7 = onView(
                    allOf(withId(R.id.attendanceLimitEditText), withText("50"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.attendanceLimitInputLayout),
                                            0),
                                    0),
                            isDisplayed()));
            textInputEditText7.perform(pressImeActionButton());

            ViewInteraction materialButton6 = onView(
                    allOf(withId(R.id.newQrButton), withText("New Qr"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.LinearLayout")),
                                            7),
                                    0),
                            isDisplayed()));
            materialButton6.perform(click());


            // Checks if activity is switched to generate QR code
            intended(hasComponent(GenerateQrCodeActivity.class.getName()));


            ViewInteraction materialButton9 = onView(
                    allOf(withId(R.id.generateQrCodeButton), withText("Generate Unique QR Code"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    2),
                            isDisplayed()));
            materialButton9.perform(click());

            ViewInteraction materialButton10 = onView(
                    allOf(withId(R.id.finishButton), withText("Home Page"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    5),
                            isDisplayed()));
            materialButton10.perform(click());


            // Checks if activity is switched back to organizer dashboard
            intended(hasComponent(OrganizerPageActivity.class.getName()));
        }

        public void testEventCreated(){
            ViewInteraction appCompatImageButton3 = onView(
                    allOf(withId(R.id.expandButton),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventListRecyclerView),
                                            0),
                                    3),
                            isDisplayed()));
            appCompatImageButton3.perform(click());

            ViewInteraction materialButton11 = onView(
                    allOf(withId(R.id.viewMapButton), withText("Check map"),
                            childAtPosition(
                                    allOf(withId(R.id.expandEventLayout),
                                            childAtPosition(
                                                    withClassName(is("android.widget.LinearLayout")),
                                                    2)),
                                    3),
                            isDisplayed()));
            materialButton11.perform(click());

            ViewInteraction materialButton12 = onView(
                    allOf(withId(R.id.goBackButton), withText("BACK"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    1),
                            isDisplayed()));
            materialButton12.perform(click());

            ViewInteraction appCompatImageButton4 = onView(
                    allOf(withId(R.id.expandButton),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.eventListRecyclerView),
                                            0),
                                    3),
                            isDisplayed()));
            appCompatImageButton4.perform(click());

            ViewInteraction materialTextView = onView(
                    allOf(withId(R.id.attendeeCountTextView), withText("Live Attendee Count: 0"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.LinearLayout")),
                                            1),
                                    1),
                            isDisplayed()));
            materialTextView.perform(click());

            pressBack();
        }

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
}
