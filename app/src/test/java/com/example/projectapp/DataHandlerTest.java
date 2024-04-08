package com.example.projectapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.projectapp.Controller.AddAttendeeCallback;
import com.example.projectapp.Controller.AddOrganizerCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.auth.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(MockitoJUnitRunner.class)
public class DataHandlerTest {
    //private static DataHandler mockInstance = Mockito.mock(DataHandler.class);
    FirebaseFirestore mockDB = Mockito.mock(FirebaseFirestore.class);
    Task mockTask = mock(Task.class);
    CollectionReference mockAttendeesColRef = Mockito.mock(CollectionReference.class);
    CollectionReference mockOrganizersColRef = Mockito.mock(CollectionReference.class);
    CollectionReference mockEventsColRef = Mockito.mock(CollectionReference.class);
    DocumentReference mockAttendeeDocRef = Mockito.mock(DocumentReference.class);
    DocumentReference mockOrganizerDocRef = Mockito.mock(DocumentReference.class);
    DocumentReference mockEventDocRef = Mockito.mock(DocumentReference.class);

    Attendee mockLocalAttendee = Mockito.mock(Attendee.class);
    Organizer mockLocalOrganizer = Mockito.mock(Organizer.class);
    Event mockEvent = Mockito.mock(Event.class);

    @Mock
    DataHandler mockDataHandler;


    @BeforeEach
    public void setUp() {
        //when(mockDB.collection(anyString())).thenReturn(mockCollectionRef);
        //when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

        //firestore = mock(FirebaseFirestore.class);

        //dataHandler.setLocalOrganizer(organizer);
        //dataHandler.setFirestore(firestore);

        //DataHandler dataHandler = new DataHandler.getInstance();
        //when(mockDB.collection(anyString())).thenReturn(mockCollectionRef);
        //when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

        when(mockAttendeeDocRef.getId()).thenReturn("mockAttendeeID");
        when(mockOrganizerDocRef.getId()).thenReturn("mockOrganizerID");
        when(mockEventDocRef.getId()).thenReturn("mockEventID");

        when(mockAttendeesColRef.add(any(Attendee.class))).thenReturn(mockTask);
        when(mockOrganizersColRef.add(any(Organizer.class))).thenReturn(mockTask);
        when(mockEventsColRef.add(any(Event.class))).thenReturn(mockTask);

        mockDataHandler = new DataHandler();
    }

    @Test
    public void testAddOrganizer() {
        Organizer mockOrganizer = mock(Organizer.class);
        //DocumentReference attendeeDocRef = attendeesRef.document(attendee.getAttendeeId());

        //when(mockOrganizersRef.getId()).thenReturn("mockOrganizerID");


        // Set the local attendee
        mockDataHandler.addOrganizer(new AddOrganizerCallback() {
            @Override
            public void onAddOrganizer(Organizer mockOrganizer) {
                    // man idfk
            }
        });

        mockDataHandler.setLocalOrganizer(mockOrganizer);

        // Verify that the local organizer is set correctly
        assertEquals("mockOrganizerID", mockDataHandler.getLocalOrganizer());
    }
    

    /*@Test
    public void testAddAttendee() {
        // Mock data
        Attendee localAttendee = new Attendee("defaultPic.png", "Local Attendee", "LocalAttendee@gmail.com", "+123456789");
        AddAttendeeCallback callback = mock(AddAttendeeCallback.class);

        // Mock Firestore behavior
        DocumentReference attendeeDocRef = mock(DocumentReference.class);
        when(attendeesRef.document(localAttendee.getAttendeeId())).thenReturn(attendeeDocRef);
        Task<Void> successTask = mock(Task.class);
        when(attendeeDocRef.set(localAttendee)).thenReturn(successTask);
        doAnswer(invocation -> {
            ((OnSuccessListener<Void>) invocation.getArgument(0)).onSuccess(null);
            return null;
        }).when(successTask).addOnSuccessListener(any());

        // Call the method
        dataHandler.addAttendee(localAttendee, true, callback);

        // Verify that the callback was called with the correct parameters
        verify(callback).onAddAttendee(localAttendee, true);
    }

    public void testLocalOrganizerGetterAndSetter() {

    }*/

}
