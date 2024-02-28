package com.example.projectapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ProfileFragment extends Fragment {

    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize EditText fields
        userNameEditText = view.findViewById(R.id.userNameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);

        // Load saved values from SharedPreferences

        // Save button click listener
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                loadSavedValues();
                fragmentManager.popBackStackImmediate(); // Navigate back to the previous fragment
            }
        });

        return view;
    }

    private void loadSavedValues() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPrefs", 0);
        userNameEditText.setText(preferences.getString("userName", ""));
        emailEditText.setText(preferences.getString("email", ""));
        phoneEditText.setText(preferences.getString("phone", ""));
    }

    private void saveValues() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPrefs", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userNameEditText.getText().toString());
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("phone", phoneEditText.getText().toString());
        editor.apply();
    }
}
