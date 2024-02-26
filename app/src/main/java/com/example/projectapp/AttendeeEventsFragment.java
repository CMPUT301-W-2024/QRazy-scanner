package com.example.projectapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AttendeeEventsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_events, container, false);

        // Initialize RecyclerViews
        RecyclerView eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        RecyclerView notificationsRecyclerView = view.findViewById(R.id.notificationsRecyclerView);

        // Set up layout managers (you can choose LinearLayoutManager or GridLayoutManager)
        LinearLayoutManager eventsLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager notificationsLayoutManager = new LinearLayoutManager(requireContext());

        // Set layout managers to RecyclerViews
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        notificationsRecyclerView.setLayoutManager(notificationsLayoutManager);

        // TODO: Set up adapters for events and notifications RecyclerViews
        // You'll need to create custom adapters for your data.

        return view;
    }
}
