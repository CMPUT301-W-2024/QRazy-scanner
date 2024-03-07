package com.example.projectapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ScanFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        // Find UI components
        TextView qrCodeTextView = rootView.findViewById(R.id.qrCodeTextView);
        Button userButton = rootView.findViewById(R.id.userButton);
        Button scanSuccessButton = rootView.findViewById(R.id.scanSuccessButton);
        Button cancelButton = rootView.findViewById(R.id.scan_fragment_cancel_button);

        // Set click listener for user button (directs to user profile fragment)
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and navigate to the ScanFragment
                ProfileFragment profileFragment = new ProfileFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, profileFragment)
                        // Use android.R.id.content as the container
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Set click listener for scan success button (directs to event info fragment)
        scanSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Create and navigate to the ScanFragment
                EventInfoFragment eventInfoFragment = new EventInfoFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, eventInfoFragment) // Use android.R.id.content as the container
                        .addToBackStack(null) // Optional: Add to back stack for navigation
                        .commit();
            }
        });

        cancelButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate(); // Navigate back to the previous fragment
            }
        });

        // You can also set up your camera preview here (using SurfaceView or TextureView)

        return rootView;
    }
}

