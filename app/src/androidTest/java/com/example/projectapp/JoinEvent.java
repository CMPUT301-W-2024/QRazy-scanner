package com.example.projectapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class JoinEvent {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void joinEvent() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.joinEventButton), withText("Join Event"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.menuButton), withContentDescription("menu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userNameEditText), withText("sightest"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("new username"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.userNameEditText), withText("new username"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.userNameEditText), withText("new username"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(withText("new username")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.avatarImage), withContentDescription("Avatar Image"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.avatarImageButton), withText("Change and Save Avatar"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.avatarImageButton), withText("Change and Save Avatar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.emailEditText), withText("urgh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("new email"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.emailEditText), withText("new email"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.emailEditText), withText("new email"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText2.check(matches(withText("new email")));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.emailEditText), withText("new email"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText3.check(matches(withText("new email")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.phoneEditText), withText("12345"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText4.check(matches(withText("12345")));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.phoneEditText), withText("12345"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText5.check(matches(withText("12345")));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.phoneEditText), withText("12345"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("67890"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.phoneEditText), withText("67890"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.saveButton), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(withId(androidx.appcompat.R.id.action_bar_root)))),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction relativeLayout2 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(withId(androidx.appcompat.R.id.action_bar_root)))),
                        isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.allEventsList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                5)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.dialogEventSignButton), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.attendeeEventsList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                3)));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.dialogEventCloseButton), withText("Close"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0),
                        isDisplayed()));
        materialButton5.perform(click());
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
