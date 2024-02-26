package com.example.projectapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.welcome_fragment, container, false);

        // Find the "Join Event" button
        Button joinEventButton = rootView.findViewById(R.id.joinEventButton);
        joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and navigate to the ScanFragment
                ScanFragment scanFragment = new ScanFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, scanFragment) // Use android.R.id.content as the container
                        .addToBackStack(null) // Optional: Add to back stack for navigation
                        .commit();
            }
        });

        return rootView;
    }
}
