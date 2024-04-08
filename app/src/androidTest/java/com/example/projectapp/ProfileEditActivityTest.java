package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.View.AttendeePageActivity;
import com.example.projectapp.View.MainActivity;
import com.example.projectapp.View.OrganizerPageActivity;
import com.example.projectapp.View.ProfileEditActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileEditActivityTest {
    DataHandler dataHandler = DataHandler.getInstance();

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);

    @Before
    public void setUp() throws InterruptedException {
        Attendee attendee = new Attendee(null, "Test Attendee", "test@gmail.com", "1234567890");
        dataHandler.setLocalAttendee(attendee);
        dataHandler.addAttendee(attendee, false, (a,e) -> {});
        Thread.sleep(1000);
    }

    @After
    public void tearDown(){
        FirebaseFirestore.getInstance().collection("attendees").document(dataHandler.getLocalAttendee().getAttendeeId()).delete();
    }

    @Test
    public void testNameDisplayed() throws InterruptedException {
        ActivityScenario.launch(AttendeePageActivity.class);

        onView(withId(R.id.menuButton)).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.userNameEditText)).perform(click());
        onView(withId(R.id.userNameEditText)).perform(typeText("s")).perform(closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        Thread.sleep(1000);
        onView(withText("Test Attendees ")).check(matches(isDisplayed()));
    }
}
