package com.example.projectapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.projectapp.Controller.AddOrganizerCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.Model.Organizer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class DataHandlerTestt {
    FirebaseFirestore mockDB = Mockito.mock(FirebaseFirestore.class);

    CollectionReference mockAttendeesColRef = Mockito.mock(CollectionReference.class);
    CollectionReference mockOrganizersColRef = Mockito.mock(CollectionReference.class);
    CollectionReference mockEventsColRef = Mockito.mock(CollectionReference.class);

    DocumentReference mockAttendeeDocRef = Mockito.mock(DocumentReference.class);
    DocumentReference mockOrganizerDocRef = Mockito.mock(DocumentReference.class);
    DocumentReference mockEventDocRef = Mockito.mock(DocumentReference.class);

    Task<DocumentSnapshot> mockTask = mock(Task.class);
    DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

    Attendee mockLocalAttendee = Mockito.mock(Attendee.class);
    Organizer mockLocalOrganizer = Mockito.mock(Organizer.class);
    Event mockEvent = Mockito.mock(Event.class);

    DataHandler dataHandler;


    @BeforeEach
    public void setUp() {
        when(mockDB.collection("attendees")).thenReturn(mockAttendeesColRef);
        when(mockDB.collection("organizers")).thenReturn(mockOrganizersColRef);
        when(mockDB.collection("events")).thenReturn(mockEventsColRef);
        //when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

        //firestore = mock(FirebaseFirestore.class);

        //dataHandler.setLocalOrganizer(organizer);
        //dataHandler.setFirestore(firestore);

        //DataHandler dataHandler = new DataHandler.getInstance();
        //when(mockDB.collection(anyString())).thenReturn(mockCollectionRef);
        //when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);

        when(mockAttendeeDocRef.getId()).thenReturn("mockAttendeeID");

        when(mockEventDocRef.getId()).thenReturn("mockEventID");

        //when(mockAttendeesColRef.add(any(Attendee.class))).thenReturn(mockTask);
        //
        //when(mockEventsColRef.add(any(Event.class))).thenReturn(mockTask);

        DataHandler mockDataHandler = new DataHandler();
    }

    @Test
    public void testAddOrganizer() {

        Organizer mockOrganizer = mock(Organizer.class);
        //when(mockOrganizerDocRef.document.getId()).thenReturn("mockOrganizerID");

        Task mockTask = mock(Task.class);
        when(mockOrganizersColRef.add(any(Organizer.class))).thenReturn(mockTask);

        dataHandler.addOrganizer(new AddOrganizerCallback() {
            @Override
            public void onAddOrganizer(Organizer organizer) {
                dataHandler.setLocalOrganizer(organizer);
            }
        });

        // Verify that the local organizer is set correctly
        assertEquals("mockOrganizerID", dataHandler.getLocalOrganizer());
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
