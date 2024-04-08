package com.example.projectapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.projectapp.Controller.AddOrganizerCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Organizer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataHandlerTest {
    private DataHandler dataHandler;
    private CollectionReference mockOrganizersColRef;
    private DocumentReference mockOrganizerDocRef;
    private Task<Void> mockTask;

    @BeforeEach
    public void setUp() {
        FirebaseFirestore mockDB = mock(FirebaseFirestore.class);
        mockOrganizersColRef = mock(CollectionReference.class);
        mockOrganizerDocRef = mock(DocumentReference.class);
        mockTask = mock(Task.class);

        when(mockDB.collection("organizers")).thenReturn(mockOrganizersColRef);
        when(mockOrganizersColRef.document(any())).thenReturn(mockOrganizerDocRef);


        dataHandler = new DataHandler(mockDB);

        // Initialize localOrganizer
        dataHandler.setLocalOrganizer(new Organizer("mockOrganizerName"));

    }

    @Test
    public void testAddOrganizer() {
        Organizer mockOrganizer = mock(Organizer.class);
        when(mockOrganizerDocRef.set(any())).thenReturn(mockTask);
        //when(mockOrganizersColRef.add(any(Organizer.class))).thenReturn(mockTask);

        AddOrganizerCallback callback = new AddOrganizerCallback() {
            @Override
            public void onAddOrganizer(Organizer organizer) {
                dataHandler.setLocalOrganizer(organizer);
            }
        };

        dataHandler.addOrganizer(callback);

        // Verify that the local organizer is set correctly
        assertEquals(mockOrganizer, dataHandler.getLocalOrganizer());
    }
}
