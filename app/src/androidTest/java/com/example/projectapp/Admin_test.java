package com.example.projectapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.AdditionalMatchers.not;


import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RunWith(Enclosed.class)
public class adminTest {
    @RunWith(AndroidJUnit4.class)
    @LargeTest
    public static class AdminActivityTest {

        @Rule
        public ActivityScenarioRule<Admin> activityRule =
                new ActivityScenarioRule<>(Admin.class);

        @Test
        public void textViewDisplaysCorrectText() {
            onView(withId(R.id.EventsView)).check(matches(withText(R.string.events)));
            onView(withId(R.id.ProfileView)).check(matches(withText(R.string.profiles)));
            onView(withId(R.id.textView2)).check(matches(withText(R.string.Images)));
        }


        @Test
        public void verticalScrollViewIsScrollable() {
            onView(withId(R.id.adminAttendeesLayout)).perform(swipeUp());
            onView(withId(R.id.adminAttendeesLayout)).perform(swipeDown());
        }

        @Test
        public void horizontalScrollViewIsScrollable() {
            onView(withId(R.id.adminEventsLayout)).perform(swipeLeft());
            onView(withId(R.id.adminEventsLayout)).perform(swipeRight());
        }

        @Test
        public void clickOnRecyclerViewItemWidget() {
            onView(withId(R.id.adminEventsLayout)).perform(click());
        }
        @Test
        public void testDeleteButtonRemovesItemFromFirebase() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> testData = new HashMap<>();
            testData.put("name", "Sample Name");
            testData.put("date", "2024-01-01");
            testData.put("startTime", "00:00");
            testData.put("endTime", "01:00");
            testData.put("organizerName", "Test Organizer");
            testData.put("organizerId", "123456789");
            testData.put("attendanceLimit", 50);
            testData.put("description", "Sample description");
            testData.put("poster", null);
           

            db.collection("events").add(testData)
                    .addOnSuccessListener(documentReference -> {
                        
                        onView(withId(R.id.adminEventsLayout)).perform(click());
                        onView(withId(R.id.dialogEventDeleteButton)).perform(click());

                       
                        db.collection("events").document(documentReference.getId()).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    assertFalse(documentSnapshot.exists()); 
                                })
                                .addOnFailureListener(e -> {
                         
                                });
                    })
                    .addOnFailureListener(e -> {
            
                    });
        }

    }



}
