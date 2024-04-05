package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AttendeePageDialogsTest {
    @Before
    public void setUp() {
        ActivityScenarioRule<OrganizerPageActivity> ActivityScenarioRule =
                new ActivityScenarioRule<>(OrganizerPageActivity.class);

        onView(withId(R.id.organizerNameEditText)).perform(typeText("Page activities test"), closeSoftKeyboard());
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.eventNameEditText)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.eventDateEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 30));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.eventStartTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.eventEndTimeEditText)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(16, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.eventDescriptionEditText)).perform(typeText("This is a test description"), closeSoftKeyboard());
        onView(withId(R.id.attendanceLimitEditText)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.newQrButton)).perform(click());
        onView(withId(R.id.generateQrCodeButton)).perform(click());
        onView(withId(R.id.finishButton)).perform(click());
    }

    @Rule
    public ActivityScenarioRule<AttendeePageActivity> ActivityScenarioRule =
            new ActivityScenarioRule<>(AttendeePageActivity.class);

    @Test
    public void testSignUpDialog() {

    }
}
