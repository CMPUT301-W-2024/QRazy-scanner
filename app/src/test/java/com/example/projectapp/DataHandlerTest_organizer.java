package com.example.projectapp;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.annotation.UiThreadTest;


import com.example.projectapp.Controller.AddOrganizerCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Organizer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
//@Config(sdk = {Build.VERSION_CODES.O}) // Add this line
public class DataHandlerTest_organizer {
    FirebaseFirestore mockFirestore;
    CollectionReference mockCollectionReference;
    DocumentReference mockDocumentReference;
    Task mockTask;
    //DocumentSnapshot snapshot = mock(DocumentSnapshot.class);
    DataHandler dataHandler = DataHandler.getInstance();
    Organizer mockOrganizer;


    @Before
    public void setUp() {
        mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionReference = mock(CollectionReference.class);
        mockDocumentReference = mock(DocumentReference.class);
        mockTask = mock(Task.class);

        when(mockFirestore.collection("organizers")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.set(any(Organizer.class))).thenReturn(mockTask);

        dataHandler = mock(DataHandler.class);
        dataHandler = new DataHandler(mockFirestore);

        // Initialize localOrganizer if required
        Organizer localOrganizer = new Organizer("mockOrganizerName");
        localOrganizer.setOrganizerId("Mock OrgID");
        // Assuming there's a method to set the local organizer in DataHandler
        dataHandler.setLocalOrganizer(localOrganizer);
    }


    @Test
    public void testAddOrganizer() {
        // Arrange
        Organizer expectedOrganizer = new Organizer("mockOrganizerName");
        expectedOrganizer.setOrganizerId("mock org ID");

        when(mockDocumentReference.getId()).thenReturn("mock org ID");
        when(mockDocumentReference.set(any())).thenReturn(mockTask);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);

        // Act
        dataHandler.addOrganizer(new AddOrganizerCallback() {
            @Override
            public void onAddOrganizer(Organizer organizer) {
                // Assert
                assertNotNull(organizer);
                assertEquals(expectedOrganizer.getOrganizerId(), organizer.getOrganizerId());
            }
        });

        // Verify that the document reference set method is called with the correct organizer
        verify(mockDocumentReference).set(expectedOrganizer);

    }

    /*
    @Test
    public void testAddOrganizer() {
        // Arrange
        ArgumentCaptor<Organizer> organizerArgumentCaptor = ArgumentCaptor.forClass(Organizer.class);

        // Act
        dataHandler.addOrganizer(new AddOrganizerCallback() {
            @Override
            public void onAddOrganizer(Organizer mockOrganizer) {
                // Assert
                assertNotNull(mockOrganizer);
                assertEquals("Mock OrgID", mockOrganizer.getOrganizerId());
            }
        });

        // Assert
        verify(mockDocumentReference).set(organizerArgumentCaptor.capture());
        Organizer capturedOrganizer = organizerArgumentCaptor.getValue();
        assertEquals(mockOrganizer, capturedOrganizer);
    }*/
}

