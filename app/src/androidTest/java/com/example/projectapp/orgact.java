package com.example.projectapp;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class orgact {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void orgact() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.createEventButton), withText("Create Event"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.organizerNameEditText), withText("Organizer Name"),
                        withParent(withParent(withId(R.id.organizerNameInputLayout))),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.organizerNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.organizerNameInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("test organizer page activities"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.createNewEventButton), withText("Create New Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.eventNameEditText), withText("Event Name"),
                        withParent(withParent(withId(R.id.eventNameInputLayout))),
                        isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.eventNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventNameInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.eventDateEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDateInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.eventDateEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDateInputLayout),
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

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());

        pressBack();

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.eventStartTimeEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventStartTimeInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(click());

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
        appCompatEditText.perform(replaceText("13"), closeSoftKeyboard());

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

        ViewInteraction appCompatSpinner = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatSpinner")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.TextInputTimePickerView")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                2),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.eventEndTimeEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventEndTimeInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(click());

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
        appCompatEditText3.perform(replaceText("16"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.RelativeLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.TextInputTimePickerView")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("00"), closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatSpinner")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.TextInputTimePickerView")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                2),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")), withText("12"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.RelativeLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.TextInputTimePickerView")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("16"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")), withText("16"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.RelativeLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.TextInputTimePickerView")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction materialButton8 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton9.perform(scrollTo(), click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton10.perform(scrollTo(), click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.eventEndTimeEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventEndTimeInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(click());

        ViewInteraction materialRadioButton = onView(
                allOf(withClassName(is("com.google.android.material.radiobutton.MaterialRadioButton")), withText("pm"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.RadioGroup")),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                1),
                        isDisplayed()));
        materialRadioButton.perform(click());

        ViewInteraction materialButton11 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton11.perform(scrollTo(), click());

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.eventDescriptionEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.eventDescriptionInputLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(replaceText("smth test"), closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.newQrButton), withText("New Qr"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.finishButton), withText("HOME PAGE"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.generateQrCodeButton), withText("Generate Unique QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.shareQrCodeButton), withText("Share QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton14.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("1 item"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("1 item")));

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.sharePromoQrCodeButton), withText("Share Promo QR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton15.perform(click());

        ViewInteraction materialButton16 = onView(
                allOf(withId(R.id.generatePromotionQrCodeButton), withText("Generate Promotion QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton16.perform(click());

        ViewInteraction materialButton17 = onView(
                allOf(withId(R.id.sharePromoQrCodeButton), withText("Share Promo QR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton17.perform(click());

        ViewInteraction materialButton18 = onView(
                allOf(withId(R.id.generatePromotionQrCodeButton), withText("Generate Promotion QR Code"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton18.perform(click());

        ViewInteraction materialButton19 = onView(
                allOf(withId(R.id.finishButton), withText("Home Page"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton19.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.organizerNameEditText), withText("test organizer page activities"),
                        withParent(withParent(withId(R.id.organizerNameInputLayout))),
                        isDisplayed()));
        editText3.check(matches(isDisplayed()));

        ViewInteraction materialButton20 = onView(
                allOf(withId(R.id.upcomingButton), withText("UPCOMING"),
                        childAtPosition(
                                allOf(withId(R.id.statusButtonsLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                2),
                        isDisplayed()));
        materialButton20.perform(click());

        ViewInteraction materialButton21 = onView(
                allOf(withId(R.id.completeButton), withText("COMPLETE"),
                        childAtPosition(
                                allOf(withId(R.id.statusButtonsLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        materialButton21.perform(click());
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
