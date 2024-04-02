package com.example.projectapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class test2CreateEventTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void test2CreateEventTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.createEventButton), withText("Create Event"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.headerTextView), withText("Welcome"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        /*
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.organizerNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.organizerNameInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("test 2"), closeSoftKeyboard());*/

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.createNewEventButton), withText("Create New Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.eventNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventNameInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("test 2"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.eventDateEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDateInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        pressBack();

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.eventStartTimeEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventStartTimeInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.eventEndTimeEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventEndTimeInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(click());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.eventEndTimeEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventEndTimeInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.eventDescriptionEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDescriptionInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("test "), closeSoftKeyboard());

        pressBack();

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.eventDescriptionEditText), withText("test "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDescriptionInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(replaceText("test 2"));

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.eventDescriptionEditText), withText("test 2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDescriptionInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText9.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.attendanceLimitEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.attendanceLimitInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText10.perform(replaceText("50"), closeSoftKeyboard());

        ViewInteraction textInputEditText11 = onView(
                allOf(withId(R.id.attendanceLimitEditText), withText("50"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.attendanceLimitInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText11.perform(pressImeActionButton());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.newQrButton), withText("New Qr"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.generateQrCodeButton), withText("Generate Unique QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.qrCodeImageView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.generatePromotionQrCodeButton), withText("Generate Promotion QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.shareQrCodeButton), withText("Share QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton10.perform(click());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.finishButton), withText("Home Page"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.eventNameOrgText), withText("test 2"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText("test 2")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.expandButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventListRecyclerView),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.eventQrView),
                        withParent(allOf(withId(R.id.expandEventLayout),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.attendeeCountTextView), withText("Live Attendee Count: 0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withText("Checked In Attendees"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView3.check(matches(withText("Checked In Attendees")));

        pressBack();

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.viewMapButton), withText("Check map"),
                        childAtPosition(
                                allOf(withId(R.id.expandEventLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                2)),
                                3),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.mapview),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction viewGroup2 = onView(
                allOf(withId(R.id.mapview),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        viewGroup2.check(matches(isDisplayed()));

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.goBackButton), withText("BACK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton13.perform(click());

        pressBack();
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
